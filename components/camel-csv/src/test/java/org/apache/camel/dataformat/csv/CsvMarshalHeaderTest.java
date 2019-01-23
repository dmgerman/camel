begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.csv
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|csv
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
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
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
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
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
name|Produce
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
name|ProducerTemplate
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
name|RoutesBuilder
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
name|junit
operator|.
name|Rule
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
name|org
operator|.
name|junit
operator|.
name|rules
operator|.
name|TemporaryFolder
import|;
end_import

begin_comment
comment|/**  *<b>Camel</b> based test cases for {@link org.apache.camel.dataformat.csv.CsvDataFormat}.  */
end_comment

begin_class
DECL|class|CsvMarshalHeaderTest
specifier|public
class|class
name|CsvMarshalHeaderTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Rule
DECL|field|folder
specifier|public
name|TemporaryFolder
name|folder
init|=
operator|new
name|TemporaryFolder
argument_list|()
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:start"
argument_list|)
DECL|field|producerTemplate
specifier|private
name|ProducerTemplate
name|producerTemplate
decl_stmt|;
DECL|field|outputFile
specifier|private
name|File
name|outputFile
decl_stmt|;
annotation|@
name|Override
DECL|method|doPreSetup ()
specifier|protected
name|void
name|doPreSetup
parameter_list|()
throws|throws
name|Exception
block|{
name|outputFile
operator|=
operator|new
name|File
argument_list|(
name|folder
operator|.
name|newFolder
argument_list|()
argument_list|,
literal|"output.csv"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendBody ()
specifier|public
name|void
name|testSendBody
parameter_list|()
throws|throws
name|IOException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|body
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|body
operator|.
name|put
argument_list|(
literal|"first_name"
argument_list|,
literal|"John"
argument_list|)
expr_stmt|;
name|body
operator|.
name|put
argument_list|(
literal|"last_name"
argument_list|,
literal|"Doe"
argument_list|)
expr_stmt|;
name|String
name|fileName
init|=
name|outputFile
operator|.
name|getName
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"output.csv"
argument_list|,
name|fileName
argument_list|)
expr_stmt|;
name|producerTemplate
operator|.
name|sendBodyAndHeader
argument_list|(
name|body
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
name|fileName
argument_list|)
expr_stmt|;
name|body
operator|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|body
operator|.
name|put
argument_list|(
literal|"first_name"
argument_list|,
literal|"Max"
argument_list|)
expr_stmt|;
name|body
operator|.
name|put
argument_list|(
literal|"last_name"
argument_list|,
literal|"Mustermann"
argument_list|)
expr_stmt|;
name|producerTemplate
operator|.
name|sendBodyAndHeader
argument_list|(
name|body
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
name|fileName
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|lines
init|=
name|Files
operator|.
name|lines
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|outputFile
operator|.
name|toURI
argument_list|()
argument_list|)
argument_list|)
operator|.
name|filter
argument_list|(
name|l
lambda|->
name|l
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
decl_stmt|;
comment|// We got twice the headers... :(
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|lines
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testSendBodyWithList ()
specifier|public
name|void
name|testSendBodyWithList
parameter_list|()
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|body
init|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"John"
argument_list|,
literal|"Doe"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|fileName
init|=
name|outputFile
operator|.
name|getName
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"output.csv"
argument_list|,
name|fileName
argument_list|)
expr_stmt|;
name|producerTemplate
operator|.
name|sendBodyAndHeader
argument_list|(
name|body
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
name|fileName
argument_list|)
expr_stmt|;
name|body
operator|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
literal|"Max"
argument_list|,
literal|"Mustermann"
argument_list|)
argument_list|)
expr_stmt|;
name|producerTemplate
operator|.
name|sendBodyAndHeader
argument_list|(
name|body
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
name|fileName
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|lines
init|=
name|Files
operator|.
name|lines
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
name|outputFile
operator|.
name|toURI
argument_list|()
argument_list|)
argument_list|)
operator|.
name|filter
argument_list|(
name|l
lambda|->
name|l
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
decl_stmt|;
comment|// We got twice the headers... :(
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|lines
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RoutesBuilder
name|createRouteBuilder
parameter_list|()
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
block|{
name|String
name|uri
init|=
name|String
operator|.
name|format
argument_list|(
literal|"file:%s?charset=utf-8&fileExist=Append"
argument_list|,
name|outputFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
decl_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|marshal
argument_list|(
name|createCsvDataFormat
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|createCsvDataFormat ()
specifier|private
specifier|static
name|CsvDataFormat
name|createCsvDataFormat
parameter_list|()
block|{
name|CsvDataFormat
name|dataFormat
init|=
operator|new
name|CsvDataFormat
argument_list|()
decl_stmt|;
name|dataFormat
operator|.
name|setDelimiter
argument_list|(
literal|'\t'
argument_list|)
expr_stmt|;
name|dataFormat
operator|.
name|setTrim
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|dataFormat
operator|.
name|setIgnoreSurroundingSpaces
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|dataFormat
operator|.
name|setHeader
argument_list|(
operator|(
name|String
index|[]
operator|)
name|Arrays
operator|.
name|asList
argument_list|(
literal|"first_name"
argument_list|,
literal|"last_name"
argument_list|)
operator|.
name|toArray
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|dataFormat
return|;
block|}
block|}
end_class

end_unit
