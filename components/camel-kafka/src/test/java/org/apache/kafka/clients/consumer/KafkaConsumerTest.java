begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.kafka.clients.consumer
package|package
name|org
operator|.
name|apache
operator|.
name|kafka
operator|.
name|clients
operator|.
name|consumer
package|;
end_package

begin_import
import|import
name|org
operator|.
name|hamcrest
operator|.
name|core
operator|.
name|IsNot
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
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
name|mockito
operator|.
name|Mockito
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|runners
operator|.
name|MockitoJUnitRunner
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|core
operator|.
name|IsNull
operator|.
name|nullValue
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertThat
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
annotation|@
name|RunWith
argument_list|(
name|MockitoJUnitRunner
operator|.
name|class
argument_list|)
DECL|class|KafkaConsumerTest
specifier|public
class|class
name|KafkaConsumerTest
block|{
annotation|@
name|Mock
DECL|field|kafkaConsumer
specifier|private
name|KafkaConsumer
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|kafkaConsumer
decl_stmt|;
annotation|@
name|Before
DECL|method|init ()
specifier|public
name|void
name|init
parameter_list|()
block|{
name|when
argument_list|(
name|kafkaConsumer
operator|.
name|poll
argument_list|(
literal|1000
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|ConsumerRecords
operator|.
name|empty
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPollGivenReturnsEmptyConsumerRecordShouldNotBeNull ()
specifier|public
name|void
name|testPollGivenReturnsEmptyConsumerRecordShouldNotBeNull
parameter_list|()
block|{
name|ConsumerRecords
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|consumerRecords
init|=
name|kafkaConsumer
operator|.
name|poll
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|consumerRecords
argument_list|,
name|IsNot
operator|.
name|not
argument_list|(
name|nullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPollGivenReturnsEmptyPartitionsShouldNotBeNull ()
specifier|public
name|void
name|testPollGivenReturnsEmptyPartitionsShouldNotBeNull
parameter_list|()
block|{
name|ConsumerRecords
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|consumerRecords
init|=
name|kafkaConsumer
operator|.
name|poll
argument_list|(
literal|1000
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|consumerRecords
operator|.
name|partitions
argument_list|()
argument_list|,
name|IsNot
operator|.
name|not
argument_list|(
name|nullValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

