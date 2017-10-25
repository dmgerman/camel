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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
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
name|apache
operator|.
name|kafka
operator|.
name|clients
operator|.
name|consumer
operator|.
name|KafkaConsumer
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
name|common
operator|.
name|TopicPartition
import|;
end_import

begin_class
DECL|class|DefaultKafkaManualCommitFactory
specifier|public
class|class
name|DefaultKafkaManualCommitFactory
implements|implements
name|KafkaManualCommitFactory
block|{
annotation|@
name|Override
DECL|method|newInstance (Exchange exchange, KafkaConsumer consumer, String topicName, String threadId, StateRepository<String, String> offsetRepository, TopicPartition partition, long partitionLastOffset)
specifier|public
name|KafkaManualCommit
name|newInstance
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|KafkaConsumer
name|consumer
parameter_list|,
name|String
name|topicName
parameter_list|,
name|String
name|threadId
parameter_list|,
name|StateRepository
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|offsetRepository
parameter_list|,
name|TopicPartition
name|partition
parameter_list|,
name|long
name|partitionLastOffset
parameter_list|)
block|{
return|return
operator|new
name|DefaultKafkaManualCommit
argument_list|(
name|consumer
argument_list|,
name|topicName
argument_list|,
name|threadId
argument_list|,
name|offsetRepository
argument_list|,
name|partition
argument_list|,
name|partitionLastOffset
argument_list|)
return|;
block|}
block|}
end_class

end_unit

