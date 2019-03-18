begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
package|;
end_package

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
name|jaxb
operator|.
name|JaxbDataFormat
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
name|spi
operator|.
name|DataFormat
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
DECL|class|RouteWithErrorHandlerTest
specifier|public
class|class
name|RouteWithErrorHandlerTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testOk ()
specifier|public
name|void
name|testOk
parameter_list|()
throws|throws
name|Exception
block|{
name|PurchaseOrder
name|order
init|=
operator|new
name|PurchaseOrder
argument_list|()
decl_stmt|;
name|order
operator|.
name|setName
argument_list|(
literal|"Wine"
argument_list|)
expr_stmt|;
name|order
operator|.
name|setAmount
argument_list|(
literal|123.45
argument_list|)
expr_stmt|;
name|order
operator|.
name|setPrice
argument_list|(
literal|2.22
argument_list|)
expr_stmt|;
name|MockEndpoint
name|result
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:wine"
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedBodiesReceived
argument_list|(
name|order
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<purchaseOrder name='Wine' amount='123.45' price='2.22'/>"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnmarshalError ()
specifier|public
name|void
name|testUnmarshalError
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|error
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:error"
argument_list|)
decl_stmt|;
name|error
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|error
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|body
argument_list|(
name|String
operator|.
name|class
argument_list|)
operator|.
name|contains
argument_list|(
literal|"<foo"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:invalid"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<foo/>"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNotWine ()
specifier|public
name|void
name|testNotWine
parameter_list|()
throws|throws
name|Exception
block|{
name|PurchaseOrder
name|order
init|=
operator|new
name|PurchaseOrder
argument_list|()
decl_stmt|;
name|order
operator|.
name|setName
argument_list|(
literal|"Beer"
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
name|setPrice
argument_list|(
literal|1.99
argument_list|)
expr_stmt|;
name|MockEndpoint
name|error
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:invalid"
argument_list|)
decl_stmt|;
name|error
operator|.
name|expectedBodiesReceived
argument_list|(
name|order
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"<purchaseOrder name='Beer' amount='2.0' price='1.99'/>"
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
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|onException
argument_list|(
name|InvalidOrderException
operator|.
name|class
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|0
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:invalid"
argument_list|)
expr_stmt|;
name|DataFormat
name|jaxb
init|=
operator|new
name|JaxbDataFormat
argument_list|(
literal|"org.apache.camel.example"
argument_list|)
decl_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|unmarshal
argument_list|(
name|jaxb
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|()
operator|.
name|method
argument_list|(
name|RouteWithErrorHandlerTest
operator|.
name|class
argument_list|,
literal|"isWine"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:wine"
argument_list|)
operator|.
name|otherwise
argument_list|()
operator|.
name|throwException
argument_list|(
operator|new
name|InvalidOrderException
argument_list|(
literal|"We only like wine"
argument_list|)
argument_list|)
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|isWine (PurchaseOrder order)
specifier|public
specifier|static
name|boolean
name|isWine
parameter_list|(
name|PurchaseOrder
name|order
parameter_list|)
block|{
return|return
literal|"Wine"
operator|.
name|equalsIgnoreCase
argument_list|(
name|order
operator|.
name|getName
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

