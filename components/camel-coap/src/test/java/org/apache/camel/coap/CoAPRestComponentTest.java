begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.coap
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|coap
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
name|eclipse
operator|.
name|californium
operator|.
name|core
operator|.
name|CoapClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|californium
operator|.
name|core
operator|.
name|CoapResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|californium
operator|.
name|core
operator|.
name|coap
operator|.
name|CoAP
operator|.
name|ResponseCode
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|californium
operator|.
name|core
operator|.
name|coap
operator|.
name|MediaTypeRegistry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|californium
operator|.
name|core
operator|.
name|network
operator|.
name|config
operator|.
name|NetworkConfig
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
DECL|class|CoAPRestComponentTest
specifier|public
class|class
name|CoAPRestComponentTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|coapport
specifier|static
name|int
name|coapport
init|=
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testCoAP ()
specifier|public
name|void
name|testCoAP
parameter_list|()
throws|throws
name|Exception
block|{
name|NetworkConfig
operator|.
name|createStandardWithoutFile
argument_list|()
expr_stmt|;
name|CoapClient
name|client
decl_stmt|;
name|CoapResponse
name|rsp
decl_stmt|;
name|client
operator|=
operator|new
name|CoapClient
argument_list|(
literal|"coap://localhost:"
operator|+
name|coapport
operator|+
literal|"/TestResource/Ducky"
argument_list|)
expr_stmt|;
name|rsp
operator|=
name|client
operator|.
name|get
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|ResponseCode
operator|.
name|CONTENT
argument_list|,
name|rsp
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Ducky"
argument_list|,
name|rsp
operator|.
name|getResponseText
argument_list|()
argument_list|)
expr_stmt|;
name|rsp
operator|=
name|client
operator|.
name|post
argument_list|(
literal|"data"
argument_list|,
name|MediaTypeRegistry
operator|.
name|TEXT_PLAIN
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ResponseCode
operator|.
name|CONTENT
argument_list|,
name|rsp
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Ducky: data"
argument_list|,
name|rsp
operator|.
name|getResponseText
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|=
operator|new
name|CoapClient
argument_list|(
literal|"coap://localhost:"
operator|+
name|coapport
operator|+
literal|"/TestParams?id=Ducky"
argument_list|)
expr_stmt|;
name|client
operator|.
name|setTimeout
argument_list|(
literal|1000000
argument_list|)
expr_stmt|;
name|rsp
operator|=
name|client
operator|.
name|get
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|ResponseCode
operator|.
name|CONTENT
argument_list|,
name|rsp
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Ducky"
argument_list|,
name|rsp
operator|.
name|getResponseText
argument_list|()
argument_list|)
expr_stmt|;
name|rsp
operator|=
name|client
operator|.
name|post
argument_list|(
literal|"data"
argument_list|,
name|MediaTypeRegistry
operator|.
name|TEXT_PLAIN
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ResponseCode
operator|.
name|CONTENT
argument_list|,
name|rsp
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello Ducky: data"
argument_list|,
name|rsp
operator|.
name|getResponseText
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|MediaTypeRegistry
operator|.
name|TEXT_PLAIN
argument_list|,
name|rsp
operator|.
name|getOptions
argument_list|()
operator|.
name|getContentFormat
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCoAPMethodNotAllowedResponse ()
specifier|public
name|void
name|testCoAPMethodNotAllowedResponse
parameter_list|()
throws|throws
name|Exception
block|{
name|NetworkConfig
operator|.
name|createStandardWithoutFile
argument_list|()
expr_stmt|;
name|CoapClient
name|client
init|=
operator|new
name|CoapClient
argument_list|(
literal|"coap://localhost:"
operator|+
name|coapport
operator|+
literal|"/TestResource/Ducky"
argument_list|)
decl_stmt|;
name|client
operator|.
name|setTimeout
argument_list|(
literal|1000000
argument_list|)
expr_stmt|;
name|CoapResponse
name|rsp
init|=
name|client
operator|.
name|delete
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|ResponseCode
operator|.
name|METHOD_NOT_ALLOWED
argument_list|,
name|rsp
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCoAPNotFoundResponse ()
specifier|public
name|void
name|testCoAPNotFoundResponse
parameter_list|()
throws|throws
name|Exception
block|{
name|NetworkConfig
operator|.
name|createStandardWithoutFile
argument_list|()
expr_stmt|;
name|CoapClient
name|client
init|=
operator|new
name|CoapClient
argument_list|(
literal|"coap://localhost:"
operator|+
name|coapport
operator|+
literal|"/foo/bar/cheese"
argument_list|)
decl_stmt|;
name|client
operator|.
name|setTimeout
argument_list|(
literal|1000000
argument_list|)
expr_stmt|;
name|CoapResponse
name|rsp
init|=
name|client
operator|.
name|get
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|ResponseCode
operator|.
name|NOT_FOUND
argument_list|,
name|rsp
operator|.
name|getCode
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
name|restConfiguration
argument_list|(
literal|"coap"
argument_list|)
operator|.
name|host
argument_list|(
literal|"localhost"
argument_list|)
operator|.
name|port
argument_list|(
name|coapport
argument_list|)
expr_stmt|;
name|rest
argument_list|(
literal|"/TestParams"
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|to
argument_list|(
literal|"direct:get1"
argument_list|)
operator|.
name|post
argument_list|()
operator|.
name|to
argument_list|(
literal|"direct:post1"
argument_list|)
expr_stmt|;
name|rest
argument_list|(
literal|"/TestResource"
argument_list|)
operator|.
name|get
argument_list|(
literal|"/{id}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:get1"
argument_list|)
operator|.
name|post
argument_list|(
literal|"/{id}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:post1"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:get1"
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
literal|"Hello "
operator|+
name|id
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:post1"
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
name|String
name|ct
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
literal|"text/plain"
operator|.
name|equals
argument_list|(
name|ct
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"No content type"
argument_list|)
throw|;
block|}
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Hello "
operator|+
name|id
operator|+
literal|": "
operator|+
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
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|ct
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

