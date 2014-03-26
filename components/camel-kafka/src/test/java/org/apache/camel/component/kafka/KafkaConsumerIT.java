begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|io
operator|.
name|IOException
import|;
end_import

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
name|kafka
operator|.
name|javaapi
operator|.
name|producer
operator|.
name|Producer
import|;
end_import

begin_import
import|import
name|kafka
operator|.
name|producer
operator|.
name|KeyedMessage
import|;
end_import

begin_import
import|import
name|kafka
operator|.
name|producer
operator|.
name|ProducerConfig
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
name|camel
operator|.
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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

begin_comment
comment|/**  * The Producer IT tests require a Kafka broker running on 9092 and a zookeeper instance running on 2181.  * The broker must have a topic called test created.  */
end_comment

begin_class
DECL|class|KafkaConsumerIT
specifier|public
class|class
name|KafkaConsumerIT
extends|extends
name|CamelTestSupport
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
name|uri
operator|=
literal|"kafka:localhost:9092?topic="
operator|+
name|TOPIC
operator|+
literal|"&zookeeperHost=localhost&zookeeperPort=2181&groupId=group1"
argument_list|)
DECL|field|from
specifier|private
name|Endpoint
name|from
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result"
argument_list|)
DECL|field|to
specifier|private
name|MockEndpoint
name|to
decl_stmt|;
DECL|field|producer
specifier|private
name|Producer
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
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"metadata.broker.list"
argument_list|,
literal|"localhost:9092"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"serializer.class"
argument_list|,
literal|"kafka.serializer.StringEncoder"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"partitioner.class"
argument_list|,
literal|"org.apache.camel.component.kafka.SimplePartitioner"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"request.required.acks"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|ProducerConfig
name|config
init|=
operator|new
name|ProducerConfig
argument_list|(
name|props
argument_list|)
decl_stmt|;
name|producer
operator|=
operator|new
name|Producer
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|config
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
name|producer
operator|.
name|close
argument_list|()
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
name|from
argument_list|)
operator|.
name|to
argument_list|(
name|to
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|kaftMessageIsConsumedByCamel ()
specifier|public
name|void
name|kaftMessageIsConsumedByCamel
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|IOException
block|{
name|to
operator|.
name|expectedMessageCount
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|to
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"message-0"
argument_list|,
literal|"message-1"
argument_list|,
literal|"message-2"
argument_list|,
literal|"message-3"
argument_list|,
literal|"message-4"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|k
init|=
literal|0
init|;
name|k
operator|<
literal|5
condition|;
name|k
operator|++
control|)
block|{
name|String
name|msg
init|=
literal|"message-"
operator|+
name|k
decl_stmt|;
name|KeyedMessage
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|data
init|=
operator|new
name|KeyedMessage
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
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
argument_list|(
literal|3000
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

