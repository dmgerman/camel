begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.reifier.dataformat
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|reifier
operator|.
name|dataformat
package|;
end_package

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
name|model
operator|.
name|dataformat
operator|.
name|BarcodeDataFormat
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

begin_class
DECL|class|BarcodeDataFormatReifier
specifier|public
class|class
name|BarcodeDataFormatReifier
extends|extends
name|DataFormatReifier
argument_list|<
name|BarcodeDataFormat
argument_list|>
block|{
DECL|method|BarcodeDataFormatReifier (DataFormatDefinition definition)
specifier|public
name|BarcodeDataFormatReifier
parameter_list|(
name|DataFormatDefinition
name|definition
parameter_list|)
block|{
name|super
argument_list|(
operator|(
name|BarcodeDataFormat
operator|)
name|definition
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
name|definition
operator|.
name|getWidth
argument_list|()
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
name|definition
operator|.
name|getWidth
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getHeight
argument_list|()
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
name|definition
operator|.
name|getHeight
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getImageType
argument_list|()
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
name|definition
operator|.
name|getImageType
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getBarcodeFormat
argument_list|()
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
name|definition
operator|.
name|getBarcodeFormat
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit
