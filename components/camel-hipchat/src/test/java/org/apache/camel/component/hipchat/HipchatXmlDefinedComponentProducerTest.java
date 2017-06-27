begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hipchat
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hipchat
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|EndpointInject
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
name|model
operator|.
name|ModelHelper
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
name|model
operator|.
name|RoutesDefinition
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
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|core
operator|.
name|Is
operator|.
name|is
import|;
end_import

begin_class
DECL|class|HipchatXmlDefinedComponentProducerTest
specifier|public
class|class
name|HipchatXmlDefinedComponentProducerTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"hipchat:https:foobar.com:443?authToken=abc123"
argument_list|)
DECL|field|endpoint
specifier|protected
name|Endpoint
name|endpoint
decl_stmt|;
annotation|@
name|Test
DECL|method|shouldConfigureEndpointCorrectlyViaXml ()
specifier|public
name|void
name|shouldConfigureEndpointCorrectlyViaXml
parameter_list|()
throws|throws
name|Exception
block|{
name|assertIsInstanceOf
argument_list|(
name|HipchatEndpoint
operator|.
name|class
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|HipchatEndpoint
name|hipchatEndpoint
init|=
operator|(
name|HipchatEndpoint
operator|)
name|endpoint
decl_stmt|;
name|HipchatConfiguration
name|configuration
init|=
name|hipchatEndpoint
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|configuration
operator|.
name|getAuthToken
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"abc123"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|configuration
operator|.
name|getHost
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"foobar.com"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|configuration
operator|.
name|getProtocol
argument_list|()
argument_list|,
name|is
argument_list|(
literal|"https"
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|configuration
operator|.
name|getPort
argument_list|()
argument_list|,
name|is
argument_list|(
literal|443
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|HipchatComponent
name|component
init|=
operator|new
name|HipchatComponent
argument_list|(
name|context
argument_list|)
block|{
annotation|@
name|Override
specifier|protected
name|HipchatEndpoint
name|getHipchatEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
return|return
operator|new
name|HipchatEPSuccessTestSupport
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|context
operator|.
name|addComponent
argument_list|(
literal|"hipchat"
argument_list|,
name|component
argument_list|)
expr_stmt|;
comment|// This test is all about ensuring the endpoint is configured correctly when using the XML DSL so this
try|try
init|(
name|InputStream
name|routes
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"HipchatXmlDefinedComponentProducerTest-route.xml"
argument_list|)
init|)
block|{
name|RoutesDefinition
name|routesDefinition
init|=
name|ModelHelper
operator|.
name|loadRoutesDefinition
argument_list|(
name|context
argument_list|,
name|routes
argument_list|)
decl_stmt|;
name|context
operator|.
name|addRouteDefinition
argument_list|(
name|routesDefinition
operator|.
name|getRoutes
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|context
return|;
block|}
block|}
end_class

end_unit

