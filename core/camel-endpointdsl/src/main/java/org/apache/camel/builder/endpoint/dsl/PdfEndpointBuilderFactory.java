begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.endpoint.dsl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|endpoint
operator|.
name|dsl
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|builder
operator|.
name|EndpointConsumerBuilder
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
name|builder
operator|.
name|EndpointProducerBuilder
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
name|builder
operator|.
name|endpoint
operator|.
name|AbstractEndpointBuilder
import|;
end_import

begin_comment
comment|/**  * The pdf components provides the ability to create, modify or extract content  * from PDF documents.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|PdfEndpointBuilderFactory
specifier|public
interface|interface
name|PdfEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the PDF component.      */
DECL|interface|PdfEndpointBuilder
specifier|public
interface|interface
name|PdfEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedPdfEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedPdfEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Operation type.          * The option is a          *<code>org.apache.camel.component.pdf.PdfOperation</code> type.          * @group producer          */
DECL|method|operation (PdfOperation operation)
specifier|default
name|PdfEndpointBuilder
name|operation
parameter_list|(
name|PdfOperation
name|operation
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"operation"
argument_list|,
name|operation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Operation type.          * The option will be converted to a          *<code>org.apache.camel.component.pdf.PdfOperation</code> type.          * @group producer          */
DECL|method|operation (String operation)
specifier|default
name|PdfEndpointBuilder
name|operation
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"operation"
argument_list|,
name|operation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Font.          * The option is a<code>org.apache.pdfbox.pdmodel.font.PDFont</code>          * type.          * @group producer          */
DECL|method|font (Object font)
specifier|default
name|PdfEndpointBuilder
name|font
parameter_list|(
name|Object
name|font
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"font"
argument_list|,
name|font
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Font.          * The option will be converted to a          *<code>org.apache.pdfbox.pdmodel.font.PDFont</code> type.          * @group producer          */
DECL|method|font (String font)
specifier|default
name|PdfEndpointBuilder
name|font
parameter_list|(
name|String
name|font
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"font"
argument_list|,
name|font
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Font size in pixels.          * The option is a<code>float</code> type.          * @group producer          */
DECL|method|fontSize (float fontSize)
specifier|default
name|PdfEndpointBuilder
name|fontSize
parameter_list|(
name|float
name|fontSize
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"fontSize"
argument_list|,
name|fontSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Font size in pixels.          * The option will be converted to a<code>float</code> type.          * @group producer          */
DECL|method|fontSize (String fontSize)
specifier|default
name|PdfEndpointBuilder
name|fontSize
parameter_list|(
name|String
name|fontSize
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"fontSize"
argument_list|,
name|fontSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Margin bottom in pixels.          * The option is a<code>int</code> type.          * @group producer          */
DECL|method|marginBottom (int marginBottom)
specifier|default
name|PdfEndpointBuilder
name|marginBottom
parameter_list|(
name|int
name|marginBottom
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"marginBottom"
argument_list|,
name|marginBottom
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Margin bottom in pixels.          * The option will be converted to a<code>int</code> type.          * @group producer          */
DECL|method|marginBottom (String marginBottom)
specifier|default
name|PdfEndpointBuilder
name|marginBottom
parameter_list|(
name|String
name|marginBottom
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"marginBottom"
argument_list|,
name|marginBottom
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Margin left in pixels.          * The option is a<code>int</code> type.          * @group producer          */
DECL|method|marginLeft (int marginLeft)
specifier|default
name|PdfEndpointBuilder
name|marginLeft
parameter_list|(
name|int
name|marginLeft
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"marginLeft"
argument_list|,
name|marginLeft
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Margin left in pixels.          * The option will be converted to a<code>int</code> type.          * @group producer          */
DECL|method|marginLeft (String marginLeft)
specifier|default
name|PdfEndpointBuilder
name|marginLeft
parameter_list|(
name|String
name|marginLeft
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"marginLeft"
argument_list|,
name|marginLeft
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Margin right in pixels.          * The option is a<code>int</code> type.          * @group producer          */
DECL|method|marginRight (int marginRight)
specifier|default
name|PdfEndpointBuilder
name|marginRight
parameter_list|(
name|int
name|marginRight
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"marginRight"
argument_list|,
name|marginRight
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Margin right in pixels.          * The option will be converted to a<code>int</code> type.          * @group producer          */
DECL|method|marginRight (String marginRight)
specifier|default
name|PdfEndpointBuilder
name|marginRight
parameter_list|(
name|String
name|marginRight
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"marginRight"
argument_list|,
name|marginRight
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Margin top in pixels.          * The option is a<code>int</code> type.          * @group producer          */
DECL|method|marginTop (int marginTop)
specifier|default
name|PdfEndpointBuilder
name|marginTop
parameter_list|(
name|int
name|marginTop
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"marginTop"
argument_list|,
name|marginTop
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Margin top in pixels.          * The option will be converted to a<code>int</code> type.          * @group producer          */
DECL|method|marginTop (String marginTop)
specifier|default
name|PdfEndpointBuilder
name|marginTop
parameter_list|(
name|String
name|marginTop
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"marginTop"
argument_list|,
name|marginTop
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Page size.          * The option is a          *<code>org.apache.pdfbox.pdmodel.common.PDRectangle</code> type.          * @group producer          */
DECL|method|pageSize (Object pageSize)
specifier|default
name|PdfEndpointBuilder
name|pageSize
parameter_list|(
name|Object
name|pageSize
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"pageSize"
argument_list|,
name|pageSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Page size.          * The option will be converted to a          *<code>org.apache.pdfbox.pdmodel.common.PDRectangle</code> type.          * @group producer          */
DECL|method|pageSize (String pageSize)
specifier|default
name|PdfEndpointBuilder
name|pageSize
parameter_list|(
name|String
name|pageSize
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"pageSize"
argument_list|,
name|pageSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Text processing to use. autoFormatting: Text is getting sliced by          * words, then max amount of words that fits in the line will be written          * into pdf document. With this strategy all words that doesn't fit in          * the line will be moved to the new line. lineTermination: Builds set          * of classes for line-termination writing strategy. Text getting sliced          * by line termination symbol and then it will be written regardless it          * fits in the line or not.          * The option is a          *<code>org.apache.camel.component.pdf.TextProcessingFactory</code>          * type.          * @group producer          */
DECL|method|textProcessingFactory ( TextProcessingFactory textProcessingFactory)
specifier|default
name|PdfEndpointBuilder
name|textProcessingFactory
parameter_list|(
name|TextProcessingFactory
name|textProcessingFactory
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"textProcessingFactory"
argument_list|,
name|textProcessingFactory
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Text processing to use. autoFormatting: Text is getting sliced by          * words, then max amount of words that fits in the line will be written          * into pdf document. With this strategy all words that doesn't fit in          * the line will be moved to the new line. lineTermination: Builds set          * of classes for line-termination writing strategy. Text getting sliced          * by line termination symbol and then it will be written regardless it          * fits in the line or not.          * The option will be converted to a          *<code>org.apache.camel.component.pdf.TextProcessingFactory</code>          * type.          * @group producer          */
DECL|method|textProcessingFactory ( String textProcessingFactory)
specifier|default
name|PdfEndpointBuilder
name|textProcessingFactory
parameter_list|(
name|String
name|textProcessingFactory
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"textProcessingFactory"
argument_list|,
name|textProcessingFactory
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the PDF component.      */
DECL|interface|AdvancedPdfEndpointBuilder
specifier|public
interface|interface
name|AdvancedPdfEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|PdfEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|PdfEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedPdfEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedPdfEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|synchronous (boolean synchronous)
specifier|default
name|AdvancedPdfEndpointBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|synchronous (String synchronous)
specifier|default
name|AdvancedPdfEndpointBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Proxy enum for<code>org.apache.camel.component.pdf.PdfOperation</code>      * enum.      */
DECL|enum|PdfOperation
enum|enum
name|PdfOperation
block|{
DECL|enumConstant|create
DECL|enumConstant|append
DECL|enumConstant|extractText
name|create
block|,
name|append
block|,
name|extractText
block|;     }
comment|/**      * Proxy enum for      *<code>org.apache.camel.component.pdf.TextProcessingFactory</code> enum.      */
DECL|enum|TextProcessingFactory
enum|enum
name|TextProcessingFactory
block|{
DECL|enumConstant|autoFormatting
DECL|enumConstant|lineTermination
name|autoFormatting
block|,
name|lineTermination
block|;     }
comment|/**      * The pdf components provides the ability to create, modify or extract      * content from PDF documents. Creates a builder to build endpoints for the      * PDF component.      */
DECL|method|pdf (String path)
specifier|default
name|PdfEndpointBuilder
name|pdf
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|PdfEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|PdfEndpointBuilder
implements|,
name|AdvancedPdfEndpointBuilder
block|{
specifier|public
name|PdfEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"pdf"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|PdfEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

