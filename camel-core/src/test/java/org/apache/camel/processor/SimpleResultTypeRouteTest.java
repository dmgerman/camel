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
name|mock
operator|.
name|MockEndpoint
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|SimpleResultTypeRouteTest
specifier|public
class|class
name|SimpleResultTypeRouteTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testSimpleResultTypeFoo ()
specifier|public
name|void
name|testSimpleResultTypeFoo
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// cool header is a boolean
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"cool"
argument_list|)
operator|.
name|isInstanceOf
argument_list|(
name|Boolean
operator|.
name|class
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"cool"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// fail header is not a boolean
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"fail"
argument_list|)
operator|.
name|isInstanceOf
argument_list|(
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"fail"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"true"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:foo"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSimpleResultTypeBar ()
specifier|public
name|void
name|testSimpleResultTypeBar
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:bar"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
comment|// cool header is a boolean
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"cool"
argument_list|)
operator|.
name|isInstanceOf
argument_list|(
name|Boolean
operator|.
name|class
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"cool"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// fail header is not a boolean
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"fail"
argument_list|)
operator|.
name|isInstanceOf
argument_list|(
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"fail"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"true"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:bar"
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
name|from
argument_list|(
literal|"direct:foo"
argument_list|)
comment|// set using builder support
operator|.
name|setHeader
argument_list|(
literal|"cool"
argument_list|,
name|simple
argument_list|(
literal|"true"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"fail"
argument_list|,
name|simple
argument_list|(
literal|"true"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:bar"
argument_list|)
comment|// set using expression clause
operator|.
name|setHeader
argument_list|(
literal|"cool"
argument_list|)
operator|.
name|simple
argument_list|(
literal|"true"
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"fail"
argument_list|,
name|simple
argument_list|(
literal|"true"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:bar"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

