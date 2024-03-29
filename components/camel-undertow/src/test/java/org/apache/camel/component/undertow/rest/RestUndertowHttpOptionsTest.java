begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.undertow.rest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|undertow
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
name|undertow
operator|.
name|BaseUndertowTest
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
DECL|class|RestUndertowHttpOptionsTest
specifier|public
class|class
name|RestUndertowHttpOptionsTest
extends|extends
name|BaseUndertowTest
block|{
annotation|@
name|Test
DECL|method|testUndertowServerOptions ()
specifier|public
name|void
name|testUndertowServerOptions
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"undertow:http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/users/v1/customers"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
literal|"OPTIONS"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"GET,OPTIONS"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"ALLOW"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|exchange
operator|=
name|fluentTemplate
operator|.
name|to
argument_list|(
literal|"undertow:http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/users/v1/123"
argument_list|)
operator|.
name|withHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
literal|"OPTIONS"
argument_list|)
operator|.
name|send
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"PUT,OPTIONS"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"ALLOW"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMultipleHttpOptions ()
specifier|public
name|void
name|testMultipleHttpOptions
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"undertow:http://localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/users/v1/options"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
literal|"OPTIONS"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"GET,POST,OPTIONS"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"ALLOW"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
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
comment|// configure to use undertow on localhost with the given port
name|restConfiguration
argument_list|()
operator|.
name|component
argument_list|(
literal|"undertow"
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
expr_stmt|;
comment|// use the rest DSL to define the rest services
name|rest
argument_list|(
literal|"/users/"
argument_list|)
operator|.
name|get
argument_list|(
literal|"v1/customers"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:customers"
argument_list|)
operator|.
name|put
argument_list|(
literal|"v1/{id}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:id"
argument_list|)
operator|.
name|get
argument_list|(
literal|"v1/options"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:options"
argument_list|)
operator|.
name|post
argument_list|(
literal|"v1/options"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:options"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

