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
name|IOException
import|;
end_import

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
name|io
operator|.
name|UnsupportedEncodingException
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
name|List
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|core
operator|.
name|type
operator|.
name|TypeReference
import|;
end_import

begin_import
import|import
name|com
operator|.
name|fasterxml
operator|.
name|jackson
operator|.
name|databind
operator|.
name|ObjectMapper
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
name|api
operator|.
name|dto
operator|.
name|RestError
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
name|AsyncReportResults
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
name|api
operator|.
name|dto
operator|.
name|analytics
operator|.
name|reports
operator|.
name|SyncReportResults
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
name|util
operator|.
name|BytesContentProvider
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
name|HttpHeader
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
name|HttpMethod
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
name|StringUtil
import|;
end_import

begin_comment
comment|/**  * Default implementation of {@link org.apache.camel.component.salesforce.internal.client.AnalyticsApiClient}.  */
end_comment

begin_class
DECL|class|DefaultAnalyticsApiClient
specifier|public
class|class
name|DefaultAnalyticsApiClient
extends|extends
name|AbstractClientBase
implements|implements
name|AnalyticsApiClient
block|{
DECL|field|TOKEN_PREFIX
specifier|private
specifier|static
specifier|final
name|String
name|TOKEN_PREFIX
init|=
literal|"Bearer "
decl_stmt|;
DECL|field|INCLUDE_DETAILS_QUERY_PARAM
specifier|private
specifier|static
specifier|final
name|String
name|INCLUDE_DETAILS_QUERY_PARAM
init|=
literal|"?includeDetails="
decl_stmt|;
DECL|field|objectMapper
specifier|private
name|ObjectMapper
name|objectMapper
decl_stmt|;
DECL|method|DefaultAnalyticsApiClient (String version, SalesforceSession session, SalesforceHttpClient httpClient)
specifier|public
name|DefaultAnalyticsApiClient
parameter_list|(
name|String
name|version
parameter_list|,
name|SalesforceSession
name|session
parameter_list|,
name|SalesforceHttpClient
name|httpClient
parameter_list|)
throws|throws
name|SalesforceException
block|{
name|super
argument_list|(
name|version
argument_list|,
name|session
argument_list|,
name|httpClient
argument_list|)
expr_stmt|;
name|objectMapper
operator|=
name|JsonUtils
operator|.
name|createObjectMapper
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getRecentReports (final RecentReportsResponseCallback callback)
specifier|public
name|void
name|getRecentReports
parameter_list|(
specifier|final
name|RecentReportsResponseCallback
name|callback
parameter_list|)
block|{
specifier|final
name|Request
name|request
init|=
name|getRequest
argument_list|(
name|HttpMethod
operator|.
name|GET
argument_list|,
name|reportsUrl
argument_list|()
argument_list|)
decl_stmt|;
name|doHttpRequest
argument_list|(
name|request
argument_list|,
operator|new
name|ClientResponseCallback
argument_list|()
block|{
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|void
name|onResponse
parameter_list|(
name|InputStream
name|response
parameter_list|,
name|SalesforceException
name|ex
parameter_list|)
block|{
name|List
argument_list|<
name|RecentReport
argument_list|>
name|recentReports
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|response
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|recentReports
operator|=
name|unmarshalResponse
argument_list|(
name|response
argument_list|,
name|request
argument_list|,
operator|new
name|TypeReference
argument_list|<
name|List
argument_list|<
name|RecentReport
argument_list|>
argument_list|>
argument_list|()
block|{                             }
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SalesforceException
name|e
parameter_list|)
block|{
name|ex
operator|=
name|e
expr_stmt|;
block|}
block|}
name|callback
operator|.
name|onResponse
argument_list|(
name|recentReports
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getReportDescription (String reportId, final ReportDescriptionResponseCallback callback)
specifier|public
name|void
name|getReportDescription
parameter_list|(
name|String
name|reportId
parameter_list|,
specifier|final
name|ReportDescriptionResponseCallback
name|callback
parameter_list|)
block|{
specifier|final
name|Request
name|request
init|=
name|getRequest
argument_list|(
name|HttpMethod
operator|.
name|GET
argument_list|,
name|reportsDescribeUrl
argument_list|(
name|reportId
argument_list|)
argument_list|)
decl_stmt|;
name|doHttpRequest
argument_list|(
name|request
argument_list|,
operator|new
name|ClientResponseCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onResponse
parameter_list|(
name|InputStream
name|response
parameter_list|,
name|SalesforceException
name|ex
parameter_list|)
block|{
name|ReportDescription
name|reportDescription
init|=
literal|null
decl_stmt|;
try|try
block|{
name|reportDescription
operator|=
name|unmarshalResponse
argument_list|(
name|response
argument_list|,
name|request
argument_list|,
name|ReportDescription
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SalesforceException
name|e
parameter_list|)
block|{
name|ex
operator|=
name|e
expr_stmt|;
block|}
name|callback
operator|.
name|onResponse
argument_list|(
name|reportDescription
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|executeSyncReport (String reportId, Boolean includeDetails, ReportMetadata reportMetadata, final ReportResultsResponseCallback callback)
specifier|public
name|void
name|executeSyncReport
parameter_list|(
name|String
name|reportId
parameter_list|,
name|Boolean
name|includeDetails
parameter_list|,
name|ReportMetadata
name|reportMetadata
parameter_list|,
specifier|final
name|ReportResultsResponseCallback
name|callback
parameter_list|)
block|{
specifier|final
name|boolean
name|useGet
init|=
name|reportMetadata
operator|==
literal|null
decl_stmt|;
specifier|final
name|Request
name|request
init|=
name|getRequest
argument_list|(
name|useGet
condition|?
name|HttpMethod
operator|.
name|GET
else|:
name|HttpMethod
operator|.
name|POST
argument_list|,
name|reportsUrl
argument_list|(
name|reportId
argument_list|,
name|includeDetails
argument_list|)
argument_list|)
decl_stmt|;
comment|// set POST data
if|if
condition|(
operator|!
name|useGet
condition|)
block|{
try|try
block|{
comment|// wrap reportMetadata in a map
specifier|final
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|input
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|input
operator|.
name|put
argument_list|(
literal|"reportMetadata"
argument_list|,
name|reportMetadata
argument_list|)
expr_stmt|;
name|marshalRequest
argument_list|(
name|input
argument_list|,
name|request
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SalesforceException
name|e
parameter_list|)
block|{
name|callback
operator|.
name|onResponse
argument_list|(
literal|null
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
name|doHttpRequest
argument_list|(
name|request
argument_list|,
operator|new
name|ClientResponseCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onResponse
parameter_list|(
name|InputStream
name|response
parameter_list|,
name|SalesforceException
name|ex
parameter_list|)
block|{
name|SyncReportResults
name|reportResults
init|=
literal|null
decl_stmt|;
try|try
block|{
name|reportResults
operator|=
name|unmarshalResponse
argument_list|(
name|response
argument_list|,
name|request
argument_list|,
name|SyncReportResults
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SalesforceException
name|e
parameter_list|)
block|{
name|ex
operator|=
name|e
expr_stmt|;
block|}
name|callback
operator|.
name|onResponse
argument_list|(
name|reportResults
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|executeAsyncReport (String reportId, Boolean includeDetails, ReportMetadata reportMetadata, final ReportInstanceResponseCallback callback)
specifier|public
name|void
name|executeAsyncReport
parameter_list|(
name|String
name|reportId
parameter_list|,
name|Boolean
name|includeDetails
parameter_list|,
name|ReportMetadata
name|reportMetadata
parameter_list|,
specifier|final
name|ReportInstanceResponseCallback
name|callback
parameter_list|)
block|{
specifier|final
name|Request
name|request
init|=
name|getRequest
argument_list|(
name|HttpMethod
operator|.
name|POST
argument_list|,
name|reportInstancesUrl
argument_list|(
name|reportId
argument_list|,
name|includeDetails
argument_list|)
argument_list|)
decl_stmt|;
comment|// set POST data
if|if
condition|(
name|reportMetadata
operator|!=
literal|null
condition|)
block|{
try|try
block|{
comment|// wrap reportMetadata in a map
specifier|final
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|input
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|input
operator|.
name|put
argument_list|(
literal|"reportMetadata"
argument_list|,
name|reportMetadata
argument_list|)
expr_stmt|;
name|marshalRequest
argument_list|(
name|input
argument_list|,
name|request
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SalesforceException
name|e
parameter_list|)
block|{
name|callback
operator|.
name|onResponse
argument_list|(
literal|null
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
name|doHttpRequest
argument_list|(
name|request
argument_list|,
operator|new
name|ClientResponseCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onResponse
parameter_list|(
name|InputStream
name|response
parameter_list|,
name|SalesforceException
name|ex
parameter_list|)
block|{
name|ReportInstance
name|reportInstance
init|=
literal|null
decl_stmt|;
try|try
block|{
name|reportInstance
operator|=
name|unmarshalResponse
argument_list|(
name|response
argument_list|,
name|request
argument_list|,
name|ReportInstance
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SalesforceException
name|e
parameter_list|)
block|{
name|ex
operator|=
name|e
expr_stmt|;
block|}
name|callback
operator|.
name|onResponse
argument_list|(
name|reportInstance
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getReportInstances (String reportId, final ReportInstanceListResponseCallback callback)
specifier|public
name|void
name|getReportInstances
parameter_list|(
name|String
name|reportId
parameter_list|,
specifier|final
name|ReportInstanceListResponseCallback
name|callback
parameter_list|)
block|{
specifier|final
name|Request
name|request
init|=
name|getRequest
argument_list|(
name|HttpMethod
operator|.
name|GET
argument_list|,
name|reportInstancesUrl
argument_list|(
name|reportId
argument_list|)
argument_list|)
decl_stmt|;
name|doHttpRequest
argument_list|(
name|request
argument_list|,
operator|new
name|ClientResponseCallback
argument_list|()
block|{
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|void
name|onResponse
parameter_list|(
name|InputStream
name|response
parameter_list|,
name|SalesforceException
name|ex
parameter_list|)
block|{
name|List
argument_list|<
name|ReportInstance
argument_list|>
name|reportInstances
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|response
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|reportInstances
operator|=
name|unmarshalResponse
argument_list|(
name|response
argument_list|,
name|request
argument_list|,
operator|new
name|TypeReference
argument_list|<
name|List
argument_list|<
name|ReportInstance
argument_list|>
argument_list|>
argument_list|()
block|{                             }
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SalesforceException
name|e
parameter_list|)
block|{
name|ex
operator|=
name|e
expr_stmt|;
block|}
block|}
name|callback
operator|.
name|onResponse
argument_list|(
name|reportInstances
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getReportResults (String reportId, String instanceId, final ReportResultsResponseCallback callback)
specifier|public
name|void
name|getReportResults
parameter_list|(
name|String
name|reportId
parameter_list|,
name|String
name|instanceId
parameter_list|,
specifier|final
name|ReportResultsResponseCallback
name|callback
parameter_list|)
block|{
specifier|final
name|Request
name|request
init|=
name|getRequest
argument_list|(
name|HttpMethod
operator|.
name|GET
argument_list|,
name|reportInstancesUrl
argument_list|(
name|reportId
argument_list|,
name|instanceId
argument_list|)
argument_list|)
decl_stmt|;
name|doHttpRequest
argument_list|(
name|request
argument_list|,
operator|new
name|ClientResponseCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onResponse
parameter_list|(
name|InputStream
name|response
parameter_list|,
name|SalesforceException
name|ex
parameter_list|)
block|{
name|AsyncReportResults
name|reportResults
init|=
literal|null
decl_stmt|;
try|try
block|{
name|reportResults
operator|=
name|unmarshalResponse
argument_list|(
name|response
argument_list|,
name|request
argument_list|,
name|AsyncReportResults
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SalesforceException
name|e
parameter_list|)
block|{
name|ex
operator|=
name|e
expr_stmt|;
block|}
name|callback
operator|.
name|onResponse
argument_list|(
name|reportResults
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|reportsUrl ()
specifier|private
name|String
name|reportsUrl
parameter_list|()
block|{
comment|// NOTE the prefix 'v' for the version number
return|return
name|instanceUrl
operator|+
literal|"/services/data/v"
operator|+
name|version
operator|+
literal|"/analytics/reports"
return|;
block|}
DECL|method|reportsDescribeUrl (String reportId)
specifier|private
name|String
name|reportsDescribeUrl
parameter_list|(
name|String
name|reportId
parameter_list|)
block|{
return|return
name|reportsUrl
argument_list|(
name|reportId
argument_list|)
operator|+
literal|"/describe"
return|;
block|}
DECL|method|reportsUrl (String reportId)
specifier|private
name|String
name|reportsUrl
parameter_list|(
name|String
name|reportId
parameter_list|)
block|{
return|return
name|reportsUrl
argument_list|()
operator|+
literal|"/"
operator|+
name|reportId
return|;
block|}
DECL|method|reportsUrl (String reportId, Boolean includeDetails)
specifier|private
name|String
name|reportsUrl
parameter_list|(
name|String
name|reportId
parameter_list|,
name|Boolean
name|includeDetails
parameter_list|)
block|{
return|return
name|includeDetails
operator|==
literal|null
condition|?
name|reportsUrl
argument_list|(
name|reportId
argument_list|)
else|:
name|reportsUrl
argument_list|(
name|reportId
argument_list|)
operator|+
name|INCLUDE_DETAILS_QUERY_PARAM
operator|+
name|includeDetails
return|;
block|}
DECL|method|reportInstancesUrl (String reportId)
specifier|private
name|String
name|reportInstancesUrl
parameter_list|(
name|String
name|reportId
parameter_list|)
block|{
return|return
name|reportsUrl
argument_list|(
name|reportId
argument_list|)
operator|+
literal|"/instances"
return|;
block|}
DECL|method|reportInstancesUrl (String reportId, Boolean includeDetails)
specifier|private
name|String
name|reportInstancesUrl
parameter_list|(
name|String
name|reportId
parameter_list|,
name|Boolean
name|includeDetails
parameter_list|)
block|{
return|return
name|includeDetails
operator|==
literal|null
condition|?
name|reportInstancesUrl
argument_list|(
name|reportId
argument_list|)
else|:
name|reportInstancesUrl
argument_list|(
name|reportId
argument_list|)
operator|+
name|INCLUDE_DETAILS_QUERY_PARAM
operator|+
name|includeDetails
return|;
block|}
DECL|method|reportInstancesUrl (String reportId, String instanceId)
specifier|private
name|String
name|reportInstancesUrl
parameter_list|(
name|String
name|reportId
parameter_list|,
name|String
name|instanceId
parameter_list|)
block|{
return|return
name|reportInstancesUrl
argument_list|(
name|reportId
argument_list|)
operator|+
literal|"/"
operator|+
name|instanceId
return|;
block|}
annotation|@
name|Override
DECL|method|setAccessToken (Request request)
specifier|protected
name|void
name|setAccessToken
parameter_list|(
name|Request
name|request
parameter_list|)
block|{
comment|// replace old token
name|request
operator|.
name|getHeaders
argument_list|()
operator|.
name|put
argument_list|(
name|HttpHeader
operator|.
name|AUTHORIZATION
argument_list|,
name|TOKEN_PREFIX
operator|+
name|accessToken
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRestException (Response response, InputStream responseContent)
specifier|protected
name|SalesforceException
name|createRestException
parameter_list|(
name|Response
name|response
parameter_list|,
name|InputStream
name|responseContent
parameter_list|)
block|{
specifier|final
name|int
name|statusCode
init|=
name|response
operator|.
name|getStatus
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
name|responseContent
operator|!=
literal|null
condition|)
block|{
comment|// unmarshal RestError
specifier|final
name|List
argument_list|<
name|RestError
argument_list|>
name|errors
init|=
name|objectMapper
operator|.
name|readValue
argument_list|(
name|responseContent
argument_list|,
operator|new
name|TypeReference
argument_list|<
name|List
argument_list|<
name|RestError
argument_list|>
argument_list|>
argument_list|()
block|{                     }
argument_list|)
decl_stmt|;
return|return
operator|new
name|SalesforceException
argument_list|(
name|errors
argument_list|,
name|statusCode
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
comment|// log and ignore
name|String
name|msg
init|=
literal|"Unexpected Error parsing JSON error response body + ["
operator|+
name|responseContent
operator|+
literal|"] : "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|log
operator|.
name|warn
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// log and ignore
name|String
name|msg
init|=
literal|"Unexpected Error parsing JSON error response body + ["
operator|+
name|responseContent
operator|+
literal|"] : "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|log
operator|.
name|warn
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
comment|// just report HTTP status info
name|String
name|message
init|=
name|String
operator|.
name|format
argument_list|(
literal|"Unexpected error: %s, with content: %s"
argument_list|,
name|response
operator|.
name|getReason
argument_list|()
argument_list|,
name|responseContent
argument_list|)
decl_stmt|;
return|return
operator|new
name|SalesforceException
argument_list|(
name|message
argument_list|,
name|statusCode
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|doHttpRequest (Request request, ClientResponseCallback callback)
specifier|protected
name|void
name|doHttpRequest
parameter_list|(
name|Request
name|request
parameter_list|,
name|ClientResponseCallback
name|callback
parameter_list|)
block|{
comment|// set access token for all requests
name|setAccessToken
argument_list|(
name|request
argument_list|)
expr_stmt|;
comment|// set request and response content type and charset, which is always JSON for analytics API
name|request
operator|.
name|header
argument_list|(
name|HttpHeader
operator|.
name|CONTENT_TYPE
argument_list|,
name|APPLICATION_JSON_UTF8
argument_list|)
expr_stmt|;
name|request
operator|.
name|header
argument_list|(
name|HttpHeader
operator|.
name|ACCEPT
argument_list|,
name|APPLICATION_JSON_UTF8
argument_list|)
expr_stmt|;
name|request
operator|.
name|header
argument_list|(
name|HttpHeader
operator|.
name|ACCEPT_CHARSET
argument_list|,
name|StringUtil
operator|.
name|__UTF8
argument_list|)
expr_stmt|;
name|super
operator|.
name|doHttpRequest
argument_list|(
name|request
argument_list|,
name|callback
argument_list|)
expr_stmt|;
block|}
DECL|method|marshalRequest (Object input, Request request)
specifier|private
name|void
name|marshalRequest
parameter_list|(
name|Object
name|input
parameter_list|,
name|Request
name|request
parameter_list|)
throws|throws
name|SalesforceException
block|{
try|try
block|{
name|request
operator|.
name|content
argument_list|(
operator|new
name|BytesContentProvider
argument_list|(
name|objectMapper
operator|.
name|writeValueAsBytes
argument_list|(
name|input
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SalesforceException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Error marshaling request for {%s:%s} : %s"
argument_list|,
name|request
operator|.
name|getMethod
argument_list|()
argument_list|,
name|request
operator|.
name|getURI
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
throw|;
block|}
block|}
DECL|method|unmarshalResponse (InputStream response, Request request, TypeReference<T> responseTypeReference)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|T
name|unmarshalResponse
parameter_list|(
name|InputStream
name|response
parameter_list|,
name|Request
name|request
parameter_list|,
name|TypeReference
argument_list|<
name|T
argument_list|>
name|responseTypeReference
parameter_list|)
throws|throws
name|SalesforceException
block|{
try|try
block|{
return|return
name|objectMapper
operator|.
name|readValue
argument_list|(
name|response
argument_list|,
name|responseTypeReference
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SalesforceException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Error unmarshaling response {%s:%s} : %s"
argument_list|,
name|request
operator|.
name|getMethod
argument_list|()
argument_list|,
name|request
operator|.
name|getURI
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
throw|;
block|}
block|}
DECL|method|unmarshalResponse (InputStream response, Request request, Class<T> responseClass)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|T
name|unmarshalResponse
parameter_list|(
name|InputStream
name|response
parameter_list|,
name|Request
name|request
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|responseClass
parameter_list|)
throws|throws
name|SalesforceException
block|{
if|if
condition|(
name|response
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
return|return
name|objectMapper
operator|.
name|readValue
argument_list|(
name|response
argument_list|,
name|responseClass
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|SalesforceException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Error unmarshaling response {%s:%s} : %s"
argument_list|,
name|request
operator|.
name|getMethod
argument_list|()
argument_list|,
name|request
operator|.
name|getURI
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
throw|;
block|}
block|}
block|}
end_class

end_unit

