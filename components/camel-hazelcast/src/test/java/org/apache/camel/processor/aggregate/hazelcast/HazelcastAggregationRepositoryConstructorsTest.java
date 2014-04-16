begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.aggregate.hazelcast
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|aggregate
operator|.
name|hazelcast
package|;
end_package

begin_import
import|import
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|HazelcastInstance
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
name|Exchange
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
name|DefaultExchange
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
name|Test
import|;
end_import

begin_class
DECL|class|HazelcastAggregationRepositoryConstructorsTest
specifier|public
class|class
name|HazelcastAggregationRepositoryConstructorsTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|UnsupportedOperationException
operator|.
name|class
argument_list|)
DECL|method|nonOptimisticRepoFailsOnOptimisticAdd ()
specifier|public
name|void
name|nonOptimisticRepoFailsOnOptimisticAdd
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|repoName
init|=
literal|"hzRepoMap"
decl_stmt|;
name|HazelcastAggregationRepository
name|repo
init|=
operator|new
name|HazelcastAggregationRepository
argument_list|(
name|repoName
argument_list|)
decl_stmt|;
name|repo
operator|.
name|doStart
argument_list|()
expr_stmt|;
try|try
block|{
name|Exchange
name|oldOne
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|()
argument_list|)
decl_stmt|;
name|Exchange
name|newOne
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|String
name|key
init|=
literal|"abrakadabra"
decl_stmt|;
name|repo
operator|.
name|add
argument_list|(
name|context
argument_list|()
argument_list|,
name|key
argument_list|,
name|oldOne
argument_list|,
name|newOne
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"OptimisticLockingException should has been thrown"
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|repo
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|UnsupportedOperationException
operator|.
name|class
argument_list|)
DECL|method|optimisticRepoFailsForNonOptimisticAdd ()
specifier|public
name|void
name|optimisticRepoFailsForNonOptimisticAdd
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|repoName
init|=
literal|"hzRepoMap"
decl_stmt|;
name|HazelcastAggregationRepository
name|repo
init|=
operator|new
name|HazelcastAggregationRepository
argument_list|(
name|repoName
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|repo
operator|.
name|doStart
argument_list|()
expr_stmt|;
try|try
block|{
name|Exchange
name|ex
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|String
name|key
init|=
literal|"abrakadabra"
decl_stmt|;
name|repo
operator|.
name|add
argument_list|(
name|context
argument_list|()
argument_list|,
name|key
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|repo
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|IllegalArgumentException
operator|.
name|class
argument_list|)
DECL|method|uninitializedHazelcastInstanceThrows ()
specifier|public
name|void
name|uninitializedHazelcastInstanceThrows
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|repoName
init|=
literal|"hzRepoMap"
decl_stmt|;
name|HazelcastAggregationRepository
name|repo
init|=
operator|new
name|HazelcastAggregationRepository
argument_list|(
name|repoName
argument_list|,
operator|(
name|HazelcastInstance
operator|)
literal|null
argument_list|)
decl_stmt|;
name|repo
operator|.
name|doStart
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|locallyInitializedHazelcastInstanceAdd ()
specifier|public
name|void
name|locallyInitializedHazelcastInstanceAdd
parameter_list|()
throws|throws
name|Exception
block|{
name|HazelcastAggregationRepository
name|repo
init|=
operator|new
name|HazelcastAggregationRepository
argument_list|(
literal|"hzRepoMap"
argument_list|)
decl_stmt|;
try|try
block|{
name|repo
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|Exchange
name|ex
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|()
argument_list|)
decl_stmt|;
name|repo
operator|.
name|add
argument_list|(
name|context
argument_list|()
argument_list|,
literal|"somedefaultkey"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
comment|//} catch (Throwable e) {
comment|//fail(e.getMessage());
block|}
finally|finally
block|{
name|repo
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

