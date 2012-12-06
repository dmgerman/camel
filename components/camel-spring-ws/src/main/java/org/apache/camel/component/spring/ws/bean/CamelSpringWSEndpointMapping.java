begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.ws.bean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spring
operator|.
name|ws
operator|.
name|bean
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
name|component
operator|.
name|spring
operator|.
name|ws
operator|.
name|type
operator|.
name|EndpointMappingKey
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|server
operator|.
name|EndpointMapping
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|ws
operator|.
name|server
operator|.
name|endpoint
operator|.
name|MessageEndpoint
import|;
end_import

begin_comment
comment|/**  * Allows to register different spring-ws endpoints for camel.  *   * @author azachar  */
end_comment

begin_interface
DECL|interface|CamelSpringWSEndpointMapping
specifier|public
interface|interface
name|CamelSpringWSEndpointMapping
extends|extends
name|EndpointMapping
block|{
comment|/**      * Used by Camel Spring Web Services endpoint to register consumers      *       * @param key unique consumer key      * @param endpoint consumer      */
DECL|method|addConsumer (EndpointMappingKey key, MessageEndpoint endpoint)
name|void
name|addConsumer
parameter_list|(
name|EndpointMappingKey
name|key
parameter_list|,
name|MessageEndpoint
name|endpoint
parameter_list|)
function_decl|;
comment|/**      * Used by Camel Spring Web Services endpoint to unregister consumers      *       * @param key unique consumer key      */
DECL|method|removeConsumer (Object key)
name|void
name|removeConsumer
parameter_list|(
name|Object
name|key
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

