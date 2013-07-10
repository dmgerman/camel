begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.yammer.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|yammer
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|jackson
operator|.
name|annotate
operator|.
name|JsonIgnoreProperties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|jackson
operator|.
name|annotate
operator|.
name|JsonProperty
import|;
end_import

begin_class
annotation|@
name|JsonIgnoreProperties
argument_list|(
name|ignoreUnknown
operator|=
literal|true
argument_list|)
DECL|class|Message
specifier|public
class|class
name|Message
block|{
annotation|@
name|JsonProperty
argument_list|(
literal|"replied_to_id"
argument_list|)
DECL|field|repliedToId
specifier|private
name|String
name|repliedToId
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"network_id"
argument_list|)
DECL|field|networkId
specifier|private
name|Long
name|networkId
decl_stmt|;
DECL|field|url
specifier|private
name|String
name|url
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"thread_id"
argument_list|)
DECL|field|threadId
specifier|private
name|Long
name|threadId
decl_stmt|;
DECL|field|id
specifier|private
name|Long
name|id
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"message_type"
argument_list|)
DECL|field|messageType
specifier|private
name|String
name|messageType
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"chat_client_sequence"
argument_list|)
DECL|field|chatClientSequence
specifier|private
name|String
name|chatClientSequence
decl_stmt|;
DECL|field|body
specifier|private
name|Body
name|body
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"client_url"
argument_list|)
DECL|field|clientUrl
specifier|private
name|String
name|clientUrl
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"content_excerpt"
argument_list|)
DECL|field|contentExcerpt
specifier|private
name|String
name|contentExcerpt
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"created_at"
argument_list|)
DECL|field|createdAt
specifier|private
name|String
name|createdAt
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"client_type"
argument_list|)
DECL|field|clientType
specifier|private
name|String
name|clientType
decl_stmt|;
DECL|field|privacy
specifier|private
name|String
name|privacy
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"sender_type"
argument_list|)
DECL|field|senderType
specifier|private
name|String
name|senderType
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"liked_by"
argument_list|)
DECL|field|likedBy
specifier|private
name|LikedBy
name|likedBy
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"sender_id"
argument_list|)
DECL|field|senderId
specifier|private
name|Long
name|senderId
decl_stmt|;
DECL|field|language
specifier|private
name|String
name|language
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"system_message"
argument_list|)
DECL|field|systemMessage
specifier|private
name|Boolean
name|systemMessage
decl_stmt|;
DECL|field|attachments
specifier|private
name|List
argument_list|<
name|Attachment
argument_list|>
name|attachments
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"direct_message"
argument_list|)
DECL|field|directMessage
specifier|private
name|Boolean
name|directMessage
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"web_url"
argument_list|)
DECL|field|webUrl
specifier|private
name|String
name|webUrl
decl_stmt|;
DECL|method|getRepliedToId ()
specifier|public
name|String
name|getRepliedToId
parameter_list|()
block|{
return|return
name|repliedToId
return|;
block|}
DECL|method|setRepliedToId (String repliedToId)
specifier|public
name|void
name|setRepliedToId
parameter_list|(
name|String
name|repliedToId
parameter_list|)
block|{
name|this
operator|.
name|repliedToId
operator|=
name|repliedToId
expr_stmt|;
block|}
DECL|method|getNetworkId ()
specifier|public
name|Long
name|getNetworkId
parameter_list|()
block|{
return|return
name|networkId
return|;
block|}
DECL|method|setNetworkId (Long networkId)
specifier|public
name|void
name|setNetworkId
parameter_list|(
name|Long
name|networkId
parameter_list|)
block|{
name|this
operator|.
name|networkId
operator|=
name|networkId
expr_stmt|;
block|}
DECL|method|getUrl ()
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
name|url
return|;
block|}
DECL|method|setUrl (String url)
specifier|public
name|void
name|setUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
block|}
DECL|method|getThreadId ()
specifier|public
name|Long
name|getThreadId
parameter_list|()
block|{
return|return
name|threadId
return|;
block|}
DECL|method|setThreadId (Long threadId)
specifier|public
name|void
name|setThreadId
parameter_list|(
name|Long
name|threadId
parameter_list|)
block|{
name|this
operator|.
name|threadId
operator|=
name|threadId
expr_stmt|;
block|}
DECL|method|getId ()
specifier|public
name|Long
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|setId (Long id)
specifier|public
name|void
name|setId
parameter_list|(
name|Long
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
DECL|method|getMessageType ()
specifier|public
name|String
name|getMessageType
parameter_list|()
block|{
return|return
name|messageType
return|;
block|}
DECL|method|setMessageType (String messageType)
specifier|public
name|void
name|setMessageType
parameter_list|(
name|String
name|messageType
parameter_list|)
block|{
name|this
operator|.
name|messageType
operator|=
name|messageType
expr_stmt|;
block|}
DECL|method|getChatClientSequence ()
specifier|public
name|String
name|getChatClientSequence
parameter_list|()
block|{
return|return
name|chatClientSequence
return|;
block|}
DECL|method|setChatClientSequence (String chatClientSequence)
specifier|public
name|void
name|setChatClientSequence
parameter_list|(
name|String
name|chatClientSequence
parameter_list|)
block|{
name|this
operator|.
name|chatClientSequence
operator|=
name|chatClientSequence
expr_stmt|;
block|}
DECL|method|getBody ()
specifier|public
name|Body
name|getBody
parameter_list|()
block|{
return|return
name|body
return|;
block|}
DECL|method|setBody (Body body)
specifier|public
name|void
name|setBody
parameter_list|(
name|Body
name|body
parameter_list|)
block|{
name|this
operator|.
name|body
operator|=
name|body
expr_stmt|;
block|}
DECL|method|getClientUrl ()
specifier|public
name|String
name|getClientUrl
parameter_list|()
block|{
return|return
name|clientUrl
return|;
block|}
DECL|method|setClientUrl (String clientUrl)
specifier|public
name|void
name|setClientUrl
parameter_list|(
name|String
name|clientUrl
parameter_list|)
block|{
name|this
operator|.
name|clientUrl
operator|=
name|clientUrl
expr_stmt|;
block|}
DECL|method|getContentExcerpt ()
specifier|public
name|String
name|getContentExcerpt
parameter_list|()
block|{
return|return
name|contentExcerpt
return|;
block|}
DECL|method|setContentExcerpt (String contentExcerpt)
specifier|public
name|void
name|setContentExcerpt
parameter_list|(
name|String
name|contentExcerpt
parameter_list|)
block|{
name|this
operator|.
name|contentExcerpt
operator|=
name|contentExcerpt
expr_stmt|;
block|}
DECL|method|getCreatedAt ()
specifier|public
name|String
name|getCreatedAt
parameter_list|()
block|{
return|return
name|createdAt
return|;
block|}
DECL|method|setCreatedAt (String createdAt)
specifier|public
name|void
name|setCreatedAt
parameter_list|(
name|String
name|createdAt
parameter_list|)
block|{
name|this
operator|.
name|createdAt
operator|=
name|createdAt
expr_stmt|;
block|}
DECL|method|getClientType ()
specifier|public
name|String
name|getClientType
parameter_list|()
block|{
return|return
name|clientType
return|;
block|}
DECL|method|setClientType (String clientType)
specifier|public
name|void
name|setClientType
parameter_list|(
name|String
name|clientType
parameter_list|)
block|{
name|this
operator|.
name|clientType
operator|=
name|clientType
expr_stmt|;
block|}
DECL|method|getPrivacy ()
specifier|public
name|String
name|getPrivacy
parameter_list|()
block|{
return|return
name|privacy
return|;
block|}
DECL|method|setPrivacy (String privacy)
specifier|public
name|void
name|setPrivacy
parameter_list|(
name|String
name|privacy
parameter_list|)
block|{
name|this
operator|.
name|privacy
operator|=
name|privacy
expr_stmt|;
block|}
DECL|method|getSenderType ()
specifier|public
name|String
name|getSenderType
parameter_list|()
block|{
return|return
name|senderType
return|;
block|}
DECL|method|setSenderType (String senderType)
specifier|public
name|void
name|setSenderType
parameter_list|(
name|String
name|senderType
parameter_list|)
block|{
name|this
operator|.
name|senderType
operator|=
name|senderType
expr_stmt|;
block|}
DECL|method|getLikedBy ()
specifier|public
name|LikedBy
name|getLikedBy
parameter_list|()
block|{
return|return
name|likedBy
return|;
block|}
DECL|method|setLikedBy (LikedBy likedBy)
specifier|public
name|void
name|setLikedBy
parameter_list|(
name|LikedBy
name|likedBy
parameter_list|)
block|{
name|this
operator|.
name|likedBy
operator|=
name|likedBy
expr_stmt|;
block|}
DECL|method|getSenderId ()
specifier|public
name|Long
name|getSenderId
parameter_list|()
block|{
return|return
name|senderId
return|;
block|}
DECL|method|setSenderId (Long senderId)
specifier|public
name|void
name|setSenderId
parameter_list|(
name|Long
name|senderId
parameter_list|)
block|{
name|this
operator|.
name|senderId
operator|=
name|senderId
expr_stmt|;
block|}
DECL|method|getLanguage ()
specifier|public
name|String
name|getLanguage
parameter_list|()
block|{
return|return
name|language
return|;
block|}
DECL|method|setLanguage (String language)
specifier|public
name|void
name|setLanguage
parameter_list|(
name|String
name|language
parameter_list|)
block|{
name|this
operator|.
name|language
operator|=
name|language
expr_stmt|;
block|}
DECL|method|getSystemMessage ()
specifier|public
name|Boolean
name|getSystemMessage
parameter_list|()
block|{
return|return
name|systemMessage
return|;
block|}
DECL|method|setSystemMessage (Boolean systemMessage)
specifier|public
name|void
name|setSystemMessage
parameter_list|(
name|Boolean
name|systemMessage
parameter_list|)
block|{
name|this
operator|.
name|systemMessage
operator|=
name|systemMessage
expr_stmt|;
block|}
DECL|method|getAttachments ()
specifier|public
name|List
argument_list|<
name|Attachment
argument_list|>
name|getAttachments
parameter_list|()
block|{
return|return
name|attachments
return|;
block|}
DECL|method|setAttachments (List<Attachment> attachments)
specifier|public
name|void
name|setAttachments
parameter_list|(
name|List
argument_list|<
name|Attachment
argument_list|>
name|attachments
parameter_list|)
block|{
name|this
operator|.
name|attachments
operator|=
name|attachments
expr_stmt|;
block|}
DECL|method|getDirectMessage ()
specifier|public
name|Boolean
name|getDirectMessage
parameter_list|()
block|{
return|return
name|directMessage
return|;
block|}
DECL|method|setDirectMessage (Boolean directMessage)
specifier|public
name|void
name|setDirectMessage
parameter_list|(
name|Boolean
name|directMessage
parameter_list|)
block|{
name|this
operator|.
name|directMessage
operator|=
name|directMessage
expr_stmt|;
block|}
DECL|method|getWebUrl ()
specifier|public
name|String
name|getWebUrl
parameter_list|()
block|{
return|return
name|webUrl
return|;
block|}
DECL|method|setWebUrl (String webUrl)
specifier|public
name|void
name|setWebUrl
parameter_list|(
name|String
name|webUrl
parameter_list|)
block|{
name|this
operator|.
name|webUrl
operator|=
name|webUrl
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Message [repliedToId="
operator|+
name|repliedToId
operator|+
literal|", networkId="
operator|+
name|networkId
operator|+
literal|", url="
operator|+
name|url
operator|+
literal|", threadId="
operator|+
name|threadId
operator|+
literal|", id="
operator|+
name|id
operator|+
literal|", messageType="
operator|+
name|messageType
operator|+
literal|", chatClientSequence="
operator|+
name|chatClientSequence
operator|+
literal|", body="
operator|+
name|body
operator|+
literal|", clientUrl="
operator|+
name|clientUrl
operator|+
literal|", contentExcerpt="
operator|+
name|contentExcerpt
operator|+
literal|", createdAt="
operator|+
name|createdAt
operator|+
literal|", clientType="
operator|+
name|clientType
operator|+
literal|", privacy="
operator|+
name|privacy
operator|+
literal|", senderType="
operator|+
name|senderType
operator|+
literal|", likedBy="
operator|+
name|likedBy
operator|+
literal|", senderId="
operator|+
name|senderId
operator|+
literal|", language="
operator|+
name|language
operator|+
literal|", systemMessage="
operator|+
name|systemMessage
operator|+
literal|", attachments="
operator|+
name|attachments
operator|+
literal|", directMessage="
operator|+
name|directMessage
operator|+
literal|", webUrl="
operator|+
name|webUrl
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

