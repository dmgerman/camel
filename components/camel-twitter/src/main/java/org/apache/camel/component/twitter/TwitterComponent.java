begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.twitter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|twitter
package|;
end_package

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
name|Endpoint
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
name|impl
operator|.
name|UriEndpointComponent
import|;
end_import

begin_comment
comment|/**  * Twitter component  */
end_comment

begin_class
DECL|class|TwitterComponent
specifier|public
class|class
name|TwitterComponent
extends|extends
name|UriEndpointComponent
block|{
DECL|field|consumerKey
specifier|private
name|String
name|consumerKey
decl_stmt|;
DECL|field|consumerSecret
specifier|private
name|String
name|consumerSecret
decl_stmt|;
DECL|field|accessToken
specifier|private
name|String
name|accessToken
decl_stmt|;
DECL|field|accessTokenSecret
specifier|private
name|String
name|accessTokenSecret
decl_stmt|;
DECL|field|httpProxyHost
specifier|private
name|String
name|httpProxyHost
decl_stmt|;
DECL|field|httpProxyUser
specifier|private
name|String
name|httpProxyUser
decl_stmt|;
DECL|field|httpProxyPassword
specifier|private
name|String
name|httpProxyPassword
decl_stmt|;
DECL|field|httpProxyPort
specifier|private
name|Integer
name|httpProxyPort
decl_stmt|;
DECL|method|TwitterComponent ()
specifier|public
name|TwitterComponent
parameter_list|()
block|{
name|super
argument_list|(
name|TwitterEndpointEvent
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|TwitterConfiguration
name|properties
init|=
operator|new
name|TwitterConfiguration
argument_list|()
decl_stmt|;
comment|// set options from component
name|properties
operator|.
name|setConsumerKey
argument_list|(
name|consumerKey
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setConsumerSecret
argument_list|(
name|consumerSecret
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setAccessToken
argument_list|(
name|accessToken
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setAccessTokenSecret
argument_list|(
name|accessTokenSecret
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setHttpProxyHost
argument_list|(
name|httpProxyHost
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setHttpProxyUser
argument_list|(
name|httpProxyUser
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setHttpProxyPassword
argument_list|(
name|httpProxyPassword
argument_list|)
expr_stmt|;
if|if
condition|(
name|httpProxyPort
operator|!=
literal|null
condition|)
block|{
name|properties
operator|.
name|setHttpProxyPort
argument_list|(
name|httpProxyPort
argument_list|)
expr_stmt|;
block|}
comment|// and then override from parameters
name|setProperties
argument_list|(
name|properties
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
name|TwitterEndpoint
name|endpoint
decl_stmt|;
switch|switch
condition|(
name|properties
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
name|POLLING
case|:
name|endpoint
operator|=
operator|new
name|TwitterEndpointPolling
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|properties
argument_list|)
expr_stmt|;
break|break;
case|case
name|EVENT
case|:
name|endpoint
operator|=
operator|new
name|TwitterEndpointEvent
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|properties
argument_list|)
expr_stmt|;
break|break;
default|default:
name|endpoint
operator|=
operator|new
name|TwitterEndpointDirect
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|properties
argument_list|)
expr_stmt|;
break|break;
block|}
return|return
name|endpoint
return|;
block|}
DECL|method|getAccessToken ()
specifier|public
name|String
name|getAccessToken
parameter_list|()
block|{
return|return
name|accessToken
return|;
block|}
comment|/**      * The access token      */
DECL|method|setAccessToken (String accessToken)
specifier|public
name|void
name|setAccessToken
parameter_list|(
name|String
name|accessToken
parameter_list|)
block|{
name|this
operator|.
name|accessToken
operator|=
name|accessToken
expr_stmt|;
block|}
DECL|method|getAccessTokenSecret ()
specifier|public
name|String
name|getAccessTokenSecret
parameter_list|()
block|{
return|return
name|accessTokenSecret
return|;
block|}
comment|/**      * The access token secret      */
DECL|method|setAccessTokenSecret (String accessTokenSecret)
specifier|public
name|void
name|setAccessTokenSecret
parameter_list|(
name|String
name|accessTokenSecret
parameter_list|)
block|{
name|this
operator|.
name|accessTokenSecret
operator|=
name|accessTokenSecret
expr_stmt|;
block|}
DECL|method|getConsumerKey ()
specifier|public
name|String
name|getConsumerKey
parameter_list|()
block|{
return|return
name|consumerKey
return|;
block|}
comment|/**      * The consumer key      */
DECL|method|setConsumerKey (String consumerKey)
specifier|public
name|void
name|setConsumerKey
parameter_list|(
name|String
name|consumerKey
parameter_list|)
block|{
name|this
operator|.
name|consumerKey
operator|=
name|consumerKey
expr_stmt|;
block|}
DECL|method|getConsumerSecret ()
specifier|public
name|String
name|getConsumerSecret
parameter_list|()
block|{
return|return
name|consumerSecret
return|;
block|}
comment|/**      * The consumer secret      */
DECL|method|setConsumerSecret (String consumerSecret)
specifier|public
name|void
name|setConsumerSecret
parameter_list|(
name|String
name|consumerSecret
parameter_list|)
block|{
name|this
operator|.
name|consumerSecret
operator|=
name|consumerSecret
expr_stmt|;
block|}
comment|/**      * The http proxy host which can be used for the camel-twitter.      */
DECL|method|setHttpProxyHost (String httpProxyHost)
specifier|public
name|void
name|setHttpProxyHost
parameter_list|(
name|String
name|httpProxyHost
parameter_list|)
block|{
name|this
operator|.
name|httpProxyHost
operator|=
name|httpProxyHost
expr_stmt|;
block|}
DECL|method|getHttpProxyHost ()
specifier|public
name|String
name|getHttpProxyHost
parameter_list|()
block|{
return|return
name|httpProxyHost
return|;
block|}
comment|/**      * The http proxy user which can be used for the camel-twitter.      */
DECL|method|setHttpProxyUser (String httpProxyUser)
specifier|public
name|void
name|setHttpProxyUser
parameter_list|(
name|String
name|httpProxyUser
parameter_list|)
block|{
name|this
operator|.
name|httpProxyUser
operator|=
name|httpProxyUser
expr_stmt|;
block|}
DECL|method|getHttpProxyUser ()
specifier|public
name|String
name|getHttpProxyUser
parameter_list|()
block|{
return|return
name|httpProxyUser
return|;
block|}
comment|/**      * The http proxy password which can be used for the camel-twitter.      */
DECL|method|setHttpProxyPassword (String httpProxyPassword)
specifier|public
name|void
name|setHttpProxyPassword
parameter_list|(
name|String
name|httpProxyPassword
parameter_list|)
block|{
name|this
operator|.
name|httpProxyPassword
operator|=
name|httpProxyPassword
expr_stmt|;
block|}
DECL|method|getHttpProxyPassword ()
specifier|public
name|String
name|getHttpProxyPassword
parameter_list|()
block|{
return|return
name|httpProxyPassword
return|;
block|}
comment|/**      * The http proxy port which can be used for the camel-twitter.      */
DECL|method|setHttpProxyPort (int httpProxyPort)
specifier|public
name|void
name|setHttpProxyPort
parameter_list|(
name|int
name|httpProxyPort
parameter_list|)
block|{
name|this
operator|.
name|httpProxyPort
operator|=
name|httpProxyPort
expr_stmt|;
block|}
DECL|method|getHttpProxyPort ()
specifier|public
name|int
name|getHttpProxyPort
parameter_list|()
block|{
return|return
name|httpProxyPort
return|;
block|}
block|}
end_class

end_unit

