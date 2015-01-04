begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.avro
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|avro
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|Protocol
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|avro
operator|.
name|Schema
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
name|ExchangePattern
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

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"avro"
argument_list|,
name|consumerClass
operator|=
name|AvroConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"messaging,transformation"
argument_list|)
DECL|class|AvroEndpoint
specifier|public
specifier|abstract
class|class
name|AvroEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|AvroConfiguration
name|configuration
decl_stmt|;
comment|/**      * Constructs a fully-initialized DefaultEndpoint instance. This is the      * preferred method of constructing an object from Java code (as opposed to      * Spring beans, etc.).      *      * @param endpointUri the full URI used to create this endpoint      * @param component   the component that created this endpoint      */
DECL|method|AvroEndpoint (String endpointUri, Component component, AvroConfiguration configuration)
specifier|public
name|AvroEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|AvroConfiguration
name|configuration
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
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|createExchange (Protocol.Message message, Object request)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|Protocol
operator|.
name|Message
name|message
parameter_list|,
name|Object
name|request
parameter_list|)
block|{
name|ExchangePattern
name|pattern
init|=
name|ExchangePattern
operator|.
name|InOut
decl_stmt|;
if|if
condition|(
name|message
operator|.
name|getResponse
argument_list|()
operator|.
name|equals
argument_list|(
name|Schema
operator|.
name|Type
operator|.
name|NULL
argument_list|)
condition|)
block|{
name|pattern
operator|=
name|ExchangePattern
operator|.
name|InOnly
expr_stmt|;
block|}
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|(
name|pattern
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|AvroConstants
operator|.
name|AVRO_MESSAGE_NAME
argument_list|,
name|message
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
annotation|@
name|Override
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
comment|/**      * Creates a new<a      * href="http://camel.apache.org/event-driven-consumer.html">Event      * Driven Consumer</a> which consumes messages from the endpoint using the      * given processor      *      * @param processor the given processor      * @return a newly created consumer      * @throws Exception can be thrown      */
annotation|@
name|Override
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
return|return
operator|new
name|AvroConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|AvroConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
block|}
end_class

end_unit

