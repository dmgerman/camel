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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|ManagedInterceptTest
specifier|public
class|class
name|ManagedInterceptTest
extends|extends
name|ManagementTestSupport
block|{
annotation|@
name|Test
DECL|method|testIntercept ()
specifier|public
name|void
name|testIntercept
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
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:intercept"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|,
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
name|MBeanServer
name|mbeanServer
init|=
name|getMBeanServer
argument_list|()
decl_stmt|;
name|ObjectName
name|name
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=camel-1,type=endpoints,name=\"mock://result\""
argument_list|)
decl_stmt|;
name|Long
name|queueSize
init|=
operator|(
name|Long
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|name
argument_list|,
literal|"queueSize"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|queueSize
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|name
operator|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=camel-1,type=endpoints,name=\"mock://intercept\""
argument_list|)
expr_stmt|;
name|queueSize
operator|=
operator|(
name|Long
operator|)
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|name
argument_list|,
literal|"queueSize"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|queueSize
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|name
operator|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=camel-1,type=processors,name=\"log-foo\""
argument_list|)
expr_stmt|;
name|mbeanServer
operator|.
name|isRegistered
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|Long
name|total
init|=
operator|(
name|Long
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|name
argument_list|,
literal|"ExchangesTotal"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|total
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
name|intercept
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:intercept"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:foo"
argument_list|)
operator|.
name|id
argument_list|(
literal|"log-foo"
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

