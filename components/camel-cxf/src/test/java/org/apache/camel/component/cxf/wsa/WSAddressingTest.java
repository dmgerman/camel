begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.wsa
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
name|wsa
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|cxf
operator|.
name|bus
operator|.
name|spring
operator|.
name|SpringBusFactory
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
name|common
operator|.
name|classloader
operator|.
name|ClassLoaderUtils
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
name|headers
operator|.
name|Header
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
name|apache
operator|.
name|cxf
operator|.
name|jaxws
operator|.
name|JaxWsServerFactoryBean
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
name|After
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
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|AbstractJUnit4SpringContextTests
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_comment
comment|/**  *  * @version   */
end_comment

begin_class
annotation|@
name|ContextConfiguration
DECL|class|WSAddressingTest
specifier|public
class|class
name|WSAddressingTest
extends|extends
name|AbstractJUnit4SpringContextTests
block|{
DECL|field|port0
specifier|protected
specifier|static
name|int
name|port0
init|=
name|CXFTestSupport
operator|.
name|getPort1
argument_list|()
decl_stmt|;
DECL|field|port1
specifier|protected
specifier|static
name|int
name|port1
init|=
name|CXFTestSupport
operator|.
name|getPort2
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
name|getPort3
argument_list|()
decl_stmt|;
annotation|@
name|Autowired
DECL|field|context
specifier|protected
name|CamelContext
name|context
decl_stmt|;
DECL|field|template
specifier|protected
name|ProducerTemplate
name|template
decl_stmt|;
DECL|field|serviceEndpoint
specifier|private
name|Server
name|serviceEndpoint
decl_stmt|;
DECL|method|getServerAddress ()
specifier|protected
name|String
name|getServerAddress
parameter_list|()
block|{
return|return
literal|"http://localhost:"
operator|+
name|port1
operator|+
literal|"/"
operator|+
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|"/SoapContext/SoapPort"
return|;
block|}
DECL|method|getClientAddress ()
specifier|protected
name|String
name|getClientAddress
parameter_list|()
block|{
return|return
literal|"http://localhost:"
operator|+
name|port0
operator|+
literal|"/"
operator|+
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|"/SoapContext/SoapPort"
return|;
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
name|template
operator|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
expr_stmt|;
name|JaxWsServerFactoryBean
name|svrBean
init|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
decl_stmt|;
name|svrBean
operator|.
name|setAddress
argument_list|(
name|getServerAddress
argument_list|()
argument_list|)
expr_stmt|;
name|svrBean
operator|.
name|setServiceClass
argument_list|(
name|Greeter
operator|.
name|class
argument_list|)
expr_stmt|;
name|svrBean
operator|.
name|setServiceBean
argument_list|(
operator|new
name|GreeterImpl
argument_list|()
argument_list|)
expr_stmt|;
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|URL
name|cxfConfig
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|getCxfServerConfig
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|cxfConfig
operator|=
name|ClassLoaderUtils
operator|.
name|getResource
argument_list|(
name|getCxfServerConfig
argument_list|()
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|svrBean
operator|.
name|setBus
argument_list|(
name|bf
operator|.
name|createBus
argument_list|(
name|cxfConfig
argument_list|)
argument_list|)
expr_stmt|;
name|serviceEndpoint
operator|=
name|svrBean
operator|.
name|create
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
if|if
condition|(
name|serviceEndpoint
operator|!=
literal|null
condition|)
block|{
name|serviceEndpoint
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testWSAddressing ()
specifier|public
name|void
name|testWSAddressing
parameter_list|()
throws|throws
name|Exception
block|{
name|JaxWsProxyFactoryBean
name|proxyFactory
init|=
operator|new
name|JaxWsProxyFactoryBean
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
name|getClientAddress
argument_list|()
argument_list|)
expr_stmt|;
name|clientBean
operator|.
name|setServiceClass
argument_list|(
name|Greeter
operator|.
name|class
argument_list|)
expr_stmt|;
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|URL
name|cxfConfig
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|getCxfClientConfig
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|cxfConfig
operator|=
name|ClassLoaderUtils
operator|.
name|getResource
argument_list|(
name|getCxfClientConfig
argument_list|()
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|proxyFactory
operator|.
name|setBus
argument_list|(
name|bf
operator|.
name|createBus
argument_list|(
name|cxfConfig
argument_list|)
argument_list|)
expr_stmt|;
name|Greeter
name|client
init|=
operator|(
name|Greeter
operator|)
name|proxyFactory
operator|.
name|create
argument_list|()
decl_stmt|;
name|String
name|result
init|=
name|client
operator|.
name|greetMe
argument_list|(
literal|"world!"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong response"
argument_list|,
literal|"Hello world!"
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return      */
DECL|method|getCxfServerConfig ()
specifier|protected
name|String
name|getCxfServerConfig
parameter_list|()
block|{
return|return
literal|"server.xml"
return|;
block|}
comment|/**      * @return      */
DECL|method|getCxfClientConfig ()
specifier|protected
name|String
name|getCxfClientConfig
parameter_list|()
block|{
return|return
literal|"client.xml"
return|;
block|}
DECL|class|RemoveRequestOutHeaderProcessor
specifier|public
specifier|static
class|class
name|RemoveRequestOutHeaderProcessor
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
name|List
argument_list|<
name|?
argument_list|>
name|headerList
init|=
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Header
operator|.
name|HEADER_LIST
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"We should get the header list."
argument_list|,
name|headerList
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong size of header list."
argument_list|,
literal|4
argument_list|,
name|headerList
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// we don't need send the soap headers to the client
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|Header
operator|.
name|HEADER_LIST
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

