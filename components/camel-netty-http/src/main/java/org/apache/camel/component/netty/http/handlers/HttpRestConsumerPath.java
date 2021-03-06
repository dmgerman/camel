begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty.http.handlers
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
operator|.
name|handlers
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
name|support
operator|.
name|RestConsumerContextPathMatcher
import|;
end_import

begin_class
DECL|class|HttpRestConsumerPath
specifier|public
class|class
name|HttpRestConsumerPath
implements|implements
name|RestConsumerContextPathMatcher
operator|.
name|ConsumerPath
argument_list|<
name|HttpServerChannelHandler
argument_list|>
block|{
DECL|field|handler
specifier|private
specifier|final
name|HttpServerChannelHandler
name|handler
decl_stmt|;
DECL|method|HttpRestConsumerPath (HttpServerChannelHandler handler)
specifier|public
name|HttpRestConsumerPath
parameter_list|(
name|HttpServerChannelHandler
name|handler
parameter_list|)
block|{
name|this
operator|.
name|handler
operator|=
name|handler
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getRestrictMethod ()
specifier|public
name|String
name|getRestrictMethod
parameter_list|()
block|{
return|return
name|handler
operator|.
name|getConsumer
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getHttpMethodRestrict
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getConsumerPath ()
specifier|public
name|String
name|getConsumerPath
parameter_list|()
block|{
return|return
name|handler
operator|.
name|getConsumer
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getPath
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getConsumer ()
specifier|public
name|HttpServerChannelHandler
name|getConsumer
parameter_list|()
block|{
return|return
name|handler
return|;
block|}
annotation|@
name|Override
DECL|method|isMatchOnUriPrefix ()
specifier|public
name|boolean
name|isMatchOnUriPrefix
parameter_list|()
block|{
return|return
name|handler
operator|.
name|getConsumer
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isMatchOnUriPrefix
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|getConsumerPath
argument_list|()
return|;
block|}
block|}
end_class

end_unit

