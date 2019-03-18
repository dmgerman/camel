begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|seda
operator|.
name|SedaEndpoint
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
DECL|class|ConsumerTemplateSedaQueueIssueTest
specifier|public
class|class
name|ConsumerTemplateSedaQueueIssueTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Test
DECL|method|testConsumerTemplateSedaQueue ()
specifier|public
name|void
name|testConsumerTemplateSedaQueue
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"A"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"B"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"C"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"D"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"E"
argument_list|)
expr_stmt|;
name|SedaEndpoint
name|seda
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"seda:foo"
argument_list|,
name|SedaEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|seda
operator|.
name|getCurrentQueueSize
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|body
init|=
name|consumer
operator|.
name|receiveBody
argument_list|(
name|seda
argument_list|,
literal|1000
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"A"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|seda
operator|.
name|getCurrentQueueSize
argument_list|()
argument_list|)
expr_stmt|;
name|body
operator|=
name|consumer
operator|.
name|receiveBody
argument_list|(
name|seda
argument_list|,
literal|1000
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"B"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|seda
operator|.
name|getCurrentQueueSize
argument_list|()
argument_list|)
expr_stmt|;
name|body
operator|=
name|consumer
operator|.
name|receiveBody
argument_list|(
name|seda
argument_list|,
literal|1000
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"C"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|seda
operator|.
name|getCurrentQueueSize
argument_list|()
argument_list|)
expr_stmt|;
name|body
operator|=
name|consumer
operator|.
name|receiveBody
argument_list|(
name|seda
argument_list|,
literal|1000
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"D"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|seda
operator|.
name|getCurrentQueueSize
argument_list|()
argument_list|)
expr_stmt|;
name|body
operator|=
name|consumer
operator|.
name|receiveBody
argument_list|(
name|seda
argument_list|,
literal|1000
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"E"
argument_list|,
name|body
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|seda
operator|.
name|getCurrentQueueSize
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
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"seda:foo"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

