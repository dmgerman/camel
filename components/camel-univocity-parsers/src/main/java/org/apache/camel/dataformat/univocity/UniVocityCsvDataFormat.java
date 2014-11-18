begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *<p/>  * http://www.apache.org/licenses/LICENSE-2.0  *<p/>  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|io
operator|.
name|Writer
import|;
end_import

begin_import
import|import
name|com
operator|.
name|univocity
operator|.
name|parsers
operator|.
name|csv
operator|.
name|CsvFormat
import|;
end_import

begin_import
import|import
name|com
operator|.
name|univocity
operator|.
name|parsers
operator|.
name|csv
operator|.
name|CsvParser
import|;
end_import

begin_import
import|import
name|com
operator|.
name|univocity
operator|.
name|parsers
operator|.
name|csv
operator|.
name|CsvParserSettings
import|;
end_import

begin_import
import|import
name|com
operator|.
name|univocity
operator|.
name|parsers
operator|.
name|csv
operator|.
name|CsvWriter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|univocity
operator|.
name|parsers
operator|.
name|csv
operator|.
name|CsvWriterSettings
import|;
end_import

begin_comment
comment|/**  * This class is the data format that uses the CSV uniVocity parser.  */
end_comment

begin_class
DECL|class|UniVocityCsvDataFormat
specifier|public
class|class
name|UniVocityCsvDataFormat
extends|extends
name|AbstractUniVocityDataFormat
argument_list|<
name|CsvFormat
argument_list|,
name|CsvWriterSettings
argument_list|,
name|CsvWriter
argument_list|,
name|CsvParserSettings
argument_list|,
name|CsvParser
argument_list|,
name|UniVocityCsvDataFormat
argument_list|>
block|{
DECL|field|quoteAllFields
specifier|protected
name|Boolean
name|quoteAllFields
decl_stmt|;
DECL|field|quote
specifier|protected
name|Character
name|quote
decl_stmt|;
DECL|field|quoteEscape
specifier|protected
name|Character
name|quoteEscape
decl_stmt|;
DECL|field|delimiter
specifier|protected
name|Character
name|delimiter
decl_stmt|;
comment|/**      * Gets whether or not all fields must be quoted.      * If {@code null} then the default settings value is used.      *      * @return whether or not all fields must be quoted      * @see com.univocity.parsers.csv.CsvWriterSettings#getQuoteAllFields()      */
DECL|method|getQuoteAllFields ()
specifier|public
name|Boolean
name|getQuoteAllFields
parameter_list|()
block|{
return|return
name|quoteAllFields
return|;
block|}
comment|/**      * Gets whether or not all fields must be quoted.      * If {@code null} then the default settings value is used.      *      * @param quoteAllFields whether or not all fields must be quoted      * @return current data format instance, fluent API      * @see com.univocity.parsers.csv.CsvWriterSettings#setQuoteAllFields(boolean)      */
DECL|method|setQuoteAllFields (Boolean quoteAllFields)
specifier|public
name|UniVocityCsvDataFormat
name|setQuoteAllFields
parameter_list|(
name|Boolean
name|quoteAllFields
parameter_list|)
block|{
name|this
operator|.
name|quoteAllFields
operator|=
name|quoteAllFields
expr_stmt|;
name|reset
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Gets the quote symbol.      * If {@code null} then the default format value is used.      *      * @return the quote symbol      * @see com.univocity.parsers.csv.CsvFormat#getQuote()      */
DECL|method|getQuote ()
specifier|public
name|Character
name|getQuote
parameter_list|()
block|{
return|return
name|quote
return|;
block|}
comment|/**      * Sets the quote symbol.      * If {@code null} then the default format value is used.      *      * @param quote the quote symbol      * @return current data format instance, fluent API      * @see com.univocity.parsers.csv.CsvFormat#setQuote(char)      */
DECL|method|setQuote (Character quote)
specifier|public
name|UniVocityCsvDataFormat
name|setQuote
parameter_list|(
name|Character
name|quote
parameter_list|)
block|{
name|this
operator|.
name|quote
operator|=
name|quote
expr_stmt|;
name|reset
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Gets the quote escape symbol.      * If {@code null} then the default format value is used.      *      * @return the quote escape symbol      * @see com.univocity.parsers.csv.CsvFormat#getQuoteEscape()      */
DECL|method|getQuoteEscape ()
specifier|public
name|Character
name|getQuoteEscape
parameter_list|()
block|{
return|return
name|quoteEscape
return|;
block|}
comment|/**      * Sets the quote escape symbol.      * If {@code null} then the default format value is used.      *      * @param quoteEscape the quote escape symbol      * @return current data format instance, fluent API      * @see com.univocity.parsers.csv.CsvFormat#setQuoteEscape(char)      */
DECL|method|setQuoteEscape (Character quoteEscape)
specifier|public
name|UniVocityCsvDataFormat
name|setQuoteEscape
parameter_list|(
name|Character
name|quoteEscape
parameter_list|)
block|{
name|this
operator|.
name|quoteEscape
operator|=
name|quoteEscape
expr_stmt|;
name|reset
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Gets the delimiter symbol.      * If {@code null} then the default format value is used.      *      * @return the delimiter symbol      * @see com.univocity.parsers.csv.CsvFormat#getDelimiter()      */
DECL|method|getDelimiter ()
specifier|public
name|Character
name|getDelimiter
parameter_list|()
block|{
return|return
name|delimiter
return|;
block|}
comment|/**      * Sets the delimiter symbol.      * If {@code null} then the default format value is used.      *      * @param delimiter the delimiter symbol      * @return current data format instance, fluent API      * @see com.univocity.parsers.csv.CsvFormat#setDelimiter(char)      */
DECL|method|setDelimiter (Character delimiter)
specifier|public
name|UniVocityCsvDataFormat
name|setDelimiter
parameter_list|(
name|Character
name|delimiter
parameter_list|)
block|{
name|this
operator|.
name|delimiter
operator|=
name|delimiter
expr_stmt|;
name|reset
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
DECL|method|createWriterSettings ()
specifier|protected
name|CsvWriterSettings
name|createWriterSettings
parameter_list|()
block|{
return|return
operator|new
name|CsvWriterSettings
argument_list|()
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
DECL|method|configureWriterSettings (CsvWriterSettings settings)
specifier|protected
name|void
name|configureWriterSettings
parameter_list|(
name|CsvWriterSettings
name|settings
parameter_list|)
block|{
name|super
operator|.
name|configureWriterSettings
argument_list|(
name|settings
argument_list|)
expr_stmt|;
if|if
condition|(
name|quoteAllFields
operator|!=
literal|null
condition|)
block|{
name|settings
operator|.
name|setQuoteAllFields
argument_list|(
name|quoteAllFields
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
DECL|method|createWriter (Writer writer, CsvWriterSettings settings)
specifier|protected
name|CsvWriter
name|createWriter
parameter_list|(
name|Writer
name|writer
parameter_list|,
name|CsvWriterSettings
name|settings
parameter_list|)
block|{
return|return
operator|new
name|CsvWriter
argument_list|(
name|writer
argument_list|,
name|settings
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
DECL|method|createParserSettings ()
specifier|protected
name|CsvParserSettings
name|createParserSettings
parameter_list|()
block|{
return|return
operator|new
name|CsvParserSettings
argument_list|()
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
DECL|method|configureParserSettings (CsvParserSettings settings)
specifier|protected
name|void
name|configureParserSettings
parameter_list|(
name|CsvParserSettings
name|settings
parameter_list|)
block|{
name|super
operator|.
name|configureParserSettings
argument_list|(
name|settings
argument_list|)
expr_stmt|;
if|if
condition|(
name|emptyValue
operator|!=
literal|null
condition|)
block|{
name|settings
operator|.
name|setEmptyValue
argument_list|(
name|emptyValue
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
DECL|method|createParser (CsvParserSettings settings)
specifier|protected
name|CsvParser
name|createParser
parameter_list|(
name|CsvParserSettings
name|settings
parameter_list|)
block|{
return|return
operator|new
name|CsvParser
argument_list|(
name|settings
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
DECL|method|configureFormat (CsvFormat format)
specifier|protected
name|void
name|configureFormat
parameter_list|(
name|CsvFormat
name|format
parameter_list|)
block|{
name|super
operator|.
name|configureFormat
argument_list|(
name|format
argument_list|)
expr_stmt|;
if|if
condition|(
name|quote
operator|!=
literal|null
condition|)
block|{
name|format
operator|.
name|setQuote
argument_list|(
name|quote
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|quoteEscape
operator|!=
literal|null
condition|)
block|{
name|format
operator|.
name|setQuoteEscape
argument_list|(
name|quoteEscape
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|delimiter
operator|!=
literal|null
condition|)
block|{
name|format
operator|.
name|setDelimiter
argument_list|(
name|delimiter
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

