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
name|model
operator|.
name|ProcessorDefinition
import|;
end_import

begin_comment
comment|/**  * {@link org.apache.camel.spi.Breakpoint} are used by the {@link org.apache.camel.spi.Debugger} API.  *<p/>  * This allows you to register {@link org.apache.camel.spi.Breakpoint}s to the {@link org.apache.camel.spi.Debugger}  * and have those breakpoints activated when their {@link org.apache.camel.spi.Condition}s match.  *<p/>  * If any exceptions is thrown from the callback methods then the {@link org.apache.camel.spi.Debugger}  * will catch and log those at<tt>WARN</tt> level and continue. This ensures Camel can continue to route  * the message without having breakpoints causing issues.  *  * @version   * @see org.apache.camel.spi.Debugger  * @see org.apache.camel.spi.Condition  */
end_comment

begin_interface
DECL|interface|Breakpoint
specifier|public
interface|interface
name|Breakpoint
block|{
DECL|enum|State
enum|enum
name|State
block|{
DECL|enumConstant|Active
DECL|enumConstant|Suspended
name|Active
block|,
name|Suspended
block|}
comment|/**      * Gets the state of this break      *      * @return the state      */
DECL|method|getState ()
name|State
name|getState
parameter_list|()
function_decl|;
comment|/**      * Suspend this breakpoint      */
DECL|method|suspend ()
name|void
name|suspend
parameter_list|()
function_decl|;
comment|/**      * Activates this breakpoint      */
DECL|method|activate ()
name|void
name|activate
parameter_list|()
function_decl|;
comment|/**      * Callback invoked when the breakpoint was hit and the {@link Exchange} is about to be processed (before).      *      * @param exchange   the {@link Exchange}      * @param processor  the {@link Processor} about to be processed      * @param definition the {@link org.apache.camel.model.ProcessorDefinition} definition of the processor      */
DECL|method|beforeProcess (Exchange exchange, Processor processor, ProcessorDefinition<?> definition)
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
function_decl|;
comment|/**      * Callback invoked when the breakpoint was hit and the {@link Exchange} has been processed (after).      *      * @param exchange   the {@link Exchange}      * @param processor  the {@link Processor} which was processed      * @param definition the {@link org.apache.camel.model.ProcessorDefinition} definition of the processor      * @param timeTaken  time in millis it took to process the {@link Exchange} - time spend in breakpoint callbacks may affect this time      */
DECL|method|afterProcess (Exchange exchange, Processor processor, ProcessorDefinition<?> definition, long timeTaken)
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
argument_list|<
name|?
argument_list|>
name|definition
parameter_list|,
name|long
name|timeTaken
parameter_list|)
function_decl|;
comment|/**      * Callback invoked when the breakpoint was hit and any of the {@link Exchange} {@link EventObject event}s occurred.      *      * @param exchange   the {@link Exchange}      * @param event      the event (instance of {@link org.apache.camel.management.event.AbstractExchangeEvent}      * @param definition the {@link org.apache.camel.model.ProcessorDefinition} definition of the last processor executed,      *                   may be<tt>null</tt> if not possible to resolve from tracing      * @see org.apache.camel.management.event.AbstractExchangeEvent      */
DECL|method|onEvent (Exchange exchange, EventObject event, ProcessorDefinition<?> definition)
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
function_decl|;
block|}
end_interface

end_unit

