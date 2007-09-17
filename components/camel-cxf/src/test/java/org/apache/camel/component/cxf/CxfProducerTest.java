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
name|ArrayList
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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Endpoint
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|CamelTemplate
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
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
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
name|bus
operator|.
name|CXFBusFactory
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
name|ServerFactoryBean
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
name|jaxws
operator|.
name|JaxWsServerFactoryBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|GreeterImpl
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|CxfProducerTest
specifier|public
class|class
name|CxfProducerTest
extends|extends
name|TestCase
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|CxfProducerTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|camelContext
specifier|protected
name|CamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
DECL|field|template
specifier|protected
name|CamelTemplate
argument_list|<
name|CxfExchange
argument_list|>
name|template
init|=
operator|new
name|CamelTemplate
argument_list|<
name|CxfExchange
argument_list|>
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
DECL|field|SIMPLE_SERVER_ADDRESS
specifier|private
specifier|final
specifier|static
name|String
name|SIMPLE_SERVER_ADDRESS
init|=
literal|"http://localhost:28080/test"
decl_stmt|;
DECL|field|JAXWS_SERVER_ADDRESS
specifier|private
specifier|final
specifier|static
name|String
name|JAXWS_SERVER_ADDRESS
init|=
literal|"http://localhost:28081/test"
decl_stmt|;
DECL|field|ECHO_OPERATION
specifier|private
specifier|final
specifier|static
name|String
name|ECHO_OPERATION
init|=
literal|"echo"
decl_stmt|;
DECL|field|GREET_ME_OPERATION
specifier|private
specifier|final
specifier|static
name|String
name|GREET_ME_OPERATION
init|=
literal|"greetMe"
decl_stmt|;
DECL|field|TEST_MESSAGE
specifier|private
specifier|final
specifier|static
name|String
name|TEST_MESSAGE
init|=
literal|"Hello World!"
decl_stmt|;
DECL|field|simpleServer
specifier|private
name|ServerImpl
name|simpleServer
decl_stmt|;
DECL|field|jaxwsEndpoint
specifier|private
name|Endpoint
name|jaxwsEndpoint
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
comment|// start a simple front service
name|ServerFactoryBean
name|svrBean
init|=
operator|new
name|ServerFactoryBean
argument_list|()
decl_stmt|;
name|svrBean
operator|.
name|setAddress
argument_list|(
name|SIMPLE_SERVER_ADDRESS
argument_list|)
expr_stmt|;
name|svrBean
operator|.
name|setServiceClass
argument_list|(
name|HelloService
operator|.
name|class
argument_list|)
expr_stmt|;
name|svrBean
operator|.
name|setServiceBean
argument_list|(
operator|new
name|HelloServiceImpl
argument_list|()
argument_list|)
expr_stmt|;
name|svrBean
operator|.
name|setBus
argument_list|(
name|CXFBusFactory
operator|.
name|getDefaultBus
argument_list|()
argument_list|)
expr_stmt|;
name|simpleServer
operator|=
operator|(
name|ServerImpl
operator|)
name|svrBean
operator|.
name|create
argument_list|()
expr_stmt|;
name|simpleServer
operator|.
name|start
argument_list|()
expr_stmt|;
name|GreeterImpl
name|greeterImpl
init|=
operator|new
name|GreeterImpl
argument_list|()
decl_stmt|;
name|jaxwsEndpoint
operator|=
name|Endpoint
operator|.
name|publish
argument_list|(
name|JAXWS_SERVER_ADDRESS
argument_list|,
name|greeterImpl
argument_list|)
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
if|if
condition|(
name|simpleServer
operator|!=
literal|null
condition|)
block|{
name|simpleServer
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|jaxwsEndpoint
operator|!=
literal|null
condition|)
block|{
name|jaxwsEndpoint
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|testInvokingSimpleServerWithParams ()
specifier|public
name|void
name|testInvokingSimpleServerWithParams
parameter_list|()
throws|throws
name|Exception
block|{
name|CxfExchange
name|exchange
init|=
operator|(
name|CxfExchange
operator|)
name|template
operator|.
name|send
argument_list|(
name|getSimpleEndpointUri
argument_list|()
argument_list|,
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
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|params
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|params
operator|.
name|add
argument_list|(
name|TEST_MESSAGE
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|params
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CxfConstants
operator|.
name|OPERATION_NAME
argument_list|,
name|ECHO_OPERATION
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|Object
index|[]
name|output
init|=
operator|(
name|Object
index|[]
operator|)
name|out
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Received output text: "
operator|+
name|output
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"reply body on Camel"
argument_list|,
name|TEST_MESSAGE
argument_list|,
name|output
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
DECL|method|testInvokingJawsServerWithParams ()
specifier|public
name|void
name|testInvokingJawsServerWithParams
parameter_list|()
throws|throws
name|Exception
block|{
name|CxfExchange
name|exchange
init|=
operator|(
name|CxfExchange
operator|)
name|template
operator|.
name|send
argument_list|(
name|getJaxwsEndpointUri
argument_list|()
argument_list|,
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
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|params
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|params
operator|.
name|add
argument_list|(
name|TEST_MESSAGE
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|params
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CxfConstants
operator|.
name|OPERATION_NAME
argument_list|,
name|GREET_ME_OPERATION
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|Object
index|[]
name|output
init|=
operator|(
name|Object
index|[]
operator|)
name|out
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Received output text: "
operator|+
name|output
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"reply body on Camel"
argument_list|,
literal|"Hello "
operator|+
name|TEST_MESSAGE
argument_list|,
name|output
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
DECL|method|getSimpleEndpointUri ()
specifier|private
name|String
name|getSimpleEndpointUri
parameter_list|()
block|{
return|return
literal|"cxf://"
operator|+
name|SIMPLE_SERVER_ADDRESS
operator|+
literal|"?serviceClass=org.apache.camel.component.cxf.HelloService"
return|;
block|}
DECL|method|getJaxwsEndpointUri ()
specifier|private
name|String
name|getJaxwsEndpointUri
parameter_list|()
block|{
return|return
literal|"cxf://"
operator|+
name|JAXWS_SERVER_ADDRESS
operator|+
literal|"?serviceClass=org.apache.hello_world_soap_http.Greeter"
return|;
block|}
block|}
end_class

end_unit

