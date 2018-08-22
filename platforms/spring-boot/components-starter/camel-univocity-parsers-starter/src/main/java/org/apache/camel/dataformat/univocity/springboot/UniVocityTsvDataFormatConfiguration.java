begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.univocity.springboot
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
operator|.
name|springboot
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Generated
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
name|spring
operator|.
name|boot
operator|.
name|DataFormatConfigurationPropertiesCommon
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
comment|/**  * The uniVocity TSV data format is used for working with TSV (Tabular Separated  * Values) flat payloads.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_class
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.SpringBootAutoConfigurationMojo"
argument_list|)
annotation|@
name|ConfigurationProperties
argument_list|(
name|prefix
operator|=
literal|"camel.dataformat.univocity-tsv"
argument_list|)
DECL|class|UniVocityTsvDataFormatConfiguration
specifier|public
class|class
name|UniVocityTsvDataFormatConfiguration
extends|extends
name|DataFormatConfigurationPropertiesCommon
block|{
comment|/**      * Whether to enable auto configuration of the univocity-tsv data format.      * This is enabled by default.      */
DECL|field|enabled
specifier|private
name|Boolean
name|enabled
decl_stmt|;
comment|/**      * The escape character.      */
DECL|field|escapeChar
specifier|private
name|String
name|escapeChar
init|=
literal|"\\"
decl_stmt|;
comment|/**      * The string representation of a null value. The default value is null      */
DECL|field|nullValue
specifier|private
name|String
name|nullValue
decl_stmt|;
comment|/**      * Whether or not the empty lines must be ignored. The default value is true      */
DECL|field|skipEmptyLines
specifier|private
name|Boolean
name|skipEmptyLines
init|=
literal|true
decl_stmt|;
comment|/**      * Whether or not the trailing white spaces must ignored. The default value      * is true      */
DECL|field|ignoreTrailingWhitespaces
specifier|private
name|Boolean
name|ignoreTrailingWhitespaces
init|=
literal|true
decl_stmt|;
comment|/**      * Whether or not the leading white spaces must be ignored. The default      * value is true      */
DECL|field|ignoreLeadingWhitespaces
specifier|private
name|Boolean
name|ignoreLeadingWhitespaces
init|=
literal|true
decl_stmt|;
comment|/**      * Whether or not the headers are disabled. When defined, this option      * explicitly sets the headers as null which indicates that there is no      * header. The default value is false      */
DECL|field|headersDisabled
specifier|private
name|Boolean
name|headersDisabled
init|=
literal|false
decl_stmt|;
comment|/**      * Whether or not the header must be read in the first line of the test      * document The default value is false      */
DECL|field|headerExtractionEnabled
specifier|private
name|Boolean
name|headerExtractionEnabled
init|=
literal|false
decl_stmt|;
comment|/**      * The maximum number of record to read.      */
DECL|field|numberOfRecordsToRead
specifier|private
name|Integer
name|numberOfRecordsToRead
decl_stmt|;
comment|/**      * The String representation of an empty value      */
DECL|field|emptyValue
specifier|private
name|String
name|emptyValue
decl_stmt|;
comment|/**      * The line separator of the files The default value is to use the JVM      * platform line separator      */
DECL|field|lineSeparator
specifier|private
name|String
name|lineSeparator
decl_stmt|;
comment|/**      * The normalized line separator of the files The default value is a new      * line character.      */
DECL|field|normalizedLineSeparator
specifier|private
name|String
name|normalizedLineSeparator
decl_stmt|;
comment|/**      * The comment symbol. The default value is #      */
DECL|field|comment
specifier|private
name|String
name|comment
init|=
literal|"#"
decl_stmt|;
comment|/**      * Whether the unmarshalling should produce an iterator that reads the lines      * on the fly or if all the lines must be read at one. The default value is      * false      */
DECL|field|lazyLoad
specifier|private
name|Boolean
name|lazyLoad
init|=
literal|false
decl_stmt|;
comment|/**      * Whether the unmarshalling should produce maps for the lines values      * instead of lists. It requires to have header (either defined or      * collected). The default value is false      */
DECL|field|asMap
specifier|private
name|Boolean
name|asMap
init|=
literal|false
decl_stmt|;
comment|/**      * Whether the data format should set the Content-Type header with the type      * from the data format if the data format is capable of doing so. For      * example application/xml for data formats marshalling to XML, or      * application/json for data formats marshalling to JSon etc.      */
DECL|field|contentTypeHeader
specifier|private
name|Boolean
name|contentTypeHeader
init|=
literal|false
decl_stmt|;
DECL|method|getEscapeChar ()
specifier|public
name|String
name|getEscapeChar
parameter_list|()
block|{
return|return
name|escapeChar
return|;
block|}
DECL|method|setEscapeChar (String escapeChar)
specifier|public
name|void
name|setEscapeChar
parameter_list|(
name|String
name|escapeChar
parameter_list|)
block|{
name|this
operator|.
name|escapeChar
operator|=
name|escapeChar
expr_stmt|;
block|}
DECL|method|getNullValue ()
specifier|public
name|String
name|getNullValue
parameter_list|()
block|{
return|return
name|nullValue
return|;
block|}
DECL|method|setNullValue (String nullValue)
specifier|public
name|void
name|setNullValue
parameter_list|(
name|String
name|nullValue
parameter_list|)
block|{
name|this
operator|.
name|nullValue
operator|=
name|nullValue
expr_stmt|;
block|}
DECL|method|getSkipEmptyLines ()
specifier|public
name|Boolean
name|getSkipEmptyLines
parameter_list|()
block|{
return|return
name|skipEmptyLines
return|;
block|}
DECL|method|setSkipEmptyLines (Boolean skipEmptyLines)
specifier|public
name|void
name|setSkipEmptyLines
parameter_list|(
name|Boolean
name|skipEmptyLines
parameter_list|)
block|{
name|this
operator|.
name|skipEmptyLines
operator|=
name|skipEmptyLines
expr_stmt|;
block|}
DECL|method|getIgnoreTrailingWhitespaces ()
specifier|public
name|Boolean
name|getIgnoreTrailingWhitespaces
parameter_list|()
block|{
return|return
name|ignoreTrailingWhitespaces
return|;
block|}
DECL|method|setIgnoreTrailingWhitespaces (Boolean ignoreTrailingWhitespaces)
specifier|public
name|void
name|setIgnoreTrailingWhitespaces
parameter_list|(
name|Boolean
name|ignoreTrailingWhitespaces
parameter_list|)
block|{
name|this
operator|.
name|ignoreTrailingWhitespaces
operator|=
name|ignoreTrailingWhitespaces
expr_stmt|;
block|}
DECL|method|getIgnoreLeadingWhitespaces ()
specifier|public
name|Boolean
name|getIgnoreLeadingWhitespaces
parameter_list|()
block|{
return|return
name|ignoreLeadingWhitespaces
return|;
block|}
DECL|method|setIgnoreLeadingWhitespaces (Boolean ignoreLeadingWhitespaces)
specifier|public
name|void
name|setIgnoreLeadingWhitespaces
parameter_list|(
name|Boolean
name|ignoreLeadingWhitespaces
parameter_list|)
block|{
name|this
operator|.
name|ignoreLeadingWhitespaces
operator|=
name|ignoreLeadingWhitespaces
expr_stmt|;
block|}
DECL|method|getHeadersDisabled ()
specifier|public
name|Boolean
name|getHeadersDisabled
parameter_list|()
block|{
return|return
name|headersDisabled
return|;
block|}
DECL|method|setHeadersDisabled (Boolean headersDisabled)
specifier|public
name|void
name|setHeadersDisabled
parameter_list|(
name|Boolean
name|headersDisabled
parameter_list|)
block|{
name|this
operator|.
name|headersDisabled
operator|=
name|headersDisabled
expr_stmt|;
block|}
DECL|method|getHeaderExtractionEnabled ()
specifier|public
name|Boolean
name|getHeaderExtractionEnabled
parameter_list|()
block|{
return|return
name|headerExtractionEnabled
return|;
block|}
DECL|method|setHeaderExtractionEnabled (Boolean headerExtractionEnabled)
specifier|public
name|void
name|setHeaderExtractionEnabled
parameter_list|(
name|Boolean
name|headerExtractionEnabled
parameter_list|)
block|{
name|this
operator|.
name|headerExtractionEnabled
operator|=
name|headerExtractionEnabled
expr_stmt|;
block|}
DECL|method|getNumberOfRecordsToRead ()
specifier|public
name|Integer
name|getNumberOfRecordsToRead
parameter_list|()
block|{
return|return
name|numberOfRecordsToRead
return|;
block|}
DECL|method|setNumberOfRecordsToRead (Integer numberOfRecordsToRead)
specifier|public
name|void
name|setNumberOfRecordsToRead
parameter_list|(
name|Integer
name|numberOfRecordsToRead
parameter_list|)
block|{
name|this
operator|.
name|numberOfRecordsToRead
operator|=
name|numberOfRecordsToRead
expr_stmt|;
block|}
DECL|method|getEmptyValue ()
specifier|public
name|String
name|getEmptyValue
parameter_list|()
block|{
return|return
name|emptyValue
return|;
block|}
DECL|method|setEmptyValue (String emptyValue)
specifier|public
name|void
name|setEmptyValue
parameter_list|(
name|String
name|emptyValue
parameter_list|)
block|{
name|this
operator|.
name|emptyValue
operator|=
name|emptyValue
expr_stmt|;
block|}
DECL|method|getLineSeparator ()
specifier|public
name|String
name|getLineSeparator
parameter_list|()
block|{
return|return
name|lineSeparator
return|;
block|}
DECL|method|setLineSeparator (String lineSeparator)
specifier|public
name|void
name|setLineSeparator
parameter_list|(
name|String
name|lineSeparator
parameter_list|)
block|{
name|this
operator|.
name|lineSeparator
operator|=
name|lineSeparator
expr_stmt|;
block|}
DECL|method|getNormalizedLineSeparator ()
specifier|public
name|String
name|getNormalizedLineSeparator
parameter_list|()
block|{
return|return
name|normalizedLineSeparator
return|;
block|}
DECL|method|setNormalizedLineSeparator (String normalizedLineSeparator)
specifier|public
name|void
name|setNormalizedLineSeparator
parameter_list|(
name|String
name|normalizedLineSeparator
parameter_list|)
block|{
name|this
operator|.
name|normalizedLineSeparator
operator|=
name|normalizedLineSeparator
expr_stmt|;
block|}
DECL|method|getComment ()
specifier|public
name|String
name|getComment
parameter_list|()
block|{
return|return
name|comment
return|;
block|}
DECL|method|setComment (String comment)
specifier|public
name|void
name|setComment
parameter_list|(
name|String
name|comment
parameter_list|)
block|{
name|this
operator|.
name|comment
operator|=
name|comment
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
DECL|method|getAsMap ()
specifier|public
name|Boolean
name|getAsMap
parameter_list|()
block|{
return|return
name|asMap
return|;
block|}
DECL|method|setAsMap (Boolean asMap)
specifier|public
name|void
name|setAsMap
parameter_list|(
name|Boolean
name|asMap
parameter_list|)
block|{
name|this
operator|.
name|asMap
operator|=
name|asMap
expr_stmt|;
block|}
DECL|method|getContentTypeHeader ()
specifier|public
name|Boolean
name|getContentTypeHeader
parameter_list|()
block|{
return|return
name|contentTypeHeader
return|;
block|}
DECL|method|setContentTypeHeader (Boolean contentTypeHeader)
specifier|public
name|void
name|setContentTypeHeader
parameter_list|(
name|Boolean
name|contentTypeHeader
parameter_list|)
block|{
name|this
operator|.
name|contentTypeHeader
operator|=
name|contentTypeHeader
expr_stmt|;
block|}
block|}
end_class

end_unit

