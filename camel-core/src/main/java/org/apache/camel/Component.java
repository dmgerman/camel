begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * A<a href="http://camel.apache.org/component.html">component</a> is  * a factory of {@link Endpoint} objects.  *   * @version   */
end_comment

begin_interface
DECL|interface|Component
specifier|public
interface|interface
name|Component
extends|extends
name|CamelContextAware
block|{
comment|/**      * Attempt to resolve an endpoint for the given URI if the component is      * capable of handling the URI      *       * @param uri the URI to create      * @return a newly created {@link Endpoint} or null if this component cannot create      *         {@link Endpoint} instances using the given uri      * @throws Exception is thrown if error creating the endpoint      */
DECL|method|createEndpoint (String uri)
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Attempt to create a configuration object from the give uri      *       * @param uri the configuration URI      * @return a newly created {@link EndpointConfiguration}      * @throws Exception is thrown if the configuration URI is invalid      */
DECL|method|createConfiguration (String uri)
name|EndpointConfiguration
name|createConfiguration
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

