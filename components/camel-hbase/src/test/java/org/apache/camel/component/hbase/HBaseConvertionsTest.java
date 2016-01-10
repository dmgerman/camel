begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hbase
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hbase
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|util
operator|.
name|IOHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|conf
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|client
operator|.
name|Get
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|client
operator|.
name|HTable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|client
operator|.
name|Result
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|util
operator|.
name|Bytes
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
DECL|class|HBaseConvertionsTest
specifier|public
class|class
name|HBaseConvertionsTest
extends|extends
name|CamelHBaseTestSupport
block|{
DECL|field|key
specifier|protected
name|Object
index|[]
name|key
init|=
block|{
literal|1
block|,
literal|"2"
block|,
literal|"3"
block|}
decl_stmt|;
DECL|field|body
specifier|protected
specifier|final
name|Object
index|[]
name|body
init|=
block|{
literal|1L
block|,
literal|false
block|,
literal|"3"
block|}
decl_stmt|;
DECL|field|column
specifier|protected
specifier|final
name|String
index|[]
name|column
init|=
block|{
literal|"DEFAULTCOLUMN"
block|}
decl_stmt|;
DECL|field|families
specifier|protected
specifier|final
name|byte
index|[]
index|[]
name|families
init|=
block|{
name|INFO_FAMILY
operator|.
name|getBytes
argument_list|()
block|}
decl_stmt|;
annotation|@
name|Test
DECL|method|testPutMultiRows ()
specifier|public
name|void
name|testPutMultiRows
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|systemReady
condition|)
block|{
name|ProducerTemplate
name|template
init|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|HbaseAttribute
operator|.
name|HBASE_ROW_ID
operator|.
name|asHeader
argument_list|()
argument_list|,
name|key
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|HbaseAttribute
operator|.
name|HBASE_FAMILY
operator|.
name|asHeader
argument_list|()
argument_list|,
name|INFO_FAMILY
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|HbaseAttribute
operator|.
name|HBASE_QUALIFIER
operator|.
name|asHeader
argument_list|()
argument_list|,
name|column
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|HbaseAttribute
operator|.
name|HBASE_VALUE
operator|.
name|asHeader
argument_list|()
argument_list|,
name|body
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|HbaseAttribute
operator|.
name|HBASE_ROW_ID
operator|.
name|asHeader
argument_list|(
literal|2
argument_list|)
argument_list|,
name|key
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|HbaseAttribute
operator|.
name|HBASE_FAMILY
operator|.
name|asHeader
argument_list|(
literal|2
argument_list|)
argument_list|,
name|INFO_FAMILY
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|HbaseAttribute
operator|.
name|HBASE_QUALIFIER
operator|.
name|asHeader
argument_list|(
literal|2
argument_list|)
argument_list|,
name|column
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|HbaseAttribute
operator|.
name|HBASE_VALUE
operator|.
name|asHeader
argument_list|(
literal|2
argument_list|)
argument_list|,
name|body
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|HbaseAttribute
operator|.
name|HBASE_ROW_ID
operator|.
name|asHeader
argument_list|(
literal|3
argument_list|)
argument_list|,
name|key
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|HbaseAttribute
operator|.
name|HBASE_FAMILY
operator|.
name|asHeader
argument_list|(
literal|3
argument_list|)
argument_list|,
name|INFO_FAMILY
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|HbaseAttribute
operator|.
name|HBASE_QUALIFIER
operator|.
name|asHeader
argument_list|(
literal|3
argument_list|)
argument_list|,
name|column
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|HbaseAttribute
operator|.
name|HBASE_VALUE
operator|.
name|asHeader
argument_list|(
literal|3
argument_list|)
argument_list|,
name|body
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|HBaseConstants
operator|.
name|OPERATION
argument_list|,
name|HBaseConstants
operator|.
name|PUT
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
literal|null
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|Configuration
name|configuration
init|=
name|hbaseUtil
operator|.
name|getHBaseAdmin
argument_list|()
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|HTable
name|bar
init|=
operator|new
name|HTable
argument_list|(
name|configuration
argument_list|,
name|PERSON_TABLE
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|Get
name|get
init|=
operator|new
name|Get
argument_list|(
name|Bytes
operator|.
name|toBytes
argument_list|(
operator|(
name|Integer
operator|)
name|key
index|[
literal|0
index|]
argument_list|)
argument_list|)
decl_stmt|;
comment|//Check row 1
name|get
operator|.
name|addColumn
argument_list|(
name|INFO_FAMILY
operator|.
name|getBytes
argument_list|()
argument_list|,
name|column
index|[
literal|0
index|]
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|Result
name|result
init|=
name|bar
operator|.
name|get
argument_list|(
name|get
argument_list|)
decl_stmt|;
name|byte
index|[]
name|resultValue
init|=
name|result
operator|.
name|value
argument_list|()
decl_stmt|;
name|assertArrayEquals
argument_list|(
name|Bytes
operator|.
name|toBytes
argument_list|(
operator|(
name|Long
operator|)
name|body
index|[
literal|0
index|]
argument_list|)
argument_list|,
name|resultValue
argument_list|)
expr_stmt|;
comment|//Check row 2
name|get
operator|=
operator|new
name|Get
argument_list|(
name|Bytes
operator|.
name|toBytes
argument_list|(
operator|(
name|String
operator|)
name|key
index|[
literal|1
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|get
operator|.
name|addColumn
argument_list|(
name|INFO_FAMILY
operator|.
name|getBytes
argument_list|()
argument_list|,
name|column
index|[
literal|0
index|]
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|=
name|bar
operator|.
name|get
argument_list|(
name|get
argument_list|)
expr_stmt|;
name|resultValue
operator|=
name|result
operator|.
name|value
argument_list|()
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|Bytes
operator|.
name|toBytes
argument_list|(
operator|(
name|Boolean
operator|)
name|body
index|[
literal|1
index|]
argument_list|)
argument_list|,
name|resultValue
argument_list|)
expr_stmt|;
comment|//Check row 3
name|get
operator|=
operator|new
name|Get
argument_list|(
name|Bytes
operator|.
name|toBytes
argument_list|(
operator|(
name|String
operator|)
name|key
index|[
literal|2
index|]
argument_list|)
argument_list|)
expr_stmt|;
name|get
operator|.
name|addColumn
argument_list|(
name|INFO_FAMILY
operator|.
name|getBytes
argument_list|()
argument_list|,
name|column
index|[
literal|0
index|]
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|=
name|bar
operator|.
name|get
argument_list|(
name|get
argument_list|)
expr_stmt|;
name|resultValue
operator|=
name|result
operator|.
name|value
argument_list|()
expr_stmt|;
name|assertArrayEquals
argument_list|(
name|Bytes
operator|.
name|toBytes
argument_list|(
operator|(
name|String
operator|)
name|body
index|[
literal|2
index|]
argument_list|)
argument_list|,
name|resultValue
argument_list|)
expr_stmt|;
name|IOHelper
operator|.
name|close
argument_list|(
name|bar
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Factory method which derived classes can use to create a {@link org.apache.camel.builder.RouteBuilder}      * to define the routes for testing      */
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
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"hbase://"
operator|+
name|PERSON_TABLE
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:scan"
argument_list|)
operator|.
name|to
argument_list|(
literal|"hbase://"
operator|+
name|PERSON_TABLE
operator|+
literal|"?operation="
operator|+
name|HBaseConstants
operator|.
name|SCAN
operator|+
literal|"&maxResults=2&row.family=family1&row.qualifier=column1"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

