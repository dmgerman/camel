begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.cxf.blueprint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|cxf
operator|.
name|blueprint
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|test
operator|.
name|blueprint
operator|.
name|CamelBlueprintTestSupport
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
name|interceptor
operator|.
name|Fault
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
name|Message
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
name|phase
operator|.
name|AbstractPhaseInterceptor
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
name|phase
operator|.
name|Phase
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

begin_class
DECL|class|CxfPayloadProviderRouterTest
specifier|public
class|class
name|CxfPayloadProviderRouterTest
extends|extends
name|CamelBlueprintTestSupport
block|{
DECL|field|endpoint
specifier|protected
specifier|static
name|Endpoint
name|endpoint
decl_stmt|;
DECL|field|implementor
specifier|protected
specifier|static
name|GreeterImpl
name|implementor
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
annotation|@
name|AfterClass
DECL|method|stopService ()
specifier|public
specifier|static
name|void
name|stopService
parameter_list|()
block|{
if|if
condition|(
name|endpoint
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|BeforeClass
DECL|method|startService ()
specifier|public
specifier|static
name|void
name|startService
parameter_list|()
block|{
name|implementor
operator|=
operator|new
name|GreeterImpl
argument_list|()
expr_stmt|;
name|String
name|address
init|=
literal|"http://localhost:"
operator|+
name|CXFTestSupport
operator|.
name|getPort1
argument_list|()
operator|+
literal|"/CxfPayLoadProviderRouterTest/SoapContext/SoapPort"
decl_stmt|;
name|endpoint
operator|=
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
name|implementor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|useOverridePropertiesWithPropertiesComponent ()
specifier|protected
name|Properties
name|useOverridePropertiesWithPropertiesComponent
parameter_list|()
block|{
name|Properties
name|extra
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|extra
operator|.
name|put
argument_list|(
literal|"router.address"
argument_list|,
literal|"http://localhost:"
operator|+
name|CXFTestSupport
operator|.
name|getPort2
argument_list|()
operator|+
literal|"/CxfPayloadProviderRouterTest/CamelContext/RouterPort"
argument_list|)
expr_stmt|;
name|extra
operator|.
name|put
argument_list|(
literal|"service.address"
argument_list|,
literal|"http://localhost:"
operator|+
name|CXFTestSupport
operator|.
name|getPort1
argument_list|()
operator|+
literal|"/CxfPayLoadProviderRouterTest/SoapContext/SoapPort"
argument_list|)
expr_stmt|;
return|return
name|extra
return|;
block|}
annotation|@
name|Override
DECL|method|getBlueprintDescriptor ()
specifier|protected
name|String
name|getBlueprintDescriptor
parameter_list|()
block|{
return|return
literal|"org/apache/camel/test/cxf/blueprint/CxfPayloadProviderRouterBeans.xml"
return|;
block|}
annotation|@
name|Test
DECL|method|testPublishEndpointUrl ()
specifier|public
name|void
name|testPublishEndpointUrl
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|path
init|=
literal|"CxfPayloadProviderRouterTest/CamelContext/RouterPort"
decl_stmt|;
name|String
name|response
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"http://localhost:"
operator|+
name|CXFTestSupport
operator|.
name|getPort2
argument_list|()
operator|+
literal|"/"
operator|+
name|path
operator|+
literal|"?wsdl"
argument_list|,
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Can't find the right service location."
argument_list|,
name|response
operator|.
name|indexOf
argument_list|(
name|path
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testInvokeGreetMeOverProvider ()
specifier|public
name|void
name|testInvokeGreetMeOverProvider
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
literal|"/CamelContext/RouterPort"
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|endpoint
operator|.
name|Client
name|client
init|=
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|frontend
operator|.
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|greeter
argument_list|)
decl_stmt|;
name|VerifyInboundInterceptor
name|icp
init|=
operator|new
name|VerifyInboundInterceptor
argument_list|()
decl_stmt|;
name|client
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|icp
argument_list|)
expr_stmt|;
name|int
name|ic
init|=
name|implementor
operator|.
name|getInvocationCount
argument_list|()
decl_stmt|;
name|icp
operator|.
name|setCalled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
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
name|assertEquals
argument_list|(
literal|"Got the wrong reply "
argument_list|,
literal|"Hello test"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"No Inbound message received"
argument_list|,
name|icp
operator|.
name|isCalled
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The target service not invoked"
argument_list|,
operator|++
name|ic
argument_list|,
name|implementor
operator|.
name|getInvocationCount
argument_list|()
argument_list|)
expr_stmt|;
name|icp
operator|.
name|setCalled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|greeter
operator|.
name|greetMeOneWay
argument_list|(
literal|"call greetMe OneWay !"
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"An unnecessary inbound message"
argument_list|,
name|icp
operator|.
name|isCalled
argument_list|()
argument_list|)
expr_stmt|;
comment|// wait a few seconds for the async oneway service to be invoked
name|Thread
operator|.
name|sleep
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The target service not invoked"
argument_list|,
operator|++
name|ic
argument_list|,
name|implementor
operator|.
name|getInvocationCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|VerifyInboundInterceptor
specifier|static
class|class
name|VerifyInboundInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
DECL|field|called
specifier|private
name|boolean
name|called
decl_stmt|;
DECL|method|VerifyInboundInterceptor ()
name|VerifyInboundInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|USER_PROTOCOL
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|handleMessage (Message message)
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|called
operator|=
literal|true
expr_stmt|;
block|}
DECL|method|isCalled ()
specifier|public
name|boolean
name|isCalled
parameter_list|()
block|{
return|return
name|called
return|;
block|}
DECL|method|setCalled (boolean b)
specifier|public
name|void
name|setCalled
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
name|called
operator|=
name|b
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

