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
name|MultipleConsumersSupport
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
name|jms
operator|.
name|*
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
name|producer
operator|.
name|InOnlyProducer
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
name|producer
operator|.
name|InOutProducer
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
name|DefaultEndpoint
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

begin_comment
comment|/**  * A JMS Endpoint  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"sjms"
argument_list|,
name|title
operator|=
literal|"Simple JMS"
argument_list|,
name|syntax
operator|=
literal|"sjms:destinationType:destinationName"
argument_list|,
name|consumerClass
operator|=
name|SjmsConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"messaging"
argument_list|)
DECL|class|SjmsEndpoint
specifier|public
class|class
name|SjmsEndpoint
extends|extends
name|DefaultEndpoint
implements|implements
name|MultipleConsumersSupport
block|{
DECL|field|logger
specifier|protected
specifier|final
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|topic
specifier|private
name|boolean
name|topic
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|enums
operator|=
literal|"queue,topic"
argument_list|,
name|defaultValue
operator|=
literal|"queue"
argument_list|,
name|description
operator|=
literal|"The kind of destination to use"
argument_list|)
DECL|field|destinationType
specifier|private
name|String
name|destinationType
decl_stmt|;
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|destinationName
specifier|private
name|String
name|destinationName
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|synchronous
specifier|private
name|boolean
name|synchronous
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
DECL|field|transacted
specifier|private
name|boolean
name|transacted
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|)
DECL|field|namedReplyTo
specifier|private
name|String
name|namedReplyTo
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"AUTO_ACKNOWLEDGE"
argument_list|,
name|enums
operator|=
literal|"SESSION_TRANSACTED,CLIENT_ACKNOWLEDGE,AUTO_ACKNOWLEDGE,DUPS_OK_ACKNOWLEDGE"
argument_list|)
DECL|field|acknowledgementMode
specifier|private
name|SessionAcknowledgementType
name|acknowledgementMode
init|=
name|SessionAcknowledgementType
operator|.
name|AUTO_ACKNOWLEDGE
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"1"
argument_list|)
DECL|field|sessionCount
specifier|private
name|int
name|sessionCount
init|=
literal|1
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"1"
argument_list|)
DECL|field|producerCount
specifier|private
name|int
name|producerCount
init|=
literal|1
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
literal|"-1"
argument_list|)
DECL|field|ttl
specifier|private
name|long
name|ttl
init|=
operator|-
literal|1
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|persistent
specifier|private
name|boolean
name|persistent
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|durableSubscriptionId
specifier|private
name|String
name|durableSubscriptionId
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"5000"
argument_list|)
DECL|field|responseTimeOut
specifier|private
name|long
name|responseTimeOut
init|=
literal|5000
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|)
DECL|field|messageSelector
specifier|private
name|String
name|messageSelector
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"-1"
argument_list|)
DECL|field|transactionBatchCount
specifier|private
name|int
name|transactionBatchCount
init|=
operator|-
literal|1
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"5000"
argument_list|)
DECL|field|transactionBatchTimeout
specifier|private
name|long
name|transactionBatchTimeout
init|=
literal|5000
decl_stmt|;
annotation|@
name|UriParam
DECL|field|asyncStartListener
specifier|private
name|boolean
name|asyncStartListener
decl_stmt|;
annotation|@
name|UriParam
DECL|field|asyncStopListener
specifier|private
name|boolean
name|asyncStopListener
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|prefillPool
specifier|private
name|boolean
name|prefillPool
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
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
DECL|field|transactionCommitStrategy
specifier|private
name|TransactionCommitStrategy
name|transactionCommitStrategy
decl_stmt|;
annotation|@
name|UriParam
DECL|field|destinationCreationStrategy
specifier|private
name|DestinationCreationStrategy
name|destinationCreationStrategy
init|=
operator|new
name|DefaultDestinationCreationStrategy
argument_list|()
decl_stmt|;
DECL|method|SjmsEndpoint ()
specifier|public
name|SjmsEndpoint
parameter_list|()
block|{     }
DECL|method|SjmsEndpoint (String uri, Component component, String remaining)
specifier|public
name|SjmsEndpoint
parameter_list|(
name|String
name|uri
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
name|uri
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
name|topic
operator|=
name|parser
operator|.
name|isTopic
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
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
name|SjmsComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|SjmsComponent
operator|)
name|super
operator|.
name|getComponent
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
name|SjmsProducer
name|producer
decl_stmt|;
if|if
condition|(
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
name|producer
operator|=
operator|new
name|InOnlyProducer
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|producer
operator|=
operator|new
name|InOutProducer
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
return|return
name|producer
return|;
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
name|SjmsConsumer
name|answer
init|=
operator|new
name|SjmsConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|isMultipleConsumersSupported ()
specifier|public
name|boolean
name|isMultipleConsumersSupported
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
comment|/**      * DestinationName is a JMS queue or topic name. By default, the destinationName is interpreted as a queue name.      */
DECL|method|setDestinationName (String destinationName)
specifier|public
name|void
name|setDestinationName
parameter_list|(
name|String
name|destinationName
parameter_list|)
block|{
name|this
operator|.
name|destinationName
operator|=
name|destinationName
expr_stmt|;
block|}
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
DECL|method|getConnectionResource ()
specifier|public
name|ConnectionResource
name|getConnectionResource
parameter_list|()
block|{
return|return
name|getComponent
argument_list|()
operator|.
name|getConnectionResource
argument_list|()
return|;
block|}
DECL|method|getSjmsHeaderFilterStrategy ()
specifier|public
name|HeaderFilterStrategy
name|getSjmsHeaderFilterStrategy
parameter_list|()
block|{
return|return
name|getComponent
argument_list|()
operator|.
name|getHeaderFilterStrategy
argument_list|()
return|;
block|}
DECL|method|getJmsKeyFormatStrategy ()
specifier|public
name|KeyFormatStrategy
name|getJmsKeyFormatStrategy
parameter_list|()
block|{
return|return
name|getComponent
argument_list|()
operator|.
name|getKeyFormatStrategy
argument_list|()
return|;
block|}
DECL|method|isSynchronous ()
specifier|public
name|boolean
name|isSynchronous
parameter_list|()
block|{
return|return
name|synchronous
return|;
block|}
comment|/**      * Sets whether synchronous processing should be strictly used or Camel is allowed to use asynchronous processing (if supported).      */
DECL|method|setSynchronous (boolean synchronous)
specifier|public
name|void
name|setSynchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|this
operator|.
name|synchronous
operator|=
name|synchronous
expr_stmt|;
block|}
DECL|method|getAcknowledgementMode ()
specifier|public
name|SessionAcknowledgementType
name|getAcknowledgementMode
parameter_list|()
block|{
return|return
name|acknowledgementMode
return|;
block|}
comment|/**      * The JMS acknowledgement name, which is one of: SESSION_TRANSACTED, CLIENT_ACKNOWLEDGE, AUTO_ACKNOWLEDGE, DUPS_OK_ACKNOWLEDGE      */
DECL|method|setAcknowledgementMode (SessionAcknowledgementType acknowledgementMode)
specifier|public
name|void
name|setAcknowledgementMode
parameter_list|(
name|SessionAcknowledgementType
name|acknowledgementMode
parameter_list|)
block|{
name|this
operator|.
name|acknowledgementMode
operator|=
name|acknowledgementMode
expr_stmt|;
block|}
comment|/**      * Flag set by the endpoint used by consumers and producers to determine if      * the endpoint is a JMS Topic.      */
DECL|method|isTopic ()
specifier|public
name|boolean
name|isTopic
parameter_list|()
block|{
return|return
name|topic
return|;
block|}
comment|/**      * Returns the number of Session instances expected on this endpoint.      */
annotation|@
name|Deprecated
DECL|method|getSessionCount ()
specifier|public
name|int
name|getSessionCount
parameter_list|()
block|{
return|return
name|sessionCount
return|;
block|}
comment|/**      * Sets the number of Session instances used for this endpoint. Value is      * ignored for endpoints that require a dedicated session such as a      * transacted or InOut endpoint.      *      * @param sessionCount the number of Session instances, default is 1      */
annotation|@
name|Deprecated
DECL|method|setSessionCount (int sessionCount)
specifier|public
name|void
name|setSessionCount
parameter_list|(
name|int
name|sessionCount
parameter_list|)
block|{
name|this
operator|.
name|sessionCount
operator|=
name|sessionCount
expr_stmt|;
block|}
DECL|method|getProducerCount ()
specifier|public
name|int
name|getProducerCount
parameter_list|()
block|{
return|return
name|producerCount
return|;
block|}
comment|/**      * Sets the number of producers used for this endpoint.      */
DECL|method|setProducerCount (int producerCount)
specifier|public
name|void
name|setProducerCount
parameter_list|(
name|int
name|producerCount
parameter_list|)
block|{
name|this
operator|.
name|producerCount
operator|=
name|producerCount
expr_stmt|;
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
comment|/**      * Sets the number of consumer listeners used for this endpoint.      */
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
DECL|method|getTtl ()
specifier|public
name|long
name|getTtl
parameter_list|()
block|{
return|return
name|ttl
return|;
block|}
comment|/**      * Flag used to adjust the Time To Live value of produced messages.      */
DECL|method|setTtl (long ttl)
specifier|public
name|void
name|setTtl
parameter_list|(
name|long
name|ttl
parameter_list|)
block|{
name|this
operator|.
name|ttl
operator|=
name|ttl
expr_stmt|;
block|}
DECL|method|isPersistent ()
specifier|public
name|boolean
name|isPersistent
parameter_list|()
block|{
return|return
name|persistent
return|;
block|}
comment|/**      * Flag used to enable/disable message persistence.      */
DECL|method|setPersistent (boolean persistent)
specifier|public
name|void
name|setPersistent
parameter_list|(
name|boolean
name|persistent
parameter_list|)
block|{
name|this
operator|.
name|persistent
operator|=
name|persistent
expr_stmt|;
block|}
DECL|method|getDurableSubscriptionId ()
specifier|public
name|String
name|getDurableSubscriptionId
parameter_list|()
block|{
return|return
name|durableSubscriptionId
return|;
block|}
comment|/**      * Sets the durable subscription Id required for durable topics.      */
DECL|method|setDurableSubscriptionId (String durableSubscriptionId)
specifier|public
name|void
name|setDurableSubscriptionId
parameter_list|(
name|String
name|durableSubscriptionId
parameter_list|)
block|{
name|this
operator|.
name|durableSubscriptionId
operator|=
name|durableSubscriptionId
expr_stmt|;
block|}
DECL|method|getResponseTimeOut ()
specifier|public
name|long
name|getResponseTimeOut
parameter_list|()
block|{
return|return
name|responseTimeOut
return|;
block|}
comment|/**      * Sets the amount of time we should wait before timing out a InOut response.      */
DECL|method|setResponseTimeOut (long responseTimeOut)
specifier|public
name|void
name|setResponseTimeOut
parameter_list|(
name|long
name|responseTimeOut
parameter_list|)
block|{
name|this
operator|.
name|responseTimeOut
operator|=
name|responseTimeOut
expr_stmt|;
block|}
DECL|method|getMessageSelector ()
specifier|public
name|String
name|getMessageSelector
parameter_list|()
block|{
return|return
name|messageSelector
return|;
block|}
comment|/**      * Sets the JMS Message selector syntax.      */
DECL|method|setMessageSelector (String messageSelector)
specifier|public
name|void
name|setMessageSelector
parameter_list|(
name|String
name|messageSelector
parameter_list|)
block|{
name|this
operator|.
name|messageSelector
operator|=
name|messageSelector
expr_stmt|;
block|}
DECL|method|getTransactionBatchCount ()
specifier|public
name|int
name|getTransactionBatchCount
parameter_list|()
block|{
return|return
name|transactionBatchCount
return|;
block|}
comment|/**      * If transacted sets the number of messages to process before committing a transaction.      */
DECL|method|setTransactionBatchCount (int transactionBatchCount)
specifier|public
name|void
name|setTransactionBatchCount
parameter_list|(
name|int
name|transactionBatchCount
parameter_list|)
block|{
name|this
operator|.
name|transactionBatchCount
operator|=
name|transactionBatchCount
expr_stmt|;
block|}
DECL|method|getTransactionBatchTimeout ()
specifier|public
name|long
name|getTransactionBatchTimeout
parameter_list|()
block|{
return|return
name|transactionBatchTimeout
return|;
block|}
comment|/**      * Sets timeout (in millis) for batch transactions, the value should be 1000 or higher.      */
DECL|method|setTransactionBatchTimeout (long transactionBatchTimeout)
specifier|public
name|void
name|setTransactionBatchTimeout
parameter_list|(
name|long
name|transactionBatchTimeout
parameter_list|)
block|{
if|if
condition|(
name|transactionBatchTimeout
operator|>=
literal|1000
condition|)
block|{
name|this
operator|.
name|transactionBatchTimeout
operator|=
name|transactionBatchTimeout
expr_stmt|;
block|}
block|}
DECL|method|getTransactionCommitStrategy ()
specifier|public
name|TransactionCommitStrategy
name|getTransactionCommitStrategy
parameter_list|()
block|{
return|return
name|transactionCommitStrategy
return|;
block|}
comment|/**      * Sets the commit strategy.      */
DECL|method|setTransactionCommitStrategy (TransactionCommitStrategy transactionCommitStrategy)
specifier|public
name|void
name|setTransactionCommitStrategy
parameter_list|(
name|TransactionCommitStrategy
name|transactionCommitStrategy
parameter_list|)
block|{
name|this
operator|.
name|transactionCommitStrategy
operator|=
name|transactionCommitStrategy
expr_stmt|;
block|}
DECL|method|isTransacted ()
specifier|public
name|boolean
name|isTransacted
parameter_list|()
block|{
return|return
name|transacted
return|;
block|}
comment|/**      * Specifies whether to use transacted mode      */
DECL|method|setTransacted (boolean transacted)
specifier|public
name|void
name|setTransacted
parameter_list|(
name|boolean
name|transacted
parameter_list|)
block|{
if|if
condition|(
name|transacted
condition|)
block|{
name|setAcknowledgementMode
argument_list|(
name|SessionAcknowledgementType
operator|.
name|SESSION_TRANSACTED
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|transacted
operator|=
name|transacted
expr_stmt|;
block|}
DECL|method|getNamedReplyTo ()
specifier|public
name|String
name|getNamedReplyTo
parameter_list|()
block|{
return|return
name|namedReplyTo
return|;
block|}
comment|/**      * Sets the reply to destination name used for InOut producer endpoints.      */
DECL|method|setNamedReplyTo (String namedReplyTo)
specifier|public
name|void
name|setNamedReplyTo
parameter_list|(
name|String
name|namedReplyTo
parameter_list|)
block|{
name|this
operator|.
name|namedReplyTo
operator|=
name|namedReplyTo
expr_stmt|;
name|this
operator|.
name|setExchangePattern
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
expr_stmt|;
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
comment|/**      * Whether to stop the consumer message listener asynchronously, when stopping a route.      */
DECL|method|setAsyncStopListener (boolean asyncStopListener)
specifier|public
name|void
name|setAsyncStopListener
parameter_list|(
name|boolean
name|asyncStopListener
parameter_list|)
block|{
name|this
operator|.
name|asyncStopListener
operator|=
name|asyncStopListener
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
DECL|method|isAsyncStopListener ()
specifier|public
name|boolean
name|isAsyncStopListener
parameter_list|()
block|{
return|return
name|asyncStopListener
return|;
block|}
DECL|method|isPrefillPool ()
specifier|public
name|boolean
name|isPrefillPool
parameter_list|()
block|{
return|return
name|prefillPool
return|;
block|}
comment|/**      * Whether to prefill the producer connection pool on startup, or create connections lazy when needed.      */
DECL|method|setPrefillPool (boolean prefillPool)
specifier|public
name|void
name|setPrefillPool
parameter_list|(
name|boolean
name|prefillPool
parameter_list|)
block|{
name|this
operator|.
name|prefillPool
operator|=
name|prefillPool
expr_stmt|;
block|}
DECL|method|getDestinationCreationStrategy ()
specifier|public
name|DestinationCreationStrategy
name|getDestinationCreationStrategy
parameter_list|()
block|{
return|return
name|destinationCreationStrategy
return|;
block|}
comment|/**      * To use a custom DestinationCreationStrategy.      */
DECL|method|setDestinationCreationStrategy (DestinationCreationStrategy destinationCreationStrategy)
specifier|public
name|void
name|setDestinationCreationStrategy
parameter_list|(
name|DestinationCreationStrategy
name|destinationCreationStrategy
parameter_list|)
block|{
name|this
operator|.
name|destinationCreationStrategy
operator|=
name|destinationCreationStrategy
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
block|}
end_class

end_unit

