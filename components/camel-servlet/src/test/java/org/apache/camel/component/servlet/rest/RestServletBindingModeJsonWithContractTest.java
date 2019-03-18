begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.servlet.rest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servlet
operator|.
name|rest
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|com
operator|.
name|meterware
operator|.
name|httpunit
operator|.
name|PostMethodWebRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|meterware
operator|.
name|httpunit
operator|.
name|WebRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|meterware
operator|.
name|httpunit
operator|.
name|WebResponse
import|;
end_import

begin_import
import|import
name|com
operator|.
name|meterware
operator|.
name|servletunit
operator|.
name|ServletUnitClient
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
name|Converter
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
name|TypeConverters
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
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servlet
operator|.
name|ServletCamelRouterTestSupport
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

begin_class
DECL|class|RestServletBindingModeJsonWithContractTest
specifier|public
class|class
name|RestServletBindingModeJsonWithContractTest
extends|extends
name|ServletCamelRouterTestSupport
block|{
annotation|@
name|Test
DECL|method|testBindingModeJsonWithContract ()
specifier|public
name|void
name|testBindingModeJsonWithContract
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
name|UserPojoEx
operator|.
name|class
argument_list|)
expr_stmt|;
name|String
name|body
init|=
literal|"{\"id\": 123, \"name\": \"Donald Duck\"}"
decl_stmt|;
name|WebRequest
name|req
init|=
operator|new
name|PostMethodWebRequest
argument_list|(
name|CONTEXT_URL
operator|+
literal|"/services/users/new"
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|body
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|,
literal|"application/json"
argument_list|)
decl_stmt|;
name|ServletUnitClient
name|client
init|=
name|newClient
argument_list|()
decl_stmt|;
name|client
operator|.
name|setExceptionsThrownOnErrorStatus
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|WebResponse
name|response
init|=
name|client
operator|.
name|getResponse
argument_list|(
name|req
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|response
operator|.
name|getResponseCode
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|answer
init|=
name|response
operator|.
name|getText
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Unexpected response: "
operator|+
name|answer
argument_list|,
name|answer
operator|.
name|contains
argument_list|(
literal|"\"active\":true"
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Object
name|obj
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
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|UserPojoEx
operator|.
name|class
argument_list|,
name|obj
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|UserPojoEx
name|user
init|=
operator|(
name|UserPojoEx
operator|)
name|obj
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
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|user
operator|.
name|isActive
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
name|context
operator|.
name|getTypeConverterRegistry
argument_list|()
operator|.
name|addTypeConverters
argument_list|(
operator|new
name|MyTypeConverters
argument_list|()
argument_list|)
expr_stmt|;
name|restConfiguration
argument_list|()
operator|.
name|component
argument_list|(
literal|"servlet"
argument_list|)
operator|.
name|bindingMode
argument_list|(
name|RestBindingMode
operator|.
name|json
argument_list|)
expr_stmt|;
name|rest
argument_list|(
literal|"/users/"
argument_list|)
comment|// REST binding converts from JSON to UserPojo
operator|.
name|post
argument_list|(
literal|"new"
argument_list|)
operator|.
name|type
argument_list|(
name|UserPojo
operator|.
name|class
argument_list|)
operator|.
name|route
argument_list|()
comment|// then contract advice converts from UserPojo to UserPojoEx
operator|.
name|inputType
argument_list|(
name|UserPojoEx
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
DECL|class|MyTypeConverters
specifier|public
specifier|static
class|class
name|MyTypeConverters
implements|implements
name|TypeConverters
block|{
annotation|@
name|Converter
DECL|method|toEx (UserPojo user)
specifier|public
name|UserPojoEx
name|toEx
parameter_list|(
name|UserPojo
name|user
parameter_list|)
block|{
name|UserPojoEx
name|ex
init|=
operator|new
name|UserPojoEx
argument_list|()
decl_stmt|;
name|ex
operator|.
name|setId
argument_list|(
name|user
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|ex
operator|.
name|setName
argument_list|(
name|user
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|ex
operator|.
name|setActive
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|ex
return|;
block|}
block|}
block|}
end_class

end_unit

