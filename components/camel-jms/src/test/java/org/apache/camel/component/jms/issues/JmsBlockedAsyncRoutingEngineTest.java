begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms.issues
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
operator|.
name|issues
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
name|javax
operator|.
name|jms
operator|.
name|ConnectionFactory
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
name|activemq
operator|.
name|broker
operator|.
name|BrokerPlugin
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
name|broker
operator|.
name|BrokerPluginSupport
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
name|broker
operator|.
name|BrokerService
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
name|broker
operator|.
name|ProducerBrokerExchange
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
name|command
operator|.
name|Message
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
name|spi
operator|.
name|Synchronization
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
name|After
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
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jms
operator|.
name|JmsComponent
operator|.
name|jmsComponentAutoAcknowledge
import|;
end_import

begin_comment
comment|/**  * Tests CAMEL-5769.  * Camel JMS producer can block a thread under specific circumstances.  *  */
end_comment

begin_class
DECL|class|JmsBlockedAsyncRoutingEngineTest
specifier|public
class|class
name|JmsBlockedAsyncRoutingEngineTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|JmsBlockedAsyncRoutingEngineTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|broker
specifier|private
name|BrokerService
name|broker
decl_stmt|;
DECL|field|latch
specifier|private
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|5
argument_list|)
decl_stmt|;
DECL|field|callback
specifier|private
specifier|final
name|Synchronization
name|callback
init|=
operator|new
name|Synchronization
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onFailure
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|">>>> Callback onFailure"
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|onComplete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|">>>> Callback onComplete"
argument_list|)
expr_stmt|;
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
decl_stmt|;
DECL|method|startBroker ()
specifier|public
name|void
name|startBroker
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|brokerName
init|=
literal|"test-broker-"
operator|+
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
name|String
name|brokerUri
init|=
literal|"vm://"
operator|+
name|brokerName
decl_stmt|;
name|broker
operator|=
operator|new
name|BrokerService
argument_list|()
expr_stmt|;
name|broker
operator|.
name|setBrokerName
argument_list|(
name|brokerName
argument_list|)
expr_stmt|;
name|broker
operator|.
name|setBrokerId
argument_list|(
name|brokerName
argument_list|)
expr_stmt|;
name|broker
operator|.
name|addConnector
argument_list|(
name|brokerUri
argument_list|)
expr_stmt|;
name|broker
operator|.
name|setPersistent
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// This Broker Plugin simulates Producer Flow Control by delaying the broker's ACK by 2 seconds
name|broker
operator|.
name|setPlugins
argument_list|(
operator|new
name|BrokerPlugin
index|[]
block|{
operator|new
name|DelayerBrokerPlugin
argument_list|()
block|}
argument_list|)
expr_stmt|;
name|broker
operator|.
name|start
argument_list|()
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
name|startBroker
argument_list|()
expr_stmt|;
name|ConnectionFactory
name|connectionFactory
init|=
operator|new
name|ActiveMQConnectionFactory
argument_list|(
name|broker
operator|.
name|getVmConnectorURI
argument_list|()
argument_list|)
decl_stmt|;
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"activemq"
argument_list|,
name|jmsComponentAutoAcknowledge
argument_list|(
name|connectionFactory
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|camelContext
return|;
block|}
annotation|@
name|Test
DECL|method|testBlockedAsyncRoutingEngineTest ()
specifier|public
name|void
name|testBlockedAsyncRoutingEngineTest
parameter_list|()
throws|throws
name|Exception
block|{
comment|// 0. This message takes 2000ms to ACK from the broker due to the DelayerBrokerPlugin
comment|// Until then, the correlation ID doesn't get updated locally
try|try
block|{
name|template
operator|.
name|asyncRequestBody
argument_list|(
literal|"activemq:queue:test?requestTimeout=500&useMessageIDAsCorrelationID=true"
argument_list|,
literal|"hello"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{ }
comment|// 1. We wait a bit for the CorrelationTimeoutMap purge process to run
name|Thread
operator|.
name|sleep
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
comment|// 2. We send 5 messages that take 2 seconds so that they time out
name|template
operator|.
name|asyncCallbackRequestBody
argument_list|(
literal|"activemq:queue:test?requestTimeout=500&useMessageIDAsCorrelationID=true"
argument_list|,
literal|"beSlow"
argument_list|,
name|callback
argument_list|)
expr_stmt|;
name|template
operator|.
name|asyncCallbackRequestBody
argument_list|(
literal|"activemq:queue:test?requestTimeout=500&useMessageIDAsCorrelationID=true"
argument_list|,
literal|"beSlow"
argument_list|,
name|callback
argument_list|)
expr_stmt|;
name|template
operator|.
name|asyncCallbackRequestBody
argument_list|(
literal|"activemq:queue:test?requestTimeout=500&useMessageIDAsCorrelationID=true"
argument_list|,
literal|"beSlow"
argument_list|,
name|callback
argument_list|)
expr_stmt|;
name|template
operator|.
name|asyncCallbackRequestBody
argument_list|(
literal|"activemq:queue:test?requestTimeout=500&useMessageIDAsCorrelationID=true"
argument_list|,
literal|"beSlow"
argument_list|,
name|callback
argument_list|)
expr_stmt|;
name|template
operator|.
name|asyncCallbackRequestBody
argument_list|(
literal|"activemq:queue:test?requestTimeout=500&useMessageIDAsCorrelationID=true"
argument_list|,
literal|"beSlow"
argument_list|,
name|callback
argument_list|)
expr_stmt|;
comment|// 3. We assert that we were notified of all timeout exceptions
name|assertTrue
argument_list|(
name|latch
operator|.
name|await
argument_list|(
literal|3000
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|cleanup ()
specifier|public
name|void
name|cleanup
parameter_list|()
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|">>>>> Latch countdown count was: "
operator|+
name|latch
operator|.
name|getCount
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"activemq:queue:test?concurrentConsumers=5&useMessageIDAsCorrelationID=true&transacted=true"
argument_list|)
operator|.
name|filter
argument_list|()
operator|.
name|simple
argument_list|(
literal|"${in.body} == 'beSlow'"
argument_list|)
operator|.
name|delay
argument_list|(
name|constant
argument_list|(
literal|2000
argument_list|)
argument_list|)
operator|.
name|log
argument_list|(
literal|">>>>> Received message on test queue"
argument_list|)
operator|.
name|setBody
argument_list|(
name|constant
argument_list|(
literal|"Reply"
argument_list|)
argument_list|)
operator|.
name|log
argument_list|(
literal|">>>>> Sending back reply"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|DelayerBrokerPlugin
specifier|private
class|class
name|DelayerBrokerPlugin
extends|extends
name|BrokerPluginSupport
block|{
DECL|field|i
name|int
name|i
decl_stmt|;
annotation|@
name|Override
DECL|method|send (ProducerBrokerExchange producerExchange, Message messageSend)
specifier|public
name|void
name|send
parameter_list|(
name|ProducerBrokerExchange
name|producerExchange
parameter_list|,
name|Message
name|messageSend
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|destinationName
init|=
name|messageSend
operator|.
name|getDestination
argument_list|()
operator|.
name|getPhysicalName
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"******** Received message for destination "
operator|+
name|destinationName
argument_list|)
expr_stmt|;
comment|// do not intercept sends to DLQ
if|if
condition|(
name|destinationName
operator|.
name|toLowerCase
argument_list|()
operator|.
name|contains
argument_list|(
literal|"test"
argument_list|)
operator|&&
name|i
operator|==
literal|0
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"******** Waited 2 seconds for destination: "
operator|+
name|destinationName
argument_list|)
expr_stmt|;
name|i
operator|++
expr_stmt|;
block|}
name|super
operator|.
name|send
argument_list|(
name|producerExchange
argument_list|,
name|messageSend
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

