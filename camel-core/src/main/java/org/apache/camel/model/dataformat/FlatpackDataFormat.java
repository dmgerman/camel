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
name|XmlRootElement
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
name|RouteContext
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
comment|/**  * Flatpack data format  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|label
operator|=
literal|"dataformat,transformation"
argument_list|,
name|title
operator|=
literal|"Flatpack"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"flatpack"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|FlatpackDataFormat
specifier|public
class|class
name|FlatpackDataFormat
extends|extends
name|DataFormatDefinition
block|{
annotation|@
name|XmlAttribute
DECL|field|parserFactoryRef
specifier|private
name|String
name|parserFactoryRef
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|definition
specifier|private
name|String
name|definition
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|fixed
specifier|private
name|Boolean
name|fixed
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
DECL|field|ignoreFirstRecord
specifier|private
name|Boolean
name|ignoreFirstRecord
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"\""
argument_list|)
DECL|field|textQualifier
specifier|private
name|String
name|textQualifier
decl_stmt|;
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|","
argument_list|)
DECL|field|delimiter
specifier|private
name|String
name|delimiter
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|allowShortLines
specifier|private
name|Boolean
name|allowShortLines
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|ignoreExtraColumns
specifier|private
name|Boolean
name|ignoreExtraColumns
decl_stmt|;
DECL|method|FlatpackDataFormat ()
specifier|public
name|FlatpackDataFormat
parameter_list|()
block|{
name|super
argument_list|(
literal|"flatpack"
argument_list|)
expr_stmt|;
block|}
DECL|method|getParserFactoryRef ()
specifier|public
name|String
name|getParserFactoryRef
parameter_list|()
block|{
return|return
name|parserFactoryRef
return|;
block|}
comment|/**      * References to a custom parser factory to lookup in the registry      */
DECL|method|setParserFactoryRef (String parserFactoryRef)
specifier|public
name|void
name|setParserFactoryRef
parameter_list|(
name|String
name|parserFactoryRef
parameter_list|)
block|{
name|this
operator|.
name|parserFactoryRef
operator|=
name|parserFactoryRef
expr_stmt|;
block|}
DECL|method|getDefinition ()
specifier|public
name|String
name|getDefinition
parameter_list|()
block|{
return|return
name|definition
return|;
block|}
comment|/**      * The flatpack pzmap configuration file. Can be omitted in simpler situations, but its preferred to use the pzmap.      */
DECL|method|setDefinition (String definition)
specifier|public
name|void
name|setDefinition
parameter_list|(
name|String
name|definition
parameter_list|)
block|{
name|this
operator|.
name|definition
operator|=
name|definition
expr_stmt|;
block|}
DECL|method|getFixed ()
specifier|public
name|Boolean
name|getFixed
parameter_list|()
block|{
return|return
name|fixed
return|;
block|}
comment|/**      * Delimited or fixed.      * Is by default false = delimited      */
DECL|method|setFixed (Boolean fixed)
specifier|public
name|void
name|setFixed
parameter_list|(
name|Boolean
name|fixed
parameter_list|)
block|{
name|this
operator|.
name|fixed
operator|=
name|fixed
expr_stmt|;
block|}
DECL|method|getIgnoreFirstRecord ()
specifier|public
name|Boolean
name|getIgnoreFirstRecord
parameter_list|()
block|{
return|return
name|ignoreFirstRecord
return|;
block|}
comment|/**      * Whether the first line is ignored for delimited files (for the column headers).      *<p/>      * Is by default true.      */
DECL|method|setIgnoreFirstRecord (Boolean ignoreFirstRecord)
specifier|public
name|void
name|setIgnoreFirstRecord
parameter_list|(
name|Boolean
name|ignoreFirstRecord
parameter_list|)
block|{
name|this
operator|.
name|ignoreFirstRecord
operator|=
name|ignoreFirstRecord
expr_stmt|;
block|}
DECL|method|getTextQualifier ()
specifier|public
name|String
name|getTextQualifier
parameter_list|()
block|{
return|return
name|textQualifier
return|;
block|}
comment|/**      * If the text is qualified with a char such as&quot;      */
DECL|method|setTextQualifier (String textQualifier)
specifier|public
name|void
name|setTextQualifier
parameter_list|(
name|String
name|textQualifier
parameter_list|)
block|{
name|this
operator|.
name|textQualifier
operator|=
name|textQualifier
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
comment|/**      * The delimiter char (could be ; , or similar)      */
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
DECL|method|getAllowShortLines ()
specifier|public
name|Boolean
name|getAllowShortLines
parameter_list|()
block|{
return|return
name|allowShortLines
return|;
block|}
comment|/**      * Allows for lines to be shorter than expected and ignores the extra characters      */
DECL|method|setAllowShortLines (Boolean allowShortLines)
specifier|public
name|void
name|setAllowShortLines
parameter_list|(
name|Boolean
name|allowShortLines
parameter_list|)
block|{
name|this
operator|.
name|allowShortLines
operator|=
name|allowShortLines
expr_stmt|;
block|}
DECL|method|getIgnoreExtraColumns ()
specifier|public
name|Boolean
name|getIgnoreExtraColumns
parameter_list|()
block|{
return|return
name|ignoreExtraColumns
return|;
block|}
comment|/**      * Allows for lines to be longer than expected and ignores the extra characters.      */
DECL|method|setIgnoreExtraColumns (Boolean ignoreExtraColumns)
specifier|public
name|void
name|setIgnoreExtraColumns
parameter_list|(
name|Boolean
name|ignoreExtraColumns
parameter_list|)
block|{
name|this
operator|.
name|ignoreExtraColumns
operator|=
name|ignoreExtraColumns
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createDataFormat (RouteContext routeContext)
specifier|protected
name|DataFormat
name|createDataFormat
parameter_list|(
name|RouteContext
name|routeContext
parameter_list|)
block|{
name|DataFormat
name|flatpack
init|=
name|super
operator|.
name|createDataFormat
argument_list|(
name|routeContext
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|parserFactoryRef
argument_list|)
condition|)
block|{
name|Object
name|parserFactory
init|=
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|parserFactoryRef
argument_list|)
decl_stmt|;
name|setProperty
argument_list|(
name|routeContext
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|flatpack
argument_list|,
literal|"parserFactory"
argument_list|,
name|parserFactory
argument_list|)
expr_stmt|;
block|}
return|return
name|flatpack
return|;
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
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|definition
argument_list|)
condition|)
block|{
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"definition"
argument_list|,
name|definition
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|fixed
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
literal|"fixed"
argument_list|,
name|fixed
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ignoreFirstRecord
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
literal|"ignoreFirstRecord"
argument_list|,
name|ignoreFirstRecord
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|textQualifier
argument_list|)
condition|)
block|{
if|if
condition|(
name|textQualifier
operator|.
name|length
argument_list|()
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Text qualifier must be one character long!"
argument_list|)
throw|;
block|}
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"textQualifier"
argument_list|,
name|textQualifier
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|delimiter
argument_list|)
condition|)
block|{
if|if
condition|(
name|delimiter
operator|.
name|length
argument_list|()
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Delimiter must be one character long!"
argument_list|)
throw|;
block|}
name|setProperty
argument_list|(
name|camelContext
argument_list|,
name|dataFormat
argument_list|,
literal|"delimiter"
argument_list|,
name|delimiter
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|allowShortLines
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
literal|"allowShortLines"
argument_list|,
name|allowShortLines
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ignoreExtraColumns
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
literal|"ignoreExtraColumns"
argument_list|,
name|ignoreExtraColumns
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

