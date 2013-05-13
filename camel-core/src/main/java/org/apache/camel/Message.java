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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|DataHandler
import|;
end_import

begin_comment
comment|/**  * Implements the<a  * href="http://camel.apache.org/message.html">Message</a> pattern and  * represents an inbound or outbound message as part of an {@link Exchange}.  *<p/>  * See {@link org.apache.camel.impl.DefaultMessage DefaultMessage} for how headers  * is represented in Camel using a {@link org.apache.camel.util.CaseInsensitiveMap CaseInsensitiveMap}.  *  * @version   */
end_comment

begin_interface
DECL|interface|Message
specifier|public
interface|interface
name|Message
block|{
comment|/**      * Returns the id of the message      *      * @return the message id      */
DECL|method|getMessageId ()
name|String
name|getMessageId
parameter_list|()
function_decl|;
comment|/**      * Sets the id of the message      *      * @param messageId id of the message      */
DECL|method|setMessageId (String messageId)
name|void
name|setMessageId
parameter_list|(
name|String
name|messageId
parameter_list|)
function_decl|;
comment|/**      * Returns the exchange this message is related to      *      * @return the exchange      */
DECL|method|getExchange ()
name|Exchange
name|getExchange
parameter_list|()
function_decl|;
comment|/**      * Returns true if this message represents a fault      *      * @return<tt>true</tt> if this is a fault message,<tt>false</tt> for regular messages.      */
DECL|method|isFault ()
name|boolean
name|isFault
parameter_list|()
function_decl|;
comment|/**      * Sets the fault flag on this message      *      * @param fault the fault flag      */
DECL|method|setFault (boolean fault)
name|void
name|setFault
parameter_list|(
name|boolean
name|fault
parameter_list|)
function_decl|;
comment|/**      * Accesses a specific header      *      * @param name  name of header      * @return the value of the given header or<tt>null</tt> if there is no      *         header for the given name      */
DECL|method|getHeader (String name)
name|Object
name|getHeader
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Accesses a specific header      *      * @param name  name of header      * @param defaultValue the default value to return if header was absent      * @return the value of the given header or<tt>defaultValue</tt> if there is no      *         header for the given name      */
DECL|method|getHeader (String name, Object defaultValue)
name|Object
name|getHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|defaultValue
parameter_list|)
function_decl|;
comment|/**      * Returns a header associated with this message by name and specifying the      * type required      *      * @param name the name of the header      * @param type the type of the header      * @return the value of the given header or<tt>null</tt> if there is no header for      *         the given name      * @throws TypeConversionException is thrown if error during type conversion      */
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
comment|/**      * Returns a header associated with this message by name and specifying the      * type required      *      * @param name the name of the header      * @param defaultValue the default value to return if header was absent      * @param type the type of the header      * @return the value of the given header or<tt>defaultValue</tt> if there is no header for      *         the given name or<tt>null</tt> if it cannot be converted to the given type      */
DECL|method|getHeader (String name, Object defaultValue, Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|T
name|getHeader
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|defaultValue
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**      * Sets a header on the message      *      * @param name of the header      * @param value to associate with the name      */
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
comment|/**      * Removes the named header from this message      *      * @param name name of the header      * @return the old value of the header      */
DECL|method|removeHeader (String name)
name|Object
name|removeHeader
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Removes the headers from this message      *      * @param pattern pattern of names      * @return boolean whether any headers matched      */
DECL|method|removeHeaders (String pattern)
name|boolean
name|removeHeaders
parameter_list|(
name|String
name|pattern
parameter_list|)
function_decl|;
comment|/**      * Removes the headers from this message that match the given<tt>pattern</tt>,       * except for the ones matching one ore more<tt>excludePatterns</tt>      *       * @param pattern pattern of names that should be removed      * @param excludePatterns one or more pattern of header names that should be excluded (= preserved)      * @return boolean whether any headers matched      */
DECL|method|removeHeaders (String pattern, String... excludePatterns)
name|boolean
name|removeHeaders
parameter_list|(
name|String
name|pattern
parameter_list|,
name|String
modifier|...
name|excludePatterns
parameter_list|)
function_decl|;
comment|/**      * Returns all of the headers associated with the message.      *<p/>      * See {@link org.apache.camel.impl.DefaultMessage DefaultMessage} for how headers      * is represented in Camel using a {@link org.apache.camel.util.CaseInsensitiveMap CaseInsensitiveMap}.      *<p/>      *<b>Important:</b> If you want to walk the returned {@link Map} and fetch all the keys and values, you should use      * the {@link java.util.Map#entrySet()} method, which ensure you get the keys in the original case.      *      * @return all the headers in a Map      */
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
comment|/**      * Set all the headers associated with this message      *<p/>      *<b>Important:</b> If you want to copy headers from another {@link Message} to this {@link Message}, then      * use<tt>getHeaders().putAll(other)</tt> to copy the headers, where<tt>other</tt> is the other headers.      *      * @param headers headers to set      */
DECL|method|setHeaders (Map<String, Object> headers)
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
comment|/**      * Returns whether has any headers has been set.      *      * @return<tt>true</tt> if any headers has been set      */
DECL|method|hasHeaders ()
name|boolean
name|hasHeaders
parameter_list|()
function_decl|;
comment|/**      * Returns the body of the message as a POJO      *<p/>      * The body can be<tt>null</tt> if no body is set      *      * @return the body, can be<tt>null</tt>      */
DECL|method|getBody ()
name|Object
name|getBody
parameter_list|()
function_decl|;
comment|/**      * Returns the body of the message as a POJO      *      * @return the body, is never<tt>null</tt>      * @throws InvalidPayloadException Is thrown if the body being<tt>null</tt> or wrong class type      */
DECL|method|getMandatoryBody ()
name|Object
name|getMandatoryBody
parameter_list|()
throws|throws
name|InvalidPayloadException
function_decl|;
comment|/**      * Returns the body as the specified type      *      * @param type the type that the body      * @return the body of the message as the specified type, or<tt>null</tt> if no body exists      * @throws TypeConversionException is thrown if error during type conversion      */
DECL|method|getBody (Class<T> type)
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
comment|/**      * Returns the mandatory body as the specified type      *      * @param type the type that the body      * @return the body of the message as the specified type, is never<tt>null</tt>.      * @throws InvalidPayloadException Is thrown if the body being<tt>null</tt> or wrong class type      */
DECL|method|getMandatoryBody (Class<T> type)
parameter_list|<
name|T
parameter_list|>
name|T
name|getMandatoryBody
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
throws|throws
name|InvalidPayloadException
function_decl|;
comment|/**      * Sets the body of the message      *      * @param body the body      */
DECL|method|setBody (Object body)
name|void
name|setBody
parameter_list|(
name|Object
name|body
parameter_list|)
function_decl|;
comment|/**      * Sets the body of the message as a specific type      *      * @param body the body      * @param type the type of the body      */
DECL|method|setBody (Object body, Class<T> type)
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
comment|/**      * Creates a copy of this message so that it can be used and possibly      * modified further in another exchange      *      * @return a new message instance copied from this message      */
DECL|method|copy ()
name|Message
name|copy
parameter_list|()
function_decl|;
comment|/**      * Copies the contents of the other message into this message      *      * @param message the other message      */
DECL|method|copyFrom (Message message)
name|void
name|copyFrom
parameter_list|(
name|Message
name|message
parameter_list|)
function_decl|;
comment|/**      * Returns the attachment specified by the id      *      * @param id the id under which the attachment is stored      * @return the data handler for this attachment or<tt>null</tt>      */
DECL|method|getAttachment (String id)
name|DataHandler
name|getAttachment
parameter_list|(
name|String
name|id
parameter_list|)
function_decl|;
comment|/**      * Returns a set of attachment names of the message      *      * @return a set of attachment names      */
DECL|method|getAttachmentNames ()
name|Set
argument_list|<
name|String
argument_list|>
name|getAttachmentNames
parameter_list|()
function_decl|;
comment|/**      * Removes the attachment specified by the id      *      * @param id   the id of the attachment to remove      */
DECL|method|removeAttachment (String id)
name|void
name|removeAttachment
parameter_list|(
name|String
name|id
parameter_list|)
function_decl|;
comment|/**      * Adds an attachment to the message using the id      *      * @param id        the id to store the attachment under      * @param content   the data handler for the attachment      */
DECL|method|addAttachment (String id, DataHandler content)
name|void
name|addAttachment
parameter_list|(
name|String
name|id
parameter_list|,
name|DataHandler
name|content
parameter_list|)
function_decl|;
comment|/**      * Returns all attachments of the message      *      * @return the attachments in a map or<tt>null</tt>      */
DECL|method|getAttachments ()
name|Map
argument_list|<
name|String
argument_list|,
name|DataHandler
argument_list|>
name|getAttachments
parameter_list|()
function_decl|;
comment|/**      * Set all the attachments associated with this message      *      * @param attachments the attachments      */
DECL|method|setAttachments (Map<String, DataHandler> attachments)
name|void
name|setAttachments
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|DataHandler
argument_list|>
name|attachments
parameter_list|)
function_decl|;
comment|/**      * Returns whether this message has attachments.      *      * @return<tt>true</tt> if this message has any attachments.      */
DECL|method|hasAttachments ()
name|boolean
name|hasAttachments
parameter_list|()
function_decl|;
comment|/**      * Returns the unique ID for a message exchange if this message is capable      * of creating one or<tt>null</tt> if not      *      * @return the created exchange id, or<tt>null</tt> if not capable of creating      * @deprecated will be removed in Camel 3.0. It is discouraged for messages to create exchange ids      */
annotation|@
name|Deprecated
DECL|method|createExchangeId ()
name|String
name|createExchangeId
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

