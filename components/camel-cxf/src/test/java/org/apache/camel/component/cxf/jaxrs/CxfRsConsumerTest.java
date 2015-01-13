begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.jaxrs
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|jaxrs
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
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

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|WebApplicationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
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
name|Message
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
name|RuntimeCamelException
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
name|NoErrorHandlerBuilder
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
name|cxf
operator|.
name|CXFTestSupport
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
name|cxf
operator|.
name|common
operator|.
name|message
operator|.
name|CxfConstants
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
name|cxf
operator|.
name|jaxrs
operator|.
name|testbean
operator|.
name|Customer
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
name|apache
operator|.
name|http
operator|.
name|HttpResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|methods
operator|.
name|HttpGet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|client
operator|.
name|methods
operator|.
name|HttpPut
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|entity
operator|.
name|StringEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|client
operator|.
name|CloseableHttpClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|impl
operator|.
name|client
operator|.
name|HttpClientBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|util
operator|.
name|EntityUtils
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
DECL|class|CxfRsConsumerTest
specifier|public
class|class
name|CxfRsConsumerTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|PUT_REQUEST
specifier|private
specifier|static
specifier|final
name|String
name|PUT_REQUEST
init|=
literal|"<Customer><name>Mary</name><id>123</id></Customer>"
decl_stmt|;
DECL|field|CXT
specifier|private
specifier|static
specifier|final
name|String
name|CXT
init|=
name|CXFTestSupport
operator|.
name|getPort1
argument_list|()
operator|+
literal|"/CxfRsConsumerTest"
decl_stmt|;
comment|// START SNIPPET: example
DECL|field|CXF_RS_ENDPOINT_URI
specifier|private
specifier|static
specifier|final
name|String
name|CXF_RS_ENDPOINT_URI
init|=
literal|"cxfrs://http://localhost:"
operator|+
name|CXT
operator|+
literal|"/rest?resourceClasses=org.apache.camel.component.cxf.jaxrs.testbean.CustomerServiceResource"
decl_stmt|;
DECL|field|CXF_RS_ENDPOINT_URI2
specifier|private
specifier|static
specifier|final
name|String
name|CXF_RS_ENDPOINT_URI2
init|=
literal|"cxfrs://http://localhost:"
operator|+
name|CXT
operator|+
literal|"/rest2?resourceClasses=org.apache.camel.component.cxf.jaxrs.testbean.CustomerService"
decl_stmt|;
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Processor
name|testProcessor
init|=
operator|new
name|TestProcessor
argument_list|()
decl_stmt|;
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
name|errorHandler
argument_list|(
operator|new
name|NoErrorHandlerBuilder
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|CXF_RS_ENDPOINT_URI
argument_list|)
operator|.
name|process
argument_list|(
name|testProcessor
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|CXF_RS_ENDPOINT_URI2
argument_list|)
operator|.
name|process
argument_list|(
name|testProcessor
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
comment|// END SNIPPET: example
DECL|method|invokeGetCustomer (String uri, String expect)
specifier|private
name|void
name|invokeGetCustomer
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|expect
parameter_list|)
throws|throws
name|Exception
block|{
name|HttpGet
name|get
init|=
operator|new
name|HttpGet
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|get
operator|.
name|addHeader
argument_list|(
literal|"Accept"
argument_list|,
literal|"application/json"
argument_list|)
expr_stmt|;
name|CloseableHttpClient
name|httpclient
init|=
name|HttpClientBuilder
operator|.
name|create
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
try|try
block|{
name|HttpResponse
name|response
init|=
name|httpclient
operator|.
name|execute
argument_list|(
name|get
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expect
argument_list|,
name|EntityUtils
operator|.
name|toString
argument_list|(
name|response
operator|.
name|getEntity
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|httpclient
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testGetCustomer ()
specifier|public
name|void
name|testGetCustomer
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeGetCustomer
argument_list|(
literal|"http://localhost:"
operator|+
name|CXT
operator|+
literal|"/rest/customerservice/customers/126"
argument_list|,
literal|"{\"Customer\":{\"id\":126,\"name\":\"Willem\"}}"
argument_list|)
expr_stmt|;
name|invokeGetCustomer
argument_list|(
literal|"http://localhost:"
operator|+
name|CXT
operator|+
literal|"/rest/customerservice/customers/123"
argument_list|,
literal|"customer response back!"
argument_list|)
expr_stmt|;
name|invokeGetCustomer
argument_list|(
literal|"http://localhost:"
operator|+
name|CXT
operator|+
literal|"/rest/customerservice/customers/400"
argument_list|,
literal|"The remoteAddress is 127.0.0.1"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetCustomer2 ()
specifier|public
name|void
name|testGetCustomer2
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeGetCustomer
argument_list|(
literal|"http://localhost:"
operator|+
name|CXT
operator|+
literal|"/rest2/customerservice/customers/126"
argument_list|,
literal|"{\"Customer\":{\"id\":126,\"name\":\"Willem\"}}"
argument_list|)
expr_stmt|;
name|invokeGetCustomer
argument_list|(
literal|"http://localhost:"
operator|+
name|CXT
operator|+
literal|"/rest2/customerservice/customers/123"
argument_list|,
literal|"customer response back!"
argument_list|)
expr_stmt|;
name|invokeGetCustomer
argument_list|(
literal|"http://localhost:"
operator|+
name|CXT
operator|+
literal|"/rest2/customerservice/customers/400"
argument_list|,
literal|"The remoteAddress is 127.0.0.1"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetWrongCustomer ()
specifier|public
name|void
name|testGetWrongCustomer
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
literal|"http://localhost:"
operator|+
name|CXT
operator|+
literal|"/rest/customerservice/customers/456"
argument_list|)
decl_stmt|;
try|try
block|{
name|url
operator|.
name|openStream
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expect to get exception here"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|exception
parameter_list|)
block|{
comment|// do nothing here
block|}
name|url
operator|=
operator|new
name|URL
argument_list|(
literal|"http://localhost:"
operator|+
name|CXT
operator|+
literal|"/rest/customerservice/customers/234"
argument_list|)
expr_stmt|;
try|try
block|{
name|url
operator|.
name|openStream
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expect to get exception here"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|exception
parameter_list|)
block|{
comment|// do nothing here
block|}
name|url
operator|=
operator|new
name|URL
argument_list|(
literal|"http://localhost:"
operator|+
name|CXT
operator|+
literal|"/rest/customerservice/customers/256"
argument_list|)
expr_stmt|;
try|try
block|{
name|url
operator|.
name|openStream
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Expect to get exception here"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|exception
parameter_list|)
block|{
comment|// expect the Internal error exception
block|}
block|}
annotation|@
name|Test
DECL|method|testPutConsumer ()
specifier|public
name|void
name|testPutConsumer
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpPut
name|put
init|=
operator|new
name|HttpPut
argument_list|(
literal|"http://localhost:"
operator|+
name|CXT
operator|+
literal|"/rest/customerservice/customers"
argument_list|)
decl_stmt|;
name|StringEntity
name|entity
init|=
operator|new
name|StringEntity
argument_list|(
name|PUT_REQUEST
argument_list|,
literal|"ISO-8859-1"
argument_list|)
decl_stmt|;
name|entity
operator|.
name|setContentType
argument_list|(
literal|"text/xml; charset=ISO-8859-1"
argument_list|)
expr_stmt|;
name|put
operator|.
name|addHeader
argument_list|(
literal|"test"
argument_list|,
literal|"header1;header2"
argument_list|)
expr_stmt|;
name|put
operator|.
name|setEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|CloseableHttpClient
name|httpclient
init|=
name|HttpClientBuilder
operator|.
name|create
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
try|try
block|{
name|HttpResponse
name|response
init|=
name|httpclient
operator|.
name|execute
argument_list|(
name|put
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|response
operator|.
name|getStatusLine
argument_list|()
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|EntityUtils
operator|.
name|toString
argument_list|(
name|response
operator|.
name|getEntity
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|httpclient
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|class|TestProcessor
specifier|private
specifier|static
class|class
name|TestProcessor
implements|implements
name|Processor
block|{
DECL|method|process (Exchange exchange)
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
name|Message
name|inMessage
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
comment|// Get the operation name from in message
name|String
name|operationName
init|=
name|inMessage
operator|.
name|getHeader
argument_list|(
name|CxfConstants
operator|.
name|OPERATION_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"getCustomer"
operator|.
name|equals
argument_list|(
name|operationName
argument_list|)
condition|)
block|{
name|String
name|httpMethod
init|=
name|inMessage
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong http method"
argument_list|,
literal|"GET"
argument_list|,
name|httpMethod
argument_list|)
expr_stmt|;
name|String
name|path
init|=
name|inMessage
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_PATH
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// The parameter of the invocation is stored in the body of in message
name|String
name|id
init|=
name|inMessage
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
literal|"/customerservice/customers/126"
operator|.
name|equals
argument_list|(
name|path
argument_list|)
condition|)
block|{
name|Customer
name|customer
init|=
operator|new
name|Customer
argument_list|()
decl_stmt|;
name|customer
operator|.
name|setId
argument_list|(
name|Long
operator|.
name|parseLong
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|customer
operator|.
name|setName
argument_list|(
literal|"Willem"
argument_list|)
expr_stmt|;
comment|// We just put the response Object into the out message body
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|customer
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
literal|"/customerservice/customers/400"
operator|.
name|equals
argument_list|(
name|path
argument_list|)
condition|)
block|{
comment|// We return the remote client IP address this time
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
name|cxfMessage
init|=
name|inMessage
operator|.
name|getHeader
argument_list|(
name|CxfConstants
operator|.
name|CAMEL_CXF_MESSAGE
argument_list|,
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
operator|.
name|class
argument_list|)
decl_stmt|;
name|ServletRequest
name|request
init|=
operator|(
name|ServletRequest
operator|)
name|cxfMessage
operator|.
name|get
argument_list|(
literal|"HTTP.REQUEST"
argument_list|)
decl_stmt|;
comment|// Just make sure the request object is not null
name|assertNotNull
argument_list|(
literal|"The request object should not be null"
argument_list|,
name|request
argument_list|)
expr_stmt|;
name|Response
name|r
init|=
name|Response
operator|.
name|status
argument_list|(
literal|200
argument_list|)
operator|.
name|entity
argument_list|(
literal|"The remoteAddress is 127.0.0.1"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|r
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
literal|"/customerservice/customers/123"
operator|.
name|equals
argument_list|(
name|path
argument_list|)
condition|)
block|{
comment|// send a customer response back
name|Response
name|r
init|=
name|Response
operator|.
name|status
argument_list|(
literal|200
argument_list|)
operator|.
name|entity
argument_list|(
literal|"customer response back!"
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|r
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
literal|"/customerservice/customers/456"
operator|.
name|equals
argument_list|(
name|path
argument_list|)
condition|)
block|{
name|Response
name|r
init|=
name|Response
operator|.
name|status
argument_list|(
literal|404
argument_list|)
operator|.
name|entity
argument_list|(
literal|"Can't found the customer with uri "
operator|+
name|path
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|WebApplicationException
argument_list|(
name|r
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
literal|"/customerservice/customers/234"
operator|.
name|equals
argument_list|(
name|path
argument_list|)
condition|)
block|{
name|Response
name|r
init|=
name|Response
operator|.
name|status
argument_list|(
literal|404
argument_list|)
operator|.
name|entity
argument_list|(
literal|"Can't found the customer with uri "
operator|+
name|path
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|r
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setFault
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Can't found the customer with uri "
operator|+
name|path
argument_list|)
throw|;
block|}
block|}
block|}
if|if
condition|(
literal|"updateCustomer"
operator|.
name|equals
argument_list|(
name|operationName
argument_list|)
condition|)
block|{
name|assertEquals
argument_list|(
literal|"Get a wrong customer message header"
argument_list|,
literal|"header1;header2"
argument_list|,
name|inMessage
operator|.
name|getHeader
argument_list|(
literal|"test"
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|httpMethod
init|=
name|inMessage
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong http method"
argument_list|,
literal|"PUT"
argument_list|,
name|httpMethod
argument_list|)
expr_stmt|;
name|Customer
name|customer
init|=
name|inMessage
operator|.
name|getBody
argument_list|(
name|Customer
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"The customer should not be null."
argument_list|,
name|customer
argument_list|)
expr_stmt|;
comment|// Now you can do what you want on the customer object
name|assertEquals
argument_list|(
literal|"Get a wrong customer name."
argument_list|,
literal|"Mary"
argument_list|,
name|customer
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// set the response back
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|Response
operator|.
name|ok
argument_list|()
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

