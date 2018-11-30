begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.as2.api.entity
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|as2
operator|.
name|api
operator|.
name|entity
package|;
end_package

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
name|OutputStream
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
name|as2
operator|.
name|api
operator|.
name|AS2Charset
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
name|as2
operator|.
name|api
operator|.
name|AS2Header
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
name|as2
operator|.
name|api
operator|.
name|CanonicalOutputStream
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
name|as2
operator|.
name|api
operator|.
name|util
operator|.
name|EntityUtils
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
name|Header
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
name|HeaderIterator
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
name|HttpException
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
name|ContentType
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
name|message
operator|.
name|BasicNameValuePair
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
name|util
operator|.
name|Args
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|cms
operator|.
name|CMSCompressedData
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|cms
operator|.
name|CMSCompressedDataGenerator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|cms
operator|.
name|CMSProcessableByteArray
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|cms
operator|.
name|CMSTypedData
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|operator
operator|.
name|InputExpanderProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|operator
operator|.
name|OutputCompressor
import|;
end_import

begin_class
DECL|class|ApplicationPkcs7MimeCompressedDataEntity
specifier|public
class|class
name|ApplicationPkcs7MimeCompressedDataEntity
extends|extends
name|MimeEntity
block|{
DECL|field|CONTENT_DISPOSITION
specifier|private
specifier|static
specifier|final
name|String
name|CONTENT_DISPOSITION
init|=
literal|"attachment; filename=\"smime.p7z\""
decl_stmt|;
DECL|field|compressedData
specifier|private
name|byte
index|[]
name|compressedData
decl_stmt|;
DECL|method|ApplicationPkcs7MimeCompressedDataEntity (MimeEntity entity2Encrypt, CMSCompressedDataGenerator dataGenerator, OutputCompressor compressor, String compressedContentTransferEncoding, boolean isMainBody)
specifier|public
name|ApplicationPkcs7MimeCompressedDataEntity
parameter_list|(
name|MimeEntity
name|entity2Encrypt
parameter_list|,
name|CMSCompressedDataGenerator
name|dataGenerator
parameter_list|,
name|OutputCompressor
name|compressor
parameter_list|,
name|String
name|compressedContentTransferEncoding
parameter_list|,
name|boolean
name|isMainBody
parameter_list|)
throws|throws
name|HttpException
block|{
name|setContentType
argument_list|(
name|ContentType
operator|.
name|create
argument_list|(
literal|"application/pkcs7-mime"
argument_list|,
operator|new
name|BasicNameValuePair
argument_list|(
literal|"smime-type"
argument_list|,
literal|"compressed-data"
argument_list|)
argument_list|,
operator|new
name|BasicNameValuePair
argument_list|(
literal|"name"
argument_list|,
literal|"smime.p7z"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|setContentTransferEncoding
argument_list|(
name|compressedContentTransferEncoding
argument_list|)
expr_stmt|;
name|addHeader
argument_list|(
name|AS2Header
operator|.
name|CONTENT_DISPOSITION
argument_list|,
name|CONTENT_DISPOSITION
argument_list|)
expr_stmt|;
name|setMainBody
argument_list|(
name|isMainBody
argument_list|)
expr_stmt|;
try|try
block|{
name|this
operator|.
name|compressedData
operator|=
name|createCompressedData
argument_list|(
name|entity2Encrypt
argument_list|,
name|dataGenerator
argument_list|,
name|compressor
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|HttpException
argument_list|(
literal|"Failed to create encrypted data"
argument_list|)
throw|;
block|}
block|}
DECL|method|ApplicationPkcs7MimeCompressedDataEntity (byte[] compressedData, String compressedContentTransferEncoding, boolean isMainBody)
specifier|public
name|ApplicationPkcs7MimeCompressedDataEntity
parameter_list|(
name|byte
index|[]
name|compressedData
parameter_list|,
name|String
name|compressedContentTransferEncoding
parameter_list|,
name|boolean
name|isMainBody
parameter_list|)
block|{
name|this
operator|.
name|compressedData
operator|=
name|Args
operator|.
name|notNull
argument_list|(
name|compressedData
argument_list|,
literal|"encryptedData"
argument_list|)
expr_stmt|;
name|setContentType
argument_list|(
name|ContentType
operator|.
name|create
argument_list|(
literal|"application/pkcs7-mime"
argument_list|,
operator|new
name|BasicNameValuePair
argument_list|(
literal|"smime-type"
argument_list|,
literal|"compressed-data"
argument_list|)
argument_list|,
operator|new
name|BasicNameValuePair
argument_list|(
literal|"name"
argument_list|,
literal|"smime.p7z"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|setContentTransferEncoding
argument_list|(
name|compressedContentTransferEncoding
argument_list|)
expr_stmt|;
name|addHeader
argument_list|(
name|AS2Header
operator|.
name|CONTENT_DISPOSITION
argument_list|,
name|CONTENT_DISPOSITION
argument_list|)
expr_stmt|;
name|setMainBody
argument_list|(
name|isMainBody
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|writeTo (OutputStream outstream)
specifier|public
name|void
name|writeTo
parameter_list|(
name|OutputStream
name|outstream
parameter_list|)
throws|throws
name|IOException
block|{
name|NoCloseOutputStream
name|ncos
init|=
operator|new
name|NoCloseOutputStream
argument_list|(
name|outstream
argument_list|)
decl_stmt|;
comment|// Write out mime part headers if this is not the main body of message.
if|if
condition|(
operator|!
name|isMainBody
argument_list|()
condition|)
block|{
try|try
init|(
name|CanonicalOutputStream
name|canonicalOutstream
init|=
operator|new
name|CanonicalOutputStream
argument_list|(
name|ncos
argument_list|,
name|AS2Charset
operator|.
name|US_ASCII
argument_list|)
init|)
block|{
name|HeaderIterator
name|it
init|=
name|headerIterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Header
name|header
init|=
name|it
operator|.
name|nextHeader
argument_list|()
decl_stmt|;
name|canonicalOutstream
operator|.
name|writeln
argument_list|(
name|header
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|canonicalOutstream
operator|.
name|writeln
argument_list|()
expr_stmt|;
comment|// ensure empty line between
comment|// headers and body; RFC2046 -
comment|// 5.1.1
block|}
block|}
comment|// Write out signed data.
name|String
name|transferEncoding
init|=
name|getContentTransferEncoding
argument_list|()
operator|==
literal|null
condition|?
literal|null
else|:
name|getContentTransferEncoding
argument_list|()
operator|.
name|getValue
argument_list|()
decl_stmt|;
try|try
init|(
name|OutputStream
name|transferEncodedStream
init|=
name|EntityUtils
operator|.
name|encode
argument_list|(
name|ncos
argument_list|,
name|transferEncoding
argument_list|)
init|)
block|{
name|transferEncodedStream
operator|.
name|write
argument_list|(
name|compressedData
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Failed to write to output stream"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|getCompressedEntity (InputExpanderProvider expanderProvider)
specifier|public
name|MimeEntity
name|getCompressedEntity
parameter_list|(
name|InputExpanderProvider
name|expanderProvider
parameter_list|)
throws|throws
name|HttpException
block|{
return|return
name|EntityParser
operator|.
name|parseCompressedEntity
argument_list|(
name|compressedData
argument_list|,
name|expanderProvider
argument_list|)
return|;
block|}
DECL|method|createCompressedData (MimeEntity entity2Encrypt, CMSCompressedDataGenerator compressedDataGenerator, OutputCompressor compressor)
specifier|private
name|byte
index|[]
name|createCompressedData
parameter_list|(
name|MimeEntity
name|entity2Encrypt
parameter_list|,
name|CMSCompressedDataGenerator
name|compressedDataGenerator
parameter_list|,
name|OutputCompressor
name|compressor
parameter_list|)
throws|throws
name|Exception
block|{
try|try
init|(
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
init|)
block|{
name|entity2Encrypt
operator|.
name|writeTo
argument_list|(
name|bos
argument_list|)
expr_stmt|;
name|bos
operator|.
name|flush
argument_list|()
expr_stmt|;
name|CMSTypedData
name|contentData
init|=
operator|new
name|CMSProcessableByteArray
argument_list|(
name|bos
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|CMSCompressedData
name|compressedData
init|=
name|compressedDataGenerator
operator|.
name|generate
argument_list|(
name|contentData
argument_list|,
name|compressor
argument_list|)
decl_stmt|;
return|return
name|compressedData
operator|.
name|getEncoded
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|""
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

