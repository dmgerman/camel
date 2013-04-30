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
name|Converter
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
name|FallbackConverter
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
name|TypeConverterRegistry
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
name|handler
operator|.
name|codec
operator|.
name|http
operator|.
name|HttpRequest
import|;
end_import

begin_class
annotation|@
name|Converter
DECL|class|NettyHttpConverter
specifier|public
specifier|final
class|class
name|NettyHttpConverter
block|{
DECL|method|NettyHttpConverter ()
specifier|private
name|NettyHttpConverter
parameter_list|()
block|{     }
comment|/**      * A fallback converter that allows us to easily call Java beans and use the raw Netty {@link HttpRequest} as parameter types.      */
annotation|@
name|FallbackConverter
DECL|method|convertToHttpRequest (Class<?> type, Exchange exchange, Object value, TypeConverterRegistry registry)
specifier|public
specifier|static
name|Object
name|convertToHttpRequest
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Object
name|value
parameter_list|,
name|TypeConverterRegistry
name|registry
parameter_list|)
block|{
comment|// if we want to covert to HttpRequest
if|if
condition|(
name|value
operator|!=
literal|null
operator|&&
name|HttpRequest
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
comment|// okay we may need to cheat a bit when we want to grab the HttpRequest as its stored on the NettyHttpMessage
comment|// so if the message instance is a NettyHttpMessage and its body is the value, then we can grab the
comment|// HttpRequest from the NettyHttpMessage
name|NettyHttpMessage
name|msg
init|=
name|exchange
operator|.
name|getIn
argument_list|(
name|NettyHttpMessage
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|msg
operator|!=
literal|null
operator|&&
name|msg
operator|.
name|getBody
argument_list|()
operator|==
name|value
condition|)
block|{
return|return
name|msg
operator|.
name|getHttpRequest
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

