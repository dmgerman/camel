begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.zookeepermaster
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
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|DisposableBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|FactoryBean
import|;
end_import

begin_comment
comment|/**  * Spring {@link FactoryBean} to make using {@link CuratorFramework} easier to setup  * in Spring XML files also.  */
end_comment

begin_class
DECL|class|CuratorFactoryBean
specifier|public
class|class
name|CuratorFactoryBean
implements|implements
name|FactoryBean
argument_list|<
name|CuratorFramework
argument_list|>
implements|,
name|DisposableBean
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|CuratorFactoryBean
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|connectString
specifier|private
name|String
name|connectString
init|=
literal|"localhost:2181"
decl_stmt|;
DECL|field|timeout
specifier|private
name|int
name|timeout
init|=
literal|30000
decl_stmt|;
DECL|field|curator
specifier|private
name|CuratorFramework
name|curator
decl_stmt|;
DECL|method|getConnectString ()
specifier|public
name|String
name|getConnectString
parameter_list|()
block|{
return|return
name|connectString
return|;
block|}
DECL|method|setConnectString (String connectString)
specifier|public
name|void
name|setConnectString
parameter_list|(
name|String
name|connectString
parameter_list|)
block|{
name|this
operator|.
name|connectString
operator|=
name|connectString
expr_stmt|;
block|}
DECL|method|getTimeout ()
specifier|public
name|int
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
DECL|method|setTimeout (int timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|int
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
comment|// FactoryBean interface
comment|//-------------------------------------------------------------------------
DECL|method|getObject ()
specifier|public
name|CuratorFramework
name|getObject
parameter_list|()
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Connecting to ZooKeeper on "
operator|+
name|connectString
argument_list|)
expr_stmt|;
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
name|connectString
argument_list|)
operator|.
name|retryPolicy
argument_list|(
operator|new
name|ExponentialBackoffRetry
argument_list|(
literal|5
argument_list|,
literal|10
argument_list|)
argument_list|)
operator|.
name|connectionTimeoutMs
argument_list|(
name|getTimeout
argument_list|()
argument_list|)
decl_stmt|;
name|this
operator|.
name|curator
operator|=
name|builder
operator|.
name|build
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Starting curator "
operator|+
name|curator
argument_list|)
expr_stmt|;
name|curator
operator|.
name|start
argument_list|()
expr_stmt|;
return|return
name|curator
return|;
block|}
DECL|method|getObjectType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getObjectType
parameter_list|()
block|{
return|return
name|CuratorFramework
operator|.
name|class
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|destroy ()
specifier|public
name|void
name|destroy
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|curator
operator|!=
literal|null
condition|)
block|{
comment|// Note we cannot use zkClient.close()
comment|// since you cannot currently close a client which is not connected
name|curator
operator|.
name|close
argument_list|()
expr_stmt|;
name|curator
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

