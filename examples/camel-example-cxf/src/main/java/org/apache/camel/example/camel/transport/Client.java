begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.example.camel.transport
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|example
operator|.
name|camel
operator|.
name|transport
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
name|Service
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

begin_class
DECL|class|Client
specifier|public
specifier|final
class|class
name|Client
block|{
DECL|field|SERVICE_NAME
specifier|private
specifier|static
specifier|final
name|QName
name|SERVICE_NAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"CamelService"
argument_list|)
decl_stmt|;
DECL|field|PORT_NAME
specifier|private
specifier|static
specifier|final
name|QName
name|PORT_NAME
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world_soap_http"
argument_list|,
literal|"CamelPort"
argument_list|)
decl_stmt|;
DECL|field|service
specifier|private
name|Service
name|service
decl_stmt|;
DECL|field|port
specifier|private
name|Greeter
name|port
decl_stmt|;
DECL|method|Client (String address)
specifier|public
name|Client
parameter_list|(
name|String
name|address
parameter_list|)
throws|throws
name|MalformedURLException
block|{
comment|// create the client from the SEI
name|service
operator|=
name|Service
operator|.
name|create
argument_list|(
name|SERVICE_NAME
argument_list|)
expr_stmt|;
name|service
operator|.
name|addPort
argument_list|(
name|PORT_NAME
argument_list|,
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|soap
operator|.
name|SOAPBinding
operator|.
name|SOAP11HTTP_BINDING
argument_list|,
name|address
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Acquiring router port ..."
argument_list|)
expr_stmt|;
name|port
operator|=
name|service
operator|.
name|getPort
argument_list|(
name|PORT_NAME
argument_list|,
name|Greeter
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
DECL|method|getProxy ()
specifier|public
name|Greeter
name|getProxy
parameter_list|()
block|{
return|return
name|port
return|;
block|}
DECL|method|main (String args[])
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
name|args
index|[]
parameter_list|)
throws|throws
name|Exception
block|{
comment|// set the client's service access point
name|Client
name|client
init|=
operator|new
name|Client
argument_list|(
literal|"http://localhost:9091/GreeterContext/GreeterPort"
argument_list|)
decl_stmt|;
comment|// invoking the services
name|client
operator|.
name|invoke
argument_list|()
expr_stmt|;
block|}
DECL|method|invoke ()
specifier|public
name|void
name|invoke
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|resp
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Invoking sayHi..."
argument_list|)
expr_stmt|;
name|resp
operator|=
name|port
operator|.
name|sayHi
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Server responded with: "
operator|+
name|resp
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Invoking greetMe... with Mike"
argument_list|)
expr_stmt|;
name|resp
operator|=
name|port
operator|.
name|greetMe
argument_list|(
literal|"Mike"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Server responded with: "
operator|+
name|resp
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Invoking greetMe... with James"
argument_list|)
expr_stmt|;
name|resp
operator|=
name|port
operator|.
name|greetMe
argument_list|(
literal|"James"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Server responded with: "
operator|+
name|resp
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Invoking greetMeOneWay..."
argument_list|)
expr_stmt|;
name|port
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
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"No response from server as method is OneWay"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
try|try
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Invoking pingMe, expecting exception..."
argument_list|)
expr_stmt|;
name|port
operator|.
name|pingMe
argument_list|(
literal|"hello"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PingMeFault
name|ex
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Expected exception: PingMeFault has occurred: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|FaultDetail
name|detail
init|=
name|ex
operator|.
name|getFaultInfo
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"FaultDetail major:"
operator|+
name|detail
operator|.
name|getMajor
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"FaultDetail minor:"
operator|+
name|detail
operator|.
name|getMinor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

