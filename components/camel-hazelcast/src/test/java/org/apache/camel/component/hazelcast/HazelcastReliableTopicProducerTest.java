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
name|ITopic
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
DECL|class|HazelcastReliableTopicProducerTest
specifier|public
class|class
name|HazelcastReliableTopicProducerTest
extends|extends
name|HazelcastCamelTestSupport
block|{
annotation|@
name|Mock
DECL|field|reliableTopic
specifier|private
name|ITopic
argument_list|<
name|String
argument_list|>
name|reliableTopic
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
name|getReliableTopic
argument_list|(
literal|"bar"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|reliableTopic
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
name|getReliableTopic
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
name|reliableTopic
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
literal|"direct:publishInvalid"
argument_list|,
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
name|reliableTopic
argument_list|)
operator|.
name|publish
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|publish ()
specifier|public
name|void
name|publish
parameter_list|()
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:publish"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|reliableTopic
argument_list|)
operator|.
name|publish
argument_list|(
literal|"bar"
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
literal|"hazelcast-%sbar?reliable=true"
argument_list|,
name|HazelcastConstants
operator|.
name|TOPIC_PREFIX
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:publishInvalid"
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
literal|"hazelcast-%sbar?reliable=true"
argument_list|,
name|HazelcastConstants
operator|.
name|TOPIC_PREFIX
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:publish"
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
name|PUBLISH
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"hazelcast-%sbar?reliable=true"
argument_list|,
name|HazelcastConstants
operator|.
name|TOPIC_PREFIX
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

