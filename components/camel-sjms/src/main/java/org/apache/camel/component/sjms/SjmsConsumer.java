begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms
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
name|ExchangePattern
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
name|component
operator|.
name|sjms
operator|.
name|consumer
operator|.
name|AbstractMessageHandler
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
name|consumer
operator|.
name|InOnlyMessageHandler
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
name|consumer
operator|.
name|InOutMessageHandler
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
name|ConnectionResource
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
name|jms
operator|.
name|ObjectPool
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
name|taskmanager
operator|.
name|TimedTaskManager
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
name|BatchTransactionCommitStrategy
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
name|DefaultTransactionCommitStrategy
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
name|SessionBatchTransactionSynchronization
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
name|impl
operator|.
name|DefaultConsumer
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

begin_comment
comment|/**  * The SjmsConsumer is the base class for the SJMS MessageListener pool.  *   */
end_comment

begin_class
DECL|class|SjmsConsumer
specifier|public
class|class
name|SjmsConsumer
extends|extends
name|DefaultConsumer
block|{
DECL|field|consumers
specifier|protected
name|MessageConsumerPool
name|consumers
decl_stmt|;
DECL|field|executor
specifier|private
specifier|final
name|ExecutorService
name|executor
decl_stmt|;
comment|/**      * A pool of MessageConsumerResources created at the initialization of the associated consumer.      */
DECL|class|MessageConsumerPool
specifier|protected
class|class
name|MessageConsumerPool
extends|extends
name|ObjectPool
argument_list|<
name|MessageConsumerResources
argument_list|>
block|{
DECL|method|MessageConsumerPool ()
specifier|public
name|MessageConsumerPool
parameter_list|()
block|{
name|super
argument_list|(
name|getConsumerCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**           * Creates a new MessageConsumerResources instance.          *          * @see org.apache.camel.component.sjms.jms.ObjectPool#createObject()          */
annotation|@
name|Override
DECL|method|createObject ()
specifier|protected
name|MessageConsumerResources
name|createObject
parameter_list|()
throws|throws
name|Exception
block|{
name|MessageConsumerResources
name|model
init|=
name|createConsumer
argument_list|()
decl_stmt|;
return|return
name|model
return|;
block|}
comment|/**           * Cleans up the MessageConsumerResources.          *          * @see org.apache.camel.component.sjms.jms.ObjectPool#destroyObject(java.lang.Object)          */
annotation|@
name|Override
DECL|method|destroyObject (MessageConsumerResources model)
specifier|protected
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
operator|!=
literal|null
condition|)
block|{
comment|// First clean up our message consumer
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
comment|// If the resource has a
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
block|}
DECL|class|MessageConsumerResources
specifier|protected
class|class
name|MessageConsumerResources
block|{
DECL|field|session
specifier|private
specifier|final
name|Session
name|session
decl_stmt|;
DECL|field|messageConsumer
specifier|private
specifier|final
name|MessageConsumer
name|messageConsumer
decl_stmt|;
DECL|method|MessageConsumerResources (MessageConsumer messageConsumer)
specifier|public
name|MessageConsumerResources
parameter_list|(
name|MessageConsumer
name|messageConsumer
parameter_list|)
block|{
name|this
operator|.
name|session
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|messageConsumer
operator|=
name|messageConsumer
expr_stmt|;
block|}
DECL|method|MessageConsumerResources (Session session, MessageConsumer messageConsumer)
specifier|public
name|MessageConsumerResources
parameter_list|(
name|Session
name|session
parameter_list|,
name|MessageConsumer
name|messageConsumer
parameter_list|)
block|{
name|this
operator|.
name|session
operator|=
name|session
expr_stmt|;
name|this
operator|.
name|messageConsumer
operator|=
name|messageConsumer
expr_stmt|;
block|}
comment|/**          * Gets the Session value of session for this instance of          * MessageProducerModel.          *           * @return the session          */
DECL|method|getSession ()
specifier|public
name|Session
name|getSession
parameter_list|()
block|{
return|return
name|session
return|;
block|}
comment|/**          * Gets the QueueSender value of queueSender for this instance of          * MessageProducerModel.          *           * @return the queueSender          */
DECL|method|getMessageConsumer ()
specifier|public
name|MessageConsumer
name|getMessageConsumer
parameter_list|()
block|{
return|return
name|messageConsumer
return|;
block|}
block|}
DECL|method|SjmsConsumer (Endpoint endpoint, Processor processor)
specifier|public
name|SjmsConsumer
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|executor
operator|=
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newDefaultThreadPool
argument_list|(
name|this
argument_list|,
literal|"SjmsConsumer"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|SjmsEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|SjmsEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|consumers
operator|=
operator|new
name|MessageConsumerPool
argument_list|()
expr_stmt|;
name|consumers
operator|.
name|fillPool
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
name|consumers
operator|!=
literal|null
condition|)
block|{
name|consumers
operator|.
name|drainPool
argument_list|()
expr_stmt|;
name|consumers
operator|=
literal|null
expr_stmt|;
block|}
block|}
comment|/**      * Creates a {@link MessageConsumerResources} with a dedicated      * {@link Session} required for transacted and InOut consumers.      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
DECL|method|createConsumer ()
specifier|private
name|MessageConsumerResources
name|createConsumer
parameter_list|()
throws|throws
name|Exception
block|{
name|MessageConsumerResources
name|answer
init|=
literal|null
decl_stmt|;
name|Connection
name|conn
init|=
literal|null
decl_stmt|;
try|try
block|{
name|conn
operator|=
name|getConnectionResource
argument_list|()
operator|.
name|borrowConnection
argument_list|()
expr_stmt|;
name|Session
name|session
init|=
literal|null
decl_stmt|;
name|MessageConsumer
name|messageConsumer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|isTransacted
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
name|messageConsumer
operator|=
name|JmsObjectFactory
operator|.
name|createMessageConsumer
argument_list|(
name|session
argument_list|,
name|getDestinationName
argument_list|()
argument_list|,
name|getMessageSelector
argument_list|()
argument_list|,
name|isTopic
argument_list|()
argument_list|,
name|getDurableSubscriptionId
argument_list|()
argument_list|)
expr_stmt|;
name|MessageListener
name|handler
init|=
name|createMessageHandler
argument_list|(
name|session
argument_list|)
decl_stmt|;
name|messageConsumer
operator|.
name|setMessageListener
argument_list|(
name|handler
argument_list|)
expr_stmt|;
if|if
condition|(
name|session
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
literal|"Message Consumer Creation Exception: Session is NULL"
argument_list|)
throw|;
block|}
if|if
condition|(
name|messageConsumer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
literal|"Message Consumer Creation Exception: MessageConsumer is NULL"
argument_list|)
throw|;
block|}
name|answer
operator|=
operator|new
name|MessageConsumerResources
argument_list|(
name|session
argument_list|,
name|messageConsumer
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
literal|"Unable to create the MessageConsumer: "
operator|+
name|e
operator|.
name|getLocalizedMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|conn
operator|!=
literal|null
condition|)
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
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Helper factory method used to create a MessageListener based on the MEP      *       * @param session a session is only required if we are a transacted consumer      * @return the listener      */
DECL|method|createMessageHandler (Session session)
specifier|protected
name|MessageListener
name|createMessageHandler
parameter_list|(
name|Session
name|session
parameter_list|)
block|{
name|TransactionCommitStrategy
name|commitStrategy
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|getTransactionCommitStrategy
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|commitStrategy
operator|=
name|getTransactionCommitStrategy
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|getTransactionBatchCount
argument_list|()
operator|>
literal|0
condition|)
block|{
name|commitStrategy
operator|=
operator|new
name|BatchTransactionCommitStrategy
argument_list|(
name|getTransactionBatchCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|commitStrategy
operator|=
operator|new
name|DefaultTransactionCommitStrategy
argument_list|()
expr_stmt|;
block|}
name|Synchronization
name|synchronization
decl_stmt|;
if|if
condition|(
name|commitStrategy
operator|instanceof
name|BatchTransactionCommitStrategy
condition|)
block|{
name|TimedTaskManager
name|timedTaskManager
init|=
name|getEndpoint
argument_list|()
operator|.
name|getComponent
argument_list|()
operator|.
name|getTimedTaskManager
argument_list|()
decl_stmt|;
name|synchronization
operator|=
operator|new
name|SessionBatchTransactionSynchronization
argument_list|(
name|timedTaskManager
argument_list|,
name|session
argument_list|,
name|commitStrategy
argument_list|,
name|getTransactionBatchTimeout
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|synchronization
operator|=
operator|new
name|SessionTransactionSynchronization
argument_list|(
name|session
argument_list|,
name|commitStrategy
argument_list|)
expr_stmt|;
block|}
name|AbstractMessageHandler
name|messageHandler
decl_stmt|;
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getExchangePattern
argument_list|()
operator|.
name|equals
argument_list|(
name|ExchangePattern
operator|.
name|InOnly
argument_list|)
condition|)
block|{
if|if
condition|(
name|isTransacted
argument_list|()
condition|)
block|{
name|messageHandler
operator|=
operator|new
name|InOnlyMessageHandler
argument_list|(
name|getEndpoint
argument_list|()
argument_list|,
name|executor
argument_list|,
name|synchronization
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|messageHandler
operator|=
operator|new
name|InOnlyMessageHandler
argument_list|(
name|getEndpoint
argument_list|()
argument_list|,
name|executor
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|isTransacted
argument_list|()
condition|)
block|{
name|messageHandler
operator|=
operator|new
name|InOutMessageHandler
argument_list|(
name|getEndpoint
argument_list|()
argument_list|,
name|executor
argument_list|,
name|synchronization
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|messageHandler
operator|=
operator|new
name|InOutMessageHandler
argument_list|(
name|getEndpoint
argument_list|()
argument_list|,
name|executor
argument_list|)
expr_stmt|;
block|}
block|}
name|messageHandler
operator|.
name|setSession
argument_list|(
name|session
argument_list|)
expr_stmt|;
name|messageHandler
operator|.
name|setProcessor
argument_list|(
name|getAsyncProcessor
argument_list|()
argument_list|)
expr_stmt|;
name|messageHandler
operator|.
name|setSynchronous
argument_list|(
name|isSynchronous
argument_list|()
argument_list|)
expr_stmt|;
name|messageHandler
operator|.
name|setTransacted
argument_list|(
name|isTransacted
argument_list|()
argument_list|)
expr_stmt|;
name|messageHandler
operator|.
name|setTopic
argument_list|(
name|isTopic
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|messageHandler
return|;
block|}
DECL|method|getConnectionResource ()
specifier|protected
name|ConnectionResource
name|getConnectionResource
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getConnectionResource
argument_list|()
return|;
block|}
DECL|method|getAcknowledgementMode ()
specifier|public
name|int
name|getAcknowledgementMode
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getAcknowledgementMode
argument_list|()
operator|.
name|intValue
argument_list|()
return|;
block|}
comment|/**      * Use to determine if transactions are enabled or disabled.      *       * @return true if transacted, otherwise false      */
DECL|method|isTransacted ()
specifier|public
name|boolean
name|isTransacted
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|isTransacted
argument_list|()
return|;
block|}
comment|/**      * Use to determine whether or not to process exchanges synchronously.      *       * @return true if synchronous      */
DECL|method|isSynchronous ()
specifier|public
name|boolean
name|isSynchronous
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|isSynchronous
argument_list|()
return|;
block|}
comment|/**      * The destination name for this consumer.      *       * @return String      */
DECL|method|getDestinationName ()
specifier|public
name|String
name|getDestinationName
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getDestinationName
argument_list|()
return|;
block|}
comment|/**      * Returns the number of consumer listeners.      *       * @return the consumerCount      */
DECL|method|getConsumerCount ()
specifier|public
name|int
name|getConsumerCount
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getConsumerCount
argument_list|()
return|;
block|}
comment|/**      * Flag set by the endpoint used by consumers and producers to determine if      * the consumer is a JMS Topic.      *       * @return the topic true if consumer is a JMS Topic, default is false      */
DECL|method|isTopic ()
specifier|public
name|boolean
name|isTopic
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|isTopic
argument_list|()
return|;
block|}
comment|/**      * Gets the JMS Message selector syntax.      */
DECL|method|getMessageSelector ()
specifier|public
name|String
name|getMessageSelector
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getMessageSelector
argument_list|()
return|;
block|}
comment|/**      * Gets the durable subscription Id.      *       * @return the durableSubscriptionId      */
DECL|method|getDurableSubscriptionId ()
specifier|public
name|String
name|getDurableSubscriptionId
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getDurableSubscriptionId
argument_list|()
return|;
block|}
comment|/**      * Gets the commit strategy.      *       * @return the transactionCommitStrategy      */
DECL|method|getTransactionCommitStrategy ()
specifier|public
name|TransactionCommitStrategy
name|getTransactionCommitStrategy
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getTransactionCommitStrategy
argument_list|()
return|;
block|}
comment|/**      * If transacted, returns the nubmer of messages to be processed before      * committing the transaction.      *       * @return the transactionBatchCount      */
DECL|method|getTransactionBatchCount ()
specifier|public
name|int
name|getTransactionBatchCount
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getTransactionBatchCount
argument_list|()
return|;
block|}
comment|/**      * Returns the timeout value for batch transactions.      *       * @return long      */
DECL|method|getTransactionBatchTimeout ()
specifier|public
name|long
name|getTransactionBatchTimeout
parameter_list|()
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getTransactionBatchTimeout
argument_list|()
return|;
block|}
block|}
end_class

end_unit

