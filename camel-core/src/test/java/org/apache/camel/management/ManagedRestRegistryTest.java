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
name|org
operator|.
name|junit
operator|.
name|Test
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
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|TabularData
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
name|Route
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
name|rest
operator|.
name|DummyRestConsumerFactory
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

begin_class
DECL|class|ManagedRestRegistryTest
specifier|public
class|class
name|ManagedRestRegistryTest
extends|extends
name|ManagementTestSupport
block|{
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
name|SimpleRegistry
name|registry
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|put
argument_list|(
literal|"dummy-test"
argument_list|,
operator|new
name|DummyRestConsumerFactory
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|new
name|DefaultCamelContext
argument_list|(
name|registry
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testRestRegistry ()
specifier|public
name|void
name|testRestRegistry
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
literal|"org.apache.camel:context=camel-1,type=services,*"
argument_list|)
decl_stmt|;
comment|// number of services
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|names
init|=
name|mbeanServer
operator|.
name|queryNames
argument_list|(
name|on
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|ObjectName
name|name
init|=
literal|null
decl_stmt|;
for|for
control|(
name|ObjectName
name|service
range|:
name|names
control|)
block|{
if|if
condition|(
name|service
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
literal|"DefaultRestRegistry"
argument_list|)
condition|)
block|{
name|name
operator|=
name|service
expr_stmt|;
break|break;
block|}
block|}
name|assertNotNull
argument_list|(
literal|"Cannot find DefaultRestRegistry"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|name
argument_list|,
literal|"NumberOfRestServices"
argument_list|)
argument_list|)
expr_stmt|;
name|TabularData
name|data
init|=
operator|(
name|TabularData
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|name
argument_list|,
literal|"listRestServices"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|data
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// should not be enabled as api-doc is not enabled or camel-swagger-java is not on classpath
name|String
name|json
init|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|name
argument_list|,
literal|"apiDocAsJson"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|json
argument_list|)
expr_stmt|;
comment|// remove all routes
for|for
control|(
name|Route
name|route
range|:
name|context
operator|.
name|getRoutes
argument_list|()
control|)
block|{
name|context
operator|.
name|stopRoute
argument_list|(
name|route
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|removeRoute
argument_list|(
name|route
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|name
argument_list|,
literal|"NumberOfRestServices"
argument_list|)
argument_list|)
expr_stmt|;
name|data
operator|=
operator|(
name|TabularData
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|name
argument_list|,
literal|"listRestServices"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|data
operator|.
name|size
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
name|restConfiguration
argument_list|()
operator|.
name|host
argument_list|(
literal|"localhost"
argument_list|)
expr_stmt|;
name|rest
argument_list|(
literal|"/say/hello/{name}"
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|to
argument_list|(
literal|"direct:hello"
argument_list|)
operator|.
name|description
argument_list|(
literal|"Calling direct route"
argument_list|)
expr_stmt|;
name|rest
argument_list|(
literal|"/say/bye"
argument_list|)
operator|.
name|description
argument_list|(
literal|"the bye rest service"
argument_list|)
operator|.
name|get
argument_list|()
operator|.
name|consumes
argument_list|(
literal|"application/json"
argument_list|)
operator|.
name|description
argument_list|(
literal|"I am saying bye world"
argument_list|)
operator|.
name|route
argument_list|()
operator|.
name|routeId
argument_list|(
literal|"myRestRoute"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|constant
argument_list|(
literal|"Bye World"
argument_list|)
operator|.
name|endRest
argument_list|()
operator|.
name|post
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:update"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:hello"
argument_list|)
operator|.
name|description
argument_list|(
literal|"The hello route"
argument_list|)
operator|.
name|transform
argument_list|()
operator|.
name|simple
argument_list|(
literal|"Hello ${header.name}"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

