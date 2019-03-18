begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
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
DECL|class|RestRestletBindingModeJsonWithContractTest
specifier|public
class|class
name|RestRestletBindingModeJsonWithContractTest
extends|extends
name|RestletTestSupport
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
name|Object
name|answer
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"http://localhost:"
operator|+
name|portNum
operator|+
literal|"/users/new"
argument_list|,
name|body
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|answer
argument_list|)
expr_stmt|;
name|BufferedReader
name|reader
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
operator|(
name|InputStream
operator|)
name|answer
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|line
decl_stmt|;
name|String
name|answerString
init|=
literal|""
decl_stmt|;
while|while
condition|(
operator|(
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|answerString
operator|+=
name|line
expr_stmt|;
block|}
name|assertTrue
argument_list|(
literal|"Unexpected response: "
operator|+
name|answerString
argument_list|,
name|answerString
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

