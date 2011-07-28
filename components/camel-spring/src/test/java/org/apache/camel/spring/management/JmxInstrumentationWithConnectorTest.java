begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.management
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|management
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
name|management
operator|.
name|DefaultManagementAgent
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
name|spi
operator|.
name|ManagementAgent
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
name|EndpointReferenceTest
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
DECL|class|JmxInstrumentationWithConnectorTest
specifier|public
class|class
name|JmxInstrumentationWithConnectorTest
extends|extends
name|EndpointReferenceTest
block|{
annotation|@
name|Override
DECL|method|useJmx ()
specifier|protected
name|boolean
name|useJmx
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|testJmxConfiguration ()
specifier|public
name|void
name|testJmxConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|ManagementAgent
name|agent
init|=
name|getMandatoryBean
argument_list|(
name|DefaultManagementAgent
operator|.
name|class
argument_list|,
literal|"agent"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"SpringInstrumentationAgent must be configured for JMX support"
argument_list|,
name|agent
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"MBeanServer must be configured for JMX support"
argument_list|,
name|agent
operator|.
name|getMBeanServer
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.camel.test"
argument_list|,
name|agent
operator|.
name|getMBeanServer
argument_list|()
operator|.
name|getDefaultDomain
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/spring/management/jmxInstrumentationWithConnector.xml"
argument_list|)
return|;
block|}
DECL|method|testReferenceEndpointFromOtherCamelContext ()
specifier|public
name|void
name|testReferenceEndpointFromOtherCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
comment|// don't run the test in this method
block|}
block|}
end_class

end_unit

