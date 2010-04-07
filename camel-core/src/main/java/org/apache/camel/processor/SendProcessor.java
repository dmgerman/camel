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

begin_comment
comment|/**  * Processor for forwarding exchanges to an endpoint destination.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|SendProcessor
specifier|public
class|class
name|SendProcessor
extends|extends
name|ServiceSupport
implements|implements
name|Processor
implements|,
name|Traceable
block|{
DECL|field|LOG
specifier|protected
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
DECL|field|init
specifier|private
name|boolean
name|init
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
specifier|synchronized
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
name|this
operator|.
name|init
operator|=
literal|false
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
comment|// the destination could since have been intercepted by a interceptSendToEndpoint so we got to
comment|// init this before we can use the destination
if|if
condition|(
operator|!
name|init
condition|)
block|{
name|init
operator|=
literal|true
expr_stmt|;
name|Endpoint
name|lookup
init|=
name|exchange
operator|.
name|getContext
argument_list|()
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
literal|"SendTo is intercepted using a interceptSendToEndpoint: "
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
block|}
name|doProcess
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|/**      * Strategy to process the exchange      *      * @param exchange the exchange      * @throws Exception can be thrown if error processing exchange      * @return the exchange that was processed      */
DECL|method|doProcess (final Exchange exchange)
specifier|public
name|Exchange
name|doProcess
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
return|return
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

