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
name|io
operator|.
name|Serializable
import|;
end_import

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
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
operator|.
name|Entry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|com
operator|.
name|hazelcast
operator|.
name|query
operator|.
name|SqlPredicate
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
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hazelcast
operator|.
name|testutil
operator|.
name|Dummy
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
DECL|class|HazelcastMapProducerTest
specifier|public
class|class
name|HazelcastMapProducerTest
extends|extends
name|HazelcastCamelTestSupport
implements|implements
name|Serializable
block|{
DECL|field|serialVersionUID
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
annotation|@
name|Mock
DECL|field|map
specifier|private
name|IMap
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
name|getMap
argument_list|(
literal|"foo"
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
name|getMap
argument_list|(
literal|"foo"
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
name|sendBody
argument_list|(
literal|"direct:putInvalid"
argument_list|,
literal|"my-foo"
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
DECL|method|testUpdate ()
specifier|public
name|void
name|testUpdate
parameter_list|()
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:update"
argument_list|,
literal|"my-fooo"
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
name|lock
argument_list|(
literal|"4711"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|map
argument_list|)
operator|.
name|replace
argument_list|(
literal|"4711"
argument_list|,
literal|"my-fooo"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|map
argument_list|)
operator|.
name|unlock
argument_list|(
literal|"4711"
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
literal|"my-foo"
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
name|String
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
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
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
name|assertEquals
argument_list|(
literal|"my-foo"
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetAllEmptySet ()
specifier|public
name|void
name|testGetAllEmptySet
parameter_list|()
block|{
name|Set
argument_list|<
name|Object
argument_list|>
name|l
init|=
operator|new
name|HashSet
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Map
name|t
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|t
operator|.
name|put
argument_list|(
literal|"key1"
argument_list|,
literal|"value1"
argument_list|)
expr_stmt|;
name|t
operator|.
name|put
argument_list|(
literal|"key2"
argument_list|,
literal|"value2"
argument_list|)
expr_stmt|;
name|t
operator|.
name|put
argument_list|(
literal|"key3"
argument_list|,
literal|"value3"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|map
operator|.
name|getAll
argument_list|(
name|anySet
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|t
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:getAll"
argument_list|,
literal|null
argument_list|,
name|HazelcastConstants
operator|.
name|OBJECT_ID
argument_list|,
name|l
argument_list|)
expr_stmt|;
name|String
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
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|map
argument_list|)
operator|.
name|getAll
argument_list|(
name|l
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"{key3=value3, key2=value2, key1=value1}"
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testGetAllOnlyOneKey ()
specifier|public
name|void
name|testGetAllOnlyOneKey
parameter_list|()
block|{
name|Set
argument_list|<
name|Object
argument_list|>
name|l
init|=
operator|new
name|HashSet
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|l
operator|.
name|add
argument_list|(
literal|"key1"
argument_list|)
expr_stmt|;
name|Map
name|t
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
name|t
operator|.
name|put
argument_list|(
literal|"key1"
argument_list|,
literal|"value1"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|map
operator|.
name|getAll
argument_list|(
name|l
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|t
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:getAll"
argument_list|,
literal|null
argument_list|,
name|HazelcastConstants
operator|.
name|OBJECT_ID
argument_list|,
name|l
argument_list|)
expr_stmt|;
name|String
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
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|map
argument_list|)
operator|.
name|getAll
argument_list|(
name|l
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"{key1=value1}"
argument_list|,
name|body
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
DECL|method|testQuery ()
specifier|public
name|void
name|testQuery
parameter_list|()
block|{
name|String
name|sql
init|=
literal|"bar> 1000"
decl_stmt|;
name|when
argument_list|(
name|map
operator|.
name|values
argument_list|(
name|any
argument_list|(
name|SqlPredicate
operator|.
name|class
argument_list|)
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
operator|new
name|Dummy
argument_list|(
literal|"beta"
argument_list|,
literal|2000
argument_list|)
argument_list|,
operator|new
name|Dummy
argument_list|(
literal|"gamma"
argument_list|,
literal|3000
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:query"
argument_list|,
literal|null
argument_list|,
name|HazelcastConstants
operator|.
name|QUERY
argument_list|,
name|sql
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|map
argument_list|)
operator|.
name|values
argument_list|(
name|any
argument_list|(
name|SqlPredicate
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|?
argument_list|>
name|b1
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
name|assertNotNull
argument_list|(
name|b1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|b1
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEmptyQuery ()
specifier|public
name|void
name|testEmptyQuery
parameter_list|()
block|{
name|when
argument_list|(
name|map
operator|.
name|values
argument_list|()
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
operator|new
name|Dummy
argument_list|(
literal|"beta"
argument_list|,
literal|2000
argument_list|)
argument_list|,
operator|new
name|Dummy
argument_list|(
literal|"gamma"
argument_list|,
literal|3000
argument_list|)
argument_list|,
operator|new
name|Dummy
argument_list|(
literal|"delta"
argument_list|,
literal|4000
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:query"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|map
argument_list|)
operator|.
name|values
argument_list|()
expr_stmt|;
name|Collection
argument_list|<
name|?
argument_list|>
name|b1
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
name|assertNotNull
argument_list|(
name|b1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|b1
operator|.
name|size
argument_list|()
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
literal|"hazelcast:%sfoo"
argument_list|,
name|HazelcastConstants
operator|.
name|MAP_PREFIX
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
literal|"hazelcast:%sfoo"
argument_list|,
name|HazelcastConstants
operator|.
name|MAP_PREFIX
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:update"
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
name|UPDATE_OPERATION
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"hazelcast:%sfoo"
argument_list|,
name|HazelcastConstants
operator|.
name|MAP_PREFIX
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
literal|"hazelcast:%sfoo"
argument_list|,
name|HazelcastConstants
operator|.
name|MAP_PREFIX
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
literal|"direct:getAll"
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
name|GET_ALL_OPERATION
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"hazelcast:%sfoo"
argument_list|,
name|HazelcastConstants
operator|.
name|MAP_PREFIX
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
literal|"hazelcast:%sfoo"
argument_list|,
name|HazelcastConstants
operator|.
name|MAP_PREFIX
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:query"
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
name|QUERY_OPERATION
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"hazelcast:%sfoo"
argument_list|,
name|HazelcastConstants
operator|.
name|MAP_PREFIX
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
literal|"direct:putWithOperationNumber"
argument_list|)
operator|.
name|toF
argument_list|(
literal|"hazelcast:%sfoo?operation=%s"
argument_list|,
name|HazelcastConstants
operator|.
name|MAP_PREFIX
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
literal|"hazelcast:%sfoo?operation=put"
argument_list|,
name|HazelcastConstants
operator|.
name|MAP_PREFIX
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

