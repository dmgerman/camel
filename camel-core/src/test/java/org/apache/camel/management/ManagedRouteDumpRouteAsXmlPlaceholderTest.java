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
name|properties
operator|.
name|PropertiesComponent
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
comment|/**  * @version   */
end_comment

begin_class
DECL|class|ManagedRouteDumpRouteAsXmlPlaceholderTest
specifier|public
class|class
name|ManagedRouteDumpRouteAsXmlPlaceholderTest
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
name|CamelContext
name|answer
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|PropertiesComponent
name|pc
init|=
operator|new
name|PropertiesComponent
argument_list|()
decl_stmt|;
name|pc
operator|.
name|setLocation
argument_list|(
literal|"classpath:org/apache/camel/management/rest.properties"
argument_list|)
expr_stmt|;
name|answer
operator|.
name|addComponent
argument_list|(
literal|"properties"
argument_list|,
name|pc
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Test
DECL|method|testDumpAsXml ()
specifier|public
name|void
name|testDumpAsXml
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
name|getRouteObjectName
argument_list|(
name|mbeanServer
argument_list|)
decl_stmt|;
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
comment|// should be started
name|String
name|routeId
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
literal|"RouteId"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"myRoute"
argument_list|,
name|routeId
argument_list|)
expr_stmt|;
name|String
name|xml
init|=
operator|(
name|String
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"dumpRouteAsXml"
argument_list|,
operator|new
name|Object
index|[]
block|{
literal|true
block|}
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"boolean"
block|}
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|xml
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
name|xml
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"direct:start"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"route"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"myRoute"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|xml
operator|.
name|contains
argument_list|(
literal|"mock:result"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getRouteObjectName (MBeanServer mbeanServer)
specifier|static
name|ObjectName
name|getRouteObjectName
parameter_list|(
name|MBeanServer
name|mbeanServer
parameter_list|)
throws|throws
name|Exception
block|{
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
literal|"*:type=routes,*"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|set
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|set
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
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
literal|"{{start}}"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"myRoute"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Got ${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"{{result}}"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

