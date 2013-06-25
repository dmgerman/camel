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
name|jboss
operator|.
name|netty
operator|.
name|channel
operator|.
name|ChannelHandler
import|;
end_import

begin_comment
comment|/**  * Factory for setting up Netty {@link ChannelHandler} bound to a given Netty port.  *<p/>  * This factory allows for consumers to reuse existing {@link org.jboss.netty.bootstrap.ServerBootstrap} which  * allows to share the same port for multiple consumers.  *  * This factory is needed to ensure we can handle the situations when consumers is added and removing in  * a dynamic environment such as OSGi, where Camel applications can be hot-deployed. And we want these  * Camel applications to be able to share the same Netty port in a easy way.  */
end_comment

begin_interface
DECL|interface|HttpServerConsumerChannelFactory
specifier|public
interface|interface
name|HttpServerConsumerChannelFactory
block|{
comment|/**      * Initializes this consumer channel factory with the given port.      */
DECL|method|init (int port)
name|void
name|init
parameter_list|(
name|int
name|port
parameter_list|)
function_decl|;
comment|/**      * The port number this consumer channel factory is using.      */
DECL|method|getPort ()
name|int
name|getPort
parameter_list|()
function_decl|;
comment|/**      * Adds the given consumer.      */
DECL|method|addConsumer (NettyHttpConsumer consumer)
name|void
name|addConsumer
parameter_list|(
name|NettyHttpConsumer
name|consumer
parameter_list|)
function_decl|;
comment|/**      * Removes the given consumer      */
DECL|method|removeConsumer (NettyHttpConsumer consumer)
name|void
name|removeConsumer
parameter_list|(
name|NettyHttpConsumer
name|consumer
parameter_list|)
function_decl|;
comment|/**      * Number of active consumers      */
DECL|method|consumers ()
name|int
name|consumers
parameter_list|()
function_decl|;
comment|/**      * Gets the {@link ChannelHandler}      */
DECL|method|getChannelHandler ()
name|ChannelHandler
name|getChannelHandler
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

