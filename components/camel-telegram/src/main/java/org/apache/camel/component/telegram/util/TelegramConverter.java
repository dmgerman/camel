begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.telegram.util
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
name|util
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Converter
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
name|TelegramConstants
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
name|TelegramMediaType
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
name|TelegramParseMode
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
name|IncomingMessage
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
name|Update
import|;
end_import

begin_comment
comment|/**  * Utilities for converting between Telegram APIs and standard java objects.  */
end_comment

begin_class
annotation|@
name|Converter
argument_list|(
name|generateLoader
operator|=
literal|true
argument_list|)
DECL|class|TelegramConverter
specifier|public
specifier|final
class|class
name|TelegramConverter
block|{
DECL|method|TelegramConverter ()
specifier|private
name|TelegramConverter
parameter_list|()
block|{     }
annotation|@
name|Converter
DECL|method|toString (Update update)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|Update
name|update
parameter_list|)
block|{
return|return
name|update
operator|!=
literal|null
condition|?
name|toString
argument_list|(
name|update
operator|.
name|getMessage
argument_list|()
argument_list|)
else|:
literal|null
return|;
block|}
annotation|@
name|Converter
DECL|method|toString (IncomingMessage message)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|IncomingMessage
name|message
parameter_list|)
block|{
return|return
name|message
operator|!=
literal|null
condition|?
name|message
operator|.
name|getText
argument_list|()
else|:
literal|null
return|;
block|}
comment|/**      * Fallback converter for any unlisted object, using String default mapping.      */
annotation|@
name|Converter
DECL|method|toOutgoingMessage (Object message, Exchange exchange)
specifier|public
specifier|static
name|OutgoingMessage
name|toOutgoingMessage
parameter_list|(
name|Object
name|message
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|content
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|toOutgoingMessage
argument_list|(
name|content
argument_list|,
name|exchange
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toOutgoingMessage (String message, Exchange exchange)
specifier|public
specifier|static
name|OutgoingMessage
name|toOutgoingMessage
parameter_list|(
name|String
name|message
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|message
operator|==
literal|null
condition|)
block|{
comment|// fail fast
return|return
literal|null
return|;
block|}
name|Object
name|typeObj
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|TelegramConstants
operator|.
name|TELEGRAM_MEDIA_TYPE
argument_list|)
decl_stmt|;
name|TelegramMediaType
name|type
decl_stmt|;
if|if
condition|(
name|typeObj
operator|instanceof
name|String
condition|)
block|{
name|type
operator|=
name|TelegramMediaType
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|typeObj
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|type
operator|=
operator|(
name|TelegramMediaType
operator|)
name|typeObj
expr_stmt|;
block|}
comment|// If the message is a string, it will be converted to a OutgoingTextMessage
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
name|type
operator|=
name|TelegramMediaType
operator|.
name|TEXT
expr_stmt|;
block|}
name|OutgoingMessage
name|result
decl_stmt|;
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|TEXT
case|:
block|{
name|OutgoingTextMessage
name|txt
init|=
operator|new
name|OutgoingTextMessage
argument_list|()
decl_stmt|;
name|txt
operator|.
name|setText
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|TelegramParseMode
name|parseMode
init|=
name|getParseMode
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|parseMode
operator|!=
literal|null
condition|)
block|{
name|txt
operator|.
name|setParseMode
argument_list|(
name|parseMode
operator|.
name|getCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|result
operator|=
name|txt
expr_stmt|;
break|break;
block|}
default|default:
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported conversion from String to media type "
operator|+
name|type
argument_list|)
throw|;
block|}
block|}
return|return
name|result
return|;
block|}
annotation|@
name|Converter
DECL|method|toOutgoingMessage (byte[] message, Exchange exchange)
specifier|public
specifier|static
name|OutgoingMessage
name|toOutgoingMessage
parameter_list|(
name|byte
index|[]
name|message
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|message
operator|==
literal|null
condition|)
block|{
comment|// fail fast
return|return
literal|null
return|;
block|}
name|Object
name|typeObj
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|TelegramConstants
operator|.
name|TELEGRAM_MEDIA_TYPE
argument_list|)
decl_stmt|;
name|TelegramMediaType
name|type
decl_stmt|;
if|if
condition|(
name|typeObj
operator|instanceof
name|String
condition|)
block|{
name|type
operator|=
name|TelegramMediaType
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|typeObj
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|type
operator|=
operator|(
name|TelegramMediaType
operator|)
name|typeObj
expr_stmt|;
block|}
comment|// If the message is a string, it will be converted to a OutgoingTextMessage
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Binary message require the header "
operator|+
name|TelegramConstants
operator|.
name|TELEGRAM_MEDIA_TYPE
operator|+
literal|" to be set with an appropriate org.apache.camel.component.telegram"
operator|+
literal|".TelegramMediaType object"
argument_list|)
throw|;
block|}
name|OutgoingMessage
name|result
decl_stmt|;
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|PHOTO_JPG
case|:
case|case
name|PHOTO_PNG
case|:
block|{
name|OutgoingPhotoMessage
name|img
init|=
operator|new
name|OutgoingPhotoMessage
argument_list|()
decl_stmt|;
name|String
name|caption
init|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|TelegramConstants
operator|.
name|TELEGRAM_MEDIA_TITLE_CAPTION
argument_list|)
decl_stmt|;
name|String
name|fileName
init|=
literal|"photo."
operator|+
name|type
operator|.
name|getFileExtension
argument_list|()
decl_stmt|;
name|img
operator|.
name|setCaption
argument_list|(
name|caption
argument_list|)
expr_stmt|;
name|img
operator|.
name|setFilenameWithExtension
argument_list|(
name|fileName
argument_list|)
expr_stmt|;
name|img
operator|.
name|setPhoto
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|result
operator|=
name|img
expr_stmt|;
break|break;
block|}
case|case
name|AUDIO
case|:
block|{
name|OutgoingAudioMessage
name|audio
init|=
operator|new
name|OutgoingAudioMessage
argument_list|()
decl_stmt|;
name|String
name|title
init|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|TelegramConstants
operator|.
name|TELEGRAM_MEDIA_TITLE_CAPTION
argument_list|)
decl_stmt|;
name|String
name|fileName
init|=
literal|"audio."
operator|+
name|type
operator|.
name|getFileExtension
argument_list|()
decl_stmt|;
name|audio
operator|.
name|setTitle
argument_list|(
name|title
argument_list|)
expr_stmt|;
name|audio
operator|.
name|setFilenameWithExtension
argument_list|(
name|fileName
argument_list|)
expr_stmt|;
name|audio
operator|.
name|setAudio
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|result
operator|=
name|audio
expr_stmt|;
break|break;
block|}
case|case
name|VIDEO
case|:
block|{
name|OutgoingVideoMessage
name|video
init|=
operator|new
name|OutgoingVideoMessage
argument_list|()
decl_stmt|;
name|String
name|title
init|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|TelegramConstants
operator|.
name|TELEGRAM_MEDIA_TITLE_CAPTION
argument_list|)
decl_stmt|;
name|String
name|fileName
init|=
literal|"video."
operator|+
name|type
operator|.
name|getFileExtension
argument_list|()
decl_stmt|;
name|video
operator|.
name|setCaption
argument_list|(
name|title
argument_list|)
expr_stmt|;
name|video
operator|.
name|setFilenameWithExtension
argument_list|(
name|fileName
argument_list|)
expr_stmt|;
name|video
operator|.
name|setVideo
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|result
operator|=
name|video
expr_stmt|;
break|break;
block|}
case|case
name|DOCUMENT
case|:
default|default:
block|{
comment|// this can be any file
name|OutgoingDocumentMessage
name|document
init|=
operator|new
name|OutgoingDocumentMessage
argument_list|()
decl_stmt|;
name|String
name|title
init|=
operator|(
name|String
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|TelegramConstants
operator|.
name|TELEGRAM_MEDIA_TITLE_CAPTION
argument_list|)
decl_stmt|;
name|document
operator|.
name|setCaption
argument_list|(
name|title
argument_list|)
expr_stmt|;
name|document
operator|.
name|setFilenameWithExtension
argument_list|(
literal|"file"
argument_list|)
expr_stmt|;
name|document
operator|.
name|setDocument
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|result
operator|=
name|document
expr_stmt|;
break|break;
block|}
block|}
return|return
name|result
return|;
block|}
DECL|method|getParseMode (Exchange exchange)
specifier|private
specifier|static
name|TelegramParseMode
name|getParseMode
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|TelegramParseMode
name|mode
init|=
literal|null
decl_stmt|;
name|Object
name|parseMode
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|TelegramConstants
operator|.
name|TELEGRAM_PARSE_MODE
argument_list|)
decl_stmt|;
if|if
condition|(
name|parseMode
operator|instanceof
name|String
condition|)
block|{
name|mode
operator|=
name|TelegramParseMode
operator|.
name|valueOf
argument_list|(
operator|(
name|String
operator|)
name|parseMode
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|mode
operator|=
operator|(
name|TelegramParseMode
operator|)
name|parseMode
expr_stmt|;
block|}
return|return
name|mode
return|;
block|}
block|}
end_class

end_unit

