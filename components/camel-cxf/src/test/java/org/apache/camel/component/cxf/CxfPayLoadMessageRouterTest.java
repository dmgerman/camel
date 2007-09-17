begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_class
DECL|class|CxfPayLoadMessageRouterTest
specifier|public
class|class
name|CxfPayLoadMessageRouterTest
extends|extends
name|CxfRouterTest
block|{
DECL|field|routerEndpointURI
specifier|private
name|String
name|routerEndpointURI
init|=
literal|"cxf://"
operator|+
name|ROUTER_ADDRESS
operator|+
literal|"?"
operator|+
name|SERVICE_CLASS
operator|+
literal|"&dataFormat=PAYLOAD"
decl_stmt|;
DECL|field|serviceEndpointURI
specifier|private
name|String
name|serviceEndpointURI
init|=
literal|"cxf://"
operator|+
name|SERVICE_ADDRESS
operator|+
literal|"?"
operator|+
name|SERVICE_CLASS
operator|+
literal|"&dataFormat=PAYLOAD"
decl_stmt|;
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
name|routerEndpointURI
argument_list|)
operator|.
name|to
argument_list|(
name|serviceEndpointURI
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

