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
name|hadoop
operator|.
name|hbase
operator|.
name|TableExistsException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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
DECL|class|HBaseConsumerTest
specifier|public
class|class
name|HBaseConsumerTest
extends|extends
name|CamelHBaseTestSupport
block|{
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
if|if
condition|(
name|systemReady
condition|)
block|{
try|try
block|{
name|hbaseUtil
operator|.
name|createTable
argument_list|(
name|HBaseHelper
operator|.
name|getHBaseFieldAsBytes
argument_list|(
name|DEFAULTTABLE
argument_list|)
argument_list|,
name|families
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TableExistsException
name|ex
parameter_list|)
block|{
comment|//Ignore if table exists
block|}
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|systemReady
condition|)
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testPutMultiRowsAndConsume ()
specifier|public
name|void
name|testPutMultiRowsAndConsume
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
name|family
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
name|family
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
name|family
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
name|MockEndpoint
name|mockEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mockEndpoint
operator|.
name|expectedMessageCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|mockEndpoint
operator|.
name|assertIsSatisfied
argument_list|(
literal|10000
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|10000
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
name|DEFAULTTABLE
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"hbase://"
operator|+
name|DEFAULTTABLE
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

