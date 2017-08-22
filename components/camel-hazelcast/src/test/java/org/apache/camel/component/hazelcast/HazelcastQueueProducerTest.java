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
name|ArrayList
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
name|function
operator|.
name|Predicate
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
name|IQueue
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
DECL|class|HazelcastQueueProducerTest
specifier|public
class|class
name|HazelcastQueueProducerTest
extends|extends
name|HazelcastCamelTestSupport
block|{
annotation|@
name|Mock
DECL|field|queue
specifier|private
name|IQueue
argument_list|<
name|String
argument_list|>
name|queue
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
expr|<
name|String
operator|>
name|getQueue
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|queue
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
name|getQueue
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|verifyQueueMock ()
specifier|public
name|void
name|verifyQueueMock
parameter_list|()
block|{
name|verifyNoMoreInteractions
argument_list|(
name|queue
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
literal|"foo"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|put ()
specifier|public
name|void
name|put
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:put"
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|queue
argument_list|)
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|putWithOperationNumber ()
specifier|public
name|void
name|putWithOperationNumber
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:putWithOperationNumber"
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|queue
argument_list|)
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|putWithOperationName ()
specifier|public
name|void
name|putWithOperationName
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:putWithOperationName"
argument_list|,
literal|"foo"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|queue
argument_list|)
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|noOperation ()
specifier|public
name|void
name|noOperation
parameter_list|()
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:no-operation"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|queue
argument_list|)
operator|.
name|add
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|add ()
specifier|public
name|void
name|add
parameter_list|()
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:add"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|queue
argument_list|)
operator|.
name|add
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|offer ()
specifier|public
name|void
name|offer
parameter_list|()
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:offer"
argument_list|,
literal|"foobar"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|queue
argument_list|)
operator|.
name|offer
argument_list|(
literal|"foobar"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|removeSpecifiedValue ()
specifier|public
name|void
name|removeSpecifiedValue
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:removeValue"
argument_list|,
literal|"foo2"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|queue
argument_list|)
operator|.
name|remove
argument_list|(
literal|"foo2"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|removeValue ()
specifier|public
name|void
name|removeValue
parameter_list|()
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:removeValue"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|queue
argument_list|)
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|poll ()
specifier|public
name|void
name|poll
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|when
argument_list|(
name|queue
operator|.
name|poll
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|String
name|answer
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:poll"
argument_list|,
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|queue
argument_list|)
operator|.
name|poll
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|peek ()
specifier|public
name|void
name|peek
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|when
argument_list|(
name|queue
operator|.
name|peek
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|String
name|answer
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:peek"
argument_list|,
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|queue
argument_list|)
operator|.
name|peek
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|remainingCapacity ()
specifier|public
name|void
name|remainingCapacity
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|when
argument_list|(
name|queue
operator|.
name|remainingCapacity
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|int
name|answer
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:REMAINING_CAPACITY"
argument_list|,
literal|null
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|queue
argument_list|)
operator|.
name|remainingCapacity
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|removeAll ()
specifier|public
name|void
name|removeAll
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|Collection
name|c
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|c
operator|.
name|add
argument_list|(
literal|"foo2"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:removeAll"
argument_list|,
name|c
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|queue
argument_list|)
operator|.
name|removeAll
argument_list|(
name|c
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|removeIf ()
specifier|public
name|void
name|removeIf
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|Predicate
argument_list|<
name|String
argument_list|>
name|i
init|=
parameter_list|(
name|s
parameter_list|)
lambda|->
name|s
operator|.
name|length
argument_list|()
operator|>
literal|5
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:removeIf"
argument_list|,
name|i
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|queue
argument_list|)
operator|.
name|removeIf
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|drainTo ()
specifier|public
name|void
name|drainTo
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|Collection
name|l
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|HazelcastConstants
operator|.
name|DRAIN_TO_COLLECTION
argument_list|,
name|l
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|queue
operator|.
name|drainTo
argument_list|(
name|l
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|int
name|answer
init|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
literal|"direct:drainTo"
argument_list|,
literal|"test"
argument_list|,
name|headers
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|queue
argument_list|)
operator|.
name|drainTo
argument_list|(
name|l
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|answer
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
literal|"direct:no-operation"
argument_list|)
operator|.
name|to
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"hazelcast-%sbar"
argument_list|,
name|HazelcastConstants
operator|.
name|QUEUE_PREFIX
argument_list|)
argument_list|)
expr_stmt|;
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
literal|"hazelcast-%sbar"
argument_list|,
name|HazelcastConstants
operator|.
name|QUEUE_PREFIX
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
name|HazelcastOperation
operator|.
name|PUT
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"hazelcast-%sbar"
argument_list|,
name|HazelcastConstants
operator|.
name|QUEUE_PREFIX
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:add"
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
name|HazelcastOperation
operator|.
name|ADD
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"hazelcast-%sbar"
argument_list|,
name|HazelcastConstants
operator|.
name|QUEUE_PREFIX
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:offer"
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
name|HazelcastOperation
operator|.
name|OFFER
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"hazelcast-%sbar"
argument_list|,
name|HazelcastConstants
operator|.
name|QUEUE_PREFIX
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:poll"
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
name|HazelcastOperation
operator|.
name|POLL
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"hazelcast-%sbar"
argument_list|,
name|HazelcastConstants
operator|.
name|QUEUE_PREFIX
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:peek"
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
name|HazelcastOperation
operator|.
name|PEEK
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"hazelcast-%sbar"
argument_list|,
name|HazelcastConstants
operator|.
name|QUEUE_PREFIX
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:removeValue"
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
name|HazelcastOperation
operator|.
name|REMOVE_VALUE
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"hazelcast-%sbar"
argument_list|,
name|HazelcastConstants
operator|.
name|QUEUE_PREFIX
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:removeAll"
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
name|HazelcastOperation
operator|.
name|REMOVE_ALL
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"hazelcast-%sbar"
argument_list|,
name|HazelcastConstants
operator|.
name|QUEUE_PREFIX
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:removeIf"
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
name|HazelcastOperation
operator|.
name|REMOVE_IF
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"hazelcast-%sbar"
argument_list|,
name|HazelcastConstants
operator|.
name|QUEUE_PREFIX
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:REMAINING_CAPACITY"
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
name|HazelcastOperation
operator|.
name|REMAINING_CAPACITY
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"hazelcast-%sbar"
argument_list|,
name|HazelcastConstants
operator|.
name|QUEUE_PREFIX
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:drainTo"
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
name|HazelcastOperation
operator|.
name|DRAIN_TO
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"hazelcast-%sbar"
argument_list|,
name|HazelcastConstants
operator|.
name|QUEUE_PREFIX
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
name|String
operator|.
name|format
argument_list|(
literal|"hazelcast-%sbar?operation=%s"
argument_list|,
name|HazelcastConstants
operator|.
name|QUEUE_PREFIX
argument_list|,
name|HazelcastOperation
operator|.
name|PUT
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:putWithOperationName"
argument_list|)
operator|.
name|toF
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"hazelcast-%sbar?operation=PUT"
argument_list|,
name|HazelcastConstants
operator|.
name|QUEUE_PREFIX
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

