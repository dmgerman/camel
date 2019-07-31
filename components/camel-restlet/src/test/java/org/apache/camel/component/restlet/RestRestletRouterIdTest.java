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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|BindToRegistry
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
name|Exchange
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
name|Processor
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
name|RoutesBuilder
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
name|http4
operator|.
name|HttpMethods
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
name|test
operator|.
name|AvailablePortFinder
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
name|Assert
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
DECL|class|RestRestletRouterIdTest
specifier|public
class|class
name|RestRestletRouterIdTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|BindToRegistry
argument_list|(
literal|"setId"
argument_list|)
DECL|field|SET_ROUTE_ID_AS_BODY
specifier|private
specifier|static
specifier|final
name|Processor
name|SET_ROUTE_ID_AS_BODY
init|=
name|exchange
lambda|->
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|exchange
operator|.
name|getFromRouteId
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|port
specifier|private
name|int
name|port
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|(
literal|6000
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RoutesBuilder
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
name|port
argument_list|)
operator|.
name|bindingMode
argument_list|(
name|RestBindingMode
operator|.
name|auto
argument_list|)
expr_stmt|;
name|rest
argument_list|(
literal|"/app"
argument_list|)
operator|.
name|get
argument_list|(
literal|"/test1"
argument_list|)
operator|.
name|id
argument_list|(
literal|"test1"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:setId"
argument_list|)
operator|.
name|get
argument_list|(
literal|"/test2"
argument_list|)
operator|.
name|route
argument_list|()
operator|.
name|routeId
argument_list|(
literal|"test2"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:setId"
argument_list|)
expr_stmt|;
name|rest
argument_list|(
literal|"/app"
argument_list|)
operator|.
name|get
argument_list|(
literal|"/test4"
argument_list|)
operator|.
name|route
argument_list|()
operator|.
name|routeId
argument_list|(
literal|"test4"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:setId2"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:setId"
argument_list|)
operator|.
name|process
argument_list|(
name|SET_ROUTE_ID_AS_BODY
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:setId2"
argument_list|)
operator|.
name|process
argument_list|(
literal|"setId"
argument_list|)
expr_stmt|;
name|rest
argument_list|(
literal|"/app"
argument_list|)
operator|.
name|get
argument_list|(
literal|"/test3"
argument_list|)
operator|.
name|route
argument_list|()
operator|.
name|id
argument_list|(
literal|"test3"
argument_list|)
operator|.
name|process
argument_list|(
name|SET_ROUTE_ID_AS_BODY
argument_list|)
expr_stmt|;
name|rest
argument_list|(
literal|"/app"
argument_list|)
operator|.
name|get
argument_list|(
literal|"/REST-TEST"
argument_list|)
operator|.
name|route
argument_list|()
operator|.
name|routeId
argument_list|(
literal|"TEST"
argument_list|)
operator|.
name|id
argument_list|(
literal|"REST-TEST"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:setId"
argument_list|)
expr_stmt|;
name|rest
argument_list|(
literal|"/app"
argument_list|)
operator|.
name|get
argument_list|(
literal|"/test5"
argument_list|)
operator|.
name|route
argument_list|()
operator|.
name|id
argument_list|(
literal|"test5"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:setId"
argument_list|)
operator|.
name|endRest
argument_list|()
operator|.
name|to
argument_list|(
literal|"direct:setId2"
argument_list|)
expr_stmt|;
block|}
empty_stmt|;
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|test ()
specifier|public
name|void
name|test
parameter_list|()
throws|throws
name|Exception
block|{
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"\"test1\""
argument_list|,
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"http4://localhost:"
operator|+
name|port
operator|+
literal|"/app/test1"
argument_list|,
literal|""
argument_list|,
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
name|HttpMethods
operator|.
name|GET
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"\"test2\""
argument_list|,
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"http4://localhost:"
operator|+
name|port
operator|+
literal|"/app/test2"
argument_list|,
literal|""
argument_list|,
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
name|HttpMethods
operator|.
name|GET
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"\"test3\""
argument_list|,
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"http4://localhost:"
operator|+
name|port
operator|+
literal|"/app/test3"
argument_list|,
literal|""
argument_list|,
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
name|HttpMethods
operator|.
name|GET
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"\"test4\""
argument_list|,
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"http4://localhost:"
operator|+
name|port
operator|+
literal|"/app/test4"
argument_list|,
literal|""
argument_list|,
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
name|HttpMethods
operator|.
name|GET
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"\"test5\""
argument_list|,
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"http4://localhost:"
operator|+
name|port
operator|+
literal|"/app/test5"
argument_list|,
literal|""
argument_list|,
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
name|HttpMethods
operator|.
name|GET
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"\"REST-TEST\""
argument_list|,
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"http4://localhost:"
operator|+
name|port
operator|+
literal|"/app/REST-TEST"
argument_list|,
literal|""
argument_list|,
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
name|HttpMethods
operator|.
name|GET
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

