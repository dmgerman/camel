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
name|SocketTimeoutException
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
name|hello_world_soap_http
operator|.
name|Greeter
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
DECL|class|CxfTimeoutTest
specifier|public
class|class
name|CxfTimeoutTest
extends|extends
name|CamelSpringTestSupport
block|{
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
DECL|field|JAXWS_SERVER_ADDRESS
specifier|protected
specifier|static
specifier|final
name|String
name|JAXWS_SERVER_ADDRESS
init|=
literal|"http://localhost:9023/SoapContext/SoapPort"
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|startService ()
specifier|public
specifier|static
name|void
name|startService
parameter_list|()
block|{
name|Greeter
name|implementor
init|=
operator|new
name|GreeterImplWithSleep
argument_list|()
decl_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|JAXWS_SERVER_ADDRESS
argument_list|,
name|implementor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInvokingJaxWsServerWithBusUriParams ()
specifier|public
name|void
name|testInvokingJaxWsServerWithBusUriParams
parameter_list|()
throws|throws
name|Exception
block|{
name|sendTimeOutMessage
argument_list|(
literal|"cxf://"
operator|+
name|JAXWS_SERVER_ADDRESS
operator|+
literal|"?serviceClass=org.apache.hello_world_soap_http.Greeter&bus=#cxf"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInvokingJaxWsServerWithoutBusUriParams ()
specifier|public
name|void
name|testInvokingJaxWsServerWithoutBusUriParams
parameter_list|()
throws|throws
name|Exception
block|{
name|sendTimeOutMessage
argument_list|(
literal|"cxf://"
operator|+
name|JAXWS_SERVER_ADDRESS
operator|+
literal|"?serviceClass=org.apache.hello_world_soap_http.Greeter"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInvokingJaxWsServerWithCxfEndpoint ()
specifier|public
name|void
name|testInvokingJaxWsServerWithCxfEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|sendTimeOutMessage
argument_list|(
literal|"cxf://bean:springEndpoint"
argument_list|)
expr_stmt|;
block|}
DECL|method|sendTimeOutMessage (String endpointUri)
specifier|protected
name|void
name|sendTimeOutMessage
parameter_list|(
name|String
name|endpointUri
parameter_list|)
throws|throws
name|Exception
block|{
name|Exchange
name|reply
init|=
name|sendJaxWsMessage
argument_list|(
name|endpointUri
argument_list|)
decl_stmt|;
name|Exception
name|e
init|=
name|reply
operator|.
name|getException
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"We should get the exception cause here"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"We should get the socket time out exception here"
argument_list|,
name|e
operator|instanceof
name|SocketTimeoutException
argument_list|)
expr_stmt|;
block|}
DECL|method|sendJaxWsMessage (String endpointUri)
specifier|protected
name|Exchange
name|sendJaxWsMessage
parameter_list|(
name|String
name|endpointUri
parameter_list|)
throws|throws
name|InterruptedException
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
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
argument_list|<
name|String
argument_list|>
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
block|}
block|}
argument_list|)
decl_stmt|;
return|return
name|exchange
return|;
block|}
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
comment|// we can put the http conduit configuration here
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/cxf/cxfConduitTimeOutContext.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

