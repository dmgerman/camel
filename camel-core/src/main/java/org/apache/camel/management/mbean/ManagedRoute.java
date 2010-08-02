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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jmx
operator|.
name|export
operator|.
name|annotation
operator|.
name|ManagedAttribute
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jmx
operator|.
name|export
operator|.
name|annotation
operator|.
name|ManagedOperation
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jmx
operator|.
name|export
operator|.
name|annotation
operator|.
name|ManagedResource
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
name|Route
name|route
decl_stmt|;
DECL|field|description
specifier|protected
name|String
name|description
decl_stmt|;
DECL|field|context
specifier|protected
name|CamelContext
name|context
decl_stmt|;
DECL|method|ManagedRoute (CamelContext context, Route route)
specifier|public
name|ManagedRoute
parameter_list|(
name|CamelContext
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
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Route id"
argument_list|)
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
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Route Description"
argument_list|)
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
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Route Endpoint Uri"
argument_list|)
DECL|method|getEndpointUri ()
specifier|public
name|String
name|getEndpointUri
parameter_list|()
block|{
name|Endpoint
name|ep
init|=
name|route
operator|.
name|getEndpoint
argument_list|()
decl_stmt|;
return|return
name|ep
operator|!=
literal|null
condition|?
name|ep
operator|.
name|getEndpointUri
argument_list|()
else|:
name|VALUE_UNKNOWN
return|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Route State"
argument_list|)
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
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Current number of inflight Exchanges"
argument_list|)
DECL|method|getInflightExchanges ()
specifier|public
name|Integer
name|getInflightExchanges
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
name|context
operator|.
name|getInflightRepository
argument_list|()
operator|.
name|size
argument_list|(
name|route
operator|.
name|getEndpoint
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Camel id"
argument_list|)
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
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Tracing"
argument_list|)
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
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Tracing"
argument_list|)
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
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Route Policy"
argument_list|)
DECL|method|getRoutePolicy ()
specifier|public
name|String
name|getRoutePolicy
parameter_list|()
block|{
name|RoutePolicy
name|policy
init|=
name|route
operator|.
name|getRouteContext
argument_list|()
operator|.
name|getRoutePolicy
argument_list|()
decl_stmt|;
if|if
condition|(
name|policy
operator|!=
literal|null
condition|)
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
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Start route"
argument_list|)
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|startRoute
argument_list|(
name|getRouteId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Stop route"
argument_list|)
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
name|stopRoute
argument_list|(
name|getRouteId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Stop route (using timeout in seconds)"
argument_list|)
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
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Shutdown and remove route"
argument_list|)
DECL|method|shutdown ()
specifier|public
name|void
name|shutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|context
operator|.
name|shutdownRoute
argument_list|(
name|getRouteId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Shutdown and remove route (using timeout in seconds)"
argument_list|)
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
name|context
operator|.
name|shutdownRoute
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
block|}
end_class

end_unit

