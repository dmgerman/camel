begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.jsonpath
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|jsonpath
package|;
end_package

begin_import
import|import
name|com
operator|.
name|jayway
operator|.
name|jsonpath
operator|.
name|Option
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
name|test
operator|.
name|junit5
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|JsonPathBeanTest
specifier|public
class|class
name|JsonPathBeanTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testFullName ()
specifier|public
name|void
name|testFullName
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|json
init|=
literal|"{\"person\" : {\"firstname\" : \"foo\", \"middlename\" : \"foo2\", \"lastname\" : \"bar\"}}"
decl_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"foo foo2 bar"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|json
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFullNameTwo ()
specifier|public
name|void
name|testFullNameTwo
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|json
init|=
literal|"{\"person\" : {\"firstname\" : \"foo\", \"middlename\" : \"foo2\", \"lastname\" : \"bar\"}}"
decl_stmt|;
name|String
name|json2
init|=
literal|"{\"person\" : {\"firstname\" : \"bar\", \"middlename\" : \"bar2\", \"lastname\" : \"foo\"}}"
decl_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"foo foo2 bar"
argument_list|,
literal|"bar bar2 foo"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|json
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|json2
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFirstAndLastName ()
specifier|public
name|void
name|testFirstAndLastName
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|json
init|=
literal|"{\"person\" : {\"firstname\" : \"foo\", \"lastname\" : \"bar\"}}"
decl_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"foo bar"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|json
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
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|bean
argument_list|(
name|FullNameBean
operator|.
name|class
argument_list|)
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
DECL|class|FullNameBean
specifier|protected
specifier|static
class|class
name|FullNameBean
block|{
comment|// middle name is optional
DECL|method|getName (@sonPathR) String first, @JsonPath(value = R, options = Option.SUPPRESS_EXCEPTIONS) String middle, @JsonPath(R) String last)
specifier|public
specifier|static
name|String
name|getName
parameter_list|(
annotation|@
name|JsonPath
argument_list|(
literal|"person.firstname"
argument_list|)
name|String
name|first
parameter_list|,
annotation|@
name|JsonPath
argument_list|(
name|value
operator|=
literal|"person.middlename"
argument_list|,
name|options
operator|=
name|Option
operator|.
name|SUPPRESS_EXCEPTIONS
argument_list|)
name|String
name|middle
parameter_list|,
annotation|@
name|JsonPath
argument_list|(
literal|"person.lastname"
argument_list|)
name|String
name|last
parameter_list|)
block|{
if|if
condition|(
name|middle
operator|!=
literal|null
condition|)
block|{
return|return
name|first
operator|+
literal|" "
operator|+
name|middle
operator|+
literal|" "
operator|+
name|last
return|;
block|}
return|return
name|first
operator|+
literal|" "
operator|+
name|last
return|;
block|}
block|}
block|}
end_class

end_unit

