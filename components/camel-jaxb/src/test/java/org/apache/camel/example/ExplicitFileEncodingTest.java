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
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|Unmarshaller
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
DECL|class|ExplicitFileEncodingTest
specifier|public
class|class
name|ExplicitFileEncodingTest
extends|extends
name|CamelTestSupport
block|{
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
literal|"target/charset"
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
DECL|method|testISOFileEncoding ()
specifier|public
name|void
name|testISOFileEncoding
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
comment|//Data containing characters ÃÃÃÃ¦Ã¸Ã¥ that differ in utf-8 and iso
name|String
name|name
init|=
literal|"\u00c6\u00d8\u00C5\u00e6\u00f8\u00e5"
decl_stmt|;
name|order
operator|.
name|setName
argument_list|(
name|name
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
literal|"mock:file"
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedFileExists
argument_list|(
literal|"target/charset/output.txt"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|order
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|JAXBContext
name|jaxbContext
init|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
literal|"org.apache.camel.example"
argument_list|)
decl_stmt|;
name|Unmarshaller
name|unmarshaller
init|=
name|jaxbContext
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
name|PurchaseOrder
name|obj
init|=
operator|(
name|PurchaseOrder
operator|)
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
operator|new
name|File
argument_list|(
literal|"target/charset/output.txt"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|obj
operator|.
name|getName
argument_list|()
argument_list|,
name|name
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
name|jaxb
init|=
operator|new
name|JaxbDataFormat
argument_list|(
literal|"org.apache.camel.example"
argument_list|)
decl_stmt|;
name|jaxb
operator|.
name|setEncoding
argument_list|(
literal|"iso-8859-1"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|marshal
argument_list|(
name|jaxb
argument_list|)
operator|.
name|to
argument_list|(
literal|"file:target/charset/?fileName=output.txt&charset=iso-8859-1"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

