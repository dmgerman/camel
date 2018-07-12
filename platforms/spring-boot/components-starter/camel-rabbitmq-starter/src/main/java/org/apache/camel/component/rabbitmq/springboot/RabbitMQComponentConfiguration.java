begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rabbitmq.springboot
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rabbitmq
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * The rabbitmq component allows you produce and consume messages from RabbitMQ  * instances.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.rabbitmq"
argument_list|)
DECL|class|RabbitMQComponentConfiguration
specifier|public
class|class
name|RabbitMQComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * The hostname of the running rabbitmq instance or cluster.      */
DECL|field|hostname
specifier|private
name|String
name|hostname
decl_stmt|;
comment|/**      * Port number for the host with the running rabbitmq instance or cluster.      */
DECL|field|portNumber
specifier|private
name|Integer
name|portNumber
init|=
literal|5672
decl_stmt|;
comment|/**      * Username in case of authenticated access      */
DECL|field|username
specifier|private
name|String
name|username
init|=
literal|"guest"
decl_stmt|;
comment|/**      * Password for authenticated access      */
DECL|field|password
specifier|private
name|String
name|password
init|=
literal|"guest"
decl_stmt|;
comment|/**      * The vhost for the channel      */
DECL|field|vhost
specifier|private
name|String
name|vhost
init|=
literal|"/"
decl_stmt|;
comment|/**      * If this option is set, camel-rabbitmq will try to create connection based      * on the setting of option addresses. The addresses value is a string which      * looks like server1:12345, server2:12345      */
DECL|field|addresses
specifier|private
name|String
name|addresses
decl_stmt|;
comment|/**      * To use a custom RabbitMQ connection factory. When this option is set, all      * connection options (connectionTimeout, requestedChannelMax...) set on URI      * are not used. The option is a com.rabbitmq.client.ConnectionFactory type.      */
DECL|field|connectionFactory
specifier|private
name|String
name|connectionFactory
decl_stmt|;
comment|/**      * The consumer uses a Thread Pool Executor with a fixed number of threads.      * This setting allows you to set that number of threads.      */
DECL|field|threadPoolSize
specifier|private
name|Integer
name|threadPoolSize
init|=
literal|10
decl_stmt|;
comment|/**      * Whether to auto-detect looking up RabbitMQ connection factory from the      * registry. When enabled and a single instance of the connection factory is      * found then it will be used. An explicit connection factory can be      * configured on the component or endpoint level which takes precedence.      */
DECL|field|autoDetectConnectionFactory
specifier|private
name|Boolean
name|autoDetectConnectionFactory
init|=
literal|true
decl_stmt|;
comment|/**      * Connection timeout      */
DECL|field|connectionTimeout
specifier|private
name|Integer
name|connectionTimeout
init|=
literal|60000
decl_stmt|;
comment|/**      * Connection requested channel max (max number of channels offered)      */
DECL|field|requestedChannelMax
specifier|private
name|Integer
name|requestedChannelMax
init|=
literal|2047
decl_stmt|;
comment|/**      * Connection requested frame max (max size of frame offered)      */
DECL|field|requestedFrameMax
specifier|private
name|Integer
name|requestedFrameMax
init|=
literal|0
decl_stmt|;
comment|/**      * Connection requested heartbeat (heart-beat in seconds offered)      */
DECL|field|requestedHeartbeat
specifier|private
name|Integer
name|requestedHeartbeat
init|=
literal|60
decl_stmt|;
comment|/**      * Enables connection automatic recovery (uses connection implementation      * that performs automatic recovery when connection shutdown is not      * initiated by the application)      */
DECL|field|automaticRecoveryEnabled
specifier|private
name|Boolean
name|automaticRecoveryEnabled
decl_stmt|;
comment|/**      * Network recovery interval in milliseconds (interval used when recovering      * from network failure)      */
DECL|field|networkRecoveryInterval
specifier|private
name|Integer
name|networkRecoveryInterval
init|=
literal|5000
decl_stmt|;
comment|/**      * Enables connection topology recovery (should topology recovery be      * performed)      */
DECL|field|topologyRecoveryEnabled
specifier|private
name|Boolean
name|topologyRecoveryEnabled
decl_stmt|;
comment|/**      * Enables the quality of service on the RabbitMQConsumer side. You need to      * specify the option of prefetchSize, prefetchCount, prefetchGlobal at the      * same time      */
DECL|field|prefetchEnabled
specifier|private
name|Boolean
name|prefetchEnabled
init|=
literal|false
decl_stmt|;
comment|/**      * The maximum amount of content (measured in octets) that the server will      * deliver, 0 if unlimited. You need to specify the option of prefetchSize,      * prefetchCount, prefetchGlobal at the same time      */
DECL|field|prefetchSize
specifier|private
name|Integer
name|prefetchSize
decl_stmt|;
comment|/**      * The maximum number of messages that the server will deliver, 0 if      * unlimited. You need to specify the option of prefetchSize, prefetchCount,      * prefetchGlobal at the same time      */
DECL|field|prefetchCount
specifier|private
name|Integer
name|prefetchCount
decl_stmt|;
comment|/**      * If the settings should be applied to the entire channel rather than each      * consumer You need to specify the option of prefetchSize, prefetchCount,      * prefetchGlobal at the same time      */
DECL|field|prefetchGlobal
specifier|private
name|Boolean
name|prefetchGlobal
init|=
literal|false
decl_stmt|;
comment|/**      * Get maximum number of opened channel in pool      */
DECL|field|channelPoolMaxSize
specifier|private
name|Integer
name|channelPoolMaxSize
init|=
literal|10
decl_stmt|;
comment|/**      * Set the maximum number of milliseconds to wait for a channel from the      * pool      */
DECL|field|channelPoolMaxWait
specifier|private
name|Long
name|channelPoolMaxWait
init|=
literal|1000L
decl_stmt|;
comment|/**      * Set timeout for waiting for a reply when using the InOut Exchange Pattern      * (in milliseconds)      */
DECL|field|requestTimeout
specifier|private
name|Long
name|requestTimeout
init|=
literal|20000L
decl_stmt|;
comment|/**      * Set requestTimeoutCheckerInterval for inOut exchange      */
DECL|field|requestTimeoutCheckerInterval
specifier|private
name|Long
name|requestTimeoutCheckerInterval
init|=
literal|1000L
decl_stmt|;
comment|/**      * When true and an inOut Exchange failed on the consumer side send the      * caused Exception back in the response      */
DECL|field|transferException
specifier|private
name|Boolean
name|transferException
init|=
literal|false
decl_stmt|;
comment|/**      * When true, the message will be published with publisher acknowledgements      * turned on      */
DECL|field|publisherAcknowledgements
specifier|private
name|Boolean
name|publisherAcknowledgements
init|=
literal|false
decl_stmt|;
comment|/**      * The amount of time in milliseconds to wait for a basic.ack response from      * RabbitMQ server      */
DECL|field|publisherAcknowledgementsTimeout
specifier|private
name|Long
name|publisherAcknowledgementsTimeout
decl_stmt|;
comment|/**      * When true, an exception will be thrown when the message cannot be      * delivered (basic.return) and the message is marked as mandatory.      * PublisherAcknowledgement will also be activated in this case. See also      * publisher acknowledgements - When will messages be confirmed.      */
DECL|field|guaranteedDeliveries
specifier|private
name|Boolean
name|guaranteedDeliveries
init|=
literal|false
decl_stmt|;
comment|/**      * This flag tells the server how to react if the message cannot be routed      * to a queue. If this flag is set, the server will return an unroutable      * message with a Return method. If this flag is zero, the server silently      * drops the message. If the header is present rabbitmq.MANDATORY it will      * override this option.      */
DECL|field|mandatory
specifier|private
name|Boolean
name|mandatory
init|=
literal|false
decl_stmt|;
comment|/**      * This flag tells the server how to react if the message cannot be routed      * to a queue consumer immediately. If this flag is set, the server will      * return an undeliverable message with a Return method. If this flag is      * zero, the server will queue the message, but with no guarantee that it      * will ever be consumed. If the header is present rabbitmq.IMMEDIATE it      * will override this option.      */
DECL|field|immediate
specifier|private
name|Boolean
name|immediate
init|=
literal|false
decl_stmt|;
comment|/**      * Specify arguments for configuring the different RabbitMQ concepts, a      * different prefix is required for each: Exchange: arg.exchange. Queue:      * arg.queue. Binding: arg.binding. For example to declare a queue with      * message ttl argument:      * http://localhost:5672/exchange/queueargs=arg.queue.x-message-ttl=60000.      * The option is a java.util.Map<java.lang.String,java.lang.Object> type.      */
DECL|field|args
specifier|private
name|String
name|args
decl_stmt|;
comment|/**      * Connection client properties (client info used in negotiating with the      * server). The option is a java.util.Map<java.lang.String,java.lang.Object>      * type.      */
DECL|field|clientProperties
specifier|private
name|String
name|clientProperties
decl_stmt|;
comment|/**      * Enables SSL on connection, accepted value are true, TLS and 'SSLv3      */
DECL|field|sslProtocol
specifier|private
name|String
name|sslProtocol
decl_stmt|;
comment|/**      * Configure SSL trust manager, SSL should be enabled for this option to be      * effective. The option is a javax.net.ssl.TrustManager type.      */
DECL|field|trustManager
specifier|private
name|String
name|trustManager
decl_stmt|;
comment|/**      * If messages should be auto acknowledged      */
DECL|field|autoAck
specifier|private
name|Boolean
name|autoAck
init|=
literal|true
decl_stmt|;
comment|/**      * If it is true, the exchange will be deleted when it is no longer in use      */
DECL|field|autoDelete
specifier|private
name|Boolean
name|autoDelete
init|=
literal|true
decl_stmt|;
comment|/**      * If we are declaring a durable exchange (the exchange will survive a      * server restart)      */
DECL|field|durable
specifier|private
name|Boolean
name|durable
init|=
literal|true
decl_stmt|;
comment|/**      * Exclusive queues may only be accessed by the current connection, and are      * deleted when that connection closes.      */
DECL|field|exclusive
specifier|private
name|Boolean
name|exclusive
init|=
literal|false
decl_stmt|;
comment|/**      * Request exclusive access to the queue (meaning only this consumer can      * access the queue). This is useful when you want a long-lived shared queue      * to be temporarily accessible by just one consumer.      */
DECL|field|exclusiveConsumer
specifier|private
name|Boolean
name|exclusiveConsumer
init|=
literal|false
decl_stmt|;
comment|/**      * Passive queues depend on the queue already to be available at RabbitMQ.      */
DECL|field|passive
specifier|private
name|Boolean
name|passive
init|=
literal|false
decl_stmt|;
comment|/**      * If true the producer will not declare and bind a queue. This can be used      * for directing messages via an existing routing key.      */
DECL|field|skipQueueDeclare
specifier|private
name|Boolean
name|skipQueueDeclare
init|=
literal|false
decl_stmt|;
comment|/**      * If true the queue will not be bound to the exchange after declaring it      */
DECL|field|skipQueueBind
specifier|private
name|Boolean
name|skipQueueBind
init|=
literal|false
decl_stmt|;
comment|/**      * This can be used if we need to declare the queue but not the exchange      */
DECL|field|skipExchangeDeclare
specifier|private
name|Boolean
name|skipExchangeDeclare
init|=
literal|false
decl_stmt|;
comment|/**      * If the option is true, camel declare the exchange and queue name and bind      * them together. If the option is false, camel won't declare the exchange      * and queue name on the server.      */
DECL|field|declare
specifier|private
name|Boolean
name|declare
init|=
literal|true
decl_stmt|;
comment|/**      * The name of the dead letter exchange      */
DECL|field|deadLetterExchange
specifier|private
name|String
name|deadLetterExchange
decl_stmt|;
comment|/**      * The name of the dead letter queue      */
DECL|field|deadLetterQueue
specifier|private
name|String
name|deadLetterQueue
decl_stmt|;
comment|/**      * The routing key for the dead letter exchange      */
DECL|field|deadLetterRoutingKey
specifier|private
name|String
name|deadLetterRoutingKey
decl_stmt|;
comment|/**      * The type of the dead letter exchange      */
DECL|field|deadLetterExchangeType
specifier|private
name|String
name|deadLetterExchangeType
init|=
literal|"direct"
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
DECL|method|getHostname ()
specifier|public
name|String
name|getHostname
parameter_list|()
block|{
return|return
name|hostname
return|;
block|}
DECL|method|setHostname (String hostname)
specifier|public
name|void
name|setHostname
parameter_list|(
name|String
name|hostname
parameter_list|)
block|{
name|this
operator|.
name|hostname
operator|=
name|hostname
expr_stmt|;
block|}
DECL|method|getPortNumber ()
specifier|public
name|Integer
name|getPortNumber
parameter_list|()
block|{
return|return
name|portNumber
return|;
block|}
DECL|method|setPortNumber (Integer portNumber)
specifier|public
name|void
name|setPortNumber
parameter_list|(
name|Integer
name|portNumber
parameter_list|)
block|{
name|this
operator|.
name|portNumber
operator|=
name|portNumber
expr_stmt|;
block|}
DECL|method|getUsername ()
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|username
return|;
block|}
DECL|method|setUsername (String username)
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
block|}
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
DECL|method|setPassword (String password)
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
DECL|method|getVhost ()
specifier|public
name|String
name|getVhost
parameter_list|()
block|{
return|return
name|vhost
return|;
block|}
DECL|method|setVhost (String vhost)
specifier|public
name|void
name|setVhost
parameter_list|(
name|String
name|vhost
parameter_list|)
block|{
name|this
operator|.
name|vhost
operator|=
name|vhost
expr_stmt|;
block|}
DECL|method|getAddresses ()
specifier|public
name|String
name|getAddresses
parameter_list|()
block|{
return|return
name|addresses
return|;
block|}
DECL|method|setAddresses (String addresses)
specifier|public
name|void
name|setAddresses
parameter_list|(
name|String
name|addresses
parameter_list|)
block|{
name|this
operator|.
name|addresses
operator|=
name|addresses
expr_stmt|;
block|}
DECL|method|getConnectionFactory ()
specifier|public
name|String
name|getConnectionFactory
parameter_list|()
block|{
return|return
name|connectionFactory
return|;
block|}
DECL|method|setConnectionFactory (String connectionFactory)
specifier|public
name|void
name|setConnectionFactory
parameter_list|(
name|String
name|connectionFactory
parameter_list|)
block|{
name|this
operator|.
name|connectionFactory
operator|=
name|connectionFactory
expr_stmt|;
block|}
DECL|method|getThreadPoolSize ()
specifier|public
name|Integer
name|getThreadPoolSize
parameter_list|()
block|{
return|return
name|threadPoolSize
return|;
block|}
DECL|method|setThreadPoolSize (Integer threadPoolSize)
specifier|public
name|void
name|setThreadPoolSize
parameter_list|(
name|Integer
name|threadPoolSize
parameter_list|)
block|{
name|this
operator|.
name|threadPoolSize
operator|=
name|threadPoolSize
expr_stmt|;
block|}
DECL|method|getAutoDetectConnectionFactory ()
specifier|public
name|Boolean
name|getAutoDetectConnectionFactory
parameter_list|()
block|{
return|return
name|autoDetectConnectionFactory
return|;
block|}
DECL|method|setAutoDetectConnectionFactory ( Boolean autoDetectConnectionFactory)
specifier|public
name|void
name|setAutoDetectConnectionFactory
parameter_list|(
name|Boolean
name|autoDetectConnectionFactory
parameter_list|)
block|{
name|this
operator|.
name|autoDetectConnectionFactory
operator|=
name|autoDetectConnectionFactory
expr_stmt|;
block|}
DECL|method|getConnectionTimeout ()
specifier|public
name|Integer
name|getConnectionTimeout
parameter_list|()
block|{
return|return
name|connectionTimeout
return|;
block|}
DECL|method|setConnectionTimeout (Integer connectionTimeout)
specifier|public
name|void
name|setConnectionTimeout
parameter_list|(
name|Integer
name|connectionTimeout
parameter_list|)
block|{
name|this
operator|.
name|connectionTimeout
operator|=
name|connectionTimeout
expr_stmt|;
block|}
DECL|method|getRequestedChannelMax ()
specifier|public
name|Integer
name|getRequestedChannelMax
parameter_list|()
block|{
return|return
name|requestedChannelMax
return|;
block|}
DECL|method|setRequestedChannelMax (Integer requestedChannelMax)
specifier|public
name|void
name|setRequestedChannelMax
parameter_list|(
name|Integer
name|requestedChannelMax
parameter_list|)
block|{
name|this
operator|.
name|requestedChannelMax
operator|=
name|requestedChannelMax
expr_stmt|;
block|}
DECL|method|getRequestedFrameMax ()
specifier|public
name|Integer
name|getRequestedFrameMax
parameter_list|()
block|{
return|return
name|requestedFrameMax
return|;
block|}
DECL|method|setRequestedFrameMax (Integer requestedFrameMax)
specifier|public
name|void
name|setRequestedFrameMax
parameter_list|(
name|Integer
name|requestedFrameMax
parameter_list|)
block|{
name|this
operator|.
name|requestedFrameMax
operator|=
name|requestedFrameMax
expr_stmt|;
block|}
DECL|method|getRequestedHeartbeat ()
specifier|public
name|Integer
name|getRequestedHeartbeat
parameter_list|()
block|{
return|return
name|requestedHeartbeat
return|;
block|}
DECL|method|setRequestedHeartbeat (Integer requestedHeartbeat)
specifier|public
name|void
name|setRequestedHeartbeat
parameter_list|(
name|Integer
name|requestedHeartbeat
parameter_list|)
block|{
name|this
operator|.
name|requestedHeartbeat
operator|=
name|requestedHeartbeat
expr_stmt|;
block|}
DECL|method|getAutomaticRecoveryEnabled ()
specifier|public
name|Boolean
name|getAutomaticRecoveryEnabled
parameter_list|()
block|{
return|return
name|automaticRecoveryEnabled
return|;
block|}
DECL|method|setAutomaticRecoveryEnabled (Boolean automaticRecoveryEnabled)
specifier|public
name|void
name|setAutomaticRecoveryEnabled
parameter_list|(
name|Boolean
name|automaticRecoveryEnabled
parameter_list|)
block|{
name|this
operator|.
name|automaticRecoveryEnabled
operator|=
name|automaticRecoveryEnabled
expr_stmt|;
block|}
DECL|method|getNetworkRecoveryInterval ()
specifier|public
name|Integer
name|getNetworkRecoveryInterval
parameter_list|()
block|{
return|return
name|networkRecoveryInterval
return|;
block|}
DECL|method|setNetworkRecoveryInterval (Integer networkRecoveryInterval)
specifier|public
name|void
name|setNetworkRecoveryInterval
parameter_list|(
name|Integer
name|networkRecoveryInterval
parameter_list|)
block|{
name|this
operator|.
name|networkRecoveryInterval
operator|=
name|networkRecoveryInterval
expr_stmt|;
block|}
DECL|method|getTopologyRecoveryEnabled ()
specifier|public
name|Boolean
name|getTopologyRecoveryEnabled
parameter_list|()
block|{
return|return
name|topologyRecoveryEnabled
return|;
block|}
DECL|method|setTopologyRecoveryEnabled (Boolean topologyRecoveryEnabled)
specifier|public
name|void
name|setTopologyRecoveryEnabled
parameter_list|(
name|Boolean
name|topologyRecoveryEnabled
parameter_list|)
block|{
name|this
operator|.
name|topologyRecoveryEnabled
operator|=
name|topologyRecoveryEnabled
expr_stmt|;
block|}
DECL|method|getPrefetchEnabled ()
specifier|public
name|Boolean
name|getPrefetchEnabled
parameter_list|()
block|{
return|return
name|prefetchEnabled
return|;
block|}
DECL|method|setPrefetchEnabled (Boolean prefetchEnabled)
specifier|public
name|void
name|setPrefetchEnabled
parameter_list|(
name|Boolean
name|prefetchEnabled
parameter_list|)
block|{
name|this
operator|.
name|prefetchEnabled
operator|=
name|prefetchEnabled
expr_stmt|;
block|}
DECL|method|getPrefetchSize ()
specifier|public
name|Integer
name|getPrefetchSize
parameter_list|()
block|{
return|return
name|prefetchSize
return|;
block|}
DECL|method|setPrefetchSize (Integer prefetchSize)
specifier|public
name|void
name|setPrefetchSize
parameter_list|(
name|Integer
name|prefetchSize
parameter_list|)
block|{
name|this
operator|.
name|prefetchSize
operator|=
name|prefetchSize
expr_stmt|;
block|}
DECL|method|getPrefetchCount ()
specifier|public
name|Integer
name|getPrefetchCount
parameter_list|()
block|{
return|return
name|prefetchCount
return|;
block|}
DECL|method|setPrefetchCount (Integer prefetchCount)
specifier|public
name|void
name|setPrefetchCount
parameter_list|(
name|Integer
name|prefetchCount
parameter_list|)
block|{
name|this
operator|.
name|prefetchCount
operator|=
name|prefetchCount
expr_stmt|;
block|}
DECL|method|getPrefetchGlobal ()
specifier|public
name|Boolean
name|getPrefetchGlobal
parameter_list|()
block|{
return|return
name|prefetchGlobal
return|;
block|}
DECL|method|setPrefetchGlobal (Boolean prefetchGlobal)
specifier|public
name|void
name|setPrefetchGlobal
parameter_list|(
name|Boolean
name|prefetchGlobal
parameter_list|)
block|{
name|this
operator|.
name|prefetchGlobal
operator|=
name|prefetchGlobal
expr_stmt|;
block|}
DECL|method|getChannelPoolMaxSize ()
specifier|public
name|Integer
name|getChannelPoolMaxSize
parameter_list|()
block|{
return|return
name|channelPoolMaxSize
return|;
block|}
DECL|method|setChannelPoolMaxSize (Integer channelPoolMaxSize)
specifier|public
name|void
name|setChannelPoolMaxSize
parameter_list|(
name|Integer
name|channelPoolMaxSize
parameter_list|)
block|{
name|this
operator|.
name|channelPoolMaxSize
operator|=
name|channelPoolMaxSize
expr_stmt|;
block|}
DECL|method|getChannelPoolMaxWait ()
specifier|public
name|Long
name|getChannelPoolMaxWait
parameter_list|()
block|{
return|return
name|channelPoolMaxWait
return|;
block|}
DECL|method|setChannelPoolMaxWait (Long channelPoolMaxWait)
specifier|public
name|void
name|setChannelPoolMaxWait
parameter_list|(
name|Long
name|channelPoolMaxWait
parameter_list|)
block|{
name|this
operator|.
name|channelPoolMaxWait
operator|=
name|channelPoolMaxWait
expr_stmt|;
block|}
DECL|method|getRequestTimeout ()
specifier|public
name|Long
name|getRequestTimeout
parameter_list|()
block|{
return|return
name|requestTimeout
return|;
block|}
DECL|method|setRequestTimeout (Long requestTimeout)
specifier|public
name|void
name|setRequestTimeout
parameter_list|(
name|Long
name|requestTimeout
parameter_list|)
block|{
name|this
operator|.
name|requestTimeout
operator|=
name|requestTimeout
expr_stmt|;
block|}
DECL|method|getRequestTimeoutCheckerInterval ()
specifier|public
name|Long
name|getRequestTimeoutCheckerInterval
parameter_list|()
block|{
return|return
name|requestTimeoutCheckerInterval
return|;
block|}
DECL|method|setRequestTimeoutCheckerInterval ( Long requestTimeoutCheckerInterval)
specifier|public
name|void
name|setRequestTimeoutCheckerInterval
parameter_list|(
name|Long
name|requestTimeoutCheckerInterval
parameter_list|)
block|{
name|this
operator|.
name|requestTimeoutCheckerInterval
operator|=
name|requestTimeoutCheckerInterval
expr_stmt|;
block|}
DECL|method|getTransferException ()
specifier|public
name|Boolean
name|getTransferException
parameter_list|()
block|{
return|return
name|transferException
return|;
block|}
DECL|method|setTransferException (Boolean transferException)
specifier|public
name|void
name|setTransferException
parameter_list|(
name|Boolean
name|transferException
parameter_list|)
block|{
name|this
operator|.
name|transferException
operator|=
name|transferException
expr_stmt|;
block|}
DECL|method|getPublisherAcknowledgements ()
specifier|public
name|Boolean
name|getPublisherAcknowledgements
parameter_list|()
block|{
return|return
name|publisherAcknowledgements
return|;
block|}
DECL|method|setPublisherAcknowledgements (Boolean publisherAcknowledgements)
specifier|public
name|void
name|setPublisherAcknowledgements
parameter_list|(
name|Boolean
name|publisherAcknowledgements
parameter_list|)
block|{
name|this
operator|.
name|publisherAcknowledgements
operator|=
name|publisherAcknowledgements
expr_stmt|;
block|}
DECL|method|getPublisherAcknowledgementsTimeout ()
specifier|public
name|Long
name|getPublisherAcknowledgementsTimeout
parameter_list|()
block|{
return|return
name|publisherAcknowledgementsTimeout
return|;
block|}
DECL|method|setPublisherAcknowledgementsTimeout ( Long publisherAcknowledgementsTimeout)
specifier|public
name|void
name|setPublisherAcknowledgementsTimeout
parameter_list|(
name|Long
name|publisherAcknowledgementsTimeout
parameter_list|)
block|{
name|this
operator|.
name|publisherAcknowledgementsTimeout
operator|=
name|publisherAcknowledgementsTimeout
expr_stmt|;
block|}
DECL|method|getGuaranteedDeliveries ()
specifier|public
name|Boolean
name|getGuaranteedDeliveries
parameter_list|()
block|{
return|return
name|guaranteedDeliveries
return|;
block|}
DECL|method|setGuaranteedDeliveries (Boolean guaranteedDeliveries)
specifier|public
name|void
name|setGuaranteedDeliveries
parameter_list|(
name|Boolean
name|guaranteedDeliveries
parameter_list|)
block|{
name|this
operator|.
name|guaranteedDeliveries
operator|=
name|guaranteedDeliveries
expr_stmt|;
block|}
DECL|method|getMandatory ()
specifier|public
name|Boolean
name|getMandatory
parameter_list|()
block|{
return|return
name|mandatory
return|;
block|}
DECL|method|setMandatory (Boolean mandatory)
specifier|public
name|void
name|setMandatory
parameter_list|(
name|Boolean
name|mandatory
parameter_list|)
block|{
name|this
operator|.
name|mandatory
operator|=
name|mandatory
expr_stmt|;
block|}
DECL|method|getImmediate ()
specifier|public
name|Boolean
name|getImmediate
parameter_list|()
block|{
return|return
name|immediate
return|;
block|}
DECL|method|setImmediate (Boolean immediate)
specifier|public
name|void
name|setImmediate
parameter_list|(
name|Boolean
name|immediate
parameter_list|)
block|{
name|this
operator|.
name|immediate
operator|=
name|immediate
expr_stmt|;
block|}
DECL|method|getArgs ()
specifier|public
name|String
name|getArgs
parameter_list|()
block|{
return|return
name|args
return|;
block|}
DECL|method|setArgs (String args)
specifier|public
name|void
name|setArgs
parameter_list|(
name|String
name|args
parameter_list|)
block|{
name|this
operator|.
name|args
operator|=
name|args
expr_stmt|;
block|}
DECL|method|getClientProperties ()
specifier|public
name|String
name|getClientProperties
parameter_list|()
block|{
return|return
name|clientProperties
return|;
block|}
DECL|method|setClientProperties (String clientProperties)
specifier|public
name|void
name|setClientProperties
parameter_list|(
name|String
name|clientProperties
parameter_list|)
block|{
name|this
operator|.
name|clientProperties
operator|=
name|clientProperties
expr_stmt|;
block|}
DECL|method|getSslProtocol ()
specifier|public
name|String
name|getSslProtocol
parameter_list|()
block|{
return|return
name|sslProtocol
return|;
block|}
DECL|method|setSslProtocol (String sslProtocol)
specifier|public
name|void
name|setSslProtocol
parameter_list|(
name|String
name|sslProtocol
parameter_list|)
block|{
name|this
operator|.
name|sslProtocol
operator|=
name|sslProtocol
expr_stmt|;
block|}
DECL|method|getTrustManager ()
specifier|public
name|String
name|getTrustManager
parameter_list|()
block|{
return|return
name|trustManager
return|;
block|}
DECL|method|setTrustManager (String trustManager)
specifier|public
name|void
name|setTrustManager
parameter_list|(
name|String
name|trustManager
parameter_list|)
block|{
name|this
operator|.
name|trustManager
operator|=
name|trustManager
expr_stmt|;
block|}
DECL|method|getAutoAck ()
specifier|public
name|Boolean
name|getAutoAck
parameter_list|()
block|{
return|return
name|autoAck
return|;
block|}
DECL|method|setAutoAck (Boolean autoAck)
specifier|public
name|void
name|setAutoAck
parameter_list|(
name|Boolean
name|autoAck
parameter_list|)
block|{
name|this
operator|.
name|autoAck
operator|=
name|autoAck
expr_stmt|;
block|}
DECL|method|getAutoDelete ()
specifier|public
name|Boolean
name|getAutoDelete
parameter_list|()
block|{
return|return
name|autoDelete
return|;
block|}
DECL|method|setAutoDelete (Boolean autoDelete)
specifier|public
name|void
name|setAutoDelete
parameter_list|(
name|Boolean
name|autoDelete
parameter_list|)
block|{
name|this
operator|.
name|autoDelete
operator|=
name|autoDelete
expr_stmt|;
block|}
DECL|method|getDurable ()
specifier|public
name|Boolean
name|getDurable
parameter_list|()
block|{
return|return
name|durable
return|;
block|}
DECL|method|setDurable (Boolean durable)
specifier|public
name|void
name|setDurable
parameter_list|(
name|Boolean
name|durable
parameter_list|)
block|{
name|this
operator|.
name|durable
operator|=
name|durable
expr_stmt|;
block|}
DECL|method|getExclusive ()
specifier|public
name|Boolean
name|getExclusive
parameter_list|()
block|{
return|return
name|exclusive
return|;
block|}
DECL|method|setExclusive (Boolean exclusive)
specifier|public
name|void
name|setExclusive
parameter_list|(
name|Boolean
name|exclusive
parameter_list|)
block|{
name|this
operator|.
name|exclusive
operator|=
name|exclusive
expr_stmt|;
block|}
DECL|method|getExclusiveConsumer ()
specifier|public
name|Boolean
name|getExclusiveConsumer
parameter_list|()
block|{
return|return
name|exclusiveConsumer
return|;
block|}
DECL|method|setExclusiveConsumer (Boolean exclusiveConsumer)
specifier|public
name|void
name|setExclusiveConsumer
parameter_list|(
name|Boolean
name|exclusiveConsumer
parameter_list|)
block|{
name|this
operator|.
name|exclusiveConsumer
operator|=
name|exclusiveConsumer
expr_stmt|;
block|}
DECL|method|getPassive ()
specifier|public
name|Boolean
name|getPassive
parameter_list|()
block|{
return|return
name|passive
return|;
block|}
DECL|method|setPassive (Boolean passive)
specifier|public
name|void
name|setPassive
parameter_list|(
name|Boolean
name|passive
parameter_list|)
block|{
name|this
operator|.
name|passive
operator|=
name|passive
expr_stmt|;
block|}
DECL|method|getSkipQueueDeclare ()
specifier|public
name|Boolean
name|getSkipQueueDeclare
parameter_list|()
block|{
return|return
name|skipQueueDeclare
return|;
block|}
DECL|method|setSkipQueueDeclare (Boolean skipQueueDeclare)
specifier|public
name|void
name|setSkipQueueDeclare
parameter_list|(
name|Boolean
name|skipQueueDeclare
parameter_list|)
block|{
name|this
operator|.
name|skipQueueDeclare
operator|=
name|skipQueueDeclare
expr_stmt|;
block|}
DECL|method|getSkipQueueBind ()
specifier|public
name|Boolean
name|getSkipQueueBind
parameter_list|()
block|{
return|return
name|skipQueueBind
return|;
block|}
DECL|method|setSkipQueueBind (Boolean skipQueueBind)
specifier|public
name|void
name|setSkipQueueBind
parameter_list|(
name|Boolean
name|skipQueueBind
parameter_list|)
block|{
name|this
operator|.
name|skipQueueBind
operator|=
name|skipQueueBind
expr_stmt|;
block|}
DECL|method|getSkipExchangeDeclare ()
specifier|public
name|Boolean
name|getSkipExchangeDeclare
parameter_list|()
block|{
return|return
name|skipExchangeDeclare
return|;
block|}
DECL|method|setSkipExchangeDeclare (Boolean skipExchangeDeclare)
specifier|public
name|void
name|setSkipExchangeDeclare
parameter_list|(
name|Boolean
name|skipExchangeDeclare
parameter_list|)
block|{
name|this
operator|.
name|skipExchangeDeclare
operator|=
name|skipExchangeDeclare
expr_stmt|;
block|}
DECL|method|getDeclare ()
specifier|public
name|Boolean
name|getDeclare
parameter_list|()
block|{
return|return
name|declare
return|;
block|}
DECL|method|setDeclare (Boolean declare)
specifier|public
name|void
name|setDeclare
parameter_list|(
name|Boolean
name|declare
parameter_list|)
block|{
name|this
operator|.
name|declare
operator|=
name|declare
expr_stmt|;
block|}
DECL|method|getDeadLetterExchange ()
specifier|public
name|String
name|getDeadLetterExchange
parameter_list|()
block|{
return|return
name|deadLetterExchange
return|;
block|}
DECL|method|setDeadLetterExchange (String deadLetterExchange)
specifier|public
name|void
name|setDeadLetterExchange
parameter_list|(
name|String
name|deadLetterExchange
parameter_list|)
block|{
name|this
operator|.
name|deadLetterExchange
operator|=
name|deadLetterExchange
expr_stmt|;
block|}
DECL|method|getDeadLetterQueue ()
specifier|public
name|String
name|getDeadLetterQueue
parameter_list|()
block|{
return|return
name|deadLetterQueue
return|;
block|}
DECL|method|setDeadLetterQueue (String deadLetterQueue)
specifier|public
name|void
name|setDeadLetterQueue
parameter_list|(
name|String
name|deadLetterQueue
parameter_list|)
block|{
name|this
operator|.
name|deadLetterQueue
operator|=
name|deadLetterQueue
expr_stmt|;
block|}
DECL|method|getDeadLetterRoutingKey ()
specifier|public
name|String
name|getDeadLetterRoutingKey
parameter_list|()
block|{
return|return
name|deadLetterRoutingKey
return|;
block|}
DECL|method|setDeadLetterRoutingKey (String deadLetterRoutingKey)
specifier|public
name|void
name|setDeadLetterRoutingKey
parameter_list|(
name|String
name|deadLetterRoutingKey
parameter_list|)
block|{
name|this
operator|.
name|deadLetterRoutingKey
operator|=
name|deadLetterRoutingKey
expr_stmt|;
block|}
DECL|method|getDeadLetterExchangeType ()
specifier|public
name|String
name|getDeadLetterExchangeType
parameter_list|()
block|{
return|return
name|deadLetterExchangeType
return|;
block|}
DECL|method|setDeadLetterExchangeType (String deadLetterExchangeType)
specifier|public
name|void
name|setDeadLetterExchangeType
parameter_list|(
name|String
name|deadLetterExchangeType
parameter_list|)
block|{
name|this
operator|.
name|deadLetterExchangeType
operator|=
name|deadLetterExchangeType
expr_stmt|;
block|}
DECL|method|getResolvePropertyPlaceholders ()
specifier|public
name|Boolean
name|getResolvePropertyPlaceholders
parameter_list|()
block|{
return|return
name|resolvePropertyPlaceholders
return|;
block|}
DECL|method|setResolvePropertyPlaceholders ( Boolean resolvePropertyPlaceholders)
specifier|public
name|void
name|setResolvePropertyPlaceholders
parameter_list|(
name|Boolean
name|resolvePropertyPlaceholders
parameter_list|)
block|{
name|this
operator|.
name|resolvePropertyPlaceholders
operator|=
name|resolvePropertyPlaceholders
expr_stmt|;
block|}
block|}
end_class

end_unit

