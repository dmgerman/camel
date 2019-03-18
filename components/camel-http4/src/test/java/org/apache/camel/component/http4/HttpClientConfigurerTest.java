begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http4
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

begin_class
DECL|class|HttpClientConfigurerTest
specifier|public
class|class
name|HttpClientConfigurerTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|configurer
specifier|private
name|HttpClientConfigurer
name|configurer
decl_stmt|;
annotation|@
name|Test
DECL|method|testHttpClientConfigurer ()
specifier|public
name|void
name|testHttpClientConfigurer
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpClientConfigurer
name|gotConfigurer
init|=
name|getMandatoryEndpoint
argument_list|(
literal|"http4://www.google.com/search"
argument_list|,
name|HttpEndpoint
operator|.
name|class
argument_list|)
operator|.
name|getHttpClientConfigurer
argument_list|()
decl_stmt|;
name|assertSame
argument_list|(
name|configurer
argument_list|,
name|gotConfigurer
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
comment|// add configurer to http component
name|configurer
operator|=
operator|new
name|ProxyHttpClientConfigurer
argument_list|(
literal|"proxyhost"
argument_list|,
literal|80
argument_list|,
literal|"http4"
argument_list|,
literal|"user"
argument_list|,
literal|"password"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|getContext
argument_list|()
operator|.
name|getComponent
argument_list|(
literal|"http4"
argument_list|,
name|HttpComponent
operator|.
name|class
argument_list|)
operator|.
name|setHttpClientConfigurer
argument_list|(
name|configurer
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"http4://www.google.com/search"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

