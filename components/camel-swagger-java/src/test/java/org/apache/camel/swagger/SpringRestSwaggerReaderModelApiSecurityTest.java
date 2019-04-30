begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.swagger
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|swagger
package|;
end_package

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|annotation
operator|.
name|JsonInclude
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectMapper
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|SerializationFeature
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|jaxrs
operator|.
name|config
operator|.
name|BeanConfig
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|models
operator|.
name|Swagger
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
name|impl
operator|.
name|engine
operator|.
name|DefaultClassResolver
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
name|spring
operator|.
name|CamelSpringTestSupport
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
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_class
DECL|class|SpringRestSwaggerReaderModelApiSecurityTest
specifier|public
class|class
name|SpringRestSwaggerReaderModelApiSecurityTest
extends|extends
name|CamelSpringTestSupport
block|{
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/swagger/SpringRestSwaggerReaderModelApiSecurityTest.xml"
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testReaderRead ()
specifier|public
name|void
name|testReaderRead
parameter_list|()
throws|throws
name|Exception
block|{
name|BeanConfig
name|config
init|=
operator|new
name|BeanConfig
argument_list|()
decl_stmt|;
name|config
operator|.
name|setHost
argument_list|(
literal|"localhost:8080"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setSchemes
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"http"
block|}
argument_list|)
expr_stmt|;
name|config
operator|.
name|setBasePath
argument_list|(
literal|"/api"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setTitle
argument_list|(
literal|"Camel User store"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setLicense
argument_list|(
literal|"Apache 2.0"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setLicenseUrl
argument_list|(
literal|"http://www.apache.org/licenses/LICENSE-2.0.html"
argument_list|)
expr_stmt|;
name|RestSwaggerReader
name|reader
init|=
operator|new
name|RestSwaggerReader
argument_list|()
decl_stmt|;
name|Swagger
name|swagger
init|=
name|reader
operator|.
name|read
argument_list|(
name|context
operator|.
name|getRestDefinitions
argument_list|()
argument_list|,
literal|null
argument_list|,
name|config
argument_list|,
name|context
operator|.
name|getName
argument_list|()
argument_list|,
operator|new
name|DefaultClassResolver
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|swagger
argument_list|)
expr_stmt|;
name|ObjectMapper
name|mapper
init|=
operator|new
name|ObjectMapper
argument_list|()
decl_stmt|;
name|mapper
operator|.
name|enable
argument_list|(
name|SerializationFeature
operator|.
name|INDENT_OUTPUT
argument_list|)
expr_stmt|;
name|mapper
operator|.
name|setSerializationInclusion
argument_list|(
name|JsonInclude
operator|.
name|Include
operator|.
name|NON_NULL
argument_list|)
expr_stmt|;
name|String
name|json
init|=
name|mapper
operator|.
name|writeValueAsString
argument_list|(
name|swagger
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
name|json
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"\"securityDefinitions\" : {"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"\"type\" : \"oauth2\","
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"\"authorizationUrl\" : \"http://petstore.swagger.io/oauth/dialog\","
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"\"flow\" : \"implicit\""
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"\"type\" : \"apiKey\","
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"\"in\" : \"header\""
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"\"host\" : \"localhost:8080\""
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"\"security\" : [ {"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"\"petstore_auth\" : [ \"write:pets\", \"read:pets\" ]"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"\"api_key\" : [ ]"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"\"description\" : \"The user returned\""
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"\"$ref\" : \"#/definitions/User\""
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"\"x-className\""
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"\"format\" : \"org.apache.camel.swagger.User\""
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"\"type\" : \"string\""
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"\"format\" : \"date\""
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"\"enum\""
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

