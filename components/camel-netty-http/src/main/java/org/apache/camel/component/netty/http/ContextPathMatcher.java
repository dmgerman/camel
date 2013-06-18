begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty
operator|.
name|http
package|;
end_package

begin_comment
comment|/**  * A matcher used for selecting the correct {@link org.apache.camel.component.netty.http.handlers.HttpServerChannelHandler}  * to handle an incoming {@link org.jboss.netty.handler.codec.http.HttpRequest} when you use multiple routes on the same  * port.  *<p/>  * As when we do that, we need to multiplex and select the correct consumer route to process the HTTP request.  * To do that we need to match on the incoming HTTP request context-path from the request.  */
end_comment

begin_interface
DECL|interface|ContextPathMatcher
specifier|public
interface|interface
name|ContextPathMatcher
block|{
comment|/**      * Whether the target context-path matches.      *      * @param target  the context-path from the incoming HTTP request      * @return<tt>true</tt> to match,<tt>false</tt> if not.      */
DECL|method|matches (String target)
name|boolean
name|matches
parameter_list|(
name|String
name|target
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

