begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.api.management.mbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|api
operator|.
name|management
operator|.
name|mbean
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
name|api
operator|.
name|management
operator|.
name|ManagedOperation
import|;
end_import

begin_interface
DECL|interface|ManagedRuntimeCamelCatalogMBean
specifier|public
interface|interface
name|ManagedRuntimeCamelCatalogMBean
block|{
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Returns the component information as JSon format"
argument_list|)
DECL|method|componentJSonSchema (String name)
name|String
name|componentJSonSchema
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Returns the data format information as JSon format."
argument_list|)
DECL|method|dataFormatJSonSchema (String name)
name|String
name|dataFormatJSonSchema
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Returns the language information as JSon format"
argument_list|)
DECL|method|languageJSonSchema (String name)
name|String
name|languageJSonSchema
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Returns the model information as JSon format"
argument_list|)
DECL|method|modelJSonSchema (String name)
name|String
name|modelJSonSchema
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

