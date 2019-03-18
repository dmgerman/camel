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
name|BindingProvider
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
name|non_wrapper
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
name|non_wrapper
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
name|non_wrapper
operator|.
name|UnknownPersonFault
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
name|non_wrapper
operator|.
name|types
operator|.
name|GetPerson
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
name|non_wrapper
operator|.
name|types
operator|.
name|GetPersonResponse
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
name|spring
operator|.
name|CamelSpringTestSupport
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
DECL|class|CxfNonWrapperTest
specifier|public
class|class
name|CxfNonWrapperTest
extends|extends
name|CamelSpringTestSupport
block|{
DECL|field|port1
name|int
name|port1
init|=
name|CXFTestSupport
operator|.
name|getPort1
argument_list|()
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
literal|"org/apache/camel/component/cxf/nonWrapperProcessor.xml"
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
name|Test
DECL|method|testInvokingServiceFromCXFClient ()
specifier|public
name|void
name|testInvokingServiceFromCXFClient
parameter_list|()
throws|throws
name|Exception
block|{
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
literal|"person-non-wrapper.wsdl"
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
literal|"http://camel.apache.org/non-wrapper"
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
operator|(
operator|(
name|BindingProvider
operator|)
name|client
operator|)
operator|.
name|getRequestContext
argument_list|()
operator|.
name|put
argument_list|(
name|BindingProvider
operator|.
name|ENDPOINT_ADDRESS_PROPERTY
argument_list|,
literal|"http://localhost:"
operator|+
name|port1
operator|+
literal|"/CxfNonWrapperTest/PersonService/"
argument_list|)
expr_stmt|;
name|GetPerson
name|request
init|=
operator|new
name|GetPerson
argument_list|()
decl_stmt|;
name|request
operator|.
name|setPersonId
argument_list|(
literal|"hello"
argument_list|)
expr_stmt|;
name|GetPersonResponse
name|response
init|=
name|client
operator|.
name|getPerson
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"we should get the right answer from router"
argument_list|,
literal|"Bonjour"
argument_list|,
name|response
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|request
operator|.
name|setPersonId
argument_list|(
literal|""
argument_list|)
expr_stmt|;
try|try
block|{
name|client
operator|.
name|getPerson
argument_list|(
name|request
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
block|}
block|}
end_class

end_unit

