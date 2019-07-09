begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.attachment
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|attachment
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
import|;
end_import

begin_comment
comment|/**  * Extended {@link Message} for Java Attachment Support (with javax.activation).  */
end_comment

begin_interface
DECL|interface|AttachmentMessage
specifier|public
interface|interface
name|AttachmentMessage
extends|extends
name|Message
block|{
comment|/**      * Returns the attachment specified by the id      *      * @param id the id under which the attachment is stored      * @return the data handler for this attachment or<tt>null</tt>      */
DECL|method|getAttachment (String id)
name|DataHandler
name|getAttachment
parameter_list|(
name|String
name|id
parameter_list|)
function_decl|;
comment|/**      * Returns the attachment specified by the id      *      * @param id the id under which the attachment is stored      * @return the attachment or<tt>null</tt>      */
DECL|method|getAttachmentObject (String id)
name|Attachment
name|getAttachmentObject
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
comment|/**      * Adds an attachment to the message using the id      *      * @param id        the id to store the attachment under      * @param content   the attachment      */
DECL|method|addAttachmentObject (String id, Attachment content)
name|void
name|addAttachmentObject
parameter_list|(
name|String
name|id
parameter_list|,
name|Attachment
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
comment|/**      * Returns all attachments of the message      *      * @return the attachments in a map or<tt>null</tt>      */
DECL|method|getAttachmentObjects ()
name|Map
argument_list|<
name|String
argument_list|,
name|Attachment
argument_list|>
name|getAttachmentObjects
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
comment|/**      * Set all the attachments associated with this message      *      * @param attachments the attachments      */
DECL|method|setAttachmentObjects (Map<String, Attachment> attachments)
name|void
name|setAttachmentObjects
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Attachment
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
block|}
end_interface

end_unit

