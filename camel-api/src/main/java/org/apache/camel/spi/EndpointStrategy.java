begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
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

begin_comment
comment|/**  * Endpoint strategy with callback invoked when an {@link org.apache.camel.Endpoint} is about to be registered to the  * endpoint registry in {@link org.apache.camel.CamelContext}. This callback allows you to intervene  * and return a mixed in {@link org.apache.camel.Endpoint}.  *<p/>  * The {@link org.apache.camel.model.InterceptSendToEndpointDefinition} uses this to allow it to proxy  * endpoints so it can intercept sending to the given endpoint.  */
end_comment

begin_interface
DECL|interface|EndpointStrategy
specifier|public
interface|interface
name|EndpointStrategy
block|{
comment|/**      * Register the endpoint.      *      * @param uri  uri of endpoint      * @param endpoint the current endpoint to register      * @return the real endpoint to register, for instance a wrapped/enhanced endpoint.      */
DECL|method|registerEndpoint (String uri, Endpoint endpoint)
name|Endpoint
name|registerEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

