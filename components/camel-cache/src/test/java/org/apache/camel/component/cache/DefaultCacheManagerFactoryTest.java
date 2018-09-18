begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cache
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cache
package|;
end_package

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|CacheManager
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|Status
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|config
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|ehcache
operator|.
name|config
operator|.
name|ConfigurationFactory
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

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|is
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
operator|.
name|nullValue
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
DECL|class|DefaultCacheManagerFactoryTest
specifier|public
class|class
name|DefaultCacheManagerFactoryTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|testEHCacheCompatiblity ()
specifier|public
name|void
name|testEHCacheCompatiblity
parameter_list|()
throws|throws
name|Exception
block|{
comment|// get the default cache manager
name|CacheManagerFactory
name|factory
init|=
operator|new
name|DefaultCacheManagerFactory
argument_list|()
decl_stmt|;
name|CacheManager
name|manager
init|=
name|factory
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|STATUS_ALIVE
argument_list|,
name|manager
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
comment|// create another unrelated cache manager
name|Configuration
name|conf
init|=
name|ConfigurationFactory
operator|.
name|parseConfiguration
argument_list|(
name|DefaultCacheManagerFactory
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"/test-ehcache.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|conf
argument_list|)
expr_stmt|;
name|conf
operator|.
name|setName
argument_list|(
literal|"otherCache"
argument_list|)
expr_stmt|;
name|CacheManager
name|other
init|=
name|CacheManager
operator|.
name|create
argument_list|(
name|conf
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|STATUS_ALIVE
argument_list|,
name|other
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
comment|// shutdown this unrelated cache manager
name|other
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|Status
operator|.
name|STATUS_SHUTDOWN
argument_list|,
name|other
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
comment|// the default cache manager should be still running
name|assertEquals
argument_list|(
name|Status
operator|.
name|STATUS_ALIVE
argument_list|,
name|manager
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|doStop
argument_list|()
expr_stmt|;
comment|// the default cache manger is shutdown
name|assertEquals
argument_list|(
name|Status
operator|.
name|STATUS_SHUTDOWN
argument_list|,
name|manager
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNoProvidedConfiguration ()
specifier|public
name|void
name|testNoProvidedConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|CacheManagerFactory
name|factory
init|=
operator|new
name|DefaultCacheManagerFactory
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/ehcache.xml"
argument_list|)
argument_list|,
literal|"/ehcache.xml"
argument_list|)
decl_stmt|;
name|CacheManager
name|manager
init|=
name|factory
operator|.
name|getInstance
argument_list|()
decl_stmt|;
comment|// CAMEL-7195
name|assertThat
argument_list|(
literal|"There should be no peer providers configured"
argument_list|,
name|manager
operator|.
name|getCacheManagerPeerProviders
argument_list|()
operator|.
name|size
argument_list|()
argument_list|,
name|is
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
literal|"There should be no /ehcache.xml resource by default"
argument_list|,
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/ehcache.xml"
argument_list|)
argument_list|,
name|nullValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFailSafeEHCacheManager ()
specifier|public
name|void
name|testFailSafeEHCacheManager
parameter_list|()
throws|throws
name|Exception
block|{
name|CacheManagerFactory
name|factory1
init|=
operator|new
name|DefaultCacheManagerFactory
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|CacheManagerFactory
name|factory2
init|=
operator|new
name|DefaultCacheManagerFactory
argument_list|()
decl_stmt|;
name|assertSame
argument_list|(
literal|"The cache managers should be the same, loaded from fallback ehcache config"
argument_list|,
name|factory1
operator|.
name|getInstance
argument_list|()
argument_list|,
name|factory2
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

