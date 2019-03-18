begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy.csv
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
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
name|dataformat
operator|.
name|bindy
operator|.
name|model
operator|.
name|tab
operator|.
name|PurchaseOrder
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

begin_class
DECL|class|BindyTabSeparatorTest
specifier|public
class|class
name|BindyTabSeparatorTest
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
literal|"direct:unmarshal"
argument_list|,
literal|"123\tCamel in Action\t2\tPlease hurry\tJane Doe\tJohn Doe\n"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|PurchaseOrder
name|order
init|=
name|mock
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
name|PurchaseOrder
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|order
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Camel in Action"
argument_list|,
name|order
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|order
operator|.
name|getAmount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Please hurry"
argument_list|,
name|order
operator|.
name|getOrderText
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Jane Doe"
argument_list|,
name|order
operator|.
name|getSalesRef
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"John Doe"
argument_list|,
name|order
operator|.
name|getCustomerRef
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMarshal ()
specifier|public
name|void
name|testMarshal
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
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"123\tCamel in Action\t2\tPlease hurry\tJane Doe\tJohn Doe\n"
argument_list|)
expr_stmt|;
name|PurchaseOrder
name|order
init|=
operator|new
name|PurchaseOrder
argument_list|()
decl_stmt|;
name|order
operator|.
name|setId
argument_list|(
literal|123
argument_list|)
expr_stmt|;
name|order
operator|.
name|setName
argument_list|(
literal|"Camel in Action"
argument_list|)
expr_stmt|;
name|order
operator|.
name|setAmount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|order
operator|.
name|setOrderText
argument_list|(
literal|"Please hurry"
argument_list|)
expr_stmt|;
name|order
operator|.
name|setSalesRef
argument_list|(
literal|"Jane Doe"
argument_list|)
expr_stmt|;
name|order
operator|.
name|setCustomerRef
argument_list|(
literal|"John Doe"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:marshal"
argument_list|,
name|order
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Test
DECL|method|testUnmarshalEmptyTrailingNoneRequiredFields ()
specifier|public
name|void
name|testUnmarshalEmptyTrailingNoneRequiredFields
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
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:unmarshal"
argument_list|,
literal|"123\tCamel in Action\t2\t\t\n456\tCamel in Action\t1\t\t\t\n"
operator|+
literal|"456\tCamel in Action\t2\t\t\n456\tCamel in Action\t1\t\t\t\n"
argument_list|,
name|Exchange
operator|.
name|CONTENT_ENCODING
argument_list|,
literal|"iso8859-1"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|PurchaseOrder
argument_list|>
name|orders
init|=
operator|(
name|List
argument_list|<
name|PurchaseOrder
argument_list|>
operator|)
name|mock
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
argument_list|()
decl_stmt|;
name|PurchaseOrder
name|order
init|=
name|orders
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|order
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Camel in Action"
argument_list|,
name|order
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|order
operator|.
name|getAmount
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|order
operator|.
name|getOrderText
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|order
operator|.
name|getSalesRef
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|order
operator|.
name|getCustomerRef
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testMarshalEmptyTrailingNoneRequiredFields ()
specifier|public
name|void
name|testMarshalEmptyTrailingNoneRequiredFields
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
name|mock
operator|.
name|expectedBodiesReceived
argument_list|(
literal|"123\tCamel in Action\t2\t\t\t\n"
argument_list|)
expr_stmt|;
name|PurchaseOrder
name|order
init|=
operator|new
name|PurchaseOrder
argument_list|()
decl_stmt|;
name|order
operator|.
name|setId
argument_list|(
literal|123
argument_list|)
expr_stmt|;
name|order
operator|.
name|setName
argument_list|(
literal|"Camel in Action"
argument_list|)
expr_stmt|;
name|order
operator|.
name|setAmount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|order
operator|.
name|setOrderText
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|order
operator|.
name|setSalesRef
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|order
operator|.
name|setCustomerRef
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:marshal"
argument_list|,
name|order
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|BindyCsvDataFormat
name|bindy
init|=
operator|new
name|BindyCsvDataFormat
argument_list|(
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
operator|.
name|model
operator|.
name|tab
operator|.
name|PurchaseOrder
operator|.
name|class
argument_list|)
decl_stmt|;
name|from
argument_list|(
literal|"direct:marshal"
argument_list|)
operator|.
name|marshal
argument_list|(
name|bindy
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
name|from
argument_list|(
literal|"direct:unmarshal"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|bindy
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:unmarshal"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

