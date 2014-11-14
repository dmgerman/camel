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

begin_comment
comment|/**  * Catalog of all the Camel components from this Apache Camel release.  */
end_comment

begin_interface
DECL|interface|CamelComponentCatalog
specifier|public
interface|interface
name|CamelComponentCatalog
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
comment|/**      * Find all the component names from the Camel catalog that matches the label      */
DECL|method|findComponentNames (String label)
name|List
argument_list|<
name|String
argument_list|>
name|findComponentNames
parameter_list|(
name|String
name|label
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
comment|/**      * Find all the unique label names all the components are using.      *      * @return a set of all the labels.      */
DECL|method|findLabels ()
name|Set
argument_list|<
name|String
argument_list|>
name|findLabels
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

