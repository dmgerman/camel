begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Callable
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
name|ClassResolver
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
name|support
operator|.
name|IntrospectionSupport
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
DECL|class|ManagedGroupFactoryBuilder
specifier|public
specifier|final
class|class
name|ManagedGroupFactoryBuilder
block|{
DECL|method|ManagedGroupFactoryBuilder ()
specifier|private
name|ManagedGroupFactoryBuilder
parameter_list|()
block|{     }
DECL|method|create (CuratorFramework curator, ClassLoader loader, ClassResolver resolver, Callable<CuratorFramework> factory)
specifier|public
specifier|static
name|ManagedGroupFactory
name|create
parameter_list|(
name|CuratorFramework
name|curator
parameter_list|,
name|ClassLoader
name|loader
parameter_list|,
name|ClassResolver
name|resolver
parameter_list|,
name|Callable
argument_list|<
name|CuratorFramework
argument_list|>
name|factory
parameter_list|)
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
return|return
operator|new
name|StaticManagedGroupFactory
argument_list|(
name|curator
argument_list|,
literal|false
argument_list|)
return|;
block|}
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|resolver
operator|.
name|resolveClass
argument_list|(
literal|"org.apache.camel.component.zookeepermaster.group.internal.osgi.OsgiManagedGroupFactory"
argument_list|)
decl_stmt|;
if|if
condition|(
name|clazz
operator|!=
literal|null
condition|)
block|{
name|Object
name|instance
init|=
name|clazz
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|IntrospectionSupport
operator|.
name|setProperty
argument_list|(
name|instance
argument_list|,
literal|"classLoader"
argument_list|,
name|loader
argument_list|)
expr_stmt|;
return|return
operator|(
name|ManagedGroupFactory
operator|)
name|instance
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// Ignore if we'e not in OSGi
block|}
return|return
operator|new
name|StaticManagedGroupFactory
argument_list|(
name|factory
operator|.
name|call
argument_list|()
argument_list|,
literal|true
argument_list|)
return|;
block|}
block|}
end_class

end_unit

