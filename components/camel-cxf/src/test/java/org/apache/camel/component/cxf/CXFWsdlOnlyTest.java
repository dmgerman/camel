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
name|BindingProvider
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
name|AfterClass
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
DECL|class|CXFWsdlOnlyTest
specifier|public
class|class
name|CXFWsdlOnlyTest
extends|extends
name|CamelSpringTestSupport
block|{
DECL|field|endpoint1
specifier|private
specifier|static
name|Endpoint
name|endpoint1
decl_stmt|;
DECL|field|endpoint2
specifier|private
specifier|static
name|Endpoint
name|endpoint2
decl_stmt|;
DECL|field|port1
specifier|private
specifier|static
name|int
name|port1
init|=
name|CXFTestSupport
operator|.
name|getPort1
argument_list|()
decl_stmt|;
DECL|field|port2
specifier|private
specifier|static
name|int
name|port2
init|=
name|CXFTestSupport
operator|.
name|getPort2
argument_list|()
decl_stmt|;
DECL|field|port3
specifier|private
specifier|static
name|int
name|port3
init|=
name|CXFTestSupport
operator|.
name|getPort3
argument_list|()
decl_stmt|;
DECL|field|port4
specifier|private
specifier|static
name|int
name|port4
init|=
name|CXFTestSupport
operator|.
name|getPort4
argument_list|()
decl_stmt|;
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
name|System
operator|.
name|setProperty
argument_list|(
literal|"CXFWsdlOnlyTest.port1"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|port1
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"CXFWsdlOnlyTest.port2"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|port2
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"CXFWsdlOnlyTest.port3"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|port3
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"CXFWsdlOnlyTest.port4"
argument_list|,
name|Integer
operator|.
name|toString
argument_list|(
name|port4
argument_list|)
argument_list|)
expr_stmt|;
comment|// When the Application is closed, the camel-cxf endpoint will be shutdown,
comment|// this will cause the issue of the new http server doesn't send the response back.
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
literal|"org/apache/camel/component/cxf/WsdlOnlyBeans.xml"
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
DECL|method|startServices ()
specifier|public
specifier|static
name|void
name|startServices
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
literal|"http://localhost:"
operator|+
name|port1
operator|+
literal|"/CXFWsdlOnlyTest/PersonService/"
decl_stmt|;
name|endpoint1
operator|=
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
name|implementor
argument_list|)
expr_stmt|;
name|address
operator|=
literal|"http://localhost:"
operator|+
name|port2
operator|+
literal|"/CXFWsdlOnlyTest/PersonService/"
expr_stmt|;
name|endpoint2
operator|=
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
name|AfterClass
DECL|method|stopServices ()
specifier|public
specifier|static
name|void
name|stopServices
parameter_list|()
block|{
if|if
condition|(
name|endpoint1
operator|!=
literal|null
condition|)
block|{
name|endpoint1
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|endpoint2
operator|!=
literal|null
condition|)
block|{
name|endpoint2
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testRoutesWithFault ()
specifier|public
name|void
name|testRoutesWithFault
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
name|port3
operator|+
literal|"/CXFWsdlOnlyTest/PersonService/"
argument_list|)
expr_stmt|;
name|Holder
argument_list|<
name|String
argument_list|>
name|personId
init|=
operator|new
name|Holder
argument_list|<>
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
argument_list|<>
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
argument_list|<>
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
name|ssn
operator|=
operator|new
name|Holder
argument_list|<>
argument_list|()
expr_stmt|;
name|name
operator|=
operator|new
name|Holder
argument_list|<>
argument_list|()
expr_stmt|;
name|Throwable
name|t
init|=
literal|null
decl_stmt|;
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
literal|"Expect exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnknownPersonFault
name|e
parameter_list|)
block|{
name|t
operator|=
name|e
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|t
operator|instanceof
name|UnknownPersonFault
argument_list|)
expr_stmt|;
name|Person
name|client2
init|=
name|ss
operator|.
name|getSoap2
argument_list|()
decl_stmt|;
operator|(
operator|(
name|BindingProvider
operator|)
name|client2
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
name|port4
operator|+
literal|"/CXFWsdlOnlyTest/PersonService/"
argument_list|)
expr_stmt|;
name|Holder
argument_list|<
name|String
argument_list|>
name|personId2
init|=
operator|new
name|Holder
argument_list|<>
argument_list|()
decl_stmt|;
name|personId2
operator|.
name|value
operator|=
literal|"hello"
expr_stmt|;
name|Holder
argument_list|<
name|String
argument_list|>
name|ssn2
init|=
operator|new
name|Holder
argument_list|<>
argument_list|()
decl_stmt|;
name|Holder
argument_list|<
name|String
argument_list|>
name|name2
init|=
operator|new
name|Holder
argument_list|<>
argument_list|()
decl_stmt|;
name|client2
operator|.
name|getPerson
argument_list|(
name|personId2
argument_list|,
name|ssn2
argument_list|,
name|name2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bonjour"
argument_list|,
name|name2
operator|.
name|value
argument_list|)
expr_stmt|;
name|personId2
operator|.
name|value
operator|=
literal|""
expr_stmt|;
name|ssn2
operator|=
operator|new
name|Holder
argument_list|<>
argument_list|()
expr_stmt|;
name|name2
operator|=
operator|new
name|Holder
argument_list|<>
argument_list|()
expr_stmt|;
try|try
block|{
name|client2
operator|.
name|getPerson
argument_list|(
name|personId2
argument_list|,
name|ssn2
argument_list|,
name|name2
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expect exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnknownPersonFault
name|e
parameter_list|)
block|{
name|t
operator|=
name|e
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|t
operator|instanceof
name|UnknownPersonFault
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

