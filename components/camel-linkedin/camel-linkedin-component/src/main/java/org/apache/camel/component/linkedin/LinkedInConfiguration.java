begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.linkedin
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|linkedin
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|linkedin
operator|.
name|api
operator|.
name|OAuthScope
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
name|linkedin
operator|.
name|api
operator|.
name|OAuthSecureStorage
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
name|linkedin
operator|.
name|internal
operator|.
name|LinkedInApiName
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
name|Metadata
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
name|spi
operator|.
name|UriPath
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
name|commons
operator|.
name|lang3
operator|.
name|builder
operator|.
name|HashCodeBuilder
import|;
end_import

begin_comment
comment|/**  * Component configuration for LinkedIn component.  */
end_comment

begin_class
annotation|@
name|UriParams
DECL|class|LinkedInConfiguration
specifier|public
class|class
name|LinkedInConfiguration
block|{
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|apiName
specifier|private
name|LinkedInApiName
name|apiName
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|enums
operator|=
literal|"addActivity,addComment,addCompanyUpdateComment,addCompanyUpdateCommentAsCompany,addGroupMembership,addInvite"
operator|+
literal|",addJob,addJobBookmark,addPost,addShare,addUpdateComment,editJob,flagCategory,followCompany,followPost,getComment"
operator|+
literal|",getCompanies,getCompanyById,getCompanyByName,getCompanyUpdateComments,getCompanyUpdateLikes,getCompanyUpdates"
operator|+
literal|",getConnections,getConnectionsById,getConnectionsByUrl,getFollowedCompanies,getGroup,getGroupMemberships,getGroupMembershipSettings"
operator|+
literal|",getHistoricalFollowStatistics,getHistoricalStatusUpdateStatistics,getJob,getJobBookmarks,getNetworkStats,getNetworkUpdates"
operator|+
literal|",getNetworkUpdatesById,getNumberOfFollowers,getPerson,getPersonById,getPersonByUrl,getPost,getPostComments,getPosts"
operator|+
literal|",getStatistics,getSuggestedCompanies,getSuggestedGroupPosts,getSuggestedGroups,getSuggestedJobs,getUpdateComments"
operator|+
literal|",getUpdateLikes,isShareEnabled,isViewerShareEnabled,likeCompanyUpdate,likePost,likeUpdate,removeComment,removeGroupMembership"
operator|+
literal|",removeGroupSuggestion,removeJob,removeJobBookmark,removePost,searchCompanies,searchJobs,searchPeople,share,stopFollowingCompany,updateGroupMembership"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|methodName
specifier|private
name|String
name|methodName
decl_stmt|;
annotation|@
name|UriParam
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|userName
specifier|private
name|String
name|userName
decl_stmt|;
annotation|@
name|UriParam
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|userPassword
specifier|private
name|String
name|userPassword
decl_stmt|;
annotation|@
name|UriParam
DECL|field|secureStorage
specifier|private
name|OAuthSecureStorage
name|secureStorage
decl_stmt|;
annotation|@
name|UriParam
DECL|field|clientId
specifier|private
name|String
name|clientId
decl_stmt|;
annotation|@
name|UriParam
DECL|field|clientSecret
specifier|private
name|String
name|clientSecret
decl_stmt|;
annotation|@
name|UriParam
DECL|field|scopes
specifier|private
name|OAuthScope
index|[]
name|scopes
decl_stmt|;
annotation|@
name|UriParam
DECL|field|redirectUri
specifier|private
name|String
name|redirectUri
decl_stmt|;
annotation|@
name|UriParam
DECL|field|httpParams
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|httpParams
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|lazyAuth
specifier|private
name|boolean
name|lazyAuth
init|=
literal|true
decl_stmt|;
DECL|method|getApiName ()
specifier|public
name|LinkedInApiName
name|getApiName
parameter_list|()
block|{
return|return
name|apiName
return|;
block|}
comment|/**      * What kind of operation to perform      */
DECL|method|setApiName (LinkedInApiName apiName)
specifier|public
name|void
name|setApiName
parameter_list|(
name|LinkedInApiName
name|apiName
parameter_list|)
block|{
name|this
operator|.
name|apiName
operator|=
name|apiName
expr_stmt|;
block|}
DECL|method|getMethodName ()
specifier|public
name|String
name|getMethodName
parameter_list|()
block|{
return|return
name|methodName
return|;
block|}
comment|/**      * What sub operation to use for the selected operation      */
DECL|method|setMethodName (String methodName)
specifier|public
name|void
name|setMethodName
parameter_list|(
name|String
name|methodName
parameter_list|)
block|{
name|this
operator|.
name|methodName
operator|=
name|methodName
expr_stmt|;
block|}
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
comment|/**      * LinkedIn user account name, MUST be provided      */
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
DECL|method|getUserPassword ()
specifier|public
name|String
name|getUserPassword
parameter_list|()
block|{
return|return
name|userPassword
return|;
block|}
comment|/**      * LinkedIn account password      */
DECL|method|setUserPassword (String userPassword)
specifier|public
name|void
name|setUserPassword
parameter_list|(
name|String
name|userPassword
parameter_list|)
block|{
name|this
operator|.
name|userPassword
operator|=
name|userPassword
expr_stmt|;
block|}
DECL|method|getSecureStorage ()
specifier|public
name|OAuthSecureStorage
name|getSecureStorage
parameter_list|()
block|{
return|return
name|secureStorage
return|;
block|}
comment|/**      * Callback interface for providing an OAuth token or to store the token generated by the component.      * The callback should return null on the first call and then save the created token in the saveToken() callback.      * If the callback returns null the first time, a userPassword MUST be provided      */
DECL|method|setSecureStorage (OAuthSecureStorage secureStorage)
specifier|public
name|void
name|setSecureStorage
parameter_list|(
name|OAuthSecureStorage
name|secureStorage
parameter_list|)
block|{
name|this
operator|.
name|secureStorage
operator|=
name|secureStorage
expr_stmt|;
block|}
DECL|method|getClientId ()
specifier|public
name|String
name|getClientId
parameter_list|()
block|{
return|return
name|clientId
return|;
block|}
comment|/**      * LinkedIn application client ID      */
DECL|method|setClientId (String clientId)
specifier|public
name|void
name|setClientId
parameter_list|(
name|String
name|clientId
parameter_list|)
block|{
name|this
operator|.
name|clientId
operator|=
name|clientId
expr_stmt|;
block|}
DECL|method|getClientSecret ()
specifier|public
name|String
name|getClientSecret
parameter_list|()
block|{
return|return
name|clientSecret
return|;
block|}
comment|/**      * LinkedIn application client secret      */
DECL|method|setClientSecret (String clientSecret)
specifier|public
name|void
name|setClientSecret
parameter_list|(
name|String
name|clientSecret
parameter_list|)
block|{
name|this
operator|.
name|clientSecret
operator|=
name|clientSecret
expr_stmt|;
block|}
DECL|method|getScopes ()
specifier|public
name|OAuthScope
index|[]
name|getScopes
parameter_list|()
block|{
return|return
name|scopes
return|;
block|}
comment|/**      * List of LinkedIn scopes as specified at https://developer.linkedin.com/documents/authentication#granting      */
DECL|method|setScopes (OAuthScope[] scopes)
specifier|public
name|void
name|setScopes
parameter_list|(
name|OAuthScope
index|[]
name|scopes
parameter_list|)
block|{
name|this
operator|.
name|scopes
operator|=
name|scopes
expr_stmt|;
block|}
DECL|method|getRedirectUri ()
specifier|public
name|String
name|getRedirectUri
parameter_list|()
block|{
return|return
name|redirectUri
return|;
block|}
comment|/**      * Application redirect URI, although the component never redirects to this page to avoid having to have a functioning redirect server.      * So for testing one could use https://localhost      */
DECL|method|setRedirectUri (String redirectUri)
specifier|public
name|void
name|setRedirectUri
parameter_list|(
name|String
name|redirectUri
parameter_list|)
block|{
name|this
operator|.
name|redirectUri
operator|=
name|redirectUri
expr_stmt|;
block|}
DECL|method|getHttpParams ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getHttpParams
parameter_list|()
block|{
return|return
name|httpParams
return|;
block|}
comment|/**      * Custom HTTP params, for example proxy host and port, use constants from AllClientPNames      */
DECL|method|setHttpParams (Map<String, Object> httpParams)
specifier|public
name|void
name|setHttpParams
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|httpParams
parameter_list|)
block|{
name|this
operator|.
name|httpParams
operator|=
name|httpParams
expr_stmt|;
block|}
DECL|method|isLazyAuth ()
specifier|public
name|boolean
name|isLazyAuth
parameter_list|()
block|{
return|return
name|lazyAuth
return|;
block|}
comment|/**      * Flag to enable/disable lazy OAuth, default is true. when enabled, OAuth token retrieval or generation is not done until the first REST call      */
DECL|method|setLazyAuth (boolean lazyAuth)
specifier|public
name|void
name|setLazyAuth
parameter_list|(
name|boolean
name|lazyAuth
parameter_list|)
block|{
name|this
operator|.
name|lazyAuth
operator|=
name|lazyAuth
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|equals (Object obj)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
if|if
condition|(
name|obj
operator|instanceof
name|LinkedInConfiguration
condition|)
block|{
specifier|final
name|LinkedInConfiguration
name|other
init|=
operator|(
name|LinkedInConfiguration
operator|)
name|obj
decl_stmt|;
return|return
operator|(
name|userName
operator|==
literal|null
condition|?
name|other
operator|.
name|userName
operator|==
literal|null
else|:
name|userName
operator|.
name|equals
argument_list|(
name|other
operator|.
name|userName
argument_list|)
operator|)
operator|&&
operator|(
name|userPassword
operator|==
literal|null
condition|?
name|other
operator|.
name|userPassword
operator|==
literal|null
else|:
name|userPassword
operator|.
name|equals
argument_list|(
name|other
operator|.
name|userPassword
argument_list|)
operator|)
operator|&&
name|secureStorage
operator|==
name|other
operator|.
name|secureStorage
operator|&&
operator|(
name|clientId
operator|==
literal|null
condition|?
name|other
operator|.
name|clientId
operator|==
literal|null
else|:
name|clientId
operator|.
name|equals
argument_list|(
name|other
operator|.
name|clientId
argument_list|)
operator|)
operator|&&
operator|(
name|clientSecret
operator|==
literal|null
condition|?
name|other
operator|.
name|clientSecret
operator|==
literal|null
else|:
name|clientSecret
operator|.
name|equals
argument_list|(
name|other
operator|.
name|clientSecret
argument_list|)
operator|)
operator|&&
operator|(
name|redirectUri
operator|==
literal|null
condition|?
name|other
operator|.
name|redirectUri
operator|==
literal|null
else|:
name|redirectUri
operator|.
name|equals
argument_list|(
name|other
operator|.
name|redirectUri
argument_list|)
operator|)
operator|&&
name|Arrays
operator|.
name|equals
argument_list|(
name|scopes
argument_list|,
name|other
operator|.
name|scopes
argument_list|)
operator|&&
operator|(
name|httpParams
operator|==
literal|null
condition|?
name|other
operator|.
name|httpParams
operator|==
literal|null
else|:
name|httpParams
operator|.
name|equals
argument_list|(
name|other
operator|.
name|httpParams
argument_list|)
operator|)
operator|&&
operator|(
name|lazyAuth
operator|==
name|other
operator|.
name|lazyAuth
operator|)
return|;
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
operator|new
name|HashCodeBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|userName
argument_list|)
operator|.
name|append
argument_list|(
name|userPassword
argument_list|)
operator|.
name|append
argument_list|(
name|secureStorage
argument_list|)
operator|.
name|append
argument_list|(
name|clientId
argument_list|)
operator|.
name|append
argument_list|(
name|clientSecret
argument_list|)
operator|.
name|append
argument_list|(
name|redirectUri
argument_list|)
operator|.
name|append
argument_list|(
name|scopes
argument_list|)
operator|.
name|append
argument_list|(
name|httpParams
argument_list|)
operator|.
name|append
argument_list|(
name|lazyAuth
argument_list|)
operator|.
name|toHashCode
argument_list|()
return|;
block|}
DECL|method|validate ()
specifier|public
name|void
name|validate
parameter_list|()
throws|throws
name|IllegalArgumentException
block|{
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|userName
argument_list|,
literal|"userName"
argument_list|)
expr_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|userPassword
argument_list|)
operator|&&
name|secureStorage
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Property userPassword or secureStorage is required"
argument_list|)
throw|;
block|}
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|clientId
argument_list|,
literal|"clientId"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|clientSecret
argument_list|,
literal|"clientSecret"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notEmpty
argument_list|(
name|redirectUri
argument_list|,
literal|"redirectUri"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

