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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
name|raft
operator|.
name|RaftHandle
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

begin_class
DECL|class|JGroupsRaftComponentConfiguredTest
specifier|public
class|class
name|JGroupsRaftComponentConfiguredTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|CLUSTER_NAME
specifier|static
specifier|final
name|String
name|CLUSTER_NAME
init|=
literal|"JGroupsRaftComponentConfiguredTest"
decl_stmt|;
DECL|field|CONFIGURED_ENDPOINT_URI
specifier|static
specifier|final
name|String
name|CONFIGURED_ENDPOINT_URI
init|=
name|String
operator|.
name|format
argument_list|(
literal|"my-config-jgroupsraft:%s?raftId=B&channelProperties=raftB.xml"
argument_list|,
name|CLUSTER_NAME
argument_list|)
decl_stmt|;
DECL|field|CLUSTER_NAME2
specifier|static
specifier|final
name|String
name|CLUSTER_NAME2
init|=
literal|"JGroupsraftComponentConfiguredTest2"
decl_stmt|;
DECL|field|CONFIGURED_ENDPOINT_URI2
specifier|static
specifier|final
name|String
name|CONFIGURED_ENDPOINT_URI2
init|=
name|String
operator|.
name|format
argument_list|(
literal|"my-config-jgroupsraft2:%s?raftId=C&channelProperties=raftXXX.xml"
argument_list|,
name|CLUSTER_NAME2
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
name|JGroupsRaftComponent
name|configuredComponent
init|=
operator|new
name|JGroupsRaftComponent
argument_list|()
decl_stmt|;
name|context
argument_list|()
operator|.
name|addComponent
argument_list|(
literal|"my-config-jgroupsraft"
argument_list|,
name|configuredComponent
argument_list|)
expr_stmt|;
name|JChannel
name|ch
init|=
operator|new
name|JChannel
argument_list|(
literal|"raftC.xml"
argument_list|)
decl_stmt|;
name|RaftHandle
name|handle
init|=
operator|new
name|RaftHandle
argument_list|(
name|ch
argument_list|,
operator|new
name|NopStateMachine
argument_list|()
argument_list|)
operator|.
name|raftId
argument_list|(
literal|"C"
argument_list|)
decl_stmt|;
name|JGroupsRaftComponent
name|configuredComponent2
init|=
operator|new
name|JGroupsRaftComponent
argument_list|()
decl_stmt|;
name|configuredComponent2
operator|.
name|setRaftHandle
argument_list|(
name|handle
argument_list|)
expr_stmt|;
name|context
argument_list|()
operator|.
name|addComponent
argument_list|(
literal|"my-config-jgroupsraft2"
argument_list|,
name|configuredComponent2
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|CONFIGURED_ENDPOINT_URI
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:configured"
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|CONFIGURED_ENDPOINT_URI2
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:configured2"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|shouldUseChannelPropertiesAndRaftHandle ()
specifier|public
name|void
name|shouldUseChannelPropertiesAndRaftHandle
parameter_list|()
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
name|JGroupsRaftComponent
name|component
init|=
operator|(
name|JGroupsRaftComponent
operator|)
name|endpoint
operator|.
name|getComponent
argument_list|()
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
name|JGroupsRaftComponent
name|component2
init|=
operator|(
name|JGroupsRaftComponent
operator|)
name|endpoint2
operator|.
name|getComponent
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|component
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|endpoint
operator|.
name|getResolvedRaftHandle
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"raftB.xml"
argument_list|,
name|endpoint
operator|.
name|getChannelProperties
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|component2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|endpoint2
operator|.
name|getRaftHandle
argument_list|()
argument_list|,
name|endpoint2
operator|.
name|getResolvedRaftHandle
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

