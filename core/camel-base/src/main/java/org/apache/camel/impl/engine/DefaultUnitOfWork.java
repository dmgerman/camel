begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl.engine
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|engine
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayDeque
import|;
end_import

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
name|Deque
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
name|function
operator|.
name|Predicate
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
name|Route
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
name|support
operator|.
name|DefaultMessage
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
name|support
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
name|support
operator|.
name|MessageSupport
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
name|support
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
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DefaultUnitOfWork
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// TODO: This implementation seems to have transformed itself into a to broad concern
comment|//   where unit of work is doing a bit more work than the transactional aspect that ties
comment|//   to its name. Maybe this implementation should be named ExchangeContext and we can
comment|//   introduce a simpler UnitOfWork concept. This would also allow us to refactor the
comment|//   SubUnitOfWork into a general parent/child unit of work concept. However this
comment|//   requires API changes and thus is best kept for Camel 3.0
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
name|Deque
argument_list|<
name|RouteContext
argument_list|>
name|routeContextStack
init|=
operator|new
name|ArrayDeque
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|log
specifier|private
specifier|final
specifier|transient
name|Logger
name|log
decl_stmt|;
DECL|method|DefaultUnitOfWork (Exchange exchange)
specifier|public
name|DefaultUnitOfWork
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|this
argument_list|(
name|exchange
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
block|}
DECL|method|DefaultUnitOfWork (Exchange exchange, Logger logger)
specifier|protected
name|DefaultUnitOfWork
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Logger
name|logger
parameter_list|)
block|{
name|log
operator|=
name|logger
expr_stmt|;
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
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
block|}
name|context
operator|=
name|exchange
operator|.
name|getContext
argument_list|()
expr_stmt|;
if|if
condition|(
name|context
operator|.
name|isAllowUseOriginalMessage
argument_list|()
condition|)
block|{
comment|// special for JmsMessage as it can cause it to loose headers later.
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
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
literal|"org.apache.camel.component.jms.JmsMessage"
argument_list|)
condition|)
block|{
name|this
operator|.
name|originalInMessage
operator|=
operator|new
name|DefaultMessage
argument_list|(
name|context
argument_list|)
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
name|this
operator|.
name|originalInMessage
operator|.
name|getHeaders
argument_list|()
operator|.
name|putAll
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
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
comment|// must preserve exchange on the original in message
if|if
condition|(
name|this
operator|.
name|originalInMessage
operator|instanceof
name|MessageSupport
condition|)
block|{
operator|(
operator|(
name|MessageSupport
operator|)
name|this
operator|.
name|originalInMessage
operator|)
operator|.
name|setExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
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
comment|// no existing breadcrumb, so create a new one based on the exchange id
name|breadcrumbId
operator|=
name|exchange
operator|.
name|getExchangeId
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
comment|// setup whether the exchange is externally redelivered or not (if not initialized before)
comment|// store as property so we know that the origin exchange was redelivered
if|if
condition|(
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|EXTERNAL_REDELIVERED
argument_list|)
operator|==
literal|null
condition|)
block|{
name|Boolean
name|redelivered
init|=
name|exchange
operator|.
name|isExternalRedelivered
argument_list|()
decl_stmt|;
if|if
condition|(
name|redelivered
operator|==
literal|null
condition|)
block|{
comment|// not from a transactional resource so mark it as false by default
name|redelivered
operator|=
literal|false
expr_stmt|;
block|}
name|exchange
operator|.
name|setProperty
argument_list|(
name|Exchange
operator|.
name|EXTERNAL_REDELIVERED
argument_list|,
name|redelivered
argument_list|)
expr_stmt|;
block|}
comment|// fire event
try|try
block|{
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
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// must catch exceptions to ensure the exchange is not failing due to notification event failed
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
name|routeContextStack
operator|.
name|clear
argument_list|()
expr_stmt|;
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
argument_list|<>
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
DECL|method|containsSynchronization (Synchronization synchronization)
specifier|public
specifier|synchronized
name|boolean
name|containsSynchronization
parameter_list|(
name|Synchronization
name|synchronization
parameter_list|)
block|{
return|return
name|synchronizations
operator|!=
literal|null
operator|&&
name|synchronizations
operator|.
name|contains
argument_list|(
name|synchronization
argument_list|)
return|;
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
name|handoverSynchronization
argument_list|(
name|target
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|handoverSynchronization (Exchange target, Predicate<Synchronization> filter)
specifier|public
name|void
name|handoverSynchronization
parameter_list|(
name|Exchange
name|target
parameter_list|,
name|Predicate
argument_list|<
name|Synchronization
argument_list|>
name|filter
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
operator|&&
operator|(
name|filter
operator|==
literal|null
operator|||
name|filter
operator|.
name|test
argument_list|(
name|synchronization
argument_list|)
operator|)
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
comment|// unregister from inflight registry, before signalling we are done
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
block|}
annotation|@
name|Override
DECL|method|beforeRoute (Exchange exchange, Route route)
specifier|public
name|void
name|beforeRoute
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Route
name|route
parameter_list|)
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"UnitOfWork beforeRoute: {} for ExchangeId: {} with {}"
argument_list|,
name|route
operator|.
name|getId
argument_list|()
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
name|UnitOfWorkHelper
operator|.
name|beforeRouteSynchronizations
argument_list|(
name|route
argument_list|,
name|exchange
argument_list|,
name|synchronizations
argument_list|,
name|log
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|afterRoute (Exchange exchange, Route route)
specifier|public
name|void
name|afterRoute
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Route
name|route
parameter_list|)
block|{
if|if
condition|(
name|log
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"UnitOfWork afterRoute: {} for ExchangeId: {} with {}"
argument_list|,
name|route
operator|.
name|getId
argument_list|()
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
name|UnitOfWorkHelper
operator|.
name|afterRouteSynchronizations
argument_list|(
name|route
argument_list|,
name|exchange
argument_list|,
name|synchronizations
argument_list|,
name|log
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|originalInMessage
operator|==
literal|null
operator|&&
operator|!
name|context
operator|.
name|isAllowUseOriginalMessage
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"AllowUseOriginalMessage is disabled. Cannot access the original message."
argument_list|)
throw|;
block|}
return|return
name|originalInMessage
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
name|push
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
return|return
name|routeContextStack
operator|.
name|pollFirst
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
argument_list|<>
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

