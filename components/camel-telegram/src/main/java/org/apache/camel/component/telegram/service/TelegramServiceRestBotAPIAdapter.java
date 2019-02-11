begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.telegram.service
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|telegram
operator|.
name|service
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
import|;
end_import

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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MultivaluedHashMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MultivaluedMap
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|annotation
operator|.
name|JsonInclude
operator|.
name|Include
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectMapper
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|jaxrs
operator|.
name|json
operator|.
name|JacksonJsonProvider
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
name|telegram
operator|.
name|TelegramService
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
name|telegram
operator|.
name|model
operator|.
name|EditMessageLiveLocationMessage
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
name|telegram
operator|.
name|model
operator|.
name|MessageResult
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
name|telegram
operator|.
name|model
operator|.
name|OutgoingAudioMessage
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
name|telegram
operator|.
name|model
operator|.
name|OutgoingDocumentMessage
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
name|telegram
operator|.
name|model
operator|.
name|OutgoingMessage
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
name|telegram
operator|.
name|model
operator|.
name|OutgoingPhotoMessage
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
name|telegram
operator|.
name|model
operator|.
name|OutgoingTextMessage
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
name|telegram
operator|.
name|model
operator|.
name|OutgoingVideoMessage
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
name|telegram
operator|.
name|model
operator|.
name|SendLocationMessage
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
name|telegram
operator|.
name|model
operator|.
name|SendVenueMessage
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
name|telegram
operator|.
name|model
operator|.
name|StopMessageLiveLocationMessage
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
name|telegram
operator|.
name|model
operator|.
name|UpdateResult
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
name|jaxrs
operator|.
name|client
operator|.
name|JAXRSClientFactory
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
name|jaxrs
operator|.
name|client
operator|.
name|WebClient
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
name|jaxrs
operator|.
name|ext
operator|.
name|multipart
operator|.
name|Attachment
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
name|jaxrs
operator|.
name|ext
operator|.
name|multipart
operator|.
name|ContentDisposition
import|;
end_import

begin_comment
comment|/**  * Adapts the {@code RestBotAPI} to the {@code TelegramService} interface.  */
end_comment

begin_class
DECL|class|TelegramServiceRestBotAPIAdapter
specifier|public
class|class
name|TelegramServiceRestBotAPIAdapter
implements|implements
name|TelegramService
block|{
DECL|field|api
specifier|private
name|RestBotAPI
name|api
decl_stmt|;
DECL|method|TelegramServiceRestBotAPIAdapter ()
specifier|public
name|TelegramServiceRestBotAPIAdapter
parameter_list|()
block|{
name|this
operator|.
name|api
operator|=
name|JAXRSClientFactory
operator|.
name|create
argument_list|(
name|RestBotAPI
operator|.
name|BOT_API_DEFAULT_URL
argument_list|,
name|RestBotAPI
operator|.
name|class
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|providerByCustomObjectMapper
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|WebClient
operator|.
name|getConfig
argument_list|(
name|this
operator|.
name|api
argument_list|)
operator|.
name|getHttpConduit
argument_list|()
operator|.
name|getClient
argument_list|()
operator|.
name|setAllowChunking
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|TelegramServiceRestBotAPIAdapter (RestBotAPI api)
specifier|public
name|TelegramServiceRestBotAPIAdapter
parameter_list|(
name|RestBotAPI
name|api
parameter_list|)
block|{
name|this
operator|.
name|api
operator|=
name|api
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getUpdates (String authorizationToken, Long offset, Integer limit, Integer timeoutSeconds)
specifier|public
name|UpdateResult
name|getUpdates
parameter_list|(
name|String
name|authorizationToken
parameter_list|,
name|Long
name|offset
parameter_list|,
name|Integer
name|limit
parameter_list|,
name|Integer
name|timeoutSeconds
parameter_list|)
block|{
return|return
name|api
operator|.
name|getUpdates
argument_list|(
name|authorizationToken
argument_list|,
name|offset
argument_list|,
name|limit
argument_list|,
name|timeoutSeconds
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|sendMessage (String authorizationToken, OutgoingMessage message)
specifier|public
name|Object
name|sendMessage
parameter_list|(
name|String
name|authorizationToken
parameter_list|,
name|OutgoingMessage
name|message
parameter_list|)
block|{
name|Object
name|resultMessage
decl_stmt|;
if|if
condition|(
name|message
operator|instanceof
name|OutgoingTextMessage
condition|)
block|{
name|resultMessage
operator|=
name|this
operator|.
name|sendMessage
argument_list|(
name|authorizationToken
argument_list|,
operator|(
name|OutgoingTextMessage
operator|)
name|message
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|message
operator|instanceof
name|OutgoingPhotoMessage
condition|)
block|{
name|resultMessage
operator|=
name|this
operator|.
name|sendMessage
argument_list|(
name|authorizationToken
argument_list|,
operator|(
name|OutgoingPhotoMessage
operator|)
name|message
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|message
operator|instanceof
name|OutgoingAudioMessage
condition|)
block|{
name|resultMessage
operator|=
name|this
operator|.
name|sendMessage
argument_list|(
name|authorizationToken
argument_list|,
operator|(
name|OutgoingAudioMessage
operator|)
name|message
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|message
operator|instanceof
name|OutgoingVideoMessage
condition|)
block|{
name|resultMessage
operator|=
name|this
operator|.
name|sendMessage
argument_list|(
name|authorizationToken
argument_list|,
operator|(
name|OutgoingVideoMessage
operator|)
name|message
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|message
operator|instanceof
name|OutgoingDocumentMessage
condition|)
block|{
name|resultMessage
operator|=
name|this
operator|.
name|sendMessage
argument_list|(
name|authorizationToken
argument_list|,
operator|(
name|OutgoingDocumentMessage
operator|)
name|message
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|message
operator|instanceof
name|SendLocationMessage
condition|)
block|{
name|resultMessage
operator|=
name|api
operator|.
name|sendLocation
argument_list|(
name|authorizationToken
argument_list|,
operator|(
name|SendLocationMessage
operator|)
name|message
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|message
operator|instanceof
name|EditMessageLiveLocationMessage
condition|)
block|{
name|resultMessage
operator|=
name|api
operator|.
name|editMessageLiveLocation
argument_list|(
name|authorizationToken
argument_list|,
operator|(
name|EditMessageLiveLocationMessage
operator|)
name|message
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|message
operator|instanceof
name|StopMessageLiveLocationMessage
condition|)
block|{
name|resultMessage
operator|=
name|api
operator|.
name|stopMessageLiveLocation
argument_list|(
name|authorizationToken
argument_list|,
operator|(
name|StopMessageLiveLocationMessage
operator|)
name|message
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|message
operator|instanceof
name|SendVenueMessage
condition|)
block|{
name|resultMessage
operator|=
name|api
operator|.
name|sendVenue
argument_list|(
name|authorizationToken
argument_list|,
operator|(
name|SendVenueMessage
operator|)
name|message
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported message type "
operator|+
operator|(
name|message
operator|!=
literal|null
condition|?
name|message
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
else|:
literal|null
operator|)
argument_list|)
throw|;
block|}
return|return
name|resultMessage
return|;
block|}
DECL|method|sendMessage (String authorizationToken, OutgoingTextMessage message)
specifier|private
name|MessageResult
name|sendMessage
parameter_list|(
name|String
name|authorizationToken
parameter_list|,
name|OutgoingTextMessage
name|message
parameter_list|)
block|{
return|return
name|api
operator|.
name|sendMessage
argument_list|(
name|authorizationToken
argument_list|,
name|message
argument_list|)
return|;
block|}
DECL|method|sendMessage (String authorizationToken, OutgoingPhotoMessage message)
specifier|private
name|MessageResult
name|sendMessage
parameter_list|(
name|String
name|authorizationToken
parameter_list|,
name|OutgoingPhotoMessage
name|message
parameter_list|)
block|{
name|List
argument_list|<
name|Attachment
argument_list|>
name|parts
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
name|fillCommonMediaParts
argument_list|(
name|parts
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|parts
operator|.
name|add
argument_list|(
name|buildMediaPart
argument_list|(
literal|"photo"
argument_list|,
name|message
operator|.
name|getFilenameWithExtension
argument_list|()
argument_list|,
name|message
operator|.
name|getPhoto
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|message
operator|.
name|getCaption
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|parts
operator|.
name|add
argument_list|(
name|buildTextPart
argument_list|(
literal|"caption"
argument_list|,
name|message
operator|.
name|getCaption
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|api
operator|.
name|sendPhoto
argument_list|(
name|authorizationToken
argument_list|,
name|parts
argument_list|)
return|;
block|}
DECL|method|sendMessage (String authorizationToken, OutgoingAudioMessage message)
specifier|private
name|MessageResult
name|sendMessage
parameter_list|(
name|String
name|authorizationToken
parameter_list|,
name|OutgoingAudioMessage
name|message
parameter_list|)
block|{
name|List
argument_list|<
name|Attachment
argument_list|>
name|parts
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
name|fillCommonMediaParts
argument_list|(
name|parts
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|parts
operator|.
name|add
argument_list|(
name|buildMediaPart
argument_list|(
literal|"audio"
argument_list|,
name|message
operator|.
name|getFilenameWithExtension
argument_list|()
argument_list|,
name|message
operator|.
name|getAudio
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|message
operator|.
name|getTitle
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|parts
operator|.
name|add
argument_list|(
name|buildTextPart
argument_list|(
literal|"title"
argument_list|,
name|message
operator|.
name|getTitle
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|message
operator|.
name|getDurationSeconds
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|parts
operator|.
name|add
argument_list|(
name|buildTextPart
argument_list|(
literal|"duration"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|message
operator|.
name|getDurationSeconds
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|message
operator|.
name|getPerformer
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|parts
operator|.
name|add
argument_list|(
name|buildTextPart
argument_list|(
literal|"performer"
argument_list|,
name|message
operator|.
name|getPerformer
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|api
operator|.
name|sendAudio
argument_list|(
name|authorizationToken
argument_list|,
name|parts
argument_list|)
return|;
block|}
DECL|method|sendMessage (String authorizationToken, OutgoingVideoMessage message)
specifier|private
name|MessageResult
name|sendMessage
parameter_list|(
name|String
name|authorizationToken
parameter_list|,
name|OutgoingVideoMessage
name|message
parameter_list|)
block|{
name|List
argument_list|<
name|Attachment
argument_list|>
name|parts
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
name|fillCommonMediaParts
argument_list|(
name|parts
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|parts
operator|.
name|add
argument_list|(
name|buildMediaPart
argument_list|(
literal|"video"
argument_list|,
name|message
operator|.
name|getFilenameWithExtension
argument_list|()
argument_list|,
name|message
operator|.
name|getVideo
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|message
operator|.
name|getCaption
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|parts
operator|.
name|add
argument_list|(
name|buildTextPart
argument_list|(
literal|"caption"
argument_list|,
name|message
operator|.
name|getCaption
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|message
operator|.
name|getDurationSeconds
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|parts
operator|.
name|add
argument_list|(
name|buildTextPart
argument_list|(
literal|"duration"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|message
operator|.
name|getDurationSeconds
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|message
operator|.
name|getWidth
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|parts
operator|.
name|add
argument_list|(
name|buildTextPart
argument_list|(
literal|"width"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|message
operator|.
name|getWidth
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|message
operator|.
name|getHeight
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|parts
operator|.
name|add
argument_list|(
name|buildTextPart
argument_list|(
literal|"height"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|message
operator|.
name|getHeight
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|api
operator|.
name|sendVideo
argument_list|(
name|authorizationToken
argument_list|,
name|parts
argument_list|)
return|;
block|}
DECL|method|sendMessage (String authorizationToken, OutgoingDocumentMessage message)
specifier|private
name|MessageResult
name|sendMessage
parameter_list|(
name|String
name|authorizationToken
parameter_list|,
name|OutgoingDocumentMessage
name|message
parameter_list|)
block|{
name|List
argument_list|<
name|Attachment
argument_list|>
name|parts
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
name|fillCommonMediaParts
argument_list|(
name|parts
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|parts
operator|.
name|add
argument_list|(
name|buildMediaPart
argument_list|(
literal|"document"
argument_list|,
name|message
operator|.
name|getFilenameWithExtension
argument_list|()
argument_list|,
name|message
operator|.
name|getDocument
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|message
operator|.
name|getCaption
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|parts
operator|.
name|add
argument_list|(
name|buildTextPart
argument_list|(
literal|"caption"
argument_list|,
name|message
operator|.
name|getCaption
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|api
operator|.
name|sendDocument
argument_list|(
name|authorizationToken
argument_list|,
name|parts
argument_list|)
return|;
block|}
DECL|method|fillCommonMediaParts (List<Attachment> parts, OutgoingMessage message)
specifier|private
name|void
name|fillCommonMediaParts
parameter_list|(
name|List
argument_list|<
name|Attachment
argument_list|>
name|parts
parameter_list|,
name|OutgoingMessage
name|message
parameter_list|)
block|{
name|parts
operator|.
name|add
argument_list|(
name|buildTextPart
argument_list|(
literal|"chat_id"
argument_list|,
name|message
operator|.
name|getChatId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|message
operator|.
name|getReplyToMessageId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|parts
operator|.
name|add
argument_list|(
name|buildTextPart
argument_list|(
literal|"reply_to_message_id"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|message
operator|.
name|getReplyToMessageId
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|message
operator|.
name|getDisableNotification
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|parts
operator|.
name|add
argument_list|(
name|buildTextPart
argument_list|(
literal|"disable_notification"
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|message
operator|.
name|getDisableNotification
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|buildTextPart (String name, String value)
specifier|private
name|Attachment
name|buildTextPart
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|MultivaluedMap
name|m
init|=
operator|new
name|MultivaluedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|m
operator|.
name|putSingle
argument_list|(
literal|"Content-Type"
argument_list|,
literal|"text/plain"
argument_list|)
expr_stmt|;
name|m
operator|.
name|putSingle
argument_list|(
literal|"Content-Disposition"
argument_list|,
literal|"form-data; name=\""
operator|+
name|escapeMimeName
argument_list|(
name|name
argument_list|)
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|Attachment
name|a
init|=
operator|new
name|Attachment
argument_list|(
name|m
argument_list|,
name|value
argument_list|)
decl_stmt|;
return|return
name|a
return|;
block|}
DECL|method|buildMediaPart (String name, String fileNameWithExtension, byte[] value)
specifier|private
name|Attachment
name|buildMediaPart
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|fileNameWithExtension
parameter_list|,
name|byte
index|[]
name|value
parameter_list|)
block|{
name|Attachment
name|a
init|=
operator|new
name|Attachment
argument_list|(
name|name
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|value
argument_list|)
argument_list|,
operator|new
name|ContentDisposition
argument_list|(
literal|"form-data; name=\""
operator|+
name|escapeMimeName
argument_list|(
name|name
argument_list|)
operator|+
literal|"\"; filename=\""
operator|+
name|escapeMimeName
argument_list|(
name|fileNameWithExtension
argument_list|)
operator|+
literal|"\""
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|a
return|;
block|}
DECL|method|escapeMimeName (String name)
specifier|private
name|String
name|escapeMimeName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|name
operator|.
name|replace
argument_list|(
literal|"\""
argument_list|,
literal|""
argument_list|)
return|;
block|}
DECL|method|providerByCustomObjectMapper ()
specifier|private
name|JacksonJsonProvider
name|providerByCustomObjectMapper
parameter_list|()
block|{
name|ObjectMapper
name|mapper
init|=
operator|new
name|ObjectMapper
argument_list|()
decl_stmt|;
name|mapper
operator|.
name|setSerializationInclusion
argument_list|(
name|Include
operator|.
name|NON_NULL
argument_list|)
expr_stmt|;
return|return
operator|new
name|JacksonJsonProvider
argument_list|(
name|mapper
argument_list|)
return|;
block|}
block|}
end_class

end_unit

