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
name|ArrayList
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
name|ExtendedCamelContext
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|ManagedBeanIntrospectionTest
specifier|public
class|class
name|ManagedBeanIntrospectionTest
extends|extends
name|ManagementTestSupport
block|{
DECL|method|getDummy ()
specifier|public
name|String
name|getDummy
parameter_list|()
block|{
return|return
literal|"MyDummy"
return|;
block|}
annotation|@
name|Test
DECL|method|testManageBeanIntrospection ()
specifier|public
name|void
name|testManageBeanIntrospection
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
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
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
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// get the bean introspection for the route
name|MBeanServer
name|mbeanServer
init|=
name|getMBeanServer
argument_list|()
decl_stmt|;
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|set
init|=
name|mbeanServer
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
literal|"*:type=services,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ObjectName
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|set
argument_list|)
decl_stmt|;
name|ObjectName
name|on
init|=
literal|null
decl_stmt|;
for|for
control|(
name|ObjectName
name|name
range|:
name|list
control|)
block|{
if|if
condition|(
name|name
operator|.
name|getCanonicalName
argument_list|()
operator|.
name|contains
argument_list|(
literal|"DefaultBeanIntrospection"
argument_list|)
condition|)
block|{
name|on
operator|=
name|name
expr_stmt|;
break|break;
block|}
block|}
name|assertNotNull
argument_list|(
literal|"Should have found DefaultBeanIntrospection"
argument_list|,
name|on
argument_list|)
expr_stmt|;
comment|// reset counter
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"resetCounters"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|Long
name|counter
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
literal|"InvokedCounter"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Should not have been invoked"
argument_list|,
literal|0
argument_list|,
name|counter
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|Object
name|dummy
init|=
name|context
operator|.
name|adapt
argument_list|(
name|ExtendedCamelContext
operator|.
name|class
argument_list|)
operator|.
name|getBeanIntrospection
argument_list|()
operator|.
name|getOrElseProperty
argument_list|(
name|this
argument_list|,
literal|"dummy"
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"MyDummy"
argument_list|,
name|dummy
argument_list|)
expr_stmt|;
name|counter
operator|=
operator|(
name|Long
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"InvokedCounter"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should have been invoked"
argument_list|,
literal|1
argument_list|,
name|counter
operator|.
name|intValue
argument_list|()
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
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

