begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.loanbroker
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|loanbroker
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
name|Processor
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
name|loanbroker
operator|.
name|bank
operator|.
name|BankQuote
import|;
end_import

begin_comment
comment|/**  * Processor to set the reply message for the loan broker web service  */
end_comment

begin_class
DECL|class|ReplyProcessor
specifier|public
class|class
name|ReplyProcessor
implements|implements
name|Processor
block|{
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|BankQuote
name|quote
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|BankQuote
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|answer
init|=
literal|"The best rate is "
operator|+
name|quote
operator|.
name|toString
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|answer
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

