begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.servicenow
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|servicenow
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|JsonInclude
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
name|DeserializationFeature
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|UriParam
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
name|spi
operator|.
name|UriParams
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|ServiceNowConfiguration
specifier|public
class|class
name|ServiceNowConfiguration
block|{
DECL|field|MAPPER
specifier|private
specifier|static
specifier|final
name|ObjectMapper
name|MAPPER
init|=
operator|new
name|ObjectMapper
argument_list|()
operator|.
name|configure
argument_list|(
name|DeserializationFeature
operator|.
name|FAIL_ON_UNKNOWN_PROPERTIES
argument_list|,
literal|false
argument_list|)
operator|.
name|setSerializationInclusion
argument_list|(
name|JsonInclude
operator|.
name|Include
operator|.
name|NON_NULL
argument_list|)
decl_stmt|;
annotation|@
name|UriParam
DECL|field|userName
specifier|private
name|String
name|userName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
annotation|@
name|UriParam
DECL|field|oauthClientId
specifier|private
name|String
name|oauthClientId
decl_stmt|;
annotation|@
name|UriParam
DECL|field|oauthClientSecret
specifier|private
name|String
name|oauthClientSecret
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|oauthTokenUrl
specifier|private
name|String
name|oauthTokenUrl
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|apiUrl
specifier|private
name|String
name|apiUrl
decl_stmt|;
annotation|@
name|UriParam
DECL|field|table
specifier|private
name|String
name|table
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|excludeReferenceLink
specifier|private
name|Boolean
name|excludeReferenceLink
init|=
literal|false
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|suppressAutoSysField
specifier|private
name|Boolean
name|suppressAutoSysField
init|=
literal|false
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|displayValue
specifier|private
name|String
name|displayValue
init|=
literal|"false"
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|inputDisplayValue
specifier|private
name|Boolean
name|inputDisplayValue
init|=
literal|false
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|prefix
operator|=
literal|"model."
argument_list|,
name|multiValue
operator|=
literal|true
argument_list|,
name|javaType
operator|=
literal|"java.lang.String"
argument_list|)
DECL|field|models
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|models
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|mapper
specifier|private
name|ObjectMapper
name|mapper
init|=
name|MAPPER
decl_stmt|;
DECL|method|getUserName ()
specifier|public
name|String
name|getUserName
parameter_list|()
block|{
return|return
name|userName
return|;
block|}
DECL|method|getApiUrl ()
specifier|public
name|String
name|getApiUrl
parameter_list|()
block|{
return|return
name|apiUrl
return|;
block|}
comment|/**      * The ServiceNow REST API url      */
DECL|method|setApiUrl (String apiUrl)
specifier|public
name|void
name|setApiUrl
parameter_list|(
name|String
name|apiUrl
parameter_list|)
block|{
name|this
operator|.
name|apiUrl
operator|=
name|apiUrl
expr_stmt|;
block|}
DECL|method|hasApiUrl ()
specifier|public
name|boolean
name|hasApiUrl
parameter_list|()
block|{
return|return
name|apiUrl
operator|!=
literal|null
return|;
block|}
comment|/**      * ServiceNow user account name, MUST be provided      */
DECL|method|setUserName (String userName)
specifier|public
name|void
name|setUserName
parameter_list|(
name|String
name|userName
parameter_list|)
block|{
name|this
operator|.
name|userName
operator|=
name|userName
expr_stmt|;
block|}
DECL|method|getPassword ()
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
comment|/**      * ServiceNow account password, MUST be provided      */
DECL|method|setPassword (String password)
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
DECL|method|getOauthClientId ()
specifier|public
name|String
name|getOauthClientId
parameter_list|()
block|{
return|return
name|oauthClientId
return|;
block|}
comment|/**      * OAuth2 ClientID      */
DECL|method|setOauthClientId (String oauthClientId)
specifier|public
name|void
name|setOauthClientId
parameter_list|(
name|String
name|oauthClientId
parameter_list|)
block|{
name|this
operator|.
name|oauthClientId
operator|=
name|oauthClientId
expr_stmt|;
block|}
DECL|method|getOauthClientSecret ()
specifier|public
name|String
name|getOauthClientSecret
parameter_list|()
block|{
return|return
name|oauthClientSecret
return|;
block|}
comment|/**      * OAuth2 ClientSecret      */
DECL|method|setOauthClientSecret (String oauthClientSecret)
specifier|public
name|void
name|setOauthClientSecret
parameter_list|(
name|String
name|oauthClientSecret
parameter_list|)
block|{
name|this
operator|.
name|oauthClientSecret
operator|=
name|oauthClientSecret
expr_stmt|;
block|}
DECL|method|getOauthTokenUrl ()
specifier|public
name|String
name|getOauthTokenUrl
parameter_list|()
block|{
return|return
name|oauthTokenUrl
return|;
block|}
DECL|method|hasOautTokenUrl ()
specifier|public
name|boolean
name|hasOautTokenUrl
parameter_list|()
block|{
return|return
name|oauthTokenUrl
operator|!=
literal|null
return|;
block|}
comment|/**      * OAuth token Url      */
DECL|method|setOauthTokenUrl (String oauthTokenUrl)
specifier|public
name|void
name|setOauthTokenUrl
parameter_list|(
name|String
name|oauthTokenUrl
parameter_list|)
block|{
name|this
operator|.
name|oauthTokenUrl
operator|=
name|oauthTokenUrl
expr_stmt|;
block|}
DECL|method|hasBasicAuthentication ()
specifier|public
name|boolean
name|hasBasicAuthentication
parameter_list|()
block|{
return|return
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|userName
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|password
argument_list|)
return|;
block|}
DECL|method|hasOAuthAuthentication ()
specifier|public
name|boolean
name|hasOAuthAuthentication
parameter_list|()
block|{
return|return
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|userName
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|password
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|oauthClientId
argument_list|)
operator|&&
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|oauthClientSecret
argument_list|)
return|;
block|}
DECL|method|getTable ()
specifier|public
name|String
name|getTable
parameter_list|()
block|{
return|return
name|table
return|;
block|}
comment|/**      * The default table, can be overridden by header CamelServiceNowTable      */
DECL|method|setTable (String table)
specifier|public
name|void
name|setTable
parameter_list|(
name|String
name|table
parameter_list|)
block|{
name|this
operator|.
name|table
operator|=
name|table
expr_stmt|;
block|}
DECL|method|getExcludeReferenceLink ()
specifier|public
name|Boolean
name|getExcludeReferenceLink
parameter_list|()
block|{
return|return
name|excludeReferenceLink
return|;
block|}
comment|/**      * True to exclude Table API links for reference fields (default: false)      */
DECL|method|setExcludeReferenceLink (Boolean excludeReferenceLink)
specifier|public
name|void
name|setExcludeReferenceLink
parameter_list|(
name|Boolean
name|excludeReferenceLink
parameter_list|)
block|{
name|this
operator|.
name|excludeReferenceLink
operator|=
name|excludeReferenceLink
expr_stmt|;
block|}
DECL|method|getSuppressAutoSysField ()
specifier|public
name|Boolean
name|getSuppressAutoSysField
parameter_list|()
block|{
return|return
name|suppressAutoSysField
return|;
block|}
comment|/**      * True to suppress auto generation of system fields (default: false)      */
DECL|method|setSuppressAutoSysField (Boolean suppressAutoSysField)
specifier|public
name|void
name|setSuppressAutoSysField
parameter_list|(
name|Boolean
name|suppressAutoSysField
parameter_list|)
block|{
name|this
operator|.
name|suppressAutoSysField
operator|=
name|suppressAutoSysField
expr_stmt|;
block|}
DECL|method|getDisplayValue ()
specifier|public
name|String
name|getDisplayValue
parameter_list|()
block|{
return|return
name|displayValue
return|;
block|}
comment|/**      * Return the display value (true), actual value (false), or both (all) for      * reference fields (default: false)      */
DECL|method|setDisplayValue (String displayValue)
specifier|public
name|void
name|setDisplayValue
parameter_list|(
name|String
name|displayValue
parameter_list|)
block|{
name|this
operator|.
name|displayValue
operator|=
name|displayValue
expr_stmt|;
block|}
DECL|method|getInputDisplayValue ()
specifier|public
name|Boolean
name|getInputDisplayValue
parameter_list|()
block|{
return|return
name|inputDisplayValue
return|;
block|}
comment|/**      * True to set raw value of input fields (default: false)      */
DECL|method|setInputDisplayValue (Boolean inputDisplayValue)
specifier|public
name|void
name|setInputDisplayValue
parameter_list|(
name|Boolean
name|inputDisplayValue
parameter_list|)
block|{
name|this
operator|.
name|inputDisplayValue
operator|=
name|inputDisplayValue
expr_stmt|;
block|}
DECL|method|getModels ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|getModels
parameter_list|()
block|{
return|return
name|models
return|;
block|}
comment|/**      * Defines the default model to use for a table      */
DECL|method|setModels (Map<String, Class<?>> models)
specifier|public
name|void
name|setModels
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|models
parameter_list|)
block|{
name|this
operator|.
name|models
operator|=
name|models
expr_stmt|;
block|}
DECL|method|addModel (String name, Class<?> type)
specifier|public
name|void
name|addModel
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|models
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|models
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|models
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
DECL|method|getModel (String name)
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getModel
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|getModel
argument_list|(
name|name
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|getModel (String name, Class<?> defaultType)
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getModel
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|defaultType
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|model
init|=
name|defaultType
decl_stmt|;
if|if
condition|(
name|this
operator|.
name|models
operator|!=
literal|null
operator|&&
name|this
operator|.
name|models
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|model
operator|=
name|this
operator|.
name|models
operator|.
name|get
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|model
return|;
block|}
comment|/**      * Sets Jackson's ObjectMapper to use for request/reply      */
DECL|method|setMapper (ObjectMapper mapper)
specifier|public
name|void
name|setMapper
parameter_list|(
name|ObjectMapper
name|mapper
parameter_list|)
block|{
name|this
operator|.
name|mapper
operator|=
name|mapper
expr_stmt|;
block|}
DECL|method|getMapper ()
specifier|public
name|ObjectMapper
name|getMapper
parameter_list|()
block|{
return|return
name|mapper
return|;
block|}
DECL|method|hasMapper ()
specifier|public
name|boolean
name|hasMapper
parameter_list|()
block|{
return|return
name|mapper
operator|!=
literal|null
return|;
block|}
block|}
end_class

end_unit

