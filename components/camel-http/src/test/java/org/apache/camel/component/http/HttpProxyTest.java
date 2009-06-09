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
name|ContextTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|HttpClient
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|HttpProxyTest
specifier|public
class|class
name|HttpProxyTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testNoHttpProxyConfigured ()
specifier|public
name|void
name|testNoHttpProxyConfigured
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpEndpoint
name|http
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"http://www.google.com"
argument_list|,
name|HttpEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|HttpClient
name|client
init|=
name|http
operator|.
name|createHttpClient
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
literal|"No proxy configured yet"
argument_list|,
name|client
operator|.
name|getHostConfiguration
argument_list|()
operator|.
name|getProxyHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"No proxy configured yet"
argument_list|,
operator|-
literal|1
argument_list|,
name|client
operator|.
name|getHostConfiguration
argument_list|()
operator|.
name|getProxyPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testHttpProxyConfigured ()
specifier|public
name|void
name|testHttpProxyConfigured
parameter_list|()
throws|throws
name|Exception
block|{
name|HttpEndpoint
name|http
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
literal|"http://www.google.com"
argument_list|,
name|HttpEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"http.proxyHost"
argument_list|,
literal|"myproxy"
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"http.proxyPort"
argument_list|,
literal|"1234"
argument_list|)
expr_stmt|;
try|try
block|{
name|HttpClient
name|client
init|=
name|http
operator|.
name|createHttpClient
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"myproxy"
argument_list|,
name|client
operator|.
name|getHostConfiguration
argument_list|()
operator|.
name|getProxyHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1234
argument_list|,
name|client
operator|.
name|getHostConfiguration
argument_list|()
operator|.
name|getProxyPort
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|System
operator|.
name|clearProperty
argument_list|(
literal|"http.proxyHost"
argument_list|)
expr_stmt|;
name|System
operator|.
name|clearProperty
argument_list|(
literal|"http.proxyPort"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

