begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.transport
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
name|transport
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Future
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebMethod
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebParam
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebResult
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|WebService
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
name|AsyncHandler
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
name|Response
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
name|cxf
operator|.
name|Bus
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
name|jaxws
operator|.
name|JaxWsProxyFactoryBean
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

begin_class
DECL|class|JaxWSCamelTestSupport
specifier|public
class|class
name|JaxWSCamelTestSupport
extends|extends
name|CamelTestSupport
block|{
comment|/**      * Expected SOAP answer for the 'SampleWS.getSomething' method      */
DECL|field|ANSWER
specifier|public
specifier|static
specifier|final
name|String
name|ANSWER
init|=
literal|"<Envelope xmlns='http://schemas.xmlsoap.org/soap/envelope/'>"
operator|+
literal|"<Body>"
operator|+
literal|"<getSomethingResponse xmlns='urn:test'>"
operator|+
literal|"<result>Something</result>"
operator|+
literal|"</getSomethingResponse>"
operator|+
literal|"</Body>"
operator|+
literal|"</Envelope>"
decl_stmt|;
DECL|field|REQUEST
specifier|public
specifier|static
specifier|final
name|String
name|REQUEST
init|=
literal|"<Envelope xmlns='http://schemas.xmlsoap.org/soap/envelope/'>"
operator|+
literal|"<Body>"
operator|+
literal|"<getSomething xmlns='urn:test'/>"
operator|+
literal|"</Body>"
operator|+
literal|"</Envelope>"
decl_stmt|;
DECL|field|bus
specifier|private
name|Bus
name|bus
decl_stmt|;
comment|/**      * Sample WebService      */
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"urn:test"
argument_list|,
name|serviceName
operator|=
literal|"testService"
argument_list|,
name|portName
operator|=
literal|"testPort"
argument_list|)
DECL|interface|SampleWS
specifier|public
interface|interface
name|SampleWS
block|{
annotation|@
name|WebMethod
argument_list|(
name|operationName
operator|=
literal|"getSomething"
argument_list|)
annotation|@
name|WebResult
argument_list|(
name|name
operator|=
literal|"result"
argument_list|,
name|targetNamespace
operator|=
literal|"urn:test"
argument_list|)
DECL|method|getSomething ()
name|String
name|getSomething
parameter_list|()
function_decl|;
block|}
annotation|@
name|WebService
argument_list|(
name|targetNamespace
operator|=
literal|"urn:test"
argument_list|,
name|serviceName
operator|=
literal|"testService"
argument_list|,
name|portName
operator|=
literal|"testPort"
argument_list|)
DECL|interface|SampleWSAsync
specifier|public
interface|interface
name|SampleWSAsync
block|{
annotation|@
name|WebMethod
argument_list|(
name|operationName
operator|=
literal|"getSomething"
argument_list|)
annotation|@
name|WebResult
argument_list|(
name|name
operator|=
literal|"result"
argument_list|,
name|targetNamespace
operator|=
literal|"urn:test"
argument_list|)
DECL|method|getSomething ()
name|String
name|getSomething
parameter_list|()
function_decl|;
annotation|@
name|WebMethod
argument_list|(
name|operationName
operator|=
literal|"getSomething"
argument_list|)
DECL|method|getSomethingAsync ()
name|Response
argument_list|<
name|String
argument_list|>
name|getSomethingAsync
parameter_list|()
function_decl|;
annotation|@
name|WebMethod
argument_list|(
name|operationName
operator|=
literal|"getSomething"
argument_list|)
DECL|method|getSomethingAsync (@ebParamname = R, targetNamespace = R) AsyncHandler<String> asyncHandler)
name|Future
argument_list|<
name|?
argument_list|>
name|getSomethingAsync
parameter_list|(
annotation|@
name|WebParam
argument_list|(
name|name
operator|=
literal|"asyncHandler"
argument_list|,
name|targetNamespace
operator|=
literal|""
argument_list|)
name|AsyncHandler
argument_list|<
name|String
argument_list|>
name|asyncHandler
parameter_list|)
function_decl|;
block|}
DECL|class|SampleWSImpl
specifier|public
specifier|static
class|class
name|SampleWSImpl
implements|implements
name|SampleWS
block|{
annotation|@
name|Override
DECL|method|getSomething ()
specifier|public
name|String
name|getSomething
parameter_list|()
block|{
return|return
literal|"something!"
return|;
block|}
block|}
comment|/**      * Initialize CamelTransportFactory without Spring      */
annotation|@
name|Before
DECL|method|setUpCXFCamelContext ()
specifier|public
name|void
name|setUpCXFCamelContext
parameter_list|()
block|{
name|bus
operator|=
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|()
expr_stmt|;
comment|// make sure the CamelTransportFactory is injected with right camel context
name|bus
operator|.
name|getExtension
argument_list|(
name|CamelTransportFactory
operator|.
name|class
argument_list|)
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
comment|/**      * Create a SampleWS JAXWS-Proxy to a specified route      *       * @param camelEndpoint      * @return      */
DECL|method|getSampleWS (String camelEndpoint)
specifier|public
name|SampleWS
name|getSampleWS
parameter_list|(
name|String
name|camelEndpoint
parameter_list|)
block|{
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"urn:test"
argument_list|,
literal|"testService"
argument_list|)
decl_stmt|;
name|Service
name|s
init|=
name|Service
operator|.
name|create
argument_list|(
name|serviceName
argument_list|)
decl_stmt|;
name|QName
name|portName
init|=
operator|new
name|QName
argument_list|(
literal|"urn:test"
argument_list|,
literal|"testPort"
argument_list|)
decl_stmt|;
name|s
operator|.
name|addPort
argument_list|(
name|portName
argument_list|,
literal|"http://schemas.xmlsoap.org/soap/"
argument_list|,
literal|"camel://"
operator|+
name|camelEndpoint
argument_list|)
expr_stmt|;
return|return
name|s
operator|.
name|getPort
argument_list|(
name|SampleWS
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getSampleWSWithCXFAPI (String camelEndpoint)
specifier|public
name|SampleWS
name|getSampleWSWithCXFAPI
parameter_list|(
name|String
name|camelEndpoint
parameter_list|)
block|{
name|JaxWsProxyFactoryBean
name|factory
init|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
literal|"camel://"
operator|+
name|camelEndpoint
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setServiceClass
argument_list|(
name|SampleWS
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
return|return
name|factory
operator|.
name|create
argument_list|(
name|SampleWS
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|getSampleWSAsyncWithCXFAPI (String camelEndpoint)
specifier|public
name|SampleWSAsync
name|getSampleWSAsyncWithCXFAPI
parameter_list|(
name|String
name|camelEndpoint
parameter_list|)
block|{
name|JaxWsProxyFactoryBean
name|factory
init|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setAddress
argument_list|(
literal|"camel://"
operator|+
name|camelEndpoint
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setServiceClass
argument_list|(
name|SampleWSAsync
operator|.
name|class
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
return|return
name|factory
operator|.
name|create
argument_list|(
name|SampleWSAsync
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Create a SampleWS Server to a specified route      * @param camelEndpoint      */
DECL|method|publishSampleWS (String camelEndpoint)
specifier|public
name|Endpoint
name|publishSampleWS
parameter_list|(
name|String
name|camelEndpoint
parameter_list|)
block|{
return|return
name|Endpoint
operator|.
name|publish
argument_list|(
literal|"camel://"
operator|+
name|camelEndpoint
argument_list|,
operator|new
name|SampleWSImpl
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

