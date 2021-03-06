begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.wordpress.api.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|wordpress
operator|.
name|api
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
name|dataformat
operator|.
name|xml
operator|.
name|annotation
operator|.
name|JacksonXmlRootElement
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|base
operator|.
name|MoreObjects
operator|.
name|toStringHelper
import|;
end_import

begin_comment
comment|/**  * Describes a object that may be published on the Wordpress engine, eg. a Post, a Page etc.  */
end_comment

begin_class
annotation|@
name|JacksonXmlRootElement
argument_list|(
name|localName
operator|=
literal|"textPublishable"
argument_list|)
annotation|@
name|JsonIgnoreProperties
argument_list|(
name|ignoreUnknown
operator|=
literal|true
argument_list|)
DECL|class|TextPublishable
specifier|public
specifier|abstract
class|class
name|TextPublishable
extends|extends
name|Publishable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|2913318702739560478L
decl_stmt|;
DECL|field|guid
specifier|private
name|Content
name|guid
decl_stmt|;
DECL|field|link
specifier|private
name|String
name|link
decl_stmt|;
DECL|field|status
specifier|private
name|PublishableStatus
name|status
decl_stmt|;
DECL|field|type
specifier|private
name|String
name|type
decl_stmt|;
DECL|field|title
specifier|private
name|Content
name|title
decl_stmt|;
DECL|field|content
specifier|private
name|Content
name|content
decl_stmt|;
DECL|field|excerpt
specifier|private
name|Content
name|excerpt
decl_stmt|;
DECL|field|template
specifier|private
name|String
name|template
decl_stmt|;
DECL|field|meta
specifier|private
name|List
argument_list|<
name|Content
argument_list|>
name|meta
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"comment_status"
argument_list|)
DECL|field|commentStatus
specifier|private
name|PostCommentStatus
name|commentStatus
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"ping_status"
argument_list|)
DECL|field|pingStatus
specifier|private
name|PingStatus
name|pingStatus
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"featured_media"
argument_list|)
DECL|field|featuredMedia
specifier|private
name|Integer
name|featuredMedia
decl_stmt|;
DECL|method|TextPublishable ()
specifier|public
name|TextPublishable
parameter_list|()
block|{      }
DECL|method|getTitle ()
specifier|public
name|Content
name|getTitle
parameter_list|()
block|{
return|return
name|title
return|;
block|}
DECL|method|setTitle (Content title)
specifier|public
name|void
name|setTitle
parameter_list|(
name|Content
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
DECL|method|getContent ()
specifier|public
name|Content
name|getContent
parameter_list|()
block|{
return|return
name|content
return|;
block|}
DECL|method|setContent (Content content)
specifier|public
name|void
name|setContent
parameter_list|(
name|Content
name|content
parameter_list|)
block|{
name|this
operator|.
name|content
operator|=
name|content
expr_stmt|;
block|}
DECL|method|getExcerpt ()
specifier|public
name|Content
name|getExcerpt
parameter_list|()
block|{
return|return
name|excerpt
return|;
block|}
DECL|method|setExcerpt (Content excerpt)
specifier|public
name|void
name|setExcerpt
parameter_list|(
name|Content
name|excerpt
parameter_list|)
block|{
name|this
operator|.
name|excerpt
operator|=
name|excerpt
expr_stmt|;
block|}
DECL|method|getGuid ()
specifier|public
name|Content
name|getGuid
parameter_list|()
block|{
return|return
name|guid
return|;
block|}
DECL|method|setGuid (Content guid)
specifier|public
name|void
name|setGuid
parameter_list|(
name|Content
name|guid
parameter_list|)
block|{
name|this
operator|.
name|guid
operator|=
name|guid
expr_stmt|;
block|}
DECL|method|getLink ()
specifier|public
name|String
name|getLink
parameter_list|()
block|{
return|return
name|link
return|;
block|}
DECL|method|setLink (String link)
specifier|public
name|void
name|setLink
parameter_list|(
name|String
name|link
parameter_list|)
block|{
name|this
operator|.
name|link
operator|=
name|link
expr_stmt|;
block|}
DECL|method|getStatus ()
specifier|public
name|PublishableStatus
name|getStatus
parameter_list|()
block|{
return|return
name|status
return|;
block|}
DECL|method|setStatus (PublishableStatus status)
specifier|public
name|void
name|setStatus
parameter_list|(
name|PublishableStatus
name|status
parameter_list|)
block|{
name|this
operator|.
name|status
operator|=
name|status
expr_stmt|;
block|}
DECL|method|getType ()
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
DECL|method|setType (String type)
specifier|public
name|void
name|setType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
DECL|method|getFeaturedMedia ()
specifier|public
name|Integer
name|getFeaturedMedia
parameter_list|()
block|{
return|return
name|featuredMedia
return|;
block|}
DECL|method|setFeaturedMedia (Integer featuredMedia)
specifier|public
name|void
name|setFeaturedMedia
parameter_list|(
name|Integer
name|featuredMedia
parameter_list|)
block|{
name|this
operator|.
name|featuredMedia
operator|=
name|featuredMedia
expr_stmt|;
block|}
DECL|method|getCommentStatus ()
specifier|public
name|PostCommentStatus
name|getCommentStatus
parameter_list|()
block|{
return|return
name|commentStatus
return|;
block|}
DECL|method|setCommentStatus (PostCommentStatus commentStatus)
specifier|public
name|void
name|setCommentStatus
parameter_list|(
name|PostCommentStatus
name|commentStatus
parameter_list|)
block|{
name|this
operator|.
name|commentStatus
operator|=
name|commentStatus
expr_stmt|;
block|}
DECL|method|getPingStatus ()
specifier|public
name|PingStatus
name|getPingStatus
parameter_list|()
block|{
return|return
name|pingStatus
return|;
block|}
DECL|method|setPingStatus (PingStatus pingStatus)
specifier|public
name|void
name|setPingStatus
parameter_list|(
name|PingStatus
name|pingStatus
parameter_list|)
block|{
name|this
operator|.
name|pingStatus
operator|=
name|pingStatus
expr_stmt|;
block|}
DECL|method|getMeta ()
specifier|public
name|List
argument_list|<
name|Content
argument_list|>
name|getMeta
parameter_list|()
block|{
return|return
name|meta
return|;
block|}
DECL|method|setMeta (List<Content> meta)
specifier|public
name|void
name|setMeta
parameter_list|(
name|List
argument_list|<
name|Content
argument_list|>
name|meta
parameter_list|)
block|{
name|this
operator|.
name|meta
operator|=
name|meta
expr_stmt|;
block|}
DECL|method|getTemplate ()
specifier|public
name|String
name|getTemplate
parameter_list|()
block|{
return|return
name|template
return|;
block|}
DECL|method|setTemplate (String template)
specifier|public
name|void
name|setTemplate
parameter_list|(
name|String
name|template
parameter_list|)
block|{
name|this
operator|.
name|template
operator|=
name|template
expr_stmt|;
block|}
comment|// @formatter:off
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|toStringHelper
argument_list|(
name|this
argument_list|)
operator|.
name|add
argument_list|(
literal|"ID"
argument_list|,
name|this
operator|.
name|getId
argument_list|()
argument_list|)
operator|.
name|add
argument_list|(
literal|"Status"
argument_list|,
name|this
operator|.
name|getStatus
argument_list|()
argument_list|)
operator|.
name|addValue
argument_list|(
name|this
operator|.
name|guid
argument_list|)
operator|.
name|addValue
argument_list|(
name|this
operator|.
name|getTitle
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
comment|// @formatter:on
block|}
end_class

end_unit

