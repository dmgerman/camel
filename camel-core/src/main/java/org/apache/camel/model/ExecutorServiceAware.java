begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
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
name|ExecutorService
import|;
end_import

begin_comment
comment|/**  * Enables definitions to support concurrency using {@link java.util.concurrent.ExecutorService}  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|ExecutorServiceAware
specifier|public
interface|interface
name|ExecutorServiceAware
parameter_list|<
name|Type
extends|extends
name|ProcessorDefinition
parameter_list|>
block|{
comment|/**      * Setting the executor service for executing      *      * @param executorService the executor service      * @return the builder      */
DECL|method|executorService (ExecutorService executorService)
parameter_list|<
name|Type
parameter_list|>
name|Type
name|executorService
parameter_list|(
name|ExecutorService
name|executorService
parameter_list|)
function_decl|;
comment|/**      * Setting the executor service for executing      *      * @param executorServiceRef reference for a {@link java.util.concurrent.ExecutorService}      *                           to lookup in the {@link org.apache.camel.spi.Registry}      * @return the builder      */
DECL|method|executorServiceRef (String executorServiceRef)
parameter_list|<
name|Type
parameter_list|>
name|Type
name|executorServiceRef
parameter_list|(
name|String
name|executorServiceRef
parameter_list|)
function_decl|;
DECL|method|getExecutorService ()
name|ExecutorService
name|getExecutorService
parameter_list|()
function_decl|;
DECL|method|setExecutorService (ExecutorService executorService)
name|void
name|setExecutorService
parameter_list|(
name|ExecutorService
name|executorService
parameter_list|)
function_decl|;
DECL|method|getExecutorServiceRef ()
name|String
name|getExecutorServiceRef
parameter_list|()
function_decl|;
DECL|method|setExecutorServiceRef (String executorServiceRef)
name|void
name|setExecutorServiceRef
parameter_list|(
name|String
name|executorServiceRef
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

