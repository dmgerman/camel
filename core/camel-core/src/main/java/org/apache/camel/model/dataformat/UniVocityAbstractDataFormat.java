begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.dataformat
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|dataformat
package|;
end_package

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
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAttribute
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElementRef
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
name|CamelContext
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
name|model
operator|.
name|DataFormatDefinition
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
name|spi
operator|.
name|DataFormat
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
name|spi
operator|.
name|Metadata
import|;
end_import

begin_comment
comment|/**  * Represents the common parts of all uniVocity {@link org.apache.camel.spi.DataFormat} parsers.  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"dataformat,transformation,csv"
argument_list|,
name|title
operator|=
literal|"uniVocity"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|UniVocityAbstractDataFormat
specifier|public
specifier|abstract
class|class
name|UniVocityAbstractDataFormat
extends|extends
name|DataFormatDefinition
block|{
annotation|@
name|XmlAttribute
DECL|field|nullValue
specifier|protected
name|String
name|nullValue
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|skipEmptyLines
specifier|protected
name|Boolean
name|skipEmptyLines
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|ignoreTrailingWhitespaces
specifier|protected
name|Boolean
name|ignoreTrailingWhitespaces
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|ignoreLeadingWhitespaces
specifier|protected
name|Boolean
name|ignoreLeadingWhitespaces
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|headersDisabled
specifier|protected
name|Boolean
name|headersDisabled
decl_stmt|;
annotation|@
name|XmlElementRef
DECL|field|headers
specifier|protected
name|List
argument_list|<
name|UniVocityHeader
argument_list|>
name|headers
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|headerExtractionEnabled
specifier|protected
name|Boolean
name|headerExtractionEnabled
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|numberOfRecordsToRead
specifier|protected
name|Integer
name|numberOfRecordsToRead
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|emptyValue
specifier|protected
name|String
name|emptyValue
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|lineSeparator
specifier|protected
name|String
name|lineSeparator
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"\\n"
argument_list|)
DECL|field|normalizedLineSeparator
specifier|protected
name|String
name|normalizedLineSeparator
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"#"
argument_list|)
DECL|field|comment
specifier|protected
name|String
name|comment
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|lazyLoad
specifier|protected
name|Boolean
name|lazyLoad
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|asMap
specifier|protected
name|Boolean
name|asMap
decl_stmt|;
DECL|method|UniVocityAbstractDataFormat ()
specifier|protected
name|UniVocityAbstractDataFormat
parameter_list|()
block|{
comment|// This constructor is needed by jaxb for schema generation
block|}
DECL|method|UniVocityAbstractDataFormat (String dataFormatName)
specifier|protected
name|UniVocityAbstractDataFormat
parameter_list|(
name|String
name|dataFormatName
parameter_list|)
block|{
name|super
argument_list|(
name|dataFormatName
argument_list|)
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
comment|/**      * The string representation of a null value.      *<p/>      * The default value is null      */
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
comment|/**      * Whether or not the empty lines must be ignored.      *<p/>      * The default value is true      */
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
comment|/**      * Whether or not the trailing white spaces must ignored.      *<p/>      * The default value is true      */
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
comment|/**      * Whether or not the leading white spaces must be ignored.      *<p/>      * The default value is true      */
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
comment|/**      * Whether or not the headers are disabled. When defined, this option explicitly sets the headers as null which indicates that there is no header.      *<p/>      * The default value is false      */
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
DECL|method|getHeaders ()
specifier|public
name|List
argument_list|<
name|UniVocityHeader
argument_list|>
name|getHeaders
parameter_list|()
block|{
return|return
name|headers
return|;
block|}
comment|/**      * The headers to use.      */
DECL|method|setHeaders (List<UniVocityHeader> headers)
specifier|public
name|void
name|setHeaders
parameter_list|(
name|List
argument_list|<
name|UniVocityHeader
argument_list|>
name|headers
parameter_list|)
block|{
name|this
operator|.
name|headers
operator|=
name|headers
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
comment|/**      * Whether or not the header must be read in the first line of the test document      *<p/>      * The default value is false      */
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
comment|/**      * The maximum number of record to read.      */
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
comment|/**      * The String representation of an empty value      */
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
comment|/**      * The line separator of the files      *<p/>      * The default value is to use the JVM platform line separator      */
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
comment|/**      * The normalized line separator of the files      *<p/>      * The default value is a new line character.      */
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
comment|/**      * The comment symbol.      *<p/>      * The default value is #      */
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
comment|/**      * Whether the unmarshalling should produce an iterator that reads the lines on the fly or if all the lines must be read at one.      *<p/>      * The default value is false      */
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
comment|/**      * Whether the unmarshalling should produce maps for the lines values instead of lists.      * It requires to have header (either defined or collected).      *<p/>      * The default value is false      */
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
annotation|@
name|Override
DECL|method|configureDataFormat (DataFormat dataFormat, CamelContext camelContext)
specifier|protected
name|void
name|configureDataFormat
parameter_list|(
name|DataFormat
name|dataFormat
parameter_list|,
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|super
operator|.
name|configureDataFormat
argument_list|(
name|dataFormat
argument_list|,
name|camelContext
argument_list|)
expr_stmt|;
if|if
condition|(
name|nullValue
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"nullValue"
argument_list|,
name|nullValue
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|skipEmptyLines
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"skipEmptyLines"
argument_list|,
name|skipEmptyLines
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ignoreTrailingWhitespaces
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"ignoreTrailingWhitespaces"
argument_list|,
name|ignoreTrailingWhitespaces
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ignoreLeadingWhitespaces
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"ignoreLeadingWhitespaces"
argument_list|,
name|ignoreLeadingWhitespaces
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|headersDisabled
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"headersDisabled"
argument_list|,
name|headersDisabled
argument_list|)
expr_stmt|;
block|}
name|String
index|[]
name|validHeaderNames
init|=
name|getValidHeaderNames
argument_list|()
decl_stmt|;
if|if
condition|(
name|validHeaderNames
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"headers"
argument_list|,
name|validHeaderNames
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|headerExtractionEnabled
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"headerExtractionEnabled"
argument_list|,
name|headerExtractionEnabled
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|numberOfRecordsToRead
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"numberOfRecordsToRead"
argument_list|,
name|numberOfRecordsToRead
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|emptyValue
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"emptyValue"
argument_list|,
name|emptyValue
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|lineSeparator
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"lineSeparator"
argument_list|,
name|lineSeparator
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|normalizedLineSeparator
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"normalizedLineSeparator"
argument_list|,
name|singleCharOf
argument_list|(
literal|"normalizedLineSeparator"
argument_list|,
name|normalizedLineSeparator
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|comment
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"comment"
argument_list|,
name|singleCharOf
argument_list|(
literal|"comment"
argument_list|,
name|comment
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|lazyLoad
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"lazyLoad"
argument_list|,
name|lazyLoad
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|asMap
operator|!=
literal|null
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"asMap"
argument_list|,
name|asMap
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|singleCharOf (String attributeName, String string)
specifier|protected
specifier|static
name|Character
name|singleCharOf
parameter_list|(
name|String
name|attributeName
parameter_list|,
name|String
name|string
parameter_list|)
block|{
if|if
condition|(
name|string
operator|.
name|length
argument_list|()
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Only one character must be defined for "
operator|+
name|attributeName
argument_list|)
throw|;
block|}
return|return
name|string
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
return|;
block|}
comment|/**      * Gets only the headers with non-null and non-empty names. It returns {@code null} if there's no such headers.      *      * @return The headers with non-null and non-empty names      */
DECL|method|getValidHeaderNames ()
specifier|private
name|String
index|[]
name|getValidHeaderNames
parameter_list|()
block|{
if|if
condition|(
name|headers
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|names
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|headers
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|UniVocityHeader
name|header
range|:
name|headers
control|)
block|{
if|if
condition|(
name|header
operator|.
name|getName
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|header
operator|.
name|getName
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|names
operator|.
name|add
argument_list|(
name|header
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|names
operator|.
name|isEmpty
argument_list|()
condition|?
literal|null
else|:
name|names
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|names
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
block|}
end_class

end_unit
