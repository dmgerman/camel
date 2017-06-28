begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.zookeeper
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
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
name|CuratorFramework
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
name|CuratorFrameworkFactory
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
name|retry
operator|.
name|ExponentialBackoffRetry
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
name|x
operator|.
name|discovery
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
name|curator
operator|.
name|x
operator|.
name|discovery
operator|.
name|ServiceDiscoveryBuilder
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
name|x
operator|.
name|discovery
operator|.
name|details
operator|.
name|JsonInstanceSerializer
import|;
end_import

begin_class
DECL|class|ZooKeeperCuratorHelper
specifier|public
specifier|final
class|class
name|ZooKeeperCuratorHelper
block|{
DECL|method|ZooKeeperCuratorHelper ()
specifier|private
name|ZooKeeperCuratorHelper
parameter_list|()
block|{     }
DECL|method|createCurator (ZooKeeperCuratorConfiguration configuration)
specifier|public
specifier|static
name|CuratorFramework
name|createCurator
parameter_list|(
name|ZooKeeperCuratorConfiguration
name|configuration
parameter_list|)
throws|throws
name|Exception
block|{
name|CuratorFramework
name|curator
init|=
name|configuration
operator|.
name|getCuratorFramework
argument_list|()
decl_stmt|;
if|if
condition|(
name|curator
operator|==
literal|null
condition|)
block|{
comment|// Validate parameters
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|configuration
operator|.
name|getNodes
argument_list|()
argument_list|,
literal|"ZooKeeper Nodes"
argument_list|)
expr_stmt|;
name|RetryPolicy
name|retryPolicy
init|=
name|configuration
operator|.
name|getRetryPolicy
argument_list|()
decl_stmt|;
if|if
condition|(
name|retryPolicy
operator|==
literal|null
condition|)
block|{
name|retryPolicy
operator|=
operator|new
name|ExponentialBackoffRetry
argument_list|(
operator|(
name|int
operator|)
name|configuration
operator|.
name|getReconnectBaseSleepTimeUnit
argument_list|()
operator|.
name|toMillis
argument_list|(
name|configuration
operator|.
name|getReconnectBaseSleepTime
argument_list|()
argument_list|)
argument_list|,
operator|(
name|int
operator|)
name|configuration
operator|.
name|getReconnectMaxSleepTimeUnit
argument_list|()
operator|.
name|toMillis
argument_list|(
name|configuration
operator|.
name|getReconnectMaxSleepTime
argument_list|()
argument_list|)
argument_list|,
name|configuration
operator|.
name|getReconnectMaxRetries
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|CuratorFrameworkFactory
operator|.
name|Builder
name|builder
init|=
name|CuratorFrameworkFactory
operator|.
name|builder
argument_list|()
operator|.
name|connectString
argument_list|(
name|String
operator|.
name|join
argument_list|(
literal|","
argument_list|,
name|configuration
operator|.
name|getNodes
argument_list|()
argument_list|)
argument_list|)
operator|.
name|sessionTimeoutMs
argument_list|(
operator|(
name|int
operator|)
name|configuration
operator|.
name|getSessionTimeoutUnit
argument_list|()
operator|.
name|toMillis
argument_list|(
name|configuration
operator|.
name|getSessionTimeout
argument_list|()
argument_list|)
argument_list|)
operator|.
name|connectionTimeoutMs
argument_list|(
operator|(
name|int
operator|)
name|configuration
operator|.
name|getConnectionTimeoutUnit
argument_list|()
operator|.
name|toMillis
argument_list|(
name|configuration
operator|.
name|getConnectionTimeout
argument_list|()
argument_list|)
argument_list|)
operator|.
name|maxCloseWaitMs
argument_list|(
operator|(
name|int
operator|)
name|configuration
operator|.
name|getMaxCloseWaitUnit
argument_list|()
operator|.
name|toMillis
argument_list|(
name|configuration
operator|.
name|getMaxCloseWait
argument_list|()
argument_list|)
argument_list|)
operator|.
name|retryPolicy
argument_list|(
name|retryPolicy
argument_list|)
decl_stmt|;
name|Optional
operator|.
name|ofNullable
argument_list|(
name|configuration
operator|.
name|getNamespace
argument_list|()
argument_list|)
operator|.
name|ifPresent
argument_list|(
name|builder
operator|::
name|namespace
argument_list|)
expr_stmt|;
name|Optional
operator|.
name|ofNullable
argument_list|(
name|configuration
operator|.
name|getAuthInfoList
argument_list|()
argument_list|)
operator|.
name|ifPresent
argument_list|(
name|builder
operator|::
name|authorization
argument_list|)
expr_stmt|;
name|curator
operator|=
name|builder
operator|.
name|build
argument_list|()
expr_stmt|;
block|}
return|return
name|curator
return|;
block|}
DECL|method|createServiceDiscovery (ZooKeeperCuratorConfiguration configuration, CuratorFramework curator, Class<T> payloadType)
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|ServiceDiscovery
argument_list|<
name|T
argument_list|>
name|createServiceDiscovery
parameter_list|(
name|ZooKeeperCuratorConfiguration
name|configuration
parameter_list|,
name|CuratorFramework
name|curator
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|payloadType
parameter_list|)
block|{
return|return
name|ServiceDiscoveryBuilder
operator|.
name|builder
argument_list|(
name|payloadType
argument_list|)
operator|.
name|client
argument_list|(
name|curator
argument_list|)
operator|.
name|basePath
argument_list|(
name|configuration
operator|.
name|getBasePath
argument_list|()
argument_list|)
operator|.
name|serializer
argument_list|(
operator|new
name|JsonInstanceSerializer
argument_list|<>
argument_list|(
name|payloadType
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
end_class

end_unit

