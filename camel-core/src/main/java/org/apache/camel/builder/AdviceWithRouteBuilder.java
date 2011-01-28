begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
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
name|impl
operator|.
name|InterceptSendToMockEndpointStrategy
import|;
end_import

begin_comment
comment|/**  * A {@link RouteBuilder} which has extended features when using  * {@link org.apache.camel.model.RouteDefinition#adviceWith(org.apache.camel.CamelContext, RouteBuilder) adviceWith}.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|AdviceWithRouteBuilder
specifier|public
specifier|abstract
class|class
name|AdviceWithRouteBuilder
extends|extends
name|RouteBuilder
block|{
comment|/**      * Mock all endpoints in the route.      */
DECL|method|mockEndpoints ()
specifier|public
name|void
name|mockEndpoints
parameter_list|()
throws|throws
name|Exception
block|{
name|getContext
argument_list|()
operator|.
name|removeEndpoints
argument_list|(
literal|"*"
argument_list|)
expr_stmt|;
name|getContext
argument_list|()
operator|.
name|addRegisterEndpointCallback
argument_list|(
operator|new
name|InterceptSendToMockEndpointStrategy
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Mock all endpoints matching the given pattern.      *      * @param pattern  the pattern.      * @see org.apache.camel.util.EndpointHelper#matchEndpoint(String, String)      */
DECL|method|mockEndpoints (String pattern)
specifier|public
name|void
name|mockEndpoints
parameter_list|(
name|String
name|pattern
parameter_list|)
throws|throws
name|Exception
block|{
name|getContext
argument_list|()
operator|.
name|removeEndpoints
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
name|getContext
argument_list|()
operator|.
name|addRegisterEndpointCallback
argument_list|(
operator|new
name|InterceptSendToMockEndpointStrategy
argument_list|(
name|pattern
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

