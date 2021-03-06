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
name|concurrent
operator|.
name|TimeUnit
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
name|RouteController
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
name|RoutePolicy
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
name|support
operator|.
name|service
operator|.
name|ServiceSupport
import|;
end_import

begin_comment
comment|/**  * A base class for developing custom {@link RoutePolicy} implementations.  */
end_comment

begin_class
DECL|class|RoutePolicySupport
specifier|public
specifier|abstract
class|class
name|RoutePolicySupport
extends|extends
name|ServiceSupport
implements|implements
name|RoutePolicy
block|{
DECL|field|exceptionHandler
specifier|private
name|ExceptionHandler
name|exceptionHandler
decl_stmt|;
annotation|@
name|Override
DECL|method|onInit (Route route)
specifier|public
name|void
name|onInit
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
if|if
condition|(
name|exceptionHandler
operator|==
literal|null
condition|)
block|{
name|exceptionHandler
operator|=
operator|new
name|LoggingExceptionHandler
argument_list|(
name|route
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|onRemove (Route route)
specifier|public
name|void
name|onRemove
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|onStart (Route route)
specifier|public
name|void
name|onStart
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|onStop (Route route)
specifier|public
name|void
name|onStop
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|onSuspend (Route route)
specifier|public
name|void
name|onSuspend
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|onResume (Route route)
specifier|public
name|void
name|onResume
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|onExchangeBegin (Route route, Exchange exchange)
specifier|public
name|void
name|onExchangeBegin
parameter_list|(
name|Route
name|route
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|onExchangeDone (Route route, Exchange exchange)
specifier|public
name|void
name|onExchangeDone
parameter_list|(
name|Route
name|route
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// noop
block|}
comment|/**      * Starts the consumer.      *      * @return the returned value is always<tt>true</tt> and should not be used.      * @see #resumeOrStartConsumer(Consumer)      */
DECL|method|startConsumer (Consumer consumer)
specifier|public
name|void
name|startConsumer
parameter_list|(
name|Consumer
name|consumer
parameter_list|)
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Started consumer {}"
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
block|}
comment|/**      * Stops the consumer.      *      * @return the returned value is always<tt>true</tt> and should not be used.      * @see #suspendOrStopConsumer(Consumer)      */
DECL|method|stopConsumer (Consumer consumer)
specifier|public
name|void
name|stopConsumer
parameter_list|(
name|Consumer
name|consumer
parameter_list|)
throws|throws
name|Exception
block|{
comment|// stop and shutdown
name|ServiceHelper
operator|.
name|stopAndShutdownServices
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Stopped consumer {}"
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
block|}
comment|/**      * Suspends or stops the consumer.      *      * If the consumer is {@link org.apache.camel.Suspendable} then the consumer is suspended,      * otherwise the consumer is stopped.      *      * @see #stopConsumer(Consumer)      * @return<tt>true</tt> if the consumer was suspended or stopped,<tt>false</tt> if the consumer was already suspend or stopped      */
DECL|method|suspendOrStopConsumer (Consumer consumer)
specifier|public
name|boolean
name|suspendOrStopConsumer
parameter_list|(
name|Consumer
name|consumer
parameter_list|)
throws|throws
name|Exception
block|{
name|boolean
name|suspended
init|=
name|ServiceHelper
operator|.
name|suspendService
argument_list|(
name|consumer
argument_list|)
decl_stmt|;
if|if
condition|(
name|suspended
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Suspended consumer {}"
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Consumer already suspended {}"
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
block|}
return|return
name|suspended
return|;
block|}
comment|/**      * Resumes or starts the consumer.      *      * If the consumer is {@link org.apache.camel.Suspendable} then the consumer is resumed,      * otherwise the consumer is started.      *      * @see #startConsumer(Consumer)      * @return<tt>true</tt> if the consumer was resumed or started,<tt>false</tt> if the consumer was already resumed or started      */
DECL|method|resumeOrStartConsumer (Consumer consumer)
specifier|public
name|boolean
name|resumeOrStartConsumer
parameter_list|(
name|Consumer
name|consumer
parameter_list|)
throws|throws
name|Exception
block|{
name|boolean
name|resumed
init|=
name|ServiceHelper
operator|.
name|resumeService
argument_list|(
name|consumer
argument_list|)
decl_stmt|;
if|if
condition|(
name|resumed
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Resumed consumer {}"
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Consumer already resumed {}"
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
block|}
return|return
name|resumed
return|;
block|}
DECL|method|startRoute (Route route)
specifier|public
name|void
name|startRoute
parameter_list|(
name|Route
name|route
parameter_list|)
throws|throws
name|Exception
block|{
name|controller
argument_list|(
name|route
argument_list|)
operator|.
name|startRoute
argument_list|(
name|route
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|resumeRoute (Route route)
specifier|public
name|void
name|resumeRoute
parameter_list|(
name|Route
name|route
parameter_list|)
throws|throws
name|Exception
block|{
name|controller
argument_list|(
name|route
argument_list|)
operator|.
name|resumeRoute
argument_list|(
name|route
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|suspendRoute (Route route)
specifier|public
name|void
name|suspendRoute
parameter_list|(
name|Route
name|route
parameter_list|)
throws|throws
name|Exception
block|{
name|controller
argument_list|(
name|route
argument_list|)
operator|.
name|suspendRoute
argument_list|(
name|route
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|suspendRoute (Route route, long timeout, TimeUnit timeUnit)
specifier|public
name|void
name|suspendRoute
parameter_list|(
name|Route
name|route
parameter_list|,
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|timeUnit
parameter_list|)
throws|throws
name|Exception
block|{
name|controller
argument_list|(
name|route
argument_list|)
operator|.
name|suspendRoute
argument_list|(
name|route
operator|.
name|getId
argument_list|()
argument_list|,
name|timeout
argument_list|,
name|timeUnit
argument_list|)
expr_stmt|;
block|}
comment|/**      * @see #stopRouteAsync(Route)      */
DECL|method|stopRoute (Route route)
specifier|public
name|void
name|stopRoute
parameter_list|(
name|Route
name|route
parameter_list|)
throws|throws
name|Exception
block|{
name|controller
argument_list|(
name|route
argument_list|)
operator|.
name|stopRoute
argument_list|(
name|route
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * @see #stopRouteAsync(Route)      */
DECL|method|stopRoute (Route route, long timeout, TimeUnit timeUnit)
specifier|public
name|void
name|stopRoute
parameter_list|(
name|Route
name|route
parameter_list|,
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|timeUnit
parameter_list|)
throws|throws
name|Exception
block|{
name|controller
argument_list|(
name|route
argument_list|)
operator|.
name|stopRoute
argument_list|(
name|route
operator|.
name|getId
argument_list|()
argument_list|,
name|timeout
argument_list|,
name|timeUnit
argument_list|)
expr_stmt|;
block|}
comment|/**      * Allows to stop a route asynchronously using a separate background thread which can allow any current in-flight exchange      * to complete while the route is being shutdown.      * You may attempt to stop a route from processing an exchange which would be in-flight and therefore attempting to stop      * the route will defer due there is an inflight exchange in-progress. By stopping the route independently using a separate      * thread ensures the exchange can continue process and complete and the route can be stopped.      */
DECL|method|stopRouteAsync (final Route route)
specifier|public
name|void
name|stopRouteAsync
parameter_list|(
specifier|final
name|Route
name|route
parameter_list|)
block|{
name|String
name|threadId
init|=
name|route
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|resolveThreadName
argument_list|(
literal|"StopRouteAsync"
argument_list|)
decl_stmt|;
name|Runnable
name|task
init|=
parameter_list|()
lambda|->
block|{
try|try
block|{
name|controller
argument_list|(
name|route
argument_list|)
operator|.
name|stopRoute
argument_list|(
name|route
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
operator|new
name|Thread
argument_list|(
name|task
argument_list|,
name|threadId
argument_list|)
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
DECL|method|controller (Route route)
specifier|protected
name|RouteController
name|controller
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
return|return
name|route
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getRouteController
argument_list|()
return|;
block|}
comment|/**      * Handles the given exception using the {@link #getExceptionHandler()}      *      * @param t the exception to handle      */
DECL|method|handleException (Throwable t)
specifier|protected
name|void
name|handleException
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
if|if
condition|(
name|exceptionHandler
operator|!=
literal|null
condition|)
block|{
name|exceptionHandler
operator|.
name|handleException
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
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
block|}
end_class

end_unit

