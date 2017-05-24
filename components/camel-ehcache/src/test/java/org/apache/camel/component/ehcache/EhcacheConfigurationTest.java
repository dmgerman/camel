begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|Collections
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
name|config
operator|.
name|CacheRuntimeConfiguration
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
name|ehcache
operator|.
name|core
operator|.
name|Ehcache
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
DECL|class|EhcacheConfigurationTest
specifier|public
class|class
name|EhcacheConfigurationTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"ehcache:globalConfig"
argument_list|)
DECL|field|globalConfig
name|EhcacheEndpoint
name|globalConfig
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"ehcache:customConfig"
argument_list|)
DECL|field|customConfig
name|EhcacheEndpoint
name|customConfig
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
name|EhcacheComponent
name|component
init|=
operator|new
name|EhcacheComponent
argument_list|()
decl_stmt|;
name|component
operator|.
name|setCacheConfiguration
argument_list|(
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
name|component
operator|.
name|setCachesConfigurations
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"customConfig"
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
literal|200
argument_list|,
name|EntryUnit
operator|.
name|ENTRIES
argument_list|)
operator|.
name|offheap
argument_list|(
literal|2
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
argument_list|)
expr_stmt|;
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
literal|"ehcache"
argument_list|,
name|component
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
DECL|method|testConfiguration ()
specifier|public
name|void
name|testConfiguration
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
name|globalConfigCache
init|=
name|globalConfig
operator|.
name|getManager
argument_list|()
operator|.
name|getCache
argument_list|(
literal|"globalConfig"
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Cache
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|customConfigCache
init|=
name|customConfig
operator|.
name|getManager
argument_list|()
operator|.
name|getCache
argument_list|(
literal|"customConfig"
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|globalConfigCache
operator|instanceof
name|Ehcache
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|customConfigCache
operator|instanceof
name|Ehcache
argument_list|)
expr_stmt|;
name|CacheRuntimeConfiguration
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|gc
init|=
name|globalConfigCache
operator|.
name|getRuntimeConfiguration
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|100
argument_list|,
name|gc
operator|.
name|getResourcePools
argument_list|()
operator|.
name|getPoolForResource
argument_list|(
name|ResourceType
operator|.
name|Core
operator|.
name|HEAP
argument_list|)
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
name|gc
operator|.
name|getResourcePools
argument_list|()
operator|.
name|getPoolForResource
argument_list|(
name|ResourceType
operator|.
name|Core
operator|.
name|HEAP
argument_list|)
operator|.
name|getUnit
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|gc
operator|.
name|getResourcePools
argument_list|()
operator|.
name|getPoolForResource
argument_list|(
name|ResourceType
operator|.
name|Core
operator|.
name|OFFHEAP
argument_list|)
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
name|gc
operator|.
name|getResourcePools
argument_list|()
operator|.
name|getPoolForResource
argument_list|(
name|ResourceType
operator|.
name|Core
operator|.
name|OFFHEAP
argument_list|)
operator|.
name|getUnit
argument_list|()
argument_list|)
expr_stmt|;
name|CacheRuntimeConfiguration
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|cc
init|=
name|customConfigCache
operator|.
name|getRuntimeConfiguration
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|200
argument_list|,
name|cc
operator|.
name|getResourcePools
argument_list|()
operator|.
name|getPoolForResource
argument_list|(
name|ResourceType
operator|.
name|Core
operator|.
name|HEAP
argument_list|)
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
name|cc
operator|.
name|getResourcePools
argument_list|()
operator|.
name|getPoolForResource
argument_list|(
name|ResourceType
operator|.
name|Core
operator|.
name|HEAP
argument_list|)
operator|.
name|getUnit
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|cc
operator|.
name|getResourcePools
argument_list|()
operator|.
name|getPoolForResource
argument_list|(
name|ResourceType
operator|.
name|Core
operator|.
name|OFFHEAP
argument_list|)
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
name|cc
operator|.
name|getResourcePools
argument_list|()
operator|.
name|getPoolForResource
argument_list|(
name|ResourceType
operator|.
name|Core
operator|.
name|OFFHEAP
argument_list|)
operator|.
name|getUnit
argument_list|()
argument_list|)
expr_stmt|;
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
literal|"direct:ehcache"
argument_list|)
operator|.
name|to
argument_list|(
name|globalConfig
argument_list|)
operator|.
name|to
argument_list|(
name|customConfig
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

