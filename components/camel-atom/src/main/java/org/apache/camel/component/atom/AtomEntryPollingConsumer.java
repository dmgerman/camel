begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atom
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atom
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
name|abdera
operator|.
name|model
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|model
operator|.
name|Feed
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|parser
operator|.
name|ParseException
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
comment|/**  * Consumer to poll atom feeds and return each entry from the feed step by step.  */
end_comment

begin_class
DECL|class|AtomEntryPollingConsumer
specifier|public
class|class
name|AtomEntryPollingConsumer
extends|extends
name|FeedEntryPollingConsumer
block|{
DECL|field|document
specifier|private
name|Document
argument_list|<
name|Feed
argument_list|>
name|document
decl_stmt|;
DECL|method|AtomEntryPollingConsumer (AtomEndpoint endpoint, Processor processor, boolean filter, Date lastUpdate, boolean throttleEntries)
specifier|public
name|AtomEntryPollingConsumer
parameter_list|(
name|AtomEndpoint
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
DECL|method|getDocument ()
specifier|private
name|Document
argument_list|<
name|Feed
argument_list|>
name|getDocument
parameter_list|()
throws|throws
name|IOException
throws|,
name|ParseException
block|{
if|if
condition|(
name|document
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|endpoint
operator|.
name|getUsername
argument_list|()
argument_list|)
operator|||
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|endpoint
operator|.
name|getPassword
argument_list|()
argument_list|)
condition|)
block|{
name|document
operator|=
name|AtomUtils
operator|.
name|parseDocument
argument_list|(
name|endpoint
operator|.
name|getFeedUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|document
operator|=
name|AtomUtils
operator|.
name|parseDocument
argument_list|(
name|endpoint
operator|.
name|getFeedUri
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getUsername
argument_list|()
argument_list|,
name|endpoint
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Feed
name|root
init|=
name|document
operator|.
name|getRoot
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|isSortEntries
argument_list|()
condition|)
block|{
name|sortEntries
argument_list|(
name|root
argument_list|)
expr_stmt|;
block|}
name|list
operator|=
name|root
operator|.
name|getEntries
argument_list|()
expr_stmt|;
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
return|return
name|document
return|;
block|}
DECL|method|sortEntries (Feed feed)
specifier|protected
name|void
name|sortEntries
parameter_list|(
name|Feed
name|feed
parameter_list|)
block|{
name|feed
operator|.
name|sortEntriesByUpdated
argument_list|(
literal|true
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
name|ParseException
throws|,
name|IOException
block|{
comment|// list is populated already in the createFeed method
block|}
annotation|@
name|Override
DECL|method|createFeed ()
specifier|protected
name|Object
name|createFeed
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|getDocument
argument_list|()
operator|.
name|getRoot
argument_list|()
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
name|document
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Override
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

