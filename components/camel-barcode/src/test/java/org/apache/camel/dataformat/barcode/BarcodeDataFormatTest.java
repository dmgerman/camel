begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|util
operator|.
name|Map
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
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_comment
comment|/**  * This class tests all Camel independend test cases   * for {@link BarcodeDataFormat}.  */
end_comment

begin_class
DECL|class|BarcodeDataFormatTest
specifier|public
class|class
name|BarcodeDataFormatTest
block|{
comment|/**      * Test default constructor.      */
annotation|@
name|Test
DECL|method|testDefaultConstructor ()
specifier|public
specifier|final
name|void
name|testDefaultConstructor
parameter_list|()
block|{
name|BarcodeDataFormat
name|barcodeDataFormat
init|=
operator|new
name|BarcodeDataFormat
argument_list|()
decl_stmt|;
name|this
operator|.
name|checkParams
argument_list|(
name|BarcodeParameters
operator|.
name|IMAGE_TYPE
argument_list|,
name|BarcodeParameters
operator|.
name|WIDTH
argument_list|,
name|BarcodeParameters
operator|.
name|HEIGHT
argument_list|,
name|BarcodeParameters
operator|.
name|FORMAT
argument_list|,
name|barcodeDataFormat
operator|.
name|getParams
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test constructor with barcode format.      */
annotation|@
name|Test
DECL|method|testConstructorWithBarcodeFormat ()
specifier|public
specifier|final
name|void
name|testConstructorWithBarcodeFormat
parameter_list|()
block|{
name|BarcodeDataFormat
name|barcodeDataFormat
init|=
operator|new
name|BarcodeDataFormat
argument_list|(
name|BarcodeFormat
operator|.
name|AZTEC
argument_list|)
decl_stmt|;
name|this
operator|.
name|checkParams
argument_list|(
name|BarcodeParameters
operator|.
name|IMAGE_TYPE
argument_list|,
name|BarcodeParameters
operator|.
name|WIDTH
argument_list|,
name|BarcodeParameters
operator|.
name|HEIGHT
argument_list|,
name|BarcodeFormat
operator|.
name|AZTEC
argument_list|,
name|barcodeDataFormat
operator|.
name|getParams
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test constructor with size.      */
annotation|@
name|Test
DECL|method|testConstructorWithSize ()
specifier|public
specifier|final
name|void
name|testConstructorWithSize
parameter_list|()
block|{
name|BarcodeDataFormat
name|barcodeDataFormat
init|=
operator|new
name|BarcodeDataFormat
argument_list|(
literal|200
argument_list|,
literal|250
argument_list|)
decl_stmt|;
name|this
operator|.
name|checkParams
argument_list|(
name|BarcodeParameters
operator|.
name|IMAGE_TYPE
argument_list|,
literal|200
argument_list|,
literal|250
argument_list|,
name|BarcodeParameters
operator|.
name|FORMAT
argument_list|,
name|barcodeDataFormat
operator|.
name|getParams
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test constructor with image type.      */
annotation|@
name|Test
DECL|method|testConstructorWithImageType ()
specifier|public
specifier|final
name|void
name|testConstructorWithImageType
parameter_list|()
block|{
name|BarcodeDataFormat
name|barcodeDataFormat
init|=
operator|new
name|BarcodeDataFormat
argument_list|(
name|BarcodeImageType
operator|.
name|JPG
argument_list|)
decl_stmt|;
name|this
operator|.
name|checkParams
argument_list|(
name|BarcodeImageType
operator|.
name|JPG
argument_list|,
name|BarcodeParameters
operator|.
name|WIDTH
argument_list|,
name|BarcodeParameters
operator|.
name|HEIGHT
argument_list|,
name|BarcodeParameters
operator|.
name|FORMAT
argument_list|,
name|barcodeDataFormat
operator|.
name|getParams
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test constructor with all.      */
annotation|@
name|Test
DECL|method|testConstructorWithAll ()
specifier|public
specifier|final
name|void
name|testConstructorWithAll
parameter_list|()
block|{
name|BarcodeDataFormat
name|barcodeDataFormat
init|=
operator|new
name|BarcodeDataFormat
argument_list|(
literal|200
argument_list|,
literal|250
argument_list|,
name|BarcodeImageType
operator|.
name|JPG
argument_list|,
name|BarcodeFormat
operator|.
name|AZTEC
argument_list|)
decl_stmt|;
name|this
operator|.
name|checkParams
argument_list|(
name|BarcodeImageType
operator|.
name|JPG
argument_list|,
literal|200
argument_list|,
literal|250
argument_list|,
name|BarcodeFormat
operator|.
name|AZTEC
argument_list|,
name|barcodeDataFormat
operator|.
name|getParams
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test of optimizeHints method, of class BarcodeDataFormat.      */
annotation|@
name|Test
DECL|method|testOptimizeHints ()
specifier|public
specifier|final
name|void
name|testOptimizeHints
parameter_list|()
block|{
name|BarcodeDataFormat
name|instance
init|=
operator|new
name|BarcodeDataFormat
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|instance
operator|.
name|getWriterHintMap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|EncodeHintType
operator|.
name|ERROR_CORRECTION
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|instance
operator|.
name|getReaderHintMap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|DecodeHintType
operator|.
name|TRY_HARDER
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test optimized hints for data matrix.      */
annotation|@
name|Test
DECL|method|testOptimizieHintsForDataMatrix ()
specifier|public
specifier|final
name|void
name|testOptimizieHintsForDataMatrix
parameter_list|()
block|{
name|BarcodeDataFormat
name|instance
init|=
operator|new
name|BarcodeDataFormat
argument_list|(
name|BarcodeFormat
operator|.
name|DATA_MATRIX
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"data matrix shape hint incorrect."
argument_list|,
name|instance
operator|.
name|getWriterHintMap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|EncodeHintType
operator|.
name|DATA_MATRIX_SHAPE
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"try harder hint incorrect."
argument_list|,
name|instance
operator|.
name|getReaderHintMap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|DecodeHintType
operator|.
name|TRY_HARDER
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test re-optimize hints.      */
annotation|@
name|Test
DECL|method|testReOptimizeHints ()
specifier|public
specifier|final
name|void
name|testReOptimizeHints
parameter_list|()
block|{
comment|// DATA-MATRIX
name|BarcodeDataFormat
name|instance
init|=
operator|new
name|BarcodeDataFormat
argument_list|(
name|BarcodeFormat
operator|.
name|DATA_MATRIX
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|instance
operator|.
name|getWriterHintMap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|EncodeHintType
operator|.
name|DATA_MATRIX_SHAPE
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|instance
operator|.
name|getReaderHintMap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|DecodeHintType
operator|.
name|TRY_HARDER
argument_list|)
argument_list|)
expr_stmt|;
comment|// -> QR-CODE
name|instance
operator|.
name|setBarcodeFormat
argument_list|(
name|BarcodeFormat
operator|.
name|QR_CODE
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|instance
operator|.
name|getWriterHintMap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|EncodeHintType
operator|.
name|DATA_MATRIX_SHAPE
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|instance
operator|.
name|getReaderHintMap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|DecodeHintType
operator|.
name|TRY_HARDER
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test of addToHintMap method, of class BarcodeDataFormat.      */
annotation|@
name|Test
DECL|method|testAddToHintMapEncodeHintTypeObject ()
specifier|public
specifier|final
name|void
name|testAddToHintMapEncodeHintTypeObject
parameter_list|()
block|{
name|EncodeHintType
name|hintType
init|=
name|EncodeHintType
operator|.
name|MARGIN
decl_stmt|;
name|Object
name|value
init|=
literal|10
decl_stmt|;
name|BarcodeDataFormat
name|instance
init|=
operator|new
name|BarcodeDataFormat
argument_list|()
decl_stmt|;
name|instance
operator|.
name|addToHintMap
argument_list|(
name|hintType
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|instance
operator|.
name|getWriterHintMap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|hintType
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|instance
operator|.
name|getWriterHintMap
argument_list|()
operator|.
name|get
argument_list|(
name|hintType
argument_list|)
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test of addToHintMap method, of class BarcodeDataFormat.      */
annotation|@
name|Test
DECL|method|testAddToHintMapDecodeHintTypeObject ()
specifier|public
specifier|final
name|void
name|testAddToHintMapDecodeHintTypeObject
parameter_list|()
block|{
name|DecodeHintType
name|hintType
init|=
name|DecodeHintType
operator|.
name|CHARACTER_SET
decl_stmt|;
name|Object
name|value
init|=
literal|"UTF-8"
decl_stmt|;
name|BarcodeDataFormat
name|instance
init|=
operator|new
name|BarcodeDataFormat
argument_list|()
decl_stmt|;
name|instance
operator|.
name|addToHintMap
argument_list|(
name|hintType
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|instance
operator|.
name|getReaderHintMap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|hintType
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|instance
operator|.
name|getReaderHintMap
argument_list|()
operator|.
name|get
argument_list|(
name|hintType
argument_list|)
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test of removeFromHintMap method, of class BarcodeDataFormat.      */
annotation|@
name|Test
DECL|method|testRemoveFromHintMapEncodeHintType ()
specifier|public
specifier|final
name|void
name|testRemoveFromHintMapEncodeHintType
parameter_list|()
block|{
name|EncodeHintType
name|hintType
init|=
name|EncodeHintType
operator|.
name|ERROR_CORRECTION
decl_stmt|;
name|BarcodeDataFormat
name|instance
init|=
operator|new
name|BarcodeDataFormat
argument_list|()
decl_stmt|;
name|instance
operator|.
name|removeFromHintMap
argument_list|(
name|hintType
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|instance
operator|.
name|getWriterHintMap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|hintType
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test of removeFromHintMap method, of class BarcodeDataFormat.      */
annotation|@
name|Test
DECL|method|testRemoveFromHintMapDecodeHintType ()
specifier|public
specifier|final
name|void
name|testRemoveFromHintMapDecodeHintType
parameter_list|()
block|{
name|DecodeHintType
name|hintType
init|=
name|DecodeHintType
operator|.
name|TRY_HARDER
decl_stmt|;
name|BarcodeDataFormat
name|instance
init|=
operator|new
name|BarcodeDataFormat
argument_list|()
decl_stmt|;
name|instance
operator|.
name|removeFromHintMap
argument_list|(
name|hintType
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|instance
operator|.
name|getReaderHintMap
argument_list|()
operator|.
name|containsKey
argument_list|(
name|hintType
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test of getParams method, of class BarcodeDataFormat.      */
annotation|@
name|Test
DECL|method|testGetParams ()
specifier|public
specifier|final
name|void
name|testGetParams
parameter_list|()
block|{
name|BarcodeDataFormat
name|instance
init|=
operator|new
name|BarcodeDataFormat
argument_list|()
decl_stmt|;
name|BarcodeParameters
name|result
init|=
name|instance
operator|.
name|getParams
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test of getWriterHintMap method, of class BarcodeDataFormat.      */
annotation|@
name|Test
DECL|method|testGetWriterHintMap ()
specifier|public
specifier|final
name|void
name|testGetWriterHintMap
parameter_list|()
block|{
name|BarcodeDataFormat
name|instance
init|=
operator|new
name|BarcodeDataFormat
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|EncodeHintType
argument_list|,
name|Object
argument_list|>
name|result
init|=
name|instance
operator|.
name|getWriterHintMap
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test of getReaderHintMap method, of class BarcodeDataFormat.      */
annotation|@
name|Test
DECL|method|testGetReaderHintMap ()
specifier|public
specifier|final
name|void
name|testGetReaderHintMap
parameter_list|()
block|{
name|BarcodeDataFormat
name|instance
init|=
operator|new
name|BarcodeDataFormat
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|DecodeHintType
argument_list|,
name|Object
argument_list|>
name|result
init|=
name|instance
operator|.
name|getReaderHintMap
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
comment|/**      * Helper to check the saved parameters.      *       * @param imageType      * @param width      * @param height      * @param encoding      * @param format      * @param params       */
DECL|method|checkParams (BarcodeImageType imageType, int width, int height , BarcodeFormat format, BarcodeParameters params)
specifier|private
name|void
name|checkParams
parameter_list|(
name|BarcodeImageType
name|imageType
parameter_list|,
name|int
name|width
parameter_list|,
name|int
name|height
parameter_list|,
name|BarcodeFormat
name|format
parameter_list|,
name|BarcodeParameters
name|params
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|params
operator|.
name|getType
argument_list|()
argument_list|,
name|imageType
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|params
operator|.
name|getWidth
argument_list|()
operator|==
name|width
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|params
operator|.
name|getHeight
argument_list|()
operator|==
name|height
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|params
operator|.
name|getFormat
argument_list|()
argument_list|,
name|format
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

