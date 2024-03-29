begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.feed
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|feed
package|;
end_package

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
name|Consumer
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
name|Exchange
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
name|Processor
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
name|Producer
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
name|support
operator|.
name|DefaultPollingEndpoint
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

begin_comment
comment|/**  * A base class for feed (atom, RSS) endpoints.  */
end_comment

begin_class
DECL|class|FeedEndpoint
specifier|public
specifier|abstract
class|class
name|FeedEndpoint
extends|extends
name|DefaultPollingEndpoint
block|{
annotation|@
name|UriPath
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|feedUri
specifier|protected
name|String
name|feedUri
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|splitEntries
specifier|protected
name|boolean
name|splitEntries
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
DECL|field|lastUpdate
specifier|protected
name|Date
name|lastUpdate
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|filter
specifier|protected
name|boolean
name|filter
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|feedHeader
specifier|private
name|boolean
name|feedHeader
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
DECL|field|sortEntries
specifier|private
name|boolean
name|sortEntries
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|throttleEntries
specifier|private
name|boolean
name|throttleEntries
init|=
literal|true
decl_stmt|;
annotation|@
name|UriParam
DECL|field|username
specifier|private
name|String
name|username
decl_stmt|;
annotation|@
name|UriParam
DECL|field|password
specifier|private
name|String
name|password
decl_stmt|;
DECL|method|FeedEndpoint ()
specifier|public
name|FeedEndpoint
parameter_list|()
block|{     }
DECL|method|FeedEndpoint (String endpointUri, FeedComponent component, String feedUri)
specifier|public
name|FeedEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|FeedComponent
name|component
parameter_list|,
name|String
name|feedUri
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|feedUri
operator|=
name|feedUri
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"FeedProducer is not implemented"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|feedUri
argument_list|,
literal|"feedUri"
argument_list|)
expr_stmt|;
name|FeedPollingConsumer
name|answer
decl_stmt|;
if|if
condition|(
name|isSplitEntries
argument_list|()
condition|)
block|{
name|answer
operator|=
name|createEntryPollingConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|filter
argument_list|,
name|lastUpdate
argument_list|,
name|throttleEntries
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
name|createPollingConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
comment|// ScheduledPollConsumer default delay is 500 millis and that is too often for polling a feed,
comment|// so we override with a new default value. End user can override this value by providing a consumer.delay parameter
name|answer
operator|.
name|setDelay
argument_list|(
name|FeedPollingConsumer
operator|.
name|DEFAULT_CONSUMER_DELAY
argument_list|)
expr_stmt|;
name|configureConsumer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|createPollingConsumer (FeedEndpoint feedEndpoint, Processor processor)
specifier|protected
specifier|abstract
name|FeedPollingConsumer
name|createPollingConsumer
parameter_list|(
name|FeedEndpoint
name|feedEndpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
function_decl|;
DECL|method|createEntryPollingConsumer (FeedEndpoint feedEndpoint, Processor processor, boolean filter, Date lastUpdate, boolean throttleEntries)
specifier|protected
specifier|abstract
name|FeedPollingConsumer
name|createEntryPollingConsumer
parameter_list|(
name|FeedEndpoint
name|feedEndpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|boolean
name|filter
parameter_list|,
name|Date
name|lastUpdate
parameter_list|,
name|boolean
name|throttleEntries
parameter_list|)
throws|throws
name|Exception
function_decl|;
DECL|method|createExchangeWithFeedHeader (Object feed, String header)
specifier|protected
name|Exchange
name|createExchangeWithFeedHeader
parameter_list|(
name|Object
name|feed
parameter_list|,
name|String
name|header
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|()
decl_stmt|;
if|if
condition|(
name|isFeedHeader
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|header
argument_list|,
name|feed
argument_list|)
expr_stmt|;
block|}
return|return
name|exchange
return|;
block|}
comment|/**      * Creates an Exchange with the entries as the in body.      *      * @param feed   the atom feed      * @return the created exchange      */
DECL|method|createExchange (Object feed)
specifier|public
specifier|abstract
name|Exchange
name|createExchange
parameter_list|(
name|Object
name|feed
parameter_list|)
function_decl|;
comment|/**      * Creates an Exchange with the given entry as the in body.      *      * @param feed   the feed      * @param entry  the entry as the in body      * @return the created exchange      */
DECL|method|createExchange (Object feed, Object entry)
specifier|public
specifier|abstract
name|Exchange
name|createExchange
parameter_list|(
name|Object
name|feed
parameter_list|,
name|Object
name|entry
parameter_list|)
function_decl|;
annotation|@
name|Override
DECL|method|createEndpointUri ()
specifier|protected
name|String
name|createEndpointUri
parameter_list|()
block|{
return|return
literal|"atom:"
operator|+
name|feedUri
return|;
block|}
annotation|@
name|Override
DECL|method|getEndpointKey ()
specifier|public
name|String
name|getEndpointKey
parameter_list|()
block|{
comment|// use the full endpoint uri because the lenient properties matters as they should be unique based on the feed uri
return|return
name|getEndpointUri
argument_list|()
return|;
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getFeedUri ()
specifier|public
name|String
name|getFeedUri
parameter_list|()
block|{
return|return
name|feedUri
return|;
block|}
comment|/**      * The URI to the feed to poll.      */
DECL|method|setFeedUri (String feedUri)
specifier|public
name|void
name|setFeedUri
parameter_list|(
name|String
name|feedUri
parameter_list|)
block|{
name|this
operator|.
name|feedUri
operator|=
name|feedUri
expr_stmt|;
block|}
DECL|method|isSplitEntries ()
specifier|public
name|boolean
name|isSplitEntries
parameter_list|()
block|{
return|return
name|splitEntries
return|;
block|}
comment|/**      * Sets whether or not entries should be sent individually or whether the entire      * feed should be sent as a single message      */
DECL|method|setSplitEntries (boolean splitEntries)
specifier|public
name|void
name|setSplitEntries
parameter_list|(
name|boolean
name|splitEntries
parameter_list|)
block|{
name|this
operator|.
name|splitEntries
operator|=
name|splitEntries
expr_stmt|;
block|}
DECL|method|getLastUpdate ()
specifier|public
name|Date
name|getLastUpdate
parameter_list|()
block|{
return|return
name|lastUpdate
return|;
block|}
comment|/**      * Sets the timestamp to be used for filtering entries from the atom feeds.      * This options is only in conjunction with the splitEntries.      */
DECL|method|setLastUpdate (Date lastUpdate)
specifier|public
name|void
name|setLastUpdate
parameter_list|(
name|Date
name|lastUpdate
parameter_list|)
block|{
name|this
operator|.
name|lastUpdate
operator|=
name|lastUpdate
expr_stmt|;
block|}
DECL|method|isFilter ()
specifier|public
name|boolean
name|isFilter
parameter_list|()
block|{
return|return
name|filter
return|;
block|}
comment|/**      * Sets whether to use filtering or not of the entries.      */
DECL|method|setFilter (boolean filter)
specifier|public
name|void
name|setFilter
parameter_list|(
name|boolean
name|filter
parameter_list|)
block|{
name|this
operator|.
name|filter
operator|=
name|filter
expr_stmt|;
block|}
comment|/**      * Sets whether to add the feed object as a header      */
DECL|method|setFeedHeader (boolean feedHeader)
specifier|public
name|void
name|setFeedHeader
parameter_list|(
name|boolean
name|feedHeader
parameter_list|)
block|{
name|this
operator|.
name|feedHeader
operator|=
name|feedHeader
expr_stmt|;
block|}
DECL|method|isFeedHeader ()
specifier|public
name|boolean
name|isFeedHeader
parameter_list|()
block|{
return|return
name|feedHeader
return|;
block|}
comment|/**      * Sets whether to sort entries by published date. Only works when splitEntries = true.      */
DECL|method|setSortEntries (boolean sortEntries)
specifier|public
name|void
name|setSortEntries
parameter_list|(
name|boolean
name|sortEntries
parameter_list|)
block|{
name|this
operator|.
name|sortEntries
operator|=
name|sortEntries
expr_stmt|;
block|}
DECL|method|isSortEntries ()
specifier|public
name|boolean
name|isSortEntries
parameter_list|()
block|{
return|return
name|sortEntries
return|;
block|}
annotation|@
name|Override
DECL|method|isLenientProperties ()
specifier|public
name|boolean
name|isLenientProperties
parameter_list|()
block|{
comment|// true to allow dynamic URI options to be configured and passed to external system for eg. the HttpProducer
return|return
literal|true
return|;
block|}
comment|/**      * Sets whether all entries identified in a single feed poll should be delivered immediately. If true, only one      * entry is processed per consumer.delay. Only applicable when splitEntries = true.      */
DECL|method|setThrottleEntries (boolean throttleEntries)
specifier|public
name|void
name|setThrottleEntries
parameter_list|(
name|boolean
name|throttleEntries
parameter_list|)
block|{
name|this
operator|.
name|throttleEntries
operator|=
name|throttleEntries
expr_stmt|;
block|}
DECL|method|isThrottleEntries ()
specifier|public
name|boolean
name|isThrottleEntries
parameter_list|()
block|{
return|return
name|this
operator|.
name|throttleEntries
return|;
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
comment|/**      * Sets the username to be used for basic authentication when polling from a HTTP feed      */
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
comment|/**      * Sets the password to be used for basic authentication when polling from a HTTP feed      */
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
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
block|}
end_class

end_unit

