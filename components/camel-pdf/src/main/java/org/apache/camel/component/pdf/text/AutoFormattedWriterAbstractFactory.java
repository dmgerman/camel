begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.pdf.text
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
operator|.
name|text
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
name|component
operator|.
name|pdf
operator|.
name|PdfConfiguration
import|;
end_import

begin_comment
comment|/**  * Builds set of classes for auto-formatting writing strategy. Text is getting sliced by words,  * then max amount of words that fits in the line will be written into pdf document. With this strategy all words  * that doesn't fit in the line will be moved to the new line.  */
end_comment

begin_class
DECL|class|AutoFormattedWriterAbstractFactory
specifier|public
class|class
name|AutoFormattedWriterAbstractFactory
implements|implements
name|TextProcessingAbstractFactory
block|{
DECL|field|pdfConfiguration
specifier|private
specifier|final
name|PdfConfiguration
name|pdfConfiguration
decl_stmt|;
DECL|method|AutoFormattedWriterAbstractFactory (PdfConfiguration pdfConfiguration)
specifier|public
name|AutoFormattedWriterAbstractFactory
parameter_list|(
name|PdfConfiguration
name|pdfConfiguration
parameter_list|)
block|{
name|this
operator|.
name|pdfConfiguration
operator|=
name|pdfConfiguration
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createWriteStrategy ()
specifier|public
name|WriteStrategy
name|createWriteStrategy
parameter_list|()
block|{
return|return
operator|new
name|DefaultWriteStrategy
argument_list|(
name|pdfConfiguration
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createSplitStrategy ()
specifier|public
name|SplitStrategy
name|createSplitStrategy
parameter_list|()
block|{
return|return
operator|new
name|WordSplitStrategy
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|createLineBuilderStrategy ()
specifier|public
name|LineBuilderStrategy
name|createLineBuilderStrategy
parameter_list|()
block|{
return|return
operator|new
name|DefaultLineBuilderStrategy
argument_list|(
name|pdfConfiguration
argument_list|)
return|;
block|}
block|}
end_class

end_unit

