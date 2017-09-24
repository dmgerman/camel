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
comment|/**  * The uniVocity TSV data format is used for working with TSV (Tabular Separated Values) flat payloads.  */
end_comment

begin_class
annotation|@
name|Metadata
argument_list|(
name|firstVersion
operator|=
literal|"2.15.0"
argument_list|,
name|label
operator|=
literal|"dataformat,transformation,csv"
argument_list|,
name|title
operator|=
literal|"uniVocity TSV"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"univocity-tsv"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|UniVocityTsvDataFormat
specifier|public
class|class
name|UniVocityTsvDataFormat
extends|extends
name|UniVocityAbstractDataFormat
block|{
annotation|@
name|XmlAttribute
annotation|@
name|Metadata
argument_list|(
name|defaultValue
operator|=
literal|"\\"
argument_list|)
DECL|field|escapeChar
specifier|private
name|String
name|escapeChar
decl_stmt|;
DECL|method|UniVocityTsvDataFormat ()
specifier|public
name|UniVocityTsvDataFormat
parameter_list|()
block|{
name|super
argument_list|(
literal|"univocity-tsv"
argument_list|)
expr_stmt|;
block|}
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
comment|/**      * The escape character.      */
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
name|escapeChar
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
literal|"escapeChar"
argument_list|,
name|singleCharOf
argument_list|(
literal|"escapeChar"
argument_list|,
name|escapeChar
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

