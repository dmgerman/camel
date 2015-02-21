begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.catalog
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|catalog
package|;
end_package

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
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MXBean
import|;
end_import

begin_comment
comment|/**  * Catalog of components, data formats, models (EIPs), languages, and more  from this Apache Camel release.  */
end_comment

begin_interface
annotation|@
name|MXBean
DECL|interface|CamelCatalog
specifier|public
interface|interface
name|CamelCatalog
block|{
comment|/**      * Find all the component names from the Camel catalog      */
DECL|method|findComponentNames ()
name|List
argument_list|<
name|String
argument_list|>
name|findComponentNames
parameter_list|()
function_decl|;
comment|/**      * Find all the data format names from the Camel catalog      */
DECL|method|findDataFormatNames ()
name|List
argument_list|<
name|String
argument_list|>
name|findDataFormatNames
parameter_list|()
function_decl|;
comment|/**      * Find all the language names from the Camel catalog      */
DECL|method|findLanguageNames ()
name|List
argument_list|<
name|String
argument_list|>
name|findLanguageNames
parameter_list|()
function_decl|;
comment|/**      * Find all the model names from the Camel catalog      */
DECL|method|findModelNames ()
name|List
argument_list|<
name|String
argument_list|>
name|findModelNames
parameter_list|()
function_decl|;
comment|/**      * Find all the component names from the Camel catalog that matches the label      */
DECL|method|findComponentNames (String filter)
name|List
argument_list|<
name|String
argument_list|>
name|findComponentNames
parameter_list|(
name|String
name|filter
parameter_list|)
function_decl|;
comment|/**      * Find all the data format names from the Camel catalog that matches the label      */
DECL|method|findDataFormatNames (String filter)
name|List
argument_list|<
name|String
argument_list|>
name|findDataFormatNames
parameter_list|(
name|String
name|filter
parameter_list|)
function_decl|;
comment|/**      * Find all the language names from the Camel catalog that matches the label      */
DECL|method|findLanguageNames (String filter)
name|List
argument_list|<
name|String
argument_list|>
name|findLanguageNames
parameter_list|(
name|String
name|filter
parameter_list|)
function_decl|;
comment|/**      * Find all the model names from the Camel catalog that matches the label      */
DECL|method|findModelNames (String filter)
name|List
argument_list|<
name|String
argument_list|>
name|findModelNames
parameter_list|(
name|String
name|filter
parameter_list|)
function_decl|;
comment|/**      * Returns the component information as JSon format.      *      * @param name the component name      * @return component details in JSon      */
DECL|method|componentJSonSchema (String name)
name|String
name|componentJSonSchema
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns the data format information as JSon format.      *      * @param name the data format name      * @return data format details in JSon      */
DECL|method|dataFormatJSonSchema (String name)
name|String
name|dataFormatJSonSchema
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns the language information as JSon format.      *      * @param name the language name      * @return language details in JSon      */
DECL|method|languageJSonSchema (String name)
name|String
name|languageJSonSchema
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Returns the model information as JSon format.      *      * @param name the model name      * @return model details in JSon      */
DECL|method|modelJSonSchema (String name)
name|String
name|modelJSonSchema
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**      * Find all the unique label names all the components are using.      *      * @return a set of all the labels.      */
DECL|method|findComponentLabels ()
name|Set
argument_list|<
name|String
argument_list|>
name|findComponentLabels
parameter_list|()
function_decl|;
comment|/**      * Find all the unique label names all the data formats are using.      *      * @return a set of all the labels.      */
DECL|method|findDataFormatLabels ()
name|Set
argument_list|<
name|String
argument_list|>
name|findDataFormatLabels
parameter_list|()
function_decl|;
comment|/**      * Find all the unique label names all the data formats are using.      *      * @return a set of all the labels.      */
DECL|method|findLanguageLabels ()
name|Set
argument_list|<
name|String
argument_list|>
name|findLanguageLabels
parameter_list|()
function_decl|;
comment|/**      * Find all the unique label names all the models are using.      *      * @return a set of all the labels.      */
DECL|method|findModelLabels ()
name|Set
argument_list|<
name|String
argument_list|>
name|findModelLabels
parameter_list|()
function_decl|;
comment|/**      * Returns the Apache Camel Maven Archetype catalog in XML format.      *      * @return the catalog in XML      */
DECL|method|archetypeCatalogAsXml ()
name|String
name|archetypeCatalogAsXml
parameter_list|()
function_decl|;
comment|/**      * Returns the Camel Spring XML schema      *      * @return the spring XML schema      */
DECL|method|springSchemaAsXml ()
name|String
name|springSchemaAsXml
parameter_list|()
function_decl|;
comment|/**      * Returns the Camel Blueprint XML schema      *      * @return the blueprint XML schema      */
DECL|method|blueprintSchemaAsXml ()
name|String
name|blueprintSchemaAsXml
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

