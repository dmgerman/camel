begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.apns.spring
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
operator|.
name|spring
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
name|utils
operator|.
name|ApnsServerStub
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
name|apns
operator|.
name|util
operator|.
name|TestConstants
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
name|Assert
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|annotation
operator|.
name|Autowired
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|AbstractJUnit4SpringContextTests
import|;
end_import

begin_comment
comment|/**  * Unit test that we can produce JMS message from files  */
end_comment

begin_class
annotation|@
name|ContextConfiguration
DECL|class|SpringApnsConsumerTest
specifier|public
class|class
name|SpringApnsConsumerTest
extends|extends
name|AbstractJUnit4SpringContextTests
block|{
annotation|@
name|Autowired
DECL|field|camelContext
specifier|protected
name|CamelContext
name|camelContext
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:result"
argument_list|)
DECL|field|mock
specifier|protected
name|MockEndpoint
name|mock
decl_stmt|;
DECL|field|server
specifier|private
name|ApnsServerStub
name|server
decl_stmt|;
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
name|ApnsUtils
operator|.
name|prepareAndStartServer
argument_list|(
name|TestConstants
operator|.
name|TEST_GATEWAY_PORT
argument_list|,
name|TestConstants
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
literal|5000
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
name|getToSend
argument_list|()
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
name|mock
operator|.
name|assertIsSatisfied
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
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|inactiveDevice
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|inactiveDevice
operator|.
name|getDate
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertNotNull
argument_list|(
name|inactiveDevice
operator|.
name|getDeviceToken
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
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
block|}
end_class

end_unit

