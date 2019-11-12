begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_comment
comment|/**  * Global endpoint configurations which can be set as defaults when Camel creates new {@link Endpoint}s.  */
end_comment

begin_interface
DECL|interface|GlobalEndpointConfiguration
specifier|public
interface|interface
name|GlobalEndpointConfiguration
block|{
DECL|method|isLazyStartProducer ()
name|boolean
name|isLazyStartProducer
parameter_list|()
function_decl|;
comment|/**      * Whether the producer should be started lazy (on the first message). By starting lazy you can use this to allow CamelContext and routes to startup      * in situations where a producer may otherwise fail during starting and cause the route to fail being started. By deferring this startup to be lazy then      * the startup failure can be handled during routing messages via Camel's routing error handlers. Beware that when the first message is processed      * then creating and starting the producer may take a little time and prolong the total processing time of the processing.      */
DECL|method|setLazyStartProducer (boolean lazyStartProducer)
name|void
name|setLazyStartProducer
parameter_list|(
name|boolean
name|lazyStartProducer
parameter_list|)
function_decl|;
DECL|method|isBridgeErrorHandler ()
name|boolean
name|isBridgeErrorHandler
parameter_list|()
function_decl|;
comment|/**      * Allows for bridging the consumer to the Camel routing Error Handler, which mean any exceptions occurred while      * the consumer is trying to pickup incoming messages, or the likes, will now be processed as a message and      * handled by the routing Error Handler.      *<p/>      * By default the consumer will use the org.apache.camel.spi.ExceptionHandler to deal with exceptions,      * that will be logged at WARN/ERROR level and ignored.      */
DECL|method|setBridgeErrorHandler (boolean bridgeErrorHandler)
name|void
name|setBridgeErrorHandler
parameter_list|(
name|boolean
name|bridgeErrorHandler
parameter_list|)
function_decl|;
DECL|method|isBasicPropertyBinding ()
name|boolean
name|isBasicPropertyBinding
parameter_list|()
function_decl|;
comment|/**      * Whether the endpoint should use basic property binding (Camel 2.x) or the newer property binding with additional capabilities.      */
DECL|method|setBasicPropertyBinding (boolean basicPropertyBinding)
name|void
name|setBasicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

