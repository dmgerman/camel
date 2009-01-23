begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|cxf
operator|.
name|interceptor
operator|.
name|LoggingInInterceptor
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

begin_comment
comment|/**  * Unit test for testing CXF bus injection.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|CxfEndpointBeanWithBusTest
specifier|public
class|class
name|CxfEndpointBeanWithBusTest
extends|extends
name|CxfEndpointBeanTest
block|{
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
name|ctx
operator|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"org/apache/camel/component/cxf/spring/CxfEndpointBeansRouterWithBus.xml"
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|testBusInjectedBySpring ()
specifier|public
name|void
name|testBusInjectedBySpring
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|camelContext
init|=
operator|(
name|CamelContext
operator|)
name|ctx
operator|.
name|getBean
argument_list|(
literal|"camel"
argument_list|)
decl_stmt|;
name|CxfEndpoint
name|endpoint
init|=
operator|(
name|CxfEndpoint
operator|)
name|camelContext
operator|.
name|getEndpoint
argument_list|(
literal|"cxf:bean:routerEndpoint"
argument_list|)
decl_stmt|;
comment|// verify the interceptor that is added by the logging feature
name|assertTrue
argument_list|(
name|endpoint
operator|.
name|getBus
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|LoggingInInterceptor
operator|.
name|class
argument_list|,
name|endpoint
operator|.
name|getBus
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

