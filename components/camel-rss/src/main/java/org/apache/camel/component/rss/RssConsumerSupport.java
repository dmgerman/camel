begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rss
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rss
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|syndication
operator|.
name|feed
operator|.
name|synd
operator|.
name|SyndFeed
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|syndication
operator|.
name|io
operator|.
name|FeedException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|syndication
operator|.
name|io
operator|.
name|SyndFeedInput
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|syndication
operator|.
name|io
operator|.
name|XmlReader
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
name|impl
operator|.
name|ScheduledPollConsumer
import|;
end_import

begin_comment
comment|/**  * Base class for consuming RSS feeds.  */
end_comment

begin_class
DECL|class|RssConsumerSupport
specifier|public
specifier|abstract
class|class
name|RssConsumerSupport
extends|extends
name|ScheduledPollConsumer
block|{
DECL|field|DEFAULT_CONSUMER_DELAY
specifier|public
specifier|static
specifier|final
name|long
name|DEFAULT_CONSUMER_DELAY
init|=
literal|60
operator|*
literal|1000L
decl_stmt|;
DECL|field|endpoint
specifier|protected
specifier|final
name|RssEndpoint
name|endpoint
decl_stmt|;
DECL|method|RssConsumerSupport (RssEndpoint endpoint, Processor processor)
specifier|public
name|RssConsumerSupport
parameter_list|(
name|RssEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
DECL|method|createFeed ()
specifier|protected
name|SyndFeed
name|createFeed
parameter_list|()
throws|throws
name|IOException
throws|,
name|MalformedURLException
throws|,
name|FeedException
block|{
name|InputStream
name|in
init|=
operator|new
name|URL
argument_list|(
name|endpoint
operator|.
name|getRssUri
argument_list|()
argument_list|)
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|SyndFeedInput
name|input
init|=
operator|new
name|SyndFeedInput
argument_list|()
decl_stmt|;
name|SyndFeed
name|feed
init|=
name|input
operator|.
name|build
argument_list|(
operator|new
name|XmlReader
argument_list|(
name|in
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|feed
return|;
block|}
block|}
end_class

end_unit

