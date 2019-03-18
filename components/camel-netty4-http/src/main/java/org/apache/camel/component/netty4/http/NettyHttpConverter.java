begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty4.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty4
operator|.
name|http
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|http
operator|.
name|FullHttpRequest
import|;
end_import

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|http
operator|.
name|FullHttpResponse
import|;
end_import

begin_import
import|import
name|io
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

begin_import
import|import
name|io
operator|.
name|netty
operator|.
name|handler
operator|.
name|codec
operator|.
name|http
operator|.
name|HttpResponse
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
name|component
operator|.
name|netty4
operator|.
name|NettyConverter
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

begin_class
annotation|@
name|Converter
argument_list|(
name|loader
operator|=
literal|true
argument_list|)
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
name|Converter
argument_list|(
name|fallback
operator|=
literal|true
argument_list|)
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
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|msg
operator|=
name|exchange
operator|.
name|getOut
argument_list|(
name|NettyHttpMessage
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|msg
operator|=
name|exchange
operator|.
name|getIn
argument_list|(
name|NettyHttpMessage
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
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
comment|// ensure the http request content is reset so we can read all the content out-of-the-box
name|FullHttpRequest
name|request
init|=
name|msg
operator|.
name|getHttpRequest
argument_list|()
decl_stmt|;
name|request
operator|.
name|content
argument_list|()
operator|.
name|resetReaderIndex
argument_list|()
expr_stmt|;
return|return
name|request
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * A fallback converter that allows us to easily call Java beans and use the raw Netty {@link HttpRequest} as parameter types.      */
annotation|@
name|Converter
argument_list|(
name|fallback
operator|=
literal|true
argument_list|)
DECL|method|convertToHttpResponse (Class<?> type, Exchange exchange, Object value, TypeConverterRegistry registry)
specifier|public
specifier|static
name|Object
name|convertToHttpResponse
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
comment|// if we want to covert to convertToHttpResponse
if|if
condition|(
name|value
operator|!=
literal|null
operator|&&
name|HttpResponse
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
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|msg
operator|=
name|exchange
operator|.
name|getOut
argument_list|(
name|NettyHttpMessage
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|msg
operator|=
name|exchange
operator|.
name|getIn
argument_list|(
name|NettyHttpMessage
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
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
name|getHttpResponse
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Converter
DECL|method|toString (FullHttpResponse response, Exchange exchange)
specifier|public
specifier|static
name|String
name|toString
parameter_list|(
name|FullHttpResponse
name|response
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|String
name|contentType
init|=
name|response
operator|.
name|headers
argument_list|()
operator|.
name|get
argument_list|(
name|Exchange
operator|.
name|CONTENT_TYPE
argument_list|)
decl_stmt|;
name|String
name|charset
init|=
name|NettyHttpHelper
operator|.
name|getCharsetFromContentType
argument_list|(
name|contentType
argument_list|)
decl_stmt|;
if|if
condition|(
name|charset
operator|==
literal|null
operator|&&
name|exchange
operator|!=
literal|null
condition|)
block|{
name|charset
operator|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|charset
operator|!=
literal|null
condition|)
block|{
return|return
name|response
operator|.
name|content
argument_list|()
operator|.
name|toString
argument_list|(
name|Charset
operator|.
name|forName
argument_list|(
name|charset
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|response
operator|.
name|content
argument_list|()
operator|.
name|toString
argument_list|(
name|Charset
operator|.
name|defaultCharset
argument_list|()
argument_list|)
return|;
block|}
block|}
annotation|@
name|Converter
DECL|method|toBytes (FullHttpResponse response, Exchange exchange)
specifier|public
specifier|static
name|byte
index|[]
name|toBytes
parameter_list|(
name|FullHttpResponse
name|response
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|NettyConverter
operator|.
name|toByteArray
argument_list|(
name|response
operator|.
name|content
argument_list|()
argument_list|,
name|exchange
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toInputStream (FullHttpResponse response, Exchange exchange)
specifier|public
specifier|static
name|InputStream
name|toInputStream
parameter_list|(
name|FullHttpResponse
name|response
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|NettyConverter
operator|.
name|toInputStream
argument_list|(
name|response
operator|.
name|content
argument_list|()
argument_list|,
name|exchange
argument_list|)
return|;
block|}
block|}
end_class

end_unit

