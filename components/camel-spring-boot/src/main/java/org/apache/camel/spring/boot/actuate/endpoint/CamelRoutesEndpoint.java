begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spring.boot.actuate.endpoint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spring
operator|.
name|boot
operator|.
name|actuate
operator|.
name|endpoint
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|Optional
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
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
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
name|annotation
operator|.
name|JsonInclude
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
name|annotation
operator|.
name|JsonProperty
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
name|annotation
operator|.
name|JsonPropertyOrder
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
name|RuntimeCamelException
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
name|StatefulService
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
name|RouteError
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|actuate
operator|.
name|endpoint
operator|.
name|annotation
operator|.
name|Endpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|actuate
operator|.
name|endpoint
operator|.
name|annotation
operator|.
name|ReadOperation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|actuate
operator|.
name|endpoint
operator|.
name|annotation
operator|.
name|Selector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|actuate
operator|.
name|endpoint
operator|.
name|annotation
operator|.
name|WriteOperation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|lang
operator|.
name|Nullable
import|;
end_import

begin_comment
comment|/**  * {@link Endpoint} to expose {@link org.apache.camel.Route} information.  */
end_comment

begin_class
annotation|@
name|Endpoint
argument_list|(
name|id
operator|=
literal|"camelroutes"
argument_list|,
name|enableByDefault
operator|=
literal|true
argument_list|)
DECL|class|CamelRoutesEndpoint
specifier|public
class|class
name|CamelRoutesEndpoint
block|{
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|properties
specifier|private
name|CamelRoutesEndpointProperties
name|properties
decl_stmt|;
DECL|method|CamelRoutesEndpoint (CamelContext camelContext, CamelRoutesEndpointProperties properties)
specifier|public
name|CamelRoutesEndpoint
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|CamelRoutesEndpointProperties
name|properties
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
annotation|@
name|ReadOperation
DECL|method|readRoutes ()
specifier|public
name|List
argument_list|<
name|RouteEndpointInfo
argument_list|>
name|readRoutes
parameter_list|()
block|{
return|return
name|getRoutesInfo
argument_list|()
return|;
block|}
annotation|@
name|ReadOperation
DECL|method|doReadAction (@elector String id, @Selector ReadAction action)
specifier|public
name|Object
name|doReadAction
parameter_list|(
annotation|@
name|Selector
name|String
name|id
parameter_list|,
annotation|@
name|Selector
name|ReadAction
name|action
parameter_list|)
block|{
switch|switch
condition|(
name|action
condition|)
block|{
case|case
name|DETAIL
case|:
return|return
name|getRouteDetailsInfo
argument_list|(
name|id
argument_list|)
return|;
case|case
name|INFO
case|:
return|return
name|getRouteInfo
argument_list|(
name|id
argument_list|)
return|;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported read action "
operator|+
name|action
argument_list|)
throw|;
block|}
block|}
annotation|@
name|WriteOperation
DECL|method|doWriteAction (@elector String id, @Selector WriteAction action, @Nullable TimeInfo timeInfo)
specifier|public
name|void
name|doWriteAction
parameter_list|(
annotation|@
name|Selector
name|String
name|id
parameter_list|,
annotation|@
name|Selector
name|WriteAction
name|action
parameter_list|,
annotation|@
name|Nullable
name|TimeInfo
name|timeInfo
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|properties
operator|.
name|isReadOnly
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Read only: write action %s is not allowed"
argument_list|,
name|action
argument_list|)
argument_list|)
throw|;
block|}
switch|switch
condition|(
name|action
condition|)
block|{
case|case
name|STOP
case|:
name|stopRoute
argument_list|(
name|id
argument_list|,
name|Optional
operator|.
name|ofNullable
argument_list|(
name|timeInfo
argument_list|)
operator|.
name|flatMap
argument_list|(
name|ti
lambda|->
name|Optional
operator|.
name|ofNullable
argument_list|(
name|ti
operator|.
name|getTimeout
argument_list|()
argument_list|)
argument_list|)
argument_list|,
name|Optional
operator|.
name|of
argument_list|(
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|,
name|Optional
operator|.
name|ofNullable
argument_list|(
name|timeInfo
argument_list|)
operator|.
name|flatMap
argument_list|(
name|ti
lambda|->
name|Optional
operator|.
name|ofNullable
argument_list|(
name|ti
operator|.
name|getAbortAfterTimeout
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|START
case|:
name|startRoute
argument_list|(
name|id
argument_list|)
expr_stmt|;
break|break;
case|case
name|RESET
case|:
name|resetRoute
argument_list|(
name|id
argument_list|)
expr_stmt|;
break|break;
case|case
name|SUSPEND
case|:
name|suspendRoute
argument_list|(
name|id
argument_list|,
name|Optional
operator|.
name|ofNullable
argument_list|(
name|timeInfo
argument_list|)
operator|.
name|flatMap
argument_list|(
name|ti
lambda|->
name|Optional
operator|.
name|ofNullable
argument_list|(
name|ti
operator|.
name|getTimeout
argument_list|()
argument_list|)
argument_list|)
argument_list|,
name|Optional
operator|.
name|of
argument_list|(
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
name|RESUME
case|:
name|resumeRoute
argument_list|(
name|id
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported write action "
operator|+
name|action
argument_list|)
throw|;
block|}
block|}
annotation|@
name|WriteOperation
DECL|method|getRouteDump (@elector String id)
specifier|public
name|String
name|getRouteDump
parameter_list|(
annotation|@
name|Selector
name|String
name|id
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|properties
operator|.
name|isReadOnly
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Read only: route dump is not permitted in read-only mode"
argument_list|)
throw|;
block|}
name|RouteDefinition
name|route
init|=
name|camelContext
operator|.
name|getRouteDefinition
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|route
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
name|ModelHelper
operator|.
name|dumpModelAsXml
argument_list|(
name|camelContext
argument_list|,
name|route
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|getRouteInfo (String id)
specifier|private
name|RouteEndpointInfo
name|getRouteInfo
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|Route
name|route
init|=
name|camelContext
operator|.
name|getRoute
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|route
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|RouteEndpointInfo
argument_list|(
name|route
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|getRoutesInfo ()
specifier|private
name|List
argument_list|<
name|RouteEndpointInfo
argument_list|>
name|getRoutesInfo
parameter_list|()
block|{
return|return
name|camelContext
operator|.
name|getRoutes
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|RouteEndpointInfo
operator|::
operator|new
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getRouteDetailsInfo (String id)
specifier|private
name|RouteDetailsEndpointInfo
name|getRouteDetailsInfo
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|Route
name|route
init|=
name|camelContext
operator|.
name|getRoute
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|route
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|RouteDetailsEndpointInfo
argument_list|(
name|camelContext
argument_list|,
name|route
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|startRoute (String id)
specifier|private
name|void
name|startRoute
parameter_list|(
name|String
name|id
parameter_list|)
block|{
try|try
block|{
name|camelContext
operator|.
name|getRouteController
argument_list|()
operator|.
name|startRoute
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|resetRoute (String id)
specifier|private
name|void
name|resetRoute
parameter_list|(
name|String
name|id
parameter_list|)
block|{
try|try
block|{
name|ManagedRouteMBean
name|managedRouteMBean
init|=
name|camelContext
operator|.
name|getManagedRoute
argument_list|(
name|id
argument_list|,
name|ManagedRouteMBean
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|managedRouteMBean
operator|!=
literal|null
condition|)
block|{
name|managedRouteMBean
operator|.
name|reset
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|stopRoute (String id, Optional<Long> timeout, Optional<TimeUnit> timeUnit, Optional<Boolean> abortAfterTimeout)
specifier|private
name|void
name|stopRoute
parameter_list|(
name|String
name|id
parameter_list|,
name|Optional
argument_list|<
name|Long
argument_list|>
name|timeout
parameter_list|,
name|Optional
argument_list|<
name|TimeUnit
argument_list|>
name|timeUnit
parameter_list|,
name|Optional
argument_list|<
name|Boolean
argument_list|>
name|abortAfterTimeout
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|timeout
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|camelContext
operator|.
name|getRouteController
argument_list|()
operator|.
name|stopRoute
argument_list|(
name|id
argument_list|,
name|timeout
operator|.
name|get
argument_list|()
argument_list|,
name|timeUnit
operator|.
name|orElse
argument_list|(
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|,
name|abortAfterTimeout
operator|.
name|orElse
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|camelContext
operator|.
name|getRouteController
argument_list|()
operator|.
name|stopRoute
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|suspendRoute (String id, Optional<Long> timeout, Optional<TimeUnit> timeUnit)
specifier|private
name|void
name|suspendRoute
parameter_list|(
name|String
name|id
parameter_list|,
name|Optional
argument_list|<
name|Long
argument_list|>
name|timeout
parameter_list|,
name|Optional
argument_list|<
name|TimeUnit
argument_list|>
name|timeUnit
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|timeout
operator|.
name|isPresent
argument_list|()
condition|)
block|{
name|camelContext
operator|.
name|getRouteController
argument_list|()
operator|.
name|suspendRoute
argument_list|(
name|id
argument_list|,
name|timeout
operator|.
name|get
argument_list|()
argument_list|,
name|timeUnit
operator|.
name|orElse
argument_list|(
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|camelContext
operator|.
name|getRouteController
argument_list|()
operator|.
name|suspendRoute
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|resumeRoute (String id)
specifier|private
name|void
name|resumeRoute
parameter_list|(
name|String
name|id
parameter_list|)
block|{
try|try
block|{
name|camelContext
operator|.
name|getRouteController
argument_list|()
operator|.
name|resumeRoute
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Container for exposing {@link org.apache.camel.Route} information as JSON.      */
annotation|@
name|JsonPropertyOrder
argument_list|(
block|{
literal|"id"
block|,
literal|"description"
block|,
literal|"uptime"
block|,
literal|"uptimeMillis"
block|}
argument_list|)
annotation|@
name|JsonInclude
argument_list|(
name|JsonInclude
operator|.
name|Include
operator|.
name|NON_EMPTY
argument_list|)
DECL|class|RouteEndpointInfo
specifier|public
specifier|static
class|class
name|RouteEndpointInfo
block|{
DECL|field|id
specifier|private
specifier|final
name|String
name|id
decl_stmt|;
DECL|field|description
specifier|private
specifier|final
name|String
name|description
decl_stmt|;
DECL|field|uptime
specifier|private
specifier|final
name|String
name|uptime
decl_stmt|;
DECL|field|uptimeMillis
specifier|private
specifier|final
name|long
name|uptimeMillis
decl_stmt|;
DECL|field|status
specifier|private
specifier|final
name|String
name|status
decl_stmt|;
DECL|method|RouteEndpointInfo (Route route)
specifier|public
name|RouteEndpointInfo
parameter_list|(
name|Route
name|route
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|route
operator|.
name|getId
argument_list|()
expr_stmt|;
name|this
operator|.
name|description
operator|=
name|route
operator|.
name|getDescription
argument_list|()
expr_stmt|;
name|this
operator|.
name|uptime
operator|=
name|route
operator|.
name|getUptime
argument_list|()
expr_stmt|;
name|this
operator|.
name|uptimeMillis
operator|=
name|route
operator|.
name|getUptimeMillis
argument_list|()
expr_stmt|;
if|if
condition|(
name|route
operator|instanceof
name|StatefulService
condition|)
block|{
name|this
operator|.
name|status
operator|=
operator|(
operator|(
name|StatefulService
operator|)
name|route
operator|)
operator|.
name|getStatus
argument_list|()
operator|.
name|name
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|status
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
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
DECL|method|getUptime ()
specifier|public
name|String
name|getUptime
parameter_list|()
block|{
return|return
name|uptime
return|;
block|}
DECL|method|getUptimeMillis ()
specifier|public
name|long
name|getUptimeMillis
parameter_list|()
block|{
return|return
name|uptimeMillis
return|;
block|}
DECL|method|getStatus ()
specifier|public
name|String
name|getStatus
parameter_list|()
block|{
return|return
name|status
return|;
block|}
block|}
comment|/**      * Container for exposing {@link org.apache.camel.Route} information      * with route details as JSON. Route details are retrieved from JMX.      */
DECL|class|RouteDetailsEndpointInfo
specifier|public
specifier|static
class|class
name|RouteDetailsEndpointInfo
extends|extends
name|RouteEndpointInfo
block|{
annotation|@
name|JsonProperty
argument_list|(
literal|"details"
argument_list|)
DECL|field|routeDetails
specifier|private
name|RouteDetails
name|routeDetails
decl_stmt|;
DECL|method|RouteDetailsEndpointInfo (final CamelContext camelContext, final Route route)
specifier|public
name|RouteDetailsEndpointInfo
parameter_list|(
specifier|final
name|CamelContext
name|camelContext
parameter_list|,
specifier|final
name|Route
name|route
parameter_list|)
block|{
name|super
argument_list|(
name|route
argument_list|)
expr_stmt|;
if|if
condition|(
name|camelContext
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|getManagementAgent
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|routeDetails
operator|=
operator|new
name|RouteDetails
argument_list|(
name|camelContext
operator|.
name|getManagedRoute
argument_list|(
name|route
operator|.
name|getId
argument_list|()
argument_list|,
name|ManagedRouteMBean
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|JsonInclude
argument_list|(
name|JsonInclude
operator|.
name|Include
operator|.
name|NON_EMPTY
argument_list|)
DECL|class|RouteDetails
specifier|static
class|class
name|RouteDetails
block|{
DECL|field|deltaProcessingTime
specifier|private
name|long
name|deltaProcessingTime
decl_stmt|;
DECL|field|exchangesInflight
specifier|private
name|long
name|exchangesInflight
decl_stmt|;
DECL|field|exchangesTotal
specifier|private
name|long
name|exchangesTotal
decl_stmt|;
DECL|field|externalRedeliveries
specifier|private
name|long
name|externalRedeliveries
decl_stmt|;
DECL|field|failuresHandled
specifier|private
name|long
name|failuresHandled
decl_stmt|;
DECL|field|firstExchangeCompletedExchangeId
specifier|private
name|String
name|firstExchangeCompletedExchangeId
decl_stmt|;
DECL|field|firstExchangeCompletedTimestamp
specifier|private
name|Date
name|firstExchangeCompletedTimestamp
decl_stmt|;
DECL|field|firstExchangeFailureExchangeId
specifier|private
name|String
name|firstExchangeFailureExchangeId
decl_stmt|;
DECL|field|firstExchangeFailureTimestamp
specifier|private
name|Date
name|firstExchangeFailureTimestamp
decl_stmt|;
DECL|field|lastExchangeCompletedExchangeId
specifier|private
name|String
name|lastExchangeCompletedExchangeId
decl_stmt|;
DECL|field|lastExchangeCompletedTimestamp
specifier|private
name|Date
name|lastExchangeCompletedTimestamp
decl_stmt|;
DECL|field|lastExchangeFailureExchangeId
specifier|private
name|String
name|lastExchangeFailureExchangeId
decl_stmt|;
DECL|field|lastExchangeFailureTimestamp
specifier|private
name|Date
name|lastExchangeFailureTimestamp
decl_stmt|;
DECL|field|lastProcessingTime
specifier|private
name|long
name|lastProcessingTime
decl_stmt|;
DECL|field|load01
specifier|private
name|String
name|load01
decl_stmt|;
DECL|field|load05
specifier|private
name|String
name|load05
decl_stmt|;
DECL|field|load15
specifier|private
name|String
name|load15
decl_stmt|;
DECL|field|maxProcessingTime
specifier|private
name|long
name|maxProcessingTime
decl_stmt|;
DECL|field|meanProcessingTime
specifier|private
name|long
name|meanProcessingTime
decl_stmt|;
DECL|field|minProcessingTime
specifier|private
name|long
name|minProcessingTime
decl_stmt|;
DECL|field|oldestInflightDuration
specifier|private
name|Long
name|oldestInflightDuration
decl_stmt|;
DECL|field|oldestInflightExchangeId
specifier|private
name|String
name|oldestInflightExchangeId
decl_stmt|;
DECL|field|redeliveries
specifier|private
name|long
name|redeliveries
decl_stmt|;
DECL|field|totalProcessingTime
specifier|private
name|long
name|totalProcessingTime
decl_stmt|;
DECL|field|lastError
specifier|private
name|RouteError
name|lastError
decl_stmt|;
DECL|field|hasRouteController
specifier|private
name|boolean
name|hasRouteController
decl_stmt|;
DECL|method|RouteDetails (ManagedRouteMBean managedRoute)
name|RouteDetails
parameter_list|(
name|ManagedRouteMBean
name|managedRoute
parameter_list|)
block|{
try|try
block|{
name|this
operator|.
name|deltaProcessingTime
operator|=
name|managedRoute
operator|.
name|getDeltaProcessingTime
argument_list|()
expr_stmt|;
name|this
operator|.
name|exchangesInflight
operator|=
name|managedRoute
operator|.
name|getExchangesInflight
argument_list|()
expr_stmt|;
name|this
operator|.
name|exchangesTotal
operator|=
name|managedRoute
operator|.
name|getExchangesTotal
argument_list|()
expr_stmt|;
name|this
operator|.
name|externalRedeliveries
operator|=
name|managedRoute
operator|.
name|getExternalRedeliveries
argument_list|()
expr_stmt|;
name|this
operator|.
name|failuresHandled
operator|=
name|managedRoute
operator|.
name|getFailuresHandled
argument_list|()
expr_stmt|;
name|this
operator|.
name|firstExchangeCompletedExchangeId
operator|=
name|managedRoute
operator|.
name|getFirstExchangeCompletedExchangeId
argument_list|()
expr_stmt|;
name|this
operator|.
name|firstExchangeCompletedTimestamp
operator|=
name|managedRoute
operator|.
name|getFirstExchangeCompletedTimestamp
argument_list|()
expr_stmt|;
name|this
operator|.
name|firstExchangeFailureExchangeId
operator|=
name|managedRoute
operator|.
name|getFirstExchangeFailureExchangeId
argument_list|()
expr_stmt|;
name|this
operator|.
name|firstExchangeFailureTimestamp
operator|=
name|managedRoute
operator|.
name|getFirstExchangeFailureTimestamp
argument_list|()
expr_stmt|;
name|this
operator|.
name|lastExchangeCompletedExchangeId
operator|=
name|managedRoute
operator|.
name|getLastExchangeCompletedExchangeId
argument_list|()
expr_stmt|;
name|this
operator|.
name|lastExchangeCompletedTimestamp
operator|=
name|managedRoute
operator|.
name|getLastExchangeCompletedTimestamp
argument_list|()
expr_stmt|;
name|this
operator|.
name|lastExchangeFailureExchangeId
operator|=
name|managedRoute
operator|.
name|getLastExchangeFailureExchangeId
argument_list|()
expr_stmt|;
name|this
operator|.
name|lastExchangeFailureTimestamp
operator|=
name|managedRoute
operator|.
name|getLastExchangeFailureTimestamp
argument_list|()
expr_stmt|;
name|this
operator|.
name|lastProcessingTime
operator|=
name|managedRoute
operator|.
name|getLastProcessingTime
argument_list|()
expr_stmt|;
name|this
operator|.
name|load01
operator|=
name|managedRoute
operator|.
name|getLoad01
argument_list|()
expr_stmt|;
name|this
operator|.
name|load05
operator|=
name|managedRoute
operator|.
name|getLoad05
argument_list|()
expr_stmt|;
name|this
operator|.
name|load15
operator|=
name|managedRoute
operator|.
name|getLoad15
argument_list|()
expr_stmt|;
name|this
operator|.
name|maxProcessingTime
operator|=
name|managedRoute
operator|.
name|getMaxProcessingTime
argument_list|()
expr_stmt|;
name|this
operator|.
name|meanProcessingTime
operator|=
name|managedRoute
operator|.
name|getMeanProcessingTime
argument_list|()
expr_stmt|;
name|this
operator|.
name|minProcessingTime
operator|=
name|managedRoute
operator|.
name|getMinProcessingTime
argument_list|()
expr_stmt|;
name|this
operator|.
name|oldestInflightDuration
operator|=
name|managedRoute
operator|.
name|getOldestInflightDuration
argument_list|()
expr_stmt|;
name|this
operator|.
name|oldestInflightExchangeId
operator|=
name|managedRoute
operator|.
name|getOldestInflightExchangeId
argument_list|()
expr_stmt|;
name|this
operator|.
name|redeliveries
operator|=
name|managedRoute
operator|.
name|getRedeliveries
argument_list|()
expr_stmt|;
name|this
operator|.
name|totalProcessingTime
operator|=
name|managedRoute
operator|.
name|getTotalProcessingTime
argument_list|()
expr_stmt|;
name|this
operator|.
name|lastError
operator|=
name|managedRoute
operator|.
name|getLastError
argument_list|()
expr_stmt|;
name|this
operator|.
name|hasRouteController
operator|=
name|managedRoute
operator|.
name|getHasRouteController
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// Ignore
block|}
block|}
DECL|method|getDeltaProcessingTime ()
specifier|public
name|long
name|getDeltaProcessingTime
parameter_list|()
block|{
return|return
name|deltaProcessingTime
return|;
block|}
DECL|method|getExchangesInflight ()
specifier|public
name|long
name|getExchangesInflight
parameter_list|()
block|{
return|return
name|exchangesInflight
return|;
block|}
DECL|method|getExchangesTotal ()
specifier|public
name|long
name|getExchangesTotal
parameter_list|()
block|{
return|return
name|exchangesTotal
return|;
block|}
DECL|method|getExternalRedeliveries ()
specifier|public
name|long
name|getExternalRedeliveries
parameter_list|()
block|{
return|return
name|externalRedeliveries
return|;
block|}
DECL|method|getFailuresHandled ()
specifier|public
name|long
name|getFailuresHandled
parameter_list|()
block|{
return|return
name|failuresHandled
return|;
block|}
DECL|method|getFirstExchangeCompletedExchangeId ()
specifier|public
name|String
name|getFirstExchangeCompletedExchangeId
parameter_list|()
block|{
return|return
name|firstExchangeCompletedExchangeId
return|;
block|}
DECL|method|getFirstExchangeCompletedTimestamp ()
specifier|public
name|Date
name|getFirstExchangeCompletedTimestamp
parameter_list|()
block|{
return|return
name|firstExchangeCompletedTimestamp
return|;
block|}
DECL|method|getFirstExchangeFailureExchangeId ()
specifier|public
name|String
name|getFirstExchangeFailureExchangeId
parameter_list|()
block|{
return|return
name|firstExchangeFailureExchangeId
return|;
block|}
DECL|method|getFirstExchangeFailureTimestamp ()
specifier|public
name|Date
name|getFirstExchangeFailureTimestamp
parameter_list|()
block|{
return|return
name|firstExchangeFailureTimestamp
return|;
block|}
DECL|method|getLastExchangeCompletedExchangeId ()
specifier|public
name|String
name|getLastExchangeCompletedExchangeId
parameter_list|()
block|{
return|return
name|lastExchangeCompletedExchangeId
return|;
block|}
DECL|method|getLastExchangeCompletedTimestamp ()
specifier|public
name|Date
name|getLastExchangeCompletedTimestamp
parameter_list|()
block|{
return|return
name|lastExchangeCompletedTimestamp
return|;
block|}
DECL|method|getLastExchangeFailureExchangeId ()
specifier|public
name|String
name|getLastExchangeFailureExchangeId
parameter_list|()
block|{
return|return
name|lastExchangeFailureExchangeId
return|;
block|}
DECL|method|getLastExchangeFailureTimestamp ()
specifier|public
name|Date
name|getLastExchangeFailureTimestamp
parameter_list|()
block|{
return|return
name|lastExchangeFailureTimestamp
return|;
block|}
DECL|method|getLastProcessingTime ()
specifier|public
name|long
name|getLastProcessingTime
parameter_list|()
block|{
return|return
name|lastProcessingTime
return|;
block|}
DECL|method|getLoad01 ()
specifier|public
name|String
name|getLoad01
parameter_list|()
block|{
return|return
name|load01
return|;
block|}
DECL|method|getLoad05 ()
specifier|public
name|String
name|getLoad05
parameter_list|()
block|{
return|return
name|load05
return|;
block|}
DECL|method|getLoad15 ()
specifier|public
name|String
name|getLoad15
parameter_list|()
block|{
return|return
name|load15
return|;
block|}
DECL|method|getMaxProcessingTime ()
specifier|public
name|long
name|getMaxProcessingTime
parameter_list|()
block|{
return|return
name|maxProcessingTime
return|;
block|}
DECL|method|getMeanProcessingTime ()
specifier|public
name|long
name|getMeanProcessingTime
parameter_list|()
block|{
return|return
name|meanProcessingTime
return|;
block|}
DECL|method|getMinProcessingTime ()
specifier|public
name|long
name|getMinProcessingTime
parameter_list|()
block|{
return|return
name|minProcessingTime
return|;
block|}
DECL|method|getOldestInflightDuration ()
specifier|public
name|Long
name|getOldestInflightDuration
parameter_list|()
block|{
return|return
name|oldestInflightDuration
return|;
block|}
DECL|method|getOldestInflightExchangeId ()
specifier|public
name|String
name|getOldestInflightExchangeId
parameter_list|()
block|{
return|return
name|oldestInflightExchangeId
return|;
block|}
DECL|method|getRedeliveries ()
specifier|public
name|long
name|getRedeliveries
parameter_list|()
block|{
return|return
name|redeliveries
return|;
block|}
DECL|method|getTotalProcessingTime ()
specifier|public
name|long
name|getTotalProcessingTime
parameter_list|()
block|{
return|return
name|totalProcessingTime
return|;
block|}
DECL|method|getLastError ()
specifier|public
name|RouteError
name|getLastError
parameter_list|()
block|{
return|return
name|lastError
return|;
block|}
DECL|method|getHasRouteController ()
specifier|public
name|boolean
name|getHasRouteController
parameter_list|()
block|{
return|return
name|hasRouteController
return|;
block|}
block|}
block|}
comment|/**      * List of write actions available for the endpoint      */
DECL|enum|WriteAction
specifier|public
enum|enum
name|WriteAction
block|{
DECL|enumConstant|STOP
name|STOP
block|,
DECL|enumConstant|START
name|START
block|,
DECL|enumConstant|RESET
name|RESET
block|,
DECL|enumConstant|SUSPEND
name|SUSPEND
block|,
DECL|enumConstant|RESUME
name|RESUME
block|}
comment|/*      * List of read actions available for the endpoint      */
DECL|enum|ReadAction
specifier|public
enum|enum
name|ReadAction
block|{
DECL|enumConstant|DETAIL
name|DETAIL
block|,
DECL|enumConstant|INFO
name|INFO
block|}
comment|/**      * Optional time information for the actions      */
DECL|class|TimeInfo
specifier|public
specifier|static
class|class
name|TimeInfo
block|{
DECL|field|timeout
specifier|private
name|Long
name|timeout
decl_stmt|;
DECL|field|abortAfterTimeout
specifier|private
name|Boolean
name|abortAfterTimeout
decl_stmt|;
DECL|method|getTimeout ()
specifier|public
name|Long
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
DECL|method|setTimeout (Long timeout)
specifier|public
name|void
name|setTimeout
parameter_list|(
name|Long
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
DECL|method|getAbortAfterTimeout ()
specifier|public
name|Boolean
name|getAbortAfterTimeout
parameter_list|()
block|{
return|return
name|abortAfterTimeout
return|;
block|}
DECL|method|setAbortAfterTimeout (Boolean abortAfterTimeout)
specifier|public
name|void
name|setAbortAfterTimeout
parameter_list|(
name|Boolean
name|abortAfterTimeout
parameter_list|)
block|{
name|this
operator|.
name|abortAfterTimeout
operator|=
name|abortAfterTimeout
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

