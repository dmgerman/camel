begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring
package|package
name|org
operator|.
name|apache
operator|.
name|camel
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
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServerConnection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServerFactory
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
name|management
operator|.
name|DefaultInstrumentationAgent
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
comment|/**  * Test that verifies JMX is enabled by default.  *   * @version $Revision$  *  */
end_comment

begin_class
DECL|class|DefaultJMXAgentTest
specifier|public
class|class
name|DefaultJMXAgentTest
extends|extends
name|SpringTestSupport
block|{
DECL|field|mbsc
specifier|protected
name|MBeanServerConnection
name|mbsc
decl_stmt|;
DECL|field|sleepForConnection
specifier|protected
name|long
name|sleepForConnection
init|=
literal|0
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
name|releaseMBeanServers
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
name|sleepForConnection
argument_list|)
expr_stmt|;
name|mbsc
operator|=
name|getMBeanConnection
argument_list|()
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
name|releaseMBeanServers
argument_list|()
expr_stmt|;
name|mbsc
operator|=
literal|null
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
DECL|method|getDomainName ()
specifier|protected
name|String
name|getDomainName
parameter_list|()
block|{
return|return
name|DefaultInstrumentationAgent
operator|.
name|DEFAULT_DOMAIN
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|releaseMBeanServers ()
specifier|protected
name|void
name|releaseMBeanServers
parameter_list|()
block|{
name|List
argument_list|<
name|MBeanServer
argument_list|>
name|servers
init|=
operator|(
name|List
argument_list|<
name|MBeanServer
argument_list|>
operator|)
name|MBeanServerFactory
operator|.
name|findMBeanServer
argument_list|(
literal|null
argument_list|)
decl_stmt|;
for|for
control|(
name|MBeanServer
name|server
range|:
name|servers
control|)
block|{
name|MBeanServerFactory
operator|.
name|releaseMBeanServer
argument_list|(
name|server
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testGetJMXConnector ()
specifier|public
name|void
name|testGetJMXConnector
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
literal|"Get the wrong domain name"
argument_list|,
name|mbsc
operator|.
name|getDefaultDomain
argument_list|()
argument_list|,
name|getDomainName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
literal|"org/apache/camel/spring/defaultJmxConfig.xml"
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getMBeanConnection ()
specifier|protected
name|MBeanServerConnection
name|getMBeanConnection
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|mbsc
operator|==
literal|null
condition|)
block|{
name|List
argument_list|<
name|MBeanServer
argument_list|>
name|servers
init|=
operator|(
name|List
argument_list|<
name|MBeanServer
argument_list|>
operator|)
name|MBeanServerFactory
operator|.
name|findMBeanServer
argument_list|(
literal|null
argument_list|)
decl_stmt|;
for|for
control|(
name|MBeanServer
name|server
range|:
name|servers
control|)
block|{
if|if
condition|(
name|getDomainName
argument_list|()
operator|.
name|equals
argument_list|(
name|server
operator|.
name|getDefaultDomain
argument_list|()
argument_list|)
condition|)
block|{
name|mbsc
operator|=
name|server
expr_stmt|;
break|break;
block|}
block|}
block|}
return|return
name|mbsc
return|;
block|}
block|}
end_class

end_unit

