begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.saga
package|package
name|org
operator|.
name|apache
operator|.
name|camel
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
name|CamelContextAware
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
comment|/**  * A Camel saga service is a factory of saga coordinators.  */
end_comment

begin_interface
DECL|interface|CamelSagaService
specifier|public
interface|interface
name|CamelSagaService
extends|extends
name|Service
extends|,
name|CamelContextAware
block|{
DECL|method|newSaga ()
name|CompletableFuture
argument_list|<
name|CamelSagaCoordinator
argument_list|>
name|newSaga
parameter_list|()
function_decl|;
DECL|method|getSaga (String id)
name|CompletableFuture
argument_list|<
name|CamelSagaCoordinator
argument_list|>
name|getSaga
parameter_list|(
name|String
name|id
parameter_list|)
function_decl|;
DECL|method|registerStep (CamelSagaStep step)
name|void
name|registerStep
parameter_list|(
name|CamelSagaStep
name|step
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

