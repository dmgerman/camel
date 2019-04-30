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
name|NotifyBuilder
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
name|junit
operator|.
name|Rule
import|;
end_import

begin_class
DECL|class|TcpServerConsumerAcknowledgementTestSupport
specifier|public
specifier|abstract
class|class
name|TcpServerConsumerAcknowledgementTestSupport
extends|extends
name|CamelTestSupport
block|{
DECL|field|TEST_MESSAGE
specifier|static
specifier|final
name|String
name|TEST_MESSAGE
init|=
literal|"MSH|^~\\&|APP_A|FAC_A|^org^sys||||ADT^A04^ADT_A04|||2.6"
operator|+
literal|'\r'
operator|+
literal|"PID|1||1100832^^^^PI||TEST^FIG||98765432|U||R|435 MAIN STREET^^LONGMONT^CO^80503||123-456-7890|||S"
operator|+
literal|'\r'
decl_stmt|;
DECL|field|EXPECTED_ACKNOWLEDGEMENT
specifier|static
specifier|final
name|String
name|EXPECTED_ACKNOWLEDGEMENT
init|=
literal|"MSH|^~\\&|^org^sys||APP_A|FAC_A|||ACK^A04^ADT_A04|||2.6"
operator|+
literal|'\r'
operator|+
literal|"MSA|AA|"
operator|+
literal|'\r'
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
name|EndpointInject
argument_list|(
literal|"mock://invalid-ack-ex"
argument_list|)
DECL|field|invalidAckEx
name|MockEndpoint
name|invalidAckEx
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"mock://ack-generation-ex"
argument_list|)
DECL|field|ackGenerationEx
name|MockEndpoint
name|ackGenerationEx
decl_stmt|;
annotation|@
name|Override
DECL|method|doPostSetup ()
specifier|protected
name|void
name|doPostSetup
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doPostSetup
argument_list|()
expr_stmt|;
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|0
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
literal|0
argument_list|)
expr_stmt|;
name|invalidAckEx
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|ackGenerationEx
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
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
name|onException
argument_list|(
name|MllpInvalidAcknowledgementException
operator|.
name|class
argument_list|)
operator|.
name|handled
argument_list|(
literal|false
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock://invalid-ack-ex"
argument_list|)
expr_stmt|;
name|onException
argument_list|(
name|MllpAcknowledgementGenerationException
operator|.
name|class
argument_list|)
operator|.
name|handled
argument_list|(
literal|false
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock://ack-generation-ex"
argument_list|)
expr_stmt|;
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
literal|"mock://on-complete-only"
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
literal|"Test route complete"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock://on-failure-only"
argument_list|)
expr_stmt|;
name|fromF
argument_list|(
literal|"mllp://%s:%d?bridgeErrorHandler=%b&autoAck=%b&connectTimeout=%d&receiveTimeout=%d"
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
name|isBridgeErrorHandler
argument_list|()
argument_list|,
name|isAutoAck
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
name|to
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|isBridgeErrorHandler ()
specifier|protected
specifier|abstract
name|boolean
name|isBridgeErrorHandler
parameter_list|()
function_decl|;
DECL|method|isAutoAck ()
specifier|protected
specifier|abstract
name|boolean
name|isAutoAck
parameter_list|()
function_decl|;
DECL|method|receiveSingleMessage ()
specifier|public
name|void
name|receiveSingleMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|NotifyBuilder
name|done
init|=
operator|new
name|NotifyBuilder
argument_list|(
name|context
argument_list|)
operator|.
name|whenDone
argument_list|(
literal|1
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|mllpClient
operator|.
name|connect
argument_list|()
expr_stmt|;
name|mllpClient
operator|.
name|sendFramedData
argument_list|(
name|TEST_MESSAGE
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Exchange should have completed"
argument_list|,
name|done
operator|.
name|matches
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|acknowledgementDeliveryFailure ()
specifier|public
name|void
name|acknowledgementDeliveryFailure
parameter_list|()
throws|throws
name|Exception
block|{
name|boolean
name|disconnectAfterSend
init|=
literal|true
decl_stmt|;
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
name|connect
argument_list|()
expr_stmt|;
name|mllpClient
operator|.
name|sendFramedData
argument_list|(
name|TEST_MESSAGE
argument_list|,
name|disconnectAfterSend
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
DECL|method|unparsableMessage (String testMessage)
specifier|public
name|void
name|unparsableMessage
parameter_list|(
name|String
name|testMessage
parameter_list|)
throws|throws
name|Exception
block|{
name|NotifyBuilder
name|done
init|=
operator|new
name|NotifyBuilder
argument_list|(
name|context
argument_list|)
operator|.
name|whenDone
argument_list|(
literal|1
argument_list|)
operator|.
name|create
argument_list|()
decl_stmt|;
name|mllpClient
operator|.
name|connect
argument_list|()
expr_stmt|;
name|mllpClient
operator|.
name|sendFramedData
argument_list|(
name|testMessage
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"One exchange should have complete"
argument_list|,
name|done
operator|.
name|matches
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

