begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|apache
operator|.
name|camel
operator|.
name|ExchangePattern
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
name|DefaultExchange
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
name|MessageState
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
name|QuerySmResult
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
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
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
name|assertNotNull
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
name|when
import|;
end_import

begin_class
DECL|class|SmppQuerySmCommandTest
specifier|public
class|class
name|SmppQuerySmCommandTest
block|{
DECL|field|session
specifier|private
name|SMPPSession
name|session
decl_stmt|;
DECL|field|config
specifier|private
name|SmppConfiguration
name|config
decl_stmt|;
DECL|field|command
specifier|private
name|SmppQuerySmCommand
name|command
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|session
operator|=
name|mock
argument_list|(
name|SMPPSession
operator|.
name|class
argument_list|)
expr_stmt|;
name|config
operator|=
operator|new
name|SmppConfiguration
argument_list|()
expr_stmt|;
name|command
operator|=
operator|new
name|SmppQuerySmCommand
argument_list|(
name|session
argument_list|,
name|config
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|executeWithConfigurationData ()
specifier|public
name|void
name|executeWithConfigurationData
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|COMMAND
argument_list|,
literal|"QuerySm"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|ID
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|session
operator|.
name|queryShortMessage
argument_list|(
literal|"1"
argument_list|,
name|TypeOfNumber
operator|.
name|UNKNOWN
argument_list|,
name|NumberingPlanIndicator
operator|.
name|UNKNOWN
argument_list|,
literal|"1616"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|QuerySmResult
argument_list|(
literal|"-300101010000004+"
argument_list|,
name|MessageState
operator|.
name|DELIVERED
argument_list|,
operator|(
name|byte
operator|)
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|command
operator|.
name|execute
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|ID
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"DELIVERED"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|MESSAGE_STATE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|byte
operator|)
literal|0
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|ERROR
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|FINAL_DATE
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|execute ()
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|,
name|ExchangePattern
operator|.
name|InOut
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|COMMAND
argument_list|,
literal|"QuerySm"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|ID
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|SOURCE_ADDR_TON
argument_list|,
name|TypeOfNumber
operator|.
name|NATIONAL
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|SOURCE_ADDR_NPI
argument_list|,
name|NumberingPlanIndicator
operator|.
name|NATIONAL
operator|.
name|value
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SmppConstants
operator|.
name|SOURCE_ADDR
argument_list|,
literal|"1818"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|session
operator|.
name|queryShortMessage
argument_list|(
literal|"1"
argument_list|,
name|TypeOfNumber
operator|.
name|NATIONAL
argument_list|,
name|NumberingPlanIndicator
operator|.
name|NATIONAL
argument_list|,
literal|"1818"
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|QuerySmResult
argument_list|(
literal|"-300101010000004+"
argument_list|,
name|MessageState
operator|.
name|DELIVERED
argument_list|,
operator|(
name|byte
operator|)
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|command
operator|.
name|execute
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|ID
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"DELIVERED"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|MESSAGE_STATE
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|byte
operator|)
literal|0
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|ERROR
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SmppConstants
operator|.
name|FINAL_DATE
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

