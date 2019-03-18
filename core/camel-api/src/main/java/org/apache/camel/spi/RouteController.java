begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Collection
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
name|ServiceStatus
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
comment|// TODO: Add javadoc
end_comment

begin_interface
DECL|interface|RouteController
specifier|public
interface|interface
name|RouteController
extends|extends
name|CamelContextAware
extends|,
name|StaticService
block|{
comment|/**      * Return the list of routes controlled by this controller.      *      * @return the list of controlled routes      */
DECL|method|getControlledRoutes ()
name|Collection
argument_list|<
name|Route
argument_list|>
name|getControlledRoutes
parameter_list|()
function_decl|;
comment|/**      * Starts all the routes which currently is not started.      *      * @throws Exception is thrown if a route could not be started for whatever reason      */
DECL|method|startAllRoutes ()
name|void
name|startAllRoutes
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Indicates whether current thread is starting route(s).      *<p/>      * This can be useful to know by {@link LifecycleStrategy} or the likes, in case      * they need to react differently.      *      * @return<tt>true</tt> if current thread is starting route(s), or<tt>false</tt> if not.      */
DECL|method|isStartingRoutes ()
name|boolean
name|isStartingRoutes
parameter_list|()
function_decl|;
comment|/**      * Returns the current status of the given route      *      * @param routeId the route id      * @return the status for the route      */
DECL|method|getRouteStatus (String routeId)
name|ServiceStatus
name|getRouteStatus
parameter_list|(
name|String
name|routeId
parameter_list|)
function_decl|;
comment|/**      * Starts the given route if it has been previously stopped      *      * @param routeId the route id      * @throws Exception is thrown if the route could not be started for whatever reason      */
DECL|method|startRoute (String routeId)
name|void
name|startRoute
parameter_list|(
name|String
name|routeId
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Stops the given route using {@link org.apache.camel.spi.ShutdownStrategy}.      *      * @param routeId the route id      * @throws Exception is thrown if the route could not be stopped for whatever reason      * @see #suspendRoute(String)      */
DECL|method|stopRoute (String routeId)
name|void
name|stopRoute
parameter_list|(
name|String
name|routeId
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Stops the given route using {@link org.apache.camel.spi.ShutdownStrategy} with a specified timeout.      *      * @param routeId the route id      * @param timeout  timeout      * @param timeUnit the unit to use      * @throws Exception is thrown if the route could not be stopped for whatever reason      * @see #suspendRoute(String, long, java.util.concurrent.TimeUnit)      */
DECL|method|stopRoute (String routeId, long timeout, TimeUnit timeUnit)
name|void
name|stopRoute
parameter_list|(
name|String
name|routeId
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
comment|/**      * Stops the given route using {@link org.apache.camel.spi.ShutdownStrategy} with a specified timeout      * and optional abortAfterTimeout mode.      *      * @param routeId the route id      * @param timeout  timeout      * @param timeUnit the unit to use      * @param abortAfterTimeout should abort shutdown after timeout      * @return<tt>true</tt> if the route is stopped before the timeout      * @throws Exception is thrown if the route could not be stopped for whatever reason      * @see #suspendRoute(String, long, java.util.concurrent.TimeUnit)      */
DECL|method|stopRoute (String routeId, long timeout, TimeUnit timeUnit, boolean abortAfterTimeout)
name|boolean
name|stopRoute
parameter_list|(
name|String
name|routeId
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
comment|/**      * Suspends the given route using {@link org.apache.camel.spi.ShutdownStrategy}.      *<p/>      * Suspending a route is more gently than stopping, as the route consumers will be suspended (if they support)      * otherwise the consumers will be stopped.      *<p/>      * By suspending the route services will be kept running (if possible) and therefore its faster to resume the route.      *<p/>      * If the route does<b>not</b> support suspension the route will be stopped instead      *      * @param routeId the route id      * @throws Exception is thrown if the route could not be suspended for whatever reason      */
DECL|method|suspendRoute (String routeId)
name|void
name|suspendRoute
parameter_list|(
name|String
name|routeId
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Suspends the given route using {@link org.apache.camel.spi.ShutdownStrategy} with a specified timeout.      *<p/>      * Suspending a route is more gently than stopping, as the route consumers will be suspended (if they support)      * otherwise the consumers will be stopped.      *<p/>      * By suspending the route services will be kept running (if possible) and therefore its faster to resume the route.      *<p/>      * If the route does<b>not</b> support suspension the route will be stopped instead      *      * @param routeId  the route id      * @param timeout  timeout      * @param timeUnit the unit to use      * @throws Exception is thrown if the route could not be suspended for whatever reason      */
DECL|method|suspendRoute (String routeId, long timeout, TimeUnit timeUnit)
name|void
name|suspendRoute
parameter_list|(
name|String
name|routeId
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
comment|/**      * Resumes the given route if it has been previously suspended      *<p/>      * If the route does<b>not</b> support suspension the route will be started instead      *      * @param routeId the route id      * @throws Exception is thrown if the route could not be resumed for whatever reason      */
DECL|method|resumeRoute (String routeId)
name|void
name|resumeRoute
parameter_list|(
name|String
name|routeId
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Access the underlying concrete RouteController implementation.      *      * @param clazz the proprietary class or interface of the underlying concrete RouteController.      * @return an instance of the underlying concrete RouteController as the required type.      */
DECL|method|unwrap (Class<T> clazz)
specifier|default
parameter_list|<
name|T
extends|extends
name|RouteController
parameter_list|>
name|T
name|unwrap
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
block|{
if|if
condition|(
name|RouteController
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|clazz
argument_list|)
condition|)
block|{
return|return
name|clazz
operator|.
name|cast
argument_list|(
name|this
argument_list|)
return|;
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unable to unwrap this RouteController type ("
operator|+
name|getClass
argument_list|()
operator|+
literal|") to the required type ("
operator|+
name|clazz
operator|+
literal|")"
argument_list|)
throw|;
block|}
block|}
end_interface

end_unit

