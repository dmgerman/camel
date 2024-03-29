begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.pdf
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|pdf
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|UriParam
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
name|UriParams
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
name|UriPath
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|common
operator|.
name|PDRectangle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|font
operator|.
name|PDFont
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pdfbox
operator|.
name|pdmodel
operator|.
name|font
operator|.
name|PDType1Font
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
name|component
operator|.
name|pdf
operator|.
name|PdfPageSizeConstant
operator|.
name|PAGE_SIZE_A0
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
name|component
operator|.
name|pdf
operator|.
name|PdfPageSizeConstant
operator|.
name|PAGE_SIZE_A1
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
name|component
operator|.
name|pdf
operator|.
name|PdfPageSizeConstant
operator|.
name|PAGE_SIZE_A2
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
name|component
operator|.
name|pdf
operator|.
name|PdfPageSizeConstant
operator|.
name|PAGE_SIZE_A3
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
name|component
operator|.
name|pdf
operator|.
name|PdfPageSizeConstant
operator|.
name|PAGE_SIZE_A4
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
name|component
operator|.
name|pdf
operator|.
name|PdfPageSizeConstant
operator|.
name|PAGE_SIZE_A5
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
name|component
operator|.
name|pdf
operator|.
name|PdfPageSizeConstant
operator|.
name|PAGE_SIZE_A6
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
name|component
operator|.
name|pdf
operator|.
name|PdfPageSizeConstant
operator|.
name|PAGE_SIZE_LETTER
import|;
end_import

begin_comment
comment|/**  * Handles pdf component configuration values.  */
end_comment

begin_class
annotation|@
name|UriParams
DECL|class|PdfConfiguration
specifier|public
class|class
name|PdfConfiguration
block|{
DECL|field|PAGE_MAP
specifier|private
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|PDRectangle
argument_list|>
name|PAGE_MAP
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
static|static
block|{
name|PAGE_MAP
operator|.
name|put
argument_list|(
name|PAGE_SIZE_A0
argument_list|,
name|PDRectangle
operator|.
name|A0
argument_list|)
expr_stmt|;
name|PAGE_MAP
operator|.
name|put
argument_list|(
name|PAGE_SIZE_A1
argument_list|,
name|PDRectangle
operator|.
name|A1
argument_list|)
expr_stmt|;
name|PAGE_MAP
operator|.
name|put
argument_list|(
name|PAGE_SIZE_A2
argument_list|,
name|PDRectangle
operator|.
name|A2
argument_list|)
expr_stmt|;
name|PAGE_MAP
operator|.
name|put
argument_list|(
name|PAGE_SIZE_A3
argument_list|,
name|PDRectangle
operator|.
name|A3
argument_list|)
expr_stmt|;
name|PAGE_MAP
operator|.
name|put
argument_list|(
name|PAGE_SIZE_A4
argument_list|,
name|PDRectangle
operator|.
name|A4
argument_list|)
expr_stmt|;
name|PAGE_MAP
operator|.
name|put
argument_list|(
name|PAGE_SIZE_A5
argument_list|,
name|PDRectangle
operator|.
name|A5
argument_list|)
expr_stmt|;
name|PAGE_MAP
operator|.
name|put
argument_list|(
name|PAGE_SIZE_A6
argument_list|,
name|PDRectangle
operator|.
name|A6
argument_list|)
expr_stmt|;
name|PAGE_MAP
operator|.
name|put
argument_list|(
name|PAGE_SIZE_LETTER
argument_list|,
name|PDRectangle
operator|.
name|LETTER
argument_list|)
expr_stmt|;
block|}
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"Operation type"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|operation
specifier|private
name|PdfOperation
name|operation
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"20"
argument_list|)
DECL|field|marginTop
specifier|private
name|int
name|marginTop
init|=
literal|20
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"20"
argument_list|)
DECL|field|marginBottom
specifier|private
name|int
name|marginBottom
init|=
literal|20
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"20"
argument_list|)
DECL|field|marginLeft
specifier|private
name|int
name|marginLeft
init|=
literal|20
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"40"
argument_list|)
DECL|field|marginRight
specifier|private
name|int
name|marginRight
init|=
literal|40
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"14"
argument_list|)
DECL|field|fontSize
specifier|private
name|float
name|fontSize
init|=
literal|14
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"A4"
argument_list|,
name|enums
operator|=
literal|"LETTER,LEGAL,A0,A1,A2,A3,A4,A5,A6"
argument_list|)
DECL|field|pageSize
specifier|private
name|PDRectangle
name|pageSize
init|=
name|PDRectangle
operator|.
name|A4
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"Helvetica"
argument_list|)
DECL|field|font
specifier|private
name|PDFont
name|font
init|=
name|PDType1Font
operator|.
name|HELVETICA
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"lineTermination"
argument_list|)
DECL|field|textProcessingFactory
specifier|private
name|TextProcessingFactory
name|textProcessingFactory
init|=
name|TextProcessingFactory
operator|.
name|lineTermination
decl_stmt|;
DECL|method|getOperation ()
specifier|public
name|PdfOperation
name|getOperation
parameter_list|()
block|{
return|return
name|operation
return|;
block|}
DECL|method|setOperation (String operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|PdfOperation
operator|.
name|valueOf
argument_list|(
name|operation
argument_list|)
expr_stmt|;
block|}
DECL|method|setOperation (PdfOperation operation)
specifier|public
name|void
name|setOperation
parameter_list|(
name|PdfOperation
name|operation
parameter_list|)
block|{
name|this
operator|.
name|operation
operator|=
name|operation
expr_stmt|;
block|}
DECL|method|getMarginTop ()
specifier|public
name|int
name|getMarginTop
parameter_list|()
block|{
return|return
name|marginTop
return|;
block|}
comment|/**      * Margin top in pixels      */
DECL|method|setMarginTop (int marginTop)
specifier|public
name|void
name|setMarginTop
parameter_list|(
name|int
name|marginTop
parameter_list|)
block|{
name|this
operator|.
name|marginTop
operator|=
name|marginTop
expr_stmt|;
block|}
DECL|method|getMarginBottom ()
specifier|public
name|int
name|getMarginBottom
parameter_list|()
block|{
return|return
name|marginBottom
return|;
block|}
comment|/**      * Margin bottom in pixels      */
DECL|method|setMarginBottom (int marginBottom)
specifier|public
name|void
name|setMarginBottom
parameter_list|(
name|int
name|marginBottom
parameter_list|)
block|{
name|this
operator|.
name|marginBottom
operator|=
name|marginBottom
expr_stmt|;
block|}
DECL|method|getMarginLeft ()
specifier|public
name|int
name|getMarginLeft
parameter_list|()
block|{
return|return
name|marginLeft
return|;
block|}
comment|/**      * Margin left in pixels      */
DECL|method|setMarginLeft (int marginLeft)
specifier|public
name|void
name|setMarginLeft
parameter_list|(
name|int
name|marginLeft
parameter_list|)
block|{
name|this
operator|.
name|marginLeft
operator|=
name|marginLeft
expr_stmt|;
block|}
DECL|method|getMarginRight ()
specifier|public
name|int
name|getMarginRight
parameter_list|()
block|{
return|return
name|marginRight
return|;
block|}
comment|/**      * Margin right in pixels      */
DECL|method|setMarginRight (int marginRight)
specifier|public
name|void
name|setMarginRight
parameter_list|(
name|int
name|marginRight
parameter_list|)
block|{
name|this
operator|.
name|marginRight
operator|=
name|marginRight
expr_stmt|;
block|}
DECL|method|getFontSize ()
specifier|public
name|float
name|getFontSize
parameter_list|()
block|{
return|return
name|fontSize
return|;
block|}
comment|/**      * Font size in pixels      */
DECL|method|setFontSize (float fontSize)
specifier|public
name|void
name|setFontSize
parameter_list|(
name|float
name|fontSize
parameter_list|)
block|{
name|this
operator|.
name|fontSize
operator|=
name|fontSize
expr_stmt|;
block|}
DECL|method|getPageSize ()
specifier|public
name|PDRectangle
name|getPageSize
parameter_list|()
block|{
return|return
name|pageSize
return|;
block|}
comment|/**      * Page size      */
DECL|method|setPageSize (PDRectangle pageSize)
specifier|public
name|void
name|setPageSize
parameter_list|(
name|PDRectangle
name|pageSize
parameter_list|)
block|{
name|this
operator|.
name|pageSize
operator|=
name|pageSize
expr_stmt|;
block|}
DECL|method|setPageSize (String pageSize)
specifier|public
name|void
name|setPageSize
parameter_list|(
name|String
name|pageSize
parameter_list|)
block|{
name|setPageSize
argument_list|(
name|PAGE_MAP
operator|.
name|get
argument_list|(
name|pageSize
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getFont ()
specifier|public
name|PDFont
name|getFont
parameter_list|()
block|{
return|return
name|font
return|;
block|}
comment|/**      * Font      */
DECL|method|setFont (PDFont font)
specifier|public
name|void
name|setFont
parameter_list|(
name|PDFont
name|font
parameter_list|)
block|{
name|this
operator|.
name|font
operator|=
name|font
expr_stmt|;
block|}
DECL|method|setFont (String font)
specifier|public
name|void
name|setFont
parameter_list|(
name|String
name|font
parameter_list|)
block|{
name|setFont
argument_list|(
name|Standard14Fonts
operator|.
name|getByName
argument_list|(
name|font
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|getTextProcessingFactory ()
specifier|public
name|TextProcessingFactory
name|getTextProcessingFactory
parameter_list|()
block|{
return|return
name|textProcessingFactory
return|;
block|}
comment|/**      * Text processing to use.      *<ul>      *<li>autoFormatting: Text is getting sliced by words, then max amount of words that fits in the line will      *   be written into pdf document. With this strategy all words that doesn't fit in the line will be moved to the new line.</li>      *<li>lineTermination: Builds set of classes for line-termination writing strategy. Text getting sliced by line termination symbol      *   and then it will be written regardless it fits in the line or not.</li>      *</ul>      */
DECL|method|setTextProcessingFactory (TextProcessingFactory textProcessingFactory)
specifier|public
name|void
name|setTextProcessingFactory
parameter_list|(
name|TextProcessingFactory
name|textProcessingFactory
parameter_list|)
block|{
name|this
operator|.
name|textProcessingFactory
operator|=
name|textProcessingFactory
expr_stmt|;
block|}
DECL|method|setTextProcessingFactory (String textProcessingFactory)
specifier|public
name|void
name|setTextProcessingFactory
parameter_list|(
name|String
name|textProcessingFactory
parameter_list|)
block|{
name|this
operator|.
name|textProcessingFactory
operator|=
name|TextProcessingFactory
operator|.
name|valueOf
argument_list|(
name|textProcessingFactory
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

