begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.loanbroker.webservice.version
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|loanbroker
operator|.
name|webservice
operator|.
name|version
package|;
end_package

begin_comment
comment|//START SNIPPET: loanBroker
end_comment

begin_comment
comment|// This SEI has no @WebService annotation, we use the simple frontend API to create client and server
end_comment

begin_interface
DECL|interface|LoanBrokerWS
specifier|public
interface|interface
name|LoanBrokerWS
block|{
DECL|method|getLoanQuote (String ssn, Double loanAmount, Integer loanDuration)
name|String
name|getLoanQuote
parameter_list|(
name|String
name|ssn
parameter_list|,
name|Double
name|loanAmount
parameter_list|,
name|Integer
name|loanDuration
parameter_list|)
function_decl|;
block|}
end_interface

begin_comment
comment|//END SNIPPET: loanBroker
end_comment

end_unit

