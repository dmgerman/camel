begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.nsq
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|nsq
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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|brainlag
operator|.
name|nsq
operator|.
name|NSQConsumer
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|brainlag
operator|.
name|nsq
operator|.
name|lookup
operator|.
name|DefaultNSQLookup
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|brainlag
operator|.
name|nsq
operator|.
name|lookup
operator|.
name|NSQLookup
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
DECL|class|NsqProducerTest
specifier|public
class|class
name|NsqProducerTest
extends|extends
name|NsqTestSupport
block|{
DECL|field|NUMBER_OF_MESSAGES
specifier|private
specifier|static
specifier|final
name|int
name|NUMBER_OF_MESSAGES
init|=
literal|10000
decl_stmt|;
DECL|field|TEST_MESSAGE
specifier|private
specifier|static
specifier|final
name|String
name|TEST_MESSAGE
init|=
literal|"Hello NSQProducer!"
decl_stmt|;
annotation|@
name|Test
DECL|method|testProducer ()
specifier|public
name|void
name|testProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|CountDownLatch
name|lock
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:send"
argument_list|,
name|TEST_MESSAGE
argument_list|)
expr_stmt|;
name|AtomicInteger
name|counter
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|NSQLookup
name|lookup
init|=
operator|new
name|DefaultNSQLookup
argument_list|()
decl_stmt|;
name|lookup
operator|.
name|addLookupAddress
argument_list|(
literal|"localhost"
argument_list|,
literal|4161
argument_list|)
expr_stmt|;
name|NSQConsumer
name|consumer
init|=
operator|new
name|NSQConsumer
argument_list|(
name|lookup
argument_list|,
literal|"test"
argument_list|,
literal|"testconsumer"
argument_list|,
name|message
lambda|->
block|{
name|counter
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
name|message
operator|.
name|finished
argument_list|()
expr_stmt|;
name|lock
operator|.
name|countDown
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|String
argument_list|(
name|message
operator|.
name|getMessage
argument_list|()
argument_list|)
operator|.
name|equals
argument_list|(
name|TEST_MESSAGE
argument_list|)
argument_list|)
expr_stmt|;
block|}
argument_list|)
decl_stmt|;
name|consumer
operator|.
name|start
argument_list|()
expr_stmt|;
name|lock
operator|.
name|await
argument_list|(
literal|30
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|counter
operator|.
name|get
argument_list|()
operator|==
name|Long
operator|.
name|valueOf
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testLoadProducer ()
specifier|public
name|void
name|testLoadProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|CountDownLatch
name|lock
init|=
operator|new
name|CountDownLatch
argument_list|(
name|NUMBER_OF_MESSAGES
argument_list|)
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
name|NUMBER_OF_MESSAGES
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:send"
argument_list|,
literal|"test"
operator|+
name|i
argument_list|)
expr_stmt|;
block|}
name|AtomicInteger
name|counter
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|NSQLookup
name|lookup
init|=
operator|new
name|DefaultNSQLookup
argument_list|()
decl_stmt|;
name|lookup
operator|.
name|addLookupAddress
argument_list|(
literal|"localhost"
argument_list|,
literal|4161
argument_list|)
expr_stmt|;
name|NSQConsumer
name|consumer
init|=
operator|new
name|NSQConsumer
argument_list|(
name|lookup
argument_list|,
literal|"test"
argument_list|,
literal|"testconsumer"
argument_list|,
name|message
lambda|->
block|{
name|counter
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
name|message
operator|.
name|finished
argument_list|()
expr_stmt|;
name|lock
operator|.
name|countDown
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|message
operator|.
name|getAttempts
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
block|}
argument_list|)
decl_stmt|;
name|consumer
operator|.
name|start
argument_list|()
expr_stmt|;
name|lock
operator|.
name|await
argument_list|(
literal|30
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|counter
operator|.
name|get
argument_list|()
operator|==
name|Long
operator|.
name|valueOf
argument_list|(
name|NUMBER_OF_MESSAGES
argument_list|)
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|shutdown
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
name|NsqComponent
name|nsq
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"nsq"
argument_list|,
name|NsqComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|nsq
operator|.
name|setServers
argument_list|(
name|getNsqProducerUrl
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:send"
argument_list|)
operator|.
name|to
argument_list|(
literal|"nsq://test"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

