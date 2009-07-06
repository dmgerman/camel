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
name|net
operator|.
name|URL
import|;
end_import

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
name|namespace
operator|.
name|QName
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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Holder
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
name|WebServiceException
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
name|test
operator|.
name|junit4
operator|.
name|CamelSpringTestSupport
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
name|wsdl_first
operator|.
name|JaxwsTestHandler
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
name|wsdl_first
operator|.
name|Person
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
name|wsdl_first
operator|.
name|PersonImpl
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
name|wsdl_first
operator|.
name|PersonService
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
name|wsdl_first
operator|.
name|UnknownPersonFault
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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
name|ClassPathXmlApplicationContext
import|;
end_import

begin_class
DECL|class|CxfWsdlFirstTest
specifier|public
class|class
name|CxfWsdlFirstTest
extends|extends
name|CamelSpringTestSupport
block|{
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|createApplicationContext ()
specifier|protected
name|ClassPathXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/cxf/WsdlFirstBeans.xml"
argument_list|)
return|;
block|}
DECL|method|assertValidContext (CamelContext context)
specifier|protected
name|void
name|assertValidContext
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|assertNotNull
argument_list|(
literal|"No context found!"
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|BeforeClass
DECL|method|startService ()
specifier|public
specifier|static
name|void
name|startService
parameter_list|()
block|{
name|Object
name|implementor
init|=
operator|new
name|PersonImpl
argument_list|()
decl_stmt|;
name|String
name|address
init|=
literal|"http://localhost:9000/PersonService/"
decl_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
name|implementor
argument_list|)
expr_stmt|;
block|}
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
name|JaxwsTestHandler
name|fromHandler
init|=
name|getMandatoryBean
argument_list|(
name|JaxwsTestHandler
operator|.
name|class
argument_list|,
literal|"fromEndpointJaxwsHandler"
argument_list|)
decl_stmt|;
name|fromHandler
operator|.
name|reset
argument_list|()
expr_stmt|;
name|JaxwsTestHandler
name|toHandler
init|=
name|getMandatoryBean
argument_list|(
name|JaxwsTestHandler
operator|.
name|class
argument_list|,
literal|"toEndpointJaxwsHandler"
argument_list|)
decl_stmt|;
name|toHandler
operator|.
name|reset
argument_list|()
expr_stmt|;
name|URL
name|wsdlURL
init|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"person.wsdl"
argument_list|)
decl_stmt|;
name|PersonService
name|ss
init|=
operator|new
name|PersonService
argument_list|(
name|wsdlURL
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://camel.apache.org/wsdl-first"
argument_list|,
literal|"PersonService"
argument_list|)
argument_list|)
decl_stmt|;
name|Person
name|client
init|=
name|ss
operator|.
name|getSoap
argument_list|()
decl_stmt|;
name|Holder
argument_list|<
name|String
argument_list|>
name|personId
init|=
operator|new
name|Holder
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|personId
operator|.
name|value
operator|=
literal|"hello"
expr_stmt|;
name|Holder
argument_list|<
name|String
argument_list|>
name|ssn
init|=
operator|new
name|Holder
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|Holder
argument_list|<
name|String
argument_list|>
name|name
init|=
operator|new
name|Holder
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|client
operator|.
name|getPerson
argument_list|(
name|personId
argument_list|,
name|ssn
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"we should get the right answer from router"
argument_list|,
literal|"Bonjour"
argument_list|,
name|name
operator|.
name|value
argument_list|)
expr_stmt|;
name|personId
operator|.
name|value
operator|=
literal|""
expr_stmt|;
try|try
block|{
name|client
operator|.
name|getPerson
argument_list|(
name|personId
argument_list|,
name|ssn
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"We expect to get the UnknowPersonFault here"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnknownPersonFault
name|fault
parameter_list|)
block|{
comment|// We expect to get fault here
block|}
name|personId
operator|.
name|value
operator|=
literal|"Invoking getPerson with invalid length string, expecting exception...xxxxxxxxx"
expr_stmt|;
try|try
block|{
name|client
operator|.
name|getPerson
argument_list|(
name|personId
argument_list|,
name|ssn
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"We expect to get the WebSerivceException here"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WebServiceException
name|ex
parameter_list|)
block|{
comment|// Caught expected WebServiceException here
name|assertTrue
argument_list|(
literal|"Should get the xml vaildate error!"
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"MyStringType"
argument_list|)
operator|>
literal|0
argument_list|)
expr_stmt|;
block|}
name|verifyJaxwsHandlers
argument_list|(
name|fromHandler
argument_list|,
name|toHandler
argument_list|)
expr_stmt|;
block|}
DECL|method|verifyJaxwsHandlers (JaxwsTestHandler fromHandler, JaxwsTestHandler toHandler)
specifier|protected
name|void
name|verifyJaxwsHandlers
parameter_list|(
name|JaxwsTestHandler
name|fromHandler
parameter_list|,
name|JaxwsTestHandler
name|toHandler
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|fromHandler
operator|.
name|getFaultCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|fromHandler
operator|.
name|getMessageCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|7
argument_list|,
name|toHandler
operator|.
name|getGetHeadersCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|8
argument_list|,
name|toHandler
operator|.
name|getMessageCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|6
argument_list|,
name|toHandler
operator|.
name|getFaultCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|testInvokingServiceWithCamelProducer ()
specifier|public
name|void
name|testInvokingServiceWithCamelProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|Exchange
name|exchange
init|=
name|sendJaxWsMessageWithHolders
argument_list|(
literal|"hello"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"The request should be handled sucessfully "
argument_list|,
name|exchange
operator|.
name|isFailed
argument_list|()
argument_list|,
literal|false
argument_list|)
expr_stmt|;
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
name|List
name|result
init|=
name|out
operator|.
name|getBody
argument_list|(
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"The result list should not be empty"
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|Holder
argument_list|<
name|String
argument_list|>
name|name
init|=
operator|(
name|Holder
argument_list|<
name|String
argument_list|>
operator|)
name|result
operator|.
name|get
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"we should get the right answer from router"
argument_list|,
literal|"Bonjour"
argument_list|,
name|name
operator|.
name|value
argument_list|)
expr_stmt|;
name|exchange
operator|=
name|sendJaxWsMessageWithHolders
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"We should get a fault here"
argument_list|,
name|exchange
operator|.
name|isFailed
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|Throwable
name|ex
init|=
name|exchange
operator|.
name|getException
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"We should get the UnknowPersonFault here"
argument_list|,
name|ex
operator|instanceof
name|UnknownPersonFault
argument_list|)
expr_stmt|;
block|}
DECL|method|sendJaxWsMessageWithHolders (final String personIdString)
specifier|protected
name|Exchange
name|sendJaxWsMessageWithHolders
parameter_list|(
specifier|final
name|String
name|personIdString
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|template
operator|.
name|send
argument_list|(
literal|"direct:producer"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
name|params
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|Holder
argument_list|<
name|String
argument_list|>
name|personId
init|=
operator|new
name|Holder
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|personId
operator|.
name|value
operator|=
name|personIdString
expr_stmt|;
name|params
operator|.
name|add
argument_list|(
name|personId
argument_list|)
expr_stmt|;
name|Holder
argument_list|<
name|String
argument_list|>
name|ssn
init|=
operator|new
name|Holder
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|Holder
argument_list|<
name|String
argument_list|>
name|name
init|=
operator|new
name|Holder
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|params
operator|.
name|add
argument_list|(
name|ssn
argument_list|)
expr_stmt|;
name|params
operator|.
name|add
argument_list|(
name|name
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
literal|"GetPerson"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
decl_stmt|;
return|return
name|exchange
return|;
block|}
block|}
end_class

end_unit

