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
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|CamelSpringTestSupport
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
name|CastUtils
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
DECL|class|CxfRsProducerTest
specifier|public
class|class
name|CxfRsProducerTest
extends|extends
name|CamelSpringTestSupport
block|{
DECL|class|JettyProcessor
specifier|public
specifier|static
class|class
name|JettyProcessor
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
comment|// check the query
name|Message
name|inMessage
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|inMessage
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_QUERY
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
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
literal|"org/apache/camel/component/cxf/jaxrs/CxfRsSpringProducer.xml"
argument_list|)
return|;
block|}
DECL|method|setupDestinationURL (Message inMessage)
specifier|protected
name|void
name|setupDestinationURL
parameter_list|(
name|Message
name|inMessage
parameter_list|)
block|{
comment|// do nothing here
block|}
annotation|@
name|Test
DECL|method|testGetConstumerWithClientProxyAPI ()
specifier|public
name|void
name|testGetConstumerWithClientProxyAPI
parameter_list|()
block|{
comment|// START SNIPPET: ProxyExample
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct://proxy"
argument_list|,
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
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
name|Message
name|inMessage
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|setupDestinationURL
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
comment|// set the operation name
name|inMessage
operator|.
name|setHeader
argument_list|(
name|CxfConstants
operator|.
name|OPERATION_NAME
argument_list|,
literal|"getCustomer"
argument_list|)
expr_stmt|;
comment|// using the proxy client API
name|inMessage
operator|.
name|setHeader
argument_list|(
name|CxfConstants
operator|.
name|CAMEL_CXF_RS_USING_HTTP_API
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
comment|// set the parameters , if you just have one parameter
comment|// camel will put this object into an Object[] itself
name|inMessage
operator|.
name|setBody
argument_list|(
literal|"123"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
comment|// get the response message
name|Customer
name|response
init|=
operator|(
name|Customer
operator|)
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"The response should not be null "
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong customer id "
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|response
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong customer name"
argument_list|,
name|response
operator|.
name|getName
argument_list|()
argument_list|,
literal|"John"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: ProxyExample
block|}
annotation|@
name|Test
DECL|method|testGetConstumersWithClientProxyAPI ()
specifier|public
name|void
name|testGetConstumersWithClientProxyAPI
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct://proxy"
argument_list|,
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
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
name|Message
name|inMessage
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|setupDestinationURL
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
comment|// set the operation name
name|inMessage
operator|.
name|setHeader
argument_list|(
name|CxfConstants
operator|.
name|OPERATION_NAME
argument_list|,
literal|"getCustomers"
argument_list|)
expr_stmt|;
comment|// using the proxy client API
name|inMessage
operator|.
name|setHeader
argument_list|(
name|CxfConstants
operator|.
name|CAMEL_CXF_RS_USING_HTTP_API
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
comment|// set the parameters , if you just have one parameter
comment|// camel will put this object into an Object[] itself
name|inMessage
operator|.
name|setBody
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
comment|// get the response message
name|List
argument_list|<
name|Customer
argument_list|>
name|response
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
operator|)
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"The response should not be null "
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong customer id "
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|response
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|,
literal|"113"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong customer name"
argument_list|,
name|response
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|,
literal|"Dan"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetConstumerWithHttpCentralClientAPI ()
specifier|public
name|void
name|testGetConstumerWithHttpCentralClientAPI
parameter_list|()
block|{
comment|// START SNIPPET: HttpExample
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct://http"
argument_list|,
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
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
name|Message
name|inMessage
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|setupDestinationURL
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
comment|// using the http central client API
name|inMessage
operator|.
name|setHeader
argument_list|(
name|CxfConstants
operator|.
name|CAMEL_CXF_RS_USING_HTTP_API
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
comment|// set the Http method
name|inMessage
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
literal|"GET"
argument_list|)
expr_stmt|;
comment|// set the relative path
name|inMessage
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_PATH
argument_list|,
literal|"/customerservice/customers/123"
argument_list|)
expr_stmt|;
comment|// Specify the response class , cxfrs will use InputStream as the response object type
name|inMessage
operator|.
name|setHeader
argument_list|(
name|CxfConstants
operator|.
name|CAMEL_CXF_RS_RESPONSE_CLASS
argument_list|,
name|Customer
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// since we use the Get method, so we don't need to set the message body
name|inMessage
operator|.
name|setBody
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
comment|// get the response message
name|Customer
name|response
init|=
operator|(
name|Customer
operator|)
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"The response should not be null "
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong customer id "
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|response
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong customer name"
argument_list|,
name|response
operator|.
name|getName
argument_list|()
argument_list|,
literal|"John"
argument_list|)
expr_stmt|;
comment|// END SNIPPET: HttpExample
block|}
annotation|@
name|Test
DECL|method|testGetConstumerWithCxfRsEndpoint ()
specifier|public
name|void
name|testGetConstumerWithCxfRsEndpoint
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"cxfrs://http://localhost:9002?httpClientAPI=true"
argument_list|,
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
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
name|Message
name|inMessage
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
comment|// set the Http method
name|inMessage
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
literal|"GET"
argument_list|)
expr_stmt|;
comment|// set the relative path
name|inMessage
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_PATH
argument_list|,
literal|"/customerservice/customers/123"
argument_list|)
expr_stmt|;
comment|// Specify the response class , cxfrs will use InputStream as the response object type
name|inMessage
operator|.
name|setHeader
argument_list|(
name|CxfConstants
operator|.
name|CAMEL_CXF_RS_RESPONSE_CLASS
argument_list|,
name|Customer
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// since we use the Get method, so we don't need to set the message body
name|inMessage
operator|.
name|setBody
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
comment|// get the response message
name|Customer
name|response
init|=
operator|(
name|Customer
operator|)
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"The response should not be null "
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong customer id "
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|response
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|,
literal|"123"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong customer name"
argument_list|,
name|response
operator|.
name|getName
argument_list|()
argument_list|,
literal|"John"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAddCustomerUniqueResponseCodeWithHttpClientAPI ()
specifier|public
name|void
name|testAddCustomerUniqueResponseCodeWithHttpClientAPI
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"cxfrs://http://localhost:9002?httpClientAPI=true"
argument_list|,
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
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
name|Message
name|inMessage
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
comment|// set the Http method
name|inMessage
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
literal|"POST"
argument_list|)
expr_stmt|;
comment|// set the relative path
name|inMessage
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_PATH
argument_list|,
literal|"/customerservice/customersUniqueResponseCode"
argument_list|)
expr_stmt|;
comment|// create a new customer object
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
literal|9999
argument_list|)
expr_stmt|;
name|customer
operator|.
name|setName
argument_list|(
literal|"HttpClient"
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|setBody
argument_list|(
name|customer
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
comment|// get the response message
name|Response
name|response
init|=
operator|(
name|Response
operator|)
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"The response should not be null "
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"The response entity should not be null"
argument_list|,
name|response
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
comment|// check the response code
name|assertEquals
argument_list|(
literal|"Get a wrong response code"
argument_list|,
literal|201
argument_list|,
name|response
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
comment|// check the response code from message header
name|assertEquals
argument_list|(
literal|"Get a wrong response code"
argument_list|,
literal|201
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
block|}
annotation|@
name|Test
DECL|method|testAddCustomerUniqueResponseCodeWithProxyAPI ()
specifier|public
name|void
name|testAddCustomerUniqueResponseCodeWithProxyAPI
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct://proxy"
argument_list|,
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
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
name|Message
name|inMessage
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|setupDestinationURL
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
comment|// set the operation name
name|inMessage
operator|.
name|setHeader
argument_list|(
name|CxfConstants
operator|.
name|OPERATION_NAME
argument_list|,
literal|"addCustomerUniqueResponseCode"
argument_list|)
expr_stmt|;
comment|// using the proxy client API
name|inMessage
operator|.
name|setHeader
argument_list|(
name|CxfConstants
operator|.
name|CAMEL_CXF_RS_USING_HTTP_API
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
comment|// set the parameters , if you just have one parameter
comment|// camel will put this object into an Object[] itself
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
literal|8888
argument_list|)
expr_stmt|;
name|customer
operator|.
name|setName
argument_list|(
literal|"ProxyAPI"
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|setBody
argument_list|(
name|customer
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
comment|// get the response message
name|Response
name|response
init|=
operator|(
name|Response
operator|)
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"The response should not be null "
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"The response entity should not be null"
argument_list|,
name|response
operator|.
name|getEntity
argument_list|()
argument_list|)
expr_stmt|;
comment|// check the response code
name|assertEquals
argument_list|(
literal|"Get a wrong response code"
argument_list|,
literal|201
argument_list|,
name|response
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
comment|// check the response code from message header
name|assertEquals
argument_list|(
literal|"Get a wrong response code"
argument_list|,
literal|201
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
block|}
annotation|@
name|Test
DECL|method|testAddCustomerUniqueResponseCode ()
specifier|public
name|void
name|testAddCustomerUniqueResponseCode
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"cxfrs://http://localhost:9002?httpClientAPI=true"
argument_list|,
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
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
name|Message
name|inMessage
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
comment|// set the Http method
name|inMessage
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
literal|"POST"
argument_list|)
expr_stmt|;
comment|// set the relative path
name|inMessage
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_PATH
argument_list|,
literal|"/customerservice/customersUniqueResponseCode"
argument_list|)
expr_stmt|;
comment|// put the response's entity into out message body
name|inMessage
operator|.
name|setHeader
argument_list|(
name|CxfConstants
operator|.
name|CAMEL_CXF_RS_RESPONSE_CLASS
argument_list|,
name|Customer
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// create a new customer object
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
literal|8888
argument_list|)
expr_stmt|;
name|customer
operator|.
name|setName
argument_list|(
literal|"Willem"
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|setBody
argument_list|(
name|customer
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
comment|// get the response message
name|Customer
name|response
init|=
operator|(
name|Customer
operator|)
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Response is "
operator|+
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"The response should not be null "
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Get a wrong customer id "
argument_list|,
name|response
operator|.
name|getId
argument_list|()
operator|!=
literal|8888
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong customer name"
argument_list|,
name|response
operator|.
name|getName
argument_list|()
argument_list|,
literal|"Willem"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testProducerWithQueryParameters ()
specifier|public
name|void
name|testProducerWithQueryParameters
parameter_list|()
block|{
comment|// START SNIPPET: QueryExample
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"cxfrs://http://localhost:9003/testQuery?httpClientAPI=true&q1=12&q2=13"
comment|// END SNIPPET: QueryExample
argument_list|,
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
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
name|Message
name|inMessage
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
comment|// set the Http method
name|inMessage
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
literal|"GET"
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|setHeader
argument_list|(
name|CxfConstants
operator|.
name|CAMEL_CXF_RS_RESPONSE_CLASS
argument_list|,
name|InputStream
operator|.
name|class
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|setBody
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
comment|// get the response message
name|String
name|response
init|=
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
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"The response should not be null "
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The response value is wrong"
argument_list|,
literal|"q1=12&q2=13"
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testProducerWithQueryParametersHeader ()
specifier|public
name|void
name|testProducerWithQueryParametersHeader
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"cxfrs://http://localhost:9003/testQuery?httpClientAPI=true&q1=12&q2=13"
argument_list|,
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
name|setPattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
name|Message
name|inMessage
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
comment|// set the Http method
name|inMessage
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
literal|"GET"
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|setHeader
argument_list|(
name|CxfConstants
operator|.
name|CAMEL_CXF_RS_RESPONSE_CLASS
argument_list|,
name|InputStream
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// override the parameter setting from URI
comment|// START SNIPPET: QueryMapExample
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|queryMap
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|queryMap
operator|.
name|put
argument_list|(
literal|"q1"
argument_list|,
literal|"new"
argument_list|)
expr_stmt|;
name|queryMap
operator|.
name|put
argument_list|(
literal|"q2"
argument_list|,
literal|"world"
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|setHeader
argument_list|(
name|CxfConstants
operator|.
name|CAMEL_CXF_RS_QUERY_MAP
argument_list|,
name|queryMap
argument_list|)
expr_stmt|;
comment|// END SNIPPET: QueryMapExample
name|inMessage
operator|.
name|setBody
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
comment|// get the response message
name|String
name|response
init|=
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
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"The response should not be null "
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The response value is wrong"
argument_list|,
literal|"q1=new&q2=world"
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRestServerDirectlyGetCustomer ()
specifier|public
name|void
name|testRestServerDirectlyGetCustomer
parameter_list|()
block|{
comment|// we cannot convert directly to Customer as we need camel-jaxb
name|String
name|response
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"cxfrs:http://localhost:9002/customerservice/customers/123"
argument_list|,
literal|null
argument_list|,
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
literal|"GET"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"The response should not be null "
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRestServerDirectlyAddCustomer ()
specifier|public
name|void
name|testRestServerDirectlyAddCustomer
parameter_list|()
block|{
name|Customer
name|input
init|=
operator|new
name|Customer
argument_list|()
decl_stmt|;
name|input
operator|.
name|setName
argument_list|(
literal|"Donald Duck"
argument_list|)
expr_stmt|;
comment|// we cannot convert directly to Customer as we need camel-jaxb
name|String
name|response
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
literal|"cxfrs:http://localhost:9002/customerservice/customers"
argument_list|,
name|input
argument_list|,
name|Exchange
operator|.
name|HTTP_METHOD
argument_list|,
literal|"POST"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|response
operator|.
name|endsWith
argument_list|(
literal|"<name>Donald Duck</name></Customer>"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

