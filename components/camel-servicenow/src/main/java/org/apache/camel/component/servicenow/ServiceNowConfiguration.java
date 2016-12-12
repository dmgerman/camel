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
name|RuntimeCamelException
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
name|jsse
operator|.
name|SSLContextParameters
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
name|configuration
operator|.
name|security
operator|.
name|ProxyAuthorizationPolicy
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
name|transports
operator|.
name|http
operator|.
name|configuration
operator|.
name|HTTPClientPolicy
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|ServiceNowConfiguration
specifier|public
class|class
name|ServiceNowConfiguration
implements|implements
name|Cloneable
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
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|userName
specifier|private
name|String
name|userName
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
DECL|field|oauthClientId
specifier|private
name|String
name|oauthClientId
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
argument_list|)
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
literal|"security"
argument_list|,
name|secret
operator|=
literal|true
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
literal|"security"
argument_list|)
DECL|field|apiUrl
specifier|private
name|String
name|apiUrl
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|apiVersion
specifier|private
name|String
name|apiVersion
decl_stmt|;
annotation|@
name|UriParam
DECL|field|resource
specifier|private
name|String
name|resource
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
DECL|field|excludeReferenceLink
specifier|private
name|Boolean
name|excludeReferenceLink
init|=
literal|false
decl_stmt|;
annotation|@
name|UriParam
DECL|field|suppressAutoSysField
specifier|private
name|Boolean
name|suppressAutoSysField
init|=
literal|false
decl_stmt|;
annotation|@
name|UriParam
DECL|field|includeScores
specifier|private
name|Boolean
name|includeScores
init|=
literal|false
decl_stmt|;
annotation|@
name|UriParam
DECL|field|includeAggregates
specifier|private
name|Boolean
name|includeAggregates
init|=
literal|false
decl_stmt|;
annotation|@
name|UriParam
DECL|field|includeAvailableBreakdowns
specifier|private
name|Boolean
name|includeAvailableBreakdowns
init|=
literal|false
decl_stmt|;
annotation|@
name|UriParam
DECL|field|includeAvailableAggregates
specifier|private
name|Boolean
name|includeAvailableAggregates
init|=
literal|false
decl_stmt|;
annotation|@
name|UriParam
DECL|field|includeScoreNotes
specifier|private
name|Boolean
name|includeScoreNotes
init|=
literal|false
decl_stmt|;
annotation|@
name|UriParam
DECL|field|topLevelOnly
specifier|private
name|Boolean
name|topLevelOnly
decl_stmt|;
annotation|@
name|UriParam
DECL|field|favorites
specifier|private
name|Boolean
name|favorites
decl_stmt|;
annotation|@
name|UriParam
DECL|field|key
specifier|private
name|Boolean
name|key
decl_stmt|;
annotation|@
name|UriParam
DECL|field|target
specifier|private
name|Boolean
name|target
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|,
name|enums
operator|=
literal|"false,true,all"
argument_list|)
DECL|field|display
specifier|private
name|String
name|display
init|=
literal|"true"
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"10"
argument_list|)
DECL|field|perPage
specifier|private
name|Integer
name|perPage
init|=
literal|10
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|enums
operator|=
literal|"value,change,changeperc,gap,gapperc,duedate,name,order,default,group,indicator_group,frequency,target,date,trend,bullet,direction"
argument_list|)
DECL|field|sortBy
specifier|private
name|String
name|sortBy
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|enums
operator|=
literal|"asc,desc"
argument_list|)
DECL|field|sortDir
specifier|private
name|String
name|sortDir
decl_stmt|;
annotation|@
name|UriParam
DECL|field|suppressPaginationHeader
specifier|private
name|Boolean
name|suppressPaginationHeader
init|=
literal|false
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"false"
argument_list|,
name|enums
operator|=
literal|"false,true,all"
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
argument_list|,
name|description
operator|=
literal|"Defines both request and response models"
argument_list|)
DECL|field|models
specifier|private
specifier|transient
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
comment|// field not in use as its a shortcut for both requestModels/responseModels
annotation|@
name|UriParam
argument_list|(
name|prefix
operator|=
literal|"request-model."
argument_list|,
name|multiValue
operator|=
literal|true
argument_list|,
name|javaType
operator|=
literal|"java.lang.String"
argument_list|)
DECL|field|requestModels
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
name|requestModels
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|prefix
operator|=
literal|"response-model."
argument_list|,
name|multiValue
operator|=
literal|true
argument_list|,
name|javaType
operator|=
literal|"java.lang.String"
argument_list|)
DECL|field|responseModels
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
name|responseModels
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
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"HELSINKI"
argument_list|,
name|enums
operator|=
literal|"FUJI,GENEVA,HELSINKI"
argument_list|)
DECL|field|release
specifier|private
name|ServiceNowRelease
name|release
init|=
name|ServiceNowRelease
operator|.
name|HELSINKI
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"security"
argument_list|)
DECL|field|sslContextParameters
specifier|private
name|SSLContextParameters
name|sslContextParameters
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|httpClientPolicy
specifier|private
name|HTTPClientPolicy
name|httpClientPolicy
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"advanced"
argument_list|)
DECL|field|proxyAuthorizationPolicy
specifier|private
name|ProxyAuthorizationPolicy
name|proxyAuthorizationPolicy
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
DECL|method|getApiVersion ()
specifier|public
name|String
name|getApiVersion
parameter_list|()
block|{
return|return
name|apiVersion
return|;
block|}
comment|/**      * The ServiceNow REST API version, default latest      */
DECL|method|setApiVersion (String apiVersion)
specifier|public
name|void
name|setApiVersion
parameter_list|(
name|String
name|apiVersion
parameter_list|)
block|{
name|this
operator|.
name|apiVersion
operator|=
name|apiVersion
expr_stmt|;
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
DECL|method|getResource ()
specifier|public
name|String
name|getResource
parameter_list|()
block|{
return|return
name|resource
return|;
block|}
comment|/**      * The default resource, can be overridden by header CamelServiceNowResource      */
DECL|method|setResource (String resource)
specifier|public
name|void
name|setResource
parameter_list|(
name|String
name|resource
parameter_list|)
block|{
name|this
operator|.
name|resource
operator|=
name|resource
expr_stmt|;
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
DECL|method|getSuppressPaginationHeader ()
specifier|public
name|Boolean
name|getSuppressPaginationHeader
parameter_list|()
block|{
return|return
name|suppressPaginationHeader
return|;
block|}
comment|/**      * Set this value to true to remove the Link header from the response. The      * Link header allows you to request additional pages of data when the number      * of records matching your query exceeds the query limit      */
DECL|method|setSuppressPaginationHeader (Boolean suppressPaginationHeader)
specifier|public
name|void
name|setSuppressPaginationHeader
parameter_list|(
name|Boolean
name|suppressPaginationHeader
parameter_list|)
block|{
name|this
operator|.
name|suppressPaginationHeader
operator|=
name|suppressPaginationHeader
expr_stmt|;
block|}
DECL|method|getIncludeScores ()
specifier|public
name|Boolean
name|getIncludeScores
parameter_list|()
block|{
return|return
name|includeScores
return|;
block|}
comment|/**      * Set this parameter to true to return all scores for a scorecard. If a value      * is not specified, this parameter defaults to false and returns only the most      * recent score value.      */
DECL|method|setIncludeScores (Boolean includeScores)
specifier|public
name|void
name|setIncludeScores
parameter_list|(
name|Boolean
name|includeScores
parameter_list|)
block|{
name|this
operator|.
name|includeScores
operator|=
name|includeScores
expr_stmt|;
block|}
DECL|method|getIncludeAggregates ()
specifier|public
name|Boolean
name|getIncludeAggregates
parameter_list|()
block|{
return|return
name|includeAggregates
return|;
block|}
comment|/**      * Set this parameter to true to always return all available aggregates for      * an indicator, including when an aggregate has already been applied. If a      * value is not specified, this parameter defaults to false and returns no      * aggregates.      */
DECL|method|setIncludeAggregates (Boolean includeAggregates)
specifier|public
name|void
name|setIncludeAggregates
parameter_list|(
name|Boolean
name|includeAggregates
parameter_list|)
block|{
name|this
operator|.
name|includeAggregates
operator|=
name|includeAggregates
expr_stmt|;
block|}
DECL|method|getIncludeAvailableBreakdowns ()
specifier|public
name|Boolean
name|getIncludeAvailableBreakdowns
parameter_list|()
block|{
return|return
name|includeAvailableBreakdowns
return|;
block|}
comment|/**      * Set this parameter to true to return all available breakdowns for an indicator.      * If a value is not specified, this parameter defaults to false and returns      * no breakdowns.      */
DECL|method|setIncludeAvailableBreakdowns (Boolean includeAvailableBreakdowns)
specifier|public
name|void
name|setIncludeAvailableBreakdowns
parameter_list|(
name|Boolean
name|includeAvailableBreakdowns
parameter_list|)
block|{
name|this
operator|.
name|includeAvailableBreakdowns
operator|=
name|includeAvailableBreakdowns
expr_stmt|;
block|}
DECL|method|getIncludeAvailableAggregates ()
specifier|public
name|Boolean
name|getIncludeAvailableAggregates
parameter_list|()
block|{
return|return
name|includeAvailableAggregates
return|;
block|}
comment|/**      * Set this parameter to true to return all available aggregates for an indicator      * when no aggregate has been applied. If a value is not specified, this parameter      * defaults to false and returns no aggregates.      */
DECL|method|setIncludeAvailableAggregates (Boolean includeAvailableAggregates)
specifier|public
name|void
name|setIncludeAvailableAggregates
parameter_list|(
name|Boolean
name|includeAvailableAggregates
parameter_list|)
block|{
name|this
operator|.
name|includeAvailableAggregates
operator|=
name|includeAvailableAggregates
expr_stmt|;
block|}
DECL|method|getIncludeScoreNotes ()
specifier|public
name|Boolean
name|getIncludeScoreNotes
parameter_list|()
block|{
return|return
name|includeScoreNotes
return|;
block|}
comment|/**      * Set this parameter to true to return all notes associated with the score.      * The note element contains the note text as well as the author and timestamp      * when the note was added.      */
DECL|method|setIncludeScoreNotes (Boolean includeScoreNotes)
specifier|public
name|void
name|setIncludeScoreNotes
parameter_list|(
name|Boolean
name|includeScoreNotes
parameter_list|)
block|{
name|this
operator|.
name|includeScoreNotes
operator|=
name|includeScoreNotes
expr_stmt|;
block|}
DECL|method|getFavorites ()
specifier|public
name|Boolean
name|getFavorites
parameter_list|()
block|{
return|return
name|favorites
return|;
block|}
comment|/**      * Set this parameter to true to return only scorecards that are favorites of      * the querying user.      */
DECL|method|setFavorites (Boolean favorites)
specifier|public
name|void
name|setFavorites
parameter_list|(
name|Boolean
name|favorites
parameter_list|)
block|{
name|this
operator|.
name|favorites
operator|=
name|favorites
expr_stmt|;
block|}
DECL|method|getKey ()
specifier|public
name|Boolean
name|getKey
parameter_list|()
block|{
return|return
name|key
return|;
block|}
comment|/**      * Set this parameter to true to return only scorecards for key indicators.      */
DECL|method|setKey (Boolean key)
specifier|public
name|void
name|setKey
parameter_list|(
name|Boolean
name|key
parameter_list|)
block|{
name|this
operator|.
name|key
operator|=
name|key
expr_stmt|;
block|}
DECL|method|getTarget ()
specifier|public
name|Boolean
name|getTarget
parameter_list|()
block|{
return|return
name|target
return|;
block|}
comment|/**      * Set this parameter to true to return only scorecards that have a target.      */
DECL|method|setTarget (Boolean target)
specifier|public
name|void
name|setTarget
parameter_list|(
name|Boolean
name|target
parameter_list|)
block|{
name|this
operator|.
name|target
operator|=
name|target
expr_stmt|;
block|}
DECL|method|getDisplay ()
specifier|public
name|String
name|getDisplay
parameter_list|()
block|{
return|return
name|display
return|;
block|}
comment|/**      * Set this parameter to true to return only scorecards where the indicator      * Display field is selected. Set this parameter to all to return scorecards      * with any Display field value. This parameter is true by default.      */
DECL|method|setDisplay (String display)
specifier|public
name|void
name|setDisplay
parameter_list|(
name|String
name|display
parameter_list|)
block|{
name|this
operator|.
name|display
operator|=
name|display
expr_stmt|;
block|}
DECL|method|getPerPage ()
specifier|public
name|Integer
name|getPerPage
parameter_list|()
block|{
return|return
name|perPage
return|;
block|}
comment|/**      * Enter the maximum number of scorecards each query can return. By default      * this value is 10, and the maximum is 100.      */
DECL|method|setPerPage (Integer perPage)
specifier|public
name|void
name|setPerPage
parameter_list|(
name|Integer
name|perPage
parameter_list|)
block|{
name|this
operator|.
name|perPage
operator|=
name|perPage
expr_stmt|;
block|}
DECL|method|getSortBy ()
specifier|public
name|String
name|getSortBy
parameter_list|()
block|{
return|return
name|sortBy
return|;
block|}
comment|/**      * Specify the value to use when sorting results. By default, queries sort      * records by value.      */
DECL|method|setSortBy (String sortBy)
specifier|public
name|void
name|setSortBy
parameter_list|(
name|String
name|sortBy
parameter_list|)
block|{
name|this
operator|.
name|sortBy
operator|=
name|sortBy
expr_stmt|;
block|}
DECL|method|getSortDir ()
specifier|public
name|String
name|getSortDir
parameter_list|()
block|{
return|return
name|sortDir
return|;
block|}
comment|/**      * Specify the sort direction, ascending or descending. By default, queries      * sort records in descending order. Use sysparm_sortdir=asc to sort in      * ascending order.      */
DECL|method|setSortDir (String sortDir)
specifier|public
name|void
name|setSortDir
parameter_list|(
name|String
name|sortDir
parameter_list|)
block|{
name|this
operator|.
name|sortDir
operator|=
name|sortDir
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
DECL|method|getRequestModels ()
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
name|getRequestModels
parameter_list|()
block|{
return|return
name|requestModels
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
comment|/**      * The ServiceNow release to target, default to Helsinki      *      * See https://docs.servicenow.com      */
DECL|method|setRelease (ServiceNowRelease release)
specifier|public
name|void
name|setRelease
parameter_list|(
name|ServiceNowRelease
name|release
parameter_list|)
block|{
name|this
operator|.
name|release
operator|=
name|release
expr_stmt|;
block|}
DECL|method|getRelease ()
specifier|public
name|ServiceNowRelease
name|getRelease
parameter_list|()
block|{
return|return
name|release
return|;
block|}
DECL|method|getTopLevelOnly ()
specifier|public
name|Boolean
name|getTopLevelOnly
parameter_list|()
block|{
return|return
name|topLevelOnly
return|;
block|}
comment|/**      * Gets only those categories whose parent is a catalog.      */
DECL|method|setTopLevelOnly (Boolean topLevelOnly)
specifier|public
name|void
name|setTopLevelOnly
parameter_list|(
name|Boolean
name|topLevelOnly
parameter_list|)
block|{
name|this
operator|.
name|topLevelOnly
operator|=
name|topLevelOnly
expr_stmt|;
block|}
DECL|method|getSslContextParameters ()
specifier|public
name|SSLContextParameters
name|getSslContextParameters
parameter_list|()
block|{
return|return
name|sslContextParameters
return|;
block|}
comment|/**      * To configure security using SSLContextParameters. See http://camel.apache.org/camel-configuration-utilities.html      */
DECL|method|setSslContextParameters (SSLContextParameters sslContextParameters)
specifier|public
name|void
name|setSslContextParameters
parameter_list|(
name|SSLContextParameters
name|sslContextParameters
parameter_list|)
block|{
name|this
operator|.
name|sslContextParameters
operator|=
name|sslContextParameters
expr_stmt|;
block|}
DECL|method|getHttpClientPolicy ()
specifier|public
name|HTTPClientPolicy
name|getHttpClientPolicy
parameter_list|()
block|{
return|return
name|httpClientPolicy
return|;
block|}
comment|/**      * To configure http-client      */
DECL|method|setHttpClientPolicy (HTTPClientPolicy httpClientPolicy)
specifier|public
name|void
name|setHttpClientPolicy
parameter_list|(
name|HTTPClientPolicy
name|httpClientPolicy
parameter_list|)
block|{
name|this
operator|.
name|httpClientPolicy
operator|=
name|httpClientPolicy
expr_stmt|;
block|}
DECL|method|getProxyAuthorizationPolicy ()
specifier|public
name|ProxyAuthorizationPolicy
name|getProxyAuthorizationPolicy
parameter_list|()
block|{
return|return
name|proxyAuthorizationPolicy
return|;
block|}
comment|/**      * To configure proxy authentication      */
DECL|method|setProxyAuthorizationPolicy (ProxyAuthorizationPolicy proxyAuthorizationPolicy)
specifier|public
name|void
name|setProxyAuthorizationPolicy
parameter_list|(
name|ProxyAuthorizationPolicy
name|proxyAuthorizationPolicy
parameter_list|)
block|{
name|this
operator|.
name|proxyAuthorizationPolicy
operator|=
name|proxyAuthorizationPolicy
expr_stmt|;
block|}
comment|// *************************************************
comment|//
comment|// *************************************************
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
name|setRequestModels
argument_list|(
name|models
argument_list|)
expr_stmt|;
name|setResponseModels
argument_list|(
name|models
argument_list|)
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
name|addRequestModel
argument_list|(
name|name
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|addResponseModel
argument_list|(
name|name
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|// *************************************************
comment|// Request model
comment|// *************************************************
comment|/**      * Defines the request model      */
DECL|method|setRequestModels (Map<String, Class<?>> models)
specifier|public
name|void
name|setRequestModels
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
if|if
condition|(
name|this
operator|.
name|requestModels
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|requestModels
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|requestModels
operator|.
name|clear
argument_list|()
expr_stmt|;
name|this
operator|.
name|requestModels
operator|.
name|putAll
argument_list|(
name|models
argument_list|)
expr_stmt|;
block|}
DECL|method|addRequestModel (String name, Class<?> type)
specifier|public
name|void
name|addRequestModel
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
name|requestModels
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|requestModels
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|requestModels
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
DECL|method|getRequestModel (String name)
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getRequestModel
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|getRequestModel
argument_list|(
name|name
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|getRequestModel (String name, Class<?> defaultType)
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getRequestModel
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
name|requestModels
operator|!=
literal|null
operator|&&
name|this
operator|.
name|requestModels
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
name|requestModels
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
comment|// *************************************************
comment|// Response model
comment|// *************************************************
comment|/**      * Defines the response model      */
DECL|method|setResponseModels (Map<String, Class<?>> models)
specifier|public
name|void
name|setResponseModels
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
if|if
condition|(
name|this
operator|.
name|responseModels
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|responseModels
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|responseModels
operator|.
name|putAll
argument_list|(
name|models
argument_list|)
expr_stmt|;
block|}
DECL|method|addResponseModel (String name, Class<?> type)
specifier|public
name|void
name|addResponseModel
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
name|responseModels
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|responseModels
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|responseModels
operator|.
name|clear
argument_list|()
expr_stmt|;
name|this
operator|.
name|responseModels
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
DECL|method|getResponseModel (String name)
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getResponseModel
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|getResponseModel
argument_list|(
name|name
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|getResponseModel (String name, Class<?> defaultType)
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getResponseModel
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
name|responseModels
operator|!=
literal|null
operator|&&
name|this
operator|.
name|responseModels
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
name|responseModels
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
comment|// *************************************************
comment|//
comment|// *************************************************
DECL|method|copy ()
specifier|public
name|ServiceNowConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|ServiceNowConfiguration
operator|)
name|super
operator|.
name|clone
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

