begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.reifier.dataformat
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|reifier
operator|.
name|dataformat
package|;
end_package

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
name|model
operator|.
name|dataformat
operator|.
name|FlatpackDataFormat
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
name|support
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

begin_class
DECL|class|FlatpackDataFormatReifier
specifier|public
class|class
name|FlatpackDataFormatReifier
extends|extends
name|DataFormatReifier
argument_list|<
name|FlatpackDataFormat
argument_list|>
block|{
DECL|method|FlatpackDataFormatReifier (DataFormatDefinition definition)
specifier|public
name|FlatpackDataFormatReifier
parameter_list|(
name|DataFormatDefinition
name|definition
parameter_list|)
block|{
name|super
argument_list|(
operator|(
name|FlatpackDataFormat
operator|)
name|definition
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doCreateDataFormat (CamelContext camelContext)
specifier|protected
name|DataFormat
name|doCreateDataFormat
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|DataFormat
name|flatpack
init|=
name|super
operator|.
name|doCreateDataFormat
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|definition
operator|.
name|getParserFactoryRef
argument_list|()
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
name|camelContext
argument_list|,
name|definition
operator|.
name|getParserFactoryRef
argument_list|()
argument_list|)
decl_stmt|;
name|setProperty
argument_list|(
name|camelContext
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
operator|.
name|getDefinition
argument_list|()
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
operator|.
name|getDefinition
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getFixed
argument_list|()
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
name|definition
operator|.
name|getFixed
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getIgnoreFirstRecord
argument_list|()
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
name|definition
operator|.
name|getIgnoreFirstRecord
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|definition
operator|.
name|getTextQualifier
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|definition
operator|.
name|getTextQualifier
argument_list|()
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
name|definition
operator|.
name|getTextQualifier
argument_list|()
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
name|definition
operator|.
name|getDelimiter
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|definition
operator|.
name|getDelimiter
argument_list|()
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
name|definition
operator|.
name|getDelimiter
argument_list|()
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
name|definition
operator|.
name|getAllowShortLines
argument_list|()
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
name|definition
operator|.
name|getAllowShortLines
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|definition
operator|.
name|getIgnoreExtraColumns
argument_list|()
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
name|definition
operator|.
name|getIgnoreExtraColumns
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit
