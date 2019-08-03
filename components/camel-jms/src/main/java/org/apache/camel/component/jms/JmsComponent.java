begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Message
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
name|LoggingLevel
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
name|Metadata
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
name|annotations
operator|.
name|Component
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
name|HeaderFilterStrategyComponent
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
name|connection
operator|.
name|UserCredentialsConnectionFactoryAdapter
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
import|import
name|org
operator|.
name|springframework
operator|.
name|util
operator|.
name|ErrorHandler
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
name|StringHelper
operator|.
name|removeStartingCharacters
import|;
end_import

begin_comment
comment|/**  * A<a href="http://activemq.apache.org/jms.html">JMS Component</a>  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
literal|"jms"
argument_list|)
DECL|class|JmsComponent
specifier|public
class|class
name|JmsComponent
extends|extends
name|HeaderFilterStrategyComponent
implements|implements
name|ApplicationContextAware
block|{
DECL|field|KEY_FORMAT_STRATEGY_PARAM
specifier|private
specifier|static
specifier|final
name|String
name|KEY_FORMAT_STRATEGY_PARAM
init|=
literal|"jmsKeyFormatStrategy"
decl_stmt|;
DECL|field|asyncStartStopExecutorService
specifier|private
name|ExecutorService
name|asyncStartStopExecutorService
decl_stmt|;
DECL|field|applicationContext
specifier|private
name|ApplicationContext
name|applicationContext
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"To use a shared JMS configuration"
argument_list|)
DECL|field|configuration
specifier|private
name|JmsConfiguration
name|configuration
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"To use a custom QueueBrowseStrategy when browsing queues"
argument_list|)
DECL|field|queueBrowseStrategy
specifier|private
name|QueueBrowseStrategy
name|queueBrowseStrategy
decl_stmt|;
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"To use the given MessageCreatedStrategy which are invoked when Camel creates new instances"
operator|+
literal|" of javax.jms.Message objects when Camel is sending a JMS message."
argument_list|)
DECL|field|messageCreatedStrategy
specifier|private
name|MessageCreatedStrategy
name|messageCreatedStrategy
decl_stmt|;
DECL|method|JmsComponent ()
specifier|public
name|JmsComponent
parameter_list|()
block|{     }
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
argument_list|()
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
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
if|if
condition|(
name|isAllowAutoWiredConnectionFactory
argument_list|()
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|ConnectionFactory
argument_list|>
name|beansOfTypeConnectionFactory
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
name|beansOfTypeConnectionFactory
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|ConnectionFactory
name|cf
init|=
name|beansOfTypeConnectionFactory
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
block|}
if|if
condition|(
name|isAllowAutoWiredDestinationResolver
argument_list|()
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|DestinationResolver
argument_list|>
name|beansOfTypeDestinationResolver
init|=
name|applicationContext
operator|.
name|getBeansOfType
argument_list|(
name|DestinationResolver
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|beansOfTypeDestinationResolver
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|DestinationResolver
name|destinationResolver
init|=
name|beansOfTypeDestinationResolver
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
block|}
return|return
name|configuration
return|;
block|}
comment|/**      * Subclasses can override to prevent the jms configuration from being      * setup to use an auto-wired the connection factory that's found in the spring      * application context.      *      * @return true by default      */
DECL|method|isAllowAutoWiredConnectionFactory ()
specifier|public
name|boolean
name|isAllowAutoWiredConnectionFactory
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
comment|/**      * Subclasses can override to prevent the jms configuration from being      * setup to use an auto-wired the destination resolved that's found in the spring      * application context.      *      * @return true by default      */
DECL|method|isAllowAutoWiredDestinationResolver ()
specifier|public
name|boolean
name|isAllowAutoWiredDestinationResolver
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
comment|/**      * To use a shared JMS configuration      */
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
comment|/**      * Specifies whether the consumer accept messages while it is stopping.      * You may consider enabling this option, if you start and stop JMS routes at runtime, while there are still messages      * enqueued on the queue. If this option is false, and you stop the JMS route, then messages may be rejected,      * and the JMS broker would have to attempt redeliveries, which yet again may be rejected, and eventually the message      * may be moved at a dead letter queue on the JMS broker. To avoid this its recommended to enable this option.      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"consumer,advanced"
argument_list|,
name|description
operator|=
literal|"Specifies whether the consumer accept messages while it is stopping."
operator|+
literal|" You may consider enabling this option, if you start and stop JMS routes at runtime, while there are still messages"
operator|+
literal|" enqueued on the queue. If this option is false, and you stop the JMS route, then messages may be rejected,"
operator|+
literal|" and the JMS broker would have to attempt redeliveries, which yet again may be rejected, and eventually the message"
operator|+
literal|" may be moved at a dead letter queue on the JMS broker. To avoid this its recommended to enable this option."
argument_list|)
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
comment|/**      * Whether the DefaultMessageListenerContainer used in the reply managers for request-reply messaging allow       * the DefaultMessageListenerContainer.runningAllowed flag to quick stop in case JmsConfiguration#isAcceptMessagesWhileStopping      * is enabled, and org.apache.camel.CamelContext is currently being stopped. This quick stop ability is enabled by      * default in the regular JMS consumers but to enable for reply managers you must enable this flag.       */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"consumer,advanced"
argument_list|,
name|description
operator|=
literal|"Whether the DefaultMessageListenerContainer used in the reply managers for request-reply messaging allow "
operator|+
literal|" the DefaultMessageListenerContainer.runningAllowed flag to quick stop in case JmsConfiguration#isAcceptMessagesWhileStopping"
operator|+
literal|" is enabled, and org.apache.camel.CamelContext is currently being stopped. This quick stop ability is enabled by"
operator|+
literal|" default in the regular JMS consumers but to enable for reply managers you must enable this flag."
argument_list|)
DECL|method|setAllowReplyManagerQuickStop (boolean allowReplyManagerQuickStop)
specifier|public
name|void
name|setAllowReplyManagerQuickStop
parameter_list|(
name|boolean
name|allowReplyManagerQuickStop
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setAllowReplyManagerQuickStop
argument_list|(
name|allowReplyManagerQuickStop
argument_list|)
expr_stmt|;
block|}
comment|/**      * The JMS acknowledgement mode defined as an Integer.      * Allows you to set vendor-specific extensions to the acknowledgment mode.      * For the regular modes, it is preferable to use the acknowledgementModeName instead.      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|description
operator|=
literal|"The JMS acknowledgement mode defined as an Integer. Allows you to set vendor-specific extensions to the acknowledgment mode."
operator|+
literal|" For the regular modes, it is preferable to use the acknowledgementModeName instead."
argument_list|)
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
comment|/**      * Enables eager loading of JMS properties as soon as a message is loaded      * which generally is inefficient as the JMS properties may not be required      * but sometimes can catch early any issues with the underlying JMS provider      * and the use of JMS properties      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"consumer,advanced"
argument_list|,
name|description
operator|=
literal|"Enables eager loading of JMS properties as soon as a message is loaded"
operator|+
literal|" which generally is inefficient as the JMS properties may not be required"
operator|+
literal|" but sometimes can catch early any issues with the underlying JMS provider"
operator|+
literal|" and the use of JMS properties"
argument_list|)
DECL|method|setEagerLoadingOfProperties (boolean eagerLoadingOfProperties)
specifier|public
name|void
name|setEagerLoadingOfProperties
parameter_list|(
name|boolean
name|eagerLoadingOfProperties
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setEagerLoadingOfProperties
argument_list|(
name|eagerLoadingOfProperties
argument_list|)
expr_stmt|;
block|}
comment|/**      * The JMS acknowledgement name, which is one of: SESSION_TRANSACTED, CLIENT_ACKNOWLEDGE, AUTO_ACKNOWLEDGE, DUPS_OK_ACKNOWLEDGE      */
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"AUTO_ACKNOWLEDGE"
argument_list|,
name|label
operator|=
literal|"consumer"
argument_list|,
name|enums
operator|=
literal|"SESSION_TRANSACTED,CLIENT_ACKNOWLEDGE,AUTO_ACKNOWLEDGE,DUPS_OK_ACKNOWLEDGE"
argument_list|,
name|description
operator|=
literal|"The JMS acknowledgement name, which is one of: SESSION_TRANSACTED, CLIENT_ACKNOWLEDGE, AUTO_ACKNOWLEDGE, DUPS_OK_ACKNOWLEDGE"
argument_list|)
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
comment|/**      * Specifies whether the consumer container should auto-startup.      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|,
name|description
operator|=
literal|"Specifies whether the consumer container should auto-startup."
argument_list|)
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
comment|/**      * Sets the cache level by ID for the underlying JMS resources. See cacheLevelName option for more details.      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|description
operator|=
literal|"Sets the cache level by ID for the underlying JMS resources. See cacheLevelName option for more details."
argument_list|)
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
comment|/**      * Sets the cache level by name for the underlying JMS resources.      * Possible values are: CACHE_AUTO, CACHE_CONNECTION, CACHE_CONSUMER, CACHE_NONE, and CACHE_SESSION.      * The default setting is CACHE_AUTO. See the Spring documentation and Transactions Cache Levels for more information.      */
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"CACHE_AUTO"
argument_list|,
name|label
operator|=
literal|"consumer"
argument_list|,
name|enums
operator|=
literal|"CACHE_AUTO,CACHE_CONNECTION,CACHE_CONSUMER,CACHE_NONE,CACHE_SESSION"
argument_list|,
name|description
operator|=
literal|"Sets the cache level by name for the underlying JMS resources."
operator|+
literal|" Possible values are: CACHE_AUTO, CACHE_CONNECTION, CACHE_CONSUMER, CACHE_NONE, and CACHE_SESSION."
operator|+
literal|" The default setting is CACHE_AUTO. See the Spring documentation and Transactions Cache Levels for more information."
argument_list|)
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
comment|/**      * Sets the cache level by name for the reply consumer when doing request/reply over JMS.      * This option only applies when using fixed reply queues (not temporary).      * Camel will by default use: CACHE_CONSUMER for exclusive or shared w/ replyToSelectorName.      * And CACHE_SESSION for shared without replyToSelectorName. Some JMS brokers such as IBM WebSphere      * may require to set the replyToCacheLevelName=CACHE_NONE to work.      * Note: If using temporary queues then CACHE_NONE is not allowed,      * and you must use a higher value such as CACHE_CONSUMER or CACHE_SESSION.      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"producer,advanced"
argument_list|,
name|enums
operator|=
literal|"CACHE_AUTO,CACHE_CONNECTION,CACHE_CONSUMER,CACHE_NONE,CACHE_SESSION"
argument_list|,
name|description
operator|=
literal|"Sets the cache level by name for the reply consumer when doing request/reply over JMS."
operator|+
literal|" This option only applies when using fixed reply queues (not temporary)."
operator|+
literal|" Camel will by default use: CACHE_CONSUMER for exclusive or shared w/ replyToSelectorName."
operator|+
literal|" And CACHE_SESSION for shared without replyToSelectorName. Some JMS brokers such as IBM WebSphere"
operator|+
literal|" may require to set the replyToCacheLevelName=CACHE_NONE to work."
operator|+
literal|" Note: If using temporary queues then CACHE_NONE is not allowed,"
operator|+
literal|" and you must use a higher value such as CACHE_CONSUMER or CACHE_SESSION."
argument_list|)
DECL|method|setReplyToCacheLevelName (String cacheName)
specifier|public
name|void
name|setReplyToCacheLevelName
parameter_list|(
name|String
name|cacheName
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setReplyToCacheLevelName
argument_list|(
name|cacheName
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the JMS client ID to use. Note that this value, if specified, must be unique and can only be used by a single JMS connection instance.      * It is typically only required for durable topic subscriptions.      *<p/>      * If using Apache ActiveMQ you may prefer to use Virtual Topics instead.      */
annotation|@
name|Metadata
argument_list|(
name|description
operator|=
literal|"Sets the JMS client ID to use. Note that this value, if specified, must be unique and can only be used by a single JMS connection instance."
operator|+
literal|" It is typically only required for durable topic subscriptions."
operator|+
literal|" If using Apache ActiveMQ you may prefer to use Virtual Topics instead."
argument_list|)
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
comment|/**      * Specifies the default number of concurrent consumers when consuming from JMS (not for request/reply over JMS).      * See also the maxMessagesPerTask option to control dynamic scaling up/down of threads.      *<p/>      * When doing request/reply over JMS then the option replyToConcurrentConsumers is used to control number      * of concurrent consumers on the reply message listener.      */
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"1"
argument_list|,
name|label
operator|=
literal|"consumer"
argument_list|,
name|description
operator|=
literal|"Specifies the default number of concurrent consumers when consuming from JMS (not for request/reply over JMS)."
operator|+
literal|" See also the maxMessagesPerTask option to control dynamic scaling up/down of threads."
operator|+
literal|" When doing request/reply over JMS then the option replyToConcurrentConsumers is used to control number"
operator|+
literal|" of concurrent consumers on the reply message listener."
argument_list|)
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
comment|/**      * Specifies the default number of concurrent consumers when doing request/reply over JMS.      * See also the maxMessagesPerTask option to control dynamic scaling up/down of threads.      */
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"1"
argument_list|,
name|label
operator|=
literal|"producer"
argument_list|,
name|description
operator|=
literal|"Specifies the default number of concurrent consumers when doing request/reply over JMS."
operator|+
literal|" See also the maxMessagesPerTask option to control dynamic scaling up/down of threads."
argument_list|)
DECL|method|setReplyToConcurrentConsumers (int concurrentConsumers)
specifier|public
name|void
name|setReplyToConcurrentConsumers
parameter_list|(
name|int
name|concurrentConsumers
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setReplyToConcurrentConsumers
argument_list|(
name|concurrentConsumers
argument_list|)
expr_stmt|;
block|}
comment|/**      * Gets the connection factory to be used.      */
DECL|method|getConnectionFactory ()
specifier|public
name|ConnectionFactory
name|getConnectionFactory
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getConnectionFactory
argument_list|()
return|;
block|}
comment|/**      * The connection factory to be use. A connection factory must be configured either on the component or endpoint.      */
annotation|@
name|Metadata
argument_list|(
name|description
operator|=
literal|"The connection factory to be use. A connection factory must be configured either on the component or endpoint."
argument_list|)
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
comment|/**      * Username to use with the ConnectionFactory. You can also configure username/password directly on the ConnectionFactory.      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|,
name|description
operator|=
literal|"Username to use with the ConnectionFactory. You can also configure username/password directly on the ConnectionFactory."
argument_list|)
DECL|method|setUsername (String username)
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setUsername
argument_list|(
name|username
argument_list|)
expr_stmt|;
block|}
comment|/**      * Password to use with the ConnectionFactory. You can also configure username/password directly on the ConnectionFactory.      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|,
name|description
operator|=
literal|"Password to use with the ConnectionFactory. You can also configure username/password directly on the ConnectionFactory."
argument_list|)
DECL|method|setPassword (String password)
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setPassword
argument_list|(
name|password
argument_list|)
expr_stmt|;
block|}
comment|/**      * Specifies whether persistent delivery is used by default.      */
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|,
name|label
operator|=
literal|"producer"
argument_list|,
name|description
operator|=
literal|"Specifies whether persistent delivery is used by default."
argument_list|)
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
comment|/**      * Specifies the delivery mode to be used.      * Possibles values are those defined by javax.jms.DeliveryMode.      * NON_PERSISTENT = 1 and PERSISTENT = 2.      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|enums
operator|=
literal|"1,2"
argument_list|,
name|description
operator|=
literal|"Specifies the delivery mode to be used."
operator|+
literal|" Possibles values are those defined by javax.jms.DeliveryMode."
operator|+
literal|" NON_PERSISTENT = 1 and PERSISTENT = 2."
argument_list|)
DECL|method|setDeliveryMode (Integer deliveryMode)
specifier|public
name|void
name|setDeliveryMode
parameter_list|(
name|Integer
name|deliveryMode
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setDeliveryMode
argument_list|(
name|deliveryMode
argument_list|)
expr_stmt|;
block|}
comment|/**      * The durable subscriber name for specifying durable topic subscriptions. The clientId option must be configured as well.      */
annotation|@
name|Metadata
argument_list|(
name|description
operator|=
literal|"The durable subscriber name for specifying durable topic subscriptions. The clientId option must be configured as well."
argument_list|)
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
comment|/**      * Specifies the JMS Exception Listener that is to be notified of any underlying JMS exceptions.      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"Specifies the JMS Exception Listener that is to be notified of any underlying JMS exceptions."
argument_list|)
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
comment|/**      * Specifies a org.springframework.util.ErrorHandler to be invoked in case of any uncaught exceptions thrown while processing a Message.      * By default these exceptions will be logged at the WARN level, if no errorHandler has been configured.      * You can configure logging level and whether stack traces should be logged using errorHandlerLoggingLevel and errorHandlerLogStackTrace options.      * This makes it much easier to configure, than having to code a custom errorHandler.      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"Specifies a org.springframework.util.ErrorHandler to be invoked in case of any uncaught exceptions thrown while processing a Message."
operator|+
literal|" By default these exceptions will be logged at the WARN level, if no errorHandler has been configured."
operator|+
literal|" You can configure logging level and whether stack traces should be logged using errorHandlerLoggingLevel and errorHandlerLogStackTrace options."
operator|+
literal|" This makes it much easier to configure, than having to code a custom errorHandler."
argument_list|)
DECL|method|setErrorHandler (ErrorHandler errorHandler)
specifier|public
name|void
name|setErrorHandler
parameter_list|(
name|ErrorHandler
name|errorHandler
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setErrorHandler
argument_list|(
name|errorHandler
argument_list|)
expr_stmt|;
block|}
comment|/**      * Allows to configure the default errorHandler logging level for logging uncaught exceptions.      */
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"WARN"
argument_list|,
name|label
operator|=
literal|"consumer,logging"
argument_list|,
name|description
operator|=
literal|"Allows to configure the default errorHandler logging level for logging uncaught exceptions."
argument_list|)
DECL|method|setErrorHandlerLoggingLevel (LoggingLevel errorHandlerLoggingLevel)
specifier|public
name|void
name|setErrorHandlerLoggingLevel
parameter_list|(
name|LoggingLevel
name|errorHandlerLoggingLevel
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setErrorHandlerLoggingLevel
argument_list|(
name|errorHandlerLoggingLevel
argument_list|)
expr_stmt|;
block|}
comment|/**      * Allows to control whether stacktraces should be logged or not, by the default errorHandler.      */
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|,
name|label
operator|=
literal|"consumer,logging"
argument_list|,
name|description
operator|=
literal|"Allows to control whether stacktraces should be logged or not, by the default errorHandler."
argument_list|)
DECL|method|setErrorHandlerLogStackTrace (boolean errorHandlerLogStackTrace)
specifier|public
name|void
name|setErrorHandlerLogStackTrace
parameter_list|(
name|boolean
name|errorHandlerLogStackTrace
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setErrorHandlerLogStackTrace
argument_list|(
name|errorHandlerLogStackTrace
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set if the deliveryMode, priority or timeToLive qualities of service should be used when sending messages.      * This option is based on Spring's JmsTemplate. The deliveryMode, priority and timeToLive options are applied to the current endpoint.      * This contrasts with the preserveMessageQos option, which operates at message granularity,      * reading QoS properties exclusively from the Camel In message headers.      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"false"
argument_list|,
name|description
operator|=
literal|"Set if the deliveryMode, priority or timeToLive qualities of service should be used when sending messages."
operator|+
literal|" This option is based on Spring's JmsTemplate. The deliveryMode, priority and timeToLive options are applied to the current endpoint."
operator|+
literal|" This contrasts with the preserveMessageQos option, which operates at message granularity,"
operator|+
literal|" reading QoS properties exclusively from the Camel In message headers."
argument_list|)
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
comment|/**      * Specifies whether the listener session should be exposed when consuming messages.      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"consumer,advanced"
argument_list|,
name|description
operator|=
literal|"Specifies whether the listener session should be exposed when consuming messages."
argument_list|)
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
comment|/**      * Specifies the limit for idle executions of a receive task, not having received any message within its execution.      * If this limit is reached, the task will shut down and leave receiving to other executing tasks      * (in the case of dynamic scheduling; see the maxConcurrentConsumers setting).      * There is additional doc available from Spring.      */
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"1"
argument_list|,
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"Specifies the limit for idle executions of a receive task, not having received any message within its execution."
operator|+
literal|" If this limit is reached, the task will shut down and leave receiving to other executing tasks"
operator|+
literal|" (in the case of dynamic scheduling; see the maxConcurrentConsumers setting)."
operator|+
literal|" There is additional doc available from Spring."
argument_list|)
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
comment|/**      * Specify the limit for the number of consumers that are allowed to be idle at any given time.      */
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"1"
argument_list|,
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"Specify the limit for the number of consumers that are allowed to be idle at any given time."
argument_list|)
DECL|method|setIdleConsumerLimit (int idleConsumerLimit)
specifier|public
name|void
name|setIdleConsumerLimit
parameter_list|(
name|int
name|idleConsumerLimit
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setIdleConsumerLimit
argument_list|(
name|idleConsumerLimit
argument_list|)
expr_stmt|;
block|}
comment|/**      * Specifies the maximum number of concurrent consumers when consuming from JMS (not for request/reply over JMS).      * See also the maxMessagesPerTask option to control dynamic scaling up/down of threads.      *<p/>      * When doing request/reply over JMS then the option replyToMaxConcurrentConsumers is used to control number      * of concurrent consumers on the reply message listener.      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|description
operator|=
literal|"Specifies the maximum number of concurrent consumers when consuming from JMS (not for request/reply over JMS)."
operator|+
literal|" See also the maxMessagesPerTask option to control dynamic scaling up/down of threads."
operator|+
literal|" When doing request/reply over JMS then the option replyToMaxConcurrentConsumers is used to control number"
operator|+
literal|" of concurrent consumers on the reply message listener."
argument_list|)
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
comment|/**      * Specifies the maximum number of concurrent consumers when using request/reply over JMS.      * See also the maxMessagesPerTask option to control dynamic scaling up/down of threads.      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|description
operator|=
literal|"Specifies the maximum number of concurrent consumers when using request/reply over JMS."
operator|+
literal|" See also the maxMessagesPerTask option to control dynamic scaling up/down of threads."
argument_list|)
DECL|method|setReplyToMaxConcurrentConsumers (int maxConcurrentConsumers)
specifier|public
name|void
name|setReplyToMaxConcurrentConsumers
parameter_list|(
name|int
name|maxConcurrentConsumers
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setReplyToMaxConcurrentConsumers
argument_list|(
name|maxConcurrentConsumers
argument_list|)
expr_stmt|;
block|}
comment|/**      * Specifies the maximum number of concurrent consumers for continue routing when timeout occurred when using request/reply over JMS.      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"1"
argument_list|,
name|description
operator|=
literal|"Specifies the maximum number of concurrent consumers for continue routing when timeout occurred when using request/reply over JMS."
argument_list|)
DECL|method|setReplyOnTimeoutToMaxConcurrentConsumers (int maxConcurrentConsumers)
specifier|public
name|void
name|setReplyOnTimeoutToMaxConcurrentConsumers
parameter_list|(
name|int
name|maxConcurrentConsumers
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setReplyToOnTimeoutMaxConcurrentConsumers
argument_list|(
name|maxConcurrentConsumers
argument_list|)
expr_stmt|;
block|}
comment|/**      * The number of messages per task. -1 is unlimited.      * If you use a range for concurrent consumers (eg min< max), then this option can be used to set      * a value to eg 100 to control how fast the consumers will shrink when less work is required.      */
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"-1"
argument_list|,
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"The number of messages per task. -1 is unlimited."
operator|+
literal|" If you use a range for concurrent consumers (eg min< max), then this option can be used to set"
operator|+
literal|" a value to eg 100 to control how fast the consumers will shrink when less work is required."
argument_list|)
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
comment|/**      * To use a custom Spring org.springframework.jms.support.converter.MessageConverter so you can be in control      * how to map to/from a javax.jms.Message.      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"To use a custom Spring org.springframework.jms.support.converter.MessageConverter so you can be in control how to map to/from a javax.jms.Message."
argument_list|)
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
comment|/**      * Specifies whether Camel should auto map the received JMS message to a suited payload type, such as javax.jms.TextMessage to a String etc.      * See section about how mapping works below for more details.      */
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|,
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"Specifies whether Camel should auto map the received JMS message to a suited payload type, such as javax.jms.TextMessage to a String etc."
argument_list|)
DECL|method|setMapJmsMessage (boolean mapJmsMessage)
specifier|public
name|void
name|setMapJmsMessage
parameter_list|(
name|boolean
name|mapJmsMessage
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setMapJmsMessage
argument_list|(
name|mapJmsMessage
argument_list|)
expr_stmt|;
block|}
comment|/**      * When sending, specifies whether message IDs should be added. This is just an hint to the JMS Broker.      * If the JMS provider accepts this hint, these messages must have the message ID set to null; if the provider ignores the hint, the message ID must be set to its normal unique value.      */
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|,
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"When sending, specifies whether message IDs should be added. This is just an hint to the JMS broker."
operator|+
literal|" If the JMS provider accepts this hint, these messages must have the message ID set to null; if the provider ignores the hint, "
operator|+
literal|"the message ID must be set to its normal unique value."
argument_list|)
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
comment|/**      * Specifies whether timestamps should be enabled by default on sending messages. This is just an hint to the JMS broker.      * If the JMS provider accepts this hint, these messages must have the timestamp set to zero;      * if the provider ignores the hint the timestamp must be set to its normal value.      */
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|,
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"Specifies whether timestamps should be enabled by default on sending messages. This is just an hint to the JMS broker."
operator|+
literal|" If the JMS provider accepts this hint, these messages must have the timestamp set to zero; if the provider ignores the hint "
operator|+
literal|"the timestamp must be set to its normal value."
argument_list|)
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
comment|/**      * If true, Camel will always make a JMS message copy of the message when it is passed to the producer for sending.      * Copying the message is needed in some situations, such as when a replyToDestinationSelectorName is set      * (incidentally, Camel will set the alwaysCopyMessage option to true, if a replyToDestinationSelectorName is set).      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"producer,advanced"
argument_list|,
name|description
operator|=
literal|"If true, Camel will always make a JMS message copy of the message when it is passed to the producer for sending."
operator|+
literal|" Copying the message is needed in some situations, such as when a replyToDestinationSelectorName is set"
operator|+
literal|" (incidentally, Camel will set the alwaysCopyMessage option to true, if a replyToDestinationSelectorName is set)."
argument_list|)
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
comment|/**      * Specifies whether JMSMessageID should always be used as JMSCorrelationID for InOut messages.      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"Specifies whether JMSMessageID should always be used as JMSCorrelationID for InOut messages."
argument_list|)
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
comment|/**      * Values greater than 1 specify the message priority when sending (where 0 is the lowest priority and 9 is the highest).      * The explicitQosEnabled option must also be enabled in order for this option to have any effect.      */
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|""
operator|+
name|Message
operator|.
name|DEFAULT_PRIORITY
argument_list|,
name|enums
operator|=
literal|"1,2,3,4,5,6,7,8,9"
argument_list|,
name|label
operator|=
literal|"producer"
argument_list|,
name|description
operator|=
literal|"Values greater than 1 specify the message priority when sending (where 0 is the lowest priority and 9 is the highest)."
operator|+
literal|" The explicitQosEnabled option must also be enabled in order for this option to have any effect."
argument_list|)
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
comment|/**      * Specifies whether to inhibit the delivery of messages published by its own connection.      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"Specifies whether to inhibit the delivery of messages published by its own connection."
argument_list|)
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
comment|/**      * The timeout for receiving messages (in milliseconds).      */
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"1000"
argument_list|,
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"The timeout for receiving messages (in milliseconds)."
argument_list|)
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
comment|/**      * Specifies the interval between recovery attempts, i.e. when a connection is being refreshed, in milliseconds.      * The default is 5000 ms, that is, 5 seconds.      */
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"5000"
argument_list|,
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"Specifies the interval between recovery attempts, i.e. when a connection is being refreshed, in milliseconds."
operator|+
literal|" The default is 5000 ms, that is, 5 seconds."
argument_list|)
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
comment|/**      * Allows you to specify a custom task executor for consuming messages.      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"consumer,advanced"
argument_list|,
name|description
operator|=
literal|"Allows you to specify a custom task executor for consuming messages."
argument_list|)
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
comment|/**      * When sending messages, specifies the time-to-live of the message (in milliseconds).      */
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"-1"
argument_list|,
name|label
operator|=
literal|"producer"
argument_list|,
name|description
operator|=
literal|"When sending messages, specifies the time-to-live of the message (in milliseconds)."
argument_list|)
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
comment|/**      * Specifies whether to use transacted mode      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"transaction"
argument_list|,
name|description
operator|=
literal|"Specifies whether to use transacted mode"
argument_list|)
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
comment|/**      * If true, Camel will create a JmsTransactionManager, if there is no transactionManager injected when option transacted=true.      */
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|,
name|label
operator|=
literal|"transaction,advanced"
argument_list|,
name|description
operator|=
literal|"If true, Camel will create a JmsTransactionManager, if there is no transactionManager injected when option transacted=true."
argument_list|)
DECL|method|setLazyCreateTransactionManager (boolean lazyCreating)
specifier|public
name|void
name|setLazyCreateTransactionManager
parameter_list|(
name|boolean
name|lazyCreating
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setLazyCreateTransactionManager
argument_list|(
name|lazyCreating
argument_list|)
expr_stmt|;
block|}
comment|/**      * The Spring transaction manager to use.      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"transaction,advanced"
argument_list|,
name|description
operator|=
literal|"The Spring transaction manager to use."
argument_list|)
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
comment|/**      * The name of the transaction to use.      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"transaction,advanced"
argument_list|,
name|description
operator|=
literal|"The name of the transaction to use."
argument_list|)
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
comment|/**      * The timeout value of the transaction (in seconds), if using transacted mode.      */
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"-1"
argument_list|,
name|label
operator|=
literal|"transaction,advanced"
argument_list|,
name|description
operator|=
literal|"The timeout value of the transaction (in seconds), if using transacted mode."
argument_list|)
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
comment|/**      * Specifies whether to test the connection on startup.      * This ensures that when Camel starts that all the JMS consumers have a valid connection to the JMS broker.      * If a connection cannot be granted then Camel throws an exception on startup.      * This ensures that Camel is not started with failed connections.      * The JMS producers is tested as well.      */
annotation|@
name|Metadata
argument_list|(
name|description
operator|=
literal|"Specifies whether to test the connection on startup."
operator|+
literal|" This ensures that when Camel starts that all the JMS consumers have a valid connection to the JMS broker."
operator|+
literal|" If a connection cannot be granted then Camel throws an exception on startup."
operator|+
literal|" This ensures that Camel is not started with failed connections."
operator|+
literal|" The JMS producers is tested as well."
argument_list|)
DECL|method|setTestConnectionOnStartup (boolean testConnectionOnStartup)
specifier|public
name|void
name|setTestConnectionOnStartup
parameter_list|(
name|boolean
name|testConnectionOnStartup
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setTestConnectionOnStartup
argument_list|(
name|testConnectionOnStartup
argument_list|)
expr_stmt|;
block|}
comment|/**      * Whether to startup the JmsConsumer message listener asynchronously, when starting a route.      * For example if a JmsConsumer cannot get a connection to a remote JMS broker, then it may block while retrying      * and/or failover. This will cause Camel to block while starting routes. By setting this option to true,      * you will let routes startup, while the JmsConsumer connects to the JMS broker using a dedicated thread      * in asynchronous mode. If this option is used, then beware that if the connection could not be established,      * then an exception is logged at WARN level, and the consumer will not be able to receive messages;      * You can then restart the route to retry.      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"Whether to startup the JmsConsumer message listener asynchronously, when starting a route."
operator|+
literal|" For example if a JmsConsumer cannot get a connection to a remote JMS broker, then it may block while retrying"
operator|+
literal|" and/or failover. This will cause Camel to block while starting routes. By setting this option to true,"
operator|+
literal|" you will let routes startup, while the JmsConsumer connects to the JMS broker using a dedicated thread"
operator|+
literal|" in asynchronous mode. If this option is used, then beware that if the connection could not be established,"
operator|+
literal|" then an exception is logged at WARN level, and the consumer will not be able to receive messages;"
operator|+
literal|" You can then restart the route to retry."
argument_list|)
DECL|method|setAsyncStartListener (boolean asyncStartListener)
specifier|public
name|void
name|setAsyncStartListener
parameter_list|(
name|boolean
name|asyncStartListener
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setAsyncStartListener
argument_list|(
name|asyncStartListener
argument_list|)
expr_stmt|;
block|}
comment|/**      * Whether to stop the JmsConsumer message listener asynchronously, when stopping a route.      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"Whether to stop the JmsConsumer message listener asynchronously, when stopping a route."
argument_list|)
DECL|method|setAsyncStopListener (boolean asyncStopListener)
specifier|public
name|void
name|setAsyncStopListener
parameter_list|(
name|boolean
name|asyncStopListener
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setAsyncStopListener
argument_list|(
name|asyncStopListener
argument_list|)
expr_stmt|;
block|}
comment|/**      * When using mapJmsMessage=false Camel will create a new JMS message to send to a new JMS destination      * if you touch the headers (get or set) during the route. Set this option to true to force Camel to send      * the original JMS message that was received.      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"producer,advanced"
argument_list|,
name|description
operator|=
literal|"When using mapJmsMessage=false Camel will create a new JMS message to send to a new JMS destination"
operator|+
literal|" if you touch the headers (get or set) during the route. Set this option to true to force Camel to send"
operator|+
literal|" the original JMS message that was received."
argument_list|)
DECL|method|setForceSendOriginalMessage (boolean forceSendOriginalMessage)
specifier|public
name|void
name|setForceSendOriginalMessage
parameter_list|(
name|boolean
name|forceSendOriginalMessage
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setForceSendOriginalMessage
argument_list|(
name|forceSendOriginalMessage
argument_list|)
expr_stmt|;
block|}
comment|/**      * The timeout for waiting for a reply when using the InOut Exchange Pattern (in milliseconds).      * The default is 20 seconds. You can include the header "CamelJmsRequestTimeout" to override this endpoint configured      * timeout value, and thus have per message individual timeout values.      * See also the requestTimeoutCheckerInterval option.      */
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"20000"
argument_list|,
name|label
operator|=
literal|"producer"
argument_list|,
name|description
operator|=
literal|"The timeout for waiting for a reply when using the InOut Exchange Pattern (in milliseconds)."
operator|+
literal|" The default is 20 seconds. You can include the header \"CamelJmsRequestTimeout\" to override this endpoint configured"
operator|+
literal|" timeout value, and thus have per message individual timeout values."
operator|+
literal|" See also the requestTimeoutCheckerInterval option."
argument_list|)
DECL|method|setRequestTimeout (long requestTimeout)
specifier|public
name|void
name|setRequestTimeout
parameter_list|(
name|long
name|requestTimeout
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setRequestTimeout
argument_list|(
name|requestTimeout
argument_list|)
expr_stmt|;
block|}
comment|/**      * Configures how often Camel should check for timed out Exchanges when doing request/reply over JMS.      * By default Camel checks once per second. But if you must react faster when a timeout occurs,      * then you can lower this interval, to check more frequently. The timeout is determined by the option requestTimeout.      */
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"1000"
argument_list|,
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"Configures how often Camel should check for timed out Exchanges when doing request/reply over JMS."
operator|+
literal|" By default Camel checks once per second. But if you must react faster when a timeout occurs,"
operator|+
literal|" then you can lower this interval, to check more frequently. The timeout is determined by the option requestTimeout."
argument_list|)
DECL|method|setRequestTimeoutCheckerInterval (long requestTimeoutCheckerInterval)
specifier|public
name|void
name|setRequestTimeoutCheckerInterval
parameter_list|(
name|long
name|requestTimeoutCheckerInterval
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setRequestTimeoutCheckerInterval
argument_list|(
name|requestTimeoutCheckerInterval
argument_list|)
expr_stmt|;
block|}
comment|/**      * You can transfer the exchange over the wire instead of just the body and headers.      * The following fields are transferred: In body, Out body, Fault body, In headers, Out headers, Fault headers,      * exchange properties, exchange exception.      * This requires that the objects are serializable. Camel will exclude any non-serializable objects and log it at WARN level.      * You must enable this option on both the producer and consumer side, so Camel knows the payloads is an Exchange and not a regular payload.      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"You can transfer the exchange over the wire instead of just the body and headers."
operator|+
literal|" The following fields are transferred: In body, Out body, Fault body, In headers, Out headers, Fault headers,"
operator|+
literal|" exchange properties, exchange exception."
operator|+
literal|" This requires that the objects are serializable. Camel will exclude any non-serializable objects and log it at WARN level."
operator|+
literal|" You must enable this option on both the producer and consumer side, so Camel knows the payloads is an Exchange and not a regular payload."
argument_list|)
DECL|method|setTransferExchange (boolean transferExchange)
specifier|public
name|void
name|setTransferExchange
parameter_list|(
name|boolean
name|transferExchange
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setTransferExchange
argument_list|(
name|transferExchange
argument_list|)
expr_stmt|;
block|}
comment|/**      * If enabled and you are using Request Reply messaging (InOut) and an Exchange failed on the consumer side,      * then the caused Exception will be send back in response as a javax.jms.ObjectMessage.      * If the client is Camel, the returned Exception is rethrown. This allows you to use Camel JMS as a bridge      * in your routing - for example, using persistent queues to enable robust routing.      * Notice that if you also have transferExchange enabled, this option takes precedence.      * The caught exception is required to be serializable.      * The original Exception on the consumer side can be wrapped in an outer exception      * such as org.apache.camel.RuntimeCamelException when returned to the producer.      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"If enabled and you are using Request Reply messaging (InOut) and an Exchange failed on the consumer side,"
operator|+
literal|" then the caused Exception will be send back in response as a javax.jms.ObjectMessage."
operator|+
literal|" If the client is Camel, the returned Exception is rethrown. This allows you to use Camel JMS as a bridge"
operator|+
literal|" in your routing - for example, using persistent queues to enable robust routing."
operator|+
literal|" Notice that if you also have transferExchange enabled, this option takes precedence."
operator|+
literal|" The caught exception is required to be serializable."
operator|+
literal|" The original Exception on the consumer side can be wrapped in an outer exception"
operator|+
literal|" such as org.apache.camel.RuntimeCamelException when returned to the producer."
argument_list|)
DECL|method|setTransferException (boolean transferException)
specifier|public
name|void
name|setTransferException
parameter_list|(
name|boolean
name|transferException
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setTransferException
argument_list|(
name|transferException
argument_list|)
expr_stmt|;
block|}
comment|/**      * Allows you to use your own implementation of the org.springframework.jms.core.JmsOperations interface.      * Camel uses JmsTemplate as default. Can be used for testing purpose, but not used much as stated in the spring API docs.      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"Allows you to use your own implementation of the org.springframework.jms.core.JmsOperations interface."
operator|+
literal|" Camel uses JmsTemplate as default. Can be used for testing purpose, but not used much as stated in the spring API docs."
argument_list|)
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
comment|/**      * A pluggable org.springframework.jms.support.destination.DestinationResolver that allows you to use your own resolver      * (for example, to lookup the real destination in a JNDI registry).      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"A pluggable org.springframework.jms.support.destination.DestinationResolver that allows you to use your own resolver"
operator|+
literal|" (for example, to lookup the real destination in a JNDI registry)."
argument_list|)
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
comment|/**      * Allows for explicitly specifying which kind of strategy to use for replyTo queues when doing request/reply over JMS.      * Possible values are: Temporary, Shared, or Exclusive.      * By default Camel will use temporary queues. However if replyTo has been configured, then Shared is used by default.      * This option allows you to use exclusive queues instead of shared ones.      * See Camel JMS documentation for more details, and especially the notes about the implications if running in a clustered environment,      * and the fact that Shared reply queues has lower performance than its alternatives Temporary and Exclusive.      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|description
operator|=
literal|"Allows for explicitly specifying which kind of strategy to use for replyTo queues when doing request/reply over JMS."
operator|+
literal|" Possible values are: Temporary, Shared, or Exclusive."
operator|+
literal|" By default Camel will use temporary queues. However if replyTo has been configured, then Shared is used by default."
operator|+
literal|" This option allows you to use exclusive queues instead of shared ones."
operator|+
literal|" See Camel JMS documentation for more details, and especially the notes about the implications if running in a clustered environment,"
operator|+
literal|" and the fact that Shared reply queues has lower performance than its alternatives Temporary and Exclusive."
argument_list|)
DECL|method|setReplyToType (ReplyToType replyToType)
specifier|public
name|void
name|setReplyToType
parameter_list|(
name|ReplyToType
name|replyToType
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setReplyToType
argument_list|(
name|replyToType
argument_list|)
expr_stmt|;
block|}
comment|/**      * Set to true, if you want to send message using the QoS settings specified on the message,      * instead of the QoS settings on the JMS endpoint. The following three headers are considered JMSPriority, JMSDeliveryMode,      * and JMSExpiration. You can provide all or only some of them. If not provided, Camel will fall back to use the      * values from the endpoint instead. So, when using this option, the headers override the values from the endpoint.      * The explicitQosEnabled option, by contrast, will only use options set on the endpoint, and not values from the message header.      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|description
operator|=
literal|"Set to true, if you want to send message using the QoS settings specified on the message,"
operator|+
literal|" instead of the QoS settings on the JMS endpoint. The following three headers are considered JMSPriority, JMSDeliveryMode,"
operator|+
literal|" and JMSExpiration. You can provide all or only some of them. If not provided, Camel will fall back to use the"
operator|+
literal|" values from the endpoint instead. So, when using this option, the headers override the values from the endpoint."
operator|+
literal|" The explicitQosEnabled option, by contrast, will only use options set on the endpoint, and not values from the message header."
argument_list|)
DECL|method|setPreserveMessageQos (boolean preserveMessageQos)
specifier|public
name|void
name|setPreserveMessageQos
parameter_list|(
name|boolean
name|preserveMessageQos
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setPreserveMessageQos
argument_list|(
name|preserveMessageQos
argument_list|)
expr_stmt|;
block|}
comment|/**      * Whether the JmsConsumer processes the Exchange asynchronously.      * If enabled then the JmsConsumer may pickup the next message from the JMS queue,      * while the previous message is being processed asynchronously (by the Asynchronous Routing Engine).      * This means that messages may be processed not 100% strictly in order. If disabled (as default)      * then the Exchange is fully processed before the JmsConsumer will pickup the next message from the JMS queue.      * Note if transacted has been enabled, then asyncConsumer=true does not run asynchronously, as transaction      * must be executed synchronously (Camel 3.0 may support async transactions).      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|description
operator|=
literal|"Whether the JmsConsumer processes the Exchange asynchronously."
operator|+
literal|" If enabled then the JmsConsumer may pickup the next message from the JMS queue,"
operator|+
literal|" while the previous message is being processed asynchronously (by the Asynchronous Routing Engine)."
operator|+
literal|" This means that messages may be processed not 100% strictly in order. If disabled (as default)"
operator|+
literal|" then the Exchange is fully processed before the JmsConsumer will pickup the next message from the JMS queue."
operator|+
literal|" Note if transacted has been enabled, then asyncConsumer=true does not run asynchronously, as transaction"
operator|+
literal|"  must be executed synchronously (Camel 3.0 may support async transactions)."
argument_list|)
DECL|method|setAsyncConsumer (boolean asyncConsumer)
specifier|public
name|void
name|setAsyncConsumer
parameter_list|(
name|boolean
name|asyncConsumer
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setAsyncConsumer
argument_list|(
name|asyncConsumer
argument_list|)
expr_stmt|;
block|}
comment|/**      * Whether to allow sending messages with no body. If this option is false and the message body is null, then an JMSException is thrown.      */
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|,
name|label
operator|=
literal|"producer,advanced"
argument_list|,
name|description
operator|=
literal|"Whether to allow sending messages with no body. If this option is false and the message body is null, then an JMSException is thrown."
argument_list|)
DECL|method|setAllowNullBody (boolean allowNullBody)
specifier|public
name|void
name|setAllowNullBody
parameter_list|(
name|boolean
name|allowNullBody
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setAllowNullBody
argument_list|(
name|allowNullBody
argument_list|)
expr_stmt|;
block|}
comment|/**      * Only applicable when sending to JMS destination using InOnly (eg fire and forget).      * Enabling this option will enrich the Camel Exchange with the actual JMSMessageID      * that was used by the JMS client when the message was sent to the JMS destination.      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"producer,advanced"
argument_list|,
name|description
operator|=
literal|"Only applicable when sending to JMS destination using InOnly (eg fire and forget)."
operator|+
literal|" Enabling this option will enrich the Camel Exchange with the actual JMSMessageID"
operator|+
literal|" that was used by the JMS client when the message was sent to the JMS destination."
argument_list|)
DECL|method|setIncludeSentJMSMessageID (boolean includeSentJMSMessageID)
specifier|public
name|void
name|setIncludeSentJMSMessageID
parameter_list|(
name|boolean
name|includeSentJMSMessageID
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setIncludeSentJMSMessageID
argument_list|(
name|includeSentJMSMessageID
argument_list|)
expr_stmt|;
block|}
comment|/**      * Whether to include all JMSXxxx properties when mapping from JMS to Camel Message.      * Setting this to true will include properties such as JMSXAppID, and JMSXUserID etc.      * Note: If you are using a custom headerFilterStrategy then this option does not apply.      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"Whether to include all JMSXxxx properties when mapping from JMS to Camel Message."
operator|+
literal|" Setting this to true will include properties such as JMSXAppID, and JMSXUserID etc."
operator|+
literal|" Note: If you are using a custom headerFilterStrategy then this option does not apply."
argument_list|)
DECL|method|setIncludeAllJMSXProperties (boolean includeAllJMSXProperties)
specifier|public
name|void
name|setIncludeAllJMSXProperties
parameter_list|(
name|boolean
name|includeAllJMSXProperties
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setIncludeAllJMSXProperties
argument_list|(
name|includeAllJMSXProperties
argument_list|)
expr_stmt|;
block|}
comment|/**      * Specifies what default TaskExecutor type to use in the DefaultMessageListenerContainer,      * for both consumer endpoints and the ReplyTo consumer of producer endpoints.      * Possible values: SimpleAsync (uses Spring's SimpleAsyncTaskExecutor) or ThreadPool      * (uses Spring's ThreadPoolTaskExecutor with optimal values - cached threadpool-like).      * If not set, it defaults to the previous behaviour, which uses a cached thread pool      * for consumer endpoints and SimpleAsync for reply consumers.      * The use of ThreadPool is recommended to reduce "thread trash" in elastic configurations      * with dynamically increasing and decreasing concurrent consumers.      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"consumer,advanced"
argument_list|,
name|description
operator|=
literal|"Specifies what default TaskExecutor type to use in the DefaultMessageListenerContainer,"
operator|+
literal|" for both consumer endpoints and the ReplyTo consumer of producer endpoints."
operator|+
literal|" Possible values: SimpleAsync (uses Spring's SimpleAsyncTaskExecutor) or ThreadPool"
operator|+
literal|" (uses Spring's ThreadPoolTaskExecutor with optimal values - cached threadpool-like)."
operator|+
literal|" If not set, it defaults to the previous behaviour, which uses a cached thread pool"
operator|+
literal|" for consumer endpoints and SimpleAsync for reply consumers."
operator|+
literal|" The use of ThreadPool is recommended to reduce thread trash in elastic configurations"
operator|+
literal|" with dynamically increasing and decreasing concurrent consumers."
argument_list|)
DECL|method|setDefaultTaskExecutorType (DefaultTaskExecutorType type)
specifier|public
name|void
name|setDefaultTaskExecutorType
parameter_list|(
name|DefaultTaskExecutorType
name|type
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setDefaultTaskExecutorType
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
comment|/**      * Pluggable strategy for encoding and decoding JMS keys so they can be compliant with the JMS specification.      * Camel provides two implementations out of the box: default and passthrough.      * The default strategy will safely marshal dots and hyphens (. and -). The passthrough strategy leaves the key as is.      * Can be used for JMS brokers which do not care whether JMS header keys contain illegal characters.      * You can provide your own implementation of the org.apache.camel.component.jms.JmsKeyFormatStrategy      * and refer to it using the # notation.      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"Pluggable strategy for encoding and decoding JMS keys so they can be compliant with the JMS specification."
operator|+
literal|" Camel provides two implementations out of the box: default and passthrough."
operator|+
literal|" The default strategy will safely marshal dots and hyphens (. and -). The passthrough strategy leaves the key as is."
operator|+
literal|" Can be used for JMS brokers which do not care whether JMS header keys contain illegal characters."
operator|+
literal|" You can provide your own implementation of the org.apache.camel.component.jms.JmsKeyFormatStrategy"
operator|+
literal|" and refer to it using the # notation."
argument_list|)
DECL|method|setJmsKeyFormatStrategy (JmsKeyFormatStrategy jmsKeyFormatStrategy)
specifier|public
name|void
name|setJmsKeyFormatStrategy
parameter_list|(
name|JmsKeyFormatStrategy
name|jmsKeyFormatStrategy
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setJmsKeyFormatStrategy
argument_list|(
name|jmsKeyFormatStrategy
argument_list|)
expr_stmt|;
block|}
comment|/**      * Pluggable strategy for encoding and decoding JMS keys so they can be compliant with the JMS specification.      * Camel provides two implementations out of the box: default and passthrough.      * The default strategy will safely marshal dots and hyphens (. and -). The passthrough strategy leaves the key as is.      * Can be used for JMS brokers which do not care whether JMS header keys contain illegal characters.      * You can provide your own implementation of the org.apache.camel.component.jms.JmsKeyFormatStrategy      * and refer to it using the # notation.      */
DECL|method|setJmsKeyFormatStrategy (String jmsKeyFormatStrategyName)
specifier|public
name|void
name|setJmsKeyFormatStrategy
parameter_list|(
name|String
name|jmsKeyFormatStrategyName
parameter_list|)
block|{
comment|// allow to configure a standard by its name, which is simpler
name|JmsKeyFormatStrategy
name|strategy
init|=
name|resolveStandardJmsKeyFormatStrategy
argument_list|(
name|jmsKeyFormatStrategyName
argument_list|)
decl_stmt|;
if|if
condition|(
name|strategy
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"JmsKeyFormatStrategy with name "
operator|+
name|jmsKeyFormatStrategyName
operator|+
literal|" is not a standard supported name"
argument_list|)
throw|;
block|}
else|else
block|{
name|getConfiguration
argument_list|()
operator|.
name|setJmsKeyFormatStrategy
argument_list|(
name|strategy
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * This option is used to allow additional headers which may have values that are invalid according to JMS specification.      * For example some message systems such as WMQ do this with header names using prefix JMS_IBM_MQMD_ containing values with byte array or other invalid types.      * You can specify multiple header names separated by comma, and use * as suffix for wildcard matching.      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"producer,advanced"
argument_list|,
name|description
operator|=
literal|"This option is used to allow additional headers which may have values that are invalid according to JMS specification."
operator|+
literal|" For example some message systems such as WMQ do this with header names using prefix JMS_IBM_MQMD_ containing values with byte array or other invalid types."
operator|+
literal|" You can specify multiple header names separated by comma, and use * as suffix for wildcard matching."
argument_list|)
DECL|method|setAllowAdditionalHeaders (String allowAdditionalHeaders)
specifier|public
name|void
name|setAllowAdditionalHeaders
parameter_list|(
name|String
name|allowAdditionalHeaders
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setAllowAdditionalHeaders
argument_list|(
name|allowAdditionalHeaders
argument_list|)
expr_stmt|;
block|}
comment|/**      * Sets the Spring ApplicationContext to use      */
annotation|@
name|Override
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
name|queueBrowseStrategy
operator|=
operator|new
name|DefaultQueueBrowseStrategy
argument_list|()
expr_stmt|;
block|}
return|return
name|queueBrowseStrategy
return|;
block|}
comment|/**      * To use a custom QueueBrowseStrategy when browsing queues      */
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
DECL|method|getMessageCreatedStrategy ()
specifier|public
name|MessageCreatedStrategy
name|getMessageCreatedStrategy
parameter_list|()
block|{
return|return
name|messageCreatedStrategy
return|;
block|}
comment|/**      * To use the given MessageCreatedStrategy which are invoked when Camel creates new instances of<tt>javax.jms.Message</tt>      * objects when Camel is sending a JMS message.      */
DECL|method|setMessageCreatedStrategy (MessageCreatedStrategy messageCreatedStrategy)
specifier|public
name|void
name|setMessageCreatedStrategy
parameter_list|(
name|MessageCreatedStrategy
name|messageCreatedStrategy
parameter_list|)
block|{
name|this
operator|.
name|messageCreatedStrategy
operator|=
name|messageCreatedStrategy
expr_stmt|;
block|}
DECL|method|getWaitForProvisionCorrelationToBeUpdatedCounter ()
specifier|public
name|int
name|getWaitForProvisionCorrelationToBeUpdatedCounter
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getWaitForProvisionCorrelationToBeUpdatedCounter
argument_list|()
return|;
block|}
comment|/**      * Number of times to wait for provisional correlation id to be updated to the actual correlation id when doing request/reply over JMS      * and when the option useMessageIDAsCorrelationID is enabled.      */
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"50"
argument_list|,
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"Number of times to wait for provisional correlation id to be updated to the actual correlation id when doing request/reply over JMS"
operator|+
literal|" and when the option useMessageIDAsCorrelationID is enabled."
argument_list|)
DECL|method|setWaitForProvisionCorrelationToBeUpdatedCounter (int counter)
specifier|public
name|void
name|setWaitForProvisionCorrelationToBeUpdatedCounter
parameter_list|(
name|int
name|counter
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setWaitForProvisionCorrelationToBeUpdatedCounter
argument_list|(
name|counter
argument_list|)
expr_stmt|;
block|}
DECL|method|getWaitForProvisionCorrelationToBeUpdatedThreadSleepingTime ()
specifier|public
name|long
name|getWaitForProvisionCorrelationToBeUpdatedThreadSleepingTime
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getWaitForProvisionCorrelationToBeUpdatedThreadSleepingTime
argument_list|()
return|;
block|}
comment|/**      * Interval in millis to sleep each time while waiting for provisional correlation id to be updated.      */
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"100"
argument_list|,
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"Interval in millis to sleep each time while waiting for provisional correlation id to be updated."
argument_list|)
DECL|method|setWaitForProvisionCorrelationToBeUpdatedThreadSleepingTime (long sleepingTime)
specifier|public
name|void
name|setWaitForProvisionCorrelationToBeUpdatedThreadSleepingTime
parameter_list|(
name|long
name|sleepingTime
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setWaitForProvisionCorrelationToBeUpdatedThreadSleepingTime
argument_list|(
name|sleepingTime
argument_list|)
expr_stmt|;
block|}
comment|/**      * Use this JMS property to correlate messages in InOut exchange pattern (request-reply)      * instead of JMSCorrelationID property. This allows you to exchange messages with       * systems that do not correlate messages using JMSCorrelationID JMS property. If used      * JMSCorrelationID will not be used or set by Camel. The value of here named property      * will be generated if not supplied in the header of the message under the same name.      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"producer,advanced"
argument_list|,
name|description
operator|=
literal|"Use this JMS property to correlate messages in InOut exchange pattern (request-reply)"
operator|+
literal|" instead of JMSCorrelationID property. This allows you to exchange messages with"
operator|+
literal|" systems that do not correlate messages using JMSCorrelationID JMS property. If used"
operator|+
literal|" JMSCorrelationID will not be used or set by Camel. The value of here named property"
operator|+
literal|" will be generated if not supplied in the header of the message under the same name."
argument_list|)
DECL|method|setCorrelationProperty (final String correlationProperty)
specifier|public
name|void
name|setCorrelationProperty
parameter_list|(
specifier|final
name|String
name|correlationProperty
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setCorrelationProperty
argument_list|(
name|correlationProperty
argument_list|)
expr_stmt|;
block|}
comment|// JMS 2.0 API
comment|// -------------------------------------------------------------------------
DECL|method|isSubscriptionDurable ()
specifier|public
name|boolean
name|isSubscriptionDurable
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|isSubscriptionDurable
argument_list|()
return|;
block|}
comment|/**      * Set whether to make the subscription durable. The durable subscription name      * to be used can be specified through the "subscriptionName" property.      *<p>Default is "false". Set this to "true" to register a durable subscription,      * typically in combination with a "subscriptionName" value (unless      * your message listener class name is good enough as subscription name).      *<p>Only makes sense when listening to a topic (pub-sub domain),      * therefore this method switches the "pubSubDomain" flag as well.      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|description
operator|=
literal|"Set whether to make the subscription durable. The durable subscription name"
operator|+
literal|" to be used can be specified through the subscriptionName property."
operator|+
literal|" Default is false. Set this to true to register a durable subscription,"
operator|+
literal|" typically in combination with a subscriptionName value (unless"
operator|+
literal|" your message listener class name is good enough as subscription name)."
operator|+
literal|" Only makes sense when listening to a topic (pub-sub domain),"
operator|+
literal|" therefore this method switches the pubSubDomain flag as well."
argument_list|)
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
DECL|method|isSubscriptionShared ()
specifier|public
name|boolean
name|isSubscriptionShared
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|isSubscriptionShared
argument_list|()
return|;
block|}
comment|/**      * Set whether to make the subscription shared. The shared subscription name      * to be used can be specified through the "subscriptionName" property.      *<p>Default is "false". Set this to "true" to register a shared subscription,      * typically in combination with a "subscriptionName" value (unless      * your message listener class name is good enough as subscription name).      * Note that shared subscriptions may also be durable, so this flag can      * (and often will) be combined with "subscriptionDurable" as well.      *<p>Only makes sense when listening to a topic (pub-sub domain),      * therefore this method switches the "pubSubDomain" flag as well.      *<p><b>Requires a JMS 2.0 compatible message broker.</b>      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|description
operator|=
literal|"Set whether to make the subscription shared. The shared subscription name"
operator|+
literal|" to be used can be specified through the subscriptionName property."
operator|+
literal|" Default is false. Set this to true to register a shared subscription,"
operator|+
literal|" typically in combination with a subscriptionName value (unless"
operator|+
literal|" your message listener class name is good enough as subscription name)."
operator|+
literal|" Note that shared subscriptions may also be durable, so this flag can"
operator|+
literal|" (and often will) be combined with subscriptionDurable as well."
operator|+
literal|" Only makes sense when listening to a topic (pub-sub domain),"
operator|+
literal|" therefore this method switches the pubSubDomain flag as well."
operator|+
literal|" Requires a JMS 2.0 compatible message broker."
argument_list|)
DECL|method|setSubscriptionShared (boolean subscriptionShared)
specifier|public
name|void
name|setSubscriptionShared
parameter_list|(
name|boolean
name|subscriptionShared
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setSubscriptionShared
argument_list|(
name|subscriptionShared
argument_list|)
expr_stmt|;
block|}
DECL|method|getSubscriptionName ()
specifier|public
name|String
name|getSubscriptionName
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getSubscriptionName
argument_list|()
return|;
block|}
comment|/**      * Set the name of a subscription to create. To be applied in case      * of a topic (pub-sub domain) with a shared or durable subscription.      *<p>The subscription name needs to be unique within this client's      * JMS client id. Default is the class name of the specified message listener.      *<p>Note: Only 1 concurrent consumer (which is the default of this      * message listener container) is allowed for each subscription,      * except for a shared subscription (which requires JMS 2.0).      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|description
operator|=
literal|"Set the name of a subscription to create. To be applied in case"
operator|+
literal|" of a topic (pub-sub domain) with a shared or durable subscription."
operator|+
literal|" The subscription name needs to be unique within this client's"
operator|+
literal|" JMS client id. Default is the class name of the specified message listener."
operator|+
literal|" Note: Only 1 concurrent consumer (which is the default of this"
operator|+
literal|" message listener container) is allowed for each subscription,"
operator|+
literal|" except for a shared subscription (which requires JMS 2.0)."
argument_list|)
DECL|method|setSubscriptionName (String subscriptionName)
specifier|public
name|void
name|setSubscriptionName
parameter_list|(
name|String
name|subscriptionName
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setSubscriptionName
argument_list|(
name|subscriptionName
argument_list|)
expr_stmt|;
block|}
DECL|method|isStreamMessageTypeEnabled ()
specifier|public
name|boolean
name|isStreamMessageTypeEnabled
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|isStreamMessageTypeEnabled
argument_list|()
return|;
block|}
comment|/**      * Sets whether StreamMessage type is enabled or not.      * Message payloads of streaming kind such as files, InputStream, etc will either by sent as BytesMessage or StreamMessage.      * This option controls which kind will be used. By default BytesMessage is used which enforces the entire message payload to be read into memory.      * By enabling this option the message payload is read into memory in chunks and each chunk is then written to the StreamMessage until no more data.      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"producer,advanced"
argument_list|,
name|description
operator|=
literal|"Sets whether StreamMessage type is enabled or not."
operator|+
literal|" Message payloads of streaming kind such as files, InputStream, etc will either by sent as BytesMessage or StreamMessage."
operator|+
literal|" This option controls which kind will be used. By default BytesMessage is used which enforces the entire message payload to be read into memory."
operator|+
literal|" By enabling this option the message payload is read into memory in chunks and each chunk is then written to the StreamMessage until no more data."
argument_list|)
DECL|method|setStreamMessageTypeEnabled (boolean streamMessageTypeEnabled)
specifier|public
name|void
name|setStreamMessageTypeEnabled
parameter_list|(
name|boolean
name|streamMessageTypeEnabled
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setStreamMessageTypeEnabled
argument_list|(
name|streamMessageTypeEnabled
argument_list|)
expr_stmt|;
block|}
comment|/**      * Gets whether date headers should be formatted according to the ISO 8601      * standard.      */
DECL|method|isFormatDateHeadersToIso8601 ()
specifier|public
name|boolean
name|isFormatDateHeadersToIso8601
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|isFormatDateHeadersToIso8601
argument_list|()
return|;
block|}
comment|/**      * Sets whether date headers should be formatted according to the ISO 8601      * standard.      */
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|description
operator|=
literal|"Sets whether date headers should be formatted according to the ISO 8601 standard."
argument_list|)
DECL|method|setFormatDateHeadersToIso8601 (boolean formatDateHeadersToIso8601)
specifier|public
name|void
name|setFormatDateHeadersToIso8601
parameter_list|(
name|boolean
name|formatDateHeadersToIso8601
parameter_list|)
block|{
name|getConfiguration
argument_list|()
operator|.
name|setFormatDateHeadersToIso8601
argument_list|(
name|formatDateHeadersToIso8601
argument_list|)
expr_stmt|;
block|}
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
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
name|getHeaderFilterStrategy
argument_list|()
operator|==
literal|null
condition|)
block|{
name|setHeaderFilterStrategy
argument_list|(
operator|new
name|JmsHeaderFilterStrategy
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|isIncludeAllJMSXProperties
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doShutdown ()
specifier|protected
name|void
name|doShutdown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|asyncStartStopExecutorService
operator|!=
literal|null
condition|)
block|{
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdownNow
argument_list|(
name|asyncStartStopExecutorService
argument_list|)
expr_stmt|;
name|asyncStartStopExecutorService
operator|=
literal|null
expr_stmt|;
block|}
name|super
operator|.
name|doShutdown
argument_list|()
expr_stmt|;
block|}
DECL|method|getAsyncStartStopExecutorService ()
specifier|protected
specifier|synchronized
name|ExecutorService
name|getAsyncStartStopExecutorService
parameter_list|()
block|{
if|if
condition|(
name|asyncStartStopExecutorService
operator|==
literal|null
condition|)
block|{
comment|// use a cached thread pool for async start tasks as they can run for a while, and we need a dedicated thread
comment|// for each task, and the thread pool will shrink when no more tasks running
name|asyncStartStopExecutorService
operator|=
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|newCachedThreadPool
argument_list|(
name|this
argument_list|,
literal|"AsyncStartStopListener"
argument_list|)
expr_stmt|;
block|}
return|return
name|asyncStartStopExecutorService
return|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|remaining
argument_list|)
condition|)
block|{
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
name|createTemporaryTopicEndpoint
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
name|createTopicEndpoint
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
name|createTemporaryQueueEndpoint
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
name|createQueueEndpoint
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
comment|// resolve any custom connection factory first
name|ConnectionFactory
name|cf
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"connectionFactory"
argument_list|,
name|ConnectionFactory
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|cf
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setConnectionFactory
argument_list|(
name|cf
argument_list|)
expr_stmt|;
block|}
comment|// if username or password provided then wrap the connection factory
name|String
name|cfUsername
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"username"
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|getUsername
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|cfPassword
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"password"
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|getConfiguration
argument_list|()
operator|.
name|getPassword
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|cfUsername
operator|!=
literal|null
operator|&&
name|cfPassword
operator|!=
literal|null
condition|)
block|{
name|cf
operator|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getOrCreateConnectionFactory
argument_list|()
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|cf
argument_list|,
literal|"ConnectionFactory"
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Wrapping existing ConnectionFactory with UserCredentialsConnectionFactoryAdapter using username: {} and password: ******"
argument_list|,
name|cfUsername
argument_list|)
expr_stmt|;
name|UserCredentialsConnectionFactoryAdapter
name|ucfa
init|=
operator|new
name|UserCredentialsConnectionFactoryAdapter
argument_list|()
decl_stmt|;
name|ucfa
operator|.
name|setTargetConnectionFactory
argument_list|(
name|cf
argument_list|)
expr_stmt|;
name|ucfa
operator|.
name|setPassword
argument_list|(
name|cfPassword
argument_list|)
expr_stmt|;
name|ucfa
operator|.
name|setUsername
argument_list|(
name|cfUsername
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setConnectionFactory
argument_list|(
name|ucfa
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// if only username or password was provided then fail
if|if
condition|(
name|cfUsername
operator|!=
literal|null
operator|||
name|cfPassword
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|cfUsername
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Username must also be provided when using username/password as credentials."
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Password must also be provided when using username/password as credentials."
argument_list|)
throw|;
block|}
block|}
block|}
comment|// jms header strategy
name|String
name|strategyVal
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
name|KEY_FORMAT_STRATEGY_PARAM
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|JmsKeyFormatStrategy
name|strategy
init|=
name|resolveStandardJmsKeyFormatStrategy
argument_list|(
name|strategyVal
argument_list|)
decl_stmt|;
if|if
condition|(
name|strategy
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setJmsKeyFormatStrategy
argument_list|(
name|strategy
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// its not a standard, but a reference
name|parameters
operator|.
name|put
argument_list|(
name|KEY_FORMAT_STRATEGY_PARAM
argument_list|,
name|strategyVal
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setJmsKeyFormatStrategy
argument_list|(
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
name|KEY_FORMAT_STRATEGY_PARAM
argument_list|,
name|JmsKeyFormatStrategy
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|MessageListenerContainerFactory
name|messageListenerContainerFactory
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"messageListenerContainerFactoryRef"
argument_list|,
name|MessageListenerContainerFactory
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|messageListenerContainerFactory
operator|==
literal|null
condition|)
block|{
name|messageListenerContainerFactory
operator|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"messageListenerContainerFactory"
argument_list|,
name|MessageListenerContainerFactory
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|messageListenerContainerFactory
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|setMessageListenerContainerFactory
argument_list|(
name|messageListenerContainerFactory
argument_list|)
expr_stmt|;
block|}
name|endpoint
operator|.
name|setHeaderFilterStrategy
argument_list|(
name|getHeaderFilterStrategy
argument_list|()
argument_list|)
expr_stmt|;
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
DECL|method|createTemporaryTopicEndpoint (String uri, JmsComponent component, String subject, JmsConfiguration configuration)
specifier|protected
name|JmsEndpoint
name|createTemporaryTopicEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|JmsComponent
name|component
parameter_list|,
name|String
name|subject
parameter_list|,
name|JmsConfiguration
name|configuration
parameter_list|)
block|{
return|return
operator|new
name|JmsTemporaryTopicEndpoint
argument_list|(
name|uri
argument_list|,
name|component
argument_list|,
name|subject
argument_list|,
name|configuration
argument_list|)
return|;
block|}
DECL|method|createTopicEndpoint (String uri, JmsComponent component, String subject, JmsConfiguration configuration)
specifier|protected
name|JmsEndpoint
name|createTopicEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|JmsComponent
name|component
parameter_list|,
name|String
name|subject
parameter_list|,
name|JmsConfiguration
name|configuration
parameter_list|)
block|{
return|return
operator|new
name|JmsEndpoint
argument_list|(
name|uri
argument_list|,
name|component
argument_list|,
name|subject
argument_list|,
literal|true
argument_list|,
name|configuration
argument_list|)
return|;
block|}
DECL|method|createTemporaryQueueEndpoint (String uri, JmsComponent component, String subject, JmsConfiguration configuration, QueueBrowseStrategy queueBrowseStrategy)
specifier|protected
name|JmsEndpoint
name|createTemporaryQueueEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|JmsComponent
name|component
parameter_list|,
name|String
name|subject
parameter_list|,
name|JmsConfiguration
name|configuration
parameter_list|,
name|QueueBrowseStrategy
name|queueBrowseStrategy
parameter_list|)
block|{
return|return
operator|new
name|JmsTemporaryQueueEndpoint
argument_list|(
name|uri
argument_list|,
name|component
argument_list|,
name|subject
argument_list|,
name|configuration
argument_list|,
name|queueBrowseStrategy
argument_list|)
return|;
block|}
DECL|method|createQueueEndpoint (String uri, JmsComponent component, String subject, JmsConfiguration configuration, QueueBrowseStrategy queueBrowseStrategy)
specifier|protected
name|JmsEndpoint
name|createQueueEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|JmsComponent
name|component
parameter_list|,
name|String
name|subject
parameter_list|,
name|JmsConfiguration
name|configuration
parameter_list|,
name|QueueBrowseStrategy
name|queueBrowseStrategy
parameter_list|)
block|{
return|return
operator|new
name|JmsQueueEndpoint
argument_list|(
name|uri
argument_list|,
name|component
argument_list|,
name|subject
argument_list|,
name|configuration
argument_list|,
name|queueBrowseStrategy
argument_list|)
return|;
block|}
comment|/**      * Resolves the standard supported {@link JmsKeyFormatStrategy} by a name which can be:      *<ul>      *<li>default - to use the default strategy</li>      *<li>passthrough - to use the passthrough strategy</li>      *</ul>      *      * @param name  the name      * @return the strategy, or<tt>null</tt> if not a standard name.      */
DECL|method|resolveStandardJmsKeyFormatStrategy (String name)
specifier|private
specifier|static
name|JmsKeyFormatStrategy
name|resolveStandardJmsKeyFormatStrategy
parameter_list|(
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
literal|"default"
operator|.
name|equalsIgnoreCase
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
operator|new
name|DefaultJmsKeyFormatStrategy
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
literal|"passthrough"
operator|.
name|equalsIgnoreCase
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return
operator|new
name|PassThroughJmsKeyFormatStrategy
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * A strategy method allowing the URI destination to be translated into the      * actual JMS destination name (say by looking up in JNDI or something)      */
DECL|method|convertPathToActualDestination (String path, Map<String, Object> parameters)
specifier|protected
name|String
name|convertPathToActualDestination
parameter_list|(
name|String
name|path
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
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
block|}
end_class

end_unit

