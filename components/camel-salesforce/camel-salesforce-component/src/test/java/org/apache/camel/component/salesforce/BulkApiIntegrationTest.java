begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce
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
name|api
operator|.
name|dto
operator|.
name|bulk
operator|.
name|ContentType
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
name|api
operator|.
name|dto
operator|.
name|bulk
operator|.
name|JobInfo
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
name|api
operator|.
name|dto
operator|.
name|bulk
operator|.
name|OperationEnum
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
name|dto
operator|.
name|generated
operator|.
name|Merchandise__c
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
name|util
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
name|client
operator|.
name|ContentExchange
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
name|client
operator|.
name|HttpClient
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
name|client
operator|.
name|HttpExchange
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
name|client
operator|.
name|RedirectListener
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
name|http
operator|.
name|HttpMethods
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
name|http
operator|.
name|HttpStatus
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
name|Test
import|;
end_import

begin_class
DECL|class|BulkApiIntegrationTest
specifier|public
class|class
name|BulkApiIntegrationTest
extends|extends
name|AbstractBulkApiTestBase
block|{
annotation|@
name|Test
DECL|method|testRetry ()
specifier|public
name|void
name|testRetry
parameter_list|()
throws|throws
name|Exception
block|{
name|SalesforceComponent
name|sf
init|=
name|context
argument_list|()
operator|.
name|getComponent
argument_list|(
literal|"salesforce"
argument_list|,
name|SalesforceComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|accessToken
init|=
name|sf
operator|.
name|getSession
argument_list|()
operator|.
name|getAccessToken
argument_list|()
decl_stmt|;
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
argument_list|()
argument_list|)
expr_stmt|;
name|HttpClient
name|httpClient
init|=
operator|new
name|HttpClient
argument_list|(
name|sslContextFactory
argument_list|)
decl_stmt|;
name|httpClient
operator|.
name|setConnectTimeout
argument_list|(
literal|60000
argument_list|)
expr_stmt|;
name|httpClient
operator|.
name|setTimeout
argument_list|(
literal|60000
argument_list|)
expr_stmt|;
name|httpClient
operator|.
name|registerListener
argument_list|(
name|RedirectListener
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|httpClient
operator|.
name|start
argument_list|()
expr_stmt|;
name|ContentExchange
name|logoutGet
init|=
operator|new
name|ContentExchange
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|logoutGet
operator|.
name|setURL
argument_list|(
name|sf
operator|.
name|getLoginConfig
argument_list|()
operator|.
name|getLoginUrl
argument_list|()
operator|+
literal|"/services/oauth2/revoke?token="
operator|+
name|accessToken
argument_list|)
expr_stmt|;
name|logoutGet
operator|.
name|setMethod
argument_list|(
name|HttpMethods
operator|.
name|GET
argument_list|)
expr_stmt|;
name|httpClient
operator|.
name|send
argument_list|(
name|logoutGet
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|HttpExchange
operator|.
name|STATUS_COMPLETED
argument_list|,
name|logoutGet
operator|.
name|waitForDone
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|HttpStatus
operator|.
name|OK_200
argument_list|,
name|logoutGet
operator|.
name|getResponseStatus
argument_list|()
argument_list|)
expr_stmt|;
name|JobInfo
name|jobInfo
init|=
operator|new
name|JobInfo
argument_list|()
decl_stmt|;
name|jobInfo
operator|.
name|setOperation
argument_list|(
name|OperationEnum
operator|.
name|INSERT
argument_list|)
expr_stmt|;
name|jobInfo
operator|.
name|setContentType
argument_list|(
name|ContentType
operator|.
name|CSV
argument_list|)
expr_stmt|;
name|jobInfo
operator|.
name|setObject
argument_list|(
name|Merchandise__c
operator|.
name|class
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
name|createJob
argument_list|(
name|jobInfo
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

