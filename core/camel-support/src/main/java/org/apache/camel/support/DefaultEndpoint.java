begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.support
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|support
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|AsyncProducer
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
name|CamelContextAware
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
name|PollingConsumer
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
name|HasId
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
name|PropertyConfigurer
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
name|PropertyConfigurerAware
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
name|support
operator|.
name|service
operator|.
name|ServiceSupport
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
name|camel
operator|.
name|util
operator|.
name|StringHelper
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
name|URISupport
import|;
end_import

begin_comment
comment|/**  * A default endpoint useful for implementation inheritance.  *<p/>  * Components which leverages<a  * href="http://camel.apache.org/asynchronous-routing-engine.html">asynchronous  * processing model</a> should check the {@link #isSynchronous()} to determine  * if asynchronous processing is allowed. The<tt>synchronous</tt> option on the  * endpoint allows Camel end users to dictate whether they want the asynchronous  * model or not. The option is default<tt>false</tt> which means asynchronous  * processing is allowed.  */
end_comment

begin_class
DECL|class|DefaultEndpoint
specifier|public
specifier|abstract
class|class
name|DefaultEndpoint
extends|extends
name|ServiceSupport
implements|implements
name|Endpoint
implements|,
name|HasId
implements|,
name|CamelContextAware
block|{
DECL|field|id
specifier|private
specifier|final
name|String
name|id
init|=
name|EndpointHelper
operator|.
name|createEndpointId
argument_list|()
decl_stmt|;
DECL|field|endpointUriToString
specifier|private
specifier|transient
name|String
name|endpointUriToString
decl_stmt|;
DECL|field|endpointUri
specifier|private
name|String
name|endpointUri
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|component
specifier|private
name|Component
name|component
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"producer"
argument_list|,
name|description
operator|=
literal|"Whether the producer should be started lazy (on the first message). By starting lazy you can use this to allow CamelContext and routes to startup"
operator|+
literal|" in situations where a producer may otherwise fail during starting and cause the route to fail being started. By deferring this startup to be lazy then"
operator|+
literal|" the startup failure can be handled during routing messages via Camel's routing error handlers. Beware that when the first message is processed"
operator|+
literal|" then creating and starting the producer may take a little time and prolong the total processing time of the processing."
argument_list|)
DECL|field|lazyStartProducer
specifier|private
name|boolean
name|lazyStartProducer
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|optionalPrefix
operator|=
literal|"consumer."
argument_list|,
name|description
operator|=
literal|"Allows for bridging the consumer to the Camel routing Error Handler, which mean any exceptions occurred while"
operator|+
literal|" the consumer is trying to pickup incoming messages, or the likes, will now be processed as a message and handled by the routing Error Handler."
operator|+
literal|" By default the consumer will use the org.apache.camel.spi.ExceptionHandler to deal with exceptions, that will be logged at WARN or ERROR level and ignored."
argument_list|)
DECL|field|bridgeErrorHandler
specifier|private
name|boolean
name|bridgeErrorHandler
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer,advanced"
argument_list|,
name|optionalPrefix
operator|=
literal|"consumer."
argument_list|,
name|description
operator|=
literal|"To let the consumer use a custom ExceptionHandler."
operator|+
literal|" Notice if the option bridgeErrorHandler is enabled then this option is not in use."
operator|+
literal|" By default the consumer will deal with exceptions, that will be logged at WARN or ERROR level and ignored."
argument_list|)
DECL|field|exceptionHandler
specifier|private
name|ExceptionHandler
name|exceptionHandler
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer,advanced"
argument_list|,
name|description
operator|=
literal|"Sets the exchange pattern when the consumer creates an exchange."
argument_list|)
comment|// no default value set on @UriParam as the MEP is sometimes InOnly or InOut depending on the component in use
DECL|field|exchangePattern
specifier|private
name|ExchangePattern
name|exchangePattern
init|=
name|ExchangePattern
operator|.
name|InOnly
decl_stmt|;
comment|// option to allow end user to dictate whether async processing should be
comment|// used or not (if possible)
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|,
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"Sets whether synchronous processing should be strictly used, or Camel is allowed to use asynchronous processing (if supported)."
argument_list|)
DECL|field|synchronous
specifier|private
name|boolean
name|synchronous
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|,
name|description
operator|=
literal|"Whether the endpoint should use basic property binding (Camel 2.x) or the newer property binding with additional capabilities"
argument_list|)
DECL|field|basicPropertyBinding
specifier|private
name|boolean
name|basicPropertyBinding
decl_stmt|;
comment|// pooling consumer options only related to EventDrivenPollingConsumer which are very seldom in use
comment|// so lets not expose them in the component docs as it will be included in every component
DECL|field|pollingConsumerQueueSize
specifier|private
name|int
name|pollingConsumerQueueSize
init|=
literal|1000
decl_stmt|;
DECL|field|pollingConsumerBlockWhenFull
specifier|private
name|boolean
name|pollingConsumerBlockWhenFull
init|=
literal|true
decl_stmt|;
DECL|field|pollingConsumerBlockTimeout
specifier|private
name|long
name|pollingConsumerBlockTimeout
decl_stmt|;
comment|/**      * Constructs a fully-initialized DefaultEndpoint instance. This is the      * preferred method of constructing an object from Java code (as opposed to      * Spring beans, etc.).      *       * @param endpointUri the full URI used to create this endpoint      * @param component the component that created this endpoint      */
DECL|method|DefaultEndpoint (String endpointUri, Component component)
specifier|protected
name|DefaultEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|component
operator|==
literal|null
condition|?
literal|null
else|:
name|component
operator|.
name|getCamelContext
argument_list|()
expr_stmt|;
name|this
operator|.
name|component
operator|=
name|component
expr_stmt|;
name|this
operator|.
name|setEndpointUri
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructs a partially-initialized DefaultEndpoint instance. Useful when      * creating endpoints manually (e.g., as beans in Spring).      *<p/>      * Please note that the endpoint URI must be set through properties (or      * overriding {@link #createEndpointUri()} if one uses this constructor.      *<p/>      *<b>Note:</b> It is preferred to create endpoints using the associated      * component.      */
DECL|method|DefaultEndpoint ()
specifier|protected
name|DefaultEndpoint
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|getEndpointUri
argument_list|()
operator|.
name|hashCode
argument_list|()
operator|*
literal|37
operator|+
literal|1
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object object)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|object
operator|instanceof
name|DefaultEndpoint
condition|)
block|{
name|DefaultEndpoint
name|that
init|=
operator|(
name|DefaultEndpoint
operator|)
name|object
decl_stmt|;
comment|// must also match the same CamelContext in case we compare endpoints from different contexts
name|String
name|thisContextName
init|=
name|this
operator|.
name|getCamelContext
argument_list|()
operator|!=
literal|null
condition|?
name|this
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getName
argument_list|()
else|:
literal|null
decl_stmt|;
name|String
name|thatContextName
init|=
name|that
operator|.
name|getCamelContext
argument_list|()
operator|!=
literal|null
condition|?
name|that
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getName
argument_list|()
else|:
literal|null
decl_stmt|;
return|return
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|this
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
name|that
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|equal
argument_list|(
name|thisContextName
argument_list|,
name|thatContextName
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|endpointUriToString
operator|==
literal|null
condition|)
block|{
name|String
name|value
init|=
literal|null
decl_stmt|;
try|try
block|{
name|value
operator|=
name|getEndpointUri
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
comment|// ignore any exception and use null for building the string value
block|}
comment|// ensure to sanitize uri so we do not show sensitive information such as passwords
name|endpointUriToString
operator|=
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|endpointUriToString
return|;
block|}
comment|/**      * Returns a unique String ID which can be used for aliasing without having      * to use the whole URI which is not unique      */
annotation|@
name|Override
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
annotation|@
name|Override
DECL|method|getEndpointUri ()
specifier|public
name|String
name|getEndpointUri
parameter_list|()
block|{
if|if
condition|(
name|endpointUri
operator|==
literal|null
condition|)
block|{
name|endpointUri
operator|=
name|createEndpointUri
argument_list|()
expr_stmt|;
if|if
condition|(
name|endpointUri
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"endpointUri is not specified and "
operator|+
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" does not implement createEndpointUri() to create a default value"
argument_list|)
throw|;
block|}
block|}
return|return
name|endpointUri
return|;
block|}
annotation|@
name|Override
DECL|method|getEndpointKey ()
specifier|public
name|String
name|getEndpointKey
parameter_list|()
block|{
if|if
condition|(
name|isLenientProperties
argument_list|()
condition|)
block|{
comment|// only use the endpoint uri without parameters as the properties are lenient
name|String
name|uri
init|=
name|getEndpointUri
argument_list|()
decl_stmt|;
if|if
condition|(
name|uri
operator|.
name|indexOf
argument_list|(
literal|'?'
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
return|return
name|StringHelper
operator|.
name|before
argument_list|(
name|uri
argument_list|,
literal|"?"
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|uri
return|;
block|}
block|}
else|else
block|{
comment|// use the full endpoint uri
return|return
name|getEndpointUri
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
annotation|@
name|Override
DECL|method|createAsyncProducer ()
specifier|public
name|AsyncProducer
name|createAsyncProducer
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|isLazyStartProducer
argument_list|()
condition|)
block|{
return|return
operator|new
name|LazyStartProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|AsyncProcessorConverterHelper
operator|.
name|convert
argument_list|(
name|createProducer
argument_list|()
argument_list|)
return|;
block|}
block|}
comment|/**      * Returns the component that created this endpoint, or<tt>null</tt> if none set.      */
DECL|method|getComponent ()
specifier|public
name|Component
name|getComponent
parameter_list|()
block|{
return|return
name|component
return|;
block|}
annotation|@
name|Override
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createPollingConsumer ()
specifier|public
name|PollingConsumer
name|createPollingConsumer
parameter_list|()
throws|throws
name|Exception
block|{
comment|// should not call configurePollingConsumer when its EventDrivenPollingConsumer
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Creating EventDrivenPollingConsumer with queueSize: {} blockWhenFull: {} blockTimeout: {}"
argument_list|,
operator|new
name|Object
index|[]
block|{
name|getPollingConsumerQueueSize
argument_list|()
block|,
name|isPollingConsumerBlockWhenFull
argument_list|()
block|,
name|getPollingConsumerBlockTimeout
argument_list|()
block|}
argument_list|)
expr_stmt|;
block|}
name|EventDrivenPollingConsumer
name|consumer
init|=
operator|new
name|EventDrivenPollingConsumer
argument_list|(
name|this
argument_list|,
name|getPollingConsumerQueueSize
argument_list|()
argument_list|)
decl_stmt|;
name|consumer
operator|.
name|setBlockWhenFull
argument_list|(
name|isPollingConsumerBlockWhenFull
argument_list|()
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|setBlockTimeout
argument_list|(
name|getPollingConsumerBlockTimeout
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
annotation|@
name|Override
DECL|method|createExchange ()
specifier|public
name|Exchange
name|createExchange
parameter_list|()
block|{
return|return
name|createExchange
argument_list|(
name|getExchangePattern
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createExchange (ExchangePattern pattern)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|)
block|{
return|return
operator|new
name|DefaultExchange
argument_list|(
name|this
argument_list|,
name|pattern
argument_list|)
return|;
block|}
comment|/**      * Returns the default exchange pattern to use when creating an exchange.      */
DECL|method|getExchangePattern ()
specifier|public
name|ExchangePattern
name|getExchangePattern
parameter_list|()
block|{
return|return
name|exchangePattern
return|;
block|}
comment|/**      * Sets the default exchange pattern when creating an exchange.      */
DECL|method|setExchangePattern (ExchangePattern exchangePattern)
specifier|public
name|void
name|setExchangePattern
parameter_list|(
name|ExchangePattern
name|exchangePattern
parameter_list|)
block|{
name|this
operator|.
name|exchangePattern
operator|=
name|exchangePattern
expr_stmt|;
block|}
comment|/**      * Returns whether synchronous processing should be strictly used.      */
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
comment|/**      * Sets whether synchronous processing should be strictly used, or Camel is      * allowed to use asynchronous processing (if supported).      */
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
comment|/**      * Whether the endpoint should use basic property binding (Camel 2.x) or the newer property binding with additional capabilities.      */
DECL|method|isBasicPropertyBinding ()
specifier|public
name|boolean
name|isBasicPropertyBinding
parameter_list|()
block|{
return|return
name|basicPropertyBinding
return|;
block|}
comment|/**      * Whether the endpoint should use basic property binding (Camel 2.x) or the newer property binding with additional capabilities.      */
DECL|method|setBasicPropertyBinding (boolean basicPropertyBinding)
specifier|public
name|void
name|setBasicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|this
operator|.
name|basicPropertyBinding
operator|=
name|basicPropertyBinding
expr_stmt|;
block|}
DECL|method|isLazyStartProducer ()
specifier|public
name|boolean
name|isLazyStartProducer
parameter_list|()
block|{
return|return
name|lazyStartProducer
return|;
block|}
comment|/**      * Whether the producer should be started lazy (on the first message). By starting lazy you can use this to allow CamelContext and routes to startup      * in situations where a producer may otherwise fail during starting and cause the route to fail being started. By deferring this startup to be lazy then      * the startup failure can be handled during routing messages via Camel's routing error handlers. Beware that when the first message is processed      * then creating and starting the producer may take a little time and prolong the total processing time of the processing.      */
DECL|method|setLazyStartProducer (boolean lazyStartProducer)
specifier|public
name|void
name|setLazyStartProducer
parameter_list|(
name|boolean
name|lazyStartProducer
parameter_list|)
block|{
name|this
operator|.
name|lazyStartProducer
operator|=
name|lazyStartProducer
expr_stmt|;
block|}
DECL|method|isBridgeErrorHandler ()
specifier|public
name|boolean
name|isBridgeErrorHandler
parameter_list|()
block|{
return|return
name|bridgeErrorHandler
return|;
block|}
comment|/**      * Allows for bridging the consumer to the Camel routing Error Handler, which mean any exceptions occurred while      * the consumer is trying to pickup incoming messages, or the likes, will now be processed as a message and      * handled by the routing Error Handler.      *<p/>      * By default the consumer will use the org.apache.camel.spi.ExceptionHandler to deal with exceptions,      * that will be logged at WARN/ERROR level and ignored.      */
DECL|method|setBridgeErrorHandler (boolean bridgeErrorHandler)
specifier|public
name|void
name|setBridgeErrorHandler
parameter_list|(
name|boolean
name|bridgeErrorHandler
parameter_list|)
block|{
name|this
operator|.
name|bridgeErrorHandler
operator|=
name|bridgeErrorHandler
expr_stmt|;
block|}
DECL|method|getExceptionHandler ()
specifier|public
name|ExceptionHandler
name|getExceptionHandler
parameter_list|()
block|{
return|return
name|exceptionHandler
return|;
block|}
comment|/**      * To let the consumer use a custom ExceptionHandler.      + Notice if the option bridgeErrorHandler is enabled then this options is not in use.      + By default the consumer will deal with exceptions, that will be logged at WARN/ERROR level and ignored.      */
DECL|method|setExceptionHandler (ExceptionHandler exceptionHandler)
specifier|public
name|void
name|setExceptionHandler
parameter_list|(
name|ExceptionHandler
name|exceptionHandler
parameter_list|)
block|{
name|this
operator|.
name|exceptionHandler
operator|=
name|exceptionHandler
expr_stmt|;
block|}
comment|/**      * Gets the {@link org.apache.camel.PollingConsumer} queue size, when {@link EventDrivenPollingConsumer}      * is being used. Notice some Camel components may have their own implementation of {@link org.apache.camel.PollingConsumer} and      * therefore not using the default {@link EventDrivenPollingConsumer} implementation.      *<p/>      * The default value is<tt>1000</tt>      */
DECL|method|getPollingConsumerQueueSize ()
specifier|public
name|int
name|getPollingConsumerQueueSize
parameter_list|()
block|{
return|return
name|pollingConsumerQueueSize
return|;
block|}
comment|/**      * Sets the {@link org.apache.camel.PollingConsumer} queue size, when {@link EventDrivenPollingConsumer}      * is being used. Notice some Camel components may have their own implementation of {@link org.apache.camel.PollingConsumer} and      * therefore not using the default {@link EventDrivenPollingConsumer} implementation.      *<p/>      * The default value is<tt>1000</tt>      */
DECL|method|setPollingConsumerQueueSize (int pollingConsumerQueueSize)
specifier|public
name|void
name|setPollingConsumerQueueSize
parameter_list|(
name|int
name|pollingConsumerQueueSize
parameter_list|)
block|{
name|this
operator|.
name|pollingConsumerQueueSize
operator|=
name|pollingConsumerQueueSize
expr_stmt|;
block|}
comment|/**      * Whether to block when adding to the internal queue off when {@link EventDrivenPollingConsumer}      * is being used. Notice some Camel components may have their own implementation of {@link org.apache.camel.PollingConsumer} and      * therefore not using the default {@link EventDrivenPollingConsumer} implementation.      *<p/>      * Setting this option to<tt>false</tt>, will result in an {@link java.lang.IllegalStateException} being thrown      * when trying to add to the queue, and its full.      *<p/>      * The default value is<tt>true</tt> which will block the producer queue until the queue has space.      */
DECL|method|isPollingConsumerBlockWhenFull ()
specifier|public
name|boolean
name|isPollingConsumerBlockWhenFull
parameter_list|()
block|{
return|return
name|pollingConsumerBlockWhenFull
return|;
block|}
comment|/**      * Set whether to block when adding to the internal queue off when {@link EventDrivenPollingConsumer}      * is being used. Notice some Camel components may have their own implementation of {@link org.apache.camel.PollingConsumer} and      * therefore not using the default {@link EventDrivenPollingConsumer} implementation.      *<p/>      * Setting this option to<tt>false</tt>, will result in an {@link java.lang.IllegalStateException} being thrown      * when trying to add to the queue, and its full.      *<p/>      * The default value is<tt>true</tt> which will block the producer queue until the queue has space.      */
DECL|method|setPollingConsumerBlockWhenFull (boolean pollingConsumerBlockWhenFull)
specifier|public
name|void
name|setPollingConsumerBlockWhenFull
parameter_list|(
name|boolean
name|pollingConsumerBlockWhenFull
parameter_list|)
block|{
name|this
operator|.
name|pollingConsumerBlockWhenFull
operator|=
name|pollingConsumerBlockWhenFull
expr_stmt|;
block|}
comment|/**      * Sets the timeout in millis to use when adding to the internal queue off when {@link EventDrivenPollingConsumer}      * is being used.      *      * @see #setPollingConsumerBlockWhenFull(boolean)      */
DECL|method|getPollingConsumerBlockTimeout ()
specifier|public
name|long
name|getPollingConsumerBlockTimeout
parameter_list|()
block|{
return|return
name|pollingConsumerBlockTimeout
return|;
block|}
comment|/**      * Sets the timeout in millis to use when adding to the internal queue off when {@link EventDrivenPollingConsumer}      * is being used.      *      * @see #setPollingConsumerBlockWhenFull(boolean)      */
DECL|method|setPollingConsumerBlockTimeout (long pollingConsumerBlockTimeout)
specifier|public
name|void
name|setPollingConsumerBlockTimeout
parameter_list|(
name|long
name|pollingConsumerBlockTimeout
parameter_list|)
block|{
name|this
operator|.
name|pollingConsumerBlockTimeout
operator|=
name|pollingConsumerBlockTimeout
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configureProperties (Map<String, Object> options)
specifier|public
name|void
name|configureProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
parameter_list|)
block|{
comment|// noop
block|}
comment|/**      * Sets the bean properties on the given bean.      *<p/>      * This is the same logical implementation as {@link DefaultComponent#setProperties(Object, java.util.Map)}      *      * @param bean        the bean      * @param parameters  properties to set      */
DECL|method|setProperties (Object bean, Map<String, Object> parameters)
specifier|public
name|void
name|setProperties
parameter_list|(
name|Object
name|bean
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
if|if
condition|(
name|parameters
operator|==
literal|null
operator|||
name|parameters
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|boolean
name|basic
init|=
name|basicPropertyBinding
operator|||
literal|"true"
operator|.
name|equals
argument_list|(
name|parameters
operator|.
name|getOrDefault
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
literal|"false"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|basic
condition|)
block|{
comment|// use basic binding
name|PropertyBindingSupport
operator|.
name|build
argument_list|()
operator|.
name|withPlaceholder
argument_list|(
literal|false
argument_list|)
operator|.
name|withNesting
argument_list|(
literal|false
argument_list|)
operator|.
name|withDeepNesting
argument_list|(
literal|false
argument_list|)
operator|.
name|withReference
argument_list|(
literal|false
argument_list|)
operator|.
name|bind
argument_list|(
name|camelContext
argument_list|,
name|bean
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|PropertyConfigurer
name|configurer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|bean
operator|instanceof
name|Component
condition|)
block|{
name|configurer
operator|=
name|getComponent
argument_list|()
operator|.
name|getComponentPropertyConfigurer
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|bean
operator|instanceof
name|Endpoint
condition|)
block|{
name|configurer
operator|=
name|getComponent
argument_list|()
operator|.
name|getEndpointPropertyConfigurer
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|bean
operator|instanceof
name|PropertyConfigurerAware
condition|)
block|{
name|configurer
operator|=
operator|(
operator|(
name|PropertyConfigurerAware
operator|)
name|bean
operator|)
operator|.
name|getPropertyConfigurer
argument_list|(
name|bean
argument_list|)
expr_stmt|;
block|}
comment|// use advanced binding
name|PropertyBindingSupport
operator|.
name|build
argument_list|()
operator|.
name|withConfigurer
argument_list|(
name|configurer
argument_list|)
operator|.
name|bind
argument_list|(
name|camelContext
argument_list|,
name|bean
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * A factory method to lazily create the endpointUri if none is specified      */
DECL|method|createEndpointUri ()
specifier|protected
name|String
name|createEndpointUri
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/**      * Sets the endpointUri if it has not been specified yet via some kind of      * dependency injection mechanism. This allows dependency injection      * frameworks such as Spring or Guice to set the default endpoint URI in      * cases where it has not been explicitly configured using the name/context      * in which an Endpoint is created.      */
DECL|method|setEndpointUriIfNotSpecified (String value)
specifier|public
name|void
name|setEndpointUriIfNotSpecified
parameter_list|(
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|endpointUri
operator|==
literal|null
condition|)
block|{
name|setEndpointUri
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Sets the URI that created this endpoint.      */
DECL|method|setEndpointUri (String endpointUri)
specifier|protected
name|void
name|setEndpointUri
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
name|this
operator|.
name|endpointUri
operator|=
name|endpointUri
expr_stmt|;
block|}
comment|/**      * An endpoint should favour to be singleton by default, only in some rare special cases      * can an endpoint be non-singleton. This implementation is singleton and this method returns true.      */
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
annotation|@
name|Override
DECL|method|isLenientProperties ()
specifier|public
name|boolean
name|isLenientProperties
parameter_list|()
block|{
comment|// default should be false for most components
return|return
literal|false
return|;
block|}
DECL|method|configureConsumer (Consumer consumer)
specifier|protected
name|void
name|configureConsumer
parameter_list|(
name|Consumer
name|consumer
parameter_list|)
throws|throws
name|Exception
block|{
comment|// inject CamelContext
if|if
condition|(
name|consumer
operator|instanceof
name|CamelContextAware
condition|)
block|{
operator|(
operator|(
name|CamelContextAware
operator|)
name|consumer
operator|)
operator|.
name|setCamelContext
argument_list|(
name|getCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|bridgeErrorHandler
condition|)
block|{
if|if
condition|(
name|consumer
operator|instanceof
name|DefaultConsumer
condition|)
block|{
name|DefaultConsumer
name|defaultConsumer
init|=
operator|(
name|DefaultConsumer
operator|)
name|consumer
decl_stmt|;
name|defaultConsumer
operator|.
name|setExceptionHandler
argument_list|(
operator|new
name|BridgeExceptionHandlerToErrorHandler
argument_list|(
name|defaultConsumer
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Option bridgeErrorHandler is only supported by endpoints,"
operator|+
literal|" having their consumer extend DefaultConsumer. The consumer is a "
operator|+
name|consumer
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" class."
argument_list|)
throw|;
block|}
block|}
if|if
condition|(
name|exceptionHandler
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|consumer
operator|instanceof
name|DefaultConsumer
condition|)
block|{
name|DefaultConsumer
name|defaultConsumer
init|=
operator|(
name|DefaultConsumer
operator|)
name|consumer
decl_stmt|;
name|defaultConsumer
operator|.
name|setExceptionHandler
argument_list|(
name|exceptionHandler
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|configurePollingConsumer (PollingConsumer consumer)
specifier|protected
name|void
name|configurePollingConsumer
parameter_list|(
name|PollingConsumer
name|consumer
parameter_list|)
throws|throws
name|Exception
block|{
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doInit ()
specifier|protected
name|void
name|doInit
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
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
comment|// noop
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
comment|// noop
block|}
block|}
end_class

end_unit

