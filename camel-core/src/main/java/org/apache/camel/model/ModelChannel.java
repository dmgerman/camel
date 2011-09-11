begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
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
name|spi
operator|.
name|RouteContext
import|;
end_import

begin_interface
DECL|interface|ModelChannel
specifier|public
interface|interface
name|ModelChannel
extends|extends
name|Channel
block|{
comment|/**      * Initializes the channel.      *      * @param outputDefinition  the route definition the {@link Channel} represents      * @param routeContext      the route context      * @throws Exception is thrown if some error occurred      */
DECL|method|initChannel (ProcessorDefinition<?> outputDefinition, RouteContext routeContext)
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
function_decl|;
comment|/**      * Post initializes the channel.      *      * @param outputDefinition  the route definition the {@link Channel} represents      * @param routeContext      the route context      * @throws Exception is thrown if some error occurred      */
DECL|method|postInitChannel (ProcessorDefinition<?> outputDefinition, RouteContext routeContext)
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
function_decl|;
comment|/**      * If the initialized output definition contained outputs (children) then we need to      * set the child so we can leverage fine grained tracing      *      * @param child the child      */
DECL|method|setChildDefinition (ProcessorDefinition<?> child)
name|void
name|setChildDefinition
parameter_list|(
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|child
parameter_list|)
function_decl|;
comment|/**      * Gets the definition of the next processor      *      * @return the processor definition      */
DECL|method|getProcessorDefinition ()
name|ProcessorDefinition
argument_list|<
name|?
argument_list|>
name|getProcessorDefinition
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

