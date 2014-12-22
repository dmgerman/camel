begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http4
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
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
name|util
operator|.
name|ExchangeHelper
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
name|util
operator|.
name|GZIPHelper
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|entity
operator|.
name|AbstractHttpEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|entity
operator|.
name|ByteArrayEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|entity
operator|.
name|InputStreamEntity
import|;
end_import

begin_comment
comment|/**  * Some converter methods to make it easier to convert the body to RequestEntity types.  */
end_comment

begin_class
annotation|@
name|Converter
DECL|class|HttpEntityConverter
specifier|public
specifier|final
class|class
name|HttpEntityConverter
block|{
DECL|method|HttpEntityConverter ()
specifier|private
name|HttpEntityConverter
parameter_list|()
block|{     }
annotation|@
name|Converter
DECL|method|toHttpEntity (byte[] data, Exchange exchange)
specifier|public
specifier|static
name|HttpEntity
name|toHttpEntity
parameter_list|(
name|byte
index|[]
name|data
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|asHttpEntity
argument_list|(
name|data
argument_list|,
name|exchange
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toHttpEntity (InputStream inStream, Exchange exchange)
specifier|public
specifier|static
name|HttpEntity
name|toHttpEntity
parameter_list|(
name|InputStream
name|inStream
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|asHttpEntity
argument_list|(
name|inStream
argument_list|,
name|exchange
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toHttpEntity (String str, Exchange exchange)
specifier|public
specifier|static
name|HttpEntity
name|toHttpEntity
parameter_list|(
name|String
name|str
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|exchange
operator|!=
literal|null
operator|&&
name|GZIPHelper
operator|.
name|isGzip
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
condition|)
block|{
name|byte
index|[]
name|data
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|byte
index|[]
operator|.
expr|class
argument_list|,
name|str
argument_list|)
decl_stmt|;
return|return
name|asHttpEntity
argument_list|(
name|data
argument_list|,
name|exchange
argument_list|)
return|;
block|}
else|else
block|{
comment|// will use the default StringRequestEntity
return|return
literal|null
return|;
block|}
block|}
DECL|method|asHttpEntity (InputStream in, Exchange exchange)
specifier|private
specifier|static
name|HttpEntity
name|asHttpEntity
parameter_list|(
name|InputStream
name|in
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
name|InputStreamEntity
name|entity
decl_stmt|;
if|if
condition|(
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_ENCODING
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|InputStream
name|stream
init|=
name|GZIPHelper
operator|.
name|compressGzip
argument_list|(
name|contentEncoding
argument_list|,
name|in
argument_list|)
decl_stmt|;
name|entity
operator|=
operator|new
name|InputStreamEntity
argument_list|(
name|stream
argument_list|,
name|stream
operator|instanceof
name|ByteArrayInputStream
condition|?
name|stream
operator|.
name|available
argument_list|()
operator|!=
literal|0
condition|?
name|stream
operator|.
name|available
argument_list|()
else|:
operator|-
literal|1
else|:
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Message
name|inMessage
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|String
name|length
init|=
name|inMessage
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_LENGTH
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|length
argument_list|)
condition|)
block|{
name|entity
operator|=
operator|new
name|InputStreamEntity
argument_list|(
name|in
argument_list|,
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|entity
operator|=
operator|new
name|InputStreamEntity
argument_list|(
name|in
argument_list|,
name|Long
operator|.
name|parseLong
argument_list|(
name|length
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
name|String
name|contentEncoding
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_ENCODING
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|contentType
init|=
name|ExchangeHelper
operator|.
name|getContentType
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|entity
operator|.
name|setContentEncoding
argument_list|(
name|contentEncoding
argument_list|)
expr_stmt|;
name|entity
operator|.
name|setContentType
argument_list|(
name|contentType
argument_list|)
expr_stmt|;
block|}
return|return
name|entity
return|;
block|}
DECL|method|asHttpEntity (byte[] data, Exchange exchange)
specifier|private
specifier|static
name|HttpEntity
name|asHttpEntity
parameter_list|(
name|byte
index|[]
name|data
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|AbstractHttpEntity
name|entity
decl_stmt|;
if|if
condition|(
name|exchange
operator|!=
literal|null
operator|&&
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_ENCODING
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|InputStream
name|stream
init|=
name|GZIPHelper
operator|.
name|compressGzip
argument_list|(
name|contentEncoding
argument_list|,
name|data
argument_list|)
decl_stmt|;
name|entity
operator|=
operator|new
name|InputStreamEntity
argument_list|(
name|stream
argument_list|,
name|stream
operator|instanceof
name|ByteArrayInputStream
condition|?
name|stream
operator|.
name|available
argument_list|()
operator|!=
literal|0
condition|?
name|stream
operator|.
name|available
argument_list|()
else|:
operator|-
literal|1
else|:
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// create the Repeatable HttpEntity
name|entity
operator|=
operator|new
name|ByteArrayEntity
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
name|String
name|contentEncoding
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|CONTENT_ENCODING
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|contentType
init|=
name|ExchangeHelper
operator|.
name|getContentType
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|entity
operator|.
name|setContentEncoding
argument_list|(
name|contentEncoding
argument_list|)
expr_stmt|;
name|entity
operator|.
name|setContentType
argument_list|(
name|contentType
argument_list|)
expr_stmt|;
block|}
return|return
name|entity
return|;
block|}
block|}
end_class

end_unit

