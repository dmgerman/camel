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
name|XmlElement
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
name|XmlRootElement
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
name|util
operator|.
name|CamelContextHelper
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * Represents a CSV (Comma Separated Values) {@link org.apache.camel.spi.DataFormat}  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"csv"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|CsvDataFormat
specifier|public
class|class
name|CsvDataFormat
extends|extends
name|DataFormatDefinition
block|{
comment|// Format options
annotation|@
name|XmlAttribute
DECL|field|formatRef
specifier|private
name|String
name|formatRef
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|formatName
specifier|private
name|String
name|formatName
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|commentMarkerDisabled
specifier|private
name|Boolean
name|commentMarkerDisabled
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|commentMarker
specifier|private
name|String
name|commentMarker
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|delimiter
specifier|private
name|String
name|delimiter
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|escapeDisabled
specifier|private
name|Boolean
name|escapeDisabled
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|escape
specifier|private
name|String
name|escape
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|headerDisabled
specifier|private
name|Boolean
name|headerDisabled
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"header"
argument_list|)
DECL|field|header
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|header
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|allowMissingColumnNames
specifier|private
name|Boolean
name|allowMissingColumnNames
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|ignoreEmptyLines
specifier|private
name|Boolean
name|ignoreEmptyLines
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|ignoreSurroundingSpaces
specifier|private
name|Boolean
name|ignoreSurroundingSpaces
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|nullStringDisabled
specifier|private
name|Boolean
name|nullStringDisabled
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|nullString
specifier|private
name|String
name|nullString
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|quoteDisabled
specifier|private
name|Boolean
name|quoteDisabled
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|quote
specifier|private
name|String
name|quote
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|recordSeparatorDisabled
specifier|private
name|String
name|recordSeparatorDisabled
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|recordSeparator
specifier|private
name|String
name|recordSeparator
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|skipHeaderRecord
specifier|private
name|Boolean
name|skipHeaderRecord
decl_stmt|;
comment|// Unmarshall options
annotation|@
name|XmlAttribute
DECL|field|lazyLoad
specifier|private
name|Boolean
name|lazyLoad
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|useMaps
specifier|private
name|Boolean
name|useMaps
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|recordConverterRef
specifier|private
name|String
name|recordConverterRef
decl_stmt|;
DECL|method|CsvDataFormat ()
specifier|public
name|CsvDataFormat
parameter_list|()
block|{
name|super
argument_list|(
literal|"csv"
argument_list|)
expr_stmt|;
block|}
DECL|method|CsvDataFormat (String delimiter)
specifier|public
name|CsvDataFormat
parameter_list|(
name|String
name|delimiter
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|setDelimiter
argument_list|(
name|delimiter
argument_list|)
expr_stmt|;
block|}
DECL|method|CsvDataFormat (boolean lazyLoad)
specifier|public
name|CsvDataFormat
parameter_list|(
name|boolean
name|lazyLoad
parameter_list|)
block|{
name|this
argument_list|()
expr_stmt|;
name|setLazyLoad
argument_list|(
name|lazyLoad
argument_list|)
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
comment|// Format options
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|formatRef
argument_list|)
condition|)
block|{
name|Object
name|format
init|=
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|camelContext
argument_list|,
name|formatRef
argument_list|)
decl_stmt|;
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"format"
argument_list|,
name|format
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|formatName
argument_list|)
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"formatName"
argument_list|,
name|formatName
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|commentMarkerDisabled
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
literal|"commentMarkerDisabled"
argument_list|,
name|commentMarkerDisabled
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|commentMarker
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
literal|"commentMarker"
argument_list|,
name|singleChar
argument_list|(
name|commentMarker
argument_list|,
literal|"commentMarker"
argument_list|)
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
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"delimiter"
argument_list|,
name|singleChar
argument_list|(
name|delimiter
argument_list|,
literal|"delimiter"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|escapeDisabled
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
literal|"escapeDisabled"
argument_list|,
name|escapeDisabled
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|escape
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
literal|"escape"
argument_list|,
name|singleChar
argument_list|(
name|escape
argument_list|,
literal|"escape"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|headerDisabled
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
literal|"headerDisabled"
argument_list|,
name|headerDisabled
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|header
operator|!=
literal|null
operator|&&
operator|!
name|header
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"header"
argument_list|,
name|header
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|header
operator|.
name|size
argument_list|()
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|allowMissingColumnNames
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
literal|"allowMissingColumnNames"
argument_list|,
name|allowMissingColumnNames
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ignoreEmptyLines
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
literal|"ignoreEmptyLines"
argument_list|,
name|ignoreEmptyLines
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ignoreSurroundingSpaces
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
literal|"ignoreSurroundingSpaces"
argument_list|,
name|ignoreSurroundingSpaces
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|nullStringDisabled
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
literal|"nullStringDisabled"
argument_list|,
name|nullStringDisabled
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|nullString
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
literal|"nullString"
argument_list|,
name|nullString
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|quoteDisabled
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
literal|"quoteDisabled"
argument_list|,
name|quoteDisabled
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|quote
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
literal|"quote"
argument_list|,
name|singleChar
argument_list|(
name|quote
argument_list|,
literal|"quote"
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|recordSeparatorDisabled
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
literal|"recordSeparatorDisabled"
argument_list|,
name|recordSeparatorDisabled
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|recordSeparator
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
literal|"recordSeparator"
argument_list|,
name|recordSeparator
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|skipHeaderRecord
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
literal|"skipHeaderRecord"
argument_list|,
name|skipHeaderRecord
argument_list|)
expr_stmt|;
block|}
comment|// Unmarshall options
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
name|useMaps
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
literal|"useMaps"
argument_list|,
name|useMaps
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|recordConverterRef
argument_list|)
condition|)
block|{
name|Object
name|recordConverter
init|=
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|camelContext
argument_list|,
name|recordConverterRef
argument_list|)
decl_stmt|;
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"recordConverter"
argument_list|,
name|recordConverter
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|singleChar (String value, String attributeName)
specifier|private
specifier|static
name|Character
name|singleChar
parameter_list|(
name|String
name|value
parameter_list|,
name|String
name|attributeName
parameter_list|)
block|{
if|if
condition|(
name|value
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
name|String
operator|.
name|format
argument_list|(
literal|"The '%s' attribute must be exactly one character long."
argument_list|,
name|attributeName
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|value
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
return|;
block|}
comment|//region Getters/Setters
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
comment|//endregion
block|}
end_class

end_unit

