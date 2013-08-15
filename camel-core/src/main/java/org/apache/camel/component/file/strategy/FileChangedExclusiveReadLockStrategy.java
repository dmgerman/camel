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
name|LoggingLevel
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
name|CamelLogger
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
name|StopWatch
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Acquires exclusive read lock to the given file by checking whether the file is being  * changed by scanning the file at different intervals (to detect changes).  */
end_comment

begin_class
DECL|class|FileChangedExclusiveReadLockStrategy
specifier|public
class|class
name|FileChangedExclusiveReadLockStrategy
extends|extends
name|MarkerFileExclusiveReadLockStrategy
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|FileChangedExclusiveReadLockStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|timeout
specifier|private
name|long
name|timeout
decl_stmt|;
DECL|field|checkInterval
specifier|private
name|long
name|checkInterval
init|=
literal|1000
decl_stmt|;
DECL|field|minLength
specifier|private
name|long
name|minLength
init|=
literal|1
decl_stmt|;
DECL|field|readLockLoggingLevel
specifier|private
name|LoggingLevel
name|readLockLoggingLevel
init|=
name|LoggingLevel
operator|.
name|WARN
decl_stmt|;
annotation|@
name|Override
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
comment|// must call super
if|if
condition|(
operator|!
name|super
operator|.
name|acquireExclusiveReadLock
argument_list|(
name|operations
argument_list|,
name|file
argument_list|,
name|exchange
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|File
name|target
init|=
operator|new
name|File
argument_list|(
name|file
operator|.
name|getAbsoluteFilePath
argument_list|()
argument_list|)
decl_stmt|;
name|boolean
name|exclusive
init|=
literal|false
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Waiting for exclusive read lock to file: {}"
argument_list|,
name|file
argument_list|)
expr_stmt|;
name|long
name|lastModified
init|=
name|Long
operator|.
name|MIN_VALUE
decl_stmt|;
name|long
name|length
init|=
name|Long
operator|.
name|MIN_VALUE
decl_stmt|;
name|StopWatch
name|watch
init|=
operator|new
name|StopWatch
argument_list|()
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
name|watch
operator|.
name|taken
argument_list|()
decl_stmt|;
if|if
condition|(
name|delta
operator|>
name|timeout
condition|)
block|{
name|CamelLogger
operator|.
name|log
argument_list|(
name|LOG
argument_list|,
name|readLockLoggingLevel
argument_list|,
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
name|long
name|newLastModified
init|=
name|target
operator|.
name|lastModified
argument_list|()
decl_stmt|;
name|long
name|newLength
init|=
name|target
operator|.
name|length
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Previous last modified: {}, new last modified: {}"
argument_list|,
name|lastModified
argument_list|,
name|newLastModified
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Previous length: {}, new length: {}"
argument_list|,
name|length
argument_list|,
name|newLength
argument_list|)
expr_stmt|;
if|if
condition|(
name|length
operator|>=
name|minLength
operator|&&
operator|(
name|newLastModified
operator|==
name|lastModified
operator|&&
name|newLength
operator|==
name|length
operator|)
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Read lock acquired."
argument_list|)
expr_stmt|;
name|exclusive
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
comment|// set new base file change information
name|lastModified
operator|=
name|newLastModified
expr_stmt|;
name|length
operator|=
name|newLength
expr_stmt|;
name|boolean
name|interrupted
init|=
name|sleep
argument_list|()
decl_stmt|;
if|if
condition|(
name|interrupted
condition|)
block|{
comment|// we were interrupted while sleeping, we are likely being shutdown so return false
return|return
literal|false
return|;
block|}
block|}
block|}
return|return
name|exclusive
return|;
block|}
DECL|method|sleep ()
specifier|private
name|boolean
name|sleep
parameter_list|()
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Exclusive read lock not granted. Sleeping for {} millis."
argument_list|,
name|checkInterval
argument_list|)
expr_stmt|;
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|checkInterval
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Sleep interrupted while waiting for exclusive read lock, so breaking out"
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
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
annotation|@
name|Override
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
DECL|method|getCheckInterval ()
specifier|public
name|long
name|getCheckInterval
parameter_list|()
block|{
return|return
name|checkInterval
return|;
block|}
annotation|@
name|Override
DECL|method|setCheckInterval (long checkInterval)
specifier|public
name|void
name|setCheckInterval
parameter_list|(
name|long
name|checkInterval
parameter_list|)
block|{
name|this
operator|.
name|checkInterval
operator|=
name|checkInterval
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setReadLockLoggingLevel (LoggingLevel readLockLoggingLevel)
specifier|public
name|void
name|setReadLockLoggingLevel
parameter_list|(
name|LoggingLevel
name|readLockLoggingLevel
parameter_list|)
block|{
name|this
operator|.
name|readLockLoggingLevel
operator|=
name|readLockLoggingLevel
expr_stmt|;
block|}
DECL|method|getMinLength ()
specifier|public
name|long
name|getMinLength
parameter_list|()
block|{
return|return
name|minLength
return|;
block|}
DECL|method|setMinLength (long minLength)
specifier|public
name|void
name|setMinLength
parameter_list|(
name|long
name|minLength
parameter_list|)
block|{
name|this
operator|.
name|minLength
operator|=
name|minLength
expr_stmt|;
block|}
block|}
end_class

end_unit

