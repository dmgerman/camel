begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Consumer
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
name|Producer
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
name|HazelcastQueueConfiguration
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
name|topic
operator|.
name|HazelcastTopicConfiguration
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
name|UriParam
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
name|UriPath
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
name|DefaultEndpoint
import|;
end_import

begin_comment
comment|/**  * The hazelcast component allows you to work with the Hazelcast distributed data grid / cache.  */
end_comment

begin_class
DECL|class|HazelcastDefaultEndpoint
specifier|public
specifier|abstract
class|class
name|HazelcastDefaultEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|command
specifier|protected
name|HazelcastCommand
name|command
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|cacheName
specifier|protected
name|String
name|cacheName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|hazelcastInstance
specifier|protected
name|HazelcastInstance
name|hazelcastInstance
decl_stmt|;
annotation|@
name|UriParam
DECL|field|hazelcastInstanceName
specifier|protected
name|String
name|hazelcastInstanceName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|defaultOperation
specifier|private
name|HazelcastOperation
name|defaultOperation
decl_stmt|;
DECL|method|HazelcastDefaultEndpoint (HazelcastInstance hazelcastInstance, String endpointUri, Component component)
specifier|public
name|HazelcastDefaultEndpoint
parameter_list|(
name|HazelcastInstance
name|hazelcastInstance
parameter_list|,
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
name|this
argument_list|(
name|hazelcastInstance
argument_list|,
name|endpointUri
argument_list|,
name|component
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|HazelcastDefaultEndpoint (HazelcastInstance hazelcastInstance, String endpointUri, Component component, String cacheName)
specifier|public
name|HazelcastDefaultEndpoint
parameter_list|(
name|HazelcastInstance
name|hazelcastInstance
parameter_list|,
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|String
name|cacheName
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|cacheName
operator|=
name|cacheName
expr_stmt|;
name|this
operator|.
name|hazelcastInstance
operator|=
name|hazelcastInstance
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
specifier|abstract
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
function_decl|;
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
specifier|abstract
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
function_decl|;
DECL|method|getCommand ()
specifier|public
name|HazelcastCommand
name|getCommand
parameter_list|()
block|{
return|return
name|command
return|;
block|}
comment|/**      * What operation to perform.      */
DECL|method|setCommand (HazelcastCommand command)
specifier|public
name|void
name|setCommand
parameter_list|(
name|HazelcastCommand
name|command
parameter_list|)
block|{
name|this
operator|.
name|command
operator|=
name|command
expr_stmt|;
block|}
DECL|method|getCacheName ()
specifier|public
name|String
name|getCacheName
parameter_list|()
block|{
return|return
name|cacheName
return|;
block|}
comment|/**      * The name of the cache      */
DECL|method|setCacheName (String cacheName)
specifier|public
name|void
name|setCacheName
parameter_list|(
name|String
name|cacheName
parameter_list|)
block|{
name|this
operator|.
name|cacheName
operator|=
name|cacheName
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
comment|/**      * The hazelcast instance reference which can be used for hazelcast endpoint.      */
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
DECL|method|getHazelcastInstanceName ()
specifier|public
name|String
name|getHazelcastInstanceName
parameter_list|()
block|{
return|return
name|hazelcastInstanceName
return|;
block|}
comment|/**      * The hazelcast instance reference name which can be used for hazelcast endpoint.      * If you don't specify the instance reference, camel use the default hazelcast instance from the camel-hazelcast instance.      */
DECL|method|setHazelcastInstanceName (String hazelcastInstanceName)
specifier|public
name|void
name|setHazelcastInstanceName
parameter_list|(
name|String
name|hazelcastInstanceName
parameter_list|)
block|{
name|this
operator|.
name|hazelcastInstanceName
operator|=
name|hazelcastInstanceName
expr_stmt|;
block|}
comment|/**      * To specify a default operation to use, if no operation header has been provided.      */
DECL|method|setDefaultOperation (HazelcastOperation defaultOperation)
specifier|public
name|void
name|setDefaultOperation
parameter_list|(
name|HazelcastOperation
name|defaultOperation
parameter_list|)
block|{
name|this
operator|.
name|defaultOperation
operator|=
name|defaultOperation
expr_stmt|;
block|}
DECL|method|getDefaultOperation ()
specifier|public
name|HazelcastOperation
name|getDefaultOperation
parameter_list|()
block|{
return|return
name|defaultOperation
return|;
block|}
block|}
end_class

end_unit

