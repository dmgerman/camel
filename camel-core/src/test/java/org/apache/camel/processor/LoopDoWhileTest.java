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
name|builder
operator|.
name|RouteBuilder
import|;
end_import

begin_class
DECL|class|LoopDoWhileTest
specifier|public
class|class
name|LoopDoWhileTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testLoopDoWhileSimple ()
specifier|public
name|void
name|testLoopDoWhileSimple
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
literal|"AAAAAA"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:loop"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"A"
argument_list|,
literal|"AA"
argument_list|,
literal|"AAA"
argument_list|,
literal|"AAAA"
argument_list|,
literal|"AAAAA"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:simple"
argument_list|,
literal|"A"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|testLoopDoWhileFunctional ()
specifier|public
name|void
name|testLoopDoWhileFunctional
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
literal|"AAAAAA"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:loop"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"A"
argument_list|,
literal|"AA"
argument_list|,
literal|"AAA"
argument_list|,
literal|"AAAA"
argument_list|,
literal|"AAAAA"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:functional"
argument_list|,
literal|"A"
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
name|from
argument_list|(
literal|"direct:simple"
argument_list|)
operator|.
name|loopDoWhile
argument_list|(
name|simple
argument_list|(
literal|"${body.length}<= 5"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:loop"
argument_list|)
operator|.
name|transform
argument_list|(
name|body
argument_list|()
operator|.
name|append
argument_list|(
literal|"A"
argument_list|)
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:functional"
argument_list|)
operator|.
name|loopDoWhile
argument_list|()
operator|.
name|body
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|b
lambda|->
name|b
operator|.
name|length
argument_list|()
operator|<=
literal|5
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:loop"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|body
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|b
lambda|->
name|b
operator|+=
literal|"A"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

