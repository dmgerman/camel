begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.zookeeper.cloud
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|zookeeper
operator|.
name|cloud
package|;
end_package

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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|cloud
operator|.
name|ServiceDiscovery
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
name|cloud
operator|.
name|ServiceDiscoveryFactory
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
name|zookeeper
operator|.
name|ZooKeeperCuratorConfiguration
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
name|annotations
operator|.
name|CloudServiceFactory
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
name|RetryPolicy
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
name|AuthInfo
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

begin_class
annotation|@
name|CloudServiceFactory
argument_list|(
literal|"zookeeper-service-discovery"
argument_list|)
DECL|class|ZooKeeperServiceDiscoveryFactory
specifier|public
class|class
name|ZooKeeperServiceDiscoveryFactory
implements|implements
name|ServiceDiscoveryFactory
block|{
DECL|field|configuration
specifier|private
name|ZooKeeperCuratorConfiguration
name|configuration
decl_stmt|;
DECL|method|ZooKeeperServiceDiscoveryFactory ()
specifier|public
name|ZooKeeperServiceDiscoveryFactory
parameter_list|()
block|{
name|this
operator|.
name|configuration
operator|=
operator|new
name|ZooKeeperCuratorConfiguration
argument_list|()
expr_stmt|;
block|}
DECL|method|ZooKeeperServiceDiscoveryFactory (ZooKeeperCuratorConfiguration configuration)
specifier|public
name|ZooKeeperServiceDiscoveryFactory
parameter_list|(
name|ZooKeeperCuratorConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
operator|.
name|copy
argument_list|()
expr_stmt|;
block|}
comment|// *********************************************
comment|// Properties
comment|// *********************************************
DECL|method|getConfiguration ()
specifier|public
name|ZooKeeperCuratorConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (ZooKeeperCuratorConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|ZooKeeperCuratorConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
operator|.
name|copy
argument_list|()
expr_stmt|;
block|}
DECL|method|getCuratorFramework ()
specifier|public
name|CuratorFramework
name|getCuratorFramework
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getCuratorFramework
argument_list|()
return|;
block|}
DECL|method|setCuratorFramework (CuratorFramework curatorFramework)
specifier|public
name|void
name|setCuratorFramework
parameter_list|(
name|CuratorFramework
name|curatorFramework
parameter_list|)
block|{
name|configuration
operator|.
name|setCuratorFramework
argument_list|(
name|curatorFramework
argument_list|)
expr_stmt|;
block|}
DECL|method|getNodes ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getNodes
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getNodes
argument_list|()
return|;
block|}
DECL|method|setNodes (String nodes)
specifier|public
name|void
name|setNodes
parameter_list|(
name|String
name|nodes
parameter_list|)
block|{
name|configuration
operator|.
name|setNodes
argument_list|(
name|nodes
argument_list|)
expr_stmt|;
block|}
DECL|method|setNodes (List<String> nodes)
specifier|public
name|void
name|setNodes
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|nodes
parameter_list|)
block|{
name|configuration
operator|.
name|setNodes
argument_list|(
name|nodes
argument_list|)
expr_stmt|;
block|}
DECL|method|getNamespace ()
specifier|public
name|String
name|getNamespace
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getNamespace
argument_list|()
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
name|configuration
operator|.
name|setNamespace
argument_list|(
name|namespace
argument_list|)
expr_stmt|;
block|}
DECL|method|getReconnectBaseSleepTime ()
specifier|public
name|long
name|getReconnectBaseSleepTime
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getReconnectBaseSleepTime
argument_list|()
return|;
block|}
DECL|method|setReconnectBaseSleepTime (long reconnectBaseSleepTime)
specifier|public
name|void
name|setReconnectBaseSleepTime
parameter_list|(
name|long
name|reconnectBaseSleepTime
parameter_list|)
block|{
name|configuration
operator|.
name|setReconnectBaseSleepTime
argument_list|(
name|reconnectBaseSleepTime
argument_list|)
expr_stmt|;
block|}
DECL|method|setReconnectBaseSleepTime (long reconnectBaseSleepTime, TimeUnit reconnectBaseSleepTimeUnit)
specifier|public
name|void
name|setReconnectBaseSleepTime
parameter_list|(
name|long
name|reconnectBaseSleepTime
parameter_list|,
name|TimeUnit
name|reconnectBaseSleepTimeUnit
parameter_list|)
block|{
name|configuration
operator|.
name|setReconnectBaseSleepTime
argument_list|(
name|reconnectBaseSleepTime
argument_list|,
name|reconnectBaseSleepTimeUnit
argument_list|)
expr_stmt|;
block|}
DECL|method|getReconnectBaseSleepTimeUnit ()
specifier|public
name|TimeUnit
name|getReconnectBaseSleepTimeUnit
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getReconnectBaseSleepTimeUnit
argument_list|()
return|;
block|}
DECL|method|setReconnectBaseSleepTimeUnit (TimeUnit reconnectBaseSleepTimeUnit)
specifier|public
name|void
name|setReconnectBaseSleepTimeUnit
parameter_list|(
name|TimeUnit
name|reconnectBaseSleepTimeUnit
parameter_list|)
block|{
name|configuration
operator|.
name|setReconnectBaseSleepTimeUnit
argument_list|(
name|reconnectBaseSleepTimeUnit
argument_list|)
expr_stmt|;
block|}
DECL|method|getReconnectMaxSleepTime ()
specifier|public
name|long
name|getReconnectMaxSleepTime
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getReconnectMaxSleepTime
argument_list|()
return|;
block|}
DECL|method|setReconnectMaxSleepTime (long reconnectMaxSleepTime)
specifier|public
name|void
name|setReconnectMaxSleepTime
parameter_list|(
name|long
name|reconnectMaxSleepTime
parameter_list|)
block|{
name|configuration
operator|.
name|setReconnectMaxSleepTime
argument_list|(
name|reconnectMaxSleepTime
argument_list|)
expr_stmt|;
block|}
DECL|method|setReconnectMaxSleepTime (long reconnectMaxSleepTime, TimeUnit reconnectBaseSleepTimeUnit)
specifier|public
name|void
name|setReconnectMaxSleepTime
parameter_list|(
name|long
name|reconnectMaxSleepTime
parameter_list|,
name|TimeUnit
name|reconnectBaseSleepTimeUnit
parameter_list|)
block|{
name|configuration
operator|.
name|setReconnectMaxSleepTime
argument_list|(
name|reconnectMaxSleepTime
argument_list|,
name|reconnectBaseSleepTimeUnit
argument_list|)
expr_stmt|;
block|}
DECL|method|getReconnectMaxSleepTimeUnit ()
specifier|public
name|TimeUnit
name|getReconnectMaxSleepTimeUnit
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getReconnectMaxSleepTimeUnit
argument_list|()
return|;
block|}
DECL|method|setReconnectMaxSleepTimeUnit (TimeUnit reconnectMaxSleepTimeUnit)
specifier|public
name|void
name|setReconnectMaxSleepTimeUnit
parameter_list|(
name|TimeUnit
name|reconnectMaxSleepTimeUnit
parameter_list|)
block|{
name|configuration
operator|.
name|setReconnectMaxSleepTimeUnit
argument_list|(
name|reconnectMaxSleepTimeUnit
argument_list|)
expr_stmt|;
block|}
DECL|method|getReconnectMaxRetries ()
specifier|public
name|int
name|getReconnectMaxRetries
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getReconnectMaxRetries
argument_list|()
return|;
block|}
DECL|method|setReconnectMaxRetries (int reconnectMaxRetries)
specifier|public
name|void
name|setReconnectMaxRetries
parameter_list|(
name|int
name|reconnectMaxRetries
parameter_list|)
block|{
name|configuration
operator|.
name|setReconnectMaxRetries
argument_list|(
name|reconnectMaxRetries
argument_list|)
expr_stmt|;
block|}
DECL|method|getSessionTimeout ()
specifier|public
name|long
name|getSessionTimeout
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getSessionTimeout
argument_list|()
return|;
block|}
DECL|method|setSessionTimeout (long sessionTimeout)
specifier|public
name|void
name|setSessionTimeout
parameter_list|(
name|long
name|sessionTimeout
parameter_list|)
block|{
name|configuration
operator|.
name|setSessionTimeout
argument_list|(
name|sessionTimeout
argument_list|)
expr_stmt|;
block|}
DECL|method|setSessionTimeout (long sessionTimeout, TimeUnit sessionTimeoutUnit)
specifier|public
name|void
name|setSessionTimeout
parameter_list|(
name|long
name|sessionTimeout
parameter_list|,
name|TimeUnit
name|sessionTimeoutUnit
parameter_list|)
block|{
name|configuration
operator|.
name|setSessionTimeout
argument_list|(
name|sessionTimeout
argument_list|,
name|sessionTimeoutUnit
argument_list|)
expr_stmt|;
block|}
DECL|method|getSessionTimeoutUnit ()
specifier|public
name|TimeUnit
name|getSessionTimeoutUnit
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getSessionTimeoutUnit
argument_list|()
return|;
block|}
DECL|method|setSessionTimeoutUnit (TimeUnit sessionTimeoutUnit)
specifier|public
name|void
name|setSessionTimeoutUnit
parameter_list|(
name|TimeUnit
name|sessionTimeoutUnit
parameter_list|)
block|{
name|configuration
operator|.
name|setSessionTimeoutUnit
argument_list|(
name|sessionTimeoutUnit
argument_list|)
expr_stmt|;
block|}
DECL|method|getConnectionTimeout ()
specifier|public
name|long
name|getConnectionTimeout
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getConnectionTimeout
argument_list|()
return|;
block|}
DECL|method|setConnectionTimeout (long connectionTimeout)
specifier|public
name|void
name|setConnectionTimeout
parameter_list|(
name|long
name|connectionTimeout
parameter_list|)
block|{
name|configuration
operator|.
name|setConnectionTimeout
argument_list|(
name|connectionTimeout
argument_list|)
expr_stmt|;
block|}
DECL|method|setConnectionTimeout (long connectionTimeout, TimeUnit connectionTimeotUnit)
specifier|public
name|void
name|setConnectionTimeout
parameter_list|(
name|long
name|connectionTimeout
parameter_list|,
name|TimeUnit
name|connectionTimeotUnit
parameter_list|)
block|{
name|configuration
operator|.
name|setConnectionTimeout
argument_list|(
name|connectionTimeout
argument_list|,
name|connectionTimeotUnit
argument_list|)
expr_stmt|;
block|}
DECL|method|getConnectionTimeoutUnit ()
specifier|public
name|TimeUnit
name|getConnectionTimeoutUnit
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getConnectionTimeoutUnit
argument_list|()
return|;
block|}
DECL|method|setConnectionTimeoutUnit (TimeUnit connectionTimeoutUnit)
specifier|public
name|void
name|setConnectionTimeoutUnit
parameter_list|(
name|TimeUnit
name|connectionTimeoutUnit
parameter_list|)
block|{
name|configuration
operator|.
name|setConnectionTimeoutUnit
argument_list|(
name|connectionTimeoutUnit
argument_list|)
expr_stmt|;
block|}
DECL|method|getAuthInfoList ()
specifier|public
name|List
argument_list|<
name|AuthInfo
argument_list|>
name|getAuthInfoList
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getAuthInfoList
argument_list|()
return|;
block|}
DECL|method|setAuthInfoList (List<AuthInfo> authInfoList)
specifier|public
name|void
name|setAuthInfoList
parameter_list|(
name|List
argument_list|<
name|AuthInfo
argument_list|>
name|authInfoList
parameter_list|)
block|{
name|configuration
operator|.
name|setAuthInfoList
argument_list|(
name|authInfoList
argument_list|)
expr_stmt|;
block|}
DECL|method|getMaxCloseWait ()
specifier|public
name|long
name|getMaxCloseWait
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getMaxCloseWait
argument_list|()
return|;
block|}
DECL|method|setMaxCloseWait (long maxCloseWait)
specifier|public
name|void
name|setMaxCloseWait
parameter_list|(
name|long
name|maxCloseWait
parameter_list|)
block|{
name|configuration
operator|.
name|setMaxCloseWait
argument_list|(
name|maxCloseWait
argument_list|)
expr_stmt|;
block|}
DECL|method|getMaxCloseWaitUnit ()
specifier|public
name|TimeUnit
name|getMaxCloseWaitUnit
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getMaxCloseWaitUnit
argument_list|()
return|;
block|}
DECL|method|setMaxCloseWaitUnit (TimeUnit maxCloseWaitUnit)
specifier|public
name|void
name|setMaxCloseWaitUnit
parameter_list|(
name|TimeUnit
name|maxCloseWaitUnit
parameter_list|)
block|{
name|configuration
operator|.
name|setMaxCloseWaitUnit
argument_list|(
name|maxCloseWaitUnit
argument_list|)
expr_stmt|;
block|}
DECL|method|getRetryPolicy ()
specifier|public
name|RetryPolicy
name|getRetryPolicy
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getRetryPolicy
argument_list|()
return|;
block|}
DECL|method|setRetryPolicy (RetryPolicy retryPolicy)
specifier|public
name|void
name|setRetryPolicy
parameter_list|(
name|RetryPolicy
name|retryPolicy
parameter_list|)
block|{
name|configuration
operator|.
name|setRetryPolicy
argument_list|(
name|retryPolicy
argument_list|)
expr_stmt|;
block|}
DECL|method|getBasePath ()
specifier|public
name|String
name|getBasePath
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getBasePath
argument_list|()
return|;
block|}
DECL|method|setBasePath (String basePath)
specifier|public
name|void
name|setBasePath
parameter_list|(
name|String
name|basePath
parameter_list|)
block|{
name|configuration
operator|.
name|setBasePath
argument_list|(
name|basePath
argument_list|)
expr_stmt|;
block|}
comment|// *********************************************
comment|// Factory
comment|// *********************************************
annotation|@
name|Override
DECL|method|newInstance (CamelContext context)
specifier|public
name|ServiceDiscovery
name|newInstance
parameter_list|(
name|CamelContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
name|ZooKeeperServiceDiscovery
name|discovery
init|=
operator|new
name|ZooKeeperServiceDiscovery
argument_list|(
name|configuration
argument_list|)
decl_stmt|;
name|discovery
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
return|return
name|discovery
return|;
block|}
block|}
end_class

end_unit

