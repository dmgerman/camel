begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Set
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
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * A unit test which verifies disabling of JMX instrumentation.  */
end_comment

begin_class
DECL|class|JmxInstrumentationDisableOnCamelContextTest
specifier|public
class|class
name|JmxInstrumentationDisableOnCamelContextTest
extends|extends
name|JmxInstrumentationUsingPropertiesTest
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
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|camel
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|camel
operator|.
name|disableJMX
argument_list|()
expr_stmt|;
return|return
name|camel
return|;
block|}
annotation|@
name|Override
annotation|@
name|Test
DECL|method|testMBeansRegistered ()
specifier|public
name|void
name|testMBeansRegistered
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
literal|"Could not find 0 endpoints: "
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
literal|":type=contexts,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Could not find 0 context: "
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
literal|":type=processors,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Could not find 0 processor: "
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
literal|"Could not find 0 route: "
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
block|}
annotation|@
name|Override
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
literal|0
argument_list|,
name|s
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

