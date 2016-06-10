begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.csv.springboot
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
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationProperties
import|;
end_import

begin_comment
comment|/**  * Camel CSV data format support  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.dataformat.csv"
argument_list|)
DECL|class|CsvDataFormatConfiguration
specifier|public
class|class
name|CsvDataFormatConfiguration
block|{
comment|/**      * The reference format to use it will be updated with the other format      * options the default value is CSVFormat.DEFAULT      */
DECL|field|formatRef
specifier|private
name|String
name|formatRef
decl_stmt|;
comment|/**      * The name of the format to use the default value is CSVFormat.DEFAULT      */
DECL|field|formatName
specifier|private
name|String
name|formatName
decl_stmt|;
comment|/**      * Disables the comment marker of the reference format.      */
DECL|field|commentMarkerDisabled
specifier|private
name|Boolean
name|commentMarkerDisabled
decl_stmt|;
comment|/**      * Sets the comment marker of the reference format.      */
DECL|field|commentMarker
specifier|private
name|String
name|commentMarker
decl_stmt|;
comment|/**      * Sets the delimiter to use. The default value is (comma)      */
DECL|field|delimiter
specifier|private
name|String
name|delimiter
decl_stmt|;
comment|/**      * Use for disabling using escape character      */
DECL|field|escapeDisabled
specifier|private
name|Boolean
name|escapeDisabled
decl_stmt|;
comment|/**      * Sets the escape character to use      */
DECL|field|escape
specifier|private
name|String
name|escape
decl_stmt|;
comment|/**      * Use for disabling headers      */
DECL|field|headerDisabled
specifier|private
name|Boolean
name|headerDisabled
decl_stmt|;
comment|/**      * To configure the CSV headers      */
DECL|field|header
specifier|private
name|List
argument_list|<
name|java
operator|.
name|lang
operator|.
name|String
argument_list|>
name|header
decl_stmt|;
comment|/**      * Whether to allow missing column names.      */
DECL|field|allowMissingColumnNames
specifier|private
name|Boolean
name|allowMissingColumnNames
decl_stmt|;
comment|/**      * Whether to ignore empty lines.      */
DECL|field|ignoreEmptyLines
specifier|private
name|Boolean
name|ignoreEmptyLines
decl_stmt|;
comment|/**      * Whether to ignore surrounding spaces      */
DECL|field|ignoreSurroundingSpaces
specifier|private
name|Boolean
name|ignoreSurroundingSpaces
decl_stmt|;
comment|/**      * Used to disable null strings      */
DECL|field|nullStringDisabled
specifier|private
name|Boolean
name|nullStringDisabled
decl_stmt|;
comment|/**      * Sets the null string      */
DECL|field|nullString
specifier|private
name|String
name|nullString
decl_stmt|;
comment|/**      * Used to disable quotes      */
DECL|field|quoteDisabled
specifier|private
name|Boolean
name|quoteDisabled
decl_stmt|;
comment|/**      * Sets the quote which by default is      */
DECL|field|quote
specifier|private
name|String
name|quote
decl_stmt|;
comment|/**      * Used for disabling record separator      */
DECL|field|recordSeparatorDisabled
specifier|private
name|String
name|recordSeparatorDisabled
decl_stmt|;
comment|/**      * Sets the record separator (aka new line) which by default is \r\n (CRLF)      */
DECL|field|recordSeparator
specifier|private
name|String
name|recordSeparator
decl_stmt|;
comment|/**      * Whether to skip the header record in the output      */
DECL|field|skipHeaderRecord
specifier|private
name|Boolean
name|skipHeaderRecord
decl_stmt|;
comment|/**      * Sets the quote mode      */
DECL|field|quoteMode
specifier|private
name|String
name|quoteMode
decl_stmt|;
comment|/**      * Whether the unmarshalling should produce an iterator that reads the lines      * on the fly or if all the lines must be read at one.      */
DECL|field|lazyLoad
specifier|private
name|Boolean
name|lazyLoad
decl_stmt|;
comment|/**      * Whether the unmarshalling should produce maps for the lines values      * instead of lists. It requires to have header (either defined or      * collected).      */
DECL|field|useMaps
specifier|private
name|Boolean
name|useMaps
decl_stmt|;
comment|/**      * Refers to a custom CsvRecordConverter to lookup from the registry to use.      */
DECL|field|recordConverterRef
specifier|private
name|String
name|recordConverterRef
decl_stmt|;
comment|/**      * Sets the value of the id property.      */
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
DECL|method|getFormatRef ()
specifier|public
name|String
name|getFormatRef
parameter_list|()
block|{
return|return
name|formatRef
return|;
block|}
DECL|method|setFormatRef (String formatRef)
specifier|public
name|void
name|setFormatRef
parameter_list|(
name|String
name|formatRef
parameter_list|)
block|{
name|this
operator|.
name|formatRef
operator|=
name|formatRef
expr_stmt|;
block|}
DECL|method|getFormatName ()
specifier|public
name|String
name|getFormatName
parameter_list|()
block|{
return|return
name|formatName
return|;
block|}
DECL|method|setFormatName (String formatName)
specifier|public
name|void
name|setFormatName
parameter_list|(
name|String
name|formatName
parameter_list|)
block|{
name|this
operator|.
name|formatName
operator|=
name|formatName
expr_stmt|;
block|}
DECL|method|getCommentMarkerDisabled ()
specifier|public
name|Boolean
name|getCommentMarkerDisabled
parameter_list|()
block|{
return|return
name|commentMarkerDisabled
return|;
block|}
DECL|method|setCommentMarkerDisabled (Boolean commentMarkerDisabled)
specifier|public
name|void
name|setCommentMarkerDisabled
parameter_list|(
name|Boolean
name|commentMarkerDisabled
parameter_list|)
block|{
name|this
operator|.
name|commentMarkerDisabled
operator|=
name|commentMarkerDisabled
expr_stmt|;
block|}
DECL|method|getCommentMarker ()
specifier|public
name|String
name|getCommentMarker
parameter_list|()
block|{
return|return
name|commentMarker
return|;
block|}
DECL|method|setCommentMarker (String commentMarker)
specifier|public
name|void
name|setCommentMarker
parameter_list|(
name|String
name|commentMarker
parameter_list|)
block|{
name|this
operator|.
name|commentMarker
operator|=
name|commentMarker
expr_stmt|;
block|}
DECL|method|getDelimiter ()
specifier|public
name|String
name|getDelimiter
parameter_list|()
block|{
return|return
name|delimiter
return|;
block|}
DECL|method|setDelimiter (String delimiter)
specifier|public
name|void
name|setDelimiter
parameter_list|(
name|String
name|delimiter
parameter_list|)
block|{
name|this
operator|.
name|delimiter
operator|=
name|delimiter
expr_stmt|;
block|}
DECL|method|getEscapeDisabled ()
specifier|public
name|Boolean
name|getEscapeDisabled
parameter_list|()
block|{
return|return
name|escapeDisabled
return|;
block|}
DECL|method|setEscapeDisabled (Boolean escapeDisabled)
specifier|public
name|void
name|setEscapeDisabled
parameter_list|(
name|Boolean
name|escapeDisabled
parameter_list|)
block|{
name|this
operator|.
name|escapeDisabled
operator|=
name|escapeDisabled
expr_stmt|;
block|}
DECL|method|getEscape ()
specifier|public
name|String
name|getEscape
parameter_list|()
block|{
return|return
name|escape
return|;
block|}
DECL|method|setEscape (String escape)
specifier|public
name|void
name|setEscape
parameter_list|(
name|String
name|escape
parameter_list|)
block|{
name|this
operator|.
name|escape
operator|=
name|escape
expr_stmt|;
block|}
DECL|method|getHeaderDisabled ()
specifier|public
name|Boolean
name|getHeaderDisabled
parameter_list|()
block|{
return|return
name|headerDisabled
return|;
block|}
DECL|method|setHeaderDisabled (Boolean headerDisabled)
specifier|public
name|void
name|setHeaderDisabled
parameter_list|(
name|Boolean
name|headerDisabled
parameter_list|)
block|{
name|this
operator|.
name|headerDisabled
operator|=
name|headerDisabled
expr_stmt|;
block|}
DECL|method|getHeader ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getHeader
parameter_list|()
block|{
return|return
name|header
return|;
block|}
DECL|method|setHeader (List<String> header)
specifier|public
name|void
name|setHeader
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|header
parameter_list|)
block|{
name|this
operator|.
name|header
operator|=
name|header
expr_stmt|;
block|}
DECL|method|getAllowMissingColumnNames ()
specifier|public
name|Boolean
name|getAllowMissingColumnNames
parameter_list|()
block|{
return|return
name|allowMissingColumnNames
return|;
block|}
DECL|method|setAllowMissingColumnNames (Boolean allowMissingColumnNames)
specifier|public
name|void
name|setAllowMissingColumnNames
parameter_list|(
name|Boolean
name|allowMissingColumnNames
parameter_list|)
block|{
name|this
operator|.
name|allowMissingColumnNames
operator|=
name|allowMissingColumnNames
expr_stmt|;
block|}
DECL|method|getIgnoreEmptyLines ()
specifier|public
name|Boolean
name|getIgnoreEmptyLines
parameter_list|()
block|{
return|return
name|ignoreEmptyLines
return|;
block|}
DECL|method|setIgnoreEmptyLines (Boolean ignoreEmptyLines)
specifier|public
name|void
name|setIgnoreEmptyLines
parameter_list|(
name|Boolean
name|ignoreEmptyLines
parameter_list|)
block|{
name|this
operator|.
name|ignoreEmptyLines
operator|=
name|ignoreEmptyLines
expr_stmt|;
block|}
DECL|method|getIgnoreSurroundingSpaces ()
specifier|public
name|Boolean
name|getIgnoreSurroundingSpaces
parameter_list|()
block|{
return|return
name|ignoreSurroundingSpaces
return|;
block|}
DECL|method|setIgnoreSurroundingSpaces (Boolean ignoreSurroundingSpaces)
specifier|public
name|void
name|setIgnoreSurroundingSpaces
parameter_list|(
name|Boolean
name|ignoreSurroundingSpaces
parameter_list|)
block|{
name|this
operator|.
name|ignoreSurroundingSpaces
operator|=
name|ignoreSurroundingSpaces
expr_stmt|;
block|}
DECL|method|getNullStringDisabled ()
specifier|public
name|Boolean
name|getNullStringDisabled
parameter_list|()
block|{
return|return
name|nullStringDisabled
return|;
block|}
DECL|method|setNullStringDisabled (Boolean nullStringDisabled)
specifier|public
name|void
name|setNullStringDisabled
parameter_list|(
name|Boolean
name|nullStringDisabled
parameter_list|)
block|{
name|this
operator|.
name|nullStringDisabled
operator|=
name|nullStringDisabled
expr_stmt|;
block|}
DECL|method|getNullString ()
specifier|public
name|String
name|getNullString
parameter_list|()
block|{
return|return
name|nullString
return|;
block|}
DECL|method|setNullString (String nullString)
specifier|public
name|void
name|setNullString
parameter_list|(
name|String
name|nullString
parameter_list|)
block|{
name|this
operator|.
name|nullString
operator|=
name|nullString
expr_stmt|;
block|}
DECL|method|getQuoteDisabled ()
specifier|public
name|Boolean
name|getQuoteDisabled
parameter_list|()
block|{
return|return
name|quoteDisabled
return|;
block|}
DECL|method|setQuoteDisabled (Boolean quoteDisabled)
specifier|public
name|void
name|setQuoteDisabled
parameter_list|(
name|Boolean
name|quoteDisabled
parameter_list|)
block|{
name|this
operator|.
name|quoteDisabled
operator|=
name|quoteDisabled
expr_stmt|;
block|}
DECL|method|getQuote ()
specifier|public
name|String
name|getQuote
parameter_list|()
block|{
return|return
name|quote
return|;
block|}
DECL|method|setQuote (String quote)
specifier|public
name|void
name|setQuote
parameter_list|(
name|String
name|quote
parameter_list|)
block|{
name|this
operator|.
name|quote
operator|=
name|quote
expr_stmt|;
block|}
DECL|method|getRecordSeparatorDisabled ()
specifier|public
name|String
name|getRecordSeparatorDisabled
parameter_list|()
block|{
return|return
name|recordSeparatorDisabled
return|;
block|}
DECL|method|setRecordSeparatorDisabled (String recordSeparatorDisabled)
specifier|public
name|void
name|setRecordSeparatorDisabled
parameter_list|(
name|String
name|recordSeparatorDisabled
parameter_list|)
block|{
name|this
operator|.
name|recordSeparatorDisabled
operator|=
name|recordSeparatorDisabled
expr_stmt|;
block|}
DECL|method|getRecordSeparator ()
specifier|public
name|String
name|getRecordSeparator
parameter_list|()
block|{
return|return
name|recordSeparator
return|;
block|}
DECL|method|setRecordSeparator (String recordSeparator)
specifier|public
name|void
name|setRecordSeparator
parameter_list|(
name|String
name|recordSeparator
parameter_list|)
block|{
name|this
operator|.
name|recordSeparator
operator|=
name|recordSeparator
expr_stmt|;
block|}
DECL|method|getSkipHeaderRecord ()
specifier|public
name|Boolean
name|getSkipHeaderRecord
parameter_list|()
block|{
return|return
name|skipHeaderRecord
return|;
block|}
DECL|method|setSkipHeaderRecord (Boolean skipHeaderRecord)
specifier|public
name|void
name|setSkipHeaderRecord
parameter_list|(
name|Boolean
name|skipHeaderRecord
parameter_list|)
block|{
name|this
operator|.
name|skipHeaderRecord
operator|=
name|skipHeaderRecord
expr_stmt|;
block|}
DECL|method|getQuoteMode ()
specifier|public
name|String
name|getQuoteMode
parameter_list|()
block|{
return|return
name|quoteMode
return|;
block|}
DECL|method|setQuoteMode (String quoteMode)
specifier|public
name|void
name|setQuoteMode
parameter_list|(
name|String
name|quoteMode
parameter_list|)
block|{
name|this
operator|.
name|quoteMode
operator|=
name|quoteMode
expr_stmt|;
block|}
DECL|method|getLazyLoad ()
specifier|public
name|Boolean
name|getLazyLoad
parameter_list|()
block|{
return|return
name|lazyLoad
return|;
block|}
DECL|method|setLazyLoad (Boolean lazyLoad)
specifier|public
name|void
name|setLazyLoad
parameter_list|(
name|Boolean
name|lazyLoad
parameter_list|)
block|{
name|this
operator|.
name|lazyLoad
operator|=
name|lazyLoad
expr_stmt|;
block|}
DECL|method|getUseMaps ()
specifier|public
name|Boolean
name|getUseMaps
parameter_list|()
block|{
return|return
name|useMaps
return|;
block|}
DECL|method|setUseMaps (Boolean useMaps)
specifier|public
name|void
name|setUseMaps
parameter_list|(
name|Boolean
name|useMaps
parameter_list|)
block|{
name|this
operator|.
name|useMaps
operator|=
name|useMaps
expr_stmt|;
block|}
DECL|method|getRecordConverterRef ()
specifier|public
name|String
name|getRecordConverterRef
parameter_list|()
block|{
return|return
name|recordConverterRef
return|;
block|}
DECL|method|setRecordConverterRef (String recordConverterRef)
specifier|public
name|void
name|setRecordConverterRef
parameter_list|(
name|String
name|recordConverterRef
parameter_list|)
block|{
name|this
operator|.
name|recordConverterRef
operator|=
name|recordConverterRef
expr_stmt|;
block|}
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|setId (String id)
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
block|}
end_class

end_unit

