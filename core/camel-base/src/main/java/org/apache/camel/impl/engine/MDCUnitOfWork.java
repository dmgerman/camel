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

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|MDC
import|;
end_import

begin_comment
comment|/**  * This unit of work supports<a href="http://www.slf4j.org/api/org/slf4j/MDC.html">MDC</a>.  */
end_comment

begin_class
DECL|class|MDCUnitOfWork
specifier|public
class|class
name|MDCUnitOfWork
extends|extends
name|DefaultUnitOfWork
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
name|MDCUnitOfWork
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|originalBreadcrumbId
specifier|private
specifier|final
name|String
name|originalBreadcrumbId
decl_stmt|;
DECL|field|originalExchangeId
specifier|private
specifier|final
name|String
name|originalExchangeId
decl_stmt|;
DECL|field|originalMessageId
specifier|private
specifier|final
name|String
name|originalMessageId
decl_stmt|;
DECL|field|originalCorrelationId
specifier|private
specifier|final
name|String
name|originalCorrelationId
decl_stmt|;
DECL|field|originalRouteId
specifier|private
specifier|final
name|String
name|originalRouteId
decl_stmt|;
DECL|field|originalStepId
specifier|private
specifier|final
name|String
name|originalStepId
decl_stmt|;
DECL|field|originalCamelContextId
specifier|private
specifier|final
name|String
name|originalCamelContextId
decl_stmt|;
DECL|field|originalTransactionKey
specifier|private
specifier|final
name|String
name|originalTransactionKey
decl_stmt|;
DECL|method|MDCUnitOfWork (Exchange exchange)
specifier|public
name|MDCUnitOfWork
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|super
argument_list|(
name|exchange
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
comment|// remember existing values
name|this
operator|.
name|originalExchangeId
operator|=
name|MDC
operator|.
name|get
argument_list|(
name|MDC_EXCHANGE_ID
argument_list|)
expr_stmt|;
name|this
operator|.
name|originalMessageId
operator|=
name|MDC
operator|.
name|get
argument_list|(
name|MDC_MESSAGE_ID
argument_list|)
expr_stmt|;
name|this
operator|.
name|originalBreadcrumbId
operator|=
name|MDC
operator|.
name|get
argument_list|(
name|MDC_BREADCRUMB_ID
argument_list|)
expr_stmt|;
name|this
operator|.
name|originalCorrelationId
operator|=
name|MDC
operator|.
name|get
argument_list|(
name|MDC_CORRELATION_ID
argument_list|)
expr_stmt|;
name|this
operator|.
name|originalRouteId
operator|=
name|MDC
operator|.
name|get
argument_list|(
name|MDC_ROUTE_ID
argument_list|)
expr_stmt|;
name|this
operator|.
name|originalStepId
operator|=
name|MDC
operator|.
name|get
argument_list|(
name|MDC_STEP_ID
argument_list|)
expr_stmt|;
name|this
operator|.
name|originalCamelContextId
operator|=
name|MDC
operator|.
name|get
argument_list|(
name|MDC_CAMEL_CONTEXT_ID
argument_list|)
expr_stmt|;
name|this
operator|.
name|originalTransactionKey
operator|=
name|MDC
operator|.
name|get
argument_list|(
name|MDC_TRANSACTION_KEY
argument_list|)
expr_stmt|;
comment|// must add exchange and message id in constructor
name|MDC
operator|.
name|put
argument_list|(
name|MDC_EXCHANGE_ID
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|msgId
init|=
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|getMessageId
argument_list|()
decl_stmt|;
name|MDC
operator|.
name|put
argument_list|(
name|MDC_MESSAGE_ID
argument_list|,
name|msgId
argument_list|)
expr_stmt|;
comment|// the camel context id is from exchange
name|MDC
operator|.
name|put
argument_list|(
name|MDC_CAMEL_CONTEXT_ID
argument_list|,
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// and add optional correlation id
name|String
name|corrId
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|CORRELATION_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|corrId
operator|!=
literal|null
condition|)
block|{
name|MDC
operator|.
name|put
argument_list|(
name|MDC_CORRELATION_ID
argument_list|,
name|corrId
argument_list|)
expr_stmt|;
block|}
comment|// and add optional breadcrumb id
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
operator|!=
literal|null
condition|)
block|{
name|MDC
operator|.
name|put
argument_list|(
name|MDC_BREADCRUMB_ID
argument_list|,
name|breadcrumbId
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|newInstance (Exchange exchange)
specifier|public
name|UnitOfWork
name|newInstance
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
operator|new
name|MDCUnitOfWork
argument_list|(
name|exchange
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|super
operator|.
name|stop
argument_list|()
expr_stmt|;
comment|// and remove when stopping
name|clear
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|pushRouteContext (RouteContext routeContext)
specifier|public
name|void
name|pushRouteContext
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
name|super
operator|.
name|pushRouteContext
argument_list|(
name|routeContext
argument_list|)
expr_stmt|;
name|MDC
operator|.
name|put
argument_list|(
name|MDC_ROUTE_ID
argument_list|,
name|routeContext
operator|.
name|getRouteId
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|popRouteContext ()
specifier|public
name|RouteContext
name|popRouteContext
parameter_list|()
block|{
name|RouteContext
name|answer
init|=
name|super
operator|.
name|popRouteContext
argument_list|()
decl_stmt|;
comment|// restore old route id back again after we have popped
name|RouteContext
name|previous
init|=
name|getRouteContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|previous
operator|!=
literal|null
condition|)
block|{
comment|// restore old route id back again
name|MDC
operator|.
name|put
argument_list|(
name|MDC_ROUTE_ID
argument_list|,
name|previous
operator|.
name|getRouteId
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// not running in route, so clear (should ideally not happen)
name|MDC
operator|.
name|remove
argument_list|(
name|MDC_ROUTE_ID
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|beginTransactedBy (Object key)
specifier|public
name|void
name|beginTransactedBy
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|MDC
operator|.
name|put
argument_list|(
name|MDC_TRANSACTION_KEY
argument_list|,
name|key
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|super
operator|.
name|beginTransactedBy
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|endTransactedBy (Object key)
specifier|public
name|void
name|endTransactedBy
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|MDC
operator|.
name|remove
argument_list|(
name|MDC_TRANSACTION_KEY
argument_list|)
expr_stmt|;
name|super
operator|.
name|endTransactedBy
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
comment|// add optional step id
name|String
name|stepId
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|STEP_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|stepId
operator|!=
literal|null
condition|)
block|{
name|MDC
operator|.
name|put
argument_list|(
name|MDC_STEP_ID
argument_list|,
name|stepId
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|MDCCallback
argument_list|(
name|callback
argument_list|)
return|;
block|}
annotation|@
name|Override
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
block|{
comment|// if we are no longer under step then remove it
name|String
name|stepId
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|STEP_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|stepId
operator|==
literal|null
condition|)
block|{
name|MDC
operator|.
name|remove
argument_list|(
name|MDC_STEP_ID
argument_list|)
expr_stmt|;
block|}
comment|/*         if (!doneSync) {             // must clear MDC on current thread as the exchange is being processed asynchronously             // by another thread             clear();         }         super.afterProcess(processor, exchange, callback, doneSync);         */
block|}
comment|/**      * Clears information put on the MDC by this {@link MDCUnitOfWork}      */
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|originalBreadcrumbId
operator|!=
literal|null
condition|)
block|{
name|MDC
operator|.
name|put
argument_list|(
name|MDC_BREADCRUMB_ID
argument_list|,
name|originalBreadcrumbId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|MDC
operator|.
name|remove
argument_list|(
name|MDC_BREADCRUMB_ID
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|originalExchangeId
operator|!=
literal|null
condition|)
block|{
name|MDC
operator|.
name|put
argument_list|(
name|MDC_EXCHANGE_ID
argument_list|,
name|originalExchangeId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|MDC
operator|.
name|remove
argument_list|(
name|MDC_EXCHANGE_ID
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|originalMessageId
operator|!=
literal|null
condition|)
block|{
name|MDC
operator|.
name|put
argument_list|(
name|MDC_MESSAGE_ID
argument_list|,
name|originalMessageId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|MDC
operator|.
name|remove
argument_list|(
name|MDC_MESSAGE_ID
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|originalCorrelationId
operator|!=
literal|null
condition|)
block|{
name|MDC
operator|.
name|put
argument_list|(
name|MDC_CORRELATION_ID
argument_list|,
name|originalCorrelationId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|MDC
operator|.
name|remove
argument_list|(
name|MDC_CORRELATION_ID
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|originalRouteId
operator|!=
literal|null
condition|)
block|{
name|MDC
operator|.
name|put
argument_list|(
name|MDC_ROUTE_ID
argument_list|,
name|originalRouteId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|MDC
operator|.
name|remove
argument_list|(
name|MDC_ROUTE_ID
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|originalStepId
operator|!=
literal|null
condition|)
block|{
name|MDC
operator|.
name|put
argument_list|(
name|MDC_STEP_ID
argument_list|,
name|originalStepId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|MDC
operator|.
name|remove
argument_list|(
name|MDC_STEP_ID
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|originalCamelContextId
operator|!=
literal|null
condition|)
block|{
name|MDC
operator|.
name|put
argument_list|(
name|MDC_CAMEL_CONTEXT_ID
argument_list|,
name|originalCamelContextId
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|MDC
operator|.
name|remove
argument_list|(
name|MDC_CAMEL_CONTEXT_ID
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|originalTransactionKey
operator|!=
literal|null
condition|)
block|{
name|MDC
operator|.
name|put
argument_list|(
name|MDC_TRANSACTION_KEY
argument_list|,
name|originalTransactionKey
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|MDC
operator|.
name|remove
argument_list|(
name|MDC_TRANSACTION_KEY
argument_list|)
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
literal|"MDCUnitOfWork"
return|;
block|}
comment|/**      * {@link AsyncCallback} which preserves {@link org.slf4j.MDC} when      * the asynchronous routing engine is being used.      */
DECL|class|MDCCallback
specifier|private
specifier|static
specifier|final
class|class
name|MDCCallback
implements|implements
name|AsyncCallback
block|{
DECL|field|delegate
specifier|private
specifier|final
name|AsyncCallback
name|delegate
decl_stmt|;
DECL|field|breadcrumbId
specifier|private
specifier|final
name|String
name|breadcrumbId
decl_stmt|;
DECL|field|exchangeId
specifier|private
specifier|final
name|String
name|exchangeId
decl_stmt|;
DECL|field|messageId
specifier|private
specifier|final
name|String
name|messageId
decl_stmt|;
DECL|field|correlationId
specifier|private
specifier|final
name|String
name|correlationId
decl_stmt|;
DECL|field|routeId
specifier|private
specifier|final
name|String
name|routeId
decl_stmt|;
DECL|field|stepId
specifier|private
specifier|final
name|String
name|stepId
decl_stmt|;
DECL|field|camelContextId
specifier|private
specifier|final
name|String
name|camelContextId
decl_stmt|;
DECL|method|MDCCallback (AsyncCallback delegate)
specifier|private
name|MDCCallback
parameter_list|(
name|AsyncCallback
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
name|this
operator|.
name|exchangeId
operator|=
name|MDC
operator|.
name|get
argument_list|(
name|MDC_EXCHANGE_ID
argument_list|)
expr_stmt|;
name|this
operator|.
name|messageId
operator|=
name|MDC
operator|.
name|get
argument_list|(
name|MDC_MESSAGE_ID
argument_list|)
expr_stmt|;
name|this
operator|.
name|breadcrumbId
operator|=
name|MDC
operator|.
name|get
argument_list|(
name|MDC_BREADCRUMB_ID
argument_list|)
expr_stmt|;
name|this
operator|.
name|correlationId
operator|=
name|MDC
operator|.
name|get
argument_list|(
name|MDC_CORRELATION_ID
argument_list|)
expr_stmt|;
name|this
operator|.
name|camelContextId
operator|=
name|MDC
operator|.
name|get
argument_list|(
name|MDC_CAMEL_CONTEXT_ID
argument_list|)
expr_stmt|;
name|this
operator|.
name|routeId
operator|=
name|MDC
operator|.
name|get
argument_list|(
name|MDC_ROUTE_ID
argument_list|)
expr_stmt|;
name|this
operator|.
name|stepId
operator|=
name|MDC
operator|.
name|get
argument_list|(
name|MDC_STEP_ID
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|done (boolean doneSync)
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
operator|!
name|doneSync
condition|)
block|{
comment|// when done asynchronously then restore information from previous thread
if|if
condition|(
name|breadcrumbId
operator|!=
literal|null
condition|)
block|{
name|MDC
operator|.
name|put
argument_list|(
name|MDC_BREADCRUMB_ID
argument_list|,
name|breadcrumbId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exchangeId
operator|!=
literal|null
condition|)
block|{
name|MDC
operator|.
name|put
argument_list|(
name|MDC_EXCHANGE_ID
argument_list|,
name|exchangeId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|messageId
operator|!=
literal|null
condition|)
block|{
name|MDC
operator|.
name|put
argument_list|(
name|MDC_MESSAGE_ID
argument_list|,
name|messageId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|correlationId
operator|!=
literal|null
condition|)
block|{
name|MDC
operator|.
name|put
argument_list|(
name|MDC_CORRELATION_ID
argument_list|,
name|correlationId
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|camelContextId
operator|!=
literal|null
condition|)
block|{
name|MDC
operator|.
name|put
argument_list|(
name|MDC_CAMEL_CONTEXT_ID
argument_list|,
name|camelContextId
argument_list|)
expr_stmt|;
block|}
block|}
comment|// need to setup the routeId finally
if|if
condition|(
name|routeId
operator|!=
literal|null
condition|)
block|{
name|MDC
operator|.
name|put
argument_list|(
name|MDC_ROUTE_ID
argument_list|,
name|routeId
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
comment|// muse ensure delegate is invoked
name|delegate
operator|.
name|done
argument_list|(
name|doneSync
argument_list|)
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
name|delegate
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

