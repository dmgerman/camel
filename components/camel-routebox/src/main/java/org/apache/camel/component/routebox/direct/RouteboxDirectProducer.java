begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.routebox.direct
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|routebox
operator|.
name|direct
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
name|CamelExchangeException
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
name|ProducerTemplate
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
name|routebox
operator|.
name|RouteboxServiceSupport
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
name|routebox
operator|.
name|strategy
operator|.
name|RouteboxDispatcher
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
name|converter
operator|.
name|AsyncProcessorTypeConverter
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

begin_class
DECL|class|RouteboxDirectProducer
specifier|public
class|class
name|RouteboxDirectProducer
extends|extends
name|RouteboxServiceSupport
implements|implements
name|Producer
implements|,
name|AsyncProcessor
block|{
DECL|field|LOG
specifier|private
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
name|RouteboxDirectProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|producer
specifier|protected
name|ProducerTemplate
name|producer
decl_stmt|;
DECL|method|RouteboxDirectProducer (RouteboxDirectEndpoint endpoint)
specifier|public
name|RouteboxDirectProducer
parameter_list|(
name|RouteboxDirectEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|producer
operator|=
name|endpoint
operator|.
name|getConfig
argument_list|()
operator|.
name|getInnerProducerTemplate
argument_list|()
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Exchange
name|result
decl_stmt|;
if|if
condition|(
operator|(
operator|(
operator|(
name|RouteboxDirectEndpoint
operator|)
name|getRouteboxEndpoint
argument_list|()
operator|)
operator|.
name|getConsumer
argument_list|()
operator|==
literal|null
operator|)
operator|&&
operator|(
name|getRouteboxEndpoint
argument_list|()
operator|.
name|getConfig
argument_list|()
operator|.
name|isSendToConsumer
argument_list|()
operator|)
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"No consumers available on endpoint: "
operator|+
name|getRouteboxEndpoint
argument_list|()
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
else|else
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
literal|"Dispatching to Inner Route "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
name|RouteboxDispatcher
name|dispatcher
init|=
operator|new
name|RouteboxDispatcher
argument_list|(
name|producer
argument_list|)
decl_stmt|;
name|result
operator|=
name|dispatcher
operator|.
name|dispatchSync
argument_list|(
name|getRouteboxEndpoint
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getRouteboxEndpoint
argument_list|()
operator|.
name|getConfig
argument_list|()
operator|.
name|isSendToConsumer
argument_list|()
condition|)
block|{
operator|(
operator|(
name|RouteboxDirectEndpoint
operator|)
name|getRouteboxEndpoint
argument_list|()
operator|)
operator|.
name|getConsumer
argument_list|()
operator|.
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
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
name|boolean
name|flag
init|=
literal|true
decl_stmt|;
if|if
condition|(
operator|(
operator|(
operator|(
name|RouteboxDirectEndpoint
operator|)
name|getRouteboxEndpoint
argument_list|()
operator|)
operator|.
name|getConsumer
argument_list|()
operator|==
literal|null
operator|)
operator|&&
operator|(
operator|(
name|getRouteboxEndpoint
argument_list|()
operator|)
operator|.
name|getConfig
argument_list|()
operator|.
name|isSendToConsumer
argument_list|()
operator|)
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|CamelExchangeException
argument_list|(
literal|"No consumers available on endpoint: "
operator|+
name|getRouteboxEndpoint
argument_list|()
argument_list|,
name|exchange
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
name|flag
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
try|try
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
literal|"Dispatching to Inner Route "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
name|RouteboxDispatcher
name|dispatcher
init|=
operator|new
name|RouteboxDispatcher
argument_list|(
name|producer
argument_list|)
decl_stmt|;
name|exchange
operator|=
name|dispatcher
operator|.
name|dispatchAsync
argument_list|(
name|getRouteboxEndpoint
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|getRouteboxEndpoint
argument_list|()
operator|.
name|getConfig
argument_list|()
operator|.
name|isSendToConsumer
argument_list|()
condition|)
block|{
name|AsyncProcessor
name|processor
init|=
name|AsyncProcessorTypeConverter
operator|.
name|convert
argument_list|(
operator|(
operator|(
name|RouteboxDirectEndpoint
operator|)
name|getRouteboxEndpoint
argument_list|()
operator|)
operator|.
name|getConsumer
argument_list|()
operator|.
name|getProcessor
argument_list|()
argument_list|)
decl_stmt|;
name|flag
operator|=
name|AsyncProcessorHelper
operator|.
name|process
argument_list|(
name|processor
argument_list|,
name|exchange
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
comment|// we only have to handle async completion of this policy
if|if
condition|(
name|doneSync
condition|)
block|{
return|return;
block|}
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
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
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error processing exchange"
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|flag
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
operator|!
operator|(
name|getRouteboxEndpoint
argument_list|()
operator|)
operator|.
name|getConfig
argument_list|()
operator|.
name|isSendToConsumer
argument_list|()
condition|)
block|{
comment|// start an inner context
if|if
condition|(
operator|!
name|isStartedInnerContext
argument_list|()
condition|)
block|{
name|doStartInnerContext
argument_list|()
expr_stmt|;
block|}
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
if|if
condition|(
operator|!
operator|(
name|getRouteboxEndpoint
argument_list|()
operator|)
operator|.
name|getConfig
argument_list|()
operator|.
name|isSendToConsumer
argument_list|()
condition|)
block|{
comment|// stop the inner context
if|if
condition|(
name|isStartedInnerContext
argument_list|()
condition|)
block|{
name|doStopInnerContext
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|getEndpoint ()
specifier|public
name|Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|getRouteboxEndpoint
argument_list|()
return|;
block|}
DECL|method|createExchange ()
specifier|public
name|Exchange
name|createExchange
parameter_list|()
block|{
return|return
name|getRouteboxEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
return|;
block|}
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
name|getRouteboxEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|(
name|pattern
argument_list|)
return|;
block|}
DECL|method|createExchange (Exchange exchange)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|getRouteboxEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|(
name|exchange
argument_list|)
return|;
block|}
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
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Producer["
operator|+
name|getRouteboxEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

