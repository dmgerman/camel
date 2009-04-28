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
name|Message
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
name|DelegateProcessor
import|;
end_import

begin_comment
comment|/**  * An interceptor for debugging and tracing routes  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DebugInterceptor
specifier|public
class|class
name|DebugInterceptor
extends|extends
name|DelegateProcessor
block|{
DECL|field|node
specifier|private
specifier|final
name|ProcessorDefinition
name|node
decl_stmt|;
DECL|field|exchanges
specifier|private
specifier|final
name|List
argument_list|<
name|Exchange
argument_list|>
name|exchanges
decl_stmt|;
DECL|field|exceptions
specifier|private
specifier|final
name|List
argument_list|<
name|ExceptionEvent
argument_list|>
name|exceptions
decl_stmt|;
DECL|field|traceFilter
specifier|private
name|Predicate
name|traceFilter
decl_stmt|;
DECL|field|breakpoint
specifier|private
name|Breakpoint
name|breakpoint
init|=
operator|new
name|Breakpoint
argument_list|()
decl_stmt|;
DECL|field|traceExceptions
specifier|private
name|boolean
name|traceExceptions
init|=
literal|true
decl_stmt|;
DECL|field|enabled
specifier|private
name|boolean
name|enabled
init|=
literal|true
decl_stmt|;
DECL|method|DebugInterceptor (ProcessorDefinition node, Processor target, List<Exchange> exchanges, List<ExceptionEvent> exceptions)
specifier|public
name|DebugInterceptor
parameter_list|(
name|ProcessorDefinition
name|node
parameter_list|,
name|Processor
name|target
parameter_list|,
name|List
argument_list|<
name|Exchange
argument_list|>
name|exchanges
parameter_list|,
name|List
argument_list|<
name|ExceptionEvent
argument_list|>
name|exceptions
parameter_list|)
block|{
name|super
argument_list|(
name|target
argument_list|)
expr_stmt|;
name|this
operator|.
name|node
operator|=
name|node
expr_stmt|;
name|this
operator|.
name|exchanges
operator|=
name|exchanges
expr_stmt|;
name|this
operator|.
name|exceptions
operator|=
name|exceptions
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
literal|"DebugInterceptor["
operator|+
name|node
operator|+
literal|"]"
return|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|isEnabled
argument_list|()
condition|)
block|{
name|checkForBreakpoint
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|addTraceExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|super
operator|.
name|proceed
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|onException
argument_list|(
name|exchange
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Error
name|e
parameter_list|)
block|{
name|onException
argument_list|(
name|exchange
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
block|}
DECL|method|getNode ()
specifier|public
name|ProcessorDefinition
name|getNode
parameter_list|()
block|{
return|return
name|node
return|;
block|}
DECL|method|isEnabled ()
specifier|public
name|boolean
name|isEnabled
parameter_list|()
block|{
return|return
name|enabled
return|;
block|}
DECL|method|setEnabled (boolean flag)
specifier|public
name|void
name|setEnabled
parameter_list|(
name|boolean
name|flag
parameter_list|)
block|{
name|enabled
operator|=
name|flag
expr_stmt|;
block|}
DECL|method|getExchanges ()
specifier|public
name|List
argument_list|<
name|Exchange
argument_list|>
name|getExchanges
parameter_list|()
block|{
return|return
name|exchanges
return|;
block|}
DECL|method|getExceptions ()
specifier|public
name|List
argument_list|<
name|ExceptionEvent
argument_list|>
name|getExceptions
parameter_list|()
block|{
return|return
name|exceptions
return|;
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
DECL|method|getTraceFilter ()
specifier|public
name|Predicate
name|getTraceFilter
parameter_list|()
block|{
return|return
name|traceFilter
return|;
block|}
DECL|method|setTraceFilter (Predicate traceFilter)
specifier|public
name|void
name|setTraceFilter
parameter_list|(
name|Predicate
name|traceFilter
parameter_list|)
block|{
name|this
operator|.
name|traceFilter
operator|=
name|traceFilter
expr_stmt|;
block|}
DECL|method|isTraceExceptions ()
specifier|public
name|boolean
name|isTraceExceptions
parameter_list|()
block|{
return|return
name|traceExceptions
return|;
block|}
DECL|method|setTraceExceptions (boolean traceExceptions)
specifier|public
name|void
name|setTraceExceptions
parameter_list|(
name|boolean
name|traceExceptions
parameter_list|)
block|{
name|this
operator|.
name|traceExceptions
operator|=
name|traceExceptions
expr_stmt|;
block|}
comment|/**      * Stategy method to wait for a breakpoint if one is set      */
DECL|method|checkForBreakpoint (Exchange exchange)
specifier|protected
name|void
name|checkForBreakpoint
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|breakpoint
operator|.
name|waitForBreakpoint
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|/**      * Fired when an exception is thrown when processing the underlying processor      */
DECL|method|onException (Exchange exchange, Throwable e)
specifier|protected
name|void
name|onException
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Throwable
name|e
parameter_list|)
block|{
if|if
condition|(
name|shouldTraceExceptionEvents
argument_list|(
name|exchange
argument_list|,
name|e
argument_list|)
condition|)
block|{
name|exceptions
operator|.
name|add
argument_list|(
operator|new
name|ExceptionEvent
argument_list|(
name|this
argument_list|,
name|exchange
argument_list|,
name|e
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|shouldTraceExceptionEvents (Exchange exchange, Throwable e)
specifier|private
name|boolean
name|shouldTraceExceptionEvents
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Throwable
name|e
parameter_list|)
block|{
return|return
name|isTraceExceptions
argument_list|()
operator|&&
name|isEnabled
argument_list|()
return|;
block|}
comment|/**      * Strategy method to store the exchange in a trace log if it is enabled      */
DECL|method|addTraceExchange (Exchange exchange)
specifier|protected
name|void
name|addTraceExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|shouldTraceExchange
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
name|exchanges
operator|.
name|add
argument_list|(
name|copyExchange
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|copyExchange (Exchange previousExchange)
specifier|protected
name|Exchange
name|copyExchange
parameter_list|(
name|Exchange
name|previousExchange
parameter_list|)
block|{
name|Exchange
name|answer
init|=
name|previousExchange
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|answer
operator|.
name|getProperties
argument_list|()
operator|.
name|putAll
argument_list|(
name|previousExchange
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
name|answer
operator|.
name|getIn
argument_list|()
operator|.
name|copyFrom
argument_list|(
name|previousExchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
comment|// only copy the out if its defined
if|if
condition|(
name|previousExchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|answer
operator|.
name|getOut
argument_list|()
operator|.
name|copyFrom
argument_list|(
name|previousExchange
operator|.
name|getOut
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Returns true if the given exchange should be logged in the trace list      */
DECL|method|shouldTraceExchange (Exchange exchange)
specifier|protected
name|boolean
name|shouldTraceExchange
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|traceFilter
operator|==
literal|null
operator|||
name|traceFilter
operator|.
name|matches
argument_list|(
name|exchange
argument_list|)
return|;
block|}
block|}
end_class

end_unit

