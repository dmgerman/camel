begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.interceptor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|interceptor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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

begin_comment
comment|/**  * A trace event message that contains decomposed information about the traced  * {@link org.apache.camel.Exchange} at the point of interception. The information is stored as snapshot copies  * using String types.  *<p/>  * Notice not all implementations may provide direct access to the traced {@link Exchange} using  * the {@link #getTracedExchange()} method, and thus this method may return<tt>null</tt>.  * For example the JPA implementation will return<tt>null</tt>.  */
end_comment

begin_interface
DECL|interface|TraceEventMessage
specifier|public
interface|interface
name|TraceEventMessage
block|{
comment|/**      * Gets the timestamp when the interception occurred      */
DECL|method|getTimestamp ()
name|Date
name|getTimestamp
parameter_list|()
function_decl|;
comment|/**      * Uri of the endpoint that started the {@link org.apache.camel.Exchange} currently being traced.      */
DECL|method|getFromEndpointUri ()
name|String
name|getFromEndpointUri
parameter_list|()
function_decl|;
comment|/**      * Gets the previous node.      *<p/>      * Will return<tt>null</tt> if this is the first node, then you can use the from endpoint uri      * instead to indicate the start      */
DECL|method|getPreviousNode ()
name|String
name|getPreviousNode
parameter_list|()
function_decl|;
comment|/**      * Gets the current node that just have been intercepted and processed      *<p/>      * Is never<tt>null</tt>.      */
DECL|method|getToNode ()
name|String
name|getToNode
parameter_list|()
function_decl|;
DECL|method|getExchangeId ()
name|String
name|getExchangeId
parameter_list|()
function_decl|;
comment|/**      * Gets the exchange id without the leading hostname      */
DECL|method|getShortExchangeId ()
name|String
name|getShortExchangeId
parameter_list|()
function_decl|;
DECL|method|getExchangePattern ()
name|String
name|getExchangePattern
parameter_list|()
function_decl|;
DECL|method|getProperties ()
name|String
name|getProperties
parameter_list|()
function_decl|;
DECL|method|getHeaders ()
name|String
name|getHeaders
parameter_list|()
function_decl|;
DECL|method|getBody ()
name|String
name|getBody
parameter_list|()
function_decl|;
DECL|method|getBodyType ()
name|String
name|getBodyType
parameter_list|()
function_decl|;
DECL|method|getOutBody ()
name|String
name|getOutBody
parameter_list|()
function_decl|;
DECL|method|getOutBodyType ()
name|String
name|getOutBodyType
parameter_list|()
function_decl|;
comment|/**      * Gets the caused by exception (ie {@link org.apache.camel.Exchange#getException() Exchange#getException()}.      */
DECL|method|getCausedByException ()
name|String
name|getCausedByException
parameter_list|()
function_decl|;
comment|/**      * Gets the traced {@link Exchange}.      *<p/>      * Not all implementations may provide direct access to the traced {@link Exchange} and thus this      * method may return<tt>null</tt>. For example the JPA implementation will return<tt>null</tt>.      *      * @return the traced {@link Exchange}, however it can be<tt>null</tt> in some implementations.      */
DECL|method|getTracedExchange ()
name|Exchange
name|getTracedExchange
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

