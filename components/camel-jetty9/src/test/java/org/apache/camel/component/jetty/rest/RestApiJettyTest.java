begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jetty.rest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jetty
operator|.
name|rest
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
name|jetty
operator|.
name|BaseJettyTest
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
name|RestParamType
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
DECL|class|RestApiJettyTest
specifier|public
class|class
name|RestApiJettyTest
extends|extends
name|BaseJettyTest
block|{
annotation|@
name|Override
DECL|method|useJmx ()
specifier|protected
name|boolean
name|useJmx
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Test
DECL|method|testApi ()
specifier|public
name|void
name|testApi
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"jetty:http://localhost:{{port}}/api-doc"
argument_list|,
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|out
operator|.
name|contains
argument_list|(
literal|"\"version\" : \"1.2.3\""
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|out
operator|.
name|contains
argument_list|(
literal|"\"title\" : \"The hello rest thing\""
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|out
operator|.
name|contains
argument_list|(
literal|"\"/hello/bye/{name}\""
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|out
operator|.
name|contains
argument_list|(
literal|"\"/hello/hi/{name}\""
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|out
operator|.
name|contains
argument_list|(
literal|"\"summary\" : \"To update the greeting message\""
argument_list|)
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
literal|"jetty"
argument_list|)
operator|.
name|host
argument_list|(
literal|"localhost"
argument_list|)
operator|.
name|port
argument_list|(
name|getPort
argument_list|()
argument_list|)
operator|.
name|apiContextPath
argument_list|(
literal|"/api-doc"
argument_list|)
operator|.
name|apiProperty
argument_list|(
literal|"cors"
argument_list|,
literal|"true"
argument_list|)
operator|.
name|apiProperty
argument_list|(
literal|"api.title"
argument_list|,
literal|"The hello rest thing"
argument_list|)
operator|.
name|apiProperty
argument_list|(
literal|"api.version"
argument_list|,
literal|"1.2.3"
argument_list|)
expr_stmt|;
name|rest
argument_list|(
literal|"/hello"
argument_list|)
operator|.
name|consumes
argument_list|(
literal|"application/json"
argument_list|)
operator|.
name|produces
argument_list|(
literal|"application/json"
argument_list|)
operator|.
name|get
argument_list|(
literal|"/hi/{name}"
argument_list|)
operator|.
name|description
argument_list|(
literal|"Saying hi"
argument_list|)
operator|.
name|param
argument_list|()
operator|.
name|name
argument_list|(
literal|"name"
argument_list|)
operator|.
name|type
argument_list|(
name|RestParamType
operator|.
name|path
argument_list|)
operator|.
name|dataType
argument_list|(
literal|"string"
argument_list|)
operator|.
name|description
argument_list|(
literal|"Who is it"
argument_list|)
operator|.
name|endParam
argument_list|()
operator|.
name|to
argument_list|(
literal|"log:hi"
argument_list|)
operator|.
name|get
argument_list|(
literal|"/bye/{name}"
argument_list|)
operator|.
name|description
argument_list|(
literal|"Saying bye"
argument_list|)
operator|.
name|param
argument_list|()
operator|.
name|name
argument_list|(
literal|"name"
argument_list|)
operator|.
name|type
argument_list|(
name|RestParamType
operator|.
name|path
argument_list|)
operator|.
name|dataType
argument_list|(
literal|"string"
argument_list|)
operator|.
name|description
argument_list|(
literal|"Who is it"
argument_list|)
operator|.
name|endParam
argument_list|()
operator|.
name|responseMessage
argument_list|()
operator|.
name|code
argument_list|(
literal|200
argument_list|)
operator|.
name|message
argument_list|(
literal|"A reply message"
argument_list|)
operator|.
name|endResponseMessage
argument_list|()
operator|.
name|to
argument_list|(
literal|"log:bye"
argument_list|)
operator|.
name|post
argument_list|(
literal|"/bye"
argument_list|)
operator|.
name|description
argument_list|(
literal|"To update the greeting message"
argument_list|)
operator|.
name|consumes
argument_list|(
literal|"application/xml"
argument_list|)
operator|.
name|produces
argument_list|(
literal|"application/xml"
argument_list|)
operator|.
name|param
argument_list|()
operator|.
name|name
argument_list|(
literal|"greeting"
argument_list|)
operator|.
name|type
argument_list|(
name|RestParamType
operator|.
name|body
argument_list|)
operator|.
name|dataType
argument_list|(
literal|"string"
argument_list|)
operator|.
name|description
argument_list|(
literal|"Message to use as greeting"
argument_list|)
operator|.
name|endParam
argument_list|()
operator|.
name|to
argument_list|(
literal|"log:bye"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

