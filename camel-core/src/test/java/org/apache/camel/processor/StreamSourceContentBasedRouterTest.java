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
name|Before
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

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
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
name|StringSource
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
comment|/**  * Test for handling a StreamSource in a content-based router with XPath predicates  *  * @version   */
end_comment

begin_class
DECL|class|StreamSourceContentBasedRouterTest
specifier|public
class|class
name|StreamSourceContentBasedRouterTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|x
specifier|protected
name|MockEndpoint
name|x
decl_stmt|;
DECL|field|y
specifier|protected
name|MockEndpoint
name|y
decl_stmt|;
annotation|@
name|Test
DECL|method|testSendStreamSource ()
specifier|public
name|void
name|testSendStreamSource
parameter_list|()
throws|throws
name|Exception
block|{
name|x
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|y
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|StreamSource
argument_list|(
operator|new
name|StringReader
argument_list|(
literal|"<message>xx</message>"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|StreamSource
argument_list|(
operator|new
name|StringReader
argument_list|(
literal|"<message>yy</message>"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendStringSource ()
specifier|public
name|void
name|testSendStringSource
parameter_list|()
throws|throws
name|Exception
block|{
name|x
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|y
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|StringSource
argument_list|(
literal|"<message>xx</message>"
argument_list|)
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
operator|new
name|StringSource
argument_list|(
literal|"<message>yy</message>"
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|x
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:x"
argument_list|)
expr_stmt|;
name|y
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:y"
argument_list|)
expr_stmt|;
block|}
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
comment|// should work with default error handler as the stream cache
comment|// is enabled and make sure the predicates can be evaluated
comment|// multiple times
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|streamCaching
argument_list|()
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|()
operator|.
name|xpath
argument_list|(
literal|"/message/text() = 'xx'"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:x"
argument_list|)
operator|.
name|when
argument_list|()
operator|.
name|xpath
argument_list|(
literal|"/message/text() = 'yy'"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:y"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

