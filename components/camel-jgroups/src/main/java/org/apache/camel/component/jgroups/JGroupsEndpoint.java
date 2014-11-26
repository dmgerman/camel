begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jgroups
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jgroups
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
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
name|Component
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
name|DefaultEndpoint
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
name|UriParam
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
name|UriPath
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jgroups
operator|.
name|Channel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jgroups
operator|.
name|JChannel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jgroups
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jgroups
operator|.
name|View
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

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|scheme
operator|=
literal|"jgroups"
argument_list|,
name|consumerClass
operator|=
name|JGroupsConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"clustering,messaging"
argument_list|)
DECL|class|JGroupsEndpoint
specifier|public
class|class
name|JGroupsEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|HEADER_JGROUPS_ORIGINAL_MESSAGE
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_JGROUPS_ORIGINAL_MESSAGE
init|=
literal|"JGROUPS_ORIGINAL_MESSAGE"
decl_stmt|;
DECL|field|HEADER_JGROUPS_SRC
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_JGROUPS_SRC
init|=
literal|"JGROUPS_SRC"
decl_stmt|;
DECL|field|HEADER_JGROUPS_DEST
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_JGROUPS_DEST
init|=
literal|"JGROUPS_DEST"
decl_stmt|;
DECL|field|HEADER_JGROUPS_CHANNEL_ADDRESS
specifier|public
specifier|static
specifier|final
name|String
name|HEADER_JGROUPS_CHANNEL_ADDRESS
init|=
literal|"JGROUPS_CHANNEL_ADDRESS"
decl_stmt|;
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|JGroupsEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|channel
specifier|private
name|Channel
name|channel
decl_stmt|;
DECL|field|connectCount
specifier|private
name|AtomicInteger
name|connectCount
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
decl_stmt|;
DECL|field|resolvedChannel
specifier|private
name|Channel
name|resolvedChannel
decl_stmt|;
annotation|@
name|UriPath
DECL|field|clusterName
specifier|private
name|String
name|clusterName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|channelProperties
specifier|private
name|String
name|channelProperties
decl_stmt|;
annotation|@
name|UriParam
DECL|field|enableViewMessages
specifier|private
name|Boolean
name|enableViewMessages
decl_stmt|;
annotation|@
name|UriParam
DECL|field|resolvedEnableViewMessages
specifier|private
name|boolean
name|resolvedEnableViewMessages
decl_stmt|;
DECL|method|JGroupsEndpoint (String endpointUri, Component component, Channel channel, String clusterName, String channelProperties, Boolean enableViewMessages)
specifier|public
name|JGroupsEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|Channel
name|channel
parameter_list|,
name|String
name|clusterName
parameter_list|,
name|String
name|channelProperties
parameter_list|,
name|Boolean
name|enableViewMessages
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
name|channel
operator|=
name|channel
expr_stmt|;
name|this
operator|.
name|clusterName
operator|=
name|clusterName
expr_stmt|;
name|this
operator|.
name|channelProperties
operator|=
name|channelProperties
expr_stmt|;
name|this
operator|.
name|enableViewMessages
operator|=
name|enableViewMessages
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|JGroupsProducer
argument_list|(
name|this
argument_list|,
name|resolvedChannel
argument_list|,
name|clusterName
argument_list|)
return|;
block|}
annotation|@
name|Override
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
return|return
operator|new
name|JGroupsConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|resolvedChannel
argument_list|,
name|clusterName
argument_list|)
return|;
block|}
annotation|@
name|Override
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
DECL|method|createExchange (Message message)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|Message
name|message
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
name|setHeader
argument_list|(
name|HEADER_JGROUPS_ORIGINAL_MESSAGE
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HEADER_JGROUPS_SRC
argument_list|,
name|message
operator|.
name|getSrc
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
name|HEADER_JGROUPS_DEST
argument_list|,
name|message
operator|.
name|getDest
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|message
operator|.
name|getObject
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
DECL|method|createExchange (View view)
specifier|public
name|Exchange
name|createExchange
parameter_list|(
name|View
name|view
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
name|view
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
annotation|@
name|Override
DECL|method|createExchange ()
specifier|public
name|Exchange
name|createExchange
parameter_list|()
block|{
name|Exchange
name|exchange
init|=
name|super
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
name|HEADER_JGROUPS_CHANNEL_ADDRESS
argument_list|,
name|resolvedChannel
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
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
name|resolvedChannel
operator|=
name|resolveChannel
argument_list|()
expr_stmt|;
name|resolvedEnableViewMessages
operator|=
name|resolveEnableViewMessages
argument_list|()
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
name|LOG
operator|.
name|trace
argument_list|(
literal|"Closing JGroups Channel {}"
argument_list|,
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|resolvedChannel
operator|.
name|close
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|resolveChannel ()
specifier|private
name|Channel
name|resolveChannel
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|channel
operator|!=
literal|null
condition|)
block|{
return|return
name|channel
return|;
block|}
if|if
condition|(
name|channelProperties
operator|!=
literal|null
operator|&&
operator|!
name|channelProperties
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
operator|new
name|JChannel
argument_list|(
name|channelProperties
argument_list|)
return|;
block|}
return|return
operator|new
name|JChannel
argument_list|()
return|;
block|}
comment|/**      * Connect shared channel, called by producer and consumer.      * @throws Exception      */
DECL|method|connect ()
specifier|public
name|void
name|connect
parameter_list|()
throws|throws
name|Exception
block|{
name|connectCount
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Connecting JGroups Channel {}"
argument_list|,
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|resolvedChannel
operator|.
name|connect
argument_list|(
name|clusterName
argument_list|)
expr_stmt|;
block|}
comment|/**      * Disconnect shared channel, called by producer and consumer.      */
DECL|method|disconnect ()
specifier|public
name|void
name|disconnect
parameter_list|()
block|{
if|if
condition|(
name|connectCount
operator|.
name|decrementAndGet
argument_list|()
operator|==
literal|0
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Disconnecting JGroups Channel {}"
argument_list|,
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
name|resolvedChannel
operator|.
name|disconnect
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|resolveEnableViewMessages ()
specifier|private
name|boolean
name|resolveEnableViewMessages
parameter_list|()
block|{
if|if
condition|(
name|enableViewMessages
operator|!=
literal|null
condition|)
block|{
name|resolvedEnableViewMessages
operator|=
name|enableViewMessages
expr_stmt|;
block|}
return|return
name|resolvedEnableViewMessages
return|;
block|}
DECL|method|getChannel ()
specifier|public
name|Channel
name|getChannel
parameter_list|()
block|{
return|return
name|channel
return|;
block|}
DECL|method|setChannel (Channel channel)
specifier|public
name|void
name|setChannel
parameter_list|(
name|Channel
name|channel
parameter_list|)
block|{
name|this
operator|.
name|channel
operator|=
name|channel
expr_stmt|;
block|}
DECL|method|getClusterName ()
specifier|public
name|String
name|getClusterName
parameter_list|()
block|{
return|return
name|clusterName
return|;
block|}
DECL|method|setClusterName (String clusterName)
specifier|public
name|void
name|setClusterName
parameter_list|(
name|String
name|clusterName
parameter_list|)
block|{
name|this
operator|.
name|clusterName
operator|=
name|clusterName
expr_stmt|;
block|}
DECL|method|getChannelProperties ()
specifier|public
name|String
name|getChannelProperties
parameter_list|()
block|{
return|return
name|channelProperties
return|;
block|}
DECL|method|setChannelProperties (String channelProperties)
specifier|public
name|void
name|setChannelProperties
parameter_list|(
name|String
name|channelProperties
parameter_list|)
block|{
name|this
operator|.
name|channelProperties
operator|=
name|channelProperties
expr_stmt|;
block|}
DECL|method|getResolvedChannel ()
specifier|public
name|Channel
name|getResolvedChannel
parameter_list|()
block|{
return|return
name|resolvedChannel
return|;
block|}
DECL|method|setResolvedChannel (Channel resolvedChannel)
specifier|public
name|void
name|setResolvedChannel
parameter_list|(
name|Channel
name|resolvedChannel
parameter_list|)
block|{
name|this
operator|.
name|resolvedChannel
operator|=
name|resolvedChannel
expr_stmt|;
block|}
DECL|method|getEnableViewMessages ()
specifier|public
name|Boolean
name|getEnableViewMessages
parameter_list|()
block|{
return|return
name|enableViewMessages
return|;
block|}
DECL|method|setEnableViewMessages (Boolean enableViewMessages)
specifier|public
name|void
name|setEnableViewMessages
parameter_list|(
name|Boolean
name|enableViewMessages
parameter_list|)
block|{
name|this
operator|.
name|enableViewMessages
operator|=
name|enableViewMessages
expr_stmt|;
block|}
DECL|method|isResolvedEnableViewMessages ()
specifier|public
name|boolean
name|isResolvedEnableViewMessages
parameter_list|()
block|{
return|return
name|resolvedEnableViewMessages
return|;
block|}
DECL|method|setResolvedEnableViewMessages (boolean resolvedEnableViewMessages)
specifier|public
name|void
name|setResolvedEnableViewMessages
parameter_list|(
name|boolean
name|resolvedEnableViewMessages
parameter_list|)
block|{
name|this
operator|.
name|resolvedEnableViewMessages
operator|=
name|resolvedEnableViewMessages
expr_stmt|;
block|}
block|}
end_class

end_unit

