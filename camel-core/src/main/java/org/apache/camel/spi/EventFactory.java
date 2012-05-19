begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EventObject
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

begin_comment
comment|/**  * Factory to create {@link java.util.EventObject events} that are emitted when such an event occur.  *<p/>  * For example when an {@link Exchange} is being created and then later when its done.  *  * @version   */
end_comment

begin_interface
DECL|interface|EventFactory
specifier|public
interface|interface
name|EventFactory
block|{
comment|/**      * Creates an {@link EventObject} for Camel is starting.      *      * @param context camel context      * @return the created event      */
DECL|method|createCamelContextStartingEvent (CamelContext context)
name|EventObject
name|createCamelContextStartingEvent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
function_decl|;
comment|/**      * Creates an {@link EventObject} for Camel has been started successfully.      *      * @param context camel context      * @return the created event      */
DECL|method|createCamelContextStartedEvent (CamelContext context)
name|EventObject
name|createCamelContextStartedEvent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
function_decl|;
comment|/**      * Creates an {@link EventObject} for Camel failing to start      *      * @param context camel context      * @param cause   the cause exception      * @return the created event      */
DECL|method|createCamelContextStartupFailureEvent (CamelContext context, Throwable cause)
name|EventObject
name|createCamelContextStartupFailureEvent
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Throwable
name|cause
parameter_list|)
function_decl|;
comment|/**      * Creates an {@link EventObject} for Camel failing to stop cleanly      *      * @param context camel context      * @param cause   the cause exception      * @return the created event      */
DECL|method|createCamelContextStopFailureEvent (CamelContext context, Throwable cause)
name|EventObject
name|createCamelContextStopFailureEvent
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Throwable
name|cause
parameter_list|)
function_decl|;
comment|/**      * Creates an {@link EventObject} for Camel is stopping.      *      * @param context camel context      * @return the created event      */
DECL|method|createCamelContextStoppingEvent (CamelContext context)
name|EventObject
name|createCamelContextStoppingEvent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
function_decl|;
comment|/**      * Creates an {@link EventObject} for Camel has been stopped successfully.      *      * @param context camel context      * @return the created event      */
DECL|method|createCamelContextStoppedEvent (CamelContext context)
name|EventObject
name|createCamelContextStoppedEvent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
function_decl|;
comment|/**      * Creates an {@link EventObject} for a Service failed to start cleanly      *      * @param context camel context      * @param service the service      * @param cause   the cause exception      * @return the created event      */
DECL|method|createServiceStartupFailureEvent (CamelContext context, Object service, Throwable cause)
name|EventObject
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
function_decl|;
comment|/**      * Creates an {@link EventObject} for a Service failed to stop cleanly      *      * @param context camel context      * @param service the service      * @param cause   the cause exception      * @return the created event      */
DECL|method|createServiceStopFailureEvent (CamelContext context, Object service, Throwable cause)
name|EventObject
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
function_decl|;
comment|/**      * Creates an {@link EventObject} for {@link Route} has been started successfully.      *      * @param route the route      * @return the created event      */
DECL|method|createRouteStartedEvent (Route route)
name|EventObject
name|createRouteStartedEvent
parameter_list|(
name|Route
name|route
parameter_list|)
function_decl|;
comment|/**      * Creates an {@link EventObject} for {@link Route} has been stopped successfully.      *      * @param route the route      * @return the created event      */
DECL|method|createRouteStoppedEvent (Route route)
name|EventObject
name|createRouteStoppedEvent
parameter_list|(
name|Route
name|route
parameter_list|)
function_decl|;
comment|/**      * Creates an {@link EventObject} when an {@link org.apache.camel.Exchange} has been created      *      * @param exchange the exchange      * @return the created event      */
DECL|method|createExchangeCreatedEvent (Exchange exchange)
name|EventObject
name|createExchangeCreatedEvent
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Creates an {@link EventObject} when an {@link org.apache.camel.Exchange} has been completed successfully      *      * @param exchange the exchange      * @return the created event      */
DECL|method|createExchangeCompletedEvent (Exchange exchange)
name|EventObject
name|createExchangeCompletedEvent
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Creates an {@link EventObject} when an {@link org.apache.camel.Exchange} has failed      *      * @param exchange the exchange      * @return the created event      */
DECL|method|createExchangeFailedEvent (Exchange exchange)
name|EventObject
name|createExchangeFailedEvent
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Creates an {@link EventObject} when an {@link org.apache.camel.Exchange} has failed      * but was handled by the Camel error handlers such as an dead letter channel.      *      * @param exchange          the exchange      * @param failureHandler    the failure handler such as moving the message to a dead letter queue      * @param deadLetterChannel whether it was a dead letter channel or not handling the failure      * @return the created event      */
DECL|method|createExchangeFailureHandledEvent (Exchange exchange, Processor failureHandler, boolean deadLetterChannel)
name|EventObject
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
parameter_list|)
function_decl|;
comment|/**      * Creates an {@link EventObject} when an {@link org.apache.camel.Exchange} is about to be redelivered      *      * @param exchange the exchange      * @param attempt  the current redelivery attempt (starts from 1)      * @return the created event      */
DECL|method|createExchangeRedeliveryEvent (Exchange exchange, int attempt)
name|EventObject
name|createExchangeRedeliveryEvent
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|int
name|attempt
parameter_list|)
function_decl|;
comment|/**      * Creates an {@link EventObject} when an {@link org.apache.camel.Exchange} is about to be sent to the endpoint (eg before).      *      * @param exchange  the exchange      * @param endpoint  the destination      * @return the created event      */
DECL|method|createExchangeSendingEvent (Exchange exchange, Endpoint endpoint)
name|EventObject
name|createExchangeSendingEvent
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
function_decl|;
comment|/**      * Creates an {@link EventObject} when an {@link org.apache.camel.Exchange} has completely been sent to the endpoint (eg after).      *      * @param exchange  the exchange      * @param endpoint  the destination      * @param timeTaken time in millis taken      * @return the created event      */
DECL|method|createExchangeSentEvent (Exchange exchange, Endpoint endpoint, long timeTaken)
name|EventObject
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
function_decl|;
comment|/**      * Creates an {@link EventObject} for Camel is suspending.      *      * @param context camel context      * @return the created event      */
DECL|method|createCamelContextSuspendingEvent (CamelContext context)
name|EventObject
name|createCamelContextSuspendingEvent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
function_decl|;
comment|/**      * Creates an {@link EventObject} for Camel has been suspended successfully.      *      * @param context camel context      * @return the created event      */
DECL|method|createCamelContextSuspendedEvent (CamelContext context)
name|EventObject
name|createCamelContextSuspendedEvent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
function_decl|;
comment|/**      * Creates an {@link EventObject} for Camel is resuming.      *      * @param context camel context      * @return the created event      */
DECL|method|createCamelContextResumingEvent (CamelContext context)
name|EventObject
name|createCamelContextResumingEvent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
function_decl|;
comment|/**      * Creates an {@link EventObject} for Camel has been resumed successfully.      *      * @param context camel context      * @return the created event      */
DECL|method|createCamelContextResumedEvent (CamelContext context)
name|EventObject
name|createCamelContextResumedEvent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
function_decl|;
comment|/**      * Creates an {@link EventObject} for Camel failing to resume      *      * @param context camel context      * @param cause   the cause exception      * @return the created event      */
DECL|method|createCamelContextResumeFailureEvent (CamelContext context, Throwable cause)
name|EventObject
name|createCamelContextResumeFailureEvent
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Throwable
name|cause
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

