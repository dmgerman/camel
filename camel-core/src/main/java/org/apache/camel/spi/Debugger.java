begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
package|;
end_package

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
name|ProcessorDefinition
import|;
end_import

begin_comment
comment|/**  * A debugger which allows tooling to attach breakpoints which is is being invoked  * when {@link Exchange}s is being routed.  *  * @version   */
end_comment

begin_interface
DECL|interface|Debugger
specifier|public
interface|interface
name|Debugger
extends|extends
name|Service
extends|,
name|CamelContextAware
block|{
comment|/**      * Add the given breakpoint      *      * @param breakpoint the breakpoint      */
DECL|method|addBreakpoint (Breakpoint breakpoint)
name|void
name|addBreakpoint
parameter_list|(
name|Breakpoint
name|breakpoint
parameter_list|)
function_decl|;
comment|/**      * Add the given breakpoint      *      * @param breakpoint the breakpoint      * @param conditions a number of {@link org.apache.camel.spi.Condition}s      */
DECL|method|addBreakpoint (Breakpoint breakpoint, Condition... conditions)
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
function_decl|;
comment|/**      * Add the given breakpoint which will be used in single step mode      *<p/>      * The debugger will single step the first message arriving.      *      * @param breakpoint the breakpoint      */
DECL|method|addSingleStepBreakpoint (Breakpoint breakpoint)
name|void
name|addSingleStepBreakpoint
parameter_list|(
name|Breakpoint
name|breakpoint
parameter_list|)
function_decl|;
comment|/**      * Add the given breakpoint which will be used in single step mode      *<p/>      * The debugger will single step the first message arriving.      *      * @param breakpoint the breakpoint      * @param conditions a number of {@link org.apache.camel.spi.Condition}s      */
DECL|method|addSingleStepBreakpoint (Breakpoint breakpoint, Condition... conditions)
name|void
name|addSingleStepBreakpoint
parameter_list|(
name|Breakpoint
name|breakpoint
parameter_list|,
name|Condition
modifier|...
name|conditions
parameter_list|)
function_decl|;
comment|/**      * Removes the given breakpoint      *      * @param breakpoint the breakpoint      */
DECL|method|removeBreakpoint (Breakpoint breakpoint)
name|void
name|removeBreakpoint
parameter_list|(
name|Breakpoint
name|breakpoint
parameter_list|)
function_decl|;
comment|/**      * Suspends all breakpoints.      */
DECL|method|suspendAllBreakpoints ()
name|void
name|suspendAllBreakpoints
parameter_list|()
function_decl|;
comment|/**      * Activate all breakpoints.      */
DECL|method|activateAllBreakpoints ()
name|void
name|activateAllBreakpoints
parameter_list|()
function_decl|;
comment|/**      * Gets a list of all the breakpoints      *      * @return the breakpoints wrapped in an unmodifiable list, is never<tt>null</tt>.      */
DECL|method|getBreakpoints ()
name|List
argument_list|<
name|Breakpoint
argument_list|>
name|getBreakpoints
parameter_list|()
function_decl|;
comment|/**      * Starts the single step debug mode for the given exchange      *      * @param exchangeId the exchange id      * @param breakpoint the breakpoint      * @return<tt>true</tt> if the debugger will single step the given exchange,<tt>false</tt> if the debugger is already      * single stepping another, and thus cannot simultaneously single step another exchange      */
DECL|method|startSingleStepExchange (String exchangeId, Breakpoint breakpoint)
name|boolean
name|startSingleStepExchange
parameter_list|(
name|String
name|exchangeId
parameter_list|,
name|Breakpoint
name|breakpoint
parameter_list|)
function_decl|;
comment|/**      * Stops the single step debug mode for the given exchange.      *<p/>      *<b>Notice:</b> The default implementation of the debugger is capable of auto stopping when the exchange is complete.      *      * @param exchangeId the exchange id      */
DECL|method|stopSingleStepExchange (String exchangeId)
name|void
name|stopSingleStepExchange
parameter_list|(
name|String
name|exchangeId
parameter_list|)
function_decl|;
comment|/**      * Callback invoked when an {@link Exchange} is about to be processed which allows implementators      * to notify breakpoints.      *      * @param exchange   the exchange      * @param processor  the {@link Processor} about to be processed      * @param definition the definition of the processor      * @return<tt>true</tt> if any breakpoint was hit,<tt>false</tt> if not breakpoint was hit      */
DECL|method|beforeProcess (Exchange exchange, Processor processor, ProcessorDefinition<?> definition)
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
function_decl|;
comment|/**      * Callback invoked when an {@link Exchange} has been processed which allows implementators      * to notify breakpoints.      *      * @param exchange   the exchange      * @param processor  the {@link Processor} which was processed      * @param definition the definition of the processor      * @param timeTaken  time in millis it took to process the {@link Exchange} - time spend in breakpoint callbacks may affect this time      * @return<tt>true</tt> if any breakpoint was hit,<tt>false</tt> if not breakpoint was hit      */
DECL|method|afterProcess (Exchange exchange, Processor processor, ProcessorDefinition<?> definition, long timeTaken)
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
function_decl|;
comment|/**      * Callback invoked when an {@link Exchange} is being processed which allows implementators      * to notify breakpoints.      *      * @param exchange the exchange      * @param event    the event (instance of {@link org.apache.camel.management.event.AbstractExchangeEvent}      * @return<tt>true</tt> if any breakpoint was hit,<tt>false</tt> if not breakpoint was hit      */
DECL|method|onEvent (Exchange exchange, EventObject event)
name|boolean
name|onEvent
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|EventObject
name|event
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

