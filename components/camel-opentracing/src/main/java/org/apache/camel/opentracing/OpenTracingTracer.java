begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.opentracing
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|opentracing
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EventObject
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ServiceLoader
import|;
end_import

begin_import
import|import
name|io
operator|.
name|opentracing
operator|.
name|Span
import|;
end_import

begin_import
import|import
name|io
operator|.
name|opentracing
operator|.
name|Tracer
import|;
end_import

begin_import
import|import
name|io
operator|.
name|opentracing
operator|.
name|Tracer
operator|.
name|SpanBuilder
import|;
end_import

begin_import
import|import
name|io
operator|.
name|opentracing
operator|.
name|contrib
operator|.
name|global
operator|.
name|GlobalTracer
import|;
end_import

begin_import
import|import
name|io
operator|.
name|opentracing
operator|.
name|contrib
operator|.
name|spanmanager
operator|.
name|DefaultSpanManager
import|;
end_import

begin_import
import|import
name|io
operator|.
name|opentracing
operator|.
name|contrib
operator|.
name|spanmanager
operator|.
name|SpanManager
import|;
end_import

begin_import
import|import
name|io
operator|.
name|opentracing
operator|.
name|propagation
operator|.
name|Format
import|;
end_import

begin_import
import|import
name|io
operator|.
name|opentracing
operator|.
name|tag
operator|.
name|Tags
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
name|CamelContextAware
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
name|Exchange
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
name|StaticService
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
name|management
operator|.
name|event
operator|.
name|ExchangeSendingEvent
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
name|management
operator|.
name|event
operator|.
name|ExchangeSentEvent
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
name|spi
operator|.
name|RoutePolicyFactory
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
name|support
operator|.
name|EventNotifierSupport
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
name|support
operator|.
name|RoutePolicySupport
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
name|support
operator|.
name|ServiceSupport
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ServiceHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * To use OpenTracing with Camel then setup this {@link OpenTracingTracer} in your Camel application.  *<p/>  * This class is implemented as both an {@link org.apache.camel.spi.EventNotifier} and {@link RoutePolicy} that allows  * to trap when Camel starts/ends an {@link Exchange} being routed using the {@link RoutePolicy} and during the routing  * if the {@link Exchange} sends messages, then we track them using the {@link org.apache.camel.spi.EventNotifier}.  */
end_comment

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"OpenTracingTracer"
argument_list|)
DECL|class|OpenTracingTracer
specifier|public
class|class
name|OpenTracingTracer
extends|extends
name|ServiceSupport
implements|implements
name|RoutePolicyFactory
implements|,
name|StaticService
implements|,
name|CamelContextAware
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|OpenTracingTracer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|decorators
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|SpanDecorator
argument_list|>
name|decorators
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|eventNotifier
specifier|private
specifier|final
name|OpenTracingEventNotifier
name|eventNotifier
init|=
operator|new
name|OpenTracingEventNotifier
argument_list|()
decl_stmt|;
DECL|field|spanManager
specifier|private
specifier|final
name|SpanManager
name|spanManager
init|=
name|DefaultSpanManager
operator|.
name|getInstance
argument_list|()
decl_stmt|;
DECL|field|tracer
specifier|private
name|Tracer
name|tracer
init|=
name|GlobalTracer
operator|.
name|get
argument_list|()
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
static|static
block|{
name|ServiceLoader
operator|.
name|load
argument_list|(
name|SpanDecorator
operator|.
name|class
argument_list|)
operator|.
name|forEach
argument_list|(
name|d
lambda|->
block|{
name|SpanDecorator
name|existing
init|=
name|decorators
operator|.
name|get
argument_list|(
name|d
operator|.
name|getComponent
argument_list|()
argument_list|)
decl_stmt|;
comment|// Add span decorator if no existing decorator for the component,
comment|// or if derived from the existing decorator's class, allowing
comment|// custom decorators to be added if they extend the standard
comment|// decorators
if|if
condition|(
name|existing
operator|==
literal|null
operator|||
name|existing
operator|.
name|getClass
argument_list|()
operator|.
name|isInstance
argument_list|(
name|d
argument_list|)
condition|)
block|{
name|decorators
operator|.
name|put
argument_list|(
name|d
operator|.
name|getComponent
argument_list|()
argument_list|,
name|d
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|OpenTracingTracer ()
specifier|public
name|OpenTracingTracer
parameter_list|()
block|{     }
annotation|@
name|Override
DECL|method|createRoutePolicy (CamelContext camelContext, String routeId, RouteDefinition route)
specifier|public
name|RoutePolicy
name|createRoutePolicy
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|routeId
parameter_list|,
name|RouteDefinition
name|route
parameter_list|)
block|{
comment|// ensure this opentracing tracer gets initialized when Camel starts
name|init
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
return|return
operator|new
name|OpenTracingRoutePolicy
argument_list|(
name|routeId
argument_list|)
return|;
block|}
comment|/**      * Registers this {@link OpenTracingTracer} on the {@link CamelContext} if not already registered.      */
DECL|method|init (CamelContext camelContext)
specifier|public
name|void
name|init
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
if|if
condition|(
operator|!
name|camelContext
operator|.
name|hasService
argument_list|(
name|this
argument_list|)
condition|)
block|{
try|try
block|{
comment|// start this service eager so we init before Camel is starting up
name|camelContext
operator|.
name|addService
argument_list|(
name|this
argument_list|,
literal|true
argument_list|,
literal|true
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
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
annotation|@
name|Override
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
DECL|method|getTracer ()
specifier|public
name|Tracer
name|getTracer
parameter_list|()
block|{
return|return
name|tracer
return|;
block|}
DECL|method|setTracer (Tracer tracer)
specifier|public
name|void
name|setTracer
parameter_list|(
name|Tracer
name|tracer
parameter_list|)
block|{
name|this
operator|.
name|tracer
operator|=
name|tracer
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"CamelContext"
argument_list|,
name|this
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|addEventNotifier
argument_list|(
name|eventNotifier
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|camelContext
operator|.
name|getRoutePolicyFactories
argument_list|()
operator|.
name|contains
argument_list|(
name|this
argument_list|)
condition|)
block|{
name|camelContext
operator|.
name|addRoutePolicyFactory
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|tracer
operator|==
literal|null
condition|)
block|{
name|tracer
operator|=
name|GlobalTracer
operator|.
name|get
argument_list|()
expr_stmt|;
block|}
name|ServiceHelper
operator|.
name|startServices
argument_list|(
name|eventNotifier
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// stop event notifier
name|camelContext
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|removeEventNotifier
argument_list|(
name|eventNotifier
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|eventNotifier
argument_list|)
expr_stmt|;
comment|// remove route policy
name|camelContext
operator|.
name|getRoutePolicyFactories
argument_list|()
operator|.
name|remove
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
DECL|method|getSpanDecorator (Endpoint endpoint)
specifier|protected
name|SpanDecorator
name|getSpanDecorator
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|SpanDecorator
name|sd
init|=
name|decorators
operator|.
name|get
argument_list|(
name|URI
operator|.
name|create
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
operator|.
name|getScheme
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|sd
operator|==
literal|null
condition|)
block|{
return|return
name|SpanDecorator
operator|.
name|DEFAULT
return|;
block|}
return|return
name|sd
return|;
block|}
DECL|class|OpenTracingEventNotifier
specifier|private
specifier|final
class|class
name|OpenTracingEventNotifier
extends|extends
name|EventNotifierSupport
block|{
annotation|@
name|Override
DECL|method|notify (EventObject event)
specifier|public
name|void
name|notify
parameter_list|(
name|EventObject
name|event
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|event
operator|instanceof
name|ExchangeSendingEvent
condition|)
block|{
name|ExchangeSendingEvent
name|ese
init|=
operator|(
name|ExchangeSendingEvent
operator|)
name|event
decl_stmt|;
name|SpanManager
operator|.
name|ManagedSpan
name|parent
init|=
name|spanManager
operator|.
name|current
argument_list|()
decl_stmt|;
name|SpanDecorator
name|sd
init|=
name|getSpanDecorator
argument_list|(
name|ese
operator|.
name|getEndpoint
argument_list|()
argument_list|)
decl_stmt|;
name|SpanBuilder
name|spanBuilder
init|=
name|tracer
operator|.
name|buildSpan
argument_list|(
name|sd
operator|.
name|getOperationName
argument_list|(
name|ese
operator|.
name|getExchange
argument_list|()
argument_list|,
name|ese
operator|.
name|getEndpoint
argument_list|()
argument_list|)
argument_list|)
operator|.
name|withTag
argument_list|(
name|Tags
operator|.
name|SPAN_KIND
operator|.
name|getKey
argument_list|()
argument_list|,
name|sd
operator|.
name|getInitiatorSpanKind
argument_list|()
argument_list|)
decl_stmt|;
comment|// Temporary workaround to avoid adding 'null' span as a parent
if|if
condition|(
name|parent
operator|!=
literal|null
operator|&&
name|parent
operator|.
name|getSpan
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|spanBuilder
operator|.
name|asChildOf
argument_list|(
name|parent
operator|.
name|getSpan
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|Span
name|span
init|=
name|spanBuilder
operator|.
name|start
argument_list|()
decl_stmt|;
name|sd
operator|.
name|pre
argument_list|(
name|span
argument_list|,
name|ese
operator|.
name|getExchange
argument_list|()
argument_list|,
name|ese
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
name|tracer
operator|.
name|inject
argument_list|(
name|span
operator|.
name|context
argument_list|()
argument_list|,
name|Format
operator|.
name|Builtin
operator|.
name|TEXT_MAP
argument_list|,
operator|new
name|CamelHeadersInjectAdapter
argument_list|(
name|ese
operator|.
name|getExchange
argument_list|()
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|spanManager
operator|.
name|activate
argument_list|(
name|span
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"OpenTracing: start client span="
operator|+
name|span
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|event
operator|instanceof
name|ExchangeSentEvent
condition|)
block|{
name|SpanManager
operator|.
name|ManagedSpan
name|managedSpan
init|=
name|spanManager
operator|.
name|current
argument_list|()
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"OpenTracing: start client span="
operator|+
name|managedSpan
operator|.
name|getSpan
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|SpanDecorator
name|sd
init|=
name|getSpanDecorator
argument_list|(
operator|(
operator|(
name|ExchangeSentEvent
operator|)
name|event
operator|)
operator|.
name|getEndpoint
argument_list|()
argument_list|)
decl_stmt|;
name|sd
operator|.
name|post
argument_list|(
name|managedSpan
operator|.
name|getSpan
argument_list|()
argument_list|,
operator|(
operator|(
name|ExchangeSentEvent
operator|)
name|event
operator|)
operator|.
name|getExchange
argument_list|()
argument_list|,
operator|(
operator|(
name|ExchangeSentEvent
operator|)
name|event
operator|)
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
name|managedSpan
operator|.
name|getSpan
argument_list|()
operator|.
name|finish
argument_list|()
expr_stmt|;
name|managedSpan
operator|.
name|deactivate
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|isEnabled (EventObject event)
specifier|public
name|boolean
name|isEnabled
parameter_list|(
name|EventObject
name|event
parameter_list|)
block|{
return|return
name|event
operator|instanceof
name|ExchangeSendingEvent
operator|||
name|event
operator|instanceof
name|ExchangeSentEvent
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"OpenTracingEventNotifier"
return|;
block|}
block|}
DECL|class|OpenTracingRoutePolicy
specifier|private
specifier|final
class|class
name|OpenTracingRoutePolicy
extends|extends
name|RoutePolicySupport
block|{
DECL|method|OpenTracingRoutePolicy (String routeId)
name|OpenTracingRoutePolicy
parameter_list|(
name|String
name|routeId
parameter_list|)
block|{         }
annotation|@
name|Override
DECL|method|onExchangeBegin (Route route, Exchange exchange)
specifier|public
name|void
name|onExchangeBegin
parameter_list|(
name|Route
name|route
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|SpanDecorator
name|sd
init|=
name|getSpanDecorator
argument_list|(
name|route
operator|.
name|getEndpoint
argument_list|()
argument_list|)
decl_stmt|;
name|Span
name|span
init|=
name|tracer
operator|.
name|buildSpan
argument_list|(
name|sd
operator|.
name|getOperationName
argument_list|(
name|exchange
argument_list|,
name|route
operator|.
name|getEndpoint
argument_list|()
argument_list|)
argument_list|)
operator|.
name|asChildOf
argument_list|(
name|tracer
operator|.
name|extract
argument_list|(
name|Format
operator|.
name|Builtin
operator|.
name|TEXT_MAP
argument_list|,
operator|new
name|CamelHeadersExtractAdapter
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
argument_list|)
argument_list|)
operator|.
name|withTag
argument_list|(
name|Tags
operator|.
name|SPAN_KIND
operator|.
name|getKey
argument_list|()
argument_list|,
name|sd
operator|.
name|getReceiverSpanKind
argument_list|()
argument_list|)
operator|.
name|start
argument_list|()
decl_stmt|;
name|sd
operator|.
name|pre
argument_list|(
name|span
argument_list|,
name|exchange
argument_list|,
name|route
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
name|spanManager
operator|.
name|activate
argument_list|(
name|span
argument_list|)
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"OpenTracing: start server span="
operator|+
name|span
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|onExchangeDone (Route route, Exchange exchange)
specifier|public
name|void
name|onExchangeDone
parameter_list|(
name|Route
name|route
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|SpanManager
operator|.
name|ManagedSpan
name|managedSpan
init|=
name|spanManager
operator|.
name|current
argument_list|()
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"OpenTracing: finish server span="
operator|+
name|managedSpan
operator|.
name|getSpan
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|SpanDecorator
name|sd
init|=
name|getSpanDecorator
argument_list|(
name|route
operator|.
name|getEndpoint
argument_list|()
argument_list|)
decl_stmt|;
name|sd
operator|.
name|post
argument_list|(
name|managedSpan
operator|.
name|getSpan
argument_list|()
argument_list|,
name|exchange
argument_list|,
name|route
operator|.
name|getEndpoint
argument_list|()
argument_list|)
expr_stmt|;
name|managedSpan
operator|.
name|getSpan
argument_list|()
operator|.
name|finish
argument_list|()
expr_stmt|;
name|managedSpan
operator|.
name|deactivate
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

