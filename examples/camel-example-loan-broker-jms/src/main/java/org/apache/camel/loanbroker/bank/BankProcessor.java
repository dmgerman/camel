begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.loanbroker.bank
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|loanbroker
operator|.
name|bank
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Random
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
name|Constants
import|;
end_import

begin_comment
comment|//START SNIPPET: bank
end_comment

begin_class
DECL|class|BankProcessor
specifier|public
class|class
name|BankProcessor
implements|implements
name|Processor
block|{
DECL|field|bankName
specifier|private
specifier|final
name|String
name|bankName
decl_stmt|;
DECL|field|primeRate
specifier|private
specifier|final
name|double
name|primeRate
decl_stmt|;
DECL|method|BankProcessor (String name)
specifier|public
name|BankProcessor
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|bankName
operator|=
name|name
expr_stmt|;
name|primeRate
operator|=
literal|3.5
expr_stmt|;
block|}
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
name|String
name|ssn
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Constants
operator|.
name|PROPERTY_SSN
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Integer
name|historyLength
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Constants
operator|.
name|PROPERTY_HISTORYLENGTH
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|Random
name|rand
init|=
operator|new
name|Random
argument_list|()
decl_stmt|;
name|double
name|rate
init|=
name|primeRate
operator|+
call|(
name|double
call|)
argument_list|(
name|historyLength
operator|/
literal|12
argument_list|)
operator|/
literal|10
operator|+
operator|(
name|rand
operator|.
name|nextDouble
argument_list|()
operator|*
literal|10
operator|)
operator|/
literal|10
decl_stmt|;
comment|// set reply details as headers
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Constants
operator|.
name|PROPERTY_BANK
argument_list|,
name|bankName
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Constants
operator|.
name|PROPERTY_SSN
argument_list|,
name|ssn
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Constants
operator|.
name|PROPERTY_RATE
argument_list|,
name|rate
argument_list|)
expr_stmt|;
block|}
block|}
end_class

begin_comment
comment|//END SNIPPET: bank
end_comment

end_unit

