begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hazelcast
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hazelcast
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
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
name|Set
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|config
operator|.
name|Config
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|config
operator|.
name|XmlConfigBuilder
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|Hazelcast
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|HazelcastInstance
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
name|component
operator|.
name|hazelcast
operator|.
name|atomicnumber
operator|.
name|HazelcastAtomicnumberEndpoint
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
name|hazelcast
operator|.
name|instance
operator|.
name|HazelcastInstanceEndpoint
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
name|hazelcast
operator|.
name|list
operator|.
name|HazelcastListEndpoint
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
name|hazelcast
operator|.
name|map
operator|.
name|HazelcastMapEndpoint
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
name|hazelcast
operator|.
name|multimap
operator|.
name|HazelcastMultimapEndpoint
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
name|hazelcast
operator|.
name|queue
operator|.
name|HazelcastQueueEndpoint
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
name|hazelcast
operator|.
name|replicatedmap
operator|.
name|HazelcastReplicatedmapEndpoint
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
name|hazelcast
operator|.
name|ringbuffer
operator|.
name|HazelcastRingbufferEndpoint
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
name|hazelcast
operator|.
name|seda
operator|.
name|HazelcastSedaConfiguration
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
name|hazelcast
operator|.
name|seda
operator|.
name|HazelcastSedaEndpoint
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
name|hazelcast
operator|.
name|set
operator|.
name|HazelcastSetEndpoint
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
name|hazelcast
operator|.
name|topic
operator|.
name|HazelcastTopicEndpoint
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
name|impl
operator|.
name|UriEndpointComponent
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
name|camel
operator|.
name|util
operator|.
name|ResourceHelper
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

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hazelcast
operator|.
name|HazelcastConstants
operator|.
name|HAZELCAST_CONFIGU_PARAM
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hazelcast
operator|.
name|HazelcastConstants
operator|.
name|HAZELCAST_CONFIGU_URI_PARAM
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hazelcast
operator|.
name|HazelcastConstants
operator|.
name|HAZELCAST_INSTANCE_NAME_PARAM
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hazelcast
operator|.
name|HazelcastConstants
operator|.
name|HAZELCAST_INSTANCE_PARAM
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|removeStartingCharacters
import|;
end_import

begin_class
DECL|class|HazelcastComponent
specifier|public
class|class
name|HazelcastComponent
extends|extends
name|UriEndpointComponent
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|HazelcastComponent
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|customHazelcastInstances
specifier|private
specifier|final
name|Set
argument_list|<
name|HazelcastInstance
argument_list|>
name|customHazelcastInstances
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|hazelcastInstance
specifier|private
name|HazelcastInstance
name|hazelcastInstance
decl_stmt|;
DECL|method|HazelcastComponent ()
specifier|public
name|HazelcastComponent
parameter_list|()
block|{
name|super
argument_list|(
name|HazelcastDefaultEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|customHazelcastInstances
operator|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
expr_stmt|;
block|}
DECL|method|HazelcastComponent (final CamelContext context)
specifier|public
name|HazelcastComponent
parameter_list|(
specifier|final
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|HazelcastDefaultEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|customHazelcastInstances
operator|=
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
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
comment|// use the given hazelcast Instance or create one if not given
name|HazelcastInstance
name|hzInstance
init|=
name|getOrCreateHzInstance
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
name|String
name|defaultOperation
init|=
name|getAndRemoveOrResolveReferenceParameter
argument_list|(
name|parameters
argument_list|,
name|HazelcastConstants
operator|.
name|OPERATION_PARAM
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|defaultOperation
operator|==
literal|null
condition|)
block|{
name|defaultOperation
operator|=
name|getAndRemoveOrResolveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"defaultOperation"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
name|HazelcastDefaultEndpoint
name|endpoint
init|=
literal|null
decl_stmt|;
comment|// check type of endpoint
if|if
condition|(
name|remaining
operator|.
name|startsWith
argument_list|(
name|HazelcastConstants
operator|.
name|MAP_PREFIX
argument_list|)
condition|)
block|{
comment|// remaining is the cache name
name|remaining
operator|=
name|removeStartingCharacters
argument_list|(
name|remaining
operator|.
name|substring
argument_list|(
name|HazelcastConstants
operator|.
name|MAP_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
argument_list|,
literal|'/'
argument_list|)
expr_stmt|;
name|endpoint
operator|=
operator|new
name|HazelcastMapEndpoint
argument_list|(
name|hzInstance
argument_list|,
name|uri
argument_list|,
name|remaining
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setCommand
argument_list|(
name|HazelcastCommand
operator|.
name|map
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|remaining
operator|.
name|startsWith
argument_list|(
name|HazelcastConstants
operator|.
name|MULTIMAP_PREFIX
argument_list|)
condition|)
block|{
comment|// remaining is the cache name
name|remaining
operator|=
name|removeStartingCharacters
argument_list|(
name|remaining
operator|.
name|substring
argument_list|(
name|HazelcastConstants
operator|.
name|MULTIMAP_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
argument_list|,
literal|'/'
argument_list|)
expr_stmt|;
name|endpoint
operator|=
operator|new
name|HazelcastMultimapEndpoint
argument_list|(
name|hzInstance
argument_list|,
name|uri
argument_list|,
name|remaining
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setCommand
argument_list|(
name|HazelcastCommand
operator|.
name|multimap
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|remaining
operator|.
name|startsWith
argument_list|(
name|HazelcastConstants
operator|.
name|ATOMICNUMBER_PREFIX
argument_list|)
condition|)
block|{
comment|// remaining is the name of the atomic value
name|remaining
operator|=
name|removeStartingCharacters
argument_list|(
name|remaining
operator|.
name|substring
argument_list|(
name|HazelcastConstants
operator|.
name|ATOMICNUMBER_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
argument_list|,
literal|'/'
argument_list|)
expr_stmt|;
name|endpoint
operator|=
operator|new
name|HazelcastAtomicnumberEndpoint
argument_list|(
name|hzInstance
argument_list|,
name|uri
argument_list|,
name|this
argument_list|,
name|remaining
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setCommand
argument_list|(
name|HazelcastCommand
operator|.
name|atomicvalue
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|remaining
operator|.
name|startsWith
argument_list|(
name|HazelcastConstants
operator|.
name|INSTANCE_PREFIX
argument_list|)
condition|)
block|{
comment|// remaining is anything (name it foo ;)
name|remaining
operator|=
name|removeStartingCharacters
argument_list|(
name|remaining
operator|.
name|substring
argument_list|(
name|HazelcastConstants
operator|.
name|INSTANCE_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
argument_list|,
literal|'/'
argument_list|)
expr_stmt|;
name|endpoint
operator|=
operator|new
name|HazelcastInstanceEndpoint
argument_list|(
name|hzInstance
argument_list|,
name|uri
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setCommand
argument_list|(
name|HazelcastCommand
operator|.
name|instance
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|remaining
operator|.
name|startsWith
argument_list|(
name|HazelcastConstants
operator|.
name|QUEUE_PREFIX
argument_list|)
condition|)
block|{
comment|// remaining is anything (name it foo ;)
name|remaining
operator|=
name|removeStartingCharacters
argument_list|(
name|remaining
operator|.
name|substring
argument_list|(
name|HazelcastConstants
operator|.
name|QUEUE_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
argument_list|,
literal|'/'
argument_list|)
expr_stmt|;
name|endpoint
operator|=
operator|new
name|HazelcastQueueEndpoint
argument_list|(
name|hzInstance
argument_list|,
name|uri
argument_list|,
name|this
argument_list|,
name|remaining
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setCommand
argument_list|(
name|HazelcastCommand
operator|.
name|queue
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|remaining
operator|.
name|startsWith
argument_list|(
name|HazelcastConstants
operator|.
name|TOPIC_PREFIX
argument_list|)
condition|)
block|{
comment|// remaining is anything (name it foo ;)
name|remaining
operator|=
name|removeStartingCharacters
argument_list|(
name|remaining
operator|.
name|substring
argument_list|(
name|HazelcastConstants
operator|.
name|TOPIC_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
argument_list|,
literal|'/'
argument_list|)
expr_stmt|;
name|endpoint
operator|=
operator|new
name|HazelcastTopicEndpoint
argument_list|(
name|hzInstance
argument_list|,
name|uri
argument_list|,
name|this
argument_list|,
name|remaining
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setCommand
argument_list|(
name|HazelcastCommand
operator|.
name|topic
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|remaining
operator|.
name|startsWith
argument_list|(
name|HazelcastConstants
operator|.
name|SEDA_PREFIX
argument_list|)
condition|)
block|{
comment|// remaining is anything (name it foo ;)
name|remaining
operator|=
name|removeStartingCharacters
argument_list|(
name|remaining
operator|.
name|substring
argument_list|(
name|HazelcastConstants
operator|.
name|SEDA_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
argument_list|,
literal|'/'
argument_list|)
expr_stmt|;
specifier|final
name|HazelcastSedaConfiguration
name|config
init|=
operator|new
name|HazelcastSedaConfiguration
argument_list|()
decl_stmt|;
name|setProperties
argument_list|(
name|config
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|config
operator|.
name|setQueueName
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
name|endpoint
operator|=
operator|new
name|HazelcastSedaEndpoint
argument_list|(
name|hzInstance
argument_list|,
name|uri
argument_list|,
name|this
argument_list|,
name|config
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setCacheName
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setCommand
argument_list|(
name|HazelcastCommand
operator|.
name|seda
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|remaining
operator|.
name|startsWith
argument_list|(
name|HazelcastConstants
operator|.
name|LIST_PREFIX
argument_list|)
condition|)
block|{
comment|// remaining is anything (name it foo ;)
name|remaining
operator|=
name|removeStartingCharacters
argument_list|(
name|remaining
operator|.
name|substring
argument_list|(
name|HazelcastConstants
operator|.
name|LIST_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
argument_list|,
literal|'/'
argument_list|)
expr_stmt|;
name|endpoint
operator|=
operator|new
name|HazelcastListEndpoint
argument_list|(
name|hzInstance
argument_list|,
name|uri
argument_list|,
name|this
argument_list|,
name|remaining
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setCommand
argument_list|(
name|HazelcastCommand
operator|.
name|list
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|remaining
operator|.
name|startsWith
argument_list|(
name|HazelcastConstants
operator|.
name|REPLICATEDMAP_PREFIX
argument_list|)
condition|)
block|{
comment|// remaining is anything (name it foo ;)
name|remaining
operator|=
name|removeStartingCharacters
argument_list|(
name|remaining
operator|.
name|substring
argument_list|(
name|HazelcastConstants
operator|.
name|REPLICATEDMAP_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
argument_list|,
literal|'/'
argument_list|)
expr_stmt|;
name|endpoint
operator|=
operator|new
name|HazelcastReplicatedmapEndpoint
argument_list|(
name|hzInstance
argument_list|,
name|uri
argument_list|,
name|remaining
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setCommand
argument_list|(
name|HazelcastCommand
operator|.
name|replicatedmap
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|remaining
operator|.
name|startsWith
argument_list|(
name|HazelcastConstants
operator|.
name|SET_PREFIX
argument_list|)
condition|)
block|{
comment|// remaining is anything (name it foo ;)
name|remaining
operator|=
name|removeStartingCharacters
argument_list|(
name|remaining
operator|.
name|substring
argument_list|(
name|HazelcastConstants
operator|.
name|SET_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
argument_list|,
literal|'/'
argument_list|)
expr_stmt|;
name|endpoint
operator|=
operator|new
name|HazelcastSetEndpoint
argument_list|(
name|hzInstance
argument_list|,
name|uri
argument_list|,
name|this
argument_list|,
name|remaining
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setCommand
argument_list|(
name|HazelcastCommand
operator|.
name|set
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|remaining
operator|.
name|startsWith
argument_list|(
name|HazelcastConstants
operator|.
name|RINGBUFFER_PREFIX
argument_list|)
condition|)
block|{
comment|// remaining is anything (name it foo ;)
name|remaining
operator|=
name|removeStartingCharacters
argument_list|(
name|remaining
operator|.
name|substring
argument_list|(
name|HazelcastConstants
operator|.
name|RINGBUFFER_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
argument_list|,
literal|'/'
argument_list|)
expr_stmt|;
name|endpoint
operator|=
operator|new
name|HazelcastRingbufferEndpoint
argument_list|(
name|hzInstance
argument_list|,
name|uri
argument_list|,
name|this
argument_list|,
name|remaining
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setCommand
argument_list|(
name|HazelcastCommand
operator|.
name|ringbuffer
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|endpoint
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Your URI does not provide a correct 'type' prefix. It should be anything like "
operator|+
literal|"'hazelcast:[%s|%s|%s|%s|%s|%s|%s|%s|%s|%s]name' but is '%s'."
argument_list|,
name|HazelcastConstants
operator|.
name|MAP_PREFIX
argument_list|,
name|HazelcastConstants
operator|.
name|MULTIMAP_PREFIX
argument_list|,
name|HazelcastConstants
operator|.
name|ATOMICNUMBER_PREFIX
argument_list|,
name|HazelcastConstants
operator|.
name|INSTANCE_PREFIX
argument_list|,
name|HazelcastConstants
operator|.
name|QUEUE_PREFIX
argument_list|,
name|HazelcastConstants
operator|.
name|SEDA_PREFIX
argument_list|,
name|HazelcastConstants
operator|.
name|LIST_PREFIX
argument_list|,
name|HazelcastConstants
operator|.
name|REPLICATEDMAP_PREFIX
argument_list|,
name|HazelcastConstants
operator|.
name|SET_PREFIX
argument_list|,
name|HazelcastConstants
operator|.
name|RINGBUFFER_PREFIX
argument_list|,
name|uri
argument_list|)
argument_list|)
throw|;
block|}
name|endpoint
operator|.
name|setDefaultOperation
argument_list|(
name|defaultOperation
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|public
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
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|public
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
for|for
control|(
name|HazelcastInstance
name|hazelcastInstance
range|:
name|customHazelcastInstances
control|)
block|{
name|hazelcastInstance
operator|.
name|getLifecycleService
argument_list|()
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
name|customHazelcastInstances
operator|.
name|clear
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|getHazelcastInstance ()
specifier|public
name|HazelcastInstance
name|getHazelcastInstance
parameter_list|()
block|{
return|return
name|hazelcastInstance
return|;
block|}
comment|/**      * The hazelcast instance reference which can be used for hazelcast endpoint.      * If you don't specify the instance reference, camel use the default hazelcast instance from the camel-hazelcast instance.      */
DECL|method|setHazelcastInstance (HazelcastInstance hazelcastInstance)
specifier|public
name|void
name|setHazelcastInstance
parameter_list|(
name|HazelcastInstance
name|hazelcastInstance
parameter_list|)
block|{
name|this
operator|.
name|hazelcastInstance
operator|=
name|hazelcastInstance
expr_stmt|;
block|}
DECL|method|getOrCreateHzInstance (CamelContext context, Map<String, Object> parameters)
specifier|private
name|HazelcastInstance
name|getOrCreateHzInstance
parameter_list|(
name|CamelContext
name|context
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
name|HazelcastInstance
name|hzInstance
init|=
literal|null
decl_stmt|;
name|Config
name|config
init|=
literal|null
decl_stmt|;
comment|// Query param named 'hazelcastInstance' (if exists) overrides the instance that was set
name|hzInstance
operator|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
name|HAZELCAST_INSTANCE_PARAM
argument_list|,
name|HazelcastInstance
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// Check if an already created instance is given then just get instance by its name.
if|if
condition|(
name|hzInstance
operator|==
literal|null
operator|&&
name|parameters
operator|.
name|get
argument_list|(
name|HAZELCAST_INSTANCE_NAME_PARAM
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|hzInstance
operator|=
name|Hazelcast
operator|.
name|getHazelcastInstanceByName
argument_list|(
operator|(
name|String
operator|)
name|parameters
operator|.
name|get
argument_list|(
name|HAZELCAST_INSTANCE_NAME_PARAM
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// If instance neither supplied nor found by name, try to lookup its config
comment|// as reference or as xml configuration file.
if|if
condition|(
name|hzInstance
operator|==
literal|null
condition|)
block|{
name|config
operator|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
name|HAZELCAST_CONFIGU_PARAM
argument_list|,
name|Config
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|config
operator|==
literal|null
condition|)
block|{
name|String
name|configUri
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
name|HAZELCAST_CONFIGU_URI_PARAM
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|configUri
operator|!=
literal|null
condition|)
block|{
name|configUri
operator|=
name|getCamelContext
argument_list|()
operator|.
name|resolvePropertyPlaceholders
argument_list|(
name|configUri
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|configUri
operator|!=
literal|null
condition|)
block|{
name|InputStream
name|is
init|=
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsInputStream
argument_list|(
name|context
argument_list|,
name|configUri
argument_list|)
decl_stmt|;
name|config
operator|=
operator|new
name|XmlConfigBuilder
argument_list|(
name|is
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|hazelcastInstance
operator|==
literal|null
operator|&&
name|config
operator|==
literal|null
condition|)
block|{
name|config
operator|=
operator|new
name|XmlConfigBuilder
argument_list|()
operator|.
name|build
argument_list|()
expr_stmt|;
comment|// Disable the version check
name|config
operator|.
name|getProperties
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"hazelcast.version.check.enabled"
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
name|config
operator|.
name|getProperties
argument_list|()
operator|.
name|setProperty
argument_list|(
literal|"hazelcast.phone.home.enabled"
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
name|hzInstance
operator|=
name|Hazelcast
operator|.
name|newHazelcastInstance
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|config
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|config
operator|.
name|getInstanceName
argument_list|()
argument_list|)
condition|)
block|{
name|hzInstance
operator|=
name|Hazelcast
operator|.
name|getOrCreateHazelcastInstance
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|hzInstance
operator|=
name|Hazelcast
operator|.
name|newHazelcastInstance
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|hzInstance
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|this
operator|.
name|customHazelcastInstances
operator|.
name|add
argument_list|(
name|hzInstance
argument_list|)
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Add managed HZ instance {}"
argument_list|,
name|hzInstance
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|hzInstance
operator|==
literal|null
condition|?
name|hazelcastInstance
else|:
name|hzInstance
return|;
block|}
block|}
end_class

end_unit

