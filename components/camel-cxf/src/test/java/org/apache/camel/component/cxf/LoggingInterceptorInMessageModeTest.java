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
name|After
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
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
name|interceptor
operator|.
name|Interceptor
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
name|LoggingOutInterceptor
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
DECL|class|LoggingInterceptorInMessageModeTest
specifier|public
class|class
name|LoggingInterceptorInMessageModeTest
extends|extends
name|AbstractJUnit4SpringContextTests
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
literal|"/LoggingInterceptorInMessageModeTest/router"
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
literal|"/LoggingInterceptorInMessageModeTest/helloworld"
decl_stmt|;
DECL|field|server
specifier|static
name|Server
name|server
decl_stmt|;
annotation|@
name|Autowired
DECL|field|context
specifier|protected
name|CamelContext
name|context
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
block|}
annotation|@
name|AfterClass
DECL|method|stopService ()
specifier|public
specifier|static
name|void
name|stopService
parameter_list|()
block|{
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
name|Test
DECL|method|testInvokingServiceFromCXFClient ()
specifier|public
name|void
name|testInvokingServiceFromCXFClient
parameter_list|()
throws|throws
name|Exception
block|{
name|LoggingOutInterceptor
name|logInterceptor
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Interceptor
argument_list|<
name|?
argument_list|>
name|interceptor
range|:
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"cxf:bean:serviceEndpoint"
argument_list|,
name|CxfSpringEndpoint
operator|.
name|class
argument_list|)
operator|.
name|getOutInterceptors
argument_list|()
control|)
block|{
if|if
condition|(
name|interceptor
operator|instanceof
name|LoggingOutInterceptor
condition|)
block|{
name|logInterceptor
operator|=
name|LoggingOutInterceptor
operator|.
name|class
operator|.
name|cast
argument_list|(
name|interceptor
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
name|assertNotNull
argument_list|(
name|logInterceptor
argument_list|)
expr_stmt|;
comment|// StringPrintWriter writer = new StringPrintWriter();
comment|// Unfortunately, LoggingOutInterceptor does not have a setter for writer so
comment|// we can't capture the output to verify.
comment|// logInterceptor.setPrintWriter(writer);
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
comment|//assertTrue(writer.getString().indexOf("hello world")> 0);
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
DECL|class|StringPrintWriter
specifier|private
specifier|static
specifier|final
class|class
name|StringPrintWriter
extends|extends
name|PrintWriter
block|{
DECL|method|StringPrintWriter ()
specifier|private
name|StringPrintWriter
parameter_list|()
block|{
name|super
argument_list|(
operator|new
name|StringWriter
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|StringPrintWriter (int initialSize)
specifier|private
name|StringPrintWriter
parameter_list|(
name|int
name|initialSize
parameter_list|)
block|{
name|super
argument_list|(
operator|new
name|StringWriter
argument_list|(
name|initialSize
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getString ()
specifier|private
name|String
name|getString
parameter_list|()
block|{
name|flush
argument_list|()
expr_stmt|;
return|return
operator|(
operator|(
name|StringWriter
operator|)
name|out
operator|)
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

