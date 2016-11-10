begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xslt
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xslt
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|URIResolver
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

begin_comment
comment|/**  * Can be used to create custom URI resolver for the XSLT endpoint. This  * interface is useful, if the custom URI resolver depends on the resource URI  * specified in the XSLT endpoint. The resource URI of the endpoint can be even  * dynamic, like in the following example:  *   *<pre>  * {@code<camel:recipientList>}   * {@code<camel:simple>xslt:${header.XSLT_FILE}?uriResolverFactory=#uriResolverFactory</camel:simple>}  * {@code</camel:recipientList>}  *</pre>  *   * The dynamic resource URI given in ${header.XSLT_FILE} will be past as  * resourceUri parameter in the method  * {@link #createUriResolver(CamelContext, String)}  */
end_comment

begin_interface
DECL|interface|XsltUriResolverFactory
specifier|public
interface|interface
name|XsltUriResolverFactory
block|{
comment|/**      * Method is called during the creation of a xslt endpoint.      *       * @param camelContext camel context      * @param resourceUri resource URI specified in the endpoint URI      * @return URI resolver      */
DECL|method|createUriResolver (CamelContext camelContext, String resourceUri)
name|URIResolver
name|createUriResolver
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|String
name|resourceUri
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

