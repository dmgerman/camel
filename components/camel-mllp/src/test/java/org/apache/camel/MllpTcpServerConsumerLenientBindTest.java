begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|ServerSocket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|SocketTimeoutException
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
name|junit
operator|.
name|rule
operator|.
name|mllp
operator|.
name|MllpJUnitResourceException
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
name|MllpJUnitResourceTimeoutException
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
DECL|class|MllpTcpServerConsumerLenientBindTest
specifier|public
class|class
name|MllpTcpServerConsumerLenientBindTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|RECEIVE_TIMEOUT
specifier|static
specifier|final
name|int
name|RECEIVE_TIMEOUT
init|=
literal|1000
decl_stmt|;
DECL|field|READ_TIMEOUT
specifier|static
specifier|final
name|int
name|READ_TIMEOUT
init|=
literal|500
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
name|uri
operator|=
literal|"mock://result"
argument_list|)
DECL|field|result
name|MockEndpoint
name|result
decl_stmt|;
DECL|field|portBlocker
name|ServerSocket
name|portBlocker
decl_stmt|;
annotation|@
name|Override
DECL|method|doPreSetup ()
specifier|protected
name|void
name|doPreSetup
parameter_list|()
throws|throws
name|Exception
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
name|portBlocker
operator|=
operator|new
name|ServerSocket
argument_list|(
name|mllpClient
operator|.
name|getMllpPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|portBlocker
operator|.
name|isBound
argument_list|()
argument_list|)
expr_stmt|;
name|super
operator|.
name|doPreSetup
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RoutesBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
name|RouteBuilder
name|builder
init|=
operator|new
name|RouteBuilder
argument_list|()
block|{
name|String
name|routeId
init|=
literal|"mllp-receiver-with-lenient-bind"
decl_stmt|;
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|fromF
argument_list|(
literal|"mllp://%s:%d?bindTimeout=15000&bindRetryInterval=500&receiveTimeout=%d&readTimeout=%d&reuseAddress=false&lenientBind=true"
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
name|RECEIVE_TIMEOUT
argument_list|,
name|READ_TIMEOUT
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
literal|"Receiving: ${body}"
argument_list|)
operator|.
name|to
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
return|return
name|builder
return|;
block|}
annotation|@
name|Test
DECL|method|testLenientBind ()
specifier|public
name|void
name|testLenientBind
parameter_list|()
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Started
argument_list|,
name|context
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|mllpClient
operator|.
name|connect
argument_list|()
expr_stmt|;
try|try
block|{
name|mllpClient
operator|.
name|sendMessageAndWaitForAcknowledgement
argument_list|(
name|Hl7TestMessageGenerator
operator|.
name|generateMessage
argument_list|(
literal|10001
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MllpJUnitResourceTimeoutException
name|expectedEx
parameter_list|)
block|{
name|assertIsInstanceOf
argument_list|(
name|SocketTimeoutException
operator|.
name|class
argument_list|,
name|expectedEx
operator|.
name|getCause
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|mllpClient
operator|.
name|reset
argument_list|()
expr_stmt|;
name|portBlocker
operator|.
name|close
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ServiceStatus
operator|.
name|Started
argument_list|,
name|context
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|mllpClient
operator|.
name|connect
argument_list|()
expr_stmt|;
name|String
name|acknowledgement
init|=
name|mllpClient
operator|.
name|sendMessageAndWaitForAcknowledgement
argument_list|(
name|Hl7TestMessageGenerator
operator|.
name|generateMessage
argument_list|(
literal|10002
argument_list|)
argument_list|)
decl_stmt|;
name|assertStringContains
argument_list|(
name|acknowledgement
argument_list|,
literal|"10002"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

