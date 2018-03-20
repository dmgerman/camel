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
name|CamelExchangeException
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

begin_comment
comment|/**  * Saga processor implementing the MANDATORY propagation mode.  */
end_comment

begin_class
DECL|class|MandatorySagaProcessor
specifier|public
class|class
name|MandatorySagaProcessor
extends|extends
name|SagaProcessor
block|{
DECL|method|MandatorySagaProcessor (CamelContext camelContext, Processor childProcessor, CamelSagaService sagaService, SagaCompletionMode completionMode, CamelSagaStep step)
specifier|public
name|MandatorySagaProcessor
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
name|camelContext
argument_list|,
name|childProcessor
argument_list|,
name|sagaService
argument_list|,
name|completionMode
argument_list|,
name|step
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|getCurrentSagaCoordinator
argument_list|(
name|exchange
argument_list|)
operator|.
name|whenComplete
argument_list|(
parameter_list|(
name|coordinator
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
block|if (coordinator == null
argument_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|CamelExchangeException
argument_list|(
literal|"Exchange is not part of a saga"
argument_list|,
name|exchange
argument_list|)
argument_list|)
block|;
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
block|;             }
else|else
block|{
name|coordinator
operator|.
name|beginStep
argument_list|(
name|exchange
argument_list|,
name|step
argument_list|)
operator|.
name|whenComplete
argument_list|(
parameter_list|(
name|done
parameter_list|,
name|ex2
parameter_list|)
lambda|->
name|ifNotException
argument_list|(
name|ex2
argument_list|,
name|exchange
argument_list|,
name|callback
argument_list|,
parameter_list|()
lambda|->
block|{
name|super
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|doneSync
lambda|->
name|callback
operator|.
name|done
argument_list|(
literal|false
argument_list|)
argument_list|)
argument_list|;
block|}
block|)
block|)
class|;
end_class

begin_empty_stmt
unit|}         }))
empty_stmt|;
end_empty_stmt

begin_return
return|return
literal|false
return|;
end_return

unit|} }
end_unit

