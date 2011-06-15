begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|io
operator|.
name|IOException
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
name|ExchangePattern
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
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|Client
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|Request
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|data
operator|.
name|MediaType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|data
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|data
operator|.
name|Protocol
import|;
end_import

begin_import
import|import
name|org
operator|.
name|restlet
operator|.
name|data
operator|.
name|Status
import|;
end_import

begin_class
DECL|class|RestletRouteBuilderTest
specifier|public
class|class
name|RestletRouteBuilderTest
extends|extends
name|RestletTestSupport
block|{
DECL|field|ID
specifier|private
specifier|static
specifier|final
name|String
name|ID
init|=
literal|"89531"
decl_stmt|;
DECL|field|JSON
specifier|private
specifier|static
specifier|final
name|String
name|JSON
init|=
literal|"{\"document type\": \"JSON\"}"
decl_stmt|;
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// Restlet producer to use POST method. The RestletMethod=post will be stripped
comment|// before request is sent.
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|setHeader
argument_list|(
literal|"id"
argument_list|,
name|constant
argument_list|(
name|ID
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"restlet:http://localhost:"
operator|+
name|portNum
operator|+
literal|"/orders?restletMethod=post&foo=bar"
argument_list|)
expr_stmt|;
comment|// Restlet consumer to handler POST method
name|from
argument_list|(
literal|"restlet:http://localhost:"
operator|+
name|portNum
operator|+
literal|"/orders?restletMethod=post"
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
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"received ["
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
operator|+
literal|"] as an order id = "
operator|+
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"id"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// Restlet consumer to handler POST method
name|from
argument_list|(
literal|"restlet:http://localhost:"
operator|+
name|portNum
operator|+
literal|"/ordersJSON?restletMethod=post"
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
name|body
init|=
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
decl_stmt|;
if|if
condition|(
name|body
operator|.
name|indexOf
argument_list|(
literal|"{"
argument_list|)
operator|==
operator|-
literal|1
condition|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Inproperly formatted JSON:  "
operator|+
name|body
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
name|body
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// Restlet consumer default to handle GET method
name|from
argument_list|(
literal|"restlet:http://localhost:"
operator|+
name|portNum
operator|+
literal|"/orders/{id}/{x}"
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
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"received GET request with id="
operator|+
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"id"
argument_list|)
operator|+
literal|" and x="
operator|+
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"x"
argument_list|)
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
annotation|@
name|Test
DECL|method|testProducer ()
specifier|public
name|void
name|testProducer
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|response
init|=
operator|(
name|String
operator|)
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<order foo='1'/>"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"received [<order foo='1'/>] as an order id = "
operator|+
name|ID
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|response
operator|=
operator|(
name|String
operator|)
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"restlet:http://localhost:"
operator|+
name|portNum
operator|+
literal|"/orders?restletMethod=post&foo=bar"
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|,
literal|"<order foo='1'/>"
argument_list|,
literal|"id"
argument_list|,
literal|"89531"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"received [<order foo='1'/>] as an order id = "
operator|+
name|ID
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testProducerJSON ()
specifier|public
name|void
name|testProducerJSON
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|response
init|=
operator|(
name|String
operator|)
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"restlet:http://localhost:"
operator|+
name|portNum
operator|+
literal|"/ordersJSON?restletMethod=post&foo=bar"
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|,
name|JSON
argument_list|,
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|JSON
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testProducerJSONFailure ()
specifier|public
name|void
name|testProducerJSONFailure
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|response
init|=
operator|(
name|String
operator|)
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"restlet:http://localhost:"
operator|+
name|portNum
operator|+
literal|"/ordersJSON?restletMethod=post&foo=bar"
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|,
literal|"{'JSON'}"
argument_list|,
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|,
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"{'JSON'}"
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConsumer ()
specifier|public
name|void
name|testConsumer
parameter_list|()
throws|throws
name|IOException
block|{
name|Client
name|client
init|=
operator|new
name|Client
argument_list|(
name|Protocol
operator|.
name|HTTP
argument_list|)
decl_stmt|;
name|Response
name|response
init|=
name|client
operator|.
name|handle
argument_list|(
operator|new
name|Request
argument_list|(
name|Method
operator|.
name|GET
argument_list|,
literal|"http://localhost:"
operator|+
name|portNum
operator|+
literal|"/orders/99991/6"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"received GET request with id=99991 and x=6"
argument_list|,
name|response
operator|.
name|getEntity
argument_list|()
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnhandledConsumer ()
specifier|public
name|void
name|testUnhandledConsumer
parameter_list|()
throws|throws
name|IOException
block|{
name|Client
name|client
init|=
operator|new
name|Client
argument_list|(
name|Protocol
operator|.
name|HTTP
argument_list|)
decl_stmt|;
name|Response
name|response
init|=
name|client
operator|.
name|handle
argument_list|(
operator|new
name|Request
argument_list|(
name|Method
operator|.
name|POST
argument_list|,
literal|"http://localhost:"
operator|+
name|portNum
operator|+
literal|"/orders/99991/6"
argument_list|)
argument_list|)
decl_stmt|;
comment|// expect error status as no Restlet consumer to handle POST method
name|assertEquals
argument_list|(
name|Status
operator|.
name|CLIENT_ERROR_NOT_FOUND
argument_list|,
name|response
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|response
operator|.
name|getEntity
argument_list|()
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

