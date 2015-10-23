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
name|com
operator|.
name|univocity
operator|.
name|parsers
operator|.
name|tsv
operator|.
name|TsvFormat
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
name|tsv
operator|.
name|TsvParser
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
name|tsv
operator|.
name|TsvParserSettings
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
name|tsv
operator|.
name|TsvWriter
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
name|tsv
operator|.
name|TsvWriterSettings
import|;
end_import

begin_comment
comment|/**  * This class is the data format that uses the TSV uniVocity parser.  */
end_comment

begin_class
DECL|class|UniVocityTsvDataFormat
specifier|public
class|class
name|UniVocityTsvDataFormat
extends|extends
name|AbstractUniVocityDataFormat
argument_list|<
name|TsvFormat
argument_list|,
name|TsvWriterSettings
argument_list|,
name|TsvWriter
argument_list|,
name|TsvParserSettings
argument_list|,
name|TsvParser
argument_list|,
name|UniVocityTsvDataFormat
argument_list|>
block|{
DECL|field|escapeChar
specifier|protected
name|Character
name|escapeChar
decl_stmt|;
comment|/**      * Gets the escape character symbol.      * If {@code null} then the default format value is used.      *      * @return the escape character symbol      * @see com.univocity.parsers.tsv.TsvFormat#getEscapeChar()      */
DECL|method|getEscapeChar ()
specifier|public
name|Character
name|getEscapeChar
parameter_list|()
block|{
return|return
name|escapeChar
return|;
block|}
comment|/**      * Sets the escape character symbol.      * If {@code null} then the default settings value is used.      *      * @param escapeChar the escape character symbol      * @return current data format instance, fluent API      * @see com.univocity.parsers.tsv.TsvFormat#setEscapeChar(char)      */
DECL|method|setEscapeChar (Character escapeChar)
specifier|public
name|UniVocityTsvDataFormat
name|setEscapeChar
parameter_list|(
name|Character
name|escapeChar
parameter_list|)
block|{
name|this
operator|.
name|escapeChar
operator|=
name|escapeChar
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
name|TsvWriterSettings
name|createWriterSettings
parameter_list|()
block|{
return|return
operator|new
name|TsvWriterSettings
argument_list|()
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
DECL|method|createWriter (Writer writer, TsvWriterSettings settings)
specifier|protected
name|TsvWriter
name|createWriter
parameter_list|(
name|Writer
name|writer
parameter_list|,
name|TsvWriterSettings
name|settings
parameter_list|)
block|{
return|return
operator|new
name|TsvWriter
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
name|TsvParserSettings
name|createParserSettings
parameter_list|()
block|{
return|return
operator|new
name|TsvParserSettings
argument_list|()
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
DECL|method|createParser (TsvParserSettings settings)
specifier|protected
name|TsvParser
name|createParser
parameter_list|(
name|TsvParserSettings
name|settings
parameter_list|)
block|{
return|return
operator|new
name|TsvParser
argument_list|(
name|settings
argument_list|)
return|;
block|}
comment|/**      * {@inheritDoc}      */
annotation|@
name|Override
DECL|method|configureFormat (TsvFormat format)
specifier|protected
name|void
name|configureFormat
parameter_list|(
name|TsvFormat
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
name|escapeChar
operator|!=
literal|null
condition|)
block|{
name|format
operator|.
name|setEscapeChar
argument_list|(
name|escapeChar
argument_list|)
expr_stmt|;
block|}
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
literal|"univocity-tsv"
return|;
block|}
block|}
end_class

end_unit

