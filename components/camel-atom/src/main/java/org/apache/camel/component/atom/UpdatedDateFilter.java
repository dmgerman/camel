begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * Filters out all entries which occur before the last time of the entry we saw (assuming  * entries arrive sorted in order).  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|UpdatedDateFilter
specifier|public
class|class
name|UpdatedDateFilter
implements|implements
name|EntryFilter
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|UpdatedDateFilter
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|lastTime
specifier|private
name|Date
name|lastTime
decl_stmt|;
DECL|method|isValidEntry (AtomEndpoint endpoint, Document<Feed> feed, Entry entry)
specifier|public
name|boolean
name|isValidEntry
parameter_list|(
name|AtomEndpoint
name|endpoint
parameter_list|,
name|Document
argument_list|<
name|Feed
argument_list|>
name|feed
parameter_list|,
name|Entry
name|entry
parameter_list|)
block|{
name|Date
name|updated
init|=
name|getUpdated
argument_list|(
name|endpoint
argument_list|,
name|feed
argument_list|,
name|entry
argument_list|)
decl_stmt|;
if|if
condition|(
name|updated
operator|==
literal|null
condition|)
block|{
name|warnNoUpdatedTime
argument_list|(
name|endpoint
argument_list|,
name|feed
argument_list|,
name|entry
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
if|if
condition|(
name|lastTime
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|lastTime
operator|.
name|after
argument_list|(
name|updated
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
name|lastTime
operator|=
name|updated
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|getUpdated (AtomEndpoint endpoint, Document<Feed> feed, Entry entry)
specifier|protected
name|Date
name|getUpdated
parameter_list|(
name|AtomEndpoint
name|endpoint
parameter_list|,
name|Document
argument_list|<
name|Feed
argument_list|>
name|feed
parameter_list|,
name|Entry
name|entry
parameter_list|)
block|{
name|Date
name|answer
init|=
name|entry
operator|.
name|getUpdated
argument_list|()
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
name|entry
operator|.
name|getEdited
argument_list|()
expr_stmt|;
comment|// TODO is this valid?
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|answer
operator|=
name|entry
operator|.
name|getPublished
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|warnNoUpdatedTime (AtomEndpoint endpoint, Document<Feed> feed, Entry entry)
specifier|protected
name|void
name|warnNoUpdatedTime
parameter_list|(
name|AtomEndpoint
name|endpoint
parameter_list|,
name|Document
argument_list|<
name|Feed
argument_list|>
name|feed
parameter_list|,
name|Entry
name|entry
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"No updated time for entry so assuming new: "
operator|+
name|entry
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

