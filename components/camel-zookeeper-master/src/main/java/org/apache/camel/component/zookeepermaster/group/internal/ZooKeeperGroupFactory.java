begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.zookeepermaster.group.internal
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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ThreadFactory
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
name|Group
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
name|GroupFactory
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
name|NodeState
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

begin_comment
comment|/**  */
end_comment

begin_class
DECL|class|ZooKeeperGroupFactory
specifier|public
class|class
name|ZooKeeperGroupFactory
implements|implements
name|GroupFactory
block|{
DECL|field|curator
specifier|private
name|CuratorFramework
name|curator
decl_stmt|;
DECL|method|ZooKeeperGroupFactory (CuratorFramework curator)
specifier|public
name|ZooKeeperGroupFactory
parameter_list|(
name|CuratorFramework
name|curator
parameter_list|)
block|{
name|this
operator|.
name|curator
operator|=
name|curator
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createGroup (String path, Class<T> clazz)
specifier|public
parameter_list|<
name|T
extends|extends
name|NodeState
parameter_list|>
name|Group
argument_list|<
name|T
argument_list|>
name|createGroup
parameter_list|(
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
return|return
operator|new
name|ZooKeeperGroup
argument_list|<>
argument_list|(
name|curator
argument_list|,
name|path
argument_list|,
name|clazz
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createGroup (String path, Class<T> clazz, ThreadFactory threadFactory)
specifier|public
parameter_list|<
name|T
extends|extends
name|NodeState
parameter_list|>
name|Group
argument_list|<
name|T
argument_list|>
name|createGroup
parameter_list|(
name|String
name|path
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|,
name|ThreadFactory
name|threadFactory
parameter_list|)
block|{
return|return
operator|new
name|ZooKeeperGroup
argument_list|<>
argument_list|(
name|curator
argument_list|,
name|path
argument_list|,
name|clazz
argument_list|,
name|threadFactory
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createMultiGroup (String path, Class<T> clazz)
specifier|public
parameter_list|<
name|T
extends|extends
name|NodeState
parameter_list|>
name|Group
argument_list|<
name|T
argument_list|>
name|createMultiGroup
parameter_list|(
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
return|return
operator|new
name|ZooKeeperMultiGroup
argument_list|<>
argument_list|(
name|curator
argument_list|,
name|path
argument_list|,
name|clazz
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createMultiGroup (String path, Class<T> clazz, ThreadFactory threadFactory)
specifier|public
parameter_list|<
name|T
extends|extends
name|NodeState
parameter_list|>
name|Group
argument_list|<
name|T
argument_list|>
name|createMultiGroup
parameter_list|(
name|String
name|path
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|,
name|ThreadFactory
name|threadFactory
parameter_list|)
block|{
return|return
operator|new
name|ZooKeeperMultiGroup
argument_list|<>
argument_list|(
name|curator
argument_list|,
name|path
argument_list|,
name|clazz
argument_list|,
name|threadFactory
argument_list|)
return|;
block|}
block|}
end_class

end_unit

