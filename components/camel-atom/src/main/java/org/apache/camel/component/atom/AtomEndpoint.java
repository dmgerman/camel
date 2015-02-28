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
name|Feed
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
name|component
operator|.
name|feed
operator|.
name|FeedComponent
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
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|feed
operator|.
name|FeedPollingConsumer
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
name|UriEndpoint
import|;
end_import

begin_comment
comment|/**  * Atom is used for polling atom feeds  *  * @version   */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"atom"
argument_list|,
name|syntax
operator|=
literal|"atom:feedUri"
argument_list|,
name|consumerOnly
operator|=
literal|true
argument_list|,
name|consumerClass
operator|=
name|FeedPollingConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"feeds"
argument_list|)
DECL|class|AtomEndpoint
specifier|public
class|class
name|AtomEndpoint
extends|extends
name|FeedEndpoint
block|{
DECL|method|AtomEndpoint ()
specifier|public
name|AtomEndpoint
parameter_list|()
block|{     }
DECL|method|AtomEndpoint (String endpointUri, FeedComponent component, String feedUri)
specifier|public
name|AtomEndpoint
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
argument_list|,
name|feedUri
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createExchange (Object feed)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|Object
name|feed
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|createExchangeWithFeedHeader
argument_list|(
name|feed
argument_list|,
name|AtomConstants
operator|.
name|ATOM_FEED
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
operator|(
operator|(
name|Feed
operator|)
name|feed
operator|)
operator|.
name|getEntries
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
annotation|@
name|Override
DECL|method|createExchange (Object feed, Object entry)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|Object
name|feed
parameter_list|,
name|Object
name|entry
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|createExchangeWithFeedHeader
argument_list|(
name|feed
argument_list|,
name|AtomConstants
operator|.
name|ATOM_FEED
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|entry
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
annotation|@
name|Override
DECL|method|createEntryPollingConsumer (FeedEndpoint feedEndpoint, Processor processor, boolean filter, Date lastUpdate, boolean throttleEntries)
specifier|protected
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
block|{
name|AtomEntryPollingConsumer
name|answer
init|=
operator|new
name|AtomEntryPollingConsumer
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
decl_stmt|;
name|configureConsumer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|createPollingConsumer (FeedEndpoint feedEndpoint, Processor processor)
specifier|protected
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
block|{
name|AtomPollingConsumer
name|answer
init|=
operator|new
name|AtomPollingConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

