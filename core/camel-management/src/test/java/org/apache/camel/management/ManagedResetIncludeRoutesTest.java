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
name|AttributeValueExp
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
name|Query
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|QueryExp
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|StringValueExp
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
DECL|class|ManagedResetIncludeRoutesTest
specifier|public
class|class
name|ManagedResetIncludeRoutesTest
extends|extends
name|ManagementTestSupport
block|{
annotation|@
name|Test
DECL|method|testReset ()
specifier|public
name|void
name|testReset
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
comment|// get the stats for the route
name|MBeanServer
name|mbeanServer
init|=
name|getMBeanServer
argument_list|()
decl_stmt|;
name|QueryExp
name|queryExp
init|=
name|Query
operator|.
name|match
argument_list|(
operator|new
name|AttributeValueExp
argument_list|(
literal|"RouteId"
argument_list|)
argument_list|,
operator|new
name|StringValueExp
argument_list|(
literal|"first"
argument_list|)
argument_list|)
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
literal|"*:type=routes,*"
argument_list|)
argument_list|,
name|queryExp
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
name|ObjectName
name|on
init|=
name|set
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// send in 5 messages
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"A"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"B"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"C"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"D"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"E"
argument_list|)
expr_stmt|;
comment|// and 1 for the 2nd route
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:baz"
argument_list|,
literal|"F"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// should be 5 on the route
name|Long
name|completed
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
literal|"ExchangesCompleted"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|completed
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
comment|// and on the processors as well
name|set
operator|=
name|mbeanServer
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
literal|"*:type=processors,*"
argument_list|)
argument_list|,
name|queryExp
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|set
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|ObjectName
name|name
range|:
name|set
control|)
block|{
name|completed
operator|=
operator|(
name|Long
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|name
argument_list|,
literal|"ExchangesCompleted"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|completed
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// reset which should reset all routes also
name|ObjectName
name|ctx
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=camel-1,type=context,name=\"camel-1\""
argument_list|)
decl_stmt|;
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|ctx
argument_list|,
literal|"reset"
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
expr_stmt|;
comment|// should be 0 on the route
name|completed
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
literal|"ExchangesCompleted"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|completed
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
comment|// and on the processors as well
name|set
operator|=
name|mbeanServer
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
literal|"*:type=processors,*"
argument_list|)
argument_list|,
name|queryExp
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|set
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|ObjectName
name|name
range|:
name|set
control|)
block|{
name|completed
operator|=
operator|(
name|Long
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|name
argument_list|,
literal|"ExchangesCompleted"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|completed
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// test that the 2nd route is also reset
name|queryExp
operator|=
name|Query
operator|.
name|match
argument_list|(
operator|new
name|AttributeValueExp
argument_list|(
literal|"RouteId"
argument_list|)
argument_list|,
operator|new
name|StringValueExp
argument_list|(
literal|"second"
argument_list|)
argument_list|)
expr_stmt|;
name|set
operator|=
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
name|queryExp
argument_list|)
expr_stmt|;
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
name|on
operator|=
name|set
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
name|completed
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
literal|"ExchangesCompleted"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|completed
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
comment|// and on the processors as well
name|set
operator|=
name|mbeanServer
operator|.
name|queryNames
argument_list|(
operator|new
name|ObjectName
argument_list|(
literal|"*:type=processors,*"
argument_list|)
argument_list|,
name|queryExp
argument_list|)
expr_stmt|;
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
for|for
control|(
name|ObjectName
name|name
range|:
name|set
control|)
block|{
name|completed
operator|=
operator|(
name|Long
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|name
argument_list|,
literal|"ExchangesCompleted"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|completed
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
literal|"first"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:foo"
argument_list|)
operator|.
name|id
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:bar"
argument_list|)
operator|.
name|id
argument_list|(
literal|"bar"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|id
argument_list|(
literal|"mock"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:baz"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"second"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:baz"
argument_list|)
operator|.
name|id
argument_list|(
literal|"baz"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

