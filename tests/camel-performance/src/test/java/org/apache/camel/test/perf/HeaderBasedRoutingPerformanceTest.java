begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.test.perf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|perf
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
name|util
operator|.
name|StopWatch
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
DECL|class|HeaderBasedRoutingPerformanceTest
specifier|public
class|class
name|HeaderBasedRoutingPerformanceTest
extends|extends
name|AbstractBasePerformanceTest
block|{
DECL|field|count
specifier|private
specifier|final
name|int
name|count
init|=
literal|30000
decl_stmt|;
annotation|@
name|Test
DECL|method|testChoiceSimple ()
specifier|public
name|void
name|testChoiceSimple
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|template
operator|.
name|setDefaultEndpointUri
argument_list|(
literal|"direct:choice-simple"
argument_list|)
expr_stmt|;
comment|// warm up with 20.000 messages so that the JIT compiler kicks in
name|execute
argument_list|(
literal|20000
argument_list|)
expr_stmt|;
name|resetMock
argument_list|(
name|count
argument_list|)
expr_stmt|;
name|StopWatch
name|watch
init|=
operator|new
name|StopWatch
argument_list|()
decl_stmt|;
name|execute
argument_list|(
name|count
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|log
operator|.
name|warn
argument_list|(
literal|"Ran {} tests in {}ms"
argument_list|,
name|count
argument_list|,
name|watch
operator|.
name|taken
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testChoiceExpression ()
specifier|public
name|void
name|testChoiceExpression
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|template
operator|.
name|setDefaultEndpointUri
argument_list|(
literal|"direct:choice-expression"
argument_list|)
expr_stmt|;
comment|// warm up with 20.000 messages so that the JIT compiler kicks in
name|execute
argument_list|(
literal|20000
argument_list|)
expr_stmt|;
name|resetMock
argument_list|(
name|count
argument_list|)
expr_stmt|;
name|StopWatch
name|watch
init|=
operator|new
name|StopWatch
argument_list|()
decl_stmt|;
name|execute
argument_list|(
name|count
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|log
operator|.
name|warn
argument_list|(
literal|"Ran {} tests in {}ms"
argument_list|,
name|count
argument_list|,
name|watch
operator|.
name|taken
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFilterSimple ()
specifier|public
name|void
name|testFilterSimple
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|template
operator|.
name|setDefaultEndpointUri
argument_list|(
literal|"direct:filter-simple"
argument_list|)
expr_stmt|;
comment|// warm up with 20.000 messages so that the JIT compiler kicks in
name|execute
argument_list|(
literal|20000
argument_list|)
expr_stmt|;
name|resetMock
argument_list|(
name|count
argument_list|)
expr_stmt|;
name|StopWatch
name|watch
init|=
operator|new
name|StopWatch
argument_list|()
decl_stmt|;
name|execute
argument_list|(
name|count
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|log
operator|.
name|warn
argument_list|(
literal|"Ran {} tests in {}ms"
argument_list|,
name|count
argument_list|,
name|watch
operator|.
name|taken
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testFilterExpression ()
specifier|public
name|void
name|testFilterExpression
parameter_list|()
throws|throws
name|InterruptedException
block|{
name|template
operator|.
name|setDefaultEndpointUri
argument_list|(
literal|"direct:filter-expression"
argument_list|)
expr_stmt|;
comment|// warm up with 20.000 messages so that the JIT compiler kicks in
name|execute
argument_list|(
literal|20000
argument_list|)
expr_stmt|;
name|resetMock
argument_list|(
name|count
argument_list|)
expr_stmt|;
name|StopWatch
name|watch
init|=
operator|new
name|StopWatch
argument_list|()
decl_stmt|;
name|execute
argument_list|(
name|count
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|log
operator|.
name|warn
argument_list|(
literal|"Ran {} tests in {}ms"
argument_list|,
name|count
argument_list|,
name|watch
operator|.
name|taken
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|execute (int count)
specifier|protected
name|void
name|execute
parameter_list|(
name|int
name|count
parameter_list|)
block|{
for|for
control|(
name|int
name|counter
init|=
literal|0
init|;
name|counter
operator|<
name|count
condition|;
name|counter
operator|++
control|)
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|getPayload
argument_list|()
argument_list|,
literal|"routing"
argument_list|,
literal|"xadmin;server1;community#1.0##"
argument_list|)
expr_stmt|;
block|}
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:filter-simple"
argument_list|)
operator|.
name|filter
argument_list|(
name|simple
argument_list|(
literal|"${in.header.routing} == 'xadmin;server1;community#1.0##'"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:end"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:filter-expression"
argument_list|)
operator|.
name|filter
argument_list|(
name|header
argument_list|(
literal|"routing"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"xadmin;server1;community#1.0##"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:end"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:choice-simple"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|simple
argument_list|(
literal|"${in.header.routing} == 'xadmin;server1;community#1.0##'"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:end"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:choice-expression"
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|header
argument_list|(
literal|"routing"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|"xadmin;server1;community#1.0##"
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:end"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

