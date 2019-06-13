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
name|Consumer
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
name|Producer
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
operator|.
name|CamelCxfClientImpl
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
name|camel
operator|.
name|support
operator|.
name|SimpleRegistry
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
name|bus
operator|.
name|extension
operator|.
name|ExtensionManagerBus
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
name|AbstractWSDLBasedEndpointFactory
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
name|ClassPathXmlApplicationContext
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|isA
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|reset
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|verify
import|;
end_import

begin_comment
comment|/**  * A unit test for spring configured cxf endpoint.  */
end_comment

begin_class
DECL|class|CxfEndpointTest
specifier|public
class|class
name|CxfEndpointTest
extends|extends
name|Assert
block|{
DECL|field|port1
specifier|private
name|int
name|port1
init|=
name|CXFTestSupport
operator|.
name|getPort1
argument_list|()
decl_stmt|;
DECL|field|port2
specifier|private
name|int
name|port2
init|=
name|CXFTestSupport
operator|.
name|getPort2
argument_list|()
decl_stmt|;
DECL|field|routerEndpointURI
specifier|private
name|String
name|routerEndpointURI
init|=
literal|"cxf://http://localhost:"
operator|+
name|port1
operator|+
literal|"/CxfEndpointTest/router"
operator|+
literal|"?serviceClass=org.apache.camel.component.cxf.HelloService"
operator|+
literal|"&dataFormat=POJO"
decl_stmt|;
DECL|field|wsdlEndpointURI
specifier|private
name|String
name|wsdlEndpointURI
init|=
literal|"cxf://http://localhost:"
operator|+
name|port2
operator|+
literal|"/CxfEndpointTest/helloworld"
operator|+
literal|"?wsdlURL=classpath:person.wsdl"
operator|+
literal|"&serviceName={http://camel.apache.org/wsdl-first}PersonService"
operator|+
literal|"&portName={http://camel.apache.org/wsdl-first}soap"
operator|+
literal|"&dataFormat=PAYLOAD"
decl_stmt|;
annotation|@
name|Test
DECL|method|testSettingContinucationTimout ()
specifier|public
name|void
name|testSettingContinucationTimout
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|CxfEndpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|routerEndpointURI
operator|+
literal|"&continuationTimeout=800000"
argument_list|,
name|CxfEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong continucationTimeout value"
argument_list|,
literal|800000
argument_list|,
name|endpoint
operator|.
name|getContinuationTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSpringCxfEndpoint ()
specifier|public
name|void
name|testSpringCxfEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
name|ClassPathXmlApplicationContext
name|ctx
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"org/apache/camel/component/cxf/CxfEndpointBeans.xml"
block|}
argument_list|)
decl_stmt|;
name|CxfComponent
name|cxfComponent
init|=
operator|new
name|CxfComponent
argument_list|(
operator|new
name|SpringCamelContext
argument_list|(
name|ctx
argument_list|)
argument_list|)
decl_stmt|;
name|CxfSpringEndpoint
name|endpoint
init|=
operator|(
name|CxfSpringEndpoint
operator|)
name|cxfComponent
operator|.
name|createEndpoint
argument_list|(
literal|"cxf://bean:serviceEndpoint"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Got the wrong endpoint address"
argument_list|,
name|endpoint
operator|.
name|getAddress
argument_list|()
argument_list|,
literal|"http://localhost:"
operator|+
name|port2
operator|+
literal|"/CxfEndpointTest/helloworld"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Got the wrong endpont service class"
argument_list|,
name|endpoint
operator|.
name|getServiceClass
argument_list|()
operator|.
name|getCanonicalName
argument_list|()
argument_list|,
literal|"org.apache.camel.component.cxf.HelloService"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSettingClientBus ()
specifier|public
name|void
name|testSettingClientBus
parameter_list|()
throws|throws
name|Exception
block|{
name|ExtensionManagerBus
name|bus
init|=
operator|(
name|ExtensionManagerBus
operator|)
name|BusFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createBus
argument_list|()
decl_stmt|;
name|bus
operator|.
name|setId
argument_list|(
literal|"oldCXF"
argument_list|)
expr_stmt|;
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|ExtensionManagerBus
name|newBus
init|=
operator|(
name|ExtensionManagerBus
operator|)
name|BusFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|createBus
argument_list|()
decl_stmt|;
name|newBus
operator|.
name|setId
argument_list|(
literal|"newCXF"
argument_list|)
expr_stmt|;
name|CxfComponent
name|cxfComponent
init|=
operator|new
name|CxfComponent
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|)
decl_stmt|;
name|CxfEndpoint
name|endpoint
init|=
operator|(
name|CxfEndpoint
operator|)
name|cxfComponent
operator|.
name|createEndpoint
argument_list|(
name|routerEndpointURI
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setBus
argument_list|(
name|newBus
argument_list|)
expr_stmt|;
name|CamelCxfClientImpl
name|client
init|=
operator|(
name|CamelCxfClientImpl
operator|)
name|endpoint
operator|.
name|createClient
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"CamelCxfClientImpl should has the same bus with CxfEndpoint"
argument_list|,
name|newBus
argument_list|,
name|client
operator|.
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|=
operator|(
name|CxfEndpoint
operator|)
name|cxfComponent
operator|.
name|createEndpoint
argument_list|(
name|wsdlEndpointURI
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setBus
argument_list|(
name|newBus
argument_list|)
expr_stmt|;
name|client
operator|=
operator|(
name|CamelCxfClientImpl
operator|)
name|endpoint
operator|.
name|createClient
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CamelCxfClientImpl should has the same bus with CxfEndpoint"
argument_list|,
name|newBus
argument_list|,
name|client
operator|.
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCxfEndpointConfigurer ()
specifier|public
name|void
name|testCxfEndpointConfigurer
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleRegistry
name|registry
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|CxfEndpointConfigurer
name|configurer
init|=
name|mock
argument_list|(
name|CxfEndpointConfigurer
operator|.
name|class
argument_list|)
decl_stmt|;
name|Processor
name|processor
init|=
name|mock
argument_list|(
name|Processor
operator|.
name|class
argument_list|)
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"myConfigurer"
argument_list|,
name|configurer
argument_list|)
expr_stmt|;
name|CamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|(
name|registry
argument_list|)
decl_stmt|;
name|CxfComponent
name|cxfComponent
init|=
operator|new
name|CxfComponent
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|CxfEndpoint
name|endpoint
init|=
operator|(
name|CxfEndpoint
operator|)
name|cxfComponent
operator|.
name|createEndpoint
argument_list|(
name|routerEndpointURI
operator|+
literal|"&cxfEndpointConfigurer=#myConfigurer"
argument_list|)
decl_stmt|;
name|Consumer
name|consumer
init|=
name|endpoint
operator|.
name|createConsumer
argument_list|(
name|processor
argument_list|)
decl_stmt|;
name|consumer
operator|.
name|start
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|configurer
argument_list|)
operator|.
name|configure
argument_list|(
name|isA
argument_list|(
name|AbstractWSDLBasedEndpointFactory
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|configurer
argument_list|)
operator|.
name|configureServer
argument_list|(
name|isA
argument_list|(
name|Server
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|reset
argument_list|(
name|configurer
argument_list|)
expr_stmt|;
name|Producer
name|producer
init|=
name|endpoint
operator|.
name|createProducer
argument_list|()
decl_stmt|;
name|producer
operator|.
name|start
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|configurer
argument_list|)
operator|.
name|configure
argument_list|(
name|isA
argument_list|(
name|AbstractWSDLBasedEndpointFactory
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|configurer
argument_list|)
operator|.
name|configureClient
argument_list|(
name|isA
argument_list|(
name|Client
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

