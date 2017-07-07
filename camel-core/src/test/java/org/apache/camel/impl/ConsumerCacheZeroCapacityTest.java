begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|ContextTestSupport
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
name|Endpoint
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
name|PollingConsumer
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
name|support
operator|.
name|ServiceSupport
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|ConsumerCacheZeroCapacityTest
specifier|public
class|class
name|ConsumerCacheZeroCapacityTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testConsumerCacheZeroCapacity ()
specifier|public
name|void
name|testConsumerCacheZeroCapacity
parameter_list|()
throws|throws
name|Exception
block|{
name|ConsumerCache
name|cache
init|=
operator|new
name|ConsumerCache
argument_list|(
name|this
argument_list|,
name|context
argument_list|,
literal|0
argument_list|)
decl_stmt|;
name|cache
operator|.
name|start
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Size should be 0"
argument_list|,
literal|0
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"file:target/foo?fileName=foo.txt&delay=10"
argument_list|)
decl_stmt|;
name|PollingConsumer
name|consumer
init|=
name|cache
operator|.
name|acquirePollingConsumer
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Started"
argument_list|,
operator|(
operator|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|ServiceSupport
operator|)
name|consumer
operator|)
operator|.
name|getStatus
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
comment|// let it run a poll
name|consumer
operator|.
name|receive
argument_list|(
literal|50
argument_list|)
expr_stmt|;
name|boolean
name|found
init|=
name|Thread
operator|.
name|getAllStackTraces
argument_list|()
operator|.
name|keySet
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|anyMatch
argument_list|(
name|t
lambda|->
name|t
operator|.
name|getName
argument_list|()
operator|.
name|contains
argument_list|(
literal|"target/foo"
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Should find file consumer thread"
argument_list|,
name|found
argument_list|)
expr_stmt|;
name|cache
operator|.
name|releasePollingConsumer
argument_list|(
name|endpoint
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
comment|// takes a little to stop
name|await
argument_list|()
operator|.
name|atMost
argument_list|(
literal|1
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
operator|(
operator|(
name|ServiceSupport
operator|)
name|consumer
operator|)
operator|.
name|getStatus
argument_list|()
operator|.
name|isStopped
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Stopped"
argument_list|,
operator|(
operator|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|ServiceSupport
operator|)
name|consumer
operator|)
operator|.
name|getStatus
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
comment|// should not be a file consumer thread
name|found
operator|=
name|Thread
operator|.
name|getAllStackTraces
argument_list|()
operator|.
name|keySet
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|anyMatch
argument_list|(
name|t
lambda|->
name|t
operator|.
name|getName
argument_list|()
operator|.
name|contains
argument_list|(
literal|"target/foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Should not find file consumer thread"
argument_list|,
name|found
argument_list|)
expr_stmt|;
name|cache
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

