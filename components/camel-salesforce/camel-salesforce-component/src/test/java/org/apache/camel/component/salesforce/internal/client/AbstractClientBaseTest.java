begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.internal.client
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
operator|.
name|client
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
name|component
operator|.
name|salesforce
operator|.
name|api
operator|.
name|SalesforceException
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
name|internal
operator|.
name|SalesforceSession
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
name|HttpConversation
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
name|api
operator|.
name|Request
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
name|api
operator|.
name|Response
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
name|api
operator|.
name|Response
operator|.
name|CompleteListener
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
name|api
operator|.
name|Result
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
name|HttpFields
import|;
end_import

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
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|ArgumentCaptor
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|doNothing
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
import|;
end_import

begin_class
DECL|class|AbstractClientBaseTest
specifier|public
class|class
name|AbstractClientBaseTest
block|{
DECL|class|Client
specifier|static
class|class
name|Client
extends|extends
name|AbstractClientBase
block|{
DECL|method|Client (final SalesforceSession session)
name|Client
parameter_list|(
specifier|final
name|SalesforceSession
name|session
parameter_list|)
throws|throws
name|SalesforceException
block|{
name|super
argument_list|(
literal|null
argument_list|,
name|session
argument_list|,
name|mock
argument_list|(
name|SalesforceHttpClient
operator|.
name|class
argument_list|)
argument_list|,
literal|1
comment|/* 1 second termination timeout */
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRestException (final Response response, final InputStream responseContent)
specifier|protected
name|SalesforceException
name|createRestException
parameter_list|(
specifier|final
name|Response
name|response
parameter_list|,
specifier|final
name|InputStream
name|responseContent
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|setAccessToken (final Request request)
specifier|protected
name|void
name|setAccessToken
parameter_list|(
specifier|final
name|Request
name|request
parameter_list|)
block|{         }
block|}
DECL|field|session
name|SalesforceSession
name|session
init|=
name|mock
argument_list|(
name|SalesforceSession
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// having client as a field also tests that the same client instance can be
comment|// stopped and started again
DECL|field|client
specifier|final
name|Client
name|client
decl_stmt|;
DECL|method|AbstractClientBaseTest ()
specifier|public
name|AbstractClientBaseTest
parameter_list|()
throws|throws
name|SalesforceException
block|{
name|client
operator|=
operator|new
name|Client
argument_list|(
name|session
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|session
operator|.
name|getAccessToken
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"token"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Before
DECL|method|startClient ()
specifier|public
name|void
name|startClient
parameter_list|()
throws|throws
name|Exception
block|{
name|client
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldNotHangIfRequestsHaveFinished ()
specifier|public
name|void
name|shouldNotHangIfRequestsHaveFinished
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|Request
name|request
init|=
name|mock
argument_list|(
name|Request
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|ArgumentCaptor
argument_list|<
name|CompleteListener
argument_list|>
name|listener
init|=
name|ArgumentCaptor
operator|.
name|forClass
argument_list|(
name|CompleteListener
operator|.
name|class
argument_list|)
decl_stmt|;
name|doNothing
argument_list|()
operator|.
name|when
argument_list|(
name|request
argument_list|)
operator|.
name|send
argument_list|(
name|listener
operator|.
name|capture
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|doHttpRequest
argument_list|(
name|request
argument_list|,
parameter_list|(
name|response
parameter_list|,
name|headers
parameter_list|,
name|exception
parameter_list|)
lambda|->
block|{         }
argument_list|)
expr_stmt|;
specifier|final
name|Result
name|result
init|=
name|mock
argument_list|(
name|Result
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|final
name|Response
name|response
init|=
name|mock
argument_list|(
name|Response
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|result
operator|.
name|getResponse
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|response
operator|.
name|getHeaders
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
operator|new
name|HttpFields
argument_list|()
argument_list|)
expr_stmt|;
specifier|final
name|SalesforceHttpRequest
name|salesforceRequest
init|=
name|mock
argument_list|(
name|SalesforceHttpRequest
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|result
operator|.
name|getRequest
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|salesforceRequest
argument_list|)
expr_stmt|;
specifier|final
name|HttpConversation
name|conversation
init|=
name|mock
argument_list|(
name|HttpConversation
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|salesforceRequest
operator|.
name|getConversation
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|conversation
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|conversation
operator|.
name|getAttribute
argument_list|(
name|SalesforceSecurityHandler
operator|.
name|AUTHENTICATION_REQUEST_ATTRIBUTE
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|salesforceRequest
argument_list|)
expr_stmt|;
comment|// completes the request
name|listener
operator|.
name|getValue
argument_list|()
operator|.
name|onComplete
argument_list|(
name|result
argument_list|)
expr_stmt|;
specifier|final
name|long
name|stopStartTime
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
comment|// should not wait
name|client
operator|.
name|stop
argument_list|()
expr_stmt|;
specifier|final
name|long
name|elapsed
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|stopStartTime
decl_stmt|;
name|assertTrue
argument_list|(
name|elapsed
operator|<
literal|10
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldTimeoutWhenRequestsAreStillOngoing ()
specifier|public
name|void
name|shouldTimeoutWhenRequestsAreStillOngoing
parameter_list|()
throws|throws
name|Exception
block|{
name|client
operator|.
name|doHttpRequest
argument_list|(
name|mock
argument_list|(
name|Request
operator|.
name|class
argument_list|)
argument_list|,
parameter_list|(
name|response
parameter_list|,
name|headers
parameter_list|,
name|exception
parameter_list|)
lambda|->
block|{         }
argument_list|)
expr_stmt|;
comment|// the request never completes
specifier|final
name|long
name|stopStartTime
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
comment|// will wait for 1 second
name|client
operator|.
name|stop
argument_list|()
expr_stmt|;
specifier|final
name|long
name|elapsed
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|-
name|stopStartTime
decl_stmt|;
name|assertTrue
argument_list|(
name|elapsed
operator|>
literal|900
operator|&&
name|elapsed
operator|<
literal|1100
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

