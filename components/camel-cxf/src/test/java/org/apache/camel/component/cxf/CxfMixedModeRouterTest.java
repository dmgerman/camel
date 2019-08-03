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
name|io
operator|.
name|StringReader
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
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|binding
operator|.
name|soap
operator|.
name|SoapHeader
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
name|ClientFactoryBean
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
name|ClientProxyFactoryBean
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
name|staxutils
operator|.
name|StaxUtils
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
DECL|class|CxfMixedModeRouterTest
specifier|public
class|class
name|CxfMixedModeRouterTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|port1
specifier|protected
specifier|static
name|int
name|port1
init|=
name|CXFTestSupport
operator|.
name|getPort1
argument_list|()
decl_stmt|;
DECL|field|port2
specifier|protected
specifier|static
name|int
name|port2
init|=
name|CXFTestSupport
operator|.
name|getPort2
argument_list|()
decl_stmt|;
DECL|field|server
specifier|protected
specifier|static
name|Server
name|server
decl_stmt|;
DECL|field|ROUTER_ADDRESS
specifier|protected
specifier|static
specifier|final
name|String
name|ROUTER_ADDRESS
init|=
literal|"http://localhost:"
operator|+
name|port1
operator|+
literal|"/CxfMixedModeRouterTest/router"
decl_stmt|;
DECL|field|SERVICE_ADDRESS
specifier|protected
specifier|static
specifier|final
name|String
name|SERVICE_ADDRESS
init|=
literal|"http://localhost:"
operator|+
name|port2
operator|+
literal|"/CxfMixedModeRouterTest/helloworld"
decl_stmt|;
DECL|field|SERVICE_CLASS
specifier|protected
specifier|static
specifier|final
name|String
name|SERVICE_CLASS
init|=
literal|"serviceClass=org.apache.camel.component.cxf.HelloService"
decl_stmt|;
DECL|field|routerEndpointURI
specifier|private
name|String
name|routerEndpointURI
init|=
literal|"cxf://"
operator|+
name|ROUTER_ADDRESS
operator|+
literal|"?"
operator|+
name|SERVICE_CLASS
operator|+
literal|"&dataFormat=PAYLOAD&allowStreaming=false"
decl_stmt|;
DECL|field|serviceEndpointURI
specifier|private
name|String
name|serviceEndpointURI
init|=
literal|"cxf://"
operator|+
name|SERVICE_ADDRESS
operator|+
literal|"?"
operator|+
name|SERVICE_CLASS
operator|+
literal|"&dataFormat=POJO"
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
DECL|method|startService ()
specifier|public
specifier|static
name|void
name|startService
parameter_list|()
block|{
comment|//start a service
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
name|SERVICE_ADDRESS
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
name|server
operator|=
name|svrBean
operator|.
name|create
argument_list|()
expr_stmt|;
name|server
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|AfterClass
DECL|method|shutdownService ()
specifier|public
specifier|static
name|void
name|shutdownService
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
annotation|@
name|Override
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
name|errorHandler
argument_list|(
name|noErrorHandler
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|routerEndpointURI
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
comment|// convert request message
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
name|CxfPayload
argument_list|<
name|?
argument_list|>
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|CxfPayload
operator|.
name|class
argument_list|)
decl_stmt|;
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
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
comment|// convert CxfPayload to list of objects any way you like
name|Element
name|element
init|=
operator|new
name|XmlConverter
argument_list|()
operator|.
name|toDOMElement
argument_list|(
name|message
operator|.
name|getBody
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
name|params
operator|.
name|add
argument_list|(
name|element
operator|.
name|getFirstChild
argument_list|()
operator|.
name|getTextContent
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// replace the body
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
comment|// if you need to change the operation name
comment|//exchange.getIn().setHeader(CxfConstants.OPERATION_NAME, GREET_ME_OPERATION);
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
name|serviceEndpointURI
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
comment|// convert response to CxfPayload
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
name|List
argument_list|<
name|?
argument_list|>
name|list
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|CxfPayload
argument_list|<
name|SoapHeader
argument_list|>
name|message
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|list
operator|!=
literal|null
condition|)
block|{
comment|// convert the list of objects to CxfPayload any way you like
name|String
name|s
init|=
literal|"<?xml version=\"1.0\" encoding=\"utf-8\"?>"
operator|+
literal|"<ns1:echoResponse xmlns:ns1=\"http://cxf.component.camel.apache.org/\">"
operator|+
literal|"<return xmlns=\"http://cxf.component.camel.apache.org/\">"
operator|+
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|+
literal|"</return></ns1:echoResponse>"
decl_stmt|;
name|List
argument_list|<
name|Element
argument_list|>
name|body
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|body
operator|.
name|add
argument_list|(
name|StaxUtils
operator|.
name|read
argument_list|(
operator|new
name|StringReader
argument_list|(
name|s
argument_list|)
argument_list|)
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|=
operator|new
name|CxfPayload
argument_list|<>
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|SoapHeader
argument_list|>
argument_list|()
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|message
argument_list|)
expr_stmt|;
comment|// we probably should be smarter in detecting data format based on message body
comment|// but for now we need to explicitly reset the mode (see CAMEL-3420)
name|exchange
operator|.
name|setProperty
argument_list|(
name|CxfConstants
operator|.
name|DATA_FORMAT_PROPERTY
argument_list|,
name|DataFormat
operator|.
name|PAYLOAD
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
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
operator|new
name|DefaultCamelContext
argument_list|()
return|;
block|}
DECL|method|getCXFClient ()
specifier|protected
name|HelloService
name|getCXFClient
parameter_list|()
throws|throws
name|Exception
block|{
name|ClientProxyFactoryBean
name|proxyFactory
init|=
operator|new
name|ClientProxyFactoryBean
argument_list|()
decl_stmt|;
name|ClientFactoryBean
name|clientBean
init|=
name|proxyFactory
operator|.
name|getClientFactoryBean
argument_list|()
decl_stmt|;
name|clientBean
operator|.
name|setAddress
argument_list|(
name|ROUTER_ADDRESS
argument_list|)
expr_stmt|;
name|clientBean
operator|.
name|setServiceClass
argument_list|(
name|HelloService
operator|.
name|class
argument_list|)
expr_stmt|;
name|clientBean
operator|.
name|setBus
argument_list|(
name|BusFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createBus
argument_list|()
argument_list|)
expr_stmt|;
name|HelloService
name|client
init|=
operator|(
name|HelloService
operator|)
name|proxyFactory
operator|.
name|create
argument_list|()
decl_stmt|;
return|return
name|client
return|;
block|}
annotation|@
name|Test
DECL|method|testInvokingServiceFromCXFClient ()
specifier|public
name|void
name|testInvokingServiceFromCXFClient
parameter_list|()
throws|throws
name|Exception
block|{
name|HelloService
name|client
init|=
name|getCXFClient
argument_list|()
decl_stmt|;
name|String
name|result
init|=
name|client
operator|.
name|echo
argument_list|(
literal|"hello world"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"we should get the right answer from router"
argument_list|,
name|result
argument_list|,
literal|"echo hello world"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

