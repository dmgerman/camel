begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.jaxb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|jaxb
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
comment|/**  * A prefix mapper for namespaces to control namespaces during JAXB marshalling.  */
end_comment

begin_interface
DECL|interface|JaxbNamespacePrefixMapper
specifier|public
interface|interface
name|JaxbNamespacePrefixMapper
block|{
comment|/**      * JAXB requires the mapper to be registered as a property on the {@link javax.xml.bind.JAXBContext}.      */
DECL|method|getRegistrationKey ()
name|String
name|getRegistrationKey
parameter_list|()
function_decl|;
comment|/**      * Sets the namespace prefix mapping.      *<p/>      * The key is the namespace, the value is the prefix to use.      *      * @param namespaces  namespace mappings      */
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
comment|/**      * Used by JAXB to obtain the preferred prefix.      */
DECL|method|getPreferredPrefix (String namespaceUri, String suggestion, boolean requirePrefix)
name|String
name|getPreferredPrefix
parameter_list|(
name|String
name|namespaceUri
parameter_list|,
name|String
name|suggestion
parameter_list|,
name|boolean
name|requirePrefix
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

