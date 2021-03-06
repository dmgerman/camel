begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
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
name|spi
operator|.
name|RouteContext
import|;
end_import

begin_comment
comment|/**  * Channel acts as a channel between {@link Processor}s in the route graph.  *<p/>  * The channel is responsible for routing the {@link Exchange} to the next {@link Processor} in the route graph.  */
end_comment

begin_interface
DECL|interface|Channel
specifier|public
interface|interface
name|Channel
extends|extends
name|AsyncProcessor
extends|,
name|Navigate
argument_list|<
name|Processor
argument_list|>
block|{
comment|/**      * Gets the {@link org.apache.camel.processor.ErrorHandler} this Channel uses.      *      * @return the error handler, or<tt>null</tt> if no error handler is used.      */
DECL|method|getErrorHandler ()
name|Processor
name|getErrorHandler
parameter_list|()
function_decl|;
comment|/**      * Gets the wrapped output that at runtime should be delegated to.      *      * @return the output to route the {@link Exchange} to      */
DECL|method|getOutput ()
name|Processor
name|getOutput
parameter_list|()
function_decl|;
comment|/**      * Gets the next {@link Processor} to route to (not wrapped)      *      * @return  the next processor      */
DECL|method|getNextProcessor ()
name|Processor
name|getNextProcessor
parameter_list|()
function_decl|;
comment|/**      * Gets the {@link RouteContext}      *      * @return the route context      */
DECL|method|getRouteContext ()
name|RouteContext
name|getRouteContext
parameter_list|()
function_decl|;
comment|/**      * Gets the definition of the next processor      *      * @return the processor definition      */
DECL|method|getProcessorDefinition ()
name|NamedNode
name|getProcessorDefinition
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

