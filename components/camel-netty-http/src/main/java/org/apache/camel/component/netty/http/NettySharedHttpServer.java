begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty
operator|.
name|http
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
name|Service
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
name|component
operator|.
name|netty
operator|.
name|NettyServerBootstrapConfiguration
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
name|component
operator|.
name|netty
operator|.
name|NettyServerBootstrapFactory
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
name|ClassResolver
import|;
end_import

begin_comment
comment|/**  * A single interface to easily configure and setup a shared Netty HTTP server  * to be re-used among other Camel applications.  *<p/>  * To use this, just define a {@link NettyServerBootstrapConfiguration} configuration, and  * set this using {@link #setNettyServerBootstrapConfiguration(NettySharedHttpServerBootstrapConfiguration)}.  * Then call the {@link #start()} to initialize this shared server.  */
end_comment

begin_interface
DECL|interface|NettySharedHttpServer
specifier|public
interface|interface
name|NettySharedHttpServer
extends|extends
name|Service
block|{
comment|/**      * Sets the bootstrap configuration to use by this shared Netty HTTP server.      */
DECL|method|setNettyServerBootstrapConfiguration (NettySharedHttpServerBootstrapConfiguration configuration)
name|void
name|setNettyServerBootstrapConfiguration
parameter_list|(
name|NettySharedHttpServerBootstrapConfiguration
name|configuration
parameter_list|)
function_decl|;
comment|/**      * To use a custom {@link ClassResolver} for loading resource on the classpath.      */
DECL|method|setClassResolver (ClassResolver classResolver)
name|void
name|setClassResolver
parameter_list|(
name|ClassResolver
name|classResolver
parameter_list|)
function_decl|;
comment|/**      * Whether to start the Netty HTTP server eager and bind to the port, or wait on first demand      */
DECL|method|setStartServer (boolean startServer)
name|void
name|setStartServer
parameter_list|(
name|boolean
name|startServer
parameter_list|)
function_decl|;
comment|/**      * Sets a custom thread name pattern to be used for naming the Netty HTTP server threads.      */
DECL|method|setThreadNamePattern (String pattern)
name|void
name|setThreadNamePattern
parameter_list|(
name|String
name|pattern
parameter_list|)
function_decl|;
comment|/**      * Gets the port number this Netty HTTP server uses.      */
DECL|method|getPort ()
name|int
name|getPort
parameter_list|()
function_decl|;
comment|/**      * Gets the {@link HttpServerConsumerChannelFactory} to use.      */
DECL|method|getConsumerChannelFactory ()
name|HttpServerConsumerChannelFactory
name|getConsumerChannelFactory
parameter_list|()
function_decl|;
comment|/**      * Gets the {@link NettyServerBootstrapFactory} to use.      */
DECL|method|getServerBootstrapFactory ()
name|NettyServerBootstrapFactory
name|getServerBootstrapFactory
parameter_list|()
function_decl|;
comment|/**      * Number of consumers using this shared Netty HTTP server.      */
DECL|method|getConsumersSize ()
name|int
name|getConsumersSize
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

