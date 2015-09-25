begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|fixed
operator|.
name|FixedWidthFieldLengths
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
name|fixed
operator|.
name|FixedWidthFormat
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
name|fixed
operator|.
name|FixedWidthParser
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
name|fixed
operator|.
name|FixedWidthParserSettings
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
name|fixed
operator|.
name|FixedWidthWriter
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
name|fixed
operator|.
name|FixedWidthWriterSettings
import|;
end_import

begin_comment
comment|/**  * This class is the data format that uses the fixed-width uniVocity parser.  */
end_comment

begin_class
DECL|class|UniVocityFixedWidthDataFormat
specifier|public
class|class
name|UniVocityFixedWidthDataFormat
extends|extends
name|AbstractUniVocityDataFormat
argument_list|<
name|FixedWidthFormat
argument_list|,
name|FixedWidthWriterSettings
argument_list|,
name|FixedWidthWriter
argument_list|,
name|FixedWidthParserSettings
argument_list|,
name|FixedWidthParser
argument_list|,
name|UniVocityFixedWidthDataFormat
argument_list|>
block|{
DECL|field|fieldLengths
specifier|protected
name|int
index|[]
name|fieldLengths
decl_stmt|;
DECL|field|skipTrailingCharsUntilNewline
specifier|protected
name|Boolean
name|skipTrailingCharsUntilNewline
decl_stmt|;
DECL|field|recordEndsOnNewline
specifier|protected
name|Boolean
name|recordEndsOnNewline
decl_stmt|;
DECL|field|padding
specifier|protected
name|Character
name|padding
decl_stmt|;
comment|/**      * Gets the field lengths.      * It's used to construct uniVocity {@link com.univocity.parsers.fixed.FixedWidthFieldLengths} instance.      *      * @return the field lengths      */
DECL|method|getFieldLengths ()
specifier|public
name|int
index|[]
name|getFieldLengths
parameter_list|()
block|{
return|return
name|fieldLengths
return|;
block|}
comment|/**      * Sets the field lengths      * It's used to construct uniVocity {@link com.univocity.parsers.fixed.FixedWidthFieldLengths} instance.      *      * @param fieldLengths the field length      * @return current data format instance, fluent API      */
DECL|method|setFieldLengths (int[] fieldLengths)
specifier|public
name|UniVocityFixedWidthDataFormat
name|setFieldLengths
parameter_list|(
name|int
index|[]
name|fieldLengths
parameter_list|)
block|{
name|this
operator|.
name|fieldLengths
operator|=
name|fieldLengths
expr_stmt|;
name|reset
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Gets whether or not trailing characters until new line must be ignored.      *      * @return whether or not trailing characters until new line must be ignored      * @see com.univocity.parsers.fixed.FixedWidthParserSettings#getSkipTrailingCharsUntilNewline()      */
DECL|method|getSkipTrailingCharsUntilNewline ()
specifier|public
name|Boolean
name|getSkipTrailingCharsUntilNewline
parameter_list|()
block|{
return|return
name|skipTrailingCharsUntilNewline
return|;
block|}
comment|/**      * Sets whether or not trailing characters until new line must be ignored.      *      * @param skipTrailingCharsUntilNewline whether or not trailing characters until new line must be ignored      * @return current data format instance, fluent API      * @see com.univocity.parsers.fixed.FixedWidthParserSettings#setSkipTrailingCharsUntilNewline(boolean)      */
DECL|method|setSkipTrailingCharsUntilNewline (Boolean skipTrailingCharsUntilNewline)
specifier|public
name|UniVocityFixedWidthDataFormat
name|setSkipTrailingCharsUntilNewline
parameter_list|(
name|Boolean
name|skipTrailingCharsUntilNewline
parameter_list|)
block|{
name|this
operator|.
name|skipTrailingCharsUntilNewline
operator|=
name|skipTrailingCharsUntilNewline
expr_stmt|;
name|reset
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Gets whether or not the record ends on new line.      *      * @return whether or not the record ends on new line      * @see com.univocity.parsers.fixed.FixedWidthParserSettings#getRecordEndsOnNewline()      */
DECL|method|getRecordEndsOnNewline ()
specifier|public
name|Boolean
name|getRecordEndsOnNewline
parameter_list|()
block|{
return|return
name|recordEndsOnNewline
return|;
block|}
comment|/**      * Sets whether or not the record ends on new line      *      * @param recordEndsOnNewline whether or not the record ends on new line      * @return current data format instance, fluent API      * @see com.univocity.parsers.fixed.FixedWidthParserSettings#setRecordEndsOnNewline(boolean)      */
DECL|method|setRecordEndsOnNewline (Boolean recordEndsOnNewline)
specifier|public
name|UniVocityFixedWidthDataFormat
name|setRecordEndsOnNewline
parameter_list|(
name|Boolean
name|recordEndsOnNewline
parameter_list|)
block|{
name|this
operator|.
name|recordEndsOnNewline
operator|=
name|recordEndsOnNewline
expr_stmt|;
name|reset
argument_list|()
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Gets the padding symbol.      * If {@code null} then the default format value is used.      *      * @return the padding symbol      * @see com.univocity.parsers.fixed.FixedWidthFormat#getPadding()      */
DECL|method|getPadding ()
specifier|public
name|Character
name|getPadding
parameter_list|()
block|{
return|return
name|padding
return|;
block|}
comment|/**      * Sets the padding symbol.      * If {@code null} then the default format value is used.      *      * @param padding the padding symbol      * @return current data format instance, fluent API      * @see com.univocity.parsers.fixed.FixedWidthFormat#setPadding(char)      */
DECL|method|setPadding (Character padding)
specifier|public
name|UniVocityFixedWidthDataFormat
name|setPadding
parameter_list|(
name|Character
name|padding
parameter_list|)
block|{
name|this
operator|.
name|padding
operator|=
name|padding
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
name|FixedWidthWriterSettings
name|createWriterSettings
parameter_list|()
block|{
return|return
operator|new
name|FixedWidthWriterSettings
argument_list|(
name|createFixedWidthFieldLengths
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
DECL|method|createWriter (Writer writer, FixedWidthWriterSettings settings)
specifier|protected
name|FixedWidthWriter
name|createWriter
parameter_list|(
name|Writer
name|writer
parameter_list|,
name|FixedWidthWriterSettings
name|settings
parameter_list|)
block|{
return|return
operator|new
name|FixedWidthWriter
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
name|FixedWidthParserSettings
name|createParserSettings
parameter_list|()
block|{
return|return
operator|new
name|FixedWidthParserSettings
argument_list|(
name|createFixedWidthFieldLengths
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|configureParserSettings (FixedWidthParserSettings settings)
specifier|protected
name|void
name|configureParserSettings
parameter_list|(
name|FixedWidthParserSettings
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
name|skipTrailingCharsUntilNewline
operator|!=
literal|null
condition|)
block|{
name|settings
operator|.
name|setSkipTrailingCharsUntilNewline
argument_list|(
name|skipTrailingCharsUntilNewline
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|recordEndsOnNewline
operator|!=
literal|null
condition|)
block|{
name|settings
operator|.
name|setRecordEndsOnNewline
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
DECL|method|createParser (FixedWidthParserSettings settings)
specifier|protected
name|FixedWidthParser
name|createParser
parameter_list|(
name|FixedWidthParserSettings
name|settings
parameter_list|)
block|{
return|return
operator|new
name|FixedWidthParser
argument_list|(
name|settings
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
DECL|method|configureFormat (FixedWidthFormat format)
specifier|protected
name|void
name|configureFormat
parameter_list|(
name|FixedWidthFormat
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
name|padding
operator|!=
literal|null
condition|)
block|{
name|format
operator|.
name|setPadding
argument_list|(
name|padding
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Creates the {@link com.univocity.parsers.fixed.FixedWidthFieldLengths} instance based on the headers and field      * lengths.      *      * @return new {@code FixedWidthFieldLengths} based on the header and field lengthsl      */
DECL|method|createFixedWidthFieldLengths ()
specifier|private
name|FixedWidthFieldLengths
name|createFixedWidthFieldLengths
parameter_list|()
block|{
comment|// Ensure that the field lengths have been defined.
if|if
condition|(
name|fieldLengths
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The fieldLengths must have been defined in order to use the fixed-width format."
argument_list|)
throw|;
block|}
comment|// If there's no header then we only use their length
if|if
condition|(
name|headers
operator|==
literal|null
condition|)
block|{
return|return
operator|new
name|FixedWidthFieldLengths
argument_list|(
name|fieldLengths
argument_list|)
return|;
block|}
comment|// Use both headers and field lengths (same size and no duplicate headers)
if|if
condition|(
name|fieldLengths
operator|.
name|length
operator|!=
name|headers
operator|.
name|length
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The headers and fieldLengths must have the same number of element in order to use the fixed-width format."
argument_list|)
throw|;
block|}
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|fields
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|headers
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|fields
operator|.
name|put
argument_list|(
name|headers
index|[
name|i
index|]
argument_list|,
name|fieldLengths
index|[
name|i
index|]
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|fields
operator|.
name|size
argument_list|()
operator|!=
name|headers
operator|.
name|length
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The headers cannot have duplicates in order to use the fixed-width format."
argument_list|)
throw|;
block|}
return|return
operator|new
name|FixedWidthFieldLengths
argument_list|(
name|fields
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getDataFormatName ()
specifier|public
name|String
name|getDataFormatName
parameter_list|()
block|{
return|return
literal|"univocity-fixed"
return|;
block|}
block|}
end_class

end_unit

