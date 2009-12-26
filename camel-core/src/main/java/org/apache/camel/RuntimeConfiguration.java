begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_comment
comment|/**  * Various runtime configuration used by {@link org.apache.camel.CamelContext} and {@link org.apache.camel.spi.RouteContext}  * for cross cutting functions such as tracing, delayer, stream cache and the likes.  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|RuntimeConfiguration
specifier|public
interface|interface
name|RuntimeConfiguration
block|{
comment|/**      * Sets whether stream caching is enabled or not (default is disabled).      *<p/>      * Is disabled by default      *      * @param cache whether stream caching is enabled or not      */
DECL|method|setStreamCaching (Boolean cache)
name|void
name|setStreamCaching
parameter_list|(
name|Boolean
name|cache
parameter_list|)
function_decl|;
comment|/**      * Returns whether stream cache is enabled      *      * @return true if stream cache is enabled      */
DECL|method|isStreamCaching ()
name|boolean
name|isStreamCaching
parameter_list|()
function_decl|;
comment|/**      * Sets whether tracing is enabled or not (default is disabled).      *<p/>      * Is disabled by default      *      * @param tracing whether tracing is enabled or not.      */
DECL|method|setTracing (Boolean tracing)
name|void
name|setTracing
parameter_list|(
name|Boolean
name|tracing
parameter_list|)
function_decl|;
comment|/**      * Returns whether tracing enabled      *      * @return true if tracing is enabled      */
DECL|method|isTracing ()
name|boolean
name|isTracing
parameter_list|()
function_decl|;
comment|/**      * Sets whether handle fault is enabled or not (default is disabled).      *<p/>      * Is disabled by default      *      * @param handleFault whether handle fault is enabled or not.      */
DECL|method|setHandleFault (Boolean handleFault)
name|void
name|setHandleFault
parameter_list|(
name|Boolean
name|handleFault
parameter_list|)
function_decl|;
comment|/**      * Returns whether tracing enabled      *      * @return true if tracing is enabled      */
DECL|method|isHandleFault ()
name|boolean
name|isHandleFault
parameter_list|()
function_decl|;
comment|/**      * Sets a delay value in millis that a message is delayed at every step it takes in the route path,      * to slow things down to better helps you to see what goes      *<p/>      * Is disabled by default      *      * @param delay delay in millis      */
DECL|method|setDelayer (long delay)
name|void
name|setDelayer
parameter_list|(
name|long
name|delay
parameter_list|)
function_decl|;
comment|/**      * Gets the delay value      *      * @return delay in millis, or<tt>null</tt> if disabled      */
DECL|method|getDelayer ()
name|Long
name|getDelayer
parameter_list|()
function_decl|;
comment|/**      * Sets whether it should automatic start when Camel starts.      *<p/>      * Currently only routes can be disabled, as {@link CamelContext} itself are always started}      *<br/>      * Default is true to always startup.      *      * @param autoStartup  whether to auto startup.      */
DECL|method|setAutoStartup (Boolean autoStartup)
name|void
name|setAutoStartup
parameter_list|(
name|Boolean
name|autoStartup
parameter_list|)
function_decl|;
comment|/**      * Gets whether it should automatic start when Camel starts.      *      * @return true if should auto start      */
DECL|method|isAutoStartup ()
name|boolean
name|isAutoStartup
parameter_list|()
function_decl|;
comment|/**      * Sets the option to use when shutting down routes.      *      * @param shutdownRoute the option to use.      */
DECL|method|setShutdownRoute (ShutdownRoute shutdownRoute)
name|void
name|setShutdownRoute
parameter_list|(
name|ShutdownRoute
name|shutdownRoute
parameter_list|)
function_decl|;
comment|/**      * Gets the option to use when shutting down route.      *      * @return the option      */
DECL|method|getShutdownRoute ()
name|ShutdownRoute
name|getShutdownRoute
parameter_list|()
function_decl|;
comment|/**      * Sets the option to use when shutting down a route and how to act when it has running tasks.      *<p/>      * A running task is for example a {@link org.apache.camel.BatchConsumer} which has a group      * of messages to process. With this option you can control whether it should complete the entire      * group or stop after the current message has been processed.      *      * @param shutdownRunningTask the option to use.      */
DECL|method|setShutdownRunningTask (ShutdownRunningTask shutdownRunningTask)
name|void
name|setShutdownRunningTask
parameter_list|(
name|ShutdownRunningTask
name|shutdownRunningTask
parameter_list|)
function_decl|;
comment|/**      * Gets the option to use when shutting down a route and how to act when it has running tasks.      *      * @return the option      */
DECL|method|getShutdownRunningTask ()
name|ShutdownRunningTask
name|getShutdownRunningTask
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

