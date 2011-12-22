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
block|}
end_interface

end_unit

