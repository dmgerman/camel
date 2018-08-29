begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.krati.processor.idempotent
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|krati
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
name|After
import|;
end_import

begin_import
import|import
name|krati
operator|.
name|core
operator|.
name|segment
operator|.
name|ChannelSegmentFactory
import|;
end_import

begin_import
import|import
name|krati
operator|.
name|io
operator|.
name|Serializer
import|;
end_import

begin_import
import|import
name|krati
operator|.
name|store
operator|.
name|DataSet
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
name|krati
operator|.
name|KratiHelper
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
name|krati
operator|.
name|serializer
operator|.
name|KratiDefaultSerializer
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
name|Test
import|;
end_import

begin_class
DECL|class|KratiIdempotentRepositoryTest
specifier|public
class|class
name|KratiIdempotentRepositoryTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|path
specifier|private
name|String
name|path
init|=
literal|"target/test/idempotent"
decl_stmt|;
DECL|field|dataSet
specifier|private
name|DataSet
argument_list|<
name|byte
index|[]
argument_list|>
name|dataSet
init|=
name|KratiHelper
operator|.
name|createDataSet
argument_list|(
name|path
argument_list|,
literal|2
argument_list|,
operator|new
name|ChannelSegmentFactory
argument_list|()
argument_list|)
decl_stmt|;
DECL|field|serializer
specifier|private
name|Serializer
argument_list|<
name|String
argument_list|>
name|serializer
init|=
operator|new
name|KratiDefaultSerializer
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|repository
specifier|private
name|KratiIdempotentRepository
name|repository
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
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|repository
operator|=
operator|new
name|KratiIdempotentRepository
argument_list|(
literal|"target/test/idempotent"
argument_list|)
expr_stmt|;
name|repository
operator|.
name|setDataSet
argument_list|(
name|dataSet
argument_list|)
expr_stmt|;
name|dataSet
operator|.
name|clear
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
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
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
name|dataSet
operator|.
name|clear
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
comment|// add first key
name|assertTrue
argument_list|(
name|dataSet
operator|.
name|add
argument_list|(
name|serializer
operator|.
name|serialize
argument_list|(
name|key01
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repository
operator|.
name|contains
argument_list|(
name|key01
argument_list|)
argument_list|)
expr_stmt|;
comment|// try to add an other one
name|assertTrue
argument_list|(
name|dataSet
operator|.
name|add
argument_list|(
name|serializer
operator|.
name|serialize
argument_list|(
name|key02
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repository
operator|.
name|contains
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
name|repository
operator|.
name|contains
argument_list|(
name|key01
argument_list|)
argument_list|)
expr_stmt|;
comment|// add key and check again
name|assertTrue
argument_list|(
name|repository
operator|.
name|add
argument_list|(
name|key01
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repository
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
comment|// add key to remove
name|assertTrue
argument_list|(
name|repository
operator|.
name|add
argument_list|(
name|key01
argument_list|)
argument_list|)
expr_stmt|;
comment|// assertEquals(1, dataSet.size());
comment|// remove key
name|assertTrue
argument_list|(
name|repository
operator|.
name|remove
argument_list|(
name|key01
argument_list|)
argument_list|)
expr_stmt|;
comment|//assertEquals(0, dataSet.size());
comment|// try to remove a key that isn't there
name|assertFalse
argument_list|(
name|repository
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
DECL|method|testClear ()
specifier|public
name|void
name|testClear
parameter_list|()
throws|throws
name|Exception
block|{
comment|// add keys to clear
name|assertTrue
argument_list|(
name|repository
operator|.
name|add
argument_list|(
name|key01
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repository
operator|.
name|add
argument_list|(
name|key02
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
name|repository
operator|.
name|contains
argument_list|(
name|key01
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|repository
operator|.
name|contains
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
name|repository
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
name|repository
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

