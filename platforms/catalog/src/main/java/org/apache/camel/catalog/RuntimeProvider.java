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

begin_comment
comment|/**  * A pluggable strategy for chosen runtime to run Camel such as default, karaf, spring-boot, etc.  * This allows third party runtimes to provide their own provider, that can amend the catalog  * to match the runtime. For example spring-boot or karaf does not support all the default Camel components.  */
end_comment

begin_interface
DECL|interface|RuntimeProvider
specifier|public
interface|interface
name|RuntimeProvider
block|{
comment|/**      * Gets the {@link CamelCatalog}      */
DECL|method|getCamelCatalog ()
name|CamelCatalog
name|getCamelCatalog
parameter_list|()
function_decl|;
comment|/**      * Sets the {@link CamelCatalog} to use      */
DECL|method|setCamelCatalog (CamelCatalog camelCatalog)
name|void
name|setCamelCatalog
parameter_list|(
name|CamelCatalog
name|camelCatalog
parameter_list|)
function_decl|;
comment|/**      * Name of provider such as<tt>default</tt>,<tt>karaf</tt>,<tt>spring-boot</tt>      */
DECL|method|getProviderName ()
name|String
name|getProviderName
parameter_list|()
function_decl|;
comment|/**      * Maven group id of the runtime provider JAR dependency.      */
DECL|method|getProviderGroupId ()
name|String
name|getProviderGroupId
parameter_list|()
function_decl|;
comment|/**      * Maven artifact id of the runtime provider JAR dependency.      */
DECL|method|getProviderArtifactId ()
name|String
name|getProviderArtifactId
parameter_list|()
function_decl|;
comment|/**      * Gets the directory where the component json files are stored in the catalog JAR file      */
DECL|method|getComponentJSonSchemaDirectory ()
name|String
name|getComponentJSonSchemaDirectory
parameter_list|()
function_decl|;
comment|/**      * Gets the directory where the data format json files are stored in the catalog JAR file      */
DECL|method|getDataFormatJSonSchemaDirectory ()
name|String
name|getDataFormatJSonSchemaDirectory
parameter_list|()
function_decl|;
comment|/**      * Gets the directory where the language json files are stored in the catalog JAR file      */
DECL|method|getLanguageJSonSchemaDirectory ()
name|String
name|getLanguageJSonSchemaDirectory
parameter_list|()
function_decl|;
comment|/**      * Gets the directory where the other (miscellaneous) json files are stored in the catalog JAR file      */
DECL|method|getOtherJSonSchemaDirectory ()
name|String
name|getOtherJSonSchemaDirectory
parameter_list|()
function_decl|;
comment|/**      * Find all the component names from the Camel catalog supported by the provider      */
DECL|method|findComponentNames ()
name|List
argument_list|<
name|String
argument_list|>
name|findComponentNames
parameter_list|()
function_decl|;
comment|/**      * Find all the data format names from the Camel catalog supported by the provider      */
DECL|method|findDataFormatNames ()
name|List
argument_list|<
name|String
argument_list|>
name|findDataFormatNames
parameter_list|()
function_decl|;
comment|/**      * Find all the language names from the Camel catalog supported by the provider      */
DECL|method|findLanguageNames ()
name|List
argument_list|<
name|String
argument_list|>
name|findLanguageNames
parameter_list|()
function_decl|;
comment|/**      * Find all the other (miscellaneous) names from the Camel catalog supported by the provider      */
DECL|method|findOtherNames ()
name|List
argument_list|<
name|String
argument_list|>
name|findOtherNames
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

