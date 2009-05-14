begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http.helper
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
operator|.
name|helper
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedInputStream
import|;
end_import

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
name|ByteArrayOutputStream
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
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|GZIPInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|zip
operator|.
name|GZIPOutputStream
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
name|Message
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
name|Header
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
name|HttpMethod
import|;
end_import

begin_comment
comment|/**  *   * Helper/Utility class to help wrapping  * content into GZIP Input/Output Streams.  *   *  */
end_comment

begin_class
DECL|class|GZIPHelper
specifier|public
specifier|final
class|class
name|GZIPHelper
block|{
DECL|field|CONTENT_ENCODING
specifier|public
specifier|static
specifier|final
name|String
name|CONTENT_ENCODING
init|=
literal|"Content-Encoding"
decl_stmt|;
DECL|field|GZIP
specifier|public
specifier|static
specifier|final
name|String
name|GZIP
init|=
literal|"gzip"
decl_stmt|;
comment|// No need for instatiating, so avoid it.
DECL|method|GZIPHelper ()
specifier|private
name|GZIPHelper
parameter_list|()
block|{ }
DECL|method|setGZIPMessageHeader (Message message)
specifier|public
specifier|static
name|void
name|setGZIPMessageHeader
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|CONTENT_ENCODING
argument_list|,
name|GZIP
argument_list|)
expr_stmt|;
block|}
DECL|method|setGZIPContentEncoding (HttpServletResponse response)
specifier|public
specifier|static
name|void
name|setGZIPContentEncoding
parameter_list|(
name|HttpServletResponse
name|response
parameter_list|)
block|{
name|response
operator|.
name|setHeader
argument_list|(
name|CONTENT_ENCODING
argument_list|,
name|GZIP
argument_list|)
expr_stmt|;
block|}
comment|// --------- Methods To Decompress ----------
DECL|method|getInputStream (HttpMethod method)
specifier|public
specifier|static
name|InputStream
name|getInputStream
parameter_list|(
name|HttpMethod
name|method
parameter_list|)
throws|throws
name|IOException
block|{
name|Header
name|header
init|=
name|method
operator|.
name|getRequestHeader
argument_list|(
name|CONTENT_ENCODING
argument_list|)
decl_stmt|;
name|String
name|contentEncoding
init|=
name|header
operator|!=
literal|null
condition|?
name|header
operator|.
name|getValue
argument_list|()
else|:
literal|null
decl_stmt|;
return|return
name|getGZIPWrappedInputStream
argument_list|(
name|contentEncoding
argument_list|,
name|method
operator|.
name|getResponseBodyAsStream
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getInputStream (HttpServletRequest request)
specifier|public
specifier|static
name|InputStream
name|getInputStream
parameter_list|(
name|HttpServletRequest
name|request
parameter_list|)
throws|throws
name|IOException
block|{
name|InputStream
name|dataStream
init|=
name|request
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
name|String
name|contentEncoding
init|=
name|request
operator|.
name|getHeader
argument_list|(
name|CONTENT_ENCODING
argument_list|)
decl_stmt|;
return|return
name|getGZIPWrappedInputStream
argument_list|(
name|contentEncoding
argument_list|,
name|dataStream
argument_list|)
return|;
block|}
DECL|method|getGZIPWrappedInputStream (String gzipEncoding, InputStream inStream)
specifier|public
specifier|static
name|InputStream
name|getGZIPWrappedInputStream
parameter_list|(
name|String
name|gzipEncoding
parameter_list|,
name|InputStream
name|inStream
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|containsGzip
argument_list|(
name|gzipEncoding
argument_list|)
condition|)
block|{
return|return
operator|new
name|GZIPInputStream
argument_list|(
operator|new
name|BufferedInputStream
argument_list|(
name|inStream
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|inStream
return|;
block|}
block|}
DECL|method|toGZIPInputStreamIfRequested (String gzipEncoding, byte[] array)
specifier|public
specifier|static
name|InputStream
name|toGZIPInputStreamIfRequested
parameter_list|(
name|String
name|gzipEncoding
parameter_list|,
name|byte
index|[]
name|array
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|containsGzip
argument_list|(
name|gzipEncoding
argument_list|)
condition|)
block|{
comment|// GZip byte array content
name|ByteArrayOutputStream
name|outputByteArray
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|GZIPOutputStream
name|gzipOutputStream
init|=
operator|new
name|GZIPOutputStream
argument_list|(
name|outputByteArray
argument_list|)
decl_stmt|;
name|gzipOutputStream
operator|.
name|write
argument_list|(
name|array
argument_list|)
expr_stmt|;
name|gzipOutputStream
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|outputByteArray
operator|.
name|toByteArray
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
return|return
operator|new
name|ByteArrayInputStream
argument_list|(
name|array
argument_list|)
return|;
block|}
block|}
comment|// -------------- Methods To Compress --------------
DECL|method|compressArrayIfGZIPRequested (String gzipEncoding, byte[] array)
specifier|public
specifier|static
name|byte
index|[]
name|compressArrayIfGZIPRequested
parameter_list|(
name|String
name|gzipEncoding
parameter_list|,
name|byte
index|[]
name|array
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|containsGzip
argument_list|(
name|gzipEncoding
argument_list|)
condition|)
block|{
return|return
name|getGZIPWrappedOutputStream
argument_list|(
name|array
argument_list|)
operator|.
name|toByteArray
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|array
return|;
block|}
block|}
DECL|method|compressArrayIfGZIPRequested (String gzipEncoding, byte[] array, HttpServletResponse response)
specifier|public
specifier|static
name|byte
index|[]
name|compressArrayIfGZIPRequested
parameter_list|(
name|String
name|gzipEncoding
parameter_list|,
name|byte
index|[]
name|array
parameter_list|,
name|HttpServletResponse
name|response
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|containsGzip
argument_list|(
name|gzipEncoding
argument_list|)
condition|)
block|{
return|return
name|getGZIPWrappedOutputStream
argument_list|(
name|array
argument_list|)
operator|.
name|toByteArray
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|array
return|;
block|}
block|}
DECL|method|getGZIPWrappedOutputStream (byte[] array)
specifier|public
specifier|static
name|ByteArrayOutputStream
name|getGZIPWrappedOutputStream
parameter_list|(
name|byte
index|[]
name|array
parameter_list|)
throws|throws
name|IOException
block|{
name|ByteArrayOutputStream
name|compressed
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|GZIPOutputStream
name|gzout
init|=
operator|new
name|GZIPOutputStream
argument_list|(
name|compressed
argument_list|)
decl_stmt|;
name|gzout
operator|.
name|write
argument_list|(
name|array
argument_list|)
expr_stmt|;
name|gzout
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|compressed
return|;
block|}
DECL|method|containsGzip (String str)
specifier|public
specifier|static
name|boolean
name|containsGzip
parameter_list|(
name|String
name|str
parameter_list|)
block|{
return|return
name|str
operator|!=
literal|null
operator|&&
name|str
operator|.
name|toLowerCase
argument_list|()
operator|.
name|indexOf
argument_list|(
name|GZIP
argument_list|)
operator|>=
literal|0
return|;
block|}
block|}
end_class

end_unit

