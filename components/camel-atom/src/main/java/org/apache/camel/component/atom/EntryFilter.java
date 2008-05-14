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

begin_comment
comment|/**  * Filter used by the {@link org.apache.camel.component.atom.AtomEntryPollingConsumer} to filter entries  * from the feed.  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|EntryFilter
specifier|public
interface|interface
name|EntryFilter
block|{
comment|/**      * Tests to be used as filtering the feed for only entries of interest, such as only new entries, etc.      *      * @param endpoint  the endpoint      * @param feed      the Atom feed      * @param entry     the given entry to filter      * @return<tt>true</tt> to include the entry,<ff>false</tt> to skip it      */
DECL|method|isValidEntry (AtomEndpoint endpoint, Document<Feed> feed, Entry entry)
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
function_decl|;
block|}
end_interface

end_unit

