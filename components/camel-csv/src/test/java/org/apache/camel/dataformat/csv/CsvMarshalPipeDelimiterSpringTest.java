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
name|util
operator|.
name|ArrayList
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|EndpointInject
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
name|spring
operator|.
name|CamelSpringTestSupport
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
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|ClassPathXmlApplicationContext
import|;
end_import

begin_comment
comment|/**  * Spring based integration test for the<code>CsvDataFormat</code>  */
end_comment

begin_class
DECL|class|CsvMarshalPipeDelimiterSpringTest
specifier|public
class|class
name|CsvMarshalPipeDelimiterSpringTest
extends|extends
name|CamelSpringTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result"
argument_list|)
DECL|field|result
specifier|private
name|MockEndpoint
name|result
decl_stmt|;
annotation|@
name|Test
DECL|method|testCsvMarshal ()
specifier|public
name|void
name|testCsvMarshal
parameter_list|()
throws|throws
name|Exception
block|{
name|result
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
literal|"direct:start"
argument_list|,
name|createBody
argument_list|()
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|String
name|body
init|=
name|result
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
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
name|String
index|[]
name|lines
init|=
name|body
operator|.
name|split
argument_list|(
name|System
operator|.
name|lineSeparator
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|lines
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"123|Camel in Action|1"
argument_list|,
name|lines
index|[
literal|0
index|]
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"124|ActiveMQ in Action|2"
argument_list|,
name|lines
index|[
literal|1
index|]
operator|.
name|trim
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createBody ()
specifier|private
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|createBody
parameter_list|()
block|{
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|data
init|=
operator|new
name|ArrayList
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|row1
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|row1
operator|.
name|put
argument_list|(
literal|"orderId"
argument_list|,
literal|123
argument_list|)
expr_stmt|;
name|row1
operator|.
name|put
argument_list|(
literal|"item"
argument_list|,
literal|"Camel in Action"
argument_list|)
expr_stmt|;
name|row1
operator|.
name|put
argument_list|(
literal|"amount"
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|data
operator|.
name|add
argument_list|(
name|row1
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|row2
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|row2
operator|.
name|put
argument_list|(
literal|"orderId"
argument_list|,
literal|124
argument_list|)
expr_stmt|;
name|row2
operator|.
name|put
argument_list|(
literal|"item"
argument_list|,
literal|"ActiveMQ in Action"
argument_list|)
expr_stmt|;
name|row2
operator|.
name|put
argument_list|(
literal|"amount"
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|data
operator|.
name|add
argument_list|(
name|row2
argument_list|)
expr_stmt|;
return|return
name|data
return|;
block|}
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|ClassPathXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/dataformat/csv/CsvMarshalPipeDelimiterSpringTest-context.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

