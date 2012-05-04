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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
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
name|Endpoint
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
name|ProducerTemplate
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
name|ManagedCamelContextMBean
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
name|model
operator|.
name|RoutesDefinition
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
name|ManagementStrategy
import|;
end_import

begin_comment
comment|/**  * @version   */
end_comment

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed CamelContext"
argument_list|)
DECL|class|ManagedCamelContext
specifier|public
class|class
name|ManagedCamelContext
implements|implements
name|TimerListener
implements|,
name|ManagedCamelContextMBean
block|{
DECL|field|context
specifier|private
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
DECL|method|ManagedCamelContext (ModelCamelContext context)
specifier|public
name|ManagedCamelContext
parameter_list|(
name|ModelCamelContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
DECL|method|init (ManagementStrategy strategy)
specifier|public
name|void
name|init
parameter_list|(
name|ManagementStrategy
name|strategy
parameter_list|)
block|{
comment|// do nothing
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
DECL|method|getManagementName ()
specifier|public
name|String
name|getManagementName
parameter_list|()
block|{
return|return
name|context
operator|.
name|getManagementName
argument_list|()
return|;
block|}
DECL|method|getCamelVersion ()
specifier|public
name|String
name|getCamelVersion
parameter_list|()
block|{
return|return
name|context
operator|.
name|getVersion
argument_list|()
return|;
block|}
DECL|method|getState ()
specifier|public
name|String
name|getState
parameter_list|()
block|{
return|return
name|context
operator|.
name|getStatus
argument_list|()
operator|.
name|name
argument_list|()
return|;
block|}
DECL|method|getUptime ()
specifier|public
name|String
name|getUptime
parameter_list|()
block|{
return|return
name|context
operator|.
name|getUptime
argument_list|()
return|;
block|}
DECL|method|getProperties ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getProperties
parameter_list|()
block|{
if|if
condition|(
name|context
operator|.
name|getProperties
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|context
operator|.
name|getProperties
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
name|context
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
name|context
operator|.
name|setTracing
argument_list|(
name|tracing
argument_list|)
expr_stmt|;
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
argument_list|()
return|;
block|}
DECL|method|setTimeout (long timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|long
name|timeout
parameter_list|)
block|{
name|context
operator|.
name|getShutdownStrategy
argument_list|()
operator|.
name|setTimeout
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
block|}
DECL|method|getTimeout ()
specifier|public
name|long
name|getTimeout
parameter_list|()
block|{
return|return
name|context
operator|.
name|getShutdownStrategy
argument_list|()
operator|.
name|getTimeout
argument_list|()
return|;
block|}
DECL|method|setTimeUnit (TimeUnit timeUnit)
specifier|public
name|void
name|setTimeUnit
parameter_list|(
name|TimeUnit
name|timeUnit
parameter_list|)
block|{
name|context
operator|.
name|getShutdownStrategy
argument_list|()
operator|.
name|setTimeUnit
argument_list|(
name|timeUnit
argument_list|)
expr_stmt|;
block|}
DECL|method|getTimeUnit ()
specifier|public
name|TimeUnit
name|getTimeUnit
parameter_list|()
block|{
return|return
name|context
operator|.
name|getShutdownStrategy
argument_list|()
operator|.
name|getTimeUnit
argument_list|()
return|;
block|}
DECL|method|setShutdownNowOnTimeout (boolean shutdownNowOnTimeout)
specifier|public
name|void
name|setShutdownNowOnTimeout
parameter_list|(
name|boolean
name|shutdownNowOnTimeout
parameter_list|)
block|{
name|context
operator|.
name|getShutdownStrategy
argument_list|()
operator|.
name|setShutdownNowOnTimeout
argument_list|(
name|shutdownNowOnTimeout
argument_list|)
expr_stmt|;
block|}
DECL|method|isShutdownNowOnTimeout ()
specifier|public
name|boolean
name|isShutdownNowOnTimeout
parameter_list|()
block|{
return|return
name|context
operator|.
name|getShutdownStrategy
argument_list|()
operator|.
name|isShutdownNowOnTimeout
argument_list|()
return|;
block|}
DECL|method|getLoad01 ()
specifier|public
name|String
name|getLoad01
parameter_list|()
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%.2f"
argument_list|,
name|load
operator|.
name|getLoad1
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getLoad05 ()
specifier|public
name|String
name|getLoad05
parameter_list|()
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%.2f"
argument_list|,
name|load
operator|.
name|getLoad5
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getLoad15 ()
specifier|public
name|String
name|getLoad15
parameter_list|()
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%.2f"
argument_list|,
name|load
operator|.
name|getLoad15
argument_list|()
argument_list|)
return|;
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
name|context
operator|.
name|isSuspended
argument_list|()
condition|)
block|{
name|context
operator|.
name|resume
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|suspend ()
specifier|public
name|void
name|suspend
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|suspend
argument_list|()
expr_stmt|;
block|}
DECL|method|resume ()
specifier|public
name|void
name|resume
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|context
operator|.
name|isSuspended
argument_list|()
condition|)
block|{
name|context
operator|.
name|resume
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"CamelContext is not suspended"
argument_list|)
throw|;
block|}
block|}
DECL|method|sendBody (String endpointUri, Object body)
specifier|public
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
block|{
name|ProducerTemplate
name|template
init|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
name|endpointUri
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|template
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|sendStringBody (String endpointUri, String body)
specifier|public
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
block|{
name|sendBody
argument_list|(
name|endpointUri
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
DECL|method|sendBodyAndHeaders (String endpointUri, Object body, Map<String, Object> headers)
specifier|public
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
block|{
name|ProducerTemplate
name|template
init|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
try|try
block|{
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
name|endpointUri
argument_list|,
name|body
argument_list|,
name|headers
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|template
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|requestBody (String endpointUri, Object body)
specifier|public
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
block|{
name|ProducerTemplate
name|template
init|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|Object
name|answer
init|=
literal|null
decl_stmt|;
try|try
block|{
name|answer
operator|=
name|template
operator|.
name|requestBody
argument_list|(
name|endpointUri
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|template
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|requestStringBody (String endpointUri, String body)
specifier|public
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
block|{
return|return
name|requestBody
argument_list|(
name|endpointUri
argument_list|,
name|body
argument_list|)
return|;
block|}
DECL|method|requestBodyAndHeaders (String endpointUri, Object body, Map<String, Object> headers)
specifier|public
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
block|{
name|ProducerTemplate
name|template
init|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|Object
name|answer
init|=
literal|null
decl_stmt|;
try|try
block|{
name|answer
operator|=
name|template
operator|.
name|requestBodyAndHeaders
argument_list|(
name|endpointUri
argument_list|,
name|body
argument_list|,
name|headers
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|template
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|dumpRoutesAsXml ()
specifier|public
name|String
name|dumpRoutesAsXml
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|routes
init|=
name|context
operator|.
name|getRouteDefinitions
argument_list|()
decl_stmt|;
if|if
condition|(
name|routes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
comment|// use a routes definition to dump the routes
name|RoutesDefinition
name|def
init|=
operator|new
name|RoutesDefinition
argument_list|()
decl_stmt|;
name|def
operator|.
name|setRoutes
argument_list|(
name|routes
argument_list|)
expr_stmt|;
return|return
name|ModelHelper
operator|.
name|dumpModelAsXml
argument_list|(
name|def
argument_list|)
return|;
block|}
DECL|method|addOrUpdateRoutesFromXml (String xml)
specifier|public
name|void
name|addOrUpdateRoutesFromXml
parameter_list|(
name|String
name|xml
parameter_list|)
throws|throws
name|Exception
block|{
comment|// convert to model from xml
name|InputStream
name|is
init|=
name|context
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|xml
argument_list|)
decl_stmt|;
name|RoutesDefinition
name|def
init|=
name|context
operator|.
name|loadRoutesDefinition
argument_list|(
name|is
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
comment|// add will remove existing route first
name|context
operator|.
name|addRouteDefinitions
argument_list|(
name|def
operator|.
name|getRoutes
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|dumpRoutesStatsAsXml (boolean fullStats, boolean includeProcessors)
specifier|public
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
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"<camelContextStat"
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
name|getCamelId
argument_list|()
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|">\n"
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
comment|// gather all the routes for this CamelContext, which requires JMX
name|ObjectName
name|query
init|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=*/"
operator|+
name|getContext
argument_list|()
operator|.
name|getManagementName
argument_list|()
operator|+
literal|",type=routes,*"
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|ObjectName
argument_list|>
name|routes
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
name|Set
argument_list|<
name|ManagedProcessorMBean
argument_list|>
name|processors
init|=
operator|new
name|LinkedHashSet
argument_list|<
name|ManagedProcessorMBean
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|includeProcessors
condition|)
block|{
comment|// gather all the processors for this CamelContext, which requires JMX
name|query
operator|=
name|ObjectName
operator|.
name|getInstance
argument_list|(
literal|"org.apache.camel:context=*/"
operator|+
name|getContext
argument_list|()
operator|.
name|getManagementName
argument_list|()
operator|+
literal|",type=processors,*"
argument_list|)
expr_stmt|;
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
name|processors
operator|.
name|add
argument_list|(
name|processor
argument_list|)
expr_stmt|;
block|}
block|}
comment|// loop the routes, and append the processor stats if needed
name|sb
operator|.
name|append
argument_list|(
literal|"<routeStats>\n"
argument_list|)
expr_stmt|;
for|for
control|(
name|ObjectName
name|on
range|:
name|routes
control|)
block|{
name|ManagedRouteMBean
name|route
init|=
name|MBeanServerInvocationHandler
operator|.
name|newProxyInstance
argument_list|(
name|server
argument_list|,
name|on
argument_list|,
name|ManagedRouteMBean
operator|.
name|class
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|sb
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
name|getRouteId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|// use substring as we only want the attributes
name|String
name|stat
init|=
name|route
operator|.
name|dumpStatsAsXml
argument_list|(
name|fullStats
argument_list|)
decl_stmt|;
name|sb
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
comment|// add processor details if needed
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
for|for
control|(
name|ManagedProcessorMBean
name|processor
range|:
name|processors
control|)
block|{
comment|// the processor must belong to this route
if|if
condition|(
name|route
operator|.
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
literal|" id=\"%s\""
argument_list|,
name|processor
operator|.
name|getProcessorId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
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
name|sb
operator|.
name|append
argument_list|(
literal|"</routeStat>\n"
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|"</routeStats>\n"
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|"</camelContextStat>"
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|createEndpoint (String uri)
specifier|public
name|boolean
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|context
operator|.
name|hasEndpoint
argument_list|(
name|uri
argument_list|)
operator|!=
literal|null
condition|)
block|{
comment|// endpoint already exists
return|return
literal|false
return|;
block|}
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|uri
argument_list|)
decl_stmt|;
if|if
condition|(
name|endpoint
operator|!=
literal|null
condition|)
block|{
comment|// ensure endpoint is registered, as the management strategy could have been configured to not always
comment|// register new endpoints in JMX, so we need to check if its registered, and if not register it manually
name|ObjectName
name|on
init|=
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementNamingStrategy
argument_list|()
operator|.
name|getObjectNameForEndpoint
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
if|if
condition|(
name|on
operator|!=
literal|null
operator|&&
operator|!
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementAgent
argument_list|()
operator|.
name|isRegistered
argument_list|(
name|on
argument_list|)
condition|)
block|{
comment|// register endpoint as mbean
name|Object
name|me
init|=
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementObjectStrategy
argument_list|()
operator|.
name|getManagedObjectForEndpoint
argument_list|(
name|context
argument_list|,
name|endpoint
argument_list|)
decl_stmt|;
name|context
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementAgent
argument_list|()
operator|.
name|register
argument_list|(
name|me
argument_list|,
name|on
argument_list|)
expr_stmt|;
block|}
return|return
literal|true
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
DECL|method|removeEndpoints (String pattern)
specifier|public
name|int
name|removeEndpoints
parameter_list|(
name|String
name|pattern
parameter_list|)
throws|throws
name|Exception
block|{
comment|// endpoints is always removed from JMX if removed from context
name|Collection
argument_list|<
name|Endpoint
argument_list|>
name|removed
init|=
name|context
operator|.
name|removeEndpoints
argument_list|(
name|pattern
argument_list|)
decl_stmt|;
return|return
name|removed
operator|.
name|size
argument_list|()
return|;
block|}
block|}
end_class

end_unit

