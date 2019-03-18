begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.undertow
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|undertow
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|io
operator|.
name|undertow
operator|.
name|server
operator|.
name|HttpHandler
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
name|undertow
operator|.
name|handlers
operator|.
name|CamelWebSocketHandler
import|;
end_import

begin_comment
comment|/**  * An undertow host abstraction  *  */
end_comment

begin_interface
DECL|interface|UndertowHost
specifier|public
interface|interface
name|UndertowHost
block|{
comment|/**      * Validate whether this host can process the given URI      */
DECL|method|validateEndpointURI (URI httpURI)
name|void
name|validateEndpointURI
parameter_list|(
name|URI
name|httpURI
parameter_list|)
function_decl|;
comment|/**      * Register a handler with the given {@link HttpHandlerRegistrationInfo}. Note that for some kinds of handlers (most      * notably {@link CamelWebSocketHandler}), it is legal to call this method multiple times with equal      * {@link HttpHandlerRegistrationInfo} and {@link HttpHandler}. In such cases the returned {@link HttpHandler} may      * differ from the passed {@link HttpHandler} and the returned instance is the effectively registered one for the      * given {@link HttpHandlerRegistrationInfo}.      *      * @param registrationInfo      *            the {@link HttpHandlerRegistrationInfo} related to {@code handler}      * @param handler      *            the {@link HttpHandler} to register      * @return the given {@code handler} or a different {@link HttpHandler} that has been registered with the given      *         {@link HttpHandlerRegistrationInfo} earlier.      */
DECL|method|registerHandler (HttpHandlerRegistrationInfo registrationInfo, HttpHandler handler)
name|HttpHandler
name|registerHandler
parameter_list|(
name|HttpHandlerRegistrationInfo
name|registrationInfo
parameter_list|,
name|HttpHandler
name|handler
parameter_list|)
function_decl|;
comment|/**      * Unregister a handler with the given {@link HttpHandlerRegistrationInfo}. Note that if      * {@link #registerHandler(HttpHandlerRegistrationInfo, HttpHandler)} was successfully invoked multiple times for an      * equivalent {@link HttpHandlerRegistrationInfo} then {@link #unregisterHandler(HttpHandlerRegistrationInfo)} must      * be called the same number of times to unregister the associated handler completely.      */
DECL|method|unregisterHandler (HttpHandlerRegistrationInfo registrationInfo)
name|void
name|unregisterHandler
parameter_list|(
name|HttpHandlerRegistrationInfo
name|registrationInfo
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

