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
name|Consumer
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
name|Producer
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
name|DefaultPollingEndpoint
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
comment|/**  * An<a href="http://activemq.apache.org/camel/atom.html">Atom Endpoint</a>.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|AtomEndpoint
specifier|public
class|class
name|AtomEndpoint
extends|extends
name|DefaultPollingEndpoint
block|{
comment|/**      * Header key for the {@link org.apache.abdera.model.Feed} object is stored on the in message on the exchange.      */
DECL|field|HEADER_ATOM_FEED
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_ATOM_FEED
init|=
literal|"org.apache.camel.component.atom.feed"
decl_stmt|;
DECL|field|atomUri
specifier|private
name|String
name|atomUri
decl_stmt|;
DECL|field|splitEntries
specifier|private
name|boolean
name|splitEntries
init|=
literal|true
decl_stmt|;
DECL|field|lastUpdate
specifier|private
name|Date
name|lastUpdate
decl_stmt|;
DECL|field|filter
specifier|private
name|boolean
name|filter
init|=
literal|true
decl_stmt|;
DECL|method|AtomEndpoint (String endpointUri, AtomComponent component, String atomUri)
specifier|public
name|AtomEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|AtomComponent
name|component
parameter_list|,
name|String
name|atomUri
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|atomUri
operator|=
name|atomUri
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|atomUri
argument_list|,
literal|"atomUri property"
argument_list|)
expr_stmt|;
block|}
DECL|method|AtomEndpoint (String endpointUri, String atomUri)
specifier|public
name|AtomEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|String
name|atomUri
parameter_list|)
block|{
name|this
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
name|this
operator|.
name|atomUri
operator|=
name|atomUri
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|atomUri
argument_list|,
literal|"atomUri property"
argument_list|)
expr_stmt|;
block|}
DECL|method|AtomEndpoint (String endpointUri)
specifier|public
name|AtomEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"AtomProducer is not implemented"
argument_list|)
throw|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|AtomConsumerSupport
name|answer
decl_stmt|;
if|if
condition|(
name|isSplitEntries
argument_list|()
condition|)
block|{
name|answer
operator|=
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
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
operator|new
name|AtomPollingConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
comment|// ScheduledPollConsumer default delay is 500 millis and that is too often for polling a feed,
comment|// so we override with a new default value. End user can override this value by providing a consumer.delay parameter
name|answer
operator|.
name|setDelay
argument_list|(
name|AtomConsumerSupport
operator|.
name|DEFAULT_CONSUMER_DELAY
argument_list|)
expr_stmt|;
name|configureConsumer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * Creates an Exchange with the entries as the in body.      *      * @param feed   the atom feed      * @return the created exchange      */
DECL|method|createExchange (Feed feed)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|Feed
name|feed
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|feed
operator|.
name|getEntries
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HEADER_ATOM_FEED
argument_list|,
name|feed
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
comment|/**      * Creates an Exchange with the given entry as the in body.      *      * @param feed   the atom feed      * @param entry  the entry as the in body      * @return the created exchange      */
DECL|method|createExchange (Feed feed, Entry entry)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|Feed
name|feed
parameter_list|,
name|Entry
name|entry
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|createExchange
argument_list|()
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HEADER_ATOM_FEED
argument_list|,
name|feed
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getAtomUri ()
specifier|public
name|String
name|getAtomUri
parameter_list|()
block|{
return|return
name|atomUri
return|;
block|}
DECL|method|setAtomUri (String atomUri)
specifier|public
name|void
name|setAtomUri
parameter_list|(
name|String
name|atomUri
parameter_list|)
block|{
name|this
operator|.
name|atomUri
operator|=
name|atomUri
expr_stmt|;
block|}
DECL|method|isSplitEntries ()
specifier|public
name|boolean
name|isSplitEntries
parameter_list|()
block|{
return|return
name|splitEntries
return|;
block|}
comment|/**      * Sets whether or not entries should be sent individually or whether the entire      * feed should be sent as a single message      */
DECL|method|setSplitEntries (boolean splitEntries)
specifier|public
name|void
name|setSplitEntries
parameter_list|(
name|boolean
name|splitEntries
parameter_list|)
block|{
name|this
operator|.
name|splitEntries
operator|=
name|splitEntries
expr_stmt|;
block|}
DECL|method|getLastUpdate ()
specifier|public
name|Date
name|getLastUpdate
parameter_list|()
block|{
return|return
name|lastUpdate
return|;
block|}
comment|/**      * Sets the timestamp to be used for filtering entries from the atom feeds.      * This options is only in conjunction with the splitEntries.      */
DECL|method|setLastUpdate (Date lastUpdate)
specifier|public
name|void
name|setLastUpdate
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
DECL|method|isFilter ()
specifier|public
name|boolean
name|isFilter
parameter_list|()
block|{
return|return
name|filter
return|;
block|}
comment|/**      * Sets wether to use filtering or not of the entries.      */
DECL|method|setFilter (boolean filter)
specifier|public
name|void
name|setFilter
parameter_list|(
name|boolean
name|filter
parameter_list|)
block|{
name|this
operator|.
name|filter
operator|=
name|filter
expr_stmt|;
block|}
comment|// Implementation methods
comment|//-------------------------------------------------------------------------
block|}
end_class

end_unit

