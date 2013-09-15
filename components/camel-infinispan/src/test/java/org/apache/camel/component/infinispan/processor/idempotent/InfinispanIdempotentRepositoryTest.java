begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.infinispan.processor.idempotent
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
operator|.
name|processor
operator|.
name|idempotent
package|;
end_package

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
name|BasicCache
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
name|configuration
operator|.
name|global
operator|.
name|GlobalConfiguration
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
name|global
operator|.
name|GlobalConfigurationBuilder
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
name|DefaultCacheManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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
name|jgroups
operator|.
name|util
operator|.
name|Util
operator|.
name|assertFalse
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|jgroups
operator|.
name|util
operator|.
name|Util
operator|.
name|assertTrue
import|;
end_import

begin_class
DECL|class|InfinispanIdempotentRepositoryTest
specifier|public
class|class
name|InfinispanIdempotentRepositoryTest
block|{
DECL|field|basicCacheContainer
specifier|protected
name|BasicCacheContainer
name|basicCacheContainer
decl_stmt|;
DECL|field|idempotentRepository
specifier|protected
name|InfinispanIdempotentRepository
name|idempotentRepository
decl_stmt|;
DECL|field|cacheName
specifier|protected
name|String
name|cacheName
init|=
literal|"test"
decl_stmt|;
DECL|field|GLOBAL_CONFIGURATION
specifier|public
specifier|static
specifier|final
name|GlobalConfiguration
name|GLOBAL_CONFIGURATION
init|=
operator|new
name|GlobalConfigurationBuilder
argument_list|()
operator|.
name|globalJmxStatistics
argument_list|()
operator|.
name|allowDuplicateDomains
argument_list|(
literal|true
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
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
name|basicCacheContainer
operator|=
operator|new
name|DefaultCacheManager
argument_list|(
name|GLOBAL_CONFIGURATION
argument_list|)
expr_stmt|;
name|basicCacheContainer
operator|.
name|start
argument_list|()
expr_stmt|;
name|idempotentRepository
operator|=
name|InfinispanIdempotentRepository
operator|.
name|infinispanIdempotentRepository
argument_list|(
name|basicCacheContainer
argument_list|,
name|cacheName
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|basicCacheContainer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|addsNewKeysToCache ()
specifier|public
name|void
name|addsNewKeysToCache
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
name|idempotentRepository
operator|.
name|add
argument_list|(
literal|"One"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|idempotentRepository
operator|.
name|add
argument_list|(
literal|"Two"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getCache
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|"One"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|getCache
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|"Two"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|skipsAddingSecondTimeTheSameKey ()
specifier|public
name|void
name|skipsAddingSecondTimeTheSameKey
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
name|idempotentRepository
operator|.
name|add
argument_list|(
literal|"One"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|idempotentRepository
operator|.
name|add
argument_list|(
literal|"One"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|containsPreviouslyAddedKey ()
specifier|public
name|void
name|containsPreviouslyAddedKey
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFalse
argument_list|(
name|idempotentRepository
operator|.
name|contains
argument_list|(
literal|"One"
argument_list|)
argument_list|)
expr_stmt|;
name|idempotentRepository
operator|.
name|add
argument_list|(
literal|"One"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|idempotentRepository
operator|.
name|contains
argument_list|(
literal|"One"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|removesAnExistingKey ()
specifier|public
name|void
name|removesAnExistingKey
parameter_list|()
throws|throws
name|Exception
block|{
name|idempotentRepository
operator|.
name|add
argument_list|(
literal|"One"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|idempotentRepository
operator|.
name|remove
argument_list|(
literal|"One"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|idempotentRepository
operator|.
name|contains
argument_list|(
literal|"One"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|doesntRemoveMissingKey ()
specifier|public
name|void
name|doesntRemoveMissingKey
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFalse
argument_list|(
name|idempotentRepository
operator|.
name|remove
argument_list|(
literal|"One"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getCache ()
specifier|private
name|BasicCache
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|getCache
parameter_list|()
block|{
return|return
name|basicCacheContainer
operator|.
name|getCache
argument_list|(
name|cacheName
argument_list|)
return|;
block|}
block|}
end_class

end_unit

