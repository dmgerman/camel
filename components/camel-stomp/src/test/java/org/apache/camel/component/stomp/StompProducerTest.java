begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.stomp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|stomp
package|;
end_package

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
name|Producer
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
name|fusesource
operator|.
name|stomp
operator|.
name|client
operator|.
name|BlockingConnection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|stomp
operator|.
name|client
operator|.
name|Stomp
import|;
end_import

begin_import
import|import
name|org
operator|.
name|fusesource
operator|.
name|stomp
operator|.
name|codec
operator|.
name|StompFrame
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
name|fusesource
operator|.
name|stomp
operator|.
name|client
operator|.
name|Constants
operator|.
name|DESTINATION
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|fusesource
operator|.
name|stomp
operator|.
name|client
operator|.
name|Constants
operator|.
name|ID
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|fusesource
operator|.
name|stomp
operator|.
name|client
operator|.
name|Constants
operator|.
name|SUBSCRIBE
import|;
end_import

begin_class
DECL|class|StompProducerTest
specifier|public
class|class
name|StompProducerTest
extends|extends
name|StompBaseTest
block|{
annotation|@
name|Test
DECL|method|testProduce ()
specifier|public
name|void
name|testProduce
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canTest
argument_list|()
condition|)
block|{
return|return;
block|}
name|context
operator|.
name|addRoutes
argument_list|(
name|createRouteBuilder
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|Stomp
name|stomp
init|=
operator|new
name|Stomp
argument_list|(
literal|"tcp://localhost:"
operator|+
name|getPort
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|BlockingConnection
name|subscribeConnection
init|=
name|stomp
operator|.
name|connectBlocking
argument_list|()
decl_stmt|;
name|StompFrame
name|frame
init|=
operator|new
name|StompFrame
argument_list|(
name|SUBSCRIBE
argument_list|)
decl_stmt|;
name|frame
operator|.
name|addHeader
argument_list|(
name|DESTINATION
argument_list|,
name|StompFrame
operator|.
name|encodeHeader
argument_list|(
literal|"/queue/test"
argument_list|)
argument_list|)
expr_stmt|;
name|frame
operator|.
name|addHeader
argument_list|(
name|ID
argument_list|,
name|subscribeConnection
operator|.
name|nextId
argument_list|()
argument_list|)
expr_stmt|;
name|StompFrame
name|response
init|=
name|subscribeConnection
operator|.
name|request
argument_list|(
name|frame
argument_list|)
decl_stmt|;
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
name|numberOfMessages
argument_list|)
decl_stmt|;
name|Thread
name|thread
init|=
operator|new
name|Thread
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|numberOfMessages
condition|;
name|i
operator|++
control|)
block|{
try|try
block|{
name|StompFrame
name|frame
init|=
name|subscribeConnection
operator|.
name|receive
argument_list|()
decl_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
block|}
block|}
argument_list|)
decl_stmt|;
name|thread
operator|.
name|start
argument_list|()
expr_stmt|;
name|Producer
name|producer
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|createProducer
argument_list|()
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
name|numberOfMessages
condition|;
name|i
operator|++
control|)
block|{
name|Exchange
name|exchange
init|=
name|producer
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"test message "
operator|+
name|i
argument_list|)
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
name|latch
operator|.
name|await
argument_list|(
literal|20
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Messages not consumed = "
operator|+
name|latch
operator|.
name|getCount
argument_list|()
argument_list|,
name|latch
operator|.
name|getCount
argument_list|()
operator|==
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"stomp:queue:test"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

