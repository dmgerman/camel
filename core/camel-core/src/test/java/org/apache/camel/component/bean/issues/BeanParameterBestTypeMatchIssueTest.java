begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.bean.issues
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|bean
operator|.
name|issues
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|BeanParameterBestTypeMatchIssueTest
specifier|public
class|class
name|BeanParameterBestTypeMatchIssueTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testNoParam ()
specifier|public
name|void
name|testNoParam
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:end"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"A"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:noParam"
argument_list|,
literal|"body"
argument_list|,
literal|"key"
argument_list|,
literal|"value"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|test1Param ()
specifier|public
name|void
name|test1Param
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:end"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"B"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:1Param"
argument_list|,
literal|"body"
argument_list|,
literal|"key"
argument_list|,
literal|"value"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|test2ParamString ()
specifier|public
name|void
name|test2ParamString
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:end"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"C"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:2Param"
argument_list|,
literal|"body"
argument_list|,
literal|"key"
argument_list|,
literal|"value"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|test2ParamClassB ()
specifier|public
name|void
name|test2ParamClassB
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:end"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"D"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:2Param"
argument_list|,
literal|"body"
argument_list|,
literal|"key"
argument_list|,
operator|new
name|ClassB
argument_list|()
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|test2ParamBoolBody ()
specifier|public
name|void
name|test2ParamBoolBody
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:end"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"E"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:2Param"
argument_list|,
literal|true
argument_list|,
literal|"key"
argument_list|,
literal|"value"
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
literal|"direct:noParam"
argument_list|)
operator|.
name|bean
argument_list|(
name|ClassA
operator|.
name|class
argument_list|,
literal|"foo()"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:end"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:1Param"
argument_list|)
operator|.
name|bean
argument_list|(
name|ClassA
operator|.
name|class
argument_list|,
literal|"foo(${body})"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:end"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:2Param"
argument_list|)
operator|.
name|bean
argument_list|(
name|ClassA
operator|.
name|class
argument_list|,
literal|"foo(${body}, ${header.key})"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:end"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

