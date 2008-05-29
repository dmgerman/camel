begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.loanbroker.queue.version
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|loanbroker
operator|.
name|queue
operator|.
name|version
package|;
end_package

begin_interface
DECL|interface|Constants
specifier|public
interface|interface
name|Constants
block|{
DECL|field|LOANBROKER_NS
name|String
name|LOANBROKER_NS
init|=
literal|"http://servicemix.org/demos/loan-broker"
decl_stmt|;
DECL|field|LOANBROKER_SERVICE
name|String
name|LOANBROKER_SERVICE
init|=
literal|"loan-broker"
decl_stmt|;
DECL|field|CREDITAGENCY_SERVICE
name|String
name|CREDITAGENCY_SERVICE
init|=
literal|"credit-agency"
decl_stmt|;
DECL|field|LENDERGATEWAY_SERVICE
name|String
name|LENDERGATEWAY_SERVICE
init|=
literal|"lender-gateway"
decl_stmt|;
DECL|field|PROPERTY_SSN
name|String
name|PROPERTY_SSN
init|=
literal|"ssn"
decl_stmt|;
DECL|field|PROPERTY_AMOUNT
name|String
name|PROPERTY_AMOUNT
init|=
literal|"amount"
decl_stmt|;
DECL|field|PROPERTY_DURATION
name|String
name|PROPERTY_DURATION
init|=
literal|"duration"
decl_stmt|;
DECL|field|PROPERTY_SCORE
name|String
name|PROPERTY_SCORE
init|=
literal|"score"
decl_stmt|;
DECL|field|PROPERTY_HISTORYLENGTH
name|String
name|PROPERTY_HISTORYLENGTH
init|=
literal|"hlength"
decl_stmt|;
DECL|field|PROPERTY_RECIPIENTS
name|String
name|PROPERTY_RECIPIENTS
init|=
literal|"recipients"
decl_stmt|;
DECL|field|PROPERTY_CLIENT_ID
name|String
name|PROPERTY_CLIENT_ID
init|=
literal|"client_Id"
decl_stmt|;
DECL|field|PROPERTY_RATE
name|String
name|PROPERTY_RATE
init|=
literal|"rate"
decl_stmt|;
DECL|field|PROPERTY_BANK
name|String
name|PROPERTY_BANK
init|=
literal|"bank"
decl_stmt|;
block|}
end_interface

end_unit

