begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|mllp
operator|.
name|PassthroughProcessor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|hamcrest
operator|.
name|CoreMatchers
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

begin_import
import|import static
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
name|Hl7MessageGenerator
operator|.
name|generateMessage
import|;
end_import

begin_class
DECL|class|MllpTcpServerConsumerMulitpleTcpPacketTest
specifier|public
class|class
name|MllpTcpServerConsumerMulitpleTcpPacketTest
extends|extends
name|CamelTestSupport
block|{
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
name|uri
operator|=
literal|"mock://result"
argument_list|)
DECL|field|result
name|MockEndpoint
name|result
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
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
specifier|final
name|int
name|groupInterval
init|=
literal|1000
decl_stmt|;
specifier|final
name|boolean
name|groupActiveOnly
init|=
literal|false
decl_stmt|;
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
name|String
name|routeId
init|=
literal|"mllp-receiver"
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
name|onCompletion
argument_list|()
operator|.
name|log
argument_list|(
name|LoggingLevel
operator|.
name|DEBUG
argument_list|,
name|routeId
argument_list|,
literal|"Test route complete"
argument_list|)
expr_stmt|;
name|fromF
argument_list|(
literal|"mllp://%s:%d"
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
argument_list|)
operator|.
name|routeId
argument_list|(
name|routeId
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|PassthroughProcessor
argument_list|(
literal|"Before send to result"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
name|result
argument_list|)
operator|.
name|toF
argument_list|(
literal|"log://%s?level=INFO&groupInterval=%d&groupActiveOnly=%b"
argument_list|,
name|routeId
argument_list|,
name|groupInterval
argument_list|,
name|groupActiveOnly
argument_list|)
operator|.
name|log
argument_list|(
name|LoggingLevel
operator|.
name|DEBUG
argument_list|,
name|routeId
argument_list|,
literal|"Test route received message"
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
name|mllpClient
operator|.
name|connect
argument_list|()
expr_stmt|;
name|String
name|message
init|=
name|generateMessage
argument_list|()
decl_stmt|;
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|mllpClient
operator|.
name|sendFramedDataInMultiplePackets
argument_list|(
name|message
argument_list|,
operator|(
name|byte
operator|)
literal|'\r'
argument_list|)
expr_stmt|;
name|String
name|acknowledgement
init|=
name|mllpClient
operator|.
name|receiveFramedData
argument_list|()
decl_stmt|;
name|assertMockEndpointsSatisfied
argument_list|(
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertThat
argument_list|(
literal|"Should be acknowledgment for message 1"
argument_list|,
name|acknowledgement
argument_list|,
name|CoreMatchers
operator|.
name|containsString
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"MSA|AA|00001"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testReceiveMultipleMessages ()
specifier|public
name|void
name|testReceiveMultipleMessages
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|sendMessageCount
init|=
literal|1000
decl_stmt|;
name|result
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|mllpClient
operator|.
name|connect
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|1
init|;
name|i
operator|<=
name|sendMessageCount
condition|;
operator|++
name|i
control|)
block|{
name|String
name|testMessage
init|=
name|generateMessage
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|result
operator|.
name|message
argument_list|(
name|i
operator|-
literal|1
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isEqualTo
argument_list|(
name|testMessage
argument_list|)
expr_stmt|;
name|mllpClient
operator|.
name|sendFramedDataInMultiplePackets
argument_list|(
name|testMessage
argument_list|,
operator|(
name|byte
operator|)
literal|'\r'
argument_list|)
expr_stmt|;
name|String
name|acknowledgement
init|=
name|mllpClient
operator|.
name|receiveFramedData
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertThat
argument_list|(
literal|"Should be acknowledgment for message "
operator|+
name|i
argument_list|,
name|acknowledgement
argument_list|,
name|CoreMatchers
operator|.
name|containsString
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"MSA|AA|%05d"
argument_list|,
name|i
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
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

