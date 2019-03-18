begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor.loadbalancer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
operator|.
name|loadbalancer
package|;
end_package

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
name|Processor
import|;
end_import

begin_comment
comment|/**  * A strategy for load balancing across a number of {@link Processor} instances  */
end_comment

begin_interface
DECL|interface|LoadBalancer
specifier|public
interface|interface
name|LoadBalancer
extends|extends
name|AsyncProcessor
block|{
comment|/**      * Adds a new processor to the load balancer      *      * @param processor the processor to be added to the load balancer      */
DECL|method|addProcessor (AsyncProcessor processor)
name|void
name|addProcessor
parameter_list|(
name|AsyncProcessor
name|processor
parameter_list|)
function_decl|;
comment|/**      * Removes the given processor from the load balancer      *      * @param processor the processor to be removed from the load balancer      */
DECL|method|removeProcessor (AsyncProcessor processor)
name|void
name|removeProcessor
parameter_list|(
name|AsyncProcessor
name|processor
parameter_list|)
function_decl|;
comment|/**      * Returns the current processors available to this load balancer      *      * @return the processors available      */
DECL|method|getProcessors ()
name|List
argument_list|<
name|AsyncProcessor
argument_list|>
name|getProcessors
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

