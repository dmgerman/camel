begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.jibx
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|jibx
package|;
end_package

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
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilderFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|ParserConfigurationException
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
name|jibx
operator|.
name|model
operator|.
name|PurchaseOrder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|InputSource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXException
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
name|CamelContext
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
name|Service
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
name|spring
operator|.
name|SpringCamelContext
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
name|AbstractXmlApplicationContext
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

begin_class
DECL|class|JibxDataFormatSpringDslTest
specifier|public
class|class
name|JibxDataFormatSpringDslTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testMarshall ()
specifier|public
name|void
name|testMarshall
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|ParserConfigurationException
throws|,
name|IOException
throws|,
name|SAXException
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|PurchaseOrder
name|purchaseOrder
init|=
operator|new
name|PurchaseOrder
argument_list|()
decl_stmt|;
name|String
name|name
init|=
literal|"foo"
decl_stmt|;
name|purchaseOrder
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|double
name|price
init|=
literal|49
decl_stmt|;
name|purchaseOrder
operator|.
name|setPrice
argument_list|(
name|price
argument_list|)
expr_stmt|;
name|double
name|amount
init|=
literal|3
decl_stmt|;
name|purchaseOrder
operator|.
name|setAmount
argument_list|(
name|amount
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:marshall"
argument_list|,
name|purchaseOrder
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|String
name|body
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
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|DocumentBuilder
name|builder
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
name|Element
name|root
init|=
name|builder
operator|.
name|parse
argument_list|(
operator|new
name|InputSource
argument_list|(
operator|new
name|StringReader
argument_list|(
name|body
argument_list|)
argument_list|)
argument_list|)
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|name
argument_list|,
name|root
operator|.
name|getAttribute
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|price
operator|+
literal|""
argument_list|,
name|root
operator|.
name|getAttribute
argument_list|(
literal|"price"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|amount
operator|+
literal|""
argument_list|,
name|root
operator|.
name|getAttribute
argument_list|(
literal|"amount"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testUnmarshall ()
specifier|public
name|void
name|testUnmarshall
parameter_list|()
throws|throws
name|InterruptedException
throws|,
name|ParserConfigurationException
throws|,
name|IOException
throws|,
name|SAXException
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|String
name|name
init|=
literal|"foo"
decl_stmt|;
name|double
name|price
init|=
literal|1
decl_stmt|;
name|double
name|amount
init|=
literal|2
decl_stmt|;
name|String
name|purchaseOrderXml
init|=
name|String
operator|.
name|format
argument_list|(
literal|"<order name='%s' price='%s' amount='%s' />"
argument_list|,
name|name
argument_list|,
name|price
operator|+
literal|""
argument_list|,
name|amount
operator|+
literal|""
argument_list|)
decl_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:unmarshall"
argument_list|,
name|purchaseOrderXml
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|PurchaseOrder
name|body
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
name|name
argument_list|,
name|body
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|price
argument_list|,
name|body
operator|.
name|getPrice
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|amount
argument_list|,
name|body
operator|.
name|getAmount
argument_list|()
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|setUseRouteBuilder
argument_list|(
literal|false
argument_list|)
expr_stmt|;
specifier|final
name|AbstractXmlApplicationContext
name|applicationContext
init|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/dataformat/jibx/SpringJibxConfigurationTest.xml"
argument_list|)
decl_stmt|;
name|setCamelContextService
argument_list|(
operator|new
name|Service
argument_list|()
block|{
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
name|applicationContext
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
name|applicationContext
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|SpringCamelContext
operator|.
name|springCamelContext
argument_list|(
name|applicationContext
argument_list|)
return|;
block|}
block|}
end_class

end_unit

