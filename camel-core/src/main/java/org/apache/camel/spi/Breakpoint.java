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
comment|/**  * {@link org.apache.camel.spi.Breakpoint} are used by the {@link org.apache.camel.spi.Debugger} API.  *<p/>  * This allows you to register {@link org.apache.camel.spi.Breakpoint}s to the {@link org.apache.camel.spi.Debugger}  * and have those breakpoints activated when their {@link org.apache.camel.spi.Condition}s match.  *<p/>  * If any exceptions is thrown from the {@link #onExchange(org.apache.camel.Exchange, org.apache.camel.Processor, org.apache.camel.model.ProcessorDefinition)}  * method then the {@link org.apache.camel.spi.Debugger} will catch and log those at<tt>WARN</tt> level and continue.  *  * @see org.apache.camel.spi.Debugger  * @see org.apache.camel.spi.Condition  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|Breakpoint
specifier|public
interface|interface
name|Breakpoint
block|{
comment|// TODO: Hook into the EventNotifier so we can have breakpoints trigger on those conditions as well
comment|// exceptions, create, done, etc. and a FollowMe condition to follow a single exchange
comment|// while others are being routed so you can follow one only, eg need an API on Debugger for that
DECL|enum|State
DECL|enumConstant|Active
DECL|enumConstant|Suspended
enum|enum
name|State
block|{
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
comment|/**      * Callback invoked when the breakpoint was hit.      *      * @param exchange    the {@link Exchange}      * @param processor   the {@link Processor} which is the next target      * @param definition  the {@link org.apache.camel.model.ProcessorDefinition} definition of the processor      */
DECL|method|onExchange (Exchange exchange, Processor processor, ProcessorDefinition definition)
name|void
name|onExchange
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
function_decl|;
block|}
end_interface

end_unit

