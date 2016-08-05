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
name|net
operator|.
name|URISyntaxException
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
name|Exchange
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
name|ConsumerRecord
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
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
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
name|assertTrue
import|;
end_import

begin_class
DECL|class|KafkaEndpointTest
specifier|public
class|class
name|KafkaEndpointTest
block|{
annotation|@
name|Test
DECL|method|testCreatingKafkaExchangeSetsHeaders ()
specifier|public
name|void
name|testCreatingKafkaExchangeSetsHeaders
parameter_list|()
throws|throws
name|URISyntaxException
block|{
name|KafkaEndpoint
name|endpoint
init|=
operator|new
name|KafkaEndpoint
argument_list|(
literal|"kafka:localhost"
argument_list|,
operator|new
name|KafkaComponent
argument_list|()
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setBrokers
argument_list|(
literal|"localhost"
argument_list|)
expr_stmt|;
name|ConsumerRecord
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|record
init|=
operator|new
name|ConsumerRecord
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
literal|"topic"
argument_list|,
literal|4
argument_list|,
literal|56
argument_list|,
literal|"somekey"
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createKafkaExchange
argument_list|(
name|record
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"somekey"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KafkaConstants
operator|.
name|KEY
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"topic"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KafkaConstants
operator|.
name|TOPIC
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KafkaConstants
operator|.
name|PARTITION
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|56L
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KafkaConstants
operator|.
name|OFFSET
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|assertSingleton ()
specifier|public
name|void
name|assertSingleton
parameter_list|()
throws|throws
name|URISyntaxException
block|{
name|KafkaEndpoint
name|endpoint
init|=
operator|new
name|KafkaEndpoint
argument_list|(
literal|"kafka:localhost"
argument_list|,
operator|new
name|KafkaComponent
argument_list|()
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setBrokers
argument_list|(
literal|"localhost"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|endpoint
operator|.
name|isSingleton
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

