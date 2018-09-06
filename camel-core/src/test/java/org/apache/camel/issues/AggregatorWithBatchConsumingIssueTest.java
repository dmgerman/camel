begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.issues
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|issues
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
name|processor
operator|.
name|BodyInAggregatingStrategy
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|AggregatorWithBatchConsumingIssueTest
specifier|public
class|class
name|AggregatorWithBatchConsumingIssueTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testAggregateLostGroupIssue ()
specifier|public
name|void
name|testAggregateLostGroupIssue
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
name|expectedMessageCount
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"0+1+2"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|1
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"3+4+5"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|2
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"6+7+8"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|3
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isEqualTo
argument_list|(
literal|"9+10+11"
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|12
condition|;
name|i
operator|++
control|)
block|{
name|sendMessage
argument_list|(
name|i
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|method|sendMessage (final int index)
specifier|private
name|void
name|sendMessage
parameter_list|(
specifier|final
name|int
name|index
parameter_list|)
block|{
name|template
operator|.
name|send
argument_list|(
literal|"direct:start"
argument_list|,
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|index
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"aggregateGroup"
argument_list|,
literal|"group1"
argument_list|)
expr_stmt|;
comment|//simulate Batch as in JpaConsumer
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_SIZE
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_INDEX
argument_list|,
name|index
operator|%
literal|3
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|BATCH_COMPLETE
argument_list|,
operator|(
name|index
operator|%
literal|3
operator|)
operator|==
literal|3
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
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
literal|"direct:start"
argument_list|)
operator|.
name|aggregate
argument_list|(
name|header
argument_list|(
literal|"aggregateGroup"
argument_list|)
argument_list|,
operator|new
name|BodyInAggregatingStrategy
argument_list|()
argument_list|)
operator|.
name|completionFromBatchConsumer
argument_list|()
operator|.
name|to
argument_list|(
literal|"log:aggregated"
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

