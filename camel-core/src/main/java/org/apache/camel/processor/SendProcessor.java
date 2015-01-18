begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|AsyncProcessor
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
name|AsyncProducerCallback
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
name|ServicePoolAware
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
name|Traceable
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
name|InterceptSendToEndpoint
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
name|ProducerCache
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
name|AsyncProcessorConverterHelper
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
name|AsyncProcessorHelper
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
name|EndpointHelper
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
name|EventHelper
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
name|ServiceHelper
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
name|StopWatch
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
comment|/**  * Processor for forwarding exchanges to an endpoint destination.  *  * @version   */
end_comment

begin_class
DECL|class|SendProcessor
specifier|public
class|class
name|SendProcessor
extends|extends
name|ServiceSupport
implements|implements
name|AsyncProcessor
implements|,
name|Traceable
implements|,
name|EndpointAware
block|{
DECL|field|LOG
specifier|protected
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SendProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|camelContext
specifier|protected
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|pattern
specifier|protected
specifier|final
name|ExchangePattern
name|pattern
decl_stmt|;
DECL|field|producerCache
specifier|protected
name|ProducerCache
name|producerCache
decl_stmt|;
DECL|field|producer
specifier|protected
name|AsyncProcessor
name|producer
decl_stmt|;
DECL|field|destination
specifier|protected
name|Endpoint
name|destination
decl_stmt|;
DECL|field|destinationExchangePattern
specifier|protected
name|ExchangePattern
name|destinationExchangePattern
decl_stmt|;
DECL|field|unhandleException
specifier|protected
specifier|final
name|boolean
name|unhandleException
decl_stmt|;
DECL|method|SendProcessor (Endpoint destination)
specifier|public
name|SendProcessor
parameter_list|(
name|Endpoint
name|destination
parameter_list|)
block|{
name|this
argument_list|(
name|destination
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|SendProcessor (Endpoint destination, ExchangePattern pattern)
specifier|public
name|SendProcessor
parameter_list|(
name|Endpoint
name|destination
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|)
block|{
name|this
argument_list|(
name|destination
argument_list|,
name|pattern
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|SendProcessor (Endpoint destination, ExchangePattern pattern, boolean unhandleException)
specifier|public
name|SendProcessor
parameter_list|(
name|Endpoint
name|destination
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|,
name|boolean
name|unhandleException
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|destination
argument_list|,
literal|"destination"
argument_list|)
expr_stmt|;
name|this
operator|.
name|destination
operator|=
name|destination
expr_stmt|;
name|this
operator|.
name|camelContext
operator|=
name|destination
operator|.
name|getCamelContext
argument_list|()
expr_stmt|;
name|this
operator|.
name|pattern
operator|=
name|pattern
expr_stmt|;
name|this
operator|.
name|unhandleException
operator|=
name|unhandleException
expr_stmt|;
try|try
block|{
name|this
operator|.
name|destinationExchangePattern
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|destinationExchangePattern
operator|=
name|EndpointHelper
operator|.
name|resolveExchangePatternFromUrl
argument_list|(
name|destination
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|this
operator|.
name|camelContext
argument_list|,
literal|"camelContext"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"sendTo("
operator|+
name|destination
operator|+
operator|(
name|pattern
operator|!=
literal|null
condition|?
literal|" "
operator|+
name|pattern
else|:
literal|""
operator|)
operator|+
literal|")"
return|;
block|}
comment|/**      * @deprecated not longer supported.      */
annotation|@
name|Deprecated
DECL|method|setDestination (Endpoint destination)
specifier|public
name|void
name|setDestination
parameter_list|(
name|Endpoint
name|destination
parameter_list|)
block|{     }
DECL|method|getTraceLabel ()
specifier|public
name|String
name|getTraceLabel
parameter_list|()
block|{
return|return
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|destination
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|destination
return|;
block|}
DECL|method|process (final Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|AsyncProcessorHelper
operator|.
name|process
argument_list|(
name|this
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|process (Exchange exchange, final AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isStarted
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|IllegalStateException
argument_list|(
literal|"SendProcessor has not been started: "
operator|+
name|this
argument_list|)
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|// we should preserve existing MEP so remember old MEP
comment|// if you want to permanently to change the MEP then use .setExchangePattern in the DSL
specifier|final
name|ExchangePattern
name|existingPattern
init|=
name|exchange
operator|.
name|getPattern
argument_list|()
decl_stmt|;
comment|// if we have a producer then use that as its optimized
if|if
condition|(
name|producer
operator|!=
literal|null
condition|)
block|{
comment|// record timing for sending the exchange using the producer
specifier|final
name|StopWatch
name|watch
init|=
operator|new
name|StopWatch
argument_list|()
decl_stmt|;
specifier|final
name|Exchange
name|target
init|=
name|configureExchange
argument_list|(
name|exchange
argument_list|,
name|pattern
argument_list|)
decl_stmt|;
name|EventHelper
operator|.
name|notifyExchangeSending
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|target
argument_list|,
name|destination
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|">>>> {} {}"
argument_list|,
name|destination
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|boolean
name|sync
init|=
literal|true
decl_stmt|;
try|try
block|{
name|sync
operator|=
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
try|try
block|{
comment|// restore previous MEP
name|target
operator|.
name|setPattern
argument_list|(
name|existingPattern
argument_list|)
expr_stmt|;
comment|// emit event that the exchange was sent to the endpoint
name|long
name|timeTaken
init|=
name|watch
operator|.
name|stop
argument_list|()
decl_stmt|;
name|EventHelper
operator|.
name|notifyExchangeSent
argument_list|(
name|target
operator|.
name|getContext
argument_list|()
argument_list|,
name|target
argument_list|,
name|destination
argument_list|,
name|timeTaken
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|checkException
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
name|doneSync
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|throwable
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|throwable
argument_list|)
expr_stmt|;
name|checkException
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
name|sync
argument_list|)
expr_stmt|;
block|}
return|return
name|sync
return|;
block|}
comment|// send the exchange to the destination using the producer cache for the non optimized producers
return|return
name|producerCache
operator|.
name|doInAsyncProducer
argument_list|(
name|destination
argument_list|,
name|exchange
argument_list|,
name|pattern
argument_list|,
name|callback
argument_list|,
operator|new
name|AsyncProducerCallback
argument_list|()
block|{
specifier|public
name|boolean
name|doInAsyncProducer
parameter_list|(
name|Producer
name|producer
parameter_list|,
name|AsyncProcessor
name|asyncProducer
parameter_list|,
specifier|final
name|Exchange
name|exchange
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{
specifier|final
name|Exchange
name|target
init|=
name|configureExchange
argument_list|(
name|exchange
argument_list|,
name|pattern
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|">>>> {} {}"
argument_list|,
name|destination
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
return|return
name|asyncProducer
operator|.
name|process
argument_list|(
name|target
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
comment|// restore previous MEP
name|target
operator|.
name|setPattern
argument_list|(
name|existingPattern
argument_list|)
expr_stmt|;
name|checkException
argument_list|(
name|target
argument_list|)
expr_stmt|;
comment|// signal we are done
name|callback
operator|.
name|done
argument_list|(
name|doneSync
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
DECL|method|checkException (Exchange exchange)
specifier|protected
name|void
name|checkException
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|unhandleException
operator|&&
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// Override the default setting of DeadLetterChannel
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|ERRORHANDLER_HANDLED
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
comment|// just override the exception with the new added
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|EXCEPTION_CAUGHT
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getDestination ()
specifier|public
name|Endpoint
name|getDestination
parameter_list|()
block|{
return|return
name|destination
return|;
block|}
DECL|method|getPattern ()
specifier|public
name|ExchangePattern
name|getPattern
parameter_list|()
block|{
return|return
name|pattern
return|;
block|}
DECL|method|configureExchange (Exchange exchange, ExchangePattern pattern)
specifier|protected
name|Exchange
name|configureExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|)
block|{
comment|// destination exchange pattern overrides pattern
if|if
condition|(
name|destinationExchangePattern
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|setPattern
argument_list|(
name|destinationExchangePattern
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|pattern
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|setPattern
argument_list|(
name|pattern
argument_list|)
expr_stmt|;
block|}
comment|// set property which endpoint we send to
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|TO_ENDPOINT
argument_list|,
name|destination
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
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
name|producerCache
operator|==
literal|null
condition|)
block|{
comment|// use a single producer cache as we need to only hold reference for one destination
comment|// and use a regular HashMap as we do not want a soft reference store that may get re-claimed when low on memory
comment|// as we want to ensure the producer is kept around, to ensure its lifecycle is fully managed,
comment|// eg stopping the producer when we stop etc.
name|producerCache
operator|=
operator|new
name|ProducerCache
argument_list|(
name|this
argument_list|,
name|camelContext
argument_list|,
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Producer
argument_list|>
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
comment|// do not add as service as we do not want to manage the producer cache
block|}
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|producerCache
argument_list|)
expr_stmt|;
comment|// the destination could since have been intercepted by a interceptSendToEndpoint so we got to
comment|// lookup this before we can use the destination
name|Endpoint
name|lookup
init|=
name|camelContext
operator|.
name|hasEndpoint
argument_list|(
name|destination
operator|.
name|getEndpointKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|lookup
operator|instanceof
name|InterceptSendToEndpoint
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Intercepted sending to {} -> {}"
argument_list|,
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|destination
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
argument_list|,
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|lookup
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|destination
operator|=
name|lookup
expr_stmt|;
block|}
comment|// warm up the producer by starting it so we can fail fast if there was a problem
comment|// however must start endpoint first
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|destination
argument_list|)
expr_stmt|;
comment|// this SendProcessor is used a lot in Camel (eg every .to in the route DSL) and therefore we
comment|// want to optimize for regular producers, by using the producer directly instead of the ProducerCache
comment|// Only for pooled and non singleton producers we have to use the ProducerCache as it supports these
comment|// kind of producer better (though these kind of producer should be rare)
name|Producer
name|producer
init|=
name|producerCache
operator|.
name|acquireProducer
argument_list|(
name|destination
argument_list|)
decl_stmt|;
if|if
condition|(
name|producer
operator|instanceof
name|ServicePoolAware
operator|||
operator|!
name|producer
operator|.
name|isSingleton
argument_list|()
condition|)
block|{
comment|// no we cannot optimize it - so release the producer back to the producer cache
comment|// and use the producer cache for sending
name|producerCache
operator|.
name|releaseProducer
argument_list|(
name|destination
argument_list|,
name|producer
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// yes we can optimize and use the producer directly for sending
name|this
operator|.
name|producer
operator|=
name|AsyncProcessorConverterHelper
operator|.
name|convert
argument_list|(
name|producer
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopServices
argument_list|(
name|producerCache
argument_list|,
name|producer
argument_list|)
expr_stmt|;
block|}
DECL|method|doShutdown ()
specifier|protected
name|void
name|doShutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|stopAndShutdownServices
argument_list|(
name|producerCache
argument_list|,
name|producer
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

