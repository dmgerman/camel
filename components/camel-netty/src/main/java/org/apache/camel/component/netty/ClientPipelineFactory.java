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
name|Channels
import|;
end_import

begin_class
DECL|class|ClientPipelineFactory
specifier|public
specifier|abstract
class|class
name|ClientPipelineFactory
implements|implements
name|ChannelPipelineFactory
block|{
DECL|field|producer
specifier|protected
name|NettyProducer
name|producer
decl_stmt|;
DECL|method|ClientPipelineFactory ()
specifier|public
name|ClientPipelineFactory
parameter_list|()
block|{     }
DECL|method|ClientPipelineFactory (NettyProducer producer)
specifier|public
name|ClientPipelineFactory
parameter_list|(
name|NettyProducer
name|producer
parameter_list|)
block|{
name|this
operator|.
name|producer
operator|=
name|producer
expr_stmt|;
block|}
DECL|method|getPipeline ()
specifier|public
name|ChannelPipeline
name|getPipeline
parameter_list|()
throws|throws
name|Exception
block|{
name|ChannelPipeline
name|channelPipeline
init|=
name|Channels
operator|.
name|pipeline
argument_list|()
decl_stmt|;
return|return
name|channelPipeline
return|;
block|}
DECL|method|getProducer ()
specifier|public
name|NettyProducer
name|getProducer
parameter_list|()
block|{
return|return
name|producer
return|;
block|}
DECL|method|setProducer (NettyProducer producer)
specifier|public
name|void
name|setProducer
parameter_list|(
name|NettyProducer
name|producer
parameter_list|)
block|{
name|this
operator|.
name|producer
operator|=
name|producer
expr_stmt|;
block|}
block|}
end_class

end_unit

