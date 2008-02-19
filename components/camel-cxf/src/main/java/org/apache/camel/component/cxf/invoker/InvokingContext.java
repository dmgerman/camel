begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.invoker
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|invoker
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
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
name|cxf
operator|.
name|message
operator|.
name|Message
import|;
end_import

begin_interface
DECL|interface|InvokingContext
specifier|public
interface|interface
name|InvokingContext
block|{
comment|/**      * This method is called when the router is preparing an outbound message      * (orignated from the router's client) to be sent to the target CXF server.      * It sets the content in the given (out) message object.      * @param content      */
DECL|method|setRequestOutMessageContent (Message message, Object content)
name|void
name|setRequestOutMessageContent
parameter_list|(
name|Message
name|message
parameter_list|,
name|Object
name|content
parameter_list|)
function_decl|;
comment|/**      * This method is call when the CxfClient receives a response from a CXF server and needs      * to extract the response object from the message.      * @param exchange      * @param responseContext      * @return response object      */
DECL|method|getResponseObject (Exchange exchange, Map<String, Object> responseContext)
name|Object
name|getResponseObject
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|responseContext
parameter_list|)
function_decl|;
comment|/**      * This method is called when the routing interceptor has received a response message      * from the target CXF server and needs to set the response in the outgoing message      * that is to be sent to the client.      * @param outMessage      * @param resultPayload      */
DECL|method|setResponseContent (Message outMessage, Object resultPayload)
name|void
name|setResponseContent
parameter_list|(
name|Message
name|outMessage
parameter_list|,
name|Object
name|resultPayload
parameter_list|)
function_decl|;
comment|/**      * This method is called when the routing interceptor has intercepted a message from      * the client and needs to extract the request content from the message.  It retreives      * and receives the request content from the incoming message.      * @param inMessage      * @return the request from client      */
DECL|method|getRequestContent (Message inMessage)
name|Object
name|getRequestContent
parameter_list|(
name|Message
name|inMessage
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

