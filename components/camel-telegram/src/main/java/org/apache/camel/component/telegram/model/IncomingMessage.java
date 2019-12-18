begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.telegram.model
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
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Instant
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
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|annotation
operator|.
name|JsonIgnoreProperties
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
name|JsonProperty
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
name|annotation
operator|.
name|JsonDeserialize
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
name|annotation
operator|.
name|JsonSerialize
import|;
end_import

begin_comment
comment|/**  * A message that is exchanged with the Telegram network.  */
end_comment

begin_class
annotation|@
name|JsonIgnoreProperties
argument_list|(
name|ignoreUnknown
operator|=
literal|true
argument_list|)
DECL|class|IncomingMessage
specifier|public
class|class
name|IncomingMessage
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|7592193511885686637L
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"message_id"
argument_list|)
DECL|field|messageId
specifier|private
name|Long
name|messageId
decl_stmt|;
annotation|@
name|JsonDeserialize
argument_list|(
name|using
operator|=
name|UnixTimestampDeserializer
operator|.
name|class
argument_list|)
annotation|@
name|JsonSerialize
argument_list|(
name|using
operator|=
name|UnixTimestampSerializer
operator|.
name|class
argument_list|)
DECL|field|date
specifier|private
name|Instant
name|date
decl_stmt|;
DECL|field|from
specifier|private
name|User
name|from
decl_stmt|;
DECL|field|text
specifier|private
name|String
name|text
decl_stmt|;
DECL|field|chat
specifier|private
name|Chat
name|chat
decl_stmt|;
DECL|field|photo
specifier|private
name|List
argument_list|<
name|IncomingPhotoSize
argument_list|>
name|photo
decl_stmt|;
DECL|field|video
specifier|private
name|IncomingVideo
name|video
decl_stmt|;
DECL|field|audio
specifier|private
name|IncomingAudio
name|audio
decl_stmt|;
DECL|field|document
specifier|private
name|IncomingDocument
name|document
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"location"
argument_list|)
DECL|field|location
specifier|private
name|Location
name|location
decl_stmt|;
DECL|method|IncomingMessage ()
specifier|public
name|IncomingMessage
parameter_list|()
block|{     }
DECL|method|getMessageId ()
specifier|public
name|Long
name|getMessageId
parameter_list|()
block|{
return|return
name|messageId
return|;
block|}
DECL|method|setMessageId (Long messageId)
specifier|public
name|void
name|setMessageId
parameter_list|(
name|Long
name|messageId
parameter_list|)
block|{
name|this
operator|.
name|messageId
operator|=
name|messageId
expr_stmt|;
block|}
DECL|method|getDate ()
specifier|public
name|Instant
name|getDate
parameter_list|()
block|{
return|return
name|date
return|;
block|}
DECL|method|setDate (Instant date)
specifier|public
name|void
name|setDate
parameter_list|(
name|Instant
name|date
parameter_list|)
block|{
name|this
operator|.
name|date
operator|=
name|date
expr_stmt|;
block|}
DECL|method|getFrom ()
specifier|public
name|User
name|getFrom
parameter_list|()
block|{
return|return
name|from
return|;
block|}
DECL|method|setFrom (User from)
specifier|public
name|void
name|setFrom
parameter_list|(
name|User
name|from
parameter_list|)
block|{
name|this
operator|.
name|from
operator|=
name|from
expr_stmt|;
block|}
DECL|method|getText ()
specifier|public
name|String
name|getText
parameter_list|()
block|{
return|return
name|text
return|;
block|}
DECL|method|setText (String text)
specifier|public
name|void
name|setText
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|this
operator|.
name|text
operator|=
name|text
expr_stmt|;
block|}
DECL|method|getChat ()
specifier|public
name|Chat
name|getChat
parameter_list|()
block|{
return|return
name|chat
return|;
block|}
DECL|method|setChat (Chat chat)
specifier|public
name|void
name|setChat
parameter_list|(
name|Chat
name|chat
parameter_list|)
block|{
name|this
operator|.
name|chat
operator|=
name|chat
expr_stmt|;
block|}
DECL|method|getPhoto ()
specifier|public
name|List
argument_list|<
name|IncomingPhotoSize
argument_list|>
name|getPhoto
parameter_list|()
block|{
return|return
name|photo
return|;
block|}
DECL|method|setPhoto (List<IncomingPhotoSize> photo)
specifier|public
name|void
name|setPhoto
parameter_list|(
name|List
argument_list|<
name|IncomingPhotoSize
argument_list|>
name|photo
parameter_list|)
block|{
name|this
operator|.
name|photo
operator|=
name|photo
expr_stmt|;
block|}
DECL|method|getVideo ()
specifier|public
name|IncomingVideo
name|getVideo
parameter_list|()
block|{
return|return
name|video
return|;
block|}
DECL|method|setVideo (IncomingVideo video)
specifier|public
name|void
name|setVideo
parameter_list|(
name|IncomingVideo
name|video
parameter_list|)
block|{
name|this
operator|.
name|video
operator|=
name|video
expr_stmt|;
block|}
DECL|method|getAudio ()
specifier|public
name|IncomingAudio
name|getAudio
parameter_list|()
block|{
return|return
name|audio
return|;
block|}
DECL|method|setAudio (IncomingAudio audio)
specifier|public
name|void
name|setAudio
parameter_list|(
name|IncomingAudio
name|audio
parameter_list|)
block|{
name|this
operator|.
name|audio
operator|=
name|audio
expr_stmt|;
block|}
DECL|method|getDocument ()
specifier|public
name|IncomingDocument
name|getDocument
parameter_list|()
block|{
return|return
name|document
return|;
block|}
DECL|method|setDocument (IncomingDocument document)
specifier|public
name|void
name|setDocument
parameter_list|(
name|IncomingDocument
name|document
parameter_list|)
block|{
name|this
operator|.
name|document
operator|=
name|document
expr_stmt|;
block|}
DECL|method|getLocation ()
specifier|public
name|Location
name|getLocation
parameter_list|()
block|{
return|return
name|location
return|;
block|}
DECL|method|setLocation (Location location)
specifier|public
name|void
name|setLocation
parameter_list|(
name|Location
name|location
parameter_list|)
block|{
name|this
operator|.
name|location
operator|=
name|location
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
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"IncomingMessage{"
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"messageId="
argument_list|)
operator|.
name|append
argument_list|(
name|messageId
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", date="
argument_list|)
operator|.
name|append
argument_list|(
name|date
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", from="
argument_list|)
operator|.
name|append
argument_list|(
name|from
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", text='"
argument_list|)
operator|.
name|append
argument_list|(
name|text
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", chat="
argument_list|)
operator|.
name|append
argument_list|(
name|chat
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", photo="
argument_list|)
operator|.
name|append
argument_list|(
name|photo
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", video="
argument_list|)
operator|.
name|append
argument_list|(
name|video
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", audio="
argument_list|)
operator|.
name|append
argument_list|(
name|audio
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", document="
argument_list|)
operator|.
name|append
argument_list|(
name|document
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", location="
argument_list|)
operator|.
name|append
argument_list|(
name|location
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

