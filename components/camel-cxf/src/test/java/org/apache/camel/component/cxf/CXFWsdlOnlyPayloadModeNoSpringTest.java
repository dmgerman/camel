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
name|apache
operator|.
name|cxf
operator|.
name|endpoint
operator|.
name|Client
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
name|ClientProxy
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
name|interceptor
operator|.
name|LoggingInInterceptor
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
name|interceptor
operator|.
name|LoggingOutInterceptor
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
DECL|class|CXFWsdlOnlyPayloadModeNoSpringTest
specifier|public
class|class
name|CXFWsdlOnlyPayloadModeNoSpringTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|SERVICE_NAME_PROP
specifier|protected
specifier|static
specifier|final
name|String
name|SERVICE_NAME_PROP
init|=
literal|"serviceName="
decl_stmt|;
DECL|field|PORT_NAME_PROP
specifier|protected
specifier|static
specifier|final
name|String
name|PORT_NAME_PROP
init|=
literal|"portName={http://camel.apache.org/wsdl-first}soap"
decl_stmt|;
DECL|field|WSDL_URL_PROP
specifier|protected
specifier|static
specifier|final
name|String
name|WSDL_URL_PROP
init|=
literal|"wsdlURL=classpath:person.wsdl"
decl_stmt|;
DECL|field|endpoint
specifier|protected
name|Endpoint
name|endpoint
decl_stmt|;
annotation|@
name|Before
DECL|method|startService ()
specifier|public
name|void
name|startService
parameter_list|()
block|{
name|endpoint
operator|=
name|Endpoint
operator|.
name|publish
argument_list|(
literal|"http://localhost:8093/PersonService"
argument_list|,
operator|new
name|PersonImpl
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|stopService ()
specifier|public
name|void
name|stopService
parameter_list|()
block|{
if|if
condition|(
name|endpoint
operator|!=
literal|null
condition|)
block|{
name|endpoint
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
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
literal|"cxf://http://localhost:8092/PersonService?"
operator|+
name|PORT_NAME_PROP
operator|+
literal|"&"
operator|+
name|SERVICE_NAME_PROP
operator|+
name|getServiceName
argument_list|()
operator|+
literal|"&"
operator|+
name|WSDL_URL_PROP
operator|+
literal|"&dataFormat="
operator|+
name|getDataFormat
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"cxf://http://localhost:8093/PersonService?"
operator|+
name|PORT_NAME_PROP
operator|+
literal|"&"
operator|+
name|SERVICE_NAME_PROP
operator|+
name|getServiceName
argument_list|()
operator|+
literal|"&"
operator|+
name|WSDL_URL_PROP
operator|+
literal|"&dataFormat="
operator|+
name|getDataFormat
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|method|getDataFormat ()
specifier|protected
name|String
name|getDataFormat
parameter_list|()
block|{
return|return
literal|"PAYLOAD"
return|;
block|}
annotation|@
name|Test
DECL|method|testRoutes ()
specifier|public
name|void
name|testRoutes
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
name|QName
operator|.
name|valueOf
argument_list|(
name|getServiceName
argument_list|()
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
name|Client
name|c
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|client
argument_list|)
decl_stmt|;
name|c
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|c
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingOutInterceptor
argument_list|()
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
literal|"Bonjour"
argument_list|,
name|name
operator|.
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testApplicationFault ()
specifier|public
name|void
name|testApplicationFault
parameter_list|()
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
name|QName
operator|.
name|valueOf
argument_list|(
name|getServiceName
argument_list|()
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
name|Client
name|c
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|client
argument_list|)
decl_stmt|;
name|c
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|c
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingOutInterceptor
argument_list|()
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
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|personId
operator|.
name|value
operator|=
literal|""
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
literal|"expect UnknownPersonFault"
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
name|assertNotNull
argument_list|(
name|t
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|t
operator|instanceof
name|UnknownPersonFault
argument_list|)
expr_stmt|;
block|}
DECL|method|getServiceName ()
specifier|protected
name|String
name|getServiceName
parameter_list|()
block|{
return|return
literal|"{http://camel.apache.org/wsdl-first}PersonService"
return|;
block|}
block|}
end_class

end_unit

