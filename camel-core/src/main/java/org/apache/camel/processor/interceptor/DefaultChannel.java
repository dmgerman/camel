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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|AsyncProcessor
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
name|Channel
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
name|Service
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
name|ModelChannel
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
name|RouteDefinitionHelper
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
name|CamelInternalProcessor
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
name|InterceptorToAsyncProcessorBridge
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
name|WrapProcessor
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
name|LifecycleStrategy
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
name|util
operator|.
name|OrderedComparator
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
comment|/**  * DefaultChannel is the default {@link Channel}.  *<p/>  * The current implementation is just a composite containing the interceptors and error handler  * that beforehand was added to the route graph directly.  *<br/>  * With this {@link Channel} we can in the future implement better strategies for routing the  * {@link Exchange} in the route graph, as we have a {@link Channel} between each and every node  * in the graph.  *  * @version   */
end_comment

begin_class
DECL|class|DefaultChannel
specifier|public
class|class
name|DefaultChannel
extends|extends
name|CamelInternalProcessor
implements|implements
name|ModelChannel
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DefaultChannel
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|interceptors
specifier|private
specifier|final
name|List
argument_list|<
name|InterceptStrategy
argument_list|>
name|interceptors
init|=
operator|new
name|ArrayList
argument_list|<
name|InterceptStrategy
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|errorHandler
specifier|private
name|Processor
name|errorHandler
decl_stmt|;
comment|// the next processor (non wrapped)
DECL|field|nextProcessor
specifier|private
name|Processor
name|nextProcessor
decl_stmt|;
comment|// the real output to invoke that has been wrapped
DECL|field|output
specifier|private
name|Processor
name|output
decl_stmt|;
DECL|field|definition
specifier|private
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
decl_stmt|;
DECL|field|childDefinition
specifier|private
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|childDefinition
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|routeContext
specifier|private
name|RouteContext
name|routeContext
decl_stmt|;
DECL|method|setNextProcessor (Processor next)
specifier|public
name|void
name|setNextProcessor
parameter_list|(
name|Processor
name|next
parameter_list|)
block|{
name|this
operator|.
name|nextProcessor
operator|=
name|next
expr_stmt|;
block|}
DECL|method|getOutput ()
specifier|public
name|Processor
name|getOutput
parameter_list|()
block|{
comment|// the errorHandler is already decorated with interceptors
comment|// so it contain the entire chain of processors, so we can safely use it directly as output
comment|// if no error handler provided we use the output
comment|// TODO: Camel 3.0 we should determine the output dynamically at runtime instead of having the
comment|// the error handlers, interceptors, etc. woven in at design time
return|return
name|errorHandler
operator|!=
literal|null
condition|?
name|errorHandler
else|:
name|output
return|;
block|}
annotation|@
name|Override
DECL|method|hasNext ()
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|nextProcessor
operator|!=
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|next ()
specifier|public
name|List
argument_list|<
name|Processor
argument_list|>
name|next
parameter_list|()
block|{
if|if
condition|(
operator|!
name|hasNext
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
argument_list|<
name|Processor
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|Processor
argument_list|>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|nextProcessor
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|setOutput (Processor output)
specifier|public
name|void
name|setOutput
parameter_list|(
name|Processor
name|output
parameter_list|)
block|{
name|this
operator|.
name|output
operator|=
name|output
expr_stmt|;
block|}
DECL|method|getNextProcessor ()
specifier|public
name|Processor
name|getNextProcessor
parameter_list|()
block|{
return|return
name|nextProcessor
return|;
block|}
DECL|method|hasInterceptorStrategy (Class<?> type)
specifier|public
name|boolean
name|hasInterceptorStrategy
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
for|for
control|(
name|InterceptStrategy
name|strategy
range|:
name|interceptors
control|)
block|{
if|if
condition|(
name|type
operator|.
name|isInstance
argument_list|(
name|strategy
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|setErrorHandler (Processor errorHandler)
specifier|public
name|void
name|setErrorHandler
parameter_list|(
name|Processor
name|errorHandler
parameter_list|)
block|{
name|this
operator|.
name|errorHandler
operator|=
name|errorHandler
expr_stmt|;
block|}
DECL|method|getErrorHandler ()
specifier|public
name|Processor
name|getErrorHandler
parameter_list|()
block|{
return|return
name|errorHandler
return|;
block|}
DECL|method|addInterceptStrategy (InterceptStrategy strategy)
specifier|public
name|void
name|addInterceptStrategy
parameter_list|(
name|InterceptStrategy
name|strategy
parameter_list|)
block|{
name|interceptors
operator|.
name|add
argument_list|(
name|strategy
argument_list|)
expr_stmt|;
block|}
DECL|method|addInterceptStrategies (List<InterceptStrategy> strategies)
specifier|public
name|void
name|addInterceptStrategies
parameter_list|(
name|List
argument_list|<
name|InterceptStrategy
argument_list|>
name|strategies
parameter_list|)
block|{
name|interceptors
operator|.
name|addAll
argument_list|(
name|strategies
argument_list|)
expr_stmt|;
block|}
DECL|method|getInterceptStrategies ()
specifier|public
name|List
argument_list|<
name|InterceptStrategy
argument_list|>
name|getInterceptStrategies
parameter_list|()
block|{
return|return
name|interceptors
return|;
block|}
DECL|method|getProcessorDefinition ()
specifier|public
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|getProcessorDefinition
parameter_list|()
block|{
return|return
name|definition
return|;
block|}
DECL|method|setChildDefinition (ProcessorDefinition<?> childDefinition)
specifier|public
name|void
name|setChildDefinition
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|childDefinition
parameter_list|)
block|{
name|this
operator|.
name|childDefinition
operator|=
name|childDefinition
expr_stmt|;
block|}
DECL|method|getRouteContext ()
specifier|public
name|RouteContext
name|getRouteContext
parameter_list|()
block|{
return|return
name|routeContext
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
comment|// the output has now been created, so assign the output as the processor
name|setProcessor
argument_list|(
name|getOutput
argument_list|()
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|startServices
argument_list|(
name|errorHandler
argument_list|,
name|output
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
name|ServiceHelper
operator|.
name|stopServices
argument_list|(
name|output
argument_list|,
name|errorHandler
argument_list|)
expr_stmt|;
block|}
DECL|method|initChannel (ProcessorDefinition<?> outputDefinition, RouteContext routeContext)
specifier|public
name|void
name|initChannel
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|outputDefinition
parameter_list|,
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|routeContext
operator|=
name|routeContext
expr_stmt|;
name|this
operator|.
name|definition
operator|=
name|outputDefinition
expr_stmt|;
name|this
operator|.
name|camelContext
operator|=
name|routeContext
operator|.
name|getCamelContext
argument_list|()
expr_stmt|;
name|Processor
name|target
init|=
name|nextProcessor
decl_stmt|;
name|Processor
name|next
decl_stmt|;
comment|// init CamelContextAware as early as possible on target
if|if
condition|(
name|target
operator|instanceof
name|CamelContextAware
condition|)
block|{
operator|(
operator|(
name|CamelContextAware
operator|)
name|target
operator|)
operator|.
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
block|}
comment|// the definition to wrap should be the fine grained,
comment|// so if a child is set then use it, if not then its the original output used
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|targetOutputDef
init|=
name|childDefinition
operator|!=
literal|null
condition|?
name|childDefinition
else|:
name|outputDefinition
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Initialize channel for target: '{}'"
argument_list|,
name|targetOutputDef
argument_list|)
expr_stmt|;
comment|// fix parent/child relationship. This will be the case of the routes has been
comment|// defined using XML DSL or end user may have manually assembled a route from the model.
comment|// Background note: parent/child relationship is assembled on-the-fly when using Java DSL (fluent builders)
comment|// where as when using XML DSL (JAXB) then it fixed after, but if people are using custom interceptors
comment|// then we need to fix the parent/child relationship beforehand, and thus we can do it here
comment|// ideally we need the design time route -> runtime route to be a 2-phase pass (scheduled work for Camel 3.0)
if|if
condition|(
name|childDefinition
operator|!=
literal|null
operator|&&
name|outputDefinition
operator|!=
name|childDefinition
condition|)
block|{
name|childDefinition
operator|.
name|setParent
argument_list|(
name|outputDefinition
argument_list|)
expr_stmt|;
block|}
comment|// force the creation of an id
name|RouteDefinitionHelper
operator|.
name|forceAssignIds
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|definition
argument_list|)
expr_stmt|;
comment|// first wrap the output with the managed strategy if any
name|InterceptStrategy
name|managed
init|=
name|routeContext
operator|.
name|getManagedInterceptStrategy
argument_list|()
decl_stmt|;
if|if
condition|(
name|managed
operator|!=
literal|null
condition|)
block|{
name|next
operator|=
name|target
operator|==
name|nextProcessor
condition|?
literal|null
else|:
name|nextProcessor
expr_stmt|;
name|target
operator|=
name|managed
operator|.
name|wrapProcessorInInterceptors
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|targetOutputDef
argument_list|,
name|target
argument_list|,
name|next
argument_list|)
expr_stmt|;
block|}
comment|// then wrap the output with the backlog and tracer (backlog first, as we do not want regular tracer to tracer the backlog)
name|InterceptStrategy
name|tracer
init|=
name|getOrCreateBacklogTracer
argument_list|()
decl_stmt|;
if|if
condition|(
name|tracer
operator|instanceof
name|BacklogTracer
condition|)
block|{
name|BacklogTracer
name|backlogTracer
init|=
operator|(
name|BacklogTracer
operator|)
name|tracer
decl_stmt|;
name|backlogTracer
operator|.
name|addDefinition
argument_list|(
name|targetOutputDef
argument_list|)
expr_stmt|;
name|RouteDefinition
name|route
init|=
name|ProcessorDefinitionHelper
operator|.
name|getRoute
argument_list|(
name|definition
argument_list|)
decl_stmt|;
name|boolean
name|first
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|route
operator|!=
literal|null
operator|&&
operator|!
name|route
operator|.
name|getOutputs
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|first
operator|=
name|route
operator|.
name|getOutputs
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|==
name|definition
expr_stmt|;
block|}
name|addTask
argument_list|(
operator|new
name|CamelInternalProcessor
operator|.
name|BacklogTracerTask
argument_list|(
name|backlogTracer
operator|.
name|getQueue
argument_list|()
argument_list|,
name|backlogTracer
argument_list|,
name|targetOutputDef
argument_list|,
name|route
argument_list|,
name|first
argument_list|)
argument_list|)
expr_stmt|;
comment|// add debugger as well so we have both tracing and debugging out of the box
name|InterceptStrategy
name|debugger
init|=
name|getOrCreateBacklogDebugger
argument_list|()
decl_stmt|;
if|if
condition|(
name|debugger
operator|instanceof
name|BacklogDebugger
condition|)
block|{
name|BacklogDebugger
name|backlogDebugger
init|=
operator|(
name|BacklogDebugger
operator|)
name|debugger
decl_stmt|;
name|addTask
argument_list|(
operator|new
name|CamelInternalProcessor
operator|.
name|BacklogDebuggerTask
argument_list|(
name|backlogDebugger
argument_list|,
name|target
argument_list|,
name|targetOutputDef
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// the regular tracer is not a task on internalProcessor as this is not really needed
comment|// end users have to explicit enable the tracer to use it, and then its okay if we wrap
comment|// the processors (but by default tracer is disabled, and therefore we do not wrap processors)
name|tracer
operator|=
name|getOrCreateTracer
argument_list|()
expr_stmt|;
if|if
condition|(
name|tracer
operator|!=
literal|null
condition|)
block|{
name|TraceInterceptor
name|trace
init|=
operator|(
name|TraceInterceptor
operator|)
name|tracer
operator|.
name|wrapProcessorInInterceptors
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|targetOutputDef
argument_list|,
name|target
argument_list|,
literal|null
argument_list|)
decl_stmt|;
comment|// trace interceptor need to have a reference to route context so we at runtime can enable/disable tracing on-the-fly
name|trace
operator|.
name|setRouteContext
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
name|target
operator|=
name|trace
expr_stmt|;
block|}
comment|// sort interceptors according to ordered
name|Collections
operator|.
name|sort
argument_list|(
name|interceptors
argument_list|,
operator|new
name|OrderedComparator
argument_list|()
argument_list|)
expr_stmt|;
comment|// then reverse list so the first will be wrapped last, as it would then be first being invoked
name|Collections
operator|.
name|reverse
argument_list|(
name|interceptors
argument_list|)
expr_stmt|;
comment|// wrap the output with the configured interceptors
for|for
control|(
name|InterceptStrategy
name|strategy
range|:
name|interceptors
control|)
block|{
name|next
operator|=
name|target
operator|==
name|nextProcessor
condition|?
literal|null
else|:
name|nextProcessor
expr_stmt|;
comment|// skip tracer as we did the specially beforehand and it could potentially be added as an interceptor strategy
if|if
condition|(
name|strategy
operator|instanceof
name|Tracer
condition|)
block|{
continue|continue;
block|}
comment|// skip stream caching as it must be wrapped as outer most, which we do later
if|if
condition|(
name|strategy
operator|instanceof
name|StreamCaching
condition|)
block|{
continue|continue;
block|}
comment|// use the fine grained definition (eg the child if available). Its always possible to get back to the parent
name|Processor
name|wrapped
init|=
name|strategy
operator|.
name|wrapProcessorInInterceptors
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|targetOutputDef
argument_list|,
name|target
argument_list|,
name|next
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|wrapped
operator|instanceof
name|AsyncProcessor
operator|)
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Interceptor: "
operator|+
name|strategy
operator|+
literal|" at: "
operator|+
name|outputDefinition
operator|+
literal|" does not return an AsyncProcessor instance."
operator|+
literal|" This causes the asynchronous routing engine to not work as optimal as possible."
operator|+
literal|" See more details at the InterceptStrategy javadoc."
operator|+
literal|" Camel will use a bridge to adapt the interceptor to the asynchronous routing engine,"
operator|+
literal|" but its not the most optimal solution. Please consider changing your interceptor to comply."
argument_list|)
expr_stmt|;
comment|// use a bridge and wrap again which allows us to adapt and leverage the asynchronous routing engine anyway
comment|// however its not the most optimal solution, but we can still run.
name|InterceptorToAsyncProcessorBridge
name|bridge
init|=
operator|new
name|InterceptorToAsyncProcessorBridge
argument_list|(
name|target
argument_list|)
decl_stmt|;
name|wrapped
operator|=
name|strategy
operator|.
name|wrapProcessorInInterceptors
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|targetOutputDef
argument_list|,
name|bridge
argument_list|,
name|next
argument_list|)
expr_stmt|;
name|bridge
operator|.
name|setTarget
argument_list|(
name|wrapped
argument_list|)
expr_stmt|;
name|wrapped
operator|=
name|bridge
expr_stmt|;
block|}
if|if
condition|(
operator|!
operator|(
name|wrapped
operator|instanceof
name|WrapProcessor
operator|)
condition|)
block|{
comment|// wrap the target so it becomes a service and we can manage its lifecycle
name|wrapped
operator|=
operator|new
name|WrapProcessor
argument_list|(
name|wrapped
argument_list|,
name|target
argument_list|)
expr_stmt|;
block|}
name|target
operator|=
name|wrapped
expr_stmt|;
block|}
comment|// sets the delegate to our wrapped output
name|output
operator|=
name|target
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|postInitChannel (ProcessorDefinition<?> outputDefinition, RouteContext routeContext)
specifier|public
name|void
name|postInitChannel
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|outputDefinition
parameter_list|,
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|InterceptStrategy
name|strategy
range|:
name|interceptors
control|)
block|{
comment|// apply stream caching at the end as it should be outer most
if|if
condition|(
name|strategy
operator|instanceof
name|StreamCaching
condition|)
block|{
if|if
condition|(
name|errorHandler
operator|!=
literal|null
condition|)
block|{
name|errorHandler
operator|=
name|strategy
operator|.
name|wrapProcessorInInterceptors
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|outputDefinition
argument_list|,
name|errorHandler
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|output
operator|=
name|strategy
operator|.
name|wrapProcessorInInterceptors
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|outputDefinition
argument_list|,
name|output
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
break|break;
block|}
block|}
block|}
DECL|method|getOrCreateTracer ()
specifier|private
name|InterceptStrategy
name|getOrCreateTracer
parameter_list|()
block|{
comment|// only use tracer if explicit enabled
if|if
condition|(
name|camelContext
operator|.
name|isTracing
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|camelContext
operator|.
name|isTracing
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|InterceptStrategy
name|tracer
init|=
name|Tracer
operator|.
name|getTracer
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
if|if
condition|(
name|tracer
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|camelContext
operator|.
name|getRegistry
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// lookup in registry
name|Map
argument_list|<
name|String
argument_list|,
name|Tracer
argument_list|>
name|map
init|=
name|camelContext
operator|.
name|getRegistry
argument_list|()
operator|.
name|findByTypeWithName
argument_list|(
name|Tracer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|map
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|tracer
operator|=
name|map
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|tracer
operator|==
literal|null
condition|)
block|{
comment|// fallback to use the default tracer
name|tracer
operator|=
name|camelContext
operator|.
name|getDefaultTracer
argument_list|()
expr_stmt|;
comment|// configure and use any trace formatter if any exists
name|Map
argument_list|<
name|String
argument_list|,
name|TraceFormatter
argument_list|>
name|formatters
init|=
name|camelContext
operator|.
name|getRegistry
argument_list|()
operator|.
name|findByTypeWithName
argument_list|(
name|TraceFormatter
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|formatters
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|TraceFormatter
name|formatter
init|=
name|formatters
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|tracer
operator|instanceof
name|Tracer
condition|)
block|{
operator|(
operator|(
name|Tracer
operator|)
name|tracer
operator|)
operator|.
name|setFormatter
argument_list|(
name|formatter
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|// which we must manage as well
for|for
control|(
name|LifecycleStrategy
name|strategy
range|:
name|camelContext
operator|.
name|getLifecycleStrategies
argument_list|()
control|)
block|{
if|if
condition|(
name|tracer
operator|instanceof
name|Service
condition|)
block|{
name|strategy
operator|.
name|onServiceAdd
argument_list|(
name|camelContext
argument_list|,
operator|(
name|Service
operator|)
name|tracer
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|tracer
return|;
block|}
DECL|method|getOrCreateBacklogTracer ()
specifier|private
name|InterceptStrategy
name|getOrCreateBacklogTracer
parameter_list|()
block|{
name|InterceptStrategy
name|tracer
init|=
name|BacklogTracer
operator|.
name|getBacklogTracer
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
if|if
condition|(
name|tracer
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|camelContext
operator|.
name|getRegistry
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// lookup in registry
name|Map
argument_list|<
name|String
argument_list|,
name|BacklogTracer
argument_list|>
name|map
init|=
name|camelContext
operator|.
name|getRegistry
argument_list|()
operator|.
name|findByTypeWithName
argument_list|(
name|BacklogTracer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|map
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|tracer
operator|=
name|map
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|tracer
operator|==
literal|null
condition|)
block|{
comment|// fallback to use the default tracer
name|tracer
operator|=
name|camelContext
operator|.
name|getDefaultBacklogTracer
argument_list|()
expr_stmt|;
block|}
block|}
comment|// which we must manage as well
for|for
control|(
name|LifecycleStrategy
name|strategy
range|:
name|camelContext
operator|.
name|getLifecycleStrategies
argument_list|()
control|)
block|{
if|if
condition|(
name|tracer
operator|instanceof
name|Service
condition|)
block|{
name|strategy
operator|.
name|onServiceAdd
argument_list|(
name|camelContext
argument_list|,
operator|(
name|Service
operator|)
name|tracer
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|tracer
return|;
block|}
DECL|method|getOrCreateBacklogDebugger ()
specifier|private
name|InterceptStrategy
name|getOrCreateBacklogDebugger
parameter_list|()
block|{
name|InterceptStrategy
name|debugger
init|=
name|BacklogDebugger
operator|.
name|getBacklogDebugger
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
if|if
condition|(
name|debugger
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|camelContext
operator|.
name|getRegistry
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// lookup in registry
name|Map
argument_list|<
name|String
argument_list|,
name|BacklogDebugger
argument_list|>
name|map
init|=
name|camelContext
operator|.
name|getRegistry
argument_list|()
operator|.
name|findByTypeWithName
argument_list|(
name|BacklogDebugger
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|map
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|debugger
operator|=
name|map
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|debugger
operator|==
literal|null
condition|)
block|{
comment|// fallback to use the default debugger
name|debugger
operator|=
name|camelContext
operator|.
name|getDefaultBacklogDebugger
argument_list|()
expr_stmt|;
block|}
block|}
comment|// which we must manage as well
for|for
control|(
name|LifecycleStrategy
name|strategy
range|:
name|camelContext
operator|.
name|getLifecycleStrategies
argument_list|()
control|)
block|{
if|if
condition|(
name|debugger
operator|instanceof
name|Service
condition|)
block|{
name|strategy
operator|.
name|onServiceAdd
argument_list|(
name|camelContext
argument_list|,
operator|(
name|Service
operator|)
name|debugger
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|debugger
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
comment|// just output the next processor as all the interceptors and error handler is just too verbose
return|return
literal|"Channel["
operator|+
name|nextProcessor
operator|+
literal|"]"
return|;
block|}
block|}
end_class

end_unit

