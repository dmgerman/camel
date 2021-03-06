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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Endpoint
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
name|Before
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
DECL|class|KafkaConsumerBatchSizeTest
specifier|public
class|class
name|KafkaConsumerBatchSizeTest
extends|extends
name|BaseEmbeddedKafkaTest
block|{
DECL|field|TOPIC
specifier|public
specifier|static
specifier|final
name|String
name|TOPIC
init|=
literal|"test"
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"kafka:"
operator|+
name|TOPIC
operator|+
literal|"?autoOffsetReset=earliest"
operator|+
literal|"&autoCommitEnable=false"
operator|+
literal|"&consumerStreams=10"
argument_list|)
DECL|field|from
specifier|private
name|Endpoint
name|from
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:result"
argument_list|)
DECL|field|to
specifier|private
name|MockEndpoint
name|to
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
name|Before
DECL|method|before ()
specifier|public
name|void
name|before
parameter_list|()
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
name|from
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|to
argument_list|(
name|to
argument_list|)
operator|.
name|setId
argument_list|(
literal|"First"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|kafkaMessagesIsConsumedByCamel ()
specifier|public
name|void
name|kafkaMessagesIsConsumedByCamel
parameter_list|()
throws|throws
name|Exception
block|{
comment|//First 2 must not be committed since batch size is 3
name|to
operator|.
name|expectedBodiesReceivedInAnyOrder
argument_list|(
literal|"m1"
argument_list|,
literal|"m2"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|k
init|=
literal|1
init|;
name|k
operator|<=
literal|2
condition|;
name|k
operator|++
control|)
block|{
name|String
name|msg
init|=
literal|"m"
operator|+
name|k
decl_stmt|;
name|ProducerRecord
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|data
init|=
operator|new
name|ProducerRecord
argument_list|<>
argument_list|(
name|TOPIC
argument_list|,
literal|"1"
argument_list|,
name|msg
argument_list|)
decl_stmt|;
name|producer
operator|.
name|send
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
name|to
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|to
operator|.
name|reset
argument_list|()
expr_stmt|;
name|to
operator|.
name|expectedBodiesReceivedInAnyOrder
argument_list|(
literal|"m3"
argument_list|,
literal|"m4"
argument_list|,
literal|"m5"
argument_list|,
literal|"m6"
argument_list|,
literal|"m7"
argument_list|,
literal|"m8"
argument_list|,
literal|"m9"
argument_list|,
literal|"m10"
argument_list|)
expr_stmt|;
comment|//Restart endpoint,
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|stopRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|startRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
comment|//Second route must wake up and consume all from scratch and commit 9 consumed
for|for
control|(
name|int
name|k
init|=
literal|3
init|;
name|k
operator|<=
literal|10
condition|;
name|k
operator|++
control|)
block|{
name|String
name|msg
init|=
literal|"m"
operator|+
name|k
decl_stmt|;
name|ProducerRecord
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|data
init|=
operator|new
name|ProducerRecord
argument_list|<>
argument_list|(
name|TOPIC
argument_list|,
literal|"1"
argument_list|,
name|msg
argument_list|)
decl_stmt|;
name|producer
operator|.
name|send
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
name|to
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

