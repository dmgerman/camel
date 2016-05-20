begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|util
operator|.
name|Arrays
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

begin_comment
comment|/**  * An outgoing audio message.  */
end_comment

begin_class
DECL|class|OutgoingAudioMessage
specifier|public
class|class
name|OutgoingAudioMessage
extends|extends
name|OutgoingMessage
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|2716544815581270395L
decl_stmt|;
DECL|field|audio
specifier|private
name|byte
index|[]
name|audio
decl_stmt|;
DECL|field|filenameWithExtension
specifier|private
name|String
name|filenameWithExtension
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"duration"
argument_list|)
DECL|field|durationSeconds
specifier|private
name|Integer
name|durationSeconds
decl_stmt|;
DECL|field|performer
specifier|private
name|String
name|performer
decl_stmt|;
DECL|field|title
specifier|private
name|String
name|title
decl_stmt|;
DECL|method|OutgoingAudioMessage ()
specifier|public
name|OutgoingAudioMessage
parameter_list|()
block|{     }
DECL|method|getAudio ()
specifier|public
name|byte
index|[]
name|getAudio
parameter_list|()
block|{
return|return
name|audio
return|;
block|}
DECL|method|setAudio (byte[] audio)
specifier|public
name|void
name|setAudio
parameter_list|(
name|byte
index|[]
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
DECL|method|getFilenameWithExtension ()
specifier|public
name|String
name|getFilenameWithExtension
parameter_list|()
block|{
return|return
name|filenameWithExtension
return|;
block|}
DECL|method|setFilenameWithExtension (String filenameWithExtension)
specifier|public
name|void
name|setFilenameWithExtension
parameter_list|(
name|String
name|filenameWithExtension
parameter_list|)
block|{
name|this
operator|.
name|filenameWithExtension
operator|=
name|filenameWithExtension
expr_stmt|;
block|}
DECL|method|getDurationSeconds ()
specifier|public
name|Integer
name|getDurationSeconds
parameter_list|()
block|{
return|return
name|durationSeconds
return|;
block|}
DECL|method|setDurationSeconds (Integer durationSeconds)
specifier|public
name|void
name|setDurationSeconds
parameter_list|(
name|Integer
name|durationSeconds
parameter_list|)
block|{
name|this
operator|.
name|durationSeconds
operator|=
name|durationSeconds
expr_stmt|;
block|}
DECL|method|getPerformer ()
specifier|public
name|String
name|getPerformer
parameter_list|()
block|{
return|return
name|performer
return|;
block|}
DECL|method|setPerformer (String performer)
specifier|public
name|void
name|setPerformer
parameter_list|(
name|String
name|performer
parameter_list|)
block|{
name|this
operator|.
name|performer
operator|=
name|performer
expr_stmt|;
block|}
DECL|method|getTitle ()
specifier|public
name|String
name|getTitle
parameter_list|()
block|{
return|return
name|title
return|;
block|}
DECL|method|setTitle (String title)
specifier|public
name|void
name|setTitle
parameter_list|(
name|String
name|title
parameter_list|)
block|{
name|this
operator|.
name|title
operator|=
name|title
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
literal|"OutgoingAudioMessage{"
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"audio(length)="
argument_list|)
operator|.
name|append
argument_list|(
name|audio
operator|!=
literal|null
condition|?
name|audio
operator|.
name|length
else|:
literal|null
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", filenameWithExtension='"
argument_list|)
operator|.
name|append
argument_list|(
name|filenameWithExtension
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
literal|", durationSeconds="
argument_list|)
operator|.
name|append
argument_list|(
name|durationSeconds
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", performer='"
argument_list|)
operator|.
name|append
argument_list|(
name|performer
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
literal|", title='"
argument_list|)
operator|.
name|append
argument_list|(
name|title
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
literal|'}'
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|super
operator|.
name|toString
argument_list|()
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

