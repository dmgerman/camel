begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.watch
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
name|watch
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
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
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
name|ExecutorService
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
name|LinkedBlockingQueue
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
name|io
operator|.
name|methvin
operator|.
name|watcher
operator|.
name|DirectoryChangeEvent
import|;
end_import

begin_import
import|import
name|io
operator|.
name|methvin
operator|.
name|watcher
operator|.
name|DirectoryChangeListener
import|;
end_import

begin_import
import|import
name|io
operator|.
name|methvin
operator|.
name|watcher
operator|.
name|DirectoryWatcher
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
name|Message
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
name|Processor
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
name|RuntimeCamelException
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
name|watch
operator|.
name|constants
operator|.
name|FileEvent
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
name|watch
operator|.
name|utils
operator|.
name|PathUtils
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
name|DefaultConsumer
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
name|AntPathMatcher
import|;
end_import

begin_comment
comment|/**  * The file-watch consumer.  */
end_comment

begin_class
DECL|class|FileWatchConsumer
specifier|public
class|class
name|FileWatchConsumer
extends|extends
name|DefaultConsumer
block|{
DECL|field|watchDirExecutorService
specifier|private
name|ExecutorService
name|watchDirExecutorService
decl_stmt|;
DECL|field|pollExecutorService
specifier|private
name|ExecutorService
name|pollExecutorService
decl_stmt|;
DECL|field|eventQueue
specifier|private
name|LinkedBlockingQueue
argument_list|<
name|FileEvent
argument_list|>
name|eventQueue
decl_stmt|;
DECL|field|baseDirectory
specifier|private
name|Path
name|baseDirectory
decl_stmt|;
DECL|field|antPathMatcher
specifier|private
name|AntPathMatcher
name|antPathMatcher
decl_stmt|;
DECL|field|watcher
specifier|private
name|DirectoryWatcher
name|watcher
decl_stmt|;
DECL|method|FileWatchConsumer (FileWatchEndpoint endpoint, Processor processor)
specifier|public
name|FileWatchConsumer
parameter_list|(
name|FileWatchEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getQueueSize
argument_list|()
operator|<=
literal|0
condition|)
block|{
name|eventQueue
operator|=
operator|new
name|LinkedBlockingQueue
argument_list|<>
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|eventQueue
operator|=
operator|new
name|LinkedBlockingQueue
argument_list|<>
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getQueueSize
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|antPathMatcher
operator|=
operator|new
name|AntPathMatcher
argument_list|()
expr_stmt|;
name|baseDirectory
operator|=
name|Paths
operator|.
name|get
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|Files
operator|.
name|exists
argument_list|(
name|baseDirectory
argument_list|)
condition|)
block|{
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isAutoCreate
argument_list|()
condition|)
block|{
name|baseDirectory
operator|=
name|Files
operator|.
name|createDirectories
argument_list|(
name|baseDirectory
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Path must exists when autoCreate = false"
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
operator|!
name|Files
operator|.
name|isDirectory
argument_list|(
name|baseDirectory
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Parameter path must be directory, %s given"
argument_list|,
name|baseDirectory
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
name|DirectoryWatcher
operator|.
name|Builder
name|watcherBuilder
init|=
name|DirectoryWatcher
operator|.
name|builder
argument_list|()
operator|.
name|path
argument_list|(
name|this
operator|.
name|baseDirectory
argument_list|)
operator|.
name|logger
argument_list|(
name|log
argument_list|)
operator|.
name|listener
argument_list|(
operator|new
name|FileWatchDirectoryChangeListener
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
operator|.
name|toLowerCase
argument_list|()
operator|.
name|contains
argument_list|(
literal|"mac"
argument_list|)
condition|)
block|{
comment|// If not macOS, use FileSystem WatchService. io.methvin.watcher uses by default WatchService associated to default FileSystem.
comment|// We need per FileSystem WatchService, to allow monitoring on machine with multiple file systems.
comment|// Keep default for macOS
name|watcherBuilder
operator|.
name|watchService
argument_list|(
name|this
operator|.
name|baseDirectory
operator|.
name|getFileSystem
argument_list|()
operator|.
name|newWatchService
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|watcherBuilder
operator|.
name|fileHashing
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|isUseFileHashing
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getFileHasher
argument_list|()
operator|!=
literal|null
operator|&&
name|getEndpoint
argument_list|()
operator|.
name|isUseFileHashing
argument_list|()
condition|)
block|{
name|watcherBuilder
operator|.
name|fileHasher
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getFileHasher
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|watcher
operator|=
name|watcherBuilder
operator|.
name|build
argument_list|()
expr_stmt|;
name|watchDirExecutorService
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newFixedThreadPool
argument_list|(
name|this
argument_list|,
literal|"CamelFileWatchService"
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getPollThreads
argument_list|()
argument_list|)
expr_stmt|;
name|pollExecutorService
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newFixedThreadPool
argument_list|(
name|this
argument_list|,
literal|"CamelFileWatchPoll"
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getConcurrentConsumers
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|getEndpoint
argument_list|()
operator|.
name|getPollThreads
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|this
operator|.
name|watcher
operator|.
name|watchAsync
argument_list|(
name|watchDirExecutorService
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|getEndpoint
argument_list|()
operator|.
name|getConcurrentConsumers
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|pollExecutorService
operator|.
name|submit
argument_list|(
operator|new
name|PollRunnable
argument_list|()
argument_list|)
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
name|this
operator|.
name|watcher
operator|.
name|close
argument_list|()
expr_stmt|;
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdownNow
argument_list|(
name|watchDirExecutorService
argument_list|)
expr_stmt|;
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdownNow
argument_list|(
name|pollExecutorService
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doSuspend ()
specifier|protected
name|void
name|doSuspend
parameter_list|()
throws|throws
name|Exception
block|{
name|doStop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doResume ()
specifier|protected
name|void
name|doResume
parameter_list|()
throws|throws
name|Exception
block|{
name|doStart
argument_list|()
expr_stmt|;
block|}
DECL|method|prepareExchange (FileEvent event)
specifier|private
name|Exchange
name|prepareExchange
parameter_list|(
name|FileEvent
name|event
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|File
name|file
init|=
name|event
operator|.
name|getEventPath
argument_list|()
operator|.
name|toFile
argument_list|()
decl_stmt|;
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|FileWatchComponent
operator|.
name|EVENT_TYPE_HEADER
argument_list|,
name|event
operator|.
name|getEventType
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME_ONLY
argument_list|,
name|event
operator|.
name|getEventPath
argument_list|()
operator|.
name|getFileName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
literal|"CamelFileAbsolute"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
specifier|final
name|String
name|absolutePath
init|=
name|PathUtils
operator|.
name|normalizeToString
argument_list|(
name|event
operator|.
name|getEventPath
argument_list|()
operator|.
name|toAbsolutePath
argument_list|()
argument_list|)
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
literal|"CamelFileAbsolutePath"
argument_list|,
name|absolutePath
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_PATH
argument_list|,
name|absolutePath
argument_list|)
expr_stmt|;
specifier|final
name|String
name|relativePath
init|=
name|PathUtils
operator|.
name|normalizeToString
argument_list|(
name|baseDirectory
operator|.
name|relativize
argument_list|(
name|event
operator|.
name|getEventPath
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
name|relativePath
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
literal|"CamelFileRelativePath"
argument_list|,
name|relativePath
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME_CONSUMED
argument_list|,
name|relativePath
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_PARENT
argument_list|,
name|PathUtils
operator|.
name|normalizeToString
argument_list|(
name|event
operator|.
name|getEventPath
argument_list|()
operator|.
name|getParent
argument_list|()
operator|.
name|toAbsolutePath
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_LAST_MODIFIED
argument_list|,
name|event
operator|.
name|getEventDate
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|matchFilters (FileEvent fileEvent)
specifier|private
name|boolean
name|matchFilters
parameter_list|(
name|FileEvent
name|fileEvent
parameter_list|)
block|{
if|if
condition|(
operator|!
name|getEndpoint
argument_list|()
operator|.
name|getEvents
argument_list|()
operator|.
name|contains
argument_list|(
name|fileEvent
operator|.
name|getEventType
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
name|getEndpoint
argument_list|()
operator|.
name|isRecursive
argument_list|()
condition|)
block|{
comment|// On some platforms (eg macOS) is WatchService always recursive,
comment|// so we need to filter this out to make this component platform independent
try|try
block|{
if|if
condition|(
operator|!
name|Files
operator|.
name|isSameFile
argument_list|(
name|fileEvent
operator|.
name|getEventPath
argument_list|()
operator|.
name|getParent
argument_list|()
argument_list|,
name|this
operator|.
name|baseDirectory
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Exception occurred during executing filter. Filtering file %s out."
argument_list|,
name|fileEvent
operator|.
name|getEventPath
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
name|String
name|pattern
init|=
name|getEndpoint
argument_list|()
operator|.
name|getAntInclude
argument_list|()
decl_stmt|;
if|if
condition|(
name|pattern
operator|==
literal|null
operator|||
name|pattern
operator|.
name|trim
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
name|antPathMatcher
operator|.
name|match
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getAntInclude
argument_list|()
argument_list|,
name|PathUtils
operator|.
name|normalizeToString
argument_list|(
name|baseDirectory
operator|.
name|relativize
argument_list|(
name|fileEvent
operator|.
name|getEventPath
argument_list|()
argument_list|)
argument_list|)
comment|// match against relativized path
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|FileWatchEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|FileWatchEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
DECL|class|FileWatchDirectoryChangeListener
class|class
name|FileWatchDirectoryChangeListener
implements|implements
name|DirectoryChangeListener
block|{
annotation|@
name|Override
DECL|method|onEvent (DirectoryChangeEvent directoryChangeEvent)
specifier|public
name|void
name|onEvent
parameter_list|(
name|DirectoryChangeEvent
name|directoryChangeEvent
parameter_list|)
block|{
if|if
condition|(
name|directoryChangeEvent
operator|.
name|eventType
argument_list|()
operator|==
name|DirectoryChangeEvent
operator|.
name|EventType
operator|.
name|OVERFLOW
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"OVERFLOW occurred, some events may be lost. Consider increasing of option 'pollThreads'"
argument_list|)
expr_stmt|;
return|return;
block|}
name|FileEvent
name|fileEvent
init|=
operator|new
name|FileEvent
argument_list|(
name|directoryChangeEvent
argument_list|)
decl_stmt|;
if|if
condition|(
name|matchFilters
argument_list|(
name|fileEvent
argument_list|)
condition|)
block|{
name|eventQueue
operator|.
name|offer
argument_list|(
name|fileEvent
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|isWatching ()
specifier|public
name|boolean
name|isWatching
parameter_list|()
block|{
return|return
operator|!
name|isStoppingOrStopped
argument_list|()
operator|&&
operator|!
name|isSuspendingOrSuspended
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|onException (Exception e)
specifier|public
name|void
name|onException
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|class|PollRunnable
class|class
name|PollRunnable
implements|implements
name|Runnable
block|{
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
while|while
condition|(
operator|!
name|isStoppingOrStopped
argument_list|()
operator|&&
operator|!
name|isSuspendingOrSuspended
argument_list|()
condition|)
block|{
name|FileEvent
name|event
decl_stmt|;
try|try
block|{
name|event
operator|=
name|eventQueue
operator|.
name|poll
argument_list|(
literal|1000
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
return|return;
block|}
if|if
condition|(
name|event
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|Exchange
name|exchange
init|=
name|prepareExchange
argument_list|(
name|event
argument_list|)
decl_stmt|;
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|handleException
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

