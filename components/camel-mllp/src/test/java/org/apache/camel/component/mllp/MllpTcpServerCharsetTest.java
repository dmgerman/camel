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
DECL|class|MllpTcpServerCharsetTest
specifier|public
class|class
name|MllpTcpServerCharsetTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|TEST_MESSAGE
specifier|static
specifier|final
name|String
name|TEST_MESSAGE
init|=
literal|"MSH|^~\\&|REQUESTING|ICE|INHOUSE|RTH00|20161206193919||ORM^O01|00001|D|2.3||||||ISO_IR 100|"
operator|+
literal|'\r'
operator|+
literal|"PID|1||ICE999999^^^ICE^ICE||Testpatient^Testy^^^Mr||19740401|M|||123 Barrel Drive^^^^SW18 4RT|||||2||||||||||||||"
operator|+
literal|'\r'
operator|+
literal|"NTE|1||Free text for entering clinical details|"
operator|+
literal|'\r'
operator|+
literal|"PV1|1||^^^^^^^^Admin Location|||||||||||||||NHS|"
operator|+
literal|'\r'
operator|+
literal|"ORC|NW|213||175|REQ||||20080808093202|ahsl^^Administrator||G999999^TestDoctor^GPtests^^^^^^NAT|^^^^^^^^Admin Location | 819600|200808080932||RTH00||ahsl^^Administrator||"
operator|+
literal|'\r'
operator|+
literal|"OBR|1|213||CCOR^Serum Cortisol ^ JRH06|||200808080932||0.100||||||^|G999999^TestDoctor^GPtests^^^^^^NAT|819600|ADM162||||||820|||^^^^^R||||||||"
operator|+
literal|'\r'
operator|+
literal|"OBR|2|213||GCU^Serum Copper ^ JRH06 |||200808080932||0.100||||||^|G999999^TestDoctor^GPtests^^^^^^NAT|819600|ADM162||||||820|||^^^^^R||||||||"
operator|+
literal|'\r'
operator|+
literal|"OBR|3|213||THYG^Serum Thyroglobulin ^JRH06|||200808080932||0.100||||||^|G999999^TestDoctor^GPtests^^^^^^NAT|819600|ADM162||||||820|||^^^^^R||||||||"
operator|+
literal|'\r'
operator|+
literal|'\n'
decl_stmt|;
DECL|field|TARGET_URI
specifier|static
specifier|final
name|String
name|TARGET_URI
init|=
literal|"mock://target"
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
name|TARGET_URI
argument_list|)
DECL|field|target
name|MockEndpoint
name|target
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
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
name|String
name|routeId
init|=
literal|"mllp-sender"
decl_stmt|;
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|fromF
argument_list|(
literal|"mllp://%d?receiveTimeout=1000&readTimeout=500&charsetName=ISO-IR-100"
argument_list|,
name|mllpClient
operator|.
name|getMllpPort
argument_list|()
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
literal|"Sending Message"
argument_list|)
operator|.
name|to
argument_list|(
name|target
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|testReceiveMessageWithInvalidMsh18 ()
specifier|public
name|void
name|testReceiveMessageWithInvalidMsh18
parameter_list|()
throws|throws
name|Exception
block|{
name|target
operator|.
name|expectedMinimumMessageCount
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
name|sendMessageAndWaitForAcknowledgement
argument_list|(
name|TEST_MESSAGE
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testReceiveMessageWithValidMsh18 ()
specifier|public
name|void
name|testReceiveMessageWithValidMsh18
parameter_list|()
throws|throws
name|Exception
block|{
name|target
operator|.
name|expectedMinimumMessageCount
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
name|sendMessageAndWaitForAcknowledgement
argument_list|(
name|TEST_MESSAGE
operator|.
name|replace
argument_list|(
literal|"ISO_IR 100"
argument_list|,
literal|"ISO-IR-100"
argument_list|)
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|(
literal|5
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

