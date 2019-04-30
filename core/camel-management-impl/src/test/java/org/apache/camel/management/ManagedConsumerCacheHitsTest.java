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
name|Exchange
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
name|impl
operator|.
name|engine
operator|.
name|DefaultConsumerCache
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
DECL|class|ManagedConsumerCacheHitsTest
specifier|public
class|class
name|ManagedConsumerCacheHitsTest
extends|extends
name|ManagementTestSupport
block|{
annotation|@
name|Test
DECL|method|testManageConsumerCache ()
specifier|public
name|void
name|testManageConsumerCache
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
comment|// always register services in JMX so we can enlist our consumer template/cache
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementAgent
argument_list|()
operator|.
name|setRegisterAlways
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|DefaultConsumerCache
name|cache
init|=
operator|new
name|DefaultConsumerCache
argument_list|(
name|this
argument_list|,
name|context
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|context
operator|.
name|addService
argument_list|(
name|cache
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:a"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|Exchange
name|out
init|=
name|cache
operator|.
name|acquirePollingConsumer
argument_list|(
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"seda:a"
argument_list|)
argument_list|)
operator|.
name|receive
argument_list|(
literal|3000
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Should got an exchange"
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hello World"
argument_list|,
name|out
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
comment|// get the stats for the route
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
literal|"ConsumerCache"
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
literal|"Should have found ConsumerCache"
argument_list|,
name|on
argument_list|)
expr_stmt|;
name|Integer
name|max
init|=
operator|(
name|Integer
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"MaximumCacheSize"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1000
argument_list|,
name|max
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|Integer
name|current
init|=
operator|(
name|Integer
operator|)
name|mbeanServer
operator|.
name|getAttribute
argument_list|(
name|on
argument_list|,
literal|"Size"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|current
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
comment|// since we only send 1 message we should have 0 hits and 1 miss
name|Long
name|hits
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
literal|"Hits"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|hits
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|Long
name|misses
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
literal|"Misses"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|misses
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
comment|// send more
comment|// ---------
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:b"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:c"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|out
operator|=
name|cache
operator|.
name|acquirePollingConsumer
argument_list|(
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"seda:b"
argument_list|)
argument_list|)
operator|.
name|receive
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|out
operator|=
name|cache
operator|.
name|acquirePollingConsumer
argument_list|(
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"seda:c"
argument_list|)
argument_list|)
operator|.
name|receive
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
comment|// we have only consumed from 3 different endpoints so all is misses
name|hits
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
literal|"Hits"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|hits
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|misses
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
literal|"Misses"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|misses
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
comment|// send to same endpoints
comment|// ----------------------
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:a"
argument_list|,
literal|"Bye World"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:b"
argument_list|,
literal|"Bye World"
argument_list|)
expr_stmt|;
name|out
operator|=
name|cache
operator|.
name|acquirePollingConsumer
argument_list|(
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"seda:a"
argument_list|)
argument_list|)
operator|.
name|receive
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|out
operator|=
name|cache
operator|.
name|acquirePollingConsumer
argument_list|(
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"seda:b"
argument_list|)
argument_list|)
operator|.
name|receive
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
comment|// we should have hits now
name|hits
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
literal|"Hits"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|hits
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|misses
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
literal|"Misses"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|misses
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
comment|// reset statistics
comment|// ----------------
name|mbeanServer
operator|.
name|invoke
argument_list|(
name|on
argument_list|,
literal|"resetStatistics"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|hits
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
literal|"Hits"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|hits
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|misses
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
literal|"Misses"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|misses
operator|.
name|longValue
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
comment|// note: this route is not used in the test
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

