begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * Client interface for Analytics API.  */
end_comment

begin_interface
DECL|interface|AnalyticsApiClient
specifier|public
interface|interface
name|AnalyticsApiClient
block|{
comment|// Report operations
DECL|interface|RecentReportsResponseCallback
specifier|public
interface|interface
name|RecentReportsResponseCallback
block|{
DECL|method|onResponse (List<RecentReport> reportDescription, Map<String, String> headers, SalesforceException ex)
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
function_decl|;
block|}
DECL|interface|ReportDescriptionResponseCallback
specifier|public
interface|interface
name|ReportDescriptionResponseCallback
block|{
DECL|method|onResponse (ReportDescription reportDescription, Map<String, String> headers, SalesforceException ex)
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
function_decl|;
block|}
DECL|interface|ReportResultsResponseCallback
specifier|public
interface|interface
name|ReportResultsResponseCallback
block|{
DECL|method|onResponse (AbstractReportResultsBase reportResults, Map<String, String> headers, SalesforceException ex)
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
function_decl|;
block|}
DECL|interface|ReportInstanceResponseCallback
specifier|public
interface|interface
name|ReportInstanceResponseCallback
block|{
DECL|method|onResponse (ReportInstance reportInstance, Map<String, String> headers, SalesforceException ex)
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
function_decl|;
block|}
DECL|interface|ReportInstanceListResponseCallback
specifier|public
interface|interface
name|ReportInstanceListResponseCallback
block|{
DECL|method|onResponse (List<ReportInstance> reportInstances, Map<String, String> headers, SalesforceException ex)
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
function_decl|;
block|}
DECL|method|getRecentReports (Map<String, List<String>> headers, RecentReportsResponseCallback callback)
name|void
name|getRecentReports
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
parameter_list|,
name|RecentReportsResponseCallback
name|callback
parameter_list|)
function_decl|;
DECL|method|getReportDescription (String reportId, Map<String, List<String>> headers, ReportDescriptionResponseCallback callback)
name|void
name|getReportDescription
parameter_list|(
name|String
name|reportId
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
parameter_list|,
name|ReportDescriptionResponseCallback
name|callback
parameter_list|)
function_decl|;
DECL|method|executeSyncReport (String reportId, Boolean includeDetails, ReportMetadata reportFilter, Map<String, List<String>> headers, ReportResultsResponseCallback callback)
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
name|reportFilter
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
parameter_list|,
name|ReportResultsResponseCallback
name|callback
parameter_list|)
function_decl|;
DECL|method|executeAsyncReport (String reportId, Boolean includeDetails, ReportMetadata reportFilter, Map<String, List<String>> headers, ReportInstanceResponseCallback callback)
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
name|reportFilter
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
parameter_list|,
name|ReportInstanceResponseCallback
name|callback
parameter_list|)
function_decl|;
DECL|method|getReportInstances (String reportId, Map<String, List<String>> headers, ReportInstanceListResponseCallback callback)
name|void
name|getReportInstances
parameter_list|(
name|String
name|reportId
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
parameter_list|,
name|ReportInstanceListResponseCallback
name|callback
parameter_list|)
function_decl|;
DECL|method|getReportResults (String reportId, String instanceId, Map<String, List<String>> headers, ReportResultsResponseCallback callback)
name|void
name|getReportResults
parameter_list|(
name|String
name|reportId
parameter_list|,
name|String
name|instanceId
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
parameter_list|,
name|ReportResultsResponseCallback
name|callback
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

