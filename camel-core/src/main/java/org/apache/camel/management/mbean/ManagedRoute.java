begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management.mbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
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
name|ArrayList
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
name|Comparator
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
name|Set
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
name|javax
operator|.
name|management
operator|.
name|AttributeValueExp
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServerInvocationHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|Query
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|QueryExp
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|StringValueExp
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
name|ManagementStatisticsLevel
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
name|Route
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
name|ServiceStatus
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
name|TimerListener
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
name|ManagedResource
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
name|mbean
operator|.
name|ManagedProcessorMBean
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
name|mbean
operator|.
name|ManagedRouteMBean
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
name|model
operator|.
name|ModelCamelContext
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
name|model
operator|.
name|ModelHelper
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
name|model
operator|.
name|RouteDefinition
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
name|spi
operator|.
name|RoutePolicy
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
name|ObjectHelper
import|;
end_import

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed Route"
argument_list|)
DECL|class|ManagedRoute
specifier|public
class|class
name|ManagedRoute
extends|extends
name|ManagedPerformanceCounter
implements|implements
name|TimerListener
implements|,
name|ManagedRouteMBean
block|{
DECL|field|VALUE_UNKNOWN
specifier|public
specifier|static
specifier|final
name|String
name|VALUE_UNKNOWN
init|=
literal|"Unknown"
decl_stmt|;
DECL|field|route
specifier|protected
specifier|final
name|Route
name|route
decl_stmt|;
DECL|field|description
specifier|protected
specifier|final
name|String
name|description
decl_stmt|;
DECL|field|context
specifier|protected
specifier|final
name|ModelCamelContext
name|context
decl_stmt|;
DECL|field|load
specifier|private
specifier|final
name|LoadTriplet
name|load
init|=
operator|new
name|LoadTriplet
argument_list|()
decl_stmt|;
DECL|method|ManagedRoute (ModelCamelContext context, Route route)
specifier|public
name|ManagedRoute
parameter_list|(
name|ModelCamelContext
name|context
parameter_list|,
name|Route
name|route
parameter_list|)
block|{
name|this
operator|.
name|route
operator|=
name|route
expr_stmt|;
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
name|this
operator|.
name|description
operator|=
name|route
operator|.
name|toString
argument_list|()
expr_stmt|;
name|boolean
name|enabled
init|=
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getStatisticsLevel
argument_list|()
operator|!=
name|ManagementStatisticsLevel
operator|.
name|Off
decl_stmt|;
name|setStatisticsEnabled
argument_list|(
name|enabled
argument_list|)
expr_stmt|;
block|}
DECL|method|getRoute ()
specifier|public
name|Route
name|getRoute
parameter_list|()
block|{
return|return
name|route
return|;
block|}
DECL|method|getContext ()
specifier|public
name|CamelContext
name|getContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
DECL|method|getRouteId ()
specifier|public
name|String
name|getRouteId
parameter_list|()
block|{
name|String
name|id
init|=
name|route
operator|.
name|getId
argument_list|()
decl_stmt|;
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
name|id
operator|=
name|VALUE_UNKNOWN
expr_stmt|;
block|}
return|return
name|id
return|;
block|}
DECL|method|getDescription ()
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
annotation|@
name|Override
DECL|method|getEndpointUri ()
specifier|public
name|String
name|getEndpointUri
parameter_list|()
block|{
if|if
condition|(
name|route
operator|.
name|getEndpoint
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|route
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
return|;
block|}
return|return
name|VALUE_UNKNOWN
return|;
block|}
DECL|method|getState ()
specifier|public
name|String
name|getState
parameter_list|()
block|{
comment|// must use String type to be sure remote JMX can read the attribute without requiring Camel classes.
name|ServiceStatus
name|status
init|=
name|context
operator|.
name|getRouteStatus
argument_list|(
name|route
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
comment|// if no status exists then its stopped
if|if
condition|(
name|status
operator|==
literal|null
condition|)
block|{
name|status
operator|=
name|ServiceStatus
operator|.
name|Stopped
expr_stmt|;
block|}
return|return
name|status
operator|.
name|name
argument_list|()
return|;
block|}
DECL|method|getInflightExchanges ()
specifier|public
name|Integer
name|getInflightExchanges
parameter_list|()
block|{
return|return
name|context
operator|.
name|getInflightRepository
argument_list|()
operator|.
name|size
argument_list|(
name|route
operator|.
name|getId
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getCamelId ()
specifier|public
name|String
name|getCamelId
parameter_list|()
block|{
return|return
name|context
operator|.
name|getName
argument_list|()
return|;
block|}
DECL|method|getCamelManagementName ()
specifier|public
name|String
name|getCamelManagementName
parameter_list|()
block|{
return|return
name|context
operator|.
name|getManagementName
argument_list|()
return|;
block|}
DECL|method|getTracing ()
specifier|public
name|Boolean
name|getTracing
parameter_list|()
block|{
return|return
name|route
operator|.
name|getRouteContext
argument_list|()
operator|.
name|isTracing
argument_list|()
return|;
block|}
DECL|method|setTracing (Boolean tracing)
specifier|public
name|void
name|setTracing
parameter_list|(
name|Boolean
name|tracing
parameter_list|)
block|{
name|route
operator|.
name|getRouteContext
argument_list|()
operator|.
name|setTracing
argument_list|(
name|tracing
argument_list|)
expr_stmt|;
block|}
DECL|method|getMessageHistory ()
specifier|public
name|Boolean
name|getMessageHistory
parameter_list|()
block|{
return|return
name|route
operator|.
name|getRouteContext
argument_list|()
operator|.
name|isMessageHistory
argument_list|()
return|;
block|}
DECL|method|getRoutePolicyList ()
specifier|public
name|String
name|getRoutePolicyList
parameter_list|()
block|{
name|List
argument_list|<
name|RoutePolicy
argument_list|>
name|policyList
init|=
name|route
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getRoutePolicyList
argument_list|()
decl_stmt|;
if|if
condition|(
name|policyList
operator|==
literal|null
operator|||
name|policyList
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// return an empty string to have it displayed nicely in JMX consoles
return|return
literal|""
return|;
block|}
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|policyList
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|RoutePolicy
name|policy
init|=
name|policyList
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|policy
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"("
argument_list|)
operator|.
name|append
argument_list|(
name|ObjectHelper
operator|.
name|getIdentityHashCode
argument_list|(
name|policy
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|")"
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|<
name|policyList
operator|.
name|size
argument_list|()
operator|-
literal|1
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|getLoad01 ()
specifier|public
name|String
name|getLoad01
parameter_list|()
block|{
name|double
name|load1
init|=
name|load
operator|.
name|getLoad1
argument_list|()
decl_stmt|;
if|if
condition|(
name|Double
operator|.
name|isNaN
argument_list|(
name|load1
argument_list|)
condition|)
block|{
comment|// empty string if load statistics is disabled
return|return
literal|""
return|;
block|}
else|else
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%.2f"
argument_list|,
name|load1
argument_list|)
return|;
block|}
block|}
DECL|method|getLoad05 ()
specifier|public
name|String
name|getLoad05
parameter_list|()
block|{
name|double
name|load5
init|=
name|load
operator|.
name|getLoad5
argument_list|()
decl_stmt|;
if|if
condition|(
name|Double
operator|.
name|isNaN
argument_list|(
name|load5
argument_list|)
condition|)
block|{
comment|// empty string if load statistics is disabled
return|return
literal|""
return|;
block|}
else|else
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%.2f"
argument_list|,
name|load5
argument_list|)
return|;
block|}
block|}
DECL|method|getLoad15 ()
specifier|public
name|String
name|getLoad15
parameter_list|()
block|{
name|double
name|load15
init|=
name|load
operator|.
name|getLoad15
argument_list|()
decl_stmt|;
if|if
condition|(
name|Double
operator|.
name|isNaN
argument_list|(
name|load15
argument_list|)
condition|)
block|{
comment|// empty string if load statistics is disabled
return|return
literal|""
return|;
block|}
else|else
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%.2f"
argument_list|,
name|load15
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|onTimer ()
specifier|public
name|void
name|onTimer
parameter_list|()
block|{
name|load
operator|.
name|update
argument_list|(
name|getInflightExchanges
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|context
operator|.
name|getStatus
argument_list|()
operator|.
name|isStarted
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"CamelContext is not started"
argument_list|)
throw|;
block|}
name|context
operator|.
name|startRoute
argument_list|(
name|getRouteId
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|context
operator|.
name|getStatus
argument_list|()
operator|.
name|isStarted
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"CamelContext is not started"
argument_list|)
throw|;
block|}
name|context
operator|.
name|stopRoute
argument_list|(
name|getRouteId
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|stop (long timeout)
specifier|public
name|void
name|stop
parameter_list|(
name|long
name|timeout
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|context
operator|.
name|getStatus
argument_list|()
operator|.
name|isStarted
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"CamelContext is not started"
argument_list|)
throw|;
block|}
name|context
operator|.
name|stopRoute
argument_list|(
name|getRouteId
argument_list|()
argument_list|,
name|timeout
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
DECL|method|stop (Long timeout, Boolean abortAfterTimeout)
specifier|public
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
block|{
if|if
condition|(
operator|!
name|context
operator|.
name|getStatus
argument_list|()
operator|.
name|isStarted
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"CamelContext is not started"
argument_list|)
throw|;
block|}
return|return
name|context
operator|.
name|stopRoute
argument_list|(
name|getRouteId
argument_list|()
argument_list|,
name|timeout
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|,
name|abortAfterTimeout
argument_list|)
return|;
block|}
DECL|method|shutdown ()
specifier|public
name|void
name|shutdown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|context
operator|.
name|getStatus
argument_list|()
operator|.
name|isStarted
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"CamelContext is not started"
argument_list|)
throw|;
block|}
name|String
name|routeId
init|=
name|getRouteId
argument_list|()
decl_stmt|;
name|context
operator|.
name|stopRoute
argument_list|(
name|routeId
argument_list|)
expr_stmt|;
name|context
operator|.
name|removeRoute
argument_list|(
name|routeId
argument_list|)
expr_stmt|;
block|}
DECL|method|shutdown (long timeout)
specifier|public
name|void
name|shutdown
parameter_list|(
name|long
name|timeout
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|context
operator|.
name|getStatus
argument_list|()
operator|.
name|isStarted
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"CamelContext is not started"
argument_list|)
throw|;
block|}
name|String
name|routeId
init|=
name|getRouteId
argument_list|()
decl_stmt|;
name|context
operator|.
name|stopRoute
argument_list|(
name|routeId
argument_list|,
name|timeout
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
name|context
operator|.
name|removeRoute
argument_list|(
name|routeId
argument_list|)
expr_stmt|;
block|}
DECL|method|remove ()
specifier|public
name|boolean
name|remove
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|context
operator|.
name|getStatus
argument_list|()
operator|.
name|isStarted
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"CamelContext is not started"
argument_list|)
throw|;
block|}
return|return
name|context
operator|.
name|removeRoute
argument_list|(
name|getRouteId
argument_list|()
argument_list|)
return|;
block|}
DECL|method|dumpRouteAsXml ()
specifier|public
name|String
name|dumpRouteAsXml
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|id
init|=
name|route
operator|.
name|getId
argument_list|()
decl_stmt|;
name|RouteDefinition
name|def
init|=
name|context
operator|.
name|getRouteDefinition
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|def
operator|!=
literal|null
condition|)
block|{
return|return
name|ModelHelper
operator|.
name|dumpModelAsXml
argument_list|(
name|def
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|updateRouteFromXml (String xml)
specifier|public
name|void
name|updateRouteFromXml
parameter_list|(
name|String
name|xml
parameter_list|)
throws|throws
name|Exception
block|{
comment|// convert to model from xml
name|RouteDefinition
name|def
init|=
name|ModelHelper
operator|.
name|createModelFromXml
argument_list|(
name|xml
argument_list|,
name|RouteDefinition
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|def
operator|==
literal|null
condition|)
block|{
return|return;
block|}
comment|// if the xml does not contain the route-id then we fix this by adding the actual route id
comment|// this may be needed if the route-id was auto-generated, as the intend is to update this route
comment|// and not add a new route, adding a new route, use the MBean operation on ManagedCamelContext instead.
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|def
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
name|def
operator|.
name|setId
argument_list|(
name|getRouteId
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|def
operator|.
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|getRouteId
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot update route from XML as routeIds does not match. routeId: "
operator|+
name|getRouteId
argument_list|()
operator|+
literal|", routeId from XML: "
operator|+
name|def
operator|.
name|getId
argument_list|()
argument_list|)
throw|;
block|}
comment|// add will remove existing route first
name|context
operator|.
name|addRouteDefinition
argument_list|(
name|def
argument_list|)
expr_stmt|;
block|}
DECL|method|dumpRouteStatsAsXml (boolean fullStats, boolean includeProcessors)
specifier|public
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
block|{
comment|// in this logic we need to calculate the accumulated processing time for the processor in the route
comment|// and hence why the logic is a bit more complicated to do this, as we need to calculate that from
comment|// the bottom -> top of the route but this information is valuable for profiling routes
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
comment|// need to calculate this value first, as we need that value for the route stat
name|Long
name|processorAccumulatedTime
init|=
literal|0L
decl_stmt|;
comment|// gather all the processors for this route, which requires JMX
if|if
condition|(
name|includeProcessors
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"<processorStats>\n"
argument_list|)
expr_stmt|;
name|MBeanServer
name|server
init|=
name|getContext
argument_list|()
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementAgent
argument_list|()
operator|.
name|getMBeanServer
argument_list|()
decl_stmt|;
if|if
condition|(
name|server
operator|!=
literal|null
condition|)
block|{
comment|// get all the processor mbeans and sort them accordingly to their index
name|String
name|prefix
init|=
name|getContext
argument_list|()
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementAgent
argument_list|()
operator|.
name|getIncludeHostName
argument_list|()
condition|?
literal|"*/"
else|:
literal|""
decl_stmt|;
name|ObjectName
name|query
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context="
operator|+
name|prefix
operator|+
name|getContext
argument_list|()
operator|.
name|getManagementName
argument_list|()
operator|+
literal|",type=processors,*"
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|names
init|=
name|server
operator|.
name|queryNames
argument_list|(
name|query
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ManagedProcessorMBean
argument_list|>
name|mps
init|=
operator|new
name|ArrayList
argument_list|<
name|ManagedProcessorMBean
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|ObjectName
name|on
range|:
name|names
control|)
block|{
name|ManagedProcessorMBean
name|processor
init|=
name|MBeanServerInvocationHandler
operator|.
name|newProxyInstance
argument_list|(
name|server
argument_list|,
name|on
argument_list|,
name|ManagedProcessorMBean
operator|.
name|class
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// the processor must belong to this route
if|if
condition|(
name|getRouteId
argument_list|()
operator|.
name|equals
argument_list|(
name|processor
operator|.
name|getRouteId
argument_list|()
argument_list|)
condition|)
block|{
name|mps
operator|.
name|add
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|mps
argument_list|,
operator|new
name|OrderProcessorMBeans
argument_list|()
argument_list|)
expr_stmt|;
comment|// walk the processors in reverse order, and calculate the accumulated total time
name|Map
argument_list|<
name|String
argument_list|,
name|Long
argument_list|>
name|accumulatedTimes
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Long
argument_list|>
argument_list|()
decl_stmt|;
name|Collections
operator|.
name|reverse
argument_list|(
name|mps
argument_list|)
expr_stmt|;
for|for
control|(
name|ManagedProcessorMBean
name|processor
range|:
name|mps
control|)
block|{
name|processorAccumulatedTime
operator|+=
name|processor
operator|.
name|getTotalProcessingTime
argument_list|()
expr_stmt|;
name|accumulatedTimes
operator|.
name|put
argument_list|(
name|processor
operator|.
name|getProcessorId
argument_list|()
argument_list|,
name|processorAccumulatedTime
argument_list|)
expr_stmt|;
block|}
comment|// and reverse back again
name|Collections
operator|.
name|reverse
argument_list|(
name|mps
argument_list|)
expr_stmt|;
comment|// and now add the sorted list of processors to the xml output
for|for
control|(
name|ManagedProcessorMBean
name|processor
range|:
name|mps
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"<processorStat"
argument_list|)
operator|.
name|append
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|" id=\"%s\" index=\"%s\" state=\"%s\""
argument_list|,
name|processor
operator|.
name|getProcessorId
argument_list|()
argument_list|,
name|processor
operator|.
name|getIndex
argument_list|()
argument_list|,
name|processor
operator|.
name|getState
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// do we have an accumulated time then append that
name|Long
name|accTime
init|=
name|accumulatedTimes
operator|.
name|get
argument_list|(
name|processor
operator|.
name|getProcessorId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|accTime
operator|!=
literal|null
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" accumulatedProcessingTime=\""
argument_list|)
operator|.
name|append
argument_list|(
name|accTime
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
block|}
comment|// use substring as we only want the attributes
name|sb
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
operator|.
name|append
argument_list|(
name|processor
operator|.
name|dumpStatsAsXml
argument_list|(
name|fullStats
argument_list|)
operator|.
name|substring
argument_list|(
literal|7
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
block|}
block|}
name|sb
operator|.
name|append
argument_list|(
literal|"</processorStats>\n"
argument_list|)
expr_stmt|;
block|}
comment|// route self time is route total - processor accumulated total)
name|long
name|routeSelfTime
init|=
name|getTotalProcessingTime
argument_list|()
operator|-
name|processorAccumulatedTime
decl_stmt|;
if|if
condition|(
name|routeSelfTime
operator|<
literal|0
condition|)
block|{
comment|// ensure we don't calculate that as negative
name|routeSelfTime
operator|=
literal|0
expr_stmt|;
block|}
name|StringBuilder
name|answer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|answer
operator|.
name|append
argument_list|(
literal|"<routeStat"
argument_list|)
operator|.
name|append
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|" id=\"%s\""
argument_list|,
name|route
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|" state=\"%s\""
argument_list|,
name|getState
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// use substring as we only want the attributes
name|String
name|stat
init|=
name|dumpStatsAsXml
argument_list|(
name|fullStats
argument_list|)
decl_stmt|;
name|answer
operator|.
name|append
argument_list|(
literal|" exchangesInflight=\""
argument_list|)
operator|.
name|append
argument_list|(
name|getInflightExchanges
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
name|answer
operator|.
name|append
argument_list|(
literal|" selfProcessingTime=\""
argument_list|)
operator|.
name|append
argument_list|(
name|routeSelfTime
argument_list|)
operator|.
name|append
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
name|answer
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
operator|.
name|append
argument_list|(
name|stat
operator|.
name|substring
argument_list|(
literal|7
argument_list|,
name|stat
operator|.
name|length
argument_list|()
operator|-
literal|2
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|">\n"
argument_list|)
expr_stmt|;
if|if
condition|(
name|includeProcessors
condition|)
block|{
name|answer
operator|.
name|append
argument_list|(
name|sb
argument_list|)
expr_stmt|;
block|}
name|answer
operator|.
name|append
argument_list|(
literal|"</routeStat>"
argument_list|)
expr_stmt|;
return|return
name|answer
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|reset (boolean includeProcessors)
specifier|public
name|void
name|reset
parameter_list|(
name|boolean
name|includeProcessors
parameter_list|)
throws|throws
name|Exception
block|{
name|reset
argument_list|()
expr_stmt|;
comment|// and now reset all processors for this route
if|if
condition|(
name|includeProcessors
condition|)
block|{
name|MBeanServer
name|server
init|=
name|getContext
argument_list|()
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementAgent
argument_list|()
operator|.
name|getMBeanServer
argument_list|()
decl_stmt|;
if|if
condition|(
name|server
operator|!=
literal|null
condition|)
block|{
comment|// get all the processor mbeans and sort them accordingly to their index
name|String
name|prefix
init|=
name|getContext
argument_list|()
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementAgent
argument_list|()
operator|.
name|getIncludeHostName
argument_list|()
condition|?
literal|"*/"
else|:
literal|""
decl_stmt|;
name|ObjectName
name|query
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context="
operator|+
name|prefix
operator|+
name|getContext
argument_list|()
operator|.
name|getManagementName
argument_list|()
operator|+
literal|",type=processors,*"
argument_list|)
decl_stmt|;
name|QueryExp
name|queryExp
init|=
name|Query
operator|.
name|match
argument_list|(
operator|new
name|AttributeValueExp
argument_list|(
literal|"RouteId"
argument_list|)
argument_list|,
operator|new
name|StringValueExp
argument_list|(
name|getRouteId
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|names
init|=
name|server
operator|.
name|queryNames
argument_list|(
name|query
argument_list|,
name|queryExp
argument_list|)
decl_stmt|;
for|for
control|(
name|ObjectName
name|name
range|:
name|names
control|)
block|{
name|server
operator|.
name|invoke
argument_list|(
name|name
argument_list|,
literal|"reset"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
DECL|method|createRouteStaticEndpointJson ()
specifier|public
name|String
name|createRouteStaticEndpointJson
parameter_list|()
block|{
return|return
name|getContext
argument_list|()
operator|.
name|createRouteStaticEndpointJson
argument_list|(
name|getRouteId
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteStaticEndpointJson (boolean includeDynamic)
specifier|public
name|String
name|createRouteStaticEndpointJson
parameter_list|(
name|boolean
name|includeDynamic
parameter_list|)
block|{
return|return
name|getContext
argument_list|()
operator|.
name|createRouteStaticEndpointJson
argument_list|(
name|getRouteId
argument_list|()
argument_list|,
name|includeDynamic
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|equals (Object o)
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
name|this
operator|==
name|o
operator|||
operator|(
name|o
operator|!=
literal|null
operator|&&
name|getClass
argument_list|()
operator|==
name|o
operator|.
name|getClass
argument_list|()
operator|&&
name|route
operator|.
name|equals
argument_list|(
operator|(
operator|(
name|ManagedRoute
operator|)
name|o
operator|)
operator|.
name|route
argument_list|)
operator|)
return|;
block|}
annotation|@
name|Override
DECL|method|hashCode ()
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|route
operator|.
name|hashCode
argument_list|()
return|;
block|}
comment|/**      * Used for sorting the processor mbeans accordingly to their index.      */
DECL|class|OrderProcessorMBeans
specifier|private
specifier|static
specifier|final
class|class
name|OrderProcessorMBeans
implements|implements
name|Comparator
argument_list|<
name|ManagedProcessorMBean
argument_list|>
block|{
annotation|@
name|Override
DECL|method|compare (ManagedProcessorMBean o1, ManagedProcessorMBean o2)
specifier|public
name|int
name|compare
parameter_list|(
name|ManagedProcessorMBean
name|o1
parameter_list|,
name|ManagedProcessorMBean
name|o2
parameter_list|)
block|{
return|return
name|o1
operator|.
name|getIndex
argument_list|()
operator|.
name|compareTo
argument_list|(
name|o2
operator|.
name|getIndex
argument_list|()
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

