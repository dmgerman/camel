begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.flatpack
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|flatpack
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
name|converter
operator|.
name|IOConverter
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
name|dataformat
operator|.
name|flatpack
operator|.
name|FlatpackDataFormat
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
name|Test
import|;
end_import

begin_comment
comment|/**  * Unit test for delimited DataFormat.  */
end_comment

begin_class
DECL|class|FlatpackDelimitedDataFormatTest
specifier|public
class|class
name|FlatpackDelimitedDataFormatTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testUnmarshal ()
specifier|public
name|void
name|testUnmarshal
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:unmarshal"
argument_list|)
decl_stmt|;
comment|// by default we get on big message
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|DataSetList
operator|.
name|class
argument_list|)
expr_stmt|;
name|String
name|data
init|=
name|IOConverter
operator|.
name|toString
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/data/delim/INVENTORY-CommaDelimitedWithQualifier.txt"
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:unmarshal"
argument_list|,
name|data
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|DataSetList
name|list
init|=
name|mock
operator|.
name|getExchanges
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
name|DataSetList
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|row
init|=
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"SOME VALVE"
argument_list|,
name|row
operator|.
name|get
argument_list|(
literal|"ITEM_DESC"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMarshalWithDefinition ()
specifier|public
name|void
name|testMarshalWithDefinition
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:marshal"
argument_list|)
decl_stmt|;
comment|// by default we get on big message
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
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
argument_list|<>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|row
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"ITEM_DESC"
argument_list|,
literal|"SOME VALVE"
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"IN_STOCK"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"PRICE"
argument_list|,
literal|"5.00"
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"LAST_RECV_DT"
argument_list|,
literal|"20050101"
argument_list|)
expr_stmt|;
name|data
operator|.
name|add
argument_list|(
name|row
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
argument_list|<>
argument_list|()
decl_stmt|;
name|row2
operator|.
name|put
argument_list|(
literal|"ITEM_DESC"
argument_list|,
literal|"AN ENGINE"
argument_list|)
expr_stmt|;
name|row2
operator|.
name|put
argument_list|(
literal|"IN_STOCK"
argument_list|,
literal|"100"
argument_list|)
expr_stmt|;
name|row2
operator|.
name|put
argument_list|(
literal|"PRICE"
argument_list|,
literal|"1000.00"
argument_list|)
expr_stmt|;
name|row2
operator|.
name|put
argument_list|(
literal|"LAST_RECV_DT"
argument_list|,
literal|"20040601"
argument_list|)
expr_stmt|;
name|data
operator|.
name|add
argument_list|(
name|row2
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:marshal"
argument_list|,
name|data
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMarshalNoDefinition ()
specifier|public
name|void
name|testMarshalNoDefinition
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:marshal2"
argument_list|)
decl_stmt|;
comment|// by default we get on big message
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
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
argument_list|<>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|row
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"ITEM_DESC"
argument_list|,
literal|"SOME VALVE"
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"IN_STOCK"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"PRICE"
argument_list|,
literal|"5.00"
argument_list|)
expr_stmt|;
name|row
operator|.
name|put
argument_list|(
literal|"LAST_RECV_DT"
argument_list|,
literal|"20050101"
argument_list|)
expr_stmt|;
name|data
operator|.
name|add
argument_list|(
name|row
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
argument_list|<>
argument_list|()
decl_stmt|;
name|row2
operator|.
name|put
argument_list|(
literal|"ITEM_DESC"
argument_list|,
literal|"AN ENGINE"
argument_list|)
expr_stmt|;
name|row2
operator|.
name|put
argument_list|(
literal|"IN_STOCK"
argument_list|,
literal|"100"
argument_list|)
expr_stmt|;
name|row2
operator|.
name|put
argument_list|(
literal|"PRICE"
argument_list|,
literal|"1000.00"
argument_list|)
expr_stmt|;
name|row2
operator|.
name|put
argument_list|(
literal|"LAST_RECV_DT"
argument_list|,
literal|"20040601"
argument_list|)
expr_stmt|;
name|data
operator|.
name|add
argument_list|(
name|row2
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:marshal2"
argument_list|,
name|data
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
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
name|FlatpackDataFormat
name|df
init|=
operator|new
name|FlatpackDataFormat
argument_list|()
decl_stmt|;
name|df
operator|.
name|setDefinition
argument_list|(
literal|"INVENTORY-Delimited.pzmap.xml"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:unmarshal"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|df
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:unmarshal"
argument_list|)
expr_stmt|;
comment|// with the definition
name|from
argument_list|(
literal|"direct:marshal"
argument_list|)
operator|.
name|marshal
argument_list|(
name|df
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:marshal"
argument_list|)
expr_stmt|;
comment|// without the definition (will auto add column names from the recieved data)
name|FlatpackDataFormat
name|df2
init|=
operator|new
name|FlatpackDataFormat
argument_list|()
decl_stmt|;
name|from
argument_list|(
literal|"direct:marshal2"
argument_list|)
operator|.
name|marshal
argument_list|(
name|df2
argument_list|)
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:marshal2"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

