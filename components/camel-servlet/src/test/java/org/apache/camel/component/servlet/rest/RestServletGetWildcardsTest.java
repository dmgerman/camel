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
name|com
operator|.
name|meterware
operator|.
name|httpunit
operator|.
name|GetMethodWebRequest
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
name|component
operator|.
name|servlet
operator|.
name|ServletRestHttpBinding
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
DECL|class|RestServletGetWildcardsTest
specifier|public
class|class
name|RestServletGetWildcardsTest
extends|extends
name|ServletCamelRouterTestSupport
block|{
annotation|@
name|BindToRegistry
argument_list|(
literal|"myBinding"
argument_list|)
DECL|field|restHttpBinding
specifier|private
name|ServletRestHttpBinding
name|restHttpBinding
init|=
operator|new
name|ServletRestHttpBinding
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testServletProducerGet ()
specifier|public
name|void
name|testServletProducerGet
parameter_list|()
throws|throws
name|Exception
block|{
name|WebRequest
name|req
init|=
operator|new
name|GetMethodWebRequest
argument_list|(
name|CONTEXT_URL
operator|+
literal|"/services/users/123/basic"
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
name|assertEquals
argument_list|(
literal|"123;Donald Duck"
argument_list|,
name|response
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testServletProducerGetWildcards ()
specifier|public
name|void
name|testServletProducerGetWildcards
parameter_list|()
throws|throws
name|Exception
block|{
name|WebRequest
name|req
init|=
operator|new
name|GetMethodWebRequest
argument_list|(
name|CONTEXT_URL
operator|+
literal|"/services/users/456/name=g*"
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
name|assertEquals
argument_list|(
literal|"456;Goofy"
argument_list|,
name|response
operator|.
name|getText
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
comment|// configure to use servlet on localhost
name|restConfiguration
argument_list|()
operator|.
name|component
argument_list|(
literal|"servlet"
argument_list|)
operator|.
name|host
argument_list|(
literal|"localhost"
argument_list|)
operator|.
name|endpointProperty
argument_list|(
literal|"httpBinding"
argument_list|,
literal|"#myBinding"
argument_list|)
expr_stmt|;
comment|// use the rest DSL to define the rest services
name|rest
argument_list|(
literal|"/users/"
argument_list|)
operator|.
name|get
argument_list|(
literal|"{id}/basic"
argument_list|)
operator|.
name|route
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:input"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|id
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"id"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|id
operator|+
literal|";Donald Duck"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|endRest
argument_list|()
operator|.
name|get
argument_list|(
literal|"{id}/{query}"
argument_list|)
operator|.
name|route
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:query"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|id
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"id"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|id
operator|+
literal|";Goofy"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|endRest
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

