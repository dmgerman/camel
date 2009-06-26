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
name|InterceptDefinition
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
name|RouteNode
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
comment|/**      * Adds the entry that was intercepted      *      * @param entry the entry      */
DECL|method|addTraced (RouteNode entry)
name|void
name|addTraced
parameter_list|(
name|RouteNode
name|entry
parameter_list|)
function_decl|;
comment|/**      * Gets the last node, is<tt>null</tt> if no last exists.      */
DECL|method|getLastNode ()
name|RouteNode
name|getLastNode
parameter_list|()
function_decl|;
comment|/**      * Gets the 2nd last node, is<tt>null</tt> if no last exists.      */
DECL|method|getSecondLastNode ()
name|RouteNode
name|getSecondLastNode
parameter_list|()
function_decl|;
comment|/**      * Gets the current list of nodes, representing the route path the      * current {@link org.apache.camel.Exchange} has currently taken.      */
DECL|method|getNodes ()
name|List
argument_list|<
name|RouteNode
argument_list|>
name|getNodes
parameter_list|()
function_decl|;
comment|/**      * A private counter that increments, is used to as book keeping how far this      * exchange have been intercepted by the general intercept().      *<p/>      * We need this special book keeping to keep correct order when dealing      * with concurrent exchanges being routed in the same route path.      *      * @param node the intercept node      * @return the current count      */
DECL|method|getAndIncrement (InterceptDefinition node)
name|int
name|getAndIncrement
parameter_list|(
name|InterceptDefinition
name|node
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

