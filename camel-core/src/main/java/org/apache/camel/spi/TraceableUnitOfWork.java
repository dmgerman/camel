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

begin_comment
comment|/**  * A Unit of work that is also traceable with the  * {@link org.apache.camel.processor.interceptor.TraceInterceptor} so we can trace the excact  * route path a given {@link org.apache.camel.Exchange} has been processed.  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|TraceableUnitOfWork
specifier|public
interface|interface
name|TraceableUnitOfWork
extends|extends
name|UnitOfWork
block|{
comment|/**      * Adds the given node that was intercepted      *      * @param node the node      */
DECL|method|addInterceptedNode (ProcessorDefinition node)
name|void
name|addInterceptedNode
parameter_list|(
name|ProcessorDefinition
name|node
parameter_list|)
function_decl|;
comment|/**      * Gets the last intercepted node, is<tt>null</tt> if no last exists.      */
DECL|method|getLastInterceptedNode ()
name|ProcessorDefinition
name|getLastInterceptedNode
parameter_list|()
function_decl|;
comment|/**      * Gets the current list of intercepted nodes, representing the route path the      * current {@link org.apache.camel.Exchange} has taken.      */
DECL|method|getInterceptedNodes ()
name|List
argument_list|<
name|ProcessorDefinition
argument_list|>
name|getInterceptedNodes
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

