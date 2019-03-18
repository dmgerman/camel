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
name|javax
operator|.
name|jws
operator|.
name|WebService
import|;
end_import

begin_comment
comment|//START SNIPPET: bank
end_comment

begin_comment
comment|// Since we use @WebServices here, please make sure to use JaxWs frontend API create the client and server
end_comment

begin_interface
annotation|@
name|WebService
DECL|interface|BankWS
specifier|public
interface|interface
name|BankWS
block|{
DECL|method|getBankName ()
name|String
name|getBankName
parameter_list|()
function_decl|;
DECL|method|getQuote (String ssn, double loanAmount, int loanDuration, int creditHistory, int creditScore)
name|BankQuote
name|getQuote
parameter_list|(
name|String
name|ssn
parameter_list|,
name|double
name|loanAmount
parameter_list|,
name|int
name|loanDuration
parameter_list|,
name|int
name|creditHistory
parameter_list|,
name|int
name|creditScore
parameter_list|)
function_decl|;
block|}
end_interface

begin_comment
comment|//END SNIPPET: bank
end_comment

end_unit

