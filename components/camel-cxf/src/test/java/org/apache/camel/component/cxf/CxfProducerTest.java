begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ConnectException
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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Endpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
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
name|ProducerTemplate
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
name|converter
operator|.
name|CxfPayloadConverter
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
name|converter
operator|.
name|jaxp
operator|.
name|XmlConverter
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
name|DefaultCamelContext
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
name|AvailablePortFinder
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
name|BusFactory
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
name|hello_world_soap_http
operator|.
name|GreeterImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
DECL|class|CxfProducerTest
specifier|public
class|class
name|CxfProducerTest
extends|extends
name|Assert
block|{
DECL|field|ECHO_OPERATION
specifier|protected
specifier|static
specifier|final
name|String
name|ECHO_OPERATION
init|=
literal|"echo"
decl_stmt|;
DECL|field|GREET_ME_OPERATION
specifier|protected
specifier|static
specifier|final
name|String
name|GREET_ME_OPERATION
init|=
literal|"greetMe"
decl_stmt|;
DECL|field|TEST_MESSAGE
specifier|protected
specifier|static
specifier|final
name|String
name|TEST_MESSAGE
init|=
literal|"Hello World!"
decl_stmt|;
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CxfProducerTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|camelContext
specifier|protected
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|template
specifier|protected
name|ProducerTemplate
name|template
decl_stmt|;
DECL|field|server
specifier|protected
name|Server
name|server
decl_stmt|;
DECL|field|endpoint
specifier|protected
name|Endpoint
name|endpoint
decl_stmt|;
DECL|method|getSimpleServerAddress ()
specifier|protected
name|String
name|getSimpleServerAddress
parameter_list|()
block|{
return|return
literal|"http://localhost:"
operator|+
name|CXFTestSupport
operator|.
name|getPort1
argument_list|()
operator|+
literal|"/"
operator|+
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|"/test"
return|;
block|}
DECL|method|getJaxWsServerAddress ()
specifier|protected
name|String
name|getJaxWsServerAddress
parameter_list|()
block|{
return|return
literal|"http://localhost:"
operator|+
name|CXFTestSupport
operator|.
name|getPort2
argument_list|()
operator|+
literal|"/"
operator|+
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|"/test"
return|;
block|}
DECL|method|getWrongServerAddress ()
specifier|protected
name|String
name|getWrongServerAddress
parameter_list|()
block|{
comment|// Avoiding the test error on camel-cxf module
return|return
literal|"http://localhost:"
operator|+
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
operator|+
literal|"/"
operator|+
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|"/test"
return|;
block|}
annotation|@
name|Before
DECL|method|startService ()
specifier|public
name|void
name|startService
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
name|getSimpleServerAddress
argument_list|()
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
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
argument_list|)
expr_stmt|;
name|server
operator|=
name|svrBean
operator|.
name|create
argument_list|()
expr_stmt|;
name|GreeterImpl
name|greeterImpl
init|=
operator|new
name|GreeterImpl
argument_list|()
decl_stmt|;
name|endpoint
operator|=
name|Endpoint
operator|.
name|publish
argument_list|(
name|getJaxWsServerAddress
argument_list|()
argument_list|,
name|greeterImpl
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|stopServices ()
specifier|public
name|void
name|stopServices
parameter_list|()
throws|throws
name|Exception
block|{
name|endpoint
operator|.
name|stop
argument_list|()
expr_stmt|;
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
name|server
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|camelContext
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|camelContext
operator|.
name|start
argument_list|()
expr_stmt|;
name|template
operator|=
name|camelContext
operator|.
name|createProducerTemplate
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|stop
argument_list|()
expr_stmt|;
name|camelContext
operator|.
name|stop
argument_list|()
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
name|Exchange
name|exchange
init|=
name|sendSimpleMessage
argument_list|()
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
name|result
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
name|LOG
operator|.
name|info
argument_list|(
literal|"Received output text: "
operator|+
name|result
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
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
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
literal|"reply body on Camel"
argument_list|,
literal|"echo "
operator|+
name|TEST_MESSAGE
argument_list|,
name|result
argument_list|)
expr_stmt|;
comment|// check the other camel header copying
name|String
name|fileName
init|=
name|out
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Should get the file name from out message header"
argument_list|,
literal|"testFile"
argument_list|,
name|fileName
argument_list|)
expr_stmt|;
comment|// check if the header object is turned into String
name|Object
name|requestObject
init|=
name|out
operator|.
name|getHeader
argument_list|(
literal|"requestObject"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"We should get the right requestObject."
argument_list|,
name|requestObject
operator|instanceof
name|DefaultCxfBinding
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInvokingAWrongServer ()
specifier|public
name|void
name|testInvokingAWrongServer
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|reply
init|=
name|sendSimpleMessage
argument_list|(
name|getWrongEndpointUri
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"We should get the exception here"
argument_list|,
name|reply
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|reply
operator|.
name|getException
argument_list|()
operator|.
name|getCause
argument_list|()
operator|instanceof
name|ConnectException
argument_list|)
expr_stmt|;
comment|//Test the data format PAYLOAD
name|reply
operator|=
name|sendSimpleMessageWithPayloadMessage
argument_list|(
name|getWrongEndpointUri
argument_list|()
operator|+
literal|"&dataFormat=PAYLOAD"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"We should get the exception here"
argument_list|,
name|reply
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|reply
operator|.
name|getException
argument_list|()
operator|.
name|getCause
argument_list|()
operator|instanceof
name|ConnectException
argument_list|)
expr_stmt|;
comment|//Test the data format MESSAGE
name|reply
operator|=
name|sendSimpleMessageWithRawMessage
argument_list|(
name|getWrongEndpointUri
argument_list|()
operator|+
literal|"&dataFormat=RAW"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"We should get the exception here"
argument_list|,
name|reply
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|reply
operator|.
name|getException
argument_list|()
operator|.
name|getCause
argument_list|()
operator|instanceof
name|ConnectException
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInvokingJaxWsServerWithParams ()
specifier|public
name|void
name|testInvokingJaxWsServerWithParams
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|sendJaxWsMessage
argument_list|()
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
name|result
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
name|LOG
operator|.
name|info
argument_list|(
literal|"Received output text: "
operator|+
name|result
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
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
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
literal|"Get the wrong wsdl operation name"
argument_list|,
literal|"{http://apache.org/hello_world_soap_http}greetMe"
argument_list|,
name|responseContext
operator|.
name|get
argument_list|(
literal|"javax.xml.ws.wsdl.operation"
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"reply body on Camel"
argument_list|,
literal|"Hello "
operator|+
name|TEST_MESSAGE
argument_list|,
name|result
argument_list|)
expr_stmt|;
comment|// check the other camel header copying
name|String
name|fileName
init|=
name|out
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Should get the file name from out message header"
argument_list|,
literal|"testFile"
argument_list|,
name|fileName
argument_list|)
expr_stmt|;
block|}
DECL|method|getSimpleEndpointUri ()
specifier|protected
name|String
name|getSimpleEndpointUri
parameter_list|()
block|{
return|return
literal|"cxf://"
operator|+
name|getSimpleServerAddress
argument_list|()
operator|+
literal|"?serviceClass=org.apache.camel.component.cxf.HelloService"
return|;
block|}
DECL|method|getJaxwsEndpointUri ()
specifier|protected
name|String
name|getJaxwsEndpointUri
parameter_list|()
block|{
return|return
literal|"cxf://"
operator|+
name|getJaxWsServerAddress
argument_list|()
operator|+
literal|"?serviceClass=org.apache.hello_world_soap_http.Greeter"
return|;
block|}
DECL|method|getWrongEndpointUri ()
specifier|protected
name|String
name|getWrongEndpointUri
parameter_list|()
block|{
return|return
literal|"cxf://"
operator|+
name|getWrongServerAddress
argument_list|()
operator|+
literal|"?serviceClass=org.apache.camel.component.cxf.HelloService"
return|;
block|}
DECL|method|sendSimpleMessage ()
specifier|protected
name|Exchange
name|sendSimpleMessage
parameter_list|()
block|{
return|return
name|sendSimpleMessage
argument_list|(
name|getSimpleEndpointUri
argument_list|()
argument_list|)
return|;
block|}
DECL|method|sendSimpleMessage (String endpointUri)
specifier|private
name|Exchange
name|sendSimpleMessage
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
name|endpointUri
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|params
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|params
operator|.
name|add
argument_list|(
name|TEST_MESSAGE
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|params
argument_list|)
expr_stmt|;
name|exchange
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"testFile"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"requestObject"
argument_list|,
operator|new
name|DefaultCxfBinding
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|sendSimpleMessageWithRawMessage (String endpointUri)
specifier|private
name|Exchange
name|sendSimpleMessageWithRawMessage
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
name|endpointUri
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
operator|+
literal|"<soap:Body><ns1:echo xmlns:ns1=\"http://cxf.component.camel.apache.org/\">"
operator|+
literal|"<arg0 xmlns=\"http://cxf.component.camel.apache.org/\">hello world</arg0>"
operator|+
literal|"</ns1:echo></soap:Body></soap:Envelope>"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|sendSimpleMessageWithPayloadMessage (String endpointUri)
specifier|private
name|Exchange
name|sendSimpleMessageWithPayloadMessage
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
name|endpointUri
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Document
name|document
init|=
operator|new
name|XmlConverter
argument_list|()
operator|.
name|toDOMDocument
argument_list|(
literal|"<ns1:echo xmlns:ns1=\"http://cxf.component.camel.apache.org/\">"
operator|+
literal|"<arg0 xmlns=\"http://cxf.component.camel.apache.org/\">hello world</arg0>"
operator|+
literal|"</ns1:echo>"
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|CxfPayloadConverter
operator|.
name|documentToCxfPayload
argument_list|(
name|document
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
name|exchange
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
block|}
block|}
argument_list|)
decl_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|sendJaxWsMessage ()
specifier|protected
name|Exchange
name|sendJaxWsMessage
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
name|getJaxwsEndpointUri
argument_list|()
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|params
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|params
operator|.
name|add
argument_list|(
name|TEST_MESSAGE
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|params
argument_list|)
expr_stmt|;
name|exchange
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
name|GREET_ME_OPERATION
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"testFile"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
return|return
name|exchange
return|;
block|}
block|}
end_class

end_unit

