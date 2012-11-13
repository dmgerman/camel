begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.jaxb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|jaxb
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|example
operator|.
name|Address
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
DECL|class|JaxbDataFormatPartClassTest
specifier|public
class|class
name|JaxbDataFormatPartClassTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:marshall"
argument_list|)
DECL|field|mockMarshall
specifier|private
name|MockEndpoint
name|mockMarshall
decl_stmt|;
annotation|@
name|Test
DECL|method|testMarshallMultipleNamespaces ()
specifier|public
name|void
name|testMarshallMultipleNamespaces
parameter_list|()
throws|throws
name|Exception
block|{
name|mockMarshall
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Address
name|address
init|=
operator|new
name|Address
argument_list|()
decl_stmt|;
name|address
operator|.
name|setStreet
argument_list|(
literal|"Main Street"
argument_list|)
expr_stmt|;
name|address
operator|.
name|setStreetNumber
argument_list|(
literal|"3a"
argument_list|)
expr_stmt|;
name|address
operator|.
name|setZip
argument_list|(
literal|"65843"
argument_list|)
expr_stmt|;
name|address
operator|.
name|setCity
argument_list|(
literal|"Sulzbach"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:marshall"
argument_list|,
name|address
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|String
name|payload
init|=
name|mockMarshall
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
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
name|payload
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|payload
operator|.
name|startsWith
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|payload
operator|.
name|contains
argument_list|(
literal|"<address:address"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|payload
operator|.
name|contains
argument_list|(
literal|"<address:street>Main Street</address:street>"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|payload
operator|.
name|contains
argument_list|(
literal|"<address:streetNumber>3a</address:streetNumber>"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|payload
operator|.
name|contains
argument_list|(
literal|"<address:zip>65843</address:zip>"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|payload
operator|.
name|contains
argument_list|(
literal|"<address:city>Sulzbach</address:city>"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|payload
operator|.
name|contains
argument_list|(
literal|"</address:address>"
argument_list|)
argument_list|)
expr_stmt|;
comment|// the namespaces
name|assertTrue
argument_list|(
name|payload
operator|.
name|contains
argument_list|(
literal|"xmlns:address=\"http://www.camel.apache.org/jaxb/example/address/1\""
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|payload
operator|.
name|contains
argument_list|(
literal|"xmlns:order=\"http://www.camel.apache.org/jaxb/example/order/1\""
argument_list|)
argument_list|)
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
name|JaxbDataFormat
name|jaxbDataFormat
init|=
operator|new
name|JaxbDataFormat
argument_list|()
decl_stmt|;
name|jaxbDataFormat
operator|.
name|setContextPath
argument_list|(
name|Address
operator|.
name|class
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|jaxbDataFormat
operator|.
name|setPartClass
argument_list|(
literal|"org.apache.camel.example.Address"
argument_list|)
expr_stmt|;
name|jaxbDataFormat
operator|.
name|setPartNamespace
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://www.camel.apache.org/jaxb/example/address/1"
argument_list|,
literal|"address"
argument_list|)
argument_list|)
expr_stmt|;
name|jaxbDataFormat
operator|.
name|setPrettyPrint
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:marshall"
argument_list|)
operator|.
name|marshal
argument_list|(
name|jaxbDataFormat
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:marshall"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

