begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jcache.processor.idempotent
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jcache
operator|.
name|processor
operator|.
name|idempotent
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|cache
operator|.
name|Cache
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
name|jcache
operator|.
name|JCacheConfiguration
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
name|jcache
operator|.
name|JCacheHelper
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
name|jcache
operator|.
name|JCacheManager
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
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|JCacheIdempotentRepositoryTest
specifier|public
class|class
name|JCacheIdempotentRepositoryTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|JCacheIdempotentRepositoryTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|cacheManager
specifier|private
name|JCacheManager
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|cacheManager
decl_stmt|;
DECL|field|cache
specifier|private
name|Cache
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|cache
decl_stmt|;
DECL|field|repository
specifier|private
name|JCacheIdempotentRepository
name|repository
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
name|cacheManager
operator|=
name|JCacheHelper
operator|.
name|createManager
argument_list|(
operator|new
name|JCacheConfiguration
argument_list|(
literal|"idempotent-repository"
argument_list|)
argument_list|)
expr_stmt|;
name|cache
operator|=
name|cacheManager
operator|.
name|getCache
argument_list|()
expr_stmt|;
name|repository
operator|=
operator|new
name|JCacheIdempotentRepository
argument_list|()
expr_stmt|;
name|repository
operator|.
name|setCache
argument_list|(
name|cache
argument_list|)
expr_stmt|;
name|repository
operator|.
name|start
argument_list|()
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
name|repository
operator|.
name|stop
argument_list|()
expr_stmt|;
name|cacheManager
operator|.
name|close
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
name|repository
operator|.
name|add
argument_list|(
literal|"One"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repository
operator|.
name|add
argument_list|(
literal|"Two"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cache
operator|.
name|containsKey
argument_list|(
literal|"One"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cache
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
name|repository
operator|.
name|add
argument_list|(
literal|"One"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|repository
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
name|repository
operator|.
name|contains
argument_list|(
literal|"One"
argument_list|)
argument_list|)
expr_stmt|;
name|repository
operator|.
name|add
argument_list|(
literal|"One"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repository
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
name|cache
operator|.
name|clear
argument_list|()
expr_stmt|;
name|repository
operator|.
name|add
argument_list|(
literal|"One"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repository
operator|.
name|remove
argument_list|(
literal|"One"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|repository
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
DECL|method|doesNotRemoveMissingKey ()
specifier|public
name|void
name|doesNotRemoveMissingKey
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFalse
argument_list|(
name|repository
operator|.
name|remove
argument_list|(
literal|"One"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|clearCache ()
specifier|public
name|void
name|clearCache
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
name|repository
operator|.
name|add
argument_list|(
literal|"One"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repository
operator|.
name|add
argument_list|(
literal|"Two"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cache
operator|.
name|containsKey
argument_list|(
literal|"One"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cache
operator|.
name|containsKey
argument_list|(
literal|"Two"
argument_list|)
argument_list|)
expr_stmt|;
name|repository
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|cache
operator|.
name|containsKey
argument_list|(
literal|"One"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|cache
operator|.
name|containsKey
argument_list|(
literal|"Two"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

