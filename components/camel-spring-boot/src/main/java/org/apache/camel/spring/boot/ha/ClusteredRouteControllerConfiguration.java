begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot.ha
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
operator|.
name|ha
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

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
name|ha
operator|.
name|CamelClusterService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.clustered.controller"
argument_list|)
DECL|class|ClusteredRouteControllerConfiguration
specifier|public
class|class
name|ClusteredRouteControllerConfiguration
block|{
comment|/**      * Global option to enable/disable this ${@link org.apache.camel.spi.RouteController}, default is false.      */
DECL|field|enabled
specifier|private
name|boolean
name|enabled
decl_stmt|;
comment|/**      * Set the amount of time the route controller should wait before to start      * the routes after the camel context is started or after the route is      * initialized if the route is created after the camel context is started.      */
DECL|field|initialDelay
specifier|private
name|String
name|initialDelay
decl_stmt|;
comment|/**      * The default namespace.      */
DECL|field|namespace
specifier|private
name|String
name|namespace
decl_stmt|;
comment|/**      * The reference to a cluster service.      */
DECL|field|clusterServiceRef
specifier|private
name|String
name|clusterServiceRef
decl_stmt|;
comment|/**      * The cluster service.      */
DECL|field|clusterService
specifier|private
name|CamelClusterService
name|clusterService
decl_stmt|;
comment|/**      * Routes configuration.      */
DECL|field|routes
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|RouteConfiguration
argument_list|>
name|routes
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|method|isEnabled ()
specifier|public
name|boolean
name|isEnabled
parameter_list|()
block|{
return|return
name|enabled
return|;
block|}
DECL|method|setEnabled (boolean enabled)
specifier|public
name|void
name|setEnabled
parameter_list|(
name|boolean
name|enabled
parameter_list|)
block|{
name|this
operator|.
name|enabled
operator|=
name|enabled
expr_stmt|;
block|}
DECL|method|getInitialDelay ()
specifier|public
name|String
name|getInitialDelay
parameter_list|()
block|{
return|return
name|initialDelay
return|;
block|}
DECL|method|setInitialDelay (String initialDelay)
specifier|public
name|void
name|setInitialDelay
parameter_list|(
name|String
name|initialDelay
parameter_list|)
block|{
name|this
operator|.
name|initialDelay
operator|=
name|initialDelay
expr_stmt|;
block|}
DECL|method|getNamespace ()
specifier|public
name|String
name|getNamespace
parameter_list|()
block|{
return|return
name|namespace
return|;
block|}
DECL|method|setNamespace (String namespace)
specifier|public
name|void
name|setNamespace
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
name|this
operator|.
name|namespace
operator|=
name|namespace
expr_stmt|;
block|}
DECL|method|getRoutes ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|RouteConfiguration
argument_list|>
name|getRoutes
parameter_list|()
block|{
return|return
name|routes
return|;
block|}
DECL|method|setRoutes (Map<String, RouteConfiguration> routes)
specifier|public
name|void
name|setRoutes
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|RouteConfiguration
argument_list|>
name|routes
parameter_list|)
block|{
name|this
operator|.
name|routes
operator|=
name|routes
expr_stmt|;
block|}
DECL|method|getClusterService ()
specifier|public
name|CamelClusterService
name|getClusterService
parameter_list|()
block|{
return|return
name|clusterService
return|;
block|}
DECL|method|setClusterService (CamelClusterService clusterService)
specifier|public
name|void
name|setClusterService
parameter_list|(
name|CamelClusterService
name|clusterService
parameter_list|)
block|{
name|this
operator|.
name|clusterService
operator|=
name|clusterService
expr_stmt|;
block|}
comment|// *****************************************
comment|// Configuration Classes
comment|// *****************************************
DECL|class|RouteConfiguration
specifier|public
specifier|static
class|class
name|RouteConfiguration
block|{
comment|/**          * Control if the route should be clustered or not, default is true.          */
DECL|field|clustered
specifier|private
name|boolean
name|clustered
init|=
literal|true
decl_stmt|;
comment|/**          * Set the amount of time the route controller should wait before to start          * the routes after the camel context is started or after the route is          * initialized if the route is created after the camel context is started.          */
DECL|field|initialDelay
specifier|private
name|String
name|initialDelay
decl_stmt|;
comment|/**          * The default namespace.          */
DECL|field|namespace
specifier|private
name|String
name|namespace
decl_stmt|;
DECL|method|isClustered ()
specifier|public
name|boolean
name|isClustered
parameter_list|()
block|{
return|return
name|clustered
return|;
block|}
DECL|method|setClustered (boolean clustered)
specifier|public
name|void
name|setClustered
parameter_list|(
name|boolean
name|clustered
parameter_list|)
block|{
name|this
operator|.
name|clustered
operator|=
name|clustered
expr_stmt|;
block|}
DECL|method|getInitialDelay ()
specifier|public
name|String
name|getInitialDelay
parameter_list|()
block|{
return|return
name|initialDelay
return|;
block|}
DECL|method|setInitialDelay (String initialDelay)
specifier|public
name|void
name|setInitialDelay
parameter_list|(
name|String
name|initialDelay
parameter_list|)
block|{
name|this
operator|.
name|initialDelay
operator|=
name|initialDelay
expr_stmt|;
block|}
DECL|method|getNamespace ()
specifier|public
name|String
name|getNamespace
parameter_list|()
block|{
return|return
name|namespace
return|;
block|}
DECL|method|setNamespace (String namespace)
specifier|public
name|void
name|setNamespace
parameter_list|(
name|String
name|namespace
parameter_list|)
block|{
name|this
operator|.
name|namespace
operator|=
name|namespace
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

