begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.salesforce.internal.processor
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
name|processor
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
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|AsyncCallback
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
name|Message
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
name|SalesforceComponent
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
name|SalesforceEndpoint
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
name|SalesforceEndpointConfig
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
name|api
operator|.
name|dto
operator|.
name|approval
operator|.
name|ApprovalRequest
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
name|approval
operator|.
name|ApprovalRequest
operator|.
name|Action
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
name|approval
operator|.
name|ApprovalRequests
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
name|client
operator|.
name|RestClient
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
name|DefaultExchange
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
name|DefaultMessage
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
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
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
name|fail
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|any
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|eq
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
name|spy
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
name|verify
import|;
end_import

begin_class
DECL|class|AbstractRestProcessorApprovalTest
specifier|public
class|class
name|AbstractRestProcessorApprovalTest
block|{
DECL|class|TestRestProcessor
specifier|static
class|class
name|TestRestProcessor
extends|extends
name|AbstractRestProcessor
block|{
DECL|field|client
name|RestClient
name|client
decl_stmt|;
DECL|method|TestRestProcessor ()
name|TestRestProcessor
parameter_list|()
throws|throws
name|SalesforceException
block|{
name|this
argument_list|(
name|endpoint
argument_list|()
argument_list|,
name|mock
argument_list|(
name|RestClient
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|TestRestProcessor (final SalesforceEndpoint endpoint, final RestClient client)
name|TestRestProcessor
parameter_list|(
specifier|final
name|SalesforceEndpoint
name|endpoint
parameter_list|,
specifier|final
name|RestClient
name|client
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|,
name|client
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|client
operator|=
name|client
expr_stmt|;
block|}
DECL|method|component ()
specifier|static
name|SalesforceComponent
name|component
parameter_list|()
block|{
return|return
operator|new
name|SalesforceComponent
argument_list|()
return|;
block|}
DECL|method|configuration ()
specifier|static
name|SalesforceEndpointConfig
name|configuration
parameter_list|()
block|{
return|return
operator|new
name|SalesforceEndpointConfig
argument_list|()
return|;
block|}
DECL|method|endpoint ()
specifier|static
name|SalesforceEndpoint
name|endpoint
parameter_list|()
block|{
return|return
operator|new
name|SalesforceEndpoint
argument_list|(
name|notUsed
argument_list|()
argument_list|,
name|component
argument_list|()
argument_list|,
name|configuration
argument_list|()
argument_list|,
name|notUsed
argument_list|()
argument_list|,
name|notUsed
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getRequestStream (final Exchange exchange)
specifier|protected
name|InputStream
name|getRequestStream
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|SalesforceException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|getRequestStream (final Message in, final Object object)
specifier|protected
name|InputStream
name|getRequestStream
parameter_list|(
specifier|final
name|Message
name|in
parameter_list|,
specifier|final
name|Object
name|object
parameter_list|)
throws|throws
name|SalesforceException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|processRequest (final Exchange exchange)
specifier|protected
name|void
name|processRequest
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|SalesforceException
block|{         }
annotation|@
name|Override
DECL|method|processResponse (final Exchange exchange, final InputStream responseEntity, final Map<String, String> headers, final SalesforceException ex, final AsyncCallback callback)
specifier|protected
name|void
name|processResponse
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|InputStream
name|responseEntity
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|,
specifier|final
name|SalesforceException
name|ex
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{         }
block|}
DECL|method|notUsed ()
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|notUsed
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
DECL|method|requestWithComment (final String comment)
specifier|static
name|ApprovalRequest
name|requestWithComment
parameter_list|(
specifier|final
name|String
name|comment
parameter_list|)
block|{
specifier|final
name|ApprovalRequest
name|approvalRequest
init|=
operator|new
name|ApprovalRequest
argument_list|()
decl_stmt|;
name|approvalRequest
operator|.
name|setComments
argument_list|(
name|comment
argument_list|)
expr_stmt|;
return|return
name|approvalRequest
return|;
block|}
annotation|@
name|Test
DECL|method|shouldApplyTemplateToRequestFromBody ()
specifier|public
name|void
name|shouldApplyTemplateToRequestFromBody
parameter_list|()
throws|throws
name|SalesforceException
block|{
specifier|final
name|ApprovalRequest
name|template
init|=
operator|new
name|ApprovalRequest
argument_list|()
decl_stmt|;
name|template
operator|.
name|setActionType
argument_list|(
name|Action
operator|.
name|Submit
argument_list|)
expr_stmt|;
specifier|final
name|ApprovalRequest
name|approvalRequest
init|=
operator|new
name|ApprovalRequest
argument_list|()
decl_stmt|;
name|approvalRequest
operator|.
name|setComments
argument_list|(
literal|"it should be me"
argument_list|)
expr_stmt|;
specifier|final
name|TestRestProcessor
name|processor
init|=
name|sendBodyAndHeader
argument_list|(
name|approvalRequest
argument_list|,
name|template
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|processor
argument_list|)
operator|.
name|getRequestStream
argument_list|(
name|any
argument_list|(
name|Message
operator|.
name|class
argument_list|)
argument_list|,
name|eq
argument_list|(
operator|new
name|ApprovalRequests
argument_list|(
name|approvalRequest
operator|.
name|applyTemplate
argument_list|(
name|template
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldApplyTemplateToRequestsFromBody ()
specifier|public
name|void
name|shouldApplyTemplateToRequestsFromBody
parameter_list|()
throws|throws
name|SalesforceException
block|{
specifier|final
name|ApprovalRequest
name|template
init|=
operator|new
name|ApprovalRequest
argument_list|()
decl_stmt|;
name|template
operator|.
name|setActionType
argument_list|(
name|Action
operator|.
name|Submit
argument_list|)
expr_stmt|;
specifier|final
name|ApprovalRequest
name|approvalRequest1
init|=
operator|new
name|ApprovalRequest
argument_list|()
decl_stmt|;
name|approvalRequest1
operator|.
name|setComments
argument_list|(
literal|"it should be me first"
argument_list|)
expr_stmt|;
specifier|final
name|ApprovalRequest
name|approvalRequest2
init|=
operator|new
name|ApprovalRequest
argument_list|()
decl_stmt|;
name|approvalRequest2
operator|.
name|setComments
argument_list|(
literal|"it should be me second"
argument_list|)
expr_stmt|;
specifier|final
name|TestRestProcessor
name|processor
init|=
name|sendBodyAndHeader
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|approvalRequest1
argument_list|,
name|approvalRequest2
argument_list|)
argument_list|,
name|template
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|processor
argument_list|)
operator|.
name|getRequestStream
argument_list|(
name|any
argument_list|(
name|Message
operator|.
name|class
argument_list|)
argument_list|,
name|eq
argument_list|(
operator|new
name|ApprovalRequests
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|approvalRequest1
operator|.
name|applyTemplate
argument_list|(
name|template
argument_list|)
argument_list|,
name|approvalRequest2
operator|.
name|applyTemplate
argument_list|(
name|template
argument_list|)
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldComplainIfNoHeaderGivenOrBodyIsEmptyIterable ()
specifier|public
name|void
name|shouldComplainIfNoHeaderGivenOrBodyIsEmptyIterable
parameter_list|()
block|{
try|try
block|{
name|sendBodyAndHeader
argument_list|(
name|Collections
operator|.
name|EMPTY_LIST
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"SalesforceException should be thrown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|SalesforceException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Exception should be about not giving a body or a header"
argument_list|,
literal|"Missing approval parameter in header or ApprovalRequest or List of ApprovalRequests body"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|shouldComplainIfNoHeaderOrBodyIsGiven ()
specifier|public
name|void
name|shouldComplainIfNoHeaderOrBodyIsGiven
parameter_list|()
block|{
try|try
block|{
name|sendBodyAndHeader
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"SalesforceException should be thrown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|SalesforceException
name|e
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Exception should be about not giving a body or a header"
argument_list|,
literal|"Missing approval parameter in header or ApprovalRequest or List of ApprovalRequests body"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|shouldFetchApprovalRequestFromBody ()
specifier|public
name|void
name|shouldFetchApprovalRequestFromBody
parameter_list|()
throws|throws
name|SalesforceException
block|{
specifier|final
name|ApprovalRequest
name|approvalRequest
init|=
operator|new
name|ApprovalRequest
argument_list|()
decl_stmt|;
name|approvalRequest
operator|.
name|setComments
argument_list|(
literal|"it should be me"
argument_list|)
expr_stmt|;
specifier|final
name|TestRestProcessor
name|processor
init|=
name|sendBody
argument_list|(
name|approvalRequest
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|processor
argument_list|)
operator|.
name|getRequestStream
argument_list|(
name|any
argument_list|(
name|Message
operator|.
name|class
argument_list|)
argument_list|,
name|eq
argument_list|(
operator|new
name|ApprovalRequests
argument_list|(
name|approvalRequest
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldFetchApprovalRequestFromHeader ()
specifier|public
name|void
name|shouldFetchApprovalRequestFromHeader
parameter_list|()
throws|throws
name|SalesforceException
block|{
specifier|final
name|ApprovalRequest
name|request
init|=
operator|new
name|ApprovalRequest
argument_list|()
decl_stmt|;
name|request
operator|.
name|setComments
argument_list|(
literal|"hi there"
argument_list|)
expr_stmt|;
specifier|final
name|TestRestProcessor
name|processor
init|=
name|sendBodyAndHeader
argument_list|(
literal|null
argument_list|,
name|request
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|processor
argument_list|)
operator|.
name|getRequestStream
argument_list|(
name|any
argument_list|(
name|Message
operator|.
name|class
argument_list|)
argument_list|,
name|eq
argument_list|(
operator|new
name|ApprovalRequests
argument_list|(
name|request
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldFetchApprovalRequestFromHeaderEvenIfBodyIsDefinedButNotConvertable ()
specifier|public
name|void
name|shouldFetchApprovalRequestFromHeaderEvenIfBodyIsDefinedButNotConvertable
parameter_list|()
throws|throws
name|SalesforceException
block|{
specifier|final
name|ApprovalRequest
name|request
init|=
operator|new
name|ApprovalRequest
argument_list|()
decl_stmt|;
name|request
operator|.
name|setComments
argument_list|(
literal|"hi there"
argument_list|)
expr_stmt|;
specifier|final
name|TestRestProcessor
name|processor
init|=
name|sendBodyAndHeaders
argument_list|(
literal|"Nothing to see here"
argument_list|,
name|request
argument_list|,
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"approval.ContextId"
argument_list|,
literal|"context-id"
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|ApprovalRequest
name|combined
init|=
operator|new
name|ApprovalRequest
argument_list|()
decl_stmt|;
name|combined
operator|.
name|setComments
argument_list|(
literal|"hi there"
argument_list|)
expr_stmt|;
name|combined
operator|.
name|setContextId
argument_list|(
literal|"context-id"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|processor
argument_list|)
operator|.
name|getRequestStream
argument_list|(
name|any
argument_list|(
name|Message
operator|.
name|class
argument_list|)
argument_list|,
name|eq
argument_list|(
operator|new
name|ApprovalRequests
argument_list|(
name|combined
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldFetchApprovalRequestsFromBody ()
specifier|public
name|void
name|shouldFetchApprovalRequestsFromBody
parameter_list|()
throws|throws
name|SalesforceException
block|{
specifier|final
name|ApprovalRequest
name|approvalRequest1
init|=
operator|new
name|ApprovalRequest
argument_list|()
decl_stmt|;
name|approvalRequest1
operator|.
name|setComments
argument_list|(
literal|"it should be me first"
argument_list|)
expr_stmt|;
specifier|final
name|ApprovalRequest
name|approvalRequest2
init|=
operator|new
name|ApprovalRequest
argument_list|()
decl_stmt|;
name|approvalRequest2
operator|.
name|setComments
argument_list|(
literal|"it should be me second"
argument_list|)
expr_stmt|;
specifier|final
name|TestRestProcessor
name|processor
init|=
name|sendBody
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|approvalRequest1
argument_list|,
name|approvalRequest2
argument_list|)
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|processor
argument_list|)
operator|.
name|getRequestStream
argument_list|(
name|any
argument_list|(
name|Message
operator|.
name|class
argument_list|)
argument_list|,
name|eq
argument_list|(
operator|new
name|ApprovalRequests
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|approvalRequest1
argument_list|,
name|approvalRequest2
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldFetchApprovalRequestsFromMultiplePropertiesInMessageHeaders ()
specifier|public
name|void
name|shouldFetchApprovalRequestsFromMultiplePropertiesInMessageHeaders
parameter_list|()
throws|throws
name|SalesforceException
block|{
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"approval.ContextId"
argument_list|,
literal|"contextId"
argument_list|)
expr_stmt|;
specifier|final
name|TestRestProcessor
name|processor
init|=
name|sendBodyAndHeaders
argument_list|(
name|notUsed
argument_list|()
argument_list|,
name|notUsed
argument_list|()
argument_list|,
name|headers
argument_list|)
decl_stmt|;
specifier|final
name|ApprovalRequest
name|request
init|=
operator|new
name|ApprovalRequest
argument_list|()
decl_stmt|;
name|request
operator|.
name|setContextId
argument_list|(
literal|"contextId"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|processor
argument_list|)
operator|.
name|getRequestStream
argument_list|(
name|any
argument_list|(
name|Message
operator|.
name|class
argument_list|)
argument_list|,
name|eq
argument_list|(
operator|new
name|ApprovalRequests
argument_list|(
name|request
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldHonorPriorities ()
specifier|public
name|void
name|shouldHonorPriorities
parameter_list|()
throws|throws
name|SalesforceException
block|{
specifier|final
name|ApprovalRequest
name|template
init|=
operator|new
name|ApprovalRequest
argument_list|()
decl_stmt|;
name|template
operator|.
name|setComments
argument_list|(
literal|"third priority"
argument_list|)
expr_stmt|;
specifier|final
name|ApprovalRequest
name|body
init|=
operator|new
name|ApprovalRequest
argument_list|()
decl_stmt|;
name|body
operator|.
name|setComments
argument_list|(
literal|"first priority"
argument_list|)
expr_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"approval.Comments"
argument_list|,
literal|"second priority"
argument_list|)
decl_stmt|;
specifier|final
name|TestRestProcessor
name|processor1
init|=
name|sendBodyAndHeaders
argument_list|(
literal|null
argument_list|,
name|template
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|processor1
argument_list|)
operator|.
name|getRequestStream
argument_list|(
name|any
argument_list|(
name|Message
operator|.
name|class
argument_list|)
argument_list|,
name|eq
argument_list|(
operator|new
name|ApprovalRequests
argument_list|(
name|requestWithComment
argument_list|(
literal|"third priority"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|TestRestProcessor
name|processor2
init|=
name|sendBodyAndHeaders
argument_list|(
literal|null
argument_list|,
name|template
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|processor2
argument_list|)
operator|.
name|getRequestStream
argument_list|(
name|any
argument_list|(
name|Message
operator|.
name|class
argument_list|)
argument_list|,
name|eq
argument_list|(
operator|new
name|ApprovalRequests
argument_list|(
name|requestWithComment
argument_list|(
literal|"second priority"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
specifier|final
name|TestRestProcessor
name|processor3
init|=
name|sendBodyAndHeaders
argument_list|(
name|body
argument_list|,
name|template
argument_list|,
name|headers
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|processor3
argument_list|)
operator|.
name|getRequestStream
argument_list|(
name|any
argument_list|(
name|Message
operator|.
name|class
argument_list|)
argument_list|,
name|eq
argument_list|(
operator|new
name|ApprovalRequests
argument_list|(
name|requestWithComment
argument_list|(
literal|"first priority"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|sendBody (final Object body)
name|TestRestProcessor
name|sendBody
parameter_list|(
specifier|final
name|Object
name|body
parameter_list|)
throws|throws
name|SalesforceException
block|{
return|return
name|sendBodyAndHeader
argument_list|(
name|body
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|sendBodyAndHeader (final Object body, final ApprovalRequest template)
name|TestRestProcessor
name|sendBodyAndHeader
parameter_list|(
specifier|final
name|Object
name|body
parameter_list|,
specifier|final
name|ApprovalRequest
name|template
parameter_list|)
throws|throws
name|SalesforceException
block|{
return|return
name|sendBodyAndHeaders
argument_list|(
name|body
argument_list|,
name|template
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
return|;
block|}
DECL|method|sendBodyAndHeaders (final Object body, final ApprovalRequest template, final Map<String, Object> headers)
name|TestRestProcessor
name|sendBodyAndHeaders
parameter_list|(
specifier|final
name|Object
name|body
parameter_list|,
specifier|final
name|ApprovalRequest
name|template
parameter_list|,
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|)
throws|throws
name|SalesforceException
block|{
specifier|final
name|TestRestProcessor
name|processor
init|=
name|spy
argument_list|(
operator|new
name|TestRestProcessor
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
specifier|final
name|Exchange
name|exchange
init|=
operator|new
name|DefaultExchange
argument_list|(
name|context
argument_list|)
decl_stmt|;
specifier|final
name|Message
name|message
init|=
operator|new
name|DefaultMessage
argument_list|(
name|context
argument_list|)
decl_stmt|;
if|if
condition|(
name|headers
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|setHeaders
argument_list|(
name|headers
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|setHeader
argument_list|(
name|SalesforceEndpointConfig
operator|.
name|APPROVAL
argument_list|,
name|template
argument_list|)
expr_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setIn
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|processor
operator|.
name|processApproval
argument_list|(
name|exchange
argument_list|,
name|notUsed
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|processor
return|;
block|}
block|}
end_class

end_unit

