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
name|IList
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
name|ISet
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
DECL|class|HazelcastSetProducerTest
specifier|public
class|class
name|HazelcastSetProducerTest
extends|extends
name|HazelcastCamelTestSupport
block|{
annotation|@
name|Mock
DECL|field|set
specifier|private
name|ISet
argument_list|<
name|String
argument_list|>
name|set
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
name|getSet
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|set
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
name|getSet
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|verifySetMock ()
specifier|public
specifier|final
name|void
name|verifySetMock
parameter_list|()
block|{
name|verifyNoMoreInteractions
argument_list|(
name|set
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
literal|"direct:addInvalid"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|addValue ()
specifier|public
name|void
name|addValue
parameter_list|()
throws|throws
name|InterruptedException
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
name|set
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
DECL|method|addValueWithOperationNumber ()
specifier|public
name|void
name|addValueWithOperationNumber
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:addWithOperationNumber"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|set
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
DECL|method|addValueWithOperationName ()
specifier|public
name|void
name|addValueWithOperationName
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:addWithOperationName"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|set
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
DECL|method|removeValue ()
specifier|public
name|void
name|removeValue
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:removevalue"
argument_list|,
literal|"foo2"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|set
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
DECL|method|clearList ()
specifier|public
name|void
name|clearList
parameter_list|()
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:clear"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|set
argument_list|)
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|addAll ()
specifier|public
name|void
name|addAll
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|Collection
name|t
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|t
operator|.
name|add
argument_list|(
literal|"test1"
argument_list|)
expr_stmt|;
name|t
operator|.
name|add
argument_list|(
literal|"test2"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:addAll"
argument_list|,
name|t
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|set
argument_list|)
operator|.
name|addAll
argument_list|(
name|t
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
name|t
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|t
operator|.
name|add
argument_list|(
literal|"test1"
argument_list|)
expr_stmt|;
name|t
operator|.
name|add
argument_list|(
literal|"test2"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:removeAll"
argument_list|,
name|t
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|set
argument_list|)
operator|.
name|removeAll
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|retainAll ()
specifier|public
name|void
name|retainAll
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|Collection
name|t
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|t
operator|.
name|add
argument_list|(
literal|"test1"
argument_list|)
expr_stmt|;
name|t
operator|.
name|add
argument_list|(
literal|"test2"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:retainAll"
argument_list|,
name|t
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|set
argument_list|)
operator|.
name|retainAll
argument_list|(
name|t
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
literal|"direct:addInvalid"
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
name|toF
argument_list|(
literal|"hazelcast:%sbar"
argument_list|,
name|HazelcastConstants
operator|.
name|SET_PREFIX
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
name|HazelcastConstants
operator|.
name|ADD_OPERATION
argument_list|)
argument_list|)
operator|.
name|toF
argument_list|(
literal|"hazelcast:%sbar"
argument_list|,
name|HazelcastConstants
operator|.
name|SET_PREFIX
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
name|SET_PREFIX
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
name|toF
argument_list|(
literal|"hazelcast:%sbar"
argument_list|,
name|HazelcastConstants
operator|.
name|SET_PREFIX
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:addAll"
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
name|ADD_ALL_OPERATION
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
name|SET_PREFIX
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
name|HazelcastConstants
operator|.
name|REMOVE_ALL_OPERATION
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
name|SET_PREFIX
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:retainAll"
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
name|RETAIN_ALL_OPERATION
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
name|SET_PREFIX
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:addWithOperationNumber"
argument_list|)
operator|.
name|toF
argument_list|(
literal|"hazelcast:%sbar?operation=%s"
argument_list|,
name|HazelcastConstants
operator|.
name|SET_PREFIX
argument_list|,
name|HazelcastConstants
operator|.
name|ADD_OPERATION
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:addWithOperationName"
argument_list|)
operator|.
name|toF
argument_list|(
literal|"hazelcast:%sbar?operation=add"
argument_list|,
name|HazelcastConstants
operator|.
name|SET_PREFIX
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

