begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty
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
name|ThreadFactory
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
name|CamelContext
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
name|SuspendableService
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|channel
operator|.
name|Channel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|channel
operator|.
name|ChannelPipelineFactory
import|;
end_import

begin_comment
comment|/**  * Factory for setting up Netty {@link org.jboss.netty.bootstrap.ServerBootstrap} and all  * the needed logic for doing that.  *<p/>  * This factory allows for consumers to reuse existing {@link org.jboss.netty.bootstrap.ServerBootstrap} which  * allows to share the same port for multiple consumers.  */
end_comment

begin_interface
DECL|interface|NettyServerBootstrapFactory
specifier|public
interface|interface
name|NettyServerBootstrapFactory
extends|extends
name|SuspendableService
block|{
comment|/**      * Initializes this<b>non-shared</b> {@link NettyServerBootstrapFactory}.      *      * @param camelContext     the {@link CamelContext} for non-shared bootstrap factory      * @param configuration    the bootstrap configuration      * @param pipelineFactory  the pipeline factory      */
DECL|method|init (CamelContext camelContext, NettyServerBootstrapConfiguration configuration, ChannelPipelineFactory pipelineFactory)
name|void
name|init
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|NettyServerBootstrapConfiguration
name|configuration
parameter_list|,
name|ChannelPipelineFactory
name|pipelineFactory
parameter_list|)
function_decl|;
comment|/**      * Initializes this<b>shared</b> {@link NettyServerBootstrapFactory}.      *      * @param threadFactory    the thread factory to use for shared bootstrap factory      * @param configuration    the bootstrap configuration      * @param pipelineFactory  the pipeline factory      */
DECL|method|init (ThreadFactory threadFactory, NettyServerBootstrapConfiguration configuration, ChannelPipelineFactory pipelineFactory)
name|void
name|init
parameter_list|(
name|ThreadFactory
name|threadFactory
parameter_list|,
name|NettyServerBootstrapConfiguration
name|configuration
parameter_list|,
name|ChannelPipelineFactory
name|pipelineFactory
parameter_list|)
function_decl|;
comment|/**      * When a new {@link Channel} is opened.      */
DECL|method|addChannel (Channel channel)
name|void
name|addChannel
parameter_list|(
name|Channel
name|channel
parameter_list|)
function_decl|;
comment|/**      * When a {@link Channel} is closed.      */
DECL|method|removeChannel (Channel channel)
name|void
name|removeChannel
parameter_list|(
name|Channel
name|channel
parameter_list|)
function_decl|;
comment|/**      * When a {@link NettyConsumer} is added and uses this bootstrap factory.      */
DECL|method|addConsumer (NettyConsumer consumer)
name|void
name|addConsumer
parameter_list|(
name|NettyConsumer
name|consumer
parameter_list|)
function_decl|;
comment|/**      * When a {@link NettyConsumer} is removed and no longer using this bootstrap factory.      */
DECL|method|removeConsumer (NettyConsumer consumer)
name|void
name|removeConsumer
parameter_list|(
name|NettyConsumer
name|consumer
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

