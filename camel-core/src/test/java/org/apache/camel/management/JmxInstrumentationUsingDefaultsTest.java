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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|management
operator|.
name|ManagementFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|java
operator|.
name|util
operator|.
name|Set
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
name|ContextTestSupport
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
import|;
end_import

begin_comment
comment|/**  * This test verifies JMX is enabled by default and it uses local mbean  * server to conduct the test as connector server is not enabled by default.  *  * @version   */
end_comment

begin_class
DECL|class|JmxInstrumentationUsingDefaultsTest
specifier|public
class|class
name|JmxInstrumentationUsingDefaultsTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|domainName
specifier|protected
name|String
name|domainName
init|=
name|DefaultManagementAgent
operator|.
name|DEFAULT_DOMAIN
decl_stmt|;
DECL|field|mbsc
specifier|protected
name|MBeanServerConnection
name|mbsc
decl_stmt|;
DECL|field|sleepForConnection
specifier|protected
name|long
name|sleepForConnection
decl_stmt|;
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
DECL|method|assertDefaultDomain ()
specifier|protected
name|void
name|assertDefaultDomain
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|System
operator|.
name|getProperty
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|USE_PLATFORM_MBS
argument_list|)
operator|!=
literal|null
operator|&&
operator|!
name|Boolean
operator|.
name|getBoolean
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|USE_PLATFORM_MBS
argument_list|)
condition|)
block|{
name|assertEquals
argument_list|(
name|domainName
argument_list|,
name|mbsc
operator|.
name|getDefaultDomain
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|testMBeansRegistered ()
specifier|public
name|void
name|testMBeansRegistered
parameter_list|()
throws|throws
name|Exception
block|{
name|assertDefaultDomain
argument_list|()
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:end"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|s
init|=
name|mbsc
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
name|domainName
operator|+
literal|":type=endpoints,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Could not find 2 endpoints: "
operator|+
name|s
argument_list|,
literal|2
argument_list|,
name|s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|s
operator|=
name|mbsc
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
name|domainName
operator|+
literal|":type=context,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Could not find 1 context: "
operator|+
name|s
argument_list|,
literal|1
argument_list|,
name|s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|s
operator|=
name|mbsc
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
name|domainName
operator|+
literal|":type=processors,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Could not find 1 processors: "
operator|+
name|s
argument_list|,
literal|1
argument_list|,
name|s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|s
operator|=
name|mbsc
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
name|domainName
operator|+
literal|":type=consumers,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Could not find 1 consumers: "
operator|+
name|s
argument_list|,
literal|1
argument_list|,
name|s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|s
operator|=
name|mbsc
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
name|domainName
operator|+
literal|":type=producers,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Could not find 2 producers: "
operator|+
name|s
argument_list|,
literal|0
argument_list|,
name|s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|s
operator|=
name|mbsc
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
name|domainName
operator|+
literal|":type=routes,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Could not find 1 route: "
operator|+
name|s
argument_list|,
literal|1
argument_list|,
name|s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testCounters ()
specifier|public
name|void
name|testCounters
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|resultEndpoint
init|=
name|resolveMandatoryEndpoint
argument_list|(
literal|"mock:end"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"<hello>world!</hello>"
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<hello>world!</hello>"
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|verifyCounter
argument_list|(
name|mbsc
argument_list|,
operator|new
name|ObjectName
argument_list|(
name|domainName
operator|+
literal|":type=routes,*"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|verifyCounter (MBeanServerConnection beanServer, ObjectName name)
specifier|protected
name|void
name|verifyCounter
parameter_list|(
name|MBeanServerConnection
name|beanServer
parameter_list|,
name|ObjectName
name|name
parameter_list|)
throws|throws
name|Exception
block|{
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|s
init|=
name|beanServer
operator|.
name|queryNames
argument_list|(
name|name
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Found mbeans: "
operator|+
name|s
argument_list|,
literal|1
argument_list|,
name|s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Iterator
argument_list|<
name|ObjectName
argument_list|>
name|iter
init|=
name|s
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|ObjectName
name|pcob
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|Long
name|valueofNumExchanges
init|=
operator|(
name|Long
operator|)
name|beanServer
operator|.
name|getAttribute
argument_list|(
name|pcob
argument_list|,
literal|"ExchangesTotal"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Expected attribute found. MBean registered under a "
operator|+
literal|"'<domain>:name=Stats,*' key must be of type PerformanceCounter.class"
argument_list|,
name|valueofNumExchanges
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
literal|1
argument_list|)
argument_list|,
name|valueofNumExchanges
argument_list|)
expr_stmt|;
name|Long
name|valueofNumCompleted
init|=
operator|(
name|Long
operator|)
name|beanServer
operator|.
name|getAttribute
argument_list|(
name|pcob
argument_list|,
literal|"ExchangesCompleted"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Expected attribute found. MBean registered under a "
operator|+
literal|"'<domain>:name=Stats,*' key must be of type PerformanceCounter.class"
argument_list|,
name|valueofNumCompleted
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
literal|1
argument_list|)
argument_list|,
name|valueofNumCompleted
argument_list|)
expr_stmt|;
name|Long
name|valueofNumFailed
init|=
operator|(
name|Long
operator|)
name|beanServer
operator|.
name|getAttribute
argument_list|(
name|pcob
argument_list|,
literal|"ExchangesFailed"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Expected attribute found. MBean registered under a "
operator|+
literal|"'<domain>:name=Stats,*' key must be of type PerformanceCounter.class"
argument_list|,
name|valueofNumFailed
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
literal|0
argument_list|)
argument_list|,
name|valueofNumFailed
argument_list|)
expr_stmt|;
name|Long
name|valueofMinProcessingTime
init|=
operator|(
name|Long
operator|)
name|beanServer
operator|.
name|getAttribute
argument_list|(
name|pcob
argument_list|,
literal|"MinProcessingTime"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Expected attribute found. MBean registered under a "
operator|+
literal|"'<domain>:name=Stats,*' key must be of type PerformanceCounter.class"
argument_list|,
name|valueofMinProcessingTime
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|valueofMinProcessingTime
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|Long
name|valueofMaxProcessingTime
init|=
operator|(
name|Long
operator|)
name|beanServer
operator|.
name|getAttribute
argument_list|(
name|pcob
argument_list|,
literal|"MaxProcessingTime"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Expected attribute found. MBean registered under a "
operator|+
literal|"'<domain>:name=Stats,*' key must be of type PerformanceCounter.class"
argument_list|,
name|valueofMaxProcessingTime
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|valueofMaxProcessingTime
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|Long
name|valueofMeanProcessingTime
init|=
operator|(
name|Long
operator|)
name|beanServer
operator|.
name|getAttribute
argument_list|(
name|pcob
argument_list|,
literal|"MeanProcessingTime"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Expected attribute found. MBean registered under a "
operator|+
literal|"'<domain>:name=Stats,*' key must be of type PerformanceCounter.class"
argument_list|,
name|valueofMeanProcessingTime
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|valueofMeanProcessingTime
operator|>=
name|valueofMinProcessingTime
operator|&&
name|valueofMeanProcessingTime
operator|<=
name|valueofMaxProcessingTime
argument_list|)
expr_stmt|;
name|Long
name|totalProcessingTime
init|=
operator|(
name|Long
operator|)
name|beanServer
operator|.
name|getAttribute
argument_list|(
name|pcob
argument_list|,
literal|"TotalProcessingTime"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Expected attribute found. MBean registered under a "
operator|+
literal|"'<domain>:name=Stats,*' key must be of type PerformanceCounter.class"
argument_list|,
name|totalProcessingTime
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|totalProcessingTime
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|Long
name|lastProcessingTime
init|=
operator|(
name|Long
operator|)
name|beanServer
operator|.
name|getAttribute
argument_list|(
name|pcob
argument_list|,
literal|"LastProcessingTime"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Expected attribute found. MBean registered under a "
operator|+
literal|"'<domain>:name=Stats,*' key must be of type PerformanceCounter.class"
argument_list|,
name|lastProcessingTime
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|lastProcessingTime
operator|>=
literal|0
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Expected first completion time to be available"
argument_list|,
name|beanServer
operator|.
name|getAttribute
argument_list|(
name|pcob
argument_list|,
literal|"FirstExchangeCompletedTimestamp"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Expected last completion time to be available"
argument_list|,
name|beanServer
operator|.
name|getAttribute
argument_list|(
name|pcob
argument_list|,
literal|"LastExchangeCompletedTimestamp"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
comment|// need a little delay for fast computers being able to process
comment|// the exchange in 0 millis and we need to simulate a little computation time
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|delay
argument_list|(
literal|10
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:end"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
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
try|try
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
name|releaseMBeanServers
argument_list|()
expr_stmt|;
name|mbsc
operator|=
literal|null
expr_stmt|;
block|}
finally|finally
block|{
comment|// restore environment to original state
comment|// the following properties may have been set by specialization of this test class
name|System
operator|.
name|clearProperty
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|USE_PLATFORM_MBS
argument_list|)
expr_stmt|;
name|System
operator|.
name|clearProperty
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|DOMAIN
argument_list|)
expr_stmt|;
name|System
operator|.
name|clearProperty
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|MBEAN_DOMAIN
argument_list|)
expr_stmt|;
name|System
operator|.
name|clearProperty
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|CREATE_CONNECTOR
argument_list|)
expr_stmt|;
name|System
operator|.
name|clearProperty
argument_list|(
name|JmxSystemPropertyKeys
operator|.
name|REGISTRY_PORT
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|releaseMBeanServers ()
specifier|protected
name|void
name|releaseMBeanServers
parameter_list|()
block|{
for|for
control|(
name|MBeanServer
name|server
range|:
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
name|mbsc
operator|=
name|ManagementFactory
operator|.
name|getPlatformMBeanServer
argument_list|()
expr_stmt|;
block|}
return|return
name|mbsc
return|;
block|}
block|}
end_class

end_unit

