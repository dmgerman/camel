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
name|commons
operator|.
name|httpclient
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
name|commons
operator|.
name|httpclient
operator|.
name|methods
operator|.
name|GetMethod
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
name|methods
operator|.
name|PostMethod
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
name|methods
operator|.
name|PutMethod
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
name|methods
operator|.
name|RequestEntity
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
name|methods
operator|.
name|StringRequestEntity
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
name|GetMethod
name|get
init|=
operator|new
name|GetMethod
argument_list|(
literal|"http://localhost:9000/customerservice/customers/123"
argument_list|)
decl_stmt|;
name|get
operator|.
name|addRequestHeader
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
name|HttpClient
argument_list|()
decl_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|httpclient
operator|.
name|executeMethod
argument_list|(
name|get
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"{\"Customer\":{\"id\":123,\"name\":\"John\"}}"
argument_list|,
name|get
operator|.
name|getResponseBodyAsString
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|get
operator|.
name|releaseConnection
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
name|GetMethod
name|get
init|=
operator|new
name|GetMethod
argument_list|(
literal|"http://localhost:9000/customerservice/customers/"
argument_list|)
decl_stmt|;
name|get
operator|.
name|addRequestHeader
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
name|HttpClient
argument_list|()
decl_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|httpclient
operator|.
name|executeMethod
argument_list|(
name|get
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<Customers><Customer><id>123</id><name>John</name></Customer><Customer><id>113</id><name>Dan</name></Customer></Customers>"
argument_list|,
name|get
operator|.
name|getResponseBodyAsString
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|get
operator|.
name|releaseConnection
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
name|GetMethod
name|get
init|=
operator|new
name|GetMethod
argument_list|(
literal|"http://localhost:9000/customerservice/orders/223/products/323"
argument_list|)
decl_stmt|;
name|get
operator|.
name|addRequestHeader
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
name|HttpClient
argument_list|()
decl_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|httpclient
operator|.
name|executeMethod
argument_list|(
name|get
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"{\"Product\":{\"description\":\"product 323\",\"id\":323}}"
argument_list|,
name|get
operator|.
name|getResponseBodyAsString
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|get
operator|.
name|releaseConnection
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
name|PutMethod
name|put
init|=
operator|new
name|PutMethod
argument_list|(
literal|"http://localhost:9000/customerservice/customers"
argument_list|)
decl_stmt|;
name|RequestEntity
name|entity
init|=
operator|new
name|StringRequestEntity
argument_list|(
name|PUT_REQUEST
argument_list|,
literal|"text/xml"
argument_list|,
literal|"ISO-8859-1"
argument_list|)
decl_stmt|;
name|put
operator|.
name|setRequestEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|HttpClient
name|httpclient
init|=
operator|new
name|HttpClient
argument_list|()
decl_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|httpclient
operator|.
name|executeMethod
argument_list|(
name|put
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|put
operator|.
name|getResponseBodyAsString
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|put
operator|.
name|releaseConnection
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
name|PostMethod
name|post
init|=
operator|new
name|PostMethod
argument_list|(
literal|"http://localhost:9000/customerservice/customers"
argument_list|)
decl_stmt|;
name|post
operator|.
name|addRequestHeader
argument_list|(
literal|"Accept"
argument_list|,
literal|"text/xml"
argument_list|)
expr_stmt|;
name|RequestEntity
name|entity
init|=
operator|new
name|StringRequestEntity
argument_list|(
name|POST_REQUEST
argument_list|,
literal|"text/xml"
argument_list|,
literal|"ISO-8859-1"
argument_list|)
decl_stmt|;
name|post
operator|.
name|setRequestEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|HttpClient
name|httpclient
init|=
operator|new
name|HttpClient
argument_list|()
decl_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|httpclient
operator|.
name|executeMethod
argument_list|(
name|post
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><Customer><id>124</id><name>Jack</name></Customer>"
argument_list|,
name|post
operator|.
name|getResponseBodyAsString
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|post
operator|.
name|releaseConnection
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
name|PostMethod
name|post
init|=
operator|new
name|PostMethod
argument_list|(
literal|"http://localhost:9000/customerservice/customersUniqueResponseCode"
argument_list|)
decl_stmt|;
name|post
operator|.
name|addRequestHeader
argument_list|(
literal|"Accept"
argument_list|,
literal|"text/xml"
argument_list|)
expr_stmt|;
name|RequestEntity
name|entity
init|=
operator|new
name|StringRequestEntity
argument_list|(
name|POST_REQUEST
argument_list|,
literal|"text/xml"
argument_list|,
literal|"ISO-8859-1"
argument_list|)
decl_stmt|;
name|post
operator|.
name|setRequestEntity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
name|HttpClient
name|httpclient
init|=
operator|new
name|HttpClient
argument_list|()
decl_stmt|;
try|try
block|{
name|assertEquals
argument_list|(
literal|201
argument_list|,
name|httpclient
operator|.
name|executeMethod
argument_list|(
name|post
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><Customer><id>124</id><name>Jack</name></Customer>"
argument_list|,
name|post
operator|.
name|getResponseBodyAsString
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|post
operator|.
name|releaseConnection
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

