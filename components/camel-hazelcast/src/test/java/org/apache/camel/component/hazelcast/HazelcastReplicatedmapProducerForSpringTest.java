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
name|com
operator|.
name|hazelcast
operator|.
name|core
operator|.
name|ReplicatedMap
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
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|AbstractApplicationContext
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
name|support
operator|.
name|ClassPathXmlApplicationContext
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
name|atLeastOnce
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
name|verify
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
name|verifyNoMoreInteractions
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
name|when
import|;
end_import

begin_class
DECL|class|HazelcastReplicatedmapProducerForSpringTest
specifier|public
class|class
name|HazelcastReplicatedmapProducerForSpringTest
extends|extends
name|HazelcastCamelSpringTestSupport
block|{
annotation|@
name|Mock
DECL|field|map
specifier|private
name|ReplicatedMap
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
name|getReplicatedMap
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
name|getReplicatedMap
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
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"/META-INF/spring/test-camel-context-replicatedmap.xml"
argument_list|)
return|;
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
DECL|method|testContainsKey ()
specifier|public
name|void
name|testContainsKey
parameter_list|()
block|{
name|when
argument_list|(
name|map
operator|.
name|containsKey
argument_list|(
literal|"testOk"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|map
operator|.
name|containsKey
argument_list|(
literal|"testKo"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:containsKey"
argument_list|,
literal|null
argument_list|,
name|HazelcastConstants
operator|.
name|OBJECT_ID
argument_list|,
literal|"testOk"
argument_list|)
expr_stmt|;
name|Boolean
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
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|map
argument_list|)
operator|.
name|containsKey
argument_list|(
literal|"testOk"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:containsKey"
argument_list|,
literal|null
argument_list|,
name|HazelcastConstants
operator|.
name|OBJECT_ID
argument_list|,
literal|"testKo"
argument_list|)
expr_stmt|;
name|body
operator|=
name|consumer
operator|.
name|receiveBody
argument_list|(
literal|"seda:out"
argument_list|,
literal|5000
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|map
argument_list|)
operator|.
name|containsKey
argument_list|(
literal|"testKo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testContainsValue ()
specifier|public
name|void
name|testContainsValue
parameter_list|()
block|{
name|when
argument_list|(
name|map
operator|.
name|containsValue
argument_list|(
literal|"testOk"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|map
operator|.
name|containsValue
argument_list|(
literal|"testKo"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:containsValue"
argument_list|,
literal|"testOk"
argument_list|)
expr_stmt|;
name|Boolean
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
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|map
argument_list|)
operator|.
name|containsValue
argument_list|(
literal|"testOk"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:containsValue"
argument_list|,
literal|"testKo"
argument_list|)
expr_stmt|;
name|body
operator|=
name|consumer
operator|.
name|receiveBody
argument_list|(
literal|"seda:out"
argument_list|,
literal|5000
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|map
argument_list|)
operator|.
name|containsValue
argument_list|(
literal|"testKo"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

