begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jms
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
name|javax
operator|.
name|jms
operator|.
name|ConnectionFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|ExceptionListener
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
name|HeaderFilterStrategyAware
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
name|jms
operator|.
name|requestor
operator|.
name|Requestor
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
name|DefaultComponent
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
name|HeaderFilterStrategy
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
name|logging
operator|.
name|Log
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
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|BeansException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContextAware
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|task
operator|.
name|TaskExecutor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jms
operator|.
name|connection
operator|.
name|JmsTransactionManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jms
operator|.
name|core
operator|.
name|JmsOperations
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jms
operator|.
name|support
operator|.
name|converter
operator|.
name|MessageConverter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jms
operator|.
name|support
operator|.
name|destination
operator|.
name|DestinationResolver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|transaction
operator|.
name|PlatformTransactionManager
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
name|util
operator|.
name|ObjectHelper
operator|.
name|removeStartingCharacters
import|;
end_import

begin_comment
comment|/**  * A<a href="http://activemq.apache.org/jms.html">JMS Component</a>  *  * @version $Revision:520964 $  */
end_comment

begin_class
DECL|class|JmsComponent
specifier|public
class|class
name|JmsComponent
extends|extends
name|DefaultComponent
implements|implements
name|ApplicationContextAware
implements|,
name|HeaderFilterStrategyAware
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|JmsComponent
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|DEFAULT_QUEUE_BROWSE_STRATEGY
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_QUEUE_BROWSE_STRATEGY
init|=
literal|"org.apache.camel.component.jms.DefaultQueueBrowseStrategy"
decl_stmt|;
DECL|field|configuration
specifier|private
name|JmsConfiguration
name|configuration
decl_stmt|;
DECL|field|applicationContext
specifier|private
name|ApplicationContext
name|applicationContext
decl_stmt|;
DECL|field|requestor
specifier|private
name|Requestor
name|requestor
decl_stmt|;
DECL|field|queueBrowseStrategy
specifier|private
name|QueueBrowseStrategy
name|queueBrowseStrategy
decl_stmt|;
DECL|field|attemptedToCreateQueueBrowserStrategy
specifier|private
name|boolean
name|attemptedToCreateQueueBrowserStrategy
decl_stmt|;
DECL|field|headerFilterStrategy
specifier|private
name|HeaderFilterStrategy
name|headerFilterStrategy
decl_stmt|;
DECL|method|JmsComponent ()
specifier|public
name|JmsComponent
parameter_list|()
block|{
name|setHeaderFilterStrategy
argument_list|(
operator|new
name|JmsHeaderFilterStrategy
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|JmsComponent (JmsConfiguration configuration)
specifier|public
name|JmsComponent
parameter_list|(
name|JmsConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|setHeaderFilterStrategy
argument_list|(
operator|new
name|JmsHeaderFilterStrategy
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|JmsComponent (CamelContext context)
specifier|public
name|JmsComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|setHeaderFilterStrategy
argument_list|(
operator|new
name|JmsHeaderFilterStrategy
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Static builder method      */
DECL|method|jmsComponent ()
specifier|public
specifier|static
name|JmsComponent
name|jmsComponent
parameter_list|()
block|{
return|return
operator|new
name|JmsComponent
argument_list|()
return|;
block|}
comment|/**      * Static builder method      */
DECL|method|jmsComponent (JmsConfiguration configuration)
specifier|public
specifier|static
name|JmsComponent
name|jmsComponent
parameter_list|(
name|JmsConfiguration
name|configuration
parameter_list|)
block|{
return|return
operator|new
name|JmsComponent
argument_list|(
name|configuration
argument_list|)
return|;
block|}
comment|/**      * Static builder method      */
DECL|method|jmsComponent (ConnectionFactory connectionFactory)
specifier|public
specifier|static
name|JmsComponent
name|jmsComponent
parameter_list|(
name|ConnectionFactory
name|connectionFactory
parameter_list|)
block|{
return|return
name|jmsComponent
argument_list|(
operator|new
name|JmsConfiguration
argument_list|(
name|connectionFactory
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Static builder method      */
DECL|method|jmsComponentClientAcknowledge (ConnectionFactory connectionFactory)
specifier|public
specifier|static
name|JmsComponent
name|jmsComponentClientAcknowledge
parameter_list|(
name|ConnectionFactory
name|connectionFactory
parameter_list|)
block|{
name|JmsConfiguration
name|template
init|=
operator|new
name|JmsConfiguration
argument_list|(
name|connectionFactory
argument_list|)
decl_stmt|;
name|template
operator|.
name|setAcknowledgementMode
argument_list|(
name|Session
operator|.
name|CLIENT_ACKNOWLEDGE
argument_list|)
expr_stmt|;
return|return
name|jmsComponent
argument_list|(
name|template
argument_list|)
return|;
block|}
comment|/**      * Static builder method      */
DECL|method|jmsComponentAutoAcknowledge (ConnectionFactory connectionFactory)
specifier|public
specifier|static
name|JmsComponent
name|jmsComponentAutoAcknowledge
parameter_list|(
name|ConnectionFactory
name|connectionFactory
parameter_list|)
block|{
name|JmsConfiguration
name|template
init|=
operator|new
name|JmsConfiguration
argument_list|(
name|connectionFactory
argument_list|)
decl_stmt|;
name|template
operator|.
name|setAcknowledgementMode
argument_list|(
name|Session
operator|.
name|AUTO_ACKNOWLEDGE
argument_list|)
expr_stmt|;
return|return
name|jmsComponent
argument_list|(
name|template
argument_list|)
return|;
block|}
DECL|method|jmsComponentTransacted (ConnectionFactory connectionFactory)
specifier|public
specifier|static
name|JmsComponent
name|jmsComponentTransacted
parameter_list|(
name|ConnectionFactory
name|connectionFactory
parameter_list|)
block|{
name|JmsTransactionManager
name|transactionManager
init|=
operator|new
name|JmsTransactionManager
argument_list|()
decl_stmt|;
name|transactionManager
operator|.
name|setConnectionFactory
argument_list|(
name|connectionFactory
argument_list|)
expr_stmt|;
return|return
name|jmsComponentTransacted
argument_list|(
name|connectionFactory
argument_list|,
name|transactionManager
argument_list|)
return|;
block|}
DECL|method|jmsComponentTransacted (ConnectionFactory connectionFactory, PlatformTransactionManager transactionManager)
specifier|public
specifier|static
name|JmsComponent
name|jmsComponentTransacted
parameter_list|(
name|ConnectionFactory
name|connectionFactory
parameter_list|,
name|PlatformTransactionManager
name|transactionManager
parameter_list|)
block|{
name|JmsConfiguration
name|template
init|=
operator|new
name|JmsConfiguration
argument_list|(
name|connectionFactory
argument_list|)
decl_stmt|;
name|template
operator|.
name|setTransactionManager
argument_list|(
name|transactionManager
argument_list|)
expr_stmt|;
name|template
operator|.
name|setTransacted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|jmsComponent
argument_list|(
name|template
argument_list|)
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|getConfiguration ()
specifier|public
name|JmsConfiguration
name|getConfiguration
parameter_list|()
block|{
if|if
condition|(
name|configuration
operator|==
literal|null
condition|)
block|{
name|configuration
operator|=
name|createConfiguration
argument_list|()
expr_stmt|;
comment|// If we are being configured with spring...
if|if
condition|(
name|applicationContext
operator|!=
literal|null
condition|)
block|{
name|Map
name|beansOfType
init|=
name|applicationContext
operator|.
name|getBeansOfType
argument_list|(
name|ConnectionFactory
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|beansOfType
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|ConnectionFactory
name|cf
init|=
operator|(
name|ConnectionFactory
operator|)
name|beansOfType
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|configuration
operator|.
name|setConnectionFactory
argument_list|(
name|cf
argument_list|)
expr_stmt|;
block|}
name|beansOfType
operator|=
name|applicationContext
operator|.
name|getBeansOfType
argument_list|(
name|DestinationResolver
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|beansOfType
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|DestinationResolver
name|destinationResolver
init|=
operator|(
name|DestinationResolver
operator|)
name|beansOfType
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|configuration
operator|.
name|setDestinationResolver
argument_list|(
name|destinationResolver
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|configuration
return|;
block|}
comment|/**      * Sets the JMS configuration      *      * @param configuration the configuration to use by default for endpoints      */
DECL|method|setConfiguration (JmsConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|JmsConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|setAcceptMessagesWhileStopping (boolean acceptMessagesWhileStopping)
specifier|public
name|void
name|setAcceptMessagesWhileStopping
parameter_list|(
name|boolean
name|acceptMessagesWhileStopping
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setAcceptMessagesWhileStopping
argument_list|(
name|acceptMessagesWhileStopping
argument_list|)
expr_stmt|;
block|}
DECL|method|setAcknowledgementMode (int consumerAcknowledgementMode)
specifier|public
name|void
name|setAcknowledgementMode
parameter_list|(
name|int
name|consumerAcknowledgementMode
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setAcknowledgementMode
argument_list|(
name|consumerAcknowledgementMode
argument_list|)
expr_stmt|;
block|}
DECL|method|setAcknowledgementModeName (String consumerAcknowledgementMode)
specifier|public
name|void
name|setAcknowledgementModeName
parameter_list|(
name|String
name|consumerAcknowledgementMode
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setAcknowledgementModeName
argument_list|(
name|consumerAcknowledgementMode
argument_list|)
expr_stmt|;
block|}
DECL|method|setAutoStartup (boolean autoStartup)
specifier|public
name|void
name|setAutoStartup
parameter_list|(
name|boolean
name|autoStartup
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setAutoStartup
argument_list|(
name|autoStartup
argument_list|)
expr_stmt|;
block|}
DECL|method|setCacheLevel (int cacheLevel)
specifier|public
name|void
name|setCacheLevel
parameter_list|(
name|int
name|cacheLevel
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setCacheLevel
argument_list|(
name|cacheLevel
argument_list|)
expr_stmt|;
block|}
DECL|method|setCacheLevelName (String cacheName)
specifier|public
name|void
name|setCacheLevelName
parameter_list|(
name|String
name|cacheName
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setCacheLevelName
argument_list|(
name|cacheName
argument_list|)
expr_stmt|;
block|}
DECL|method|setClientId (String consumerClientId)
specifier|public
name|void
name|setClientId
parameter_list|(
name|String
name|consumerClientId
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setClientId
argument_list|(
name|consumerClientId
argument_list|)
expr_stmt|;
block|}
DECL|method|setConcurrentConsumers (int concurrentConsumers)
specifier|public
name|void
name|setConcurrentConsumers
parameter_list|(
name|int
name|concurrentConsumers
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setConcurrentConsumers
argument_list|(
name|concurrentConsumers
argument_list|)
expr_stmt|;
block|}
DECL|method|setConnectionFactory (ConnectionFactory connectionFactory)
specifier|public
name|void
name|setConnectionFactory
parameter_list|(
name|ConnectionFactory
name|connectionFactory
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setConnectionFactory
argument_list|(
name|connectionFactory
argument_list|)
expr_stmt|;
block|}
DECL|method|setConsumerType (ConsumerType consumerType)
specifier|public
name|void
name|setConsumerType
parameter_list|(
name|ConsumerType
name|consumerType
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setConsumerType
argument_list|(
name|consumerType
argument_list|)
expr_stmt|;
block|}
DECL|method|setDeliveryPersistent (boolean deliveryPersistent)
specifier|public
name|void
name|setDeliveryPersistent
parameter_list|(
name|boolean
name|deliveryPersistent
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setDeliveryPersistent
argument_list|(
name|deliveryPersistent
argument_list|)
expr_stmt|;
block|}
DECL|method|setDurableSubscriptionName (String durableSubscriptionName)
specifier|public
name|void
name|setDurableSubscriptionName
parameter_list|(
name|String
name|durableSubscriptionName
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setDurableSubscriptionName
argument_list|(
name|durableSubscriptionName
argument_list|)
expr_stmt|;
block|}
DECL|method|setExceptionListener (ExceptionListener exceptionListener)
specifier|public
name|void
name|setExceptionListener
parameter_list|(
name|ExceptionListener
name|exceptionListener
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setExceptionListener
argument_list|(
name|exceptionListener
argument_list|)
expr_stmt|;
block|}
DECL|method|setExplicitQosEnabled (boolean explicitQosEnabled)
specifier|public
name|void
name|setExplicitQosEnabled
parameter_list|(
name|boolean
name|explicitQosEnabled
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setExplicitQosEnabled
argument_list|(
name|explicitQosEnabled
argument_list|)
expr_stmt|;
block|}
DECL|method|setExposeListenerSession (boolean exposeListenerSession)
specifier|public
name|void
name|setExposeListenerSession
parameter_list|(
name|boolean
name|exposeListenerSession
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setExposeListenerSession
argument_list|(
name|exposeListenerSession
argument_list|)
expr_stmt|;
block|}
DECL|method|setIdleTaskExecutionLimit (int idleTaskExecutionLimit)
specifier|public
name|void
name|setIdleTaskExecutionLimit
parameter_list|(
name|int
name|idleTaskExecutionLimit
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setIdleTaskExecutionLimit
argument_list|(
name|idleTaskExecutionLimit
argument_list|)
expr_stmt|;
block|}
DECL|method|setMaxConcurrentConsumers (int maxConcurrentConsumers)
specifier|public
name|void
name|setMaxConcurrentConsumers
parameter_list|(
name|int
name|maxConcurrentConsumers
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setMaxConcurrentConsumers
argument_list|(
name|maxConcurrentConsumers
argument_list|)
expr_stmt|;
block|}
DECL|method|setMaxMessagesPerTask (int maxMessagesPerTask)
specifier|public
name|void
name|setMaxMessagesPerTask
parameter_list|(
name|int
name|maxMessagesPerTask
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setMaxMessagesPerTask
argument_list|(
name|maxMessagesPerTask
argument_list|)
expr_stmt|;
block|}
DECL|method|setMessageConverter (MessageConverter messageConverter)
specifier|public
name|void
name|setMessageConverter
parameter_list|(
name|MessageConverter
name|messageConverter
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setMessageConverter
argument_list|(
name|messageConverter
argument_list|)
expr_stmt|;
block|}
DECL|method|setMessageIdEnabled (boolean messageIdEnabled)
specifier|public
name|void
name|setMessageIdEnabled
parameter_list|(
name|boolean
name|messageIdEnabled
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setMessageIdEnabled
argument_list|(
name|messageIdEnabled
argument_list|)
expr_stmt|;
block|}
DECL|method|setMessageTimestampEnabled (boolean messageTimestampEnabled)
specifier|public
name|void
name|setMessageTimestampEnabled
parameter_list|(
name|boolean
name|messageTimestampEnabled
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setMessageTimestampEnabled
argument_list|(
name|messageTimestampEnabled
argument_list|)
expr_stmt|;
block|}
DECL|method|setAlwaysCopyMessage (boolean alwaysCopyMessage)
specifier|public
name|void
name|setAlwaysCopyMessage
parameter_list|(
name|boolean
name|alwaysCopyMessage
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setAlwaysCopyMessage
argument_list|(
name|alwaysCopyMessage
argument_list|)
expr_stmt|;
block|}
DECL|method|setUseMessageIDAsCorrelationID (boolean useMessageIDAsCorrelationID)
specifier|public
name|void
name|setUseMessageIDAsCorrelationID
parameter_list|(
name|boolean
name|useMessageIDAsCorrelationID
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setUseMessageIDAsCorrelationID
argument_list|(
name|useMessageIDAsCorrelationID
argument_list|)
expr_stmt|;
block|}
DECL|method|setPriority (int priority)
specifier|public
name|void
name|setPriority
parameter_list|(
name|int
name|priority
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setPriority
argument_list|(
name|priority
argument_list|)
expr_stmt|;
block|}
DECL|method|setPubSubNoLocal (boolean pubSubNoLocal)
specifier|public
name|void
name|setPubSubNoLocal
parameter_list|(
name|boolean
name|pubSubNoLocal
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setPubSubNoLocal
argument_list|(
name|pubSubNoLocal
argument_list|)
expr_stmt|;
block|}
DECL|method|setReceiveTimeout (long receiveTimeout)
specifier|public
name|void
name|setReceiveTimeout
parameter_list|(
name|long
name|receiveTimeout
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setReceiveTimeout
argument_list|(
name|receiveTimeout
argument_list|)
expr_stmt|;
block|}
DECL|method|setRecoveryInterval (long recoveryInterval)
specifier|public
name|void
name|setRecoveryInterval
parameter_list|(
name|long
name|recoveryInterval
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setRecoveryInterval
argument_list|(
name|recoveryInterval
argument_list|)
expr_stmt|;
block|}
DECL|method|setSubscriptionDurable (boolean subscriptionDurable)
specifier|public
name|void
name|setSubscriptionDurable
parameter_list|(
name|boolean
name|subscriptionDurable
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setSubscriptionDurable
argument_list|(
name|subscriptionDurable
argument_list|)
expr_stmt|;
block|}
DECL|method|setTaskExecutor (TaskExecutor taskExecutor)
specifier|public
name|void
name|setTaskExecutor
parameter_list|(
name|TaskExecutor
name|taskExecutor
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setTaskExecutor
argument_list|(
name|taskExecutor
argument_list|)
expr_stmt|;
block|}
DECL|method|setTimeToLive (long timeToLive)
specifier|public
name|void
name|setTimeToLive
parameter_list|(
name|long
name|timeToLive
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setTimeToLive
argument_list|(
name|timeToLive
argument_list|)
expr_stmt|;
block|}
DECL|method|setTransacted (boolean consumerTransacted)
specifier|public
name|void
name|setTransacted
parameter_list|(
name|boolean
name|consumerTransacted
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setTransacted
argument_list|(
name|consumerTransacted
argument_list|)
expr_stmt|;
block|}
DECL|method|setTransactionManager (PlatformTransactionManager transactionManager)
specifier|public
name|void
name|setTransactionManager
parameter_list|(
name|PlatformTransactionManager
name|transactionManager
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setTransactionManager
argument_list|(
name|transactionManager
argument_list|)
expr_stmt|;
block|}
DECL|method|setTransactionName (String transactionName)
specifier|public
name|void
name|setTransactionName
parameter_list|(
name|String
name|transactionName
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setTransactionName
argument_list|(
name|transactionName
argument_list|)
expr_stmt|;
block|}
DECL|method|setTransactionTimeout (int transactionTimeout)
specifier|public
name|void
name|setTransactionTimeout
parameter_list|(
name|int
name|transactionTimeout
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setTransactionTimeout
argument_list|(
name|transactionTimeout
argument_list|)
expr_stmt|;
block|}
DECL|method|setUseVersion102 (boolean useVersion102)
specifier|public
name|void
name|setUseVersion102
parameter_list|(
name|boolean
name|useVersion102
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setUseVersion102
argument_list|(
name|useVersion102
argument_list|)
expr_stmt|;
block|}
DECL|method|setJmsOperations (JmsOperations jmsOperations)
specifier|public
name|void
name|setJmsOperations
parameter_list|(
name|JmsOperations
name|jmsOperations
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setJmsOperations
argument_list|(
name|jmsOperations
argument_list|)
expr_stmt|;
block|}
DECL|method|setDestinationResolver (DestinationResolver destinationResolver)
specifier|public
name|void
name|setDestinationResolver
parameter_list|(
name|DestinationResolver
name|destinationResolver
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setDestinationResolver
argument_list|(
name|destinationResolver
argument_list|)
expr_stmt|;
block|}
DECL|method|getRequestor ()
specifier|public
specifier|synchronized
name|Requestor
name|getRequestor
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|requestor
operator|==
literal|null
condition|)
block|{
name|requestor
operator|=
operator|new
name|Requestor
argument_list|(
name|getConfiguration
argument_list|()
argument_list|,
name|getExecutorService
argument_list|()
argument_list|)
expr_stmt|;
name|requestor
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
return|return
name|requestor
return|;
block|}
DECL|method|setRequestor (Requestor requestor)
specifier|public
name|void
name|setRequestor
parameter_list|(
name|Requestor
name|requestor
parameter_list|)
block|{
name|this
operator|.
name|requestor
operator|=
name|requestor
expr_stmt|;
block|}
DECL|method|setApplicationContext (ApplicationContext applicationContext)
specifier|public
name|void
name|setApplicationContext
parameter_list|(
name|ApplicationContext
name|applicationContext
parameter_list|)
throws|throws
name|BeansException
block|{
name|this
operator|.
name|applicationContext
operator|=
name|applicationContext
expr_stmt|;
block|}
DECL|method|getQueueBrowseStrategy ()
specifier|public
name|QueueBrowseStrategy
name|getQueueBrowseStrategy
parameter_list|()
block|{
if|if
condition|(
name|queueBrowseStrategy
operator|==
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|attemptedToCreateQueueBrowserStrategy
condition|)
block|{
name|attemptedToCreateQueueBrowserStrategy
operator|=
literal|true
expr_stmt|;
try|try
block|{
name|queueBrowseStrategy
operator|=
name|tryCreateDefaultQueueBrowseStrategy
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Could not instantiate the QueueBrowseStrategy are you using Spring 2.0.x"
operator|+
literal|" by any chance? Error: "
operator|+
name|e
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|queueBrowseStrategy
return|;
block|}
DECL|method|setQueueBrowseStrategy (QueueBrowseStrategy queueBrowseStrategy)
specifier|public
name|void
name|setQueueBrowseStrategy
parameter_list|(
name|QueueBrowseStrategy
name|queueBrowseStrategy
parameter_list|)
block|{
name|this
operator|.
name|queueBrowseStrategy
operator|=
name|queueBrowseStrategy
expr_stmt|;
block|}
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
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
if|if
condition|(
name|requestor
operator|!=
literal|null
condition|)
block|{
name|requestor
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map parameters)
specifier|protected
name|Endpoint
argument_list|<
name|JmsExchange
argument_list|>
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|boolean
name|pubSubDomain
init|=
literal|false
decl_stmt|;
name|boolean
name|tempDestination
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|remaining
operator|.
name|startsWith
argument_list|(
name|JmsConfiguration
operator|.
name|QUEUE_PREFIX
argument_list|)
condition|)
block|{
name|pubSubDomain
operator|=
literal|false
expr_stmt|;
name|remaining
operator|=
name|removeStartingCharacters
argument_list|(
name|remaining
operator|.
name|substring
argument_list|(
name|JmsConfiguration
operator|.
name|QUEUE_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
argument_list|,
literal|'/'
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|remaining
operator|.
name|startsWith
argument_list|(
name|JmsConfiguration
operator|.
name|TOPIC_PREFIX
argument_list|)
condition|)
block|{
name|pubSubDomain
operator|=
literal|true
expr_stmt|;
name|remaining
operator|=
name|removeStartingCharacters
argument_list|(
name|remaining
operator|.
name|substring
argument_list|(
name|JmsConfiguration
operator|.
name|TOPIC_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
argument_list|,
literal|'/'
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|remaining
operator|.
name|startsWith
argument_list|(
name|JmsConfiguration
operator|.
name|TEMP_QUEUE_PREFIX
argument_list|)
condition|)
block|{
name|pubSubDomain
operator|=
literal|false
expr_stmt|;
name|tempDestination
operator|=
literal|true
expr_stmt|;
name|remaining
operator|=
name|removeStartingCharacters
argument_list|(
name|remaining
operator|.
name|substring
argument_list|(
name|JmsConfiguration
operator|.
name|TEMP_QUEUE_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
argument_list|,
literal|'/'
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|remaining
operator|.
name|startsWith
argument_list|(
name|JmsConfiguration
operator|.
name|TEMP_TOPIC_PREFIX
argument_list|)
condition|)
block|{
name|pubSubDomain
operator|=
literal|true
expr_stmt|;
name|tempDestination
operator|=
literal|true
expr_stmt|;
name|remaining
operator|=
name|removeStartingCharacters
argument_list|(
name|remaining
operator|.
name|substring
argument_list|(
name|JmsConfiguration
operator|.
name|TEMP_TOPIC_PREFIX
operator|.
name|length
argument_list|()
argument_list|)
argument_list|,
literal|'/'
argument_list|)
expr_stmt|;
block|}
specifier|final
name|String
name|subject
init|=
name|convertPathToActualDestination
argument_list|(
name|remaining
argument_list|,
name|parameters
argument_list|)
decl_stmt|;
comment|// lets make sure we copy the configuration as each endpoint can
comment|// customize its own version
name|JmsConfiguration
name|newConfiguration
init|=
name|getConfiguration
argument_list|()
operator|.
name|copy
argument_list|()
decl_stmt|;
name|JmsEndpoint
name|endpoint
decl_stmt|;
if|if
condition|(
name|pubSubDomain
condition|)
block|{
if|if
condition|(
name|tempDestination
condition|)
block|{
name|endpoint
operator|=
operator|new
name|JmsTemporaryTopicEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|subject
argument_list|,
name|newConfiguration
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|endpoint
operator|=
operator|new
name|JmsEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|subject
argument_list|,
name|pubSubDomain
argument_list|,
name|newConfiguration
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|QueueBrowseStrategy
name|strategy
init|=
name|getQueueBrowseStrategy
argument_list|()
decl_stmt|;
if|if
condition|(
name|tempDestination
condition|)
block|{
name|endpoint
operator|=
operator|new
name|JmsTemporaryQueueEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|subject
argument_list|,
name|newConfiguration
argument_list|,
name|strategy
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|endpoint
operator|=
operator|new
name|JmsQueueEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|subject
argument_list|,
name|newConfiguration
argument_list|,
name|strategy
argument_list|)
expr_stmt|;
block|}
block|}
name|String
name|selector
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"selector"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|selector
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setSelector
argument_list|(
name|selector
argument_list|)
expr_stmt|;
block|}
name|setProperties
argument_list|(
name|endpoint
operator|.
name|getConfiguration
argument_list|()
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
comment|/**      * A strategy method allowing the URI destination to be translated into the      * actual JMS destination name (say by looking up in JNDI or something)      */
DECL|method|convertPathToActualDestination (String path, Map parameters)
specifier|protected
name|String
name|convertPathToActualDestination
parameter_list|(
name|String
name|path
parameter_list|,
name|Map
name|parameters
parameter_list|)
block|{
return|return
name|path
return|;
block|}
comment|/**      * Factory method to create the default configuration instance      *      * @return a newly created configuration object which can then be further      *         customized      */
DECL|method|createConfiguration ()
specifier|protected
name|JmsConfiguration
name|createConfiguration
parameter_list|()
block|{
return|return
operator|new
name|JmsConfiguration
argument_list|()
return|;
block|}
comment|/**      * Attempts to instantiate the default {@link QueueBrowseStrategy} which      * should work fine if Spring 2.5.x or later is on the classpath but this      * will fail if 2.0.x are on the classpath. We can continue to operate on      * this version we just cannot support the browseable queues supported by      * {@link JmsQueueEndpoint}      *      * @return the queue browse strategy or null if it cannot be supported      */
DECL|method|tryCreateDefaultQueueBrowseStrategy ()
specifier|protected
specifier|static
name|QueueBrowseStrategy
name|tryCreateDefaultQueueBrowseStrategy
parameter_list|()
block|{
comment|// lets try instantiate the default implementation
name|Class
argument_list|<
name|?
argument_list|>
name|type
init|=
name|ObjectHelper
operator|.
name|loadClass
argument_list|(
name|DEFAULT_QUEUE_BROWSE_STRATEGY
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Could not load class: "
operator|+
name|DEFAULT_QUEUE_BROWSE_STRATEGY
operator|+
literal|" maybe you are on Spring 2.0.x?"
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
else|else
block|{
return|return
operator|(
name|QueueBrowseStrategy
operator|)
name|ObjectHelper
operator|.
name|newInstance
argument_list|(
name|type
argument_list|)
return|;
block|}
block|}
DECL|method|getHeaderFilterStrategy ()
specifier|public
name|HeaderFilterStrategy
name|getHeaderFilterStrategy
parameter_list|()
block|{
return|return
name|headerFilterStrategy
return|;
block|}
DECL|method|setHeaderFilterStrategy (HeaderFilterStrategy strategy)
specifier|public
name|void
name|setHeaderFilterStrategy
parameter_list|(
name|HeaderFilterStrategy
name|strategy
parameter_list|)
block|{
name|this
operator|.
name|headerFilterStrategy
operator|=
name|strategy
expr_stmt|;
block|}
block|}
end_class

end_unit

