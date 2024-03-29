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
name|CountDownLatch
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
DECL|class|KafkaConsumerRebalancePartitionRevokeTest
specifier|public
class|class
name|KafkaConsumerRebalancePartitionRevokeTest
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
literal|"offset-rebalance"
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
annotation|@
name|BindToRegistry
argument_list|(
literal|"offset"
argument_list|)
DECL|field|stateRepository
specifier|private
name|OffsetStateRepository
name|stateRepository
decl_stmt|;
DECL|field|messagesLatch
specifier|private
name|CountDownLatch
name|messagesLatch
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
literal|2
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
name|messagesLatch
operator|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|stateRepository
operator|=
operator|new
name|OffsetStateRepository
argument_list|(
name|messagesLatch
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
block|}
annotation|@
name|Test
DECL|method|ensurePartitionRevokeCallsWithLastProcessedOffset ()
specifier|public
name|void
name|ensurePartitionRevokeCallsWithLastProcessedOffset
parameter_list|()
throws|throws
name|Exception
block|{
name|boolean
name|partitionRevokeCalled
init|=
name|messagesLatch
operator|.
name|await
argument_list|(
literal|30000
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"StateRepository.setState should have been called with offset>= 0 for topic"
operator|+
name|TOPIC
operator|+
literal|". Remaining count : "
operator|+
name|messagesLatch
operator|.
name|getCount
argument_list|()
argument_list|,
name|partitionRevokeCalled
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
literal|"?groupId="
operator|+
name|TOPIC
operator|+
literal|"_GROUP"
operator|+
literal|"&autoCommitIntervalMs=1000"
operator|+
literal|"&autoOffsetReset=earliest"
operator|+
literal|"&consumersCount=1"
operator|+
literal|"&offsetRepository=#offset"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"consumer-rebalance-route"
argument_list|)
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
DECL|class|OffsetStateRepository
specifier|public
class|class
name|OffsetStateRepository
extends|extends
name|MemoryStateRepository
block|{
DECL|field|messagesLatch
name|CountDownLatch
name|messagesLatch
decl_stmt|;
DECL|method|OffsetStateRepository (CountDownLatch messagesLatch)
specifier|public
name|OffsetStateRepository
parameter_list|(
name|CountDownLatch
name|messagesLatch
parameter_list|)
block|{
name|this
operator|.
name|messagesLatch
operator|=
name|messagesLatch
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
block|{         }
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{         }
annotation|@
name|Override
DECL|method|getState (String key)
specifier|public
name|String
name|getState
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|super
operator|.
name|getState
argument_list|(
name|key
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|setState (String key, String value)
specifier|public
name|void
name|setState
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|key
operator|.
name|contains
argument_list|(
name|TOPIC
argument_list|)
operator|&&
name|messagesLatch
operator|.
name|getCount
argument_list|()
operator|>
literal|0
operator|&&
name|Long
operator|.
name|parseLong
argument_list|(
name|value
argument_list|)
operator|>=
literal|0
condition|)
block|{
name|messagesLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|setState
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

