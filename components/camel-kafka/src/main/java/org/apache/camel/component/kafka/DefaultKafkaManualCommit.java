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
name|util
operator|.
name|Collections
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
name|clients
operator|.
name|consumer
operator|.
name|OffsetAndMetadata
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
DECL|class|DefaultKafkaManualCommit
specifier|public
class|class
name|DefaultKafkaManualCommit
implements|implements
name|KafkaManualCommit
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
name|DefaultKafkaManualCommit
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|consumer
specifier|private
specifier|final
name|KafkaConsumer
name|consumer
decl_stmt|;
DECL|field|topicName
specifier|private
specifier|final
name|String
name|topicName
decl_stmt|;
DECL|field|threadId
specifier|private
specifier|final
name|String
name|threadId
decl_stmt|;
DECL|field|offsetRepository
specifier|private
specifier|final
name|StateRepository
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|offsetRepository
decl_stmt|;
DECL|field|partition
specifier|private
specifier|final
name|TopicPartition
name|partition
decl_stmt|;
DECL|field|recordOffset
specifier|private
specifier|final
name|long
name|recordOffset
decl_stmt|;
DECL|method|DefaultKafkaManualCommit (KafkaConsumer consumer, String topicName, String threadId, StateRepository<String, String> offsetRepository, TopicPartition partition, long recordOffset)
specifier|public
name|DefaultKafkaManualCommit
parameter_list|(
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
name|recordOffset
parameter_list|)
block|{
name|this
operator|.
name|consumer
operator|=
name|consumer
expr_stmt|;
name|this
operator|.
name|topicName
operator|=
name|topicName
expr_stmt|;
name|this
operator|.
name|threadId
operator|=
name|threadId
expr_stmt|;
name|this
operator|.
name|offsetRepository
operator|=
name|offsetRepository
expr_stmt|;
name|this
operator|.
name|partition
operator|=
name|partition
expr_stmt|;
name|this
operator|.
name|recordOffset
operator|=
name|recordOffset
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|commitSync ()
specifier|public
name|void
name|commitSync
parameter_list|()
block|{
name|commitOffset
argument_list|(
name|offsetRepository
argument_list|,
name|partition
argument_list|,
name|recordOffset
argument_list|)
expr_stmt|;
block|}
DECL|method|commitOffset (StateRepository<String, String> offsetRepository, TopicPartition partition, long recordOffset)
specifier|protected
name|void
name|commitOffset
parameter_list|(
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
name|recordOffset
parameter_list|)
block|{
if|if
condition|(
name|recordOffset
operator|!=
operator|-
literal|1
condition|)
block|{
if|if
condition|(
name|offsetRepository
operator|!=
literal|null
condition|)
block|{
name|offsetRepository
operator|.
name|setState
argument_list|(
name|serializeOffsetKey
argument_list|(
name|partition
argument_list|)
argument_list|,
name|serializeOffsetValue
argument_list|(
name|recordOffset
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"CommitSync {} from topic {} with offset: {}"
argument_list|,
name|threadId
argument_list|,
name|topicName
argument_list|,
name|recordOffset
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|commitSync
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
name|partition
argument_list|,
operator|new
name|OffsetAndMetadata
argument_list|(
name|recordOffset
operator|+
literal|1
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|serializeOffsetKey (TopicPartition topicPartition)
specifier|protected
name|String
name|serializeOffsetKey
parameter_list|(
name|TopicPartition
name|topicPartition
parameter_list|)
block|{
return|return
name|topicPartition
operator|.
name|topic
argument_list|()
operator|+
literal|'/'
operator|+
name|topicPartition
operator|.
name|partition
argument_list|()
return|;
block|}
DECL|method|serializeOffsetValue (long offset)
specifier|protected
name|String
name|serializeOffsetValue
parameter_list|(
name|long
name|offset
parameter_list|)
block|{
return|return
name|String
operator|.
name|valueOf
argument_list|(
name|offset
argument_list|)
return|;
block|}
DECL|method|getConsumer ()
specifier|public
name|KafkaConsumer
name|getConsumer
parameter_list|()
block|{
return|return
name|consumer
return|;
block|}
DECL|method|getTopicName ()
specifier|public
name|String
name|getTopicName
parameter_list|()
block|{
return|return
name|topicName
return|;
block|}
DECL|method|getThreadId ()
specifier|public
name|String
name|getThreadId
parameter_list|()
block|{
return|return
name|threadId
return|;
block|}
DECL|method|getOffsetRepository ()
specifier|public
name|StateRepository
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getOffsetRepository
parameter_list|()
block|{
return|return
name|offsetRepository
return|;
block|}
DECL|method|getPartition ()
specifier|public
name|TopicPartition
name|getPartition
parameter_list|()
block|{
return|return
name|partition
return|;
block|}
DECL|method|getRecordOffset ()
specifier|public
name|long
name|getRecordOffset
parameter_list|()
block|{
return|return
name|recordOffset
return|;
block|}
block|}
end_class

end_unit

