begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.tracer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|tracer
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|processor
operator|.
name|aggregate
operator|.
name|AggregationStrategy
import|;
end_import

begin_comment
comment|/**  * Our aggregator where we aggregate all the quotes and find the  * the best quotes based on the one that has the most cool words  * from our cools words list  */
end_comment

begin_class
DECL|class|QuoteAggregator
specifier|public
class|class
name|QuoteAggregator
implements|implements
name|AggregationStrategy
block|{
DECL|field|coolWords
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|coolWords
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|setCoolWords (List<String> coolWords)
specifier|public
name|void
name|setCoolWords
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|coolWords
parameter_list|)
block|{
for|for
control|(
name|String
name|s
range|:
name|coolWords
control|)
block|{
comment|// use lower case to be case insensitive
name|this
operator|.
name|coolWords
operator|.
name|add
argument_list|(
name|s
operator|.
name|toLowerCase
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// reverse order so indexOf returning -1 will be the last instead
name|Collections
operator|.
name|reverse
argument_list|(
name|this
operator|.
name|coolWords
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|oldExchange
operator|==
literal|null
condition|)
block|{
comment|// the first time then just return the new exchange
return|return
name|newExchange
return|;
block|}
comment|// here we aggregate
comment|// oldExchange is the current "winner"
comment|// newExchange is the new candidate
comment|// we get the quotes of the two exchanges
name|String
name|oldQuote
init|=
name|oldExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|newQuote
init|=
name|newExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// now we compare the two and get a result indicate the best one
name|int
name|result
init|=
operator|new
name|QuoteComparator
argument_list|()
operator|.
name|compare
argument_list|(
name|oldQuote
argument_list|,
name|newQuote
argument_list|)
decl_stmt|;
comment|// we return the winner
return|return
name|result
operator|>
literal|0
condition|?
name|newExchange
else|:
name|oldExchange
return|;
block|}
DECL|class|QuoteComparator
specifier|private
class|class
name|QuoteComparator
implements|implements
name|Comparator
argument_list|<
name|String
argument_list|>
block|{
DECL|method|compare (java.lang.String o1, java.lang.String o2)
specifier|public
name|int
name|compare
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|o1
parameter_list|,
name|java
operator|.
name|lang
operator|.
name|String
name|o2
parameter_list|)
block|{
comment|// here we compare the two quotes and picks the one that
comment|// is in the top of the cool words list
name|int
name|index1
init|=
name|coolWords
operator|.
name|indexOf
argument_list|(
name|o1
operator|.
name|toLowerCase
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|index2
init|=
name|coolWords
operator|.
name|indexOf
argument_list|(
name|o2
operator|.
name|toLowerCase
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|index1
operator|-
name|index2
return|;
block|}
block|}
block|}
end_class

end_unit

