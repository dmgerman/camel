begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.flink
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|flink
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|truth
operator|.
name|Truth
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
name|flink
operator|.
name|annotations
operator|.
name|AnnotatedDataSetCallback
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
name|apache
operator|.
name|camel
operator|.
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|flink
operator|.
name|api
operator|.
name|java
operator|.
name|DataSet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|flink
operator|.
name|api
operator|.
name|java
operator|.
name|ExecutionEnvironment
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
DECL|class|FlinkProducerTest
specifier|public
class|class
name|FlinkProducerTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|executionEnvironment
specifier|static
name|ExecutionEnvironment
name|executionEnvironment
init|=
name|Flinks
operator|.
name|createExecutionEnvironment
argument_list|()
decl_stmt|;
DECL|field|flinkUri
name|String
name|flinkUri
init|=
literal|"flink:dataSet?dataSet=#myDataSet"
decl_stmt|;
DECL|field|numberOfLinesInTestFile
name|int
name|numberOfLinesInTestFile
init|=
literal|19
decl_stmt|;
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
name|registry
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"myDataSet"
argument_list|,
name|executionEnvironment
operator|.
name|readTextFile
argument_list|(
literal|"src/test/resources/testds.txt"
argument_list|)
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"countLinesContaining"
argument_list|,
operator|new
name|DataSetCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Object
name|onDataSet
parameter_list|(
name|DataSet
name|ds
parameter_list|,
name|Object
modifier|...
name|payloads
parameter_list|)
block|{
try|try
block|{
return|return
name|ds
operator|.
name|count
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteDataSetCallback ()
specifier|public
name|void
name|shouldExecuteDataSetCallback
parameter_list|()
block|{
name|Long
name|linesCount
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
name|flinkUri
argument_list|,
literal|null
argument_list|,
name|FlinkConstants
operator|.
name|FLINK_DATASET_CALLBACK_HEADER
argument_list|,
operator|new
name|DataSetCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Object
name|onDataSet
parameter_list|(
name|DataSet
name|ds
parameter_list|,
name|Object
modifier|...
name|payloads
parameter_list|)
block|{
try|try
block|{
return|return
name|ds
operator|.
name|count
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
name|Truth
operator|.
name|assertThat
argument_list|(
name|linesCount
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|numberOfLinesInTestFile
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteDataSetCallbackWithSinglePayload ()
specifier|public
name|void
name|shouldExecuteDataSetCallbackWithSinglePayload
parameter_list|()
block|{
name|Long
name|linesCount
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
name|flinkUri
argument_list|,
literal|10
argument_list|,
name|FlinkConstants
operator|.
name|FLINK_DATASET_CALLBACK_HEADER
argument_list|,
operator|new
name|DataSetCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Object
name|onDataSet
parameter_list|(
name|DataSet
name|ds
parameter_list|,
name|Object
modifier|...
name|payloads
parameter_list|)
block|{
try|try
block|{
return|return
name|ds
operator|.
name|count
argument_list|()
operator|*
operator|(
name|int
operator|)
name|payloads
index|[
literal|0
index|]
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
name|Truth
operator|.
name|assertThat
argument_list|(
name|linesCount
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|numberOfLinesInTestFile
operator|*
literal|10
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteDataSetCallbackWithPayloads ()
specifier|public
name|void
name|shouldExecuteDataSetCallbackWithPayloads
parameter_list|()
block|{
name|Long
name|linesCount
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
name|flinkUri
argument_list|,
name|Arrays
operator|.
expr|<
name|Integer
operator|>
name|asList
argument_list|(
literal|10
argument_list|,
literal|10
argument_list|)
argument_list|,
name|FlinkConstants
operator|.
name|FLINK_DATASET_CALLBACK_HEADER
argument_list|,
operator|new
name|DataSetCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Object
name|onDataSet
parameter_list|(
name|DataSet
name|ds
parameter_list|,
name|Object
modifier|...
name|payloads
parameter_list|)
block|{
try|try
block|{
return|return
name|ds
operator|.
name|count
argument_list|()
operator|*
operator|(
name|int
operator|)
name|payloads
index|[
literal|0
index|]
operator|*
operator|(
name|int
operator|)
name|payloads
index|[
literal|1
index|]
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
name|Truth
operator|.
name|assertThat
argument_list|(
name|linesCount
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|numberOfLinesInTestFile
operator|*
literal|10
operator|*
literal|10
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldUseTransformationFromRegistry ()
specifier|public
name|void
name|shouldUseTransformationFromRegistry
parameter_list|()
block|{
name|Long
name|linesCount
init|=
name|template
operator|.
name|requestBody
argument_list|(
name|flinkUri
operator|+
literal|"&dataSetCallback=#countLinesContaining"
argument_list|,
literal|null
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
name|Truth
operator|.
name|assertThat
argument_list|(
name|linesCount
argument_list|)
operator|.
name|isGreaterThan
argument_list|(
literal|0L
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteVoidCallback ()
specifier|public
name|void
name|shouldExecuteVoidCallback
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|File
name|output
init|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"camel"
argument_list|,
literal|"flink"
argument_list|)
decl_stmt|;
name|output
operator|.
name|delete
argument_list|()
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|flinkUri
argument_list|,
literal|null
argument_list|,
name|FlinkConstants
operator|.
name|FLINK_DATASET_CALLBACK_HEADER
argument_list|,
operator|new
name|VoidDataSetCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|doOnDataSet
parameter_list|(
name|DataSet
name|ds
parameter_list|,
name|Object
modifier|...
name|payloads
parameter_list|)
block|{
name|ds
operator|.
name|writeAsText
argument_list|(
name|output
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|Truth
operator|.
name|assertThat
argument_list|(
name|output
operator|.
name|length
argument_list|()
argument_list|)
operator|.
name|isAtLeast
argument_list|(
literal|0L
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteAnnotatedCallback ()
specifier|public
name|void
name|shouldExecuteAnnotatedCallback
parameter_list|()
block|{
name|DataSetCallback
name|dataSetCallback
init|=
operator|new
name|AnnotatedDataSetCallback
argument_list|(
operator|new
name|Object
argument_list|()
block|{
annotation|@
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|flink
operator|.
name|annotations
operator|.
name|DataSetCallback
name|Long
name|countLines
parameter_list|(
name|DataSet
argument_list|<
name|String
argument_list|>
name|textFile
parameter_list|)
block|{
try|try
block|{
return|return
name|textFile
operator|.
name|count
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
argument_list|)
decl_stmt|;
name|long
name|pomLinesCount
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
name|flinkUri
argument_list|,
literal|null
argument_list|,
name|FlinkConstants
operator|.
name|FLINK_DATASET_CALLBACK_HEADER
argument_list|,
name|dataSetCallback
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
name|Truth
operator|.
name|assertThat
argument_list|(
name|pomLinesCount
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|19
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteAnnotatedVoidCallback ()
specifier|public
name|void
name|shouldExecuteAnnotatedVoidCallback
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|File
name|output
init|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"camel"
argument_list|,
literal|"flink"
argument_list|)
decl_stmt|;
name|output
operator|.
name|delete
argument_list|()
expr_stmt|;
name|DataSetCallback
name|dataSetCallback
init|=
operator|new
name|AnnotatedDataSetCallback
argument_list|(
operator|new
name|Object
argument_list|()
block|{
annotation|@
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|flink
operator|.
name|annotations
operator|.
name|DataSetCallback
name|void
name|countLines
parameter_list|(
name|DataSet
argument_list|<
name|String
argument_list|>
name|textFile
parameter_list|)
block|{
name|textFile
operator|.
name|writeAsText
argument_list|(
name|output
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|flinkUri
argument_list|,
literal|null
argument_list|,
name|FlinkConstants
operator|.
name|FLINK_DATASET_CALLBACK_HEADER
argument_list|,
name|dataSetCallback
argument_list|)
expr_stmt|;
name|Truth
operator|.
name|assertThat
argument_list|(
name|output
operator|.
name|length
argument_list|()
argument_list|)
operator|.
name|isAtLeast
argument_list|(
literal|0L
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteAnnotatedCallbackWithParameters ()
specifier|public
name|void
name|shouldExecuteAnnotatedCallbackWithParameters
parameter_list|()
block|{
name|DataSetCallback
name|dataSetCallback
init|=
operator|new
name|AnnotatedDataSetCallback
argument_list|(
operator|new
name|Object
argument_list|()
block|{
annotation|@
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|flink
operator|.
name|annotations
operator|.
name|DataSetCallback
name|Long
name|countLines
parameter_list|(
name|DataSet
argument_list|<
name|String
argument_list|>
name|textFile
parameter_list|,
name|int
name|first
parameter_list|,
name|int
name|second
parameter_list|)
block|{
try|try
block|{
return|return
name|textFile
operator|.
name|count
argument_list|()
operator|*
name|first
operator|*
name|second
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
argument_list|)
decl_stmt|;
name|long
name|pomLinesCount
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
name|flinkUri
argument_list|,
name|Arrays
operator|.
expr|<
name|Integer
operator|>
name|asList
argument_list|(
literal|10
argument_list|,
literal|10
argument_list|)
argument_list|,
name|FlinkConstants
operator|.
name|FLINK_DATASET_CALLBACK_HEADER
argument_list|,
name|dataSetCallback
argument_list|,
name|Long
operator|.
name|class
argument_list|)
decl_stmt|;
name|Truth
operator|.
name|assertThat
argument_list|(
name|pomLinesCount
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|numberOfLinesInTestFile
operator|*
literal|10
operator|*
literal|10
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

