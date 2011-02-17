begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http
package|;
end_package

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
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|HttpProxyAuthDigestTest
specifier|public
class|class
name|HttpProxyAuthDigestTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testProxyAuthDigest ()
specifier|public
name|void
name|testProxyAuthDigest
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpClientConfigurer
name|configurer
init|=
name|getMandatoryEndpoint
argument_list|(
literal|"http://www.google.com/search"
argument_list|,
name|HttpEndpoint
operator|.
name|class
argument_list|)
operator|.
name|getHttpClientConfigurer
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|configurer
argument_list|)
expr_stmt|;
name|CompositeHttpConfigurer
name|comp
init|=
name|assertIsInstanceOf
argument_list|(
name|CompositeHttpConfigurer
operator|.
name|class
argument_list|,
name|configurer
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|comp
operator|.
name|getConfigurers
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|BasicAuthenticationHttpClientConfigurer
name|basic
init|=
name|assertIsInstanceOf
argument_list|(
name|BasicAuthenticationHttpClientConfigurer
operator|.
name|class
argument_list|,
name|comp
operator|.
name|getConfigurers
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|basic
operator|.
name|isProxy
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"myUser"
argument_list|,
name|basic
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"myPassword"
argument_list|,
name|basic
operator|.
name|getPassword
argument_list|()
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
throws|throws
name|Exception
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
comment|// setup proxy details
name|HttpConfiguration
name|config
init|=
operator|new
name|HttpConfiguration
argument_list|()
decl_stmt|;
name|config
operator|.
name|setProxyAuthMethod
argument_list|(
name|AuthMethod
operator|.
name|Digest
argument_list|)
expr_stmt|;
name|config
operator|.
name|setProxyAuthUsername
argument_list|(
literal|"myUser"
argument_list|)
expr_stmt|;
name|config
operator|.
name|setProxyAuthPassword
argument_list|(
literal|"myPassword"
argument_list|)
expr_stmt|;
name|HttpComponent
name|http
init|=
name|context
operator|.
name|getComponent
argument_list|(
literal|"http"
argument_list|,
name|HttpComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|http
operator|.
name|setHttpConfiguration
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"http://www.google.com/search"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

