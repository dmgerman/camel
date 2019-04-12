begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.remote.strategy
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
name|remote
operator|.
name|strategy
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jcraft
operator|.
name|jsch
operator|.
name|ChannelSftp
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
name|GenericFileEndpoint
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
name|component
operator|.
name|file
operator|.
name|remote
operator|.
name|SftpRemoteFile
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
name|spi
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

begin_class
DECL|class|SftpChangedExclusiveReadLockStrategy
specifier|public
class|class
name|SftpChangedExclusiveReadLockStrategy
implements|implements
name|GenericFileExclusiveReadLockStrategy
argument_list|<
name|ChannelSftp
operator|.
name|LsEntry
argument_list|>
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
name|SftpChangedExclusiveReadLockStrategy
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
literal|5000
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
DECL|field|minLength
specifier|private
name|long
name|minLength
init|=
literal|1
decl_stmt|;
DECL|field|minAge
specifier|private
name|long
name|minAge
decl_stmt|;
DECL|field|fastExistsCheck
specifier|private
name|boolean
name|fastExistsCheck
decl_stmt|;
annotation|@
name|Override
DECL|method|prepareOnStartup (GenericFileOperations<ChannelSftp.LsEntry> tGenericFileOperations, GenericFileEndpoint<ChannelSftp.LsEntry> tGenericFileEndpoint)
specifier|public
name|void
name|prepareOnStartup
parameter_list|(
name|GenericFileOperations
argument_list|<
name|ChannelSftp
operator|.
name|LsEntry
argument_list|>
name|tGenericFileOperations
parameter_list|,
name|GenericFileEndpoint
argument_list|<
name|ChannelSftp
operator|.
name|LsEntry
argument_list|>
name|tGenericFileEndpoint
parameter_list|)
throws|throws
name|Exception
block|{
comment|// noop
block|}
DECL|method|acquireExclusiveReadLock (GenericFileOperations<ChannelSftp.LsEntry> operations, GenericFile<ChannelSftp.LsEntry> file, Exchange exchange)
specifier|public
name|boolean
name|acquireExclusiveReadLock
parameter_list|(
name|GenericFileOperations
argument_list|<
name|ChannelSftp
operator|.
name|LsEntry
argument_list|>
name|operations
parameter_list|,
name|GenericFile
argument_list|<
name|ChannelSftp
operator|.
name|LsEntry
argument_list|>
name|file
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
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
name|long
name|startTime
init|=
name|System
operator|.
name|currentTimeMillis
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
literal|0
decl_stmt|;
name|long
name|newLength
init|=
literal|0
decl_stmt|;
name|List
name|files
decl_stmt|;
comment|// operations.listFiles returns List<SftpRemoteFile> so do not use generic in the List files
if|if
condition|(
name|fastExistsCheck
condition|)
block|{
comment|// use the absolute file path to only pickup the file we want to check, this avoids expensive
comment|// list operations if we have a lot of files in the directory
name|String
name|path
init|=
name|file
operator|.
name|getAbsoluteFilePath
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|equals
argument_list|(
literal|"/"
argument_list|)
operator|||
name|path
operator|.
name|equals
argument_list|(
literal|"\\"
argument_list|)
condition|)
block|{
comment|// special for root (= home) directory
name|LOG
operator|.
name|trace
argument_list|(
literal|"Using fast exists to update file information in home directory"
argument_list|)
expr_stmt|;
name|files
operator|=
name|operations
operator|.
name|listFiles
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Using fast exists to update file information for {}"
argument_list|,
name|path
argument_list|)
expr_stmt|;
name|files
operator|=
name|operations
operator|.
name|listFiles
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|String
name|path
init|=
name|file
operator|.
name|getParent
argument_list|()
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|equals
argument_list|(
literal|"/"
argument_list|)
operator|||
name|path
operator|.
name|equals
argument_list|(
literal|"\\"
argument_list|)
condition|)
block|{
comment|// special for root (= home) directory
name|LOG
operator|.
name|trace
argument_list|(
literal|"Using full directory listing in home directory to update file information. Consider enabling fastExistsCheck option."
argument_list|)
expr_stmt|;
name|files
operator|=
name|operations
operator|.
name|listFiles
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Using full directory listing to update file information for {}. Consider enabling fastExistsCheck option."
argument_list|,
name|path
argument_list|)
expr_stmt|;
name|files
operator|=
name|operations
operator|.
name|listFiles
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"List files {} found {} files"
argument_list|,
name|file
operator|.
name|getAbsoluteFilePath
argument_list|()
argument_list|,
name|files
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Object
name|f
range|:
name|files
control|)
block|{
name|SftpRemoteFile
name|rf
init|=
operator|(
name|SftpRemoteFile
operator|)
name|f
decl_stmt|;
name|boolean
name|match
decl_stmt|;
if|if
condition|(
name|fastExistsCheck
condition|)
block|{
comment|// uses the absolute file path as well
name|match
operator|=
name|rf
operator|.
name|getFilename
argument_list|()
operator|.
name|equals
argument_list|(
name|file
operator|.
name|getAbsoluteFilePath
argument_list|()
argument_list|)
operator|||
name|rf
operator|.
name|getFilename
argument_list|()
operator|.
name|equals
argument_list|(
name|file
operator|.
name|getFileNameOnly
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|match
operator|=
name|rf
operator|.
name|getFilename
argument_list|()
operator|.
name|equals
argument_list|(
name|file
operator|.
name|getFileNameOnly
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|match
condition|)
block|{
name|newLastModified
operator|=
name|rf
operator|.
name|getLastModified
argument_list|()
expr_stmt|;
name|newLength
operator|=
name|rf
operator|.
name|getFileLength
argument_list|()
expr_stmt|;
block|}
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Previous last modified: "
operator|+
name|lastModified
operator|+
literal|", new last modified: "
operator|+
name|newLastModified
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Previous length: "
operator|+
name|length
operator|+
literal|", new length: "
operator|+
name|newLength
argument_list|)
expr_stmt|;
name|long
name|newOlderThan
init|=
name|startTime
operator|+
name|watch
operator|.
name|taken
argument_list|()
operator|-
name|minAge
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"New older than threshold: {}"
argument_list|,
name|newOlderThan
argument_list|)
expr_stmt|;
if|if
condition|(
name|newLength
operator|>=
name|minLength
operator|&&
operator|(
operator|(
name|minAge
operator|==
literal|0
operator|&&
name|newLastModified
operator|==
name|lastModified
operator|&&
name|newLength
operator|==
name|length
operator|)
operator|||
operator|(
name|minAge
operator|!=
literal|0
operator|&&
name|newLastModified
operator|<
name|newOlderThan
operator|)
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
annotation|@
name|Override
DECL|method|releaseExclusiveReadLockOnAbort (GenericFileOperations<ChannelSftp.LsEntry> operations, GenericFile<ChannelSftp.LsEntry> file, Exchange exchange)
specifier|public
name|void
name|releaseExclusiveReadLockOnAbort
parameter_list|(
name|GenericFileOperations
argument_list|<
name|ChannelSftp
operator|.
name|LsEntry
argument_list|>
name|operations
parameter_list|,
name|GenericFile
argument_list|<
name|ChannelSftp
operator|.
name|LsEntry
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
annotation|@
name|Override
DECL|method|releaseExclusiveReadLockOnRollback (GenericFileOperations<ChannelSftp.LsEntry> operations, GenericFile<ChannelSftp.LsEntry> file, Exchange exchange)
specifier|public
name|void
name|releaseExclusiveReadLockOnRollback
parameter_list|(
name|GenericFileOperations
argument_list|<
name|ChannelSftp
operator|.
name|LsEntry
argument_list|>
name|operations
parameter_list|,
name|GenericFile
argument_list|<
name|ChannelSftp
operator|.
name|LsEntry
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
annotation|@
name|Override
DECL|method|releaseExclusiveReadLockOnCommit (GenericFileOperations<ChannelSftp.LsEntry> operations, GenericFile<ChannelSftp.LsEntry> file, Exchange exchange)
specifier|public
name|void
name|releaseExclusiveReadLockOnCommit
parameter_list|(
name|GenericFileOperations
argument_list|<
name|ChannelSftp
operator|.
name|LsEntry
argument_list|>
name|operations
parameter_list|,
name|GenericFile
argument_list|<
name|ChannelSftp
operator|.
name|LsEntry
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
DECL|method|getMinAge ()
specifier|public
name|long
name|getMinAge
parameter_list|()
block|{
return|return
name|minAge
return|;
block|}
DECL|method|setMinAge (long minAge)
specifier|public
name|void
name|setMinAge
parameter_list|(
name|long
name|minAge
parameter_list|)
block|{
name|this
operator|.
name|minAge
operator|=
name|minAge
expr_stmt|;
block|}
DECL|method|isFastExistsCheck ()
specifier|public
name|boolean
name|isFastExistsCheck
parameter_list|()
block|{
return|return
name|fastExistsCheck
return|;
block|}
DECL|method|setFastExistsCheck (boolean fastExistsCheck)
specifier|public
name|void
name|setFastExistsCheck
parameter_list|(
name|boolean
name|fastExistsCheck
parameter_list|)
block|{
name|this
operator|.
name|fastExistsCheck
operator|=
name|fastExistsCheck
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setMarkerFiler (boolean markerFiler)
specifier|public
name|void
name|setMarkerFiler
parameter_list|(
name|boolean
name|markerFiler
parameter_list|)
block|{
comment|// noop - not supported by ftp
block|}
annotation|@
name|Override
DECL|method|setDeleteOrphanLockFiles (boolean deleteOrphanLockFiles)
specifier|public
name|void
name|setDeleteOrphanLockFiles
parameter_list|(
name|boolean
name|deleteOrphanLockFiles
parameter_list|)
block|{
comment|// noop - not supported by ftp
block|}
block|}
end_class

end_unit

