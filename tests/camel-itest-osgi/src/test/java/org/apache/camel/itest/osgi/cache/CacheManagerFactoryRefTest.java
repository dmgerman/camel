begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.itest.osgi.cache
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|itest
operator|.
name|osgi
operator|.
name|cache
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|naming
operator|.
name|Context
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
name|Cache
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
name|CacheManager
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
name|component
operator|.
name|cache
operator|.
name|CacheConstants
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
name|cache
operator|.
name|CacheEndpoint
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
name|cache
operator|.
name|CacheManagerFactory
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
name|itest
operator|.
name|osgi
operator|.
name|OSGiIntegrationTestSupport
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
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|Option
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|junit
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|junit
operator|.
name|JUnit4TestRunner
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|OptionUtils
operator|.
name|combine
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|container
operator|.
name|def
operator|.
name|PaxRunnerOptions
operator|.
name|scanFeatures
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|JUnit4TestRunner
operator|.
name|class
argument_list|)
DECL|class|CacheManagerFactoryRefTest
specifier|public
class|class
name|CacheManagerFactoryRefTest
extends|extends
name|OSGiIntegrationTestSupport
block|{
DECL|field|CACHE_URI
specifier|private
specifier|static
specifier|final
name|String
name|CACHE_URI
init|=
literal|"cache:foo?cacheManagerFactory=#cacheManagerFactory"
decl_stmt|;
DECL|field|cmfRef
specifier|private
name|TestingCacheManagerFactory
name|cmfRef
init|=
operator|new
name|TestingCacheManagerFactory
argument_list|(
literal|"ehcache_test.xml"
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|createJndiContext ()
specifier|protected
name|Context
name|createJndiContext
parameter_list|()
throws|throws
name|Exception
block|{
name|Context
name|ctx
init|=
name|super
operator|.
name|createJndiContext
argument_list|()
decl_stmt|;
name|ctx
operator|.
name|bind
argument_list|(
literal|"cacheManagerFactory"
argument_list|,
name|cmfRef
argument_list|)
expr_stmt|;
return|return
name|ctx
return|;
block|}
annotation|@
name|Test
DECL|method|testCache ()
specifier|public
name|void
name|testCache
parameter_list|()
throws|throws
name|Exception
block|{
name|CacheEndpoint
name|endpoint
init|=
operator|(
name|CacheEndpoint
operator|)
name|context
operator|.
name|getEndpoint
argument_list|(
name|CACHE_URI
argument_list|)
decl_stmt|;
comment|// do some routes to let everything be initialized
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:add"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
comment|// Is CacheManagerFactory really referenced?
name|CacheManagerFactory
name|cmf
init|=
name|endpoint
operator|.
name|getCacheManagerFactory
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Cache Manager Factory Referenced"
argument_list|,
name|cmfRef
argument_list|,
name|cmf
argument_list|)
expr_stmt|;
comment|// Is the right ehcache_test.xml config. loaded?
name|Cache
name|cache
init|=
name|cmfRef
operator|.
name|getCacheManager
argument_list|()
operator|.
name|getCache
argument_list|(
literal|"testingOne"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Is ehcache_test.xml loaded"
argument_list|,
name|cache
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
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
literal|"direct:add"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|CacheConstants
operator|.
name|CACHE_OPERATION
argument_list|,
name|constant
argument_list|(
name|CacheConstants
operator|.
name|CACHE_OPERATION_ADD
argument_list|)
argument_list|)
operator|.
name|setHeader
argument_list|(
name|CacheConstants
operator|.
name|CACHE_KEY
argument_list|,
name|constant
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|CACHE_URI
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Configuration
DECL|method|configure ()
specifier|public
specifier|static
name|Option
index|[]
name|configure
parameter_list|()
block|{
name|Option
index|[]
name|options
init|=
name|combine
argument_list|(
name|getDefaultCamelKarafOptions
argument_list|()
argument_list|,
comment|// using the features to install the other camel components
name|scanFeatures
argument_list|(
name|getCamelKarafFeatureUrl
argument_list|()
argument_list|,
literal|"camel-cache"
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|options
return|;
block|}
DECL|class|TestingCacheManagerFactory
specifier|public
class|class
name|TestingCacheManagerFactory
extends|extends
name|CacheManagerFactory
block|{
DECL|field|xmlName
specifier|private
name|String
name|xmlName
decl_stmt|;
comment|//Only for testing purpose, normally not needed
DECL|field|cacheManager
specifier|private
name|CacheManager
name|cacheManager
decl_stmt|;
DECL|method|TestingCacheManagerFactory (String xmlName)
specifier|public
name|TestingCacheManagerFactory
parameter_list|(
name|String
name|xmlName
parameter_list|)
block|{
name|this
operator|.
name|xmlName
operator|=
name|xmlName
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createCacheManagerInstance ()
specifier|protected
specifier|synchronized
name|CacheManager
name|createCacheManagerInstance
parameter_list|()
block|{
comment|//Singleton- only for testing purpose, normally not needed
if|if
condition|(
name|cacheManager
operator|==
literal|null
condition|)
block|{
name|cacheManager
operator|=
name|CacheManager
operator|.
name|create
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|xmlName
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|cacheManager
return|;
block|}
DECL|method|getCacheManager ()
specifier|public
name|CacheManager
name|getCacheManager
parameter_list|()
block|{
return|return
name|cacheManager
return|;
block|}
block|}
block|}
end_class

end_unit

