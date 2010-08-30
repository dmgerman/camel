begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|ContextTestSupport
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
name|Header
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|DynamicRouter2Test
specifier|public
class|class
name|DynamicRouter2Test
extends|extends
name|ContextTestSupport
block|{
DECL|method|testDynamicRouter ()
specifier|public
name|void
name|testDynamicRouter
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: e1
name|from
argument_list|(
literal|"direct:start"
argument_list|)
comment|// use a bean as the dynamic router
operator|.
name|dynamicRouter
argument_list|()
operator|.
name|method
argument_list|(
name|DynamicRouter2Test
operator|.
name|class
argument_list|,
literal|"slip"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
block|}
block|}
return|;
block|}
comment|// START SNIPPET: e2
comment|/**      * Use this method to compute dynamic where we should route next.      *      * @param body the message body      * @param previous the previous slip      * @return endpoints to go, or<tt>null</tt> to indicate the end      */
DECL|method|slip (String body, @Header(Exchange.SLIP_ENDPOINT) String previous)
specifier|public
name|String
name|slip
parameter_list|(
name|String
name|body
parameter_list|,
annotation|@
name|Header
argument_list|(
name|Exchange
operator|.
name|SLIP_ENDPOINT
argument_list|)
name|String
name|previous
parameter_list|)
block|{
if|if
condition|(
name|previous
operator|==
literal|null
condition|)
block|{
return|return
literal|"mock:a"
return|;
block|}
elseif|else
if|if
condition|(
literal|"mock://a"
operator|.
name|equals
argument_list|(
name|previous
argument_list|)
condition|)
block|{
return|return
literal|"mock:b"
return|;
block|}
elseif|else
if|if
condition|(
literal|"mock://b"
operator|.
name|equals
argument_list|(
name|previous
argument_list|)
condition|)
block|{
return|return
literal|"mock:result"
return|;
block|}
comment|// no more so return null
return|return
literal|null
return|;
block|}
comment|// END SNIPPET: e2
block|}
end_class

end_unit

