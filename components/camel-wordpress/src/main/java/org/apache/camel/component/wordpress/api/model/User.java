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
name|io
operator|.
name|Serializable
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
name|java
operator|.
name|util
operator|.
name|Map
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

begin_class
annotation|@
name|JacksonXmlRootElement
annotation|@
name|JsonIgnoreProperties
argument_list|(
name|ignoreUnknown
operator|=
literal|true
argument_list|)
DECL|class|User
specifier|public
class|class
name|User
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
literal|4247427179764560935L
decl_stmt|;
DECL|field|id
specifier|private
name|Integer
name|id
decl_stmt|;
DECL|field|username
specifier|private
name|String
name|username
decl_stmt|;
DECL|field|name
specifier|private
name|String
name|name
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"first_name"
argument_list|)
DECL|field|firstName
specifier|private
name|String
name|firstName
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"last_name"
argument_list|)
DECL|field|lastName
specifier|private
name|String
name|lastName
decl_stmt|;
DECL|field|email
specifier|private
name|String
name|email
decl_stmt|;
DECL|field|url
specifier|private
name|String
name|url
decl_stmt|;
DECL|field|description
specifier|private
name|String
name|description
decl_stmt|;
DECL|field|link
specifier|private
name|String
name|link
decl_stmt|;
DECL|field|locale
specifier|private
name|String
name|locale
decl_stmt|;
DECL|field|nickname
specifier|private
name|String
name|nickname
decl_stmt|;
DECL|field|slug
specifier|private
name|String
name|slug
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"registered_date"
argument_list|)
DECL|field|registeredDate
specifier|private
name|String
name|registeredDate
decl_stmt|;
DECL|field|roles
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|roles
decl_stmt|;
DECL|field|capabilities
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|capabilities
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"extra_capabilities"
argument_list|)
DECL|field|extraCapabilities
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extraCapabilities
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"avatar_urls"
argument_list|)
DECL|field|avatarUrls
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|avatarUrls
decl_stmt|;
annotation|@
name|JsonProperty
argument_list|(
literal|"meta"
argument_list|)
DECL|field|meta
specifier|private
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|meta
decl_stmt|;
DECL|method|User ()
specifier|public
name|User
parameter_list|()
block|{      }
DECL|method|getId ()
specifier|public
name|Integer
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|setId (Integer id)
specifier|public
name|void
name|setId
parameter_list|(
name|Integer
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
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|setName (String name)
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
DECL|method|getFirstName ()
specifier|public
name|String
name|getFirstName
parameter_list|()
block|{
return|return
name|firstName
return|;
block|}
DECL|method|setFirstName (String firstName)
specifier|public
name|void
name|setFirstName
parameter_list|(
name|String
name|firstName
parameter_list|)
block|{
name|this
operator|.
name|firstName
operator|=
name|firstName
expr_stmt|;
block|}
DECL|method|getLastName ()
specifier|public
name|String
name|getLastName
parameter_list|()
block|{
return|return
name|lastName
return|;
block|}
DECL|method|setLastName (String lastName)
specifier|public
name|void
name|setLastName
parameter_list|(
name|String
name|lastName
parameter_list|)
block|{
name|this
operator|.
name|lastName
operator|=
name|lastName
expr_stmt|;
block|}
DECL|method|getEmail ()
specifier|public
name|String
name|getEmail
parameter_list|()
block|{
return|return
name|email
return|;
block|}
DECL|method|setEmail (String email)
specifier|public
name|void
name|setEmail
parameter_list|(
name|String
name|email
parameter_list|)
block|{
name|this
operator|.
name|email
operator|=
name|email
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
DECL|method|getDescription ()
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
DECL|method|setDescription (String description)
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|description
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
DECL|method|getLocale ()
specifier|public
name|String
name|getLocale
parameter_list|()
block|{
return|return
name|locale
return|;
block|}
DECL|method|setLocale (String locale)
specifier|public
name|void
name|setLocale
parameter_list|(
name|String
name|locale
parameter_list|)
block|{
name|this
operator|.
name|locale
operator|=
name|locale
expr_stmt|;
block|}
DECL|method|getNickname ()
specifier|public
name|String
name|getNickname
parameter_list|()
block|{
return|return
name|nickname
return|;
block|}
DECL|method|setNickname (String nickname)
specifier|public
name|void
name|setNickname
parameter_list|(
name|String
name|nickname
parameter_list|)
block|{
name|this
operator|.
name|nickname
operator|=
name|nickname
expr_stmt|;
block|}
DECL|method|getSlug ()
specifier|public
name|String
name|getSlug
parameter_list|()
block|{
return|return
name|slug
return|;
block|}
DECL|method|setSlug (String slug)
specifier|public
name|void
name|setSlug
parameter_list|(
name|String
name|slug
parameter_list|)
block|{
name|this
operator|.
name|slug
operator|=
name|slug
expr_stmt|;
block|}
DECL|method|getRegisteredDate ()
specifier|public
name|String
name|getRegisteredDate
parameter_list|()
block|{
return|return
name|registeredDate
return|;
block|}
DECL|method|setRegisteredDate (String registeredDate)
specifier|public
name|void
name|setRegisteredDate
parameter_list|(
name|String
name|registeredDate
parameter_list|)
block|{
name|this
operator|.
name|registeredDate
operator|=
name|registeredDate
expr_stmt|;
block|}
DECL|method|getRoles ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getRoles
parameter_list|()
block|{
return|return
name|roles
return|;
block|}
DECL|method|setRoles (List<String> roles)
specifier|public
name|void
name|setRoles
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|roles
parameter_list|)
block|{
name|this
operator|.
name|roles
operator|=
name|roles
expr_stmt|;
block|}
DECL|method|getCapabilities ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getCapabilities
parameter_list|()
block|{
return|return
name|capabilities
return|;
block|}
DECL|method|setCapabilities (Map<String, String> capabilities)
specifier|public
name|void
name|setCapabilities
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|capabilities
parameter_list|)
block|{
name|this
operator|.
name|capabilities
operator|=
name|capabilities
expr_stmt|;
block|}
DECL|method|getExtraCapabilities ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getExtraCapabilities
parameter_list|()
block|{
return|return
name|extraCapabilities
return|;
block|}
DECL|method|setExtraCapabilities (Map<String, String> extraCapabilities)
specifier|public
name|void
name|setExtraCapabilities
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extraCapabilities
parameter_list|)
block|{
name|this
operator|.
name|extraCapabilities
operator|=
name|extraCapabilities
expr_stmt|;
block|}
DECL|method|getAvatarUrls ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getAvatarUrls
parameter_list|()
block|{
return|return
name|avatarUrls
return|;
block|}
DECL|method|setAvatarUrls (Map<String, String> avatarUrls)
specifier|public
name|void
name|setAvatarUrls
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|avatarUrls
parameter_list|)
block|{
name|this
operator|.
name|avatarUrls
operator|=
name|avatarUrls
expr_stmt|;
block|}
DECL|method|getMeta ()
specifier|public
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|getMeta
parameter_list|()
block|{
return|return
name|meta
return|;
block|}
DECL|method|setMeta (List<Map<String, String>> meta)
specifier|public
name|void
name|setMeta
parameter_list|(
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
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
name|addValue
argument_list|(
name|this
operator|.
name|id
argument_list|)
operator|.
name|addValue
argument_list|(
name|this
operator|.
name|username
argument_list|)
operator|.
name|addValue
argument_list|(
name|this
operator|.
name|email
argument_list|)
operator|.
name|addValue
argument_list|(
name|this
operator|.
name|name
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

