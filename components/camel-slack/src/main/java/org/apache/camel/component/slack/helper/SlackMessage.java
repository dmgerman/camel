begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.slack.helper
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|slack
operator|.
name|helper
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

begin_class
DECL|class|SlackMessage
specifier|public
class|class
name|SlackMessage
block|{
DECL|field|text
specifier|private
name|String
name|text
decl_stmt|;
DECL|field|channel
specifier|private
name|String
name|channel
decl_stmt|;
DECL|field|username
specifier|private
name|String
name|username
decl_stmt|;
DECL|field|user
specifier|private
name|String
name|user
decl_stmt|;
DECL|field|iconUrl
specifier|private
name|String
name|iconUrl
decl_stmt|;
DECL|field|iconEmoji
specifier|private
name|String
name|iconEmoji
decl_stmt|;
DECL|field|attachments
specifier|private
name|List
argument_list|<
name|Attachment
argument_list|>
name|attachments
decl_stmt|;
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
DECL|method|getChannel ()
specifier|public
name|String
name|getChannel
parameter_list|()
block|{
return|return
name|channel
return|;
block|}
DECL|method|setChannel (String channel)
specifier|public
name|void
name|setChannel
parameter_list|(
name|String
name|channel
parameter_list|)
block|{
name|this
operator|.
name|channel
operator|=
name|channel
expr_stmt|;
block|}
DECL|method|getUsername ()
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|username
return|;
block|}
DECL|method|setUsername (String username)
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
block|}
DECL|method|getUser ()
specifier|public
name|String
name|getUser
parameter_list|()
block|{
return|return
name|user
return|;
block|}
DECL|method|setUser (String user)
specifier|public
name|void
name|setUser
parameter_list|(
name|String
name|user
parameter_list|)
block|{
name|this
operator|.
name|user
operator|=
name|user
expr_stmt|;
block|}
DECL|method|getIconUrl ()
specifier|public
name|String
name|getIconUrl
parameter_list|()
block|{
return|return
name|iconUrl
return|;
block|}
DECL|method|setIconUrl (String iconUrl)
specifier|public
name|void
name|setIconUrl
parameter_list|(
name|String
name|iconUrl
parameter_list|)
block|{
name|this
operator|.
name|iconUrl
operator|=
name|iconUrl
expr_stmt|;
block|}
DECL|method|getIconEmoji ()
specifier|public
name|String
name|getIconEmoji
parameter_list|()
block|{
return|return
name|iconEmoji
return|;
block|}
DECL|method|setIconEmoji (String iconEmoji)
specifier|public
name|void
name|setIconEmoji
parameter_list|(
name|String
name|iconEmoji
parameter_list|)
block|{
name|this
operator|.
name|iconEmoji
operator|=
name|iconEmoji
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
DECL|class|Attachment
specifier|public
class|class
name|Attachment
block|{
DECL|field|fallback
specifier|private
name|String
name|fallback
decl_stmt|;
DECL|field|color
specifier|private
name|String
name|color
decl_stmt|;
DECL|field|pretext
specifier|private
name|String
name|pretext
decl_stmt|;
DECL|field|authorName
specifier|private
name|String
name|authorName
decl_stmt|;
DECL|field|authorLink
specifier|private
name|String
name|authorLink
decl_stmt|;
DECL|field|authorIcon
specifier|private
name|String
name|authorIcon
decl_stmt|;
DECL|field|title
specifier|private
name|String
name|title
decl_stmt|;
DECL|field|titleLink
specifier|private
name|String
name|titleLink
decl_stmt|;
DECL|field|text
specifier|private
name|String
name|text
decl_stmt|;
DECL|field|imageUrl
specifier|private
name|String
name|imageUrl
decl_stmt|;
DECL|field|thumbUrl
specifier|private
name|String
name|thumbUrl
decl_stmt|;
DECL|field|footer
specifier|private
name|String
name|footer
decl_stmt|;
DECL|field|footerIcon
specifier|private
name|String
name|footerIcon
decl_stmt|;
DECL|field|ts
specifier|private
name|Long
name|ts
decl_stmt|;
DECL|field|fields
specifier|private
name|List
argument_list|<
name|Field
argument_list|>
name|fields
decl_stmt|;
DECL|method|getFallback ()
specifier|public
name|String
name|getFallback
parameter_list|()
block|{
return|return
name|fallback
return|;
block|}
DECL|method|setFallback (String fallback)
specifier|public
name|void
name|setFallback
parameter_list|(
name|String
name|fallback
parameter_list|)
block|{
name|this
operator|.
name|fallback
operator|=
name|fallback
expr_stmt|;
block|}
DECL|method|getColor ()
specifier|public
name|String
name|getColor
parameter_list|()
block|{
return|return
name|color
return|;
block|}
DECL|method|setColor (String color)
specifier|public
name|void
name|setColor
parameter_list|(
name|String
name|color
parameter_list|)
block|{
name|this
operator|.
name|color
operator|=
name|color
expr_stmt|;
block|}
DECL|method|getPretext ()
specifier|public
name|String
name|getPretext
parameter_list|()
block|{
return|return
name|pretext
return|;
block|}
DECL|method|setPretext (String pretext)
specifier|public
name|void
name|setPretext
parameter_list|(
name|String
name|pretext
parameter_list|)
block|{
name|this
operator|.
name|pretext
operator|=
name|pretext
expr_stmt|;
block|}
DECL|method|getAuthorName ()
specifier|public
name|String
name|getAuthorName
parameter_list|()
block|{
return|return
name|authorName
return|;
block|}
DECL|method|setAuthorName (String authorName)
specifier|public
name|void
name|setAuthorName
parameter_list|(
name|String
name|authorName
parameter_list|)
block|{
name|this
operator|.
name|authorName
operator|=
name|authorName
expr_stmt|;
block|}
DECL|method|getAuthorLink ()
specifier|public
name|String
name|getAuthorLink
parameter_list|()
block|{
return|return
name|authorLink
return|;
block|}
DECL|method|setAuthorLink (String authorLink)
specifier|public
name|void
name|setAuthorLink
parameter_list|(
name|String
name|authorLink
parameter_list|)
block|{
name|this
operator|.
name|authorLink
operator|=
name|authorLink
expr_stmt|;
block|}
DECL|method|getAuthorIcon ()
specifier|public
name|String
name|getAuthorIcon
parameter_list|()
block|{
return|return
name|authorIcon
return|;
block|}
DECL|method|setAuthorIcon (String authorIcon)
specifier|public
name|void
name|setAuthorIcon
parameter_list|(
name|String
name|authorIcon
parameter_list|)
block|{
name|this
operator|.
name|authorIcon
operator|=
name|authorIcon
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
DECL|method|getTitleLink ()
specifier|public
name|String
name|getTitleLink
parameter_list|()
block|{
return|return
name|titleLink
return|;
block|}
DECL|method|setTitleLink (String titleLink)
specifier|public
name|void
name|setTitleLink
parameter_list|(
name|String
name|titleLink
parameter_list|)
block|{
name|this
operator|.
name|titleLink
operator|=
name|titleLink
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
DECL|method|getImageUrl ()
specifier|public
name|String
name|getImageUrl
parameter_list|()
block|{
return|return
name|imageUrl
return|;
block|}
DECL|method|setImageUrl (String imageUrl)
specifier|public
name|void
name|setImageUrl
parameter_list|(
name|String
name|imageUrl
parameter_list|)
block|{
name|this
operator|.
name|imageUrl
operator|=
name|imageUrl
expr_stmt|;
block|}
DECL|method|getThumbUrl ()
specifier|public
name|String
name|getThumbUrl
parameter_list|()
block|{
return|return
name|thumbUrl
return|;
block|}
DECL|method|setThumbUrl (String thumbUrl)
specifier|public
name|void
name|setThumbUrl
parameter_list|(
name|String
name|thumbUrl
parameter_list|)
block|{
name|this
operator|.
name|thumbUrl
operator|=
name|thumbUrl
expr_stmt|;
block|}
DECL|method|getFooter ()
specifier|public
name|String
name|getFooter
parameter_list|()
block|{
return|return
name|footer
return|;
block|}
DECL|method|setFooter (String footer)
specifier|public
name|void
name|setFooter
parameter_list|(
name|String
name|footer
parameter_list|)
block|{
name|this
operator|.
name|footer
operator|=
name|footer
expr_stmt|;
block|}
DECL|method|getFooterIcon ()
specifier|public
name|String
name|getFooterIcon
parameter_list|()
block|{
return|return
name|footerIcon
return|;
block|}
DECL|method|setFooterIcon (String footerIcon)
specifier|public
name|void
name|setFooterIcon
parameter_list|(
name|String
name|footerIcon
parameter_list|)
block|{
name|this
operator|.
name|footerIcon
operator|=
name|footerIcon
expr_stmt|;
block|}
DECL|method|getTs ()
specifier|public
name|Long
name|getTs
parameter_list|()
block|{
return|return
name|ts
return|;
block|}
DECL|method|setTs (Long ts)
specifier|public
name|void
name|setTs
parameter_list|(
name|Long
name|ts
parameter_list|)
block|{
name|this
operator|.
name|ts
operator|=
name|ts
expr_stmt|;
block|}
DECL|method|getFields ()
specifier|public
name|List
argument_list|<
name|Field
argument_list|>
name|getFields
parameter_list|()
block|{
return|return
name|fields
return|;
block|}
DECL|method|setFields (List<Field> fields)
specifier|public
name|void
name|setFields
parameter_list|(
name|List
argument_list|<
name|Field
argument_list|>
name|fields
parameter_list|)
block|{
name|this
operator|.
name|fields
operator|=
name|fields
expr_stmt|;
block|}
DECL|class|Field
specifier|public
class|class
name|Field
block|{
DECL|field|title
specifier|private
name|String
name|title
decl_stmt|;
DECL|field|value
specifier|private
name|String
name|value
decl_stmt|;
DECL|field|shortValue
specifier|private
name|Boolean
name|shortValue
decl_stmt|;
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
DECL|method|getValue ()
specifier|public
name|String
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
DECL|method|setValue (String value)
specifier|public
name|void
name|setValue
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
DECL|method|isShortValue ()
specifier|public
name|Boolean
name|isShortValue
parameter_list|()
block|{
return|return
name|shortValue
return|;
block|}
DECL|method|setShortValue (Boolean shortValue)
specifier|public
name|void
name|setShortValue
parameter_list|(
name|Boolean
name|shortValue
parameter_list|)
block|{
name|this
operator|.
name|shortValue
operator|=
name|shortValue
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

