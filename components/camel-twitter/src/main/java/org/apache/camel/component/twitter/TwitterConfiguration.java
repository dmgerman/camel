begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|data
operator|.
name|EndpointType
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
annotation|@
name|UriParams
DECL|class|TwitterConfiguration
specifier|public
class|class
name|TwitterConfiguration
block|{
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer"
argument_list|,
name|defaultValue
operator|=
literal|"polling"
argument_list|,
name|enums
operator|=
literal|"polling,direct"
argument_list|)
DECL|field|type
specifier|private
name|EndpointType
name|type
init|=
name|EndpointType
operator|.
name|POLLING
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
DECL|field|accessToken
specifier|private
name|String
name|accessToken
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
DECL|field|accessTokenSecret
specifier|private
name|String
name|accessTokenSecret
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
DECL|field|consumerKey
specifier|private
name|String
name|consumerKey
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
DECL|field|consumerSecret
specifier|private
name|String
name|consumerSecret
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer,filter"
argument_list|)
DECL|field|userIds
specifier|private
name|String
name|userIds
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer,filter"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|filterOld
specifier|private
name|boolean
name|filterOld
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer,filter"
argument_list|,
name|defaultValue
operator|=
literal|"1"
argument_list|)
DECL|field|sinceId
specifier|private
name|long
name|sinceId
init|=
literal|1
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer,filter"
argument_list|)
DECL|field|lang
specifier|private
name|String
name|lang
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer,filter"
argument_list|,
name|defaultValue
operator|=
literal|"5"
argument_list|)
DECL|field|count
specifier|private
name|Integer
name|count
init|=
literal|5
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer,filter"
argument_list|,
name|defaultValue
operator|=
literal|"1"
argument_list|)
DECL|field|numberOfPages
specifier|private
name|Integer
name|numberOfPages
init|=
literal|1
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer,sort"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|sortById
specifier|private
name|boolean
name|sortById
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"proxy"
argument_list|)
DECL|field|httpProxyHost
specifier|private
name|String
name|httpProxyHost
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"proxy"
argument_list|)
DECL|field|httpProxyUser
specifier|private
name|String
name|httpProxyUser
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"proxy"
argument_list|)
DECL|field|httpProxyPassword
specifier|private
name|String
name|httpProxyPassword
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"proxy"
argument_list|)
DECL|field|httpProxyPort
specifier|private
name|Integer
name|httpProxyPort
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer,advanced"
argument_list|)
DECL|field|locations
specifier|private
name|String
name|locations
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer,advanced"
argument_list|)
DECL|field|latitude
specifier|private
name|Double
name|latitude
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer,advanced"
argument_list|)
DECL|field|longitude
specifier|private
name|Double
name|longitude
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer,advanced"
argument_list|)
DECL|field|radius
specifier|private
name|Double
name|radius
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer,advanced"
argument_list|,
name|defaultValue
operator|=
literal|"km"
argument_list|,
name|enums
operator|=
literal|"km,mi"
argument_list|)
DECL|field|distanceMetric
specifier|private
name|String
name|distanceMetric
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"consumer,advanced"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|extendedMode
specifier|private
name|boolean
name|extendedMode
init|=
literal|true
decl_stmt|;
comment|/**      * Singleton, on demand instances of Twitter4J's Twitter.      * This should not be created by an endpoint's doStart(), etc., since      * instances of twitter can be supplied by the route      * itself.      */
DECL|field|twitter
specifier|private
name|Twitter
name|twitter
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
operator|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|consumerKey
argument_list|)
operator|||
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|consumerSecret
argument_list|)
operator|||
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|accessToken
argument_list|)
operator|||
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|accessTokenSecret
argument_list|)
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"twitter or all of consumerKey, consumerSecret, accessToken, and accessTokenSecret must be set!"
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
name|checkComplete
argument_list|()
expr_stmt|;
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
name|confBuilder
operator|.
name|setTweetModeExtended
argument_list|(
name|isExtendedMode
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|getHttpProxyHost
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|confBuilder
operator|.
name|setHttpProxyHost
argument_list|(
name|getHttpProxyHost
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getHttpProxyUser
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|confBuilder
operator|.
name|setHttpProxyUser
argument_list|(
name|getHttpProxyUser
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|getHttpProxyPassword
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|confBuilder
operator|.
name|setHttpProxyPassword
argument_list|(
name|getHttpProxyPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|httpProxyPort
operator|!=
literal|null
condition|)
block|{
name|confBuilder
operator|.
name|setHttpProxyPort
argument_list|(
name|httpProxyPort
argument_list|)
expr_stmt|;
block|}
return|return
name|confBuilder
operator|.
name|build
argument_list|()
return|;
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
comment|/**      * The consumer key. Can also be configured on the TwitterComponent level instead.      */
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
comment|/**      * The consumer secret. Can also be configured on the TwitterComponent level instead.      */
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
comment|/**      * The access token. Can also be configured on the TwitterComponent level instead.      */
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
comment|/**      * The access secret. Can also be configured on the TwitterComponent level instead.      */
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
DECL|method|getType ()
specifier|public
name|EndpointType
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
comment|/**      * Endpoint type to use.      */
DECL|method|setType (EndpointType type)
specifier|public
name|void
name|setType
parameter_list|(
name|EndpointType
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
comment|/**      * Bounding boxes, created by pairs of lat/lons. Can be used for filter. A pair is defined as lat,lon. And multiple paris can be separated by semi colon.      */
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
comment|/**      * To filter by user ids for filter. Multiple values can be separated by comma.      */
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
comment|/**      * Filter out old tweets, that has previously been polled.      * This state is stored in memory only, and based on last tweet id.      */
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
comment|/**      * The last tweet id which will be used for pulling the tweets. It is useful when the camel route is restarted after a long running.      */
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
comment|/**      * The lang string ISO_639-1 which will be used for searching      */
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
comment|/**      * Limiting number of results per page.      */
DECL|method|setCount (Integer count)
specifier|public
name|void
name|setCount
parameter_list|(
name|Integer
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
comment|/**      * The number of pages result which you want camel-twitter to consume.      */
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
DECL|method|isSortById ()
specifier|public
name|boolean
name|isSortById
parameter_list|()
block|{
return|return
name|sortById
return|;
block|}
comment|/**      * Sorts by id, so the oldest are first, and newest last.      */
DECL|method|setSortById (boolean sortById)
specifier|public
name|void
name|setSortById
parameter_list|(
name|boolean
name|sortById
parameter_list|)
block|{
name|this
operator|.
name|sortById
operator|=
name|sortById
expr_stmt|;
block|}
comment|/**      * The http proxy host which can be used for the camel-twitter. Can also be configured on the TwitterComponent level instead.      */
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
comment|/**      * The http proxy user which can be used for the camel-twitter. Can also be configured on the TwitterComponent level instead.      */
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
comment|/**      * The http proxy password which can be used for the camel-twitter. Can also be configured on the TwitterComponent level instead.      */
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
comment|/**      * The http proxy port which can be used for the camel-twitter. Can also be configured on the TwitterComponent level instead.      */
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
DECL|method|getLongitude ()
specifier|public
name|Double
name|getLongitude
parameter_list|()
block|{
return|return
name|longitude
return|;
block|}
comment|/**      * Used by the geography search to search by longitude.      *<p/>      * You need to configure all the following options: longitude, latitude, radius, and distanceMetric.      */
DECL|method|setLongitude (Double longitude)
specifier|public
name|void
name|setLongitude
parameter_list|(
name|Double
name|longitude
parameter_list|)
block|{
name|this
operator|.
name|longitude
operator|=
name|longitude
expr_stmt|;
block|}
DECL|method|getLatitude ()
specifier|public
name|Double
name|getLatitude
parameter_list|()
block|{
return|return
name|latitude
return|;
block|}
comment|/**      * Used by the geography search to search by latitude.      *<p/>      * You need to configure all the following options: longitude, latitude, radius, and distanceMetric.      */
DECL|method|setLatitude (Double latitude)
specifier|public
name|void
name|setLatitude
parameter_list|(
name|Double
name|latitude
parameter_list|)
block|{
name|this
operator|.
name|latitude
operator|=
name|latitude
expr_stmt|;
block|}
DECL|method|getRadius ()
specifier|public
name|Double
name|getRadius
parameter_list|()
block|{
return|return
name|radius
return|;
block|}
comment|/**      * Used by the geography search to search by radius.      *<p/>      * You need to configure all the following options: longitude, latitude, radius, and distanceMetric.      */
DECL|method|setRadius (Double radius)
specifier|public
name|void
name|setRadius
parameter_list|(
name|Double
name|radius
parameter_list|)
block|{
name|this
operator|.
name|radius
operator|=
name|radius
expr_stmt|;
block|}
DECL|method|getDistanceMetric ()
specifier|public
name|String
name|getDistanceMetric
parameter_list|()
block|{
return|return
name|distanceMetric
return|;
block|}
comment|/**      * Used by the geography search, to search by radius using the configured metrics.      *<p/>      * The unit can either be mi for miles, or km for kilometers.      *<p/>      * You need to configure all the following options: longitude, latitude, radius, and distanceMetric.      */
DECL|method|setDistanceMetric (String distanceMetric)
specifier|public
name|void
name|setDistanceMetric
parameter_list|(
name|String
name|distanceMetric
parameter_list|)
block|{
name|this
operator|.
name|distanceMetric
operator|=
name|distanceMetric
expr_stmt|;
block|}
comment|/**      * Used for enabling full text from twitter (eg receive tweets that contains more than 140 characters).      */
DECL|method|setExtendedMode (Boolean extendedMode)
specifier|public
name|void
name|setExtendedMode
parameter_list|(
name|Boolean
name|extendedMode
parameter_list|)
block|{
name|this
operator|.
name|extendedMode
operator|=
name|extendedMode
expr_stmt|;
block|}
DECL|method|isExtendedMode ()
specifier|public
name|boolean
name|isExtendedMode
parameter_list|()
block|{
return|return
name|extendedMode
return|;
block|}
block|}
end_class

end_unit

