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
name|java
operator|.
name|util
operator|.
name|List
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

begin_comment
comment|/**  * Consumer to poll feeds and return each entry from the feed step by step.  */
end_comment

begin_class
DECL|class|FeedEntryPollingConsumer
specifier|public
specifier|abstract
class|class
name|FeedEntryPollingConsumer
extends|extends
name|FeedPollingConsumer
block|{
DECL|field|entryIndex
specifier|protected
name|int
name|entryIndex
decl_stmt|;
DECL|field|entryFilter
specifier|protected
name|EntryFilter
name|entryFilter
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
DECL|field|list
specifier|protected
name|List
name|list
decl_stmt|;
DECL|field|throttleEntries
specifier|protected
name|boolean
name|throttleEntries
decl_stmt|;
DECL|field|feed
specifier|protected
name|Object
name|feed
decl_stmt|;
DECL|method|FeedEntryPollingConsumer (FeedEndpoint endpoint, Processor processor, boolean filter, Date lastUpdate, boolean throttleEntries)
specifier|public
name|FeedEntryPollingConsumer
parameter_list|(
name|FeedEndpoint
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
argument_list|)
expr_stmt|;
if|if
condition|(
name|filter
condition|)
block|{
name|entryFilter
operator|=
name|createEntryFilter
argument_list|(
name|lastUpdate
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|throttleEntries
operator|=
name|throttleEntries
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|poll ()
specifier|public
name|int
name|poll
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|feed
operator|==
literal|null
condition|)
block|{
comment|// populate new feed
name|feed
operator|=
name|createFeed
argument_list|()
expr_stmt|;
name|populateList
argument_list|(
name|feed
argument_list|)
expr_stmt|;
block|}
name|int
name|polledMessages
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|hasNextEntry
argument_list|()
condition|)
block|{
name|Object
name|entry
init|=
name|list
operator|.
name|get
argument_list|(
name|entryIndex
operator|--
argument_list|)
decl_stmt|;
name|polledMessages
operator|++
expr_stmt|;
name|boolean
name|valid
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|entryFilter
operator|!=
literal|null
condition|)
block|{
name|valid
operator|=
name|entryFilter
operator|.
name|isValidEntry
argument_list|(
name|endpoint
argument_list|,
name|feed
argument_list|,
name|entry
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|valid
condition|)
block|{
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|(
name|feed
argument_list|,
name|entry
argument_list|)
decl_stmt|;
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|throttleEntries
condition|)
block|{
comment|// return and wait for the next poll to continue from last time (this consumer is stateful)
return|return
name|polledMessages
return|;
block|}
block|}
block|}
comment|// reset feed and list to be able to poll again
name|feed
operator|=
literal|null
expr_stmt|;
name|resetList
argument_list|()
expr_stmt|;
return|return
name|polledMessages
return|;
block|}
DECL|method|createEntryFilter (Date lastUpdate)
specifier|protected
specifier|abstract
name|EntryFilter
name|createEntryFilter
parameter_list|(
name|Date
name|lastUpdate
parameter_list|)
function_decl|;
DECL|method|resetList ()
specifier|protected
specifier|abstract
name|void
name|resetList
parameter_list|()
function_decl|;
DECL|method|populateList (Object feed)
specifier|protected
specifier|abstract
name|void
name|populateList
parameter_list|(
name|Object
name|feed
parameter_list|)
throws|throws
name|Exception
function_decl|;
DECL|method|hasNextEntry ()
specifier|private
name|boolean
name|hasNextEntry
parameter_list|()
block|{
return|return
name|entryIndex
operator|>=
literal|0
return|;
block|}
block|}
end_class

end_unit

