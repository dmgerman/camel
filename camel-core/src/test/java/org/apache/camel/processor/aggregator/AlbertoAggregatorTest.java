begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.aggregator
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|aggregator
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
name|HashMap
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
name|java
operator|.
name|util
operator|.
name|Map
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
name|ContextTestSupport
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
name|builder
operator|.
name|RouteBuilder
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
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
name|model
operator|.
name|AggregateDefinition
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
name|AggregationStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|AlbertoAggregatorTest
specifier|public
class|class
name|AlbertoAggregatorTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|SURNAME_HEADER
specifier|private
specifier|static
specifier|final
name|String
name|SURNAME_HEADER
init|=
literal|"surname"
decl_stmt|;
DECL|field|TYPE_HEADER
specifier|private
specifier|static
specifier|final
name|String
name|TYPE_HEADER
init|=
literal|"type"
decl_stmt|;
DECL|field|BROTHERS_TYPE
specifier|private
specifier|static
specifier|final
name|String
name|BROTHERS_TYPE
init|=
literal|"brothers"
decl_stmt|;
annotation|@
name|Test
DECL|method|testAggregator ()
specifier|public
name|void
name|testAggregator
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|allNames
init|=
literal|"Harpo Marx,Fiodor Karamazov,Chico Marx,Ivan Karamazov,Groucho Marx,Alexei Karamazov,Dimitri Karamazov"
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|marxBrothers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|marxBrothers
operator|.
name|add
argument_list|(
literal|"Harpo"
argument_list|)
expr_stmt|;
name|marxBrothers
operator|.
name|add
argument_list|(
literal|"Chico"
argument_list|)
expr_stmt|;
name|marxBrothers
operator|.
name|add
argument_list|(
literal|"Groucho"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|karamazovBrothers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|karamazovBrothers
operator|.
name|add
argument_list|(
literal|"Fiodor"
argument_list|)
expr_stmt|;
name|karamazovBrothers
operator|.
name|add
argument_list|(
literal|"Ivan"
argument_list|)
expr_stmt|;
name|karamazovBrothers
operator|.
name|add
argument_list|(
literal|"Alexei"
argument_list|)
expr_stmt|;
name|karamazovBrothers
operator|.
name|add
argument_list|(
literal|"Dimitri"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|allBrothers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|allBrothers
operator|.
name|put
argument_list|(
literal|"Marx"
argument_list|,
name|marxBrothers
argument_list|)
expr_stmt|;
name|allBrothers
operator|.
name|put
argument_list|(
literal|"Karamazov"
argument_list|,
name|karamazovBrothers
argument_list|)
expr_stmt|;
name|MockEndpoint
name|resultEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|expectedBodiesReceived
argument_list|(
name|allBrothers
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|allNames
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
name|AggregationStrategy
name|surnameAggregator
init|=
operator|new
name|AggregationStrategy
argument_list|()
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
name|debugIn
argument_list|(
literal|"Surname Aggregator"
argument_list|,
name|oldExchange
argument_list|,
name|newExchange
argument_list|)
expr_stmt|;
name|Exchange
name|answer
init|=
name|newExchange
decl_stmt|;
if|if
condition|(
name|oldExchange
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|brothers
init|=
name|oldExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|brothers
operator|.
name|add
argument_list|(
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
argument_list|)
expr_stmt|;
name|answer
operator|=
name|oldExchange
expr_stmt|;
block|}
else|else
block|{
name|List
argument_list|<
name|String
argument_list|>
name|brothers
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|brothers
operator|.
name|add
argument_list|(
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
argument_list|)
expr_stmt|;
name|newExchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|brothers
argument_list|)
expr_stmt|;
block|}
name|debugOut
argument_list|(
literal|"Surname Aggregator"
argument_list|,
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|AggregationStrategy
name|brothersAggregator
init|=
operator|new
name|AggregationStrategy
argument_list|()
block|{
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
name|debugIn
argument_list|(
literal|"Brothers Aggregator"
argument_list|,
name|oldExchange
argument_list|,
name|newExchange
argument_list|)
expr_stmt|;
name|Exchange
name|answer
init|=
name|newExchange
decl_stmt|;
if|if
condition|(
name|oldExchange
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|?
argument_list|>
argument_list|>
name|brothers
init|=
name|oldExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
name|brothers
operator|.
name|put
argument_list|(
name|newExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SURNAME_HEADER
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|newExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|List
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|answer
operator|=
name|oldExchange
expr_stmt|;
block|}
else|else
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|?
argument_list|>
argument_list|>
name|brothers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|brothers
operator|.
name|put
argument_list|(
name|newExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SURNAME_HEADER
argument_list|,
name|String
operator|.
name|class
argument_list|)
argument_list|,
name|newExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|List
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|newExchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|brothers
argument_list|)
expr_stmt|;
block|}
name|debugOut
argument_list|(
literal|"Brothers Aggregator"
argument_list|,
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
block|}
decl_stmt|;
specifier|private
name|void
name|debugIn
parameter_list|(
name|String
name|stringId
parameter_list|,
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
operator|!=
literal|null
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
name|stringId
operator|+
literal|" old headers in: "
operator|+
name|oldExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
name|stringId
operator|+
literal|" old body in: "
operator|+
name|oldExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|debug
argument_list|(
name|stringId
operator|+
literal|" new headers in: "
operator|+
name|newExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
name|stringId
operator|+
literal|" new body in: "
operator|+
name|newExchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|debugOut
parameter_list|(
name|String
name|stringId
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
name|stringId
operator|+
literal|" old headers out: "
operator|+
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
name|stringId
operator|+
literal|" old body out: "
operator|+
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
comment|// Separate people
operator|.
name|split
argument_list|(
name|bodyAs
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|tokenize
argument_list|(
literal|","
argument_list|)
argument_list|)
operator|.
name|process
argument_list|(
comment|// Split the name, erase the surname and put it in a
comment|// header
operator|new
name|Processor
argument_list|()
block|{
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
index|[]
name|parts
init|=
name|exchange
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
operator|.
name|split
argument_list|(
literal|" "
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|parts
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SURNAME_HEADER
argument_list|,
name|parts
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
comment|// process
block|}
argument_list|)
comment|// Processor
operator|.
name|to
argument_list|(
literal|"direct:joinSurnames"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:joinSurnames"
argument_list|)
operator|.
name|aggregate
argument_list|(
name|header
argument_list|(
name|SURNAME_HEADER
argument_list|)
argument_list|,
name|surnameAggregator
argument_list|)
operator|.
name|completionTimeout
argument_list|(
literal|100
argument_list|)
operator|.
name|completionTimeoutCheckerInterval
argument_list|(
literal|10
argument_list|)
operator|.
name|setHeader
argument_list|(
name|TYPE_HEADER
argument_list|,
name|constant
argument_list|(
name|BROTHERS_TYPE
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:joinBrothers"
argument_list|)
expr_stmt|;
comment|// Join all brothers lists and remove surname and type headers
name|AggregateDefinition
name|agg
init|=
name|from
argument_list|(
literal|"direct:joinBrothers"
argument_list|)
operator|.
name|aggregate
argument_list|(
name|header
argument_list|(
name|TYPE_HEADER
argument_list|)
argument_list|,
name|brothersAggregator
argument_list|)
decl_stmt|;
name|agg
operator|.
name|setCompletionTimeout
argument_list|(
literal|100L
argument_list|)
expr_stmt|;
name|agg
operator|.
name|setCompletionTimeoutCheckerInterval
argument_list|(
literal|10L
argument_list|)
expr_stmt|;
name|agg
operator|.
name|removeHeader
argument_list|(
name|SURNAME_HEADER
argument_list|)
operator|.
name|removeHeader
argument_list|(
name|TYPE_HEADER
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

