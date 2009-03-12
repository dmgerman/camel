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
name|Holder
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
DECL|class|CxfWsdlFirstPayloadModeTest
specifier|public
class|class
name|CxfWsdlFirstPayloadModeTest
extends|extends
name|CxfWsdlFirstTest
block|{
annotation|@
name|Override
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
literal|"org/apache/camel/component/cxf/WsdlFirstBeansPayloadMode.xml"
argument_list|)
return|;
block|}
annotation|@
name|Override
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
name|Throwable
name|t
init|=
literal|null
decl_stmt|;
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
name|t
operator|=
name|fault
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|t
operator|instanceof
name|UnknownPersonFault
argument_list|)
expr_stmt|;
comment|// Note: Since unmarshal phase has been removed in PAYLOAD mode,
comment|// it is not able to validate against the schema.
name|personId
operator|.
name|value
operator|=
literal|"Invoking getPerson with invalid length string, expecting exception...xxxxxxxxx"
expr_stmt|;
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
name|verifyJaxwsHandlers
argument_list|(
name|fromHandler
argument_list|,
name|toHandler
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|testInvokingServiceWithCamelProducer ()
specifier|public
name|void
name|testInvokingServiceWithCamelProducer
parameter_list|()
throws|throws
name|Exception
block|{
comment|// this test does not apply to PAYLOAD mode
block|}
annotation|@
name|Override
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
literal|1
argument_list|,
name|fromHandler
operator|.
name|getFaultCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|fromHandler
operator|.
name|getMessageCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|8
argument_list|,
name|toHandler
operator|.
name|getGetHeadersCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|10
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
block|}
end_class

end_unit

