begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Message
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
name|impl
operator|.
name|DefaultConsumer
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
name|ExchangeHelper
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

begin_comment
comment|/**  * A Consumer for receiving messages using Cometd and Bayeux protocol.  */
end_comment

begin_class
DECL|class|CometdConsumer
specifier|public
class|class
name|CometdConsumer
extends|extends
name|DefaultConsumer
implements|implements
name|CometdProducerConsumer
block|{
DECL|field|bayeux
specifier|private
name|BayeuxServerImpl
name|bayeux
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|CometdEndpoint
name|endpoint
decl_stmt|;
DECL|field|service
specifier|private
name|ConsumerService
name|service
decl_stmt|;
DECL|method|CometdConsumer (CometdEndpoint endpoint, Processor processor)
specifier|public
name|CometdConsumer
parameter_list|(
name|CometdEndpoint
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
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|start
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
name|service
operator|=
operator|new
name|ConsumerService
argument_list|(
name|endpoint
operator|.
name|getPath
argument_list|()
argument_list|,
name|bayeux
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
name|endpoint
operator|.
name|disconnect
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|super
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
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
DECL|method|getConsumerService ()
specifier|public
name|ConsumerService
name|getConsumerService
parameter_list|()
block|{
return|return
name|service
return|;
block|}
DECL|class|ConsumerService
specifier|public
specifier|static
class|class
name|ConsumerService
extends|extends
name|AbstractService
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|CometdEndpoint
name|endpoint
decl_stmt|;
DECL|field|consumer
specifier|private
specifier|final
name|CometdConsumer
name|consumer
decl_stmt|;
DECL|field|binding
specifier|private
specifier|final
name|CometdBinding
name|binding
decl_stmt|;
DECL|method|ConsumerService (String channel, BayeuxServerImpl bayeux, CometdConsumer consumer)
specifier|public
name|ConsumerService
parameter_list|(
name|String
name|channel
parameter_list|,
name|BayeuxServerImpl
name|bayeux
parameter_list|,
name|CometdConsumer
name|consumer
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
name|binding
operator|=
operator|new
name|CometdBinding
argument_list|(
name|bayeux
argument_list|)
expr_stmt|;
name|this
operator|.
name|consumer
operator|=
name|consumer
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|consumer
operator|.
name|getEndpoint
argument_list|()
expr_stmt|;
name|addService
argument_list|(
name|channel
argument_list|,
literal|"push"
argument_list|)
expr_stmt|;
block|}
DECL|method|push (ServerSession remote, String channelName, ServerMessage cometdMessage, String messageId)
specifier|public
name|void
name|push
parameter_list|(
name|ServerSession
name|remote
parameter_list|,
name|String
name|channelName
parameter_list|,
name|ServerMessage
name|cometdMessage
parameter_list|,
name|String
name|messageId
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|data
init|=
literal|null
decl_stmt|;
name|Message
name|message
init|=
name|binding
operator|.
name|createCamelMessage
argument_list|(
name|remote
argument_list|,
name|cometdMessage
argument_list|,
name|data
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|ExchangeHelper
operator|.
name|isOutCapable
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
name|ServerChannel
name|channel
init|=
name|getBayeux
argument_list|()
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
name|ServerMessage
operator|.
name|Mutable
name|outMessage
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
name|getOut
argument_list|()
argument_list|)
decl_stmt|;
name|remote
operator|.
name|deliver
argument_list|(
name|serverSession
argument_list|,
name|outMessage
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

