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
name|jgroups
operator|.
name|Address
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

begin_comment
comment|/**  * Producer sending messages to the JGroups cluster.  */
end_comment

begin_class
DECL|class|JGroupsProducer
specifier|public
class|class
name|JGroupsProducer
extends|extends
name|DefaultProducer
block|{
comment|// Producer settings
DECL|field|endpoint
specifier|private
specifier|final
name|JGroupsEndpoint
name|endpoint
decl_stmt|;
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
comment|// Constructor
DECL|method|JGroupsProducer (JGroupsEndpoint endpoint, JChannel channel, String clusterName)
specifier|public
name|JGroupsProducer
parameter_list|(
name|JGroupsEndpoint
name|endpoint
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
block|}
comment|// Life cycle callbacks
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
comment|// Processing logic
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|body
operator|!=
literal|null
condition|)
block|{
name|Address
name|destinationAddress
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JGroupsEndpoint
operator|.
name|HEADER_JGROUPS_DEST
argument_list|,
name|Address
operator|.
name|class
argument_list|)
decl_stmt|;
name|Address
name|sourceAddress
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JGroupsEndpoint
operator|.
name|HEADER_JGROUPS_SRC
argument_list|,
name|Address
operator|.
name|class
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Posting: {} to cluster: {}"
argument_list|,
name|body
argument_list|,
name|clusterName
argument_list|)
expr_stmt|;
if|if
condition|(
name|destinationAddress
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Posting to custom destination address: {}"
argument_list|,
name|destinationAddress
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sourceAddress
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Posting from custom source address: {}"
argument_list|,
name|sourceAddress
argument_list|)
expr_stmt|;
block|}
name|Message
name|message
init|=
operator|new
name|Message
argument_list|(
name|destinationAddress
argument_list|,
name|body
argument_list|)
decl_stmt|;
name|message
operator|.
name|setSrc
argument_list|(
name|sourceAddress
argument_list|)
expr_stmt|;
name|channel
operator|.
name|send
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Body is null, cannot post to channel."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

