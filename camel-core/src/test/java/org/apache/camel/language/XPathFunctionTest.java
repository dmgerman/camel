begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.language
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|language
package|;
end_package

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
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|NodeList
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|XPathFunctionTest
specifier|public
class|class
name|XPathFunctionTest
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
DECL|field|end
specifier|protected
name|MockEndpoint
name|end
decl_stmt|;
annotation|@
name|Test
DECL|method|testCheckHeader ()
specifier|public
name|void
name|testCheckHeader
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
comment|// The SpringChoiceTest.java can't setup the header by Spring configure file
comment|// x.expectedHeaderReceived("name", "a");
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
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCheckBody ()
specifier|public
name|void
name|testCheckBody
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
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSetXpathProperty ()
specifier|public
name|void
name|testSetXpathProperty
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|body
init|=
literal|"<soapenv:Body xmlns:ns=\"http://myNamesapce\" xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">"
operator|+
literal|"<ns:Addresses><Address>address1</Address>"
operator|+
literal|"<Address>address2</Address><Address>address3</Address>"
operator|+
literal|"<Address>address4</Address></ns:Addresses></soapenv:Body>"
decl_stmt|;
name|end
operator|.
name|reset
argument_list|()
expr_stmt|;
name|end
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:setProperty"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Exchange
name|exchange
init|=
name|end
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|NodeList
name|nodeList
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
literal|"Addresses"
argument_list|,
name|NodeList
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"The node list should not be null"
argument_list|,
name|nodeList
argument_list|)
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
name|z
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:z"
argument_list|)
expr_stmt|;
name|end
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:end"
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
comment|// START SNIPPET: ex
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|()
operator|.
name|xpath
argument_list|(
literal|"in:header('foo') = 'bar'"
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
literal|"in:body() = '<two/>'"
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
comment|// END SNIPPET: ex
name|from
argument_list|(
literal|"direct:setProperty"
argument_list|)
operator|.
name|setProperty
argument_list|(
literal|"Addresses"
argument_list|)
operator|.
name|xpath
argument_list|(
literal|"//Address"
argument_list|,
name|NodeList
operator|.
name|class
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

