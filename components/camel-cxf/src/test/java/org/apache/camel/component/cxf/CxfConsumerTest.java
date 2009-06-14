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

begin_class
DECL|class|CxfConsumerTest
specifier|public
class|class
name|CxfConsumerTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|SIMPLE_ENDPOINT_ADDRESS
specifier|protected
specifier|static
specifier|final
name|String
name|SIMPLE_ENDPOINT_ADDRESS
init|=
literal|"http://localhost:28080/test"
decl_stmt|;
DECL|field|SIMPLE_ENDPOINT_URI
specifier|protected
specifier|static
specifier|final
name|String
name|SIMPLE_ENDPOINT_URI
init|=
literal|"cxf://"
operator|+
name|SIMPLE_ENDPOINT_ADDRESS
operator|+
literal|"?serviceClass=org.apache.camel.component.cxf.HelloService"
decl_stmt|;
DECL|field|ECHO_OPERATION
specifier|private
specifier|static
specifier|final
name|String
name|ECHO_OPERATION
init|=
literal|"echo"
decl_stmt|;
DECL|field|ECHO_BOOLEAN_OPERATION
specifier|private
specifier|static
specifier|final
name|String
name|ECHO_BOOLEAN_OPERATION
init|=
literal|"echoBoolean"
decl_stmt|;
DECL|field|TEST_MESSAGE
specifier|private
specifier|static
specifier|final
name|String
name|TEST_MESSAGE
init|=
literal|"Hello World!"
decl_stmt|;
comment|// START SNIPPET: example
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
name|SIMPLE_ENDPOINT_URI
argument_list|)
operator|.
name|choice
argument_list|()
operator|.
name|when
argument_list|(
name|header
argument_list|(
name|CxfConstants
operator|.
name|OPERATION_NAME
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ECHO_OPERATION
argument_list|)
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
name|assertEquals
argument_list|(
name|DataFormat
operator|.
name|POJO
argument_list|,
name|exchange
operator|.
name|getProperty
argument_list|(
name|CxfConstants
operator|.
name|DATA_FORMAT_PROPERTY
argument_list|,
name|DataFormat
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
comment|// Get the parameter list
name|List
argument_list|<
name|?
argument_list|>
name|parameter
init|=
name|in
operator|.
name|getBody
argument_list|(
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Get the operation name
name|String
name|operation
init|=
operator|(
name|String
operator|)
name|in
operator|.
name|getHeader
argument_list|(
name|CxfConstants
operator|.
name|OPERATION_NAME
argument_list|)
decl_stmt|;
name|Object
name|result
init|=
name|operation
operator|+
literal|" "
operator|+
operator|(
name|String
operator|)
name|parameter
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// Put the result back
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
operator|.
name|when
argument_list|(
name|header
argument_list|(
name|CxfConstants
operator|.
name|OPERATION_NAME
argument_list|)
operator|.
name|isEqualTo
argument_list|(
name|ECHO_BOOLEAN_OPERATION
argument_list|)
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
comment|// Get the parameter list
name|List
argument_list|<
name|?
argument_list|>
name|parameter
init|=
name|in
operator|.
name|getBody
argument_list|(
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// Put the result back
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
operator|(
name|Boolean
operator|)
name|parameter
operator|.
name|get
argument_list|(
literal|0
argument_list|)
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
comment|// END SNIPPET: example
annotation|@
name|Test
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
name|SIMPLE_ENDPOINT_ADDRESS
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

