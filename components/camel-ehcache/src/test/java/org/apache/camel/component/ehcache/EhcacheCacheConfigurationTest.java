begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ehcache
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ehcache
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
name|EndpointInject
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
name|builder
operator|.
name|RouteBuilder
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
name|ehcache
operator|.
name|Cache
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ehcache
operator|.
name|CacheManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ehcache
operator|.
name|UserManagedCache
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ehcache
operator|.
name|config
operator|.
name|ResourcePools
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ehcache
operator|.
name|config
operator|.
name|ResourceType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ehcache
operator|.
name|config
operator|.
name|SizedResourcePool
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ehcache
operator|.
name|config
operator|.
name|builders
operator|.
name|CacheConfigurationBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ehcache
operator|.
name|config
operator|.
name|builders
operator|.
name|CacheManagerBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ehcache
operator|.
name|config
operator|.
name|builders
operator|.
name|ResourcePoolsBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ehcache
operator|.
name|config
operator|.
name|units
operator|.
name|EntryUnit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ehcache
operator|.
name|config
operator|.
name|units
operator|.
name|MemoryUnit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|EhcacheCacheConfigurationTest
specifier|public
class|class
name|EhcacheCacheConfigurationTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
literal|"ehcache:myProgrammaticCacheConf?configuration=#myProgrammaticConfiguration"
argument_list|)
DECL|field|ehcacheProgrammaticConf
specifier|private
name|EhcacheEndpoint
name|ehcacheProgrammaticConf
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"ehcache:myFileCacheConf?keyType=java.lang.String&valueType=java.lang.String&configurationUri=classpath:ehcache/ehcache-file-config.xml"
argument_list|)
DECL|field|ehcacheFileConf
specifier|private
name|EhcacheEndpoint
name|ehcacheFileConf
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"ehcache:myUserCacheConf"
argument_list|)
DECL|field|ehcacheUserConf
specifier|private
name|EhcacheEndpoint
name|ehcacheUserConf
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"ehcache:myCache?cacheManager=#myCacheManager&keyType=java.lang.String&valueType=java.lang.String"
argument_list|)
DECL|field|ehcacheCacheManager
specifier|private
name|EhcacheEndpoint
name|ehcacheCacheManager
decl_stmt|;
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
literal|"myProgrammaticConfiguration"
argument_list|,
name|CacheConfigurationBuilder
operator|.
name|newCacheConfigurationBuilder
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|ResourcePoolsBuilder
operator|.
name|newResourcePoolsBuilder
argument_list|()
operator|.
name|heap
argument_list|(
literal|100
argument_list|,
name|EntryUnit
operator|.
name|ENTRIES
argument_list|)
operator|.
name|offheap
argument_list|(
literal|1
argument_list|,
name|MemoryUnit
operator|.
name|MB
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"myCacheManager"
argument_list|,
name|CacheManagerBuilder
operator|.
name|newCacheManagerBuilder
argument_list|()
operator|.
name|withCache
argument_list|(
literal|"myCache"
argument_list|,
name|CacheConfigurationBuilder
operator|.
name|newCacheConfigurationBuilder
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|ResourcePoolsBuilder
operator|.
name|newResourcePoolsBuilder
argument_list|()
operator|.
name|heap
argument_list|(
literal|100
argument_list|,
name|EntryUnit
operator|.
name|ENTRIES
argument_list|)
operator|.
name|offheap
argument_list|(
literal|1
argument_list|,
name|MemoryUnit
operator|.
name|MB
argument_list|)
argument_list|)
argument_list|)
operator|.
name|build
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
comment|// *****************************
comment|// Test
comment|// *****************************
annotation|@
name|Test
DECL|method|testProgrammaticConfiguration ()
specifier|public
name|void
name|testProgrammaticConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|Cache
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|cache
init|=
name|getCache
argument_list|(
name|ehcacheProgrammaticConf
argument_list|,
literal|"myProgrammaticCacheConf"
argument_list|)
decl_stmt|;
name|ResourcePools
name|pools
init|=
name|cache
operator|.
name|getRuntimeConfiguration
argument_list|()
operator|.
name|getResourcePools
argument_list|()
decl_stmt|;
name|SizedResourcePool
name|h
init|=
name|pools
operator|.
name|getPoolForResource
argument_list|(
name|ResourceType
operator|.
name|Core
operator|.
name|HEAP
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|h
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|100
argument_list|,
name|h
operator|.
name|getSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|EntryUnit
operator|.
name|ENTRIES
argument_list|,
name|h
operator|.
name|getUnit
argument_list|()
argument_list|)
expr_stmt|;
name|SizedResourcePool
name|o
init|=
name|pools
operator|.
name|getPoolForResource
argument_list|(
name|ResourceType
operator|.
name|Core
operator|.
name|OFFHEAP
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|o
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|o
operator|.
name|getSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|MemoryUnit
operator|.
name|MB
argument_list|,
name|o
operator|.
name|getUnit
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFileConfiguration ()
specifier|public
name|void
name|testFileConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|Cache
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|cache
init|=
name|getCache
argument_list|(
name|ehcacheFileConf
argument_list|,
literal|"myFileCacheConf"
argument_list|)
decl_stmt|;
name|ResourcePools
name|pools
init|=
name|cache
operator|.
name|getRuntimeConfiguration
argument_list|()
operator|.
name|getResourcePools
argument_list|()
decl_stmt|;
name|SizedResourcePool
name|h
init|=
name|pools
operator|.
name|getPoolForResource
argument_list|(
name|ResourceType
operator|.
name|Core
operator|.
name|HEAP
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|h
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|150
argument_list|,
name|h
operator|.
name|getSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|EntryUnit
operator|.
name|ENTRIES
argument_list|,
name|h
operator|.
name|getUnit
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUserConfiguration ()
specifier|public
name|void
name|testUserConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|fluentTemplate
argument_list|()
operator|.
name|withHeader
argument_list|(
name|EhcacheConstants
operator|.
name|ACTION
argument_list|,
name|EhcacheConstants
operator|.
name|ACTION_PUT
argument_list|)
operator|.
name|withHeader
argument_list|(
name|EhcacheConstants
operator|.
name|KEY
argument_list|,
literal|"user-key"
argument_list|)
operator|.
name|withBody
argument_list|(
literal|"user-val"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:ehcacheUserConf"
argument_list|)
operator|.
name|send
argument_list|()
expr_stmt|;
name|Cache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|cache
init|=
name|ehcacheUserConf
operator|.
name|getManager
argument_list|()
operator|.
name|getCache
argument_list|(
literal|"myUserCacheConf"
argument_list|,
name|Object
operator|.
name|class
argument_list|,
name|Object
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|cache
operator|instanceof
name|UserManagedCache
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"user-val"
argument_list|,
name|cache
operator|.
name|get
argument_list|(
literal|"user-key"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCacheManager ()
specifier|public
name|void
name|testCacheManager
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
name|context
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
literal|"myCacheManager"
argument_list|,
name|CacheManager
operator|.
name|class
argument_list|)
argument_list|,
name|ehcacheCacheManager
operator|.
name|getManager
argument_list|()
operator|.
name|getCacheManager
argument_list|()
argument_list|)
expr_stmt|;
name|Cache
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|cache
init|=
name|getCache
argument_list|(
name|ehcacheCacheManager
argument_list|,
literal|"myCache"
argument_list|)
decl_stmt|;
name|ResourcePools
name|pools
init|=
name|cache
operator|.
name|getRuntimeConfiguration
argument_list|()
operator|.
name|getResourcePools
argument_list|()
decl_stmt|;
name|SizedResourcePool
name|h
init|=
name|pools
operator|.
name|getPoolForResource
argument_list|(
name|ResourceType
operator|.
name|Core
operator|.
name|HEAP
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|h
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|100
argument_list|,
name|h
operator|.
name|getSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|EntryUnit
operator|.
name|ENTRIES
argument_list|,
name|h
operator|.
name|getUnit
argument_list|()
argument_list|)
expr_stmt|;
name|SizedResourcePool
name|o
init|=
name|pools
operator|.
name|getPoolForResource
argument_list|(
name|ResourceType
operator|.
name|Core
operator|.
name|OFFHEAP
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|o
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|o
operator|.
name|getSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|MemoryUnit
operator|.
name|MB
argument_list|,
name|o
operator|.
name|getUnit
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getCache (EhcacheEndpoint endpoint, String cacheName)
specifier|protected
name|Cache
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getCache
parameter_list|(
name|EhcacheEndpoint
name|endpoint
parameter_list|,
name|String
name|cacheName
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|endpoint
operator|.
name|getManager
argument_list|()
operator|.
name|getCache
argument_list|(
name|cacheName
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
comment|// ****************************
comment|// Route
comment|// ****************************
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"direct:ehcacheProgrammaticConf"
argument_list|)
operator|.
name|to
argument_list|(
name|ehcacheProgrammaticConf
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:ehcacheFileConf"
argument_list|)
operator|.
name|to
argument_list|(
name|ehcacheFileConf
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:ehcacheUserConf"
argument_list|)
operator|.
name|to
argument_list|(
name|ehcacheUserConf
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:ehcacheCacheManager"
argument_list|)
operator|.
name|to
argument_list|(
name|ehcacheCacheManager
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

