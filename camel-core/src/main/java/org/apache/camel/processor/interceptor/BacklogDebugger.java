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
name|Date
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
name|CountDownLatch
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
name|atomic
operator|.
name|AtomicBoolean
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
name|atomic
operator|.
name|AtomicLong
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
name|Expression
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
name|Predicate
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
name|api
operator|.
name|management
operator|.
name|mbean
operator|.
name|BacklogTracerEventMessage
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
name|BreakpointSupport
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
name|DefaultDebugger
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
name|CamelLogger
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
name|MessageHelper
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
comment|/**  * A {@link org.apache.camel.spi.Debugger} that should be used together with the {@link BacklogTracer} to  * offer debugging and tracing functionality.  */
end_comment

begin_class
DECL|class|BacklogDebugger
specifier|public
class|class
name|BacklogDebugger
extends|extends
name|ServiceSupport
implements|implements
name|InterceptStrategy
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
name|BacklogDebugger
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|loggingLevel
specifier|private
name|LoggingLevel
name|loggingLevel
init|=
name|LoggingLevel
operator|.
name|INFO
decl_stmt|;
DECL|field|logger
specifier|private
specifier|final
name|CamelLogger
name|logger
init|=
operator|new
name|CamelLogger
argument_list|(
name|LOG
argument_list|,
name|loggingLevel
argument_list|)
decl_stmt|;
DECL|field|enabled
specifier|private
specifier|final
name|AtomicBoolean
name|enabled
init|=
operator|new
name|AtomicBoolean
argument_list|()
decl_stmt|;
DECL|field|debugCounter
specifier|private
specifier|final
name|AtomicLong
name|debugCounter
init|=
operator|new
name|AtomicLong
argument_list|(
literal|0
argument_list|)
decl_stmt|;
DECL|field|debugger
specifier|private
specifier|final
name|Debugger
name|debugger
decl_stmt|;
DECL|field|breakpoints
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|NodeBreakpoint
argument_list|>
name|breakpoints
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|NodeBreakpoint
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|suspendedBreakpoints
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|CountDownLatch
argument_list|>
name|suspendedBreakpoints
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|CountDownLatch
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|suspendedBreakpointMessages
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|BacklogTracerEventMessage
argument_list|>
name|suspendedBreakpointMessages
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|BacklogTracerEventMessage
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|singleStepExchangeId
specifier|private
specifier|volatile
name|String
name|singleStepExchangeId
decl_stmt|;
DECL|method|BacklogDebugger (CamelContext camelContext)
specifier|public
name|BacklogDebugger
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
name|DefaultDebugger
name|debugger
init|=
operator|new
name|DefaultDebugger
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
name|debugger
operator|.
name|setUseTracer
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|this
operator|.
name|debugger
operator|=
name|debugger
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|Deprecated
DECL|method|wrapProcessorInInterceptors (CamelContext context, ProcessorDefinition<?> definition, Processor target, Processor nextTarget)
specifier|public
name|Processor
name|wrapProcessorInInterceptors
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|,
name|Processor
name|target
parameter_list|,
name|Processor
name|nextTarget
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Deprecated"
argument_list|)
throw|;
block|}
comment|/**      * A helper method to return the BacklogDebugger instance if one is enabled      *      * @return the backlog debugger or null if none can be found      */
DECL|method|getBacklogDebugger (CamelContext context)
specifier|public
specifier|static
name|BacklogDebugger
name|getBacklogDebugger
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|List
argument_list|<
name|InterceptStrategy
argument_list|>
name|list
init|=
name|context
operator|.
name|getInterceptStrategies
argument_list|()
decl_stmt|;
for|for
control|(
name|InterceptStrategy
name|interceptStrategy
range|:
name|list
control|)
block|{
if|if
condition|(
name|interceptStrategy
operator|instanceof
name|BacklogDebugger
condition|)
block|{
return|return
operator|(
name|BacklogDebugger
operator|)
name|interceptStrategy
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|getDebugger ()
specifier|public
name|Debugger
name|getDebugger
parameter_list|()
block|{
return|return
name|debugger
return|;
block|}
DECL|method|getLoggingLevel ()
specifier|public
name|String
name|getLoggingLevel
parameter_list|()
block|{
return|return
name|loggingLevel
operator|.
name|name
argument_list|()
return|;
block|}
DECL|method|setLoggingLevel (String level)
specifier|public
name|void
name|setLoggingLevel
parameter_list|(
name|String
name|level
parameter_list|)
block|{
name|loggingLevel
operator|=
name|LoggingLevel
operator|.
name|valueOf
argument_list|(
name|level
argument_list|)
expr_stmt|;
name|logger
operator|.
name|setLevel
argument_list|(
name|loggingLevel
argument_list|)
expr_stmt|;
block|}
DECL|method|enableDebugger ()
specifier|public
name|void
name|enableDebugger
parameter_list|()
block|{
name|logger
operator|.
name|log
argument_list|(
literal|"Enabling debugger"
argument_list|)
expr_stmt|;
try|try
block|{
name|ServiceHelper
operator|.
name|startService
argument_list|(
name|debugger
argument_list|)
expr_stmt|;
name|enabled
operator|.
name|set
argument_list|(
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
DECL|method|disableDebugger ()
specifier|public
name|void
name|disableDebugger
parameter_list|()
block|{
name|logger
operator|.
name|log
argument_list|(
literal|"Disabling debugger"
argument_list|)
expr_stmt|;
try|try
block|{
name|enabled
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|ServiceHelper
operator|.
name|stopService
argument_list|(
name|debugger
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
comment|// make sure to clear state and latches is counted down so we wont have hanging threads
name|breakpoints
operator|.
name|clear
argument_list|()
expr_stmt|;
for|for
control|(
name|CountDownLatch
name|latch
range|:
name|suspendedBreakpoints
operator|.
name|values
argument_list|()
control|)
block|{
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
name|suspendedBreakpoints
operator|.
name|clear
argument_list|()
expr_stmt|;
name|suspendedBreakpointMessages
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|isEnabled ()
specifier|public
name|boolean
name|isEnabled
parameter_list|()
block|{
return|return
name|enabled
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|hasBreakpoint (String nodeId)
specifier|public
name|boolean
name|hasBreakpoint
parameter_list|(
name|String
name|nodeId
parameter_list|)
block|{
return|return
name|breakpoints
operator|.
name|containsKey
argument_list|(
name|nodeId
argument_list|)
return|;
block|}
DECL|method|isSingleStepMode ()
specifier|public
name|boolean
name|isSingleStepMode
parameter_list|()
block|{
return|return
name|singleStepExchangeId
operator|!=
literal|null
return|;
block|}
DECL|method|addBreakpoint (String nodeId)
specifier|public
name|void
name|addBreakpoint
parameter_list|(
name|String
name|nodeId
parameter_list|)
block|{
name|NodeBreakpoint
name|breakpoint
init|=
name|breakpoints
operator|.
name|get
argument_list|(
name|nodeId
argument_list|)
decl_stmt|;
if|if
condition|(
name|breakpoint
operator|==
literal|null
condition|)
block|{
name|logger
operator|.
name|log
argument_list|(
literal|"Adding breakpoint "
operator|+
name|nodeId
argument_list|)
expr_stmt|;
name|breakpoint
operator|=
operator|new
name|NodeBreakpoint
argument_list|(
name|nodeId
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|breakpoints
operator|.
name|put
argument_list|(
name|nodeId
argument_list|,
name|breakpoint
argument_list|)
expr_stmt|;
name|debugger
operator|.
name|addBreakpoint
argument_list|(
name|breakpoint
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|breakpoint
operator|.
name|setCondition
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|addConditionalBreakpoint (String nodeId, String language, String predicate)
specifier|public
name|void
name|addConditionalBreakpoint
parameter_list|(
name|String
name|nodeId
parameter_list|,
name|String
name|language
parameter_list|,
name|String
name|predicate
parameter_list|)
block|{
name|Predicate
name|condition
init|=
name|camelContext
operator|.
name|resolveLanguage
argument_list|(
name|language
argument_list|)
operator|.
name|createPredicate
argument_list|(
name|predicate
argument_list|)
decl_stmt|;
name|NodeBreakpoint
name|breakpoint
init|=
name|breakpoints
operator|.
name|get
argument_list|(
name|nodeId
argument_list|)
decl_stmt|;
if|if
condition|(
name|breakpoint
operator|==
literal|null
condition|)
block|{
name|logger
operator|.
name|log
argument_list|(
literal|"Adding conditional breakpoint "
operator|+
name|nodeId
operator|+
literal|" ["
operator|+
name|predicate
operator|+
literal|"]"
argument_list|)
expr_stmt|;
name|breakpoint
operator|=
operator|new
name|NodeBreakpoint
argument_list|(
name|nodeId
argument_list|,
name|condition
argument_list|)
expr_stmt|;
name|breakpoints
operator|.
name|put
argument_list|(
name|nodeId
argument_list|,
name|breakpoint
argument_list|)
expr_stmt|;
name|debugger
operator|.
name|addBreakpoint
argument_list|(
name|breakpoint
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|logger
operator|.
name|log
argument_list|(
literal|"Updating conditional breakpoint "
operator|+
name|nodeId
operator|+
literal|" ["
operator|+
name|predicate
operator|+
literal|"]"
argument_list|)
expr_stmt|;
comment|// update condition
name|breakpoint
operator|.
name|setCondition
argument_list|(
name|condition
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|removeBreakpoint (String nodeId)
specifier|public
name|void
name|removeBreakpoint
parameter_list|(
name|String
name|nodeId
parameter_list|)
block|{
name|logger
operator|.
name|log
argument_list|(
literal|"Removing breakpoint "
operator|+
name|nodeId
argument_list|)
expr_stmt|;
comment|// when removing a break point then ensure latches is cleared and counted down so we wont have hanging threads
name|suspendedBreakpointMessages
operator|.
name|remove
argument_list|(
name|nodeId
argument_list|)
expr_stmt|;
name|CountDownLatch
name|latch
init|=
name|suspendedBreakpoints
operator|.
name|remove
argument_list|(
name|nodeId
argument_list|)
decl_stmt|;
name|NodeBreakpoint
name|breakpoint
init|=
name|breakpoints
operator|.
name|remove
argument_list|(
name|nodeId
argument_list|)
decl_stmt|;
if|if
condition|(
name|breakpoint
operator|!=
literal|null
condition|)
block|{
name|debugger
operator|.
name|removeBreakpoint
argument_list|(
name|breakpoint
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|latch
operator|!=
literal|null
condition|)
block|{
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|getBreakpoints ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getBreakpoints
parameter_list|()
block|{
return|return
operator|new
name|LinkedHashSet
argument_list|<
name|String
argument_list|>
argument_list|(
name|breakpoints
operator|.
name|keySet
argument_list|()
argument_list|)
return|;
block|}
DECL|method|resumeBreakpoint (String nodeId)
specifier|public
name|void
name|resumeBreakpoint
parameter_list|(
name|String
name|nodeId
parameter_list|)
block|{
name|resumeBreakpoint
argument_list|(
name|nodeId
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|resumeBreakpoint (String nodeId, boolean stepMode)
specifier|private
name|void
name|resumeBreakpoint
parameter_list|(
name|String
name|nodeId
parameter_list|,
name|boolean
name|stepMode
parameter_list|)
block|{
name|logger
operator|.
name|log
argument_list|(
literal|"Resume breakpoint "
operator|+
name|nodeId
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|stepMode
condition|)
block|{
if|if
condition|(
name|singleStepExchangeId
operator|!=
literal|null
condition|)
block|{
name|debugger
operator|.
name|stopSingleStepExchange
argument_list|(
name|singleStepExchangeId
argument_list|)
expr_stmt|;
name|singleStepExchangeId
operator|=
literal|null
expr_stmt|;
block|}
block|}
comment|// remember to remove the dumped message as its no longer in need
name|suspendedBreakpointMessages
operator|.
name|remove
argument_list|(
name|nodeId
argument_list|)
expr_stmt|;
name|CountDownLatch
name|latch
init|=
name|suspendedBreakpoints
operator|.
name|remove
argument_list|(
name|nodeId
argument_list|)
decl_stmt|;
if|if
condition|(
name|latch
operator|!=
literal|null
condition|)
block|{
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|resumeAll ()
specifier|public
name|void
name|resumeAll
parameter_list|()
block|{
name|logger
operator|.
name|log
argument_list|(
literal|"Resume all"
argument_list|)
expr_stmt|;
comment|// stop single stepping
name|singleStepExchangeId
operator|=
literal|null
expr_stmt|;
for|for
control|(
name|String
name|node
range|:
name|getSuspendedBreakpointNodeIds
argument_list|()
control|)
block|{
comment|// remember to remove the dumped message as its no longer in need
name|suspendedBreakpointMessages
operator|.
name|remove
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|CountDownLatch
name|latch
init|=
name|suspendedBreakpoints
operator|.
name|remove
argument_list|(
name|node
argument_list|)
decl_stmt|;
if|if
condition|(
name|latch
operator|!=
literal|null
condition|)
block|{
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|stepBreakpoint (String nodeId)
specifier|public
name|void
name|stepBreakpoint
parameter_list|(
name|String
name|nodeId
parameter_list|)
block|{
name|logger
operator|.
name|log
argument_list|(
literal|"Step breakpoint "
operator|+
name|nodeId
argument_list|)
expr_stmt|;
comment|// we want to step current exchange to next
name|BacklogTracerEventMessage
name|msg
init|=
name|suspendedBreakpointMessages
operator|.
name|get
argument_list|(
name|nodeId
argument_list|)
decl_stmt|;
name|NodeBreakpoint
name|breakpoint
init|=
name|breakpoints
operator|.
name|get
argument_list|(
name|nodeId
argument_list|)
decl_stmt|;
if|if
condition|(
name|msg
operator|!=
literal|null
operator|&&
name|breakpoint
operator|!=
literal|null
condition|)
block|{
name|singleStepExchangeId
operator|=
name|msg
operator|.
name|getExchangeId
argument_list|()
expr_stmt|;
if|if
condition|(
name|debugger
operator|.
name|startSingleStepExchange
argument_list|(
name|singleStepExchangeId
argument_list|,
operator|new
name|StepBreakpoint
argument_list|()
argument_list|)
condition|)
block|{
comment|// now resume
name|resumeBreakpoint
argument_list|(
name|nodeId
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|step ()
specifier|public
name|void
name|step
parameter_list|()
block|{
for|for
control|(
name|String
name|node
range|:
name|getSuspendedBreakpointNodeIds
argument_list|()
control|)
block|{
comment|// remember to remove the dumped message as its no longer in need
name|suspendedBreakpointMessages
operator|.
name|remove
argument_list|(
name|node
argument_list|)
expr_stmt|;
name|CountDownLatch
name|latch
init|=
name|suspendedBreakpoints
operator|.
name|remove
argument_list|(
name|node
argument_list|)
decl_stmt|;
if|if
condition|(
name|latch
operator|!=
literal|null
condition|)
block|{
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|getSuspendedBreakpointNodeIds ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getSuspendedBreakpointNodeIds
parameter_list|()
block|{
return|return
operator|new
name|LinkedHashSet
argument_list|<
name|String
argument_list|>
argument_list|(
name|suspendedBreakpoints
operator|.
name|keySet
argument_list|()
argument_list|)
return|;
block|}
DECL|method|suspendBreakpoint (String nodeId)
specifier|public
name|void
name|suspendBreakpoint
parameter_list|(
name|String
name|nodeId
parameter_list|)
block|{
name|logger
operator|.
name|log
argument_list|(
literal|"Suspend breakpoint "
operator|+
name|nodeId
argument_list|)
expr_stmt|;
name|NodeBreakpoint
name|breakpoint
init|=
name|breakpoints
operator|.
name|get
argument_list|(
name|nodeId
argument_list|)
decl_stmt|;
if|if
condition|(
name|breakpoint
operator|!=
literal|null
condition|)
block|{
name|breakpoint
operator|.
name|suspend
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|activateBreakpoint (String nodeId)
specifier|public
name|void
name|activateBreakpoint
parameter_list|(
name|String
name|nodeId
parameter_list|)
block|{
name|logger
operator|.
name|log
argument_list|(
literal|"Activate breakpoint "
operator|+
name|nodeId
argument_list|)
expr_stmt|;
name|NodeBreakpoint
name|breakpoint
init|=
name|breakpoints
operator|.
name|get
argument_list|(
name|nodeId
argument_list|)
decl_stmt|;
if|if
condition|(
name|breakpoint
operator|!=
literal|null
condition|)
block|{
name|breakpoint
operator|.
name|activate
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|dumpTracedMessagesAsXml (String nodeId)
specifier|public
name|String
name|dumpTracedMessagesAsXml
parameter_list|(
name|String
name|nodeId
parameter_list|)
block|{
name|logger
operator|.
name|log
argument_list|(
literal|"Dump trace message from breakpoint "
operator|+
name|nodeId
argument_list|)
expr_stmt|;
name|BacklogTracerEventMessage
name|msg
init|=
name|suspendedBreakpointMessages
operator|.
name|get
argument_list|(
name|nodeId
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
name|toXml
argument_list|(
literal|4
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
DECL|method|getDebugCounter ()
specifier|public
name|long
name|getDebugCounter
parameter_list|()
block|{
return|return
name|debugCounter
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|resetDebugCounter ()
specifier|public
name|void
name|resetDebugCounter
parameter_list|()
block|{
name|logger
operator|.
name|log
argument_list|(
literal|"Reset debug counter"
argument_list|)
expr_stmt|;
name|debugCounter
operator|.
name|set
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
DECL|method|beforeProcess (Exchange exchange, Processor processor, ProcessorDefinition<?> definition)
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
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|)
block|{
return|return
name|debugger
operator|.
name|beforeProcess
argument_list|(
name|exchange
argument_list|,
name|processor
argument_list|,
name|definition
argument_list|)
return|;
block|}
DECL|method|afterProcess (Exchange exchange, Processor processor, ProcessorDefinition<?> definition, long timeTaken)
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
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|,
name|long
name|timeTaken
parameter_list|)
block|{
return|return
name|debugger
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
return|;
block|}
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
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|disableDebugger
argument_list|()
expr_stmt|;
block|}
comment|/**      * Represents a {@link org.apache.camel.spi.Breakpoint} that has a {@link Condition} on a specific node id.      */
DECL|class|NodeBreakpoint
specifier|private
specifier|final
class|class
name|NodeBreakpoint
extends|extends
name|BreakpointSupport
implements|implements
name|Condition
block|{
DECL|field|nodeId
specifier|private
specifier|final
name|String
name|nodeId
decl_stmt|;
DECL|field|condition
specifier|private
name|Predicate
name|condition
decl_stmt|;
DECL|method|NodeBreakpoint (String nodeId, Predicate condition)
specifier|private
name|NodeBreakpoint
parameter_list|(
name|String
name|nodeId
parameter_list|,
name|Predicate
name|condition
parameter_list|)
block|{
name|this
operator|.
name|nodeId
operator|=
name|nodeId
expr_stmt|;
name|this
operator|.
name|condition
operator|=
name|condition
expr_stmt|;
block|}
DECL|method|getNodeId ()
specifier|public
name|String
name|getNodeId
parameter_list|()
block|{
return|return
name|nodeId
return|;
block|}
DECL|method|setCondition (Predicate predicate)
specifier|public
name|void
name|setCondition
parameter_list|(
name|Predicate
name|predicate
parameter_list|)
block|{
name|this
operator|.
name|condition
operator|=
name|predicate
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|beforeProcess (Exchange exchange, Processor processor, ProcessorDefinition<?> definition)
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
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|)
block|{
comment|// store a copy of the message so we can see that from the debugger
name|Date
name|timestamp
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|String
name|toNode
init|=
name|nodeId
decl_stmt|;
name|String
name|routeId
init|=
name|ProcessorDefinitionHelper
operator|.
name|getRouteId
argument_list|(
name|definition
argument_list|)
decl_stmt|;
name|String
name|exchangeId
init|=
name|exchange
operator|.
name|getExchangeId
argument_list|()
decl_stmt|;
name|String
name|messageAsXml
init|=
name|MessageHelper
operator|.
name|dumpAsXml
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
literal|true
argument_list|,
literal|4
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|1000
argument_list|)
decl_stmt|;
name|long
name|uid
init|=
name|debugCounter
operator|.
name|incrementAndGet
argument_list|()
decl_stmt|;
name|BacklogTracerEventMessage
name|msg
init|=
operator|new
name|DefaultBacklogTracerEventMessage
argument_list|(
name|uid
argument_list|,
name|timestamp
argument_list|,
name|routeId
argument_list|,
name|toNode
argument_list|,
name|exchangeId
argument_list|,
name|messageAsXml
argument_list|)
decl_stmt|;
name|suspendedBreakpointMessages
operator|.
name|put
argument_list|(
name|nodeId
argument_list|,
name|msg
argument_list|)
expr_stmt|;
comment|// mark as suspend
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|suspendedBreakpoints
operator|.
name|put
argument_list|(
name|nodeId
argument_list|,
name|latch
argument_list|)
expr_stmt|;
comment|// now wait until we should continue
name|logger
operator|.
name|log
argument_list|(
literal|"NodeBreakpoint at node "
operator|+
name|nodeId
operator|+
literal|" is waiting to continue for exchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
comment|// TODO: have a fallback timeout so we wont wait forever
name|latch
operator|.
name|await
argument_list|()
expr_stmt|;
name|logger
operator|.
name|log
argument_list|(
literal|"NodeBreakpoint at node "
operator|+
name|nodeId
operator|+
literal|" is continued exchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
name|super
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
annotation|@
name|Override
DECL|method|matchProcess (Exchange exchange, Processor processor, ProcessorDefinition<?> definition)
specifier|public
name|boolean
name|matchProcess
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|)
block|{
name|boolean
name|match
init|=
name|nodeId
operator|.
name|equals
argument_list|(
name|definition
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|match
operator|&&
name|condition
operator|!=
literal|null
condition|)
block|{
return|return
name|condition
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
return|;
block|}
return|return
name|match
return|;
block|}
annotation|@
name|Override
DECL|method|matchEvent (Exchange exchange, EventObject event)
specifier|public
name|boolean
name|matchEvent
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|EventObject
name|event
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
comment|/**      * Represents a {@link org.apache.camel.spi.Breakpoint} that is used during single step mode.      */
DECL|class|StepBreakpoint
specifier|private
specifier|final
class|class
name|StepBreakpoint
extends|extends
name|BreakpointSupport
implements|implements
name|Condition
block|{
annotation|@
name|Override
DECL|method|beforeProcess (Exchange exchange, Processor processor, ProcessorDefinition<?> definition)
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
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|)
block|{
comment|// store a copy of the message so we can see that from the debugger
name|Date
name|timestamp
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
name|String
name|toNode
init|=
name|definition
operator|.
name|getId
argument_list|()
decl_stmt|;
name|String
name|routeId
init|=
name|ProcessorDefinitionHelper
operator|.
name|getRouteId
argument_list|(
name|definition
argument_list|)
decl_stmt|;
name|String
name|exchangeId
init|=
name|exchange
operator|.
name|getExchangeId
argument_list|()
decl_stmt|;
name|String
name|messageAsXml
init|=
name|MessageHelper
operator|.
name|dumpAsXml
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
literal|true
argument_list|,
literal|4
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|1000
argument_list|)
decl_stmt|;
name|long
name|uid
init|=
name|debugCounter
operator|.
name|incrementAndGet
argument_list|()
decl_stmt|;
name|BacklogTracerEventMessage
name|msg
init|=
operator|new
name|DefaultBacklogTracerEventMessage
argument_list|(
name|uid
argument_list|,
name|timestamp
argument_list|,
name|routeId
argument_list|,
name|toNode
argument_list|,
name|exchangeId
argument_list|,
name|messageAsXml
argument_list|)
decl_stmt|;
name|suspendedBreakpointMessages
operator|.
name|put
argument_list|(
name|toNode
argument_list|,
name|msg
argument_list|)
expr_stmt|;
comment|// mark as suspend
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|suspendedBreakpoints
operator|.
name|put
argument_list|(
name|toNode
argument_list|,
name|latch
argument_list|)
expr_stmt|;
comment|// now wait until we should continue
name|logger
operator|.
name|log
argument_list|(
literal|"StepBreakpoint at node "
operator|+
name|toNode
operator|+
literal|" is waiting to continue for exchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
comment|// TODO: have a fallback timeout so we wont wait forever
name|latch
operator|.
name|await
argument_list|()
expr_stmt|;
name|logger
operator|.
name|log
argument_list|(
literal|"StepBreakpoint at node "
operator|+
name|toNode
operator|+
literal|" is continued exchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
name|super
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
annotation|@
name|Override
DECL|method|matchProcess (Exchange exchange, Processor processor, ProcessorDefinition<?> definition)
specifier|public
name|boolean
name|matchProcess
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|matchEvent (Exchange exchange, EventObject event)
specifier|public
name|boolean
name|matchEvent
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|EventObject
name|event
parameter_list|)
block|{
return|return
name|event
operator|instanceof
name|ExchangeCompletedEvent
return|;
block|}
annotation|@
name|Override
DECL|method|onEvent (Exchange exchange, EventObject event, ProcessorDefinition<?> definition)
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
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|)
block|{
comment|// when the exchange is complete, we need to turn off single step mode if we were debug stepping the exchange
if|if
condition|(
name|event
operator|instanceof
name|ExchangeCompletedEvent
condition|)
block|{
name|String
name|completedId
init|=
operator|(
operator|(
name|ExchangeCompletedEvent
operator|)
name|event
operator|)
operator|.
name|getExchange
argument_list|()
operator|.
name|getExchangeId
argument_list|()
decl_stmt|;
if|if
condition|(
name|singleStepExchangeId
operator|!=
literal|null
operator|&&
name|singleStepExchangeId
operator|.
name|equals
argument_list|(
name|completedId
argument_list|)
condition|)
block|{
name|logger
operator|.
name|log
argument_list|(
literal|"ExchangeId: "
operator|+
name|completedId
operator|+
literal|" is completed, so exiting single step mode."
argument_list|)
expr_stmt|;
name|singleStepExchangeId
operator|=
literal|null
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

