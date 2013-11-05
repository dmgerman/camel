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

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|ManagedCBRTest
specifier|public
class|class
name|ManagedCBRTest
extends|extends
name|ManagementTestSupport
block|{
comment|// CAMEL-4044: mbeans not registered for children of choice
DECL|method|testManagedCBR ()
specifier|public
name|void
name|testManagedCBR
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
literal|"org.apache.camel:context=camel-1,type=routes,name=\"route\""
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"MBean '"
operator|+
name|on
operator|+
literal|"' not registered"
argument_list|,
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|on
argument_list|)
argument_list|)
expr_stmt|;
name|on
operator|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=camel-1,type=processors,name=\"task-a\""
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"MBean '"
operator|+
name|on
operator|+
literal|"' not registered"
argument_list|,
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|on
argument_list|)
argument_list|)
expr_stmt|;
name|on
operator|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=camel-1,type=processors,name=\"choice\""
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"MBean '"
operator|+
name|on
operator|+
literal|"' not registered"
argument_list|,
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|on
argument_list|)
argument_list|)
expr_stmt|;
name|on
operator|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=camel-1,type=processors,name=\"task-b\""
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"MBean '"
operator|+
name|on
operator|+
literal|"' not registered"
argument_list|,
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|on
argument_list|)
argument_list|)
expr_stmt|;
name|on
operator|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=camel-1,type=processors,name=\"task-c\""
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"MBean '"
operator|+
name|on
operator|+
literal|"' not registered"
argument_list|,
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|on
argument_list|)
argument_list|)
expr_stmt|;
name|on
operator|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=camel-1,type=processors,name=\"task-d\""
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"MBean '"
operator|+
name|on
operator|+
literal|"' not registered"
argument_list|,
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|on
argument_list|)
argument_list|)
expr_stmt|;
name|on
operator|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=camel-1,type=processors,name=\"task-e\""
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"MBean '"
operator|+
name|on
operator|+
literal|"' not registered"
argument_list|,
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|on
argument_list|)
argument_list|)
expr_stmt|;
name|on
operator|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=camel-1,type=processors,name=\"task-done\""
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"MBean '"
operator|+
name|on
operator|+
literal|"' not registered"
argument_list|,
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|on
argument_list|)
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
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"route"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|id
argument_list|(
literal|"task-a"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|id
argument_list|(
literal|"choice"
argument_list|)
operator|.
name|when
argument_list|(
name|simple
argument_list|(
literal|"${body} contains Camel"
argument_list|)
argument_list|)
operator|.
name|id
argument_list|(
literal|"when"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|id
argument_list|(
literal|"task-b"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:c"
argument_list|)
operator|.
name|id
argument_list|(
literal|"task-c"
argument_list|)
operator|.
name|when
argument_list|(
name|simple
argument_list|(
literal|"${body} contains Donkey"
argument_list|)
argument_list|)
operator|.
name|id
argument_list|(
literal|"when2"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:d"
argument_list|)
operator|.
name|id
argument_list|(
literal|"task-d"
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|id
argument_list|(
literal|"otherwise"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:e"
argument_list|)
operator|.
name|id
argument_list|(
literal|"task-e"
argument_list|)
operator|.
name|end
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:done"
argument_list|)
operator|.
name|id
argument_list|(
literal|"task-done"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

