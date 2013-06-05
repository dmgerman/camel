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
name|text
operator|.
name|ParseException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|twitter4j
operator|.
name|Twitter
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|TwitterFactory
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
name|TwitterStreamFactory
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

begin_import
import|import
name|twitter4j
operator|.
name|conf
operator|.
name|ConfigurationBuilder
import|;
end_import

begin_class
DECL|class|TwitterConfiguration
specifier|public
class|class
name|TwitterConfiguration
block|{
comment|/**      * OAuth      */
annotation|@
name|UriParam
DECL|field|consumerKey
specifier|private
name|String
name|consumerKey
decl_stmt|;
annotation|@
name|UriParam
DECL|field|consumerSecret
specifier|private
name|String
name|consumerSecret
decl_stmt|;
annotation|@
name|UriParam
DECL|field|accessToken
specifier|private
name|String
name|accessToken
decl_stmt|;
annotation|@
name|UriParam
DECL|field|accessTokenSecret
specifier|private
name|String
name|accessTokenSecret
decl_stmt|;
comment|/**      * Defines the Twitter API endpoint.      */
annotation|@
name|UriParam
DECL|field|type
specifier|private
name|String
name|type
decl_stmt|;
comment|/**      * Polling delay.      */
annotation|@
name|UriParam
DECL|field|delay
specifier|private
name|int
name|delay
init|=
literal|60
decl_stmt|;
comment|/**      * Username -- used for searching, etc.      */
annotation|@
name|UriParam
DECL|field|user
specifier|private
name|String
name|user
decl_stmt|;
comment|/**      * Keywords used for search and filters.      */
annotation|@
name|UriParam
DECL|field|keywords
specifier|private
name|String
name|keywords
decl_stmt|;
comment|/**      * Lon/Lat bounding boxes used for filtering.      */
annotation|@
name|UriParam
DECL|field|locations
specifier|private
name|String
name|locations
decl_stmt|;
comment|/**      * List of userIds used for searching, etc.      */
annotation|@
name|UriParam
DECL|field|userIds
specifier|private
name|String
name|userIds
decl_stmt|;
comment|/**      * Filter out old tweets that have been previously polled.      */
annotation|@
name|UriParam
DECL|field|filterOld
specifier|private
name|boolean
name|filterOld
init|=
literal|true
decl_stmt|;
comment|/**      * Used for time-based endpoints (trends, etc.)      */
annotation|@
name|UriParam
DECL|field|date
specifier|private
name|String
name|date
decl_stmt|;
comment|/**      * Used to set the sinceId from pulling      */
annotation|@
name|UriParam
DECL|field|sinceId
specifier|private
name|long
name|sinceId
init|=
literal|1
decl_stmt|;
comment|/**      * Used ot set the preferred language on which to search      */
annotation|@
name|UriParam
DECL|field|lang
specifier|private
name|String
name|lang
decl_stmt|;
comment|/**      * Used to set the maximum tweets per page (max = 100)      */
annotation|@
name|UriParam
DECL|field|count
specifier|private
name|Integer
name|count
decl_stmt|;
annotation|@
name|UriParam
DECL|field|parsedDate
specifier|private
name|Date
name|parsedDate
decl_stmt|;
comment|/**      * Number of page to iterate before stop (default is 1)      */
annotation|@
name|UriParam
DECL|field|numberOfPages
specifier|private
name|Integer
name|numberOfPages
init|=
operator|new
name|Integer
argument_list|(
literal|1
argument_list|)
decl_stmt|;
comment|/**      * Singleton, on demand instances of Twitter4J's Twitter& TwitterStream.      * This should not be created by an endpoint's doStart(), etc., since      * instances of twitter and/or twitterStream can be supplied by the route      * itself.  Further, as an example, we don't want to initialize twitter      * if we only need twitterStream.      */
DECL|field|twitter
specifier|private
name|Twitter
name|twitter
decl_stmt|;
DECL|field|twitterStream
specifier|private
name|TwitterStream
name|twitterStream
decl_stmt|;
comment|/**      * Ensures required fields are available.      */
DECL|method|checkComplete ()
specifier|public
name|void
name|checkComplete
parameter_list|()
block|{
if|if
condition|(
name|twitter
operator|==
literal|null
operator|&&
name|twitterStream
operator|==
literal|null
operator|&&
operator|(
name|consumerKey
operator|.
name|isEmpty
argument_list|()
operator|||
name|consumerSecret
operator|.
name|isEmpty
argument_list|()
operator|||
name|accessToken
operator|.
name|isEmpty
argument_list|()
operator|||
name|accessTokenSecret
operator|.
name|isEmpty
argument_list|()
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"twitter or twitterStream or all of consumerKey, consumerSecret, accessToken, and accessTokenSecret must be set!"
argument_list|)
throw|;
block|}
block|}
comment|/**      * Builds a Twitter4J Configuration using the OAuth params.      *      * @return Configuration      */
DECL|method|getConfiguration ()
specifier|public
name|Configuration
name|getConfiguration
parameter_list|()
block|{
name|ConfigurationBuilder
name|confBuilder
init|=
operator|new
name|ConfigurationBuilder
argument_list|()
decl_stmt|;
name|confBuilder
operator|.
name|setOAuthConsumerKey
argument_list|(
name|consumerKey
argument_list|)
expr_stmt|;
name|confBuilder
operator|.
name|setOAuthConsumerSecret
argument_list|(
name|consumerSecret
argument_list|)
expr_stmt|;
name|confBuilder
operator|.
name|setOAuthAccessToken
argument_list|(
name|accessToken
argument_list|)
expr_stmt|;
name|confBuilder
operator|.
name|setOAuthAccessTokenSecret
argument_list|(
name|accessTokenSecret
argument_list|)
expr_stmt|;
return|return
name|confBuilder
operator|.
name|build
argument_list|()
return|;
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
DECL|method|getKeywords ()
specifier|public
name|String
name|getKeywords
parameter_list|()
block|{
return|return
name|keywords
return|;
block|}
DECL|method|setKeywords (String keywords)
specifier|public
name|void
name|setKeywords
parameter_list|(
name|String
name|keywords
parameter_list|)
block|{
name|this
operator|.
name|keywords
operator|=
name|keywords
expr_stmt|;
block|}
DECL|method|getDelay ()
specifier|public
name|int
name|getDelay
parameter_list|()
block|{
return|return
name|delay
return|;
block|}
DECL|method|setDelay (int delay)
specifier|public
name|void
name|setDelay
parameter_list|(
name|int
name|delay
parameter_list|)
block|{
name|this
operator|.
name|delay
operator|=
name|delay
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
DECL|method|getLocations ()
specifier|public
name|String
name|getLocations
parameter_list|()
block|{
return|return
name|locations
return|;
block|}
DECL|method|setLocations (String locations)
specifier|public
name|void
name|setLocations
parameter_list|(
name|String
name|locations
parameter_list|)
block|{
name|this
operator|.
name|locations
operator|=
name|locations
expr_stmt|;
block|}
DECL|method|getUserIds ()
specifier|public
name|String
name|getUserIds
parameter_list|()
block|{
return|return
name|userIds
return|;
block|}
DECL|method|setUserIds (String userIds)
specifier|public
name|void
name|setUserIds
parameter_list|(
name|String
name|userIds
parameter_list|)
block|{
name|this
operator|.
name|userIds
operator|=
name|userIds
expr_stmt|;
block|}
DECL|method|isFilterOld ()
specifier|public
name|boolean
name|isFilterOld
parameter_list|()
block|{
return|return
name|filterOld
return|;
block|}
DECL|method|setFilterOld (boolean filterOld)
specifier|public
name|void
name|setFilterOld
parameter_list|(
name|boolean
name|filterOld
parameter_list|)
block|{
name|this
operator|.
name|filterOld
operator|=
name|filterOld
expr_stmt|;
block|}
DECL|method|getTwitter ()
specifier|public
name|Twitter
name|getTwitter
parameter_list|()
block|{
if|if
condition|(
name|twitter
operator|==
literal|null
condition|)
block|{
name|twitter
operator|=
operator|new
name|TwitterFactory
argument_list|(
name|getConfiguration
argument_list|()
argument_list|)
operator|.
name|getInstance
argument_list|()
expr_stmt|;
block|}
return|return
name|twitter
return|;
block|}
DECL|method|setTwitter (Twitter twitter)
specifier|public
name|void
name|setTwitter
parameter_list|(
name|Twitter
name|twitter
parameter_list|)
block|{
name|this
operator|.
name|twitter
operator|=
name|twitter
expr_stmt|;
block|}
DECL|method|getTwitterStream ()
specifier|public
name|TwitterStream
name|getTwitterStream
parameter_list|()
block|{
return|return
name|twitterStream
return|;
block|}
DECL|method|setTwitterStream (TwitterStream twitterStream)
specifier|public
name|void
name|setTwitterStream
parameter_list|(
name|TwitterStream
name|twitterStream
parameter_list|)
block|{
name|this
operator|.
name|twitterStream
operator|=
name|twitterStream
expr_stmt|;
block|}
DECL|method|getDate ()
specifier|public
name|String
name|getDate
parameter_list|()
block|{
return|return
name|date
return|;
block|}
DECL|method|parseDate ()
specifier|public
name|Date
name|parseDate
parameter_list|()
block|{
return|return
name|parsedDate
return|;
block|}
DECL|method|setDate (String date)
specifier|public
name|void
name|setDate
parameter_list|(
name|String
name|date
parameter_list|)
block|{
name|this
operator|.
name|date
operator|=
name|date
expr_stmt|;
try|try
block|{
name|SimpleDateFormat
name|sdf
init|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyy-MM-dd"
argument_list|)
decl_stmt|;
name|parsedDate
operator|=
name|sdf
operator|.
name|parse
argument_list|(
name|date
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"date must be in yyyy-mm-dd format!"
argument_list|)
throw|;
block|}
block|}
DECL|method|createTwitterStream ()
specifier|public
name|TwitterStream
name|createTwitterStream
parameter_list|()
block|{
if|if
condition|(
name|twitterStream
operator|==
literal|null
condition|)
block|{
name|twitterStream
operator|=
operator|new
name|TwitterStreamFactory
argument_list|(
name|getConfiguration
argument_list|()
argument_list|)
operator|.
name|getInstance
argument_list|()
expr_stmt|;
block|}
return|return
name|twitterStream
return|;
block|}
DECL|method|getSinceId ()
specifier|public
name|long
name|getSinceId
parameter_list|()
block|{
return|return
name|sinceId
return|;
block|}
DECL|method|setSinceId (long sinceId)
specifier|public
name|void
name|setSinceId
parameter_list|(
name|long
name|sinceId
parameter_list|)
block|{
name|this
operator|.
name|sinceId
operator|=
name|sinceId
expr_stmt|;
block|}
DECL|method|getLang ()
specifier|public
name|String
name|getLang
parameter_list|()
block|{
return|return
name|lang
return|;
block|}
DECL|method|setLang (String lang)
specifier|public
name|void
name|setLang
parameter_list|(
name|String
name|lang
parameter_list|)
block|{
name|this
operator|.
name|lang
operator|=
name|lang
expr_stmt|;
block|}
DECL|method|getCount ()
specifier|public
name|Integer
name|getCount
parameter_list|()
block|{
return|return
name|count
return|;
block|}
DECL|method|setCount (int count)
specifier|public
name|void
name|setCount
parameter_list|(
name|int
name|count
parameter_list|)
block|{
name|this
operator|.
name|count
operator|=
name|count
expr_stmt|;
block|}
DECL|method|getNumberOfPages ()
specifier|public
name|Integer
name|getNumberOfPages
parameter_list|()
block|{
return|return
name|numberOfPages
return|;
block|}
DECL|method|setNumberOfPages (Integer numberOfPages)
specifier|public
name|void
name|setNumberOfPages
parameter_list|(
name|Integer
name|numberOfPages
parameter_list|)
block|{
name|this
operator|.
name|numberOfPages
operator|=
name|numberOfPages
expr_stmt|;
block|}
block|}
end_class

end_unit

