begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.deflater
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|deflater
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
name|Deflater
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
name|DeflaterOutputStream
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
name|InflaterInputStream
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
name|support
operator|.
name|builder
operator|.
name|OutputStreamBuilder
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
name|spi
operator|.
name|annotations
operator|.
name|Dataformat
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
name|service
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

begin_comment
comment|/**  * Deflate (zip) compression data format (does not support zip files, instead use zipfile dataformat).  */
end_comment

begin_class
annotation|@
name|Dataformat
argument_list|(
literal|"zipdeflater"
argument_list|)
DECL|class|ZipDeflaterDataFormat
specifier|public
class|class
name|ZipDeflaterDataFormat
extends|extends
name|ServiceSupport
implements|implements
name|DataFormat
implements|,
name|DataFormatName
block|{
DECL|field|compressionLevel
specifier|private
name|int
name|compressionLevel
decl_stmt|;
DECL|method|ZipDeflaterDataFormat ()
specifier|public
name|ZipDeflaterDataFormat
parameter_list|()
block|{
name|this
operator|.
name|compressionLevel
operator|=
name|Deflater
operator|.
name|DEFAULT_COMPRESSION
expr_stmt|;
block|}
DECL|method|ZipDeflaterDataFormat (int compressionLevel)
specifier|public
name|ZipDeflaterDataFormat
parameter_list|(
name|int
name|compressionLevel
parameter_list|)
block|{
name|this
operator|.
name|compressionLevel
operator|=
name|compressionLevel
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getDataFormatName ()
specifier|public
name|String
name|getDataFormatName
parameter_list|()
block|{
return|return
literal|"zipdeflater"
return|;
block|}
DECL|method|getCompressionLevel ()
specifier|public
name|int
name|getCompressionLevel
parameter_list|()
block|{
return|return
name|compressionLevel
return|;
block|}
DECL|method|setCompressionLevel (int compressionLevel)
specifier|public
name|void
name|setCompressionLevel
parameter_list|(
name|int
name|compressionLevel
parameter_list|)
block|{
name|this
operator|.
name|compressionLevel
operator|=
name|compressionLevel
expr_stmt|;
block|}
DECL|method|marshal (final Exchange exchange, final Object graph, final OutputStream stream)
specifier|public
name|void
name|marshal
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|Object
name|graph
parameter_list|,
specifier|final
name|OutputStream
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
comment|// ask for a mandatory type conversion to avoid a possible NPE beforehand as we do copy from the InputStream
specifier|final
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
name|exchange
argument_list|,
name|graph
argument_list|)
decl_stmt|;
specifier|final
name|Deflater
name|deflater
init|=
operator|new
name|Deflater
argument_list|(
name|compressionLevel
argument_list|)
decl_stmt|;
specifier|final
name|DeflaterOutputStream
name|zipOutput
init|=
operator|new
name|DeflaterOutputStream
argument_list|(
name|stream
argument_list|,
name|deflater
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
name|zipOutput
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
name|zipOutput
argument_list|)
expr_stmt|;
comment|/*             * As we create the Deflater our self and do not use the stream default             * (see {@link java.util.zip.DeflaterOutputStream#usesDefaultDeflater})             * we need to close the Deflater to not risk a OutOfMemoryException             * in native code parts (see {@link java.util.zip.Deflater#end})             */
name|deflater
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|unmarshal (final Exchange exchange, final InputStream inputStream)
specifier|public
name|Object
name|unmarshal
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|InputStream
name|inputStream
parameter_list|)
throws|throws
name|Exception
block|{
name|InflaterInputStream
name|inflaterInputStream
init|=
operator|new
name|InflaterInputStream
argument_list|(
name|inputStream
argument_list|)
decl_stmt|;
name|OutputStreamBuilder
name|osb
init|=
name|OutputStreamBuilder
operator|.
name|withExchange
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
try|try
block|{
name|IOHelper
operator|.
name|copy
argument_list|(
name|inflaterInputStream
argument_list|,
name|osb
argument_list|)
expr_stmt|;
return|return
name|osb
operator|.
name|build
argument_list|()
return|;
block|}
finally|finally
block|{
comment|// must close input streams
name|IOHelper
operator|.
name|close
argument_list|(
name|osb
argument_list|,
name|inflaterInputStream
argument_list|,
name|inputStream
argument_list|)
expr_stmt|;
block|}
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

