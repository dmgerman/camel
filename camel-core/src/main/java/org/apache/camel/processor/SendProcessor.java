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
name|ProducerCallback
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
name|impl
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
block|{
DECL|field|log
specifier|protected
specifier|final
specifier|transient
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|camelContext
specifier|protected
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|producerCache
specifier|protected
name|ProducerCache
name|producerCache
decl_stmt|;
DECL|field|destination
specifier|protected
name|Endpoint
name|destination
decl_stmt|;
DECL|field|pattern
specifier|protected
name|ExchangePattern
name|pattern
decl_stmt|;
DECL|method|SendProcessor (Endpoint destination)
specifier|public
name|SendProcessor
parameter_list|(
name|Endpoint
name|destination
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
argument_list|)
expr_stmt|;
name|this
operator|.
name|pattern
operator|=
name|pattern
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
DECL|method|setDestination (Endpoint destination)
specifier|public
name|void
name|setDestination
parameter_list|(
name|Endpoint
name|destination
parameter_list|)
block|{
name|this
operator|.
name|destination
operator|=
name|destination
expr_stmt|;
block|}
DECL|method|getTraceLabel ()
specifier|public
name|String
name|getTraceLabel
parameter_list|()
block|{
return|return
name|destination
operator|.
name|getEndpointUri
argument_list|()
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
if|if
condition|(
operator|!
name|isStarted
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"SendProcessor has not been started: "
operator|+
name|this
argument_list|)
throw|;
block|}
comment|// send the exchange to the destination using a producer
name|producerCache
operator|.
name|doInProducer
argument_list|(
name|destination
argument_list|,
name|exchange
argument_list|,
name|pattern
argument_list|,
operator|new
name|ProducerCallback
argument_list|<
name|Exchange
argument_list|>
argument_list|()
block|{
specifier|public
name|Exchange
name|doInProducer
parameter_list|(
name|Producer
name|producer
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|)
throws|throws
name|Exception
block|{
name|exchange
operator|=
name|configureExchange
argument_list|(
name|exchange
argument_list|,
name|pattern
argument_list|)
expr_stmt|;
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
literal|">>>> "
operator|+
name|destination
operator|+
literal|" "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
block|}
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
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"SendProcessor has not been started: "
operator|+
name|this
argument_list|)
throw|;
block|}
comment|// send the exchange to the destination using a producer
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
name|Exchange
name|exchange
parameter_list|,
name|ExchangePattern
name|pattern
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|exchange
operator|=
name|configureExchange
argument_list|(
name|exchange
argument_list|,
name|pattern
argument_list|)
expr_stmt|;
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
literal|">>>> "
operator|+
name|destination
operator|+
literal|" "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
return|return
name|AsyncProcessorHelper
operator|.
name|process
argument_list|(
name|asyncProducer
argument_list|,
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
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
comment|// add it as a service so we can manage it
name|camelContext
operator|.
name|addService
argument_list|(
name|producerCache
argument_list|)
expr_stmt|;
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
literal|"Intercepted sending to "
operator|+
name|destination
operator|.
name|getEndpointUri
argument_list|()
operator|+
literal|" -> "
operator|+
name|lookup
operator|.
name|getEndpointUri
argument_list|()
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
name|producerCache
operator|.
name|startProducer
argument_list|(
name|destination
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
name|stopService
argument_list|(
name|producerCache
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

