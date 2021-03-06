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
name|io
operator|.
name|ByteArrayOutputStream
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
name|Collection
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
name|pdf
operator|.
name|text
operator|.
name|AutoFormattedWriterAbstractFactory
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
name|pdf
operator|.
name|text
operator|.
name|LineBuilderStrategy
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
name|pdf
operator|.
name|text
operator|.
name|LineTerminationWriterAbstractFactory
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
name|pdf
operator|.
name|text
operator|.
name|SplitStrategy
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
name|pdf
operator|.
name|text
operator|.
name|TextProcessingAbstractFactory
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
name|pdf
operator|.
name|text
operator|.
name|WriteStrategy
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
name|DefaultProducer
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
name|PDDocument
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
name|encryption
operator|.
name|ProtectionPolicy
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
name|encryption
operator|.
name|StandardProtectionPolicy
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
name|text
operator|.
name|PDFTextStripper
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
name|PdfHeaderConstants
operator|.
name|*
import|;
end_import

begin_class
DECL|class|PdfProducer
specifier|public
class|class
name|PdfProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|writeStrategy
specifier|private
specifier|final
name|WriteStrategy
name|writeStrategy
decl_stmt|;
DECL|field|splitStrategy
specifier|private
specifier|final
name|SplitStrategy
name|splitStrategy
decl_stmt|;
DECL|field|lineBuilderStrategy
specifier|private
specifier|final
name|LineBuilderStrategy
name|lineBuilderStrategy
decl_stmt|;
DECL|field|pdfConfiguration
specifier|private
specifier|final
name|PdfConfiguration
name|pdfConfiguration
decl_stmt|;
DECL|method|PdfProducer (PdfEndpoint endpoint)
specifier|public
name|PdfProducer
parameter_list|(
name|PdfEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|pdfConfiguration
operator|=
name|endpoint
operator|.
name|getPdfConfiguration
argument_list|()
expr_stmt|;
name|TextProcessingAbstractFactory
name|textProcessingFactory
init|=
name|createTextProcessingFactory
argument_list|(
name|pdfConfiguration
argument_list|)
decl_stmt|;
name|this
operator|.
name|writeStrategy
operator|=
name|textProcessingFactory
operator|.
name|createWriteStrategy
argument_list|()
expr_stmt|;
name|this
operator|.
name|splitStrategy
operator|=
name|textProcessingFactory
operator|.
name|createSplitStrategy
argument_list|()
expr_stmt|;
name|this
operator|.
name|lineBuilderStrategy
operator|=
name|textProcessingFactory
operator|.
name|createLineBuilderStrategy
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|result
decl_stmt|;
switch|switch
condition|(
name|pdfConfiguration
operator|.
name|getOperation
argument_list|()
condition|)
block|{
case|case
name|append
case|:
name|result
operator|=
name|doAppend
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|create
case|:
name|result
operator|=
name|doCreate
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|extractText
case|:
name|result
operator|=
name|doExtractText
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Unknown operation %s"
argument_list|,
name|pdfConfiguration
operator|.
name|getOperation
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
comment|// propagate headers
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|setHeaders
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
comment|// and set result
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
DECL|method|doAppend (Exchange exchange)
specifier|private
name|Object
name|doAppend
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Got {} operation, going to append text to provided pdf."
argument_list|,
name|pdfConfiguration
operator|.
name|getOperation
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|PDDocument
name|document
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|PDF_DOCUMENT_HEADER_NAME
argument_list|,
name|PDDocument
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|document
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"%s header is expected for append operation"
argument_list|,
name|PDF_DOCUMENT_HEADER_NAME
argument_list|)
argument_list|)
throw|;
block|}
if|if
condition|(
name|document
operator|.
name|isEncrypted
argument_list|()
condition|)
block|{
name|document
operator|.
name|setAllSecurityToBeRemoved
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|ProtectionPolicy
name|protectionPolicy
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|PROTECTION_POLICY_HEADER_NAME
argument_list|,
name|ProtectionPolicy
operator|.
name|class
argument_list|)
decl_stmt|;
name|appendToPdfDocument
argument_list|(
name|body
argument_list|,
name|document
argument_list|,
name|protectionPolicy
argument_list|)
expr_stmt|;
name|OutputStream
name|byteArrayOutputStream
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|document
operator|.
name|save
argument_list|(
name|byteArrayOutputStream
argument_list|)
expr_stmt|;
return|return
name|byteArrayOutputStream
return|;
block|}
DECL|method|doExtractText (Exchange exchange)
specifier|private
name|String
name|doExtractText
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Got {} operation, going to extract text from provided pdf."
argument_list|,
name|pdfConfiguration
operator|.
name|getOperation
argument_list|()
argument_list|)
expr_stmt|;
name|PDDocument
name|document
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|PDDocument
operator|.
name|class
argument_list|)
decl_stmt|;
name|PDFTextStripper
name|pdfTextStripper
init|=
operator|new
name|PDFTextStripper
argument_list|()
decl_stmt|;
return|return
name|pdfTextStripper
operator|.
name|getText
argument_list|(
name|document
argument_list|)
return|;
block|}
DECL|method|doCreate (Exchange exchange)
specifier|private
name|OutputStream
name|doCreate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Got {} operation, going to create and write provided string to pdf document."
argument_list|,
name|pdfConfiguration
operator|.
name|getOperation
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|PDDocument
name|document
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
name|StandardProtectionPolicy
name|protectionPolicy
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|PROTECTION_POLICY_HEADER_NAME
argument_list|,
name|StandardProtectionPolicy
operator|.
name|class
argument_list|)
decl_stmt|;
name|appendToPdfDocument
argument_list|(
name|body
argument_list|,
name|document
argument_list|,
name|protectionPolicy
argument_list|)
expr_stmt|;
name|OutputStream
name|byteArrayOutputStream
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|document
operator|.
name|save
argument_list|(
name|byteArrayOutputStream
argument_list|)
expr_stmt|;
return|return
name|byteArrayOutputStream
return|;
block|}
DECL|method|appendToPdfDocument (String text, PDDocument document, ProtectionPolicy protectionPolicy)
specifier|private
name|void
name|appendToPdfDocument
parameter_list|(
name|String
name|text
parameter_list|,
name|PDDocument
name|document
parameter_list|,
name|ProtectionPolicy
name|protectionPolicy
parameter_list|)
throws|throws
name|IOException
block|{
name|Collection
argument_list|<
name|String
argument_list|>
name|words
init|=
name|splitStrategy
operator|.
name|split
argument_list|(
name|text
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|String
argument_list|>
name|lines
init|=
name|lineBuilderStrategy
operator|.
name|buildLines
argument_list|(
name|words
argument_list|)
decl_stmt|;
name|writeStrategy
operator|.
name|write
argument_list|(
name|lines
argument_list|,
name|document
argument_list|)
expr_stmt|;
if|if
condition|(
name|protectionPolicy
operator|!=
literal|null
condition|)
block|{
name|document
operator|.
name|protect
argument_list|(
name|protectionPolicy
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createTextProcessingFactory (PdfConfiguration pdfConfiguration)
specifier|private
name|TextProcessingAbstractFactory
name|createTextProcessingFactory
parameter_list|(
name|PdfConfiguration
name|pdfConfiguration
parameter_list|)
block|{
name|TextProcessingAbstractFactory
name|result
decl_stmt|;
switch|switch
condition|(
name|pdfConfiguration
operator|.
name|getTextProcessingFactory
argument_list|()
condition|)
block|{
case|case
name|autoFormatting
case|:
name|result
operator|=
operator|new
name|AutoFormattedWriterAbstractFactory
argument_list|(
name|pdfConfiguration
argument_list|)
expr_stmt|;
break|break;
case|case
name|lineTermination
case|:
name|result
operator|=
operator|new
name|LineTerminationWriterAbstractFactory
argument_list|(
name|pdfConfiguration
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Unknown text processing factory %s"
argument_list|,
name|pdfConfiguration
operator|.
name|getTextProcessingFactory
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

