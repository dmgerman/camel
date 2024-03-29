begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
package|;
end_package

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
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
DECL|class|CxfPayloadProducerNamespaceOnEnvelopeTest
specifier|public
class|class
name|CxfPayloadProducerNamespaceOnEnvelopeTest
extends|extends
name|CamelTestSupport
block|{
comment|/*      * The response message is generated directly. The issue here is that the      * xsi and xs namespaces are defined on the SOAP envelope but are used      * within the payload. This can cause issues with some type conversions in      * PAYLOAD mode, as the Camel-CXF endpoint will return some kind of window      * within the StAX parsing (and the namespace definitions are outside).      *       * If some CXF implementation bean is used as the service the namespaces      * will be defined within the payload (and everything works fine).      */
DECL|field|RESPONSE_MESSAGE
specifier|protected
specifier|static
specifier|final
name|String
name|RESPONSE_MESSAGE
init|=
literal|"<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"><soap:Body>"
operator|+
literal|"<ns2:getTokenResponse xmlns:ns2=\"http://camel.apache.org/cxf/namespace\"><return xsi:type=\"xs:string\">Return Value</return></ns2:getTokenResponse></soap:Body></soap:Envelope>"
decl_stmt|;
DECL|field|REQUEST_PAYLOAD
specifier|protected
specifier|static
specifier|final
name|String
name|REQUEST_PAYLOAD
init|=
literal|"<ns2:getToken xmlns:ns2=\"http://camel.apache.org/cxf/namespace\"/>"
decl_stmt|;
DECL|field|applicationContext
specifier|private
name|AbstractXmlApplicationContext
name|applicationContext
decl_stmt|;
comment|// Don't remove this, it initializes the CXFTestSupport class
static|static
block|{
name|CXFTestSupport
operator|.
name|getPort1
argument_list|()
expr_stmt|;
comment|// Works without streaming...
comment|// System.setProperty("org.apache.camel.component.cxf.streaming", "false");
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|SpringCamelContext
operator|.
name|springCamelContext
argument_list|(
name|applicationContext
argument_list|,
literal|true
argument_list|)
return|;
block|}
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
name|applicationContext
operator|=
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/cxf/GetTokenBeans.xml"
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Should have created a valid spring context"
argument_list|,
name|applicationContext
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
name|IOHelper
operator|.
name|close
argument_list|(
name|applicationContext
argument_list|)
expr_stmt|;
name|super
operator|.
name|tearDown
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
block|{
name|from
argument_list|(
literal|"direct:router"
argument_list|)
comment|//
comment|// call an external Web service in payload mode
operator|.
name|to
argument_list|(
literal|"cxf:bean:serviceEndpoint?dataFormat=PAYLOAD"
argument_list|)
comment|// Convert the CxfPayload to a String to trigger the
comment|// issue
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|)
comment|// Parse to DOM to make sure it's still valid XML
operator|.
name|convertBodyTo
argument_list|(
name|Document
operator|.
name|class
argument_list|)
comment|// Convert back to String to make testing the result
comment|// easier
operator|.
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
comment|// This route just returns the test message
name|from
argument_list|(
literal|"cxf:bean:serviceEndpoint?dataFormat=RAW"
argument_list|)
operator|.
name|setBody
argument_list|()
operator|.
name|constant
argument_list|(
name|RESPONSE_MESSAGE
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|testInvokeRouter ()
specifier|public
name|void
name|testInvokeRouter
parameter_list|()
block|{
name|Object
name|returnValue
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"direct:router"
argument_list|,
name|REQUEST_PAYLOAD
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|returnValue
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|returnValue
operator|instanceof
name|String
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|(
operator|(
name|String
operator|)
name|returnValue
operator|)
operator|.
name|contains
argument_list|(
literal|"Return Value"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|(
operator|(
name|String
operator|)
name|returnValue
operator|)
operator|.
name|contains
argument_list|(
literal|"http://www.w3.org/2001/XMLSchema-instance"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

