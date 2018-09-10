begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.dataset
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|dataset
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicBoolean
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
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|DataSetEndpointTest
specifier|public
class|class
name|DataSetEndpointTest
extends|extends
name|ContextTestSupport
block|{
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Test
DECL|method|testDataSetEndpoint ()
specifier|public
name|void
name|testDataSetEndpoint
parameter_list|()
throws|throws
name|Exception
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
specifier|final
name|DataSetEndpoint
name|endpoint
init|=
operator|new
name|DataSetEndpoint
argument_list|(
literal|"dataset://foo"
argument_list|,
literal|null
argument_list|,
operator|new
name|SimpleDataSet
argument_list|(
literal|2
argument_list|)
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setCamelContext
argument_list|(
name|context
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setInitialDelay
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|endpoint
operator|.
name|getPreloadSize
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|endpoint
operator|.
name|getConsumeDelay
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|endpoint
operator|.
name|getProduceDelay
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
name|endpoint
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:foo"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|to
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|endpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDataSetEndpointCtr ()
specifier|public
name|void
name|testDataSetEndpointCtr
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|DataSetEndpoint
name|endpoint
init|=
operator|new
name|DataSetEndpoint
argument_list|(
literal|"dataset://foo"
argument_list|,
name|context
operator|.
name|getComponent
argument_list|(
literal|"dataset"
argument_list|)
argument_list|,
operator|new
name|SimpleDataSet
argument_list|(
literal|2
argument_list|)
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setConsumeDelay
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|endpoint
operator|.
name|getConsumeDelay
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setProduceDelay
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|endpoint
operator|.
name|getProduceDelay
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setInitialDelay
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|endpoint
operator|.
name|getInitialDelay
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
name|endpoint
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:foo"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|to
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|endpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDataSetReporter ()
specifier|public
name|void
name|testDataSetReporter
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|DataSetEndpoint
name|endpoint
init|=
operator|new
name|DataSetEndpoint
argument_list|(
literal|"dataset://foo"
argument_list|,
name|context
operator|.
name|getComponent
argument_list|(
literal|"dataset"
argument_list|)
argument_list|,
operator|new
name|SimpleDataSet
argument_list|(
literal|10
argument_list|)
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setInitialDelay
argument_list|(
literal|0
argument_list|)
expr_stmt|;
specifier|final
name|AtomicBoolean
name|reported
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setReporter
argument_list|(
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
name|reported
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
name|endpoint
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:foo"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|to
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|endpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|assertTrue
argument_list|(
name|reported
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSimpleDataSet ()
specifier|public
name|void
name|testSimpleDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|SimpleDataSet
name|ds
init|=
operator|new
name|SimpleDataSet
argument_list|()
decl_stmt|;
name|ds
operator|.
name|setSize
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|ds
operator|.
name|setDefaultBody
argument_list|(
literal|"Hi"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hi"
argument_list|,
name|ds
operator|.
name|getDefaultBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDataSetSupport ()
specifier|public
name|void
name|testDataSetSupport
parameter_list|()
throws|throws
name|Exception
block|{
name|MyDataSet
name|ds
init|=
operator|new
name|MyDataSet
argument_list|()
decl_stmt|;
name|ds
operator|.
name|setSize
argument_list|(
literal|4
argument_list|)
expr_stmt|;
name|ds
operator|.
name|setReportCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|ds
operator|.
name|setOutputTransformer
argument_list|(
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
name|body
init|=
literal|"Hi "
operator|+
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
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ds
operator|.
name|getOutputTransformer
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|DataSetEndpoint
name|endpoint
init|=
operator|new
name|DataSetEndpoint
argument_list|(
literal|"dataset://foo"
argument_list|,
name|context
operator|.
name|getComponent
argument_list|(
literal|"dataset"
argument_list|)
argument_list|,
name|ds
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setInitialDelay
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|allMessages
argument_list|()
operator|.
name|body
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"Hi "
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
name|endpoint
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:foo"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:foo"
argument_list|)
operator|.
name|to
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|endpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
DECL|class|MyDataSet
specifier|private
specifier|static
class|class
name|MyDataSet
extends|extends
name|DataSetSupport
block|{
annotation|@
name|Override
DECL|method|createMessageBody (long messageIndex)
specifier|protected
name|Object
name|createMessageBody
parameter_list|(
name|long
name|messageIndex
parameter_list|)
block|{
return|return
literal|"Message "
operator|+
name|messageIndex
return|;
block|}
block|}
block|}
end_class

end_unit

