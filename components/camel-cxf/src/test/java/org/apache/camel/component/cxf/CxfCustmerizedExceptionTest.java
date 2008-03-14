begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|w3c
operator|.
name|dom
operator|.
name|Text
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
name|CamelException
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
name|ContextTestSupport
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
name|Processor
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
name|impl
operator|.
name|DefaultCamelContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|Bus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|BusFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|binding
operator|.
name|soap
operator|.
name|SoapFault
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|endpoint
operator|.
name|ServerImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|frontend
operator|.
name|ClientFactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|frontend
operator|.
name|ClientProxyFactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|frontend
operator|.
name|ServerFactoryBean
import|;
end_import

begin_class
DECL|class|CxfCustmerizedExceptionTest
specifier|public
class|class
name|CxfCustmerizedExceptionTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|ROUTER_ADDRESS
specifier|protected
specifier|static
specifier|final
name|String
name|ROUTER_ADDRESS
init|=
literal|"http://localhost:9002/router"
decl_stmt|;
DECL|field|SERVICE_CLASS
specifier|protected
specifier|static
specifier|final
name|String
name|SERVICE_CLASS
init|=
literal|"serviceClass=org.apache.camel.component.cxf.HelloService"
decl_stmt|;
DECL|field|ROUTER_ENDPOINT_URI
specifier|protected
specifier|static
name|String
name|ROUTER_ENDPOINT_URI
init|=
literal|"cxf://"
operator|+
name|ROUTER_ADDRESS
operator|+
literal|"?"
operator|+
name|SERVICE_CLASS
decl_stmt|;
DECL|field|EXCEPTION_MESSAGE
specifier|private
specifier|static
specifier|final
name|String
name|EXCEPTION_MESSAGE
init|=
literal|"This is an exception test message"
decl_stmt|;
DECL|field|DETAIL_TEXT
specifier|private
specifier|static
specifier|final
name|String
name|DETAIL_TEXT
init|=
literal|"This is a detail text node"
decl_stmt|;
DECL|field|bus
specifier|private
name|Bus
name|bus
decl_stmt|;
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|bus
operator|=
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|tearDown ()
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
comment|//TODO need to shutdown the server
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
comment|//bus.shutdown(true);
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
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
name|ROUTER_ENDPOINT_URI
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Message
name|message
init|=
name|exchange
operator|.
name|getFault
argument_list|()
decl_stmt|;
name|SoapFault
name|fault
init|=
operator|new
name|SoapFault
argument_list|(
name|EXCEPTION_MESSAGE
argument_list|,
name|SoapFault
operator|.
name|FAULT_CODE_CLIENT
argument_list|)
decl_stmt|;
name|Element
name|detail
init|=
name|fault
operator|.
name|getOrCreateDetail
argument_list|()
decl_stmt|;
name|Document
name|doc
init|=
name|detail
operator|.
name|getOwnerDocument
argument_list|()
decl_stmt|;
name|Text
name|tn
init|=
name|doc
operator|.
name|createTextNode
argument_list|(
name|DETAIL_TEXT
argument_list|)
decl_stmt|;
name|detail
operator|.
name|appendChild
argument_list|(
name|tn
argument_list|)
expr_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|fault
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|DefaultCamelContext
argument_list|()
return|;
block|}
DECL|method|testInvokingServiceFromCXFClient ()
specifier|public
name|void
name|testInvokingServiceFromCXFClient
parameter_list|()
throws|throws
name|Exception
block|{
name|ClientProxyFactoryBean
name|proxyFactory
init|=
operator|new
name|ClientProxyFactoryBean
argument_list|()
decl_stmt|;
name|ClientFactoryBean
name|clientBean
init|=
name|proxyFactory
operator|.
name|getClientFactoryBean
argument_list|()
decl_stmt|;
name|clientBean
operator|.
name|setAddress
argument_list|(
name|ROUTER_ADDRESS
argument_list|)
expr_stmt|;
name|clientBean
operator|.
name|setServiceClass
argument_list|(
name|HelloService
operator|.
name|class
argument_list|)
expr_stmt|;
name|clientBean
operator|.
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|HelloService
name|client
init|=
operator|(
name|HelloService
operator|)
name|proxyFactory
operator|.
name|create
argument_list|()
decl_stmt|;
try|try
block|{
name|client
operator|.
name|echo
argument_list|(
literal|"hello world"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Except to get an exception here"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Except to get right exception message"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|EXCEPTION_MESSAGE
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Except to get right exception message"
argument_list|,
name|EXCEPTION_MESSAGE
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Exception is not instance of SoapFault"
argument_list|,
name|e
operator|instanceof
name|SoapFault
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Except to get right detail message"
argument_list|,
name|DETAIL_TEXT
argument_list|,
operator|(
operator|(
name|SoapFault
operator|)
name|e
operator|)
operator|.
name|getDetail
argument_list|()
operator|.
name|getTextContent
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Except to get right fault-code"
argument_list|,
name|SoapFault
operator|.
name|FAULT_CODE_CLIENT
argument_list|,
operator|(
operator|(
name|SoapFault
operator|)
name|e
operator|)
operator|.
name|getFaultCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

