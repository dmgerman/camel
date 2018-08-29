begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.seda
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|seda
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|SedaInOutChainedTest
specifier|public
class|class
name|SedaInOutChainedTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testInOutSedaChained ()
specifier|public
name|void
name|testInOutSedaChained
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
literal|"start"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"start-a"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:c"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"start-a-b"
argument_list|)
expr_stmt|;
name|String
name|reply
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"seda:a"
argument_list|,
literal|"start"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"start-a-b-c"
argument_list|,
name|reply
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
literal|"seda:a"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|transform
argument_list|(
name|simple
argument_list|(
literal|"${body}-a"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:b"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:b"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|transform
argument_list|(
name|simple
argument_list|(
literal|"${body}-b"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:c"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:c"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:c"
argument_list|)
operator|.
name|transform
argument_list|(
name|simple
argument_list|(
literal|"${body}-c"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

