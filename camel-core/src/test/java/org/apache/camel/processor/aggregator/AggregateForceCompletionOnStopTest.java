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
name|processor
operator|.
name|BodyInAggregatingStrategy
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|AggregateForceCompletionOnStopTest
specifier|public
class|class
name|AggregateForceCompletionOnStopTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|myCompletionProcessor
name|MyCompletionProcessor
name|myCompletionProcessor
init|=
operator|new
name|MyCompletionProcessor
argument_list|()
decl_stmt|;
DECL|method|testForceCompletionTrue ()
specifier|public
name|void
name|testForceCompletionTrue
parameter_list|()
throws|throws
name|Exception
block|{
name|myCompletionProcessor
operator|.
name|reset
argument_list|()
expr_stmt|;
name|context
operator|.
name|getShutdownStrategy
argument_list|()
operator|.
name|setShutdownNowOnTimeout
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|context
operator|.
name|getShutdownStrategy
argument_list|()
operator|.
name|setTimeout
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:forceCompletionTrue"
argument_list|,
literal|"test1"
argument_list|,
literal|"id"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:forceCompletionTrue"
argument_list|,
literal|"test2"
argument_list|,
literal|"id"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:forceCompletionTrue"
argument_list|,
literal|"test3"
argument_list|,
literal|"id"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:forceCompletionTrue"
argument_list|,
literal|"test4"
argument_list|,
literal|"id"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"aggregation should not have completed yet"
argument_list|,
literal|0
argument_list|,
name|myCompletionProcessor
operator|.
name|getAggregationCount
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"aggregation should have completed"
argument_list|,
literal|2
argument_list|,
name|myCompletionProcessor
operator|.
name|getAggregationCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testForceCompletionFalse ()
specifier|public
name|void
name|testForceCompletionFalse
parameter_list|()
throws|throws
name|Exception
block|{
name|myCompletionProcessor
operator|.
name|reset
argument_list|()
expr_stmt|;
name|context
operator|.
name|getShutdownStrategy
argument_list|()
operator|.
name|setShutdownNowOnTimeout
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|context
operator|.
name|getShutdownStrategy
argument_list|()
operator|.
name|setTimeout
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:forceCompletionFalse"
argument_list|,
literal|"test1"
argument_list|,
literal|"id"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:forceCompletionFalse"
argument_list|,
literal|"test2"
argument_list|,
literal|"id"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:forceCompletionFalse"
argument_list|,
literal|"test3"
argument_list|,
literal|"id"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:forceCompletionFalse"
argument_list|,
literal|"test4"
argument_list|,
literal|"id"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"aggregation should not have completed yet"
argument_list|,
literal|0
argument_list|,
name|myCompletionProcessor
operator|.
name|getAggregationCount
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"aggregation should not have completed yet"
argument_list|,
literal|0
argument_list|,
name|myCompletionProcessor
operator|.
name|getAggregationCount
argument_list|()
argument_list|)
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
literal|"direct:forceCompletionTrue"
argument_list|)
operator|.
name|aggregate
argument_list|(
name|header
argument_list|(
literal|"id"
argument_list|)
argument_list|,
operator|new
name|BodyInAggregatingStrategy
argument_list|()
argument_list|)
operator|.
name|forceCompletionOnStop
argument_list|()
operator|.
name|completionSize
argument_list|(
literal|10
argument_list|)
operator|.
name|delay
argument_list|(
literal|100
argument_list|)
operator|.
name|process
argument_list|(
name|myCompletionProcessor
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:forceCompletionFalse"
argument_list|)
operator|.
name|aggregate
argument_list|(
name|header
argument_list|(
literal|"id"
argument_list|)
argument_list|,
operator|new
name|BodyInAggregatingStrategy
argument_list|()
argument_list|)
operator|.
name|completionSize
argument_list|(
literal|10
argument_list|)
operator|.
name|delay
argument_list|(
literal|100
argument_list|)
operator|.
name|process
argument_list|(
name|myCompletionProcessor
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

