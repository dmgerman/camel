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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|ManagedResourceTest
specifier|public
class|class
name|ManagedResourceTest
extends|extends
name|ManagementTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ManagedResourceTest
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|bean
argument_list|(
name|MyManagedBean
operator|.
name|class
argument_list|)
operator|.
name|id
argument_list|(
literal|"myManagedBean"
argument_list|)
operator|.
name|log
argument_list|(
literal|"${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|testManagedResource ()
specifier|public
name|void
name|testManagedResource
parameter_list|()
throws|throws
name|Exception
block|{
comment|// JMX tests dont work well on AIX CI servers (hangs them)
if|if
condition|(
name|isPlatform
argument_list|(
literal|"aix"
argument_list|)
condition|)
block|{
return|return;
block|}
specifier|final
name|ManagementAgent
name|managementAgent
init|=
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementAgent
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|managementAgent
argument_list|)
expr_stmt|;
specifier|final
name|MBeanServer
name|mBeanServer
init|=
name|managementAgent
operator|.
name|getMBeanServer
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|mBeanServer
argument_list|)
expr_stmt|;
specifier|final
name|String
name|mBeanServerDefaultDomain
init|=
name|managementAgent
operator|.
name|getMBeanServerDefaultDomain
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"org.apache.camel"
argument_list|,
name|mBeanServerDefaultDomain
argument_list|)
expr_stmt|;
specifier|final
name|String
name|managementName
init|=
name|context
operator|.
name|getManagementName
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
literal|"CamelContext should have a management name if JMX is enabled"
argument_list|,
name|managementName
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"managementName = {}"
argument_list|,
name|managementName
argument_list|)
expr_stmt|;
comment|// Get the Camel Context MBean
name|ObjectName
name|onContext
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
name|mBeanServerDefaultDomain
operator|+
literal|":context="
operator|+
name|managementName
operator|+
literal|",type=context,name=\""
operator|+
name|context
operator|.
name|getName
argument_list|()
operator|+
literal|"\""
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"Should be registered"
argument_list|,
name|mBeanServer
operator|.
name|isRegistered
argument_list|(
name|onContext
argument_list|)
argument_list|)
expr_stmt|;
comment|// Get myManagedBean
name|ObjectName
name|onManagedBean
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
name|mBeanServerDefaultDomain
operator|+
literal|":context="
operator|+
name|managementName
operator|+
literal|",type=processors,name=\"myManagedBean\""
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Canonical Name = {}"
argument_list|,
name|onManagedBean
operator|.
name|getCanonicalName
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"Should be registered"
argument_list|,
name|mBeanServer
operator|.
name|isRegistered
argument_list|(
name|onManagedBean
argument_list|)
argument_list|)
expr_stmt|;
comment|// Send a couple of messages to get some route statistics
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello Camel"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Camel Rocks!"
argument_list|)
expr_stmt|;
comment|// Get MBean attribute
name|int
name|camelsSeenCount
init|=
operator|(
name|Integer
operator|)
name|mBeanServer
operator|.
name|getAttribute
argument_list|(
name|onManagedBean
argument_list|,
literal|"CamelsSeenCount"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|camelsSeenCount
argument_list|)
expr_stmt|;
comment|// Stop the route via JMX
name|mBeanServer
operator|.
name|invoke
argument_list|(
name|onManagedBean
argument_list|,
literal|"resetCamelsSeenCount"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|camelsSeenCount
operator|=
operator|(
name|Integer
operator|)
name|mBeanServer
operator|.
name|getAttribute
argument_list|(
name|onManagedBean
argument_list|,
literal|"CamelsSeenCount"
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|camelsSeenCount
argument_list|)
expr_stmt|;
name|String
name|camelId
init|=
operator|(
name|String
operator|)
name|mBeanServer
operator|.
name|getAttribute
argument_list|(
name|onManagedBean
argument_list|,
literal|"CamelId"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|context
operator|.
name|getName
argument_list|()
argument_list|,
name|camelId
argument_list|)
expr_stmt|;
name|String
name|state
init|=
operator|(
name|String
operator|)
name|mBeanServer
operator|.
name|getAttribute
argument_list|(
name|onManagedBean
argument_list|,
literal|"State"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Started"
argument_list|,
name|state
argument_list|)
expr_stmt|;
name|String
name|fqn
init|=
operator|(
name|String
operator|)
name|mBeanServer
operator|.
name|getAttribute
argument_list|(
name|onManagedBean
argument_list|,
literal|"BeanClassName"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|MyManagedBean
operator|.
name|class
operator|.
name|getCanonicalName
argument_list|()
argument_list|,
name|fqn
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

