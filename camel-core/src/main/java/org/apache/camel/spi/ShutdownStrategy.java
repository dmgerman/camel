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
name|Service
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
name|StaticService
import|;
end_import

begin_comment
comment|/**  * Pluggable shutdown strategy executed during shutdown of routes.  *<p/>  * Shutting down routes in a reliable and graceful manner is not a trivial task. Therefore Camel provides a pluggable  * strategy allowing 3rd party to use their own strategy if needed.  *<p/>  * The key problem is to stop the input consumers for the routes such that no new messages is coming into Camel.  * But at the same time still keep the routes running so the existing in flight exchanges can still be run to  * completion. On top of that there are some in memory components (such as SEDA) which may have pending messages  * on its in memory queue which we want to run to completion as well, otherwise they will get lost.  *<p/>  * Camel provides a default strategy which supports all that that can be used as inspiration for your own strategy.  *  * @version   * @see org.apache.camel.spi.ShutdownAware  */
end_comment

begin_interface
DECL|interface|ShutdownStrategy
specifier|public
interface|interface
name|ShutdownStrategy
extends|extends
name|StaticService
block|{
comment|/**      * Shutdown the routes, forcing shutdown being more aggressive, if timeout occurred.      *<p/>      * This operation is used when {@link CamelContext} is shutting down, to ensure Camel will shutdown      * if messages seems to be<i>stuck</i>.      *      * @param context   the camel context      * @param routes    the routes, ordered by the order they was started      * @throws Exception is thrown if error shutting down the consumers, however its preferred to avoid this      */
DECL|method|shutdownForced (CamelContext context, List<RouteStartupOrder> routes)
name|void
name|shutdownForced
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
function_decl|;
comment|/**      * Shutdown the routes      *      * @param context   the camel context      * @param routes    the routes, ordered by the order they was started      * @throws Exception is thrown if error shutting down the consumers, however its preferred to avoid this      */
DECL|method|shutdown (CamelContext context, List<RouteStartupOrder> routes)
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
function_decl|;
comment|/**      * Suspends the routes      *      * @param context   the camel context      * @param routes    the routes, ordered by the order they was started      * @throws Exception is thrown if error suspending the consumers, however its preferred to avoid this      */
DECL|method|suspend (CamelContext context, List<RouteStartupOrder> routes)
name|void
name|suspend
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
function_decl|;
comment|/**      * Shutdown the routes using a specified timeout instead of the default timeout values      *      * @param context   the camel context      * @param routes    the routes, ordered by the order they was started      * @param timeout   timeout      * @param timeUnit  the unit to use      * @throws Exception is thrown if error shutting down the consumers, however its preferred to avoid this      */
DECL|method|shutdown (CamelContext context, List<RouteStartupOrder> routes, long timeout, TimeUnit timeUnit)
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
function_decl|;
comment|/**      * Shutdown the route using a specified timeout instead of the default timeout values and supports abortAfterTimeout mode      *      * @param context   the camel context      * @param route     the route      * @param timeout   timeout      * @param timeUnit  the unit to use      * @param abortAfterTimeout   should abort shutdown after timeout      * @return<tt>true</tt> if the route is stopped before the timeout      * @throws Exception is thrown if error shutting down the consumer, however its preferred to avoid this      */
DECL|method|shutdown (CamelContext context, RouteStartupOrder route, long timeout, TimeUnit timeUnit, boolean abortAfterTimeout)
name|boolean
name|shutdown
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|RouteStartupOrder
name|route
parameter_list|,
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|timeUnit
parameter_list|,
name|boolean
name|abortAfterTimeout
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Suspends the routes using a specified timeout instead of the default timeout values      *      * @param context   the camel context      * @param routes    the routes, ordered by the order they was started      * @param timeout   timeout      * @param timeUnit  the unit to use      * @throws Exception is thrown if error suspending the consumers, however its preferred to avoid this      */
DECL|method|suspend (CamelContext context, List<RouteStartupOrder> routes, long timeout, TimeUnit timeUnit)
name|void
name|suspend
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
function_decl|;
comment|/**      * Set an timeout to wait for the shutdown to complete.      *<p/>      * Setting a value of 0 or negative will disable timeout and wait until complete      * (potential blocking forever)      *<p/>      * The default timeout unit is<tt>SECONDS</tt>      *      * @param timeout timeout      */
DECL|method|setTimeout (long timeout)
name|void
name|setTimeout
parameter_list|(
name|long
name|timeout
parameter_list|)
function_decl|;
comment|/**      * Gets the timeout.      *<p/>      * Use 0 or a negative value to disable timeout      *<p/>      * The default timeout unit is<tt>SECONDS</tt>      *      * @return the timeout      */
DECL|method|getTimeout ()
name|long
name|getTimeout
parameter_list|()
function_decl|;
comment|/**      * Set the time unit to use      *      * @param timeUnit the unit to use      */
DECL|method|setTimeUnit (TimeUnit timeUnit)
name|void
name|setTimeUnit
parameter_list|(
name|TimeUnit
name|timeUnit
parameter_list|)
function_decl|;
comment|/**      * Gets the time unit used      *      * @return the time unit      */
DECL|method|getTimeUnit ()
name|TimeUnit
name|getTimeUnit
parameter_list|()
function_decl|;
comment|/**      * Sets whether to force shutdown of all consumers when a timeout occurred and thus      * not all consumers was shutdown within that period.      *<p/>      * You should have good reasons to set this option to<tt>false</tt> as it means that the routes      * keep running and is halted abruptly when {@link CamelContext} has been shutdown.      *      * @param shutdownNowOnTimeout<tt>true</tt> to force shutdown,<tt>false</tt> to leave them running      */
DECL|method|setShutdownNowOnTimeout (boolean shutdownNowOnTimeout)
name|void
name|setShutdownNowOnTimeout
parameter_list|(
name|boolean
name|shutdownNowOnTimeout
parameter_list|)
function_decl|;
comment|/**      * Whether to force shutdown of all consumers when a timeout occurred.      *      * @return force shutdown or not      */
DECL|method|isShutdownNowOnTimeout ()
name|boolean
name|isShutdownNowOnTimeout
parameter_list|()
function_decl|;
comment|/**      * Sets whether routes should be shutdown in reverse or the same order as they where started.      *      * @param shutdownRoutesInReverseOrder<tt>true</tt> to shutdown in reverse order      */
DECL|method|setShutdownRoutesInReverseOrder (boolean shutdownRoutesInReverseOrder)
name|void
name|setShutdownRoutesInReverseOrder
parameter_list|(
name|boolean
name|shutdownRoutesInReverseOrder
parameter_list|)
function_decl|;
comment|/**      * Whether to shutdown routes in reverse order than they where started.      *<p/>      * This option is by default set to<tt>true</tt>.      *      * @return<tt>true</tt> if routes should be shutdown in reverse order.      */
DECL|method|isShutdownRoutesInReverseOrder ()
name|boolean
name|isShutdownRoutesInReverseOrder
parameter_list|()
function_decl|;
comment|/**      * Whether a service is forced to shutdown.      *<p/>      * Can be used to signal to services that they are no longer allowed to run, such as if a forced      * shutdown is currently in progress.      *<p/>      * For example the Camel {@link org.apache.camel.processor.RedeliveryErrorHandler} uses this information      * to know if a forced shutdown is in progress, and then break out of redelivery attempts.      *       * @param service the service      * @return<tt>true</tt> indicates the service is to be forced to shutdown,<tt>false</tt> the service can keep running.      */
DECL|method|forceShutdown (Service service)
name|boolean
name|forceShutdown
parameter_list|(
name|Service
name|service
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

