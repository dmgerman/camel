begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * Represents an object which is aware of the namespaces in which its used such as  * XPath and XQuery type expressions so that the current namespace context can be injected  */
end_comment

begin_interface
DECL|interface|NamespaceAware
specifier|public
interface|interface
name|NamespaceAware
block|{
comment|/**      * Injects the XML Namespaces of prefix -> uri mappings      *      * @param namespaces the XML namespaces with the key of prefixes and the value the URIs      */
DECL|method|setNamespaces (Map<String, String> namespaces)
name|void
name|setNamespaces
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
parameter_list|)
function_decl|;
comment|/**      * Gets the XML Namespaces      */
DECL|method|getNamespaces ()
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getNamespaces
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

