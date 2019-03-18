begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.runtimecatalog
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|runtimecatalog
package|;
end_package

begin_comment
comment|/**  * Pluggable resolver to load JSon schema files for components, data formats, languages etc.  */
end_comment

begin_interface
DECL|interface|JSonSchemaResolver
specifier|public
interface|interface
name|JSonSchemaResolver
block|{
comment|/**      * Returns the component information as JSon format.      *      * @param name the component name      * @return component details in JSon      */
DECL|method|getComponentJSonSchema (String name)
name|String
name|getComponentJSonSchema
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns the data format information as JSon format.      *      * @param name the data format name      * @return data format details in JSon      */
DECL|method|getDataFormatJSonSchema (String name)
name|String
name|getDataFormatJSonSchema
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns the language information as JSon format.      *      * @param name the language name      * @return language details in JSon      */
DECL|method|getLanguageJSonSchema (String name)
name|String
name|getLanguageJSonSchema
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns the other (miscellaneous) information as JSon format.      *      * @param name the other (miscellaneous) name      * @return other (miscellaneous) details in JSon      */
DECL|method|getOtherJSonSchema (String name)
name|String
name|getOtherJSonSchema
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns the model information as JSon format.      *      * @param name the model name      * @return model details in JSon      */
DECL|method|getModelJSonSchema (String name)
name|String
name|getModelJSonSchema
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

