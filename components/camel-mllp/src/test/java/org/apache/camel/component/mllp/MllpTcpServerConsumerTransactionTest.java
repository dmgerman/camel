begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mllp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mllp
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|ActiveMQConnectionFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|activemq
operator|.
name|junit
operator|.
name|EmbeddedActiveMQBroker
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
name|BindToRegistry
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
name|LoggingLevel
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
name|component
operator|.
name|sjms
operator|.
name|SjmsComponent
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
name|test
operator|.
name|AvailablePortFinder
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
name|junit
operator|.
name|rule
operator|.
name|mllp
operator|.
name|MllpClientResource
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
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|mllp
operator|.
name|Hl7TestMessageGenerator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Rule
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

begin_class
DECL|class|MllpTcpServerConsumerTransactionTest
specifier|public
class|class
name|MllpTcpServerConsumerTransactionTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Rule
DECL|field|broker
specifier|public
name|EmbeddedActiveMQBroker
name|broker
init|=
operator|new
name|EmbeddedActiveMQBroker
argument_list|()
decl_stmt|;
annotation|@
name|Rule
DECL|field|mllpClient
specifier|public
name|MllpClientResource
name|mllpClient
init|=
operator|new
name|MllpClientResource
argument_list|()
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"mock://result"
argument_list|)
DECL|field|result
name|MockEndpoint
name|result
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"mock://on-complete-only"
argument_list|)
DECL|field|complete
name|MockEndpoint
name|complete
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"mock://on-failure-only"
argument_list|)
DECL|field|failure
name|MockEndpoint
name|failure
decl_stmt|;
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultCamelContext
name|context
init|=
operator|(
name|DefaultCamelContext
operator|)
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|setUseMDCLogging
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|context
operator|.
name|setName
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|BindToRegistry
argument_list|(
literal|"target"
argument_list|)
DECL|method|addTargetComponent ()
specifier|public
name|SjmsComponent
name|addTargetComponent
parameter_list|()
throws|throws
name|Exception
block|{
name|SjmsComponent
name|target
init|=
operator|new
name|SjmsComponent
argument_list|()
decl_stmt|;
name|target
operator|.
name|setConnectionFactory
argument_list|(
operator|new
name|ActiveMQConnectionFactory
argument_list|(
name|broker
operator|.
name|getVmURL
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|target
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
name|mllpClient
operator|.
name|setMllpHost
argument_list|(
literal|"localhost"
argument_list|)
expr_stmt|;
name|mllpClient
operator|.
name|setMllpPort
argument_list|(
name|AvailablePortFinder
operator|.
name|getNextAvailable
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
name|int
name|connectTimeout
init|=
literal|500
decl_stmt|;
name|int
name|responseTimeout
init|=
literal|5000
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|routeId
init|=
literal|"mllp-test-receiver-route"
decl_stmt|;
name|onCompletion
argument_list|()
operator|.
name|onCompleteOnly
argument_list|()
operator|.
name|log
argument_list|(
name|LoggingLevel
operator|.
name|INFO
argument_list|,
name|routeId
argument_list|,
literal|"Test route complete"
argument_list|)
operator|.
name|to
argument_list|(
name|complete
argument_list|)
expr_stmt|;
name|onCompletion
argument_list|()
operator|.
name|onFailureOnly
argument_list|()
operator|.
name|log
argument_list|(
name|LoggingLevel
operator|.
name|INFO
argument_list|,
name|routeId
argument_list|,
literal|"Test route failed"
argument_list|)
operator|.
name|to
argument_list|(
name|failure
argument_list|)
expr_stmt|;
name|fromF
argument_list|(
literal|"mllp://%s:%d?autoAck=true&connectTimeout=%d&receiveTimeout=%d"
argument_list|,
name|mllpClient
operator|.
name|getMllpHost
argument_list|()
argument_list|,
name|mllpClient
operator|.
name|getMllpPort
argument_list|()
argument_list|,
name|connectTimeout
argument_list|,
name|responseTimeout
argument_list|)
operator|.
name|routeId
argument_list|(
name|routeId
argument_list|)
operator|.
name|log
argument_list|(
name|LoggingLevel
operator|.
name|INFO
argument_list|,
name|routeId
argument_list|,
literal|"Test route received message"
argument_list|)
operator|.
name|to
argument_list|(
literal|"target://test-queue?transacted=true"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"target://test-queue"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"jms-consumer"
argument_list|)
operator|.
name|log
argument_list|(
name|LoggingLevel
operator|.
name|INFO
argument_list|,
name|routeId
argument_list|,
literal|"Test JMS Consumer received message"
argument_list|)
operator|.
name|to
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|testReceiveSingleMessage ()
specifier|public
name|void
name|testReceiveSingleMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|complete
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|failure
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|mllpClient
operator|.
name|connect
argument_list|()
expr_stmt|;
name|mllpClient
operator|.
name|sendMessageAndWaitForAcknowledgement
argument_list|(
name|Hl7TestMessageGenerator
operator|.
name|generateMessage
argument_list|()
argument_list|,
literal|10000
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAcknowledgementWriteFailure ()
specifier|public
name|void
name|testAcknowledgementWriteFailure
parameter_list|()
throws|throws
name|Exception
block|{
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|result
operator|.
name|setAssertPeriod
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|complete
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|failure
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mllpClient
operator|.
name|connect
argument_list|()
expr_stmt|;
name|mllpClient
operator|.
name|setDisconnectMethod
argument_list|(
name|MllpClientResource
operator|.
name|DisconnectMethod
operator|.
name|RESET
argument_list|)
expr_stmt|;
name|mllpClient
operator|.
name|sendFramedData
argument_list|(
name|Hl7TestMessageGenerator
operator|.
name|generateMessage
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

