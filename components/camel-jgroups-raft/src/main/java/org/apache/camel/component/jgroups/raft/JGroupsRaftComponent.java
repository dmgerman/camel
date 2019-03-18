begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jgroups.raft
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
operator|.
name|raft
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|Endpoint
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
name|jgroups
operator|.
name|raft
operator|.
name|utils
operator|.
name|NopStateMachine
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
name|Metadata
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
name|annotations
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
name|support
operator|.
name|DefaultComponent
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jgroups
operator|.
name|protocols
operator|.
name|raft
operator|.
name|StateMachine
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jgroups
operator|.
name|raft
operator|.
name|RaftHandle
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
comment|/**  * Component providing support for JGroups-raft leader election and shared state machine implementation ({@code org.jgroups.raft.RaftHandle}).  */
end_comment

begin_class
annotation|@
name|Component
argument_list|(
literal|"jgroups-raft"
argument_list|)
DECL|class|JGroupsRaftComponent
specifier|public
class|class
name|JGroupsRaftComponent
extends|extends
name|DefaultComponent
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
name|JGroupsRaftComponent
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|UriParam
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"null"
argument_list|)
DECL|field|raftHandle
specifier|private
name|RaftHandle
name|raftHandle
decl_stmt|;
annotation|@
name|UriParam
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"NopStateMachine"
argument_list|)
DECL|field|stateMachine
specifier|private
name|StateMachine
name|stateMachine
init|=
operator|new
name|NopStateMachine
argument_list|()
decl_stmt|;
annotation|@
name|UriParam
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|raftId
specifier|private
name|String
name|raftId
decl_stmt|;
annotation|@
name|UriParam
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"raft.xml"
argument_list|)
DECL|field|channelProperties
specifier|private
name|String
name|channelProperties
decl_stmt|;
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
return|return
operator|new
name|JGroupsRaftEndpoint
argument_list|(
name|uri
argument_list|,
name|remaining
argument_list|,
name|this
argument_list|,
name|remaining
argument_list|,
name|parameters
argument_list|,
name|raftId
argument_list|,
name|channelProperties
argument_list|,
name|stateMachine
argument_list|,
name|raftHandle
argument_list|)
return|;
block|}
DECL|method|getRaftHandle ()
specifier|public
name|RaftHandle
name|getRaftHandle
parameter_list|()
block|{
return|return
name|raftHandle
return|;
block|}
comment|/**      * RaftHandle to use.      */
DECL|method|setRaftHandle (RaftHandle raftHandle)
specifier|public
name|void
name|setRaftHandle
parameter_list|(
name|RaftHandle
name|raftHandle
parameter_list|)
block|{
name|this
operator|.
name|raftHandle
operator|=
name|raftHandle
expr_stmt|;
block|}
DECL|method|getStateMachine ()
specifier|public
name|StateMachine
name|getStateMachine
parameter_list|()
block|{
return|return
name|stateMachine
return|;
block|}
comment|/**      *      * StateMachine to use.      */
DECL|method|setStateMachine (StateMachine stateMachine)
specifier|public
name|void
name|setStateMachine
parameter_list|(
name|StateMachine
name|stateMachine
parameter_list|)
block|{
name|this
operator|.
name|stateMachine
operator|=
name|stateMachine
expr_stmt|;
block|}
DECL|method|getRaftId ()
specifier|public
name|String
name|getRaftId
parameter_list|()
block|{
return|return
name|raftId
return|;
block|}
comment|/**      *      * Unique raftId to use.      */
DECL|method|setRaftId (String raftId)
specifier|public
name|void
name|setRaftId
parameter_list|(
name|String
name|raftId
parameter_list|)
block|{
name|this
operator|.
name|raftId
operator|=
name|raftId
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
comment|/**      * Specifies configuration properties of the RaftHandle JChannel used by the endpoint (ignored if raftHandle ref is provided).      */
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
comment|//TODO: implement a org.jgroups.protocols.raft.StateMachine as a Camel Consumer.
block|}
end_class

end_unit

