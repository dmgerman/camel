begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
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
name|component
operator|.
name|http
operator|.
name|helper
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
name|ExchangeHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|methods
operator|.
name|InputStreamRequestEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|httpclient
operator|.
name|methods
operator|.
name|RequestEntity
import|;
end_import

begin_comment
comment|/**  * Some converter methods to make it easier to convert the body to RequestEntity types.  */
end_comment

begin_class
annotation|@
name|Converter
DECL|class|RequestEntityConverter
specifier|public
class|class
name|RequestEntityConverter
block|{
annotation|@
name|Converter
DECL|method|toRequestEntity (byte[] data, Exchange exchange)
specifier|public
name|RequestEntity
name|toRequestEntity
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
name|asRequestEntity
argument_list|(
name|data
argument_list|,
name|exchange
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toRequestEntity (InputStream inStream, Exchange exchange)
specifier|public
name|RequestEntity
name|toRequestEntity
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
name|asRequestEntity
argument_list|(
name|inStream
argument_list|,
name|exchange
argument_list|)
return|;
block|}
annotation|@
name|Converter
DECL|method|toRequestEntity (String str, Exchange exchange)
specifier|public
name|RequestEntity
name|toRequestEntity
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
name|asRequestEntity
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
DECL|method|asRequestEntity (InputStream in, Exchange exchange)
specifier|private
name|RequestEntity
name|asRequestEntity
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
return|return
operator|new
name|InputStreamRequestEntity
argument_list|(
name|GZIPHelper
operator|.
name|compressGzip
argument_list|(
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
argument_list|,
name|in
argument_list|)
argument_list|,
name|ExchangeHelper
operator|.
name|getContentType
argument_list|(
name|exchange
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
comment|// should set the content type here
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|InputStreamRequestEntity
argument_list|(
name|in
argument_list|,
name|ExchangeHelper
operator|.
name|getContentType
argument_list|(
name|exchange
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|InputStreamRequestEntity
argument_list|(
name|in
argument_list|)
return|;
block|}
block|}
block|}
DECL|method|asRequestEntity (byte[] data, Exchange exchange)
specifier|private
name|RequestEntity
name|asRequestEntity
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
return|return
operator|new
name|InputStreamRequestEntity
argument_list|(
name|GZIPHelper
operator|.
name|compressGzip
argument_list|(
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
argument_list|,
name|data
argument_list|)
argument_list|,
name|ExchangeHelper
operator|.
name|getContentType
argument_list|(
name|exchange
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
comment|// should set the content type here
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
return|return
operator|new
name|InputStreamRequestEntity
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|data
argument_list|)
argument_list|,
name|ExchangeHelper
operator|.
name|getContentType
argument_list|(
name|exchange
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|InputStreamRequestEntity
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|data
argument_list|)
argument_list|)
return|;
block|}
block|}
block|}
block|}
end_class

end_unit

