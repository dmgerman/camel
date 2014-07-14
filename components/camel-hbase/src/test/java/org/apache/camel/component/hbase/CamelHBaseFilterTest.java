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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Endpoint
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
name|ExchangePattern
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
name|Message
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
name|hbase
operator|.
name|filters
operator|.
name|ModelAwareColumnMatchingFilter
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
name|hadoop
operator|.
name|hbase
operator|.
name|filter
operator|.
name|Filter
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
DECL|class|CamelHBaseFilterTest
specifier|public
class|class
name|CamelHBaseFilterTest
extends|extends
name|CamelHBaseTestSupport
block|{
DECL|field|filters
name|List
argument_list|<
name|Filter
argument_list|>
name|filters
init|=
operator|new
name|LinkedList
argument_list|<
name|Filter
argument_list|>
argument_list|()
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
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|filters
operator|.
name|add
argument_list|(
operator|new
name|ModelAwareColumnMatchingFilter
argument_list|()
argument_list|)
expr_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"myFilters"
argument_list|,
name|filters
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
annotation|@
name|Test
DECL|method|testPutMultiRowsAndScanWithFilters ()
specifier|public
name|void
name|testPutMultiRowsAndScanWithFilters
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|systemReady
condition|)
block|{
name|putMultipleRows
argument_list|()
expr_stmt|;
name|ProducerTemplate
name|template
init|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"direct:scan"
argument_list|)
decl_stmt|;
name|Exchange
name|exchange
init|=
name|endpoint
operator|.
name|createExchange
argument_list|(
name|ExchangePattern
operator|.
name|InOut
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
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
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
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
index|[
literal|0
index|]
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|Exchange
name|resp
init|=
name|template
operator|.
name|send
argument_list|(
name|endpoint
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
name|Message
name|out
init|=
name|resp
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|out
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsValue
argument_list|(
name|body
index|[
literal|0
index|]
index|[
literal|0
index|]
index|[
literal|0
index|]
argument_list|)
operator|&&
operator|!
name|out
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsValue
argument_list|(
name|body
index|[
literal|1
index|]
index|[
literal|0
index|]
index|[
literal|0
index|]
argument_list|)
operator|&&
operator|!
name|out
operator|.
name|getHeaders
argument_list|()
operator|.
name|containsValue
argument_list|(
name|body
index|[
literal|2
index|]
index|[
literal|0
index|]
index|[
literal|0
index|]
argument_list|)
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
literal|"&maxResults=2&filters=#myFilters"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

