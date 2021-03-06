begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.cxf.httptojms
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|cxf
operator|.
name|httptojms
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
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
name|hello_world_soap_http
operator|.
name|Greeter
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
name|PingMeFault
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
name|types
operator|.
name|FaultDetail
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
DECL|class|CxfHttpJmsClientServerTest
specifier|public
class|class
name|CxfHttpJmsClientServerTest
extends|extends
name|CamelSpringTestSupport
block|{
DECL|field|ROUTER_ADDRESS
specifier|private
specifier|static
specifier|final
name|String
name|ROUTER_ADDRESS
init|=
literal|"http://localhost:{{routerPort}}/SoapContext/SoapPort"
decl_stmt|;
annotation|@
name|Test
DECL|method|testClientInvocation ()
specifier|public
name|void
name|testClientInvocation
parameter_list|()
throws|throws
name|MalformedURLException
block|{
name|String
name|address
init|=
name|ROUTER_ADDRESS
operator|.
name|replace
argument_list|(
literal|"{{routerPort}}"
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
literal|"routerPort"
argument_list|)
argument_list|)
decl_stmt|;
name|Client
name|client
init|=
operator|new
name|Client
argument_list|(
name|address
operator|+
literal|"?wsdl"
argument_list|)
decl_stmt|;
name|Greeter
name|proxy
init|=
name|client
operator|.
name|getProxy
argument_list|()
decl_stmt|;
name|String
name|resp
decl_stmt|;
name|resp
operator|=
name|proxy
operator|.
name|sayHi
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong response"
argument_list|,
literal|"Bonjour"
argument_list|,
name|resp
argument_list|)
expr_stmt|;
name|resp
operator|=
name|proxy
operator|.
name|greetMe
argument_list|(
literal|"Willem"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Get a wrong response"
argument_list|,
literal|"Hello Willem"
argument_list|,
name|resp
argument_list|)
expr_stmt|;
name|proxy
operator|.
name|greetMeOneWay
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.name"
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|proxy
operator|.
name|pingMe
argument_list|(
literal|"hello"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"exception expected but none thrown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PingMeFault
name|ex
parameter_list|)
block|{
name|FaultDetail
name|detail
init|=
name|ex
operator|.
name|getFaultInfo
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong FaultDetail major:"
argument_list|,
literal|2
argument_list|,
name|detail
operator|.
name|getMajor
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Wrong FaultDetail minor:"
argument_list|,
literal|1
argument_list|,
name|detail
operator|.
name|getMinor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|createApplicationContext ()
specifier|protected
name|AbstractXmlApplicationContext
name|createApplicationContext
parameter_list|()
block|{
return|return
operator|new
name|ClassPathXmlApplicationContext
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"/META-INF/spring/HttpToJmsCamelContext.xml"
block|}
argument_list|)
return|;
block|}
block|}
end_class

end_unit

