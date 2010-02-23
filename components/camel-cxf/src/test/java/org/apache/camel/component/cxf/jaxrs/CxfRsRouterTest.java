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
name|CamelSpringTestSupport
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
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractXmlApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_class
DECL|class|CxfRsRouterTest
specifier|public
class|class
name|CxfRsRouterTest
extends|extends
name|CamelSpringTestSupport
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
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/cxf/jaxrs/CxfRsSpringRouter.xml"
argument_list|)
return|;
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
literal|"http://localhost:9000/customerservice/customers/123"
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
literal|"http://localhost:9000/customerservice/customers/"
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
literal|"http://localhost:9000/customerservice/orders/223/products/323"
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
literal|"http://localhost:9000/customerservice/customers"
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
literal|"http://localhost:9000/customerservice/customers"
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
literal|"http://localhost:9000/customerservice/customersUniqueResponseCode"
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
block|}
end_class

end_unit

