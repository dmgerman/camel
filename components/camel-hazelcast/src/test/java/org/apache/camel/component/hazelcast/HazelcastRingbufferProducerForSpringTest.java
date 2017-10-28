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
name|ringbuffer
operator|.
name|Ringbuffer
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
name|ArgumentMatchers
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
name|when
import|;
end_import

begin_class
DECL|class|HazelcastRingbufferProducerForSpringTest
specifier|public
class|class
name|HazelcastRingbufferProducerForSpringTest
extends|extends
name|HazelcastCamelSpringTestSupport
block|{
annotation|@
name|Mock
DECL|field|ringbuffer
specifier|private
name|Ringbuffer
argument_list|<
name|Object
argument_list|>
name|ringbuffer
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
name|getRingbuffer
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|ringbuffer
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
name|getRingbuffer
argument_list|(
literal|"foo"
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
literal|"/META-INF/spring/test-camel-context-ringbuffer.xml"
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testReadTail ()
specifier|public
name|void
name|testReadTail
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|when
argument_list|(
name|ringbuffer
operator|.
name|readOne
argument_list|(
name|ArgumentMatchers
operator|.
name|anyLong
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"pippo"
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:readonceTail"
argument_list|,
literal|12L
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"pippo"
argument_list|,
name|result
argument_list|)
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
name|InterruptedException
block|{
name|when
argument_list|(
name|ringbuffer
operator|.
name|add
argument_list|(
name|ArgumentMatchers
operator|.
name|anyLong
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|13L
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:add"
argument_list|,
literal|12L
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|13L
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testCapacity ()
specifier|public
name|void
name|testCapacity
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|when
argument_list|(
name|ringbuffer
operator|.
name|capacity
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|13L
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:capacity"
argument_list|,
literal|12L
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|13L
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRemainingCapacity ()
specifier|public
name|void
name|testRemainingCapacity
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|when
argument_list|(
name|ringbuffer
operator|.
name|remainingCapacity
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|2L
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:remainingCapacity"
argument_list|,
literal|""
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2L
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

