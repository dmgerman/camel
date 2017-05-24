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
name|DefaultCamelContext
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
name|SimpleRegistry
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
name|Status
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
name|xml
operator|.
name|XmlConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
DECL|class|EhcacheManagerTest
specifier|public
class|class
name|EhcacheManagerTest
block|{
annotation|@
name|Test
DECL|method|testCacheManagerFromFile ()
specifier|public
name|void
name|testCacheManagerFromFile
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
literal|null
decl_stmt|;
try|try
block|{
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:ehcache"
argument_list|)
operator|.
name|to
argument_list|(
literal|"ehcache:myCache1?configurationUri=classpath:ehcache/ehcache-file-config.xml"
argument_list|)
operator|.
name|to
argument_list|(
literal|"ehcache:myCache2?configurationUri=classpath:ehcache/ehcache-file-config.xml"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|EhcacheEndpoint
name|e1
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"ehcache:myCache1?configurationUri=classpath:ehcache/ehcache-file-config.xml"
argument_list|,
name|EhcacheEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|EhcacheEndpoint
name|e2
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"ehcache:myCache2?configurationUri=classpath:ehcache/ehcache-file-config.xml"
argument_list|,
name|EhcacheEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|e1
operator|.
name|getManager
argument_list|()
argument_list|,
name|e2
operator|.
name|getManager
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|e1
operator|.
name|getManager
argument_list|()
operator|.
name|getCacheManager
argument_list|()
argument_list|,
name|e2
operator|.
name|getManager
argument_list|()
operator|.
name|getCacheManager
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|e1
operator|.
name|getManager
argument_list|()
operator|.
name|getReferenceCount
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|Status
operator|.
name|AVAILABLE
argument_list|,
name|e1
operator|.
name|getManager
argument_list|()
operator|.
name|getCacheManager
argument_list|()
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|e1
operator|.
name|getManager
argument_list|()
argument_list|,
name|e2
operator|.
name|getManager
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|e1
operator|.
name|getManager
argument_list|()
operator|.
name|getCacheManager
argument_list|()
argument_list|,
name|e2
operator|.
name|getManager
argument_list|()
operator|.
name|getCacheManager
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|e1
operator|.
name|getManager
argument_list|()
operator|.
name|getReferenceCount
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|Status
operator|.
name|UNINITIALIZED
argument_list|,
name|e1
operator|.
name|getManager
argument_list|()
operator|.
name|getCacheManager
argument_list|()
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Test
DECL|method|testCacheManagerFromConfiguration ()
specifier|public
name|void
name|testCacheManagerFromConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
literal|null
decl_stmt|;
try|try
block|{
name|SimpleRegistry
name|registry
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|put
argument_list|(
literal|"myConf"
argument_list|,
operator|new
name|XmlConfiguration
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/ehcache/ehcache-file-config.xml"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|(
name|registry
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:ehcache"
argument_list|)
operator|.
name|to
argument_list|(
literal|"ehcache:myCache1?cacheManagerConfiguration=#myConf"
argument_list|)
operator|.
name|to
argument_list|(
literal|"ehcache:myCache2?cacheManagerConfiguration=#myConf"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|EhcacheEndpoint
name|e1
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"ehcache:myCache1?cacheManagerConfiguration=#myConf"
argument_list|,
name|EhcacheEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|EhcacheEndpoint
name|e2
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"ehcache:myCache2?cacheManagerConfiguration=#myConf"
argument_list|,
name|EhcacheEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|e1
operator|.
name|getManager
argument_list|()
argument_list|,
name|e2
operator|.
name|getManager
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|e1
operator|.
name|getManager
argument_list|()
operator|.
name|getCacheManager
argument_list|()
argument_list|,
name|e2
operator|.
name|getManager
argument_list|()
operator|.
name|getCacheManager
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|e1
operator|.
name|getManager
argument_list|()
operator|.
name|getReferenceCount
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|Status
operator|.
name|AVAILABLE
argument_list|,
name|e1
operator|.
name|getManager
argument_list|()
operator|.
name|getCacheManager
argument_list|()
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|e1
operator|.
name|getManager
argument_list|()
argument_list|,
name|e2
operator|.
name|getManager
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|e1
operator|.
name|getManager
argument_list|()
operator|.
name|getCacheManager
argument_list|()
argument_list|,
name|e2
operator|.
name|getManager
argument_list|()
operator|.
name|getCacheManager
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|e1
operator|.
name|getManager
argument_list|()
operator|.
name|getReferenceCount
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|Status
operator|.
name|UNINITIALIZED
argument_list|,
name|e1
operator|.
name|getManager
argument_list|()
operator|.
name|getCacheManager
argument_list|()
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
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
name|CamelContext
name|context
init|=
literal|null
decl_stmt|;
name|CacheManager
name|cacheManager
init|=
literal|null
decl_stmt|;
try|try
block|{
name|cacheManager
operator|=
name|CacheManagerBuilder
operator|.
name|newCacheManagerBuilder
argument_list|()
operator|.
name|build
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|SimpleRegistry
name|registry
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|put
argument_list|(
literal|"myManager"
argument_list|,
name|cacheManager
argument_list|)
expr_stmt|;
name|context
operator|=
operator|new
name|DefaultCamelContext
argument_list|(
name|registry
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:ehcache"
argument_list|)
operator|.
name|to
argument_list|(
literal|"ehcache:myCache1?cacheManager=#myManager"
argument_list|)
operator|.
name|to
argument_list|(
literal|"ehcache:myCache2?cacheManager=#myManager"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|EhcacheEndpoint
name|e1
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"ehcache:myCache1?cacheManager=#myManager"
argument_list|,
name|EhcacheEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|EhcacheEndpoint
name|e2
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"ehcache:myCache2?cacheManager=#myManager"
argument_list|,
name|EhcacheEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|e1
operator|.
name|getManager
argument_list|()
argument_list|,
name|e2
operator|.
name|getManager
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|e1
operator|.
name|getManager
argument_list|()
operator|.
name|getCacheManager
argument_list|()
argument_list|,
name|e2
operator|.
name|getManager
argument_list|()
operator|.
name|getCacheManager
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|e1
operator|.
name|getManager
argument_list|()
operator|.
name|getReferenceCount
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|Status
operator|.
name|AVAILABLE
argument_list|,
name|e1
operator|.
name|getManager
argument_list|()
operator|.
name|getCacheManager
argument_list|()
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|e1
operator|.
name|getManager
argument_list|()
argument_list|,
name|e2
operator|.
name|getManager
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|e1
operator|.
name|getManager
argument_list|()
operator|.
name|getCacheManager
argument_list|()
argument_list|,
name|e2
operator|.
name|getManager
argument_list|()
operator|.
name|getCacheManager
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|e1
operator|.
name|getManager
argument_list|()
operator|.
name|getReferenceCount
argument_list|()
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|Status
operator|.
name|AVAILABLE
argument_list|,
name|e1
operator|.
name|getManager
argument_list|()
operator|.
name|getCacheManager
argument_list|()
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|cacheManager
operator|!=
literal|null
condition|)
block|{
name|cacheManager
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

