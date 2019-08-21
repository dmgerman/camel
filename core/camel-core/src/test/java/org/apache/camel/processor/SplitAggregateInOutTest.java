begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|AggregationStrategy
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
name|impl
operator|.
name|JndiRegistry
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

begin_class
DECL|class|SplitAggregateInOutTest
specifier|public
class|class
name|SplitAggregateInOutTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SplitAggregateInOutTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|expectedBody
specifier|private
name|String
name|expectedBody
init|=
literal|"Response[(id=1,item=A);(id=2,item=B);(id=3,item=C)]"
decl_stmt|;
annotation|@
name|Test
DECL|method|testSplitAndAggregateInOut ()
specifier|public
name|void
name|testSplitAndAggregateInOut
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
name|expectedBody
argument_list|)
expr_stmt|;
comment|// use requestBody as its InOut
name|Object
name|out
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"A@B@C"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedBody
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Response to caller: "
operator|+
name|out
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"MyOrderService"
argument_list|,
operator|new
name|MyOrderService
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// START SNIPPET: e1
comment|// this routes starts from the direct:start endpoint
comment|// the body is then splitted based on @ separator
comment|// the splitter in Camel supports InOut as well and for that we
comment|// need
comment|// to be able to aggregate what response we need to send back,
comment|// so we provide our
comment|// own strategy with the class MyOrderStrategy.
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|split
argument_list|(
name|body
argument_list|()
operator|.
name|tokenize
argument_list|(
literal|"@"
argument_list|)
argument_list|,
operator|new
name|MyOrderStrategy
argument_list|()
argument_list|)
comment|// each splitted message is then send to this bean where we
comment|// can process it
operator|.
name|to
argument_list|(
literal|"bean:MyOrderService?method=handleOrder"
argument_list|)
comment|// this is important to end the splitter route as we do not
comment|// want to do more routing
comment|// on each splitted message
operator|.
name|end
argument_list|()
comment|// after we have splitted and handled each message we want
comment|// to send a single combined
comment|// response back to the original caller, so we let this bean
comment|// build it for us
comment|// this bean will receive the result of the aggregate
comment|// strategy: MyOrderStrategy
operator|.
name|to
argument_list|(
literal|"bean:MyOrderService?method=buildCombinedResponse"
argument_list|)
comment|// END SNIPPET: e1
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
comment|// START SNIPPET: e2
DECL|class|MyOrderService
specifier|public
specifier|static
class|class
name|MyOrderService
block|{
DECL|field|counter
specifier|private
specifier|static
name|int
name|counter
decl_stmt|;
comment|/**          * We just handle the order by returning a id line for the order          */
DECL|method|handleOrder (String line)
specifier|public
name|String
name|handleOrder
parameter_list|(
name|String
name|line
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"HandleOrder: "
operator|+
name|line
argument_list|)
expr_stmt|;
return|return
literal|"(id="
operator|+
operator|++
name|counter
operator|+
literal|",item="
operator|+
name|line
operator|+
literal|")"
return|;
block|}
comment|/**          * We use the same bean for building the combined response to send back          * to the original caller          */
DECL|method|buildCombinedResponse (String line)
specifier|public
name|String
name|buildCombinedResponse
parameter_list|(
name|String
name|line
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"BuildCombinedResponse: "
operator|+
name|line
argument_list|)
expr_stmt|;
return|return
literal|"Response["
operator|+
name|line
operator|+
literal|"]"
return|;
block|}
block|}
comment|// END SNIPPET: e2
comment|// START SNIPPET: e3
comment|/**      * This is our own order aggregation strategy where we can control how each      * splitted message should be combined. As we do not want to loos any      * message we copy from the new to the old to preserve the order lines as      * long we process them      */
DECL|class|MyOrderStrategy
specifier|public
specifier|static
class|class
name|MyOrderStrategy
implements|implements
name|AggregationStrategy
block|{
annotation|@
name|Override
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
comment|// put order together in old exchange by adding the order from new
comment|// exchange
if|if
condition|(
name|oldExchange
operator|==
literal|null
condition|)
block|{
comment|// the first time we aggregate we only have the new exchange,
comment|// so we just return it
return|return
name|newExchange
return|;
block|}
name|String
name|orders
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
name|newLine
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"Aggregate old orders: "
operator|+
name|orders
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Aggregate new order: "
operator|+
name|newLine
argument_list|)
expr_stmt|;
comment|// put orders together separating by semi colon
name|orders
operator|=
name|orders
operator|+
literal|";"
operator|+
name|newLine
expr_stmt|;
comment|// put combined order back on old to preserve it
name|oldExchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|orders
argument_list|)
expr_stmt|;
comment|// return old as this is the one that has all the orders gathered
comment|// until now
return|return
name|oldExchange
return|;
block|}
block|}
comment|// END SNIPPET: e3
block|}
end_class

end_unit

