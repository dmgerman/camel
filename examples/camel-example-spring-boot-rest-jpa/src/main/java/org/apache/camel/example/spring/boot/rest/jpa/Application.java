begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.spring.boot.rest.jpa
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|spring
operator|.
name|boot
operator|.
name|rest
operator|.
name|jpa
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
name|servlet
operator|.
name|CamelHttpTransportServlet
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
name|springframework
operator|.
name|boot
operator|.
name|SpringApplication
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|SpringBootApplication
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|web
operator|.
name|servlet
operator|.
name|ServletRegistrationBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|web
operator|.
name|support
operator|.
name|SpringBootServletInitializer
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
name|annotation
operator|.
name|Bean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Component
import|;
end_import

begin_class
annotation|@
name|SpringBootApplication
DECL|class|Application
specifier|public
class|class
name|Application
extends|extends
name|SpringBootServletInitializer
block|{
DECL|method|main (String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
name|SpringApplication
operator|.
name|run
argument_list|(
name|Application
operator|.
name|class
argument_list|,
name|args
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Bean
DECL|method|servletRegistrationBean ()
name|ServletRegistrationBean
name|servletRegistrationBean
parameter_list|()
block|{
name|ServletRegistrationBean
name|servlet
init|=
operator|new
name|ServletRegistrationBean
argument_list|(
operator|new
name|CamelHttpTransportServlet
argument_list|()
argument_list|,
literal|"/camel-rest-sql/*"
argument_list|)
decl_stmt|;
name|servlet
operator|.
name|setName
argument_list|(
literal|"CamelServlet"
argument_list|)
expr_stmt|;
return|return
name|servlet
return|;
block|}
annotation|@
name|Component
DECL|class|RestApi
class|class
name|RestApi
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
block|{
name|restConfiguration
argument_list|()
operator|.
name|contextPath
argument_list|(
literal|"/camel-rest-sql"
argument_list|)
operator|.
name|apiContextPath
argument_list|(
literal|"/api-doc"
argument_list|)
operator|.
name|apiProperty
argument_list|(
literal|"api.title"
argument_list|,
literal|"Camel REST API"
argument_list|)
operator|.
name|apiProperty
argument_list|(
literal|"api.version"
argument_list|,
literal|"1.0"
argument_list|)
operator|.
name|apiProperty
argument_list|(
literal|"cors"
argument_list|,
literal|"true"
argument_list|)
operator|.
name|apiContextRouteId
argument_list|(
literal|"doc-api"
argument_list|)
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
literal|"/books"
argument_list|)
operator|.
name|description
argument_list|(
literal|"Books REST service"
argument_list|)
operator|.
name|get
argument_list|(
literal|"/"
argument_list|)
operator|.
name|description
argument_list|(
literal|"The list of all the books"
argument_list|)
operator|.
name|route
argument_list|()
operator|.
name|routeId
argument_list|(
literal|"books-api"
argument_list|)
operator|.
name|bean
argument_list|(
name|Database
operator|.
name|class
argument_list|,
literal|"findBooks"
argument_list|)
operator|.
name|endRest
argument_list|()
operator|.
name|get
argument_list|(
literal|"order/{id}"
argument_list|)
operator|.
name|description
argument_list|(
literal|"Details of an order by id"
argument_list|)
operator|.
name|route
argument_list|()
operator|.
name|routeId
argument_list|(
literal|"order-api"
argument_list|)
operator|.
name|bean
argument_list|(
name|Database
operator|.
name|class
argument_list|,
literal|"findOrder(${header.id})"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Component
DECL|class|Backend
class|class
name|Backend
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
block|{
comment|// A first route generates some orders and queue them in DB
name|from
argument_list|(
literal|"timer:new-order?delay=1s&period={{example.generateOrderPeriod:2s}}"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"generate-order"
argument_list|)
operator|.
name|bean
argument_list|(
literal|"orderService"
argument_list|,
literal|"generateOrder"
argument_list|)
operator|.
name|to
argument_list|(
literal|"jpa:io.fabric8.quickstarts.camel.Order"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Inserted new order ${body.id}"
argument_list|)
expr_stmt|;
comment|// A second route polls the DB for new orders and processes them
name|from
argument_list|(
literal|"jpa:org.apache.camel.example.spring.boot.rest.jpa.Order"
operator|+
literal|"?consumer.namedQuery=new-orders"
operator|+
literal|"&consumer.delay={{example.processOrderPeriod:5s}}"
operator|+
literal|"&consumeDelete=false"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"process-order"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Processed order #id ${body.id} with ${body.amount} copies of the Â«${body.book.description}Â» book"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

