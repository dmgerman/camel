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
name|client
operator|.
name|hotrod
operator|.
name|RemoteCacheManager
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
name|context
operator|.
name|ApplicationContext
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

begin_class
DECL|class|CacheManagerCustomizerNotEnabledTestBase
class|class
name|CacheManagerCustomizerNotEnabledTestBase
block|{
annotation|@
name|Autowired
DECL|field|embeddedCacheManager
name|EmbeddedCacheManager
name|embeddedCacheManager
decl_stmt|;
annotation|@
name|Autowired
DECL|field|remoteCacheManager
name|RemoteCacheManager
name|remoteCacheManager
decl_stmt|;
annotation|@
name|Autowired
DECL|field|component
name|InfinispanComponent
name|component
decl_stmt|;
annotation|@
name|Autowired
DECL|field|context
name|ApplicationContext
name|context
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
name|embeddedCacheManager
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|remoteCacheManager
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
name|assertNull
argument_list|(
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
block|{
annotation|@
name|Bean
DECL|method|embeddedCacheManagerInstance ()
specifier|public
name|EmbeddedCacheManager
name|embeddedCacheManagerInstance
parameter_list|()
block|{
return|return
name|CacheManagerCustomizerTestSupport
operator|.
name|newEmbeddedCacheManagerInstance
argument_list|()
return|;
block|}
annotation|@
name|Bean
DECL|method|remoteCacheManagerInstance ()
specifier|public
name|RemoteCacheManager
name|remoteCacheManagerInstance
parameter_list|()
block|{
return|return
name|CacheManagerCustomizerTestSupport
operator|.
name|newRemoteCacheManagerInstance
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

