begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mybatis
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mybatis
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayDeque
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Queue
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
name|Processor
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
name|support
operator|.
name|DefaultExchange
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_class
DECL|class|MyBatisConsumerIsolatedTest
specifier|public
class|class
name|MyBatisConsumerIsolatedTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
DECL|method|shouldRespectBatchSize ()
specifier|public
name|void
name|shouldRespectBatchSize
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Given
name|int
name|batchSize
init|=
literal|5
decl_stmt|;
name|MyBatisConsumer
name|consumer
init|=
operator|new
name|MyBatisConsumer
argument_list|(
name|mock
argument_list|(
name|MyBatisEndpoint
operator|.
name|class
argument_list|)
argument_list|,
name|mock
argument_list|(
name|Processor
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|consumer
operator|.
name|setMaxMessagesPerPoll
argument_list|(
name|batchSize
argument_list|)
expr_stmt|;
name|Queue
argument_list|<
name|Object
argument_list|>
name|emptyMessageQueue
init|=
operator|new
name|ArrayDeque
argument_list|<>
argument_list|()
decl_stmt|;
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
name|MyBatisConsumer
operator|.
name|DataHolder
name|dataHolder
init|=
operator|new
name|MyBatisConsumer
operator|.
name|DataHolder
argument_list|()
decl_stmt|;
name|dataHolder
operator|.
name|exchange
operator|=
operator|new
name|DefaultExchange
argument_list|(
name|mock
argument_list|(
name|CamelContext
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|emptyMessageQueue
operator|.
name|add
argument_list|(
name|dataHolder
argument_list|)
expr_stmt|;
block|}
comment|// When
name|int
name|processedMessages
init|=
name|consumer
operator|.
name|processBatch
argument_list|(
name|emptyMessageQueue
argument_list|)
decl_stmt|;
comment|// Then
name|assertEquals
argument_list|(
name|batchSize
argument_list|,
name|processedMessages
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

