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
comment|/**  * A condition to define when a given {@link Exchange} matches when is being routed.  *<p/>  * Is used by the {@link org.apache.camel.spi.Debugger} to apply {@link Condition}s  * to {@link org.apache.camel.spi.Breakpoint}s to define rules when the breakpoints should match.  *  * @version   */
end_comment

begin_interface
DECL|interface|Condition
specifier|public
interface|interface
name|Condition
block|{
comment|/**      * Does the condition match      *      * @param exchange the exchange      * @param processor  the {@link Processor}      * @param definition the present location in the route where the {@link Exchange} is located at      * @return<tt>true</tt> to match,<tt>false</tt> otherwise      */
DECL|method|matchProcess (Exchange exchange, Processor processor, ProcessorDefinition<?> definition)
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
function_decl|;
comment|/**      * Does the condition match      *      * @param exchange the exchange      * @param event    the event (instance of {@link org.apache.camel.management.event.AbstractExchangeEvent}      * @return<tt>true</tt> to match,<tt>false</tt> otherwise      * @see org.apache.camel.management.event.AbstractExchangeEvent      */
DECL|method|matchEvent (Exchange exchange, EventObject event)
name|boolean
name|matchEvent
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

