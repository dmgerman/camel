begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|ArrayList
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
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|PdfConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang3
operator|.
name|builder
operator|.
name|ToStringBuilder
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
name|PdfConstants
operator|.
name|MIN_CONTENT_WIDTH
import|;
end_import

begin_comment
comment|/**  * Builds lines from words based on line width and PDF document page size.  * Built lines then will be written to pdf document.  */
end_comment

begin_class
DECL|class|DefaultLineBuilderStrategy
specifier|public
class|class
name|DefaultLineBuilderStrategy
implements|implements
name|LineBuilderStrategy
block|{
DECL|field|pdfConfiguration
specifier|private
specifier|final
name|PdfConfiguration
name|pdfConfiguration
decl_stmt|;
DECL|method|DefaultLineBuilderStrategy (PdfConfiguration pdfConfiguration)
specifier|public
name|DefaultLineBuilderStrategy
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
comment|/**      * Builds lines from words. Utilizes the same behaviour as office software:      *<ul>      *<li>If word doesn't fit in current line, and current lines contains other words, then      *     it will be moved to new line.</td>      *<li>Word doesn't fit in the line and line does not contain other words, then word will be      *     slitted, and split index will be on max amount of characters that fits in the line</li>      *</ul>      */
annotation|@
name|Override
DECL|method|buildLines (Collection<String> splittedText)
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|buildLines
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|splittedText
parameter_list|)
throws|throws
name|IOException
block|{
name|LinkedList
argument_list|<
name|String
argument_list|>
name|wordsList
init|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|(
name|splittedText
argument_list|)
decl_stmt|;
name|Collection
argument_list|<
name|String
argument_list|>
name|lines
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|LineBuilder
name|currentLine
init|=
operator|new
name|LineBuilder
argument_list|()
decl_stmt|;
name|float
name|allowedLineWidth
init|=
name|getAllowedLineWidth
argument_list|()
decl_stmt|;
while|while
condition|(
operator|!
name|wordsList
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
name|word
init|=
name|wordsList
operator|.
name|removeFirst
argument_list|()
decl_stmt|;
if|if
condition|(
name|isWordFitInCurrentLine
argument_list|(
name|currentLine
argument_list|,
name|word
argument_list|,
name|allowedLineWidth
argument_list|)
condition|)
block|{
name|currentLine
operator|.
name|appendWord
argument_list|(
name|word
argument_list|)
expr_stmt|;
if|if
condition|(
name|wordsList
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|lines
operator|.
name|add
argument_list|(
name|currentLine
operator|.
name|buildLine
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|currentLine
operator|.
name|getWordsCount
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|lines
operator|.
name|add
argument_list|(
name|currentLine
operator|.
name|buildLine
argument_list|()
argument_list|)
expr_stmt|;
name|wordsList
operator|.
name|addFirst
argument_list|(
name|word
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|int
name|splitIndex
init|=
name|findSplitIndex
argument_list|(
name|word
argument_list|,
name|allowedLineWidth
argument_list|)
decl_stmt|;
name|currentLine
operator|.
name|appendWord
argument_list|(
name|word
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|splitIndex
argument_list|)
argument_list|)
expr_stmt|;
name|lines
operator|.
name|add
argument_list|(
name|currentLine
operator|.
name|buildLine
argument_list|()
argument_list|)
expr_stmt|;
name|wordsList
operator|.
name|addFirst
argument_list|(
name|word
operator|.
name|substring
argument_list|(
name|splitIndex
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|lines
return|;
block|}
DECL|method|findSplitIndex (String word, float allowedLineWidth)
specifier|private
name|int
name|findSplitIndex
parameter_list|(
name|String
name|word
parameter_list|,
name|float
name|allowedLineWidth
parameter_list|)
throws|throws
name|IOException
block|{
comment|// Using binary search algorithm to find max amount of characters that fit int the line.
name|int
name|middle
init|=
name|word
operator|.
name|length
argument_list|()
operator|>>
literal|1
decl_stmt|;
name|int
name|end
init|=
name|word
operator|.
name|length
argument_list|()
decl_stmt|;
name|int
name|currentSplitIndex
init|=
literal|0
decl_stmt|;
do|do
block|{
if|if
condition|(
name|isLineFitInLineWidth
argument_list|(
name|word
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|middle
argument_list|)
argument_list|,
name|allowedLineWidth
argument_list|)
condition|)
block|{
name|currentSplitIndex
operator|=
name|middle
expr_stmt|;
name|middle
operator|+=
name|word
operator|.
name|substring
argument_list|(
name|middle
argument_list|,
name|end
argument_list|)
operator|.
name|length
argument_list|()
operator|>>
literal|1
expr_stmt|;
block|}
else|else
block|{
name|end
operator|=
name|middle
expr_stmt|;
name|middle
operator|=
name|currentSplitIndex
operator|+
operator|(
name|word
operator|.
name|substring
argument_list|(
name|currentSplitIndex
argument_list|,
name|middle
argument_list|)
operator|.
name|length
argument_list|()
operator|>>
literal|1
operator|)
expr_stmt|;
block|}
block|}
do|while
condition|(
operator|(
name|currentSplitIndex
operator|==
operator|-
literal|1
operator|)
operator|||
operator|!
name|isSplitIndexFound
argument_list|(
name|word
argument_list|,
name|allowedLineWidth
argument_list|,
name|currentSplitIndex
argument_list|)
condition|)
do|;
return|return
name|currentSplitIndex
return|;
block|}
DECL|method|isSplitIndexFound (String word, float allowedLineWidth, int currentSplitIndex)
specifier|private
name|boolean
name|isSplitIndexFound
parameter_list|(
name|String
name|word
parameter_list|,
name|float
name|allowedLineWidth
parameter_list|,
name|int
name|currentSplitIndex
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|isLineFitInLineWidth
argument_list|(
name|word
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|currentSplitIndex
argument_list|)
argument_list|,
name|allowedLineWidth
argument_list|)
operator|&&
operator|!
name|isLineFitInLineWidth
argument_list|(
name|word
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|currentSplitIndex
operator|+
literal|1
argument_list|)
argument_list|,
name|allowedLineWidth
argument_list|)
return|;
block|}
DECL|method|isWordFitInCurrentLine (LineBuilder currentLine, String word, float allowedLineWidth)
specifier|private
name|boolean
name|isWordFitInCurrentLine
parameter_list|(
name|LineBuilder
name|currentLine
parameter_list|,
name|String
name|word
parameter_list|,
name|float
name|allowedLineWidth
parameter_list|)
throws|throws
name|IOException
block|{
name|LineBuilder
name|lineBuilder
init|=
name|currentLine
operator|.
name|clone
argument_list|()
operator|.
name|appendWord
argument_list|(
name|word
argument_list|)
decl_stmt|;
return|return
name|isLineFitInLineWidth
argument_list|(
name|lineBuilder
operator|.
name|buildLine
argument_list|()
argument_list|,
name|allowedLineWidth
argument_list|)
return|;
block|}
DECL|method|isLineFitInLineWidth (String currentLine, float allowedLineWidth)
specifier|private
name|boolean
name|isLineFitInLineWidth
parameter_list|(
name|String
name|currentLine
parameter_list|,
name|float
name|allowedLineWidth
parameter_list|)
throws|throws
name|IOException
block|{
name|float
name|fontWidth
init|=
name|PdfUtils
operator|.
name|getFontWidth
argument_list|(
name|currentLine
argument_list|,
name|pdfConfiguration
operator|.
name|getFont
argument_list|()
argument_list|,
name|pdfConfiguration
operator|.
name|getFontSize
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|fontWidth
operator|<=
name|allowedLineWidth
return|;
block|}
DECL|method|getAllowedLineWidth ()
specifier|public
name|float
name|getAllowedLineWidth
parameter_list|()
block|{
name|float
name|result
init|=
name|pdfConfiguration
operator|.
name|getPageSize
argument_list|()
operator|.
name|getWidth
argument_list|()
operator|-
name|pdfConfiguration
operator|.
name|getMarginLeft
argument_list|()
operator|-
name|pdfConfiguration
operator|.
name|getMarginRight
argument_list|()
decl_stmt|;
if|if
condition|(
name|result
operator|<
name|MIN_CONTENT_WIDTH
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Allowed line width cannot be< %d, make sure"
operator|+
literal|" (marginLeft + marginRight)< pageSize"
argument_list|,
name|MIN_CONTENT_WIDTH
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|result
return|;
block|}
DECL|class|LineBuilder
specifier|private
specifier|static
specifier|final
class|class
name|LineBuilder
block|{
DECL|field|line
specifier|private
name|StringBuilder
name|line
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
DECL|field|wordsCount
specifier|private
name|int
name|wordsCount
decl_stmt|;
DECL|method|LineBuilder ()
name|LineBuilder
parameter_list|()
block|{ }
DECL|method|LineBuilder (String line, int wordsCount)
name|LineBuilder
parameter_list|(
name|String
name|line
parameter_list|,
name|int
name|wordsCount
parameter_list|)
block|{
name|this
operator|.
name|line
operator|=
operator|new
name|StringBuilder
argument_list|(
name|line
argument_list|)
expr_stmt|;
name|this
operator|.
name|wordsCount
operator|=
name|wordsCount
expr_stmt|;
block|}
DECL|method|appendWord (String word)
specifier|public
name|LineBuilder
name|appendWord
parameter_list|(
name|String
name|word
parameter_list|)
block|{
name|line
operator|.
name|append
argument_list|(
name|word
argument_list|)
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
name|wordsCount
operator|++
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|buildLine ()
specifier|public
name|String
name|buildLine
parameter_list|()
block|{
name|String
name|line
init|=
name|this
operator|.
name|line
operator|.
name|toString
argument_list|()
decl_stmt|;
name|reset
argument_list|()
expr_stmt|;
return|return
name|line
return|;
block|}
DECL|method|getWordsCount ()
specifier|public
name|int
name|getWordsCount
parameter_list|()
block|{
return|return
name|wordsCount
return|;
block|}
DECL|method|reset ()
specifier|private
name|void
name|reset
parameter_list|()
block|{
name|line
operator|=
operator|new
name|StringBuilder
argument_list|()
expr_stmt|;
name|wordsCount
operator|=
literal|0
expr_stmt|;
block|}
DECL|method|clone ()
specifier|public
name|LineBuilder
name|clone
parameter_list|()
block|{
return|return
operator|new
name|LineBuilder
argument_list|(
name|this
operator|.
name|line
operator|.
name|toString
argument_list|()
argument_list|,
name|this
operator|.
name|wordsCount
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|ToStringBuilder
operator|.
name|reflectionToString
argument_list|(
name|this
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

