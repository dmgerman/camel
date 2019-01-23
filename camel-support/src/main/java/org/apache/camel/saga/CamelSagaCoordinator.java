begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|spi
operator|.
name|HasId
import|;
end_import

begin_comment
comment|/**  * A saga coordinator can be used to register compensator's and  * take the final decision on the saga: compensate or complete (successfully).  */
end_comment

begin_interface
DECL|interface|CamelSagaCoordinator
specifier|public
interface|interface
name|CamelSagaCoordinator
extends|extends
name|HasId
block|{
DECL|method|beginStep (Exchange exchange, CamelSagaStep step)
name|CompletableFuture
argument_list|<
name|Void
argument_list|>
name|beginStep
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|CamelSagaStep
name|step
parameter_list|)
function_decl|;
DECL|method|compensate ()
name|CompletableFuture
argument_list|<
name|Void
argument_list|>
name|compensate
parameter_list|()
function_decl|;
DECL|method|complete ()
name|CompletableFuture
argument_list|<
name|Void
argument_list|>
name|complete
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

