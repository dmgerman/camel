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
name|After
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
name|hello_world_soap_http
operator|.
name|GreeterImpl
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
DECL|class|CxfGreeterMessageCamelHttpRouterTest
specifier|public
class|class
name|CxfGreeterMessageCamelHttpRouterTest
extends|extends
name|CxfGreeterMessageRouterTest
block|{
DECL|field|endpoint
specifier|protected
specifier|static
name|Endpoint
name|endpoint
decl_stmt|;
DECL|field|serverAddress
specifier|protected
specifier|static
name|String
name|serverAddress
init|=
literal|"http://localhost:"
operator|+
name|getPort1
argument_list|()
operator|+
literal|"/CxfGreeterMessageCamelHttpRouterTest/SoapContext/SoapPort"
decl_stmt|;
annotation|@
name|AfterClass
DECL|method|stopService ()
specifier|public
specifier|static
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
name|GreeterImpl
argument_list|()
decl_stmt|;
name|String
name|address
init|=
literal|"http://localhost:"
operator|+
name|getPort1
argument_list|()
operator|+
literal|"/CxfGreeterMessageCamelHttpRouterTest/SoapContext/SoapPort"
decl_stmt|;
name|endpoint
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
literal|"cxf:bean:routerEndpoint?dataFormat=Message&publishedEndpointUrl=http://www.simple.com/services/test"
argument_list|)
comment|// The fix the side effect of CAMEL-7436
operator|.
name|removeHeaders
argument_list|(
literal|"CamelHttp*"
argument_list|)
operator|.
name|to
argument_list|(
name|serverAddress
operator|+
literal|"?throwExceptionOnFailure=false"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
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
literal|"org/apache/camel/component/cxf/GreeterEndpointWithCamelHttpBeans.xml"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

