begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms.producer
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
name|producer
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
name|javax
operator|.
name|jms
operator|.
name|JMSException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|MessageConsumer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|MessageListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|MessageProducer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|TextMessage
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
name|support
operator|.
name|JmsTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
DECL|class|InOutQueueProducerSyncLoadTest
specifier|public
class|class
name|InOutQueueProducerSyncLoadTest
extends|extends
name|JmsTestSupport
block|{
DECL|field|TEST_DESTINATION_NAME
specifier|private
specifier|static
specifier|final
name|String
name|TEST_DESTINATION_NAME
init|=
literal|"in.out.queue.producer.test"
decl_stmt|;
DECL|field|mc1
specifier|private
name|MessageConsumer
name|mc1
decl_stmt|;
DECL|field|mc2
specifier|private
name|MessageConsumer
name|mc2
decl_stmt|;
DECL|method|InOutQueueProducerSyncLoadTest ()
specifier|public
name|InOutQueueProducerSyncLoadTest
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|useJmx ()
specifier|protected
name|boolean
name|useJmx
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|mc1
operator|=
name|createQueueConsumer
argument_list|(
name|TEST_DESTINATION_NAME
operator|+
literal|".request"
argument_list|)
expr_stmt|;
name|mc2
operator|=
name|createQueueConsumer
argument_list|(
name|TEST_DESTINATION_NAME
operator|+
literal|".request"
argument_list|)
expr_stmt|;
name|mc1
operator|.
name|setMessageListener
argument_list|(
operator|new
name|MyMessageListener
argument_list|()
argument_list|)
expr_stmt|;
name|mc2
operator|.
name|setMessageListener
argument_list|(
operator|new
name|MyMessageListener
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|MyMessageListener
name|l1
init|=
operator|(
name|MyMessageListener
operator|)
name|mc1
operator|.
name|getMessageListener
argument_list|()
decl_stmt|;
name|l1
operator|.
name|close
argument_list|()
expr_stmt|;
name|mc1
operator|.
name|close
argument_list|()
expr_stmt|;
name|MyMessageListener
name|l2
init|=
operator|(
name|MyMessageListener
operator|)
name|mc2
operator|.
name|getMessageListener
argument_list|()
decl_stmt|;
name|l2
operator|.
name|close
argument_list|()
expr_stmt|;
name|mc2
operator|.
name|close
argument_list|()
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
comment|/**      * Test to verify that when using the consumer listener for the InOut      * producer we get the correct message back.      *       * @throws Exception      */
annotation|@
name|Test
DECL|method|testInOutQueueProducer ()
specifier|public
name|void
name|testInOutQueueProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|ExecutorService
name|executor
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
literal|2
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
literal|5000
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|int
name|tempI
init|=
name|i
decl_stmt|;
name|Runnable
name|worker
init|=
operator|new
name|Runnable
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
specifier|final
name|String
name|requestText
init|=
literal|"Message "
operator|+
name|tempI
decl_stmt|;
specifier|final
name|String
name|responseText
init|=
literal|"Response Message "
operator|+
name|tempI
decl_stmt|;
name|String
name|response
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
name|requestText
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|responseText
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"TODO Auto-generated catch block"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
decl_stmt|;
name|executor
operator|.
name|execute
argument_list|(
name|worker
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
name|context
operator|.
name|getInflightRepository
argument_list|()
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{          }
name|executor
operator|.
name|shutdown
argument_list|()
expr_stmt|;
while|while
condition|(
operator|!
name|executor
operator|.
name|isTerminated
argument_list|()
condition|)
block|{
comment|//
block|}
block|}
comment|/*      * @see org.apache.camel.test.junit4.CamelTestSupport#createRouteBuilder()      *       * @return      *       * @throws Exception      */
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
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:"
operator|+
name|TEST_DESTINATION_NAME
operator|+
literal|".in.log?showBody=true"
argument_list|)
operator|.
name|inOut
argument_list|(
literal|"sjms:queue:"
operator|+
name|TEST_DESTINATION_NAME
operator|+
literal|".request"
operator|+
literal|"?namedReplyTo="
operator|+
name|TEST_DESTINATION_NAME
operator|+
literal|".response&consumerCount=20&producerCount=40&synchronous=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:"
operator|+
name|TEST_DESTINATION_NAME
operator|+
literal|".out.log?showBody=true"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyMessageListener
specifier|protected
class|class
name|MyMessageListener
implements|implements
name|MessageListener
block|{
DECL|field|mp
specifier|private
name|MessageProducer
name|mp
decl_stmt|;
annotation|@
name|Override
DECL|method|onMessage (Message message)
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
try|try
block|{
name|TextMessage
name|request
init|=
operator|(
name|TextMessage
operator|)
name|message
decl_stmt|;
name|String
name|text
init|=
name|request
operator|.
name|getText
argument_list|()
decl_stmt|;
name|TextMessage
name|response
init|=
name|getSession
argument_list|()
operator|.
name|createTextMessage
argument_list|()
decl_stmt|;
name|response
operator|.
name|setText
argument_list|(
literal|"Response "
operator|+
name|text
argument_list|)
expr_stmt|;
name|response
operator|.
name|setJMSCorrelationID
argument_list|(
name|request
operator|.
name|getJMSCorrelationID
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|mp
operator|==
literal|null
condition|)
block|{
name|mp
operator|=
name|getSession
argument_list|()
operator|.
name|createProducer
argument_list|(
name|message
operator|.
name|getJMSReplyTo
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|mp
operator|.
name|send
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
name|e
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|JMSException
block|{
name|mp
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

