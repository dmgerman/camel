begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|Language
import|;
end_import

begin_comment
comment|/**  * Catalog level interface for the {@link CamelContext}  */
end_comment

begin_interface
DECL|interface|CatalogCamelContext
specifier|public
interface|interface
name|CatalogCamelContext
extends|extends
name|CamelContext
block|{
comment|/**      * Returns the JSON schema representation of the component and endpoint parameters for the given component name.      *      * @return the json or<tt>null</tt> if the component is<b>not</b> built with JSon schema support      */
DECL|method|getComponentParameterJsonSchema (String componentName)
name|String
name|getComponentParameterJsonSchema
parameter_list|(
name|String
name|componentName
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns the JSON schema representation of the {@link DataFormat} parameters for the given data format name.      *      * @return the json or<tt>null</tt> if the data format does not exist      */
DECL|method|getDataFormatParameterJsonSchema (String dataFormatName)
name|String
name|getDataFormatParameterJsonSchema
parameter_list|(
name|String
name|dataFormatName
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns the JSON schema representation of the {@link Language} parameters for the given language name.      *      * @return the json or<tt>null</tt> if the language does not exist      */
DECL|method|getLanguageParameterJsonSchema (String languageName)
name|String
name|getLanguageParameterJsonSchema
parameter_list|(
name|String
name|languageName
parameter_list|)
throws|throws
name|IOException
function_decl|;
comment|/**      * Returns the JSON schema representation of the EIP parameters for the given EIP name.      *      * @return the json or<tt>null</tt> if the EIP does not exist      */
DECL|method|getEipParameterJsonSchema (String eipName)
name|String
name|getEipParameterJsonSchema
parameter_list|(
name|String
name|eipName
parameter_list|)
throws|throws
name|IOException
function_decl|;
block|}
end_interface

end_unit

