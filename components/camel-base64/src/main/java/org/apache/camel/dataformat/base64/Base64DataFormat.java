begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.base64
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|base64
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|codec
operator|.
name|binary
operator|.
name|Base64
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
name|codec
operator|.
name|binary
operator|.
name|Base64InputStream
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
name|codec
operator|.
name|binary
operator|.
name|Base64OutputStream
import|;
end_import

begin_class
annotation|@
name|Dataformat
argument_list|(
literal|"base64"
argument_list|)
DECL|class|Base64DataFormat
specifier|public
class|class
name|Base64DataFormat
extends|extends
name|ServiceSupport
implements|implements
name|DataFormat
implements|,
name|DataFormatName
block|{
DECL|field|lineLength
specifier|private
name|int
name|lineLength
init|=
name|Base64
operator|.
name|MIME_CHUNK_SIZE
decl_stmt|;
DECL|field|lineSeparator
specifier|private
name|byte
index|[]
name|lineSeparator
init|=
block|{
literal|'\r'
block|,
literal|'\n'
block|}
decl_stmt|;
DECL|field|urlSafe
specifier|private
name|boolean
name|urlSafe
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
literal|"base64"
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
if|if
condition|(
name|urlSafe
condition|)
block|{
name|marshalUrlSafe
argument_list|(
name|exchange
argument_list|,
name|graph
argument_list|,
name|stream
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|marshalStreaming
argument_list|(
name|exchange
argument_list|,
name|graph
argument_list|,
name|stream
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|marshalStreaming (Exchange exchange, Object graph, OutputStream stream)
specifier|private
name|void
name|marshalStreaming
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
name|InputStream
name|decoded
init|=
name|ExchangeHelper
operator|.
name|convertToMandatoryType
argument_list|(
name|exchange
argument_list|,
name|InputStream
operator|.
name|class
argument_list|,
name|graph
argument_list|)
decl_stmt|;
name|Base64OutputStream
name|base64Output
init|=
operator|new
name|Base64OutputStream
argument_list|(
name|stream
argument_list|,
literal|true
argument_list|,
name|lineLength
argument_list|,
name|lineSeparator
argument_list|)
decl_stmt|;
try|try
block|{
name|IOHelper
operator|.
name|copy
argument_list|(
name|decoded
argument_list|,
name|base64Output
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|decoded
argument_list|,
name|base64Output
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|marshalUrlSafe (Exchange exchange, Object graph, OutputStream stream)
specifier|private
name|void
name|marshalUrlSafe
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
name|byte
index|[]
name|decoded
init|=
name|ExchangeHelper
operator|.
name|convertToMandatoryType
argument_list|(
name|exchange
argument_list|,
name|byte
index|[]
operator|.
expr|class
argument_list|,
name|graph
argument_list|)
decl_stmt|;
name|Base64
name|codec
init|=
operator|new
name|Base64
argument_list|(
name|lineLength
argument_list|,
name|lineSeparator
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|byte
index|[]
name|encoded
init|=
name|codec
operator|.
name|encode
argument_list|(
name|decoded
argument_list|)
decl_stmt|;
name|stream
operator|.
name|write
argument_list|(
name|encoded
argument_list|)
expr_stmt|;
name|stream
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|unmarshal (Exchange exchange, InputStream input)
specifier|public
name|Object
name|unmarshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|input
parameter_list|)
throws|throws
name|Exception
block|{
return|return
operator|new
name|Base64InputStream
argument_list|(
name|input
argument_list|,
literal|false
argument_list|,
name|lineLength
argument_list|,
name|lineSeparator
argument_list|)
return|;
block|}
DECL|method|getLineLength ()
specifier|public
name|int
name|getLineLength
parameter_list|()
block|{
return|return
name|lineLength
return|;
block|}
DECL|method|setLineLength (int lineLength)
specifier|public
name|void
name|setLineLength
parameter_list|(
name|int
name|lineLength
parameter_list|)
block|{
name|this
operator|.
name|lineLength
operator|=
name|lineLength
expr_stmt|;
block|}
DECL|method|getLineSeparator ()
specifier|public
name|byte
index|[]
name|getLineSeparator
parameter_list|()
block|{
return|return
name|lineSeparator
return|;
block|}
DECL|method|setLineSeparator (byte[] lineSeparator)
specifier|public
name|void
name|setLineSeparator
parameter_list|(
name|byte
index|[]
name|lineSeparator
parameter_list|)
block|{
name|this
operator|.
name|lineSeparator
operator|=
name|lineSeparator
expr_stmt|;
block|}
DECL|method|isUrlSafe ()
specifier|public
name|boolean
name|isUrlSafe
parameter_list|()
block|{
return|return
name|urlSafe
return|;
block|}
DECL|method|setUrlSafe (boolean urlSafe)
specifier|public
name|void
name|setUrlSafe
parameter_list|(
name|boolean
name|urlSafe
parameter_list|)
block|{
name|this
operator|.
name|urlSafe
operator|=
name|urlSafe
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

