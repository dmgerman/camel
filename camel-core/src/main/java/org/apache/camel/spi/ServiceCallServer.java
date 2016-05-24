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

begin_comment
comment|/**  * Represents a server that host a service for the Service Call EIP.  *  * @see ServiceCallLoadBalancer  * @see ServiceCallServerListStrategy  */
end_comment

begin_interface
DECL|interface|ServiceCallServer
specifier|public
interface|interface
name|ServiceCallServer
block|{
comment|/**      * Gets the IP or hostname of the server hosting the service      */
DECL|method|getIp ()
name|String
name|getIp
parameter_list|()
function_decl|;
comment|/**      * Gets the port number of the server hosting the service      */
DECL|method|getPort ()
name|int
name|getPort
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

