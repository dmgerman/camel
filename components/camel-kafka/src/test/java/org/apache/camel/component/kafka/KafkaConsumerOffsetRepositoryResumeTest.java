begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kafka
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kafka
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|BindToRegistry
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
name|EndpointInject
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
name|impl
operator|.
name|engine
operator|.
name|MemoryStateRepository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|clients
operator|.
name|producer
operator|.
name|ProducerRecord
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

begin_class
DECL|class|KafkaConsumerOffsetRepositoryResumeTest
specifier|public
class|class
name|KafkaConsumerOffsetRepositoryResumeTest
extends|extends
name|BaseEmbeddedKafkaTest
block|{
DECL|field|TOPIC
specifier|private
specifier|static
specifier|final
name|String
name|TOPIC
init|=
literal|"offset-resume"
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:result"
argument_list|)
DECL|field|result
specifier|private
name|MockEndpoint
name|result
decl_stmt|;
DECL|field|producer
specifier|private
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|clients
operator|.
name|producer
operator|.
name|KafkaProducer
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|producer
decl_stmt|;
annotation|@
name|BindToRegistry
argument_list|(
literal|"offset"
argument_list|)
DECL|field|stateRepository
specifier|private
name|MemoryStateRepository
name|stateRepository
decl_stmt|;
annotation|@
name|Override
DECL|method|doPreSetup ()
specifier|protected
name|void
name|doPreSetup
parameter_list|()
throws|throws
name|Exception
block|{
name|Properties
name|props
init|=
name|getDefaultProperties
argument_list|()
decl_stmt|;
name|producer
operator|=
operator|new
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|clients
operator|.
name|producer
operator|.
name|KafkaProducer
argument_list|<>
argument_list|(
name|props
argument_list|)
expr_stmt|;
comment|// Create the topic with 2 partitions + send 10 messages (5 in each partitions)
name|kafkaBroker
operator|.
name|createTopic
argument_list|(
name|TOPIC
argument_list|,
literal|2
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|producer
operator|.
name|send
argument_list|(
operator|new
name|ProducerRecord
argument_list|<>
argument_list|(
name|TOPIC
argument_list|,
name|i
operator|%
literal|2
argument_list|,
literal|"key"
argument_list|,
literal|"message-"
operator|+
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Create the state repository with some initial offsets
name|stateRepository
operator|=
operator|new
name|MemoryStateRepository
argument_list|()
expr_stmt|;
name|stateRepository
operator|.
name|setState
argument_list|(
name|TOPIC
operator|+
literal|"/0"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|stateRepository
operator|.
name|setState
argument_list|(
name|TOPIC
operator|+
literal|"/1"
argument_list|,
literal|"3"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|after ()
specifier|public
name|void
name|after
parameter_list|()
block|{
if|if
condition|(
name|producer
operator|!=
literal|null
condition|)
block|{
name|producer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|stateRepository
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * Given an offset repository with values      * When consuming with this repository      * Then we're consuming from the saved offsets      */
annotation|@
name|Test
DECL|method|shouldResumeFromAnyParticularOffset ()
specifier|public
name|void
name|shouldResumeFromAnyParticularOffset
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|result
operator|.
name|expectedBodiesReceivedInAnyOrder
argument_list|(
literal|"message-6"
argument_list|,
literal|"message-8"
argument_list|,
literal|"message-9"
argument_list|)
expr_stmt|;
name|result
operator|.
name|assertIsSatisfied
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
comment|// to give the local state some buffer
name|TimeUnit
operator|.
name|MILLISECONDS
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"partition-0"
argument_list|,
literal|"4"
argument_list|,
name|stateRepository
operator|.
name|getState
argument_list|(
name|TOPIC
operator|+
literal|"/0"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"partition-1"
argument_list|,
literal|"4"
argument_list|,
name|stateRepository
operator|.
name|getState
argument_list|(
name|TOPIC
operator|+
literal|"/1"
argument_list|)
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
literal|"kafka:"
operator|+
name|TOPIC
operator|+
literal|"?groupId=A"
operator|+
literal|"&autoCommitIntervalMs=1000"
operator|+
literal|"&autoOffsetReset=earliest"
comment|// Ask to start from the beginning if we have unknown offset
operator|+
literal|"&consumersCount=2"
comment|// We have 2 partitions, we want 1 consumer per partition
operator|+
literal|"&offsetRepository=#offset"
argument_list|)
comment|// Keep the offset in our repository
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

