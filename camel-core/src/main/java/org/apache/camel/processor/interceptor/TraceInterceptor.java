begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.interceptor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|interceptor
package|;
end_package

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
name|List
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
name|AsyncCallback
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
name|Processor
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
name|impl
operator|.
name|AggregateRouteNode
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
name|impl
operator|.
name|DefaultRouteNode
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
name|impl
operator|.
name|DoCatchRouteNode
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
name|impl
operator|.
name|DoFinallyRouteNode
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
name|impl
operator|.
name|OnCompletionRouteNode
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
name|impl
operator|.
name|OnExceptionRouteNode
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
name|AggregateDefinition
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
name|CatchDefinition
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
name|Constants
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
name|FinallyDefinition
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
name|InterceptDefinition
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
name|OnCompletionDefinition
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
name|OnExceptionDefinition
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
name|ProcessorDefinition
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
name|ProcessorDefinitionHelper
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
name|processor
operator|.
name|CamelLogProcessor
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
name|processor
operator|.
name|DefaultMaskingFormatter
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
name|processor
operator|.
name|DelegateAsyncProcessor
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
name|ExchangeFormatter
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
name|InterceptStrategy
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
name|MaskingFormatter
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
name|RouteContext
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
name|TracedRouteNodes
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
comment|/**  * An interceptor for debugging and tracing routes  *  * @version   */
end_comment

begin_class
DECL|class|TraceInterceptor
specifier|public
class|class
name|TraceInterceptor
extends|extends
name|DelegateAsyncProcessor
implements|implements
name|ExchangeFormatter
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
name|TraceInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|logger
specifier|private
name|CamelLogProcessor
name|logger
decl_stmt|;
DECL|field|node
specifier|private
specifier|final
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|node
decl_stmt|;
DECL|field|tracer
specifier|private
specifier|final
name|Tracer
name|tracer
decl_stmt|;
DECL|field|formatter
specifier|private
name|TraceFormatter
name|formatter
decl_stmt|;
DECL|field|routeContext
specifier|private
name|RouteContext
name|routeContext
decl_stmt|;
DECL|field|traceHandlers
specifier|private
name|List
argument_list|<
name|TraceEventHandler
argument_list|>
name|traceHandlers
decl_stmt|;
DECL|method|TraceInterceptor (ProcessorDefinition<?> node, Processor target, TraceFormatter formatter, Tracer tracer)
specifier|public
name|TraceInterceptor
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|node
parameter_list|,
name|Processor
name|target
parameter_list|,
name|TraceFormatter
name|formatter
parameter_list|,
name|Tracer
name|tracer
parameter_list|)
block|{
name|super
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|this
operator|.
name|tracer
operator|=
name|tracer
expr_stmt|;
name|this
operator|.
name|node
operator|=
name|node
expr_stmt|;
name|this
operator|.
name|formatter
operator|=
name|formatter
expr_stmt|;
name|this
operator|.
name|logger
operator|=
name|tracer
operator|.
name|getLogger
argument_list|(
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|tracer
operator|.
name|getFormatter
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|formatter
operator|=
name|tracer
operator|.
name|getFormatter
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|traceHandlers
operator|=
name|tracer
operator|.
name|getTraceHandlers
argument_list|()
expr_stmt|;
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
literal|"TraceInterceptor["
operator|+
name|node
operator|+
literal|"]"
return|;
block|}
DECL|method|setRouteContext (RouteContext routeContext)
specifier|public
name|void
name|setRouteContext
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
name|this
operator|.
name|routeContext
operator|=
name|routeContext
expr_stmt|;
name|prepareMaskingFormatter
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
block|}
DECL|method|prepareMaskingFormatter (RouteContext routeContext)
specifier|private
name|void
name|prepareMaskingFormatter
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
if|if
condition|(
name|routeContext
operator|.
name|isLogMask
argument_list|()
condition|)
block|{
name|MaskingFormatter
name|formatter
init|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
name|Constants
operator|.
name|CUSTOM_LOG_MASK_REF
argument_list|,
name|MaskingFormatter
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|formatter
operator|==
literal|null
condition|)
block|{
name|formatter
operator|=
operator|new
name|DefaultMaskingFormatter
argument_list|()
expr_stmt|;
block|}
name|logger
operator|.
name|setMaskingFormatter
argument_list|(
name|formatter
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|process (final Exchange exchange, final AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{
comment|// do not trace if tracing is disabled
if|if
condition|(
operator|!
name|tracer
operator|.
name|isEnabled
argument_list|()
operator|||
operator|(
name|routeContext
operator|!=
literal|null
operator|&&
operator|!
name|routeContext
operator|.
name|isTracing
argument_list|()
operator|)
condition|)
block|{
return|return
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
comment|// interceptor will also trace routes supposed only for TraceEvents so we need to skip
comment|// logging TraceEvents to avoid infinite looping
if|if
condition|(
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|TRACE_EVENT
argument_list|,
literal|false
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
condition|)
block|{
comment|// but we must still process to allow routing of TraceEvents to eg a JPA endpoint
return|return
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
specifier|final
name|boolean
name|shouldLog
init|=
name|shouldLogNode
argument_list|(
name|node
argument_list|)
operator|&&
name|shouldLogExchange
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
comment|// whether we should trace it or not, some nodes should be skipped as they are abstract
comment|// intermediate steps for instance related to on completion
name|boolean
name|trace
init|=
literal|true
decl_stmt|;
name|boolean
name|sync
init|=
literal|true
decl_stmt|;
comment|// okay this is a regular exchange being routed we might need to log and trace
try|try
block|{
comment|// before
if|if
condition|(
name|shouldLog
condition|)
block|{
comment|// traced holds the information about the current traced route path
if|if
condition|(
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|TracedRouteNodes
name|traced
init|=
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|.
name|getTracedRouteNodes
argument_list|()
decl_stmt|;
if|if
condition|(
name|node
operator|instanceof
name|OnCompletionDefinition
operator|||
name|node
operator|instanceof
name|OnExceptionDefinition
condition|)
block|{
comment|// skip any of these as its just a marker definition
name|trace
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ProcessorDefinitionHelper
operator|.
name|isFirstChildOfType
argument_list|(
name|OnCompletionDefinition
operator|.
name|class
argument_list|,
name|node
argument_list|)
condition|)
block|{
comment|// special for on completion tracing
name|traceOnCompletion
argument_list|(
name|traced
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ProcessorDefinitionHelper
operator|.
name|isFirstChildOfType
argument_list|(
name|OnExceptionDefinition
operator|.
name|class
argument_list|,
name|node
argument_list|)
condition|)
block|{
comment|// special for on exception
name|traceOnException
argument_list|(
name|traced
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ProcessorDefinitionHelper
operator|.
name|isFirstChildOfType
argument_list|(
name|CatchDefinition
operator|.
name|class
argument_list|,
name|node
argument_list|)
condition|)
block|{
comment|// special for do catch
name|traceDoCatch
argument_list|(
name|traced
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ProcessorDefinitionHelper
operator|.
name|isFirstChildOfType
argument_list|(
name|FinallyDefinition
operator|.
name|class
argument_list|,
name|node
argument_list|)
condition|)
block|{
comment|// special for do finally
name|traceDoFinally
argument_list|(
name|traced
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ProcessorDefinitionHelper
operator|.
name|isFirstChildOfType
argument_list|(
name|AggregateDefinition
operator|.
name|class
argument_list|,
name|node
argument_list|)
condition|)
block|{
comment|// special for aggregate
name|traceAggregate
argument_list|(
name|traced
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// regular so just add it
name|traced
operator|.
name|addTraced
argument_list|(
operator|new
name|DefaultRouteNode
argument_list|(
name|node
argument_list|,
name|super
operator|.
name|getProcessor
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Cannot trace as this Exchange does not have an UnitOfWork: {}"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
comment|// log and trace the processor
name|Object
name|state
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|shouldLog
operator|&&
name|trace
condition|)
block|{
name|logExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
comment|// either call the in or generic trace method depending on OUT has been enabled or not
if|if
condition|(
name|tracer
operator|.
name|isTraceOutExchanges
argument_list|()
condition|)
block|{
name|state
operator|=
name|traceExchangeIn
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|traceExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
specifier|final
name|Object
name|traceState
init|=
name|state
decl_stmt|;
comment|// special for interceptor where we need to keep booking how far we have routed in the intercepted processors
if|if
condition|(
name|node
operator|.
name|getParent
argument_list|()
operator|instanceof
name|InterceptDefinition
operator|&&
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|TracedRouteNodes
name|traced
init|=
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|.
name|getTracedRouteNodes
argument_list|()
decl_stmt|;
name|traceIntercept
argument_list|(
operator|(
name|InterceptDefinition
operator|)
name|node
operator|.
name|getParent
argument_list|()
argument_list|,
name|traced
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|// process the exchange
name|sync
operator|=
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
try|try
block|{
comment|// after (trace out)
if|if
condition|(
name|shouldLog
operator|&&
name|tracer
operator|.
name|isTraceOutExchanges
argument_list|()
condition|)
block|{
name|logExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|traceExchangeOut
argument_list|(
name|exchange
argument_list|,
name|traceState
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// some exception occurred in trace logic
if|if
condition|(
name|shouldLogException
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
name|logException
argument_list|(
name|exchange
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// ensure callback is always invoked
name|callback
operator|.
name|done
argument_list|(
name|doneSync
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// some exception occurred in trace logic
if|if
condition|(
name|shouldLogException
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
name|logException
argument_list|(
name|exchange
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|sync
return|;
block|}
DECL|method|traceOnCompletion (TracedRouteNodes traced, Exchange exchange)
specifier|private
name|void
name|traceOnCompletion
parameter_list|(
name|TracedRouteNodes
name|traced
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|traced
operator|.
name|addTraced
argument_list|(
operator|new
name|OnCompletionRouteNode
argument_list|()
argument_list|)
expr_stmt|;
comment|// do not log and trace as onCompletion should be a new event on its own
comment|// add the next step as well so we have onCompletion -> new step
name|traced
operator|.
name|addTraced
argument_list|(
operator|new
name|DefaultRouteNode
argument_list|(
name|node
argument_list|,
name|super
operator|.
name|getProcessor
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|traceOnException (TracedRouteNodes traced, Exchange exchange)
specifier|private
name|void
name|traceOnException
parameter_list|(
name|TracedRouteNodes
name|traced
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|traced
operator|.
name|getLastNode
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|traced
operator|.
name|addTraced
argument_list|(
operator|new
name|DefaultRouteNode
argument_list|(
name|traced
operator|.
name|getLastNode
argument_list|()
operator|.
name|getProcessorDefinition
argument_list|()
argument_list|,
name|traced
operator|.
name|getLastNode
argument_list|()
operator|.
name|getProcessor
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|traced
operator|.
name|addTraced
argument_list|(
operator|new
name|OnExceptionRouteNode
argument_list|()
argument_list|)
expr_stmt|;
comment|// log and trace so we have the from -> onException event as well
name|logExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|traceExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|traced
operator|.
name|addTraced
argument_list|(
operator|new
name|DefaultRouteNode
argument_list|(
name|node
argument_list|,
name|super
operator|.
name|getProcessor
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|traceDoCatch (TracedRouteNodes traced, Exchange exchange)
specifier|private
name|void
name|traceDoCatch
parameter_list|(
name|TracedRouteNodes
name|traced
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|traced
operator|.
name|getLastNode
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|traced
operator|.
name|addTraced
argument_list|(
operator|new
name|DefaultRouteNode
argument_list|(
name|traced
operator|.
name|getLastNode
argument_list|()
operator|.
name|getProcessorDefinition
argument_list|()
argument_list|,
name|traced
operator|.
name|getLastNode
argument_list|()
operator|.
name|getProcessor
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|traced
operator|.
name|addTraced
argument_list|(
operator|new
name|DoCatchRouteNode
argument_list|()
argument_list|)
expr_stmt|;
comment|// log and trace so we have the from -> doCatch event as well
name|logExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|traceExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|traced
operator|.
name|addTraced
argument_list|(
operator|new
name|DefaultRouteNode
argument_list|(
name|node
argument_list|,
name|super
operator|.
name|getProcessor
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|traceDoFinally (TracedRouteNodes traced, Exchange exchange)
specifier|private
name|void
name|traceDoFinally
parameter_list|(
name|TracedRouteNodes
name|traced
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|traced
operator|.
name|getLastNode
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|traced
operator|.
name|addTraced
argument_list|(
operator|new
name|DefaultRouteNode
argument_list|(
name|traced
operator|.
name|getLastNode
argument_list|()
operator|.
name|getProcessorDefinition
argument_list|()
argument_list|,
name|traced
operator|.
name|getLastNode
argument_list|()
operator|.
name|getProcessor
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|traced
operator|.
name|addTraced
argument_list|(
operator|new
name|DoFinallyRouteNode
argument_list|()
argument_list|)
expr_stmt|;
comment|// log and trace so we have the from -> doFinally event as well
name|logExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|traceExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|traced
operator|.
name|addTraced
argument_list|(
operator|new
name|DefaultRouteNode
argument_list|(
name|node
argument_list|,
name|super
operator|.
name|getProcessor
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|traceAggregate (TracedRouteNodes traced, Exchange exchange)
specifier|private
name|void
name|traceAggregate
parameter_list|(
name|TracedRouteNodes
name|traced
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|traced
operator|.
name|addTraced
argument_list|(
operator|new
name|AggregateRouteNode
argument_list|(
operator|(
name|AggregateDefinition
operator|)
name|node
operator|.
name|getParent
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|traced
operator|.
name|addTraced
argument_list|(
operator|new
name|DefaultRouteNode
argument_list|(
name|node
argument_list|,
name|super
operator|.
name|getProcessor
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|traceIntercept (InterceptDefinition intercept, TracedRouteNodes traced, Exchange exchange)
specifier|protected
name|void
name|traceIntercept
parameter_list|(
name|InterceptDefinition
name|intercept
parameter_list|,
name|TracedRouteNodes
name|traced
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// use the counter to get the index of the intercepted processor to be traced
name|Processor
name|last
init|=
name|intercept
operator|.
name|getInterceptedProcessor
argument_list|(
name|traced
operator|.
name|getAndIncrementCounter
argument_list|(
name|intercept
argument_list|)
argument_list|)
decl_stmt|;
comment|// skip doing any double tracing of interceptors, so the last must not be a TraceInterceptor instance
if|if
condition|(
name|last
operator|!=
literal|null
operator|&&
operator|!
operator|(
name|last
operator|instanceof
name|TraceInterceptor
operator|)
condition|)
block|{
name|traced
operator|.
name|addTraced
argument_list|(
operator|new
name|DefaultRouteNode
argument_list|(
name|node
argument_list|,
name|last
argument_list|)
argument_list|)
expr_stmt|;
name|boolean
name|shouldLog
init|=
name|shouldLogNode
argument_list|(
name|node
argument_list|)
operator|&&
name|shouldLogExchange
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|shouldLog
condition|)
block|{
comment|// log and trace the processor that was intercepted so we can see it
name|logExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|traceExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|format (Exchange exchange)
specifier|public
name|String
name|format
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Object
name|msg
init|=
name|formatter
operator|.
name|format
argument_list|(
name|this
argument_list|,
name|this
operator|.
name|getNode
argument_list|()
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|msg
operator|!=
literal|null
condition|)
block|{
return|return
name|msg
operator|.
name|toString
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
comment|// Properties
comment|//-------------------------------------------------------------------------
DECL|method|getNode ()
specifier|public
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|getNode
parameter_list|()
block|{
return|return
name|node
return|;
block|}
DECL|method|getLogger ()
specifier|public
name|CamelLogProcessor
name|getLogger
parameter_list|()
block|{
return|return
name|logger
return|;
block|}
DECL|method|getFormatter ()
specifier|public
name|TraceFormatter
name|getFormatter
parameter_list|()
block|{
return|return
name|formatter
return|;
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
DECL|method|logExchange (Exchange exchange)
specifier|protected
name|void
name|logExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
comment|// process the exchange that formats and logs it
name|logger
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|traceExchange (Exchange exchange)
specifier|protected
name|void
name|traceExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|TraceEventHandler
name|traceHandler
range|:
name|traceHandlers
control|)
block|{
name|traceHandler
operator|.
name|traceExchange
argument_list|(
name|node
argument_list|,
name|processor
argument_list|,
name|this
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|traceExchangeIn (Exchange exchange)
specifier|protected
name|Object
name|traceExchangeIn
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|result
init|=
literal|null
decl_stmt|;
for|for
control|(
name|TraceEventHandler
name|traceHandler
range|:
name|traceHandlers
control|)
block|{
name|Object
name|result1
init|=
name|traceHandler
operator|.
name|traceExchangeIn
argument_list|(
name|node
argument_list|,
name|processor
argument_list|,
name|this
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|result1
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
name|result1
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
DECL|method|traceExchangeOut (Exchange exchange, Object traceState)
specifier|protected
name|void
name|traceExchangeOut
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|traceState
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|TraceEventHandler
name|traceHandler
range|:
name|traceHandlers
control|)
block|{
name|traceHandler
operator|.
name|traceExchangeOut
argument_list|(
name|node
argument_list|,
name|processor
argument_list|,
name|this
argument_list|,
name|exchange
argument_list|,
name|traceState
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|logException (Exchange exchange, Throwable throwable)
specifier|protected
name|void
name|logException
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Throwable
name|throwable
parameter_list|)
block|{
if|if
condition|(
name|tracer
operator|.
name|isTraceExceptions
argument_list|()
condition|)
block|{
if|if
condition|(
name|tracer
operator|.
name|isLogStackTrace
argument_list|()
condition|)
block|{
name|logger
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|throwable
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|logger
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
literal|", Exception: "
operator|+
name|throwable
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Returns true if the given exchange should be logged in the trace list      */
DECL|method|shouldLogExchange (Exchange exchange)
specifier|protected
name|boolean
name|shouldLogExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|tracer
operator|.
name|isEnabled
argument_list|()
operator|&&
operator|(
name|tracer
operator|.
name|getTraceFilter
argument_list|()
operator|==
literal|null
operator|||
name|tracer
operator|.
name|getTraceFilter
argument_list|()
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
operator|)
return|;
block|}
comment|/**      * Returns true if the given exchange should be logged when an exception was thrown      */
DECL|method|shouldLogException (Exchange exchange)
specifier|protected
name|boolean
name|shouldLogException
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|tracer
operator|.
name|isTraceExceptions
argument_list|()
return|;
block|}
comment|/**      * Returns whether exchanges coming out of processors should be traced      */
DECL|method|shouldTraceOutExchanges ()
specifier|public
name|boolean
name|shouldTraceOutExchanges
parameter_list|()
block|{
return|return
name|tracer
operator|.
name|isTraceOutExchanges
argument_list|()
return|;
block|}
comment|/**      * Returns true if the given node should be logged in the trace list      */
DECL|method|shouldLogNode (ProcessorDefinition<?> node)
specifier|protected
name|boolean
name|shouldLogNode
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|node
parameter_list|)
block|{
if|if
condition|(
name|node
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
name|tracer
operator|.
name|isTraceInterceptors
argument_list|()
operator|&&
operator|(
name|node
operator|instanceof
name|InterceptStrategy
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|traceHandlers
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
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|traceHandlers
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Deprecated
DECL|method|setTraceHandler (TraceEventHandler traceHandler)
specifier|public
name|void
name|setTraceHandler
parameter_list|(
name|TraceEventHandler
name|traceHandler
parameter_list|)
block|{
name|traceHandlers
operator|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|traceHandler
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

