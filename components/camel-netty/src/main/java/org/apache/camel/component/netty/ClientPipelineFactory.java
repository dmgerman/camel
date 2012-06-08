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
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|channel
operator|.
name|ChannelPipeline
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
comment|/**  * Factory to create {@link ChannelPipeline} for clients, eg {@link NettyProducer}.  *<p/>  * Implementators must support creating a new instance of this factory which is associated  * to the given {@link NettyProducer} using the {@link #createPipelineFactory(NettyProducer)}  * method.  *  * @see ChannelPipelineFactory  */
end_comment

begin_class
DECL|class|ClientPipelineFactory
specifier|public
specifier|abstract
class|class
name|ClientPipelineFactory
implements|implements
name|ChannelPipelineFactory
block|{
comment|/**      * Creates a new {@link ClientPipelineFactory} using the given {@link NettyProducer}      *      * @param producer the associated producers      * @return the {@link ClientPipelineFactory} associated to ghe given producer.      */
DECL|method|createPipelineFactory (NettyProducer producer)
specifier|public
specifier|abstract
name|ClientPipelineFactory
name|createPipelineFactory
parameter_list|(
name|NettyProducer
name|producer
parameter_list|)
function_decl|;
block|}
end_class

end_unit

