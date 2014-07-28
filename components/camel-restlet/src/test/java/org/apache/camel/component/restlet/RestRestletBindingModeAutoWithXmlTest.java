begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.restlet
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|restlet
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
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|rest
operator|.
name|RestBindingMode
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|RestRestletBindingModeAutoWithXmlTest
specifier|public
class|class
name|RestRestletBindingModeAutoWithXmlTest
extends|extends
name|RestletTestSupport
block|{
annotation|@
name|Test
DECL|method|testBindingMode ()
specifier|public
name|void
name|testBindingMode
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:input"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|UserJaxbPojo
operator|.
name|class
argument_list|)
expr_stmt|;
name|String
name|body
init|=
literal|"<user name=\"Donald Duck\" id=\"123\"></user>"
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"http://localhost:"
operator|+
name|portNum
operator|+
literal|"/users/new"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|UserJaxbPojo
name|user
init|=
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|UserJaxbPojo
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|user
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|user
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Donald Duck"
argument_list|,
name|user
operator|.
name|getName
argument_list|()
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
name|restConfiguration
argument_list|()
operator|.
name|component
argument_list|(
literal|"restlet"
argument_list|)
operator|.
name|host
argument_list|(
literal|"localhost"
argument_list|)
operator|.
name|port
argument_list|(
name|portNum
argument_list|)
operator|.
name|bindingMode
argument_list|(
name|RestBindingMode
operator|.
name|auto
argument_list|)
expr_stmt|;
comment|// use the rest DSL to define the rest services
name|rest
argument_list|(
literal|"/users/"
argument_list|)
operator|.
name|post
argument_list|(
literal|"new"
argument_list|)
operator|.
name|consumes
argument_list|(
literal|"application/xml"
argument_list|)
operator|.
name|type
argument_list|(
name|UserJaxbPojo
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:input"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

