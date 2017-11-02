begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.fop
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|fop
package|;
end_package

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
name|StringWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
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
name|cos
operator|.
name|COSName
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
name|PDDocumentInformation
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

begin_class
DECL|class|FopHelper
specifier|public
specifier|final
class|class
name|FopHelper
block|{
DECL|method|FopHelper ()
specifier|private
name|FopHelper
parameter_list|()
block|{     }
DECL|method|extractTextFrom (PDDocument document)
specifier|public
specifier|static
name|String
name|extractTextFrom
parameter_list|(
name|PDDocument
name|document
parameter_list|)
throws|throws
name|IOException
block|{
name|Writer
name|output
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|PDFTextStripper
name|stripper
init|=
operator|new
name|PDFTextStripper
argument_list|()
decl_stmt|;
name|stripper
operator|.
name|writeText
argument_list|(
name|document
argument_list|,
name|output
argument_list|)
expr_stmt|;
return|return
name|output
operator|.
name|toString
argument_list|()
operator|.
name|trim
argument_list|()
return|;
block|}
DECL|method|getDocumentMetadataValue (PDDocument document, COSName name)
specifier|public
specifier|static
name|String
name|getDocumentMetadataValue
parameter_list|(
name|PDDocument
name|document
parameter_list|,
name|COSName
name|name
parameter_list|)
block|{
name|PDDocumentInformation
name|info
init|=
name|document
operator|.
name|getDocumentInformation
argument_list|()
decl_stmt|;
return|return
name|info
operator|.
name|getCOSObject
argument_list|()
operator|.
name|getString
argument_list|(
name|name
argument_list|)
return|;
block|}
DECL|method|decorateTextWithXSLFO (String text)
specifier|public
specifier|static
name|String
name|decorateTextWithXSLFO
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
literal|"<fo:root xmlns:fo=\"http://www.w3.org/1999/XSL/Format\">\n"
operator|+
literal|"<fo:layout-master-set>\n"
operator|+
literal|"<fo:simple-page-master master-name=\"only\">\n"
operator|+
literal|"<fo:region-body region-name=\"xsl-region-body\" margin=\"0.7in\"  padding=\"0\" />\n"
operator|+
literal|"<fo:region-before region-name=\"xsl-region-before\" extent=\"0.7in\" />\n"
operator|+
literal|"<fo:region-after region-name=\"xsl-region-after\" extent=\"0.7in\" />\n"
operator|+
literal|"</fo:simple-page-master>\n"
operator|+
literal|"</fo:layout-master-set>\n"
operator|+
literal|"<fo:page-sequence master-reference=\"only\">\n"
operator|+
literal|"<fo:flow flow-name=\"xsl-region-body\">\n"
operator|+
literal|"<fo:block>"
operator|+
name|text
operator|+
literal|"</fo:block>\n"
operator|+
literal|"</fo:flow>\n"
operator|+
literal|"</fo:page-sequence>\n"
operator|+
literal|"</fo:root>"
return|;
block|}
block|}
end_class

end_unit

