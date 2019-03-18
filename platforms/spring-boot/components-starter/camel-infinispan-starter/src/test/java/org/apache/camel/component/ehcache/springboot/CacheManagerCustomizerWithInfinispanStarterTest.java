begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|infinispan
operator|.
name|InfinispanComponent
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
name|Configuration
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
name|CacheManagerCustomizerWithInfinispanStarterTest
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
literal|"infinispan.embedded.enabled=true"
block|,
literal|"infinispan.remote.enabled=false"
block|}
argument_list|)
DECL|class|CacheManagerCustomizerWithInfinispanStarterTest
specifier|public
class|class
name|CacheManagerCustomizerWithInfinispanStarterTest
block|{
annotation|@
name|Autowired
DECL|field|cacheManager
name|EmbeddedCacheManager
name|cacheManager
decl_stmt|;
annotation|@
name|Autowired
DECL|field|component
name|InfinispanComponent
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
name|assertEquals
argument_list|(
name|cacheManager
argument_list|,
name|component
operator|.
name|getCacheContainer
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Configuration
DECL|class|TestConfiguration
specifier|public
specifier|static
class|class
name|TestConfiguration
block|{     }
block|}
end_class

end_unit

