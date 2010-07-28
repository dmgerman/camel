begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutionException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Future
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeoutException
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
name|ShutdownRoute
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
name|ShutdownRunningTask
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
name|SuspendableService
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
name|RouteStartupOrder
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
name|ShutdownAware
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
name|ShutdownStrategy
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
comment|/**  * Default {@link org.apache.camel.spi.ShutdownStrategy} which uses graceful shutdown.  *<p/>  * Graceful shutdown ensures that any inflight and pending messages will be taken into account  * and it will wait until these exchanges has been completed.  *<p/>  * As this strategy will politely wait until all exchanges has been completed it can potential wait  * for a long time, and hence why a timeout value can be set. When the timeout triggers you can also  * specify whether the remainder consumers should be shutdown now or ignore.  *<p/>  * Will by default use a timeout of 300 seconds (5 minutes) by which it will shutdown now the remaining consumers.  * This ensures that when shutting down Camel it at some point eventually will shutdown.  * This behavior can of course be configured using the {@link #setTimeout(long)} and  * {@link #setShutdownNowOnTimeout(boolean)} methods.  *<p/>  * Routes will by default be shutdown in the reverse order of which they where started.  * You can customize this using the {@link #setShutdownRoutesInReverseOrder(boolean)} method.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultShutdownStrategy
specifier|public
class|class
name|DefaultShutdownStrategy
extends|extends
name|ServiceSupport
implements|implements
name|ShutdownStrategy
implements|,
name|CamelContextAware
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
name|DefaultShutdownStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|executor
specifier|private
name|ExecutorService
name|executor
decl_stmt|;
DECL|field|timeout
specifier|private
name|long
name|timeout
init|=
literal|5
operator|*
literal|60
decl_stmt|;
DECL|field|timeUnit
specifier|private
name|TimeUnit
name|timeUnit
init|=
name|TimeUnit
operator|.
name|SECONDS
decl_stmt|;
DECL|field|shutdownNowOnTimeout
specifier|private
name|boolean
name|shutdownNowOnTimeout
init|=
literal|true
decl_stmt|;
DECL|field|shutdownRoutesInReverseOrder
specifier|private
name|boolean
name|shutdownRoutesInReverseOrder
init|=
literal|true
decl_stmt|;
DECL|method|DefaultShutdownStrategy ()
specifier|public
name|DefaultShutdownStrategy
parameter_list|()
block|{     }
DECL|method|DefaultShutdownStrategy (CamelContext camelContext)
specifier|public
name|DefaultShutdownStrategy
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
DECL|method|shutdown (CamelContext context, List<RouteStartupOrder> routes)
specifier|public
name|void
name|shutdown
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|List
argument_list|<
name|RouteStartupOrder
argument_list|>
name|routes
parameter_list|)
throws|throws
name|Exception
block|{
name|shutdown
argument_list|(
name|context
argument_list|,
name|routes
argument_list|,
name|getTimeout
argument_list|()
argument_list|,
name|getTimeUnit
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|shutdown (CamelContext context, List<RouteStartupOrder> routes, long timeout, TimeUnit timeUnit)
specifier|public
name|void
name|shutdown
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|List
argument_list|<
name|RouteStartupOrder
argument_list|>
name|routes
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
name|StopWatch
name|watch
init|=
operator|new
name|StopWatch
argument_list|()
decl_stmt|;
comment|// should the order of routes be reversed?
name|List
argument_list|<
name|RouteStartupOrder
argument_list|>
name|routesOrdered
init|=
operator|new
name|ArrayList
argument_list|<
name|RouteStartupOrder
argument_list|>
argument_list|(
name|routes
argument_list|)
decl_stmt|;
if|if
condition|(
name|shutdownRoutesInReverseOrder
condition|)
block|{
name|Collections
operator|.
name|reverse
argument_list|(
name|routesOrdered
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|timeout
operator|>
literal|0
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Starting to graceful shutdown "
operator|+
name|routesOrdered
operator|.
name|size
argument_list|()
operator|+
literal|" routes (timeout "
operator|+
name|timeout
operator|+
literal|" "
operator|+
name|timeUnit
operator|.
name|toString
argument_list|()
operator|.
name|toLowerCase
argument_list|()
operator|+
literal|")"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Starting to graceful shutdown "
operator|+
name|routesOrdered
operator|.
name|size
argument_list|()
operator|+
literal|" routes (no timeout)"
argument_list|)
expr_stmt|;
block|}
comment|// use another thread to perform the shutdowns so we can support timeout
name|Future
name|future
init|=
name|getExecutorService
argument_list|()
operator|.
name|submit
argument_list|(
operator|new
name|ShutdownTask
argument_list|(
name|context
argument_list|,
name|routesOrdered
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|timeout
operator|>
literal|0
condition|)
block|{
name|future
operator|.
name|get
argument_list|(
name|timeout
argument_list|,
name|timeUnit
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|future
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|TimeoutException
name|e
parameter_list|)
block|{
comment|// timeout then cancel the task
name|future
operator|.
name|cancel
argument_list|(
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|shutdownNowOnTimeout
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Timeout occurred. Now forcing the routes to be shutdown now."
argument_list|)
expr_stmt|;
comment|// force the routes to shutdown now
name|shutdownRoutesNow
argument_list|(
name|routesOrdered
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Timeout occurred. Will ignore shutting down the remainder routes."
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ExecutionException
name|e
parameter_list|)
block|{
comment|// unwrap execution exception
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
throw|;
block|}
comment|// convert to seconds as its easier to read than a big milli seconds number
name|long
name|seconds
init|=
name|TimeUnit
operator|.
name|SECONDS
operator|.
name|convert
argument_list|(
name|watch
operator|.
name|stop
argument_list|()
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Graceful shutdown of "
operator|+
name|routesOrdered
operator|.
name|size
argument_list|()
operator|+
literal|" routes completed in "
operator|+
name|seconds
operator|+
literal|" seconds"
argument_list|)
expr_stmt|;
block|}
DECL|method|setTimeout (long timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
DECL|method|getTimeout ()
specifier|public
name|long
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
DECL|method|setTimeUnit (TimeUnit timeUnit)
specifier|public
name|void
name|setTimeUnit
parameter_list|(
name|TimeUnit
name|timeUnit
parameter_list|)
block|{
name|this
operator|.
name|timeUnit
operator|=
name|timeUnit
expr_stmt|;
block|}
DECL|method|getTimeUnit ()
specifier|public
name|TimeUnit
name|getTimeUnit
parameter_list|()
block|{
return|return
name|timeUnit
return|;
block|}
DECL|method|setShutdownNowOnTimeout (boolean shutdownNowOnTimeout)
specifier|public
name|void
name|setShutdownNowOnTimeout
parameter_list|(
name|boolean
name|shutdownNowOnTimeout
parameter_list|)
block|{
name|this
operator|.
name|shutdownNowOnTimeout
operator|=
name|shutdownNowOnTimeout
expr_stmt|;
block|}
DECL|method|isShutdownNowOnTimeout ()
specifier|public
name|boolean
name|isShutdownNowOnTimeout
parameter_list|()
block|{
return|return
name|shutdownNowOnTimeout
return|;
block|}
DECL|method|isShutdownRoutesInReverseOrder ()
specifier|public
name|boolean
name|isShutdownRoutesInReverseOrder
parameter_list|()
block|{
return|return
name|shutdownRoutesInReverseOrder
return|;
block|}
DECL|method|setShutdownRoutesInReverseOrder (boolean shutdownRoutesInReverseOrder)
specifier|public
name|void
name|setShutdownRoutesInReverseOrder
parameter_list|(
name|boolean
name|shutdownRoutesInReverseOrder
parameter_list|)
block|{
name|this
operator|.
name|shutdownRoutesInReverseOrder
operator|=
name|shutdownRoutesInReverseOrder
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
comment|/**      * Shutdown all the consumers immediately.      *      * @param routes the routes to shutdown      */
DECL|method|shutdownRoutesNow (List<RouteStartupOrder> routes)
specifier|protected
name|void
name|shutdownRoutesNow
parameter_list|(
name|List
argument_list|<
name|RouteStartupOrder
argument_list|>
name|routes
parameter_list|)
block|{
for|for
control|(
name|RouteStartupOrder
name|order
range|:
name|routes
control|)
block|{
comment|// set the route to shutdown as fast as possible by stopping after
comment|// it has completed its current task
name|ShutdownRunningTask
name|current
init|=
name|order
operator|.
name|getRoute
argument_list|()
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getShutdownRunningTask
argument_list|()
decl_stmt|;
if|if
condition|(
name|current
operator|!=
name|ShutdownRunningTask
operator|.
name|CompleteCurrentTaskOnly
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Changing shutdownRunningTask from "
operator|+
name|current
operator|+
literal|" to "
operator|+
name|ShutdownRunningTask
operator|.
name|CompleteCurrentTaskOnly
operator|+
literal|" on route "
operator|+
name|order
operator|.
name|getRoute
argument_list|()
operator|.
name|getId
argument_list|()
operator|+
literal|" to shutdown faster"
argument_list|)
expr_stmt|;
name|order
operator|.
name|getRoute
argument_list|()
operator|.
name|getRouteContext
argument_list|()
operator|.
name|setShutdownRunningTask
argument_list|(
name|ShutdownRunningTask
operator|.
name|CompleteCurrentTaskOnly
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Consumer
name|consumer
range|:
name|order
operator|.
name|getInputs
argument_list|()
control|)
block|{
name|shutdownNow
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Shutdown all the consumers immediately.      *      * @param consumers the consumers to shutdown      */
DECL|method|shutdownNow (List<Consumer> consumers)
specifier|protected
name|void
name|shutdownNow
parameter_list|(
name|List
argument_list|<
name|Consumer
argument_list|>
name|consumers
parameter_list|)
block|{
for|for
control|(
name|Consumer
name|consumer
range|:
name|consumers
control|)
block|{
name|shutdownNow
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Shutdown the consumer immediately.      *      * @param consumer the consumer to shutdown      */
DECL|method|shutdownNow (Consumer consumer)
specifier|protected
name|void
name|shutdownNow
parameter_list|(
name|Consumer
name|consumer
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Shutting down: "
operator|+
name|consumer
argument_list|)
expr_stmt|;
block|}
comment|// allow us to do custom work before delegating to service helper
try|try
block|{
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error occurred while shutting down route: "
operator|+
name|consumer
operator|+
literal|". This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
comment|// fire event
name|EventHelper
operator|.
name|notifyServiceStopFailure
argument_list|(
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|consumer
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
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
literal|"Shutdown complete for: "
operator|+
name|consumer
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Suspends the consumer immediately.      *      * @param service the suspendable consumer      * @param consumer the consumer to suspend      */
DECL|method|suspendNow (SuspendableService service, Consumer consumer)
specifier|protected
name|void
name|suspendNow
parameter_list|(
name|SuspendableService
name|service
parameter_list|,
name|Consumer
name|consumer
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Suspending: "
operator|+
name|consumer
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|service
operator|.
name|suspend
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error occurred while suspending route: "
operator|+
name|consumer
operator|+
literal|". This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
comment|// fire event
name|EventHelper
operator|.
name|notifyServiceStopFailure
argument_list|(
name|consumer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|consumer
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
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
literal|"Suspend complete for: "
operator|+
name|consumer
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getExecutorService ()
specifier|private
name|ExecutorService
name|getExecutorService
parameter_list|()
block|{
if|if
condition|(
name|executor
operator|==
literal|null
condition|)
block|{
name|executor
operator|=
name|camelContext
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|newSingleThreadExecutor
argument_list|(
name|this
argument_list|,
literal|"ShutdownTask"
argument_list|)
expr_stmt|;
block|}
return|return
name|executor
return|;
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"CamelContext"
argument_list|)
expr_stmt|;
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
annotation|@
name|Override
DECL|method|doShutdown ()
specifier|protected
name|void
name|doShutdown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|executor
operator|!=
literal|null
condition|)
block|{
name|camelContext
operator|.
name|getExecutorServiceStrategy
argument_list|()
operator|.
name|shutdownNow
argument_list|(
name|executor
argument_list|)
expr_stmt|;
comment|// should clear executor so we can restart by creating a new thread pool
name|executor
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|class|ShutdownDeferredConsumer
class|class
name|ShutdownDeferredConsumer
block|{
DECL|field|route
specifier|private
specifier|final
name|Route
name|route
decl_stmt|;
DECL|field|consumer
specifier|private
specifier|final
name|Consumer
name|consumer
decl_stmt|;
DECL|method|ShutdownDeferredConsumer (Route route, Consumer consumer)
name|ShutdownDeferredConsumer
parameter_list|(
name|Route
name|route
parameter_list|,
name|Consumer
name|consumer
parameter_list|)
block|{
name|this
operator|.
name|route
operator|=
name|route
expr_stmt|;
name|this
operator|.
name|consumer
operator|=
name|consumer
expr_stmt|;
block|}
DECL|method|getRoute ()
name|Route
name|getRoute
parameter_list|()
block|{
return|return
name|route
return|;
block|}
DECL|method|getConsumer ()
name|Consumer
name|getConsumer
parameter_list|()
block|{
return|return
name|consumer
return|;
block|}
block|}
comment|/**      * Shutdown task which shutdown all the routes in a graceful manner.      */
DECL|class|ShutdownTask
class|class
name|ShutdownTask
implements|implements
name|Runnable
block|{
DECL|field|context
specifier|private
specifier|final
name|CamelContext
name|context
decl_stmt|;
DECL|field|routes
specifier|private
specifier|final
name|List
argument_list|<
name|RouteStartupOrder
argument_list|>
name|routes
decl_stmt|;
DECL|method|ShutdownTask (CamelContext context, List<RouteStartupOrder> routes)
specifier|public
name|ShutdownTask
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|List
argument_list|<
name|RouteStartupOrder
argument_list|>
name|routes
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
name|this
operator|.
name|routes
operator|=
name|routes
expr_stmt|;
block|}
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
comment|// the strategy in this run method is to
comment|// 1) go over the routes and shutdown those routes which can be shutdown asap
comment|//    some routes will be deferred to shutdown at the end, as they are needed
comment|//    by other routes so they can complete their tasks
comment|// 2) wait until all inflight and pending exchanges has been completed
comment|// 3) shutdown the deferred routes
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
literal|"There are "
operator|+
name|routes
operator|.
name|size
argument_list|()
operator|+
literal|" routes to shutdown"
argument_list|)
expr_stmt|;
block|}
comment|// list of deferred consumers to shutdown when all exchanges has been completed routed
comment|// and thus there are no more inflight exchanges so they can be safely shutdown at that time
name|List
argument_list|<
name|ShutdownDeferredConsumer
argument_list|>
name|deferredConsumers
init|=
operator|new
name|ArrayList
argument_list|<
name|ShutdownDeferredConsumer
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|RouteStartupOrder
name|order
range|:
name|routes
control|)
block|{
name|ShutdownRoute
name|shutdownRoute
init|=
name|order
operator|.
name|getRoute
argument_list|()
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getShutdownRoute
argument_list|()
decl_stmt|;
name|ShutdownRunningTask
name|shutdownRunningTask
init|=
name|order
operator|.
name|getRoute
argument_list|()
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getShutdownRunningTask
argument_list|()
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Shutting down route: "
operator|+
name|order
operator|.
name|getRoute
argument_list|()
operator|.
name|getId
argument_list|()
operator|+
literal|" with options ["
operator|+
name|shutdownRoute
operator|+
literal|","
operator|+
name|shutdownRunningTask
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|Consumer
name|consumer
range|:
name|order
operator|.
name|getInputs
argument_list|()
control|)
block|{
name|boolean
name|suspend
init|=
literal|false
decl_stmt|;
comment|// assume we should shutdown if we are not deferred
name|boolean
name|shutdown
init|=
name|shutdownRoute
operator|!=
name|ShutdownRoute
operator|.
name|Defer
decl_stmt|;
if|if
condition|(
name|shutdown
condition|)
block|{
comment|// if we are to shutdown then check whether we can suspend instead as its a more
comment|// gentle way to graceful shutdown
comment|// some consumers do not support shutting down so let them decide
comment|// if a consumer is suspendable then prefer to use that and then shutdown later
if|if
condition|(
name|consumer
operator|instanceof
name|ShutdownAware
condition|)
block|{
name|shutdown
operator|=
operator|!
operator|(
operator|(
name|ShutdownAware
operator|)
name|consumer
operator|)
operator|.
name|deferShutdown
argument_list|(
name|shutdownRunningTask
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|shutdown
operator|&&
name|consumer
operator|instanceof
name|SuspendableService
condition|)
block|{
comment|// we prefer to suspend over shutdown
name|suspend
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
name|suspend
condition|)
block|{
comment|// only suspend it and then later shutdown it
name|suspendNow
argument_list|(
operator|(
name|SuspendableService
operator|)
name|consumer
argument_list|,
name|consumer
argument_list|)
expr_stmt|;
comment|// add it to the deferred list so the route will be shutdown later
name|deferredConsumers
operator|.
name|add
argument_list|(
operator|new
name|ShutdownDeferredConsumer
argument_list|(
name|order
operator|.
name|getRoute
argument_list|()
argument_list|,
name|consumer
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Route: "
operator|+
name|order
operator|.
name|getRoute
argument_list|()
operator|.
name|getId
argument_list|()
operator|+
literal|" suspended and shutdown deferred, was consuming from: "
operator|+
name|order
operator|.
name|getRoute
argument_list|()
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|shutdown
condition|)
block|{
name|shutdownNow
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Route: "
operator|+
name|order
operator|.
name|getRoute
argument_list|()
operator|.
name|getId
argument_list|()
operator|+
literal|" shutdown complete, was consuming from: "
operator|+
name|order
operator|.
name|getRoute
argument_list|()
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// we will stop it later, but for now it must run to be able to help all inflight messages
comment|// be safely completed
name|deferredConsumers
operator|.
name|add
argument_list|(
operator|new
name|ShutdownDeferredConsumer
argument_list|(
name|order
operator|.
name|getRoute
argument_list|()
argument_list|,
name|consumer
argument_list|)
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Route: "
operator|+
name|order
operator|.
name|getRoute
argument_list|()
operator|.
name|getId
argument_list|()
operator|+
literal|" shutdown deferred."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// wait till there are no more pending and inflight messages
name|boolean
name|done
init|=
literal|false
decl_stmt|;
name|long
name|loopDelaySeconds
init|=
literal|1
decl_stmt|;
name|long
name|loopCount
init|=
literal|0
decl_stmt|;
while|while
condition|(
operator|!
name|done
condition|)
block|{
name|int
name|size
init|=
literal|0
decl_stmt|;
for|for
control|(
name|RouteStartupOrder
name|order
range|:
name|routes
control|)
block|{
for|for
control|(
name|Consumer
name|consumer
range|:
name|order
operator|.
name|getInputs
argument_list|()
control|)
block|{
name|int
name|inflight
init|=
name|context
operator|.
name|getInflightRepository
argument_list|()
operator|.
name|size
argument_list|(
name|consumer
operator|.
name|getEndpoint
argument_list|()
argument_list|)
decl_stmt|;
comment|// include any additional pending exchanges on some consumers which may have internal
comment|// memory queues such as seda
if|if
condition|(
name|consumer
operator|instanceof
name|ShutdownAware
condition|)
block|{
name|inflight
operator|+=
operator|(
operator|(
name|ShutdownAware
operator|)
name|consumer
operator|)
operator|.
name|getPendingExchangesSize
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|inflight
operator|>
literal|0
condition|)
block|{
name|size
operator|+=
name|inflight
expr_stmt|;
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
name|inflight
operator|+
literal|" inflight and pending exchanges for consumer: "
operator|+
name|consumer
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
if|if
condition|(
name|size
operator|>
literal|0
condition|)
block|{
try|try
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Waiting as there are still "
operator|+
name|size
operator|+
literal|" inflight and pending exchanges to complete, timeout in "
operator|+
operator|(
name|TimeUnit
operator|.
name|SECONDS
operator|.
name|convert
argument_list|(
name|getTimeout
argument_list|()
argument_list|,
name|getTimeUnit
argument_list|()
argument_list|)
operator|-
operator|(
name|loopCount
operator|++
operator|*
name|loopDelaySeconds
operator|)
operator|)
operator|+
literal|" seconds."
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
name|loopDelaySeconds
operator|*
literal|1000
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Interrupted while waiting during graceful shutdown, will force shutdown now."
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|interrupt
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
else|else
block|{
name|done
operator|=
literal|true
expr_stmt|;
block|}
block|}
comment|// now all messages has been completed then stop the deferred consumers
for|for
control|(
name|ShutdownDeferredConsumer
name|deferred
range|:
name|deferredConsumers
control|)
block|{
name|shutdownNow
argument_list|(
name|deferred
operator|.
name|getConsumer
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Route: "
operator|+
name|deferred
operator|.
name|getRoute
argument_list|()
operator|.
name|getId
argument_list|()
operator|+
literal|" shutdown complete."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

