begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.twitter.springboot
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
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|spring
operator|.
name|boot
operator|.
name|ComponentConfigurationPropertiesCommon
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * Use twitter-directmessage, twitter-search, twitter-streaming and  * twitter-timeline instead of this component.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.component.twitter"
argument_list|)
DECL|class|TwitterComponentConfiguration
specifier|public
class|class
name|TwitterComponentConfiguration
extends|extends
name|ComponentConfigurationPropertiesCommon
block|{
comment|/**      * The access token      */
DECL|field|accessToken
specifier|private
name|String
name|accessToken
decl_stmt|;
comment|/**      * The access token secret      */
DECL|field|accessTokenSecret
specifier|private
name|String
name|accessTokenSecret
decl_stmt|;
comment|/**      * The consumer key      */
DECL|field|consumerKey
specifier|private
name|String
name|consumerKey
decl_stmt|;
comment|/**      * The consumer secret      */
DECL|field|consumerSecret
specifier|private
name|String
name|consumerSecret
decl_stmt|;
comment|/**      * The http proxy host which can be used for the camel-twitter.      */
DECL|field|httpProxyHost
specifier|private
name|String
name|httpProxyHost
decl_stmt|;
comment|/**      * The http proxy user which can be used for the camel-twitter.      */
DECL|field|httpProxyUser
specifier|private
name|String
name|httpProxyUser
decl_stmt|;
comment|/**      * The http proxy password which can be used for the camel-twitter.      */
DECL|field|httpProxyPassword
specifier|private
name|String
name|httpProxyPassword
decl_stmt|;
comment|/**      * The http proxy port which can be used for the camel-twitter.      */
DECL|field|httpProxyPort
specifier|private
name|Integer
name|httpProxyPort
decl_stmt|;
comment|/**      * Whether the component should resolve property placeholders on itself when      * starting. Only properties which are of String type can use property      * placeholders.      */
DECL|field|resolvePropertyPlaceholders
specifier|private
name|Boolean
name|resolvePropertyPlaceholders
init|=
literal|true
decl_stmt|;
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
DECL|method|getHttpProxyPort ()
specifier|public
name|Integer
name|getHttpProxyPort
parameter_list|()
block|{
return|return
name|httpProxyPort
return|;
block|}
DECL|method|setHttpProxyPort (Integer httpProxyPort)
specifier|public
name|void
name|setHttpProxyPort
parameter_list|(
name|Integer
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
DECL|method|getResolvePropertyPlaceholders ()
specifier|public
name|Boolean
name|getResolvePropertyPlaceholders
parameter_list|()
block|{
return|return
name|resolvePropertyPlaceholders
return|;
block|}
DECL|method|setResolvePropertyPlaceholders ( Boolean resolvePropertyPlaceholders)
specifier|public
name|void
name|setResolvePropertyPlaceholders
parameter_list|(
name|Boolean
name|resolvePropertyPlaceholders
parameter_list|)
block|{
name|this
operator|.
name|resolvePropertyPlaceholders
operator|=
name|resolvePropertyPlaceholders
expr_stmt|;
block|}
block|}
end_class

end_unit

