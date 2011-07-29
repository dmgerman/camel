begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|impl
operator|.
name|DefaultExchange
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
name|camel
operator|.
name|util
operator|.
name|URISupport
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
name|bus
operator|.
name|CXFBusFactory
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
name|Client
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
name|frontend
operator|.
name|ServerFactoryBean
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
name|helpers
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
name|cxf
operator|.
name|message
operator|.
name|MessageContentsList
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|CxfProducerRouterTest
specifier|public
class|class
name|CxfProducerRouterTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CxfProducerRouterTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|SIMPLE_SERVER_ADDRESS
specifier|private
specifier|static
specifier|final
name|String
name|SIMPLE_SERVER_ADDRESS
init|=
literal|"http://localhost:"
operator|+
name|CXFTestSupport
operator|.
name|getPort1
argument_list|()
operator|+
literal|"/CxfProducerRouterTest/test"
decl_stmt|;
DECL|field|REQUEST_MESSAGE
specifier|private
specifier|static
specifier|final
name|String
name|REQUEST_MESSAGE
init|=
literal|"<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
operator|+
literal|"<soap:Body><ns1:echo xmlns:ns1=\"http://cxf.component.camel.apache.org/\">"
operator|+
literal|"<arg0 xmlns=\"http://cxf.component.camel.apache.org/\">Hello World!</arg0>"
operator|+
literal|"</ns1:echo></soap:Body></soap:Envelope>"
decl_stmt|;
DECL|field|REQUEST_PAYLOAD
specifier|private
specifier|static
specifier|final
name|String
name|REQUEST_PAYLOAD
init|=
literal|"<ns1:echo xmlns:ns1=\"http://cxf.component.camel.apache.org/\">"
operator|+
literal|"<arg0 xmlns=\"http://cxf.component.camel.apache.org/\">Hello World!</arg0></ns1:echo>"
decl_stmt|;
DECL|field|ECHO_OPERATION
specifier|private
specifier|static
specifier|final
name|String
name|ECHO_OPERATION
init|=
literal|"echo"
decl_stmt|;
DECL|field|TEST_MESSAGE
specifier|private
specifier|static
specifier|final
name|String
name|TEST_MESSAGE
init|=
literal|"Hello World!"
decl_stmt|;
annotation|@
name|Override
DECL|method|isCreateCamelContextPerClass ()
specifier|public
name|boolean
name|isCreateCamelContextPerClass
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|BeforeClass
DECL|method|startServer ()
specifier|public
specifier|static
name|void
name|startServer
parameter_list|()
throws|throws
name|Exception
block|{
comment|// start a simple front service
name|ServerFactoryBean
name|svrBean
init|=
operator|new
name|ServerFactoryBean
argument_list|()
decl_stmt|;
name|svrBean
operator|.
name|setAddress
argument_list|(
name|SIMPLE_SERVER_ADDRESS
argument_list|)
expr_stmt|;
name|svrBean
operator|.
name|setServiceClass
argument_list|(
name|HelloService
operator|.
name|class
argument_list|)
expr_stmt|;
name|svrBean
operator|.
name|setServiceBean
argument_list|(
operator|new
name|HelloServiceImpl
argument_list|()
argument_list|)
expr_stmt|;
name|svrBean
operator|.
name|setBus
argument_list|(
name|CXFBusFactory
operator|.
name|getDefaultBus
argument_list|()
argument_list|)
expr_stmt|;
name|svrBean
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
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
name|from
argument_list|(
literal|"direct:EndpointA"
argument_list|)
operator|.
name|to
argument_list|(
name|getSimpleEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:EndpointB"
argument_list|)
operator|.
name|to
argument_list|(
name|getSimpleEndpointUri
argument_list|()
operator|+
literal|"&dataFormat=MESSAGE"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:EndpointC"
argument_list|)
operator|.
name|to
argument_list|(
name|getSimpleEndpointUri
argument_list|()
operator|+
literal|"&dataFormat=PAYLOAD"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|testCxfEndpointUris ()
specifier|public
name|void
name|testCxfEndpointUris
parameter_list|()
throws|throws
name|URISyntaxException
block|{
name|CxfEndpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|getSimpleEndpointUri
argument_list|()
argument_list|,
name|CxfEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong endpoint uri"
argument_list|,
name|getSimpleEndpointUri
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|getSimpleEndpointUri
argument_list|()
operator|+
literal|"&dataFormat=MESSAGE"
argument_list|,
name|CxfEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong endpoint uri"
argument_list|,
name|URISupport
operator|.
name|normalizeUri
argument_list|(
name|getSimpleEndpointUri
argument_list|()
operator|+
literal|"&dataFormat=MESSAGE"
argument_list|)
argument_list|,
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInvokingSimpleServerWithParams ()
specifier|public
name|void
name|testInvokingSimpleServerWithParams
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: sending
name|Exchange
name|senderExchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|)
decl_stmt|;
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|params
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
comment|// Prepare the request message for the camel-cxf procedure
name|params
operator|.
name|add
argument_list|(
name|TEST_MESSAGE
argument_list|)
expr_stmt|;
name|senderExchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|params
argument_list|)
expr_stmt|;
name|senderExchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CxfConstants
operator|.
name|OPERATION_NAME
argument_list|,
name|ECHO_OPERATION
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:EndpointA"
argument_list|,
name|senderExchange
argument_list|)
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
comment|// The response message's body is an MessageContentsList which first element is the return value of the operation,
comment|// If there are some holder parameters, the holder parameter will be filled in the reset of List.
comment|// The result will be extract from the MessageContentsList with the String class type
name|MessageContentsList
name|result
init|=
operator|(
name|MessageContentsList
operator|)
name|out
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Received output text: "
operator|+
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|responseContext
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
operator|)
name|out
operator|.
name|getHeader
argument_list|(
name|Client
operator|.
name|RESPONSE_CONTEXT
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|responseContext
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"We should get the response context here"
argument_list|,
literal|"UTF-8"
argument_list|,
name|responseContext
operator|.
name|get
argument_list|(
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
name|ENCODING
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Reply body on Camel is wrong"
argument_list|,
literal|"echo "
operator|+
name|TEST_MESSAGE
argument_list|,
name|result
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
comment|// END SNIPPET: sending
block|}
annotation|@
name|Test
DECL|method|testInvokingSimpleServerWithMessageDataFormat ()
specifier|public
name|void
name|testInvokingSimpleServerWithMessageDataFormat
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|senderExchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|)
decl_stmt|;
name|senderExchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|REQUEST_MESSAGE
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:EndpointB"
argument_list|,
name|senderExchange
argument_list|)
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|String
name|response
init|=
name|out
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"It should has the echo message"
argument_list|,
name|response
operator|.
name|indexOf
argument_list|(
literal|"echo "
operator|+
name|TEST_MESSAGE
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"It should has the echoResponse tag"
argument_list|,
name|response
operator|.
name|indexOf
argument_list|(
literal|"echoResponse"
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInvokingSimpleServerWithPayLoadDataFormat ()
specifier|public
name|void
name|testInvokingSimpleServerWithPayLoadDataFormat
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|senderExchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|)
decl_stmt|;
name|senderExchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|REQUEST_PAYLOAD
argument_list|)
expr_stmt|;
comment|// We need to specify the operation name to help CxfProducer to look up the BindingOperationInfo
name|senderExchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CxfConstants
operator|.
name|OPERATION_NAME
argument_list|,
literal|"echo"
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:EndpointC"
argument_list|,
name|senderExchange
argument_list|)
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|String
name|response
init|=
name|out
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"It should has the echo message"
argument_list|,
name|response
operator|.
name|indexOf
argument_list|(
literal|"echo "
operator|+
name|TEST_MESSAGE
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"It should has the echoResponse tag"
argument_list|,
name|response
operator|.
name|indexOf
argument_list|(
literal|"echoResponse"
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
name|senderExchange
operator|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
name|senderExchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|REQUEST_PAYLOAD
argument_list|)
expr_stmt|;
comment|// Don't specify operation information here
name|exchange
operator|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:EndpointC"
argument_list|,
name|senderExchange
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Expect exception here."
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|exchange
operator|.
name|getException
argument_list|()
operator|instanceof
name|IllegalArgumentException
argument_list|)
expr_stmt|;
block|}
DECL|method|getSimpleEndpointUri ()
specifier|private
name|String
name|getSimpleEndpointUri
parameter_list|()
block|{
return|return
literal|"cxf://"
operator|+
name|SIMPLE_SERVER_ADDRESS
operator|+
literal|"?serviceClass=org.apache.camel.component.cxf.HelloService"
return|;
block|}
block|}
end_class

end_unit

