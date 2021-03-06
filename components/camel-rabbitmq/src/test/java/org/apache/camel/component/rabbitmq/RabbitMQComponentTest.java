begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|com
operator|.
name|rabbitmq
operator|.
name|client
operator|.
name|ConnectionFactory
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
name|impl
operator|.
name|DefaultCamelContext
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
name|SimpleRegistry
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
DECL|class|RabbitMQComponentTest
specifier|public
class|class
name|RabbitMQComponentTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
name|super
operator|.
name|isUseRouteBuilder
argument_list|()
return|;
block|}
annotation|@
name|Test
DECL|method|testDefaultProperties ()
specifier|public
name|void
name|testDefaultProperties
parameter_list|()
throws|throws
name|Exception
block|{
name|RabbitMQEndpoint
name|endpoint
init|=
name|createEndpoint
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|14
argument_list|,
name|endpoint
operator|.
name|getPortNumber
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
argument_list|,
name|endpoint
operator|.
name|getThreadPoolSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|endpoint
operator|.
name|isAutoAck
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|endpoint
operator|.
name|isAutoDelete
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|endpoint
operator|.
name|isDurable
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|endpoint
operator|.
name|isExclusiveConsumer
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|endpoint
operator|.
name|isAllowNullHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"direct"
argument_list|,
name|endpoint
operator|.
name|getExchangeType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ConnectionFactory
operator|.
name|DEFAULT_CONNECTION_TIMEOUT
argument_list|,
name|endpoint
operator|.
name|getConnectionTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ConnectionFactory
operator|.
name|DEFAULT_CHANNEL_MAX
argument_list|,
name|endpoint
operator|.
name|getRequestedChannelMax
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ConnectionFactory
operator|.
name|DEFAULT_FRAME_MAX
argument_list|,
name|endpoint
operator|.
name|getRequestedFrameMax
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ConnectionFactory
operator|.
name|DEFAULT_HEARTBEAT
argument_list|,
name|endpoint
operator|.
name|getRequestedHeartbeat
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|endpoint
operator|.
name|getConnectionFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPropertiesSet ()
specifier|public
name|void
name|testPropertiesSet
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"username"
argument_list|,
literal|"coldplay"
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"password"
argument_list|,
literal|"chrism"
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"autoAck"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"vhost"
argument_list|,
literal|"vman"
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"threadPoolSize"
argument_list|,
literal|515
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"portNumber"
argument_list|,
literal|14123
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"hostname"
argument_list|,
literal|"special.host"
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"queue"
argument_list|,
literal|"queuey"
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"exchangeType"
argument_list|,
literal|"topic"
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"connectionTimeout"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"requestedChannelMax"
argument_list|,
literal|456
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"requestedFrameMax"
argument_list|,
literal|789
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"requestedHeartbeat"
argument_list|,
literal|321
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"exclusiveConsumer"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"allowNullHeaders"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|RabbitMQEndpoint
name|endpoint
init|=
name|createEndpoint
argument_list|(
name|params
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"chrism"
argument_list|,
name|endpoint
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"coldplay"
argument_list|,
name|endpoint
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"queuey"
argument_list|,
name|endpoint
operator|.
name|getQueue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"vman"
argument_list|,
name|endpoint
operator|.
name|getVhost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"special.host"
argument_list|,
name|endpoint
operator|.
name|getHostname
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|14123
argument_list|,
name|endpoint
operator|.
name|getPortNumber
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|515
argument_list|,
name|endpoint
operator|.
name|getThreadPoolSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|endpoint
operator|.
name|isAutoAck
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|endpoint
operator|.
name|isAutoDelete
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|endpoint
operator|.
name|isDurable
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"topic"
argument_list|,
name|endpoint
operator|.
name|getExchangeType
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|endpoint
operator|.
name|getConnectionTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|456
argument_list|,
name|endpoint
operator|.
name|getRequestedChannelMax
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|789
argument_list|,
name|endpoint
operator|.
name|getRequestedFrameMax
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|321
argument_list|,
name|endpoint
operator|.
name|getRequestedHeartbeat
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|endpoint
operator|.
name|isExclusiveConsumer
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|endpoint
operator|.
name|isAllowNullHeaders
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createEndpoint (Map<String, Object> params)
specifier|private
name|RabbitMQEndpoint
name|createEndpoint
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|uri
init|=
literal|"rabbitmq:special.host:14/queuey"
decl_stmt|;
name|String
name|remaining
init|=
literal|"special.host:14/queuey"
decl_stmt|;
name|RabbitMQComponent
name|comp
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"rabbitmq"
argument_list|,
name|RabbitMQComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|comp
operator|.
name|setAutoDetectConnectionFactory
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return
operator|(
name|RabbitMQEndpoint
operator|)
name|comp
operator|.
name|createEndpoint
argument_list|(
name|uri
argument_list|,
name|params
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testConnectionFactoryRef ()
specifier|public
name|void
name|testConnectionFactoryRef
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleRegistry
name|registry
init|=
operator|new
name|SimpleRegistry
argument_list|()
decl_stmt|;
name|ConnectionFactory
name|connectionFactoryMock
init|=
name|Mockito
operator|.
name|mock
argument_list|(
name|ConnectionFactory
operator|.
name|class
argument_list|)
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"connectionFactoryMock"
argument_list|,
name|connectionFactoryMock
argument_list|)
expr_stmt|;
name|CamelContext
name|defaultContext
init|=
operator|new
name|DefaultCamelContext
argument_list|(
name|registry
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|params
operator|.
name|put
argument_list|(
literal|"connectionFactory"
argument_list|,
literal|"#connectionFactoryMock"
argument_list|)
expr_stmt|;
name|RabbitMQEndpoint
name|endpoint
init|=
operator|new
name|RabbitMQComponent
argument_list|(
name|defaultContext
argument_list|)
operator|.
name|createEndpoint
argument_list|(
literal|"rabbitmq:localhost/exchange"
argument_list|,
literal|"localhost/exchange"
argument_list|,
name|params
argument_list|)
decl_stmt|;
name|assertSame
argument_list|(
name|connectionFactoryMock
argument_list|,
name|endpoint
operator|.
name|getConnectionFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

