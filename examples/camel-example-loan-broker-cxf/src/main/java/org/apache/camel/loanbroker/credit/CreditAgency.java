begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.loanbroker.credit
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|loanbroker
operator|.
name|credit
package|;
end_package

begin_comment
comment|//START SNIPPET: creditAgencyImpl
end_comment

begin_class
DECL|class|CreditAgency
specifier|public
class|class
name|CreditAgency
implements|implements
name|CreditAgencyWS
block|{
annotation|@
name|Override
DECL|method|getCreditHistoryLength (String ssn)
specifier|public
name|int
name|getCreditHistoryLength
parameter_list|(
name|String
name|ssn
parameter_list|)
block|{
name|int
name|creditScore
init|=
call|(
name|int
call|)
argument_list|(
name|Math
operator|.
name|random
argument_list|()
operator|*
literal|600
operator|+
literal|300
argument_list|)
decl_stmt|;
return|return
name|creditScore
return|;
block|}
annotation|@
name|Override
DECL|method|getCreditScore (String ssn)
specifier|public
name|int
name|getCreditScore
parameter_list|(
name|String
name|ssn
parameter_list|)
block|{
name|int
name|creditHistoryLength
init|=
call|(
name|int
call|)
argument_list|(
name|Math
operator|.
name|random
argument_list|()
operator|*
literal|19
operator|+
literal|1
argument_list|)
decl_stmt|;
return|return
name|creditHistoryLength
return|;
block|}
block|}
end_class

begin_comment
comment|//END SNIPPET: creditAgencyImpl
end_comment

end_unit

