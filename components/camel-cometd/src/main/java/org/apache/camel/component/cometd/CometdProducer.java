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
name|java
operator|.
name|util
operator|.
name|Collection
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

begin_import
import|import
name|dojox
operator|.
name|cometd
operator|.
name|Client
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
name|impl
operator|.
name|DefaultProducer
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
name|mortbay
operator|.
name|cometd
operator|.
name|AbstractBayeux
import|;
end_import

begin_comment
comment|/**  * A Producer to send messages using Cometd and Bayeux protocol.  *   * @version $Revision$  */
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
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|CometdProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|bayeux
specifier|private
name|AbstractBayeux
name|bayeux
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
name|endpoint
operator|.
name|connect
argument_list|(
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
name|Collection
argument_list|<
name|Client
argument_list|>
name|clients
init|=
name|bayeux
operator|.
name|getClients
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|Client
argument_list|>
name|iterator
init|=
name|clients
operator|.
name|iterator
argument_list|()
init|;
name|iterator
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Client
name|client
init|=
operator|(
name|Client
operator|)
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|client
operator|.
name|deliver
argument_list|(
name|client
argument_list|,
name|endpoint
operator|.
name|getPath
argument_list|()
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
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
DECL|method|getBayeux ()
specifier|public
name|AbstractBayeux
name|getBayeux
parameter_list|()
block|{
return|return
name|bayeux
return|;
block|}
DECL|method|setBayeux (AbstractBayeux bayeux)
specifier|public
name|void
name|setBayeux
parameter_list|(
name|AbstractBayeux
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
block|}
end_class

end_unit

