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
name|awt
operator|.
name|image
operator|.
name|BufferedImage
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
name|FileInputStream
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
name|javax
operator|.
name|imageio
operator|.
name|ImageIO
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|imageio
operator|.
name|ImageReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|imageio
operator|.
name|stream
operator|.
name|ImageInputStream
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
name|Reader
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
name|ReaderException
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
name|common
operator|.
name|HybridBinarizer
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
name|EndpointInject
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
name|mock
operator|.
name|MockEndpoint
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
name|FileUtil
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

begin_class
DECL|class|BarcodeTestBase
specifier|public
class|class
name|BarcodeTestBase
extends|extends
name|CamelTestSupport
block|{
DECL|field|MSG
specifier|protected
specifier|static
specifier|final
name|String
name|MSG
init|=
literal|"This is a testmessage!"
decl_stmt|;
DECL|field|PATH
specifier|protected
specifier|static
specifier|final
name|String
name|PATH
init|=
literal|"target/out"
decl_stmt|;
DECL|field|FILE_ENDPOINT
specifier|protected
specifier|static
specifier|final
name|String
name|FILE_ENDPOINT
init|=
literal|"file:"
operator|+
name|PATH
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:out"
argument_list|)
DECL|field|out
name|MockEndpoint
name|out
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
literal|"mock:image"
argument_list|)
DECL|field|image
name|MockEndpoint
name|image
decl_stmt|;
DECL|method|checkImage (MockEndpoint mock, int height, int width, String type, BarcodeFormat format)
specifier|protected
name|void
name|checkImage
parameter_list|(
name|MockEndpoint
name|mock
parameter_list|,
name|int
name|height
parameter_list|,
name|int
name|width
parameter_list|,
name|String
name|type
parameter_list|,
name|BarcodeFormat
name|format
parameter_list|)
throws|throws
name|IOException
block|{
name|Exchange
name|ex
init|=
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|File
name|in
init|=
name|ex
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|File
operator|.
name|class
argument_list|)
decl_stmt|;
name|FileInputStream
name|fis
init|=
operator|new
name|FileInputStream
argument_list|(
name|in
argument_list|)
decl_stmt|;
comment|// check image
name|BufferedImage
name|i
init|=
name|ImageIO
operator|.
name|read
argument_list|(
name|fis
argument_list|)
decl_stmt|;
name|IOHelper
operator|.
name|close
argument_list|(
name|fis
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|height
operator|>=
name|i
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|width
operator|>=
name|i
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|checkType
argument_list|(
name|in
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|this
operator|.
name|checkFormat
argument_list|(
name|in
argument_list|,
name|format
argument_list|)
expr_stmt|;
name|FileUtil
operator|.
name|deleteFile
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
DECL|method|checkImage (MockEndpoint mock, String type, BarcodeFormat format)
specifier|protected
name|void
name|checkImage
parameter_list|(
name|MockEndpoint
name|mock
parameter_list|,
name|String
name|type
parameter_list|,
name|BarcodeFormat
name|format
parameter_list|)
throws|throws
name|IOException
block|{
name|Exchange
name|ex
init|=
name|mock
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|File
name|in
init|=
name|ex
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|File
operator|.
name|class
argument_list|)
decl_stmt|;
name|this
operator|.
name|checkType
argument_list|(
name|in
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|this
operator|.
name|checkFormat
argument_list|(
name|in
argument_list|,
name|format
argument_list|)
expr_stmt|;
name|FileUtil
operator|.
name|deleteFile
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
DECL|method|checkFormat (File file, BarcodeFormat format)
specifier|private
name|void
name|checkFormat
parameter_list|(
name|File
name|file
parameter_list|,
name|BarcodeFormat
name|format
parameter_list|)
throws|throws
name|IOException
block|{
name|Reader
name|reader
init|=
operator|new
name|MultiFormatReader
argument_list|()
decl_stmt|;
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
name|file
argument_list|)
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|Result
name|result
decl_stmt|;
try|try
block|{
name|result
operator|=
name|reader
operator|.
name|decode
argument_list|(
name|bitmap
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ReaderException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
name|assertEquals
argument_list|(
name|format
argument_list|,
name|result
operator|.
name|getBarcodeFormat
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|checkType (File file, String type)
specifier|private
name|void
name|checkType
parameter_list|(
name|File
name|file
parameter_list|,
name|String
name|type
parameter_list|)
throws|throws
name|IOException
block|{
name|ImageInputStream
name|iis
init|=
name|ImageIO
operator|.
name|createImageInputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|ImageReader
name|reader
init|=
name|ImageIO
operator|.
name|getImageReaders
argument_list|(
name|iis
argument_list|)
operator|.
name|next
argument_list|()
decl_stmt|;
name|IOHelper
operator|.
name|close
argument_list|(
name|iis
argument_list|)
expr_stmt|;
name|String
name|format
init|=
name|reader
operator|.
name|getFormatName
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|type
argument_list|,
name|format
operator|.
name|toUpperCase
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

