begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.apns
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|apns
package|;
end_package

begin_import
import|import
name|com
operator|.
name|notnoop
operator|.
name|apns
operator|.
name|ApnsService
import|;
end_import

begin_import
import|import
name|com
operator|.
name|notnoop
operator|.
name|apns
operator|.
name|utils
operator|.
name|ApnsServerStub
import|;
end_import

begin_import
import|import
name|com
operator|.
name|notnoop
operator|.
name|apns
operator|.
name|utils
operator|.
name|FixedCertificates
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
name|apns
operator|.
name|factory
operator|.
name|ApnsServiceFactory
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
name|apns
operator|.
name|model
operator|.
name|InactiveDevice
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
name|apns
operator|.
name|util
operator|.
name|ApnsUtils
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
name|After
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

begin_comment
comment|/**  * Unit test that we can produce JMS message from files  */
end_comment

begin_class
DECL|class|ApnsConsumerTest
specifier|public
class|class
name|ApnsConsumerTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|server
name|ApnsServerStub
name|server
decl_stmt|;
DECL|method|ApnsConsumerTest ()
specifier|public
name|ApnsConsumerTest
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Before
DECL|method|startup ()
specifier|public
name|void
name|startup
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|server
operator|=
name|ApnsServerStub
operator|.
name|prepareAndStartServer
argument_list|(
name|FixedCertificates
operator|.
name|TEST_GATEWAY_PORT
argument_list|,
name|FixedCertificates
operator|.
name|TEST_FEEDBACK_PORT
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|server
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|timeout
operator|=
literal|10000
argument_list|)
DECL|method|testConsumer ()
specifier|public
name|void
name|testConsumer
parameter_list|()
throws|throws
name|Exception
block|{
name|byte
index|[]
name|deviceTokenBytes
init|=
name|ApnsUtils
operator|.
name|createRandomDeviceTokenBytes
argument_list|()
decl_stmt|;
name|String
name|deviceToken
init|=
name|ApnsUtils
operator|.
name|encodeHexToken
argument_list|(
name|deviceTokenBytes
argument_list|)
decl_stmt|;
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|InactiveDevice
operator|.
name|class
argument_list|)
expr_stmt|;
name|byte
index|[]
name|feedBackBytes
init|=
name|ApnsUtils
operator|.
name|generateFeedbackBytes
argument_list|(
name|deviceTokenBytes
argument_list|)
decl_stmt|;
name|server
operator|.
name|toSend
operator|.
name|write
argument_list|(
name|feedBackBytes
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|InactiveDevice
name|inactiveDevice
init|=
operator|(
name|InactiveDevice
operator|)
name|mock
operator|.
name|getExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|inactiveDevice
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|inactiveDevice
operator|.
name|getDate
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|inactiveDevice
operator|.
name|getDeviceToken
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|deviceToken
argument_list|,
name|inactiveDevice
operator|.
name|getDeviceToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|camelContext
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|ApnsServiceFactory
name|apnsServiceFactory
init|=
name|ApnsUtils
operator|.
name|createDefaultTestConfiguration
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|ApnsService
name|apnsService
init|=
name|apnsServiceFactory
operator|.
name|getApnsService
argument_list|()
decl_stmt|;
name|ApnsComponent
name|apnsComponent
init|=
operator|new
name|ApnsComponent
argument_list|(
name|apnsService
argument_list|)
decl_stmt|;
name|camelContext
operator|.
name|addComponent
argument_list|(
literal|"apns"
argument_list|,
name|apnsComponent
argument_list|)
expr_stmt|;
return|return
name|camelContext
return|;
block|}
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"apns:consumer?initialDelay=500&delay=500&timeUnit=MILLISECONDS"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:apns?showAll=true&multiline=true"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

