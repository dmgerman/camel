begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ignite
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ignite
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|Ignite
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ignite
operator|.
name|cluster
operator|.
name|ClusterGroup
import|;
end_import

begin_comment
comment|/**  * Convenient set of commonly used {@link ClusterGroupExpression}s.  */
end_comment

begin_class
DECL|class|ClusterGroupExpressions
specifier|public
specifier|final
class|class
name|ClusterGroupExpressions
block|{
DECL|field|FOR_CLIENTS
specifier|public
specifier|static
specifier|final
name|ClusterGroupExpression
name|FOR_CLIENTS
init|=
operator|new
name|ClusterGroupExpression
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ClusterGroup
name|getClusterGroup
parameter_list|(
name|Ignite
name|ignite
parameter_list|)
block|{
return|return
name|ignite
operator|.
name|cluster
argument_list|()
operator|.
name|forClients
argument_list|()
return|;
block|}
block|}
decl_stmt|;
DECL|field|FOR_DAEMONS
specifier|public
specifier|static
specifier|final
name|ClusterGroupExpression
name|FOR_DAEMONS
init|=
operator|new
name|ClusterGroupExpression
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ClusterGroup
name|getClusterGroup
parameter_list|(
name|Ignite
name|ignite
parameter_list|)
block|{
return|return
name|ignite
operator|.
name|cluster
argument_list|()
operator|.
name|forDaemons
argument_list|()
return|;
block|}
block|}
decl_stmt|;
DECL|field|FOR_LOCAL
specifier|public
specifier|static
specifier|final
name|ClusterGroupExpression
name|FOR_LOCAL
init|=
operator|new
name|ClusterGroupExpression
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ClusterGroup
name|getClusterGroup
parameter_list|(
name|Ignite
name|ignite
parameter_list|)
block|{
return|return
name|ignite
operator|.
name|cluster
argument_list|()
operator|.
name|forLocal
argument_list|()
return|;
block|}
block|}
decl_stmt|;
DECL|field|FOR_OLDEST
specifier|public
specifier|static
specifier|final
name|ClusterGroupExpression
name|FOR_OLDEST
init|=
operator|new
name|ClusterGroupExpression
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ClusterGroup
name|getClusterGroup
parameter_list|(
name|Ignite
name|ignite
parameter_list|)
block|{
return|return
name|ignite
operator|.
name|cluster
argument_list|()
operator|.
name|forOldest
argument_list|()
return|;
block|}
block|}
decl_stmt|;
DECL|field|FOR_YOUNGEST
specifier|public
specifier|static
specifier|final
name|ClusterGroupExpression
name|FOR_YOUNGEST
init|=
operator|new
name|ClusterGroupExpression
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ClusterGroup
name|getClusterGroup
parameter_list|(
name|Ignite
name|ignite
parameter_list|)
block|{
return|return
name|ignite
operator|.
name|cluster
argument_list|()
operator|.
name|forYoungest
argument_list|()
return|;
block|}
block|}
decl_stmt|;
DECL|field|FOR_RANDOM
specifier|public
specifier|static
specifier|final
name|ClusterGroupExpression
name|FOR_RANDOM
init|=
operator|new
name|ClusterGroupExpression
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ClusterGroup
name|getClusterGroup
parameter_list|(
name|Ignite
name|ignite
parameter_list|)
block|{
return|return
name|ignite
operator|.
name|cluster
argument_list|()
operator|.
name|forRandom
argument_list|()
return|;
block|}
block|}
decl_stmt|;
DECL|field|FOR_REMOTES
specifier|public
specifier|static
specifier|final
name|ClusterGroupExpression
name|FOR_REMOTES
init|=
operator|new
name|ClusterGroupExpression
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ClusterGroup
name|getClusterGroup
parameter_list|(
name|Ignite
name|ignite
parameter_list|)
block|{
return|return
name|ignite
operator|.
name|cluster
argument_list|()
operator|.
name|forRemotes
argument_list|()
return|;
block|}
block|}
decl_stmt|;
DECL|field|FOR_SERVERS
specifier|public
specifier|static
specifier|final
name|ClusterGroupExpression
name|FOR_SERVERS
init|=
operator|new
name|ClusterGroupExpression
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|ClusterGroup
name|getClusterGroup
parameter_list|(
name|Ignite
name|ignite
parameter_list|)
block|{
return|return
name|ignite
operator|.
name|cluster
argument_list|()
operator|.
name|forServers
argument_list|()
return|;
block|}
block|}
decl_stmt|;
DECL|method|ClusterGroupExpressions ()
specifier|private
name|ClusterGroupExpressions
parameter_list|()
block|{ }
block|}
end_class

end_unit

