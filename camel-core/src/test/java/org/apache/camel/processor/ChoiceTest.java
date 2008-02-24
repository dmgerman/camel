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

begin_import
import|import static
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
operator|.
name|assertIsSatisfied
import|;
end_import

begin_import
import|import static
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
operator|.
name|expectsMessageCount
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|ChoiceTest
specifier|public
class|class
name|ChoiceTest
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
DECL|field|z
specifier|protected
name|MockEndpoint
name|z
decl_stmt|;
DECL|method|testSendToFirstWhen ()
specifier|public
name|void
name|testSendToFirstWhen
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|body
init|=
literal|"<one/>"
decl_stmt|;
name|x
operator|.
name|expectedBodiesReceived
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|expectsMessageCount
argument_list|(
literal|0
argument_list|,
name|y
argument_list|,
name|z
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"bar"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisifed
argument_list|()
expr_stmt|;
block|}
DECL|method|testSendToSecondWhen ()
specifier|public
name|void
name|testSendToSecondWhen
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|body
init|=
literal|"<two/>"
decl_stmt|;
name|y
operator|.
name|expectedBodiesReceived
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|expectsMessageCount
argument_list|(
literal|0
argument_list|,
name|x
argument_list|,
name|z
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"cheese"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisifed
argument_list|()
expr_stmt|;
block|}
DECL|method|testSendToOtherwiseClause ()
specifier|public
name|void
name|testSendToOtherwiseClause
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|body
init|=
literal|"<three/>"
decl_stmt|;
name|z
operator|.
name|expectedBodiesReceived
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|expectsMessageCount
argument_list|(
literal|0
argument_list|,
name|x
argument_list|,
name|y
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"somethingUndefined"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisifed
argument_list|()
expr_stmt|;
block|}
DECL|method|sendMessage (final Object headerValue, final Object body)
specifier|protected
name|void
name|sendMessage
parameter_list|(
specifier|final
name|Object
name|headerValue
parameter_list|,
specifier|final
name|Object
name|body
parameter_list|)
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
name|body
argument_list|,
literal|"foo"
argument_list|,
name|headerValue
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
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
name|z
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:z"
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
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|header
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"name"
argument_list|,
name|constant
argument_list|(
literal|"a"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:x"
argument_list|)
operator|.
name|when
argument_list|(
name|header
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"cheese"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:y"
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:z"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

