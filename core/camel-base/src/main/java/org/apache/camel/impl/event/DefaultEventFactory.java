begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.event
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|event
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
name|DelegateProcessor
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
name|Route
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
name|CamelEvent
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
name|EventFactory
import|;
end_import

begin_comment
comment|/**  * Default implementation of the {@link org.apache.camel.spi.EventFactory}.  */
end_comment

begin_class
DECL|class|DefaultEventFactory
specifier|public
class|class
name|DefaultEventFactory
implements|implements
name|EventFactory
block|{
annotation|@
name|Override
DECL|method|createCamelContextStartingEvent (CamelContext context)
specifier|public
name|CamelEvent
name|createCamelContextStartingEvent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
return|return
operator|new
name|CamelContextStartingEvent
argument_list|(
name|context
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createCamelContextStartedEvent (CamelContext context)
specifier|public
name|CamelEvent
name|createCamelContextStartedEvent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
return|return
operator|new
name|CamelContextStartedEvent
argument_list|(
name|context
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createCamelContextStoppingEvent (CamelContext context)
specifier|public
name|CamelEvent
name|createCamelContextStoppingEvent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
return|return
operator|new
name|CamelContextStoppingEvent
argument_list|(
name|context
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createCamelContextStoppedEvent (CamelContext context)
specifier|public
name|CamelEvent
name|createCamelContextStoppedEvent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
return|return
operator|new
name|CamelContextStoppedEvent
argument_list|(
name|context
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createCamelContextRoutesStartingEvent (CamelContext context)
specifier|public
name|CamelEvent
name|createCamelContextRoutesStartingEvent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
return|return
operator|new
name|CamelContextRoutesStartingEvent
argument_list|(
name|context
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createCamelContextRoutesStartedEvent (CamelContext context)
specifier|public
name|CamelEvent
name|createCamelContextRoutesStartedEvent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
return|return
operator|new
name|CamelContextRoutesStartedEvent
argument_list|(
name|context
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createCamelContextRoutesStoppingEvent (CamelContext context)
specifier|public
name|CamelEvent
name|createCamelContextRoutesStoppingEvent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
return|return
operator|new
name|CamelContextRoutesStoppingEvent
argument_list|(
name|context
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createCamelContextRoutesStoppedEvent (CamelContext context)
specifier|public
name|CamelEvent
name|createCamelContextRoutesStoppedEvent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
return|return
operator|new
name|CamelContextRoutesStoppedEvent
argument_list|(
name|context
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createCamelContextStartupFailureEvent (CamelContext context, Throwable cause)
specifier|public
name|CamelEvent
name|createCamelContextStartupFailureEvent
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
return|return
operator|new
name|CamelContextStartupFailureEvent
argument_list|(
name|context
argument_list|,
name|cause
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createCamelContextStopFailureEvent (CamelContext context, Throwable cause)
specifier|public
name|CamelEvent
name|createCamelContextStopFailureEvent
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
return|return
operator|new
name|CamelContextStopFailureEvent
argument_list|(
name|context
argument_list|,
name|cause
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createServiceStartupFailureEvent (CamelContext context, Object service, Throwable cause)
specifier|public
name|CamelEvent
name|createServiceStartupFailureEvent
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Object
name|service
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
return|return
operator|new
name|ServiceStartupFailureEvent
argument_list|(
name|context
argument_list|,
name|service
argument_list|,
name|cause
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createServiceStopFailureEvent (CamelContext context, Object service, Throwable cause)
specifier|public
name|CamelEvent
name|createServiceStopFailureEvent
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Object
name|service
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
return|return
operator|new
name|ServiceStopFailureEvent
argument_list|(
name|context
argument_list|,
name|service
argument_list|,
name|cause
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteStartedEvent (Route route)
specifier|public
name|CamelEvent
name|createRouteStartedEvent
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
return|return
operator|new
name|RouteStartedEvent
argument_list|(
name|route
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteStoppedEvent (Route route)
specifier|public
name|CamelEvent
name|createRouteStoppedEvent
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
return|return
operator|new
name|RouteStoppedEvent
argument_list|(
name|route
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteAddedEvent (Route route)
specifier|public
name|CamelEvent
name|createRouteAddedEvent
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
return|return
operator|new
name|RouteAddedEvent
argument_list|(
name|route
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteRemovedEvent (Route route)
specifier|public
name|CamelEvent
name|createRouteRemovedEvent
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
return|return
operator|new
name|RouteRemovedEvent
argument_list|(
name|route
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createExchangeCreatedEvent (Exchange exchange)
specifier|public
name|CamelEvent
name|createExchangeCreatedEvent
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
operator|new
name|ExchangeCreatedEvent
argument_list|(
name|exchange
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createExchangeCompletedEvent (Exchange exchange)
specifier|public
name|CamelEvent
name|createExchangeCompletedEvent
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
operator|new
name|ExchangeCompletedEvent
argument_list|(
name|exchange
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createExchangeFailedEvent (Exchange exchange)
specifier|public
name|CamelEvent
name|createExchangeFailedEvent
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
operator|new
name|ExchangeFailedEvent
argument_list|(
name|exchange
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createExchangeFailureHandlingEvent (Exchange exchange, Processor failureHandler, boolean deadLetterChannel, String deadLetterUri)
specifier|public
name|CamelEvent
name|createExchangeFailureHandlingEvent
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Processor
name|failureHandler
parameter_list|,
name|boolean
name|deadLetterChannel
parameter_list|,
name|String
name|deadLetterUri
parameter_list|)
block|{
comment|// unwrap delegate processor
name|Processor
name|handler
init|=
name|failureHandler
decl_stmt|;
if|if
condition|(
name|handler
operator|instanceof
name|DelegateProcessor
condition|)
block|{
name|handler
operator|=
operator|(
operator|(
name|DelegateProcessor
operator|)
name|handler
operator|)
operator|.
name|getProcessor
argument_list|()
expr_stmt|;
block|}
return|return
operator|new
name|ExchangeFailureHandlingEvent
argument_list|(
name|exchange
argument_list|,
name|handler
argument_list|,
name|deadLetterChannel
argument_list|,
name|deadLetterUri
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createExchangeFailureHandledEvent (Exchange exchange, Processor failureHandler, boolean deadLetterChannel, String deadLetterUri)
specifier|public
name|CamelEvent
name|createExchangeFailureHandledEvent
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Processor
name|failureHandler
parameter_list|,
name|boolean
name|deadLetterChannel
parameter_list|,
name|String
name|deadLetterUri
parameter_list|)
block|{
comment|// unwrap delegate processor
name|Processor
name|handler
init|=
name|failureHandler
decl_stmt|;
if|if
condition|(
name|handler
operator|instanceof
name|DelegateProcessor
condition|)
block|{
name|handler
operator|=
operator|(
operator|(
name|DelegateProcessor
operator|)
name|handler
operator|)
operator|.
name|getProcessor
argument_list|()
expr_stmt|;
block|}
return|return
operator|new
name|ExchangeFailureHandledEvent
argument_list|(
name|exchange
argument_list|,
name|handler
argument_list|,
name|deadLetterChannel
argument_list|,
name|deadLetterUri
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createExchangeRedeliveryEvent (Exchange exchange, int attempt)
specifier|public
name|CamelEvent
name|createExchangeRedeliveryEvent
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|int
name|attempt
parameter_list|)
block|{
return|return
operator|new
name|ExchangeRedeliveryEvent
argument_list|(
name|exchange
argument_list|,
name|attempt
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createExchangeSendingEvent (Exchange exchange, Endpoint endpoint)
specifier|public
name|CamelEvent
name|createExchangeSendingEvent
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
block|{
return|return
operator|new
name|ExchangeSendingEvent
argument_list|(
name|exchange
argument_list|,
name|endpoint
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createExchangeSentEvent (Exchange exchange, Endpoint endpoint, long timeTaken)
specifier|public
name|CamelEvent
name|createExchangeSentEvent
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|long
name|timeTaken
parameter_list|)
block|{
return|return
operator|new
name|ExchangeSentEvent
argument_list|(
name|exchange
argument_list|,
name|endpoint
argument_list|,
name|timeTaken
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createStepStartedEvent (Exchange exchange, String stepId)
specifier|public
name|CamelEvent
name|createStepStartedEvent
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|stepId
parameter_list|)
block|{
return|return
operator|new
name|StepStartedEvent
argument_list|(
name|exchange
argument_list|,
name|stepId
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createStepCompletedEvent (Exchange exchange, String stepId)
specifier|public
name|CamelEvent
name|createStepCompletedEvent
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|stepId
parameter_list|)
block|{
return|return
operator|new
name|StepCompletedEvent
argument_list|(
name|exchange
argument_list|,
name|stepId
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createStepFailedEvent (Exchange exchange, String stepId)
specifier|public
name|CamelEvent
name|createStepFailedEvent
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|stepId
parameter_list|)
block|{
return|return
operator|new
name|StepFailedEvent
argument_list|(
name|exchange
argument_list|,
name|stepId
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createCamelContextSuspendingEvent (CamelContext context)
specifier|public
name|CamelEvent
name|createCamelContextSuspendingEvent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
return|return
operator|new
name|CamelContextSuspendingEvent
argument_list|(
name|context
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createCamelContextSuspendedEvent (CamelContext context)
specifier|public
name|CamelEvent
name|createCamelContextSuspendedEvent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
return|return
operator|new
name|CamelContextSuspendedEvent
argument_list|(
name|context
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createCamelContextResumingEvent (CamelContext context)
specifier|public
name|CamelEvent
name|createCamelContextResumingEvent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
return|return
operator|new
name|CamelContextResumingEvent
argument_list|(
name|context
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createCamelContextResumedEvent (CamelContext context)
specifier|public
name|CamelEvent
name|createCamelContextResumedEvent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
return|return
operator|new
name|CamelContextResumedEvent
argument_list|(
name|context
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createCamelContextResumeFailureEvent (CamelContext context, Throwable cause)
specifier|public
name|CamelEvent
name|createCamelContextResumeFailureEvent
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
return|return
operator|new
name|CamelContextResumeFailureEvent
argument_list|(
name|context
argument_list|,
name|cause
argument_list|)
return|;
block|}
block|}
end_class

end_unit

