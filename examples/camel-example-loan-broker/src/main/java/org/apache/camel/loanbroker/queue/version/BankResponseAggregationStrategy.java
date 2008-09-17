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
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
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
name|Integer
name|old
init|=
name|oldExchange
operator|.
name|getProperty
argument_list|(
literal|"aggregated"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|Double
name|oldRate
init|=
name|oldExchange
operator|.
name|getIn
argument_list|()
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
name|newExchange
operator|.
name|getIn
argument_list|()
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
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|old
operator|==
literal|null
condition|)
block|{
name|old
operator|=
literal|1
expr_stmt|;
block|}
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
comment|// Set the property for the completeness condition
name|result
operator|.
name|setProperty
argument_list|(
literal|"aggregated"
argument_list|,
name|old
operator|+
literal|1
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

