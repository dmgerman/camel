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
name|Message
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

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|//START SNIPPET: aggregation
end_comment

begin_class
DECL|class|BankResponseAggregationStrategy
specifier|public
class|class
name|BankResponseAggregationStrategy
implements|implements
name|AggregationStrategy
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|BankResponseAggregationStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Here we put the bank response together
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"Get the exchange to aggregate, older: "
operator|+
name|oldExchange
operator|+
literal|" newer:"
operator|+
name|newExchange
argument_list|)
expr_stmt|;
comment|// the first time we only have the new exchange
if|if
condition|(
name|oldExchange
operator|==
literal|null
condition|)
block|{
return|return
name|newExchange
return|;
block|}
name|Message
name|oldMessage
decl_stmt|;
name|Message
name|newMessage
decl_stmt|;
name|oldMessage
operator|=
name|oldExchange
operator|.
name|getIn
argument_list|()
expr_stmt|;
name|newMessage
operator|=
name|newExchange
operator|.
name|getIn
argument_list|()
expr_stmt|;
name|Double
name|oldRate
init|=
name|oldMessage
operator|.
name|getHeader
argument_list|(
name|Constants
operator|.
name|PROPERTY_RATE
argument_list|,
name|Double
operator|.
name|class
argument_list|)
decl_stmt|;
name|Double
name|newRate
init|=
name|newMessage
operator|.
name|getHeader
argument_list|(
name|Constants
operator|.
name|PROPERTY_RATE
argument_list|,
name|Double
operator|.
name|class
argument_list|)
decl_stmt|;
name|Exchange
name|result
decl_stmt|;
if|if
condition|(
name|newRate
operator|>=
name|oldRate
condition|)
block|{
name|result
operator|=
name|oldExchange
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
name|newExchange
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Get the lower rate exchange "
operator|+
name|result
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

begin_comment
comment|// END SNIPPET: aggregation
end_comment

end_unit

