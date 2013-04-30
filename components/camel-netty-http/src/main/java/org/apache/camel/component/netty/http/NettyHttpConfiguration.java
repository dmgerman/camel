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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|RuntimeCamelException
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
name|NettyConfiguration
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
name|ChannelHandler
import|;
end_import

begin_comment
comment|/**  * Extended configuration for using HTTP with Netty.  */
end_comment

begin_class
DECL|class|NettyHttpConfiguration
specifier|public
class|class
name|NettyHttpConfiguration
extends|extends
name|NettyConfiguration
block|{
DECL|field|chunked
specifier|private
name|boolean
name|chunked
init|=
literal|true
decl_stmt|;
DECL|field|compression
specifier|private
name|boolean
name|compression
decl_stmt|;
DECL|method|NettyHttpConfiguration ()
specifier|public
name|NettyHttpConfiguration
parameter_list|()
block|{
comment|// we need sync=true as http is request/reply by nature
name|setSync
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setReuseAddress
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|setServerPipelineFactory
argument_list|(
operator|new
name|HttpServerPipelineFactory
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|copy ()
specifier|public
name|NettyHttpConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
comment|// clone as NettyHttpConfiguration
name|NettyHttpConfiguration
name|answer
init|=
operator|(
name|NettyHttpConfiguration
operator|)
name|clone
argument_list|()
decl_stmt|;
comment|// make sure the lists is copied in its own instance
name|List
argument_list|<
name|ChannelHandler
argument_list|>
name|encodersCopy
init|=
operator|new
name|ArrayList
argument_list|<
name|ChannelHandler
argument_list|>
argument_list|(
name|getEncoders
argument_list|()
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setEncoders
argument_list|(
name|encodersCopy
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ChannelHandler
argument_list|>
name|decodersCopy
init|=
operator|new
name|ArrayList
argument_list|<
name|ChannelHandler
argument_list|>
argument_list|(
name|getDecoders
argument_list|()
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setDecoders
argument_list|(
name|decodersCopy
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|isChunked ()
specifier|public
name|boolean
name|isChunked
parameter_list|()
block|{
return|return
name|chunked
return|;
block|}
DECL|method|setChunked (boolean chunked)
specifier|public
name|void
name|setChunked
parameter_list|(
name|boolean
name|chunked
parameter_list|)
block|{
name|this
operator|.
name|chunked
operator|=
name|chunked
expr_stmt|;
block|}
DECL|method|isCompression ()
specifier|public
name|boolean
name|isCompression
parameter_list|()
block|{
return|return
name|compression
return|;
block|}
DECL|method|setCompression (boolean compression)
specifier|public
name|void
name|setCompression
parameter_list|(
name|boolean
name|compression
parameter_list|)
block|{
name|this
operator|.
name|compression
operator|=
name|compression
expr_stmt|;
block|}
block|}
end_class

end_unit

