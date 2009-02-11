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
name|Endpoint
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
name|Service
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
name|component
operator|.
name|http
operator|.
name|HttpOperationFailedException
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
name|spring
operator|.
name|SpringCamelContext
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
name|jaxws
operator|.
name|EndpointImpl
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
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|NoSuchCodeLitFault
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
DECL|class|CXFGreeterRouterTest
specifier|public
class|class
name|CXFGreeterRouterTest
extends|extends
name|CxfRouterTestSupport
block|{
DECL|field|applicationContext
specifier|protected
name|AbstractXmlApplicationContext
name|applicationContext
decl_stmt|;
DECL|field|serviceName
specifier|private
specifier|final
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"SOAPService"
argument_list|)
decl_stmt|;
DECL|field|routerPortName
specifier|private
specifier|final
name|QName
name|routerPortName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"RouterPort"
argument_list|)
decl_stmt|;
DECL|field|testDocLitFaultBody
specifier|private
specifier|final
name|String
name|testDocLitFaultBody
init|=
literal|"<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
operator|+
literal|"<soap:Body><testDocLitFault xmlns=\"http://apache.org/hello_world_soap_http/types\">"
operator|+
literal|"<faultType>NoSuchCodeLitFault</faultType></testDocLitFault>"
operator|+
literal|"</soap:Body></soap:Envelope>"
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|applicationContext
operator|=
name|createApplicationContext
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have created a valid spring context"
argument_list|,
name|applicationContext
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|applicationContext
operator|!=
literal|null
condition|)
block|{
name|applicationContext
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|startService ()
specifier|protected
name|void
name|startService
parameter_list|()
block|{
name|Object
name|implementor
init|=
operator|new
name|GreeterImpl
argument_list|()
decl_stmt|;
name|String
name|address
init|=
literal|"http://localhost:9000/SoapContext/SoapPort"
decl_stmt|;
name|EndpointImpl
name|endpoint
init|=
operator|(
name|EndpointImpl
operator|)
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
name|implementor
argument_list|)
decl_stmt|;
name|server
operator|=
name|endpoint
operator|.
name|getServer
argument_list|()
expr_stmt|;
block|}
DECL|method|testInvokingServiceFromCXFClient ()
specifier|public
name|void
name|testInvokingServiceFromCXFClient
parameter_list|()
throws|throws
name|Exception
block|{
name|Service
name|service
init|=
name|Service
operator|.
name|create
argument_list|(
name|serviceName
argument_list|)
decl_stmt|;
name|service
operator|.
name|addPort
argument_list|(
name|routerPortName
argument_list|,
literal|"http://schemas.xmlsoap.org/soap/"
argument_list|,
literal|"http://localhost:9003/CamelContext/RouterPort"
argument_list|)
expr_stmt|;
name|Greeter
name|greeter
init|=
name|service
operator|.
name|getPort
argument_list|(
name|routerPortName
argument_list|,
name|Greeter
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|reply
init|=
name|greeter
operator|.
name|greetMe
argument_list|(
literal|"test"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No response received from service"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Got the wrong reply "
argument_list|,
literal|"Hello test"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|reply
operator|=
name|greeter
operator|.
name|sayHi
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"No response received from service"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Got the wrong reply "
argument_list|,
literal|"Bonjour"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|greeter
operator|.
name|greetMeOneWay
argument_list|(
literal|"call greetMe OneWay !"
argument_list|)
expr_stmt|;
comment|// test throw the exception
try|try
block|{
name|greeter
operator|.
name|testDocLitFault
argument_list|(
literal|"NoSuchCodeLitFault"
argument_list|)
expr_stmt|;
comment|// should get the exception here
name|fail
argument_list|(
literal|"Should get the NoSuchCodeLitFault here."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchCodeLitFault
name|fault
parameter_list|)
block|{
comment|// expect the fault here
name|assertNotNull
argument_list|(
literal|"The fault info should not be null"
argument_list|,
name|fault
operator|.
name|getFaultInfo
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testRoutingSOAPFault ()
specifier|public
name|void
name|testRoutingSOAPFault
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"http://localhost:9003/CamelContext/RouterPort"
argument_list|,
name|testDocLitFaultBody
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should get an exception here."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeCamelException
name|exception
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"It should get the response error"
argument_list|,
name|exception
operator|.
name|getCause
argument_list|()
operator|instanceof
name|HttpOperationFailedException
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong response code"
argument_list|,
operator|(
operator|(
name|HttpOperationFailedException
operator|)
name|exception
operator|.
name|getCause
argument_list|()
operator|)
operator|.
name|getStatusCode
argument_list|()
argument_list|,
literal|500
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|SpringCamelContext
operator|.
name|springCamelContext
argument_list|(
name|applicationContext
argument_list|)
return|;
block|}
DECL|method|createApplicationContext ()
specifier|protected
name|ClassPathXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/cxf/GreeterEndpointsRouterContext.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

