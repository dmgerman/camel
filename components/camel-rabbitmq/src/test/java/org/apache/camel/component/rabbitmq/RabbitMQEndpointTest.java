begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.rabbitmq
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|rabbitmq
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
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
name|ThreadPoolExecutor
import|;
end_import

begin_import
import|import
name|com
operator|.
name|rabbitmq
operator|.
name|client
operator|.
name|Envelope
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
name|Test
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

begin_class
DECL|class|RabbitMQEndpointTest
specifier|public
class|class
name|RabbitMQEndpointTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|envelope
specifier|private
name|Envelope
name|envelope
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|Envelope
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Test
DECL|method|testCreatingRabbitExchangeSetsHeaders ()
specifier|public
name|void
name|testCreatingRabbitExchangeSetsHeaders
parameter_list|()
throws|throws
name|Exception
block|{
name|RabbitMQEndpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"rabbitmq:localhost/exchange"
argument_list|,
name|RabbitMQEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|routingKey
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|String
name|exchangeName
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
name|long
name|tag
init|=
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|hashCode
argument_list|()
decl_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|envelope
operator|.
name|getRoutingKey
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|routingKey
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|envelope
operator|.
name|getExchange
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|exchangeName
argument_list|)
expr_stmt|;
name|Mockito
operator|.
name|when
argument_list|(
name|envelope
operator|.
name|getDeliveryTag
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|tag
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createRabbitExchange
argument_list|(
name|envelope
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|exchangeName
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|EXCHANGE_NAME
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|routingKey
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|ROUTING_KEY
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|tag
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|RabbitMQConstants
operator|.
name|DELIVERY_TAG
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|creatingExecutorUsesThreadPoolSettings ()
specifier|public
name|void
name|creatingExecutorUsesThreadPoolSettings
parameter_list|()
throws|throws
name|Exception
block|{
name|RabbitMQEndpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"rabbitmq:localhost/exchange?threadPoolSize=20"
argument_list|,
name|RabbitMQEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|endpoint
operator|.
name|getThreadPoolSize
argument_list|()
argument_list|)
expr_stmt|;
name|ThreadPoolExecutor
name|executor
init|=
name|assertIsInstanceOf
argument_list|(
name|ThreadPoolExecutor
operator|.
name|class
argument_list|,
name|endpoint
operator|.
name|createExecutor
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|20
argument_list|,
name|executor
operator|.
name|getCorePoolSize
argument_list|()
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
name|Exception
block|{
name|RabbitMQEndpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"rabbitmq:localhost/exchange"
argument_list|,
name|RabbitMQEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
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

