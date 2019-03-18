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
name|ByteArrayInputStream
import|;
end_import

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
name|Predicate
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
name|RouteBuilder
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
name|PDPage
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
name|PDPageContentStream
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
name|encryption
operator|.
name|AccessPermission
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
name|StandardDecryptionMaterial
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
name|pdmodel
operator|.
name|font
operator|.
name|PDType1Font
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
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|hamcrest
operator|.
name|Matchers
operator|.
name|containsString
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|instanceOf
import|;
end_import

begin_class
DECL|class|PdfAppendTest
specifier|public
class|class
name|PdfAppendTest
extends|extends
name|CamelTestSupport
block|{
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result"
argument_list|)
DECL|field|resultEndpoint
specifier|protected
name|MockEndpoint
name|resultEndpoint
decl_stmt|;
annotation|@
name|Override
annotation|@
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAppend ()
specifier|public
name|void
name|testAppend
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|originalText
init|=
literal|"Test"
decl_stmt|;
specifier|final
name|String
name|textToAppend
init|=
literal|"Append"
decl_stmt|;
name|PDDocument
name|document
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
name|PDPage
name|page
init|=
operator|new
name|PDPage
argument_list|(
name|PDRectangle
operator|.
name|A4
argument_list|)
decl_stmt|;
name|document
operator|.
name|addPage
argument_list|(
name|page
argument_list|)
expr_stmt|;
name|PDPageContentStream
name|contentStream
init|=
operator|new
name|PDPageContentStream
argument_list|(
name|document
argument_list|,
name|page
argument_list|)
decl_stmt|;
name|contentStream
operator|.
name|setFont
argument_list|(
name|PDType1Font
operator|.
name|HELVETICA
argument_list|,
literal|12
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|beginText
argument_list|()
expr_stmt|;
name|contentStream
operator|.
name|moveTextPositionByAmount
argument_list|(
literal|20
argument_list|,
literal|400
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|drawString
argument_list|(
name|originalText
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|endText
argument_list|()
expr_stmt|;
name|contentStream
operator|.
name|close
argument_list|()
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"direct:start"
argument_list|,
name|textToAppend
argument_list|,
name|PdfHeaderConstants
operator|.
name|PDF_DOCUMENT_HEADER_NAME
argument_list|,
name|document
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|expectedMessagesMatches
argument_list|(
operator|new
name|Predicate
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|matches
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|body
argument_list|,
name|instanceOf
argument_list|(
name|ByteArrayOutputStream
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|PDDocument
name|doc
init|=
name|PDDocument
operator|.
name|load
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
operator|(
operator|(
name|ByteArrayOutputStream
operator|)
name|body
operator|)
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|PDFTextStripper
name|pdfTextStripper
init|=
operator|new
name|PDFTextStripper
argument_list|()
decl_stmt|;
name|String
name|text
init|=
name|pdfTextStripper
operator|.
name|getText
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|doc
operator|.
name|getNumberOfPages
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|text
argument_list|,
name|containsString
argument_list|(
name|originalText
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|text
argument_list|,
name|containsString
argument_list|(
name|textToAppend
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
literal|true
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testAppendEncrypted ()
specifier|public
name|void
name|testAppendEncrypted
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|originalText
init|=
literal|"Test"
decl_stmt|;
specifier|final
name|String
name|textToAppend
init|=
literal|"Append"
decl_stmt|;
name|PDDocument
name|document
init|=
operator|new
name|PDDocument
argument_list|()
decl_stmt|;
name|PDPage
name|page
init|=
operator|new
name|PDPage
argument_list|(
name|PDRectangle
operator|.
name|A4
argument_list|)
decl_stmt|;
name|document
operator|.
name|addPage
argument_list|(
name|page
argument_list|)
expr_stmt|;
name|PDPageContentStream
name|contentStream
init|=
operator|new
name|PDPageContentStream
argument_list|(
name|document
argument_list|,
name|page
argument_list|)
decl_stmt|;
name|contentStream
operator|.
name|setFont
argument_list|(
name|PDType1Font
operator|.
name|HELVETICA
argument_list|,
literal|12
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|beginText
argument_list|()
expr_stmt|;
name|contentStream
operator|.
name|moveTextPositionByAmount
argument_list|(
literal|20
argument_list|,
literal|400
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|drawString
argument_list|(
name|originalText
argument_list|)
expr_stmt|;
name|contentStream
operator|.
name|endText
argument_list|()
expr_stmt|;
name|contentStream
operator|.
name|close
argument_list|()
expr_stmt|;
specifier|final
name|String
name|ownerPass
init|=
literal|"ownerPass"
decl_stmt|;
specifier|final
name|String
name|userPass
init|=
literal|"userPass"
decl_stmt|;
name|AccessPermission
name|accessPermission
init|=
operator|new
name|AccessPermission
argument_list|()
decl_stmt|;
name|accessPermission
operator|.
name|setCanExtractContent
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|StandardProtectionPolicy
name|protectionPolicy
init|=
operator|new
name|StandardProtectionPolicy
argument_list|(
name|ownerPass
argument_list|,
name|userPass
argument_list|,
name|accessPermission
argument_list|)
decl_stmt|;
name|protectionPolicy
operator|.
name|setEncryptionKeyLength
argument_list|(
literal|128
argument_list|)
expr_stmt|;
name|document
operator|.
name|protect
argument_list|(
name|protectionPolicy
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|output
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|document
operator|.
name|save
argument_list|(
name|output
argument_list|)
expr_stmt|;
comment|// Encryption happens after saving.
name|PDDocument
name|encryptedDocument
init|=
name|PDDocument
operator|.
name|load
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|output
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|,
name|userPass
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|PdfHeaderConstants
operator|.
name|PDF_DOCUMENT_HEADER_NAME
argument_list|,
name|encryptedDocument
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
name|PdfHeaderConstants
operator|.
name|DECRYPTION_MATERIAL_HEADER_NAME
argument_list|,
operator|new
name|StandardDecryptionMaterial
argument_list|(
name|userPass
argument_list|)
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeaders
argument_list|(
literal|"direct:start"
argument_list|,
name|textToAppend
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|expectedMessagesMatches
argument_list|(
operator|new
name|Predicate
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|matches
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|assertThat
argument_list|(
name|body
argument_list|,
name|instanceOf
argument_list|(
name|ByteArrayOutputStream
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|PDDocument
name|doc
init|=
name|PDDocument
operator|.
name|load
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
operator|(
operator|(
name|ByteArrayOutputStream
operator|)
name|body
operator|)
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|,
name|userPass
argument_list|)
decl_stmt|;
name|PDFTextStripper
name|pdfTextStripper
init|=
operator|new
name|PDFTextStripper
argument_list|()
decl_stmt|;
name|String
name|text
init|=
name|pdfTextStripper
operator|.
name|getText
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|doc
operator|.
name|getNumberOfPages
argument_list|()
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|text
argument_list|,
name|containsString
argument_list|(
name|originalText
argument_list|)
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|text
argument_list|,
name|containsString
argument_list|(
name|textToAppend
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
literal|true
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"pdf:append"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

