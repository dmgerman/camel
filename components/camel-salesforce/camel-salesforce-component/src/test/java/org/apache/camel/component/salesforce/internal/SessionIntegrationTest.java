begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.internal
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|salesforce
operator|.
name|internal
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
name|component
operator|.
name|salesforce
operator|.
name|LoginConfigHelper
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
name|salesforce
operator|.
name|SalesforceHttpClient
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
name|DefaultCamelContext
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
name|support
operator|.
name|jsse
operator|.
name|SSLContextParameters
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|jetty
operator|.
name|util
operator|.
name|ssl
operator|.
name|SslContextFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|SessionIntegrationTest
specifier|public
class|class
name|SessionIntegrationTest
extends|extends
name|Assert
implements|implements
name|SalesforceSession
operator|.
name|SalesforceSessionListener
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SessionIntegrationTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|TIMEOUT
specifier|private
specifier|static
specifier|final
name|int
name|TIMEOUT
init|=
literal|60000
decl_stmt|;
DECL|field|onLoginTriggered
specifier|private
name|boolean
name|onLoginTriggered
decl_stmt|;
DECL|field|onLogoutTriggered
specifier|private
name|boolean
name|onLogoutTriggered
decl_stmt|;
annotation|@
name|Test
DECL|method|testLogin ()
specifier|public
name|void
name|testLogin
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|SslContextFactory
name|sslContextFactory
init|=
operator|new
name|SslContextFactory
argument_list|()
decl_stmt|;
name|sslContextFactory
operator|.
name|setSslContext
argument_list|(
operator|new
name|SSLContextParameters
argument_list|()
operator|.
name|createSSLContext
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|SalesforceHttpClient
name|httpClient
init|=
operator|new
name|SalesforceHttpClient
argument_list|(
name|sslContextFactory
argument_list|)
decl_stmt|;
name|httpClient
operator|.
name|setConnectTimeout
argument_list|(
name|TIMEOUT
argument_list|)
expr_stmt|;
specifier|final
name|SalesforceSession
name|session
init|=
operator|new
name|SalesforceSession
argument_list|(
operator|new
name|DefaultCamelContext
argument_list|()
argument_list|,
name|httpClient
argument_list|,
name|TIMEOUT
argument_list|,
name|LoginConfigHelper
operator|.
name|getLoginConfig
argument_list|()
argument_list|)
decl_stmt|;
name|session
operator|.
name|addListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|httpClient
operator|.
name|setSession
argument_list|(
name|session
argument_list|)
expr_stmt|;
name|httpClient
operator|.
name|start
argument_list|()
expr_stmt|;
try|try
block|{
name|String
name|loginToken
init|=
name|session
operator|.
name|login
argument_list|(
name|session
operator|.
name|getAccessToken
argument_list|()
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"First token "
operator|+
name|loginToken
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"SalesforceSessionListener onLogin NOT called"
argument_list|,
name|onLoginTriggered
argument_list|)
expr_stmt|;
name|onLoginTriggered
operator|=
literal|false
expr_stmt|;
comment|// refresh token, also causes logout
name|loginToken
operator|=
name|session
operator|.
name|login
argument_list|(
name|loginToken
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Refreshed token "
operator|+
name|loginToken
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"SalesforceSessionListener onLogout NOT called"
argument_list|,
name|onLogoutTriggered
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"SalesforceSessionListener onLogin NOT called"
argument_list|,
name|onLoginTriggered
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// logout finally
name|session
operator|.
name|logout
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|onLogin (String accessToken, String instanceUrl)
specifier|public
name|void
name|onLogin
parameter_list|(
name|String
name|accessToken
parameter_list|,
name|String
name|instanceUrl
parameter_list|)
block|{
name|onLoginTriggered
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onLogout ()
specifier|public
name|void
name|onLogout
parameter_list|()
block|{
name|onLogoutTriggered
operator|=
literal|true
expr_stmt|;
block|}
block|}
end_class

end_unit

