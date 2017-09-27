begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.ha
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|ha
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
name|CamelContext
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
name|RuntimeCamelException
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
name|ha
operator|.
name|CamelClusterService
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
name|model
operator|.
name|RouteDefinition
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
name|RoutePolicy
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
name|RoutePolicyFactory
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_class
DECL|class|ClusteredRoutePolicyFactory
specifier|public
class|class
name|ClusteredRoutePolicyFactory
implements|implements
name|RoutePolicyFactory
block|{
DECL|field|namespace
specifier|private
specifier|final
name|String
name|namespace
decl_stmt|;
DECL|field|clusterService
specifier|private
specifier|final
name|CamelClusterService
name|clusterService
decl_stmt|;
DECL|field|clusterServiceSelector
specifier|private
specifier|final
name|CamelClusterService
operator|.
name|Selector
name|clusterServiceSelector
decl_stmt|;
DECL|method|ClusteredRoutePolicyFactory (String namespace)
specifier|public
name|ClusteredRoutePolicyFactory
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|namespace
argument_list|,
literal|"Cluster View Namespace"
argument_list|)
expr_stmt|;
name|this
operator|.
name|namespace
operator|=
name|namespace
expr_stmt|;
name|this
operator|.
name|clusterService
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|clusterServiceSelector
operator|=
name|ClusterServiceSelectors
operator|.
name|DEFAULT_SELECTOR
expr_stmt|;
block|}
DECL|method|ClusteredRoutePolicyFactory (CamelClusterService.Selector selector, String namespace)
specifier|public
name|ClusteredRoutePolicyFactory
parameter_list|(
name|CamelClusterService
operator|.
name|Selector
name|selector
parameter_list|,
name|String
name|namespace
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|namespace
argument_list|,
literal|"Cluster View Namespace"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|selector
argument_list|,
literal|"Cluster Service Selector"
argument_list|)
expr_stmt|;
name|this
operator|.
name|namespace
operator|=
name|namespace
expr_stmt|;
name|this
operator|.
name|clusterService
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|clusterServiceSelector
operator|=
name|selector
expr_stmt|;
block|}
DECL|method|ClusteredRoutePolicyFactory (CamelClusterService clusterService, String viewName)
specifier|public
name|ClusteredRoutePolicyFactory
parameter_list|(
name|CamelClusterService
name|clusterService
parameter_list|,
name|String
name|viewName
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|clusterService
argument_list|,
literal|"Cluster Service"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|viewName
argument_list|,
literal|"Cluster View Namespace"
argument_list|)
expr_stmt|;
name|this
operator|.
name|clusterService
operator|=
name|clusterService
expr_stmt|;
name|this
operator|.
name|namespace
operator|=
name|viewName
expr_stmt|;
name|this
operator|.
name|clusterServiceSelector
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRoutePolicy (CamelContext camelContext, String routeId, RouteDefinition route)
specifier|public
name|RoutePolicy
name|createRoutePolicy
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|routeId
parameter_list|,
name|RouteDefinition
name|route
parameter_list|)
block|{
try|try
block|{
return|return
name|clusterService
operator|!=
literal|null
condition|?
name|ClusteredRoutePolicy
operator|.
name|forNamespace
argument_list|(
name|clusterService
argument_list|,
name|namespace
argument_list|)
else|:
name|ClusteredRoutePolicy
operator|.
name|forNamespace
argument_list|(
name|camelContext
argument_list|,
name|clusterServiceSelector
argument_list|,
name|namespace
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|// ****************************************************
comment|// Static helpers
comment|// ****************************************************
DECL|method|forNamespace (String namespace)
specifier|public
specifier|static
name|ClusteredRoutePolicyFactory
name|forNamespace
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
return|return
operator|new
name|ClusteredRoutePolicyFactory
argument_list|(
name|namespace
argument_list|)
return|;
block|}
DECL|method|forNamespace (CamelClusterService.Selector selector, String namespace)
specifier|public
specifier|static
name|ClusteredRoutePolicyFactory
name|forNamespace
parameter_list|(
name|CamelClusterService
operator|.
name|Selector
name|selector
parameter_list|,
name|String
name|namespace
parameter_list|)
block|{
return|return
operator|new
name|ClusteredRoutePolicyFactory
argument_list|(
name|selector
argument_list|,
name|namespace
argument_list|)
return|;
block|}
DECL|method|forNamespace (CamelClusterService clusterService, String namespace)
specifier|public
specifier|static
name|ClusteredRoutePolicyFactory
name|forNamespace
parameter_list|(
name|CamelClusterService
name|clusterService
parameter_list|,
name|String
name|namespace
parameter_list|)
block|{
return|return
operator|new
name|ClusteredRoutePolicyFactory
argument_list|(
name|clusterService
argument_list|,
name|namespace
argument_list|)
return|;
block|}
block|}
end_class

end_unit

