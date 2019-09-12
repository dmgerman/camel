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
name|util
operator|.
name|Map
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
name|hashing
operator|.
name|FileHasher
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
name|Endpoint
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
name|Metadata
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
name|Registry
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
name|annotations
operator|.
name|Component
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
name|DefaultComponent
import|;
end_import

begin_comment
comment|/**  * Represents the component that manages {@link FileWatchEndpoint}.  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
literal|"file-watch"
argument_list|)
DECL|class|FileWatchComponent
specifier|public
class|class
name|FileWatchComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|EVENT_TYPE_HEADER
specifier|public
specifier|static
specifier|final
name|String
name|EVENT_TYPE_HEADER
init|=
literal|"CamelFileEventType"
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|description
operator|=
literal|"The number of concurrent consumers. Increase this value, if your route is slow to prevent buffering in queue."
argument_list|,
name|defaultValue
operator|=
literal|"1"
argument_list|)
DECL|field|concurrentConsumers
specifier|private
name|int
name|concurrentConsumers
init|=
literal|1
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|description
operator|=
literal|"Maximum size of queue between WatchService and consumer. Unbounded by default."
argument_list|,
name|defaultValue
operator|=
literal|""
operator|+
name|Integer
operator|.
name|MAX_VALUE
argument_list|)
DECL|field|queueSize
specifier|private
name|int
name|queueSize
init|=
name|Integer
operator|.
name|MAX_VALUE
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|description
operator|=
literal|"The number of threads polling WatchService. Increase this value, if you see OVERFLOW messages in log."
argument_list|,
name|defaultValue
operator|=
literal|"1"
argument_list|)
DECL|field|pollThreads
specifier|private
name|int
name|pollThreads
init|=
literal|1
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|description
operator|=
literal|"Reference to io.methvin.watcher.hashing.FileHasher. "
operator|+
literal|"This prevents emitting duplicate events on some platforms. "
operator|+
literal|"For working with large files and if you dont need detect multiple modifications per second per file, "
operator|+
literal|"use #lastModifiedTimeFileHasher. You can also provide custom implementation in registry."
argument_list|,
name|defaultValue
operator|=
literal|"#murmur3FFileHasher"
argument_list|)
DECL|field|fileHasher
specifier|private
name|FileHasher
name|fileHasher
init|=
name|FileHasher
operator|.
name|DEFAULT_FILE_HASHER
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|description
operator|=
literal|"Enables or disables file hashing to detect duplicate events. "
operator|+
literal|"If you disable this, you can get some events multiple times on some platforms and JDKs. "
operator|+
literal|"Check java.nio.file.WatchService limitations for your target platform."
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|useFileHashing
specifier|private
name|boolean
name|useFileHashing
init|=
literal|true
decl_stmt|;
DECL|method|getConcurrentConsumers ()
specifier|public
name|int
name|getConcurrentConsumers
parameter_list|()
block|{
return|return
name|concurrentConsumers
return|;
block|}
DECL|method|setConcurrentConsumers (int concurrentConsumers)
specifier|public
name|void
name|setConcurrentConsumers
parameter_list|(
name|int
name|concurrentConsumers
parameter_list|)
block|{
name|this
operator|.
name|concurrentConsumers
operator|=
name|concurrentConsumers
expr_stmt|;
block|}
DECL|method|getQueueSize ()
specifier|public
name|int
name|getQueueSize
parameter_list|()
block|{
return|return
name|queueSize
return|;
block|}
DECL|method|setQueueSize (int queueSize)
specifier|public
name|void
name|setQueueSize
parameter_list|(
name|int
name|queueSize
parameter_list|)
block|{
name|this
operator|.
name|queueSize
operator|=
name|queueSize
expr_stmt|;
block|}
DECL|method|getPollThreads ()
specifier|public
name|int
name|getPollThreads
parameter_list|()
block|{
return|return
name|pollThreads
return|;
block|}
DECL|method|setPollThreads (int pollThreads)
specifier|public
name|void
name|setPollThreads
parameter_list|(
name|int
name|pollThreads
parameter_list|)
block|{
name|this
operator|.
name|pollThreads
operator|=
name|pollThreads
expr_stmt|;
block|}
DECL|method|getFileHasher ()
specifier|public
name|FileHasher
name|getFileHasher
parameter_list|()
block|{
return|return
name|fileHasher
return|;
block|}
DECL|method|setFileHasher (FileHasher fileHasher)
specifier|public
name|void
name|setFileHasher
parameter_list|(
name|FileHasher
name|fileHasher
parameter_list|)
block|{
name|this
operator|.
name|fileHasher
operator|=
name|fileHasher
expr_stmt|;
block|}
DECL|method|isUseFileHashing ()
specifier|public
name|boolean
name|isUseFileHashing
parameter_list|()
block|{
return|return
name|useFileHashing
return|;
block|}
DECL|method|setUseFileHashing (boolean useFileHashing)
specifier|public
name|void
name|setUseFileHashing
parameter_list|(
name|boolean
name|useFileHashing
parameter_list|)
block|{
name|this
operator|.
name|useFileHashing
operator|=
name|useFileHashing
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|FileWatchEndpoint
name|endpoint
init|=
operator|new
name|FileWatchEndpoint
argument_list|(
name|uri
argument_list|,
name|remaining
argument_list|,
name|this
argument_list|)
decl_stmt|;
comment|// CAMEL-13954: Due to the auto generated property configurator, this intends to set it manually instead of relying on the auto generated property configurator
if|if
condition|(
name|parameters
operator|.
name|containsKey
argument_list|(
literal|"events"
argument_list|)
condition|)
block|{
name|endpoint
operator|.
name|setEvents
argument_list|(
name|parameters
operator|.
name|get
argument_list|(
literal|"events"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|remove
argument_list|(
literal|"events"
argument_list|)
expr_stmt|;
block|}
name|setProperties
argument_list|(
name|endpoint
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
comment|/**      * Register common FileHashers to registry, to make this available out-of-box to use in endpoint definition.      */
annotation|@
name|Override
DECL|method|doInit ()
specifier|protected
name|void
name|doInit
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doInit
argument_list|()
expr_stmt|;
name|Registry
name|registry
init|=
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"murmur3FFileHasher"
argument_list|,
name|FileHasher
operator|.
name|class
argument_list|,
name|FileHasher
operator|.
name|DEFAULT_FILE_HASHER
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"lastModifiedTimeFileHasher"
argument_list|,
name|FileHasher
operator|.
name|class
argument_list|,
name|FileHasher
operator|.
name|LAST_MODIFIED_TIME
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

