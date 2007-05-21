begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * Implements the<a href="http://activemq.apache.org/camel/message.html">Message</a>  * pattern and represents an inbound or outbound message as part of an {@link Exchange}  *   * @version $Revision$  */
end_comment

begin_interface
DECL|interface|Message
specifier|public
interface|interface
name|Message
block|{
comment|/**      * @return the id of the message      */
DECL|method|getMessageId ()
name|String
name|getMessageId
parameter_list|()
function_decl|;
comment|/**      * set the id of the message      * @param messageId      */
DECL|method|setMessageId (String messageId)
name|void
name|setMessageId
parameter_list|(
name|String
name|messageId
parameter_list|)
function_decl|;
comment|/**      * Returns the exchange this message is related to      *       * @return      */
DECL|method|getExchange ()
name|Exchange
name|getExchange
parameter_list|()
function_decl|;
comment|/**      * Accesses a specific header      *      * @param name      * @return object header associated with the name      */
DECL|method|getHeader (String name)
name|Object
name|getHeader
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns a header associated with this message by name and specifying the type required      *      * @param name the name of the header      * @param type the type of the header      * @return the value of the given header or null if there is no property for the given name or it cannot be      * converted to the given type      */
DECL|method|getHeader (String name, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|T
name|getHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Sets a header on the message      *      * @param name  of the header      * @param value to associate with the name      */
DECL|method|setHeader (String name, Object value)
name|void
name|setHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
function_decl|;
comment|/**      * Returns all of the headers associated with the message      *      * @return all the headers in a Map      */
DECL|method|getHeaders ()
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getHeaders
parameter_list|()
function_decl|;
comment|/**      * Set all the headers associated with this message      * @param headers      */
DECL|method|setHeaders (Map<String,Object> headers)
name|void
name|setHeaders
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|)
function_decl|;
comment|/**      * Returns the body of the message as a POJO      *      * @return the body of the message      */
DECL|method|getBody ()
specifier|public
name|Object
name|getBody
parameter_list|()
function_decl|;
comment|/**      * Returns the body as the specified type      *      * @param type the type that the body      * @return the body of the message as the specified type      */
DECL|method|getBody (Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|getBody
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Sets the body of the message      */
DECL|method|setBody (Object body)
specifier|public
name|void
name|setBody
parameter_list|(
name|Object
name|body
parameter_list|)
function_decl|;
comment|/**      * Sets the body of the message as a specific type      */
DECL|method|setBody (Object body, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|void
name|setBody
parameter_list|(
name|Object
name|body
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Creates a copy of this message so that it can be used and possibly modified further in another exchange      *       * @return a new message instance copied from this message      */
DECL|method|copy ()
name|Message
name|copy
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

