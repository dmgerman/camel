begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Entry
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
comment|/**  * Consumer to poll atom feeds and return each entry from the feed step by step.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|AtomEntryPollingConsumer
specifier|public
class|class
name|AtomEntryPollingConsumer
extends|extends
name|AtomPollingConsumer
block|{
DECL|field|document
specifier|private
name|Document
argument_list|<
name|Feed
argument_list|>
name|document
decl_stmt|;
DECL|field|entryIndex
specifier|private
name|int
name|entryIndex
decl_stmt|;
DECL|field|entryFilter
specifier|private
name|EntryFilter
name|entryFilter
decl_stmt|;
DECL|field|list
specifier|private
name|List
argument_list|<
name|Entry
argument_list|>
name|list
decl_stmt|;
DECL|method|AtomEntryPollingConsumer (AtomEndpoint endpoint, Processor processor, boolean filter, Date lastUpdate)
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
operator|new
name|UpdatedDateFilter
argument_list|(
name|lastUpdate
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|poll ()
specifier|public
name|void
name|poll
parameter_list|()
throws|throws
name|Exception
block|{
name|getDocument
argument_list|()
expr_stmt|;
name|Feed
name|feed
init|=
name|document
operator|.
name|getRoot
argument_list|()
decl_stmt|;
while|while
condition|(
name|hasNextEntry
argument_list|()
condition|)
block|{
name|Entry
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
name|document
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
comment|// return and wait for the next poll to continue from last time (this consumer is stateful)
return|return;
block|}
block|}
comment|// reset document to be able to poll again
name|document
operator|=
literal|null
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
name|document
operator|=
name|AtomUtils
operator|.
name|parseDocument
argument_list|(
name|endpoint
operator|.
name|getAtomUri
argument_list|()
argument_list|)
expr_stmt|;
name|list
operator|=
name|document
operator|.
name|getRoot
argument_list|()
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

