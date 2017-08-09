begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.saga
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|saga
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CompletableFuture
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
name|model
operator|.
name|SagaCompletionMode
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
name|processor
operator|.
name|DelegateAsyncProcessor
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
name|saga
operator|.
name|CamelSagaCoordinator
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
name|saga
operator|.
name|CamelSagaService
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
name|saga
operator|.
name|CamelSagaStep
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
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * Processor for handling sagas.  */
end_comment

begin_class
DECL|class|SagaProcessor
specifier|public
specifier|abstract
class|class
name|SagaProcessor
extends|extends
name|DelegateAsyncProcessor
block|{
DECL|field|camelContext
specifier|protected
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|sagaService
specifier|protected
name|CamelSagaService
name|sagaService
decl_stmt|;
DECL|field|step
specifier|protected
name|CamelSagaStep
name|step
decl_stmt|;
DECL|field|completionMode
specifier|protected
name|SagaCompletionMode
name|completionMode
decl_stmt|;
DECL|method|SagaProcessor (CamelContext camelContext, Processor childProcessor, CamelSagaService sagaService, SagaCompletionMode completionMode, CamelSagaStep step)
specifier|public
name|SagaProcessor
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|Processor
name|childProcessor
parameter_list|,
name|CamelSagaService
name|sagaService
parameter_list|,
name|SagaCompletionMode
name|completionMode
parameter_list|,
name|CamelSagaStep
name|step
parameter_list|)
block|{
name|super
argument_list|(
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|childProcessor
argument_list|,
literal|"childProcessor"
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|camelContext
operator|=
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"camelContext"
argument_list|)
expr_stmt|;
name|this
operator|.
name|sagaService
operator|=
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|sagaService
argument_list|,
literal|"sagaService"
argument_list|)
expr_stmt|;
name|this
operator|.
name|completionMode
operator|=
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|completionMode
argument_list|,
literal|"completionMode"
argument_list|)
expr_stmt|;
name|this
operator|.
name|step
operator|=
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|step
argument_list|,
literal|"step"
argument_list|)
expr_stmt|;
block|}
DECL|method|getCurrentSagaCoordinator (Exchange exchange)
specifier|protected
name|CompletableFuture
argument_list|<
name|CamelSagaCoordinator
argument_list|>
name|getCurrentSagaCoordinator
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|currentSaga
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
name|SAGA_LONG_RUNNING_ACTION
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|currentSaga
operator|!=
literal|null
condition|)
block|{
return|return
name|sagaService
operator|.
name|getSaga
argument_list|(
name|currentSaga
argument_list|)
return|;
block|}
return|return
name|CompletableFuture
operator|.
name|completedFuture
argument_list|(
literal|null
argument_list|)
return|;
block|}
DECL|method|setCurrentSagaCoordinator (Exchange exchange, CamelSagaCoordinator coordinator)
specifier|protected
name|void
name|setCurrentSagaCoordinator
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|CamelSagaCoordinator
name|coordinator
parameter_list|)
block|{
if|if
condition|(
name|coordinator
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Exchange
operator|.
name|SAGA_LONG_RUNNING_ACTION
argument_list|,
name|coordinator
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|removeHeader
argument_list|(
name|Exchange
operator|.
name|SAGA_LONG_RUNNING_ACTION
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|handleSagaCompletion (Exchange exchange, CamelSagaCoordinator coordinator, CamelSagaCoordinator previousCoordinator, AsyncCallback callback)
specifier|protected
name|void
name|handleSagaCompletion
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|CamelSagaCoordinator
name|coordinator
parameter_list|,
name|CamelSagaCoordinator
name|previousCoordinator
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|completionMode
operator|==
name|SagaCompletionMode
operator|.
name|AUTO
condition|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|coordinator
operator|.
name|compensate
argument_list|()
operator|.
name|whenComplete
argument_list|(
parameter_list|(
name|done
parameter_list|,
name|ex
parameter_list|)
lambda|->
name|ifNotException
argument_list|(
name|ex
argument_list|,
name|exchange
argument_list|,
name|callback
argument_list|,
parameter_list|()
lambda|->
block|{
name|setCurrentSagaCoordinator
argument_list|(
name|exchange
argument_list|,
name|previousCoordinator
argument_list|)
argument_list|;
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|)
block|)
function|;
block|}
end_class

begin_else
else|else
block|{
name|coordinator
operator|.
name|complete
argument_list|()
operator|.
name|whenComplete
argument_list|(
parameter_list|(
name|done
parameter_list|,
name|ex
parameter_list|)
lambda|->
name|ifNotException
argument_list|(
name|ex
argument_list|,
name|exchange
argument_list|,
name|callback
argument_list|,
parameter_list|()
lambda|->
block|{
name|setCurrentSagaCoordinator
argument_list|(
name|exchange
argument_list|,
name|previousCoordinator
argument_list|)
argument_list|;
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
argument_list|;
block|}
end_else

begin_empty_stmt
unit|))
empty_stmt|;
end_empty_stmt

begin_elseif
unit|}         }
elseif|else
if|if
condition|(
name|this
operator|.
name|completionMode
operator|==
name|SagaCompletionMode
operator|.
name|MANUAL
condition|)
block|{
comment|// Completion will be handled manually by the user
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
end_elseif

begin_else
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Unsupported completion mode: "
operator|+
name|this
operator|.
name|completionMode
argument_list|)
throw|;
block|}
end_else

begin_function
unit|}      public
DECL|method|getSagaService ()
name|CamelSagaService
name|getSagaService
parameter_list|()
block|{
return|return
name|sagaService
return|;
block|}
end_function

begin_function
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"saga"
return|;
block|}
end_function

begin_function
DECL|method|ifNotException (Throwable ex, Exchange exchange, AsyncCallback callback, Runnable code)
specifier|protected
name|void
name|ifNotException
parameter_list|(
name|Throwable
name|ex
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|,
name|Runnable
name|code
parameter_list|)
block|{
if|if
condition|(
name|ex
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|ex
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|code
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
end_function

unit|}
end_unit

