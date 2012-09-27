begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms.tx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sjms
operator|.
name|tx
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
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|ActiveMQConnectionFactory
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
name|sjms
operator|.
name|SjmsComponent
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
comment|/**  * Test to verify concurrent consumers on a transacted endpoint.  */
end_comment

begin_class
DECL|class|TransactedConcurrentConsumersTest
specifier|public
class|class
name|TransactedConcurrentConsumersTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|CONSUMER_COUNT
specifier|private
specifier|static
specifier|final
name|int
name|CONSUMER_COUNT
init|=
literal|2
decl_stmt|;
DECL|field|MAX_ATTEMPTS_COUNT
specifier|private
specifier|static
specifier|final
name|int
name|MAX_ATTEMPTS_COUNT
init|=
literal|10
decl_stmt|;
DECL|field|MESSAGE_COUNT
specifier|private
specifier|static
specifier|final
name|int
name|MESSAGE_COUNT
init|=
literal|20
decl_stmt|;
DECL|field|TOTAL_REDELIVERED_FALSE
specifier|private
specifier|static
specifier|final
name|int
name|TOTAL_REDELIVERED_FALSE
init|=
literal|18
decl_stmt|;
DECL|field|TOTAL_REDELIVERED_TRUE
specifier|private
specifier|static
specifier|final
name|int
name|TOTAL_REDELIVERED_TRUE
init|=
literal|2
decl_stmt|;
DECL|field|BROKER_URI
specifier|private
specifier|static
specifier|final
name|String
name|BROKER_URI
init|=
literal|"vm://tccTestBroker?broker.persistent=false&broker.useJmx=true"
decl_stmt|;
comment|/**      * Verify that transacted and concurrent consumers work correctly together.      *       * @throws Exception      */
annotation|@
name|Test
DECL|method|testEndpointConfiguredBatchTransaction ()
specifier|public
name|void
name|testEndpointConfiguredBatchTransaction
parameter_list|()
throws|throws
name|Exception
block|{
comment|// We are set up for a failure to occur every 10 messages. We should see
comment|// 2 message failures over the course of 20 messages.
name|getMockEndpoint
argument_list|(
literal|"mock:test.redelivered.false"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
name|TOTAL_REDELIVERED_FALSE
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:test.redelivered.true"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
name|TOTAL_REDELIVERED_TRUE
argument_list|)
expr_stmt|;
comment|// We should never see a message appear in this endpoint or we
comment|// have problem with our JMS provider
name|getMockEndpoint
argument_list|(
literal|"mock:test.after"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|// Send messages
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
name|MESSAGE_COUNT
condition|;
name|i
operator|++
control|)
block|{
name|String
name|message
init|=
literal|"Hello World "
operator|+
name|i
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Sending message: {}"
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
name|Thread
operator|.
name|sleep
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
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
name|camelContext
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|ActiveMQConnectionFactory
name|connectionFactory
init|=
operator|new
name|ActiveMQConnectionFactory
argument_list|(
name|BROKER_URI
argument_list|)
decl_stmt|;
name|SjmsComponent
name|component
init|=
operator|new
name|SjmsComponent
argument_list|()
decl_stmt|;
name|component
operator|.
name|setConnectionFactory
argument_list|(
name|connectionFactory
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"sjms"
argument_list|,
name|component
argument_list|)
expr_stmt|;
return|return
name|camelContext
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
block|{
comment|// Having a producer route helps with debugging and logging
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"sjms:queue:transacted.consumer.test"
argument_list|)
expr_stmt|;
comment|// Our test consumer route
name|from
argument_list|(
literal|"sjms:queue:transacted.consumer.test?transacted=true&consumerCount="
operator|+
name|CONSUMER_COUNT
argument_list|)
comment|// first consume all the messages that are not redelivered
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|header
argument_list|(
literal|"JMSRedelivered"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"false"
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|private
specifier|final
name|AtomicInteger
name|counter
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
decl_stmt|;
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
name|int
name|count
init|=
name|counter
operator|.
name|incrementAndGet
argument_list|()
decl_stmt|;
if|if
condition|(
name|count
operator|==
name|MAX_ATTEMPTS_COUNT
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"{} Messages have been processed. Failing the exchange to force a rollback of the transaction."
argument_list|,
name|MAX_ATTEMPTS_COUNT
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setFault
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|counter
operator|.
name|set
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
operator|.
name|log
argument_list|(
literal|"1st attempt Body: ${body} | Redeliverd: ${header.JMSRedelivered}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:test.redelivered.false"
argument_list|)
comment|// Now process again any messages that were redelivered
operator|.
name|when
argument_list|(
name|header
argument_list|(
literal|"JMSRedelivered"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"true"
argument_list|)
argument_list|)
operator|.
name|log
argument_list|(
literal|"2nd attempt Body: ${body} | Redeliverd: ${header.JMSRedelivered}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:test.redelivered.true"
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:test.after"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
comment|//    private class RedeliveredProcessor implements Processor {
comment|//        private CountDownLatch latch;
comment|//
comment|//        public RedeliveredProcessor(CountDownLatch latch) {
comment|//            super();
comment|//            this.latch = latch;
comment|//        }
comment|//
comment|//        @Override
comment|//        public void process(Exchange exchange) throws Exception {
comment|//            // TODO Auto-generated method stub
comment|//
comment|//        }
comment|//
comment|//    }
comment|//
comment|//    private class NotRedeliveredProcessor implements Processor {
comment|//        private final AtomicInteger counter = new AtomicInteger(0);
comment|//        private final AtomicInteger total = new AtomicInteger(0);
comment|//        private CountDownLatch latch;
comment|//
comment|//        public NotRedeliveredProcessor(CountDownLatch latch) {
comment|//            super();
comment|//            this.latch = latch;
comment|//        }
comment|//
comment|//        @Override
comment|//        public void process(Exchange exchange) throws Exception {
comment|//            int count = counter.incrementAndGet();
comment|//            if (count == MAX_ATTEMPTS_COUNT) {
comment|//                log.info("{} Messages have been processed. Failing the exchange to force a rollback of the transaction.", MAX_ATTEMPTS_COUNT);
comment|//                exchange.getOut().setFault(true);
comment|//                counter.set(0);
comment|//            }
comment|//            if(total.incrementAndGet() == TOTAL_REDELIVERED_FALSE) {
comment|//                cdl.countDown();
comment|//            }
comment|//        }
comment|//
comment|//    }
block|}
end_class

end_unit

