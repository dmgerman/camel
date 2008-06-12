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

begin_class
DECL|class|JmxInstrumentationUsingDefaultsTest
specifier|public
class|class
name|JmxInstrumentationUsingDefaultsTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|DEFAULT_PORT
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_PORT
init|=
literal|1099
decl_stmt|;
DECL|field|iAgent
specifier|protected
name|DefaultInstrumentationAgent
name|iAgent
decl_stmt|;
DECL|field|domainName
specifier|protected
name|String
name|domainName
init|=
name|DefaultInstrumentationAgent
operator|.
name|DEFAULT_DOMAIN
decl_stmt|;
DECL|field|sleepSoYouCanBrowseInJConsole
specifier|protected
name|boolean
name|sleepSoYouCanBrowseInJConsole
decl_stmt|;
DECL|method|testMBeansRegistered ()
specifier|public
name|void
name|testMBeansRegistered
parameter_list|()
throws|throws
name|Exception
block|{
name|assertNotNull
argument_list|(
name|iAgent
operator|.
name|getMBeanServer
argument_list|()
argument_list|)
expr_stmt|;
comment|// assertEquals(domainName, iAgent.getMBeanServer().getDefaultDomain());
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
name|s
init|=
name|iAgent
operator|.
name|getMBeanServer
argument_list|()
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
name|domainName
operator|+
literal|":type=endpoint,*"
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
name|iAgent
operator|.
name|getMBeanServer
argument_list|()
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
name|iAgent
operator|.
name|getMBeanServer
argument_list|()
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
name|domainName
operator|+
literal|":type=processor,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Could not find 1 processor: "
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
name|iAgent
operator|.
name|getMBeanServer
argument_list|()
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
name|domainName
operator|+
literal|":type=route,*"
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
if|if
condition|(
name|sleepSoYouCanBrowseInJConsole
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|100000
argument_list|)
expr_stmt|;
block|}
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
name|MBeanServer
name|mbs
init|=
name|iAgent
operator|.
name|getMBeanServer
argument_list|()
decl_stmt|;
name|verifyCounter
argument_list|(
name|mbs
argument_list|,
operator|new
name|ObjectName
argument_list|(
name|domainName
operator|+
literal|":type=route,*"
argument_list|)
argument_list|)
expr_stmt|;
name|verifyCounter
argument_list|(
name|mbs
argument_list|,
operator|new
name|ObjectName
argument_list|(
name|domainName
operator|+
literal|":type=processor,*"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|verifyCounter (MBeanServer mbs, ObjectName name)
specifier|private
name|void
name|verifyCounter
parameter_list|(
name|MBeanServer
name|mbs
parameter_list|,
name|ObjectName
name|name
parameter_list|)
throws|throws
name|Exception
block|{
name|Set
name|s
init|=
name|mbs
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
operator|(
name|ObjectName
operator|)
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
name|mbs
operator|.
name|getAttribute
argument_list|(
name|pcob
argument_list|,
literal|"NumExchanges"
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
name|assertTrue
argument_list|(
name|valueofNumExchanges
operator|==
literal|1
argument_list|)
expr_stmt|;
name|Long
name|valueofNumCompleted
init|=
operator|(
name|Long
operator|)
name|mbs
operator|.
name|getAttribute
argument_list|(
name|pcob
argument_list|,
literal|"NumCompleted"
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
name|assertTrue
argument_list|(
name|valueofNumCompleted
operator|==
literal|1
argument_list|)
expr_stmt|;
name|Long
name|valueofNumFailed
init|=
operator|(
name|Long
operator|)
name|mbs
operator|.
name|getAttribute
argument_list|(
name|pcob
argument_list|,
literal|"NumFailed"
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
name|assertTrue
argument_list|(
name|valueofNumFailed
operator|==
literal|0
argument_list|)
expr_stmt|;
name|Double
name|valueofMinProcessingTime
init|=
operator|(
name|Double
operator|)
name|mbs
operator|.
name|getAttribute
argument_list|(
name|pcob
argument_list|,
literal|"MinProcessingTimeMillis"
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
operator|>
literal|0
argument_list|)
expr_stmt|;
name|Double
name|valueofMaxProcessingTime
init|=
operator|(
name|Double
operator|)
name|mbs
operator|.
name|getAttribute
argument_list|(
name|pcob
argument_list|,
literal|"MaxProcessingTimeMillis"
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
operator|>
literal|0
argument_list|)
expr_stmt|;
name|Double
name|valueofMeanProcessingTime
init|=
operator|(
name|Double
operator|)
name|mbs
operator|.
name|getAttribute
argument_list|(
name|pcob
argument_list|,
literal|"MeanProcessingTimeMillis"
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
name|Double
name|totalProcessingTime
init|=
operator|(
name|Double
operator|)
name|mbs
operator|.
name|getAttribute
argument_list|(
name|pcob
argument_list|,
literal|"TotalProcessingTimeMillis"
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
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Expected first completion time to be available"
argument_list|,
name|mbs
operator|.
name|getAttribute
argument_list|(
name|pcob
argument_list|,
literal|"FirstExchangeCompletionTime"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Expected last completion time to be available"
argument_list|,
name|mbs
operator|.
name|getAttribute
argument_list|(
name|pcob
argument_list|,
literal|"LastExchangeCompletionTime"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|enableJmx ()
specifier|protected
name|void
name|enableJmx
parameter_list|()
block|{
name|iAgent
operator|.
name|enableJmx
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|createInstrumentationAgent
argument_list|(
name|context
argument_list|,
name|DEFAULT_PORT
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
DECL|method|createInstrumentationAgent (CamelContext context, int port)
specifier|protected
name|void
name|createInstrumentationAgent
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|int
name|port
parameter_list|)
throws|throws
name|Exception
block|{
name|iAgent
operator|=
operator|new
name|DefaultInstrumentationAgent
argument_list|()
expr_stmt|;
name|iAgent
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|enableJmx
argument_list|()
expr_stmt|;
name|iAgent
operator|.
name|start
argument_list|()
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
name|from
argument_list|(
literal|"direct:start"
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
DECL|method|tearDown ()
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|iAgent
operator|.
name|stop
argument_list|()
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

