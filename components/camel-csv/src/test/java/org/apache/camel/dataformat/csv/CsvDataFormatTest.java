begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.csv
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|csv
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|csv
operator|.
name|CSVFormat
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
name|csv
operator|.
name|CSVRecord
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
name|csv
operator|.
name|QuoteMode
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
name|junit
operator|.
name|Assert
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
name|Assert
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
name|Assert
operator|.
name|assertFalse
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
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
name|Assert
operator|.
name|assertSame
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_comment
comment|/**  * This class tests the creation of the proper {@link org.apache.commons.csv.CSVFormat} based on the properties of  * {@link org.apache.camel.dataformat.csv.CsvDataFormat}.  * It doesn't test the marshalling and unmarshalling based on the CSV format.  */
end_comment

begin_class
DECL|class|CsvDataFormatTest
specifier|public
class|class
name|CsvDataFormatTest
block|{
annotation|@
name|Test
DECL|method|shouldUseDefaultFormat ()
specifier|public
name|void
name|shouldUseDefaultFormat
parameter_list|()
block|{
name|CsvDataFormat
name|dataFormat
init|=
operator|new
name|CsvDataFormat
argument_list|()
decl_stmt|;
comment|// Properly initialized
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
comment|// Properly used
name|assertEquals
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getActiveFormat
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldUseFormatFromConstructor ()
specifier|public
name|void
name|shouldUseFormatFromConstructor
parameter_list|()
block|{
name|CsvDataFormat
name|dataFormat
init|=
operator|new
name|CsvDataFormat
argument_list|(
name|CSVFormat
operator|.
name|EXCEL
argument_list|)
decl_stmt|;
comment|// Properly initialized
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|EXCEL
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
comment|// Properly used
name|assertEquals
argument_list|(
name|CSVFormat
operator|.
name|EXCEL
argument_list|,
name|dataFormat
operator|.
name|getActiveFormat
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldUseSpecifiedFormat ()
specifier|public
name|void
name|shouldUseSpecifiedFormat
parameter_list|()
block|{
name|CsvDataFormat
name|dataFormat
init|=
operator|new
name|CsvDataFormat
argument_list|()
operator|.
name|setFormat
argument_list|(
name|CSVFormat
operator|.
name|MYSQL
argument_list|)
decl_stmt|;
comment|// Properly saved
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|MYSQL
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
comment|// Properly used
name|assertEquals
argument_list|(
name|CSVFormat
operator|.
name|MYSQL
argument_list|,
name|dataFormat
operator|.
name|getActiveFormat
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldFallbackToDefaultFormat ()
specifier|public
name|void
name|shouldFallbackToDefaultFormat
parameter_list|()
block|{
name|CsvDataFormat
name|dataFormat
init|=
operator|new
name|CsvDataFormat
argument_list|(
name|CSVFormat
operator|.
name|EXCEL
argument_list|)
operator|.
name|setFormat
argument_list|(
literal|null
argument_list|)
decl_stmt|;
comment|// Properly saved
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
comment|// Properly used
name|assertEquals
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getActiveFormat
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldDefineFormatByName ()
specifier|public
name|void
name|shouldDefineFormatByName
parameter_list|()
block|{
name|CsvDataFormat
name|dataFormat
init|=
operator|new
name|CsvDataFormat
argument_list|()
operator|.
name|setFormatName
argument_list|(
literal|"EXCEL"
argument_list|)
decl_stmt|;
comment|// Properly saved
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|EXCEL
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
comment|// Properly used
name|assertEquals
argument_list|(
name|CSVFormat
operator|.
name|EXCEL
argument_list|,
name|dataFormat
operator|.
name|getActiveFormat
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldDisableCommentMarker ()
specifier|public
name|void
name|shouldDisableCommentMarker
parameter_list|()
block|{
name|CsvDataFormat
name|dataFormat
init|=
operator|new
name|CsvDataFormat
argument_list|()
operator|.
name|setCommentMarkerDisabled
argument_list|(
literal|true
argument_list|)
operator|.
name|setCommentMarker
argument_list|(
literal|'c'
argument_list|)
decl_stmt|;
comment|// Properly saved
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dataFormat
operator|.
name|isCommentMarkerDisabled
argument_list|()
argument_list|)
expr_stmt|;
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
name|getCommentMarker
argument_list|()
argument_list|)
expr_stmt|;
comment|// Properly used
name|assertNull
argument_list|(
name|dataFormat
operator|.
name|getActiveFormat
argument_list|()
operator|.
name|getCommentMarker
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldOverrideCommentMarker ()
specifier|public
name|void
name|shouldOverrideCommentMarker
parameter_list|()
block|{
name|CsvDataFormat
name|dataFormat
init|=
operator|new
name|CsvDataFormat
argument_list|()
operator|.
name|setCommentMarker
argument_list|(
literal|'c'
argument_list|)
decl_stmt|;
comment|// Properly saved
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
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
name|getCommentMarker
argument_list|()
argument_list|)
expr_stmt|;
comment|// Properly used
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
name|getActiveFormat
argument_list|()
operator|.
name|getCommentMarker
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldOverrideDelimiter ()
specifier|public
name|void
name|shouldOverrideDelimiter
parameter_list|()
block|{
name|CsvDataFormat
name|dataFormat
init|=
operator|new
name|CsvDataFormat
argument_list|()
operator|.
name|setDelimiter
argument_list|(
literal|'d'
argument_list|)
decl_stmt|;
comment|// Properly saved
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
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
comment|// Properly used
name|assertEquals
argument_list|(
literal|'d'
argument_list|,
name|dataFormat
operator|.
name|getActiveFormat
argument_list|()
operator|.
name|getDelimiter
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldDisableEscape ()
specifier|public
name|void
name|shouldDisableEscape
parameter_list|()
block|{
name|CsvDataFormat
name|dataFormat
init|=
operator|new
name|CsvDataFormat
argument_list|()
operator|.
name|setEscapeDisabled
argument_list|(
literal|true
argument_list|)
operator|.
name|setEscape
argument_list|(
literal|'e'
argument_list|)
decl_stmt|;
comment|// Properly saved
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dataFormat
operator|.
name|isEscapeDisabled
argument_list|()
argument_list|)
expr_stmt|;
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
name|getEscape
argument_list|()
argument_list|)
expr_stmt|;
comment|// Properly used
name|assertNull
argument_list|(
name|dataFormat
operator|.
name|getActiveFormat
argument_list|()
operator|.
name|getEscapeCharacter
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldOverrideEscape ()
specifier|public
name|void
name|shouldOverrideEscape
parameter_list|()
block|{
name|CsvDataFormat
name|dataFormat
init|=
operator|new
name|CsvDataFormat
argument_list|()
operator|.
name|setEscape
argument_list|(
literal|'e'
argument_list|)
decl_stmt|;
comment|// Properly saved
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
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
name|getEscape
argument_list|()
argument_list|)
expr_stmt|;
comment|// Properly used
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
name|getActiveFormat
argument_list|()
operator|.
name|getEscapeCharacter
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldDisableHeader ()
specifier|public
name|void
name|shouldDisableHeader
parameter_list|()
block|{
name|CsvDataFormat
name|dataFormat
init|=
operator|new
name|CsvDataFormat
argument_list|()
operator|.
name|setHeaderDisabled
argument_list|(
literal|true
argument_list|)
operator|.
name|setHeader
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"a"
block|,
literal|"b"
block|,
literal|"c"
block|}
argument_list|)
decl_stmt|;
comment|// Properly saved
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dataFormat
operator|.
name|isHeaderDisabled
argument_list|()
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"a"
block|,
literal|"b"
block|,
literal|"c"
block|}
argument_list|,
name|dataFormat
operator|.
name|getHeader
argument_list|()
argument_list|)
expr_stmt|;
comment|// Properly used
name|assertNull
argument_list|(
name|dataFormat
operator|.
name|getActiveFormat
argument_list|()
operator|.
name|getHeader
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldOverrideHeader ()
specifier|public
name|void
name|shouldOverrideHeader
parameter_list|()
block|{
name|CsvDataFormat
name|dataFormat
init|=
operator|new
name|CsvDataFormat
argument_list|()
operator|.
name|setHeader
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"a"
block|,
literal|"b"
block|,
literal|"c"
block|}
argument_list|)
decl_stmt|;
comment|// Properly saved
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertArrayEquals
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"a"
block|,
literal|"b"
block|,
literal|"c"
block|}
argument_list|,
name|dataFormat
operator|.
name|getHeader
argument_list|()
argument_list|)
expr_stmt|;
comment|// Properly used
name|assertArrayEquals
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"a"
block|,
literal|"b"
block|,
literal|"c"
block|}
argument_list|,
name|dataFormat
operator|.
name|getActiveFormat
argument_list|()
operator|.
name|getHeader
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldAllowMissingColumnNames ()
specifier|public
name|void
name|shouldAllowMissingColumnNames
parameter_list|()
block|{
name|CsvDataFormat
name|dataFormat
init|=
operator|new
name|CsvDataFormat
argument_list|()
operator|.
name|setAllowMissingColumnNames
argument_list|(
literal|true
argument_list|)
decl_stmt|;
comment|// Properly saved
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|dataFormat
operator|.
name|getAllowMissingColumnNames
argument_list|()
argument_list|)
expr_stmt|;
comment|// Properly used
name|assertTrue
argument_list|(
name|dataFormat
operator|.
name|getActiveFormat
argument_list|()
operator|.
name|getAllowMissingColumnNames
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldNotAllowMissingColumnNames ()
specifier|public
name|void
name|shouldNotAllowMissingColumnNames
parameter_list|()
block|{
name|CsvDataFormat
name|dataFormat
init|=
operator|new
name|CsvDataFormat
argument_list|()
operator|.
name|setAllowMissingColumnNames
argument_list|(
literal|false
argument_list|)
decl_stmt|;
comment|// Properly saved
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|,
name|dataFormat
operator|.
name|getAllowMissingColumnNames
argument_list|()
argument_list|)
expr_stmt|;
comment|// Properly used
name|assertFalse
argument_list|(
name|dataFormat
operator|.
name|getActiveFormat
argument_list|()
operator|.
name|getAllowMissingColumnNames
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldIgnoreEmptyLines ()
specifier|public
name|void
name|shouldIgnoreEmptyLines
parameter_list|()
block|{
name|CsvDataFormat
name|dataFormat
init|=
operator|new
name|CsvDataFormat
argument_list|()
operator|.
name|setIgnoreEmptyLines
argument_list|(
literal|true
argument_list|)
decl_stmt|;
comment|// Properly saved
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|dataFormat
operator|.
name|getIgnoreEmptyLines
argument_list|()
argument_list|)
expr_stmt|;
comment|// Properly used
name|assertTrue
argument_list|(
name|dataFormat
operator|.
name|getActiveFormat
argument_list|()
operator|.
name|getIgnoreEmptyLines
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldNotIgnoreEmptyLines ()
specifier|public
name|void
name|shouldNotIgnoreEmptyLines
parameter_list|()
block|{
name|CsvDataFormat
name|dataFormat
init|=
operator|new
name|CsvDataFormat
argument_list|()
operator|.
name|setIgnoreEmptyLines
argument_list|(
literal|false
argument_list|)
decl_stmt|;
comment|// Properly saved
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|,
name|dataFormat
operator|.
name|getIgnoreEmptyLines
argument_list|()
argument_list|)
expr_stmt|;
comment|// Properly used
name|assertFalse
argument_list|(
name|dataFormat
operator|.
name|getActiveFormat
argument_list|()
operator|.
name|getIgnoreEmptyLines
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldIgnoreSurroundingSpaces ()
specifier|public
name|void
name|shouldIgnoreSurroundingSpaces
parameter_list|()
block|{
name|CsvDataFormat
name|dataFormat
init|=
operator|new
name|CsvDataFormat
argument_list|()
operator|.
name|setIgnoreSurroundingSpaces
argument_list|(
literal|true
argument_list|)
decl_stmt|;
comment|// Properly saved
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|dataFormat
operator|.
name|getIgnoreSurroundingSpaces
argument_list|()
argument_list|)
expr_stmt|;
comment|// Properly used
name|assertTrue
argument_list|(
name|dataFormat
operator|.
name|getActiveFormat
argument_list|()
operator|.
name|getIgnoreSurroundingSpaces
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldNotIgnoreSurroundingSpaces ()
specifier|public
name|void
name|shouldNotIgnoreSurroundingSpaces
parameter_list|()
block|{
name|CsvDataFormat
name|dataFormat
init|=
operator|new
name|CsvDataFormat
argument_list|()
operator|.
name|setIgnoreSurroundingSpaces
argument_list|(
literal|false
argument_list|)
decl_stmt|;
comment|// Properly saved
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|,
name|dataFormat
operator|.
name|getIgnoreSurroundingSpaces
argument_list|()
argument_list|)
expr_stmt|;
comment|// Properly used
name|assertFalse
argument_list|(
name|dataFormat
operator|.
name|getActiveFormat
argument_list|()
operator|.
name|getIgnoreSurroundingSpaces
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldDisableNullString ()
specifier|public
name|void
name|shouldDisableNullString
parameter_list|()
block|{
name|CsvDataFormat
name|dataFormat
init|=
operator|new
name|CsvDataFormat
argument_list|()
operator|.
name|setNullStringDisabled
argument_list|(
literal|true
argument_list|)
operator|.
name|setNullString
argument_list|(
literal|"****"
argument_list|)
decl_stmt|;
comment|// Properly saved
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dataFormat
operator|.
name|isNullStringDisabled
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"****"
argument_list|,
name|dataFormat
operator|.
name|getNullString
argument_list|()
argument_list|)
expr_stmt|;
comment|// Properly used
name|assertNull
argument_list|(
name|dataFormat
operator|.
name|getActiveFormat
argument_list|()
operator|.
name|getNullString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldOverrideNullString ()
specifier|public
name|void
name|shouldOverrideNullString
parameter_list|()
block|{
name|CsvDataFormat
name|dataFormat
init|=
operator|new
name|CsvDataFormat
argument_list|()
operator|.
name|setNullString
argument_list|(
literal|"****"
argument_list|)
decl_stmt|;
comment|// Properly saved
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"****"
argument_list|,
name|dataFormat
operator|.
name|getNullString
argument_list|()
argument_list|)
expr_stmt|;
comment|// Properly used
name|assertEquals
argument_list|(
literal|"****"
argument_list|,
name|dataFormat
operator|.
name|getActiveFormat
argument_list|()
operator|.
name|getNullString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldDisableQuote ()
specifier|public
name|void
name|shouldDisableQuote
parameter_list|()
block|{
name|CsvDataFormat
name|dataFormat
init|=
operator|new
name|CsvDataFormat
argument_list|()
operator|.
name|setQuoteDisabled
argument_list|(
literal|true
argument_list|)
operator|.
name|setQuote
argument_list|(
literal|'q'
argument_list|)
decl_stmt|;
comment|// Properly saved
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dataFormat
operator|.
name|isQuoteDisabled
argument_list|()
argument_list|)
expr_stmt|;
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
comment|// Properly used
name|assertNull
argument_list|(
name|dataFormat
operator|.
name|getActiveFormat
argument_list|()
operator|.
name|getQuoteCharacter
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldOverrideQuote ()
specifier|public
name|void
name|shouldOverrideQuote
parameter_list|()
block|{
name|CsvDataFormat
name|dataFormat
init|=
operator|new
name|CsvDataFormat
argument_list|()
operator|.
name|setQuote
argument_list|(
literal|'q'
argument_list|)
decl_stmt|;
comment|// Properly saved
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
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
comment|// Properly used
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
name|getActiveFormat
argument_list|()
operator|.
name|getQuoteCharacter
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldOverrideQuoteMode ()
specifier|public
name|void
name|shouldOverrideQuoteMode
parameter_list|()
block|{
name|CsvDataFormat
name|dataFormat
init|=
operator|new
name|CsvDataFormat
argument_list|()
operator|.
name|setQuoteMode
argument_list|(
name|QuoteMode
operator|.
name|ALL
argument_list|)
decl_stmt|;
comment|// Properly saved
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|QuoteMode
operator|.
name|ALL
argument_list|,
name|dataFormat
operator|.
name|getQuoteMode
argument_list|()
argument_list|)
expr_stmt|;
comment|// Properly used
name|assertEquals
argument_list|(
name|QuoteMode
operator|.
name|ALL
argument_list|,
name|dataFormat
operator|.
name|getActiveFormat
argument_list|()
operator|.
name|getQuoteMode
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldDisableRecordSeparator ()
specifier|public
name|void
name|shouldDisableRecordSeparator
parameter_list|()
block|{
name|CsvDataFormat
name|dataFormat
init|=
operator|new
name|CsvDataFormat
argument_list|()
operator|.
name|setRecordSeparatorDisabled
argument_list|(
literal|true
argument_list|)
operator|.
name|setRecordSeparator
argument_list|(
literal|"separator"
argument_list|)
decl_stmt|;
comment|// Properly saved
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dataFormat
operator|.
name|isRecordSeparatorDisabled
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"separator"
argument_list|,
name|dataFormat
operator|.
name|getRecordSeparator
argument_list|()
argument_list|)
expr_stmt|;
comment|// Properly used
name|assertNull
argument_list|(
name|dataFormat
operator|.
name|getActiveFormat
argument_list|()
operator|.
name|getRecordSeparator
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldOverrideRecordSeparator ()
specifier|public
name|void
name|shouldOverrideRecordSeparator
parameter_list|()
block|{
name|CsvDataFormat
name|dataFormat
init|=
operator|new
name|CsvDataFormat
argument_list|()
operator|.
name|setRecordSeparator
argument_list|(
literal|"separator"
argument_list|)
decl_stmt|;
comment|// Properly saved
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"separator"
argument_list|,
name|dataFormat
operator|.
name|getRecordSeparator
argument_list|()
argument_list|)
expr_stmt|;
comment|// Properly used
name|assertEquals
argument_list|(
literal|"separator"
argument_list|,
name|dataFormat
operator|.
name|getActiveFormat
argument_list|()
operator|.
name|getRecordSeparator
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldSkipHeaderRecord ()
specifier|public
name|void
name|shouldSkipHeaderRecord
parameter_list|()
block|{
name|CsvDataFormat
name|dataFormat
init|=
operator|new
name|CsvDataFormat
argument_list|()
operator|.
name|setSkipHeaderRecord
argument_list|(
literal|true
argument_list|)
decl_stmt|;
comment|// Properly saved
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|dataFormat
operator|.
name|getSkipHeaderRecord
argument_list|()
argument_list|)
expr_stmt|;
comment|// Properly used
name|assertTrue
argument_list|(
name|dataFormat
operator|.
name|getActiveFormat
argument_list|()
operator|.
name|getSkipHeaderRecord
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldNotSkipHeaderRecord ()
specifier|public
name|void
name|shouldNotSkipHeaderRecord
parameter_list|()
block|{
name|CsvDataFormat
name|dataFormat
init|=
operator|new
name|CsvDataFormat
argument_list|()
operator|.
name|setSkipHeaderRecord
argument_list|(
literal|false
argument_list|)
decl_stmt|;
comment|// Properly saved
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|,
name|dataFormat
operator|.
name|getSkipHeaderRecord
argument_list|()
argument_list|)
expr_stmt|;
comment|// Properly used
name|assertFalse
argument_list|(
name|dataFormat
operator|.
name|getActiveFormat
argument_list|()
operator|.
name|getSkipHeaderRecord
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldHandleLazyLoad ()
specifier|public
name|void
name|shouldHandleLazyLoad
parameter_list|()
block|{
name|CsvDataFormat
name|dataFormat
init|=
operator|new
name|CsvDataFormat
argument_list|()
operator|.
name|setLazyLoad
argument_list|(
literal|true
argument_list|)
decl_stmt|;
comment|// Properly saved
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dataFormat
operator|.
name|isLazyLoad
argument_list|()
argument_list|)
expr_stmt|;
comment|// Properly used (it doesn't modify the format)
name|assertEquals
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getActiveFormat
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldHandleUseMaps ()
specifier|public
name|void
name|shouldHandleUseMaps
parameter_list|()
block|{
name|CsvDataFormat
name|dataFormat
init|=
operator|new
name|CsvDataFormat
argument_list|()
operator|.
name|setUseMaps
argument_list|(
literal|true
argument_list|)
decl_stmt|;
comment|// Properly saved
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|dataFormat
operator|.
name|isUseMaps
argument_list|()
argument_list|)
expr_stmt|;
comment|// Properly used (it doesn't modify the format)
name|assertEquals
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getActiveFormat
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldHandleRecordConverter ()
specifier|public
name|void
name|shouldHandleRecordConverter
parameter_list|()
block|{
name|CsvRecordConverter
argument_list|<
name|String
argument_list|>
name|converter
init|=
operator|new
name|CsvRecordConverter
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|convertRecord
parameter_list|(
name|CSVRecord
name|record
parameter_list|)
block|{
return|return
name|record
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
decl_stmt|;
name|CsvDataFormat
name|dataFormat
init|=
operator|new
name|CsvDataFormat
argument_list|()
operator|.
name|setRecordConverter
argument_list|(
name|converter
argument_list|)
decl_stmt|;
comment|// Properly saved
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|converter
argument_list|,
name|dataFormat
operator|.
name|getRecordConverter
argument_list|()
argument_list|)
expr_stmt|;
comment|// Properly used (it doesn't modify the format)
name|assertEquals
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getActiveFormat
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTrim ()
specifier|public
name|void
name|testTrim
parameter_list|()
block|{
comment|// Set to TRUE
name|CsvDataFormat
name|dataFormat
init|=
operator|new
name|CsvDataFormat
argument_list|()
operator|.
name|setTrim
argument_list|(
literal|true
argument_list|)
decl_stmt|;
comment|// Properly saved
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|dataFormat
operator|.
name|getTrim
argument_list|()
argument_list|)
expr_stmt|;
comment|// Properly used
name|assertTrue
argument_list|(
name|dataFormat
operator|.
name|getActiveFormat
argument_list|()
operator|.
name|getTrim
argument_list|()
argument_list|)
expr_stmt|;
comment|// NOT set
name|dataFormat
operator|=
operator|new
name|CsvDataFormat
argument_list|()
expr_stmt|;
comment|// Properly saved
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|dataFormat
operator|.
name|getTrim
argument_list|()
argument_list|)
expr_stmt|;
comment|// Properly used
name|assertFalse
argument_list|(
name|dataFormat
operator|.
name|getActiveFormat
argument_list|()
operator|.
name|getTrim
argument_list|()
argument_list|)
expr_stmt|;
comment|// Set to false
name|dataFormat
operator|=
operator|new
name|CsvDataFormat
argument_list|()
operator|.
name|setTrim
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// Properly saved
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|,
name|dataFormat
operator|.
name|getTrim
argument_list|()
argument_list|)
expr_stmt|;
comment|// Properly used
name|assertFalse
argument_list|(
name|dataFormat
operator|.
name|getActiveFormat
argument_list|()
operator|.
name|getTrim
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testIgnoreHeaderCase ()
specifier|public
name|void
name|testIgnoreHeaderCase
parameter_list|()
block|{
comment|// Set to TRUE
name|CsvDataFormat
name|dataFormat
init|=
operator|new
name|CsvDataFormat
argument_list|()
operator|.
name|setIgnoreHeaderCase
argument_list|(
literal|true
argument_list|)
decl_stmt|;
comment|// Properly saved
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|dataFormat
operator|.
name|getIgnoreHeaderCase
argument_list|()
argument_list|)
expr_stmt|;
comment|// Properly used
name|assertTrue
argument_list|(
name|dataFormat
operator|.
name|getActiveFormat
argument_list|()
operator|.
name|getIgnoreHeaderCase
argument_list|()
argument_list|)
expr_stmt|;
comment|// NOT set
name|dataFormat
operator|=
operator|new
name|CsvDataFormat
argument_list|()
expr_stmt|;
comment|// Properly saved
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|dataFormat
operator|.
name|getIgnoreHeaderCase
argument_list|()
argument_list|)
expr_stmt|;
comment|// Properly used
name|assertFalse
argument_list|(
name|dataFormat
operator|.
name|getActiveFormat
argument_list|()
operator|.
name|getIgnoreHeaderCase
argument_list|()
argument_list|)
expr_stmt|;
comment|// Set to false
name|dataFormat
operator|=
operator|new
name|CsvDataFormat
argument_list|()
operator|.
name|setIgnoreHeaderCase
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// Properly saved
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|,
name|dataFormat
operator|.
name|getIgnoreHeaderCase
argument_list|()
argument_list|)
expr_stmt|;
comment|// Properly used
name|assertFalse
argument_list|(
name|dataFormat
operator|.
name|getActiveFormat
argument_list|()
operator|.
name|getIgnoreHeaderCase
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTrailingDelimiter ()
specifier|public
name|void
name|testTrailingDelimiter
parameter_list|()
block|{
comment|// Set to TRUE
name|CsvDataFormat
name|dataFormat
init|=
operator|new
name|CsvDataFormat
argument_list|()
operator|.
name|setTrailingDelimiter
argument_list|(
literal|true
argument_list|)
decl_stmt|;
comment|// Properly saved
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|,
name|dataFormat
operator|.
name|getTrailingDelimiter
argument_list|()
argument_list|)
expr_stmt|;
comment|// Properly used
name|assertTrue
argument_list|(
name|dataFormat
operator|.
name|getActiveFormat
argument_list|()
operator|.
name|getTrailingDelimiter
argument_list|()
argument_list|)
expr_stmt|;
comment|// NOT set
name|dataFormat
operator|=
operator|new
name|CsvDataFormat
argument_list|()
expr_stmt|;
comment|// Properly saved
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|dataFormat
operator|.
name|getTrailingDelimiter
argument_list|()
argument_list|)
expr_stmt|;
comment|// Properly used
name|assertFalse
argument_list|(
name|dataFormat
operator|.
name|getActiveFormat
argument_list|()
operator|.
name|getTrailingDelimiter
argument_list|()
argument_list|)
expr_stmt|;
comment|// Set to false
name|dataFormat
operator|=
operator|new
name|CsvDataFormat
argument_list|()
operator|.
name|setTrailingDelimiter
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// Properly saved
name|assertSame
argument_list|(
name|CSVFormat
operator|.
name|DEFAULT
argument_list|,
name|dataFormat
operator|.
name|getFormat
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|,
name|dataFormat
operator|.
name|getTrailingDelimiter
argument_list|()
argument_list|)
expr_stmt|;
comment|// Properly used
name|assertFalse
argument_list|(
name|dataFormat
operator|.
name|getActiveFormat
argument_list|()
operator|.
name|getTrailingDelimiter
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

