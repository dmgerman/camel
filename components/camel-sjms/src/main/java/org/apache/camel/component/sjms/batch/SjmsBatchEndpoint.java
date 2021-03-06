begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sjms.batch
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
name|batch
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
name|ScheduledExecutorService
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
name|AggregationStrategy
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
name|Consumer
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
name|Predicate
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
name|Producer
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
name|SjmsHeaderFilterStrategy
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
name|jms
operator|.
name|DefaultJmsKeyFormatStrategy
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
name|DestinationNameParser
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
name|JmsBinding
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
name|JmsKeyFormatStrategy
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
name|MessageCreatedStrategy
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
name|spi
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
name|UriEndpoint
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
name|UriParam
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
name|UriPath
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
name|DefaultEndpoint
import|;
end_import

begin_comment
comment|/**  * The sjms-batch component is a specialized for highly performant, transactional batch consumption from a JMS queue.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.16.0"
argument_list|,
name|scheme
operator|=
literal|"sjms-batch"
argument_list|,
name|title
operator|=
literal|"Simple JMS Batch"
argument_list|,
name|syntax
operator|=
literal|"sjms-batch:destinationName"
argument_list|,
name|label
operator|=
literal|"messaging"
argument_list|,
name|consumerOnly
operator|=
literal|true
argument_list|)
DECL|class|SjmsBatchEndpoint
specifier|public
class|class
name|SjmsBatchEndpoint
extends|extends
name|DefaultEndpoint
implements|implements
name|HeaderFilterStrategyAware
block|{
DECL|field|DEFAULT_COMPLETION_SIZE
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_COMPLETION_SIZE
init|=
literal|200
decl_stmt|;
comment|// the default dispatch queue size in ActiveMQ
DECL|field|DEFAULT_COMPLETION_TIMEOUT
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_COMPLETION_TIMEOUT
init|=
literal|500
decl_stmt|;
DECL|field|binding
specifier|private
name|JmsBinding
name|binding
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|destinationName
specifier|private
name|String
name|destinationName
decl_stmt|;
annotation|@
name|UriParam
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|aggregationStrategy
specifier|private
name|AggregationStrategy
name|aggregationStrategy
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"1"
argument_list|)
DECL|field|consumerCount
specifier|private
name|int
name|consumerCount
init|=
literal|1
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"200"
argument_list|)
DECL|field|completionSize
specifier|private
name|int
name|completionSize
init|=
name|DEFAULT_COMPLETION_SIZE
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"500"
argument_list|)
DECL|field|completionTimeout
specifier|private
name|int
name|completionTimeout
init|=
name|DEFAULT_COMPLETION_TIMEOUT
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"1000"
argument_list|)
DECL|field|completionInterval
specifier|private
name|int
name|completionInterval
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|javaType
operator|=
literal|"java.lang.String"
argument_list|)
DECL|field|completionPredicate
specifier|private
name|Predicate
name|completionPredicate
decl_stmt|;
annotation|@
name|UriParam
DECL|field|eagerCheckCompletion
specifier|private
name|boolean
name|eagerCheckCompletion
decl_stmt|;
annotation|@
name|UriParam
DECL|field|sendEmptyMessageWhenIdle
specifier|private
name|boolean
name|sendEmptyMessageWhenIdle
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"1000"
argument_list|)
DECL|field|pollDuration
specifier|private
name|int
name|pollDuration
init|=
literal|1000
decl_stmt|;
annotation|@
name|UriParam
DECL|field|includeAllJMSXProperties
specifier|private
name|boolean
name|includeAllJMSXProperties
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|allowNullBody
specifier|private
name|boolean
name|allowNullBody
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|mapJmsMessage
specifier|private
name|boolean
name|mapJmsMessage
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|headerFilterStrategy
specifier|private
name|HeaderFilterStrategy
name|headerFilterStrategy
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|messageCreatedStrategy
specifier|private
name|MessageCreatedStrategy
name|messageCreatedStrategy
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|jmsKeyFormatStrategy
specifier|private
name|JmsKeyFormatStrategy
name|jmsKeyFormatStrategy
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|timeoutCheckerExecutorService
specifier|private
name|ScheduledExecutorService
name|timeoutCheckerExecutorService
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|asyncStartListener
specifier|private
name|boolean
name|asyncStartListener
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|defaultValue
operator|=
literal|"5000"
argument_list|)
DECL|field|recoveryInterval
specifier|private
name|int
name|recoveryInterval
init|=
literal|5000
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|defaultValue
operator|=
literal|"-1"
argument_list|)
DECL|field|keepAliveDelay
specifier|private
name|int
name|keepAliveDelay
init|=
operator|-
literal|1
decl_stmt|;
DECL|method|SjmsBatchEndpoint ()
specifier|public
name|SjmsBatchEndpoint
parameter_list|()
block|{     }
DECL|method|SjmsBatchEndpoint (String endpointUri, Component component, String remaining)
specifier|public
name|SjmsBatchEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|String
name|remaining
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|DestinationNameParser
name|parser
init|=
operator|new
name|DestinationNameParser
argument_list|()
decl_stmt|;
if|if
condition|(
name|parser
operator|.
name|isTopic
argument_list|(
name|remaining
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Only batch consumption from queues is supported. For topics you "
operator|+
literal|"should use a regular JMS consumer with an aggregator."
argument_list|)
throw|;
block|}
name|this
operator|.
name|destinationName
operator|=
name|parser
operator|.
name|getShortName
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|SjmsBatchComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|SjmsBatchComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Producer not supported"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|SjmsBatchConsumer
name|consumer
init|=
operator|new
name|SjmsBatchConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|consumer
operator|.
name|setTimeoutCheckerExecutorService
argument_list|(
name|timeoutCheckerExecutorService
argument_list|)
expr_stmt|;
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
DECL|method|createExchange (Message message, Session session)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|Message
name|message
parameter_list|,
name|Session
name|session
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|(
name|getExchangePattern
argument_list|()
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
operator|new
name|SjmsMessage
argument_list|(
name|exchange
argument_list|,
name|message
argument_list|,
name|session
argument_list|,
name|getBinding
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|getBinding ()
specifier|public
name|JmsBinding
name|getBinding
parameter_list|()
block|{
if|if
condition|(
name|binding
operator|==
literal|null
condition|)
block|{
name|binding
operator|=
name|createBinding
argument_list|()
expr_stmt|;
block|}
return|return
name|binding
return|;
block|}
comment|/**      * Creates the {@link org.apache.camel.component.sjms.jms.JmsBinding} to use.      */
DECL|method|createBinding ()
specifier|protected
name|JmsBinding
name|createBinding
parameter_list|()
block|{
return|return
operator|new
name|JmsBinding
argument_list|(
name|isMapJmsMessage
argument_list|()
argument_list|,
name|isAllowNullBody
argument_list|()
argument_list|,
name|getHeaderFilterStrategy
argument_list|()
argument_list|,
name|getJmsKeyFormatStrategy
argument_list|()
argument_list|,
name|getMessageCreatedStrategy
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Sets the binding used to convert from a Camel message to and from a JMS      * message      */
DECL|method|setBinding (JmsBinding binding)
specifier|public
name|void
name|setBinding
parameter_list|(
name|JmsBinding
name|binding
parameter_list|)
block|{
name|this
operator|.
name|binding
operator|=
name|binding
expr_stmt|;
block|}
DECL|method|getAggregationStrategy ()
specifier|public
name|AggregationStrategy
name|getAggregationStrategy
parameter_list|()
block|{
return|return
name|aggregationStrategy
return|;
block|}
comment|/**      * The aggregation strategy to use, which merges all the batched messages into a single message      */
DECL|method|setAggregationStrategy (AggregationStrategy aggregationStrategy)
specifier|public
name|void
name|setAggregationStrategy
parameter_list|(
name|AggregationStrategy
name|aggregationStrategy
parameter_list|)
block|{
name|this
operator|.
name|aggregationStrategy
operator|=
name|aggregationStrategy
expr_stmt|;
block|}
comment|/**      * The destination name. Only queues are supported, names may be prefixed by 'queue:'.      */
DECL|method|getDestinationName ()
specifier|public
name|String
name|getDestinationName
parameter_list|()
block|{
return|return
name|destinationName
return|;
block|}
DECL|method|getConsumerCount ()
specifier|public
name|int
name|getConsumerCount
parameter_list|()
block|{
return|return
name|consumerCount
return|;
block|}
comment|/**      * The number of JMS sessions to consume from      */
DECL|method|setConsumerCount (int consumerCount)
specifier|public
name|void
name|setConsumerCount
parameter_list|(
name|int
name|consumerCount
parameter_list|)
block|{
name|this
operator|.
name|consumerCount
operator|=
name|consumerCount
expr_stmt|;
block|}
DECL|method|getCompletionSize ()
specifier|public
name|int
name|getCompletionSize
parameter_list|()
block|{
return|return
name|completionSize
return|;
block|}
comment|/**      * The number of messages consumed at which the batch will be completed      */
DECL|method|setCompletionSize (int completionSize)
specifier|public
name|void
name|setCompletionSize
parameter_list|(
name|int
name|completionSize
parameter_list|)
block|{
name|this
operator|.
name|completionSize
operator|=
name|completionSize
expr_stmt|;
block|}
DECL|method|getCompletionTimeout ()
specifier|public
name|int
name|getCompletionTimeout
parameter_list|()
block|{
return|return
name|completionTimeout
return|;
block|}
comment|/**      * The timeout in millis from receipt of the first first message when the batch will be completed.      * The batch may be empty if the timeout triggered and there was no messages in the batch.      *<br/>      * Notice you cannot use both completion timeout and completion interval at the same time, only one can be configured.      */
DECL|method|setCompletionTimeout (int completionTimeout)
specifier|public
name|void
name|setCompletionTimeout
parameter_list|(
name|int
name|completionTimeout
parameter_list|)
block|{
name|this
operator|.
name|completionTimeout
operator|=
name|completionTimeout
expr_stmt|;
block|}
DECL|method|getCompletionInterval ()
specifier|public
name|int
name|getCompletionInterval
parameter_list|()
block|{
return|return
name|completionInterval
return|;
block|}
comment|/**      * The completion interval in millis, which causes batches to be completed in a scheduled fixed rate every interval.      * The batch may be empty if the timeout triggered and there was no messages in the batch.      *<br/>      * Notice you cannot use both completion timeout and completion interval at the same time, only one can be configured.      */
DECL|method|setCompletionInterval (int completionInterval)
specifier|public
name|void
name|setCompletionInterval
parameter_list|(
name|int
name|completionInterval
parameter_list|)
block|{
name|this
operator|.
name|completionInterval
operator|=
name|completionInterval
expr_stmt|;
block|}
DECL|method|getCompletionPredicate ()
specifier|public
name|Predicate
name|getCompletionPredicate
parameter_list|()
block|{
return|return
name|completionPredicate
return|;
block|}
comment|/**      * The completion predicate, which causes batches to be completed when the predicate evaluates as true.      *<p/>      * The predicate can also be configured using the simple language using the string syntax.      * You may want to set the option eagerCheckCompletion to true to let the predicate match the incoming message,      * as otherwise it matches the aggregated message.      */
DECL|method|setCompletionPredicate (Predicate completionPredicate)
specifier|public
name|void
name|setCompletionPredicate
parameter_list|(
name|Predicate
name|completionPredicate
parameter_list|)
block|{
name|this
operator|.
name|completionPredicate
operator|=
name|completionPredicate
expr_stmt|;
block|}
DECL|method|setCompletionPredicate (String predicate)
specifier|public
name|void
name|setCompletionPredicate
parameter_list|(
name|String
name|predicate
parameter_list|)
block|{
comment|// uses simple language
name|this
operator|.
name|completionPredicate
operator|=
name|getCamelContext
argument_list|()
operator|.
name|resolveLanguage
argument_list|(
literal|"simple"
argument_list|)
operator|.
name|createPredicate
argument_list|(
name|predicate
argument_list|)
expr_stmt|;
block|}
DECL|method|isEagerCheckCompletion ()
specifier|public
name|boolean
name|isEagerCheckCompletion
parameter_list|()
block|{
return|return
name|eagerCheckCompletion
return|;
block|}
comment|/**      * Use eager completion checking which means that the completionPredicate will use the incoming Exchange.      * As opposed to without eager completion checking the completionPredicate will use the aggregated Exchange.      */
DECL|method|setEagerCheckCompletion (boolean eagerCheckCompletion)
specifier|public
name|void
name|setEagerCheckCompletion
parameter_list|(
name|boolean
name|eagerCheckCompletion
parameter_list|)
block|{
name|this
operator|.
name|eagerCheckCompletion
operator|=
name|eagerCheckCompletion
expr_stmt|;
block|}
DECL|method|isSendEmptyMessageWhenIdle ()
specifier|public
name|boolean
name|isSendEmptyMessageWhenIdle
parameter_list|()
block|{
return|return
name|sendEmptyMessageWhenIdle
return|;
block|}
comment|/**      * If using completion timeout or interval, then the batch may be empty if the timeout triggered and there was no messages in the batch.      * If this option is<tt>true</tt> and the batch is empty then an empty message is added to the batch so an empty message is routed.      */
DECL|method|setSendEmptyMessageWhenIdle (boolean sendEmptyMessageWhenIdle)
specifier|public
name|void
name|setSendEmptyMessageWhenIdle
parameter_list|(
name|boolean
name|sendEmptyMessageWhenIdle
parameter_list|)
block|{
name|this
operator|.
name|sendEmptyMessageWhenIdle
operator|=
name|sendEmptyMessageWhenIdle
expr_stmt|;
block|}
DECL|method|getPollDuration ()
specifier|public
name|int
name|getPollDuration
parameter_list|()
block|{
return|return
name|pollDuration
return|;
block|}
comment|/**      * The duration in milliseconds of each poll for messages.      * completionTimeOut will be used if it is shorter and a batch has started.      */
DECL|method|setPollDuration (int pollDuration)
specifier|public
name|void
name|setPollDuration
parameter_list|(
name|int
name|pollDuration
parameter_list|)
block|{
name|this
operator|.
name|pollDuration
operator|=
name|pollDuration
expr_stmt|;
block|}
DECL|method|isAllowNullBody ()
specifier|public
name|boolean
name|isAllowNullBody
parameter_list|()
block|{
return|return
name|allowNullBody
return|;
block|}
comment|/**      * Whether to allow sending messages with no body. If this option is false and the message body is null, then an JMSException is thrown.      */
DECL|method|setAllowNullBody (boolean allowNullBody)
specifier|public
name|void
name|setAllowNullBody
parameter_list|(
name|boolean
name|allowNullBody
parameter_list|)
block|{
name|this
operator|.
name|allowNullBody
operator|=
name|allowNullBody
expr_stmt|;
block|}
DECL|method|isMapJmsMessage ()
specifier|public
name|boolean
name|isMapJmsMessage
parameter_list|()
block|{
return|return
name|mapJmsMessage
return|;
block|}
comment|/**      * Specifies whether Camel should auto map the received JMS message to a suited payload type, such as javax.jms.TextMessage to a String etc.      * See section about how mapping works below for more details.      */
DECL|method|setMapJmsMessage (boolean mapJmsMessage)
specifier|public
name|void
name|setMapJmsMessage
parameter_list|(
name|boolean
name|mapJmsMessage
parameter_list|)
block|{
name|this
operator|.
name|mapJmsMessage
operator|=
name|mapJmsMessage
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
DECL|method|getJmsKeyFormatStrategy ()
specifier|public
name|JmsKeyFormatStrategy
name|getJmsKeyFormatStrategy
parameter_list|()
block|{
if|if
condition|(
name|jmsKeyFormatStrategy
operator|==
literal|null
condition|)
block|{
name|jmsKeyFormatStrategy
operator|=
operator|new
name|DefaultJmsKeyFormatStrategy
argument_list|()
expr_stmt|;
block|}
return|return
name|jmsKeyFormatStrategy
return|;
block|}
comment|/**      * Pluggable strategy for encoding and decoding JMS keys so they can be compliant with the JMS specification.      * Camel provides two implementations out of the box: default and passthrough.      * The default strategy will safely marshal dots and hyphens (. and -). The passthrough strategy leaves the key as is.      * Can be used for JMS brokers which do not care whether JMS header keys contain illegal characters.      * You can provide your own implementation of the org.apache.camel.component.jms.JmsKeyFormatStrategy      * and refer to it using the # notation.      */
DECL|method|setJmsKeyFormatStrategy (JmsKeyFormatStrategy jmsKeyFormatStrategy)
specifier|public
name|void
name|setJmsKeyFormatStrategy
parameter_list|(
name|JmsKeyFormatStrategy
name|jmsKeyFormatStrategy
parameter_list|)
block|{
name|this
operator|.
name|jmsKeyFormatStrategy
operator|=
name|jmsKeyFormatStrategy
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getHeaderFilterStrategy ()
specifier|public
name|HeaderFilterStrategy
name|getHeaderFilterStrategy
parameter_list|()
block|{
if|if
condition|(
name|headerFilterStrategy
operator|==
literal|null
condition|)
block|{
name|headerFilterStrategy
operator|=
operator|new
name|SjmsHeaderFilterStrategy
argument_list|(
name|isIncludeAllJMSXProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|headerFilterStrategy
return|;
block|}
comment|/**      * To use a custom HeaderFilterStrategy to filter header to and from Camel message.      */
annotation|@
name|Override
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
DECL|method|isIncludeAllJMSXProperties ()
specifier|public
name|boolean
name|isIncludeAllJMSXProperties
parameter_list|()
block|{
return|return
name|includeAllJMSXProperties
return|;
block|}
comment|/**      * Whether to include all JMSXxxx properties when mapping from JMS to Camel Message.      * Setting this to true will include properties such as JMSXAppID, and JMSXUserID etc.      * Note: If you are using a custom headerFilterStrategy then this option does not apply.      */
DECL|method|setIncludeAllJMSXProperties (boolean includeAllJMSXProperties)
specifier|public
name|void
name|setIncludeAllJMSXProperties
parameter_list|(
name|boolean
name|includeAllJMSXProperties
parameter_list|)
block|{
name|this
operator|.
name|includeAllJMSXProperties
operator|=
name|includeAllJMSXProperties
expr_stmt|;
block|}
DECL|method|getTimeoutCheckerExecutorService ()
specifier|public
name|ScheduledExecutorService
name|getTimeoutCheckerExecutorService
parameter_list|()
block|{
return|return
name|timeoutCheckerExecutorService
return|;
block|}
comment|/**      * If using the completionInterval option a background thread is created to trigger the completion interval.      * Set this option to provide a custom thread pool to be used rather than creating a new thread for every consumer.      */
DECL|method|setTimeoutCheckerExecutorService (ScheduledExecutorService timeoutCheckerExecutorService)
specifier|public
name|void
name|setTimeoutCheckerExecutorService
parameter_list|(
name|ScheduledExecutorService
name|timeoutCheckerExecutorService
parameter_list|)
block|{
name|this
operator|.
name|timeoutCheckerExecutorService
operator|=
name|timeoutCheckerExecutorService
expr_stmt|;
block|}
DECL|method|isAsyncStartListener ()
specifier|public
name|boolean
name|isAsyncStartListener
parameter_list|()
block|{
return|return
name|asyncStartListener
return|;
block|}
comment|/**      * Whether to startup the consumer message listener asynchronously, when starting a route.      * For example if a JmsConsumer cannot get a connection to a remote JMS broker, then it may block while retrying      * and/or failover. This will cause Camel to block while starting routes. By setting this option to true,      * you will let routes startup, while the JmsConsumer connects to the JMS broker using a dedicated thread      * in asynchronous mode. If this option is used, then beware that if the connection could not be established,      * then an exception is logged at WARN level, and the consumer will not be able to receive messages;      * You can then restart the route to retry.      */
DECL|method|setAsyncStartListener (boolean asyncStartListener)
specifier|public
name|void
name|setAsyncStartListener
parameter_list|(
name|boolean
name|asyncStartListener
parameter_list|)
block|{
name|this
operator|.
name|asyncStartListener
operator|=
name|asyncStartListener
expr_stmt|;
block|}
DECL|method|getRecoveryInterval ()
specifier|public
name|int
name|getRecoveryInterval
parameter_list|()
block|{
return|return
name|recoveryInterval
return|;
block|}
comment|/**      * Specifies the interval between recovery attempts, i.e. when a connection is being refreshed, in milliseconds.      * The default is 5000 ms, that is, 5 seconds.      */
DECL|method|setRecoveryInterval (int recoveryInterval)
specifier|public
name|void
name|setRecoveryInterval
parameter_list|(
name|int
name|recoveryInterval
parameter_list|)
block|{
name|this
operator|.
name|recoveryInterval
operator|=
name|recoveryInterval
expr_stmt|;
block|}
comment|/**      * The delay in millis between attempts to re-establish a valid session.      * If this is a positive value the SjmsBatchConsumer will attempt to create a new session if it sees an IllegalStateException      * during message consumption. This delay value allows you to pause between attempts to prevent spamming the logs.      * If this is a negative value (default is -1) then the SjmsBatchConsumer will behave as it always has before - that is      * it will bail out and the route will shut down if it sees an IllegalStateException.      */
DECL|method|setKeepAliveDelay (int keepAliveDelay)
specifier|public
name|void
name|setKeepAliveDelay
parameter_list|(
name|int
name|keepAliveDelay
parameter_list|)
block|{
name|this
operator|.
name|keepAliveDelay
operator|=
name|keepAliveDelay
expr_stmt|;
block|}
DECL|method|getKeepAliveDelay ()
specifier|public
name|int
name|getKeepAliveDelay
parameter_list|()
block|{
return|return
name|keepAliveDelay
return|;
block|}
block|}
end_class

end_unit

