begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.twitter.mocks
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
name|mocks
package|;
end_package

begin_import
import|import
name|twitter4j
operator|.
name|ConnectionLifeCycleListener
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|FilterQuery
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|RateLimitStatusListener
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|SiteStreamsListener
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|StatusListener
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|StatusStream
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|TwitterException
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|TwitterStream
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|UserStream
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|UserStreamListener
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|auth
operator|.
name|AccessToken
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|auth
operator|.
name|Authorization
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|auth
operator|.
name|RequestToken
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|conf
operator|.
name|Configuration
import|;
end_import

begin_class
DECL|class|TwitterStreamMock
specifier|public
class|class
name|TwitterStreamMock
implements|implements
name|TwitterStream
block|{
DECL|field|statusListener
specifier|private
name|StatusListener
name|statusListener
decl_stmt|;
DECL|method|updateStatus (String text)
specifier|public
name|void
name|updateStatus
parameter_list|(
name|String
name|text
parameter_list|)
block|{
name|statusListener
operator|.
name|onStatus
argument_list|(
operator|new
name|StatusMock
argument_list|(
name|text
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|addConnectionLifeCycleListener (ConnectionLifeCycleListener listener)
specifier|public
name|void
name|addConnectionLifeCycleListener
parameter_list|(
name|ConnectionLifeCycleListener
name|listener
parameter_list|)
block|{      }
annotation|@
name|Override
DECL|method|addListener (UserStreamListener listener)
specifier|public
name|void
name|addListener
parameter_list|(
name|UserStreamListener
name|listener
parameter_list|)
block|{      }
annotation|@
name|Override
DECL|method|addListener (StatusListener listener)
specifier|public
name|void
name|addListener
parameter_list|(
name|StatusListener
name|listener
parameter_list|)
block|{
name|this
operator|.
name|statusListener
operator|=
name|listener
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|addListener (SiteStreamsListener listener)
specifier|public
name|void
name|addListener
parameter_list|(
name|SiteStreamsListener
name|listener
parameter_list|)
block|{      }
annotation|@
name|Override
DECL|method|firehose (int count)
specifier|public
name|void
name|firehose
parameter_list|(
name|int
name|count
parameter_list|)
block|{      }
annotation|@
name|Override
DECL|method|getFirehoseStream (int count)
specifier|public
name|StatusStream
name|getFirehoseStream
parameter_list|(
name|int
name|count
parameter_list|)
throws|throws
name|TwitterException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|links (int count)
specifier|public
name|void
name|links
parameter_list|(
name|int
name|count
parameter_list|)
block|{      }
annotation|@
name|Override
DECL|method|getLinksStream (int count)
specifier|public
name|StatusStream
name|getLinksStream
parameter_list|(
name|int
name|count
parameter_list|)
throws|throws
name|TwitterException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|retweet ()
specifier|public
name|void
name|retweet
parameter_list|()
block|{      }
annotation|@
name|Override
DECL|method|getRetweetStream ()
specifier|public
name|StatusStream
name|getRetweetStream
parameter_list|()
throws|throws
name|TwitterException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|sample ()
specifier|public
name|void
name|sample
parameter_list|()
block|{      }
annotation|@
name|Override
DECL|method|getSampleStream ()
specifier|public
name|StatusStream
name|getSampleStream
parameter_list|()
throws|throws
name|TwitterException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|user ()
specifier|public
name|void
name|user
parameter_list|()
block|{      }
annotation|@
name|Override
DECL|method|user (String[] track)
specifier|public
name|void
name|user
parameter_list|(
name|String
index|[]
name|track
parameter_list|)
block|{      }
annotation|@
name|Override
DECL|method|getUserStream ()
specifier|public
name|UserStream
name|getUserStream
parameter_list|()
throws|throws
name|TwitterException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getUserStream (String[] track)
specifier|public
name|UserStream
name|getUserStream
parameter_list|(
name|String
index|[]
name|track
parameter_list|)
throws|throws
name|TwitterException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|site (boolean withFollowings, long[] follow)
specifier|public
name|void
name|site
parameter_list|(
name|boolean
name|withFollowings
parameter_list|,
name|long
index|[]
name|follow
parameter_list|)
block|{      }
annotation|@
name|Override
DECL|method|filter (FilterQuery query)
specifier|public
name|void
name|filter
parameter_list|(
name|FilterQuery
name|query
parameter_list|)
block|{      }
annotation|@
name|Override
DECL|method|getFilterStream (FilterQuery query)
specifier|public
name|StatusStream
name|getFilterStream
parameter_list|(
name|FilterQuery
name|query
parameter_list|)
throws|throws
name|TwitterException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|cleanUp ()
specifier|public
name|void
name|cleanUp
parameter_list|()
block|{      }
annotation|@
name|Override
DECL|method|getScreenName ()
specifier|public
name|String
name|getScreenName
parameter_list|()
throws|throws
name|TwitterException
throws|,
name|IllegalStateException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getId ()
specifier|public
name|long
name|getId
parameter_list|()
throws|throws
name|TwitterException
throws|,
name|IllegalStateException
block|{
return|return
literal|0
return|;
block|}
annotation|@
name|Override
DECL|method|addRateLimitStatusListener (RateLimitStatusListener listener)
specifier|public
name|void
name|addRateLimitStatusListener
parameter_list|(
name|RateLimitStatusListener
name|listener
parameter_list|)
block|{      }
annotation|@
name|Override
DECL|method|getAuthorization ()
specifier|public
name|Authorization
name|getAuthorization
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getConfiguration ()
specifier|public
name|Configuration
name|getConfiguration
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|shutdown ()
specifier|public
name|void
name|shutdown
parameter_list|()
block|{      }
annotation|@
name|Override
DECL|method|setOAuthConsumer (String consumerKey, String consumerSecret)
specifier|public
name|void
name|setOAuthConsumer
parameter_list|(
name|String
name|consumerKey
parameter_list|,
name|String
name|consumerSecret
parameter_list|)
block|{      }
annotation|@
name|Override
DECL|method|getOAuthRequestToken ()
specifier|public
name|RequestToken
name|getOAuthRequestToken
parameter_list|()
throws|throws
name|TwitterException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getOAuthRequestToken (String callbackURL)
specifier|public
name|RequestToken
name|getOAuthRequestToken
parameter_list|(
name|String
name|callbackURL
parameter_list|)
throws|throws
name|TwitterException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getOAuthRequestToken (String callbackURL, String xAuthAccessType)
specifier|public
name|RequestToken
name|getOAuthRequestToken
parameter_list|(
name|String
name|callbackURL
parameter_list|,
name|String
name|xAuthAccessType
parameter_list|)
throws|throws
name|TwitterException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getOAuthAccessToken ()
specifier|public
name|AccessToken
name|getOAuthAccessToken
parameter_list|()
throws|throws
name|TwitterException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getOAuthAccessToken (String oauthVerifier)
specifier|public
name|AccessToken
name|getOAuthAccessToken
parameter_list|(
name|String
name|oauthVerifier
parameter_list|)
throws|throws
name|TwitterException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getOAuthAccessToken (RequestToken requestToken)
specifier|public
name|AccessToken
name|getOAuthAccessToken
parameter_list|(
name|RequestToken
name|requestToken
parameter_list|)
throws|throws
name|TwitterException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getOAuthAccessToken (RequestToken requestToken, String oauthVerifier)
specifier|public
name|AccessToken
name|getOAuthAccessToken
parameter_list|(
name|RequestToken
name|requestToken
parameter_list|,
name|String
name|oauthVerifier
parameter_list|)
throws|throws
name|TwitterException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getOAuthAccessToken (String screenName, String password)
specifier|public
name|AccessToken
name|getOAuthAccessToken
parameter_list|(
name|String
name|screenName
parameter_list|,
name|String
name|password
parameter_list|)
throws|throws
name|TwitterException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|setOAuthAccessToken (AccessToken accessToken)
specifier|public
name|void
name|setOAuthAccessToken
parameter_list|(
name|AccessToken
name|accessToken
parameter_list|)
block|{      }
block|}
end_class

end_unit

