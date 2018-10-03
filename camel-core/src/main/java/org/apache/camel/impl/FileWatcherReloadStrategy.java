begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|FileInputStream
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
name|FileVisitResult
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
name|SimpleFileVisitor
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
name|WatchEvent
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
name|WatchKey
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
name|WatchService
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
name|attribute
operator|.
name|BasicFileAttributes
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|api
operator|.
name|management
operator|.
name|ManagedAttribute
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
name|api
operator|.
name|management
operator|.
name|ManagedResource
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
name|ReloadStrategySupport
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
name|IOHelper
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
import|import static
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|StandardWatchEventKinds
operator|.
name|ENTRY_CREATE
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|StandardWatchEventKinds
operator|.
name|ENTRY_MODIFY
import|;
end_import

begin_comment
comment|/**  * A file based {@link org.apache.camel.spi.ReloadStrategy} which watches a file folder  * for modified files and reload on file changes.  *<p/>  * This implementation uses the JDK {@link WatchService} to watch for when files are  * created or modified. Mac OS X users should be noted the osx JDK does not support  * native file system changes and therefore the watch service is much slower than on  * linux or windows systems.  */
end_comment

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed FileWatcherReloadStrategy"
argument_list|)
DECL|class|FileWatcherReloadStrategy
specifier|public
class|class
name|FileWatcherReloadStrategy
extends|extends
name|ReloadStrategySupport
block|{
DECL|field|folder
specifier|private
name|String
name|folder
decl_stmt|;
DECL|field|isRecursive
specifier|private
name|boolean
name|isRecursive
decl_stmt|;
DECL|field|watcher
specifier|private
name|WatchService
name|watcher
decl_stmt|;
DECL|field|executorService
specifier|private
name|ExecutorService
name|executorService
decl_stmt|;
DECL|field|task
specifier|private
name|WatchFileChangesTask
name|task
decl_stmt|;
DECL|field|folderKeys
specifier|private
name|Map
argument_list|<
name|WatchKey
argument_list|,
name|Path
argument_list|>
name|folderKeys
decl_stmt|;
DECL|field|pollTimeout
specifier|private
name|long
name|pollTimeout
init|=
literal|2000
decl_stmt|;
DECL|method|FileWatcherReloadStrategy ()
specifier|public
name|FileWatcherReloadStrategy
parameter_list|()
block|{
name|setRecursive
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|FileWatcherReloadStrategy (String directory)
specifier|public
name|FileWatcherReloadStrategy
parameter_list|(
name|String
name|directory
parameter_list|)
block|{
name|setFolder
argument_list|(
name|directory
argument_list|)
expr_stmt|;
name|setRecursive
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|FileWatcherReloadStrategy (String directory, boolean isRecursive)
specifier|public
name|FileWatcherReloadStrategy
parameter_list|(
name|String
name|directory
parameter_list|,
name|boolean
name|isRecursive
parameter_list|)
block|{
name|setFolder
argument_list|(
name|directory
argument_list|)
expr_stmt|;
name|setRecursive
argument_list|(
name|isRecursive
argument_list|)
expr_stmt|;
block|}
DECL|method|setFolder (String folder)
specifier|public
name|void
name|setFolder
parameter_list|(
name|String
name|folder
parameter_list|)
block|{
name|this
operator|.
name|folder
operator|=
name|folder
expr_stmt|;
block|}
DECL|method|setRecursive (boolean isRecursive)
specifier|public
name|void
name|setRecursive
parameter_list|(
name|boolean
name|isRecursive
parameter_list|)
block|{
name|this
operator|.
name|isRecursive
operator|=
name|isRecursive
expr_stmt|;
block|}
comment|/**      * Sets the poll timeout in millis. The default value is 2000.      */
DECL|method|setPollTimeout (long pollTimeout)
specifier|public
name|void
name|setPollTimeout
parameter_list|(
name|long
name|pollTimeout
parameter_list|)
block|{
name|this
operator|.
name|pollTimeout
operator|=
name|pollTimeout
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Folder being watched"
argument_list|)
DECL|method|getFolder ()
specifier|public
name|String
name|getFolder
parameter_list|()
block|{
return|return
name|folder
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether the reload strategy watches directory recursively"
argument_list|)
DECL|method|isRecursive ()
specifier|public
name|boolean
name|isRecursive
parameter_list|()
block|{
return|return
name|isRecursive
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether the watcher is running"
argument_list|)
DECL|method|isRunning ()
specifier|public
name|boolean
name|isRunning
parameter_list|()
block|{
return|return
name|task
operator|!=
literal|null
operator|&&
name|task
operator|.
name|isRunning
argument_list|()
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
if|if
condition|(
name|folder
operator|==
literal|null
condition|)
block|{
comment|// no folder configured
return|return;
block|}
name|File
name|dir
init|=
operator|new
name|File
argument_list|(
name|folder
argument_list|)
decl_stmt|;
if|if
condition|(
name|dir
operator|.
name|exists
argument_list|()
operator|&&
name|dir
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"Starting ReloadStrategy to watch directory: {}"
argument_list|,
name|dir
argument_list|)
expr_stmt|;
name|WatchEvent
operator|.
name|Modifier
name|modifier
init|=
literal|null
decl_stmt|;
comment|// if its mac OSX then attempt to apply workaround or warn its slower
name|String
name|os
init|=
name|ObjectHelper
operator|.
name|getSystemProperty
argument_list|(
literal|"os.name"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
if|if
condition|(
name|os
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
operator|.
name|startsWith
argument_list|(
literal|"mac"
argument_list|)
condition|)
block|{
comment|// this modifier can speedup the scanner on mac osx (as java on mac has no native file notification integration)
name|Class
argument_list|<
name|WatchEvent
operator|.
name|Modifier
argument_list|>
name|clazz
init|=
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveClass
argument_list|(
literal|"com.sun.nio.file.SensitivityWatchEventModifier"
argument_list|,
name|WatchEvent
operator|.
name|Modifier
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|clazz
operator|!=
literal|null
condition|)
block|{
name|WatchEvent
operator|.
name|Modifier
index|[]
name|modifiers
init|=
name|clazz
operator|.
name|getEnumConstants
argument_list|()
decl_stmt|;
for|for
control|(
name|WatchEvent
operator|.
name|Modifier
name|mod
range|:
name|modifiers
control|)
block|{
if|if
condition|(
literal|"HIGH"
operator|.
name|equals
argument_list|(
name|mod
operator|.
name|name
argument_list|()
argument_list|)
condition|)
block|{
name|modifier
operator|=
name|mod
expr_stmt|;
break|break;
block|}
block|}
block|}
if|if
condition|(
name|modifier
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"On Mac OS X the JDK WatchService is slow by default so enabling SensitivityWatchEventModifier.HIGH as workaround"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"On Mac OS X the JDK WatchService is slow and it may take up till 10 seconds to notice file changes"
argument_list|)
expr_stmt|;
block|}
block|}
try|try
block|{
name|Path
name|path
init|=
name|dir
operator|.
name|toPath
argument_list|()
decl_stmt|;
name|watcher
operator|=
name|path
operator|.
name|getFileSystem
argument_list|()
operator|.
name|newWatchService
argument_list|()
expr_stmt|;
comment|// we cannot support deleting files as we don't know which routes that would be
if|if
condition|(
name|isRecursive
condition|)
block|{
name|this
operator|.
name|folderKeys
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|registerRecursive
argument_list|(
name|watcher
argument_list|,
name|path
argument_list|,
name|modifier
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|registerPathToWatcher
argument_list|(
name|modifier
argument_list|,
name|path
argument_list|,
name|watcher
argument_list|)
expr_stmt|;
block|}
name|task
operator|=
operator|new
name|WatchFileChangesTask
argument_list|(
name|watcher
argument_list|,
name|path
argument_list|)
expr_stmt|;
name|executorService
operator|=
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newSingleThreadExecutor
argument_list|(
name|this
argument_list|,
literal|"FileWatcherReloadStrategy"
argument_list|)
expr_stmt|;
name|executorService
operator|.
name|submit
argument_list|(
name|task
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
DECL|method|registerPathToWatcher (WatchEvent.Modifier modifier, Path path, WatchService watcher)
specifier|private
name|WatchKey
name|registerPathToWatcher
parameter_list|(
name|WatchEvent
operator|.
name|Modifier
name|modifier
parameter_list|,
name|Path
name|path
parameter_list|,
name|WatchService
name|watcher
parameter_list|)
throws|throws
name|IOException
block|{
name|WatchKey
name|key
decl_stmt|;
if|if
condition|(
name|modifier
operator|!=
literal|null
condition|)
block|{
name|key
operator|=
name|path
operator|.
name|register
argument_list|(
name|watcher
argument_list|,
operator|new
name|WatchEvent
operator|.
name|Kind
argument_list|<
name|?
argument_list|>
index|[]
block|{
name|ENTRY_CREATE
operator|,
name|ENTRY_MODIFY
block|}
operator|,
name|modifier
block|)
empty_stmt|;
block|}
else|else
block|{
name|key
operator|=
name|path
operator|.
name|register
argument_list|(
name|watcher
argument_list|,
name|ENTRY_CREATE
argument_list|,
name|ENTRY_MODIFY
argument_list|)
expr_stmt|;
block|}
return|return
name|key
return|;
block|}
end_class

begin_function
DECL|method|registerRecursive (final WatchService watcher, final Path root, final WatchEvent.Modifier modifier)
specifier|private
name|void
name|registerRecursive
parameter_list|(
specifier|final
name|WatchService
name|watcher
parameter_list|,
specifier|final
name|Path
name|root
parameter_list|,
specifier|final
name|WatchEvent
operator|.
name|Modifier
name|modifier
parameter_list|)
throws|throws
name|IOException
block|{
name|Files
operator|.
name|walkFileTree
argument_list|(
name|root
argument_list|,
operator|new
name|SimpleFileVisitor
argument_list|<
name|Path
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|FileVisitResult
name|preVisitDirectory
parameter_list|(
name|Path
name|dir
parameter_list|,
name|BasicFileAttributes
name|attrs
parameter_list|)
throws|throws
name|IOException
block|{
name|WatchKey
name|key
init|=
name|registerPathToWatcher
argument_list|(
name|modifier
argument_list|,
name|dir
argument_list|,
name|watcher
argument_list|)
decl_stmt|;
name|folderKeys
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|dir
argument_list|)
expr_stmt|;
return|return
name|FileVisitResult
operator|.
name|CONTINUE
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
if|if
condition|(
name|executorService
operator|!=
literal|null
condition|)
block|{
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdownGraceful
argument_list|(
name|executorService
argument_list|)
expr_stmt|;
name|executorService
operator|=
literal|null
expr_stmt|;
block|}
if|if
condition|(
name|watcher
operator|!=
literal|null
condition|)
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|watcher
argument_list|)
expr_stmt|;
block|}
block|}
end_function

begin_comment
comment|/**      * Background task which watches for file changes      */
end_comment

begin_class
DECL|class|WatchFileChangesTask
specifier|protected
class|class
name|WatchFileChangesTask
implements|implements
name|Runnable
block|{
DECL|field|watcher
specifier|private
specifier|final
name|WatchService
name|watcher
decl_stmt|;
DECL|field|folder
specifier|private
specifier|final
name|Path
name|folder
decl_stmt|;
DECL|field|running
specifier|private
specifier|volatile
name|boolean
name|running
decl_stmt|;
DECL|method|WatchFileChangesTask (WatchService watcher, Path folder)
specifier|public
name|WatchFileChangesTask
parameter_list|(
name|WatchService
name|watcher
parameter_list|,
name|Path
name|folder
parameter_list|)
block|{
name|this
operator|.
name|watcher
operator|=
name|watcher
expr_stmt|;
name|this
operator|.
name|folder
operator|=
name|folder
expr_stmt|;
block|}
DECL|method|isRunning ()
specifier|public
name|boolean
name|isRunning
parameter_list|()
block|{
return|return
name|running
return|;
block|}
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"ReloadStrategy is starting watching folder: {}"
argument_list|,
name|folder
argument_list|)
expr_stmt|;
comment|// allow to run while starting Camel
while|while
condition|(
name|isStarting
argument_list|()
operator|||
name|isRunAllowed
argument_list|()
condition|)
block|{
name|running
operator|=
literal|true
expr_stmt|;
name|WatchKey
name|key
decl_stmt|;
try|try
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"ReloadStrategy is polling for file changes in directory: {}"
argument_list|,
name|folder
argument_list|)
expr_stmt|;
comment|// wait for a key to be available
name|key
operator|=
name|watcher
operator|.
name|poll
argument_list|(
name|pollTimeout
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
name|ex
parameter_list|)
block|{
break|break;
block|}
if|if
condition|(
name|key
operator|!=
literal|null
condition|)
block|{
name|Path
name|pathToReload
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|isRecursive
condition|)
block|{
name|pathToReload
operator|=
name|folderKeys
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|pathToReload
operator|=
name|folder
expr_stmt|;
block|}
for|for
control|(
name|WatchEvent
argument_list|<
name|?
argument_list|>
name|event
range|:
name|key
operator|.
name|pollEvents
argument_list|()
control|)
block|{
name|WatchEvent
argument_list|<
name|Path
argument_list|>
name|we
init|=
operator|(
name|WatchEvent
argument_list|<
name|Path
argument_list|>
operator|)
name|event
decl_stmt|;
name|Path
name|path
init|=
name|we
operator|.
name|context
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|pathToReload
operator|.
name|resolve
argument_list|(
name|path
argument_list|)
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|toFile
argument_list|()
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Modified/Created file: {}"
argument_list|,
name|name
argument_list|)
expr_stmt|;
comment|// must be an .xml file
if|if
condition|(
name|name
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
operator|.
name|endsWith
argument_list|(
literal|".xml"
argument_list|)
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Modified/Created XML file: {}"
argument_list|,
name|name
argument_list|)
expr_stmt|;
try|try
block|{
name|FileInputStream
name|fis
init|=
operator|new
name|FileInputStream
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|onReloadXml
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|name
argument_list|,
name|fis
argument_list|)
expr_stmt|;
name|IOHelper
operator|.
name|close
argument_list|(
name|fis
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
literal|"Error reloading routes from file: "
operator|+
name|name
operator|+
literal|" due "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|". This exception is ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// the key must be reset after processed
name|boolean
name|valid
init|=
name|key
operator|.
name|reset
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|valid
condition|)
block|{
break|break;
block|}
block|}
block|}
name|running
operator|=
literal|false
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"ReloadStrategy is stopping watching folder: {}"
argument_list|,
name|folder
argument_list|)
expr_stmt|;
block|}
block|}
end_class

unit|}
end_unit

