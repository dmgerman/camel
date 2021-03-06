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
name|builder
operator|.
name|RouteBuilder
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
name|mock
operator|.
name|MockEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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
DECL|class|JGroupsRaftConsumerTest
specifier|public
class|class
name|JGroupsRaftConsumerTest
extends|extends
name|JGroupsRaftAbstractTest
block|{
DECL|field|CLUSTER_NAME
specifier|private
specifier|static
specifier|final
name|String
name|CLUSTER_NAME
init|=
literal|"JGroupsRaftConsumerTest"
decl_stmt|;
DECL|field|CONFIGURED_ENDPOINT_URI
specifier|private
specifier|static
specifier|final
name|String
name|CONFIGURED_ENDPOINT_URI
init|=
literal|"jgroups-raft:"
operator|+
name|CLUSTER_NAME
operator|+
literal|"?raftId=A&channelProperties=raftABC.xml&enableRoleChangeEvents=true"
decl_stmt|;
DECL|field|CONFIGURED_ENDPOINT_URI2
specifier|private
specifier|static
specifier|final
name|String
name|CONFIGURED_ENDPOINT_URI2
init|=
literal|"jgroups-raft:"
operator|+
name|CLUSTER_NAME
operator|+
literal|"?raftId=B&channelProperties=raftABC.xml&enableRoleChangeEvents=true"
decl_stmt|;
DECL|field|CONFIGURED_ENDPOINT_URI3
specifier|private
specifier|static
specifier|final
name|String
name|CONFIGURED_ENDPOINT_URI3
init|=
literal|"jgroups-raft:"
operator|+
name|CLUSTER_NAME
operator|+
literal|"?raftId=C&channelProperties=raftABC.xml&enableRoleChangeEvents=true"
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
name|JGroupsRaftConsumerTest
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
name|CONFIGURED_ENDPOINT_URI
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:out"
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|CONFIGURED_ENDPOINT_URI2
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:out2"
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|CONFIGURED_ENDPOINT_URI3
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:out3"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|shouldReceiveChangeRoleEvents ()
specifier|public
name|void
name|shouldReceiveChangeRoleEvents
parameter_list|()
throws|throws
name|Exception
block|{
name|JGroupsRaftEndpoint
name|endpoint
init|=
name|getMandatoryEndpoint
argument_list|(
name|CONFIGURED_ENDPOINT_URI
argument_list|,
name|JGroupsRaftEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|JGroupsRaftEndpoint
name|endpoint2
init|=
name|getMandatoryEndpoint
argument_list|(
name|CONFIGURED_ENDPOINT_URI2
argument_list|,
name|JGroupsRaftEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|JGroupsRaftEndpoint
name|endpoint3
init|=
name|getMandatoryEndpoint
argument_list|(
name|CONFIGURED_ENDPOINT_URI3
argument_list|,
name|JGroupsRaftEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|waitForLeader
argument_list|(
literal|5
argument_list|,
name|endpoint
operator|.
name|getResolvedRaftHandle
argument_list|()
argument_list|,
name|endpoint2
operator|.
name|getResolvedRaftHandle
argument_list|()
argument_list|,
name|endpoint3
operator|.
name|getResolvedRaftHandle
argument_list|()
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:out"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|mock2
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:out2"
argument_list|)
decl_stmt|;
name|MockEndpoint
name|mock3
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:out3"
argument_list|)
decl_stmt|;
name|Exchange
name|leaderEventExchange
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Exchange
name|exc
range|:
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
control|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"mock"
operator|+
name|exc
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JGroupsRaftConstants
operator|.
name|HEADER_JGROUPSRAFT_EVENT_TYPE
argument_list|,
name|JGroupsRaftEventType
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|leaderEventExchange
operator|!=
literal|null
condition|)
block|{
break|break;
block|}
if|if
condition|(
name|JGroupsRaftEventType
operator|.
name|LEADER
operator|.
name|equals
argument_list|(
name|exc
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JGroupsRaftConstants
operator|.
name|HEADER_JGROUPSRAFT_EVENT_TYPE
argument_list|,
name|JGroupsRaftEventType
operator|.
name|class
argument_list|)
argument_list|)
condition|)
block|{
name|leaderEventExchange
operator|=
name|exc
expr_stmt|;
block|}
block|}
for|for
control|(
name|Exchange
name|exc
range|:
name|mock2
operator|.
name|getReceivedExchanges
argument_list|()
control|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"mock2"
operator|+
name|exc
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JGroupsRaftConstants
operator|.
name|HEADER_JGROUPSRAFT_EVENT_TYPE
argument_list|,
name|JGroupsRaftEventType
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|leaderEventExchange
operator|!=
literal|null
condition|)
block|{
break|break;
block|}
if|if
condition|(
name|JGroupsRaftEventType
operator|.
name|LEADER
operator|.
name|equals
argument_list|(
name|exc
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JGroupsRaftConstants
operator|.
name|HEADER_JGROUPSRAFT_EVENT_TYPE
argument_list|,
name|JGroupsRaftEventType
operator|.
name|class
argument_list|)
argument_list|)
condition|)
block|{
name|leaderEventExchange
operator|=
name|exc
expr_stmt|;
block|}
block|}
for|for
control|(
name|Exchange
name|exc
range|:
name|mock3
operator|.
name|getReceivedExchanges
argument_list|()
control|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"mock3"
operator|+
name|exc
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JGroupsRaftConstants
operator|.
name|HEADER_JGROUPSRAFT_EVENT_TYPE
argument_list|,
name|JGroupsRaftEventType
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|leaderEventExchange
operator|!=
literal|null
condition|)
block|{
break|break;
block|}
if|if
condition|(
name|JGroupsRaftEventType
operator|.
name|LEADER
operator|.
name|equals
argument_list|(
name|exc
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JGroupsRaftConstants
operator|.
name|HEADER_JGROUPSRAFT_EVENT_TYPE
argument_list|,
name|JGroupsRaftEventType
operator|.
name|class
argument_list|)
argument_list|)
condition|)
block|{
name|leaderEventExchange
operator|=
name|exc
expr_stmt|;
block|}
block|}
name|checkHeaders
argument_list|(
name|leaderEventExchange
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

