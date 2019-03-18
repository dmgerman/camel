begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
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
DECL|class|ZooKeeperCuratorConfiguration
specifier|public
class|class
name|ZooKeeperCuratorConfiguration
implements|implements
name|Cloneable
block|{
DECL|field|curatorFramework
specifier|private
name|CuratorFramework
name|curatorFramework
decl_stmt|;
DECL|field|nodes
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|nodes
decl_stmt|;
DECL|field|namespace
specifier|private
name|String
name|namespace
decl_stmt|;
DECL|field|reconnectBaseSleepTime
specifier|private
name|long
name|reconnectBaseSleepTime
decl_stmt|;
DECL|field|reconnectBaseSleepTimeUnit
specifier|private
name|TimeUnit
name|reconnectBaseSleepTimeUnit
decl_stmt|;
DECL|field|reconnectMaxRetries
specifier|private
name|int
name|reconnectMaxRetries
decl_stmt|;
DECL|field|reconnectMaxSleepTime
specifier|private
name|long
name|reconnectMaxSleepTime
decl_stmt|;
DECL|field|reconnectMaxSleepTimeUnit
specifier|private
name|TimeUnit
name|reconnectMaxSleepTimeUnit
decl_stmt|;
DECL|field|sessionTimeout
specifier|private
name|long
name|sessionTimeout
decl_stmt|;
DECL|field|sessionTimeoutUnit
specifier|private
name|TimeUnit
name|sessionTimeoutUnit
decl_stmt|;
DECL|field|connectionTimeout
specifier|private
name|long
name|connectionTimeout
decl_stmt|;
DECL|field|connectionTimeoutUnit
specifier|private
name|TimeUnit
name|connectionTimeoutUnit
decl_stmt|;
DECL|field|authInfoList
specifier|private
name|List
argument_list|<
name|AuthInfo
argument_list|>
name|authInfoList
decl_stmt|;
DECL|field|maxCloseWait
specifier|private
name|long
name|maxCloseWait
decl_stmt|;
DECL|field|maxCloseWaitUnit
specifier|private
name|TimeUnit
name|maxCloseWaitUnit
decl_stmt|;
DECL|field|retryPolicy
specifier|private
name|RetryPolicy
name|retryPolicy
decl_stmt|;
DECL|field|basePath
specifier|private
name|String
name|basePath
decl_stmt|;
DECL|method|ZooKeeperCuratorConfiguration ()
specifier|public
name|ZooKeeperCuratorConfiguration
parameter_list|()
block|{
name|this
operator|.
name|reconnectBaseSleepTime
operator|=
literal|1000
expr_stmt|;
name|this
operator|.
name|reconnectBaseSleepTimeUnit
operator|=
name|TimeUnit
operator|.
name|MILLISECONDS
expr_stmt|;
name|this
operator|.
name|reconnectMaxSleepTime
operator|=
name|Integer
operator|.
name|MAX_VALUE
expr_stmt|;
name|this
operator|.
name|reconnectMaxSleepTimeUnit
operator|=
name|TimeUnit
operator|.
name|MILLISECONDS
expr_stmt|;
name|this
operator|.
name|reconnectMaxRetries
operator|=
literal|3
expr_stmt|;
comment|// from org.apache.curator.framework.CuratorFrameworkFactory
name|this
operator|.
name|sessionTimeout
operator|=
name|Integer
operator|.
name|getInteger
argument_list|(
literal|"curator-default-session-timeout"
argument_list|,
literal|60
operator|*
literal|1000
argument_list|)
expr_stmt|;
name|this
operator|.
name|sessionTimeoutUnit
operator|=
name|TimeUnit
operator|.
name|MILLISECONDS
expr_stmt|;
comment|// from org.apache.curator.framework.CuratorFrameworkFactory
name|this
operator|.
name|connectionTimeout
operator|=
name|Integer
operator|.
name|getInteger
argument_list|(
literal|"curator-default-connection-timeout"
argument_list|,
literal|15
operator|*
literal|1000
argument_list|)
expr_stmt|;
name|this
operator|.
name|connectionTimeoutUnit
operator|=
name|TimeUnit
operator|.
name|MILLISECONDS
expr_stmt|;
comment|// from org.apache.curator.framework.CuratorFrameworkFactory
name|this
operator|.
name|maxCloseWait
operator|=
literal|1000
expr_stmt|;
name|this
operator|.
name|maxCloseWaitUnit
operator|=
name|TimeUnit
operator|.
name|MILLISECONDS
expr_stmt|;
block|}
comment|// *******************************
comment|// Properties
comment|// *******************************
DECL|method|getCuratorFramework ()
specifier|public
name|CuratorFramework
name|getCuratorFramework
parameter_list|()
block|{
return|return
name|curatorFramework
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
name|this
operator|.
name|curatorFramework
operator|=
name|curatorFramework
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
name|nodes
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
name|this
operator|.
name|nodes
operator|=
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|Arrays
operator|.
name|stream
argument_list|(
name|nodes
operator|.
name|split
argument_list|(
literal|","
argument_list|)
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
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
name|this
operator|.
name|nodes
operator|=
name|Collections
operator|.
name|unmodifiableList
argument_list|(
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|nodes
argument_list|)
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
DECL|method|getReconnectBaseSleepTime ()
specifier|public
name|long
name|getReconnectBaseSleepTime
parameter_list|()
block|{
return|return
name|reconnectBaseSleepTime
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
name|this
operator|.
name|reconnectBaseSleepTime
operator|=
name|reconnectBaseSleepTime
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
name|this
operator|.
name|reconnectBaseSleepTime
operator|=
name|reconnectBaseSleepTime
expr_stmt|;
name|this
operator|.
name|reconnectBaseSleepTimeUnit
operator|=
name|reconnectBaseSleepTimeUnit
expr_stmt|;
block|}
DECL|method|getReconnectBaseSleepTimeUnit ()
specifier|public
name|TimeUnit
name|getReconnectBaseSleepTimeUnit
parameter_list|()
block|{
return|return
name|reconnectBaseSleepTimeUnit
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
name|this
operator|.
name|reconnectBaseSleepTimeUnit
operator|=
name|reconnectBaseSleepTimeUnit
expr_stmt|;
block|}
DECL|method|getReconnectMaxSleepTime ()
specifier|public
name|long
name|getReconnectMaxSleepTime
parameter_list|()
block|{
return|return
name|reconnectMaxSleepTime
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
name|this
operator|.
name|reconnectMaxSleepTime
operator|=
name|reconnectMaxSleepTime
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
name|this
operator|.
name|reconnectMaxSleepTime
operator|=
name|reconnectMaxSleepTime
expr_stmt|;
name|this
operator|.
name|reconnectBaseSleepTimeUnit
operator|=
name|reconnectBaseSleepTimeUnit
expr_stmt|;
block|}
DECL|method|getReconnectMaxSleepTimeUnit ()
specifier|public
name|TimeUnit
name|getReconnectMaxSleepTimeUnit
parameter_list|()
block|{
return|return
name|reconnectMaxSleepTimeUnit
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
name|this
operator|.
name|reconnectMaxSleepTimeUnit
operator|=
name|reconnectMaxSleepTimeUnit
expr_stmt|;
block|}
DECL|method|getReconnectMaxRetries ()
specifier|public
name|int
name|getReconnectMaxRetries
parameter_list|()
block|{
return|return
name|reconnectMaxRetries
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
name|this
operator|.
name|reconnectMaxRetries
operator|=
name|reconnectMaxRetries
expr_stmt|;
block|}
DECL|method|getSessionTimeout ()
specifier|public
name|long
name|getSessionTimeout
parameter_list|()
block|{
return|return
name|sessionTimeout
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
name|this
operator|.
name|sessionTimeout
operator|=
name|sessionTimeout
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
name|this
operator|.
name|sessionTimeout
operator|=
name|sessionTimeout
expr_stmt|;
name|this
operator|.
name|sessionTimeoutUnit
operator|=
name|sessionTimeoutUnit
expr_stmt|;
block|}
DECL|method|getSessionTimeoutUnit ()
specifier|public
name|TimeUnit
name|getSessionTimeoutUnit
parameter_list|()
block|{
return|return
name|sessionTimeoutUnit
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
name|this
operator|.
name|sessionTimeoutUnit
operator|=
name|sessionTimeoutUnit
expr_stmt|;
block|}
DECL|method|getConnectionTimeout ()
specifier|public
name|long
name|getConnectionTimeout
parameter_list|()
block|{
return|return
name|connectionTimeout
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
name|this
operator|.
name|connectionTimeout
operator|=
name|connectionTimeout
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
name|this
operator|.
name|connectionTimeout
operator|=
name|connectionTimeout
expr_stmt|;
name|this
operator|.
name|connectionTimeoutUnit
operator|=
name|connectionTimeotUnit
expr_stmt|;
block|}
DECL|method|getConnectionTimeoutUnit ()
specifier|public
name|TimeUnit
name|getConnectionTimeoutUnit
parameter_list|()
block|{
return|return
name|connectionTimeoutUnit
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
name|this
operator|.
name|connectionTimeoutUnit
operator|=
name|connectionTimeoutUnit
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
name|authInfoList
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
name|this
operator|.
name|authInfoList
operator|=
name|authInfoList
expr_stmt|;
block|}
DECL|method|getMaxCloseWait ()
specifier|public
name|long
name|getMaxCloseWait
parameter_list|()
block|{
return|return
name|maxCloseWait
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
name|this
operator|.
name|maxCloseWait
operator|=
name|maxCloseWait
expr_stmt|;
block|}
DECL|method|getMaxCloseWaitUnit ()
specifier|public
name|TimeUnit
name|getMaxCloseWaitUnit
parameter_list|()
block|{
return|return
name|maxCloseWaitUnit
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
name|this
operator|.
name|maxCloseWaitUnit
operator|=
name|maxCloseWaitUnit
expr_stmt|;
block|}
DECL|method|getRetryPolicy ()
specifier|public
name|RetryPolicy
name|getRetryPolicy
parameter_list|()
block|{
return|return
name|retryPolicy
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
name|this
operator|.
name|retryPolicy
operator|=
name|retryPolicy
expr_stmt|;
block|}
DECL|method|getBasePath ()
specifier|public
name|String
name|getBasePath
parameter_list|()
block|{
return|return
name|basePath
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
name|this
operator|.
name|basePath
operator|=
name|basePath
expr_stmt|;
block|}
comment|// *******************************
comment|// Clone
comment|// *******************************
DECL|method|copy ()
specifier|public
name|ZooKeeperCuratorConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|ZooKeeperCuratorConfiguration
operator|)
name|clone
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
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
block|}
end_class

end_unit

