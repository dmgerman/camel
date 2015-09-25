begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.zipfile
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|zipfile
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
name|File
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
name|io
operator|.
name|OutputStream
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
name|ZipEntry
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
name|ZipInputStream
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
name|ZipOutputStream
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
name|spi
operator|.
name|DataFormat
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
name|DataFormatName
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
name|ServiceSupport
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
name|IOHelper
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
name|StringHelper
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
operator|.
name|FILE_NAME
import|;
end_import

begin_comment
comment|/**  * Zip file data format.  * See {@link org.apache.camel.model.dataformat.ZipDataFormat} for "deflate" compression.  */
end_comment

begin_class
DECL|class|ZipFileDataFormat
specifier|public
class|class
name|ZipFileDataFormat
extends|extends
name|ServiceSupport
implements|implements
name|DataFormat
implements|,
name|DataFormatName
block|{
DECL|field|usingIterator
specifier|private
name|boolean
name|usingIterator
decl_stmt|;
annotation|@
name|Override
DECL|method|getDataFormatName ()
specifier|public
name|String
name|getDataFormatName
parameter_list|()
block|{
return|return
literal|"zipFile"
return|;
block|}
annotation|@
name|Override
DECL|method|marshal (Exchange exchange, Object graph, OutputStream stream)
specifier|public
name|void
name|marshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|graph
parameter_list|,
name|OutputStream
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|filename
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|FILE_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|filename
operator|!=
literal|null
condition|)
block|{
name|filename
operator|=
operator|new
name|File
argument_list|(
name|filename
argument_list|)
operator|.
name|getName
argument_list|()
expr_stmt|;
comment|// remove any path elements
block|}
else|else
block|{
comment|// generate the file name as the camel file component would do
name|filename
operator|=
name|StringHelper
operator|.
name|sanitize
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMessageId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ZipOutputStream
name|zos
init|=
operator|new
name|ZipOutputStream
argument_list|(
name|stream
argument_list|)
decl_stmt|;
name|zos
operator|.
name|putNextEntry
argument_list|(
operator|new
name|ZipEntry
argument_list|(
name|filename
argument_list|)
argument_list|)
expr_stmt|;
name|InputStream
name|is
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|graph
argument_list|)
decl_stmt|;
try|try
block|{
name|IOHelper
operator|.
name|copy
argument_list|(
name|is
argument_list|,
name|zos
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|is
argument_list|,
name|zos
argument_list|)
expr_stmt|;
block|}
name|String
name|newFilename
init|=
name|filename
operator|+
literal|".zip"
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|FILE_NAME
argument_list|,
name|newFilename
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|unmarshal (Exchange exchange, InputStream stream)
specifier|public
name|Object
name|unmarshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|usingIterator
condition|)
block|{
return|return
operator|new
name|ZipIterator
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
return|;
block|}
else|else
block|{
name|InputStream
name|is
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|ZipInputStream
name|zis
init|=
operator|new
name|ZipInputStream
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
name|ZipEntry
name|entry
init|=
name|zis
operator|.
name|getNextEntry
argument_list|()
decl_stmt|;
if|if
condition|(
name|entry
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|FILE_NAME
argument_list|,
name|entry
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|IOHelper
operator|.
name|copy
argument_list|(
name|zis
argument_list|,
name|baos
argument_list|)
expr_stmt|;
block|}
name|entry
operator|=
name|zis
operator|.
name|getNextEntry
argument_list|()
expr_stmt|;
if|if
condition|(
name|entry
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Zip file has more than 1 entry."
argument_list|)
throw|;
block|}
return|return
name|baos
operator|.
name|toByteArray
argument_list|()
return|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|zis
argument_list|,
name|baos
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|isUsingIterator ()
specifier|public
name|boolean
name|isUsingIterator
parameter_list|()
block|{
return|return
name|usingIterator
return|;
block|}
DECL|method|setUsingIterator (boolean usingIterator)
specifier|public
name|void
name|setUsingIterator
parameter_list|(
name|boolean
name|usingIterator
parameter_list|)
block|{
name|this
operator|.
name|usingIterator
operator|=
name|usingIterator
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
block|}
end_class

end_unit

