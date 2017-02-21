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

begin_class
DECL|class|KafkaConstants
specifier|public
specifier|final
class|class
name|KafkaConstants
block|{
DECL|field|PARTITION_KEY
specifier|public
specifier|static
specifier|final
name|String
name|PARTITION_KEY
init|=
literal|"kafka.PARTITION_KEY"
decl_stmt|;
DECL|field|PARTITION
specifier|public
specifier|static
specifier|final
name|String
name|PARTITION
init|=
literal|"kafka.PARTITION"
decl_stmt|;
DECL|field|KEY
specifier|public
specifier|static
specifier|final
name|String
name|KEY
init|=
literal|"kafka.KEY"
decl_stmt|;
DECL|field|TOPIC
specifier|public
specifier|static
specifier|final
name|String
name|TOPIC
init|=
literal|"kafka.TOPIC"
decl_stmt|;
DECL|field|OFFSET
specifier|public
specifier|static
specifier|final
name|String
name|OFFSET
init|=
literal|"kafka.OFFSET"
decl_stmt|;
DECL|field|LAST_RECORD_BEFORE_COMMIT
specifier|public
specifier|static
specifier|final
name|String
name|LAST_RECORD_BEFORE_COMMIT
init|=
literal|"kafka.LAST_RECORD_BEFORE_COMMIT"
decl_stmt|;
DECL|field|KAFKA_DEFAULT_ENCODER
specifier|public
specifier|static
specifier|final
name|String
name|KAFKA_DEFAULT_ENCODER
init|=
literal|"kafka.serializer.DefaultEncoder"
decl_stmt|;
DECL|field|KAFKA_STRING_ENCODER
specifier|public
specifier|static
specifier|final
name|String
name|KAFKA_STRING_ENCODER
init|=
literal|"kafka.serializer.StringEncoder"
decl_stmt|;
DECL|field|KAFKA_DEFAULT_SERIALIZER
specifier|public
specifier|static
specifier|final
name|String
name|KAFKA_DEFAULT_SERIALIZER
init|=
literal|"org.apache.kafka.common.serialization.StringSerializer"
decl_stmt|;
DECL|field|KAFKA_DEFAULT_DESERIALIZER
specifier|public
specifier|static
specifier|final
name|String
name|KAFKA_DEFAULT_DESERIALIZER
init|=
literal|"org.apache.kafka.common.serialization.StringDeserializer"
decl_stmt|;
DECL|field|KAFKA_DEFAULT_PARTITIONER
specifier|public
specifier|static
specifier|final
name|String
name|KAFKA_DEFAULT_PARTITIONER
init|=
literal|"org.apache.kafka.clients.producer.internals.DefaultPartitioner"
decl_stmt|;
DECL|field|PARTITIONER_RANGE_ASSIGNOR
specifier|public
specifier|static
specifier|final
name|String
name|PARTITIONER_RANGE_ASSIGNOR
init|=
literal|"org.apache.kafka.clients.consumer.RangeAssignor"
decl_stmt|;
DECL|field|KAFKA_RECORDMETA
specifier|public
specifier|static
specifier|final
name|String
name|KAFKA_RECORDMETA
init|=
literal|"org.apache.kafka.clients.producer.RecordMetadata"
decl_stmt|;
DECL|method|KafkaConstants ()
specifier|private
name|KafkaConstants
parameter_list|()
block|{
comment|// Utility class
block|}
block|}
end_class

end_unit

