begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.opentracing.decorators
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|opentracing
operator|.
name|decorators
package|;
end_package

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
name|Exchange
import|;
end_import

begin_class
DECL|class|RabbitmqSpanDecorator
specifier|public
class|class
name|RabbitmqSpanDecorator
extends|extends
name|AbstractMessagingSpanDecorator
block|{
comment|/**      * Constants copied from {@link org.apache.camel.component.rabbitmq.RabbitMQConstants}      */
DECL|field|EXCHANGE_NAME
specifier|protected
specifier|static
specifier|final
name|String
name|EXCHANGE_NAME
init|=
literal|"rabbitmq.EXCHANGE_NAME"
decl_stmt|;
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|String
name|getComponent
parameter_list|()
block|{
return|return
literal|"rabbitmq"
return|;
block|}
annotation|@
name|Override
DECL|method|getDestination (Exchange exchange, Endpoint endpoint)
specifier|public
name|String
name|getDestination
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
block|{
return|return
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|EXCHANGE_NAME
argument_list|)
return|;
block|}
block|}
end_class

end_unit

