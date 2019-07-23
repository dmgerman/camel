begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  * Various runtime configuration options used by {@link org.apache.camel.CamelContext} and {@link org.apache.camel.spi.RouteContext}  * for cross cutting functions such as tracing, delayer, stream cache and the like.  */
end_comment

begin_interface
DECL|interface|RuntimeConfiguration
specifier|public
interface|interface
name|RuntimeConfiguration
block|{
comment|/**      * Sets whether stream caching is enabled or not (default is disabled).      *      * @param cache whether stream caching is enabled or not      */
DECL|method|setStreamCaching (Boolean cache)
name|void
name|setStreamCaching
parameter_list|(
name|Boolean
name|cache
parameter_list|)
function_decl|;
comment|/**      * Returns whether stream cache is enabled      *      * @return<tt>true</tt> if stream cache is enabled      */
DECL|method|isStreamCaching ()
name|Boolean
name|isStreamCaching
parameter_list|()
function_decl|;
comment|/**      * Sets whether tracing is enabled or not (default is enabled).      *      * @param tracing whether to enable tracing.      */
DECL|method|setTracing (Boolean tracing)
name|void
name|setTracing
parameter_list|(
name|Boolean
name|tracing
parameter_list|)
function_decl|;
comment|/**      * Returns whether tracing enabled      *      * @return<tt>true</tt> if tracing is enabled      */
DECL|method|isTracing ()
name|Boolean
name|isTracing
parameter_list|()
function_decl|;
comment|/**      * Sets whether debugging is enabled or not (default is enabled).      *      * @param debugging whether to enable debugging.      */
DECL|method|setDebugging (Boolean debugging)
name|void
name|setDebugging
parameter_list|(
name|Boolean
name|debugging
parameter_list|)
function_decl|;
comment|/**      * Returns whether debugging enabled      *      * @return<tt>true</tt> if tracing is enabled      */
DECL|method|isDebugging ()
name|Boolean
name|isDebugging
parameter_list|()
function_decl|;
comment|/**      * Sets whether message history is enabled or not (default is enabled).      *      * @param messageHistory whether message history is enabled      */
DECL|method|setMessageHistory (Boolean messageHistory)
name|void
name|setMessageHistory
parameter_list|(
name|Boolean
name|messageHistory
parameter_list|)
function_decl|;
comment|/**      * Returns whether message history is enabled      *      * @return<tt>true</tt> if message history is enabled      */
DECL|method|isMessageHistory ()
name|Boolean
name|isMessageHistory
parameter_list|()
function_decl|;
comment|/**      * Sets whether security mask for Logging is enabled or not (default is disabled).      *       * @param logMask<tt>true</tt> if mask is enabled      */
DECL|method|setLogMask (Boolean logMask)
name|void
name|setLogMask
parameter_list|(
name|Boolean
name|logMask
parameter_list|)
function_decl|;
comment|/**      * Gets whether security mask for Logging is enabled or not.      *       * @return<tt>true</tt> if mask is enabled      */
DECL|method|isLogMask ()
name|Boolean
name|isLogMask
parameter_list|()
function_decl|;
comment|/**      * Sets whether to log exhausted message body with message history.      *      * @param logExhaustedMessageBody whether message body should be logged      */
DECL|method|setLogExhaustedMessageBody (Boolean logExhaustedMessageBody)
name|void
name|setLogExhaustedMessageBody
parameter_list|(
name|Boolean
name|logExhaustedMessageBody
parameter_list|)
function_decl|;
comment|/**      * Returns whether to log exhausted message body with message history.      *       * @return<tt>true</tt> if logging of message body is enabled      */
DECL|method|isLogExhaustedMessageBody ()
name|Boolean
name|isLogExhaustedMessageBody
parameter_list|()
function_decl|;
comment|/**      * Sets a delay value in millis that a message is delayed at every step it takes in the route path,      * slowing the process down to better observe what is occurring      *<p/>      * Is disabled by default      *      * @param delay delay in millis      */
DECL|method|setDelayer (Long delay)
name|void
name|setDelayer
parameter_list|(
name|Long
name|delay
parameter_list|)
function_decl|;
comment|/**      * Gets the delay value      *      * @return delay in millis, or<tt>null</tt> if disabled      */
DECL|method|getDelayer ()
name|Long
name|getDelayer
parameter_list|()
function_decl|;
comment|/**      * Sets whether the object should automatically start when Camel starts.      *<p/>      *<b>Important:</b> Currently only routes can be disabled, as {@link CamelContext}s are always started.      *<br/>      *<b>Note:</b> When setting auto startup<tt>false</tt> on {@link CamelContext} then that takes precedence      * and<i>no</i> routes is started. You would need to start {@link CamelContext} explicit using      * the {@link org.apache.camel.CamelContext#start()} method, to start the context, and then      * you would need to start the routes manually using {@link org.apache.camel.spi.RouteController#startRoute(String)}.      *<p/>      * Default is<tt>true</tt> to always start up.      *      * @param autoStartup whether to start up automatically.      */
DECL|method|setAutoStartup (Boolean autoStartup)
name|void
name|setAutoStartup
parameter_list|(
name|Boolean
name|autoStartup
parameter_list|)
function_decl|;
comment|/**      * Gets whether the object should automatically start when Camel starts.      *<p/>      *<b>Important:</b> Currently only routes can be disabled, as {@link CamelContext}s are always started.      *<br/>      * Default is<tt>true</tt> to always start up.      *      * @return<tt>true</tt> if object should automatically start      */
DECL|method|isAutoStartup ()
name|Boolean
name|isAutoStartup
parameter_list|()
function_decl|;
comment|/**      * Sets the ShutdownRoute option for routes.      *      * @param shutdownRoute the option to use.      */
DECL|method|setShutdownRoute (ShutdownRoute shutdownRoute)
name|void
name|setShutdownRoute
parameter_list|(
name|ShutdownRoute
name|shutdownRoute
parameter_list|)
function_decl|;
comment|/**      * Gets the option to use when shutting down the route.      *      * @return the option      */
DECL|method|getShutdownRoute ()
name|ShutdownRoute
name|getShutdownRoute
parameter_list|()
function_decl|;
comment|/**      * Sets the ShutdownRunningTask option to use when shutting down a route.      *      * @param shutdownRunningTask the option to use.      */
DECL|method|setShutdownRunningTask (ShutdownRunningTask shutdownRunningTask)
name|void
name|setShutdownRunningTask
parameter_list|(
name|ShutdownRunningTask
name|shutdownRunningTask
parameter_list|)
function_decl|;
comment|/**      * Gets the ShutdownRunningTask option in use when shutting down a route.      *      * @return the option      */
DECL|method|getShutdownRunningTask ()
name|ShutdownRunningTask
name|getShutdownRunningTask
parameter_list|()
function_decl|;
comment|/**      * Sets whether to allow access to the original message from Camel's error handler,      * or from {@link org.apache.camel.spi.UnitOfWork#getOriginalInMessage()}.      *<p/>      * Turning this off can optimize performance, as defensive copy of the original message is not needed.      *      * @param allowUseOriginalMessage the option to use.      */
DECL|method|setAllowUseOriginalMessage (Boolean allowUseOriginalMessage)
name|void
name|setAllowUseOriginalMessage
parameter_list|(
name|Boolean
name|allowUseOriginalMessage
parameter_list|)
function_decl|;
comment|/**      * Gets whether access to the original message from Camel's error handler,      * or from {@link org.apache.camel.spi.UnitOfWork#getOriginalInMessage()} is allowed.      *      * @return the option      */
DECL|method|isAllowUseOriginalMessage ()
name|Boolean
name|isAllowUseOriginalMessage
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

