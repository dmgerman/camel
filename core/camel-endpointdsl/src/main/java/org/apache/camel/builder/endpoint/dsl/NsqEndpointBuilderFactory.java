begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.endpoint.dsl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|endpoint
operator|.
name|dsl
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
name|builder
operator|.
name|EndpointConsumerBuilder
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
name|EndpointProducerBuilder
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
name|endpoint
operator|.
name|AbstractEndpointBuilder
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
name|ExceptionHandler
import|;
end_import

begin_comment
comment|/**  * Represents a nsq endpoint.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|NsqEndpointBuilderFactory
specifier|public
interface|interface
name|NsqEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint consumers for the NSQ component.      */
DECL|interface|NsqEndpointConsumerBuilder
specifier|public
interface|interface
name|NsqEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedNsqEndpointConsumerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedNsqEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * The name of topic we want to use.          *           * The option is a:<code>java.lang.String</code> type.          *           * Required: true          * Group: common          */
DECL|method|topic (String topic)
specifier|default
name|NsqEndpointConsumerBuilder
name|topic
parameter_list|(
name|String
name|topic
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"topic"
argument_list|,
name|topic
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * A String to identify the kind of client.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|userAgent (String userAgent)
specifier|default
name|NsqEndpointConsumerBuilder
name|userAgent
parameter_list|(
name|String
name|userAgent
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"userAgent"
argument_list|,
name|userAgent
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Automatically finish the NSQ message when it is retrieved from the          * quese and before the Exchange is processed.          *           * The option is a:<code>java.lang.Boolean</code> type.          *           * Group: consumer          */
DECL|method|autoFinish (Boolean autoFinish)
specifier|default
name|NsqEndpointConsumerBuilder
name|autoFinish
parameter_list|(
name|Boolean
name|autoFinish
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"autoFinish"
argument_list|,
name|autoFinish
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Automatically finish the NSQ message when it is retrieved from the          * quese and before the Exchange is processed.          *           * The option will be converted to a<code>java.lang.Boolean</code>          * type.          *           * Group: consumer          */
DECL|method|autoFinish (String autoFinish)
specifier|default
name|NsqEndpointConsumerBuilder
name|autoFinish
parameter_list|(
name|String
name|autoFinish
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"autoFinish"
argument_list|,
name|autoFinish
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows for bridging the consumer to the Camel routing Error Handler,          * which mean any exceptions occurred while the consumer is trying to          * pickup incoming messages, or the likes, will now be processed as a          * message and handled by the routing Error Handler. By default the          * consumer will use the org.apache.camel.spi.ExceptionHandler to deal          * with exceptions, that will be logged at WARN or ERROR level and          * ignored.          *           * The option is a:<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|bridgeErrorHandler ( boolean bridgeErrorHandler)
specifier|default
name|NsqEndpointConsumerBuilder
name|bridgeErrorHandler
parameter_list|(
name|boolean
name|bridgeErrorHandler
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"bridgeErrorHandler"
argument_list|,
name|bridgeErrorHandler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows for bridging the consumer to the Camel routing Error Handler,          * which mean any exceptions occurred while the consumer is trying to          * pickup incoming messages, or the likes, will now be processed as a          * message and handled by the routing Error Handler. By default the          * consumer will use the org.apache.camel.spi.ExceptionHandler to deal          * with exceptions, that will be logged at WARN or ERROR level and          * ignored.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: consumer          */
DECL|method|bridgeErrorHandler ( String bridgeErrorHandler)
specifier|default
name|NsqEndpointConsumerBuilder
name|bridgeErrorHandler
parameter_list|(
name|String
name|bridgeErrorHandler
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"bridgeErrorHandler"
argument_list|,
name|bridgeErrorHandler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The name of channel we want to use.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: consumer          */
DECL|method|channel (String channel)
specifier|default
name|NsqEndpointConsumerBuilder
name|channel
parameter_list|(
name|String
name|channel
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"channel"
argument_list|,
name|channel
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The lookup retry interval.          *           * The option is a:<code>long</code> type.          *           * Group: consumer          */
DECL|method|lookupInterval (long lookupInterval)
specifier|default
name|NsqEndpointConsumerBuilder
name|lookupInterval
parameter_list|(
name|long
name|lookupInterval
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"lookupInterval"
argument_list|,
name|lookupInterval
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The lookup retry interval.          *           * The option will be converted to a<code>long</code> type.          *           * Group: consumer          */
DECL|method|lookupInterval (String lookupInterval)
specifier|default
name|NsqEndpointConsumerBuilder
name|lookupInterval
parameter_list|(
name|String
name|lookupInterval
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"lookupInterval"
argument_list|,
name|lookupInterval
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The port of the nsqdlookupd server.          *           * The option is a:<code>int</code> type.          *           * Group: consumer          */
DECL|method|lookupServerPort (int lookupServerPort)
specifier|default
name|NsqEndpointConsumerBuilder
name|lookupServerPort
parameter_list|(
name|int
name|lookupServerPort
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"lookupServerPort"
argument_list|,
name|lookupServerPort
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The port of the nsqdlookupd server.          *           * The option will be converted to a<code>int</code> type.          *           * Group: consumer          */
DECL|method|lookupServerPort ( String lookupServerPort)
specifier|default
name|NsqEndpointConsumerBuilder
name|lookupServerPort
parameter_list|(
name|String
name|lookupServerPort
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"lookupServerPort"
argument_list|,
name|lookupServerPort
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The NSQ message timeout for a consumer.          *           * The option is a:<code>long</code> type.          *           * Group: consumer          */
DECL|method|messageTimeout (long messageTimeout)
specifier|default
name|NsqEndpointConsumerBuilder
name|messageTimeout
parameter_list|(
name|long
name|messageTimeout
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"messageTimeout"
argument_list|,
name|messageTimeout
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The NSQ message timeout for a consumer.          *           * The option will be converted to a<code>long</code> type.          *           * Group: consumer          */
DECL|method|messageTimeout (String messageTimeout)
specifier|default
name|NsqEndpointConsumerBuilder
name|messageTimeout
parameter_list|(
name|String
name|messageTimeout
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"messageTimeout"
argument_list|,
name|messageTimeout
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Consumer pool size.          *           * The option is a:<code>int</code> type.          *           * Group: consumer          */
DECL|method|poolSize (int poolSize)
specifier|default
name|NsqEndpointConsumerBuilder
name|poolSize
parameter_list|(
name|int
name|poolSize
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"poolSize"
argument_list|,
name|poolSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Consumer pool size.          *           * The option will be converted to a<code>int</code> type.          *           * Group: consumer          */
DECL|method|poolSize (String poolSize)
specifier|default
name|NsqEndpointConsumerBuilder
name|poolSize
parameter_list|(
name|String
name|poolSize
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"poolSize"
argument_list|,
name|poolSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The requeue interval.          *           * The option is a:<code>long</code> type.          *           * Group: consumer          */
DECL|method|requeueInterval (long requeueInterval)
specifier|default
name|NsqEndpointConsumerBuilder
name|requeueInterval
parameter_list|(
name|long
name|requeueInterval
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"requeueInterval"
argument_list|,
name|requeueInterval
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The requeue interval.          *           * The option will be converted to a<code>long</code> type.          *           * Group: consumer          */
DECL|method|requeueInterval ( String requeueInterval)
specifier|default
name|NsqEndpointConsumerBuilder
name|requeueInterval
parameter_list|(
name|String
name|requeueInterval
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"requeueInterval"
argument_list|,
name|requeueInterval
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Set secure option indicating TLS is required.          *           * The option is a:<code>boolean</code> type.          *           * Group: security          */
DECL|method|secure (boolean secure)
specifier|default
name|NsqEndpointConsumerBuilder
name|secure
parameter_list|(
name|boolean
name|secure
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"secure"
argument_list|,
name|secure
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Set secure option indicating TLS is required.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: security          */
DECL|method|secure (String secure)
specifier|default
name|NsqEndpointConsumerBuilder
name|secure
parameter_list|(
name|String
name|secure
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"secure"
argument_list|,
name|secure
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To configure security using SSLContextParameters.          *           * The option is a:          *<code>org.apache.camel.support.jsse.SSLContextParameters</code> type.          *           * Group: security          */
DECL|method|sslContextParameters ( Object sslContextParameters)
specifier|default
name|NsqEndpointConsumerBuilder
name|sslContextParameters
parameter_list|(
name|Object
name|sslContextParameters
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"sslContextParameters"
argument_list|,
name|sslContextParameters
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To configure security using SSLContextParameters.          *           * The option will be converted to a          *<code>org.apache.camel.support.jsse.SSLContextParameters</code> type.          *           * Group: security          */
DECL|method|sslContextParameters ( String sslContextParameters)
specifier|default
name|NsqEndpointConsumerBuilder
name|sslContextParameters
parameter_list|(
name|String
name|sslContextParameters
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"sslContextParameters"
argument_list|,
name|sslContextParameters
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint consumers for the NSQ component.      */
DECL|interface|AdvancedNsqEndpointConsumerBuilder
specifier|public
interface|interface
name|AdvancedNsqEndpointConsumerBuilder
extends|extends
name|EndpointConsumerBuilder
block|{
DECL|method|basic ()
specifier|default
name|NsqEndpointConsumerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|NsqEndpointConsumerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * To let the consumer use a custom ExceptionHandler. Notice if the          * option bridgeErrorHandler is enabled then this option is not in use.          * By default the consumer will deal with exceptions, that will be          * logged at WARN or ERROR level and ignored.          *           * The option is a:<code>org.apache.camel.spi.ExceptionHandler</code>          * type.          *           * Group: consumer (advanced)          */
DECL|method|exceptionHandler ( ExceptionHandler exceptionHandler)
specifier|default
name|AdvancedNsqEndpointConsumerBuilder
name|exceptionHandler
parameter_list|(
name|ExceptionHandler
name|exceptionHandler
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"exceptionHandler"
argument_list|,
name|exceptionHandler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To let the consumer use a custom ExceptionHandler. Notice if the          * option bridgeErrorHandler is enabled then this option is not in use.          * By default the consumer will deal with exceptions, that will be          * logged at WARN or ERROR level and ignored.          *           * The option will be converted to a          *<code>org.apache.camel.spi.ExceptionHandler</code> type.          *           * Group: consumer (advanced)          */
DECL|method|exceptionHandler ( String exceptionHandler)
specifier|default
name|AdvancedNsqEndpointConsumerBuilder
name|exceptionHandler
parameter_list|(
name|String
name|exceptionHandler
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"exceptionHandler"
argument_list|,
name|exceptionHandler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the exchange pattern when the consumer creates an exchange.          *           * The option is a:<code>org.apache.camel.ExchangePattern</code> type.          *           * Group: consumer (advanced)          */
DECL|method|exchangePattern ( ExchangePattern exchangePattern)
specifier|default
name|AdvancedNsqEndpointConsumerBuilder
name|exchangePattern
parameter_list|(
name|ExchangePattern
name|exchangePattern
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"exchangePattern"
argument_list|,
name|exchangePattern
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the exchange pattern when the consumer creates an exchange.          *           * The option will be converted to a          *<code>org.apache.camel.ExchangePattern</code> type.          *           * Group: consumer (advanced)          */
DECL|method|exchangePattern ( String exchangePattern)
specifier|default
name|AdvancedNsqEndpointConsumerBuilder
name|exchangePattern
parameter_list|(
name|String
name|exchangePattern
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"exchangePattern"
argument_list|,
name|exchangePattern
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedNsqEndpointConsumerBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedNsqEndpointConsumerBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous ( boolean synchronous)
specifier|default
name|AdvancedNsqEndpointConsumerBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous ( String synchronous)
specifier|default
name|AdvancedNsqEndpointConsumerBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Builder for endpoint producers for the NSQ component.      */
DECL|interface|NsqEndpointProducerBuilder
specifier|public
interface|interface
name|NsqEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedNsqEndpointProducerBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedNsqEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * The name of topic we want to use.          *           * The option is a:<code>java.lang.String</code> type.          *           * Required: true          * Group: common          */
DECL|method|topic (String topic)
specifier|default
name|NsqEndpointProducerBuilder
name|topic
parameter_list|(
name|String
name|topic
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"topic"
argument_list|,
name|topic
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * A String to identify the kind of client.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|userAgent (String userAgent)
specifier|default
name|NsqEndpointProducerBuilder
name|userAgent
parameter_list|(
name|String
name|userAgent
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"userAgent"
argument_list|,
name|userAgent
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|lazyStartProducer ( boolean lazyStartProducer)
specifier|default
name|NsqEndpointProducerBuilder
name|lazyStartProducer
parameter_list|(
name|boolean
name|lazyStartProducer
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"lazyStartProducer"
argument_list|,
name|lazyStartProducer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|lazyStartProducer ( String lazyStartProducer)
specifier|default
name|NsqEndpointProducerBuilder
name|lazyStartProducer
parameter_list|(
name|String
name|lazyStartProducer
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"lazyStartProducer"
argument_list|,
name|lazyStartProducer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The port of the nsqd server.          *           * The option is a:<code>int</code> type.          *           * Group: producer          */
DECL|method|port (int port)
specifier|default
name|NsqEndpointProducerBuilder
name|port
parameter_list|(
name|int
name|port
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"port"
argument_list|,
name|port
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The port of the nsqd server.          *           * The option will be converted to a<code>int</code> type.          *           * Group: producer          */
DECL|method|port (String port)
specifier|default
name|NsqEndpointProducerBuilder
name|port
parameter_list|(
name|String
name|port
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"port"
argument_list|,
name|port
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Set secure option indicating TLS is required.          *           * The option is a:<code>boolean</code> type.          *           * Group: security          */
DECL|method|secure (boolean secure)
specifier|default
name|NsqEndpointProducerBuilder
name|secure
parameter_list|(
name|boolean
name|secure
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"secure"
argument_list|,
name|secure
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Set secure option indicating TLS is required.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: security          */
DECL|method|secure (String secure)
specifier|default
name|NsqEndpointProducerBuilder
name|secure
parameter_list|(
name|String
name|secure
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"secure"
argument_list|,
name|secure
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To configure security using SSLContextParameters.          *           * The option is a:          *<code>org.apache.camel.support.jsse.SSLContextParameters</code> type.          *           * Group: security          */
DECL|method|sslContextParameters ( Object sslContextParameters)
specifier|default
name|NsqEndpointProducerBuilder
name|sslContextParameters
parameter_list|(
name|Object
name|sslContextParameters
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"sslContextParameters"
argument_list|,
name|sslContextParameters
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To configure security using SSLContextParameters.          *           * The option will be converted to a          *<code>org.apache.camel.support.jsse.SSLContextParameters</code> type.          *           * Group: security          */
DECL|method|sslContextParameters ( String sslContextParameters)
specifier|default
name|NsqEndpointProducerBuilder
name|sslContextParameters
parameter_list|(
name|String
name|sslContextParameters
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"sslContextParameters"
argument_list|,
name|sslContextParameters
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint producers for the NSQ component.      */
DECL|interface|AdvancedNsqEndpointProducerBuilder
specifier|public
interface|interface
name|AdvancedNsqEndpointProducerBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|NsqEndpointProducerBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|NsqEndpointProducerBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedNsqEndpointProducerBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedNsqEndpointProducerBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous ( boolean synchronous)
specifier|default
name|AdvancedNsqEndpointProducerBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous ( String synchronous)
specifier|default
name|AdvancedNsqEndpointProducerBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Builder for endpoint for the NSQ component.      */
DECL|interface|NsqEndpointBuilder
specifier|public
interface|interface
name|NsqEndpointBuilder
extends|extends
name|NsqEndpointConsumerBuilder
extends|,
name|NsqEndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedNsqEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedNsqEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * The name of topic we want to use.          *           * The option is a:<code>java.lang.String</code> type.          *           * Required: true          * Group: common          */
DECL|method|topic (String topic)
specifier|default
name|NsqEndpointBuilder
name|topic
parameter_list|(
name|String
name|topic
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"topic"
argument_list|,
name|topic
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * A String to identify the kind of client.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: common          */
DECL|method|userAgent (String userAgent)
specifier|default
name|NsqEndpointBuilder
name|userAgent
parameter_list|(
name|String
name|userAgent
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"userAgent"
argument_list|,
name|userAgent
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Set secure option indicating TLS is required.          *           * The option is a:<code>boolean</code> type.          *           * Group: security          */
DECL|method|secure (boolean secure)
specifier|default
name|NsqEndpointBuilder
name|secure
parameter_list|(
name|boolean
name|secure
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"secure"
argument_list|,
name|secure
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Set secure option indicating TLS is required.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: security          */
DECL|method|secure (String secure)
specifier|default
name|NsqEndpointBuilder
name|secure
parameter_list|(
name|String
name|secure
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"secure"
argument_list|,
name|secure
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To configure security using SSLContextParameters.          *           * The option is a:          *<code>org.apache.camel.support.jsse.SSLContextParameters</code> type.          *           * Group: security          */
DECL|method|sslContextParameters ( Object sslContextParameters)
specifier|default
name|NsqEndpointBuilder
name|sslContextParameters
parameter_list|(
name|Object
name|sslContextParameters
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"sslContextParameters"
argument_list|,
name|sslContextParameters
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To configure security using SSLContextParameters.          *           * The option will be converted to a          *<code>org.apache.camel.support.jsse.SSLContextParameters</code> type.          *           * Group: security          */
DECL|method|sslContextParameters ( String sslContextParameters)
specifier|default
name|NsqEndpointBuilder
name|sslContextParameters
parameter_list|(
name|String
name|sslContextParameters
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"sslContextParameters"
argument_list|,
name|sslContextParameters
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the NSQ component.      */
DECL|interface|AdvancedNsqEndpointBuilder
specifier|public
interface|interface
name|AdvancedNsqEndpointBuilder
extends|extends
name|AdvancedNsqEndpointConsumerBuilder
extends|,
name|AdvancedNsqEndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|NsqEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|NsqEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedNsqEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedNsqEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous (boolean synchronous)
specifier|default
name|AdvancedNsqEndpointBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous (String synchronous)
specifier|default
name|AdvancedNsqEndpointBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * NSQ (camel-nsq)      * Represents a nsq endpoint.      *       * Syntax:<code>nsq:servers</code>      * Category: messaging      * Available as of version: 2.23      * Maven coordinates: org.apache.camel:camel-nsq      */
DECL|method|nsq (String path)
specifier|default
name|NsqEndpointBuilder
name|nsq
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|NsqEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|NsqEndpointBuilder
implements|,
name|AdvancedNsqEndpointBuilder
block|{
specifier|public
name|NsqEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"nsq"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|NsqEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

