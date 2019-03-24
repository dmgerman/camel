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
name|JndiRegistry
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
name|spi
operator|.
name|StateRepository
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
DECL|class|KafkaConsumerRebalanceTest
specifier|public
class|class
name|KafkaConsumerRebalanceTest
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
name|uri
operator|=
literal|"mock:result"
argument_list|)
DECL|field|result
specifier|private
name|MockEndpoint
name|result
decl_stmt|;
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
name|messagesLatch
operator|=
operator|new
name|CountDownLatch
argument_list|(
literal|2
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
name|Test
DECL|method|offsetGetStateMustHaveBeenCalledTwice ()
specifier|public
name|void
name|offsetGetStateMustHaveBeenCalledTwice
parameter_list|()
throws|throws
name|Exception
block|{
name|boolean
name|offsetGetStateCalled
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
literal|"StateRepository.getState should have been called twice for topic "
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
name|offsetGetStateCalled
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|registry
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"offset"
argument_list|,
name|stateRepository
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
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
literal|"&autoOffsetReset=latest"
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
implements|implements
name|StateRepository
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
block|{
DECL|field|messagesLatch
name|CountDownLatch
name|messagesLatch
init|=
literal|null
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
throws|throws
name|Exception
block|{         }
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
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
if|if
condition|(
name|key
operator|.
name|contains
argument_list|(
name|TOPIC
argument_list|)
condition|)
block|{
name|messagesLatch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
return|return
literal|"-1"
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
block|{         }
block|}
block|}
end_class

end_unit

