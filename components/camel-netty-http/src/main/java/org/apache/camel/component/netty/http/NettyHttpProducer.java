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
name|Exchange
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
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty
operator|.
name|NettyProducer
import|;
end_import

begin_comment
comment|/**  * HTTP based {@link NettyProducer}.  */
end_comment

begin_class
DECL|class|NettyHttpProducer
specifier|public
class|class
name|NettyHttpProducer
extends|extends
name|NettyProducer
block|{
DECL|method|NettyHttpProducer (NettyHttpEndpoint nettyEndpoint, NettyConfiguration configuration)
specifier|public
name|NettyHttpProducer
parameter_list|(
name|NettyHttpEndpoint
name|nettyEndpoint
parameter_list|,
name|NettyConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|nettyEndpoint
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|NettyHttpEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|NettyHttpEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getRequestBody (Exchange exchange)
specifier|protected
name|Object
name|getRequestBody
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|uri
init|=
name|getEndpoint
argument_list|()
operator|.
name|getEndpointUri
argument_list|()
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getNettyHttpBinding
argument_list|()
operator|.
name|toNettyRequest
argument_list|(
name|exchange
operator|.
name|getOut
argument_list|()
argument_list|,
name|uri
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|getEndpoint
argument_list|()
operator|.
name|getNettyHttpBinding
argument_list|()
operator|.
name|toNettyRequest
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
name|uri
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

