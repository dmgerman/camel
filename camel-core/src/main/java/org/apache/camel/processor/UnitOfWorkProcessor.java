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
name|impl
operator|.
name|MDCUnitOfWork
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
name|AsyncProcessorHelper
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
comment|/**  * Ensures the {@link Exchange} is routed under the boundaries of an {@link org.apache.camel.spi.UnitOfWork}.  *<p/>  * Handles calling the {@link org.apache.camel.spi.UnitOfWork#done(org.apache.camel.Exchange)} method  * when processing of an {@link Exchange} is complete.  */
end_comment

begin_class
DECL|class|UnitOfWorkProcessor
specifier|public
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
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
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
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// if a route context has been configured, then wrap the processor with a
comment|// RouteContextProcessor to ensure we track the route context properly during
comment|// processing of the exchange, but only do this once
comment|// TODO: This can possible be removed!
if|if
condition|(
name|routeContext
operator|!=
literal|null
operator|&&
operator|(
operator|!
operator|(
name|processor
operator|instanceof
name|RouteContextProcessor
operator|)
operator|)
condition|)
block|{
name|processor
operator|=
operator|new
name|RouteContextProcessor
argument_list|(
name|routeContext
argument_list|,
name|processor
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
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
name|UnitOfWork
name|uow
init|=
name|createUnitOfWork
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
name|Object
name|synchronous
init|=
name|exchange
operator|.
name|removeProperty
argument_list|(
name|Exchange
operator|.
name|UNIT_OF_WORK_PROCESS_SYNC
argument_list|)
decl_stmt|;
if|if
condition|(
name|synchronous
operator|!=
literal|null
condition|)
block|{
comment|// the exchange signalled to process synchronously
return|return
name|processSync
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|uow
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|processAsync
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
name|uow
argument_list|)
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
DECL|method|processSync (final Exchange exchange, final AsyncCallback callback, final UnitOfWork uow)
specifier|protected
name|boolean
name|processSync
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|,
specifier|final
name|UnitOfWork
name|uow
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Exchange marked UnitOfWork to be processed synchronously: {}"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
comment|// process the exchange synchronously
try|try
block|{
name|AsyncProcessorHelper
operator|.
name|process
argument_list|(
name|processor
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
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|callback
operator|.
name|done
argument_list|(
literal|true
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
return|return
literal|true
return|;
block|}
DECL|method|processAsync (final Exchange exchange, final AsyncCallback callback, final UnitOfWork uow)
specifier|protected
name|boolean
name|processAsync
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|,
specifier|final
name|UnitOfWork
name|uow
parameter_list|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Processing exchange asynchronously: {}"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
comment|// process the exchange asynchronously
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
name|LOG
operator|.
name|warn
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
comment|// fallback and catch any exceptions the process may not have caught
comment|// we must ensure to done the UoW in all cases and issue done on the callback
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
comment|// Order here matters. We need to complete the callbacks
comment|// since they will likely update the exchange with some final results.
try|try
block|{
name|callback
operator|.
name|done
argument_list|(
literal|true
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
return|return
literal|true
return|;
block|}
block|}
comment|/**      * Strategy to create the unit of work for the given exchange.      *      * @param exchange the exchange      * @return the created unit of work      */
DECL|method|createUnitOfWork (Exchange exchange)
specifier|protected
name|UnitOfWork
name|createUnitOfWork
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|UnitOfWork
name|answer
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|isUseMDCLogging
argument_list|()
condition|)
block|{
name|answer
operator|=
operator|new
name|MDCUnitOfWork
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
operator|new
name|DefaultUnitOfWork
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|doneUow (UnitOfWork uow, Exchange exchange)
specifier|private
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
block|}
end_class

end_unit

