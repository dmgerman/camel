begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cometd
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cometd
package|;
end_package

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
name|support
operator|.
name|DefaultProducer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|cometd
operator|.
name|bayeux
operator|.
name|server
operator|.
name|BayeuxServer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|cometd
operator|.
name|bayeux
operator|.
name|server
operator|.
name|ServerChannel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|cometd
operator|.
name|bayeux
operator|.
name|server
operator|.
name|ServerMessage
import|;
end_import

begin_import
import|import
name|org
operator|.
name|cometd
operator|.
name|bayeux
operator|.
name|server
operator|.
name|ServerSession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|cometd
operator|.
name|server
operator|.
name|AbstractService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|cometd
operator|.
name|server
operator|.
name|BayeuxServerImpl
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
comment|/**  * A Producer to send messages using Cometd and Bayeux protocol.  */
end_comment

begin_class
DECL|class|CometdProducer
specifier|public
class|class
name|CometdProducer
extends|extends
name|DefaultProducer
implements|implements
name|CometdProducerConsumer
block|{
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
name|CometdProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|bayeux
specifier|private
name|BayeuxServerImpl
name|bayeux
decl_stmt|;
DECL|field|service
specifier|private
name|ProducerService
name|service
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|CometdEndpoint
name|endpoint
decl_stmt|;
DECL|method|CometdProducer (CometdEndpoint endpoint)
specifier|public
name|CometdProducer
parameter_list|(
name|CometdEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|public
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
comment|// must connect first
name|endpoint
operator|.
name|connect
argument_list|(
name|this
argument_list|)
expr_stmt|;
comment|// should probably look into synchronization for this.
if|if
condition|(
name|service
operator|==
literal|null
condition|)
block|{
name|service
operator|=
operator|new
name|ProducerService
argument_list|(
name|getBayeux
argument_list|()
argument_list|,
operator|new
name|CometdBinding
argument_list|(
name|bayeux
argument_list|)
argument_list|,
name|endpoint
operator|.
name|getPath
argument_list|()
argument_list|,
name|this
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|isDisconnectLocalSession
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|public
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|stop
argument_list|()
expr_stmt|;
name|endpoint
operator|.
name|disconnect
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (final Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
name|service
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|CometdEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|endpoint
return|;
block|}
DECL|method|getBayeux ()
specifier|public
name|BayeuxServerImpl
name|getBayeux
parameter_list|()
block|{
return|return
name|bayeux
return|;
block|}
DECL|method|getProducerService ()
specifier|protected
name|ProducerService
name|getProducerService
parameter_list|()
block|{
return|return
name|service
return|;
block|}
annotation|@
name|Override
DECL|method|setBayeux (BayeuxServerImpl bayeux)
specifier|public
name|void
name|setBayeux
parameter_list|(
name|BayeuxServerImpl
name|bayeux
parameter_list|)
block|{
name|this
operator|.
name|bayeux
operator|=
name|bayeux
expr_stmt|;
block|}
DECL|class|ProducerService
specifier|public
specifier|static
class|class
name|ProducerService
extends|extends
name|AbstractService
block|{
DECL|field|producer
specifier|private
specifier|final
name|CometdProducer
name|producer
decl_stmt|;
DECL|field|binding
specifier|private
specifier|final
name|CometdBinding
name|binding
decl_stmt|;
DECL|field|disconnectLocalSession
specifier|private
specifier|final
name|boolean
name|disconnectLocalSession
decl_stmt|;
DECL|method|ProducerService (BayeuxServer bayeux, CometdBinding cometdBinding, String channel, CometdProducer producer, boolean disconnectLocalSession)
specifier|public
name|ProducerService
parameter_list|(
name|BayeuxServer
name|bayeux
parameter_list|,
name|CometdBinding
name|cometdBinding
parameter_list|,
name|String
name|channel
parameter_list|,
name|CometdProducer
name|producer
parameter_list|,
name|boolean
name|disconnectLocalSession
parameter_list|)
block|{
name|super
argument_list|(
name|bayeux
argument_list|,
name|channel
argument_list|)
expr_stmt|;
name|this
operator|.
name|producer
operator|=
name|producer
expr_stmt|;
name|this
operator|.
name|binding
operator|=
name|cometdBinding
expr_stmt|;
name|this
operator|.
name|disconnectLocalSession
operator|=
name|disconnectLocalSession
expr_stmt|;
block|}
DECL|method|process (final Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|channelName
init|=
name|producer
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getPath
argument_list|()
decl_stmt|;
name|BayeuxServerImpl
name|bayeux
init|=
name|producer
operator|.
name|getBayeux
argument_list|()
decl_stmt|;
name|ServerChannel
name|channel
init|=
name|bayeux
operator|.
name|getChannel
argument_list|(
name|channelName
argument_list|)
decl_stmt|;
name|ServerSession
name|serverSession
init|=
name|getServerSession
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|channel
operator|!=
literal|null
condition|)
block|{
name|logDelivery
argument_list|(
name|exchange
argument_list|,
name|channel
argument_list|)
expr_stmt|;
name|ServerMessage
operator|.
name|Mutable
name|mutable
init|=
name|binding
operator|.
name|createCometdMessage
argument_list|(
name|channel
argument_list|,
name|serverSession
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
decl_stmt|;
name|channel
operator|.
name|publish
argument_list|(
name|serverSession
argument_list|,
name|mutable
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|disconnectLocalSession
operator|&&
name|serverSession
operator|.
name|isLocalSession
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Disconnection local session {}"
argument_list|,
name|serverSession
argument_list|)
expr_stmt|;
name|serverSession
operator|.
name|disconnect
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|logDelivery (Exchange exchange, ServerChannel channel)
specifier|private
name|void
name|logDelivery
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|ServerChannel
name|channel
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Delivering to clients %s path: %s exchange: %s"
argument_list|,
name|channel
operator|.
name|getSubscribers
argument_list|()
argument_list|,
name|channel
argument_list|,
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

