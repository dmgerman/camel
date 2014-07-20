begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty4
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|Callable
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
name|ExecutorService
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
name|Executors
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
name|Future
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

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|NettyMultipleSimultaneousClientsTest
specifier|public
class|class
name|NettyMultipleSimultaneousClientsTest
extends|extends
name|BaseNettyTest
block|{
DECL|field|uri
specifier|private
name|String
name|uri
init|=
literal|"netty4:tcp://localhost:{{port}}?sync=true&reuseAddress=true&synchronous=false"
decl_stmt|;
DECL|field|clientCount
specifier|private
name|int
name|clientCount
init|=
literal|20
decl_stmt|;
DECL|field|startLatch
specifier|private
name|CountDownLatch
name|startLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
DECL|field|finishLatch
specifier|private
name|CountDownLatch
name|finishLatch
init|=
operator|new
name|CountDownLatch
argument_list|(
name|clientCount
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|testSimultaneousClients ()
specifier|public
name|void
name|testSimultaneousClients
parameter_list|()
throws|throws
name|Exception
block|{
name|ExecutorService
name|executorService
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
name|clientCount
argument_list|)
decl_stmt|;
name|Future
argument_list|<
name|?
argument_list|>
index|[]
name|replies
init|=
operator|new
name|Future
index|[
name|clientCount
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|clientCount
condition|;
name|i
operator|++
control|)
block|{
name|replies
index|[
name|i
index|]
operator|=
name|executorService
operator|.
name|submit
argument_list|(
operator|new
name|Callable
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Object
name|call
parameter_list|()
throws|throws
name|Exception
block|{
comment|// wait until we're allowed to start
name|startLatch
operator|.
name|await
argument_list|()
expr_stmt|;
name|Object
name|reply
init|=
name|template
operator|.
name|requestBody
argument_list|(
name|uri
argument_list|,
literal|"World"
argument_list|)
decl_stmt|;
comment|// signal that we're done now
name|finishLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
return|return
name|reply
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
name|Object
index|[]
name|expectedReplies
init|=
operator|new
name|Object
index|[
name|clientCount
index|]
decl_stmt|;
name|Arrays
operator|.
name|fill
argument_list|(
name|expectedReplies
argument_list|,
literal|"Bye World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
name|clientCount
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedBodiesReceived
argument_list|(
name|expectedReplies
argument_list|)
expr_stmt|;
comment|// fire the simultaneous client calls
name|startLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
comment|// and wait long enough until they're all done
name|assertTrue
argument_list|(
literal|"Waiting on the latch ended up with a timeout!"
argument_list|,
name|finishLatch
operator|.
name|await
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|executorService
operator|.
name|shutdown
argument_list|()
expr_stmt|;
comment|// assert on what we expect to receive
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|clientCount
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|replies
index|[
name|i
index|]
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
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
name|uri
argument_list|)
operator|.
name|log
argument_list|(
literal|"${body}"
argument_list|)
operator|.
name|transform
argument_list|(
name|body
argument_list|()
operator|.
name|prepend
argument_list|(
literal|"Bye "
argument_list|)
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

