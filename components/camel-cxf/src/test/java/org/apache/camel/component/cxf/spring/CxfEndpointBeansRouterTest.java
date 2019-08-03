begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.spring
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
name|spring
package|;
end_package

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
name|namespace
operator|.
name|QName
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
name|component
operator|.
name|cxf
operator|.
name|CxfEndpoint
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
name|transport
operator|.
name|http
operator|.
name|HTTPException
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
DECL|class|CxfEndpointBeansRouterTest
specifier|public
class|class
name|CxfEndpointBeansRouterTest
extends|extends
name|AbstractSpringBeanTestSupport
block|{
annotation|@
name|Override
DECL|method|getApplicationContextFiles ()
specifier|protected
name|String
index|[]
name|getApplicationContextFiles
parameter_list|()
block|{
name|CXFTestSupport
operator|.
name|getPort1
argument_list|()
expr_stmt|;
return|return
operator|new
name|String
index|[]
block|{
literal|"org/apache/camel/component/cxf/spring/CxfEndpointBeansRouter.xml"
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|testCxfEndpointBeanDefinitionParser ()
specifier|public
name|void
name|testCxfEndpointBeanDefinitionParser
parameter_list|()
block|{
name|CxfEndpoint
name|routerEndpoint
init|=
name|ctx
operator|.
name|getBean
argument_list|(
literal|"routerEndpoint"
argument_list|,
name|CxfEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Got the wrong endpoint address"
argument_list|,
name|routerEndpoint
operator|.
name|getAddress
argument_list|()
argument_list|,
literal|"http://localhost:"
operator|+
name|CXFTestSupport
operator|.
name|getPort1
argument_list|()
operator|+
literal|"/CxfEndpointBeansRouterTest/router"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Got the wrong endpont service class"
argument_list|,
literal|"org.apache.camel.component.cxf.HelloService"
argument_list|,
name|routerEndpoint
operator|.
name|getServiceClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCreateCxfEndpointFromURI ()
specifier|public
name|void
name|testCreateCxfEndpointFromURI
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|camelContext
init|=
name|ctx
operator|.
name|getBean
argument_list|(
literal|"camel"
argument_list|,
name|CamelContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|CxfEndpoint
name|endpoint1
init|=
name|camelContext
operator|.
name|getEndpoint
argument_list|(
literal|"cxf:bean:routerEndpoint?address=http://localhost:9000/test1"
argument_list|,
name|CxfEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|CxfEndpoint
name|endpoint2
init|=
name|camelContext
operator|.
name|getEndpoint
argument_list|(
literal|"cxf:bean:routerEndpoint?address=http://localhost:8000/test2"
argument_list|,
name|CxfEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong endpoint address."
argument_list|,
literal|"http://localhost:9000/test1"
argument_list|,
name|endpoint1
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong endpoint address."
argument_list|,
literal|"http://localhost:8000/test2"
argument_list|,
name|endpoint2
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
comment|// the uri will always be normalized
name|String
name|uri1
init|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"cxf://bean:routerEndpoint?address=http://localhost:9000/test1"
argument_list|)
decl_stmt|;
name|String
name|uri2
init|=
name|URISupport
operator|.
name|normalizeUri
argument_list|(
literal|"cxf://bean:routerEndpoint?address=http://localhost:8000/test2"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong endpoint key."
argument_list|,
name|uri1
argument_list|,
name|endpoint1
operator|.
name|getEndpointKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong endpoint key."
argument_list|,
name|uri2
argument_list|,
name|endpoint2
operator|.
name|getEndpointKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCxfBusConfiguration ()
specifier|public
name|void
name|testCxfBusConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
comment|// get the camelContext from application context
name|CamelContext
name|camelContext
init|=
name|ctx
operator|.
name|getBean
argument_list|(
literal|"camel"
argument_list|,
name|CamelContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|ProducerTemplate
name|template
init|=
name|camelContext
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|Exchange
name|reply
init|=
name|template
operator|.
name|request
argument_list|(
literal|"cxf:bean:serviceEndpoint"
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
literal|"hello"
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
literal|"echo"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|Exception
name|ex
init|=
name|reply
operator|.
name|getException
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should get the fault here"
argument_list|,
name|ex
operator|instanceof
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|interceptor
operator|.
name|Fault
operator|||
name|ex
operator|instanceof
name|HTTPException
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCxfBeanWithCamelPropertiesHolder ()
specifier|public
name|void
name|testCxfBeanWithCamelPropertiesHolder
parameter_list|()
throws|throws
name|Exception
block|{
comment|// get the camelContext from application context
name|CamelContext
name|camelContext
init|=
name|ctx
operator|.
name|getBean
argument_list|(
literal|"camel"
argument_list|,
name|CamelContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|CxfEndpoint
name|testEndpoint
init|=
name|camelContext
operator|.
name|getEndpoint
argument_list|(
literal|"cxf:bean:testEndpoint"
argument_list|,
name|CxfEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|QName
name|endpointName
init|=
name|QName
operator|.
name|valueOf
argument_list|(
literal|"{http://org.apache.camel.component.cxf}myEndpoint"
argument_list|)
decl_stmt|;
name|QName
name|serviceName
init|=
name|QName
operator|.
name|valueOf
argument_list|(
literal|"{http://org.apache.camel.component.cxf}myService"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Got a wrong address"
argument_list|,
literal|"http://localhost:9000/testEndpoint"
argument_list|,
name|testEndpoint
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Got a wrong bindingId"
argument_list|,
literal|"http://schemas.xmlsoap.org/wsdl/soap12/"
argument_list|,
name|testEndpoint
operator|.
name|getBindingId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Got a wrong transportId"
argument_list|,
literal|"http://cxf.apache.org/transports/http"
argument_list|,
name|testEndpoint
operator|.
name|getTransportId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Got a wrong endpointName"
argument_list|,
name|endpointName
argument_list|,
name|testEndpoint
operator|.
name|getPortName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Got a wrong WsdlURL"
argument_list|,
literal|"wsdl/test.wsdl"
argument_list|,
name|testEndpoint
operator|.
name|getWsdlURL
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Got a wrong serviceName"
argument_list|,
name|serviceName
argument_list|,
name|testEndpoint
operator|.
name|getServiceName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

