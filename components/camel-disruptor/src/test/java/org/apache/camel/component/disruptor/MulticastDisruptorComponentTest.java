begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.disruptor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|disruptor
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
name|EndpointInject
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
name|Produce
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
name|ProducerTemplate
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
name|ShutdownRoute
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
comment|/**  * Tests that multicast functionality works correctly  */
end_comment

begin_class
DECL|class|MulticastDisruptorComponentTest
specifier|public
class|class
name|MulticastDisruptorComponentTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|MULTIPLE_CONSUMERS_ENDPOINT_URI
specifier|private
specifier|static
specifier|final
name|String
name|MULTIPLE_CONSUMERS_ENDPOINT_URI
init|=
literal|"disruptor:test?multipleConsumers=true"
decl_stmt|;
DECL|field|VALUE
specifier|private
specifier|static
specifier|final
name|Integer
name|VALUE
init|=
name|Integer
operator|.
name|valueOf
argument_list|(
literal|42
argument_list|)
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result1"
argument_list|)
DECL|field|resultEndpoint1
specifier|protected
name|MockEndpoint
name|resultEndpoint1
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result2"
argument_list|)
DECL|field|resultEndpoint2
specifier|protected
name|MockEndpoint
name|resultEndpoint2
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"disruptor:test"
argument_list|)
DECL|field|template
specifier|protected
name|ProducerTemplate
name|template
decl_stmt|;
comment|//    private ThreadCounter threadCounter = new ThreadCounter();
annotation|@
name|Test
DECL|method|testMulticastProduce ()
specifier|public
name|void
name|testMulticastProduce
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|resultEndpoint1
operator|.
name|expectedBodiesReceived
argument_list|(
name|VALUE
argument_list|)
expr_stmt|;
name|resultEndpoint1
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|resultEndpoint2
operator|.
name|expectedBodiesReceived
argument_list|(
name|VALUE
argument_list|)
expr_stmt|;
name|resultEndpoint2
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|asyncSendBody
argument_list|(
name|MULTIPLE_CONSUMERS_ENDPOINT_URI
argument_list|,
name|VALUE
argument_list|)
expr_stmt|;
name|resultEndpoint1
operator|.
name|await
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|resultEndpoint1
operator|.
name|assertIsSatisfied
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|resultEndpoint2
operator|.
name|await
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|resultEndpoint2
operator|.
name|assertIsSatisfied
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
comment|//
comment|//    @Test
comment|//    public void testAsynchronous() throws InterruptedException {
comment|//        threadCounter.reset();
comment|//
comment|//        int messagesSent = 1000;
comment|//
comment|//        resultEndpoint.setExpectedMessageCount(messagesSent);
comment|//
comment|//        long currentThreadId = Thread.currentThread().getId();
comment|//
comment|//        for (int i = 0; i< messagesSent; ++i) {
comment|//            template.asyncSendBody("disruptor:testAsynchronous", VALUE);
comment|//        }
comment|//
comment|//        resultEndpoint.await(20, TimeUnit.SECONDS);
comment|//        resultEndpoint.assertIsSatisfied();
comment|//
comment|//        assertTrue(threadCounter.getThreadIdCount()> 0);
comment|//        assertFalse(threadCounter.getThreadIds().contains(currentThreadId));
comment|//    }
comment|//
comment|//    @Test
comment|//    public void testMultipleConsumers() throws InterruptedException {
comment|//        threadCounter.reset();
comment|//
comment|//        int messagesSent = 1000;
comment|//
comment|//        resultEndpoint.setExpectedMessageCount(messagesSent);
comment|//
comment|//        for (int i = 0; i< messagesSent; ++i) {
comment|//            template.asyncSendBody("disruptor:testMultipleConsumers?concurrentConsumers=4", VALUE);
comment|//        }
comment|//
comment|//        resultEndpoint.await(20, TimeUnit.SECONDS);
comment|//
comment|//        //sleep for another second to check for duplicate messages in transit
comment|//        Thread.sleep(1000);
comment|//
comment|//        System.out.println("count = " + resultEndpoint.getReceivedCounter());
comment|//        resultEndpoint.assertIsSatisfied();
comment|//
comment|//        assertEquals(4, threadCounter.getThreadIdCount());
comment|//    }
comment|//
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
literal|"disruptor:test?multipleConsumers=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result1"
argument_list|)
operator|.
name|setShutdownRoute
argument_list|(
name|ShutdownRoute
operator|.
name|Defer
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"disruptor:test?multipleConsumers=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result2"
argument_list|)
operator|.
name|setShutdownRoute
argument_list|(
name|ShutdownRoute
operator|.
name|Defer
argument_list|)
expr_stmt|;
comment|//                from("disruptor:testAsynchronous").process(threadCounter).to("mock:result");
comment|//                from("disruptor:testMultipleConsumers?concurrentConsumers=4").process(threadCounter).to("mock:result");
block|}
block|}
return|;
block|}
comment|//    private static final class ThreadCounter implements Processor {
comment|//
comment|//        private Set<Long> threadIds = new HashSet<Long>();
comment|//
comment|//        public void reset() {
comment|//            threadIds.clear();
comment|//        }
comment|//
comment|//        @Override
comment|//        public void process(Exchange exchange) throws Exception {
comment|//            threadIds.add(Thread.currentThread().getId());
comment|//        }
comment|//
comment|//        public Set<Long> getThreadIds() {
comment|//            return Collections.unmodifiableSet(threadIds);
comment|//        }
comment|//
comment|//        public int getThreadIdCount() {
comment|//            return threadIds.size();
comment|//        }
comment|//    }
block|}
end_class

end_unit

