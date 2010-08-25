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
name|util
operator|.
name|Collections
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
name|component
operator|.
name|feed
operator|.
name|EntryFilter
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
name|feed
operator|.
name|FeedEntryPollingConsumer
import|;
end_import

begin_comment
comment|/**  * Consumer to poll RSS feeds and return each entry from the feed step by step.  */
end_comment

begin_class
DECL|class|RssEntryPollingConsumer
specifier|public
class|class
name|RssEntryPollingConsumer
extends|extends
name|FeedEntryPollingConsumer
block|{
DECL|method|RssEntryPollingConsumer (RssEndpoint endpoint, Processor processor, boolean filter, Date lastUpdate, boolean throttleEntries)
specifier|public
name|RssEntryPollingConsumer
parameter_list|(
name|RssEndpoint
name|endpoint
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
block|{
name|super
argument_list|(
name|endpoint
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
annotation|@
name|Override
DECL|method|populateList (Object feed)
specifier|protected
name|void
name|populateList
parameter_list|(
name|Object
name|feed
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|list
operator|==
literal|null
condition|)
block|{
name|list
operator|=
operator|(
operator|(
name|SyndFeed
operator|)
name|feed
operator|)
operator|.
name|getEntries
argument_list|()
expr_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|isSortEntries
argument_list|()
condition|)
block|{
name|sortEntries
argument_list|()
expr_stmt|;
block|}
name|entryIndex
operator|=
name|list
operator|.
name|size
argument_list|()
operator|-
literal|1
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|sortEntries ()
specifier|protected
name|void
name|sortEntries
parameter_list|()
block|{
name|Collections
operator|.
name|sort
argument_list|(
name|list
argument_list|,
operator|new
name|RssDateComparator
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createFeed ()
specifier|protected
name|Object
name|createFeed
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|RssUtils
operator|.
name|createFeed
argument_list|(
name|endpoint
operator|.
name|getFeedUri
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|resetList ()
specifier|protected
name|void
name|resetList
parameter_list|()
block|{
name|list
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|createEntryFilter (Date lastUpdate)
specifier|protected
name|EntryFilter
name|createEntryFilter
parameter_list|(
name|Date
name|lastUpdate
parameter_list|)
block|{
return|return
operator|new
name|UpdatedDateFilter
argument_list|(
name|lastUpdate
argument_list|)
return|;
block|}
block|}
end_class

end_unit

