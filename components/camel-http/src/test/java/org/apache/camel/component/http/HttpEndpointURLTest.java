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
name|java
operator|.
name|util
operator|.
name|List
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

begin_class
DECL|class|HttpEndpointURLTest
specifier|public
class|class
name|HttpEndpointURLTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|Test
DECL|method|testHttpEndpointURLWithAuthPassWord ()
specifier|public
name|void
name|testHttpEndpointURLWithAuthPassWord
parameter_list|()
block|{
name|HttpEndpoint
name|endpoint
init|=
operator|(
name|HttpEndpoint
operator|)
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"http://localhost/test?authMethod=Basic&authPassword=RAW(pa&&word)&authUsername=usr"
argument_list|)
decl_stmt|;
name|HttpClientConfigurer
name|configurer
init|=
name|endpoint
operator|.
name|getHttpClientConfigurer
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Expect the CompositeHttpConfigurer"
argument_list|,
name|configurer
operator|instanceof
name|CompositeHttpConfigurer
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|HttpClientConfigurer
argument_list|>
name|configurers
init|=
operator|(
operator|(
name|CompositeHttpConfigurer
operator|)
name|configurer
operator|)
operator|.
name|getConfigurers
argument_list|()
decl_stmt|;
name|BasicAuthenticationHttpClientConfigurer
name|basicAuthenticationConfigurer
init|=
literal|null
decl_stmt|;
for|for
control|(
name|HttpClientConfigurer
name|config
range|:
name|configurers
control|)
block|{
if|if
condition|(
name|config
operator|instanceof
name|BasicAuthenticationHttpClientConfigurer
condition|)
block|{
name|basicAuthenticationConfigurer
operator|=
operator|(
name|BasicAuthenticationHttpClientConfigurer
operator|)
name|config
expr_stmt|;
break|break;
block|}
block|}
name|assertNotNull
argument_list|(
literal|"We should find the basicAuthenticationConfigurer"
argument_list|,
name|basicAuthenticationConfigurer
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"pa&&word"
argument_list|,
name|basicAuthenticationConfigurer
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHttpEndpointURLWithIPv6 ()
specifier|public
name|void
name|testHttpEndpointURLWithIPv6
parameter_list|()
block|{
name|HttpEndpoint
name|endpoint
init|=
operator|(
name|HttpEndpoint
operator|)
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"http://[2a00:8a00:6000:40::1413]:30300/test?test=true"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://[2a00:8a00:6000:40::1413]:30300/test?test=true"
argument_list|,
name|endpoint
operator|.
name|getHttpUri
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

