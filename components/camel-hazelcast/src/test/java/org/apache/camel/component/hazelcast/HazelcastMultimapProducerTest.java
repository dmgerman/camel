begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hazelcast
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hazelcast
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|MultiMap
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
name|CamelExecutionException
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
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|*
import|;
end_import

begin_class
DECL|class|HazelcastMultimapProducerTest
specifier|public
class|class
name|HazelcastMultimapProducerTest
extends|extends
name|HazelcastCamelTestSupport
block|{
annotation|@
name|Mock
DECL|field|map
specifier|private
name|MultiMap
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|map
decl_stmt|;
annotation|@
name|Override
DECL|method|trainHazelcastInstance (HazelcastInstance hazelcastInstance)
specifier|protected
name|void
name|trainHazelcastInstance
parameter_list|(
name|HazelcastInstance
name|hazelcastInstance
parameter_list|)
block|{
name|when
argument_list|(
name|hazelcastInstance
operator|.
name|getMultiMap
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|verifyHazelcastInstance (HazelcastInstance hazelcastInstance)
specifier|protected
name|void
name|verifyHazelcastInstance
parameter_list|(
name|HazelcastInstance
name|hazelcastInstance
parameter_list|)
block|{
name|verify
argument_list|(
name|hazelcastInstance
argument_list|,
name|atLeastOnce
argument_list|()
argument_list|)
operator|.
name|getMultiMap
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|verifyMapMock ()
specifier|public
name|void
name|verifyMapMock
parameter_list|()
block|{
name|verifyNoMoreInteractions
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|CamelExecutionException
operator|.
name|class
argument_list|)
DECL|method|testWithInvalidOperation ()
specifier|public
name|void
name|testWithInvalidOperation
parameter_list|()
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:putInvalid"
argument_list|,
literal|"my-foo"
argument_list|,
name|HazelcastConstants
operator|.
name|OBJECT_ID
argument_list|,
literal|"4711"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPut ()
specifier|public
name|void
name|testPut
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:put"
argument_list|,
literal|"my-foo"
argument_list|,
name|HazelcastConstants
operator|.
name|OBJECT_ID
argument_list|,
literal|"4711"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|map
argument_list|)
operator|.
name|put
argument_list|(
literal|"4711"
argument_list|,
literal|"my-foo"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPutWithOperationName ()
specifier|public
name|void
name|testPutWithOperationName
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:putWithOperationName"
argument_list|,
literal|"my-foo"
argument_list|,
name|HazelcastConstants
operator|.
name|OBJECT_ID
argument_list|,
literal|"4711"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|map
argument_list|)
operator|.
name|put
argument_list|(
literal|"4711"
argument_list|,
literal|"my-foo"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPutWithOperationNumber ()
specifier|public
name|void
name|testPutWithOperationNumber
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:putWithOperationNumber"
argument_list|,
literal|"my-foo"
argument_list|,
name|HazelcastConstants
operator|.
name|OBJECT_ID
argument_list|,
literal|"4711"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|map
argument_list|)
operator|.
name|put
argument_list|(
literal|"4711"
argument_list|,
literal|"my-foo"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemoveValue ()
specifier|public
name|void
name|testRemoveValue
parameter_list|()
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:removevalue"
argument_list|,
literal|"my-foo"
argument_list|,
name|HazelcastConstants
operator|.
name|OBJECT_ID
argument_list|,
literal|"4711"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|map
argument_list|)
operator|.
name|remove
argument_list|(
literal|"4711"
argument_list|,
literal|"my-foo"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGet ()
specifier|public
name|void
name|testGet
parameter_list|()
block|{
name|when
argument_list|(
name|map
operator|.
name|get
argument_list|(
literal|"4711"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|Arrays
operator|.
expr|<
name|Object
operator|>
name|asList
argument_list|(
literal|"my-foo"
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:get"
argument_list|,
literal|null
argument_list|,
name|HazelcastConstants
operator|.
name|OBJECT_ID
argument_list|,
literal|"4711"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|map
argument_list|)
operator|.
name|get
argument_list|(
literal|"4711"
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|?
argument_list|>
name|body
init|=
name|consumer
operator|.
name|receiveBody
argument_list|(
literal|"seda:out"
argument_list|,
literal|5000
argument_list|,
name|Collection
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|body
operator|.
name|contains
argument_list|(
literal|"my-foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDelete ()
specifier|public
name|void
name|testDelete
parameter_list|()
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:delete"
argument_list|,
literal|null
argument_list|,
name|HazelcastConstants
operator|.
name|OBJECT_ID
argument_list|,
literal|4711
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|map
argument_list|)
operator|.
name|remove
argument_list|(
literal|4711
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
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:clear"
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|map
argument_list|)
operator|.
name|clear
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
literal|"direct:putInvalid"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|HazelcastConstants
operator|.
name|OPERATION
argument_list|,
name|constant
argument_list|(
literal|"bogus"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"hazelcast:%sbar"
argument_list|,
name|HazelcastConstants
operator|.
name|MULTIMAP_PREFIX
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:put"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|HazelcastConstants
operator|.
name|OPERATION
argument_list|,
name|constant
argument_list|(
name|HazelcastConstants
operator|.
name|PUT_OPERATION
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"hazelcast:%sbar"
argument_list|,
name|HazelcastConstants
operator|.
name|MULTIMAP_PREFIX
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:removevalue"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|HazelcastConstants
operator|.
name|OPERATION
argument_list|,
name|constant
argument_list|(
name|HazelcastConstants
operator|.
name|REMOVEVALUE_OPERATION
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"hazelcast:%sbar"
argument_list|,
name|HazelcastConstants
operator|.
name|MULTIMAP_PREFIX
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:get"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|HazelcastConstants
operator|.
name|OPERATION
argument_list|,
name|constant
argument_list|(
name|HazelcastConstants
operator|.
name|GET_OPERATION
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"hazelcast:%sbar"
argument_list|,
name|HazelcastConstants
operator|.
name|MULTIMAP_PREFIX
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:out"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:delete"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|HazelcastConstants
operator|.
name|OPERATION
argument_list|,
name|constant
argument_list|(
name|HazelcastConstants
operator|.
name|DELETE_OPERATION
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"hazelcast:%sbar"
argument_list|,
name|HazelcastConstants
operator|.
name|MULTIMAP_PREFIX
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:clear"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|HazelcastConstants
operator|.
name|OPERATION
argument_list|,
name|constant
argument_list|(
name|HazelcastConstants
operator|.
name|CLEAR_OPERATION
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"hazelcast:%sbar"
argument_list|,
name|HazelcastConstants
operator|.
name|MULTIMAP_PREFIX
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:putWithOperationNumber"
argument_list|)
operator|.
name|toF
argument_list|(
literal|"hazelcast:%sbar?operation=%s"
argument_list|,
name|HazelcastConstants
operator|.
name|MULTIMAP_PREFIX
argument_list|,
name|HazelcastConstants
operator|.
name|PUT_OPERATION
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:putWithOperationName"
argument_list|)
operator|.
name|toF
argument_list|(
literal|"hazelcast:%sbar?operation=put"
argument_list|,
name|HazelcastConstants
operator|.
name|MULTIMAP_PREFIX
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

