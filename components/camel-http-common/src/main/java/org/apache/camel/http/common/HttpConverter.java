begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.http.common
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|http
operator|.
name|common
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

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
name|javax
operator|.
name|servlet
operator|.
name|ServletInputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
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
name|Message
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
name|support
operator|.
name|GZIPHelper
import|;
end_import

begin_comment
comment|/**  * Some converter methods making it easy to convert the body of a message to servlet types or to switch between  * the underlying {@link ServletInputStream} or {@link BufferedReader} payloads etc.  */
end_comment

begin_class
annotation|@
name|Converter
argument_list|(
name|generateLoader
operator|=
literal|true
argument_list|)
DECL|class|HttpConverter
specifier|public
specifier|final
class|class
name|HttpConverter
block|{
DECL|method|HttpConverter ()
specifier|private
name|HttpConverter
parameter_list|()
block|{     }
annotation|@
name|Converter
DECL|method|toServletRequest (Message message)
specifier|public
specifier|static
name|HttpServletRequest
name|toServletRequest
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
name|message
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|message
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_SERVLET_REQUEST
argument_list|,
name|HttpServletRequest
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toServletResponse (Message message)
specifier|public
specifier|static
name|HttpServletResponse
name|toServletResponse
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
name|message
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|message
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_SERVLET_RESPONSE
argument_list|,
name|HttpServletResponse
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toServletInputStream (HttpMessage message)
specifier|public
specifier|static
name|ServletInputStream
name|toServletInputStream
parameter_list|(
name|HttpMessage
name|message
parameter_list|)
throws|throws
name|IOException
block|{
name|HttpServletRequest
name|request
init|=
name|toServletRequest
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|request
operator|!=
literal|null
condition|)
block|{
return|return
name|request
operator|.
name|getInputStream
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Converter
DECL|method|toInputStream (HttpMessage message, Exchange exchange)
specifier|public
specifier|static
name|InputStream
name|toInputStream
parameter_list|(
name|HttpMessage
name|message
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|toInputStream
argument_list|(
name|toServletRequest
argument_list|(
name|message
argument_list|)
argument_list|,
name|exchange
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toReader (HttpMessage message)
specifier|public
specifier|static
name|BufferedReader
name|toReader
parameter_list|(
name|HttpMessage
name|message
parameter_list|)
throws|throws
name|IOException
block|{
name|HttpServletRequest
name|request
init|=
name|toServletRequest
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|request
operator|!=
literal|null
condition|)
block|{
return|return
name|request
operator|.
name|getReader
argument_list|()
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Converter
DECL|method|toInputStream (HttpServletRequest request, Exchange exchange)
specifier|public
specifier|static
name|InputStream
name|toInputStream
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|request
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|InputStream
name|is
init|=
name|request
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
operator|&&
name|is
operator|.
name|available
argument_list|()
operator|<=
literal|0
condition|)
block|{
comment|// there is no data, so we cannot uncompress etc.
return|return
name|is
return|;
block|}
if|if
condition|(
name|exchange
operator|==
literal|null
operator|||
operator|!
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|SKIP_GZIP_ENCODING
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
condition|)
block|{
name|String
name|contentEncoding
init|=
name|request
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_ENCODING
argument_list|)
decl_stmt|;
return|return
name|GZIPHelper
operator|.
name|uncompressGzip
argument_list|(
name|contentEncoding
argument_list|,
name|is
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|is
return|;
block|}
block|}
block|}
end_class

end_unit

