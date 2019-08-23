begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|endpoint
operator|.
name|Client
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|endpoint
operator|.
name|Server
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|frontend
operator|.
name|AbstractWSDLBasedEndpointFactory
import|;
end_import

begin_comment
comment|/**  * A pluggable strategy for configuring the CXF by using java code  */
end_comment

begin_interface
DECL|interface|CxfConfigurer
specifier|public
interface|interface
name|CxfConfigurer
block|{
comment|/**      * Configure the CXF Server/Client factory bean      *      * @param factoryBean the factory bean      */
DECL|method|configure (AbstractWSDLBasedEndpointFactory factoryBean)
name|void
name|configure
parameter_list|(
name|AbstractWSDLBasedEndpointFactory
name|factoryBean
parameter_list|)
function_decl|;
comment|/**      * Configure the CXF Client such as setting some parameters on the client conduit       *      * @param client the CXF client      */
DECL|method|configureClient (Client client)
name|void
name|configureClient
parameter_list|(
name|Client
name|client
parameter_list|)
function_decl|;
comment|/**      * Configure the CXF Server such as setting some parameters on the server destination       *      * @param server the CXF server      */
DECL|method|configureServer (Server server)
name|void
name|configureServer
parameter_list|(
name|Server
name|server
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

