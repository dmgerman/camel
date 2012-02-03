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

begin_class
DECL|class|AvroNettyEndpoint
specifier|public
class|class
name|AvroNettyEndpoint
extends|extends
name|AvroEndpoint
block|{
comment|/**      * Constructs a fully-initialized DefaultEndpoint instance. This is the      * preferred method of constructing an object from Java code (as opposed to      * Spring beans, etc.).      *      * @param endpointUri the full URI used to create this endpoint      * @param component   the component that created this endpoint      */
DECL|method|AvroNettyEndpoint (String endpointUri, Component component, AvroConfiguration configuration)
specifier|public
name|AvroNettyEndpoint
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
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a new producer which is used send messages into the endpoint      *      * @return a newly created producer      * @throws Exception can be thrown      */
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|AvroNettyProducer
argument_list|(
name|this
argument_list|)
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
name|AvroNettyConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
return|;
block|}
block|}
end_class

end_unit

