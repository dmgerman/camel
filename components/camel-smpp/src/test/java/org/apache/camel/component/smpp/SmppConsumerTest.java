begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.smpp
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|smpp
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Processor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|bean
operator|.
name|BindType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|bean
operator|.
name|NumberingPlanIndicator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|bean
operator|.
name|TypeOfNumber
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|session
operator|.
name|BindParameter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|session
operator|.
name|MessageReceiverListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jsmpp
operator|.
name|session
operator|.
name|SMPPSession
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
import|import static
name|org
operator|.
name|easymock
operator|.
name|classextension
operator|.
name|EasyMock
operator|.
name|*
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
name|assertSame
import|;
end_import

begin_comment
comment|/**  * JUnit test class for<code>org.apache.camel.component.smpp.SmppConsumer</code>  *   * @version $Revision$  * @author muellerc  */
end_comment

begin_class
DECL|class|SmppConsumerTest
specifier|public
class|class
name|SmppConsumerTest
block|{
DECL|field|consumer
specifier|private
name|SmppConsumer
name|consumer
decl_stmt|;
DECL|field|endpoint
specifier|private
name|SmppEndpoint
name|endpoint
decl_stmt|;
DECL|field|configuration
specifier|private
name|SmppConfiguration
name|configuration
decl_stmt|;
DECL|field|processor
specifier|private
name|Processor
name|processor
decl_stmt|;
DECL|field|session
specifier|private
name|SMPPSession
name|session
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|configuration
operator|=
operator|new
name|SmppConfiguration
argument_list|()
expr_stmt|;
name|endpoint
operator|=
name|createMock
argument_list|(
name|SmppEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|processor
operator|=
name|createMock
argument_list|(
name|Processor
operator|.
name|class
argument_list|)
expr_stmt|;
name|session
operator|=
name|createMock
argument_list|(
name|SMPPSession
operator|.
name|class
argument_list|)
expr_stmt|;
name|consumer
operator|=
operator|new
name|SmppConsumer
argument_list|(
name|endpoint
argument_list|,
name|configuration
argument_list|,
name|processor
argument_list|)
block|{
name|SMPPSession
name|createSMPPSession
parameter_list|()
block|{
return|return
name|session
return|;
block|}
block|}
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|doStartShouldStartANewSmppSession ()
specifier|public
name|void
name|doStartShouldStartANewSmppSession
parameter_list|()
throws|throws
name|Exception
block|{
name|expect
argument_list|(
name|endpoint
operator|.
name|getConnectionString
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"smpp://smppclient@localhost:2775"
argument_list|)
operator|.
name|times
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|session
operator|.
name|setEnquireLinkTimer
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
comment|//expectation
name|session
operator|.
name|setTransactionTimer
argument_list|(
literal|10000
argument_list|)
expr_stmt|;
comment|//expectation
name|session
operator|.
name|setMessageReceiverListener
argument_list|(
name|isA
argument_list|(
name|MessageReceiverListener
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
comment|//expectation
name|expect
argument_list|(
name|session
operator|.
name|connectAndBind
argument_list|(
literal|"localhost"
argument_list|,
operator|new
name|Integer
argument_list|(
literal|2775
argument_list|)
argument_list|,
operator|new
name|BindParameter
argument_list|(
name|BindType
operator|.
name|BIND_RX
argument_list|,
literal|"smppclient"
argument_list|,
literal|"password"
argument_list|,
literal|"cp"
argument_list|,
name|TypeOfNumber
operator|.
name|UNKNOWN
argument_list|,
name|NumberingPlanIndicator
operator|.
name|UNKNOWN
argument_list|,
literal|""
argument_list|)
argument_list|)
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"1"
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|endpoint
operator|.
name|getConnectionString
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"smpp://smppclient@localhost:2775"
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|,
name|session
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|,
name|session
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|doStopShouldNotCloseTheSMPPSessionIfItIsNull ()
specifier|public
name|void
name|doStopShouldNotCloseTheSMPPSessionIfItIsNull
parameter_list|()
throws|throws
name|Exception
block|{
name|expect
argument_list|(
name|endpoint
operator|.
name|getConnectionString
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"smpp://smppclient@localhost:2775"
argument_list|)
operator|.
name|times
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|replay
argument_list|(
name|session
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|doStop
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|session
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|doStopShouldCloseTheSMPPSession ()
specifier|public
name|void
name|doStopShouldCloseTheSMPPSession
parameter_list|()
throws|throws
name|Exception
block|{
name|doStartShouldStartANewSmppSession
argument_list|()
expr_stmt|;
name|reset
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|,
name|session
argument_list|)
expr_stmt|;
name|expect
argument_list|(
name|endpoint
operator|.
name|getConnectionString
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"smpp://smppclient@localhost:2775"
argument_list|)
operator|.
name|times
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|session
operator|.
name|close
argument_list|()
expr_stmt|;
name|replay
argument_list|(
name|session
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|consumer
operator|.
name|doStop
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|session
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|getterShouldReturnTheSetValues ()
specifier|public
name|void
name|getterShouldReturnTheSetValues
parameter_list|()
block|{
name|assertSame
argument_list|(
name|endpoint
argument_list|,
name|consumer
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|configuration
argument_list|,
name|consumer
operator|.
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

