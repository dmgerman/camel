begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http
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
name|ContextTestSupport
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
name|component
operator|.
name|http
operator|.
name|HttpMethods
operator|.
name|*
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|HttpMethod
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * Unit test to verify the algorithm for selecting either GET or POST.  */
end_comment

begin_class
DECL|class|HttpProducerSelectMethodTest
specifier|public
class|class
name|HttpProducerSelectMethodTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testNoDataDefaultIsGet ()
specifier|public
name|void
name|testNoDataDefaultIsGet
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpComponent
name|component
init|=
operator|new
name|HttpComponent
argument_list|()
decl_stmt|;
name|component
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|HttpEndpoint
name|endpoiont
init|=
operator|(
name|HttpEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"http://www.google.com"
argument_list|)
decl_stmt|;
name|MyHttpProducer
name|producer
init|=
operator|new
name|MyHttpProducer
argument_list|(
name|endpoiont
argument_list|,
literal|"GET"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|HttpExchange
name|exchange
init|=
name|producer
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|null
argument_list|)
expr_stmt|;
try|try
block|{
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown HttpOperationFailedException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|HttpOperationFailedException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|500
argument_list|,
name|e
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|testDataDefaultIsPost ()
specifier|public
name|void
name|testDataDefaultIsPost
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpComponent
name|component
init|=
operator|new
name|HttpComponent
argument_list|()
decl_stmt|;
name|component
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|HttpEndpoint
name|endpoiont
init|=
operator|(
name|HttpEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"http://www.google.com"
argument_list|)
decl_stmt|;
name|MyHttpProducer
name|producer
init|=
operator|new
name|MyHttpProducer
argument_list|(
name|endpoiont
argument_list|,
literal|"POST"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|HttpExchange
name|exchange
init|=
name|producer
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"This is some data to post"
argument_list|)
expr_stmt|;
try|try
block|{
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown HttpOperationFailedException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|HttpOperationFailedException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|500
argument_list|,
name|e
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|testWithMethodPostInHeader ()
specifier|public
name|void
name|testWithMethodPostInHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpComponent
name|component
init|=
operator|new
name|HttpComponent
argument_list|()
decl_stmt|;
name|component
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|HttpEndpoint
name|endpoiont
init|=
operator|(
name|HttpEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"http://www.google.com"
argument_list|)
decl_stmt|;
name|MyHttpProducer
name|producer
init|=
operator|new
name|MyHttpProducer
argument_list|(
name|endpoiont
argument_list|,
literal|"POST"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|HttpExchange
name|exchange
init|=
name|producer
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HTTP_METHOD
argument_list|,
name|POST
argument_list|)
expr_stmt|;
try|try
block|{
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown HttpOperationFailedException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|HttpOperationFailedException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|500
argument_list|,
name|e
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|testWithMethodGetInHeader ()
specifier|public
name|void
name|testWithMethodGetInHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpComponent
name|component
init|=
operator|new
name|HttpComponent
argument_list|()
decl_stmt|;
name|component
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|HttpEndpoint
name|endpoiont
init|=
operator|(
name|HttpEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"http://www.google.com"
argument_list|)
decl_stmt|;
name|MyHttpProducer
name|producer
init|=
operator|new
name|MyHttpProducer
argument_list|(
name|endpoiont
argument_list|,
literal|"GET"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|HttpExchange
name|exchange
init|=
name|producer
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HTTP_METHOD
argument_list|,
name|GET
argument_list|)
expr_stmt|;
try|try
block|{
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown HttpOperationFailedException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|HttpOperationFailedException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|500
argument_list|,
name|e
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|testWithEndpointQuery ()
specifier|public
name|void
name|testWithEndpointQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpComponent
name|component
init|=
operator|new
name|HttpComponent
argument_list|()
decl_stmt|;
name|component
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|HttpEndpoint
name|endpoiont
init|=
operator|(
name|HttpEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"http://www.google.com?q=Camel"
argument_list|)
decl_stmt|;
name|MyHttpProducer
name|producer
init|=
operator|new
name|MyHttpProducer
argument_list|(
name|endpoiont
argument_list|,
literal|"GET"
argument_list|,
literal|"q=Camel"
argument_list|)
decl_stmt|;
name|HttpExchange
name|exchange
init|=
name|producer
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|""
argument_list|)
expr_stmt|;
try|try
block|{
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown HttpOperationFailedException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|HttpOperationFailedException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|500
argument_list|,
name|e
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|testWithQueryInHeader ()
specifier|public
name|void
name|testWithQueryInHeader
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpComponent
name|component
init|=
operator|new
name|HttpComponent
argument_list|()
decl_stmt|;
name|component
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|HttpEndpoint
name|endpoiont
init|=
operator|(
name|HttpEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"http://www.google.com"
argument_list|)
decl_stmt|;
name|MyHttpProducer
name|producer
init|=
operator|new
name|MyHttpProducer
argument_list|(
name|endpoiont
argument_list|,
literal|"GET"
argument_list|,
literal|"q=Camel"
argument_list|)
decl_stmt|;
name|HttpExchange
name|exchange
init|=
name|producer
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HttpProducer
operator|.
name|QUERY
argument_list|,
literal|"q=Camel"
argument_list|)
expr_stmt|;
try|try
block|{
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown HttpOperationFailedException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|HttpOperationFailedException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|500
argument_list|,
name|e
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|testWithQueryInHeaderOverrideEndpoint ()
specifier|public
name|void
name|testWithQueryInHeaderOverrideEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpComponent
name|component
init|=
operator|new
name|HttpComponent
argument_list|()
decl_stmt|;
name|component
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|HttpEndpoint
name|endpoiont
init|=
operator|(
name|HttpEndpoint
operator|)
name|component
operator|.
name|createEndpoint
argument_list|(
literal|"http://www.google.com?q=Donkey"
argument_list|)
decl_stmt|;
name|MyHttpProducer
name|producer
init|=
operator|new
name|MyHttpProducer
argument_list|(
name|endpoiont
argument_list|,
literal|"GET"
argument_list|,
literal|"q=Camel"
argument_list|)
decl_stmt|;
name|HttpExchange
name|exchange
init|=
name|producer
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HttpProducer
operator|.
name|QUERY
argument_list|,
literal|"q=Camel"
argument_list|)
expr_stmt|;
try|try
block|{
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown HttpOperationFailedException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|HttpOperationFailedException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|500
argument_list|,
name|e
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|producer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|class|MyHttpProducer
specifier|private
specifier|static
class|class
name|MyHttpProducer
extends|extends
name|HttpProducer
block|{
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
DECL|field|queryString
specifier|private
name|String
name|queryString
decl_stmt|;
DECL|method|MyHttpProducer (HttpEndpoint endpoint, String name, String queryString)
specifier|public
name|MyHttpProducer
parameter_list|(
name|HttpEndpoint
name|endpoint
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|queryString
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|queryString
operator|=
name|queryString
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|executeMethod (HttpMethod method)
specifier|protected
name|int
name|executeMethod
parameter_list|(
name|HttpMethod
name|method
parameter_list|)
throws|throws
name|IOException
block|{
comment|// do the assertion what to expected either GET or POST
name|assertEquals
argument_list|(
name|name
argument_list|,
name|method
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|queryString
argument_list|,
name|method
operator|.
name|getQueryString
argument_list|()
argument_list|)
expr_stmt|;
comment|// return 500 to not extract response as we dont have any
return|return
literal|500
return|;
block|}
block|}
block|}
end_class

end_unit

