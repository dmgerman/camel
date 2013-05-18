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
name|Exchange
import|;
end_import

begin_comment
comment|/**  * A task to execute cross cutting functionality in the Camel routing engine.  *<p/>  * The Camel routing engine will execute the {@link #before(org.apache.camel.Exchange)} and  * {@link #after(org.apache.camel.Exchange, Object)} methods during routing in correct order.  *  * @param<T>  * @see CamelInternalProcessor  */
end_comment

begin_interface
DECL|interface|CamelInternalProcessorTask
specifier|public
interface|interface
name|CamelInternalProcessorTask
parameter_list|<
name|T
parameter_list|>
block|{
comment|/**      * Callback executed before processing a step in the route.      *      * @param exchange  the current exchange      * @return any state to keep and provide as data to the {@link #after(org.apache.camel.Exchange, Object)} method, or use<tt>null</tt> for no state.      * @throws Exception is thrown if error during the call.      */
DECL|method|before (Exchange exchange)
name|T
name|before
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Callback executed after processing a step in the route.      *      * @param exchange  the current exchange      * @param data      the state, if any, returned in the {@link #before(org.apache.camel.Exchange)} method.      * @throws Exception is thrown if error during the call.      */
DECL|method|after (Exchange exchange, T data)
name|void
name|after
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|T
name|data
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

