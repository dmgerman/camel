begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.http.common
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|http
operator|.
name|common
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_comment
comment|/**  * Strategy to resolve which consumer to service an incoming {@link javax.servlet.http.HttpServletRequest}.  */
end_comment

begin_interface
DECL|interface|ServletResolveConsumerStrategy
specifier|public
interface|interface
name|ServletResolveConsumerStrategy
block|{
comment|/**      * Resolve the consumer to use.      *      * @param request   the http request      * @param consumers the map of registered consumers      * @return the consumer to service the request, or<tt>null</tt> if no match,      * which sends back a {@link javax.servlet.http.HttpServletResponse#SC_NOT_FOUND} to the client.      */
DECL|method|resolve (HttpServletRequest request, Map<String, HttpConsumer> consumers)
name|HttpConsumer
name|resolve
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|HttpConsumer
argument_list|>
name|consumers
parameter_list|)
function_decl|;
comment|/**      * Checks if the http request method (GET, POST, etc) would be allow among the registered consumers.       * @param request   the http request      * @param method    the http method      * @param consumers the map of registered consumers      * @return<tt>true</tt> if the method is allowed and can be serviced. Otherwise a      * {@link javax.servlet.http.HttpServletResponse#SC_METHOD_NOT_ALLOWED} is returned to the client.      */
DECL|method|isHttpMethodAllowed (HttpServletRequest request, String method, Map<String, HttpConsumer> consumers)
name|boolean
name|isHttpMethodAllowed
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|String
name|method
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|HttpConsumer
argument_list|>
name|consumers
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

