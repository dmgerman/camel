begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.eks
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
name|eks
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|eks
operator|.
name|AbstractAmazonEKS
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
name|eks
operator|.
name|model
operator|.
name|Cluster
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
name|eks
operator|.
name|model
operator|.
name|ClusterStatus
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
name|eks
operator|.
name|model
operator|.
name|CreateClusterRequest
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
name|eks
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
name|eks
operator|.
name|model
operator|.
name|DeleteClusterRequest
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
name|eks
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
name|eks
operator|.
name|model
operator|.
name|DescribeClusterRequest
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
name|eks
operator|.
name|model
operator|.
name|DescribeClusterResult
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
name|eks
operator|.
name|model
operator|.
name|ListClustersRequest
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
name|eks
operator|.
name|model
operator|.
name|ListClustersResult
import|;
end_import

begin_class
DECL|class|AmazonEKSClientMock
specifier|public
class|class
name|AmazonEKSClientMock
extends|extends
name|AbstractAmazonEKS
block|{
DECL|method|AmazonEKSClientMock ()
specifier|public
name|AmazonEKSClientMock
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createCluster (CreateClusterRequest request)
specifier|public
name|CreateClusterResult
name|createCluster
parameter_list|(
name|CreateClusterRequest
name|request
parameter_list|)
block|{
name|CreateClusterResult
name|res
init|=
operator|new
name|CreateClusterResult
argument_list|()
decl_stmt|;
name|Cluster
name|cluster
init|=
operator|new
name|Cluster
argument_list|()
decl_stmt|;
name|cluster
operator|.
name|setName
argument_list|(
literal|"Test"
argument_list|)
expr_stmt|;
name|cluster
operator|.
name|setStatus
argument_list|(
name|ClusterStatus
operator|.
name|ACTIVE
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|res
operator|.
name|setCluster
argument_list|(
name|cluster
argument_list|)
expr_stmt|;
return|return
name|res
return|;
block|}
annotation|@
name|Override
DECL|method|deleteCluster (DeleteClusterRequest request)
specifier|public
name|DeleteClusterResult
name|deleteCluster
parameter_list|(
name|DeleteClusterRequest
name|request
parameter_list|)
block|{
name|DeleteClusterResult
name|res
init|=
operator|new
name|DeleteClusterResult
argument_list|()
decl_stmt|;
name|Cluster
name|cluster
init|=
operator|new
name|Cluster
argument_list|()
decl_stmt|;
name|cluster
operator|.
name|setName
argument_list|(
literal|"Test"
argument_list|)
expr_stmt|;
name|cluster
operator|.
name|setStatus
argument_list|(
name|ClusterStatus
operator|.
name|DELETING
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|res
operator|.
name|setCluster
argument_list|(
name|cluster
argument_list|)
expr_stmt|;
return|return
name|res
return|;
block|}
annotation|@
name|Override
DECL|method|describeCluster (DescribeClusterRequest request)
specifier|public
name|DescribeClusterResult
name|describeCluster
parameter_list|(
name|DescribeClusterRequest
name|request
parameter_list|)
block|{
name|DescribeClusterResult
name|res
init|=
operator|new
name|DescribeClusterResult
argument_list|()
decl_stmt|;
name|Cluster
name|cluster
init|=
operator|new
name|Cluster
argument_list|()
decl_stmt|;
name|cluster
operator|.
name|setName
argument_list|(
literal|"Test"
argument_list|)
expr_stmt|;
name|cluster
operator|.
name|setStatus
argument_list|(
name|ClusterStatus
operator|.
name|ACTIVE
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|res
operator|.
name|setCluster
argument_list|(
name|cluster
argument_list|)
expr_stmt|;
return|return
name|res
return|;
block|}
annotation|@
name|Override
DECL|method|listClusters (ListClustersRequest request)
specifier|public
name|ListClustersResult
name|listClusters
parameter_list|(
name|ListClustersRequest
name|request
parameter_list|)
block|{
name|ListClustersResult
name|res
init|=
operator|new
name|ListClustersResult
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|"Test"
argument_list|)
expr_stmt|;
name|res
operator|.
name|setClusters
argument_list|(
name|list
argument_list|)
expr_stmt|;
return|return
name|res
return|;
block|}
block|}
end_class

end_unit

