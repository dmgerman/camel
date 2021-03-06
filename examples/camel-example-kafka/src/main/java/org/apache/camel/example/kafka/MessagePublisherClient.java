begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.kafka
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
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
name|Calendar
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
name|Map
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
name|CamelContext
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
name|ProducerTemplate
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
name|kafka
operator|.
name|KafkaComponent
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
name|kafka
operator|.
name|KafkaConstants
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
name|DefaultCamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|MessagePublisherClient
specifier|public
specifier|final
class|class
name|MessagePublisherClient
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MessagePublisherClient
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|MessagePublisherClient ()
specifier|private
name|MessagePublisherClient
parameter_list|()
block|{ }
DECL|method|main (String[] args)
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"About to run Kafka-camel integration..."
argument_list|)
expr_stmt|;
name|String
name|testKafkaMessage
init|=
literal|"Test Message from  MessagePublisherClient "
operator|+
name|Calendar
operator|.
name|getInstance
argument_list|()
operator|.
name|getTime
argument_list|()
decl_stmt|;
name|CamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
comment|// Add route to send messages to Kafka
name|camelContext
operator|.
name|addRoutes
argument_list|(
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|camelContext
operator|.
name|getPropertiesComponent
argument_list|()
operator|.
name|setLocation
argument_list|(
literal|"classpath:application.properties"
argument_list|)
expr_stmt|;
comment|// setup kafka component with the brokers
name|KafkaComponent
name|kafka
init|=
operator|new
name|KafkaComponent
argument_list|()
decl_stmt|;
name|kafka
operator|.
name|setBrokers
argument_list|(
literal|"{{kafka.host}}:{{kafka.port}}"
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"kafka"
argument_list|,
name|kafka
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:kafkaStart"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"DirectToKafka"
argument_list|)
operator|.
name|to
argument_list|(
literal|"kafka:{{producer.topic}}"
argument_list|)
operator|.
name|log
argument_list|(
literal|"${headers}"
argument_list|)
expr_stmt|;
comment|// Topic can be set in header as well.
name|from
argument_list|(
literal|"direct:kafkaStartNoTopic"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"kafkaStartNoTopic"
argument_list|)
operator|.
name|to
argument_list|(
literal|"kafka:dummy"
argument_list|)
operator|.
name|log
argument_list|(
literal|"${headers}"
argument_list|)
expr_stmt|;
comment|// Use custom partitioner based on the key.
name|from
argument_list|(
literal|"direct:kafkaStartWithPartitioner"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"kafkaStartWithPartitioner"
argument_list|)
operator|.
name|to
argument_list|(
literal|"kafka:{{producer.topic}}?partitioner={{producer.partitioner}}"
argument_list|)
operator|.
name|log
argument_list|(
literal|"${headers}"
argument_list|)
expr_stmt|;
comment|// Takes input from the command line.
name|from
argument_list|(
literal|"stream:in"
argument_list|)
operator|.
name|setHeader
argument_list|(
name|KafkaConstants
operator|.
name|PARTITION_KEY
argument_list|,
name|simple
argument_list|(
literal|"0"
argument_list|)
argument_list|)
operator|.
name|setHeader
argument_list|(
name|KafkaConstants
operator|.
name|KEY
argument_list|,
name|simple
argument_list|(
literal|"1"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:kafkaStart"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|ProducerTemplate
name|producerTemplate
init|=
name|camelContext
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|camelContext
operator|.
name|start
argument_list|()
expr_stmt|;
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
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|KafkaConstants
operator|.
name|PARTITION_KEY
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|KafkaConstants
operator|.
name|KEY
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|producerTemplate
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:kafkaStart"
argument_list|,
name|testKafkaMessage
argument_list|,
name|headers
argument_list|)
expr_stmt|;
comment|// Send with topicName in header
name|testKafkaMessage
operator|=
literal|"TOPIC "
operator|+
name|testKafkaMessage
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|KafkaConstants
operator|.
name|KEY
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|KafkaConstants
operator|.
name|TOPIC
argument_list|,
literal|"TestLog"
argument_list|)
expr_stmt|;
name|producerTemplate
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:kafkaStartNoTopic"
argument_list|,
name|testKafkaMessage
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|testKafkaMessage
operator|=
literal|"PART 0 :  "
operator|+
name|testKafkaMessage
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|newHeader
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|newHeader
operator|.
name|put
argument_list|(
name|KafkaConstants
operator|.
name|KEY
argument_list|,
literal|"AB"
argument_list|)
expr_stmt|;
comment|// This should go to partition 0
name|producerTemplate
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:kafkaStartWithPartitioner"
argument_list|,
name|testKafkaMessage
argument_list|,
name|newHeader
argument_list|)
expr_stmt|;
name|testKafkaMessage
operator|=
literal|"PART 1 :  "
operator|+
name|testKafkaMessage
expr_stmt|;
name|newHeader
operator|.
name|put
argument_list|(
name|KafkaConstants
operator|.
name|KEY
argument_list|,
literal|"ABC"
argument_list|)
expr_stmt|;
comment|// This should go to partition 1
name|producerTemplate
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:kafkaStartWithPartitioner"
argument_list|,
name|testKafkaMessage
argument_list|,
name|newHeader
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Successfully published event to Kafka."
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Enter text on the line below : [Press Ctrl-C to exit.] "
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|5
operator|*
literal|60
operator|*
literal|1000
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

