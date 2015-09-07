begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
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
name|ConcurrentHashMap
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
name|Exchanger
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
name|TimeoutException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Destination
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
name|Session
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
name|AsyncCallback
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
name|CamelException
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
name|component
operator|.
name|sjms
operator|.
name|MessageConsumerResources
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
name|MessageProducerResources
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
name|SjmsEndpoint
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
name|SjmsMessage
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
name|SjmsProducer
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
name|jms
operator|.
name|JmsConstants
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
name|jms
operator|.
name|JmsMessageHelper
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
name|jms
operator|.
name|JmsObjectFactory
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
name|tx
operator|.
name|SessionTransactionSynchronization
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
name|UuidGenerator
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
name|util
operator|.
name|ExchangeHelper
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|pool
operator|.
name|BasePoolableObjectFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|pool
operator|.
name|impl
operator|.
name|GenericObjectPool
import|;
end_import

begin_comment
comment|/**  * A Camel Producer that provides the InOut Exchange pattern.  */
end_comment

begin_class
DECL|class|InOutProducer
specifier|public
class|class
name|InOutProducer
extends|extends
name|SjmsProducer
block|{
DECL|field|EXCHANGERS
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Exchanger
argument_list|<
name|Object
argument_list|>
argument_list|>
name|EXCHANGERS
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|Exchanger
argument_list|<
name|Object
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|GENERATED_CORRELATION_ID_PREFIX
specifier|private
specifier|static
specifier|final
name|String
name|GENERATED_CORRELATION_ID_PREFIX
init|=
literal|"Camel-"
decl_stmt|;
DECL|field|uuidGenerator
specifier|private
name|UuidGenerator
name|uuidGenerator
decl_stmt|;
DECL|method|getUuidGenerator ()
specifier|public
name|UuidGenerator
name|getUuidGenerator
parameter_list|()
block|{
return|return
name|uuidGenerator
return|;
block|}
DECL|method|setUuidGenerator (UuidGenerator uuidGenerator)
specifier|public
name|void
name|setUuidGenerator
parameter_list|(
name|UuidGenerator
name|uuidGenerator
parameter_list|)
block|{
name|this
operator|.
name|uuidGenerator
operator|=
name|uuidGenerator
expr_stmt|;
block|}
comment|/**      * A pool of {@link MessageConsumerResources} objects that are the reply      * consumers.      */
DECL|class|MessageConsumerResourcesFactory
specifier|protected
class|class
name|MessageConsumerResourcesFactory
extends|extends
name|BasePoolableObjectFactory
argument_list|<
name|MessageConsumerResources
argument_list|>
block|{
annotation|@
name|Override
DECL|method|makeObject ()
specifier|public
name|MessageConsumerResources
name|makeObject
parameter_list|()
throws|throws
name|Exception
block|{
name|MessageConsumerResources
name|answer
decl_stmt|;
name|Connection
name|conn
init|=
name|getConnectionResource
argument_list|()
operator|.
name|borrowConnection
argument_list|()
decl_stmt|;
try|try
block|{
name|Session
name|session
decl_stmt|;
if|if
condition|(
name|isEndpointTransacted
argument_list|()
condition|)
block|{
name|session
operator|=
name|conn
operator|.
name|createSession
argument_list|(
literal|true
argument_list|,
name|Session
operator|.
name|SESSION_TRANSACTED
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|session
operator|=
name|conn
operator|.
name|createSession
argument_list|(
literal|false
argument_list|,
name|Session
operator|.
name|AUTO_ACKNOWLEDGE
argument_list|)
expr_stmt|;
block|}
name|Destination
name|replyToDestination
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|getNamedReplyTo
argument_list|()
argument_list|)
condition|)
block|{
name|replyToDestination
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getDestinationCreationStrategy
argument_list|()
operator|.
name|createTemporaryDestination
argument_list|(
name|session
argument_list|,
name|isTopic
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|replyToDestination
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getDestinationCreationStrategy
argument_list|()
operator|.
name|createDestination
argument_list|(
name|session
argument_list|,
name|getNamedReplyTo
argument_list|()
argument_list|,
name|isTopic
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|MessageConsumer
name|messageConsumer
init|=
name|JmsObjectFactory
operator|.
name|createMessageConsumer
argument_list|(
name|session
argument_list|,
name|replyToDestination
argument_list|,
literal|null
argument_list|,
name|isTopic
argument_list|()
argument_list|,
literal|null
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|messageConsumer
operator|.
name|setMessageListener
argument_list|(
operator|new
name|MessageListener
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onMessage
parameter_list|(
specifier|final
name|Message
name|message
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Message Received in the Consumer Pool"
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"  Message : {}"
argument_list|,
name|message
argument_list|)
expr_stmt|;
try|try
block|{
name|Exchanger
argument_list|<
name|Object
argument_list|>
name|exchanger
init|=
name|EXCHANGERS
operator|.
name|get
argument_list|(
name|message
operator|.
name|getJMSCorrelationID
argument_list|()
argument_list|)
decl_stmt|;
name|exchanger
operator|.
name|exchange
argument_list|(
name|message
argument_list|,
name|getResponseTimeOut
argument_list|()
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
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
literal|"Unable to exchange message: {}"
argument_list|,
name|message
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
name|answer
operator|=
operator|new
name|MessageConsumerResources
argument_list|(
name|session
argument_list|,
name|messageConsumer
argument_list|,
name|replyToDestination
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
literal|"Unable to create the MessageConsumerResource: "
operator|+
name|e
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|CamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|getConnectionResource
argument_list|()
operator|.
name|returnConnection
argument_list|(
name|conn
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|destroyObject (MessageConsumerResources model)
specifier|public
name|void
name|destroyObject
parameter_list|(
name|MessageConsumerResources
name|model
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|model
operator|.
name|getMessageConsumer
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|model
operator|.
name|getMessageConsumer
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|model
operator|.
name|getSession
argument_list|()
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|model
operator|.
name|getSession
argument_list|()
operator|.
name|getTransacted
argument_list|()
condition|)
block|{
try|try
block|{
name|model
operator|.
name|getSession
argument_list|()
operator|.
name|rollback
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// Do nothing. Just make sure we are cleaned up
block|}
block|}
name|model
operator|.
name|getSession
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|field|consumers
specifier|private
name|GenericObjectPool
argument_list|<
name|MessageConsumerResources
argument_list|>
name|consumers
decl_stmt|;
DECL|method|InOutProducer (final SjmsEndpoint endpoint)
specifier|public
name|InOutProducer
parameter_list|(
specifier|final
name|SjmsEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|getNamedReplyTo
argument_list|()
argument_list|)
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"No reply to destination is defined.  Using temporary destinations."
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Using {} as the reply to destination."
argument_list|,
name|getNamedReplyTo
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|uuidGenerator
operator|==
literal|null
condition|)
block|{
comment|// use the generator configured on the camel context
name|uuidGenerator
operator|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getUuidGenerator
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|getConsumers
argument_list|()
operator|==
literal|null
condition|)
block|{
name|setConsumers
argument_list|(
operator|new
name|GenericObjectPool
argument_list|<
name|MessageConsumerResources
argument_list|>
argument_list|(
operator|new
name|MessageConsumerResourcesFactory
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|getConsumers
argument_list|()
operator|.
name|setMaxActive
argument_list|(
name|getConsumerCount
argument_list|()
argument_list|)
expr_stmt|;
name|getConsumers
argument_list|()
operator|.
name|setMaxIdle
argument_list|(
name|getConsumerCount
argument_list|()
argument_list|)
expr_stmt|;
while|while
condition|(
name|getConsumers
argument_list|()
operator|.
name|getNumIdle
argument_list|()
operator|<
name|getConsumers
argument_list|()
operator|.
name|getMaxIdle
argument_list|()
condition|)
block|{
name|getConsumers
argument_list|()
operator|.
name|addObject
argument_list|()
expr_stmt|;
block|}
block|}
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
if|if
condition|(
name|getConsumers
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getConsumers
argument_list|()
operator|.
name|close
argument_list|()
expr_stmt|;
name|setConsumers
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doCreateProducerModel ()
specifier|public
name|MessageProducerResources
name|doCreateProducerModel
parameter_list|()
throws|throws
name|Exception
block|{
name|MessageProducerResources
name|answer
decl_stmt|;
name|Connection
name|conn
init|=
name|getConnectionResource
argument_list|()
operator|.
name|borrowConnection
argument_list|()
decl_stmt|;
try|try
block|{
name|Session
name|session
init|=
name|conn
operator|.
name|createSession
argument_list|(
name|isEndpointTransacted
argument_list|()
argument_list|,
name|getAcknowledgeMode
argument_list|()
argument_list|)
decl_stmt|;
name|Destination
name|destination
init|=
name|getEndpoint
argument_list|()
operator|.
name|getDestinationCreationStrategy
argument_list|()
operator|.
name|createDestination
argument_list|(
name|session
argument_list|,
name|getDestinationName
argument_list|()
argument_list|,
name|isTopic
argument_list|()
argument_list|)
decl_stmt|;
name|MessageProducer
name|messageProducer
init|=
name|JmsObjectFactory
operator|.
name|createMessageProducer
argument_list|(
name|session
argument_list|,
name|destination
argument_list|,
name|isPersistent
argument_list|()
argument_list|,
name|getTtl
argument_list|()
argument_list|)
decl_stmt|;
name|answer
operator|=
operator|new
name|MessageProducerResources
argument_list|(
name|session
argument_list|,
name|messageProducer
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
literal|"Unable to create the MessageProducer"
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
finally|finally
block|{
name|getConnectionResource
argument_list|()
operator|.
name|returnConnection
argument_list|(
name|conn
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * TODO time out is actually double as it waits for the producer and then      * waits for the response. Use an atomic long to manage the countdown      */
annotation|@
name|Override
DECL|method|sendMessage (final Exchange exchange, final AsyncCallback callback, final MessageProducerResources producer)
specifier|public
name|void
name|sendMessage
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|,
specifier|final
name|MessageProducerResources
name|producer
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|isEndpointTransacted
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|.
name|addSynchronization
argument_list|(
operator|new
name|SessionTransactionSynchronization
argument_list|(
name|producer
operator|.
name|getSession
argument_list|()
argument_list|,
name|getCommitStrategy
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Message
name|request
init|=
name|getEndpoint
argument_list|()
operator|.
name|getBinding
argument_list|()
operator|.
name|makeJmsMessage
argument_list|(
name|exchange
argument_list|,
name|producer
operator|.
name|getSession
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|correlationId
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JmsConstants
operator|.
name|JMS_CORRELATION_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|correlationId
operator|==
literal|null
condition|)
block|{
comment|// we append the 'Camel-' prefix to know it was generated by us
name|correlationId
operator|=
name|GENERATED_CORRELATION_ID_PREFIX
operator|+
name|getUuidGenerator
argument_list|()
operator|.
name|generateUuid
argument_list|()
expr_stmt|;
block|}
name|Object
name|responseObject
init|=
literal|null
decl_stmt|;
name|Exchanger
argument_list|<
name|Object
argument_list|>
name|messageExchanger
init|=
operator|new
name|Exchanger
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|JmsMessageHelper
operator|.
name|setCorrelationId
argument_list|(
name|request
argument_list|,
name|correlationId
argument_list|)
expr_stmt|;
name|EXCHANGERS
operator|.
name|put
argument_list|(
name|correlationId
argument_list|,
name|messageExchanger
argument_list|)
expr_stmt|;
name|MessageConsumerResources
name|consumer
init|=
name|consumers
operator|.
name|borrowObject
argument_list|()
decl_stmt|;
name|JmsMessageHelper
operator|.
name|setJMSReplyTo
argument_list|(
name|request
argument_list|,
name|consumer
operator|.
name|getReplyToDestination
argument_list|()
argument_list|)
expr_stmt|;
name|consumers
operator|.
name|returnObject
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|producer
operator|.
name|getMessageProducer
argument_list|()
operator|.
name|send
argument_list|(
name|request
argument_list|)
expr_stmt|;
comment|// Return the producer to the pool so another waiting producer
comment|// can move forward
comment|// without waiting on us to complete the exchange
try|try
block|{
name|getProducers
argument_list|()
operator|.
name|returnObject
argument_list|(
name|producer
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|exception
parameter_list|)
block|{
comment|// thrown if the pool is full. safe to ignore.
block|}
try|try
block|{
name|responseObject
operator|=
name|messageExchanger
operator|.
name|exchange
argument_list|(
literal|null
argument_list|,
name|getResponseTimeOut
argument_list|()
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
expr_stmt|;
name|EXCHANGERS
operator|.
name|remove
argument_list|(
name|correlationId
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Exchanger was interrupted while waiting on response"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TimeoutException
name|e
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Exchanger timed out while waiting on response"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|responseObject
operator|instanceof
name|Throwable
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|(
name|Throwable
operator|)
name|responseObject
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|responseObject
operator|instanceof
name|Message
condition|)
block|{
name|Message
name|message
init|=
operator|(
name|Message
operator|)
name|responseObject
decl_stmt|;
name|SjmsMessage
name|response
init|=
operator|new
name|SjmsMessage
argument_list|(
name|message
argument_list|,
name|consumer
operator|.
name|getSession
argument_list|()
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getBinding
argument_list|()
argument_list|)
decl_stmt|;
comment|// the JmsBinding is designed to be "pull-based": it will populate the Camel message on demand
comment|// therefore, we link Exchange and OUT message before continuing, so that the JmsBinding has full access
comment|// to everything it may need, and can populate headers, properties, etc. accordingly (solves CAMEL-6218).
name|exchange
operator|.
name|setOut
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|CamelException
argument_list|(
literal|"Unknown response type: "
operator|+
name|responseObject
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|callback
operator|.
name|done
argument_list|(
name|isSynchronous
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|setConsumers (GenericObjectPool<MessageConsumerResources> consumers)
specifier|public
name|void
name|setConsumers
parameter_list|(
name|GenericObjectPool
argument_list|<
name|MessageConsumerResources
argument_list|>
name|consumers
parameter_list|)
block|{
name|this
operator|.
name|consumers
operator|=
name|consumers
expr_stmt|;
block|}
DECL|method|getConsumers ()
specifier|public
name|GenericObjectPool
argument_list|<
name|MessageConsumerResources
argument_list|>
name|getConsumers
parameter_list|()
block|{
return|return
name|consumers
return|;
block|}
block|}
end_class

end_unit

