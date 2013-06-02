begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|Properties
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
literal|"Camel Management Name"
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
literal|"Uptime"
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
literal|"Camel Properties"
argument_list|)
DECL|method|getProperties ()
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getProperties
parameter_list|()
function_decl|;
comment|/**      * Gets the value of a CamelContext property name      *      * @param name the name of the property      * @return String the value of the property      * @throws Exception is thrown if error occurred      */
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Get the value of a Camel property"
argument_list|)
DECL|method|getProperty (String name)
name|String
name|getProperty
parameter_list|(
name|String
name|name
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Sets the value of a CamelContext property name      *      * @param name the name of the property      * @param value the new value of the property      * @throws Exception is thrown if error occurred      */
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Set the value of a Camel property"
argument_list|)
DECL|method|setProperty (String name, String value)
name|void
name|setProperty
parameter_list|(
name|String
name|name
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
literal|"Message History"
argument_list|)
DECL|method|getMessageHistory ()
name|Boolean
name|getMessageHistory
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Current number of inflight Exchanges"
argument_list|)
DECL|method|getInflightExchanges ()
name|Integer
name|getInflightExchanges
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
literal|"Dumps the routes stats as XML"
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
comment|/**      * Find information about all the Camel components available in the classpath and {@link org.apache.camel.spi.Registry}.      *      * @return a map with the component name, and value with component details.      * @throws Exception is thrown if error occurred      */
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Find all Camel components available in the classpath"
argument_list|)
DECL|method|findComponents ()
name|Map
argument_list|<
name|String
argument_list|,
name|Properties
argument_list|>
name|findComponents
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Find the names of all the Camel components available in the classpath and {@link org.apache.camel.spi.Registry}.      *      * @return a list with the names of the camel components      * @throws Exception is thrown if error occurred      */
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Find all Camel components names available in the classpath"
argument_list|)
DECL|method|findComponentNames ()
name|List
argument_list|<
name|String
argument_list|>
name|findComponentNames
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Returns the JSON schema representation of the endpoint parameters for the given component name      *      * @param componentName the name of the component to lookup      * @throws Exception is thrown if error occurred      */
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
comment|/**      * Helper method for tooling which returns the completion list of the endpoint path      * from the given endpoint name, properties and current path expression.      *<p/>      * For example if using the file endpoint, this should complete a list of files (rather like bash completion)      * or for an ActiveMQ component this should complete the list of queues or topics.      *      * @param componentName  the component name      * @param endpointParameters  parameters of the endpoint      * @param completionText  the entered text which we want to have completion suggestions for      * @throws Exception is thrown if error occurred      */
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Returns the list of available endpoint paths for the given component name, endpoint properties and completion text"
argument_list|)
DECL|method|completeEndpointPath (String componentName, Map<String, Object> endpointParameters, String completionText)
name|List
argument_list|<
name|String
argument_list|>
name|completeEndpointPath
parameter_list|(
name|String
name|componentName
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|endpointParameters
parameter_list|,
name|String
name|completionText
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

