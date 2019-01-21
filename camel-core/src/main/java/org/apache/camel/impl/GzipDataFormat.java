begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.impl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|impl
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
name|converter
operator|.
name|stream
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
comment|/**  * GZip {@link org.apache.camel.spi.DataFormat} for reading/writing data using gzip.  */
end_comment

begin_class
annotation|@
name|Dataformat
argument_list|(
literal|"gzip"
argument_list|)
DECL|class|GzipDataFormat
specifier|public
class|class
name|GzipDataFormat
extends|extends
name|ServiceSupport
implements|implements
name|DataFormat
implements|,
name|DataFormatName
block|{
annotation|@
name|Override
DECL|method|getDataFormatName ()
specifier|public
name|String
name|getDataFormatName
parameter_list|()
block|{
return|return
literal|"gzip"
return|;
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
name|GZIPOutputStream
name|zipOutput
init|=
operator|new
name|GZIPOutputStream
argument_list|(
name|stream
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
comment|// must close all input streams
name|IOHelper
operator|.
name|close
argument_list|(
name|is
argument_list|,
name|zipOutput
argument_list|)
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
name|GZIPInputStream
name|unzipInput
init|=
literal|null
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
name|unzipInput
operator|=
operator|new
name|GZIPInputStream
argument_list|(
name|inputStream
argument_list|)
expr_stmt|;
name|IOHelper
operator|.
name|copy
argument_list|(
name|unzipInput
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
comment|// must close all input streams
name|IOHelper
operator|.
name|close
argument_list|(
name|osb
argument_list|,
name|unzipInput
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

