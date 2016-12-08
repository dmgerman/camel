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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|ManagedCamelContextSuspendStartTest
specifier|public
class|class
name|ManagedCamelContextSuspendStartTest
extends|extends
name|ManagementTestSupport
block|{
DECL|method|testManagedCamelContext ()
specifier|public
name|void
name|testManagedCamelContext
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
name|MBeanServer
name|mbeanServer
init|=
name|getMBeanServer
argument_list|()
decl_stmt|;
name|ObjectName
name|on
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=camel-1,type=context,name=\"camel-1\""
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should be registered"
argument_list|,
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|on
argument_list|)
argument_list|)
expr_stmt|;
name|String
name|name
init|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"CamelId"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"camel-1"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|String
name|uptime
init|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"Uptime"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|uptime
argument_list|)
expr_stmt|;
name|long
name|uptimeMillis
init|=
operator|(
name|Long
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"UptimeMillis"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|uptimeMillis
operator|>
literal|0
argument_list|)
expr_stmt|;
name|String
name|status
init|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"State"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Started"
argument_list|,
name|status
argument_list|)
expr_stmt|;
comment|// invoke operations
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|Object
name|reply
init|=
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"requestBody"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"direct:foo"
block|,
literal|"Hello World"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|,
literal|"java.lang.Object"
block|}
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
comment|// suspend Camel
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"suspend"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|status
operator|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"State"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Suspended"
argument_list|,
name|status
argument_list|)
expr_stmt|;
comment|// start Camel
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"start"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|status
operator|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"State"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Started"
argument_list|,
name|status
argument_list|)
expr_stmt|;
name|reply
operator|=
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"requestBody"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|"direct:foo"
block|,
literal|"Hello Camel"
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"java.lang.String"
block|,
literal|"java.lang.Object"
block|}
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
block|}
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
literal|"direct:foo"
argument_list|)
operator|.
name|delay
argument_list|(
literal|10
argument_list|)
operator|.
name|transform
argument_list|(
name|constant
argument_list|(
literal|"Bye World"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

