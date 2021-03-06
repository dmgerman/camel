begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.idempotent.hazelcast
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|idempotent
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
name|Hazelcast
import|;
end_import

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
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|IMap
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
name|mock
operator|.
name|MockEndpoint
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

begin_class
DECL|class|HazelcastIdempotentRepositoryTest
specifier|public
class|class
name|HazelcastIdempotentRepositoryTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|cache
specifier|private
name|IMap
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|cache
decl_stmt|;
DECL|field|repo
specifier|private
name|HazelcastIdempotentRepository
name|repo
decl_stmt|;
DECL|field|hazelcastInstance
specifier|private
name|HazelcastInstance
name|hazelcastInstance
decl_stmt|;
DECL|field|key01
specifier|private
name|String
name|key01
init|=
literal|"123"
decl_stmt|;
DECL|field|key02
specifier|private
name|String
name|key02
init|=
literal|"456"
decl_stmt|;
annotation|@
name|Override
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
name|hazelcastInstance
operator|=
name|Hazelcast
operator|.
name|newHazelcastInstance
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|cache
operator|=
name|hazelcastInstance
operator|.
name|getMap
argument_list|(
literal|"myRepo"
argument_list|)
expr_stmt|;
name|repo
operator|=
operator|new
name|HazelcastIdempotentRepository
argument_list|(
name|hazelcastInstance
argument_list|,
literal|"myRepo"
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|cache
operator|.
name|clear
argument_list|()
expr_stmt|;
name|repo
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
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
name|repo
operator|.
name|stop
argument_list|()
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
name|cache
operator|.
name|clear
argument_list|()
expr_stmt|;
name|hazelcastInstance
operator|.
name|getLifecycleService
argument_list|()
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAdd ()
specifier|public
name|void
name|testAdd
parameter_list|()
throws|throws
name|Exception
block|{
comment|// ADD first key
name|assertTrue
argument_list|(
name|repo
operator|.
name|add
argument_list|(
name|key01
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|cache
operator|.
name|containsKey
argument_list|(
name|key01
argument_list|)
argument_list|)
expr_stmt|;
comment|// try to ADD the same key again
name|assertFalse
argument_list|(
name|repo
operator|.
name|add
argument_list|(
name|key01
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// try to ADD an other one
name|assertTrue
argument_list|(
name|repo
operator|.
name|add
argument_list|(
name|key02
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConfirm ()
specifier|public
name|void
name|testConfirm
parameter_list|()
throws|throws
name|Exception
block|{
comment|// ADD first key and confirm
name|assertTrue
argument_list|(
name|repo
operator|.
name|add
argument_list|(
name|key01
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repo
operator|.
name|confirm
argument_list|(
name|key01
argument_list|)
argument_list|)
expr_stmt|;
comment|// try to confirm a key that isn't there
name|assertFalse
argument_list|(
name|repo
operator|.
name|confirm
argument_list|(
name|key02
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testContains ()
specifier|public
name|void
name|testContains
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFalse
argument_list|(
name|repo
operator|.
name|contains
argument_list|(
name|key01
argument_list|)
argument_list|)
expr_stmt|;
comment|// ADD key and check again
name|assertTrue
argument_list|(
name|repo
operator|.
name|add
argument_list|(
name|key01
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repo
operator|.
name|contains
argument_list|(
name|key01
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemove ()
specifier|public
name|void
name|testRemove
parameter_list|()
throws|throws
name|Exception
block|{
comment|// ADD key to remove
name|assertTrue
argument_list|(
name|repo
operator|.
name|add
argument_list|(
name|key01
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repo
operator|.
name|add
argument_list|(
name|key02
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// CLEAR repo
name|repo
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testClear ()
specifier|public
name|void
name|testClear
parameter_list|()
throws|throws
name|Exception
block|{
comment|// ADD key to remove
name|assertTrue
argument_list|(
name|repo
operator|.
name|add
argument_list|(
name|key01
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// remove key
name|assertTrue
argument_list|(
name|repo
operator|.
name|remove
argument_list|(
name|key01
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// try to remove a key that isn't there
name|assertFalse
argument_list|(
name|repo
operator|.
name|remove
argument_list|(
name|key02
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRepositoryInRoute ()
specifier|public
name|void
name|testRepositoryInRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:out"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"a"
argument_list|,
literal|"b"
argument_list|)
expr_stmt|;
comment|// c is a duplicate
comment|// should be started
name|assertEquals
argument_list|(
literal|"Should be started"
argument_list|,
literal|true
argument_list|,
name|repo
operator|.
name|getStatus
argument_list|()
operator|.
name|isStarted
argument_list|()
argument_list|)
expr_stmt|;
comment|// send 3 message with one duplicated key (key01)
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct://in"
argument_list|,
literal|"a"
argument_list|,
literal|"messageId"
argument_list|,
name|key01
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct://in"
argument_list|,
literal|"b"
argument_list|,
literal|"messageId"
argument_list|,
name|key02
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct://in"
argument_list|,
literal|"c"
argument_list|,
literal|"messageId"
argument_list|,
name|key01
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
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
literal|"direct://in"
argument_list|)
operator|.
name|idempotentConsumer
argument_list|(
name|header
argument_list|(
literal|"messageId"
argument_list|)
argument_list|,
name|repo
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock://out"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

