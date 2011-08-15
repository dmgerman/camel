begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.osgi.cxf.blueprint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|osgi
operator|.
name|cxf
operator|.
name|blueprint
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
name|itest
operator|.
name|osgi
operator|.
name|blueprint
operator|.
name|OSGiBlueprintTestSupport
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
name|itest
operator|.
name|osgi
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|testbean
operator|.
name|CustomerService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|endpoint
operator|.
name|Server
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|JAXRSServerFactoryBean
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
name|AfterClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|Option
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|junit
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|junit
operator|.
name|JUnit4TestRunner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Constants
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|OptionUtils
operator|.
name|combine
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|container
operator|.
name|def
operator|.
name|PaxRunnerOptions
operator|.
name|scanFeatures
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|swissbox
operator|.
name|tinybundles
operator|.
name|core
operator|.
name|TinyBundles
operator|.
name|newBundle
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|JUnit4TestRunner
operator|.
name|class
argument_list|)
DECL|class|CxfRsBlueprintRouterTest
specifier|public
class|class
name|CxfRsBlueprintRouterTest
extends|extends
name|OSGiBlueprintTestSupport
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
DECL|field|POST_REQUEST
specifier|private
specifier|static
specifier|final
name|String
name|POST_REQUEST
init|=
literal|"<Customer><name>Jack</name></Customer>"
decl_stmt|;
DECL|field|server
specifier|private
specifier|static
name|Server
name|server
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|startServer ()
specifier|public
specifier|static
name|void
name|startServer
parameter_list|()
block|{
name|JAXRSServerFactoryBean
name|sf
init|=
operator|new
name|JAXRSServerFactoryBean
argument_list|()
decl_stmt|;
name|sf
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:9002/rest"
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setServiceBean
argument_list|(
operator|new
name|CustomerService
argument_list|()
argument_list|)
expr_stmt|;
name|sf
operator|.
name|setStaticSubresourceResolution
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|server
operator|=
name|sf
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
annotation|@
name|AfterClass
DECL|method|stopServer ()
specifier|public
specifier|static
name|void
name|stopServer
parameter_list|()
block|{
if|if
condition|(
name|server
operator|!=
literal|null
condition|)
block|{
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|doPostSetup ()
specifier|protected
name|void
name|doPostSetup
parameter_list|()
throws|throws
name|Exception
block|{
name|getInstalledBundle
argument_list|(
literal|"CxfRsBlueprintRouterTest"
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
name|getOsgiService
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|,
literal|"(camel.context.symbolicname=CxfRsBlueprintRouterTest)"
argument_list|,
literal|10000
argument_list|)
expr_stmt|;
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
name|HttpGet
name|get
init|=
operator|new
name|HttpGet
argument_list|(
literal|"http://localhost:9000/route/customerservice/customers/123"
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
literal|"{\"Customer\":{\"id\":123,\"name\":\"John\"}}"
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
annotation|@
name|Test
DECL|method|testGetCustomerWithQuery ()
specifier|public
name|void
name|testGetCustomerWithQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpGet
name|get
init|=
operator|new
name|HttpGet
argument_list|(
literal|"http://localhost:9000/route/customerservice/customers?id=123"
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
literal|"{\"Customer\":{\"id\":123,\"name\":\"John\"}}"
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
annotation|@
name|Test
DECL|method|testGetCustomers ()
specifier|public
name|void
name|testGetCustomers
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpGet
name|get
init|=
operator|new
name|HttpGet
argument_list|(
literal|"http://localhost:9000/route/customerservice/customers/"
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
comment|// order returned can differ on OS so match for both orders
name|String
name|s
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
name|boolean
name|m1
init|=
literal|"<Customers><Customer><id>123</id><name>John</name></Customer><Customer><id>113</id><name>Dan</name></Customer></Customers>"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|boolean
name|m2
init|=
literal|"<Customers><Customer><id>113</id><name>Dan</name></Customer><Customer><id>123</id><name>John</name></Customer></Customers>"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|m1
operator|&&
operator|!
name|m2
condition|)
block|{
name|fail
argument_list|(
literal|"Not expected body returned: "
operator|+
name|s
argument_list|)
expr_stmt|;
block|}
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
annotation|@
name|Test
DECL|method|testGetSubResource ()
specifier|public
name|void
name|testGetSubResource
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpGet
name|get
init|=
operator|new
name|HttpGet
argument_list|(
literal|"http://localhost:9000/route/customerservice/orders/223/products/323"
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
literal|"{\"Product\":{\"description\":\"product 323\",\"id\":323}}"
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
literal|"http://localhost:9000/route/customerservice/customers"
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
literal|"http://localhost:9000/route/customerservice/customers"
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
name|assertEquals
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><Customer><id>124</id><name>Jack</name></Customer>"
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
annotation|@
name|Configuration
DECL|method|configure ()
specifier|public
specifier|static
name|Option
index|[]
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|Option
index|[]
name|options
init|=
name|combine
argument_list|(
name|getDefaultCamelKarafOptions
argument_list|()
argument_list|,
comment|// using the features to install the camel components
name|scanFeatures
argument_list|(
name|getCamelKarafFeatureUrl
argument_list|()
argument_list|,
literal|"camel-blueprint"
argument_list|,
literal|"camel-http4"
argument_list|,
literal|"camel-cxf"
argument_list|)
argument_list|,
name|bundle
argument_list|(
name|newBundle
argument_list|()
operator|.
name|add
argument_list|(
literal|"OSGI-INF/blueprint/test.xml"
argument_list|,
name|CxfRsBlueprintRouterTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"CxfRsBlueprintRouter.xml"
argument_list|)
argument_list|)
operator|.
name|add
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|osgi
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|testbean
operator|.
name|Customer
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|osgi
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|testbean
operator|.
name|CustomerService
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|osgi
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|testbean
operator|.
name|CustomerServiceResource
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|osgi
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|testbean
operator|.
name|Order
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|osgi
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|testbean
operator|.
name|Product
operator|.
name|class
argument_list|)
operator|.
name|set
argument_list|(
name|Constants
operator|.
name|BUNDLE_SYMBOLICNAME
argument_list|,
literal|"CxfRsBlueprintRouterTest"
argument_list|)
operator|.
name|set
argument_list|(
name|Constants
operator|.
name|DYNAMICIMPORT_PACKAGE
argument_list|,
literal|"*"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|noStart
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|options
return|;
block|}
block|}
end_class

end_unit

