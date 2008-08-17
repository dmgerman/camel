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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|aggregate
operator|.
name|AggregationStrategy
import|;
end_import

begin_comment
comment|//START SNIPPET: aggregating
end_comment

begin_class
DECL|class|BankResponseAggregationStrategy
specifier|public
class|class
name|BankResponseAggregationStrategy
implements|implements
name|AggregationStrategy
block|{
DECL|field|BANK_QUOTE
specifier|public
specifier|static
specifier|final
name|String
name|BANK_QUOTE
init|=
literal|"bank_quote"
decl_stmt|;
DECL|method|aggregate (Exchange oldExchange, Exchange newExchange)
specifier|public
name|Exchange
name|aggregate
parameter_list|(
name|Exchange
name|oldExchange
parameter_list|,
name|Exchange
name|newExchange
parameter_list|)
block|{
comment|// Get the bank quote instance from the exchange
name|BankQuote
name|oldQuote
init|=
name|oldExchange
operator|.
name|getProperty
argument_list|(
name|BANK_QUOTE
argument_list|,
name|BankQuote
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Get the oldQute from out message body if we can't get it from the exchange
if|if
condition|(
name|oldQuote
operator|==
literal|null
condition|)
block|{
name|oldQuote
operator|=
name|oldExchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|BankQuote
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
comment|// Get the newQuote
name|BankQuote
name|newQuote
init|=
name|newExchange
operator|.
name|getOut
argument_list|()
operator|.
name|getBody
argument_list|(
name|BankQuote
operator|.
name|class
argument_list|)
decl_stmt|;
name|Exchange
name|result
init|=
literal|null
decl_stmt|;
name|BankQuote
name|bankQuote
decl_stmt|;
if|if
condition|(
name|newQuote
operator|.
name|getRate
argument_list|()
operator|>=
name|oldQuote
operator|.
name|getRate
argument_list|()
condition|)
block|{
name|result
operator|=
name|oldExchange
expr_stmt|;
name|bankQuote
operator|=
name|oldQuote
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
name|newExchange
expr_stmt|;
name|bankQuote
operator|=
name|newQuote
expr_stmt|;
block|}
comment|// Set the lower rate BankQuote instance back to aggregated exchange
name|result
operator|.
name|setProperty
argument_list|(
name|BANK_QUOTE
argument_list|,
name|bankQuote
argument_list|)
expr_stmt|;
comment|// Set the return message for the client
name|result
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"The best rate is "
operator|+
name|bankQuote
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

begin_comment
comment|//END SNIPPET: aggregating
end_comment

end_unit

