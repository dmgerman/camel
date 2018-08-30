begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.async
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|async
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_class
DECL|class|AsyncEndpointDynamicRouterTest
specifier|public
class|class
name|AsyncEndpointDynamicRouterTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|invoked
specifier|private
specifier|static
name|int
name|invoked
decl_stmt|;
DECL|field|bodies
specifier|private
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|bodies
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testAsyncEndpoint ()
specifier|public
name|void
name|testAsyncEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Bye World"
argument_list|)
expr_stmt|;
name|String
name|reply
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello Camel"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|invoked
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|bodies
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Camel"
argument_list|,
name|bodies
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye Camel"
argument_list|,
name|bodies
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|bodies
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|bodies
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
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
name|context
operator|.
name|addComponent
argument_list|(
literal|"async"
argument_list|,
operator|new
name|MyAsyncComponent
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|dynamicRouter
argument_list|(
name|method
argument_list|(
name|AsyncEndpointDynamicRouterTest
operator|.
name|class
argument_list|,
literal|"slip"
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"Bye World"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
comment|/**      * Use this method to compute dynamic where we should route next.      *      * @param body the message body      * @return endpoints to go, or<tt>null</tt> to indicate the end      */
DECL|method|slip (String body)
specifier|public
name|String
name|slip
parameter_list|(
name|String
name|body
parameter_list|)
block|{
name|bodies
operator|.
name|add
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|invoked
operator|++
expr_stmt|;
if|if
condition|(
name|invoked
operator|==
literal|1
condition|)
block|{
return|return
literal|"async:bye:camel"
return|;
block|}
elseif|else
if|if
condition|(
name|invoked
operator|==
literal|2
condition|)
block|{
return|return
literal|"direct:foo"
return|;
block|}
elseif|else
if|if
condition|(
name|invoked
operator|==
literal|3
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
block|}
end_class

end_unit

