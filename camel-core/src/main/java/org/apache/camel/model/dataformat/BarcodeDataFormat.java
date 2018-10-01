begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.dataformat
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|dataformat
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
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
name|CamelContext
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
name|model
operator|.
name|DataFormatDefinition
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
name|Metadata
import|;
end_import

begin_comment
comment|/**  * The Barcode data format is used for creating barccode images (such as QR-Code)  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|firstVersion
operator|=
literal|"2.14.0"
argument_list|,
name|label
operator|=
literal|"dataformat,transformation"
argument_list|,
name|title
operator|=
literal|"Barcode"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"barcode"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|BarcodeDataFormat
specifier|public
class|class
name|BarcodeDataFormat
extends|extends
name|DataFormatDefinition
block|{
annotation|@
name|XmlAttribute
DECL|field|width
specifier|private
name|Integer
name|width
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|height
specifier|private
name|Integer
name|height
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|imageType
specifier|private
name|String
name|imageType
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|barcodeFormat
specifier|private
name|String
name|barcodeFormat
decl_stmt|;
DECL|method|BarcodeDataFormat ()
specifier|public
name|BarcodeDataFormat
parameter_list|()
block|{
name|super
argument_list|(
literal|"barcode"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|configureDataFormat (DataFormat dataFormat, CamelContext camelContext)
specifier|protected
name|void
name|configureDataFormat
parameter_list|(
name|DataFormat
name|dataFormat
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
block|{
if|if
condition|(
name|width
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"width"
argument_list|,
name|width
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|height
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"height"
argument_list|,
name|height
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|imageType
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"barcodeImageType"
argument_list|,
name|imageType
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|barcodeFormat
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"barcodeFormat"
argument_list|,
name|barcodeFormat
argument_list|)
expr_stmt|;
block|}
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
comment|/**      * Width of the barcode      */
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
comment|/**      * Height of the barcode      */
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
DECL|method|getImageType ()
specifier|public
name|String
name|getImageType
parameter_list|()
block|{
return|return
name|imageType
return|;
block|}
comment|/**      * Image type of the barcode such as png      */
DECL|method|setImageType (String imageType)
specifier|public
name|void
name|setImageType
parameter_list|(
name|String
name|imageType
parameter_list|)
block|{
name|this
operator|.
name|imageType
operator|=
name|imageType
expr_stmt|;
block|}
DECL|method|getBarcodeFormat ()
specifier|public
name|String
name|getBarcodeFormat
parameter_list|()
block|{
return|return
name|barcodeFormat
return|;
block|}
comment|/**      * Barcode format such as QR-Code      */
DECL|method|setBarcodeFormat (String barcodeFormat)
specifier|public
name|void
name|setBarcodeFormat
parameter_list|(
name|String
name|barcodeFormat
parameter_list|)
block|{
name|this
operator|.
name|barcodeFormat
operator|=
name|barcodeFormat
expr_stmt|;
block|}
block|}
end_class

end_unit

