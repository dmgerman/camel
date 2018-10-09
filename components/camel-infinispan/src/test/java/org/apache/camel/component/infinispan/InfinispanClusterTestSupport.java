begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.infinispan
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|infinispan
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|JndiRegistry
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|Cache
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|commons
operator|.
name|api
operator|.
name|BasicCacheContainer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|commons
operator|.
name|time
operator|.
name|TimeService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|configuration
operator|.
name|cache
operator|.
name|CacheMode
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|configuration
operator|.
name|cache
operator|.
name|ConfigurationBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|manager
operator|.
name|EmbeddedCacheManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|test
operator|.
name|MultipleCacheManagersTest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|test
operator|.
name|TestingUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|transaction
operator|.
name|TransactionMode
import|;
end_import

begin_import
import|import
name|org
operator|.
name|infinispan
operator|.
name|util
operator|.
name|ControlledTimeService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_class
DECL|class|InfinispanClusterTestSupport
specifier|public
class|class
name|InfinispanClusterTestSupport
extends|extends
name|CamelTestSupport
block|{
DECL|field|KEY_ONE
specifier|protected
specifier|static
specifier|final
name|String
name|KEY_ONE
init|=
literal|"keyOne"
decl_stmt|;
DECL|field|VALUE_ONE
specifier|protected
specifier|static
specifier|final
name|String
name|VALUE_ONE
init|=
literal|"valueOne"
decl_stmt|;
DECL|field|clusteredCacheContainers
specifier|protected
name|List
argument_list|<
name|EmbeddedCacheManager
argument_list|>
name|clusteredCacheContainers
decl_stmt|;
DECL|field|ts0
specifier|protected
name|ControlledTimeService
name|ts0
decl_stmt|;
DECL|field|ts1
specifier|protected
name|ControlledTimeService
name|ts1
decl_stmt|;
DECL|class|ClusteredCacheSupport
specifier|protected
specifier|static
class|class
name|ClusteredCacheSupport
extends|extends
name|MultipleCacheManagersTest
block|{
DECL|field|builderUsed
specifier|protected
name|ConfigurationBuilder
name|builderUsed
decl_stmt|;
DECL|field|tx
specifier|protected
specifier|final
name|boolean
name|tx
decl_stmt|;
DECL|field|cacheMode
specifier|protected
specifier|final
name|CacheMode
name|cacheMode
decl_stmt|;
DECL|field|cacheName
specifier|protected
name|String
name|cacheName
decl_stmt|;
DECL|field|clusterSize
specifier|protected
specifier|final
name|int
name|clusterSize
decl_stmt|;
DECL|method|ClusteredCacheSupport (CacheMode cacheMode, boolean tx, int clusterSize)
specifier|public
name|ClusteredCacheSupport
parameter_list|(
name|CacheMode
name|cacheMode
parameter_list|,
name|boolean
name|tx
parameter_list|,
name|int
name|clusterSize
parameter_list|)
block|{
name|this
operator|.
name|tx
operator|=
name|tx
expr_stmt|;
name|this
operator|.
name|cacheMode
operator|=
name|cacheMode
expr_stmt|;
name|this
operator|.
name|clusterSize
operator|=
name|clusterSize
expr_stmt|;
block|}
DECL|method|ClusteredCacheSupport (CacheMode cacheMode, String cacheName, boolean tx, int clusterSize)
specifier|public
name|ClusteredCacheSupport
parameter_list|(
name|CacheMode
name|cacheMode
parameter_list|,
name|String
name|cacheName
parameter_list|,
name|boolean
name|tx
parameter_list|,
name|int
name|clusterSize
parameter_list|)
block|{
name|this
operator|.
name|tx
operator|=
name|tx
expr_stmt|;
name|this
operator|.
name|cacheMode
operator|=
name|cacheMode
expr_stmt|;
name|this
operator|.
name|cacheName
operator|=
name|cacheName
expr_stmt|;
name|this
operator|.
name|clusterSize
operator|=
name|clusterSize
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createCacheManagers ()
specifier|public
name|void
name|createCacheManagers
parameter_list|()
throws|throws
name|Throwable
block|{
name|builderUsed
operator|=
operator|new
name|ConfigurationBuilder
argument_list|()
expr_stmt|;
name|builderUsed
operator|.
name|clustering
argument_list|()
operator|.
name|cacheMode
argument_list|(
name|cacheMode
argument_list|)
expr_stmt|;
if|if
condition|(
name|tx
condition|)
block|{
name|builderUsed
operator|.
name|transaction
argument_list|()
operator|.
name|transactionMode
argument_list|(
name|TransactionMode
operator|.
name|TRANSACTIONAL
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cacheMode
operator|.
name|isDistributed
argument_list|()
condition|)
block|{
name|builderUsed
operator|.
name|clustering
argument_list|()
operator|.
name|hash
argument_list|()
operator|.
name|numOwners
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cacheName
operator|!=
literal|null
condition|)
block|{
name|createClusteredCaches
argument_list|(
name|clusterSize
argument_list|,
name|cacheName
argument_list|,
name|builderUsed
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|createClusteredCaches
argument_list|(
name|clusterSize
argument_list|,
name|builderUsed
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|ClusteredCacheSupport
name|cluster
init|=
operator|new
name|ClusteredCacheSupport
argument_list|(
name|CacheMode
operator|.
name|DIST_SYNC
argument_list|,
literal|false
argument_list|,
literal|2
argument_list|)
decl_stmt|;
try|try
block|{
name|cluster
operator|.
name|createCacheManagers
argument_list|()
expr_stmt|;
name|clusteredCacheContainers
operator|=
name|cluster
operator|.
name|getCacheManagers
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
name|ex
argument_list|)
throw|;
block|}
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
comment|// Has to be done later, maybe CamelTestSupport should
for|for
control|(
name|BasicCacheContainer
name|container
range|:
name|clusteredCacheContainers
control|)
block|{
name|container
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|registry
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"cacheContainer"
argument_list|,
name|clusteredCacheContainers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
DECL|method|defaultCache ()
specifier|protected
name|Cache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|defaultCache
parameter_list|()
block|{
return|return
name|clusteredCacheContainers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getCache
argument_list|()
return|;
block|}
DECL|method|defaultCache (int index)
specifier|protected
name|Cache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|defaultCache
parameter_list|(
name|int
name|index
parameter_list|)
block|{
return|return
name|clusteredCacheContainers
operator|.
name|get
argument_list|(
name|index
argument_list|)
operator|.
name|getCache
argument_list|()
return|;
block|}
DECL|method|namedCache (String name)
specifier|protected
name|Cache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|namedCache
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|clusteredCacheContainers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getCache
argument_list|(
name|name
argument_list|)
return|;
block|}
DECL|method|namedCache (int index, String name)
specifier|protected
name|Cache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|namedCache
parameter_list|(
name|int
name|index
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
name|clusteredCacheContainers
operator|.
name|get
argument_list|(
name|index
argument_list|)
operator|.
name|getCache
argument_list|(
name|name
argument_list|)
return|;
block|}
DECL|method|injectTimeService ()
specifier|protected
name|void
name|injectTimeService
parameter_list|()
block|{
name|ts0
operator|=
operator|new
name|ControlledTimeService
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|TestingUtil
operator|.
name|replaceComponent
argument_list|(
name|clusteredCacheContainers
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|TimeService
operator|.
name|class
argument_list|,
name|ts0
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|ts1
operator|=
operator|new
name|ControlledTimeService
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|TestingUtil
operator|.
name|replaceComponent
argument_list|(
name|clusteredCacheContainers
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|,
name|TimeService
operator|.
name|class
argument_list|,
name|ts1
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

