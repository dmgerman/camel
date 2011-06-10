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
name|Entry
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
name|FeedEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Filters out all entries which occur before the last time of the entry we saw (assuming  * entries arrive sorted in order).  *  * @version   */
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
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|UpdatedDateFilter
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|lastUpdate
specifier|private
name|Date
name|lastUpdate
decl_stmt|;
DECL|method|UpdatedDateFilter (Date lastUpdate)
specifier|public
name|UpdatedDateFilter
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
DECL|method|isValidEntry (FeedEndpoint endpoint, Object feed, Object entry)
specifier|public
name|boolean
name|isValidEntry
parameter_list|(
name|FeedEndpoint
name|endpoint
parameter_list|,
name|Object
name|feed
parameter_list|,
name|Object
name|entry
parameter_list|)
block|{
name|Date
name|updated
init|=
operator|(
operator|(
name|Entry
operator|)
name|entry
operator|)
operator|.
name|getUpdated
argument_list|()
decl_stmt|;
if|if
condition|(
name|updated
operator|==
literal|null
condition|)
block|{
comment|// never been updated so get published date
name|updated
operator|=
operator|(
operator|(
name|Entry
operator|)
name|entry
operator|)
operator|.
name|getPublished
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|updated
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"No updated time for entry so assuming its valid: entry=[{}]"
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
name|lastUpdate
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|lastUpdate
operator|.
name|after
argument_list|(
name|updated
argument_list|)
operator|||
name|lastUpdate
operator|.
name|equals
argument_list|(
name|updated
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Entry is older than lastupdate=[{}], no valid entry=[{}]"
argument_list|,
name|lastUpdate
argument_list|,
name|entry
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
name|lastUpdate
operator|=
name|updated
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

