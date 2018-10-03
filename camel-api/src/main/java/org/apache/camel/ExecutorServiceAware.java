begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
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
comment|/**  * Is used for easy configuration of {@link ExecutorService}.  */
end_comment

begin_interface
DECL|interface|ExecutorServiceAware
specifier|public
interface|interface
name|ExecutorServiceAware
block|{
comment|/**      * Gets the executor service      *      * @return the executor      */
DECL|method|getExecutorService ()
name|ExecutorService
name|getExecutorService
parameter_list|()
function_decl|;
comment|/**      * Sets the executor service to be used.      *      * @param executorService the executor      */
DECL|method|setExecutorService (ExecutorService executorService)
name|void
name|setExecutorService
parameter_list|(
name|ExecutorService
name|executorService
parameter_list|)
function_decl|;
comment|/**      * Gets the reference to lookup in the {@link org.apache.camel.spi.Registry} for the executor service to be used.      *      * @return the reference, or<tt>null</tt> if the executor was set directly      */
DECL|method|getExecutorServiceRef ()
name|String
name|getExecutorServiceRef
parameter_list|()
function_decl|;
comment|/**      * Sets a reference to lookup in the {@link org.apache.camel.spi.Registry} for the executor service to be used.      *      * @param executorServiceRef reference for the executor      */
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

