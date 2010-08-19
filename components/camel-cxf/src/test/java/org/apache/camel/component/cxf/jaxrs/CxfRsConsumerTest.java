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
name|InputStream
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
name|component
operator|.
name|cxf
operator|.
name|util
operator|.
name|CxfUtils
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
name|HttpClient
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
name|DefaultHttpClient
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
DECL|field|CXF_RS_ENDPOINT_URI
specifier|private
specifier|static
specifier|final
name|String
name|CXF_RS_ENDPOINT_URI
init|=
literal|"cxfrs://http://localhost:9000/rest?resourceClasses=org.apache.camel.component.cxf.jaxrs.testbean.CustomerService"
decl_stmt|;
comment|// START SNIPPET: example
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
operator|(
name|String
operator|)
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
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
comment|// END SNIPPET: example
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
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
literal|"http://localhost:9000/rest/customerservice/customers/126"
argument_list|)
decl_stmt|;
name|InputStream
name|in
init|=
name|url
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"{\"Customer\":{\"id\":126,\"name\":\"Willem\"}}"
argument_list|,
name|CxfUtils
operator|.
name|getStringFromInputStream
argument_list|(
name|in
argument_list|)
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
literal|"http://localhost:9000/rest/customerservice/customers/456"
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
literal|"http://localhost:9000/rest/customerservice/customers"
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
name|HttpClient
name|httpclient
init|=
operator|new
name|DefaultHttpClient
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
name|getConnectionManager
argument_list|()
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

