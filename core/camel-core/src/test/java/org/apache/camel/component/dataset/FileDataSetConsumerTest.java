begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|junit
operator|.
name|Assert
import|;
end_import

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

begin_class
DECL|class|FileDataSetConsumerTest
specifier|public
class|class
name|FileDataSetConsumerTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|dataSet
specifier|protected
name|FileDataSet
name|dataSet
decl_stmt|;
DECL|field|testDataFileName
specifier|final
name|String
name|testDataFileName
init|=
literal|"src/test/data/file-dataset-test.txt"
decl_stmt|;
DECL|field|resultUri
specifier|final
name|String
name|resultUri
init|=
literal|"mock://result"
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
literal|"foo"
argument_list|,
name|dataSet
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
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
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
name|resultUri
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedMinimumMessageCount
argument_list|(
operator|(
name|int
operator|)
name|dataSet
operator|.
name|getSize
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|assertIsSatisfied
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
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
name|resultUri
argument_list|)
decl_stmt|;
name|dataSet
operator|.
name|setSize
argument_list|(
literal|20
argument_list|)
expr_stmt|;
name|result
operator|.
name|expectedMinimumMessageCount
argument_list|(
operator|(
name|int
operator|)
name|dataSet
operator|.
name|getSize
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|assertIsSatisfied
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
name|dataSet
operator|=
operator|new
name|FileDataSet
argument_list|(
name|testDataFileName
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"Unexpected DataSet size"
argument_list|,
literal|1
argument_list|,
name|dataSet
operator|.
name|getSize
argument_list|()
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
name|dataSetUri
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock://result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit
