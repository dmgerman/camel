begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.cxfbean
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
name|cxfbean
package|;
end_package

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
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|BindingProvider
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Holder
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
name|CamelContext
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
name|Endpoint
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
name|mock
operator|.
name|MockEndpoint
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
name|util
operator|.
name|ObjectHelper
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
name|wsdl_first
operator|.
name|Person
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
name|wsdl_first
operator|.
name|PersonService
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
name|HttpPost
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Qualifier
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|AbstractJUnit4SpringContextTests
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_class
annotation|@
name|ContextConfiguration
DECL|class|CxfBeanTest
specifier|public
class|class
name|CxfBeanTest
extends|extends
name|AbstractJUnit4SpringContextTests
block|{
DECL|field|PUT_REQUEST
specifier|private
specifier|static
specifier|final
name|String
name|PUT_REQUEST
init|=
literal|"<Customer><name>Mary</name><id>113</id></Customer>"
decl_stmt|;
DECL|field|POST_REQUEST
specifier|private
specifier|static
specifier|final
name|String
name|POST_REQUEST
init|=
literal|"<Customer><name>Jack</name></Customer>"
decl_stmt|;
DECL|field|POST2_REQUEST
specifier|private
specifier|static
specifier|final
name|String
name|POST2_REQUEST
init|=
literal|"<Customer><name>James</name></Customer>"
decl_stmt|;
DECL|field|PORT1
specifier|private
specifier|static
specifier|final
name|int
name|PORT1
init|=
name|CXFTestSupport
operator|.
name|getPort
argument_list|(
literal|"CxfBeanTest.1"
argument_list|)
decl_stmt|;
DECL|field|PORT2
specifier|private
specifier|static
specifier|final
name|int
name|PORT2
init|=
name|CXFTestSupport
operator|.
name|getPort
argument_list|(
literal|"CxfBeanTest.2"
argument_list|)
decl_stmt|;
annotation|@
name|Autowired
annotation|@
name|Qualifier
argument_list|(
literal|"camel"
argument_list|)
DECL|field|camelContext
specifier|protected
name|CamelContext
name|camelContext
decl_stmt|;
comment|/**      * Test that we have an endpoint with 2 providers.      */
annotation|@
name|Test
DECL|method|testConsumerWithProviders ()
specifier|public
name|void
name|testConsumerWithProviders
parameter_list|()
throws|throws
name|Exception
block|{
name|boolean
name|testedEndpointWithProviders
init|=
literal|false
decl_stmt|;
for|for
control|(
name|Endpoint
name|endpoint
range|:
name|camelContext
operator|.
name|getEndpoints
argument_list|()
control|)
block|{
if|if
condition|(
name|endpoint
operator|instanceof
name|CxfBeanEndpoint
condition|)
block|{
name|CxfBeanEndpoint
name|beanEndpoint
init|=
operator|(
name|CxfBeanEndpoint
operator|)
name|endpoint
decl_stmt|;
if|if
condition|(
name|beanEndpoint
operator|.
name|getEndpointUri
argument_list|()
operator|.
name|equals
argument_list|(
literal|"customerServiceBean"
argument_list|)
condition|)
block|{
name|assertNotNull
argument_list|(
literal|"The bean endpoint should have provider"
argument_list|,
name|beanEndpoint
operator|.
name|getProviders
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|beanEndpoint
operator|.
name|getProviders
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|2
condition|)
block|{
name|testedEndpointWithProviders
operator|=
literal|true
expr_stmt|;
break|break;
block|}
elseif|else
if|if
condition|(
name|beanEndpoint
operator|.
name|getProviders
argument_list|()
operator|.
name|size
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|fail
argument_list|(
literal|"Unexpected number of providers present"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
name|assertTrue
argument_list|(
name|testedEndpointWithProviders
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMessageHeadersAfterCxfBeanEndpoint ()
specifier|public
name|void
name|testMessageHeadersAfterCxfBeanEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|endpoint
init|=
operator|(
name|MockEndpoint
operator|)
name|camelContext
operator|.
name|getEndpoint
argument_list|(
literal|"mock:endpointA"
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|reset
argument_list|()
expr_stmt|;
name|endpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|expectedHeaderReceived
argument_list|(
literal|"key"
argument_list|,
literal|"customer"
argument_list|)
expr_stmt|;
name|invokeRsService
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT1
operator|+
literal|"/customerservice/customers/123"
argument_list|,
literal|"{\"Customer\":{\"id\":123,\"name\":\"John\"}}"
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|invokeRsService (String getUrl, String expected)
specifier|private
name|void
name|invokeRsService
parameter_list|(
name|String
name|getUrl
parameter_list|,
name|String
name|expected
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
name|getUrl
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
name|get
operator|.
name|addHeader
argument_list|(
literal|"key"
argument_list|,
literal|"customer"
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
name|expected
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
DECL|method|testGetConsumer ()
specifier|public
name|void
name|testGetConsumer
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeRsService
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT1
operator|+
literal|"/customerservice/customers/123"
argument_list|,
literal|"{\"Customer\":{\"id\":123,\"name\":\"John\"}}"
argument_list|)
expr_stmt|;
name|invokeRsService
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT1
operator|+
literal|"/customerservice/orders/223/products/323"
argument_list|,
literal|"{\"Product\":{\"description\":\"product 323\",\"id\":323}}"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetConsumerWithQueryParam ()
specifier|public
name|void
name|testGetConsumerWithQueryParam
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeRsService
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT1
operator|+
literal|"/customerservice/customers?id=123"
argument_list|,
literal|"{\"Customer\":{\"id\":123,\"name\":\"John\"}}"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetConsumerAfterReStartCamelContext ()
specifier|public
name|void
name|testGetConsumerAfterReStartCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeRsService
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT1
operator|+
literal|"/customerservice/customers/123"
argument_list|,
literal|"{\"Customer\":{\"id\":123,\"name\":\"John\"}}"
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|stop
argument_list|()
expr_stmt|;
name|camelContext
operator|.
name|start
argument_list|()
expr_stmt|;
name|invokeRsService
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT1
operator|+
literal|"/customerservice/orders/223/products/323"
argument_list|,
literal|"{\"Product\":{\"description\":\"product 323\",\"id\":323}}"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetConsumerAfterResumingCamelContext ()
specifier|public
name|void
name|testGetConsumerAfterResumingCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|invokeRsService
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT1
operator|+
literal|"/customerservice/customers/123"
argument_list|,
literal|"{\"Customer\":{\"id\":123,\"name\":\"John\"}}"
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|suspend
argument_list|()
expr_stmt|;
name|camelContext
operator|.
name|resume
argument_list|()
expr_stmt|;
name|invokeRsService
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT1
operator|+
literal|"/customerservice/orders/223/products/323"
argument_list|,
literal|"{\"Product\":{\"description\":\"product 323\",\"id\":323}}"
argument_list|)
expr_stmt|;
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
name|PORT1
operator|+
literal|"/customerservice/customers"
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
annotation|@
name|Test
DECL|method|testPostConsumer ()
specifier|public
name|void
name|testPostConsumer
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpPost
name|post
init|=
operator|new
name|HttpPost
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT1
operator|+
literal|"/customerservice/customers"
argument_list|)
decl_stmt|;
name|post
operator|.
name|addHeader
argument_list|(
literal|"Accept"
argument_list|,
literal|"text/xml"
argument_list|)
expr_stmt|;
name|StringEntity
name|entity
init|=
operator|new
name|StringEntity
argument_list|(
name|POST_REQUEST
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
name|post
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
name|post
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
name|String
name|id
init|=
name|getCustomerId
argument_list|(
literal|"Jack"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><Customer><id>"
operator|+
name|id
operator|+
literal|"</id><name>Jack</name></Customer>"
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
DECL|method|testPostConsumerUniqueResponseCode ()
specifier|public
name|void
name|testPostConsumerUniqueResponseCode
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpPost
name|post
init|=
operator|new
name|HttpPost
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT1
operator|+
literal|"/customerservice/customersUniqueResponseCode"
argument_list|)
decl_stmt|;
name|post
operator|.
name|addHeader
argument_list|(
literal|"Accept"
argument_list|,
literal|"text/xml"
argument_list|)
expr_stmt|;
name|StringEntity
name|entity
init|=
operator|new
name|StringEntity
argument_list|(
name|POST2_REQUEST
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
name|post
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
name|post
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|201
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
name|String
name|id
init|=
name|getCustomerId
argument_list|(
literal|"James"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><Customer><id>"
operator|+
name|id
operator|+
literal|"</id><name>James</name></Customer>"
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
DECL|method|getCustomerId (String name)
specifier|private
name|String
name|getCustomerId
parameter_list|(
name|String
name|name
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
literal|"http://localhost:"
operator|+
name|PORT1
operator|+
literal|"/customerservice/customers/"
argument_list|)
decl_stmt|;
name|get
operator|.
name|addHeader
argument_list|(
literal|"Accept"
argument_list|,
literal|"application/xml"
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
name|String
name|customers
init|=
name|EntityUtils
operator|.
name|toString
argument_list|(
name|response
operator|.
name|getEntity
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|before
init|=
name|ObjectHelper
operator|.
name|before
argument_list|(
name|customers
argument_list|,
literal|"</id><name>"
operator|+
name|name
operator|+
literal|"</name></Customer>"
argument_list|)
decl_stmt|;
name|String
name|answer
init|=
name|before
operator|.
name|substring
argument_list|(
name|before
operator|.
name|lastIndexOf
argument_list|(
literal|">"
argument_list|)
operator|+
literal|1
argument_list|,
name|before
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|answer
return|;
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
DECL|method|testJaxWsBean ()
specifier|public
name|void
name|testJaxWsBean
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpPost
name|post
init|=
operator|new
name|HttpPost
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT2
operator|+
literal|"/customerservice/customers"
argument_list|)
decl_stmt|;
name|post
operator|.
name|addHeader
argument_list|(
literal|"Accept"
argument_list|,
literal|"text/xml"
argument_list|)
expr_stmt|;
name|String
name|body
init|=
literal|"<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
operator|+
literal|"<soap:Body><GetPerson xmlns=\"http://camel.apache.org/wsdl-first/types\">"
operator|+
literal|"<personId>hello</personId></GetPerson></soap:Body></soap:Envelope>"
decl_stmt|;
name|StringEntity
name|entity
init|=
operator|new
name|StringEntity
argument_list|(
name|body
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
name|post
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
name|post
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
name|String
name|responseBody
init|=
name|EntityUtils
operator|.
name|toString
argument_list|(
name|response
operator|.
name|getEntity
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|correct
init|=
literal|"<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Body>"
operator|+
literal|"<GetPersonResponse xmlns=\"http://camel.apache.org/wsdl-first/types\">"
operator|+
literal|"<personId>hello</personId><ssn>000-000-0000</ssn><name>Bonjour</name></GetPersonResponse></soap:Body></soap:Envelope>"
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong response"
argument_list|,
name|correct
argument_list|,
name|responseBody
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
DECL|method|testJaxWsBeanFromCxfRoute ()
specifier|public
name|void
name|testJaxWsBeanFromCxfRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|wsdlURL
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"person.wsdl"
argument_list|)
decl_stmt|;
name|PersonService
name|ss
init|=
operator|new
name|PersonService
argument_list|(
name|wsdlURL
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://camel.apache.org/wsdl-first"
argument_list|,
literal|"PersonService"
argument_list|)
argument_list|)
decl_stmt|;
name|Person
name|client
init|=
name|ss
operator|.
name|getSoap
argument_list|()
decl_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|client
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|,
literal|"http://localhost:"
operator|+
name|CXFTestSupport
operator|.
name|getPort1
argument_list|()
operator|+
literal|"/CxfBeanTest/PersonService/"
argument_list|)
expr_stmt|;
name|Holder
argument_list|<
name|String
argument_list|>
name|personId
init|=
operator|new
name|Holder
argument_list|<>
argument_list|()
decl_stmt|;
name|personId
operator|.
name|value
operator|=
literal|"hello"
expr_stmt|;
name|Holder
argument_list|<
name|String
argument_list|>
name|ssn
init|=
operator|new
name|Holder
argument_list|<>
argument_list|()
decl_stmt|;
name|Holder
argument_list|<
name|String
argument_list|>
name|name
init|=
operator|new
name|Holder
argument_list|<>
argument_list|()
decl_stmt|;
name|client
operator|.
name|getPerson
argument_list|(
name|personId
argument_list|,
name|ssn
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong personId"
argument_list|,
literal|"hello"
argument_list|,
name|personId
operator|.
name|value
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong SSN"
argument_list|,
literal|"000-000-0000"
argument_list|,
name|ssn
operator|.
name|value
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong name"
argument_list|,
literal|"Bonjour"
argument_list|,
name|name
operator|.
name|value
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

