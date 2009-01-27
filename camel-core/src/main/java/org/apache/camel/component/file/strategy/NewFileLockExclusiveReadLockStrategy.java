begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.strategy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
operator|.
name|strategy
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|RandomAccessFile
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|channels
operator|.
name|Channel
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|channels
operator|.
name|FileChannel
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|channels
operator|.
name|FileLock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
operator|.
name|GenericFile
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
operator|.
name|GenericFileExclusiveReadLockStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
operator|.
name|GenericFileOperations
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ExchangeHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * Acquires exclusive read lock to the given file. Will wait until the lock is granted.  * After granting the read lock it is released, we just want to make sure that when we start  * consuming the file its not currently in progress of being written by third party.  */
end_comment

begin_class
DECL|class|NewFileLockExclusiveReadLockStrategy
specifier|public
class|class
name|NewFileLockExclusiveReadLockStrategy
implements|implements
name|GenericFileExclusiveReadLockStrategy
argument_list|<
name|File
argument_list|>
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|NewFileLockExclusiveReadLockStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|timeout
specifier|private
name|long
name|timeout
decl_stmt|;
DECL|method|acquireExclusiveReadLock (GenericFileOperations<File> operations, GenericFile<File> file, Exchange exchange)
specifier|public
name|boolean
name|acquireExclusiveReadLock
parameter_list|(
name|GenericFileOperations
argument_list|<
name|File
argument_list|>
name|operations
parameter_list|,
name|GenericFile
argument_list|<
name|File
argument_list|>
name|file
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|File
name|target
init|=
operator|new
name|File
argument_list|(
name|file
operator|.
name|getAbsoluteFileName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Waiting for exclusive read lock to file: "
operator|+
name|target
argument_list|)
expr_stmt|;
block|}
try|try
block|{
comment|// try to acquire rw lock on the file before we can consume it
name|FileChannel
name|channel
init|=
operator|new
name|RandomAccessFile
argument_list|(
name|target
argument_list|,
literal|"rw"
argument_list|)
operator|.
name|getChannel
argument_list|()
decl_stmt|;
name|long
name|start
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|boolean
name|exclusive
init|=
literal|false
decl_stmt|;
while|while
condition|(
operator|!
name|exclusive
condition|)
block|{
comment|// timeout check
if|if
condition|(
name|timeout
operator|>
literal|0
condition|)
block|{
name|long
name|delta
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|start
decl_stmt|;
if|if
condition|(
name|delta
operator|>
name|timeout
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Could not acquire read lock within "
operator|+
name|timeout
operator|+
literal|" millis. Will skip the file: "
operator|+
name|target
argument_list|)
expr_stmt|;
comment|// we could not get the lock within the timeout period, so return false
return|return
literal|false
return|;
block|}
block|}
comment|// get the lock using either try lock or not depending on if we are using timeout or not
name|FileLock
name|lock
init|=
literal|null
decl_stmt|;
try|try
block|{
name|lock
operator|=
name|timeout
operator|>
literal|0
condition|?
name|channel
operator|.
name|tryLock
argument_list|()
else|:
name|channel
operator|.
name|lock
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalStateException
name|ex
parameter_list|)
block|{
comment|// Also catch the OverlappingFileLockException here. Do nothing here
block|}
if|if
condition|(
name|lock
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Acquired exclusive read lock: "
operator|+
name|lock
operator|+
literal|" to file: "
operator|+
name|target
argument_list|)
expr_stmt|;
block|}
comment|// store lock so we can release it later
name|exchange
operator|.
name|setProperty
argument_list|(
literal|"org.apache.camel.file.lock"
argument_list|,
name|lock
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
literal|"org.apache.camel.file.lock.fileName"
argument_list|,
name|target
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|exclusive
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|sleep
argument_list|()
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// must handle IOException as some apps on Windows etc. will still somehow hold a lock to a file
comment|// such as AntiVirus or MS Office that has special locks for it's supported files
if|if
condition|(
name|timeout
operator|==
literal|0
condition|)
block|{
comment|// if not using timeout, then we cant retry, so rethrow
throw|throw
name|e
throw|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Cannot acquire read lock. Will try again."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|sleep
argument_list|()
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
DECL|method|releaseExclusiveReadLock (GenericFileOperations<File> fileGenericFileOperations, GenericFile<File> fileGenericFile, Exchange exchange)
specifier|public
name|void
name|releaseExclusiveReadLock
parameter_list|(
name|GenericFileOperations
argument_list|<
name|File
argument_list|>
name|fileGenericFileOperations
parameter_list|,
name|GenericFile
argument_list|<
name|File
argument_list|>
name|fileGenericFile
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|FileLock
name|lock
init|=
name|ExchangeHelper
operator|.
name|getMandatoryProperty
argument_list|(
name|exchange
argument_list|,
literal|"org.apache.camel.file.lock"
argument_list|,
name|FileLock
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|lockFileName
init|=
name|ExchangeHelper
operator|.
name|getMandatoryProperty
argument_list|(
name|exchange
argument_list|,
literal|"org.apache.camel.file.lock.filename"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Channel
name|channel
init|=
name|lock
operator|.
name|channel
argument_list|()
decl_stmt|;
try|try
block|{
name|lock
operator|.
name|release
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
comment|// must close channel
name|ObjectHelper
operator|.
name|close
argument_list|(
name|channel
argument_list|,
literal|"while acquiring exclusive read lock for file: "
operator|+
name|lockFileName
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|sleep ()
specifier|private
name|void
name|sleep
parameter_list|()
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Exclusive read lock not granted. Sleeping for 1000 millis."
argument_list|)
expr_stmt|;
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
DECL|method|getTimeout ()
specifier|public
name|long
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
comment|/**      * Sets an optional timeout period.      *<p/>      * If the readlock could not be granted within the timeperiod then the wait is stopped and the      * acquireReadLock returns<tt>false</tt>.      *      * @param timeout period in millis      */
DECL|method|setTimeout (long timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
block|}
end_class

end_unit

