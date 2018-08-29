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
name|org
operator|.
name|junit
operator|.
name|Before
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
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|javax
operator|.
name|naming
operator|.
name|Context
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|ListDataSetProducerTest
specifier|public
class|class
name|ListDataSetProducerTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|dataSet
specifier|protected
name|ListDataSet
name|dataSet
init|=
operator|new
name|ListDataSet
argument_list|()
decl_stmt|;
DECL|field|sourceUri
specifier|final
name|String
name|sourceUri
init|=
literal|"direct://source"
decl_stmt|;
DECL|field|dataSetName
specifier|final
name|String
name|dataSetName
init|=
literal|"foo"
decl_stmt|;
DECL|field|dataSetUri
specifier|final
name|String
name|dataSetUri
init|=
literal|"dataset://"
operator|+
name|dataSetName
decl_stmt|;
annotation|@
name|Test
DECL|method|testDefaultListDataSet ()
specifier|public
name|void
name|testDefaultListDataSet
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|dataSetUri
argument_list|,
literal|"<hello>world!</hello>"
argument_list|,
name|Exchange
operator|.
name|DATASET_INDEX
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testDefaultListDataSetWithSizeGreaterThanListSize ()
specifier|public
name|void
name|testDefaultListDataSetWithSizeGreaterThanListSize
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|messageCount
init|=
literal|10
decl_stmt|;
name|getMockEndpoint
argument_list|(
name|dataSetUri
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
name|messageCount
argument_list|)
expr_stmt|;
name|dataSet
operator|.
name|setSize
argument_list|(
name|messageCount
argument_list|)
expr_stmt|;
name|long
name|size
init|=
name|dataSet
operator|.
name|getSize
argument_list|()
decl_stmt|;
for|for
control|(
name|long
name|i
init|=
literal|0
init|;
name|i
operator|<
name|size
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|sourceUri
argument_list|,
literal|"<hello>world!</hello>"
argument_list|,
name|Exchange
operator|.
name|DATASET_INDEX
argument_list|,
name|i
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|bodies
init|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
decl_stmt|;
name|bodies
operator|.
name|add
argument_list|(
literal|"<hello>world!</hello>"
argument_list|)
expr_stmt|;
name|dataSet
operator|=
operator|new
name|ListDataSet
argument_list|(
name|bodies
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createJndiContext ()
specifier|protected
name|Context
name|createJndiContext
parameter_list|()
throws|throws
name|Exception
block|{
name|Context
name|context
init|=
name|super
operator|.
name|createJndiContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|bind
argument_list|(
name|dataSetName
argument_list|,
name|dataSet
argument_list|)
expr_stmt|;
return|return
name|context
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
name|sourceUri
argument_list|)
operator|.
name|to
argument_list|(
name|dataSetUri
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

