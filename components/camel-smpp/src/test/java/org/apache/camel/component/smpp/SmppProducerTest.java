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
name|Exchange
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
name|SMPPSession
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
name|SessionStateListener
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
name|junit
operator|.
name|Assert
operator|.
name|assertSame
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|isA
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|verify
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
import|;
end_import

begin_comment
comment|/**  * JUnit test class for<code>org.apache.camel.component.smpp.SmppProducer</code>  */
end_comment

begin_class
DECL|class|SmppProducerTest
specifier|public
class|class
name|SmppProducerTest
block|{
DECL|field|producer
specifier|private
name|SmppProducer
name|producer
decl_stmt|;
DECL|field|configuration
specifier|private
name|SmppConfiguration
name|configuration
decl_stmt|;
DECL|field|endpoint
specifier|private
name|SmppEndpoint
name|endpoint
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
name|mock
argument_list|(
name|SmppEndpoint
operator|.
name|class
argument_list|)
expr_stmt|;
name|session
operator|=
name|mock
argument_list|(
name|SMPPSession
operator|.
name|class
argument_list|)
expr_stmt|;
name|producer
operator|=
operator|new
name|SmppProducer
argument_list|(
name|endpoint
argument_list|,
name|configuration
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
name|when
argument_list|(
name|endpoint
operator|.
name|getConnectionString
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"smpp://smppclient@localhost:2775"
argument_list|)
expr_stmt|;
name|BindParameter
name|expectedBindParameters
init|=
operator|new
name|BindParameter
argument_list|(
name|BindType
operator|.
name|BIND_TX
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
decl_stmt|;
name|when
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
name|expectedBindParameters
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"1"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|endpoint
operator|.
name|isSingleton
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|producer
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|session
argument_list|)
operator|.
name|setEnquireLinkTimer
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|session
argument_list|)
operator|.
name|setTransactionTimer
argument_list|(
literal|10000
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|session
argument_list|)
operator|.
name|addSessionStateListener
argument_list|(
name|isA
argument_list|(
name|SessionStateListener
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|session
argument_list|)
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
name|expectedBindParameters
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
name|when
argument_list|(
name|endpoint
operator|.
name|getConnectionString
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"smpp://smppclient@localhost:2775"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|endpoint
operator|.
name|isSingleton
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|producer
operator|.
name|doStop
argument_list|()
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
name|when
argument_list|(
name|endpoint
operator|.
name|getConnectionString
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"smpp://smppclient@localhost:2775"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|endpoint
operator|.
name|isSingleton
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|producer
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|producer
operator|.
name|doStop
argument_list|()
expr_stmt|;
name|verify
argument_list|(
name|session
argument_list|)
operator|.
name|removeSessionStateListener
argument_list|(
name|isA
argument_list|(
name|SessionStateListener
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|session
argument_list|)
operator|.
name|unbindAndClose
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|processInOnlyShouldExecuteTheCommand ()
specifier|public
name|void
name|processInOnlyShouldExecuteTheCommand
parameter_list|()
throws|throws
name|Exception
block|{
name|SmppBinding
name|binding
init|=
name|mock
argument_list|(
name|SmppBinding
operator|.
name|class
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|mock
argument_list|(
name|Exchange
operator|.
name|class
argument_list|)
decl_stmt|;
name|SmppCommand
name|command
init|=
name|mock
argument_list|(
name|SmppCommand
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|endpoint
operator|.
name|getBinding
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|binding
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|binding
operator|.
name|createSmppCommand
argument_list|(
name|session
argument_list|,
name|exchange
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|command
argument_list|)
expr_stmt|;
name|producer
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|producer
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|command
argument_list|)
operator|.
name|execute
argument_list|(
name|exchange
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
name|producer
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|configuration
argument_list|,
name|producer
operator|.
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

