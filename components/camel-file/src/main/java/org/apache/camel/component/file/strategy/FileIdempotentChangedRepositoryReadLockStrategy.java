begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|util
operator|.
name|concurrent
operator|.
name|ScheduledExecutorService
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|CamelContext
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
name|CamelContextAware
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
name|spi
operator|.
name|IdempotentRepository
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
name|support
operator|.
name|service
operator|.
name|ServiceSupport
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

begin_comment
comment|/**  * A file read lock that uses an {@link IdempotentRepository} and {@link FileChangedExclusiveReadLockStrategy changed} as the lock strategy.  * This allows to plugin and use existing idempotent repositories that for example supports clustering.  * The other read lock strategies that are using marker files or file locks, are not guaranteed to work in clustered setup with various platform and file systems.  */
end_comment

begin_class
DECL|class|FileIdempotentChangedRepositoryReadLockStrategy
specifier|public
class|class
name|FileIdempotentChangedRepositoryReadLockStrategy
extends|extends
name|ServiceSupport
implements|implements
name|GenericFileExclusiveReadLockStrategy
argument_list|<
name|File
argument_list|>
implements|,
name|CamelContextAware
block|{
DECL|field|changed
specifier|private
specifier|final
name|FileChangedExclusiveReadLockStrategy
name|changed
decl_stmt|;
DECL|field|endpoint
specifier|private
name|GenericFileEndpoint
argument_list|<
name|File
argument_list|>
name|endpoint
decl_stmt|;
DECL|field|readLockLoggingLevel
specifier|private
name|LoggingLevel
name|readLockLoggingLevel
init|=
name|LoggingLevel
operator|.
name|DEBUG
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|idempotentRepository
specifier|private
name|IdempotentRepository
name|idempotentRepository
decl_stmt|;
DECL|field|removeOnRollback
specifier|private
name|boolean
name|removeOnRollback
init|=
literal|true
decl_stmt|;
DECL|field|removeOnCommit
specifier|private
name|boolean
name|removeOnCommit
decl_stmt|;
DECL|field|readLockIdempotentReleaseDelay
specifier|private
name|int
name|readLockIdempotentReleaseDelay
decl_stmt|;
DECL|field|readLockIdempotentReleaseAsync
specifier|private
name|boolean
name|readLockIdempotentReleaseAsync
decl_stmt|;
DECL|field|readLockIdempotentReleaseAsyncPoolSize
specifier|private
name|int
name|readLockIdempotentReleaseAsyncPoolSize
decl_stmt|;
DECL|field|readLockIdempotentReleaseExecutorService
specifier|private
name|ScheduledExecutorService
name|readLockIdempotentReleaseExecutorService
decl_stmt|;
DECL|field|shutdownExecutorService
specifier|private
name|boolean
name|shutdownExecutorService
decl_stmt|;
DECL|method|FileIdempotentChangedRepositoryReadLockStrategy ()
specifier|public
name|FileIdempotentChangedRepositoryReadLockStrategy
parameter_list|()
block|{
name|this
operator|.
name|changed
operator|=
operator|new
name|FileChangedExclusiveReadLockStrategy
argument_list|()
expr_stmt|;
comment|// no need to use marker file as idempotent ensures exclusive read-lock
name|this
operator|.
name|changed
operator|.
name|setMarkerFiler
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|this
operator|.
name|changed
operator|.
name|setDeleteOrphanLockFiles
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|prepareOnStartup (GenericFileOperations<File> operations, GenericFileEndpoint<File> endpoint)
specifier|public
name|void
name|prepareOnStartup
parameter_list|(
name|GenericFileOperations
argument_list|<
name|File
argument_list|>
name|operations
parameter_list|,
name|GenericFileEndpoint
argument_list|<
name|File
argument_list|>
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Using FileIdempotentRepositoryReadLockStrategy: {} on endpoint: {}"
argument_list|,
name|idempotentRepository
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|changed
operator|.
name|prepareOnStartup
argument_list|(
name|operations
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
block|}
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
comment|// in clustered mode then another node may have processed the file so we must check here again if the file exists
name|File
name|path
init|=
name|file
operator|.
name|getFile
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|path
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// check if we can begin on this file
name|String
name|key
init|=
name|asKey
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|boolean
name|answer
init|=
name|idempotentRepository
operator|.
name|add
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|answer
condition|)
block|{
comment|// another node is processing the file so skip
name|CamelLogger
operator|.
name|log
argument_list|(
name|log
argument_list|,
name|readLockLoggingLevel
argument_list|,
literal|"Cannot acquire read lock. Will skip the file: "
operator|+
name|file
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|answer
condition|)
block|{
comment|// if we acquired during idempotent then check changed also
name|answer
operator|=
name|changed
operator|.
name|acquireExclusiveReadLock
argument_list|(
name|operations
argument_list|,
name|file
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|answer
condition|)
block|{
comment|// remove from idempontent as we did not acquire it from changed
name|idempotentRepository
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|releaseExclusiveReadLockOnAbort (GenericFileOperations<File> operations, GenericFile<File> file, Exchange exchange)
specifier|public
name|void
name|releaseExclusiveReadLockOnAbort
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
name|changed
operator|.
name|releaseExclusiveReadLockOnAbort
argument_list|(
name|operations
argument_list|,
name|file
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|releaseExclusiveReadLockOnRollback (GenericFileOperations<File> operations, GenericFile<File> file, Exchange exchange)
specifier|public
name|void
name|releaseExclusiveReadLockOnRollback
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
name|String
name|key
init|=
name|asKey
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|Runnable
name|r
init|=
parameter_list|()
lambda|->
block|{
if|if
condition|(
name|removeOnRollback
condition|)
block|{
name|idempotentRepository
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// okay we should not remove then confirm it instead
name|idempotentRepository
operator|.
name|confirm
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|changed
operator|.
name|releaseExclusiveReadLockOnRollback
argument_list|(
name|operations
argument_list|,
name|file
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Error during releasing exclusive readlock on rollback. This exception is ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
if|if
condition|(
name|readLockIdempotentReleaseDelay
operator|>
literal|0
operator|&&
name|readLockIdempotentReleaseExecutorService
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Scheduling readlock release task to run asynchronous delayed after {} millis"
argument_list|,
name|readLockIdempotentReleaseDelay
argument_list|)
expr_stmt|;
name|readLockIdempotentReleaseExecutorService
operator|.
name|schedule
argument_list|(
name|r
argument_list|,
name|readLockIdempotentReleaseDelay
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|readLockIdempotentReleaseDelay
operator|>
literal|0
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Delaying readlock release task {} millis"
argument_list|,
name|readLockIdempotentReleaseDelay
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
name|readLockIdempotentReleaseDelay
argument_list|)
expr_stmt|;
name|r
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|r
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|releaseExclusiveReadLockOnCommit (GenericFileOperations<File> operations, GenericFile<File> file, Exchange exchange)
specifier|public
name|void
name|releaseExclusiveReadLockOnCommit
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
name|String
name|key
init|=
name|asKey
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|Runnable
name|r
init|=
parameter_list|()
lambda|->
block|{
if|if
condition|(
name|removeOnCommit
condition|)
block|{
name|idempotentRepository
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// confirm on commit
name|idempotentRepository
operator|.
name|confirm
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|changed
operator|.
name|releaseExclusiveReadLockOnCommit
argument_list|(
name|operations
argument_list|,
name|file
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Error during releasing exclusive readlock on rollback. This exception is ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
if|if
condition|(
name|readLockIdempotentReleaseDelay
operator|>
literal|0
operator|&&
name|readLockIdempotentReleaseExecutorService
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Scheduling readlock release task to run asynchronous delayed after {} millis"
argument_list|,
name|readLockIdempotentReleaseDelay
argument_list|)
expr_stmt|;
name|readLockIdempotentReleaseExecutorService
operator|.
name|schedule
argument_list|(
name|r
argument_list|,
name|readLockIdempotentReleaseDelay
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|readLockIdempotentReleaseDelay
operator|>
literal|0
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Delaying readlock release task {} millis"
argument_list|,
name|readLockIdempotentReleaseDelay
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
name|readLockIdempotentReleaseDelay
argument_list|)
expr_stmt|;
name|r
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|r
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
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
name|changed
operator|.
name|setTimeout
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
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
name|changed
operator|.
name|setCheckInterval
argument_list|(
name|checkInterval
argument_list|)
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
name|changed
operator|.
name|setReadLockLoggingLevel
argument_list|(
name|readLockLoggingLevel
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setMarkerFiler (boolean markerFile)
specifier|public
name|void
name|setMarkerFiler
parameter_list|(
name|boolean
name|markerFile
parameter_list|)
block|{
comment|// we do not use marker files
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
comment|// we do not use marker files
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
name|changed
operator|.
name|setMinLength
argument_list|(
name|minLength
argument_list|)
expr_stmt|;
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
name|changed
operator|.
name|setMinAge
argument_list|(
name|minAge
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
annotation|@
name|Override
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
comment|/**      * The idempotent repository to use as the store for the read locks.      */
DECL|method|getIdempotentRepository ()
specifier|public
name|IdempotentRepository
name|getIdempotentRepository
parameter_list|()
block|{
return|return
name|idempotentRepository
return|;
block|}
comment|/**      * The idempotent repository to use as the store for the read locks.      */
DECL|method|setIdempotentRepository (IdempotentRepository idempotentRepository)
specifier|public
name|void
name|setIdempotentRepository
parameter_list|(
name|IdempotentRepository
name|idempotentRepository
parameter_list|)
block|{
name|this
operator|.
name|idempotentRepository
operator|=
name|idempotentRepository
expr_stmt|;
block|}
comment|/**      * Whether to remove the file from the idempotent repository when doing a rollback.      *<p/>      * By default this is true.      */
DECL|method|isRemoveOnRollback ()
specifier|public
name|boolean
name|isRemoveOnRollback
parameter_list|()
block|{
return|return
name|removeOnRollback
return|;
block|}
comment|/**      * Whether to remove the file from the idempotent repository when doing a rollback.      *<p/>      * By default this is true.      */
DECL|method|setRemoveOnRollback (boolean removeOnRollback)
specifier|public
name|void
name|setRemoveOnRollback
parameter_list|(
name|boolean
name|removeOnRollback
parameter_list|)
block|{
name|this
operator|.
name|removeOnRollback
operator|=
name|removeOnRollback
expr_stmt|;
block|}
comment|/**      * Whether to remove the file from the idempotent repository when doing a commit.      *<p/>      * By default this is false.      */
DECL|method|isRemoveOnCommit ()
specifier|public
name|boolean
name|isRemoveOnCommit
parameter_list|()
block|{
return|return
name|removeOnCommit
return|;
block|}
comment|/**      * Whether to remove the file from the idempotent repository when doing a commit.      *<p/>      * By default this is false.      */
DECL|method|setRemoveOnCommit (boolean removeOnCommit)
specifier|public
name|void
name|setRemoveOnCommit
parameter_list|(
name|boolean
name|removeOnCommit
parameter_list|)
block|{
name|this
operator|.
name|removeOnCommit
operator|=
name|removeOnCommit
expr_stmt|;
block|}
comment|/**      * Whether to delay the release task for a period of millis.      */
DECL|method|setReadLockIdempotentReleaseDelay (int readLockIdempotentReleaseDelay)
specifier|public
name|void
name|setReadLockIdempotentReleaseDelay
parameter_list|(
name|int
name|readLockIdempotentReleaseDelay
parameter_list|)
block|{
name|this
operator|.
name|readLockIdempotentReleaseDelay
operator|=
name|readLockIdempotentReleaseDelay
expr_stmt|;
block|}
DECL|method|isReadLockIdempotentReleaseAsync ()
specifier|public
name|boolean
name|isReadLockIdempotentReleaseAsync
parameter_list|()
block|{
return|return
name|readLockIdempotentReleaseAsync
return|;
block|}
comment|/**      * Whether the delayed release task should be synchronous or asynchronous.      */
DECL|method|setReadLockIdempotentReleaseAsync (boolean readLockIdempotentReleaseAsync)
specifier|public
name|void
name|setReadLockIdempotentReleaseAsync
parameter_list|(
name|boolean
name|readLockIdempotentReleaseAsync
parameter_list|)
block|{
name|this
operator|.
name|readLockIdempotentReleaseAsync
operator|=
name|readLockIdempotentReleaseAsync
expr_stmt|;
block|}
DECL|method|getReadLockIdempotentReleaseAsyncPoolSize ()
specifier|public
name|int
name|getReadLockIdempotentReleaseAsyncPoolSize
parameter_list|()
block|{
return|return
name|readLockIdempotentReleaseAsyncPoolSize
return|;
block|}
comment|/**      * The number of threads in the scheduled thread pool when using asynchronous release tasks.      */
DECL|method|setReadLockIdempotentReleaseAsyncPoolSize (int readLockIdempotentReleaseAsyncPoolSize)
specifier|public
name|void
name|setReadLockIdempotentReleaseAsyncPoolSize
parameter_list|(
name|int
name|readLockIdempotentReleaseAsyncPoolSize
parameter_list|)
block|{
name|this
operator|.
name|readLockIdempotentReleaseAsyncPoolSize
operator|=
name|readLockIdempotentReleaseAsyncPoolSize
expr_stmt|;
block|}
DECL|method|getReadLockIdempotentReleaseExecutorService ()
specifier|public
name|ScheduledExecutorService
name|getReadLockIdempotentReleaseExecutorService
parameter_list|()
block|{
return|return
name|readLockIdempotentReleaseExecutorService
return|;
block|}
comment|/**      * To use a custom and shared thread pool for asynchronous release tasks.      */
DECL|method|setReadLockIdempotentReleaseExecutorService (ScheduledExecutorService readLockIdempotentReleaseExecutorService)
specifier|public
name|void
name|setReadLockIdempotentReleaseExecutorService
parameter_list|(
name|ScheduledExecutorService
name|readLockIdempotentReleaseExecutorService
parameter_list|)
block|{
name|this
operator|.
name|readLockIdempotentReleaseExecutorService
operator|=
name|readLockIdempotentReleaseExecutorService
expr_stmt|;
block|}
DECL|method|asKey (GenericFile<File> file)
specifier|protected
name|String
name|asKey
parameter_list|(
name|GenericFile
argument_list|<
name|File
argument_list|>
name|file
parameter_list|)
block|{
comment|// use absolute file path as default key, but evaluate if an expression key was configured
name|String
name|key
init|=
name|file
operator|.
name|getAbsoluteFilePath
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getIdempotentKey
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Exchange
name|dummy
init|=
name|endpoint
operator|.
name|createExchange
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|key
operator|=
name|endpoint
operator|.
name|getIdempotentKey
argument_list|()
operator|.
name|evaluate
argument_list|(
name|dummy
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
return|return
name|key
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"camelContext"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|idempotentRepository
argument_list|,
literal|"idempotentRepository"
argument_list|,
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|readLockIdempotentReleaseAsync
operator|&&
name|readLockIdempotentReleaseExecutorService
operator|==
literal|null
condition|)
block|{
name|readLockIdempotentReleaseExecutorService
operator|=
name|camelContext
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newScheduledThreadPool
argument_list|(
name|this
argument_list|,
literal|"ReadLockChangedIdempotentReleaseTask"
argument_list|,
name|readLockIdempotentReleaseAsyncPoolSize
argument_list|)
expr_stmt|;
name|shutdownExecutorService
operator|=
literal|true
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|shutdownExecutorService
operator|&&
name|readLockIdempotentReleaseExecutorService
operator|!=
literal|null
condition|)
block|{
name|camelContext
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdownGraceful
argument_list|(
name|readLockIdempotentReleaseExecutorService
argument_list|,
literal|30000
argument_list|)
expr_stmt|;
name|readLockIdempotentReleaseExecutorService
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

