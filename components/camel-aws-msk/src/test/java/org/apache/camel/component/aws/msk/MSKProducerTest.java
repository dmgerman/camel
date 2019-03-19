begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.msk
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|msk
package|;
end_package

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|kafka
operator|.
name|model
operator|.
name|BrokerNodeGroupInfo
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|kafka
operator|.
name|model
operator|.
name|ClusterState
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|kafka
operator|.
name|model
operator|.
name|CreateClusterResult
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|kafka
operator|.
name|model
operator|.
name|DeleteClusterResult
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|services
operator|.
name|kafka
operator|.
name|model
operator|.
name|ListClustersResult
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
name|BindToRegistry
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
name|EndpointInject
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|MSKProducerTest
specifier|public
class|class
name|MSKProducerTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|BindToRegistry
argument_list|(
literal|"amazonMskClient"
argument_list|)
DECL|field|clientMock
name|AmazonMSKClientMock
name|clientMock
init|=
operator|new
name|AmazonMSKClientMock
argument_list|()
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:result"
argument_list|)
DECL|field|mock
specifier|private
name|MockEndpoint
name|mock
decl_stmt|;
annotation|@
name|Test
DECL|method|mskListClustersTest ()
specifier|public
name|void
name|mskListClustersTest
parameter_list|()
throws|throws
name|Exception
block|{
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:listClusters"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|MSKConstants
operator|.
name|OPERATION
argument_list|,
name|MSKOperations
operator|.
name|listClusters
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|ListClustersResult
name|resultGet
init|=
operator|(
name|ListClustersResult
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|resultGet
operator|.
name|getClusterInfoList
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test-kafka"
argument_list|,
name|resultGet
operator|.
name|getClusterInfoList
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getClusterName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|mskCreateClusterTest ()
specifier|public
name|void
name|mskCreateClusterTest
parameter_list|()
throws|throws
name|Exception
block|{
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:createCluster"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|MSKConstants
operator|.
name|OPERATION
argument_list|,
name|MSKOperations
operator|.
name|createCluster
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|MSKConstants
operator|.
name|CLUSTER_NAME
argument_list|,
literal|"test-kafka"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|MSKConstants
operator|.
name|CLUSTER_KAFKA_VERSION
argument_list|,
literal|"2.1.1"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|MSKConstants
operator|.
name|BROKER_NODES_NUMBER
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|BrokerNodeGroupInfo
name|groupInfo
init|=
operator|new
name|BrokerNodeGroupInfo
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|MSKConstants
operator|.
name|BROKER_NODES_GROUP_INFO
argument_list|,
name|groupInfo
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|CreateClusterResult
name|resultGet
init|=
operator|(
name|CreateClusterResult
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"test-kafka"
argument_list|,
name|resultGet
operator|.
name|getClusterName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ClusterState
operator|.
name|CREATING
operator|.
name|name
argument_list|()
argument_list|,
name|resultGet
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|mskDeleteClusterTest ()
specifier|public
name|void
name|mskDeleteClusterTest
parameter_list|()
throws|throws
name|Exception
block|{
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"direct:deleteCluster"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|MSKConstants
operator|.
name|OPERATION
argument_list|,
name|MSKOperations
operator|.
name|deleteCluster
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|MSKConstants
operator|.
name|CLUSTER_ARN
argument_list|,
literal|"test-kafka"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|DeleteClusterResult
name|resultGet
init|=
operator|(
name|DeleteClusterResult
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"test-kafka"
argument_list|,
name|resultGet
operator|.
name|getClusterArn
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ClusterState
operator|.
name|DELETING
operator|.
name|name
argument_list|()
argument_list|,
name|resultGet
operator|.
name|getState
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
literal|"direct:listClusters"
argument_list|)
operator|.
name|to
argument_list|(
literal|"aws-msk://test?mskClient=#amazonMskClient&operation=listClusters"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:createCluster"
argument_list|)
operator|.
name|to
argument_list|(
literal|"aws-msk://test?mskClient=#amazonMskClient&operation=createCluster"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:deleteCluster"
argument_list|)
operator|.
name|to
argument_list|(
literal|"aws-msk://test?mskClient=#amazonMskClient&operation=deleteCluster"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

