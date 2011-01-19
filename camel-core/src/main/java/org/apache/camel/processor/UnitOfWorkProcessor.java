begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|AsyncProcessor
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
name|impl
operator|.
name|DefaultUnitOfWork
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

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
import|;
end_import

begin_comment
comment|/**  * Ensures the {@link Exchange} is routed under the boundaries of an {@link org.apache.camel.spi.UnitOfWork}.  *<p/>  * Handles calling the {@link org.apache.camel.spi.UnitOfWork#done(org.apache.camel.Exchange)} method  * when processing of an {@link Exchange} is complete.  */
end_comment

begin_class
DECL|class|UnitOfWorkProcessor
specifier|public
specifier|final
class|class
name|UnitOfWorkProcessor
extends|extends
name|DelegateAsyncProcessor
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
name|UnitOfWorkProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|routeContext
specifier|private
specifier|final
name|RouteContext
name|routeContext
decl_stmt|;
DECL|field|routeId
specifier|private
specifier|final
name|String
name|routeId
decl_stmt|;
DECL|method|UnitOfWorkProcessor (Processor processor)
specifier|public
name|UnitOfWorkProcessor
parameter_list|(
name|Processor
name|processor
parameter_list|)
block|{
name|this
argument_list|(
literal|null
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
DECL|method|UnitOfWorkProcessor (AsyncProcessor processor)
specifier|public
name|UnitOfWorkProcessor
parameter_list|(
name|AsyncProcessor
name|processor
parameter_list|)
block|{
name|this
argument_list|(
literal|null
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
DECL|method|UnitOfWorkProcessor (RouteContext routeContext, Processor processor)
specifier|public
name|UnitOfWorkProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|Processor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|routeContext
operator|=
name|routeContext
expr_stmt|;
if|if
condition|(
name|routeContext
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|routeId
operator|=
name|routeContext
operator|.
name|getRoute
argument_list|()
operator|.
name|idOrCreate
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getNodeIdFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|routeId
operator|=
literal|null
expr_stmt|;
block|}
block|}
DECL|method|UnitOfWorkProcessor (RouteContext routeContext, AsyncProcessor processor)
specifier|public
name|UnitOfWorkProcessor
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|,
name|AsyncProcessor
name|processor
parameter_list|)
block|{
name|super
argument_list|(
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|routeContext
operator|=
name|routeContext
expr_stmt|;
if|if
condition|(
name|routeContext
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|routeId
operator|=
name|routeContext
operator|.
name|getRoute
argument_list|()
operator|.
name|idOrCreate
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getNodeIdFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|routeId
operator|=
literal|null
expr_stmt|;
block|}
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
literal|"UnitOfWork("
operator|+
name|processor
operator|+
literal|")"
return|;
block|}
DECL|method|getRouteContext ()
specifier|public
name|RouteContext
name|getRouteContext
parameter_list|()
block|{
return|return
name|routeContext
return|;
block|}
annotation|@
name|Override
DECL|method|process (final Exchange exchange, final AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{
comment|// if the exchange doesn't have from route id set, then set it if it originated
comment|// from this unit of work
if|if
condition|(
name|routeId
operator|!=
literal|null
operator|&&
name|exchange
operator|.
name|getFromRouteId
argument_list|()
operator|==
literal|null
condition|)
block|{
name|exchange
operator|.
name|setFromRouteId
argument_list|(
name|routeId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|==
literal|null
condition|)
block|{
comment|// If there is no existing UoW, then we should start one and
comment|// terminate it once processing is completed for the exchange.
specifier|final
name|DefaultUnitOfWork
name|uow
init|=
operator|new
name|DefaultUnitOfWork
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|setUnitOfWork
argument_list|(
name|uow
argument_list|)
expr_stmt|;
try|try
block|{
name|uow
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
comment|// process the exchange
try|try
block|{
return|return
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
operator|new
name|AsyncCallback
argument_list|()
block|{
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
comment|// Order here matters. We need to complete the callbacks
comment|// since they will likely update the exchange with some final results.
try|try
block|{
name|callback
operator|.
name|done
argument_list|(
name|doneSync
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|doneUow
argument_list|(
name|uow
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Caught unhandled exception while processing ExchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
comment|// fallback and catch any exceptions the process may not have caught
comment|// we must ensure to done the UoW in all cases and issue done on the callback
name|doneUow
argument_list|(
name|uow
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
else|else
block|{
comment|// There was an existing UoW, so we should just pass through..
comment|// so that the guy the initiated the UoW can terminate it.
return|return
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|)
return|;
block|}
block|}
DECL|method|doneUow (DefaultUnitOfWork uow, Exchange exchange)
specifier|private
name|void
name|doneUow
parameter_list|(
name|DefaultUnitOfWork
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
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
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
name|uow
operator|.
name|stop
argument_list|()
expr_stmt|;
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
name|exchange
operator|.
name|setUnitOfWork
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

