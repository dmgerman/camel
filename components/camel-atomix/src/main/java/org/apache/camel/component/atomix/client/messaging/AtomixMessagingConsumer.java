begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.atomix.client.messaging
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atomix
operator|.
name|client
operator|.
name|messaging
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|io
operator|.
name|atomix
operator|.
name|catalyst
operator|.
name|concurrent
operator|.
name|Listener
import|;
end_import

begin_import
import|import
name|io
operator|.
name|atomix
operator|.
name|group
operator|.
name|DistributedGroup
import|;
end_import

begin_import
import|import
name|io
operator|.
name|atomix
operator|.
name|group
operator|.
name|LocalMember
import|;
end_import

begin_import
import|import
name|io
operator|.
name|atomix
operator|.
name|group
operator|.
name|messaging
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|io
operator|.
name|atomix
operator|.
name|group
operator|.
name|messaging
operator|.
name|MessageConsumer
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
name|atomix
operator|.
name|client
operator|.
name|AbstractAtomixClientConsumer
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
name|atomix
operator|.
name|client
operator|.
name|AtomixClientConstants
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

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atomix
operator|.
name|client
operator|.
name|AtomixClientConstants
operator|.
name|CHANNEL_NAME
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atomix
operator|.
name|client
operator|.
name|AtomixClientConstants
operator|.
name|MEMBER_NAME
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|atomix
operator|.
name|client
operator|.
name|AtomixClientConstants
operator|.
name|RESOURCE_NAME
import|;
end_import

begin_class
DECL|class|AtomixMessagingConsumer
specifier|public
specifier|final
class|class
name|AtomixMessagingConsumer
extends|extends
name|AbstractAtomixClientConsumer
argument_list|<
name|AtomixMessagingEndpoint
argument_list|>
block|{
DECL|field|listeners
specifier|private
specifier|final
name|List
argument_list|<
name|Listener
argument_list|<
name|Message
argument_list|<
name|Object
argument_list|>
argument_list|>
argument_list|>
name|listeners
decl_stmt|;
DECL|field|resultHeader
specifier|private
specifier|final
name|String
name|resultHeader
decl_stmt|;
DECL|field|groupName
specifier|private
specifier|final
name|String
name|groupName
decl_stmt|;
DECL|field|memberName
specifier|private
specifier|final
name|String
name|memberName
decl_stmt|;
DECL|field|channelName
specifier|private
specifier|final
name|String
name|channelName
decl_stmt|;
DECL|field|localMember
specifier|private
name|LocalMember
name|localMember
decl_stmt|;
DECL|field|consumer
specifier|private
name|MessageConsumer
argument_list|<
name|Object
argument_list|>
name|consumer
decl_stmt|;
DECL|method|AtomixMessagingConsumer (AtomixMessagingEndpoint endpoint, Processor processor)
specifier|public
name|AtomixMessagingConsumer
parameter_list|(
name|AtomixMessagingEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|listeners
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|resultHeader
operator|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getResultHeader
argument_list|()
expr_stmt|;
name|this
operator|.
name|groupName
operator|=
name|endpoint
operator|.
name|getResourceName
argument_list|()
expr_stmt|;
name|this
operator|.
name|memberName
operator|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getMemberName
argument_list|()
expr_stmt|;
name|this
operator|.
name|channelName
operator|=
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getChannelName
argument_list|()
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|groupName
argument_list|,
name|RESOURCE_NAME
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|memberName
argument_list|,
name|MEMBER_NAME
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|channelName
argument_list|,
name|CHANNEL_NAME
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|DistributedGroup
name|group
init|=
name|getAtomixEndpoint
argument_list|()
operator|.
name|getAtomix
argument_list|()
operator|.
name|getGroup
argument_list|(
name|groupName
argument_list|,
operator|new
name|DistributedGroup
operator|.
name|Config
argument_list|(
name|getAtomixEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getResourceOptions
argument_list|(
name|groupName
argument_list|)
argument_list|)
argument_list|,
operator|new
name|DistributedGroup
operator|.
name|Options
argument_list|(
name|getAtomixEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getResourceConfig
argument_list|(
name|groupName
argument_list|)
argument_list|)
argument_list|)
operator|.
name|join
argument_list|()
decl_stmt|;
name|this
operator|.
name|localMember
operator|=
name|group
operator|.
name|join
argument_list|(
name|memberName
argument_list|)
operator|.
name|join
argument_list|()
expr_stmt|;
name|this
operator|.
name|consumer
operator|=
name|localMember
operator|.
name|messaging
argument_list|()
operator|.
name|consumer
argument_list|(
name|channelName
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Subscribe to group: {}, member: {}, channel: {}"
argument_list|,
name|groupName
argument_list|,
name|memberName
argument_list|,
name|channelName
argument_list|)
expr_stmt|;
name|this
operator|.
name|listeners
operator|.
name|add
argument_list|(
name|consumer
operator|.
name|onMessage
argument_list|(
name|this
operator|::
name|onMessage
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// close listeners
name|listeners
operator|.
name|forEach
argument_list|(
name|Listener
operator|::
name|close
argument_list|)
expr_stmt|;
if|if
condition|(
name|this
operator|.
name|consumer
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|consumer
operator|.
name|close
argument_list|()
expr_stmt|;
name|this
operator|.
name|consumer
operator|=
literal|null
expr_stmt|;
block|}
comment|//if (this.localMember != null) {
comment|//    this.localMember.leave().join();
comment|//    this.localMember = null;
comment|//}
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
comment|// ********************************************
comment|// Event handler
comment|// ********************************************
DECL|method|onMessage (Message<Object> message)
specifier|private
name|void
name|onMessage
parameter_list|(
name|Message
argument_list|<
name|Object
argument_list|>
name|message
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|AtomixClientConstants
operator|.
name|MESSAGE_ID
argument_list|,
name|message
operator|.
name|id
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|resultHeader
operator|==
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|message
operator|.
name|message
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|resultHeader
argument_list|,
name|message
operator|.
name|message
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|message
operator|.
name|ack
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|message
operator|.
name|fail
argument_list|()
expr_stmt|;
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

