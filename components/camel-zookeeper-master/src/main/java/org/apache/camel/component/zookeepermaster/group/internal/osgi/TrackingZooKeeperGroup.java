begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.zookeepermaster.group.internal.osgi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|zookeepermaster
operator|.
name|group
operator|.
name|internal
operator|.
name|osgi
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
name|component
operator|.
name|zookeepermaster
operator|.
name|group
operator|.
name|NodeState
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
name|zookeepermaster
operator|.
name|group
operator|.
name|internal
operator|.
name|DelegateZooKeeperGroup
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|curator
operator|.
name|framework
operator|.
name|CuratorFramework
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|ServiceReference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|util
operator|.
name|tracker
operator|.
name|ServiceTracker
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|util
operator|.
name|tracker
operator|.
name|ServiceTrackerCustomizer
import|;
end_import

begin_class
DECL|class|TrackingZooKeeperGroup
specifier|public
class|class
name|TrackingZooKeeperGroup
parameter_list|<
name|T
extends|extends
name|NodeState
parameter_list|>
extends|extends
name|DelegateZooKeeperGroup
argument_list|<
name|T
argument_list|>
block|{
DECL|field|bundleContext
specifier|private
specifier|final
name|BundleContext
name|bundleContext
decl_stmt|;
DECL|field|tracker
specifier|private
specifier|final
name|ServiceTracker
argument_list|<
name|CuratorFramework
argument_list|,
name|CuratorFramework
argument_list|>
name|tracker
decl_stmt|;
DECL|method|TrackingZooKeeperGroup (BundleContext bundleContext, String path, Class<T> clazz)
specifier|public
name|TrackingZooKeeperGroup
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|,
name|String
name|path
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
block|{
name|super
argument_list|(
name|path
argument_list|,
name|clazz
argument_list|)
expr_stmt|;
name|this
operator|.
name|bundleContext
operator|=
name|bundleContext
expr_stmt|;
name|this
operator|.
name|tracker
operator|=
operator|new
name|ServiceTracker
argument_list|<>
argument_list|(
name|bundleContext
argument_list|,
name|CuratorFramework
operator|.
name|class
argument_list|,
operator|new
name|ServiceTrackerCustomizer
argument_list|<
name|CuratorFramework
argument_list|,
name|CuratorFramework
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|CuratorFramework
name|addingService
parameter_list|(
name|ServiceReference
argument_list|<
name|CuratorFramework
argument_list|>
name|reference
parameter_list|)
block|{
name|CuratorFramework
name|curator
init|=
name|TrackingZooKeeperGroup
operator|.
name|this
operator|.
name|bundleContext
operator|.
name|getService
argument_list|(
name|reference
argument_list|)
decl_stmt|;
name|useCurator
argument_list|(
name|curator
argument_list|)
expr_stmt|;
return|return
name|curator
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|modifiedService
parameter_list|(
name|ServiceReference
argument_list|<
name|CuratorFramework
argument_list|>
name|reference
parameter_list|,
name|CuratorFramework
name|service
parameter_list|)
block|{             }
annotation|@
name|Override
specifier|public
name|void
name|removedService
parameter_list|(
name|ServiceReference
argument_list|<
name|CuratorFramework
argument_list|>
name|reference
parameter_list|,
name|CuratorFramework
name|service
parameter_list|)
block|{
name|useCurator
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|TrackingZooKeeperGroup
operator|.
name|this
operator|.
name|bundleContext
operator|.
name|ungetService
argument_list|(
name|reference
argument_list|)
expr_stmt|;
block|}
block|}
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
block|{
name|tracker
operator|.
name|open
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
block|{
name|tracker
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

