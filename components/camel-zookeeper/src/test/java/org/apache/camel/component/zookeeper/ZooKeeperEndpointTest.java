begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.zookeeper
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|zookeeper
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|Attribute
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
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
name|zookeeper
operator|.
name|ZooKeeperTestSupport
operator|.
name|TestZookeeperClient
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
name|zookeeper
operator|.
name|ZooKeeperTestSupport
operator|.
name|TestZookeeperServer
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
name|JmxInstrumentationUsingDefaultsTest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|zookeeper
operator|.
name|ZooKeeper
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
name|springframework
operator|.
name|jmx
operator|.
name|support
operator|.
name|JmxUtils
import|;
end_import

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"all"
argument_list|)
DECL|class|ZooKeeperEndpointTest
specifier|public
class|class
name|ZooKeeperEndpointTest
extends|extends
name|JmxInstrumentationUsingDefaultsTest
block|{
DECL|field|teardownAfter
specifier|private
specifier|static
name|int
name|teardownAfter
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
if|if
condition|(
name|teardownAfter
operator|==
literal|0
condition|)
block|{
name|ZooKeeperTestSupport
operator|.
name|setupTestServer
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|setUp
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
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
if|if
condition|(
operator|++
name|teardownAfter
operator|==
literal|3
condition|)
block|{
name|ZooKeeperTestSupport
operator|.
name|shutdownServer
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|testEnpointConfigurationCanBeSetViaJMX ()
specifier|public
specifier|synchronized
name|void
name|testEnpointConfigurationCanBeSetViaJMX
parameter_list|()
throws|throws
name|Exception
block|{
name|Set
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
literal|":type=endpoints,name=\"zoo:*\",*"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Could not find zookeper endpoint: "
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
name|ObjectName
name|zepName
init|=
operator|new
name|ArrayList
argument_list|<
name|ObjectName
argument_list|>
argument_list|(
name|s
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|verifyManagedAttribute
argument_list|(
name|zepName
argument_list|,
literal|"Path"
argument_list|,
literal|"/someotherpath"
argument_list|)
expr_stmt|;
name|verifyManagedAttribute
argument_list|(
name|zepName
argument_list|,
literal|"Create"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|verifyManagedAttribute
argument_list|(
name|zepName
argument_list|,
literal|"Repeat"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|verifyManagedAttribute
argument_list|(
name|zepName
argument_list|,
literal|"ListChildren"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|verifyManagedAttribute
argument_list|(
name|zepName
argument_list|,
literal|"AwaitExistence"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|verifyManagedAttribute
argument_list|(
name|zepName
argument_list|,
literal|"Timeout"
argument_list|,
literal|12345
argument_list|)
expr_stmt|;
name|verifyManagedAttribute
argument_list|(
name|zepName
argument_list|,
literal|"Backoff"
argument_list|,
literal|12345L
argument_list|)
expr_stmt|;
name|mbsc
operator|.
name|invoke
argument_list|(
name|zepName
argument_list|,
literal|"clearServers"
argument_list|,
literal|null
argument_list|,
name|JmxUtils
operator|.
name|getMethodSignature
argument_list|(
name|ZooKeeperEndpoint
operator|.
name|class
operator|.
name|getMethod
argument_list|(
literal|"clearServers"
argument_list|,
literal|null
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|mbsc
operator|.
name|invoke
argument_list|(
name|zepName
argument_list|,
literal|"addServer"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"someserver:12345"
block|}
argument_list|,
name|JmxUtils
operator|.
name|getMethodSignature
argument_list|(
name|ZooKeeperEndpoint
operator|.
name|class
operator|.
name|getMethod
argument_list|(
literal|"addServer"
argument_list|,
operator|new
name|Class
index|[]
block|{
name|String
operator|.
name|class
block|}
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|verifyManagedAttribute (ObjectName zepName, String attributeName, String attributeValue)
specifier|private
name|void
name|verifyManagedAttribute
parameter_list|(
name|ObjectName
name|zepName
parameter_list|,
name|String
name|attributeName
parameter_list|,
name|String
name|attributeValue
parameter_list|)
throws|throws
name|Exception
block|{
name|mbsc
operator|.
name|setAttribute
argument_list|(
name|zepName
argument_list|,
operator|new
name|Attribute
argument_list|(
name|attributeName
argument_list|,
name|attributeValue
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|attributeValue
argument_list|,
name|mbsc
operator|.
name|getAttribute
argument_list|(
name|zepName
argument_list|,
name|attributeName
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|verifyManagedAttribute (ObjectName zepName, String attributeName, Integer attributeValue)
specifier|private
name|void
name|verifyManagedAttribute
parameter_list|(
name|ObjectName
name|zepName
parameter_list|,
name|String
name|attributeName
parameter_list|,
name|Integer
name|attributeValue
parameter_list|)
throws|throws
name|Exception
block|{
name|mbsc
operator|.
name|setAttribute
argument_list|(
name|zepName
argument_list|,
operator|new
name|Attribute
argument_list|(
name|attributeName
argument_list|,
name|attributeValue
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|attributeValue
argument_list|,
name|mbsc
operator|.
name|getAttribute
argument_list|(
name|zepName
argument_list|,
name|attributeName
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|verifyManagedAttribute (ObjectName zepName, String attributeName, Boolean attributeValue)
specifier|private
name|void
name|verifyManagedAttribute
parameter_list|(
name|ObjectName
name|zepName
parameter_list|,
name|String
name|attributeName
parameter_list|,
name|Boolean
name|attributeValue
parameter_list|)
throws|throws
name|Exception
block|{
name|mbsc
operator|.
name|setAttribute
argument_list|(
name|zepName
argument_list|,
operator|new
name|Attribute
argument_list|(
name|attributeName
argument_list|,
name|attributeValue
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|attributeValue
argument_list|,
name|mbsc
operator|.
name|getAttribute
argument_list|(
name|zepName
argument_list|,
name|attributeName
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|verifyManagedAttribute (ObjectName zepName, String attributeName, Long attributeValue)
specifier|private
name|void
name|verifyManagedAttribute
parameter_list|(
name|ObjectName
name|zepName
parameter_list|,
name|String
name|attributeName
parameter_list|,
name|Long
name|attributeValue
parameter_list|)
throws|throws
name|Exception
block|{
name|mbsc
operator|.
name|setAttribute
argument_list|(
name|zepName
argument_list|,
operator|new
name|Attribute
argument_list|(
name|attributeName
argument_list|,
name|attributeValue
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|attributeValue
argument_list|,
name|mbsc
operator|.
name|getAttribute
argument_list|(
name|zepName
argument_list|,
name|attributeName
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|testCounters ()
specifier|public
name|void
name|testCounters
parameter_list|()
throws|throws
name|Exception
block|{     }
annotation|@
name|Override
DECL|method|testMBeansRegistered ()
specifier|public
name|void
name|testMBeansRegistered
parameter_list|()
throws|throws
name|Exception
block|{     }
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
literal|"zoo://localhost:39913/node"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:test"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

