begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.parser.java
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|parser
operator|.
name|java
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
name|RestHostNameResolver
import|;
end_import

begin_class
DECL|class|MyRestDslRouteBuilder
specifier|public
class|class
name|MyRestDslRouteBuilder
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
name|restConfiguration
argument_list|()
operator|.
name|contextPath
argument_list|(
literal|"myapi"
argument_list|)
operator|.
name|port
argument_list|(
literal|1234
argument_list|)
operator|.
name|component
argument_list|(
literal|"jetty"
argument_list|)
operator|.
name|apiComponent
argument_list|(
literal|"swagger"
argument_list|)
operator|.
name|apiHost
argument_list|(
literal|"localhost"
argument_list|)
operator|.
name|apiContextPath
argument_list|(
literal|"myapi/swagger"
argument_list|)
operator|.
name|skipBindingOnErrorCode
argument_list|(
literal|true
argument_list|)
operator|.
name|scheme
argument_list|(
literal|"https"
argument_list|)
operator|.
name|hostNameResolver
argument_list|(
name|RestHostNameResolver
operator|.
name|allLocalIp
argument_list|)
operator|.
name|bindingMode
argument_list|(
name|RestBindingMode
operator|.
name|json
argument_list|)
operator|.
name|componentProperty
argument_list|(
literal|"foo"
argument_list|,
literal|"123"
argument_list|)
operator|.
name|endpointProperty
argument_list|(
literal|"pretty"
argument_list|,
literal|"false"
argument_list|)
operator|.
name|consumerProperty
argument_list|(
literal|"bar"
argument_list|,
literal|"456"
argument_list|)
operator|.
name|corsHeaderProperty
argument_list|(
literal|"key1"
argument_list|,
literal|"value1"
argument_list|)
operator|.
name|corsHeaderProperty
argument_list|(
literal|"key2"
argument_list|,
literal|"value2"
argument_list|)
expr_stmt|;
name|rest
argument_list|(
literal|"/foo"
argument_list|)
operator|.
name|consumes
argument_list|(
literal|"xml"
argument_list|)
operator|.
name|produces
argument_list|(
literal|"json"
argument_list|)
operator|.
name|description
argument_list|(
literal|"my foo service"
argument_list|)
operator|.
name|get
argument_list|(
literal|"{id}"
argument_list|)
operator|.
name|apiDocs
argument_list|(
literal|false
argument_list|)
operator|.
name|description
argument_list|(
literal|"get by id"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:id"
argument_list|)
operator|.
name|post
argument_list|()
operator|.
name|bindingMode
argument_list|(
name|RestBindingMode
operator|.
name|xml
argument_list|)
operator|.
name|description
argument_list|(
literal|"post something"
argument_list|)
operator|.
name|toD
argument_list|(
literal|"log:post"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

