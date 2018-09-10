begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|impl
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
name|impl
operator|.
name|JndiRegistry
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
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|junit4
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
name|Test
import|;
end_import

begin_class
DECL|class|RestSwaggerReaderEnableVendorExtensionTest
specifier|public
class|class
name|RestSwaggerReaderEnableVendorExtensionTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"dummy-rest"
argument_list|,
operator|new
name|DummyRestConsumerFactory
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
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
comment|// enable vendor extensions
name|restConfiguration
argument_list|()
operator|.
name|apiVendorExtension
argument_list|(
literal|true
argument_list|)
expr_stmt|;
comment|// this user REST service is json only
name|rest
argument_list|(
literal|"/user"
argument_list|)
operator|.
name|tag
argument_list|(
literal|"dude"
argument_list|)
operator|.
name|description
argument_list|(
literal|"User rest service"
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
literal|"/{id}"
argument_list|)
operator|.
name|description
argument_list|(
literal|"Find user by id"
argument_list|)
operator|.
name|outType
argument_list|(
name|User
operator|.
name|class
argument_list|)
operator|.
name|responseMessage
argument_list|()
operator|.
name|message
argument_list|(
literal|"The user returned"
argument_list|)
operator|.
name|endResponseMessage
argument_list|()
operator|.
name|param
argument_list|()
operator|.
name|name
argument_list|(
literal|"id"
argument_list|)
operator|.
name|type
argument_list|(
name|RestParamType
operator|.
name|path
argument_list|)
operator|.
name|description
argument_list|(
literal|"The id of the user to get"
argument_list|)
operator|.
name|dataType
argument_list|(
literal|"integer"
argument_list|)
operator|.
name|endParam
argument_list|()
operator|.
name|to
argument_list|(
literal|"bean:userService?method=getUser(${header.id})"
argument_list|)
operator|.
name|put
argument_list|()
operator|.
name|description
argument_list|(
literal|"Updates or create a user"
argument_list|)
operator|.
name|type
argument_list|(
name|User
operator|.
name|class
argument_list|)
operator|.
name|param
argument_list|()
operator|.
name|name
argument_list|(
literal|"body"
argument_list|)
operator|.
name|type
argument_list|(
name|RestParamType
operator|.
name|body
argument_list|)
operator|.
name|description
argument_list|(
literal|"The user to update or create"
argument_list|)
operator|.
name|endParam
argument_list|()
operator|.
name|to
argument_list|(
literal|"bean:userService?method=updateUser"
argument_list|)
operator|.
name|get
argument_list|(
literal|"/findAll"
argument_list|)
operator|.
name|description
argument_list|(
literal|"Find all users"
argument_list|)
operator|.
name|outType
argument_list|(
name|User
index|[]
operator|.
expr|class
argument_list|)
operator|.
name|responseMessage
argument_list|()
operator|.
name|message
argument_list|(
literal|"All the found users"
argument_list|)
operator|.
name|endResponseMessage
argument_list|()
operator|.
name|to
argument_list|(
literal|"bean:userService?method=listUsers"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|testEnableVendorExtension ()
specifier|public
name|void
name|testEnableVendorExtension
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
name|String
name|camelId
init|=
name|context
operator|.
name|getName
argument_list|()
decl_stmt|;
name|String
name|routeId
init|=
name|context
operator|.
name|getRouteDefinitions
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
decl_stmt|;
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
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"\"x-camelContextId\" : \""
operator|+
name|camelId
operator|+
literal|"\""
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|json
operator|.
name|contains
argument_list|(
literal|"\"x-routeId\" : \""
operator|+
name|routeId
operator|+
literal|"\""
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

