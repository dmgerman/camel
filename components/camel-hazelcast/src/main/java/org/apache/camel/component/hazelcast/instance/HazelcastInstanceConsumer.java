begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hazelcast.instance
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hazelcast
operator|.
name|instance
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetSocketAddress
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|HazelcastInstance
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|MemberAttributeEvent
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|MembershipEvent
import|;
end_import

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|MembershipListener
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
name|hazelcast
operator|.
name|HazelcastComponentHelper
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
name|hazelcast
operator|.
name|HazelcastConstants
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
name|support
operator|.
name|DefaultEndpoint
import|;
end_import

begin_class
DECL|class|HazelcastInstanceConsumer
specifier|public
class|class
name|HazelcastInstanceConsumer
extends|extends
name|DefaultConsumer
block|{
DECL|method|HazelcastInstanceConsumer (HazelcastInstance hazelcastInstance, DefaultEndpoint endpoint, Processor processor)
specifier|public
name|HazelcastInstanceConsumer
parameter_list|(
name|HazelcastInstance
name|hazelcastInstance
parameter_list|,
name|DefaultEndpoint
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
name|hazelcastInstance
operator|.
name|getCluster
argument_list|()
operator|.
name|addMembershipListener
argument_list|(
operator|new
name|HazelcastMembershipListener
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|class|HazelcastMembershipListener
class|class
name|HazelcastMembershipListener
implements|implements
name|MembershipListener
block|{
annotation|@
name|Override
DECL|method|memberAdded (MembershipEvent event)
specifier|public
name|void
name|memberAdded
parameter_list|(
name|MembershipEvent
name|event
parameter_list|)
block|{
name|this
operator|.
name|sendExchange
argument_list|(
name|event
argument_list|,
name|HazelcastConstants
operator|.
name|ADDED
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|memberRemoved (MembershipEvent event)
specifier|public
name|void
name|memberRemoved
parameter_list|(
name|MembershipEvent
name|event
parameter_list|)
block|{
name|this
operator|.
name|sendExchange
argument_list|(
name|event
argument_list|,
name|HazelcastConstants
operator|.
name|REMOVED
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|memberAttributeChanged (MemberAttributeEvent event)
specifier|public
name|void
name|memberAttributeChanged
parameter_list|(
name|MemberAttributeEvent
name|event
parameter_list|)
block|{
name|this
operator|.
name|sendExchange
argument_list|(
name|event
argument_list|,
name|HazelcastConstants
operator|.
name|UPDATED
argument_list|)
expr_stmt|;
block|}
DECL|method|sendExchange (MembershipEvent event, String action)
specifier|private
name|void
name|sendExchange
parameter_list|(
name|MembershipEvent
name|event
parameter_list|,
name|String
name|action
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
name|HazelcastComponentHelper
operator|.
name|setListenerHeaders
argument_list|(
name|exchange
argument_list|,
name|HazelcastConstants
operator|.
name|INSTANCE_LISTENER
argument_list|,
name|action
argument_list|)
expr_stmt|;
comment|// instance listener header values
name|InetSocketAddress
name|adr
init|=
name|event
operator|.
name|getMember
argument_list|()
operator|.
name|getSocketAddress
argument_list|()
decl_stmt|;
if|if
condition|(
name|adr
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|HazelcastConstants
operator|.
name|INSTANCE_HOST
argument_list|,
name|adr
operator|.
name|getHostName
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
name|HazelcastConstants
operator|.
name|INSTANCE_PORT
argument_list|,
name|adr
operator|.
name|getPort
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
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|getExceptionHandler
argument_list|()
operator|.
name|handleException
argument_list|(
literal|"Error processing exchange for Hazelcast consumer on your Hazelcast cluster."
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

