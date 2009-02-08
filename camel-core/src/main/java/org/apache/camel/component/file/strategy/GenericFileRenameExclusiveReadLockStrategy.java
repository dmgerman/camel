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
comment|/**  * Acquires exclusive read lock to the given file. Will wait until the lock is granted.  * After granting the read lock it is realeased, we just want to make sure that when we start  * consuming the file its not currently in progress of being written by third party.  */
end_comment

begin_class
DECL|class|GenericFileRenameExclusiveReadLockStrategy
specifier|public
class|class
name|GenericFileRenameExclusiveReadLockStrategy
parameter_list|<
name|T
parameter_list|>
implements|implements
name|GenericFileExclusiveReadLockStrategy
argument_list|<
name|T
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
name|GenericFileRenameExclusiveReadLockStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|timeout
specifier|private
name|long
name|timeout
decl_stmt|;
DECL|method|acquireExclusiveReadLock (GenericFileOperations<T> operations, GenericFile<T> file, Exchange exchange)
specifier|public
name|boolean
name|acquireExclusiveReadLock
parameter_list|(
name|GenericFileOperations
argument_list|<
name|T
argument_list|>
name|operations
parameter_list|,
name|GenericFile
argument_list|<
name|T
argument_list|>
name|file
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
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
literal|"Waiting for exclusive read lock to file: "
operator|+
name|file
argument_list|)
expr_stmt|;
block|}
comment|// the trick is to try to rename the file, if we can rename then we have exclusive read
comment|// since its a Generic file we cannot use java.nio to get a RW lock
name|String
name|newName
init|=
name|file
operator|.
name|getFileName
argument_list|()
operator|+
literal|".camelExclusiveReadLock"
decl_stmt|;
comment|// clone and change the name
name|GenericFile
argument_list|<
name|T
argument_list|>
name|newFile
init|=
name|file
operator|.
name|clone
argument_list|()
decl_stmt|;
name|newFile
operator|.
name|changeFileName
argument_list|(
name|newName
argument_list|)
expr_stmt|;
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
literal|"Cannot acquire read lock within "
operator|+
name|timeout
operator|+
literal|" millis. Will skip the file: "
operator|+
name|file
argument_list|)
expr_stmt|;
comment|// we could not get the lock within the timeout period, so return false
return|return
literal|false
return|;
block|}
block|}
name|exclusive
operator|=
name|operations
operator|.
name|renameFile
argument_list|(
name|file
operator|.
name|getAbsoluteFileName
argument_list|()
argument_list|,
name|newFile
operator|.
name|getAbsoluteFileName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|exclusive
condition|)
block|{
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
literal|"Acquired exclusive read lock to file: "
operator|+
name|file
argument_list|)
expr_stmt|;
block|}
comment|// rename it back so we can read it
name|operations
operator|.
name|renameFile
argument_list|(
name|newFile
operator|.
name|getAbsoluteFileName
argument_list|()
argument_list|,
name|file
operator|.
name|getAbsoluteFileName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sleep
argument_list|()
expr_stmt|;
block|}
block|}
return|return
literal|true
return|;
block|}
DECL|method|releaseExclusiveReadLock (GenericFileOperations<T> opeations, GenericFile<T> file, Exchange exchange)
specifier|public
name|void
name|releaseExclusiveReadLock
parameter_list|(
name|GenericFileOperations
argument_list|<
name|T
argument_list|>
name|opeations
parameter_list|,
name|GenericFile
argument_list|<
name|T
argument_list|>
name|file
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// noop
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
comment|/**      * Sets an optional timeout period.      *<p/>      * If the readlock could not be granted within the timeperiod then the wait is stopped and the      *<tt>acquireExclusiveReadLock</tt> returns<tt>false</tt>.      *      * @param timeout period in millis      */
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

