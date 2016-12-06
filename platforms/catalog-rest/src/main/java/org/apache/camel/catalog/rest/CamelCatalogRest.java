begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.catalog.rest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|catalog
operator|.
name|rest
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
name|ws
operator|.
name|rs
operator|.
name|GET
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|PathParam
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Produces
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|annotations
operator|.
name|Api
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|annotations
operator|.
name|ApiOperation
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
name|catalog
operator|.
name|CamelCatalog
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
name|catalog
operator|.
name|DefaultCamelCatalog
import|;
end_import

begin_comment
comment|/**  * A REST based {@link CamelCatalog} service as a JAX-RS resource class.  */
end_comment

begin_class
annotation|@
name|Api
argument_list|(
name|value
operator|=
literal|"/camel-catalog"
argument_list|,
name|description
operator|=
literal|"Camel Catalog REST API"
argument_list|)
annotation|@
name|Path
argument_list|(
literal|"/camel-catalog"
argument_list|)
DECL|class|CamelCatalogRest
specifier|public
class|class
name|CamelCatalogRest
block|{
DECL|field|catalog
specifier|private
name|CamelCatalog
name|catalog
init|=
operator|new
name|DefaultCamelCatalog
argument_list|(
literal|true
argument_list|)
decl_stmt|;
DECL|method|getCatalog ()
specifier|public
name|CamelCatalog
name|getCatalog
parameter_list|()
block|{
return|return
name|catalog
return|;
block|}
comment|/**      * To inject an existing {@link CamelCatalog}      */
DECL|method|setCatalog (CamelCatalog catalog)
specifier|public
name|void
name|setCatalog
parameter_list|(
name|CamelCatalog
name|catalog
parameter_list|)
block|{
name|this
operator|.
name|catalog
operator|=
name|catalog
expr_stmt|;
block|}
comment|/**      * The version of this Camel Catalog      */
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/catalogVersion"
argument_list|)
annotation|@
name|ApiOperation
argument_list|(
name|value
operator|=
literal|"The version of this Camel Catalog"
argument_list|)
DECL|method|getCatalogVersion ()
specifier|public
name|String
name|getCatalogVersion
parameter_list|()
block|{
return|return
name|catalog
operator|.
name|getCatalogVersion
argument_list|()
return|;
block|}
comment|/**      * Find all the component names from the Camel catalog      */
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/findComponentNames"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
DECL|method|findComponentNames ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|findComponentNames
parameter_list|()
block|{
return|return
name|catalog
operator|.
name|findComponentNames
argument_list|()
return|;
block|}
comment|/**      * Find all the data format names from the Camel catalog      */
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/findDataFormatNames"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
DECL|method|findDataFormatNames ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|findDataFormatNames
parameter_list|()
block|{
return|return
name|catalog
operator|.
name|findDataFormatNames
argument_list|()
return|;
block|}
comment|/**      * Find all the language names from the Camel catalog      */
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/findLanguageNames"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
DECL|method|findLanguageNames ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|findLanguageNames
parameter_list|()
block|{
return|return
name|catalog
operator|.
name|findLanguageNames
argument_list|()
return|;
block|}
comment|/**      * Find all the model names from the Camel catalog      */
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/findModelNames"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
DECL|method|findModelNames ()
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|findModelNames
parameter_list|()
block|{
return|return
name|catalog
operator|.
name|findModelNames
argument_list|()
return|;
block|}
comment|/**      * Find all the component names from the Camel catalog that matches the label      */
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/findComponentNames/{filter}"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
DECL|method|findComponentNames (@athParamR) String filter)
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|findComponentNames
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"filter"
argument_list|)
name|String
name|filter
parameter_list|)
block|{
return|return
name|catalog
operator|.
name|findComponentNames
argument_list|(
name|filter
argument_list|)
return|;
block|}
comment|/**      * Find all the data format names from the Camel catalog that matches the label      */
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/findDataFormatNames/{filter}"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
DECL|method|findDataFormatNames (@athParamR) String filter)
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|findDataFormatNames
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"filter"
argument_list|)
name|String
name|filter
parameter_list|)
block|{
return|return
name|catalog
operator|.
name|findDataFormatNames
argument_list|(
name|filter
argument_list|)
return|;
block|}
comment|/**      * Find all the language names from the Camel catalog that matches the label      */
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/findLanguageNames/{filter}"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
DECL|method|findLanguageNames (@athParamR) String filter)
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|findLanguageNames
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"filter"
argument_list|)
name|String
name|filter
parameter_list|)
block|{
return|return
name|catalog
operator|.
name|findLanguageNames
argument_list|(
name|filter
argument_list|)
return|;
block|}
comment|/**      * Find all the model names from the Camel catalog that matches the label      */
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/findModelNames/{filter}"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
DECL|method|findModelNames (@athParamR) String filter)
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|findModelNames
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"filter"
argument_list|)
name|String
name|filter
parameter_list|)
block|{
return|return
name|catalog
operator|.
name|findModelNames
argument_list|(
name|filter
argument_list|)
return|;
block|}
comment|/**      * Returns the component information as JSon format.      */
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/componentJSonSchema/{name}"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
DECL|method|componentJSonSchema (@athParamR) String name)
specifier|public
name|String
name|componentJSonSchema
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"name"
argument_list|)
name|String
name|name
parameter_list|)
block|{
return|return
name|catalog
operator|.
name|componentJSonSchema
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/**      * Returns the data format information as JSon format.      */
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/dataFormatJSonSchema/{name}"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
DECL|method|dataFormatJSonSchema (@athParamR) String name)
specifier|public
name|String
name|dataFormatJSonSchema
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"name"
argument_list|)
name|String
name|name
parameter_list|)
block|{
return|return
name|catalog
operator|.
name|dataFormatJSonSchema
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/**      * Returns the language information as JSon format.      */
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/languageJSonSchema/{name}"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
DECL|method|languageJSonSchema (@athParamR) String name)
specifier|public
name|String
name|languageJSonSchema
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"name"
argument_list|)
name|String
name|name
parameter_list|)
block|{
return|return
name|catalog
operator|.
name|languageJSonSchema
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/**      * Returns the model information as JSon format.      */
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/modelJSonSchema/{name}"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
DECL|method|modelJSonSchema (@athParamR) String name)
specifier|public
name|String
name|modelJSonSchema
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"name"
argument_list|)
name|String
name|name
parameter_list|)
block|{
return|return
name|catalog
operator|.
name|modelJSonSchema
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/**      * Returns the component documentation as Ascii doc format.      */
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/componentAsciiDoc/{name}"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/plain"
argument_list|)
DECL|method|componentAsciiDoc (@athParamR) String name)
specifier|public
name|String
name|componentAsciiDoc
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"name"
argument_list|)
name|String
name|name
parameter_list|)
block|{
return|return
name|catalog
operator|.
name|componentAsciiDoc
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/**      * Returns the data format documentation as Ascii doc format.      */
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/dataFormatAsciiDoc/{name}"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/plain"
argument_list|)
DECL|method|dataFormatAsciiDoc (@athParamR) String name)
specifier|public
name|String
name|dataFormatAsciiDoc
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"name"
argument_list|)
name|String
name|name
parameter_list|)
block|{
return|return
name|catalog
operator|.
name|dataFormatAsciiDoc
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/**      * Returns the language documentation as Ascii doc format.      */
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/languageAsciiDoc/{name}"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/plain"
argument_list|)
DECL|method|languageAsciiDoc (@athParamR) String name)
specifier|public
name|String
name|languageAsciiDoc
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"name"
argument_list|)
name|String
name|name
parameter_list|)
block|{
return|return
name|catalog
operator|.
name|languageAsciiDoc
argument_list|(
name|name
argument_list|)
return|;
block|}
comment|/**      * Find all the unique label names all the components are using.      */
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/findComponentLabels"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
DECL|method|findComponentLabels ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|findComponentLabels
parameter_list|()
block|{
return|return
name|catalog
operator|.
name|findComponentLabels
argument_list|()
return|;
block|}
comment|/**      * Find all the unique label names all the data formats are using.      */
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/findDataFormatLabels"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
DECL|method|findDataFormatLabels ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|findDataFormatLabels
parameter_list|()
block|{
return|return
name|catalog
operator|.
name|findDataFormatLabels
argument_list|()
return|;
block|}
comment|/**      * Find all the unique label names all the data formats are using.      */
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/findLanguageLabels"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
DECL|method|findLanguageLabels ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|findLanguageLabels
parameter_list|()
block|{
return|return
name|catalog
operator|.
name|findLanguageLabels
argument_list|()
return|;
block|}
comment|/**      * Find all the unique label names all the models are using.      */
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/findModelLabels"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
DECL|method|findModelLabels ()
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|findModelLabels
parameter_list|()
block|{
return|return
name|catalog
operator|.
name|findModelLabels
argument_list|()
return|;
block|}
comment|/**      * Returns the Apache Camel Maven Archetype catalog in XML format.      */
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/archetypeCatalogAsXml"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/xml"
argument_list|)
DECL|method|archetypeCatalogAsXml ()
specifier|public
name|String
name|archetypeCatalogAsXml
parameter_list|()
block|{
return|return
name|catalog
operator|.
name|archetypeCatalogAsXml
argument_list|()
return|;
block|}
comment|/**      * Returns the Camel Spring XML schema      */
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/springSchemaAsXml"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/xml"
argument_list|)
DECL|method|springSchemaAsXml ()
specifier|public
name|String
name|springSchemaAsXml
parameter_list|()
block|{
return|return
name|catalog
operator|.
name|springSchemaAsXml
argument_list|()
return|;
block|}
comment|/**      * Returns the Camel Blueprint XML schema      */
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/blueprintSchemaAsXml"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/xml"
argument_list|)
DECL|method|blueprintSchemaAsXml ()
specifier|public
name|String
name|blueprintSchemaAsXml
parameter_list|()
block|{
return|return
name|catalog
operator|.
name|blueprintSchemaAsXml
argument_list|()
return|;
block|}
comment|/**      * Lists all the components summary details in JSon      */
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/listComponentsAsJson"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
DECL|method|listComponentsAsJson ()
specifier|public
name|String
name|listComponentsAsJson
parameter_list|()
block|{
return|return
name|catalog
operator|.
name|listComponentsAsJson
argument_list|()
return|;
block|}
comment|/**      * Lists all the data formats summary details in JSon      */
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/listDataFormatsAsJson"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
DECL|method|listDataFormatsAsJson ()
specifier|public
name|String
name|listDataFormatsAsJson
parameter_list|()
block|{
return|return
name|catalog
operator|.
name|listDataFormatsAsJson
argument_list|()
return|;
block|}
comment|/**      * Lists all the languages summary details in JSon      */
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/listLanguagesAsJson"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
DECL|method|listLanguagesAsJson ()
specifier|public
name|String
name|listLanguagesAsJson
parameter_list|()
block|{
return|return
name|catalog
operator|.
name|listLanguagesAsJson
argument_list|()
return|;
block|}
comment|/**      * Lists all the models (EIPs) summary details in JSon      */
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/listModelsAsJson"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
DECL|method|listModelsAsJson ()
specifier|public
name|String
name|listModelsAsJson
parameter_list|()
block|{
return|return
name|catalog
operator|.
name|listModelsAsJson
argument_list|()
return|;
block|}
comment|/**      * Reports a summary what the catalog contains in JSon      */
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/summaryAsJson"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
DECL|method|summaryAsJson ()
specifier|public
name|String
name|summaryAsJson
parameter_list|()
block|{
return|return
name|catalog
operator|.
name|summaryAsJson
argument_list|()
return|;
block|}
block|}
end_class

end_unit

