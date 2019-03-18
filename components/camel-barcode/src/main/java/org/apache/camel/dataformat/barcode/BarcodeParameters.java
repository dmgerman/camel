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
name|com
operator|.
name|google
operator|.
name|zxing
operator|.
name|BarcodeFormat
import|;
end_import

begin_comment
comment|/**  * All configuration parameters for the code component.  */
end_comment

begin_class
DECL|class|BarcodeParameters
specifier|public
class|class
name|BarcodeParameters
block|{
comment|/**      * Default image type: PNG      */
DECL|field|IMAGE_TYPE
specifier|public
specifier|static
specifier|final
name|BarcodeImageType
name|IMAGE_TYPE
init|=
name|BarcodeImageType
operator|.
name|PNG
decl_stmt|;
comment|/**      * Default width: 100px      */
DECL|field|WIDTH
specifier|public
specifier|static
specifier|final
name|int
name|WIDTH
init|=
literal|100
decl_stmt|;
comment|/**      * Default height: 100px      */
DECL|field|HEIGHT
specifier|public
specifier|static
specifier|final
name|int
name|HEIGHT
init|=
literal|100
decl_stmt|;
comment|/**      * Default barcode format: QR-CODE      */
DECL|field|FORMAT
specifier|public
specifier|static
specifier|final
name|BarcodeFormat
name|FORMAT
init|=
name|BarcodeFormat
operator|.
name|QR_CODE
decl_stmt|;
comment|/**      * The Image Type.       */
DECL|field|type
specifier|private
name|BarcodeImageType
name|type
init|=
name|IMAGE_TYPE
decl_stmt|;
comment|/**      * The width of the image.      */
DECL|field|width
specifier|private
name|Integer
name|width
init|=
name|WIDTH
decl_stmt|;
comment|/**      * The height of the image.      */
DECL|field|height
specifier|private
name|Integer
name|height
init|=
name|HEIGHT
decl_stmt|;
comment|/**      * The barcode format (e.g. QR-Code, DataMatrix,...).      */
DECL|field|format
specifier|private
name|BarcodeFormat
name|format
init|=
name|FORMAT
decl_stmt|;
comment|/**      * Default Constructor (creates a bean with default parameters).      *       *<ul>      *<li>image type: PNG</li>      *<li>image width: 100px</li>      *<li>image heigth: 100px</li>      *<li>format: QR-Code</li>      *</ul>      */
DECL|method|BarcodeParameters ()
specifier|public
name|BarcodeParameters
parameter_list|()
block|{              }
comment|/**      * Constructor with parameters.      * @param type      * @param width      * @param height      * @param format      */
DECL|method|BarcodeParameters (final BarcodeImageType type, final int width, final int height, final BarcodeFormat format)
specifier|public
name|BarcodeParameters
parameter_list|(
specifier|final
name|BarcodeImageType
name|type
parameter_list|,
specifier|final
name|int
name|width
parameter_list|,
specifier|final
name|int
name|height
parameter_list|,
specifier|final
name|BarcodeFormat
name|format
parameter_list|)
block|{
name|this
operator|.
name|height
operator|=
name|height
expr_stmt|;
name|this
operator|.
name|width
operator|=
name|width
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|format
operator|=
name|format
expr_stmt|;
block|}
DECL|method|getType ()
specifier|public
name|BarcodeImageType
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
DECL|method|setType (BarcodeImageType type)
specifier|public
name|void
name|setType
parameter_list|(
name|BarcodeImageType
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
DECL|method|getWidth ()
specifier|public
name|Integer
name|getWidth
parameter_list|()
block|{
return|return
name|width
return|;
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
name|width
operator|=
name|width
expr_stmt|;
block|}
DECL|method|getHeight ()
specifier|public
name|Integer
name|getHeight
parameter_list|()
block|{
return|return
name|height
return|;
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
name|height
operator|=
name|height
expr_stmt|;
block|}
DECL|method|getFormat ()
specifier|public
name|BarcodeFormat
name|getFormat
parameter_list|()
block|{
return|return
name|format
return|;
block|}
DECL|method|setFormat (BarcodeFormat format)
specifier|public
name|void
name|setFormat
parameter_list|(
name|BarcodeFormat
name|format
parameter_list|)
block|{
name|this
operator|.
name|format
operator|=
name|format
expr_stmt|;
block|}
block|}
end_class

end_unit

