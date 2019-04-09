begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.api.management.mbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|api
operator|.
name|management
operator|.
name|mbean
package|;
end_package

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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|api
operator|.
name|management
operator|.
name|ManagedAttribute
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
name|api
operator|.
name|management
operator|.
name|ManagedOperation
import|;
end_import

begin_interface
DECL|interface|ManagedCamelContextMBean
specifier|public
interface|interface
name|ManagedCamelContextMBean
extends|extends
name|ManagedPerformanceCounterMBean
block|{
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Camel ID"
argument_list|)
DECL|method|getCamelId ()
name|String
name|getCamelId
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Camel ManagementName"
argument_list|)
DECL|method|getManagementName ()
name|String
name|getManagementName
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Camel Version"
argument_list|)
DECL|method|getCamelVersion ()
name|String
name|getCamelVersion
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Camel State"
argument_list|)
DECL|method|getState ()
name|String
name|getState
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Uptime [human readable text]"
argument_list|)
DECL|method|getUptime ()
name|String
name|getUptime
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Uptime [milliseconds]"
argument_list|)
DECL|method|getUptimeMillis ()
name|long
name|getUptimeMillis
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Camel Management StatisticsLevel"
argument_list|)
DECL|method|getManagementStatisticsLevel ()
name|String
name|getManagementStatisticsLevel
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Camel Global Options"
argument_list|)
DECL|method|getGlobalOptions ()
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getGlobalOptions
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"ClassResolver class name"
argument_list|)
DECL|method|getClassResolver ()
name|String
name|getClassResolver
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"PackageScanClassResolver class name"
argument_list|)
DECL|method|getPackageScanClassResolver ()
name|String
name|getPackageScanClassResolver
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"ApplicationContext class name"
argument_list|)
DECL|method|getApplicationContextClassName ()
name|String
name|getApplicationContextClassName
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"HeadersMapFactory class name"
argument_list|)
DECL|method|getHeadersMapFactoryClassName ()
name|String
name|getHeadersMapFactoryClassName
parameter_list|()
function_decl|;
comment|/**      * Gets the value of a CamelContext global option      *      * @param key the global option key      * @return the global option value      * @throws Exception when an error occurred      */
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Gets the value of a Camel global option"
argument_list|)
DECL|method|getGlobalOption (String key)
name|String
name|getGlobalOption
parameter_list|(
name|String
name|key
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Sets the value of a CamelContext property name      *      * @param key the global option key      * @param value the global option value      * @throws Exception when an error occurred      */
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Sets the value of a Camel global option"
argument_list|)
DECL|method|setGlobalOption (String key, String value)
name|void
name|setGlobalOption
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Tracing"
argument_list|)
DECL|method|getTracing ()
name|Boolean
name|getTracing
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Tracing"
argument_list|)
DECL|method|setTracing (Boolean tracing)
name|void
name|setTracing
parameter_list|(
name|Boolean
name|tracing
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Total number of routes"
argument_list|)
DECL|method|getTotalRoutes ()
name|Integer
name|getTotalRoutes
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Current number of started routes"
argument_list|)
DECL|method|getStartedRoutes ()
name|Integer
name|getStartedRoutes
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Shutdown timeout"
argument_list|)
DECL|method|setTimeout (long timeout)
name|void
name|setTimeout
parameter_list|(
name|long
name|timeout
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Shutdown timeout"
argument_list|)
DECL|method|getTimeout ()
name|long
name|getTimeout
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Shutdown timeout time unit"
argument_list|)
DECL|method|setTimeUnit (TimeUnit timeUnit)
name|void
name|setTimeUnit
parameter_list|(
name|TimeUnit
name|timeUnit
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Shutdown timeout time unit"
argument_list|)
DECL|method|getTimeUnit ()
name|TimeUnit
name|getTimeUnit
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether to force shutdown now when a timeout occurred"
argument_list|)
DECL|method|setShutdownNowOnTimeout (boolean shutdownNowOnTimeout)
name|void
name|setShutdownNowOnTimeout
parameter_list|(
name|boolean
name|shutdownNowOnTimeout
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether to force shutdown now when a timeout occurred"
argument_list|)
DECL|method|isShutdownNowOnTimeout ()
name|boolean
name|isShutdownNowOnTimeout
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Average load over the last minute"
argument_list|)
DECL|method|getLoad01 ()
name|String
name|getLoad01
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Average load over the last five minutes"
argument_list|)
DECL|method|getLoad05 ()
name|String
name|getLoad05
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Average load over the last fifteen minutes"
argument_list|)
DECL|method|getLoad15 ()
name|String
name|getLoad15
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether breadcrumbs is in use"
argument_list|)
DECL|method|isUseBreadcrumb ()
name|boolean
name|isUseBreadcrumb
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether allowing access to the original message during routing"
argument_list|)
DECL|method|isAllowUseOriginalMessage ()
name|boolean
name|isAllowUseOriginalMessage
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether message history is enabled"
argument_list|)
DECL|method|isMessageHistory ()
name|boolean
name|isMessageHistory
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether security mask for Logging is enabled"
argument_list|)
DECL|method|isLogMask ()
name|boolean
name|isLogMask
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether MDC logging is supported"
argument_list|)
DECL|method|isUseMDCLogging ()
name|boolean
name|isUseMDCLogging
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether Message DataType is enabled"
argument_list|)
DECL|method|isUseDataType ()
name|boolean
name|isUseDataType
parameter_list|()
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Start Camel"
argument_list|)
DECL|method|start ()
name|void
name|start
parameter_list|()
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Stop Camel (shutdown)"
argument_list|)
DECL|method|stop ()
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Restart Camel (stop and then start)"
argument_list|)
DECL|method|restart ()
name|void
name|restart
parameter_list|()
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Suspend Camel"
argument_list|)
DECL|method|suspend ()
name|void
name|suspend
parameter_list|()
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Resume Camel"
argument_list|)
DECL|method|resume ()
name|void
name|resume
parameter_list|()
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Starts all the routes which currently is not started"
argument_list|)
DECL|method|startAllRoutes ()
name|void
name|startAllRoutes
parameter_list|()
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Whether its possible to send to the endpoint (eg the endpoint has a producer)"
argument_list|)
DECL|method|canSendToEndpoint (String endpointUri)
name|boolean
name|canSendToEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|)
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Send body (in only)"
argument_list|)
DECL|method|sendBody (String endpointUri, Object body)
name|void
name|sendBody
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Object
name|body
parameter_list|)
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Send body (String type) (in only)"
argument_list|)
DECL|method|sendStringBody (String endpointUri, String body)
name|void
name|sendStringBody
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|String
name|body
parameter_list|)
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Send body and headers (in only)"
argument_list|)
DECL|method|sendBodyAndHeaders (String endpointUri, Object body, Map<String, Object> headers)
name|void
name|sendBodyAndHeaders
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Object
name|body
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|)
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Request body (in out)"
argument_list|)
DECL|method|requestBody (String endpointUri, Object body)
name|Object
name|requestBody
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Object
name|body
parameter_list|)
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Request body (String type) (in out)"
argument_list|)
DECL|method|requestStringBody (String endpointUri, String body)
name|Object
name|requestStringBody
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|String
name|body
parameter_list|)
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Request body and headers (in out)"
argument_list|)
DECL|method|requestBodyAndHeaders (String endpointUri, Object body, Map<String, Object> headers)
name|Object
name|requestBodyAndHeaders
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Object
name|body
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|)
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Dumps the rests as XML"
argument_list|)
DECL|method|dumpRestsAsXml ()
name|String
name|dumpRestsAsXml
parameter_list|()
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Dumps the rests as XML"
argument_list|)
DECL|method|dumpRestsAsXml (boolean resolvePlaceholders)
name|String
name|dumpRestsAsXml
parameter_list|(
name|boolean
name|resolvePlaceholders
parameter_list|)
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Dumps the routes as XML"
argument_list|)
DECL|method|dumpRoutesAsXml ()
name|String
name|dumpRoutesAsXml
parameter_list|()
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Dumps the routes as XML"
argument_list|)
DECL|method|dumpRoutesAsXml (boolean resolvePlaceholders)
name|String
name|dumpRoutesAsXml
parameter_list|(
name|boolean
name|resolvePlaceholders
parameter_list|)
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Dumps the routes as XML"
argument_list|)
DECL|method|dumpRoutesAsXml (boolean resolvePlaceholders, boolean resolveDelegateEndpoints)
name|String
name|dumpRoutesAsXml
parameter_list|(
name|boolean
name|resolvePlaceholders
parameter_list|,
name|boolean
name|resolveDelegateEndpoints
parameter_list|)
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Adds or updates existing routes from XML"
argument_list|)
DECL|method|addOrUpdateRoutesFromXml (String xml)
name|void
name|addOrUpdateRoutesFromXml
parameter_list|(
name|String
name|xml
parameter_list|)
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Adds or updates existing routes from XML"
argument_list|)
DECL|method|addOrUpdateRoutesFromXml (String xml, boolean urlDecode)
name|void
name|addOrUpdateRoutesFromXml
parameter_list|(
name|String
name|xml
parameter_list|,
name|boolean
name|urlDecode
parameter_list|)
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Dumps the CamelContext and routes stats as XML"
argument_list|)
DECL|method|dumpRoutesStatsAsXml (boolean fullStats, boolean includeProcessors)
name|String
name|dumpRoutesStatsAsXml
parameter_list|(
name|boolean
name|fullStats
parameter_list|,
name|boolean
name|includeProcessors
parameter_list|)
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Dumps the CamelContext and routes and steps stats as XML"
argument_list|)
DECL|method|dumpStepStatsAsXml (boolean fullStats)
name|String
name|dumpStepStatsAsXml
parameter_list|(
name|boolean
name|fullStats
parameter_list|)
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Dumps the routes coverage as XML"
argument_list|)
DECL|method|dumpRoutesCoverageAsXml ()
name|String
name|dumpRoutesCoverageAsXml
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Creates the endpoint by the given uri      *      * @param uri uri of endpoint to create      * @return<tt>true</tt> if a new endpoint was created,<tt>false</tt> if the endpoint already existed      * @throws Exception is thrown if error occurred      */
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Creates the endpoint by the given URI"
argument_list|)
DECL|method|createEndpoint (String uri)
name|boolean
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Removes the endpoint by the given pattern      *      * @param pattern the pattern      * @return number of endpoints removed      * @throws Exception is thrown if error occurred      * @see org.apache.camel.CamelContext#removeEndpoints(String)      */
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Removes endpoints by the given pattern"
argument_list|)
DECL|method|removeEndpoints (String pattern)
name|int
name|removeEndpoints
parameter_list|(
name|String
name|pattern
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Returns the JSON schema representation with information about the component and the endpoint parameters it supports      *      * @param componentName the name of the component to lookup      * @throws Exception is thrown if error occurred      */
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Returns the JSON schema representation of the endpoint parameters for the given component name"
argument_list|)
DECL|method|componentParameterJsonSchema (String componentName)
name|String
name|componentParameterJsonSchema
parameter_list|(
name|String
name|componentName
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Returns the JSON schema representation with information about the data format and the parameters it supports      *      * @param dataFormatName the name of the data format to lookup      * @throws Exception is thrown if error occurred      */
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Returns the JSON schema representation of the data format parameters for the given data format name"
argument_list|)
DECL|method|dataFormatParameterJsonSchema (String dataFormatName)
name|String
name|dataFormatParameterJsonSchema
parameter_list|(
name|String
name|dataFormatName
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Returns the JSON schema representation with information about the language and the parameters it supports      *      * @param languageName the name of the language to lookup      * @throws Exception is thrown if error occurred      */
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Returns the JSON schema representation of the language parameters for the given language name"
argument_list|)
DECL|method|languageParameterJsonSchema (String languageName)
name|String
name|languageParameterJsonSchema
parameter_list|(
name|String
name|languageName
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Returns the JSON schema representation with information about the EIP and the parameters it supports      *      * @param eipName the name of the EIP to lookup      * @throws Exception is thrown if error occurred      */
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Returns the JSON schema representation of the EIP parameters for the given EIP name"
argument_list|)
DECL|method|eipParameterJsonSchema (String eipName)
name|String
name|eipParameterJsonSchema
parameter_list|(
name|String
name|eipName
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Resets all the performance counters.      *      * @param includeRoutes  whether to reset all routes as well.      * @throws Exception is thrown if error occurred      */
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Reset counters"
argument_list|)
DECL|method|reset (boolean includeRoutes)
name|void
name|reset
parameter_list|(
name|boolean
name|includeRoutes
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

