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
name|Date
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
name|CamelContext
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
name|CamelUnitOfWorkException
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
name|SubUnitOfWork
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
name|SubUnitOfWorkCallback
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
name|UnitOfWorkHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * The default implementation of {@link org.apache.camel.spi.UnitOfWork}  */
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
DECL|field|log
specifier|protected
specifier|final
specifier|transient
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
comment|// TODO: This implementation seems to have transformed itself into a to broad concern
comment|// where unit of work is doing a bit more work than the transactional aspect that ties
comment|// to its name. Maybe this implementation should be named ExchangeContext and we can
comment|// introduce a simpler UnitOfWork concept. This would also allow us to refactor the
comment|// SubUnitOfWork into a general parent/child unit of work concept. However this
comment|// requires API changes and thus is best kept for Camel 3.0
DECL|field|parent
specifier|private
name|UnitOfWork
name|parent
decl_stmt|;
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
DECL|field|context
specifier|private
name|CamelContext
name|context
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
DECL|field|subUnitOfWorks
specifier|private
name|Stack
argument_list|<
name|DefaultSubUnitOfWork
argument_list|>
name|subUnitOfWorks
decl_stmt|;
DECL|method|DefaultUnitOfWork (Exchange exchange)
specifier|public
name|DefaultUnitOfWork
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"UnitOfWork created for ExchangeId: {} with {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|tracedRouteNodes
operator|=
operator|new
name|DefaultTracedRouteNodes
argument_list|()
expr_stmt|;
name|context
operator|=
name|exchange
operator|.
name|getContext
argument_list|()
expr_stmt|;
comment|// TODO: Camel 3.0: the copy on facade strategy will help us here in the future
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
comment|// mark the creation time when this Exchange was created
if|if
condition|(
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|CREATED_TIMESTAMP
argument_list|)
operator|==
literal|null
condition|)
block|{
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|CREATED_TIMESTAMP
argument_list|,
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// inject breadcrumb header if enabled
if|if
condition|(
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|isUseBreadcrumb
argument_list|()
condition|)
block|{
comment|// create or use existing breadcrumb
name|String
name|breadcrumbId
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|BREADCRUMB_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|breadcrumbId
operator|==
literal|null
condition|)
block|{
comment|// no existing breadcrumb, so create a new one based on the message id
name|breadcrumbId
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMessageId
argument_list|()
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|BREADCRUMB_ID
argument_list|,
name|breadcrumbId
argument_list|)
expr_stmt|;
block|}
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
DECL|method|newInstance (Exchange exchange)
name|UnitOfWork
name|newInstance
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
operator|new
name|DefaultUnitOfWork
argument_list|(
name|exchange
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|setParentUnitOfWork (UnitOfWork parentUnitOfWork)
specifier|public
name|void
name|setParentUnitOfWork
parameter_list|(
name|UnitOfWork
name|parentUnitOfWork
parameter_list|)
block|{
name|this
operator|.
name|parent
operator|=
name|parentUnitOfWork
expr_stmt|;
block|}
DECL|method|createChildUnitOfWork (Exchange childExchange)
specifier|public
name|UnitOfWork
name|createChildUnitOfWork
parameter_list|(
name|Exchange
name|childExchange
parameter_list|)
block|{
comment|// create a new child unit of work, and mark me as its parent
name|UnitOfWork
name|answer
init|=
name|newInstance
argument_list|(
name|childExchange
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setParentUnitOfWork
argument_list|(
name|this
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
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
if|if
condition|(
name|subUnitOfWorks
operator|!=
literal|null
condition|)
block|{
name|subUnitOfWorks
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
name|originalInMessage
operator|=
literal|null
expr_stmt|;
name|parent
operator|=
literal|null
expr_stmt|;
name|id
operator|=
literal|null
expr_stmt|;
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
name|log
operator|.
name|trace
argument_list|(
literal|"Adding synchronization {}"
argument_list|,
name|synchronization
argument_list|)
expr_stmt|;
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
name|log
operator|.
name|trace
argument_list|(
literal|"Handover synchronization {} to: {}"
argument_list|,
name|synchronization
argument_list|,
name|target
argument_list|)
expr_stmt|;
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
name|log
operator|.
name|trace
argument_list|(
literal|"Handover not allow for synchronization {}"
argument_list|,
name|synchronization
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|done (Exchange exchange)
specifier|public
name|void
name|done
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"UnitOfWork done for ExchangeId: {} with {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|boolean
name|failed
init|=
name|exchange
operator|.
name|isFailed
argument_list|()
decl_stmt|;
comment|// at first done the synchronizations
name|UnitOfWorkHelper
operator|.
name|doneSynchronizations
argument_list|(
name|exchange
argument_list|,
name|synchronizations
argument_list|,
name|log
argument_list|)
expr_stmt|;
comment|// notify uow callback if in use
try|try
block|{
name|SubUnitOfWorkCallback
name|uowCallback
init|=
name|getSubUnitOfWorkCallback
argument_list|()
decl_stmt|;
if|if
condition|(
name|uowCallback
operator|!=
literal|null
condition|)
block|{
name|uowCallback
operator|.
name|onDone
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// must catch exceptions to ensure synchronizations is also invoked
name|log
operator|.
name|warn
argument_list|(
literal|"Exception occurred during savepoint onDone. This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
comment|// then fire event to signal the exchange is done
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
name|Throwable
name|e
parameter_list|)
block|{
comment|// must catch exceptions to ensure synchronizations is also invoked
name|log
operator|.
name|warn
argument_list|(
literal|"Exception occurred during event notification. This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
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
name|context
operator|.
name|getUuidGenerator
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
DECL|method|isTransacted ()
specifier|public
name|boolean
name|isTransacted
parameter_list|()
block|{
return|return
name|transactedBy
operator|!=
literal|null
operator|&&
operator|!
name|transactedBy
operator|.
name|isEmpty
argument_list|()
return|;
block|}
DECL|method|isTransactedBy (Object key)
specifier|public
name|boolean
name|isTransactedBy
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
return|return
name|getTransactedBy
argument_list|()
operator|.
name|contains
argument_list|(
name|key
argument_list|)
return|;
block|}
DECL|method|beginTransactedBy (Object key)
specifier|public
name|void
name|beginTransactedBy
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|getTransactedBy
argument_list|()
operator|.
name|add
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
DECL|method|endTransactedBy (Object key)
specifier|public
name|void
name|endTransactedBy
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|getTransactedBy
argument_list|()
operator|.
name|remove
argument_list|(
name|key
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
DECL|method|beforeProcess (Processor processor, Exchange exchange, AsyncCallback callback)
specifier|public
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
block|{
comment|// no wrapping needed
return|return
name|callback
return|;
block|}
DECL|method|afterProcess (Processor processor, Exchange exchange, AsyncCallback callback, boolean doneSync)
specifier|public
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
block|{     }
annotation|@
name|Override
DECL|method|beginSubUnitOfWork (Exchange exchange)
specifier|public
name|void
name|beginSubUnitOfWork
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"beginSubUnitOfWork exchangeId: {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|subUnitOfWorks
operator|==
literal|null
condition|)
block|{
name|subUnitOfWorks
operator|=
operator|new
name|Stack
argument_list|<
name|DefaultSubUnitOfWork
argument_list|>
argument_list|()
expr_stmt|;
block|}
comment|// push a new savepoint
name|subUnitOfWorks
operator|.
name|push
argument_list|(
operator|new
name|DefaultSubUnitOfWork
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|endSubUnitOfWork (Exchange exchange)
specifier|public
name|void
name|endSubUnitOfWork
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"endSubUnitOfWork exchangeId: {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|subUnitOfWorks
operator|==
literal|null
operator|||
name|subUnitOfWorks
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
comment|// pop last sub unit of work as its now ended
name|SubUnitOfWork
name|subUoW
init|=
name|subUnitOfWorks
operator|.
name|pop
argument_list|()
decl_stmt|;
if|if
condition|(
name|subUoW
operator|.
name|isFailed
argument_list|()
condition|)
block|{
comment|// the sub unit of work failed so set an exception containing all the caused exceptions
comment|// and mark the exchange for rollback only
comment|// if there are multiple exceptions then wrap those into another exception with them all
name|Exception
name|cause
decl_stmt|;
name|List
argument_list|<
name|Exception
argument_list|>
name|list
init|=
name|subUoW
operator|.
name|getExceptions
argument_list|()
decl_stmt|;
if|if
condition|(
name|list
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|list
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
name|cause
operator|=
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cause
operator|=
operator|new
name|CamelUnitOfWorkException
argument_list|(
name|exchange
argument_list|,
name|list
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|setException
argument_list|(
name|cause
argument_list|)
expr_stmt|;
block|}
comment|// mark it as rollback and that the unit of work is exhausted. This ensures that we do not try
comment|// to redeliver this exception (again)
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|ROLLBACK_ONLY
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|UNIT_OF_WORK_EXHAUSTED
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// and remove any indications of error handled which will prevent this exception to be noticed
comment|// by the error handler which we want to react with the result of the sub unit of work
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|ERRORHANDLER_HANDLED
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|FAILURE_HANDLED
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"endSubUnitOfWork exchangeId: {} with {} caused exceptions."
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|getSubUnitOfWorkCallback ()
specifier|public
name|SubUnitOfWorkCallback
name|getSubUnitOfWorkCallback
parameter_list|()
block|{
comment|// if there is a parent-child relationship between unit of works
comment|// then we should use the callback strategies from the parent
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
return|return
name|parent
operator|.
name|getSubUnitOfWorkCallback
argument_list|()
return|;
block|}
if|if
condition|(
name|subUnitOfWorks
operator|==
literal|null
operator|||
name|subUnitOfWorks
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
name|subUnitOfWorks
operator|.
name|peek
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
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"DefaultUnitOfWork"
return|;
block|}
block|}
end_class

end_unit

