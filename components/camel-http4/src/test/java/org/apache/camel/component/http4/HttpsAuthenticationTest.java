begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|Exchange
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
name|Processor
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
name|component
operator|.
name|http4
operator|.
name|handler
operator|.
name|AuthenticationValidationHandler
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
name|impl
operator|.
name|JndiRegistry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpRequestInterceptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpResponseInterceptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|conn
operator|.
name|ssl
operator|.
name|AllowAllHostnameVerifier
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|localserver
operator|.
name|RequestBasicAuth
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|localserver
operator|.
name|ResponseBasicUnauthorized
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|protocol
operator|.
name|HttpProcessor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|protocol
operator|.
name|ImmutableHttpProcessor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|protocol
operator|.
name|ResponseContent
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
comment|/**  *  * @version   */
end_comment

begin_class
DECL|class|HttpsAuthenticationTest
specifier|public
class|class
name|HttpsAuthenticationTest
extends|extends
name|BaseHttpsTest
block|{
DECL|field|user
specifier|private
name|String
name|user
init|=
literal|"camel"
decl_stmt|;
DECL|field|password
specifier|private
name|String
name|password
init|=
literal|"password"
decl_stmt|;
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|registry
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"x509HostnameVerifier"
argument_list|,
operator|new
name|AllowAllHostnameVerifier
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
annotation|@
name|Test
DECL|method|httpsGetWithAuthentication ()
specifier|public
name|void
name|httpsGetWithAuthentication
parameter_list|()
throws|throws
name|Exception
block|{
name|localServer
operator|.
name|register
argument_list|(
literal|"/"
argument_list|,
operator|new
name|AuthenticationValidationHandler
argument_list|(
literal|"GET"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|getExpectedContent
argument_list|()
argument_list|,
name|user
argument_list|,
name|password
argument_list|)
argument_list|)
expr_stmt|;
name|Exchange
name|exchange
init|=
name|template
operator|.
name|request
argument_list|(
literal|"https4://127.0.0.1:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/?authUsername=camel&authPassword=password&x509HostnameVerifier=x509HostnameVerifier"
argument_list|,
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{             }
block|}
argument_list|)
decl_stmt|;
name|assertExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getBasicHttpProcessor ()
specifier|protected
name|HttpProcessor
name|getBasicHttpProcessor
parameter_list|()
block|{
name|List
argument_list|<
name|HttpRequestInterceptor
argument_list|>
name|requestInterceptors
init|=
operator|new
name|ArrayList
argument_list|<
name|HttpRequestInterceptor
argument_list|>
argument_list|()
decl_stmt|;
name|requestInterceptors
operator|.
name|add
argument_list|(
operator|new
name|RequestBasicAuth
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|HttpResponseInterceptor
argument_list|>
name|responseInterceptors
init|=
operator|new
name|ArrayList
argument_list|<
name|HttpResponseInterceptor
argument_list|>
argument_list|()
decl_stmt|;
name|responseInterceptors
operator|.
name|add
argument_list|(
operator|new
name|ResponseContent
argument_list|()
argument_list|)
expr_stmt|;
name|responseInterceptors
operator|.
name|add
argument_list|(
operator|new
name|ResponseBasicUnauthorized
argument_list|()
argument_list|)
expr_stmt|;
name|ImmutableHttpProcessor
name|httpproc
init|=
operator|new
name|ImmutableHttpProcessor
argument_list|(
name|requestInterceptors
argument_list|,
name|responseInterceptors
argument_list|)
decl_stmt|;
return|return
name|httpproc
return|;
block|}
block|}
end_class

end_unit

