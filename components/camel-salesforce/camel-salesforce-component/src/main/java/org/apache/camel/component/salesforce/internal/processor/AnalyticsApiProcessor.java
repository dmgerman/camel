begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|util
operator|.
name|List
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
name|analytics
operator|.
name|reports
operator|.
name|AbstractReportResultsBase
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
name|analytics
operator|.
name|reports
operator|.
name|RecentReport
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
name|analytics
operator|.
name|reports
operator|.
name|ReportDescription
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
name|analytics
operator|.
name|reports
operator|.
name|ReportInstance
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
name|analytics
operator|.
name|reports
operator|.
name|ReportMetadata
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
name|AnalyticsApiClient
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
name|DefaultAnalyticsApiClient
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
name|service
operator|.
name|ServiceHelper
import|;
end_import

begin_import
import|import static
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
operator|.
name|INCLUDE_DETAILS
import|;
end_import

begin_import
import|import static
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
operator|.
name|INSTANCE_ID
import|;
end_import

begin_import
import|import static
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
operator|.
name|REPORT_ID
import|;
end_import

begin_import
import|import static
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
operator|.
name|REPORT_METADATA
import|;
end_import

begin_comment
comment|/**  * Exchange processor for Analytics API.  */
end_comment

begin_class
DECL|class|AnalyticsApiProcessor
specifier|public
class|class
name|AnalyticsApiProcessor
extends|extends
name|AbstractSalesforceProcessor
block|{
DECL|field|analyticsClient
specifier|private
name|AnalyticsApiClient
name|analyticsClient
decl_stmt|;
DECL|method|AnalyticsApiProcessor (SalesforceEndpoint endpoint)
specifier|public
name|AnalyticsApiProcessor
parameter_list|(
name|SalesforceEndpoint
name|endpoint
parameter_list|)
throws|throws
name|SalesforceException
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|analyticsClient
operator|=
operator|new
name|DefaultAnalyticsApiClient
argument_list|(
operator|(
name|String
operator|)
name|endpointConfigMap
operator|.
name|get
argument_list|(
name|SalesforceEndpointConfig
operator|.
name|API_VERSION
argument_list|)
argument_list|,
name|session
argument_list|,
name|httpClient
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (final Exchange exchange, final AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|boolean
name|done
init|=
literal|false
decl_stmt|;
try|try
block|{
switch|switch
condition|(
name|operationName
condition|)
block|{
case|case
name|GET_RECENT_REPORTS
case|:
name|processGetRecentReports
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
expr_stmt|;
break|break;
case|case
name|GET_REPORT_DESCRIPTION
case|:
name|processGetReportDescription
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
expr_stmt|;
break|break;
case|case
name|EXECUTE_SYNCREPORT
case|:
name|processExecuteSyncReport
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
expr_stmt|;
break|break;
case|case
name|EXECUTE_ASYNCREPORT
case|:
name|processExecuteAsyncReport
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
expr_stmt|;
break|break;
case|case
name|GET_REPORT_INSTANCES
case|:
name|processGetReportInstances
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
expr_stmt|;
break|break;
case|case
name|GET_REPORT_RESULTS
case|:
name|processGetReportResults
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|SalesforceException
argument_list|(
literal|"Unknown operation name: "
operator|+
name|operationName
operator|.
name|value
argument_list|()
argument_list|,
literal|null
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|SalesforceException
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|SalesforceException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Error processing %s: [%s] \"%s\""
argument_list|,
name|operationName
operator|.
name|value
argument_list|()
argument_list|,
name|e
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|done
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|SalesforceException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Unexpected Error processing %s: \"%s\""
argument_list|,
name|operationName
operator|.
name|value
argument_list|()
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|done
operator|=
literal|true
expr_stmt|;
block|}
comment|// continue routing asynchronously if false
return|return
name|done
return|;
block|}
DECL|method|processGetRecentReports (final Exchange exchange, final AsyncCallback callback)
specifier|private
name|void
name|processGetRecentReports
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|analyticsClient
operator|.
name|getRecentReports
argument_list|(
name|determineHeaders
argument_list|(
name|exchange
argument_list|)
argument_list|,
operator|new
name|AnalyticsApiClient
operator|.
name|RecentReportsResponseCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onResponse
parameter_list|(
name|List
argument_list|<
name|RecentReport
argument_list|>
name|reportDescription
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|,
name|SalesforceException
name|ex
parameter_list|)
block|{
name|processResponse
argument_list|(
name|exchange
argument_list|,
name|reportDescription
argument_list|,
name|headers
argument_list|,
name|ex
argument_list|,
name|callback
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|processGetReportDescription (final Exchange exchange, final AsyncCallback callback)
specifier|private
name|void
name|processGetReportDescription
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
throws|throws
name|SalesforceException
block|{
specifier|final
name|String
name|reportId
init|=
name|getParameter
argument_list|(
name|REPORT_ID
argument_list|,
name|exchange
argument_list|,
name|USE_BODY
argument_list|,
name|NOT_OPTIONAL
argument_list|)
decl_stmt|;
name|analyticsClient
operator|.
name|getReportDescription
argument_list|(
name|reportId
argument_list|,
name|determineHeaders
argument_list|(
name|exchange
argument_list|)
argument_list|,
operator|new
name|AnalyticsApiClient
operator|.
name|ReportDescriptionResponseCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onResponse
parameter_list|(
name|ReportDescription
name|reportDescription
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|,
name|SalesforceException
name|ex
parameter_list|)
block|{
name|processResponse
argument_list|(
name|exchange
argument_list|,
name|reportDescription
argument_list|,
name|headers
argument_list|,
name|ex
argument_list|,
name|callback
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|processExecuteSyncReport (final Exchange exchange, final AsyncCallback callback)
specifier|private
name|void
name|processExecuteSyncReport
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
throws|throws
name|SalesforceException
block|{
name|String
name|reportId
decl_stmt|;
specifier|final
name|Boolean
name|includeDetails
init|=
name|getParameter
argument_list|(
name|INCLUDE_DETAILS
argument_list|,
name|exchange
argument_list|,
name|IGNORE_BODY
argument_list|,
name|IS_OPTIONAL
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// try getting report metadata from body first
name|ReportMetadata
name|reportMetadata
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|ReportMetadata
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|reportMetadata
operator|!=
literal|null
condition|)
block|{
name|reportId
operator|=
name|reportMetadata
operator|.
name|getId
argument_list|()
expr_stmt|;
if|if
condition|(
name|reportId
operator|==
literal|null
condition|)
block|{
name|reportId
operator|=
name|getParameter
argument_list|(
name|REPORT_ID
argument_list|,
name|exchange
argument_list|,
name|IGNORE_BODY
argument_list|,
name|NOT_OPTIONAL
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|reportId
operator|=
name|getParameter
argument_list|(
name|REPORT_ID
argument_list|,
name|exchange
argument_list|,
name|USE_BODY
argument_list|,
name|NOT_OPTIONAL
argument_list|)
expr_stmt|;
name|reportMetadata
operator|=
name|getParameter
argument_list|(
name|REPORT_METADATA
argument_list|,
name|exchange
argument_list|,
name|IGNORE_BODY
argument_list|,
name|IS_OPTIONAL
argument_list|,
name|ReportMetadata
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
name|analyticsClient
operator|.
name|executeSyncReport
argument_list|(
name|reportId
argument_list|,
name|includeDetails
argument_list|,
name|reportMetadata
argument_list|,
name|determineHeaders
argument_list|(
name|exchange
argument_list|)
argument_list|,
operator|new
name|AnalyticsApiClient
operator|.
name|ReportResultsResponseCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onResponse
parameter_list|(
name|AbstractReportResultsBase
name|reportResults
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|,
name|SalesforceException
name|ex
parameter_list|)
block|{
name|processResponse
argument_list|(
name|exchange
argument_list|,
name|reportResults
argument_list|,
name|headers
argument_list|,
name|ex
argument_list|,
name|callback
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|processExecuteAsyncReport (final Exchange exchange, final AsyncCallback callback)
specifier|private
name|void
name|processExecuteAsyncReport
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
throws|throws
name|SalesforceException
block|{
name|String
name|reportId
decl_stmt|;
specifier|final
name|Boolean
name|includeDetails
init|=
name|getParameter
argument_list|(
name|INCLUDE_DETAILS
argument_list|,
name|exchange
argument_list|,
name|IGNORE_BODY
argument_list|,
name|IS_OPTIONAL
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// try getting report metadata from body first
name|ReportMetadata
name|reportMetadata
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|ReportMetadata
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|reportMetadata
operator|!=
literal|null
condition|)
block|{
name|reportId
operator|=
name|reportMetadata
operator|.
name|getId
argument_list|()
expr_stmt|;
if|if
condition|(
name|reportId
operator|==
literal|null
condition|)
block|{
name|reportId
operator|=
name|getParameter
argument_list|(
name|REPORT_ID
argument_list|,
name|exchange
argument_list|,
name|IGNORE_BODY
argument_list|,
name|NOT_OPTIONAL
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|reportId
operator|=
name|getParameter
argument_list|(
name|REPORT_ID
argument_list|,
name|exchange
argument_list|,
name|USE_BODY
argument_list|,
name|NOT_OPTIONAL
argument_list|)
expr_stmt|;
name|reportMetadata
operator|=
name|getParameter
argument_list|(
name|REPORT_METADATA
argument_list|,
name|exchange
argument_list|,
name|IGNORE_BODY
argument_list|,
name|IS_OPTIONAL
argument_list|,
name|ReportMetadata
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
name|analyticsClient
operator|.
name|executeAsyncReport
argument_list|(
name|reportId
argument_list|,
name|includeDetails
argument_list|,
name|reportMetadata
argument_list|,
name|determineHeaders
argument_list|(
name|exchange
argument_list|)
argument_list|,
operator|new
name|AnalyticsApiClient
operator|.
name|ReportInstanceResponseCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onResponse
parameter_list|(
name|ReportInstance
name|reportInstance
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|,
name|SalesforceException
name|ex
parameter_list|)
block|{
name|processResponse
argument_list|(
name|exchange
argument_list|,
name|reportInstance
argument_list|,
name|headers
argument_list|,
name|ex
argument_list|,
name|callback
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|processGetReportInstances (final Exchange exchange, final AsyncCallback callback)
specifier|private
name|void
name|processGetReportInstances
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
throws|throws
name|SalesforceException
block|{
specifier|final
name|String
name|reportId
init|=
name|getParameter
argument_list|(
name|REPORT_ID
argument_list|,
name|exchange
argument_list|,
name|USE_BODY
argument_list|,
name|NOT_OPTIONAL
argument_list|)
decl_stmt|;
name|analyticsClient
operator|.
name|getReportInstances
argument_list|(
name|reportId
argument_list|,
name|determineHeaders
argument_list|(
name|exchange
argument_list|)
argument_list|,
operator|new
name|AnalyticsApiClient
operator|.
name|ReportInstanceListResponseCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onResponse
parameter_list|(
name|List
argument_list|<
name|ReportInstance
argument_list|>
name|reportInstances
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|,
name|SalesforceException
name|ex
parameter_list|)
block|{
name|processResponse
argument_list|(
name|exchange
argument_list|,
name|reportInstances
argument_list|,
name|headers
argument_list|,
name|ex
argument_list|,
name|callback
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|processGetReportResults (final Exchange exchange, final AsyncCallback callback)
specifier|private
name|void
name|processGetReportResults
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
throws|throws
name|SalesforceException
block|{
specifier|final
name|String
name|reportId
init|=
name|getParameter
argument_list|(
name|REPORT_ID
argument_list|,
name|exchange
argument_list|,
name|USE_BODY
argument_list|,
name|NOT_OPTIONAL
argument_list|)
decl_stmt|;
specifier|final
name|String
name|instanceId
init|=
name|getParameter
argument_list|(
name|INSTANCE_ID
argument_list|,
name|exchange
argument_list|,
name|IGNORE_BODY
argument_list|,
name|NOT_OPTIONAL
argument_list|)
decl_stmt|;
name|analyticsClient
operator|.
name|getReportResults
argument_list|(
name|reportId
argument_list|,
name|instanceId
argument_list|,
name|determineHeaders
argument_list|(
name|exchange
argument_list|)
argument_list|,
operator|new
name|AnalyticsApiClient
operator|.
name|ReportResultsResponseCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onResponse
parameter_list|(
name|AbstractReportResultsBase
name|reportResults
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|,
name|SalesforceException
name|ex
parameter_list|)
block|{
name|processResponse
argument_list|(
name|exchange
argument_list|,
name|reportResults
argument_list|,
name|headers
argument_list|,
name|ex
argument_list|,
name|callback
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|processResponse (Exchange exchange, Object body, Map<String, String> headers, SalesforceException ex, AsyncCallback callback)
specifier|private
name|void
name|processResponse
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|body
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|,
name|SalesforceException
name|ex
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
specifier|final
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
if|if
condition|(
name|ex
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|out
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
comment|// copy headers and attachments
specifier|final
name|Message
name|inboundMessage
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|outputHeaders
init|=
name|out
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|outputHeaders
operator|.
name|putAll
argument_list|(
name|inboundMessage
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|outputHeaders
operator|.
name|putAll
argument_list|(
name|headers
argument_list|)
expr_stmt|;
name|out
operator|.
name|copyAttachments
argument_list|(
name|inboundMessage
argument_list|)
expr_stmt|;
comment|// signal exchange completion
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|analyticsClient
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// stop the client
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|analyticsClient
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

