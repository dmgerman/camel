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

begin_class
DECL|class|SmppCommandTypeTest
specifier|public
class|class
name|SmppCommandTypeTest
block|{
DECL|field|exchange
specifier|private
name|Exchange
name|exchange
decl_stmt|;
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
block|{
name|exchange
operator|=
operator|new
name|DefaultExchange
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createSmppSubmitSmCommand ()
specifier|public
name|void
name|createSmppSubmitSmCommand
parameter_list|()
block|{
name|assertSame
argument_list|(
name|SmppCommandType
operator|.
name|SUBMIT_SM
argument_list|,
name|SmppCommandType
operator|.
name|fromExchange
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createSmppSubmitMultiCommand ()
specifier|public
name|void
name|createSmppSubmitMultiCommand
parameter_list|()
block|{
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
literal|"SubmitMulti"
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|SmppCommandType
operator|.
name|SUBMIT_MULTI
argument_list|,
name|SmppCommandType
operator|.
name|fromExchange
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createSmppDataSmCommand ()
specifier|public
name|void
name|createSmppDataSmCommand
parameter_list|()
block|{
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
literal|"DataSm"
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|SmppCommandType
operator|.
name|DATA_SHORT_MESSAGE
argument_list|,
name|SmppCommandType
operator|.
name|fromExchange
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createSmppReplaceSmCommand ()
specifier|public
name|void
name|createSmppReplaceSmCommand
parameter_list|()
block|{
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
literal|"ReplaceSm"
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|SmppCommandType
operator|.
name|REPLACE_SM
argument_list|,
name|SmppCommandType
operator|.
name|fromExchange
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createSmppQuerySmCommand ()
specifier|public
name|void
name|createSmppQuerySmCommand
parameter_list|()
block|{
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
name|assertSame
argument_list|(
name|SmppCommandType
operator|.
name|QUERY_SM
argument_list|,
name|SmppCommandType
operator|.
name|fromExchange
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|createSmppCancelSmCommand ()
specifier|public
name|void
name|createSmppCancelSmCommand
parameter_list|()
block|{
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
literal|"CancelSm"
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|SmppCommandType
operator|.
name|CANCEL_SM
argument_list|,
name|SmppCommandType
operator|.
name|fromExchange
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

