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
name|webservice
operator|.
name|version
operator|.
name|bank
operator|.
name|BankQuote
import|;
end_import

begin_comment
comment|//START SNIPPET: aggregating
end_comment

begin_comment
comment|// This POJO aggregator is supported since Camel 2.12
end_comment

begin_class
DECL|class|BankResponseAggregationStrategy
specifier|public
class|class
name|BankResponseAggregationStrategy
block|{
DECL|method|aggregate (BankQuote oldQuote, BankQuote newQuote)
specifier|public
name|BankQuote
name|aggregate
parameter_list|(
name|BankQuote
name|oldQuote
parameter_list|,
name|BankQuote
name|newQuote
parameter_list|)
block|{
if|if
condition|(
name|oldQuote
operator|!=
literal|null
operator|&&
name|oldQuote
operator|.
name|getRate
argument_list|()
operator|<=
name|newQuote
operator|.
name|getRate
argument_list|()
condition|)
block|{
return|return
name|oldQuote
return|;
block|}
else|else
block|{
return|return
name|newQuote
return|;
block|}
block|}
block|}
end_class

begin_comment
comment|//END SNIPPET: aggregating
end_comment

end_unit

