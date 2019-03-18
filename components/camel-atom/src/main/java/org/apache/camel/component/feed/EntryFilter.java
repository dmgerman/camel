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

begin_comment
comment|/**  * Filter used by the {@link org.apache.camel.component.feed.FeedEntryPollingConsumer} to filter entries  * from the feed.  */
end_comment

begin_interface
DECL|interface|EntryFilter
specifier|public
interface|interface
name|EntryFilter
block|{
comment|/**      * Tests to be used as filtering the feed for only entries of interest, such as only new entries, etc.      *      * @param endpoint  the endpoint      * @param feed      the feed      * @param entry     the given entry to filter      * @return<tt>true</tt> to include the entry,<ff>false</tt> to skip it      */
DECL|method|isValidEntry (FeedEndpoint endpoint, Object feed, Object entry)
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
function_decl|;
block|}
end_interface

end_unit

