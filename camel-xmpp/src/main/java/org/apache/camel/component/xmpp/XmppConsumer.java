begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xmpp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xmpp
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

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smack
operator|.
name|PacketListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smack
operator|.
name|packet
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smack
operator|.
name|packet
operator|.
name|Packet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jivesoftware
operator|.
name|smack
operator|.
name|packet
operator|.
name|RosterPacket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_comment
comment|/**  * A {@link Consumer} which listens to XMPP packets  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|XmppConsumer
specifier|public
class|class
name|XmppConsumer
extends|extends
name|DefaultConsumer
argument_list|<
name|XmppExchange
argument_list|>
implements|implements
name|PacketListener
block|{
DECL|field|log
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|log
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|XmppConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|XmppEndpoint
name|endpoint
decl_stmt|;
DECL|method|XmppConsumer (XmppEndpoint endpoint, Processor<XmppExchange> processor)
specifier|public
name|XmppConsumer
parameter_list|(
name|XmppEndpoint
name|endpoint
parameter_list|,
name|Processor
argument_list|<
name|XmppExchange
argument_list|>
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
name|endpoint
operator|.
name|getConnection
argument_list|()
operator|.
name|addPacketListener
argument_list|(
name|this
argument_list|,
name|endpoint
operator|.
name|getFilter
argument_list|()
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
name|endpoint
operator|.
name|getConnection
argument_list|()
operator|.
name|removePacketListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|processPacket (Packet packet)
specifier|public
name|void
name|processPacket
parameter_list|(
name|Packet
name|packet
parameter_list|)
block|{
if|if
condition|(
name|packet
operator|instanceof
name|Message
condition|)
block|{
name|Message
name|message
init|=
operator|(
name|Message
operator|)
name|packet
decl_stmt|;
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"<<<< message: "
operator|+
name|message
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|XmppExchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|getProcessor
argument_list|()
operator|.
name|onExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|packet
operator|instanceof
name|RosterPacket
condition|)
block|{
name|RosterPacket
name|rosterPacket
init|=
operator|(
name|RosterPacket
operator|)
name|packet
decl_stmt|;
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Roster packet with : "
operator|+
name|rosterPacket
operator|.
name|getRosterItemCount
argument_list|()
operator|+
literal|" item(s)"
argument_list|)
expr_stmt|;
name|Iterator
name|rosterItems
init|=
name|rosterPacket
operator|.
name|getRosterItems
argument_list|()
decl_stmt|;
while|while
condition|(
name|rosterItems
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|item
init|=
name|rosterItems
operator|.
name|next
argument_list|()
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Roster item: "
operator|+
name|item
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"<<<< ignored packet: "
operator|+
name|packet
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

