begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Stack
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
name|spi
operator|.
name|RouteContext
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
name|Synchronization
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
name|SynchronizationVetoable
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
name|TracedRouteNodes
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
name|UnitOfWork
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
name|util
operator|.
name|EventHelper
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
name|util
operator|.
name|OrderedComparator
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
name|util
operator|.
name|UuidGenerator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * The default implementation of {@link org.apache.camel.spi.UnitOfWork}  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultUnitOfWork
specifier|public
class|class
name|DefaultUnitOfWork
implements|implements
name|UnitOfWork
implements|,
name|Service
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|DefaultUnitOfWork
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
DECL|field|synchronizations
specifier|private
name|List
argument_list|<
name|Synchronization
argument_list|>
name|synchronizations
decl_stmt|;
DECL|field|originalInMessage
specifier|private
name|Message
name|originalInMessage
decl_stmt|;
DECL|field|tracedRouteNodes
specifier|private
specifier|final
name|TracedRouteNodes
name|tracedRouteNodes
decl_stmt|;
DECL|field|transactedBy
specifier|private
name|Set
argument_list|<
name|Object
argument_list|>
name|transactedBy
decl_stmt|;
DECL|field|routeContextStack
specifier|private
specifier|final
name|Stack
argument_list|<
name|RouteContext
argument_list|>
name|routeContextStack
init|=
operator|new
name|Stack
argument_list|<
name|RouteContext
argument_list|>
argument_list|()
decl_stmt|;
DECL|method|DefaultUnitOfWork (Exchange exchange)
specifier|public
name|DefaultUnitOfWork
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|tracedRouteNodes
operator|=
operator|new
name|DefaultTracedRouteNodes
argument_list|()
expr_stmt|;
comment|// TODO: the copy on facade strategy will help us here in the future
comment|// TODO: optimize to only copy original message if enabled to do so in the route
comment|// special for JmsMessage as it can cause it to loose headers later.
comment|// This will be resolved when we get the message facade with copy on write implemented
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getSimpleName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"JmsMessage"
argument_list|)
condition|)
block|{
name|this
operator|.
name|originalInMessage
operator|=
operator|new
name|DefaultMessage
argument_list|()
expr_stmt|;
name|this
operator|.
name|originalInMessage
operator|.
name|setBody
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
comment|// cannot copy headers with a JmsMessage as the underlying javax.jms.Message object goes nuts
block|}
else|else
block|{
name|this
operator|.
name|originalInMessage
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|copy
argument_list|()
expr_stmt|;
block|}
comment|// fire event
name|EventHelper
operator|.
name|notifyExchangeCreated
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
comment|// register to inflight registry
if|if
condition|(
name|exchange
operator|.
name|getContext
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getInflightRepository
argument_list|()
operator|.
name|add
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{
name|id
operator|=
literal|null
expr_stmt|;
block|}
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// need to clean up when we are stopping to not leak memory
if|if
condition|(
name|synchronizations
operator|!=
literal|null
condition|)
block|{
name|synchronizations
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|tracedRouteNodes
operator|!=
literal|null
condition|)
block|{
name|tracedRouteNodes
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|transactedBy
operator|!=
literal|null
condition|)
block|{
name|transactedBy
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
name|originalInMessage
operator|=
literal|null
expr_stmt|;
if|if
condition|(
operator|!
name|routeContextStack
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|routeContextStack
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|addSynchronization (Synchronization synchronization)
specifier|public
specifier|synchronized
name|void
name|addSynchronization
parameter_list|(
name|Synchronization
name|synchronization
parameter_list|)
block|{
if|if
condition|(
name|synchronizations
operator|==
literal|null
condition|)
block|{
name|synchronizations
operator|=
operator|new
name|ArrayList
argument_list|<
name|Synchronization
argument_list|>
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Adding synchronization "
operator|+
name|synchronization
argument_list|)
expr_stmt|;
block|}
name|synchronizations
operator|.
name|add
argument_list|(
name|synchronization
argument_list|)
expr_stmt|;
block|}
DECL|method|removeSynchronization (Synchronization synchronization)
specifier|public
specifier|synchronized
name|void
name|removeSynchronization
parameter_list|(
name|Synchronization
name|synchronization
parameter_list|)
block|{
if|if
condition|(
name|synchronizations
operator|!=
literal|null
condition|)
block|{
name|synchronizations
operator|.
name|remove
argument_list|(
name|synchronization
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|handoverSynchronization (Exchange target)
specifier|public
name|void
name|handoverSynchronization
parameter_list|(
name|Exchange
name|target
parameter_list|)
block|{
if|if
condition|(
name|synchronizations
operator|==
literal|null
operator|||
name|synchronizations
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
name|Iterator
argument_list|<
name|Synchronization
argument_list|>
name|it
init|=
name|synchronizations
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Synchronization
name|synchronization
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|boolean
name|handover
init|=
literal|true
decl_stmt|;
if|if
condition|(
name|synchronization
operator|instanceof
name|SynchronizationVetoable
condition|)
block|{
name|SynchronizationVetoable
name|veto
init|=
operator|(
name|SynchronizationVetoable
operator|)
name|synchronization
decl_stmt|;
name|handover
operator|=
name|veto
operator|.
name|allowHandover
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|handover
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Handover synchronization "
operator|+
name|synchronization
operator|+
literal|" to Exchange: "
operator|+
name|target
argument_list|)
expr_stmt|;
block|}
name|target
operator|.
name|addOnCompletion
argument_list|(
name|synchronization
argument_list|)
expr_stmt|;
comment|// remove it if its handed over
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Handover not allow for synchronization "
operator|+
name|synchronization
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|done (Exchange exchange)
specifier|public
name|void
name|done
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|boolean
name|failed
init|=
name|exchange
operator|.
name|isFailed
argument_list|()
decl_stmt|;
comment|// fire event to signal the exchange is done
try|try
block|{
if|if
condition|(
name|failed
condition|)
block|{
name|EventHelper
operator|.
name|notifyExchangeFailed
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|EventHelper
operator|.
name|notifyExchangeDone
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// must catch exceptions to ensure synchronizations is also invoked
name|LOG
operator|.
name|warn
argument_list|(
literal|"Exception occurred during event notification. This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|synchronizations
operator|!=
literal|null
operator|&&
operator|!
name|synchronizations
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// reverse so we invoke it FILO style instead of FIFO
name|Collections
operator|.
name|reverse
argument_list|(
name|synchronizations
argument_list|)
expr_stmt|;
comment|// and honor if any was ordered by sorting it accordingly
name|Collections
operator|.
name|sort
argument_list|(
name|synchronizations
argument_list|,
operator|new
name|OrderedComparator
argument_list|()
argument_list|)
expr_stmt|;
comment|// invoke synchronization callbacks
for|for
control|(
name|Synchronization
name|synchronization
range|:
name|synchronizations
control|)
block|{
try|try
block|{
if|if
condition|(
name|failed
condition|)
block|{
name|synchronization
operator|.
name|onFailure
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|synchronization
operator|.
name|onComplete
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// must catch exceptions to ensure all synchronizations have a chance to run
name|LOG
operator|.
name|warn
argument_list|(
literal|"Exception occurred during onCompletion. This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// unregister from inflight registry
if|if
condition|(
name|exchange
operator|.
name|getContext
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getInflightRepository
argument_list|()
operator|.
name|remove
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
if|if
condition|(
name|id
operator|==
literal|null
condition|)
block|{
name|id
operator|=
name|UuidGenerator
operator|.
name|get
argument_list|()
operator|.
name|generateUuid
argument_list|()
expr_stmt|;
block|}
return|return
name|id
return|;
block|}
DECL|method|getOriginalInMessage ()
specifier|public
name|Message
name|getOriginalInMessage
parameter_list|()
block|{
return|return
name|originalInMessage
return|;
block|}
DECL|method|getTracedRouteNodes ()
specifier|public
name|TracedRouteNodes
name|getTracedRouteNodes
parameter_list|()
block|{
return|return
name|tracedRouteNodes
return|;
block|}
DECL|method|isTransactedBy (Object transactionDefinition)
specifier|public
name|boolean
name|isTransactedBy
parameter_list|(
name|Object
name|transactionDefinition
parameter_list|)
block|{
return|return
name|getTransactedBy
argument_list|()
operator|.
name|contains
argument_list|(
name|transactionDefinition
argument_list|)
return|;
block|}
DECL|method|beginTransactedBy (Object transactionDefinition)
specifier|public
name|void
name|beginTransactedBy
parameter_list|(
name|Object
name|transactionDefinition
parameter_list|)
block|{
name|getTransactedBy
argument_list|()
operator|.
name|add
argument_list|(
name|transactionDefinition
argument_list|)
expr_stmt|;
block|}
DECL|method|endTransactedBy (Object transactionDefinition)
specifier|public
name|void
name|endTransactedBy
parameter_list|(
name|Object
name|transactionDefinition
parameter_list|)
block|{
name|getTransactedBy
argument_list|()
operator|.
name|remove
argument_list|(
name|transactionDefinition
argument_list|)
expr_stmt|;
block|}
DECL|method|getRouteContext ()
specifier|public
name|RouteContext
name|getRouteContext
parameter_list|()
block|{
if|if
condition|(
name|routeContextStack
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|routeContextStack
operator|.
name|peek
argument_list|()
return|;
block|}
DECL|method|pushRouteContext (RouteContext routeContext)
specifier|public
name|void
name|pushRouteContext
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
name|routeContextStack
operator|.
name|add
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
block|}
DECL|method|popRouteContext ()
specifier|public
name|RouteContext
name|popRouteContext
parameter_list|()
block|{
if|if
condition|(
name|routeContextStack
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|routeContextStack
operator|.
name|pop
argument_list|()
return|;
block|}
DECL|method|getTransactedBy ()
specifier|private
name|Set
argument_list|<
name|Object
argument_list|>
name|getTransactedBy
parameter_list|()
block|{
if|if
condition|(
name|transactedBy
operator|==
literal|null
condition|)
block|{
name|transactedBy
operator|=
operator|new
name|LinkedHashSet
argument_list|<
name|Object
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|transactedBy
return|;
block|}
block|}
end_class

end_unit

