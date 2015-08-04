begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
package|;
end_package

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|createStrictMock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|expect
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|replay
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|reset
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|easymock
operator|.
name|EasyMock
operator|.
name|verify
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
name|assertFalse
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
name|assertTrue
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|JMException
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
name|ObjectInstance
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
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
name|spi
operator|.
name|ManagementAgent
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

begin_comment
comment|/**  * Tests proper behavior of DefaultManagementAgent when  * {@link MBeanServer#registerMBean(Object, ObjectName)} returns an  * {@link ObjectInstance} with a different ObjectName  */
end_comment

begin_class
DECL|class|DefaultManagementAgentMockTest
specifier|public
class|class
name|DefaultManagementAgentMockTest
block|{
annotation|@
name|Test
DECL|method|testObjectNameModification ()
specifier|public
name|void
name|testObjectNameModification
parameter_list|()
throws|throws
name|JMException
block|{
name|MBeanServer
name|mbeanServer
init|=
name|createStrictMock
argument_list|(
name|MBeanServer
operator|.
name|class
argument_list|)
decl_stmt|;
name|ObjectInstance
name|instance
init|=
name|createStrictMock
argument_list|(
name|ObjectInstance
operator|.
name|class
argument_list|)
decl_stmt|;
name|ManagementAgent
name|agent
init|=
operator|new
name|DefaultManagementAgent
argument_list|()
decl_stmt|;
name|agent
operator|.
name|setMBeanServer
argument_list|(
name|mbeanServer
argument_list|)
expr_stmt|;
name|Object
name|object
init|=
literal|"object"
decl_stmt|;
name|ObjectName
name|sourceObjectName
init|=
operator|new
name|ObjectName
argument_list|(
literal|"domain"
argument_list|,
literal|"key"
argument_list|,
literal|"value"
argument_list|)
decl_stmt|;
name|ObjectName
name|registeredObjectName
init|=
operator|new
name|ObjectName
argument_list|(
literal|"domain"
argument_list|,
literal|"key"
argument_list|,
literal|"otherValue"
argument_list|)
decl_stmt|;
comment|// Register MBean and return different ObjectName
name|expect
argument_list|(
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|sourceObjectName
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|mbeanServer
operator|.
name|registerMBean
argument_list|(
name|object
argument_list|,
name|sourceObjectName
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
name|instance
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|instance
operator|.
name|getObjectName
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
name|registeredObjectName
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|registeredObjectName
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|mbeanServer
argument_list|,
name|instance
argument_list|)
expr_stmt|;
name|agent
operator|.
name|register
argument_list|(
name|object
argument_list|,
name|sourceObjectName
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|agent
operator|.
name|isRegistered
argument_list|(
name|sourceObjectName
argument_list|)
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|mbeanServer
argument_list|,
name|instance
argument_list|)
expr_stmt|;
name|reset
argument_list|(
name|mbeanServer
argument_list|,
name|instance
argument_list|)
expr_stmt|;
comment|// ... and unregister it again
name|expect
argument_list|(
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|registeredObjectName
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|unregisterMBean
argument_list|(
name|registeredObjectName
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|sourceObjectName
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|mbeanServer
argument_list|)
expr_stmt|;
name|agent
operator|.
name|unregister
argument_list|(
name|sourceObjectName
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|agent
operator|.
name|isRegistered
argument_list|(
name|sourceObjectName
argument_list|)
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|mbeanServer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testShouldUseHostIPAddressWhenFlagisTrue ()
specifier|public
name|void
name|testShouldUseHostIPAddressWhenFlagisTrue
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|setProperty
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|USE_HOST_IP_ADDRESS
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|CREATE_CONNECTOR
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|CamelContext
name|ctx
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|ManagementAgent
name|agent
init|=
operator|new
name|DefaultManagementAgent
argument_list|(
name|ctx
argument_list|)
decl_stmt|;
name|agent
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|agent
operator|.
name|getUseHostIPAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldUseHostNameWhenFlagisFalse ()
specifier|public
name|void
name|shouldUseHostNameWhenFlagisFalse
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|setProperty
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|USE_HOST_IP_ADDRESS
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|CREATE_CONNECTOR
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|CamelContext
name|ctx
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|ManagementAgent
name|agent
init|=
operator|new
name|DefaultManagementAgent
argument_list|(
name|ctx
argument_list|)
decl_stmt|;
name|agent
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|agent
operator|.
name|getUseHostIPAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

