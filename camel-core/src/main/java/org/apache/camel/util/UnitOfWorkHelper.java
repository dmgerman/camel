begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
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
name|SynchronizationRouteAware
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
comment|/**  * Utility methods for {@link org.apache.camel.spi.UnitOfWork}  *  * @version   */
end_comment

begin_class
DECL|class|UnitOfWorkHelper
specifier|public
specifier|final
class|class
name|UnitOfWorkHelper
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
name|UnitOfWorkHelper
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|UnitOfWorkHelper ()
specifier|private
name|UnitOfWorkHelper
parameter_list|()
block|{     }
comment|/**      * Creates a new {@link UnitOfWork}.      *      * @param exchange the exchange      * @return the created unit of work (is not started)      * @deprecated use {@link org.apache.camel.CamelContext#getUnitOfWorkFactory()} instead.      */
annotation|@
name|Deprecated
DECL|method|createUoW (Exchange exchange)
specifier|public
specifier|static
name|UnitOfWork
name|createUoW
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getUnitOfWorkFactory
argument_list|()
operator|.
name|createUnitOfWork
argument_list|(
name|exchange
argument_list|)
return|;
block|}
comment|/**      * Done and stop the {@link UnitOfWork}.      *      * @param uow the unit of work      * @param exchange the exchange (will unset the UoW on the exchange)      */
DECL|method|doneUow (UnitOfWork uow, Exchange exchange)
specifier|public
specifier|static
name|void
name|doneUow
parameter_list|(
name|UnitOfWork
name|uow
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// unit of work is done
try|try
block|{
if|if
condition|(
name|uow
operator|!=
literal|null
condition|)
block|{
name|uow
operator|.
name|done
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
name|LOG
operator|.
name|warn
argument_list|(
literal|"Exception occurred during done UnitOfWork for Exchange: "
operator|+
name|exchange
operator|+
literal|". This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
try|try
block|{
if|if
condition|(
name|uow
operator|!=
literal|null
condition|)
block|{
name|uow
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Exception occurred during stopping UnitOfWork for Exchange: "
operator|+
name|exchange
operator|+
literal|". This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
comment|// remove uow from exchange as its done
name|exchange
operator|.
name|setUnitOfWork
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|doneSynchronizations (Exchange exchange, List<Synchronization> synchronizations, Logger log)
specifier|public
specifier|static
name|void
name|doneSynchronizations
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|List
argument_list|<
name|Synchronization
argument_list|>
name|synchronizations
parameter_list|,
name|Logger
name|log
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
comment|// work on a copy of the list to avoid any modification which may cause ConcurrentModificationException
name|List
argument_list|<
name|Synchronization
argument_list|>
name|copy
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|synchronizations
argument_list|)
decl_stmt|;
comment|// reverse so we invoke it FILO style instead of FIFO
name|Collections
operator|.
name|reverse
argument_list|(
name|copy
argument_list|)
expr_stmt|;
comment|// and honor if any was ordered by sorting it accordingly
name|copy
operator|.
name|sort
argument_list|(
name|OrderedComparator
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
comment|// invoke synchronization callbacks
for|for
control|(
name|Synchronization
name|synchronization
range|:
name|copy
control|)
block|{
try|try
block|{
if|if
condition|(
name|failed
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Invoking synchronization.onFailure: {} with {}"
argument_list|,
name|synchronization
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
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
name|log
operator|.
name|trace
argument_list|(
literal|"Invoking synchronization.onComplete: {} with {}"
argument_list|,
name|synchronization
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
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
name|Throwable
name|e
parameter_list|)
block|{
comment|// must catch exceptions to ensure all synchronizations have a chance to run
name|log
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
block|}
DECL|method|beforeRouteSynchronizations (Route route, Exchange exchange, List<Synchronization> synchronizations, Logger log)
specifier|public
specifier|static
name|void
name|beforeRouteSynchronizations
parameter_list|(
name|Route
name|route
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|List
argument_list|<
name|Synchronization
argument_list|>
name|synchronizations
parameter_list|,
name|Logger
name|log
parameter_list|)
block|{
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
comment|// work on a copy of the list to avoid any modification which may cause ConcurrentModificationException
name|List
argument_list|<
name|Synchronization
argument_list|>
name|copy
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|synchronizations
argument_list|)
decl_stmt|;
comment|// reverse so we invoke it FILO style instead of FIFO
name|Collections
operator|.
name|reverse
argument_list|(
name|copy
argument_list|)
expr_stmt|;
comment|// and honor if any was ordered by sorting it accordingly
name|copy
operator|.
name|sort
argument_list|(
name|OrderedComparator
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
comment|// invoke synchronization callbacks
for|for
control|(
name|Synchronization
name|synchronization
range|:
name|copy
control|)
block|{
if|if
condition|(
name|synchronization
operator|instanceof
name|SynchronizationRouteAware
condition|)
block|{
try|try
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Invoking synchronization.onBeforeRoute: {} with {}"
argument_list|,
name|synchronization
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
operator|(
operator|(
name|SynchronizationRouteAware
operator|)
name|synchronization
operator|)
operator|.
name|onBeforeRoute
argument_list|(
name|route
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
comment|// must catch exceptions to ensure all synchronizations have a chance to run
name|log
operator|.
name|warn
argument_list|(
literal|"Exception occurred during onBeforeRoute. This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
DECL|method|afterRouteSynchronizations (Route route, Exchange exchange, List<Synchronization> synchronizations, Logger log)
specifier|public
specifier|static
name|void
name|afterRouteSynchronizations
parameter_list|(
name|Route
name|route
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|List
argument_list|<
name|Synchronization
argument_list|>
name|synchronizations
parameter_list|,
name|Logger
name|log
parameter_list|)
block|{
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
comment|// work on a copy of the list to avoid any modification which may cause ConcurrentModificationException
name|List
argument_list|<
name|Synchronization
argument_list|>
name|copy
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|synchronizations
argument_list|)
decl_stmt|;
comment|// reverse so we invoke it FILO style instead of FIFO
name|Collections
operator|.
name|reverse
argument_list|(
name|copy
argument_list|)
expr_stmt|;
comment|// and honor if any was ordered by sorting it accordingly
name|copy
operator|.
name|sort
argument_list|(
name|OrderedComparator
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
comment|// invoke synchronization callbacks
for|for
control|(
name|Synchronization
name|synchronization
range|:
name|copy
control|)
block|{
if|if
condition|(
name|synchronization
operator|instanceof
name|SynchronizationRouteAware
condition|)
block|{
try|try
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Invoking synchronization.onAfterRoute: {} with {}"
argument_list|,
name|synchronization
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
operator|(
operator|(
name|SynchronizationRouteAware
operator|)
name|synchronization
operator|)
operator|.
name|onAfterRoute
argument_list|(
name|route
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
comment|// must catch exceptions to ensure all synchronizations have a chance to run
name|log
operator|.
name|warn
argument_list|(
literal|"Exception occurred during onAfterRoute. This exception will be ignored."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

