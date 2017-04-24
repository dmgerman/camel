begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hazelcast
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hazelcast
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|config
operator|.
name|Config
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|Hazelcast
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|HazelcastInstance
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
name|component
operator|.
name|hazelcast
operator|.
name|map
operator|.
name|HazelcastMapComponent
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
name|impl
operator|.
name|SimpleRegistry
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

begin_class
DECL|class|HazelcastConfigurationTest
specifier|public
class|class
name|HazelcastConfigurationTest
block|{
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
name|Hazelcast
operator|.
name|shutdownAll
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNamedInstance ()
specifier|public
name|void
name|testNamedInstance
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultCamelContext
name|context
init|=
literal|null
decl_stmt|;
try|try
block|{
name|String
name|instanceName
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|Config
name|config
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|config
operator|.
name|setInstanceName
argument_list|(
name|instanceName
argument_list|)
expr_stmt|;
name|config
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|setPort
argument_list|(
literal|6789
argument_list|)
expr_stmt|;
name|config
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|getJoin
argument_list|()
operator|.
name|getAwsConfig
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|config
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|getJoin
argument_list|()
operator|.
name|getMulticastConfig
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|config
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|getJoin
argument_list|()
operator|.
name|getTcpIpConfig
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Hazelcast
operator|.
name|newHazelcastInstance
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|HazelcastDefaultEndpoint
name|endpoint1
init|=
name|getHzEndpoint
argument_list|(
name|context
argument_list|,
literal|"hazelcast-map:my-cache-1?hazelcastInstanceName="
operator|+
name|instanceName
argument_list|)
decl_stmt|;
name|HazelcastDefaultEndpoint
name|endpoint2
init|=
name|getHzEndpoint
argument_list|(
name|context
argument_list|,
literal|"hazelcast-map:my-cache-2?hazelcastInstanceName="
operator|+
name|instanceName
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|endpoint1
operator|.
name|getHazelcastInstance
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|endpoint2
operator|.
name|getHazelcastInstance
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|endpoint1
operator|.
name|getHazelcastInstance
argument_list|()
operator|==
name|endpoint2
operator|.
name|getHazelcastInstance
argument_list|()
argument_list|)
expr_stmt|;
name|HazelcastMapComponent
name|component
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"hazelcast-map"
argument_list|,
name|HazelcastMapComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNull
argument_list|(
name|component
operator|.
name|getHazelcastInstance
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|HazelcastDefaultEndpoint
name|endpoint
range|:
name|Arrays
operator|.
name|asList
argument_list|(
name|endpoint1
argument_list|,
name|endpoint2
argument_list|)
control|)
block|{
name|HazelcastInstance
name|hz
init|=
name|endpoint
operator|.
name|getHazelcastInstance
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|instanceName
argument_list|,
name|hz
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|hz
operator|.
name|getConfig
argument_list|()
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|getJoin
argument_list|()
operator|.
name|getAwsConfig
argument_list|()
operator|.
name|isEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|hz
operator|.
name|getConfig
argument_list|()
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|getJoin
argument_list|()
operator|.
name|getMulticastConfig
argument_list|()
operator|.
name|isEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|hz
operator|.
name|getConfig
argument_list|()
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|getJoin
argument_list|()
operator|.
name|getTcpIpConfig
argument_list|()
operator|.
name|isEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|6789
argument_list|,
name|hz
operator|.
name|getConfig
argument_list|()
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
DECL|method|testDefaultConfiguration ()
specifier|public
name|void
name|testDefaultConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultCamelContext
name|context
init|=
literal|null
decl_stmt|;
try|try
block|{
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|HazelcastDefaultEndpoint
name|endpoint1
init|=
name|getHzEndpoint
argument_list|(
name|context
argument_list|,
literal|"hazelcast-map:my-cache-1"
argument_list|)
decl_stmt|;
name|HazelcastDefaultEndpoint
name|endpoint2
init|=
name|getHzEndpoint
argument_list|(
name|context
argument_list|,
literal|"hazelcast-map:my-cache-2"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|endpoint1
operator|.
name|getHazelcastInstance
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|endpoint2
operator|.
name|getHazelcastInstance
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|endpoint1
operator|.
name|getHazelcastInstance
argument_list|()
operator|!=
name|endpoint2
operator|.
name|getHazelcastInstance
argument_list|()
argument_list|)
expr_stmt|;
name|HazelcastMapComponent
name|component
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"hazelcast-map"
argument_list|,
name|HazelcastMapComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNull
argument_list|(
name|component
operator|.
name|getHazelcastInstance
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|HazelcastDefaultEndpoint
name|endpoint
range|:
name|Arrays
operator|.
name|asList
argument_list|(
name|endpoint1
argument_list|,
name|endpoint2
argument_list|)
control|)
block|{
name|HazelcastInstance
name|hz
init|=
name|endpoint
operator|.
name|getHazelcastInstance
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|hz
operator|.
name|getConfig
argument_list|()
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|getJoin
argument_list|()
operator|.
name|getAwsConfig
argument_list|()
operator|.
name|isEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|hz
operator|.
name|getConfig
argument_list|()
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|getJoin
argument_list|()
operator|.
name|getMulticastConfig
argument_list|()
operator|.
name|isEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|hz
operator|.
name|getConfig
argument_list|()
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|getJoin
argument_list|()
operator|.
name|getTcpIpConfig
argument_list|()
operator|.
name|isEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|5701
argument_list|,
name|hz
operator|.
name|getConfig
argument_list|()
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
DECL|method|testNamedInstanceWithConfigurationUri ()
specifier|public
name|void
name|testNamedInstanceWithConfigurationUri
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultCamelContext
name|context
init|=
literal|null
decl_stmt|;
try|try
block|{
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|HazelcastDefaultEndpoint
name|endpoint1
init|=
name|getHzEndpoint
argument_list|(
name|context
argument_list|,
literal|"hazelcast-map:my-cache-1?hazelcastConfigUri=classpath:hazelcast-named.xml"
argument_list|)
decl_stmt|;
name|HazelcastDefaultEndpoint
name|endpoint2
init|=
name|getHzEndpoint
argument_list|(
name|context
argument_list|,
literal|"hazelcast-map:my-cache-2?hazelcastConfigUri=classpath:hazelcast-named.xml"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|endpoint1
operator|.
name|getHazelcastInstance
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|endpoint2
operator|.
name|getHazelcastInstance
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|endpoint1
operator|.
name|getHazelcastInstance
argument_list|()
operator|==
name|endpoint2
operator|.
name|getHazelcastInstance
argument_list|()
argument_list|)
expr_stmt|;
name|HazelcastMapComponent
name|component
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"hazelcast-map"
argument_list|,
name|HazelcastMapComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNull
argument_list|(
name|component
operator|.
name|getHazelcastInstance
argument_list|()
argument_list|)
expr_stmt|;
name|HazelcastInstance
name|hz
init|=
name|endpoint1
operator|.
name|getHazelcastInstance
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|hz
operator|.
name|getConfig
argument_list|()
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|getJoin
argument_list|()
operator|.
name|getAwsConfig
argument_list|()
operator|.
name|isEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|hz
operator|.
name|getConfig
argument_list|()
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|getJoin
argument_list|()
operator|.
name|getMulticastConfig
argument_list|()
operator|.
name|isEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|hz
operator|.
name|getConfig
argument_list|()
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|getJoin
argument_list|()
operator|.
name|getTcpIpConfig
argument_list|()
operator|.
name|isEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|9876
argument_list|,
name|hz
operator|.
name|getConfig
argument_list|()
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
DECL|method|testCustomConfigurationUri ()
specifier|public
name|void
name|testCustomConfigurationUri
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultCamelContext
name|context
init|=
literal|null
decl_stmt|;
try|try
block|{
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|HazelcastDefaultEndpoint
name|endpoint1
init|=
name|getHzEndpoint
argument_list|(
name|context
argument_list|,
literal|"hazelcast-map:my-cache-1?hazelcastConfigUri=classpath:hazelcast-custom.xml"
argument_list|)
decl_stmt|;
name|HazelcastDefaultEndpoint
name|endpoint2
init|=
name|getHzEndpoint
argument_list|(
name|context
argument_list|,
literal|"hazelcast-map:my-cache-2?hazelcastConfigUri=classpath:hazelcast-custom.xml"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|endpoint1
operator|.
name|getHazelcastInstance
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|endpoint2
operator|.
name|getHazelcastInstance
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|endpoint1
operator|.
name|getHazelcastInstance
argument_list|()
operator|!=
name|endpoint2
operator|.
name|getHazelcastInstance
argument_list|()
argument_list|)
expr_stmt|;
name|HazelcastMapComponent
name|component
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"hazelcast-map"
argument_list|,
name|HazelcastMapComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNull
argument_list|(
name|component
operator|.
name|getHazelcastInstance
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|HazelcastDefaultEndpoint
name|endpoint
range|:
name|Arrays
operator|.
name|asList
argument_list|(
name|endpoint1
argument_list|,
name|endpoint2
argument_list|)
control|)
block|{
name|HazelcastInstance
name|hz
init|=
name|endpoint
operator|.
name|getHazelcastInstance
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|hz
operator|.
name|getConfig
argument_list|()
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|getJoin
argument_list|()
operator|.
name|getAwsConfig
argument_list|()
operator|.
name|isEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|hz
operator|.
name|getConfig
argument_list|()
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|getJoin
argument_list|()
operator|.
name|getMulticastConfig
argument_list|()
operator|.
name|isEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|hz
operator|.
name|getConfig
argument_list|()
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|getJoin
argument_list|()
operator|.
name|getTcpIpConfig
argument_list|()
operator|.
name|isEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|9876
argument_list|,
name|hz
operator|.
name|getConfig
argument_list|()
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
DECL|method|testCustomConfigurationReference ()
specifier|public
name|void
name|testCustomConfigurationReference
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultCamelContext
name|context
init|=
literal|null
decl_stmt|;
try|try
block|{
name|Config
name|config
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|config
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|setPort
argument_list|(
literal|6789
argument_list|)
expr_stmt|;
name|config
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|getJoin
argument_list|()
operator|.
name|getAwsConfig
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|config
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|getJoin
argument_list|()
operator|.
name|getMulticastConfig
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|config
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|getJoin
argument_list|()
operator|.
name|getTcpIpConfig
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|SimpleRegistry
name|reg
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|reg
operator|.
name|put
argument_list|(
literal|"my-config"
argument_list|,
name|config
argument_list|)
expr_stmt|;
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|(
name|reg
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"hazelcast-map:my-cache?hazelcastConfig=#my-config"
argument_list|)
expr_stmt|;
name|HazelcastDefaultEndpoint
name|endpoint
init|=
name|getHzEndpoint
argument_list|(
name|context
argument_list|,
literal|"hazelcast-map:my-cache?hazelcastConfig=#my-config"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|endpoint
operator|.
name|getHazelcastInstance
argument_list|()
argument_list|)
expr_stmt|;
name|HazelcastMapComponent
name|component
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"hazelcast-map"
argument_list|,
name|HazelcastMapComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNull
argument_list|(
name|component
operator|.
name|getHazelcastInstance
argument_list|()
argument_list|)
expr_stmt|;
name|HazelcastInstance
name|hz
init|=
name|endpoint
operator|.
name|getHazelcastInstance
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|hz
operator|.
name|getConfig
argument_list|()
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|getJoin
argument_list|()
operator|.
name|getAwsConfig
argument_list|()
operator|.
name|isEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|hz
operator|.
name|getConfig
argument_list|()
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|getJoin
argument_list|()
operator|.
name|getMulticastConfig
argument_list|()
operator|.
name|isEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertFalse
argument_list|(
name|hz
operator|.
name|getConfig
argument_list|()
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|getJoin
argument_list|()
operator|.
name|getTcpIpConfig
argument_list|()
operator|.
name|isEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|6789
argument_list|,
name|hz
operator|.
name|getConfig
argument_list|()
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
DECL|method|testMix ()
specifier|public
name|void
name|testMix
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultCamelContext
name|context
init|=
literal|null
decl_stmt|;
try|try
block|{
name|String
name|instanceName
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|Config
name|namedConfig
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|namedConfig
operator|.
name|setInstanceName
argument_list|(
literal|"named-"
operator|+
name|instanceName
argument_list|)
expr_stmt|;
name|namedConfig
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|setPort
argument_list|(
literal|9001
argument_list|)
expr_stmt|;
name|namedConfig
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|getJoin
argument_list|()
operator|.
name|getAwsConfig
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|namedConfig
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|getJoin
argument_list|()
operator|.
name|getMulticastConfig
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|namedConfig
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|getJoin
argument_list|()
operator|.
name|getTcpIpConfig
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Config
name|customConfig
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|customConfig
operator|.
name|setInstanceName
argument_list|(
literal|"custom-"
operator|+
name|instanceName
argument_list|)
expr_stmt|;
name|customConfig
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|setPort
argument_list|(
literal|9002
argument_list|)
expr_stmt|;
name|customConfig
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|getJoin
argument_list|()
operator|.
name|getAwsConfig
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|customConfig
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|getJoin
argument_list|()
operator|.
name|getMulticastConfig
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|customConfig
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|getJoin
argument_list|()
operator|.
name|getTcpIpConfig
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Config
name|sharedConfig
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|sharedConfig
operator|.
name|setInstanceName
argument_list|(
literal|"custom-"
operator|+
name|instanceName
argument_list|)
expr_stmt|;
name|sharedConfig
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|setPort
argument_list|(
literal|9003
argument_list|)
expr_stmt|;
name|sharedConfig
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|getJoin
argument_list|()
operator|.
name|getAwsConfig
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sharedConfig
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|getJoin
argument_list|()
operator|.
name|getMulticastConfig
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|sharedConfig
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|getJoin
argument_list|()
operator|.
name|getTcpIpConfig
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Config
name|componentConfig
init|=
operator|new
name|Config
argument_list|()
decl_stmt|;
name|sharedConfig
operator|.
name|setInstanceName
argument_list|(
literal|"component-"
operator|+
name|instanceName
argument_list|)
expr_stmt|;
name|sharedConfig
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|setPort
argument_list|(
literal|9004
argument_list|)
expr_stmt|;
name|sharedConfig
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|getJoin
argument_list|()
operator|.
name|getAwsConfig
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|sharedConfig
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|getJoin
argument_list|()
operator|.
name|getMulticastConfig
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|sharedConfig
operator|.
name|getNetworkConfig
argument_list|()
operator|.
name|getJoin
argument_list|()
operator|.
name|getTcpIpConfig
argument_list|()
operator|.
name|setEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|HazelcastInstance
name|hzNamed
init|=
name|Hazelcast
operator|.
name|newHazelcastInstance
argument_list|(
name|namedConfig
argument_list|)
decl_stmt|;
name|HazelcastInstance
name|hzShared
init|=
name|Hazelcast
operator|.
name|newHazelcastInstance
argument_list|(
name|sharedConfig
argument_list|)
decl_stmt|;
name|HazelcastInstance
name|hzComponent
init|=
name|Hazelcast
operator|.
name|newHazelcastInstance
argument_list|(
name|componentConfig
argument_list|)
decl_stmt|;
name|SimpleRegistry
name|reg
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|reg
operator|.
name|put
argument_list|(
name|customConfig
operator|.
name|getInstanceName
argument_list|()
argument_list|,
name|customConfig
argument_list|)
expr_stmt|;
name|reg
operator|.
name|put
argument_list|(
name|sharedConfig
operator|.
name|getInstanceName
argument_list|()
argument_list|,
name|hzShared
argument_list|)
expr_stmt|;
name|HazelcastMapComponent
name|component
init|=
operator|new
name|HazelcastMapComponent
argument_list|()
decl_stmt|;
name|component
operator|.
name|setHazelcastInstance
argument_list|(
name|hzComponent
argument_list|)
expr_stmt|;
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|(
name|reg
argument_list|)
expr_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
literal|"hazelcast-map"
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|HazelcastDefaultEndpoint
name|endpoint1
init|=
name|getHzEndpoint
argument_list|(
name|context
argument_list|,
literal|"hazelcast-map:my-cache-1?hazelcastInstanceName="
operator|+
name|namedConfig
operator|.
name|getInstanceName
argument_list|()
argument_list|)
decl_stmt|;
name|HazelcastDefaultEndpoint
name|endpoint2
init|=
name|getHzEndpoint
argument_list|(
name|context
argument_list|,
literal|"hazelcast-map:my-cache-2?hazelcastConfig=#"
operator|+
name|customConfig
operator|.
name|getInstanceName
argument_list|()
argument_list|)
decl_stmt|;
name|HazelcastDefaultEndpoint
name|endpoint3
init|=
name|getHzEndpoint
argument_list|(
name|context
argument_list|,
literal|"hazelcast-map:my-cache-2?hazelcastInstance=#"
operator|+
name|sharedConfig
operator|.
name|getInstanceName
argument_list|()
argument_list|)
decl_stmt|;
name|HazelcastDefaultEndpoint
name|endpoint4
init|=
name|getHzEndpoint
argument_list|(
name|context
argument_list|,
literal|"hazelcast-map:my-cache-4"
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|endpoint1
operator|.
name|getHazelcastInstance
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|endpoint2
operator|.
name|getHazelcastInstance
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|endpoint3
operator|.
name|getHazelcastInstance
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|endpoint4
operator|.
name|getHazelcastInstance
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|Hazelcast
operator|.
name|getAllHazelcastInstances
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|hzNamed
operator|==
name|endpoint1
operator|.
name|getHazelcastInstance
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|Hazelcast
operator|.
name|getHazelcastInstanceByName
argument_list|(
name|customConfig
operator|.
name|getInstanceName
argument_list|()
argument_list|)
operator|==
name|endpoint2
operator|.
name|getHazelcastInstance
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|hzShared
operator|==
name|endpoint3
operator|.
name|getHazelcastInstance
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|hzComponent
operator|==
name|endpoint4
operator|.
name|getHazelcastInstance
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|component
operator|.
name|getHazelcastInstance
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
name|hzComponent
operator|==
name|component
operator|.
name|getHazelcastInstance
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|Hazelcast
operator|.
name|getAllHazelcastInstances
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|context
operator|!=
literal|null
operator|&&
name|context
operator|.
name|isStarted
argument_list|()
condition|)
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|getHzEndpoint (CamelContext context, String uri)
specifier|private
name|HazelcastDefaultEndpoint
name|getHzEndpoint
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
return|return
operator|(
name|HazelcastDefaultEndpoint
operator|)
name|context
operator|.
name|getEndpoint
argument_list|(
name|uri
argument_list|)
return|;
block|}
block|}
end_class

end_unit

