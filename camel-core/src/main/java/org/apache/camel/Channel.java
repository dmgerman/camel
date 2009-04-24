begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|RouteContext
import|;
end_import

begin_comment
comment|/**  * Channel acts as a channel between {@link Processor}s in the route graph.  *<p/>  * The channel is responsible for routing the {@link Exchange} to the next {@link Processor} in the route graph.  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|Channel
specifier|public
interface|interface
name|Channel
extends|extends
name|Processor
block|{
comment|// TODO: This interface method names are not 100% settled yet
comment|// some methods should many be moved to DefaultChannel only as they are more used for testing purpose
comment|// and we should add methods to traverse the channels
comment|// and maybe a channel registry
comment|/**      * Sets the processor that the channel should route the {@link Exchange} to.      *      * @param output  the next output      */
DECL|method|setNextProcessor (Processor output)
name|void
name|setNextProcessor
parameter_list|(
name|Processor
name|output
parameter_list|)
function_decl|;
comment|/**      * Sets the {@link org.apache.camel.processor.ErrorHandler} that the Channel uses.       * @param errorHandler the error handler      */
DECL|method|setErrorHandler (Processor errorHandler)
name|void
name|setErrorHandler
parameter_list|(
name|Processor
name|errorHandler
parameter_list|)
function_decl|;
DECL|method|getErrorHandler ()
name|Processor
name|getErrorHandler
parameter_list|()
function_decl|;
comment|/**      * Adds a {@link org.apache.camel.spi.InterceptStrategy} to apply each {@link Exchange} before      * its routed to the next {@link Processor}.      *      * @param strategy  the intercept strategy      */
DECL|method|addInterceptStrategy (InterceptStrategy strategy)
name|void
name|addInterceptStrategy
parameter_list|(
name|InterceptStrategy
name|strategy
parameter_list|)
function_decl|;
comment|/**      * Adds a list of {@link org.apache.camel.spi.InterceptStrategy} to apply each {@link Exchange} before      * its routed to the next {@link Processor}.      *      * @param strategy  list of strategies      */
DECL|method|addInterceptStrategies (List<InterceptStrategy> strategy)
name|void
name|addInterceptStrategies
parameter_list|(
name|List
argument_list|<
name|InterceptStrategy
argument_list|>
name|strategy
parameter_list|)
function_decl|;
comment|/**      * Initializes the channel.      *      * @param outputDefinition  the route defintion the {@link Channel} represents      * @param routeContext      the route context      * @throws Exception is thrown if some error occured      */
DECL|method|initChannel (ProcessorDefinition outputDefinition, RouteContext routeContext)
name|void
name|initChannel
parameter_list|(
name|ProcessorDefinition
name|outputDefinition
parameter_list|,
name|RouteContext
name|routeContext
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Gets the wrapped output that at runtime should be delegated to.      *      * @return the output delegated to      */
DECL|method|getOutput ()
name|Processor
name|getOutput
parameter_list|()
function_decl|;
comment|/**      * Gets the original next {@link Processor} that is not wrapped.      *      * @return  the next processor      */
DECL|method|getNextProcessor ()
name|Processor
name|getNextProcessor
parameter_list|()
function_decl|;
DECL|method|hasInterceptorStrategy (Class type)
name|boolean
name|hasInterceptorStrategy
parameter_list|(
name|Class
name|type
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

