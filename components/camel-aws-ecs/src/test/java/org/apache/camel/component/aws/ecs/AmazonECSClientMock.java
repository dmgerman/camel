begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.ecs
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
name|ecs
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
name|Collections
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
name|ecs
operator|.
name|AbstractAmazonECS
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
name|ecs
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
name|ecs
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
name|ecs
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
name|ecs
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
name|ecs
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
name|ecs
operator|.
name|model
operator|.
name|DescribeClustersRequest
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
name|ecs
operator|.
name|model
operator|.
name|DescribeClustersResult
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
name|ecs
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
name|ecs
operator|.
name|model
operator|.
name|ListClustersResult
import|;
end_import

begin_class
DECL|class|AmazonECSClientMock
specifier|public
class|class
name|AmazonECSClientMock
extends|extends
name|AbstractAmazonECS
block|{
DECL|method|AmazonECSClientMock ()
specifier|public
name|AmazonECSClientMock
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
name|setClusterName
argument_list|(
literal|"Test"
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
name|setClusterName
argument_list|(
literal|"Test"
argument_list|)
expr_stmt|;
name|cluster
operator|.
name|setStatus
argument_list|(
literal|"INACTIVE"
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
DECL|method|describeClusters (DescribeClustersRequest request)
specifier|public
name|DescribeClustersResult
name|describeClusters
parameter_list|(
name|DescribeClustersRequest
name|request
parameter_list|)
block|{
name|DescribeClustersResult
name|res
init|=
operator|new
name|DescribeClustersResult
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
name|setClusterName
argument_list|(
literal|"Test"
argument_list|)
expr_stmt|;
name|cluster
operator|.
name|setStatus
argument_list|(
literal|"ACTIVE"
argument_list|)
expr_stmt|;
name|res
operator|.
name|setClusters
argument_list|(
name|Collections
operator|.
name|singleton
argument_list|(
name|cluster
argument_list|)
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
name|setClusterArns
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

