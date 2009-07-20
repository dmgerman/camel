begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.web.groovy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|web
operator|.
name|groovy
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
name|CamelException
import|;
end_import

begin_comment
comment|/**  * a test case for aggregate DSL  */
end_comment

begin_class
DECL|class|AggregateDSLTest
specifier|public
class|class
name|AggregateDSLTest
extends|extends
name|GroovyRendererTestSupport
block|{
DECL|method|testAggragate ()
specifier|public
name|void
name|testAggragate
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|DSL
init|=
literal|"from(\"direct:start\").aggregate().header(\"cheese\").to(\"mock:result\")"
decl_stmt|;
name|String
name|expectedDSL
init|=
name|DSL
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedDSL
argument_list|,
name|render
argument_list|(
name|DSL
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testAggragateCommon ()
specifier|public
name|void
name|testAggragateCommon
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|DSL
init|=
literal|"from(\"direct:start\").aggregate(header(\"cheese\")).to(\"mock:result\")"
decl_stmt|;
name|String
name|expectedDSL
init|=
literal|"from(\"direct:start\").aggregate().header(\"cheese\").to(\"mock:result\")"
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedDSL
argument_list|,
name|render
argument_list|(
name|DSL
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testAggregateGroupedExchange ()
specifier|public
name|void
name|testAggregateGroupedExchange
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|DSL
init|=
literal|"from(\"direct:start\").aggregate().simple(\"id\").batchTimeout(500L).groupExchanges().to(\"mock:result\")"
decl_stmt|;
name|String
name|expectedDSL
init|=
name|DSL
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedDSL
argument_list|,
name|render
argument_list|(
name|DSL
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testAggregateTimeoutOnly ()
specifier|public
name|void
name|testAggregateTimeoutOnly
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|DSL
init|=
literal|"from(\"direct:start\").aggregate(header(\"id\")).batchTimeout(3000).batchSize(0).to(\"mock:result\")"
decl_stmt|;
name|String
name|expectedDSL
init|=
literal|"from(\"direct:start\").aggregate().header(\"id\").batchTimeout(3000).batchSize(0).to(\"mock:result\")"
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedDSL
argument_list|,
name|render
argument_list|(
name|DSL
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * a route involving a external class: CamelException      *       * @throws Exception      */
DECL|method|_testAggregateAndOnException ()
specifier|public
name|void
name|_testAggregateAndOnException
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|DSL
init|=
literal|"errorHandler(deadLetterChannel(\"mock:error\"));onException(CamelException.class).maximumRedeliveries(2);from(\"direct:start\").aggregate(header(\"id\")).to(\"mock:result\")"
decl_stmt|;
name|String
name|expectedDSL
init|=
name|DSL
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedDSL
argument_list|,
name|render
argument_list|(
name|DSL
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * a set of routes that uses aggregate DSL      *       * @throws Exception      */
DECL|method|_testAggregateTimerAndTracer ()
specifier|public
name|void
name|_testAggregateTimerAndTracer
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|DSL
init|=
literal|"from(\"timer://kickoff?period=9999910000\").setHeader(\"id\").constant(\"foo\").setBody().constant(\"a b c\").split(body().tokenize(\" \")).to(\"seda:splitted\");"
operator|+
literal|"from(\"seda:splitted\").aggregate(header(\"id\")).to(\"mock:result\")"
decl_stmt|;
name|String
name|expectedDSL
init|=
name|DSL
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedDSL
argument_list|,
name|render
argument_list|(
name|DSL
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

