begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.aggregate.zipfile
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|aggregate
operator|.
name|zipfile
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
name|FileInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|ZipEntry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|ZipInputStream
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
name|camel
operator|.
name|util
operator|.
name|IOHelper
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
DECL|class|ZipAggregationStrategyEmptyFileTest
specifier|public
class|class
name|ZipAggregationStrategyEmptyFileTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|EXPECTED_NO_FILES
specifier|private
specifier|static
specifier|final
name|int
name|EXPECTED_NO_FILES
init|=
literal|3
decl_stmt|;
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
name|deleteDirectory
argument_list|(
literal|"target/foo"
argument_list|)
expr_stmt|;
name|deleteDirectory
argument_list|(
literal|"target/out"
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testEmptyFile ()
specifier|public
name|void
name|testEmptyFile
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:aggregateToZipEntry"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"file:target/foo"
argument_list|,
literal|"Hello"
argument_list|)
expr_stmt|;
comment|// empty file which is not aggregated
name|template
operator|.
name|sendBody
argument_list|(
literal|"file:target/foo"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"file:target/foo"
argument_list|,
literal|"Bye"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"file:target/foo"
argument_list|,
literal|"Howdy"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|File
index|[]
name|files
init|=
operator|new
name|File
argument_list|(
literal|"target/out"
argument_list|)
operator|.
name|listFiles
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|files
operator|!=
literal|null
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should be a file in target/out directory"
argument_list|,
name|files
operator|.
name|length
operator|>
literal|0
argument_list|)
expr_stmt|;
name|File
name|resultFile
init|=
name|files
index|[
literal|0
index|]
decl_stmt|;
name|ZipInputStream
name|zin
init|=
operator|new
name|ZipInputStream
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|resultFile
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|int
name|fileCount
init|=
literal|0
decl_stmt|;
for|for
control|(
name|ZipEntry
name|ze
init|=
name|zin
operator|.
name|getNextEntry
argument_list|()
init|;
name|ze
operator|!=
literal|null
condition|;
name|ze
operator|=
name|zin
operator|.
name|getNextEntry
argument_list|()
control|)
block|{
name|fileCount
operator|=
name|fileCount
operator|+
literal|1
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|"Zip file should contains "
operator|+
name|ZipAggregationStrategyEmptyFileTest
operator|.
name|EXPECTED_NO_FILES
operator|+
literal|" files"
argument_list|,
name|ZipAggregationStrategyEmptyFileTest
operator|.
name|EXPECTED_NO_FILES
argument_list|,
name|fileCount
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|zin
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
literal|"file:target/foo"
argument_list|)
operator|.
name|aggregate
argument_list|(
operator|new
name|ZipAggregationStrategy
argument_list|()
argument_list|)
operator|.
name|constant
argument_list|(
literal|true
argument_list|)
operator|.
name|completionSize
argument_list|(
literal|4
argument_list|)
operator|.
name|eagerCheckCompletion
argument_list|()
operator|.
name|to
argument_list|(
literal|"file:target/out"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:aggregateToZipEntry"
argument_list|)
operator|.
name|log
argument_list|(
literal|"Done processing zip file: ${header.CamelFileName}"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

