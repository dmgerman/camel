begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.univocity
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|univocity
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertArrayEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertTrue
import|;
end_import

begin_comment
comment|/**  * This class tests the options of {@link org.apache.camel.dataformat.univocity.UniVocityCsvDataFormat}.  */
end_comment

begin_class
DECL|class|UniVocityCsvDataFormatTest
specifier|public
specifier|final
class|class
name|UniVocityCsvDataFormatTest
block|{
annotation|@
name|Test
DECL|method|shouldConfigureNullValue ()
specifier|public
name|void
name|shouldConfigureNullValue
parameter_list|()
block|{
name|UniVocityCsvDataFormat
name|dataFormat
init|=
operator|new
name|UniVocityCsvDataFormat
argument_list|()
operator|.
name|setNullValue
argument_list|(
literal|"N/A"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"N/A"
argument_list|,
name|dataFormat
operator|.
name|getNullValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"N/A"
argument_list|,
name|dataFormat
operator|.
name|createAndConfigureWriterSettings
argument_list|()
operator|.
name|getNullValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"N/A"
argument_list|,
name|dataFormat
operator|.
name|createAndConfigureParserSettings
argument_list|()
operator|.
name|getNullValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldConfigureSkipEmptyLines ()
specifier|public
name|void
name|shouldConfigureSkipEmptyLines
parameter_list|()
block|{
name|UniVocityCsvDataFormat
name|dataFormat
init|=
operator|new
name|UniVocityCsvDataFormat
argument_list|()
operator|.
name|setSkipEmptyLines
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|dataFormat
operator|.
name|getSkipEmptyLines
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dataFormat
operator|.
name|createAndConfigureWriterSettings
argument_list|()
operator|.
name|getSkipEmptyLines
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dataFormat
operator|.
name|createAndConfigureParserSettings
argument_list|()
operator|.
name|getSkipEmptyLines
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldConfigureIgnoreTrailingWhitespaces ()
specifier|public
name|void
name|shouldConfigureIgnoreTrailingWhitespaces
parameter_list|()
block|{
name|UniVocityCsvDataFormat
name|dataFormat
init|=
operator|new
name|UniVocityCsvDataFormat
argument_list|()
operator|.
name|setIgnoreTrailingWhitespaces
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|dataFormat
operator|.
name|getIgnoreTrailingWhitespaces
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dataFormat
operator|.
name|createAndConfigureWriterSettings
argument_list|()
operator|.
name|getIgnoreTrailingWhitespaces
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dataFormat
operator|.
name|createAndConfigureParserSettings
argument_list|()
operator|.
name|getIgnoreTrailingWhitespaces
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldConfigureIgnoreLeadingWhitespaces ()
specifier|public
name|void
name|shouldConfigureIgnoreLeadingWhitespaces
parameter_list|()
block|{
name|UniVocityCsvDataFormat
name|dataFormat
init|=
operator|new
name|UniVocityCsvDataFormat
argument_list|()
operator|.
name|setIgnoreLeadingWhitespaces
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|dataFormat
operator|.
name|getIgnoreLeadingWhitespaces
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dataFormat
operator|.
name|createAndConfigureWriterSettings
argument_list|()
operator|.
name|getIgnoreLeadingWhitespaces
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dataFormat
operator|.
name|createAndConfigureParserSettings
argument_list|()
operator|.
name|getIgnoreLeadingWhitespaces
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldConfigureHeadersDisabled ()
specifier|public
name|void
name|shouldConfigureHeadersDisabled
parameter_list|()
block|{
name|UniVocityCsvDataFormat
name|dataFormat
init|=
operator|new
name|UniVocityCsvDataFormat
argument_list|()
operator|.
name|setHeadersDisabled
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|dataFormat
operator|.
name|isHeadersDisabled
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|dataFormat
operator|.
name|createAndConfigureWriterSettings
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|dataFormat
operator|.
name|createAndConfigureParserSettings
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldConfigureHeaders ()
specifier|public
name|void
name|shouldConfigureHeaders
parameter_list|()
block|{
name|UniVocityCsvDataFormat
name|dataFormat
init|=
operator|new
name|UniVocityCsvDataFormat
argument_list|()
operator|.
name|setHeaders
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"A"
block|,
literal|"B"
block|,
literal|"C"
block|}
argument_list|)
decl_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"A"
block|,
literal|"B"
block|,
literal|"C"
block|}
argument_list|,
name|dataFormat
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"A"
block|,
literal|"B"
block|,
literal|"C"
block|}
argument_list|,
name|dataFormat
operator|.
name|createAndConfigureWriterSettings
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"A"
block|,
literal|"B"
block|,
literal|"C"
block|}
argument_list|,
name|dataFormat
operator|.
name|createAndConfigureParserSettings
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldConfigureHeaderExtractionEnabled ()
specifier|public
name|void
name|shouldConfigureHeaderExtractionEnabled
parameter_list|()
block|{
name|UniVocityCsvDataFormat
name|dataFormat
init|=
operator|new
name|UniVocityCsvDataFormat
argument_list|()
operator|.
name|setHeaderExtractionEnabled
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|dataFormat
operator|.
name|getHeaderExtractionEnabled
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dataFormat
operator|.
name|createAndConfigureParserSettings
argument_list|()
operator|.
name|isHeaderExtractionEnabled
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldConfigureNumberOfRecordsToRead ()
specifier|public
name|void
name|shouldConfigureNumberOfRecordsToRead
parameter_list|()
block|{
name|UniVocityCsvDataFormat
name|dataFormat
init|=
operator|new
name|UniVocityCsvDataFormat
argument_list|()
operator|.
name|setNumberOfRecordsToRead
argument_list|(
literal|42
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|42
argument_list|)
argument_list|,
name|dataFormat
operator|.
name|getNumberOfRecordsToRead
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|42
argument_list|,
name|dataFormat
operator|.
name|createAndConfigureParserSettings
argument_list|()
operator|.
name|getNumberOfRecordsToRead
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldConfigureEmptyValue ()
specifier|public
name|void
name|shouldConfigureEmptyValue
parameter_list|()
block|{
name|UniVocityCsvDataFormat
name|dataFormat
init|=
operator|new
name|UniVocityCsvDataFormat
argument_list|()
operator|.
name|setEmptyValue
argument_list|(
literal|"empty"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"empty"
argument_list|,
name|dataFormat
operator|.
name|getEmptyValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"empty"
argument_list|,
name|dataFormat
operator|.
name|createAndConfigureWriterSettings
argument_list|()
operator|.
name|getEmptyValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"empty"
argument_list|,
name|dataFormat
operator|.
name|createAndConfigureParserSettings
argument_list|()
operator|.
name|getEmptyValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldConfigureLineSeparator ()
specifier|public
name|void
name|shouldConfigureLineSeparator
parameter_list|()
block|{
name|UniVocityCsvDataFormat
name|dataFormat
init|=
operator|new
name|UniVocityCsvDataFormat
argument_list|()
operator|.
name|setLineSeparator
argument_list|(
literal|"ls"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"ls"
argument_list|,
name|dataFormat
operator|.
name|getLineSeparator
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ls"
argument_list|,
name|dataFormat
operator|.
name|createAndConfigureWriterSettings
argument_list|()
operator|.
name|getFormat
argument_list|()
operator|.
name|getLineSeparatorString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ls"
argument_list|,
name|dataFormat
operator|.
name|createAndConfigureParserSettings
argument_list|()
operator|.
name|getFormat
argument_list|()
operator|.
name|getLineSeparatorString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldConfigureNormalizedLineSeparator ()
specifier|public
name|void
name|shouldConfigureNormalizedLineSeparator
parameter_list|()
block|{
name|UniVocityCsvDataFormat
name|dataFormat
init|=
operator|new
name|UniVocityCsvDataFormat
argument_list|()
operator|.
name|setNormalizedLineSeparator
argument_list|(
literal|'n'
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Character
operator|.
name|valueOf
argument_list|(
literal|'n'
argument_list|)
argument_list|,
name|dataFormat
operator|.
name|getNormalizedLineSeparator
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|'n'
argument_list|,
name|dataFormat
operator|.
name|createAndConfigureWriterSettings
argument_list|()
operator|.
name|getFormat
argument_list|()
operator|.
name|getNormalizedNewline
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|'n'
argument_list|,
name|dataFormat
operator|.
name|createAndConfigureParserSettings
argument_list|()
operator|.
name|getFormat
argument_list|()
operator|.
name|getNormalizedNewline
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldConfigureComment ()
specifier|public
name|void
name|shouldConfigureComment
parameter_list|()
block|{
name|UniVocityCsvDataFormat
name|dataFormat
init|=
operator|new
name|UniVocityCsvDataFormat
argument_list|()
operator|.
name|setComment
argument_list|(
literal|'c'
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Character
operator|.
name|valueOf
argument_list|(
literal|'c'
argument_list|)
argument_list|,
name|dataFormat
operator|.
name|getComment
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|'c'
argument_list|,
name|dataFormat
operator|.
name|createAndConfigureWriterSettings
argument_list|()
operator|.
name|getFormat
argument_list|()
operator|.
name|getComment
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|'c'
argument_list|,
name|dataFormat
operator|.
name|createAndConfigureParserSettings
argument_list|()
operator|.
name|getFormat
argument_list|()
operator|.
name|getComment
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldConfigureLazyLoad ()
specifier|public
name|void
name|shouldConfigureLazyLoad
parameter_list|()
block|{
name|UniVocityCsvDataFormat
name|dataFormat
init|=
operator|new
name|UniVocityCsvDataFormat
argument_list|()
operator|.
name|setLazyLoad
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|dataFormat
operator|.
name|isLazyLoad
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldConfigureAsMap ()
specifier|public
name|void
name|shouldConfigureAsMap
parameter_list|()
block|{
name|UniVocityCsvDataFormat
name|dataFormat
init|=
operator|new
name|UniVocityCsvDataFormat
argument_list|()
operator|.
name|setAsMap
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|dataFormat
operator|.
name|isAsMap
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldConfigureQuoteAllFields ()
specifier|public
name|void
name|shouldConfigureQuoteAllFields
parameter_list|()
block|{
name|UniVocityCsvDataFormat
name|dataFormat
init|=
operator|new
name|UniVocityCsvDataFormat
argument_list|()
operator|.
name|setQuoteAllFields
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|dataFormat
operator|.
name|getQuoteAllFields
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dataFormat
operator|.
name|createAndConfigureWriterSettings
argument_list|()
operator|.
name|getQuoteAllFields
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldConfigureQuote ()
specifier|public
name|void
name|shouldConfigureQuote
parameter_list|()
block|{
name|UniVocityCsvDataFormat
name|dataFormat
init|=
operator|new
name|UniVocityCsvDataFormat
argument_list|()
operator|.
name|setQuote
argument_list|(
literal|'q'
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Character
operator|.
name|valueOf
argument_list|(
literal|'q'
argument_list|)
argument_list|,
name|dataFormat
operator|.
name|getQuote
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|'q'
argument_list|,
name|dataFormat
operator|.
name|createAndConfigureWriterSettings
argument_list|()
operator|.
name|getFormat
argument_list|()
operator|.
name|getQuote
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|'q'
argument_list|,
name|dataFormat
operator|.
name|createAndConfigureParserSettings
argument_list|()
operator|.
name|getFormat
argument_list|()
operator|.
name|getQuote
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldConfigureQuoteEscape ()
specifier|public
name|void
name|shouldConfigureQuoteEscape
parameter_list|()
block|{
name|UniVocityCsvDataFormat
name|dataFormat
init|=
operator|new
name|UniVocityCsvDataFormat
argument_list|()
operator|.
name|setQuoteEscape
argument_list|(
literal|'e'
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Character
operator|.
name|valueOf
argument_list|(
literal|'e'
argument_list|)
argument_list|,
name|dataFormat
operator|.
name|getQuoteEscape
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|'e'
argument_list|,
name|dataFormat
operator|.
name|createAndConfigureWriterSettings
argument_list|()
operator|.
name|getFormat
argument_list|()
operator|.
name|getQuoteEscape
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|'e'
argument_list|,
name|dataFormat
operator|.
name|createAndConfigureParserSettings
argument_list|()
operator|.
name|getFormat
argument_list|()
operator|.
name|getQuoteEscape
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldConfigureDelimiter ()
specifier|public
name|void
name|shouldConfigureDelimiter
parameter_list|()
block|{
name|UniVocityCsvDataFormat
name|dataFormat
init|=
operator|new
name|UniVocityCsvDataFormat
argument_list|()
operator|.
name|setDelimiter
argument_list|(
literal|'d'
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Character
operator|.
name|valueOf
argument_list|(
literal|'d'
argument_list|)
argument_list|,
name|dataFormat
operator|.
name|getDelimiter
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|'d'
argument_list|,
name|dataFormat
operator|.
name|createAndConfigureWriterSettings
argument_list|()
operator|.
name|getFormat
argument_list|()
operator|.
name|getDelimiter
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|'d'
argument_list|,
name|dataFormat
operator|.
name|createAndConfigureParserSettings
argument_list|()
operator|.
name|getFormat
argument_list|()
operator|.
name|getDelimiter
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

