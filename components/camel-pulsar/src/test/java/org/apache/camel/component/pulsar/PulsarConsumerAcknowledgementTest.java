begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.pulsar
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|pulsar
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
name|CompletableFuture
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
name|component
operator|.
name|pulsar
operator|.
name|utils
operator|.
name|AutoConfiguration
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
name|pulsar
operator|.
name|utils
operator|.
name|message
operator|.
name|PulsarMessageHeaders
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
name|JndiRegistry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pulsar
operator|.
name|client
operator|.
name|api
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
name|pulsar
operator|.
name|client
operator|.
name|api
operator|.
name|PulsarClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pulsar
operator|.
name|client
operator|.
name|api
operator|.
name|PulsarClientException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pulsar
operator|.
name|client
operator|.
name|api
operator|.
name|Schema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pulsar
operator|.
name|client
operator|.
name|impl
operator|.
name|ClientBuilderImpl
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

begin_class
DECL|class|PulsarConsumerAcknowledgementTest
specifier|public
class|class
name|PulsarConsumerAcknowledgementTest
extends|extends
name|PulsarTestSupport
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|PulsarConsumerAcknowledgementTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|TOPIC_URI
specifier|private
specifier|static
specifier|final
name|String
name|TOPIC_URI
init|=
literal|"persistent://public/default/camel-topic"
decl_stmt|;
DECL|field|PRODUCER
specifier|private
specifier|static
specifier|final
name|String
name|PRODUCER
init|=
literal|"camel-producer-1"
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"pulsar:"
operator|+
name|TOPIC_URI
operator|+
literal|"?numberOfConsumers=1&subscriptionType=Exclusive"
operator|+
literal|"&subscriptionName=camel-subscription&consumerQueueSize=1&consumerName=camel-consumer"
operator|+
literal|"&allowManualAcknowledgement=true"
operator|+
literal|"&ackTimeoutMillis=1000"
argument_list|)
DECL|field|from
specifier|private
name|Endpoint
name|from
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result"
argument_list|)
DECL|field|to
specifier|private
name|MockEndpoint
name|to
decl_stmt|;
DECL|field|producer
specifier|private
name|Producer
argument_list|<
name|String
argument_list|>
name|producer
decl_stmt|;
annotation|@
name|Before
DECL|method|setup ()
specifier|public
name|void
name|setup
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|removeRoute
argument_list|(
literal|"myRoute"
argument_list|)
expr_stmt|;
name|producer
operator|=
name|givenPulsarClient
argument_list|()
operator|.
name|newProducer
argument_list|(
name|Schema
operator|.
name|STRING
argument_list|)
operator|.
name|producerName
argument_list|(
name|PRODUCER
argument_list|)
operator|.
name|topic
argument_list|(
name|TOPIC_URI
argument_list|)
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|registerPulsarBeans
argument_list|(
name|jndi
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
DECL|method|registerPulsarBeans (final JndiRegistry jndi)
specifier|private
name|void
name|registerPulsarBeans
parameter_list|(
specifier|final
name|JndiRegistry
name|jndi
parameter_list|)
throws|throws
name|PulsarClientException
block|{
name|PulsarClient
name|pulsarClient
init|=
name|givenPulsarClient
argument_list|()
decl_stmt|;
name|AutoConfiguration
name|autoConfiguration
init|=
operator|new
name|AutoConfiguration
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"pulsarClient"
argument_list|,
name|pulsarClient
argument_list|)
expr_stmt|;
name|PulsarComponent
name|comp
init|=
operator|new
name|PulsarComponent
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|comp
operator|.
name|setAutoConfiguration
argument_list|(
name|autoConfiguration
argument_list|)
expr_stmt|;
name|comp
operator|.
name|setPulsarClient
argument_list|(
name|pulsarClient
argument_list|)
expr_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"pulsar"
argument_list|,
name|comp
argument_list|)
expr_stmt|;
block|}
DECL|method|givenPulsarClient ()
specifier|private
name|PulsarClient
name|givenPulsarClient
parameter_list|()
throws|throws
name|PulsarClientException
block|{
return|return
operator|new
name|ClientBuilderImpl
argument_list|()
operator|.
name|serviceUrl
argument_list|(
name|getPulsarBrokerUrl
argument_list|()
argument_list|)
operator|.
name|ioThreads
argument_list|(
literal|1
argument_list|)
operator|.
name|listenerThreads
argument_list|(
literal|1
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|Test
DECL|method|testAcknowledge ()
specifier|public
name|void
name|testAcknowledge
parameter_list|()
throws|throws
name|Exception
block|{
name|to
operator|.
name|expectsNoDuplicates
argument_list|(
name|body
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
name|from
argument_list|(
name|from
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"myRoute"
argument_list|)
operator|.
name|to
argument_list|(
name|to
argument_list|)
operator|.
name|process
argument_list|(
name|exchange
lambda|->
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Processing message {}"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|PulsarMessageReceipt
name|receipt
init|=
operator|(
name|PulsarMessageReceipt
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|PulsarMessageHeaders
operator|.
name|MESSAGE_RECEIPT
argument_list|)
decl_stmt|;
name|receipt
operator|.
name|acknowledge
argument_list|()
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|producer
operator|.
name|send
argument_list|(
literal|"Hello World!"
argument_list|)
expr_stmt|;
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|,
name|to
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAcknowledgeAsync ()
specifier|public
name|void
name|testAcknowledgeAsync
parameter_list|()
throws|throws
name|Exception
block|{
name|to
operator|.
name|expectsNoDuplicates
argument_list|(
name|body
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
name|from
argument_list|(
name|from
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"myRoute"
argument_list|)
operator|.
name|to
argument_list|(
name|to
argument_list|)
operator|.
name|process
argument_list|(
name|exchange
lambda|->
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Processing message {}"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|PulsarMessageReceipt
name|receipt
init|=
operator|(
name|PulsarMessageReceipt
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|PulsarMessageHeaders
operator|.
name|MESSAGE_RECEIPT
argument_list|)
decl_stmt|;
try|try
block|{
name|CompletableFuture
argument_list|<
name|Void
argument_list|>
name|f
init|=
name|receipt
operator|.
name|acknowledgeAsync
argument_list|()
decl_stmt|;
name|f
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|producer
operator|.
name|send
argument_list|(
literal|"Hello World!"
argument_list|)
expr_stmt|;
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|,
name|to
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAcknowledgeCumulative ()
specifier|public
name|void
name|testAcknowledgeCumulative
parameter_list|()
throws|throws
name|Exception
block|{
name|to
operator|.
name|expectsNoDuplicates
argument_list|(
name|body
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
name|from
argument_list|(
name|from
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"myRoute"
argument_list|)
operator|.
name|to
argument_list|(
name|to
argument_list|)
operator|.
name|process
argument_list|(
name|exchange
lambda|->
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Processing message {}"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|PulsarMessageReceipt
name|receipt
init|=
operator|(
name|PulsarMessageReceipt
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|PulsarMessageHeaders
operator|.
name|MESSAGE_RECEIPT
argument_list|)
decl_stmt|;
comment|// Ack the second message. The first will also be acked.
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|.
name|equals
argument_list|(
literal|"Hello World Again!"
argument_list|)
condition|)
block|{
name|receipt
operator|.
name|acknowledgeCumulative
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|producer
operator|.
name|send
argument_list|(
literal|"Hello World!"
argument_list|)
expr_stmt|;
name|producer
operator|.
name|send
argument_list|(
literal|"Hello World Again!"
argument_list|)
expr_stmt|;
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|,
name|to
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAcknowledgeCumulativeAsync ()
specifier|public
name|void
name|testAcknowledgeCumulativeAsync
parameter_list|()
throws|throws
name|Exception
block|{
name|to
operator|.
name|expectsNoDuplicates
argument_list|(
name|body
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
name|from
argument_list|(
name|from
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"myRoute"
argument_list|)
operator|.
name|to
argument_list|(
name|to
argument_list|)
operator|.
name|process
argument_list|(
name|exchange
lambda|->
block|{
name|LOGGER
operator|.
name|info
argument_list|(
literal|"Processing message {}"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|PulsarMessageReceipt
name|receipt
init|=
operator|(
name|PulsarMessageReceipt
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|PulsarMessageHeaders
operator|.
name|MESSAGE_RECEIPT
argument_list|)
decl_stmt|;
comment|// Ack the second message. The first will also be acked.
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
operator|.
name|equals
argument_list|(
literal|"Hello World Again!"
argument_list|)
condition|)
block|{
try|try
block|{
name|CompletableFuture
argument_list|<
name|Void
argument_list|>
name|f
init|=
name|receipt
operator|.
name|acknowledgeCumulativeAsync
argument_list|()
decl_stmt|;
name|f
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOGGER
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|producer
operator|.
name|send
argument_list|(
literal|"Hello World!"
argument_list|)
expr_stmt|;
name|producer
operator|.
name|send
argument_list|(
literal|"Hello World Again!"
argument_list|)
expr_stmt|;
name|MockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|,
name|to
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

