begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spark
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spark
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
import|import static
name|java
operator|.
name|lang
operator|.
name|Runtime
operator|.
name|getRuntime
import|;
end_import

begin_import
import|import static
name|java
operator|.
name|util
operator|.
name|Arrays
operator|.
name|asList
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
name|spark
operator|.
name|annotations
operator|.
name|RddCallback
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
name|spark
operator|.
name|api
operator|.
name|java
operator|.
name|AbstractJavaRDDLike
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|spark
operator|.
name|api
operator|.
name|java
operator|.
name|JavaRDD
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|spark
operator|.
name|api
operator|.
name|java
operator|.
name|JavaSparkContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|spark
operator|.
name|sql
operator|.
name|DataFrame
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|spark
operator|.
name|sql
operator|.
name|hive
operator|.
name|HiveContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spark
operator|.
name|SparkConstants
operator|.
name|SPARK_DATAFRAME_CALLBACK_HEADER
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spark
operator|.
name|SparkConstants
operator|.
name|SPARK_RDD_CALLBACK_HEADER
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spark
operator|.
name|Sparks
operator|.
name|createLocalSparkContext
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spark
operator|.
name|annotations
operator|.
name|AnnotatedRddCallback
operator|.
name|annotatedRddCallback
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assume
operator|.
name|assumeTrue
import|;
end_import

begin_class
DECL|class|SparkProducerTest
specifier|public
class|class
name|SparkProducerTest
extends|extends
name|CamelTestSupport
block|{
comment|// Fixtures
DECL|field|sparkContext
specifier|static
name|JavaSparkContext
name|sparkContext
init|=
name|createLocalSparkContext
argument_list|()
decl_stmt|;
DECL|field|shouldRunHive
specifier|static
name|boolean
name|shouldRunHive
init|=
name|getRuntime
argument_list|()
operator|.
name|maxMemory
argument_list|()
operator|>
literal|2
operator|*
literal|1024
operator|*
literal|1024
operator|*
literal|1024
decl_stmt|;
DECL|field|hiveContext
specifier|static
name|HiveContext
name|hiveContext
decl_stmt|;
DECL|field|sparkUri
name|String
name|sparkUri
init|=
literal|"spark:rdd?rdd=#testFileRdd"
decl_stmt|;
DECL|field|sparkDataFrameUri
name|String
name|sparkDataFrameUri
init|=
literal|"spark:dataframe?dataFrame=#jsonCars"
decl_stmt|;
DECL|field|sparkHiveUri
name|String
name|sparkHiveUri
init|=
literal|"spark:hive"
decl_stmt|;
annotation|@
name|BeforeClass
DECL|method|beforeClass ()
specifier|public
specifier|static
name|void
name|beforeClass
parameter_list|()
block|{
if|if
condition|(
name|shouldRunHive
condition|)
block|{
name|hiveContext
operator|=
operator|new
name|HiveContext
argument_list|(
name|sparkContext
operator|.
name|sc
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Routes fixtures
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
literal|"testFileRdd"
argument_list|,
name|sparkContext
operator|.
name|textFile
argument_list|(
literal|"testrdd.txt"
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|shouldRunHive
condition|)
block|{
name|registry
operator|.
name|bind
argument_list|(
literal|"hiveContext"
argument_list|,
name|hiveContext
argument_list|)
expr_stmt|;
name|DataFrame
name|jsonCars
init|=
name|hiveContext
operator|.
name|read
argument_list|()
operator|.
name|json
argument_list|(
literal|"src/test/resources/cars.json"
argument_list|)
decl_stmt|;
name|jsonCars
operator|.
name|registerTempTable
argument_list|(
literal|"cars"
argument_list|)
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"jsonCars"
argument_list|,
name|jsonCars
argument_list|)
expr_stmt|;
block|}
name|registry
operator|.
name|bind
argument_list|(
literal|"countLinesTransformation"
argument_list|,
operator|new
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spark
operator|.
name|RddCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Object
name|onRdd
parameter_list|(
name|AbstractJavaRDDLike
name|rdd
parameter_list|,
name|Object
modifier|...
name|payloads
parameter_list|)
block|{
return|return
name|rdd
operator|.
name|count
argument_list|()
return|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
comment|// Tests
annotation|@
name|Test
DECL|method|shouldExecuteRddCallback ()
specifier|public
name|void
name|shouldExecuteRddCallback
parameter_list|()
block|{
name|long
name|linesCount
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
name|sparkUri
argument_list|,
literal|null
argument_list|,
name|SPARK_RDD_CALLBACK_HEADER
argument_list|,
operator|new
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spark
operator|.
name|RddCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Long
name|onRdd
parameter_list|(
name|AbstractJavaRDDLike
name|rdd
parameter_list|,
name|Object
modifier|...
name|payloads
parameter_list|)
block|{
return|return
name|rdd
operator|.
name|count
argument_list|()
return|;
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
literal|19
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteRddCallbackWithSinglePayload ()
specifier|public
name|void
name|shouldExecuteRddCallbackWithSinglePayload
parameter_list|()
block|{
name|long
name|pomLinesCount
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
name|sparkUri
argument_list|,
literal|10
argument_list|,
name|SPARK_RDD_CALLBACK_HEADER
argument_list|,
operator|new
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spark
operator|.
name|RddCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Long
name|onRdd
parameter_list|(
name|AbstractJavaRDDLike
name|rdd
parameter_list|,
name|Object
modifier|...
name|payloads
parameter_list|)
block|{
return|return
name|rdd
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
name|pomLinesCount
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|190
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteRddCallbackWithPayloads ()
specifier|public
name|void
name|shouldExecuteRddCallbackWithPayloads
parameter_list|()
block|{
name|long
name|pomLinesCount
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
name|sparkUri
argument_list|,
name|asList
argument_list|(
literal|10
argument_list|,
literal|10
argument_list|)
argument_list|,
name|SPARK_RDD_CALLBACK_HEADER
argument_list|,
operator|new
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spark
operator|.
name|RddCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Long
name|onRdd
parameter_list|(
name|AbstractJavaRDDLike
name|rdd
parameter_list|,
name|Object
modifier|...
name|payloads
parameter_list|)
block|{
return|return
name|rdd
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
name|pomLinesCount
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1900
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteRddCallbackWithTypedPayloads ()
specifier|public
name|void
name|shouldExecuteRddCallbackWithTypedPayloads
parameter_list|()
block|{
name|ConvertingRddCallback
name|rddCallback
init|=
operator|new
name|ConvertingRddCallback
argument_list|<
name|Long
argument_list|>
argument_list|(
name|context
argument_list|,
name|int
operator|.
name|class
argument_list|,
name|int
operator|.
name|class
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|Long
name|doOnRdd
parameter_list|(
name|AbstractJavaRDDLike
name|rdd
parameter_list|,
name|Object
modifier|...
name|payloads
parameter_list|)
block|{
return|return
name|rdd
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
block|}
decl_stmt|;
name|long
name|pomLinesCount
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
name|sparkUri
argument_list|,
name|asList
argument_list|(
literal|"10"
argument_list|,
literal|"10"
argument_list|)
argument_list|,
name|SPARK_RDD_CALLBACK_HEADER
argument_list|,
name|rddCallback
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
literal|1900
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
name|long
name|pomLinesCount
init|=
name|template
operator|.
name|requestBody
argument_list|(
name|sparkUri
operator|+
literal|"&rddCallback=#countLinesTransformation"
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
name|pomLinesCount
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
comment|// Given
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
literal|"spark"
argument_list|)
decl_stmt|;
name|output
operator|.
name|delete
argument_list|()
expr_stmt|;
comment|// When
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|sparkUri
argument_list|,
literal|null
argument_list|,
name|SPARK_RDD_CALLBACK_HEADER
argument_list|,
operator|new
name|VoidRddCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|doOnRdd
parameter_list|(
name|AbstractJavaRDDLike
name|rdd
parameter_list|,
name|Object
modifier|...
name|payloads
parameter_list|)
block|{
name|rdd
operator|.
name|saveAsTextFile
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
comment|// Then
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
name|isGreaterThan
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spark
operator|.
name|RddCallback
name|rddCallback
init|=
name|annotatedRddCallback
argument_list|(
operator|new
name|Object
argument_list|()
block|{
annotation|@
name|RddCallback
name|long
name|countLines
parameter_list|(
name|JavaRDD
argument_list|<
name|String
argument_list|>
name|textFile
parameter_list|)
block|{
return|return
name|textFile
operator|.
name|count
argument_list|()
return|;
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
name|sparkUri
argument_list|,
literal|null
argument_list|,
name|SPARK_RDD_CALLBACK_HEADER
argument_list|,
name|rddCallback
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
comment|// Given
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
literal|"spark"
argument_list|)
decl_stmt|;
name|output
operator|.
name|delete
argument_list|()
expr_stmt|;
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spark
operator|.
name|RddCallback
name|rddCallback
init|=
name|annotatedRddCallback
argument_list|(
operator|new
name|Object
argument_list|()
block|{
annotation|@
name|RddCallback
name|void
name|countLines
parameter_list|(
name|JavaRDD
argument_list|<
name|String
argument_list|>
name|textFile
parameter_list|)
block|{
name|textFile
operator|.
name|saveAsTextFile
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
comment|// When
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
name|sparkUri
argument_list|,
literal|null
argument_list|,
name|SPARK_RDD_CALLBACK_HEADER
argument_list|,
name|rddCallback
argument_list|)
expr_stmt|;
comment|// Then
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
name|isGreaterThan
argument_list|(
literal|0L
argument_list|)
expr_stmt|;
block|}
comment|// Hive tests
annotation|@
name|Test
DECL|method|shouldExecuteHiveQuery ()
specifier|public
name|void
name|shouldExecuteHiveQuery
parameter_list|()
block|{
name|assumeTrue
argument_list|(
name|shouldRunHive
argument_list|)
expr_stmt|;
name|long
name|tablesCount
init|=
name|template
operator|.
name|requestBody
argument_list|(
name|sparkHiveUri
operator|+
literal|"?collect=false"
argument_list|,
literal|"SELECT * FROM cars"
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
name|tablesCount
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
comment|// Data frames tests
annotation|@
name|Test
DECL|method|shouldCountFrame ()
specifier|public
name|void
name|shouldCountFrame
parameter_list|()
block|{
name|assumeTrue
argument_list|(
name|shouldRunHive
argument_list|)
expr_stmt|;
name|DataFrameCallback
name|callback
init|=
operator|new
name|DataFrameCallback
argument_list|<
name|Long
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Long
name|onDataFrame
parameter_list|(
name|DataFrame
name|dataFrame
parameter_list|,
name|Object
modifier|...
name|payloads
parameter_list|)
block|{
return|return
name|dataFrame
operator|.
name|count
argument_list|()
return|;
block|}
block|}
decl_stmt|;
name|long
name|tablesCount
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
name|sparkDataFrameUri
argument_list|,
literal|null
argument_list|,
name|SPARK_DATAFRAME_CALLBACK_HEADER
argument_list|,
name|callback
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
name|tablesCount
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteConditionalFrameCount ()
specifier|public
name|void
name|shouldExecuteConditionalFrameCount
parameter_list|()
block|{
name|assumeTrue
argument_list|(
name|shouldRunHive
argument_list|)
expr_stmt|;
name|DataFrameCallback
name|callback
init|=
operator|new
name|DataFrameCallback
argument_list|<
name|Long
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Long
name|onDataFrame
parameter_list|(
name|DataFrame
name|dataFrame
parameter_list|,
name|Object
modifier|...
name|payloads
parameter_list|)
block|{
name|String
name|model
init|=
operator|(
name|String
operator|)
name|payloads
index|[
literal|0
index|]
decl_stmt|;
return|return
name|dataFrame
operator|.
name|where
argument_list|(
name|dataFrame
operator|.
name|col
argument_list|(
literal|"model"
argument_list|)
operator|.
name|eqNullSafe
argument_list|(
name|model
argument_list|)
argument_list|)
operator|.
name|count
argument_list|()
return|;
block|}
block|}
decl_stmt|;
name|long
name|tablesCount
init|=
name|template
operator|.
name|requestBodyAndHeader
argument_list|(
name|sparkDataFrameUri
argument_list|,
literal|"Micra"
argument_list|,
name|SPARK_DATAFRAME_CALLBACK_HEADER
argument_list|,
name|callback
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
name|tablesCount
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

