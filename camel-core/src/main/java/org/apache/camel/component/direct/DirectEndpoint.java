begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.direct
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|direct
package|;
end_package

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
name|impl
operator|.
name|DefaultEndpoint
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
name|UriEndpoint
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * The direct component provides direct, synchronous call to another endpoint from the same CamelContext.  *  * This endpoint can be used to connect existing routes in the same CamelContext.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"1.0.0"
argument_list|,
name|scheme
operator|=
literal|"direct"
argument_list|,
name|title
operator|=
literal|"Direct"
argument_list|,
name|syntax
operator|=
literal|"direct:name"
argument_list|,
name|consumerClass
operator|=
name|DirectConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"core,endpoint"
argument_list|)
DECL|class|DirectEndpoint
specifier|public
class|class
name|DirectEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|consumers
specifier|private
specifier|volatile
name|Map
argument_list|<
name|String
argument_list|,
name|DirectConsumer
argument_list|>
name|consumers
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Name of direct endpoint"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|block
specifier|private
name|boolean
name|block
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"30000"
argument_list|)
DECL|field|timeout
specifier|private
name|long
name|timeout
init|=
literal|30000L
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|failIfNoConsumers
specifier|private
name|boolean
name|failIfNoConsumers
init|=
literal|true
decl_stmt|;
DECL|method|DirectEndpoint ()
specifier|public
name|DirectEndpoint
parameter_list|()
block|{
name|this
operator|.
name|consumers
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|DirectConsumer
argument_list|>
argument_list|()
expr_stmt|;
block|}
DECL|method|DirectEndpoint (String endpointUri, Component component)
specifier|public
name|DirectEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
name|this
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|,
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|DirectConsumer
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|DirectEndpoint (String uri, Component component, Map<String, DirectConsumer> consumers)
specifier|public
name|DirectEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|Component
name|component
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|DirectConsumer
argument_list|>
name|consumers
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|consumers
operator|=
name|consumers
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|block
condition|)
block|{
return|return
operator|new
name|DirectBlockingProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|DirectProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|Consumer
name|answer
init|=
operator|new
name|DirectConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|addConsumer (DirectConsumer consumer)
specifier|public
name|void
name|addConsumer
parameter_list|(
name|DirectConsumer
name|consumer
parameter_list|)
block|{
name|String
name|key
init|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|consumers
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
block|}
DECL|method|removeConsumer (DirectConsumer consumer)
specifier|public
name|void
name|removeConsumer
parameter_list|(
name|DirectConsumer
name|consumer
parameter_list|)
block|{
name|String
name|key
init|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|consumers
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
DECL|method|hasConsumer (DirectConsumer consumer)
specifier|public
name|boolean
name|hasConsumer
parameter_list|(
name|DirectConsumer
name|consumer
parameter_list|)
block|{
name|String
name|key
init|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getKey
argument_list|()
decl_stmt|;
return|return
name|consumers
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
return|;
block|}
DECL|method|getConsumer ()
specifier|public
name|DirectConsumer
name|getConsumer
parameter_list|()
block|{
name|String
name|key
init|=
name|getKey
argument_list|()
decl_stmt|;
return|return
name|consumers
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
DECL|method|isBlock ()
specifier|public
name|boolean
name|isBlock
parameter_list|()
block|{
return|return
name|block
return|;
block|}
comment|/**      * If sending a message to a direct endpoint which has no active consumer,      * then we can tell the producer to block and wait for the consumer to become active.      */
DECL|method|setBlock (boolean block)
specifier|public
name|void
name|setBlock
parameter_list|(
name|boolean
name|block
parameter_list|)
block|{
name|this
operator|.
name|block
operator|=
name|block
expr_stmt|;
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
comment|/**      * The timeout value to use if block is enabled.      *      * @param timeout the timeout value      */
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
DECL|method|isFailIfNoConsumers ()
specifier|public
name|boolean
name|isFailIfNoConsumers
parameter_list|()
block|{
return|return
name|failIfNoConsumers
return|;
block|}
comment|/**      * Whether the producer should fail by throwing an exception, when sending to a DIRECT endpoint with no active consumers.      */
DECL|method|setFailIfNoConsumers (boolean failIfNoConsumers)
specifier|public
name|void
name|setFailIfNoConsumers
parameter_list|(
name|boolean
name|failIfNoConsumers
parameter_list|)
block|{
name|this
operator|.
name|failIfNoConsumers
operator|=
name|failIfNoConsumers
expr_stmt|;
block|}
DECL|method|getKey ()
specifier|protected
name|String
name|getKey
parameter_list|()
block|{
name|String
name|uri
init|=
name|getEndpointUri
argument_list|()
decl_stmt|;
if|if
condition|(
name|uri
operator|.
name|indexOf
argument_list|(
literal|'?'
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
return|return
name|ObjectHelper
operator|.
name|before
argument_list|(
name|uri
argument_list|,
literal|"?"
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|uri
return|;
block|}
block|}
block|}
end_class

end_unit

