begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.coap
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|coap
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|GeneralSecurityException
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
name|rest
operator|.
name|RestConfigurationDefinition
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|californium
operator|.
name|core
operator|.
name|CoapClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|californium
operator|.
name|core
operator|.
name|network
operator|.
name|CoapEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|californium
operator|.
name|core
operator|.
name|network
operator|.
name|config
operator|.
name|NetworkConfig
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|californium
operator|.
name|elements
operator|.
name|tcp
operator|.
name|TcpClientConnector
import|;
end_import

begin_comment
comment|/**  * Test the CoAP Rest Component with plain TCP.  */
end_comment

begin_class
DECL|class|CoAPRestComponentTCPTest
specifier|public
class|class
name|CoAPRestComponentTCPTest
extends|extends
name|CoAPRestComponentTestBase
block|{
annotation|@
name|Override
DECL|method|getProtocol ()
specifier|protected
name|String
name|getProtocol
parameter_list|()
block|{
return|return
literal|"coap+tcp"
return|;
block|}
annotation|@
name|Override
DECL|method|decorateClient (CoapClient client)
specifier|protected
name|void
name|decorateClient
parameter_list|(
name|CoapClient
name|client
parameter_list|)
throws|throws
name|GeneralSecurityException
throws|,
name|IOException
block|{
name|NetworkConfig
name|config
init|=
name|NetworkConfig
operator|.
name|createStandardWithoutFile
argument_list|()
decl_stmt|;
name|int
name|tcpThreads
init|=
name|config
operator|.
name|getInt
argument_list|(
name|NetworkConfig
operator|.
name|Keys
operator|.
name|TCP_WORKER_THREADS
argument_list|)
decl_stmt|;
name|int
name|tcpConnectTimeout
init|=
name|config
operator|.
name|getInt
argument_list|(
name|NetworkConfig
operator|.
name|Keys
operator|.
name|TCP_CONNECT_TIMEOUT
argument_list|)
decl_stmt|;
name|int
name|tcpIdleTimeout
init|=
name|config
operator|.
name|getInt
argument_list|(
name|NetworkConfig
operator|.
name|Keys
operator|.
name|TCP_CONNECTION_IDLE_TIMEOUT
argument_list|)
decl_stmt|;
name|TcpClientConnector
name|tcpConnector
init|=
operator|new
name|TcpClientConnector
argument_list|(
name|tcpThreads
argument_list|,
name|tcpConnectTimeout
argument_list|,
name|tcpIdleTimeout
argument_list|)
decl_stmt|;
name|CoapEndpoint
operator|.
name|Builder
name|tcpBuilder
init|=
operator|new
name|CoapEndpoint
operator|.
name|Builder
argument_list|()
decl_stmt|;
name|tcpBuilder
operator|.
name|setConnector
argument_list|(
name|tcpConnector
argument_list|)
expr_stmt|;
name|client
operator|.
name|setEndpoint
argument_list|(
name|tcpBuilder
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|decorateRestConfiguration (RestConfigurationDefinition restConfig)
specifier|protected
name|void
name|decorateRestConfiguration
parameter_list|(
name|RestConfigurationDefinition
name|restConfig
parameter_list|)
block|{
comment|// Nothing here
block|}
block|}
end_class

end_unit

