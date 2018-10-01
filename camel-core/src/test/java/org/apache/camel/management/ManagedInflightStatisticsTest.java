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
name|Objects
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CountDownLatch
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|Processor
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
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|awaitility
operator|.
name|Awaitility
operator|.
name|await
import|;
end_import

begin_class
DECL|class|ManagedInflightStatisticsTest
specifier|public
class|class
name|ManagedInflightStatisticsTest
extends|extends
name|ManagementTestSupport
block|{
annotation|@
name|Test
DECL|method|testOldestInflight ()
specifier|public
name|void
name|testOldestInflight
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
name|Long
name|inflight
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
literal|"ExchangesInflight"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|inflight
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|Long
name|ts
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
literal|"OldestInflightDuration"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|ts
argument_list|)
expr_stmt|;
name|String
name|id
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
literal|"OldestInflightExchangeId"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|CountDownLatch
name|latch1
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|CountDownLatch
name|latch2
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
comment|// start some exchanges.
name|template
operator|.
name|asyncSendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|latch1
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|250
argument_list|)
expr_stmt|;
name|template
operator|.
name|asyncSendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|latch2
argument_list|)
expr_stmt|;
name|await
argument_list|()
operator|.
name|atMost
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|.
name|until
argument_list|(
parameter_list|()
lambda|->
block|{
name|Long
name|num
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
literal|"ExchangesInflight"
argument_list|)
decl_stmt|;
return|return
name|num
operator|!=
literal|null
operator|&&
name|num
operator|==
literal|2
return|;
block|}
argument_list|)
expr_stmt|;
name|inflight
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
literal|"ExchangesInflight"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|inflight
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|ts
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
literal|"OldestInflightDuration"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ts
argument_list|)
expr_stmt|;
name|id
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
literal|"OldestInflightExchangeId"
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Oldest Exchange id: {}, duration: {}"
argument_list|,
name|id
argument_list|,
name|ts
argument_list|)
expr_stmt|;
comment|// complete first exchange
name|latch1
operator|.
name|countDown
argument_list|()
expr_stmt|;
comment|// Lets wait for the first exchange to complete.
name|Thread
operator|.
name|sleep
argument_list|(
literal|200
argument_list|)
expr_stmt|;
name|Long
name|ts2
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
literal|"OldestInflightDuration"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|ts2
argument_list|)
expr_stmt|;
name|String
name|id2
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
literal|"OldestInflightExchangeId"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|id2
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Oldest Exchange id: {}, duration: {}"
argument_list|,
name|id2
argument_list|,
name|ts2
argument_list|)
expr_stmt|;
comment|// Lets verify the oldest changed.
name|assertTrue
argument_list|(
operator|!
name|id2
operator|.
name|equals
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
comment|// The duration values could be different
name|assertTrue
argument_list|(
operator|!
name|Objects
operator|.
name|equals
argument_list|(
name|ts2
argument_list|,
name|ts
argument_list|)
argument_list|)
expr_stmt|;
name|latch2
operator|.
name|countDown
argument_list|()
expr_stmt|;
comment|// Lets wait for all the exchanges to complete.
name|await
argument_list|()
operator|.
name|atMost
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|.
name|until
argument_list|(
parameter_list|()
lambda|->
block|{
name|Long
name|num
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
literal|"ExchangesInflight"
argument_list|)
decl_stmt|;
return|return
name|num
operator|!=
literal|null
operator|&&
name|num
operator|==
literal|0
return|;
block|}
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|inflight
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
literal|"ExchangesInflight"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|inflight
operator|.
name|longValue
argument_list|()
argument_list|)
expr_stmt|;
name|ts
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
literal|"OldestInflightDuration"
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|ts
argument_list|)
expr_stmt|;
name|id
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
literal|"OldestInflightExchangeId"
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|id
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
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|CountDownLatch
name|latch
init|=
operator|(
name|CountDownLatch
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|latch
operator|.
name|await
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
block|}
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
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

