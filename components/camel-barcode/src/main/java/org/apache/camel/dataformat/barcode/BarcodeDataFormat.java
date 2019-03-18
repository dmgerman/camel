begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.barcode
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|barcode
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
name|EnumMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|imageio
operator|.
name|ImageIO
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|zxing
operator|.
name|BarcodeFormat
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|zxing
operator|.
name|BinaryBitmap
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|zxing
operator|.
name|DecodeHintType
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|zxing
operator|.
name|EncodeHintType
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|zxing
operator|.
name|MultiFormatReader
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|zxing
operator|.
name|MultiFormatWriter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|zxing
operator|.
name|Result
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|zxing
operator|.
name|client
operator|.
name|j2se
operator|.
name|BufferedImageLuminanceSource
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|zxing
operator|.
name|client
operator|.
name|j2se
operator|.
name|MatrixToImageWriter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|zxing
operator|.
name|common
operator|.
name|BitMatrix
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|zxing
operator|.
name|common
operator|.
name|HybridBinarizer
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|zxing
operator|.
name|datamatrix
operator|.
name|encoder
operator|.
name|SymbolShapeHint
import|;
end_import

begin_import
import|import
name|com
operator|.
name|google
operator|.
name|zxing
operator|.
name|qrcode
operator|.
name|decoder
operator|.
name|ErrorCorrectionLevel
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

begin_comment
comment|/**  * {@link DataFormat} to create (encode) and  * read (decode) barcodes. For more info about  * the available barcodes have a look at:<br/><br/>  *<p/>  * https://github.com/zxing/zxing  */
end_comment

begin_class
annotation|@
name|Dataformat
argument_list|(
literal|"barcode"
argument_list|)
DECL|class|BarcodeDataFormat
specifier|public
class|class
name|BarcodeDataFormat
extends|extends
name|ServiceSupport
implements|implements
name|DataFormat
implements|,
name|DataFormatName
block|{
comment|/**      * The bean for the default parameters.      */
DECL|field|params
specifier|private
name|BarcodeParameters
name|params
decl_stmt|;
comment|/**      * The encoding hint map, used for writing a barcode.      */
DECL|field|writerHintMap
specifier|private
specifier|final
name|Map
argument_list|<
name|EncodeHintType
argument_list|,
name|Object
argument_list|>
name|writerHintMap
init|=
operator|new
name|EnumMap
argument_list|<>
argument_list|(
name|EncodeHintType
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * The decoding hint map, used for reading a barcode.      */
DECL|field|readerHintMap
specifier|private
specifier|final
name|Map
argument_list|<
name|DecodeHintType
argument_list|,
name|Object
argument_list|>
name|readerHintMap
init|=
operator|new
name|EnumMap
argument_list|<>
argument_list|(
name|DecodeHintType
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Create instance with default parameters.      */
DECL|method|BarcodeDataFormat ()
specifier|public
name|BarcodeDataFormat
parameter_list|()
block|{
name|this
operator|.
name|setDefaultParameters
argument_list|()
expr_stmt|;
name|this
operator|.
name|optimizeHints
argument_list|()
expr_stmt|;
block|}
comment|/**      * Create instance with custom {@link BarcodeFormat}. The other      * values are default.      *      * @param format the barcode format      */
DECL|method|BarcodeDataFormat (final BarcodeFormat format)
specifier|public
name|BarcodeDataFormat
parameter_list|(
specifier|final
name|BarcodeFormat
name|format
parameter_list|)
block|{
name|this
operator|.
name|setDefaultParameters
argument_list|()
expr_stmt|;
name|this
operator|.
name|params
operator|.
name|setFormat
argument_list|(
name|format
argument_list|)
expr_stmt|;
name|this
operator|.
name|optimizeHints
argument_list|()
expr_stmt|;
block|}
comment|/**      * Create instance with custom height and width. The other      * values are default.      *      * @param height the image height      * @param width  the image width      */
DECL|method|BarcodeDataFormat (final int width, final int height)
specifier|public
name|BarcodeDataFormat
parameter_list|(
specifier|final
name|int
name|width
parameter_list|,
specifier|final
name|int
name|height
parameter_list|)
block|{
name|this
operator|.
name|setDefaultParameters
argument_list|()
expr_stmt|;
name|this
operator|.
name|params
operator|.
name|setHeight
argument_list|(
name|height
argument_list|)
expr_stmt|;
name|this
operator|.
name|params
operator|.
name|setWidth
argument_list|(
name|width
argument_list|)
expr_stmt|;
name|this
operator|.
name|optimizeHints
argument_list|()
expr_stmt|;
block|}
comment|/**      * Create instance with custom {@link BarcodeImageType}. The other      * values are default.      *      * @param type the type (format) of the image. e.g. PNG      */
DECL|method|BarcodeDataFormat (final BarcodeImageType type)
specifier|public
name|BarcodeDataFormat
parameter_list|(
specifier|final
name|BarcodeImageType
name|type
parameter_list|)
block|{
name|this
operator|.
name|setDefaultParameters
argument_list|()
expr_stmt|;
name|this
operator|.
name|params
operator|.
name|setType
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|this
operator|.
name|optimizeHints
argument_list|()
expr_stmt|;
block|}
comment|/**      * Create instance with custom height, width and image type. The other      * values are default.      *      * @param height the image height      * @param width  the image width      * @param type   the type (format) of the image. e.g. PNG      * @param format the barcode format      */
DECL|method|BarcodeDataFormat (final int width, final int height, final BarcodeImageType type, final BarcodeFormat format)
specifier|public
name|BarcodeDataFormat
parameter_list|(
specifier|final
name|int
name|width
parameter_list|,
specifier|final
name|int
name|height
parameter_list|,
specifier|final
name|BarcodeImageType
name|type
parameter_list|,
specifier|final
name|BarcodeFormat
name|format
parameter_list|)
block|{
name|this
operator|.
name|setDefaultParameters
argument_list|()
expr_stmt|;
name|this
operator|.
name|params
operator|.
name|setHeight
argument_list|(
name|height
argument_list|)
expr_stmt|;
name|this
operator|.
name|params
operator|.
name|setWidth
argument_list|(
name|width
argument_list|)
expr_stmt|;
name|this
operator|.
name|params
operator|.
name|setType
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|this
operator|.
name|params
operator|.
name|setFormat
argument_list|(
name|format
argument_list|)
expr_stmt|;
name|this
operator|.
name|optimizeHints
argument_list|()
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
literal|"barcode"
return|;
block|}
comment|/**      * Marshall a {@link String} payload to a code image.      */
annotation|@
name|Override
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
name|this
operator|.
name|printImage
argument_list|(
name|exchange
argument_list|,
name|graph
argument_list|,
name|stream
argument_list|)
expr_stmt|;
block|}
comment|/**      * Unmarshall a code image to a {@link String} payload.      */
annotation|@
name|Override
DECL|method|unmarshal (final Exchange exchange, final InputStream stream)
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
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|this
operator|.
name|readImage
argument_list|(
name|exchange
argument_list|,
name|stream
argument_list|)
return|;
block|}
comment|/**      * Sets the default parameters.      */
DECL|method|setDefaultParameters ()
specifier|protected
specifier|final
name|void
name|setDefaultParameters
parameter_list|()
block|{
name|this
operator|.
name|params
operator|=
operator|new
name|BarcodeParameters
argument_list|()
expr_stmt|;
block|}
comment|/**      * Sets hints optimized for different barcode types.      */
DECL|method|optimizeHints ()
specifier|protected
specifier|final
name|void
name|optimizeHints
parameter_list|()
block|{
comment|// clear hints for re-optimization
name|this
operator|.
name|writerHintMap
operator|.
name|clear
argument_list|()
expr_stmt|;
name|this
operator|.
name|readerHintMap
operator|.
name|clear
argument_list|()
expr_stmt|;
comment|// writer hints
name|String
name|format
init|=
name|this
operator|.
name|params
operator|.
name|getFormat
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
comment|// only for QR code. AZTEC uses zxing's default error correction 33%.
if|if
condition|(
name|format
operator|.
name|equals
argument_list|(
name|BarcodeFormat
operator|.
name|QR_CODE
operator|.
name|toString
argument_list|()
argument_list|)
condition|)
block|{
name|this
operator|.
name|writerHintMap
operator|.
name|put
argument_list|(
name|EncodeHintType
operator|.
name|ERROR_CORRECTION
argument_list|,
name|ErrorCorrectionLevel
operator|.
name|H
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|format
operator|.
name|equals
argument_list|(
name|BarcodeFormat
operator|.
name|DATA_MATRIX
operator|.
name|toString
argument_list|()
argument_list|)
condition|)
block|{
name|this
operator|.
name|writerHintMap
operator|.
name|put
argument_list|(
name|EncodeHintType
operator|.
name|DATA_MATRIX_SHAPE
argument_list|,
name|SymbolShapeHint
operator|.
name|FORCE_SQUARE
argument_list|)
expr_stmt|;
block|}
comment|// reader hints
name|this
operator|.
name|readerHintMap
operator|.
name|put
argument_list|(
name|DecodeHintType
operator|.
name|TRY_HARDER
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Writes the image file to the output stream.      *      * @param graph    the object graph      * @param exchange the camel exchange      * @param stream   the output stream      */
DECL|method|printImage (final Exchange exchange, final Object graph, final OutputStream stream)
specifier|private
name|void
name|printImage
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
specifier|final
name|String
name|payload
init|=
name|ExchangeHelper
operator|.
name|convertToMandatoryType
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|graph
argument_list|)
decl_stmt|;
specifier|final
name|MultiFormatWriter
name|writer
init|=
operator|new
name|MultiFormatWriter
argument_list|()
decl_stmt|;
comment|// set values
specifier|final
name|String
name|type
init|=
name|this
operator|.
name|params
operator|.
name|getType
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
comment|// create code image
specifier|final
name|BitMatrix
name|matrix
init|=
name|writer
operator|.
name|encode
argument_list|(
name|payload
argument_list|,
name|this
operator|.
name|params
operator|.
name|getFormat
argument_list|()
argument_list|,
name|this
operator|.
name|params
operator|.
name|getWidth
argument_list|()
argument_list|,
name|this
operator|.
name|params
operator|.
name|getHeight
argument_list|()
argument_list|,
name|writerHintMap
argument_list|)
decl_stmt|;
comment|// write image back to stream
name|MatrixToImageWriter
operator|.
name|writeToStream
argument_list|(
name|matrix
argument_list|,
name|type
argument_list|,
name|stream
argument_list|)
expr_stmt|;
block|}
comment|/**      * Reads the message from a code.      */
DECL|method|readImage (final Exchange exchange, final InputStream stream)
specifier|private
name|String
name|readImage
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|InputStream
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|MultiFormatReader
name|reader
init|=
operator|new
name|MultiFormatReader
argument_list|()
decl_stmt|;
specifier|final
name|BufferedInputStream
name|in
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
name|BufferedInputStream
operator|.
name|class
argument_list|,
name|stream
argument_list|)
decl_stmt|;
specifier|final
name|BinaryBitmap
name|bitmap
init|=
operator|new
name|BinaryBitmap
argument_list|(
operator|new
name|HybridBinarizer
argument_list|(
operator|new
name|BufferedImageLuminanceSource
argument_list|(
name|ImageIO
operator|.
name|read
argument_list|(
name|in
argument_list|)
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|Result
name|result
init|=
name|reader
operator|.
name|decode
argument_list|(
name|bitmap
argument_list|,
name|readerHintMap
argument_list|)
decl_stmt|;
comment|// write the found barcode format into the header
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Barcode
operator|.
name|BARCODE_FORMAT
argument_list|,
name|result
operator|.
name|getBarcodeFormat
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|result
operator|.
name|getText
argument_list|()
return|;
block|}
comment|/**      * Adds a new hint value to writer (encode) hint map.      */
DECL|method|addToHintMap (final EncodeHintType hintType, final Object value)
specifier|public
specifier|final
name|void
name|addToHintMap
parameter_list|(
specifier|final
name|EncodeHintType
name|hintType
parameter_list|,
specifier|final
name|Object
name|value
parameter_list|)
block|{
name|this
operator|.
name|writerHintMap
operator|.
name|put
argument_list|(
name|hintType
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Added '%s' with value '%s' to writer hint map."
argument_list|,
name|hintType
operator|.
name|toString
argument_list|()
argument_list|,
name|value
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Adds a new hint value to reader (decode) hint map.      */
DECL|method|addToHintMap (final DecodeHintType hintType, final Object value)
specifier|public
specifier|final
name|void
name|addToHintMap
parameter_list|(
specifier|final
name|DecodeHintType
name|hintType
parameter_list|,
specifier|final
name|Object
name|value
parameter_list|)
block|{
name|this
operator|.
name|readerHintMap
operator|.
name|put
argument_list|(
name|hintType
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * Removes a hint from writer (encode) hint map.      */
DECL|method|removeFromHintMap (final EncodeHintType hintType)
specifier|public
specifier|final
name|void
name|removeFromHintMap
parameter_list|(
specifier|final
name|EncodeHintType
name|hintType
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|writerHintMap
operator|.
name|containsKey
argument_list|(
name|hintType
argument_list|)
condition|)
block|{
name|this
operator|.
name|writerHintMap
operator|.
name|remove
argument_list|(
name|hintType
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Removed '%s' from writer hint map."
argument_list|,
name|hintType
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|warn
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Could not find encode hint type '%s' in writer hint map."
argument_list|,
name|hintType
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Removes a hint from reader (decode) hint map.      */
DECL|method|removeFromHintMap (final DecodeHintType hintType)
specifier|public
specifier|final
name|void
name|removeFromHintMap
parameter_list|(
specifier|final
name|DecodeHintType
name|hintType
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|readerHintMap
operator|.
name|containsKey
argument_list|(
name|hintType
argument_list|)
condition|)
block|{
name|this
operator|.
name|readerHintMap
operator|.
name|remove
argument_list|(
name|hintType
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Removed '%s' from reader hint map."
argument_list|,
name|hintType
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|warn
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Could not find decode hint type '%s' in reader hint map."
argument_list|,
name|hintType
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * The (default) parameters.      */
DECL|method|getParams ()
specifier|public
specifier|final
name|BarcodeParameters
name|getParams
parameter_list|()
block|{
return|return
name|params
return|;
block|}
comment|/**      * The writer (encode) hint map.      */
DECL|method|getWriterHintMap ()
specifier|public
specifier|final
name|Map
argument_list|<
name|EncodeHintType
argument_list|,
name|Object
argument_list|>
name|getWriterHintMap
parameter_list|()
block|{
return|return
name|writerHintMap
return|;
block|}
comment|/**      * The reader (decode) hint map.      */
DECL|method|getReaderHintMap ()
specifier|public
specifier|final
name|Map
argument_list|<
name|DecodeHintType
argument_list|,
name|Object
argument_list|>
name|getReaderHintMap
parameter_list|()
block|{
return|return
name|readerHintMap
return|;
block|}
comment|// these set method is used for BarcodeDataFormat XML DSL
DECL|method|setBarcodeImageType (BarcodeImageType type)
specifier|public
name|void
name|setBarcodeImageType
parameter_list|(
name|BarcodeImageType
name|type
parameter_list|)
block|{
name|this
operator|.
name|params
operator|.
name|setType
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|this
operator|.
name|optimizeHints
argument_list|()
expr_stmt|;
block|}
DECL|method|setBarcodeFormat (BarcodeFormat format)
specifier|public
name|void
name|setBarcodeFormat
parameter_list|(
name|BarcodeFormat
name|format
parameter_list|)
block|{
name|this
operator|.
name|params
operator|.
name|setFormat
argument_list|(
name|format
argument_list|)
expr_stmt|;
name|this
operator|.
name|optimizeHints
argument_list|()
expr_stmt|;
block|}
DECL|method|setWidth (Integer width)
specifier|public
name|void
name|setWidth
parameter_list|(
name|Integer
name|width
parameter_list|)
block|{
name|this
operator|.
name|params
operator|.
name|setWidth
argument_list|(
name|width
argument_list|)
expr_stmt|;
block|}
DECL|method|setHeight (Integer height)
specifier|public
name|void
name|setHeight
parameter_list|(
name|Integer
name|height
parameter_list|)
block|{
name|this
operator|.
name|params
operator|.
name|setHeight
argument_list|(
name|height
argument_list|)
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

