begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|Arrays
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
name|LoggingLevel
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
name|RouteNode
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
name|management
operator|.
name|event
operator|.
name|AbstractExchangeEvent
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
name|ExchangeCompletedEvent
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
name|ExchangeCreatedEvent
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
name|processor
operator|.
name|interceptor
operator|.
name|Tracer
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
name|Breakpoint
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
name|Condition
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
name|Debugger
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
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * The default implementation of the {@link Debugger}.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultDebugger
specifier|public
class|class
name|DefaultDebugger
implements|implements
name|Debugger
implements|,
name|CamelContextAware
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|DefaultDebugger
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|breakpoints
specifier|private
specifier|final
name|List
argument_list|<
name|BreakpointConditions
argument_list|>
name|breakpoints
init|=
operator|new
name|ArrayList
argument_list|<
name|BreakpointConditions
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|maxConcurrentSingleSteps
specifier|private
specifier|final
name|int
name|maxConcurrentSingleSteps
init|=
literal|1
decl_stmt|;
DECL|field|singleSteps
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Breakpoint
argument_list|>
name|singleSteps
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Breakpoint
argument_list|>
argument_list|(
name|maxConcurrentSingleSteps
argument_list|)
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
comment|/**      * Holder class for breakpoint and the associated conditions      */
DECL|class|BreakpointConditions
specifier|private
specifier|final
class|class
name|BreakpointConditions
block|{
DECL|field|breakpoint
specifier|private
name|Breakpoint
name|breakpoint
decl_stmt|;
DECL|field|conditions
specifier|private
name|List
argument_list|<
name|Condition
argument_list|>
name|conditions
decl_stmt|;
DECL|method|BreakpointConditions (Breakpoint breakpoint)
specifier|private
name|BreakpointConditions
parameter_list|(
name|Breakpoint
name|breakpoint
parameter_list|)
block|{
name|this
argument_list|(
name|breakpoint
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|BreakpointConditions (Breakpoint breakpoint, List<Condition> conditions)
specifier|private
name|BreakpointConditions
parameter_list|(
name|Breakpoint
name|breakpoint
parameter_list|,
name|List
argument_list|<
name|Condition
argument_list|>
name|conditions
parameter_list|)
block|{
name|this
operator|.
name|breakpoint
operator|=
name|breakpoint
expr_stmt|;
name|this
operator|.
name|conditions
operator|=
name|conditions
expr_stmt|;
block|}
DECL|method|getBreakpoint ()
specifier|public
name|Breakpoint
name|getBreakpoint
parameter_list|()
block|{
return|return
name|breakpoint
return|;
block|}
DECL|method|getConditions ()
specifier|public
name|List
argument_list|<
name|Condition
argument_list|>
name|getConditions
parameter_list|()
block|{
return|return
name|conditions
return|;
block|}
block|}
DECL|method|DefaultDebugger ()
specifier|public
name|DefaultDebugger
parameter_list|()
block|{     }
DECL|method|DefaultDebugger (CamelContext camelContext)
specifier|public
name|DefaultDebugger
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
DECL|method|addBreakpoint (Breakpoint breakpoint)
specifier|public
name|void
name|addBreakpoint
parameter_list|(
name|Breakpoint
name|breakpoint
parameter_list|)
block|{
name|addBreakpoint
argument_list|(
name|breakpoint
argument_list|,
operator|(
name|Condition
operator|)
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|addBreakpoint (Breakpoint breakpoint, Condition... conditions)
specifier|public
name|void
name|addBreakpoint
parameter_list|(
name|Breakpoint
name|breakpoint
parameter_list|,
name|Condition
modifier|...
name|conditions
parameter_list|)
block|{
if|if
condition|(
name|conditions
operator|!=
literal|null
operator|&&
name|conditions
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|breakpoints
operator|.
name|add
argument_list|(
operator|new
name|BreakpointConditions
argument_list|(
name|breakpoint
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|conditions
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|breakpoints
operator|.
name|add
argument_list|(
operator|new
name|BreakpointConditions
argument_list|(
name|breakpoint
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|addSingleStepBreakpoint (final Breakpoint breakpoint)
specifier|public
name|void
name|addSingleStepBreakpoint
parameter_list|(
specifier|final
name|Breakpoint
name|breakpoint
parameter_list|)
block|{
name|breakpoints
operator|.
name|add
argument_list|(
operator|new
name|BreakpointConditions
argument_list|(
name|breakpoint
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|addSingleStepBreakpoint (final Breakpoint breakpoint, Condition... conditions)
specifier|public
name|void
name|addSingleStepBreakpoint
parameter_list|(
specifier|final
name|Breakpoint
name|breakpoint
parameter_list|,
name|Condition
modifier|...
name|conditions
parameter_list|)
block|{
comment|// wrap the breakpoint into single step breakpoint so we can automatic enable/disable the single step mode
name|Breakpoint
name|singlestep
init|=
operator|new
name|Breakpoint
argument_list|()
block|{
specifier|public
name|State
name|getState
parameter_list|()
block|{
return|return
name|breakpoint
operator|.
name|getState
argument_list|()
return|;
block|}
specifier|public
name|void
name|suspend
parameter_list|()
block|{
name|breakpoint
operator|.
name|suspend
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|activate
parameter_list|()
block|{
name|breakpoint
operator|.
name|activate
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|beforeProcess
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|ProcessorDefinition
name|definition
parameter_list|)
block|{
name|breakpoint
operator|.
name|beforeProcess
argument_list|(
name|exchange
argument_list|,
name|processor
argument_list|,
name|definition
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|afterProcess
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|ProcessorDefinition
name|definition
parameter_list|,
name|long
name|timeTaken
parameter_list|)
block|{
name|breakpoint
operator|.
name|afterProcess
argument_list|(
name|exchange
argument_list|,
name|processor
argument_list|,
name|definition
argument_list|,
name|timeTaken
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|onEvent
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|EventObject
name|event
parameter_list|,
name|ProcessorDefinition
name|definition
parameter_list|)
block|{
if|if
condition|(
name|event
operator|instanceof
name|ExchangeCreatedEvent
condition|)
block|{
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getDebugger
argument_list|()
operator|.
name|startSingleStepExchange
argument_list|(
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|event
operator|instanceof
name|ExchangeCompletedEvent
condition|)
block|{
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getDebugger
argument_list|()
operator|.
name|stopSingleStepExchange
argument_list|(
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|breakpoint
operator|.
name|onEvent
argument_list|(
name|exchange
argument_list|,
name|event
argument_list|,
name|definition
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|breakpoint
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
decl_stmt|;
name|addBreakpoint
argument_list|(
name|singlestep
argument_list|,
name|conditions
argument_list|)
expr_stmt|;
block|}
DECL|method|removeBreakpoint (Breakpoint breakpoint)
specifier|public
name|void
name|removeBreakpoint
parameter_list|(
name|Breakpoint
name|breakpoint
parameter_list|)
block|{
name|breakpoints
operator|.
name|remove
argument_list|(
name|breakpoint
argument_list|)
expr_stmt|;
block|}
DECL|method|suspendAllBreakpoints ()
specifier|public
name|void
name|suspendAllBreakpoints
parameter_list|()
block|{
for|for
control|(
name|BreakpointConditions
name|breakpoint
range|:
name|breakpoints
control|)
block|{
name|breakpoint
operator|.
name|getBreakpoint
argument_list|()
operator|.
name|suspend
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|activateAllBreakpoints ()
specifier|public
name|void
name|activateAllBreakpoints
parameter_list|()
block|{
for|for
control|(
name|BreakpointConditions
name|breakpoint
range|:
name|breakpoints
control|)
block|{
name|breakpoint
operator|.
name|getBreakpoint
argument_list|()
operator|.
name|activate
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getBreakpoints ()
specifier|public
name|List
argument_list|<
name|Breakpoint
argument_list|>
name|getBreakpoints
parameter_list|()
block|{
name|List
argument_list|<
name|Breakpoint
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|Breakpoint
argument_list|>
argument_list|(
name|breakpoints
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|BreakpointConditions
name|e
range|:
name|breakpoints
control|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|e
operator|.
name|getBreakpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|answer
argument_list|)
return|;
block|}
DECL|method|startSingleStepExchange (String exchangeId, Breakpoint breakpoint)
specifier|public
name|boolean
name|startSingleStepExchange
parameter_list|(
name|String
name|exchangeId
parameter_list|,
name|Breakpoint
name|breakpoint
parameter_list|)
block|{
comment|// can we accept single stepping the given exchange?
if|if
condition|(
name|singleSteps
operator|.
name|size
argument_list|()
operator|>=
name|maxConcurrentSingleSteps
condition|)
block|{
return|return
literal|false
return|;
block|}
name|singleSteps
operator|.
name|put
argument_list|(
name|exchangeId
argument_list|,
name|breakpoint
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
DECL|method|stopSingleStepExchange (String exchangeId)
specifier|public
name|void
name|stopSingleStepExchange
parameter_list|(
name|String
name|exchangeId
parameter_list|)
block|{
name|singleSteps
operator|.
name|remove
argument_list|(
name|exchangeId
argument_list|)
expr_stmt|;
block|}
DECL|method|beforeProcess (Exchange exchange, Processor processor, ProcessorDefinition definition)
specifier|public
name|boolean
name|beforeProcess
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|ProcessorDefinition
name|definition
parameter_list|)
block|{
comment|// is the exchange in single step mode?
name|Breakpoint
name|singleStep
init|=
name|singleSteps
operator|.
name|get
argument_list|(
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|singleStep
operator|!=
literal|null
condition|)
block|{
name|onBeforeProcess
argument_list|(
name|exchange
argument_list|,
name|processor
argument_list|,
name|definition
argument_list|,
name|singleStep
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|// does any of the breakpoints apply?
name|boolean
name|match
init|=
literal|false
decl_stmt|;
for|for
control|(
name|BreakpointConditions
name|breakpoint
range|:
name|breakpoints
control|)
block|{
comment|// breakpoint must be active
if|if
condition|(
name|Breakpoint
operator|.
name|State
operator|.
name|Active
operator|.
name|equals
argument_list|(
name|breakpoint
operator|.
name|getBreakpoint
argument_list|()
operator|.
name|getState
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|matchConditions
argument_list|(
name|exchange
argument_list|,
name|processor
argument_list|,
name|definition
argument_list|,
name|breakpoint
argument_list|)
condition|)
block|{
name|match
operator|=
literal|true
expr_stmt|;
name|onBeforeProcess
argument_list|(
name|exchange
argument_list|,
name|processor
argument_list|,
name|definition
argument_list|,
name|breakpoint
operator|.
name|getBreakpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|match
return|;
block|}
DECL|method|afterProcess (Exchange exchange, Processor processor, ProcessorDefinition definition, long timeTaken)
specifier|public
name|boolean
name|afterProcess
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|ProcessorDefinition
name|definition
parameter_list|,
name|long
name|timeTaken
parameter_list|)
block|{
comment|// is the exchange in single step mode?
name|Breakpoint
name|singleStep
init|=
name|singleSteps
operator|.
name|get
argument_list|(
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|singleStep
operator|!=
literal|null
condition|)
block|{
name|onAfterProcess
argument_list|(
name|exchange
argument_list|,
name|processor
argument_list|,
name|definition
argument_list|,
name|timeTaken
argument_list|,
name|singleStep
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|// does any of the breakpoints apply?
name|boolean
name|match
init|=
literal|false
decl_stmt|;
for|for
control|(
name|BreakpointConditions
name|breakpoint
range|:
name|breakpoints
control|)
block|{
comment|// breakpoint must be active
if|if
condition|(
name|Breakpoint
operator|.
name|State
operator|.
name|Active
operator|.
name|equals
argument_list|(
name|breakpoint
operator|.
name|getBreakpoint
argument_list|()
operator|.
name|getState
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|matchConditions
argument_list|(
name|exchange
argument_list|,
name|processor
argument_list|,
name|definition
argument_list|,
name|breakpoint
argument_list|)
condition|)
block|{
name|match
operator|=
literal|true
expr_stmt|;
name|onAfterProcess
argument_list|(
name|exchange
argument_list|,
name|processor
argument_list|,
name|definition
argument_list|,
name|timeTaken
argument_list|,
name|breakpoint
operator|.
name|getBreakpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|match
return|;
block|}
DECL|method|onEvent (Exchange exchange, EventObject event)
specifier|public
name|boolean
name|onEvent
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|EventObject
name|event
parameter_list|)
block|{
comment|// is the exchange in single step mode?
name|Breakpoint
name|singleStep
init|=
name|singleSteps
operator|.
name|get
argument_list|(
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|singleStep
operator|!=
literal|null
condition|)
block|{
name|onEvent
argument_list|(
name|exchange
argument_list|,
name|event
argument_list|,
name|singleStep
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|// does any of the breakpoints apply?
name|boolean
name|match
init|=
literal|false
decl_stmt|;
for|for
control|(
name|BreakpointConditions
name|breakpoint
range|:
name|breakpoints
control|)
block|{
comment|// breakpoint must be active
if|if
condition|(
name|Breakpoint
operator|.
name|State
operator|.
name|Active
operator|.
name|equals
argument_list|(
name|breakpoint
operator|.
name|getBreakpoint
argument_list|()
operator|.
name|getState
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|matchConditions
argument_list|(
name|exchange
argument_list|,
name|event
argument_list|,
name|breakpoint
argument_list|)
condition|)
block|{
name|match
operator|=
literal|true
expr_stmt|;
name|onEvent
argument_list|(
name|exchange
argument_list|,
name|event
argument_list|,
name|breakpoint
operator|.
name|getBreakpoint
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|match
return|;
block|}
DECL|method|onBeforeProcess (Exchange exchange, Processor processor, ProcessorDefinition definition, Breakpoint breakpoint)
specifier|protected
name|void
name|onBeforeProcess
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|ProcessorDefinition
name|definition
parameter_list|,
name|Breakpoint
name|breakpoint
parameter_list|)
block|{
try|try
block|{
name|breakpoint
operator|.
name|beforeProcess
argument_list|(
name|exchange
argument_list|,
name|processor
argument_list|,
name|definition
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Exception occurred in breakpoint: "
operator|+
name|breakpoint
operator|+
literal|". This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|onAfterProcess (Exchange exchange, Processor processor, ProcessorDefinition definition, long timeTaken, Breakpoint breakpoint)
specifier|protected
name|void
name|onAfterProcess
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|ProcessorDefinition
name|definition
parameter_list|,
name|long
name|timeTaken
parameter_list|,
name|Breakpoint
name|breakpoint
parameter_list|)
block|{
try|try
block|{
name|breakpoint
operator|.
name|afterProcess
argument_list|(
name|exchange
argument_list|,
name|processor
argument_list|,
name|definition
argument_list|,
name|timeTaken
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Exception occurred in breakpoint: "
operator|+
name|breakpoint
operator|+
literal|". This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|onEvent (Exchange exchange, EventObject event, Breakpoint breakpoint)
specifier|protected
name|void
name|onEvent
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|EventObject
name|event
parameter_list|,
name|Breakpoint
name|breakpoint
parameter_list|)
block|{
name|ProcessorDefinition
name|definition
init|=
literal|null
decl_stmt|;
comment|// try to get the last known definition
if|if
condition|(
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|!=
literal|null
operator|&&
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|.
name|getTracedRouteNodes
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|RouteNode
name|node
init|=
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|.
name|getTracedRouteNodes
argument_list|()
operator|.
name|getLastNode
argument_list|()
decl_stmt|;
if|if
condition|(
name|node
operator|!=
literal|null
condition|)
block|{
name|definition
operator|=
name|node
operator|.
name|getProcessorDefinition
argument_list|()
expr_stmt|;
block|}
block|}
try|try
block|{
name|breakpoint
operator|.
name|onEvent
argument_list|(
name|exchange
argument_list|,
name|event
argument_list|,
name|definition
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Exception occurred in breakpoint: "
operator|+
name|breakpoint
operator|+
literal|". This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|matchConditions (Exchange exchange, Processor processor, ProcessorDefinition definition, BreakpointConditions breakpoint)
specifier|private
name|boolean
name|matchConditions
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|ProcessorDefinition
name|definition
parameter_list|,
name|BreakpointConditions
name|breakpoint
parameter_list|)
block|{
if|if
condition|(
name|breakpoint
operator|.
name|getConditions
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|breakpoint
operator|.
name|getConditions
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|Condition
name|condition
range|:
name|breakpoint
operator|.
name|getConditions
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|condition
operator|.
name|matchProcess
argument_list|(
name|exchange
argument_list|,
name|processor
argument_list|,
name|definition
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
DECL|method|matchConditions (Exchange exchange, EventObject event, BreakpointConditions breakpoint)
specifier|private
name|boolean
name|matchConditions
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|EventObject
name|event
parameter_list|,
name|BreakpointConditions
name|breakpoint
parameter_list|)
block|{
if|if
condition|(
name|breakpoint
operator|.
name|getConditions
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|breakpoint
operator|.
name|getConditions
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|Condition
name|condition
range|:
name|breakpoint
operator|.
name|getConditions
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|condition
operator|.
name|matchEvent
argument_list|(
name|exchange
argument_list|,
name|event
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
DECL|method|start ()
specifier|public
name|void
name|start
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
comment|// register our event notifier
name|camelContext
operator|.
name|getManagementStrategy
argument_list|()
operator|.
name|addEventNotifier
argument_list|(
operator|new
name|DebugEventNotifier
argument_list|()
argument_list|)
expr_stmt|;
name|Tracer
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
comment|// tracer is disabled so enable it silently so we can leverage it to trace the Exchanges for us
name|tracer
operator|=
name|Tracer
operator|.
name|createTracer
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
name|tracer
operator|.
name|setLogLevel
argument_list|(
name|LoggingLevel
operator|.
name|OFF
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|addService
argument_list|(
name|tracer
argument_list|)
expr_stmt|;
name|camelContext
operator|.
name|addInterceptStrategy
argument_list|(
name|tracer
argument_list|)
expr_stmt|;
block|}
comment|// make sure tracer is enabled so the debugger can leverage the tracer for debugging purposes
name|tracer
operator|.
name|setEnabled
argument_list|(
literal|true
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
name|breakpoints
operator|.
name|clear
argument_list|()
expr_stmt|;
name|singleSteps
operator|.
name|clear
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
literal|"DefaultDebugger"
return|;
block|}
DECL|class|DebugEventNotifier
specifier|private
specifier|final
class|class
name|DebugEventNotifier
extends|extends
name|EventNotifierSupport
block|{
DECL|method|DebugEventNotifier ()
specifier|private
name|DebugEventNotifier
parameter_list|()
block|{
name|setIgnoreCamelContextEvents
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setIgnoreServiceEvents
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
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
name|AbstractExchangeEvent
name|aee
init|=
operator|(
name|AbstractExchangeEvent
operator|)
name|event
decl_stmt|;
name|Exchange
name|exchange
init|=
name|aee
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|onEvent
argument_list|(
name|exchange
argument_list|,
name|event
argument_list|)
expr_stmt|;
if|if
condition|(
name|event
operator|instanceof
name|ExchangeCompletedEvent
condition|)
block|{
comment|// failsafe to ensure we remote single steps when the Exchange is complete
name|singleSteps
operator|.
name|remove
argument_list|(
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
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
name|AbstractExchangeEvent
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
comment|// noop
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
comment|// noop
block|}
block|}
block|}
end_class

end_unit

