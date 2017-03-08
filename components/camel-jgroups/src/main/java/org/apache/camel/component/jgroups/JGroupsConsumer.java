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

begin_comment
comment|/**  * Consumes messages from the JGroups channels ({@code org.jgroups.Channel}). Received messages  * ({@code org.jgroups.Message}) are routed to Camel as the body of {@link org.apache.camel.Exchange}.  */
end_comment

begin_class
DECL|class|JGroupsConsumer
specifier|public
class|class
name|JGroupsConsumer
extends|extends
name|DefaultConsumer
block|{
DECL|field|channel
specifier|private
specifier|final
name|JChannel
name|channel
decl_stmt|;
DECL|field|clusterName
specifier|private
specifier|final
name|String
name|clusterName
decl_stmt|;
DECL|field|receiver
specifier|private
specifier|final
name|CamelJGroupsReceiver
name|receiver
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|JGroupsEndpoint
name|endpoint
decl_stmt|;
DECL|method|JGroupsConsumer (JGroupsEndpoint endpoint, Processor processor, JChannel channel, String clusterName)
specifier|public
name|JGroupsConsumer
parameter_list|(
name|JGroupsEndpoint
name|endpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|JChannel
name|channel
parameter_list|,
name|String
name|clusterName
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
name|receiver
operator|=
operator|new
name|CamelJGroupsReceiver
argument_list|(
name|endpoint
argument_list|,
name|processor
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
name|log
operator|.
name|debug
argument_list|(
literal|"Connecting receiver: {} to the cluster: {}."
argument_list|,
name|receiver
argument_list|,
name|clusterName
argument_list|)
expr_stmt|;
name|channel
operator|.
name|setReceiver
argument_list|(
name|receiver
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|connect
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
name|log
operator|.
name|debug
argument_list|(
literal|"Closing connection to cluster: {} from receiver: {}."
argument_list|,
name|clusterName
argument_list|,
name|receiver
argument_list|)
expr_stmt|;
name|channel
operator|.
name|setReceiver
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|disconnect
argument_list|()
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

