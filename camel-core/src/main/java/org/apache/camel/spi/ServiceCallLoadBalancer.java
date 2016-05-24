begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_comment
comment|/**  * Allows SPIs to implement custom load balancing strategies for the Service Call EIP.  *  * @see ServiceCallServerListStrategy  */
end_comment

begin_interface
DECL|interface|ServiceCallLoadBalancer
specifier|public
interface|interface
name|ServiceCallLoadBalancer
parameter_list|<
name|T
extends|extends
name|ServiceCallServer
parameter_list|>
block|{
comment|/**      * Chooses one of the servers to use using the implemented strategy.      *      * @param servers  list of servers      * @return the chosen server to use.      */
DECL|method|chooseServer (Collection<T> servers)
name|T
name|chooseServer
parameter_list|(
name|Collection
argument_list|<
name|T
argument_list|>
name|servers
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

