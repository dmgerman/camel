begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|CxfConsumerMessageTest
specifier|public
class|class
name|CxfConsumerMessageTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|TEST_MESSAGE
specifier|private
specifier|static
specifier|final
name|String
name|TEST_MESSAGE
init|=
literal|"Hello World!"
decl_stmt|;
DECL|field|ECHO_METHOD
specifier|private
specifier|static
specifier|final
name|String
name|ECHO_METHOD
init|=
literal|"ns1:echo xmlns:ns1=\"http://cxf.component.camel.apache.org/\""
decl_stmt|;
DECL|field|ECHO_RESPONSE
specifier|private
specifier|static
specifier|final
name|String
name|ECHO_RESPONSE
init|=
literal|"<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
operator|+
literal|"<soap:Body><ns1:echoResponse xmlns:ns1=\"http://cxf.component.camel.apache.org/\">"
operator|+
literal|"<return xmlns=\"http://cxf.component.camel.apache.org/\">echo Hello World!</return>"
operator|+
literal|"</ns1:echoResponse></soap:Body></soap:Envelope>"
decl_stmt|;
DECL|field|ECHO_BOOLEAN_RESPONSE
specifier|private
specifier|static
specifier|final
name|String
name|ECHO_BOOLEAN_RESPONSE
init|=
literal|"<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
operator|+
literal|"<soap:Body><ns1:echoBooleanResponse xmlns:ns1=\"http://cxf.component.camel.apache.org/\">"
operator|+
literal|"<return xmlns=\"http://cxf.component.camel.apache.org/\">true</return>"
operator|+
literal|"</ns1:echoBooleanResponse></soap:Body></soap:Envelope>"
decl_stmt|;
DECL|field|simpleEndpointAddress
specifier|protected
specifier|final
name|String
name|simpleEndpointAddress
init|=
literal|"http://localhost:"
operator|+
name|CXFTestSupport
operator|.
name|getPort1
argument_list|()
operator|+
literal|"/"
operator|+
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|+
literal|"/test"
decl_stmt|;
DECL|field|simpleEndpointURI
specifier|protected
specifier|final
name|String
name|simpleEndpointURI
init|=
literal|"cxf://"
operator|+
name|simpleEndpointAddress
operator|+
literal|"?serviceClass=org.apache.camel.component.cxf.HelloService"
decl_stmt|;
annotation|@
name|Override
DECL|method|isCreateCamelContextPerClass ()
specifier|public
name|boolean
name|isCreateCamelContextPerClass
parameter_list|()
block|{
return|return
literal|true
return|;
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
name|simpleEndpointURI
operator|+
literal|"&dataFormat=RAW"
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
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
comment|// check the content-length header is filtered
name|Object
name|value
init|=
name|in
operator|.
name|getHeader
argument_list|(
literal|"Content-Length"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"The Content-Length header should be removed"
argument_list|,
name|value
argument_list|)
expr_stmt|;
comment|// Get the request message
name|String
name|request
init|=
name|in
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Send the response message back
if|if
condition|(
name|request
operator|.
name|indexOf
argument_list|(
name|ECHO_METHOD
argument_list|)
operator|>
literal|0
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|ECHO_RESPONSE
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// echoBoolean call
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|ECHO_BOOLEAN_RESPONSE
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Test
DECL|method|testInvokingServiceFromClient ()
specifier|public
name|void
name|testInvokingServiceFromClient
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
name|simpleEndpointAddress
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
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
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
name|String
name|result
init|=
name|client
operator|.
name|echo
argument_list|(
name|TEST_MESSAGE
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"We should get the echo string result from router"
argument_list|,
name|result
argument_list|,
literal|"echo "
operator|+
name|TEST_MESSAGE
argument_list|)
expr_stmt|;
name|Boolean
name|bool
init|=
name|client
operator|.
name|echoBoolean
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"The result should not be null"
argument_list|,
name|bool
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"We should get the echo boolean result from router "
argument_list|,
name|bool
operator|.
name|toString
argument_list|()
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

