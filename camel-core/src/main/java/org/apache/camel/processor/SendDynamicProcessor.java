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
name|Expression
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
name|NoTypeConversionAvailableException
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
name|impl
operator|.
name|EmptyProducerCache
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
name|spi
operator|.
name|IdAware
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
name|ServiceHelper
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
comment|/**  * Processor for forwarding exchanges to a dynamic endpoint destination.  *  * @see org.apache.camel.processor.SendProcessor  */
end_comment

begin_class
DECL|class|SendDynamicProcessor
specifier|public
class|class
name|SendDynamicProcessor
extends|extends
name|ServiceSupport
implements|implements
name|AsyncProcessor
implements|,
name|IdAware
implements|,
name|CamelContextAware
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
name|SendDynamicProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|camelContext
specifier|protected
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|uri
specifier|protected
specifier|final
name|String
name|uri
decl_stmt|;
DECL|field|expression
specifier|protected
specifier|final
name|Expression
name|expression
decl_stmt|;
DECL|field|pattern
specifier|protected
name|ExchangePattern
name|pattern
decl_stmt|;
DECL|field|producerCache
specifier|protected
name|ProducerCache
name|producerCache
decl_stmt|;
DECL|field|id
specifier|protected
name|String
name|id
decl_stmt|;
DECL|field|ignoreInvalidEndpoint
specifier|protected
name|boolean
name|ignoreInvalidEndpoint
decl_stmt|;
DECL|field|cacheSize
specifier|protected
name|int
name|cacheSize
decl_stmt|;
DECL|method|SendDynamicProcessor (Expression expression)
specifier|public
name|SendDynamicProcessor
parameter_list|(
name|Expression
name|expression
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
block|}
DECL|method|SendDynamicProcessor (String uri, Expression expression)
specifier|public
name|SendDynamicProcessor
parameter_list|(
name|String
name|uri
parameter_list|,
name|Expression
name|expression
parameter_list|)
block|{
name|this
operator|.
name|uri
operator|=
name|uri
expr_stmt|;
name|this
operator|.
name|expression
operator|=
name|expression
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
name|getExpression
argument_list|()
operator|+
literal|")"
return|;
block|}
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
DECL|method|setId (String id)
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
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
comment|// which endpoint to send to
specifier|final
name|Endpoint
name|endpoint
decl_stmt|;
specifier|final
name|ExchangePattern
name|destinationExchangePattern
decl_stmt|;
comment|// use dynamic endpoint so calculate the endpoint to use
name|Object
name|recipient
init|=
literal|null
decl_stmt|;
try|try
block|{
name|recipient
operator|=
name|expression
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|Object
operator|.
name|class
argument_list|)
expr_stmt|;
name|endpoint
operator|=
name|resolveEndpoint
argument_list|(
name|exchange
argument_list|,
name|recipient
argument_list|)
expr_stmt|;
name|destinationExchangePattern
operator|=
name|EndpointHelper
operator|.
name|resolveExchangePatternFromUrl
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
if|if
condition|(
name|isIgnoreInvalidEndpoint
argument_list|()
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
literal|"Endpoint uri is invalid: "
operator|+
name|recipient
operator|+
literal|". This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
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
comment|// send the exchange to the destination using the producer cache
return|return
name|producerCache
operator|.
name|doInAsyncProducer
argument_list|(
name|endpoint
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
argument_list|,
name|destinationExchangePattern
argument_list|,
name|endpoint
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|">>>> {} {}"
argument_list|,
name|endpoint
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
DECL|method|resolveEndpoint (Exchange exchange, Object recipient)
specifier|protected
specifier|static
name|Endpoint
name|resolveEndpoint
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|recipient
parameter_list|)
throws|throws
name|NoTypeConversionAvailableException
block|{
comment|// trim strings as end users might have added spaces between separators
if|if
condition|(
name|recipient
operator|instanceof
name|String
condition|)
block|{
name|recipient
operator|=
operator|(
operator|(
name|String
operator|)
name|recipient
operator|)
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|recipient
operator|instanceof
name|Endpoint
condition|)
block|{
return|return
operator|(
name|Endpoint
operator|)
name|recipient
return|;
block|}
else|else
block|{
comment|// convert to a string type we can work with
name|recipient
operator|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|recipient
argument_list|)
expr_stmt|;
block|}
return|return
name|ExchangeHelper
operator|.
name|resolveEndpoint
argument_list|(
name|exchange
argument_list|,
name|recipient
argument_list|)
return|;
block|}
DECL|method|configureExchange (Exchange exchange, ExchangePattern pattern, ExchangePattern destinationExchangePattern, Endpoint endpoint)
specifier|protected
name|Exchange
name|configureExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|,
name|ExchangePattern
name|destinationExchangePattern
parameter_list|,
name|Endpoint
name|endpoint
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
name|endpoint
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
if|if
condition|(
name|cacheSize
operator|<
literal|0
condition|)
block|{
name|producerCache
operator|=
operator|new
name|EmptyProducerCache
argument_list|(
name|this
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"DynamicSendTo {} is not using ProducerCache"
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|cacheSize
operator|==
literal|0
condition|)
block|{
name|producerCache
operator|=
operator|new
name|ProducerCache
argument_list|(
name|this
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"DynamicSendTo {} using ProducerCache with default cache size"
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|producerCache
operator|=
operator|new
name|ProducerCache
argument_list|(
name|this
argument_list|,
name|camelContext
argument_list|,
name|cacheSize
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"DynamicSendTo {} using ProducerCache with cacheSize={}"
argument_list|,
name|this
argument_list|,
name|cacheSize
argument_list|)
expr_stmt|;
block|}
block|}
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|producerCache
argument_list|)
expr_stmt|;
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
argument_list|)
expr_stmt|;
block|}
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
DECL|method|getUri ()
specifier|public
name|String
name|getUri
parameter_list|()
block|{
return|return
name|uri
return|;
block|}
DECL|method|getExpression ()
specifier|public
name|Expression
name|getExpression
parameter_list|()
block|{
return|return
name|expression
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
DECL|method|setPattern (ExchangePattern pattern)
specifier|public
name|void
name|setPattern
parameter_list|(
name|ExchangePattern
name|pattern
parameter_list|)
block|{
name|this
operator|.
name|pattern
operator|=
name|pattern
expr_stmt|;
block|}
DECL|method|isIgnoreInvalidEndpoint ()
specifier|public
name|boolean
name|isIgnoreInvalidEndpoint
parameter_list|()
block|{
return|return
name|ignoreInvalidEndpoint
return|;
block|}
DECL|method|setIgnoreInvalidEndpoint (boolean ignoreInvalidEndpoint)
specifier|public
name|void
name|setIgnoreInvalidEndpoint
parameter_list|(
name|boolean
name|ignoreInvalidEndpoint
parameter_list|)
block|{
name|this
operator|.
name|ignoreInvalidEndpoint
operator|=
name|ignoreInvalidEndpoint
expr_stmt|;
block|}
DECL|method|getCacheSize ()
specifier|public
name|int
name|getCacheSize
parameter_list|()
block|{
return|return
name|cacheSize
return|;
block|}
DECL|method|setCacheSize (int cacheSize)
specifier|public
name|void
name|setCacheSize
parameter_list|(
name|int
name|cacheSize
parameter_list|)
block|{
name|this
operator|.
name|cacheSize
operator|=
name|cacheSize
expr_stmt|;
block|}
block|}
end_class

end_unit

