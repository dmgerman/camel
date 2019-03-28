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
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|TabularData
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|meta
operator|.
name|Experimental
import|;
end_import

begin_interface
DECL|interface|ManagedRouteMBean
specifier|public
interface|interface
name|ManagedRouteMBean
extends|extends
name|ManagedPerformanceCounterMBean
block|{
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Route ID"
argument_list|)
DECL|method|getRouteId ()
name|String
name|getRouteId
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Route Group"
argument_list|)
DECL|method|getRouteGroup ()
name|String
name|getRouteGroup
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Route Properties"
argument_list|)
DECL|method|getRouteProperties ()
name|TabularData
name|getRouteProperties
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Route Description"
argument_list|)
DECL|method|getDescription ()
name|String
name|getDescription
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Route Endpoint URI"
argument_list|,
name|mask
operator|=
literal|true
argument_list|)
DECL|method|getEndpointUri ()
name|String
name|getEndpointUri
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Route State"
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
literal|"Route Uptime [human readable text]"
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
literal|"Route Uptime [milliseconds]"
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
DECL|method|getCamelManagementName ()
name|String
name|getCamelManagementName
parameter_list|()
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
literal|"Route Policy List"
argument_list|)
DECL|method|getRoutePolicyList ()
name|String
name|getRoutePolicyList
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
literal|"Start route"
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
literal|"Stop route"
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
literal|"Stop route (using timeout in seconds)"
argument_list|)
DECL|method|stop (long timeout)
name|void
name|stop
parameter_list|(
name|long
name|timeout
parameter_list|)
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Stop route, abort stop after timeout (in seconds)"
argument_list|)
DECL|method|stop (Long timeout, Boolean abortAfterTimeout)
name|boolean
name|stop
parameter_list|(
name|Long
name|timeout
parameter_list|,
name|Boolean
name|abortAfterTimeout
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * @deprecated will be removed in the near future. Use stop and remove instead      */
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Shutdown route"
argument_list|)
annotation|@
name|Deprecated
DECL|method|shutdown ()
name|void
name|shutdown
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * @deprecated will be removed in the near future. Use stop and remove instead      */
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Shutdown route (using timeout in seconds)"
argument_list|)
annotation|@
name|Deprecated
DECL|method|shutdown (long timeout)
name|void
name|shutdown
parameter_list|(
name|long
name|timeout
parameter_list|)
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Remove route (must be stopped)"
argument_list|)
DECL|method|remove ()
name|boolean
name|remove
parameter_list|()
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Restarts route (1 second delay before starting)"
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
literal|"Restarts route (using delay in seconds before starting)"
argument_list|)
DECL|method|restart (long delay)
name|void
name|restart
parameter_list|(
name|long
name|delay
parameter_list|)
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Dumps the route as XML"
argument_list|)
DECL|method|dumpRouteAsXml ()
name|String
name|dumpRouteAsXml
parameter_list|()
throws|throws
name|Exception
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Dumps the route as XML"
argument_list|)
DECL|method|dumpRouteAsXml (boolean resolvePlaceholders)
name|String
name|dumpRouteAsXml
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
literal|"Dumps the route as XML"
argument_list|)
DECL|method|dumpRouteAsXml (boolean resolvePlaceholders, boolean resolveDelegateEndpoints)
name|String
name|dumpRouteAsXml
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
literal|"Updates the route from XML"
argument_list|)
DECL|method|updateRouteFromXml (String xml)
name|void
name|updateRouteFromXml
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
DECL|method|dumpRouteStatsAsXml (boolean fullStats, boolean includeProcessors)
name|String
name|dumpRouteStatsAsXml
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
literal|"Dumps the routes and steps stats as XML"
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
literal|"Reset counters"
argument_list|)
DECL|method|reset (boolean includeProcessors)
name|void
name|reset
parameter_list|(
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
literal|"Returns the JSON representation of all the static and dynamic endpoints defined in this route"
argument_list|)
DECL|method|createRouteStaticEndpointJson ()
name|String
name|createRouteStaticEndpointJson
parameter_list|()
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Returns the JSON representation of all the static endpoints (and possible dynamic) defined in this route"
argument_list|)
DECL|method|createRouteStaticEndpointJson (boolean includeDynamic)
name|String
name|createRouteStaticEndpointJson
parameter_list|(
name|boolean
name|includeDynamic
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Oldest inflight exchange duration"
argument_list|)
DECL|method|getOldestInflightDuration ()
name|Long
name|getOldestInflightDuration
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Oldest inflight exchange id"
argument_list|)
DECL|method|getOldestInflightExchangeId ()
name|String
name|getOldestInflightExchangeId
parameter_list|()
function_decl|;
annotation|@
name|Experimental
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Route controller"
argument_list|)
DECL|method|getHasRouteController ()
name|Boolean
name|getHasRouteController
parameter_list|()
function_decl|;
annotation|@
name|Experimental
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Last error"
argument_list|)
DECL|method|getLastError ()
name|RouteError
name|getLastError
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

