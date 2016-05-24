begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.consul
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|consul
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
name|consul
operator|.
name|enpoint
operator|.
name|ConsulAgentProducer
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
name|consul
operator|.
name|enpoint
operator|.
name|ConsulEventConsumer
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
name|consul
operator|.
name|enpoint
operator|.
name|ConsulEventProducer
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
name|consul
operator|.
name|enpoint
operator|.
name|ConsulKeyValueConsumer
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
name|consul
operator|.
name|enpoint
operator|.
name|ConsulKeyValueProducer
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

begin_comment
comment|/**  * Represents the component that manages {@link ConsulEndpoint}.  */
end_comment

begin_class
DECL|class|ConsulComponent
specifier|public
class|class
name|ConsulComponent
extends|extends
name|UriEndpointComponent
block|{
DECL|method|ConsulComponent ()
specifier|public
name|ConsulComponent
parameter_list|()
block|{
name|super
argument_list|(
name|ConsulEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|ConsulComponent (CamelContext context)
specifier|public
name|ConsulComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|,
name|ConsulEndpoint
operator|.
name|class
argument_list|)
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
return|return
name|ConsulApiEndpoint
operator|.
name|valueOf
argument_list|(
name|remaining
argument_list|)
operator|.
name|create
argument_list|(
name|remaining
argument_list|,
name|uri
argument_list|,
name|this
argument_list|,
name|createConfiguration
argument_list|(
name|parameters
argument_list|)
argument_list|)
return|;
block|}
DECL|method|createConfiguration (Map<String, Object> parameters)
specifier|private
name|ConsulConfiguration
name|createConfiguration
parameter_list|(
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
name|ConsulConfiguration
name|configuration
init|=
operator|new
name|ConsulConfiguration
argument_list|()
decl_stmt|;
name|setProperties
argument_list|(
name|configuration
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|configuration
return|;
block|}
comment|// *************************************************************************
comment|// Consul Api Enpoints (see https://www.consul.io/docs/agent/http.html)
comment|// *************************************************************************
DECL|enum|ConsulApiEndpoint
specifier|private
enum|enum
name|ConsulApiEndpoint
block|{
DECL|enumConstant|kv
name|kv
argument_list|(
name|ConsulKeyValueProducer
operator|::
operator|new
argument_list|,
name|ConsulKeyValueConsumer
operator|::
operator|new
argument_list|)
block|,
DECL|enumConstant|event
name|event
argument_list|(
name|ConsulEventProducer
operator|::
operator|new
argument_list|,
name|ConsulEventConsumer
operator|::
operator|new
argument_list|)
block|,
DECL|enumConstant|agent
name|agent
argument_list|(
name|ConsulAgentProducer
operator|::
operator|new
argument_list|,
literal|null
argument_list|)
block|;
DECL|field|producerFactory
specifier|private
specifier|final
name|ConsulEndpoint
operator|.
name|ProducerFactory
name|producerFactory
decl_stmt|;
DECL|field|consumerFactory
specifier|private
specifier|final
name|ConsulEndpoint
operator|.
name|ConsumerFactory
name|consumerFactory
decl_stmt|;
DECL|method|ConsulApiEndpoint (ConsulEndpoint.ProducerFactory producerFactory, ConsulEndpoint.ConsumerFactory consumerFactory)
name|ConsulApiEndpoint
parameter_list|(
name|ConsulEndpoint
operator|.
name|ProducerFactory
name|producerFactory
parameter_list|,
name|ConsulEndpoint
operator|.
name|ConsumerFactory
name|consumerFactory
parameter_list|)
block|{
name|this
operator|.
name|producerFactory
operator|=
name|producerFactory
expr_stmt|;
name|this
operator|.
name|consumerFactory
operator|=
name|consumerFactory
expr_stmt|;
block|}
DECL|method|create (String apiEndpoint, String uri, ConsulComponent component, ConsulConfiguration configuration)
specifier|public
name|Endpoint
name|create
parameter_list|(
name|String
name|apiEndpoint
parameter_list|,
name|String
name|uri
parameter_list|,
name|ConsulComponent
name|component
parameter_list|,
name|ConsulConfiguration
name|configuration
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|ConsulEndpoint
argument_list|(
name|apiEndpoint
argument_list|,
name|uri
argument_list|,
name|component
argument_list|,
name|configuration
argument_list|,
name|producerFactory
argument_list|,
name|consumerFactory
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

