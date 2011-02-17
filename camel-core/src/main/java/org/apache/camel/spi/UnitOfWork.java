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
name|AsyncCallback
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

begin_comment
comment|/**  * An object representing the unit of work processing an {@link Exchange}  * which allows the use of {@link Synchronization} hooks. This object might map one-to-one with  * a transaction in JPA or Spring; or might not.  *  * @version   */
end_comment

begin_interface
DECL|interface|UnitOfWork
specifier|public
interface|interface
name|UnitOfWork
extends|extends
name|Service
block|{
comment|/**      * Adds a synchronization hook      *      * @param synchronization  the hook      */
DECL|method|addSynchronization (Synchronization synchronization)
name|void
name|addSynchronization
parameter_list|(
name|Synchronization
name|synchronization
parameter_list|)
function_decl|;
comment|/**      * Removes a synchronization hook      *      * @param synchronization  the hook      */
DECL|method|removeSynchronization (Synchronization synchronization)
name|void
name|removeSynchronization
parameter_list|(
name|Synchronization
name|synchronization
parameter_list|)
function_decl|;
comment|/**      * Handover all the registered synchronizations to the target {@link org.apache.camel.Exchange}.      *<p/>      * This is used when a route turns into asynchronous and the {@link org.apache.camel.Exchange} that      * is continued and routed in the async thread should do the on completion callbacks instead of the      * original synchronous thread.      *       * @param target  the target exchange      */
DECL|method|handoverSynchronization (Exchange target)
name|void
name|handoverSynchronization
parameter_list|(
name|Exchange
name|target
parameter_list|)
function_decl|;
comment|/**      * Invoked when this unit of work has been completed, whether it has failed or completed      *      * @param exchange the current exchange      */
DECL|method|done (Exchange exchange)
name|void
name|done
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Returns the unique ID of this unit of work, lazily creating one if it does not yet have one      *       * @return the unique ID      */
DECL|method|getId ()
name|String
name|getId
parameter_list|()
function_decl|;
comment|/**      * Gets the original IN {@link Message} this Unit of Work was started with.      *      * @return the original IN {@link Message}      */
DECL|method|getOriginalInMessage ()
name|Message
name|getOriginalInMessage
parameter_list|()
function_decl|;
comment|/**      * Gets tracing information      *      * @return trace information      */
DECL|method|getTracedRouteNodes ()
name|TracedRouteNodes
name|getTracedRouteNodes
parameter_list|()
function_decl|;
comment|/**      * Are we transacted?      *      * @return<tt>true</tt> if transacted,<tt>false</tt> otherwise      */
DECL|method|isTransacted ()
name|boolean
name|isTransacted
parameter_list|()
function_decl|;
comment|/**      * Are we already transacted by the given transaction key?      *      * @param key the transaction key      * @return<tt>true</tt> if already,<tt>false</tt> otherwise      */
DECL|method|isTransactedBy (Object key)
name|boolean
name|isTransactedBy
parameter_list|(
name|Object
name|key
parameter_list|)
function_decl|;
comment|/**      * Mark this UnitOfWork as being transacted by the given transaction key.      *<p/>      * When the transaction is completed then invoke the {@link #endTransactedBy(Object)} method using the same key.      *      * @param key the transaction key      */
DECL|method|beginTransactedBy (Object key)
name|void
name|beginTransactedBy
parameter_list|(
name|Object
name|key
parameter_list|)
function_decl|;
comment|/**      * Mark this UnitOfWork as not transacted anymore by the given transaction definition.      *      * @param key the transaction key      */
DECL|method|endTransactedBy (Object key)
name|void
name|endTransactedBy
parameter_list|(
name|Object
name|key
parameter_list|)
function_decl|;
comment|/**      * Gets the {@link RouteContext} that this {@link UnitOfWork} currently is being routed through.      *<p/>      * Notice that an {@link Exchange} can be routed through multiple routes and thus the      * {@link org.apache.camel.spi.RouteContext} can change over time.      *      * @return the route context      * @see #pushRouteContext(RouteContext)      * @see #popRouteContext()      */
DECL|method|getRouteContext ()
name|RouteContext
name|getRouteContext
parameter_list|()
function_decl|;
comment|/**      * Pushes the {@link RouteContext} that this {@link UnitOfWork} currently is being routed through.      *<p/>      * Notice that an {@link Exchange} can be routed through multiple routes and thus the      * {@link org.apache.camel.spi.RouteContext} can change over time.      *      * @param routeContext the route context      */
DECL|method|pushRouteContext (RouteContext routeContext)
name|void
name|pushRouteContext
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
function_decl|;
comment|/**      * When finished being routed under the current {@link org.apache.camel.spi.RouteContext}      * it should be removed.      *      * @return the route context or<tt>null</tt> if none existed      */
DECL|method|popRouteContext ()
name|RouteContext
name|popRouteContext
parameter_list|()
function_decl|;
comment|/**      * Strategy for optional work to be execute before processing      *<p/>      * For example the {@link org.apache.camel.impl.MDCUnitOfWork} leverages this      * to ensure MDC is handled correctly during routing exchanges using the      * asynchronous routing engine.      *      * @param processor the processor to be executed      * @param exchange  the current exchange      * @param callback the callback      * @return the callback to be used (can return a wrapped callback)      */
DECL|method|beforeProcess (Processor processor, Exchange exchange, AsyncCallback callback)
name|AsyncCallback
name|beforeProcess
parameter_list|(
name|Processor
name|processor
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
function_decl|;
comment|/**      * Strategy for optional work to be executed after the processing      *      * @param processor the processor executed      * @param exchange  the current exchange      * @param callback  the callback used      * @param doneSync  whether the process was done synchronously or asynchronously      */
DECL|method|afterProcess (Processor processor, Exchange exchange, AsyncCallback callback, boolean doneSync)
name|void
name|afterProcess
parameter_list|(
name|Processor
name|processor
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|,
name|boolean
name|doneSync
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

