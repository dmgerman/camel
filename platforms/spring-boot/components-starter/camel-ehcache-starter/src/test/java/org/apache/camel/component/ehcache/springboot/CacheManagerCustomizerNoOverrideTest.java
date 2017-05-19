begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ehcache.springboot
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
operator|.
name|springboot
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
name|component
operator|.
name|ehcache
operator|.
name|EhcacheComponent
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
name|ComponentCustomizer
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
name|spring
operator|.
name|boot
operator|.
name|CamelAutoConfiguration
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
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|AutoConfigureAfter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|AutoConfigureBefore
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|autoconfigure
operator|.
name|SpringBootApplication
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|test
operator|.
name|context
operator|.
name|SpringBootTest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Bean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|Ordered
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|annotation
operator|.
name|Order
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|annotation
operator|.
name|DirtiesContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|SpringRunner
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|SpringRunner
operator|.
name|class
argument_list|)
annotation|@
name|DirtiesContext
annotation|@
name|SpringBootApplication
annotation|@
name|SpringBootTest
argument_list|(
name|classes
operator|=
block|{
name|CacheManagerCustomizerNoOverrideTest
operator|.
name|TestConfiguration
operator|.
name|class
block|}
argument_list|,
name|properties
operator|=
block|{
literal|"debug=false"
block|,
literal|"camel.component.ehcache.customizer.cache-manager.enabled=true"
block|,
literal|"camel.component.ehcache.customizer.cache-manager.override=false"
block|}
argument_list|)
DECL|class|CacheManagerCustomizerNoOverrideTest
specifier|public
class|class
name|CacheManagerCustomizerNoOverrideTest
block|{
DECL|field|CACHE_MANAGER
specifier|private
specifier|static
specifier|final
name|CacheManager
name|CACHE_MANAGER
init|=
name|CacheManagerBuilder
operator|.
name|newCacheManagerBuilder
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
annotation|@
name|Autowired
DECL|field|cacheManager
name|CacheManager
name|cacheManager
decl_stmt|;
annotation|@
name|Autowired
DECL|field|component
name|EhcacheComponent
name|component
decl_stmt|;
annotation|@
name|Test
DECL|method|testComponentConfiguration ()
specifier|public
name|void
name|testComponentConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|cacheManager
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|component
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|component
operator|.
name|getCacheManager
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|CACHE_MANAGER
argument_list|,
name|component
operator|.
name|getCacheManager
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Configuration
annotation|@
name|AutoConfigureAfter
argument_list|(
name|CamelAutoConfiguration
operator|.
name|class
argument_list|)
annotation|@
name|AutoConfigureBefore
argument_list|(
name|EhcacheComponentAutoConfiguration
operator|.
name|class
argument_list|)
DECL|class|TestConfiguration
specifier|public
specifier|static
class|class
name|TestConfiguration
block|{
annotation|@
name|Order
argument_list|(
name|Ordered
operator|.
name|HIGHEST_PRECEDENCE
argument_list|)
annotation|@
name|Bean
DECL|method|customizer ()
specifier|public
name|ComponentCustomizer
argument_list|<
name|EhcacheComponent
argument_list|>
name|customizer
parameter_list|()
block|{
return|return
operator|new
name|ComponentCustomizer
argument_list|<
name|EhcacheComponent
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|customize
parameter_list|(
name|EhcacheComponent
name|component
parameter_list|)
block|{
name|component
operator|.
name|setCacheManager
argument_list|(
name|CACHE_MANAGER
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Bean
argument_list|(
name|initMethod
operator|=
literal|"init"
argument_list|,
name|destroyMethod
operator|=
literal|"close"
argument_list|)
DECL|method|cacheManager ()
specifier|public
name|CacheManager
name|cacheManager
parameter_list|()
block|{
return|return
name|CacheManagerBuilder
operator|.
name|newCacheManagerBuilder
argument_list|()
operator|.
name|build
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

