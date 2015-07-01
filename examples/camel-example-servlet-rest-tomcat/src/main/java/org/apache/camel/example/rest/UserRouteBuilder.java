begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.rest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
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
name|model
operator|.
name|rest
operator|.
name|RestBindingMode
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
name|model
operator|.
name|rest
operator|.
name|RestParamType
operator|.
name|body
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
name|model
operator|.
name|rest
operator|.
name|RestParamType
operator|.
name|path
import|;
end_import

begin_comment
comment|/**  * Define REST services using the Camel REST DSL  */
end_comment

begin_class
DECL|class|UserRouteBuilder
specifier|public
class|class
name|UserRouteBuilder
extends|extends
name|RouteBuilder
block|{
annotation|@
name|Override
DECL|method|configure ()
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// configure we want to use servlet as the component for the rest DSL
comment|// and we enable json binding mode
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
comment|// and output using pretty print
operator|.
name|dataFormatProperty
argument_list|(
literal|"prettyPrint"
argument_list|,
literal|"true"
argument_list|)
comment|// setup context path and port number that Apache Tomcat will deploy
comment|// this application with, as we use the servlet component, then we
comment|// need to aid Camel to tell it these details so Camel knows the url
comment|// to the REST services.
comment|// Notice: This is optional, but needed if the RestRegistry should
comment|// enlist accurate information. You can access the RestRegistry
comment|// from JMX at runtime
operator|.
name|contextPath
argument_list|(
literal|"camel-example-servlet-rest-tomcat/rest"
argument_list|)
operator|.
name|port
argument_list|(
literal|8080
argument_list|)
expr_stmt|;
comment|// this user REST service is json only
name|rest
argument_list|(
literal|"/user"
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
literal|"int"
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
name|outTypeList
argument_list|(
name|User
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"bean:userService?method=listUsers"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

