begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xmlsecurity.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xmlsecurity
operator|.
name|processor
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

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|URIDereferencer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|XMLCryptoContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|dsig
operator|.
name|XMLSignContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|dsig
operator|.
name|XMLValidateContext
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|CamelContextAware
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
name|component
operator|.
name|xmlsecurity
operator|.
name|api
operator|.
name|XmlSignatureConstants
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
name|UriParam
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
name|UriParams
import|;
end_import

begin_class
annotation|@
name|UriParams
DECL|class|XmlSignatureConfiguration
specifier|public
specifier|abstract
class|class
name|XmlSignatureConfiguration
implements|implements
name|Cloneable
implements|,
name|CamelContextAware
block|{
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
DECL|field|uriDereferencer
specifier|private
name|URIDereferencer
name|uriDereferencer
decl_stmt|;
annotation|@
name|UriParam
DECL|field|baseUri
specifier|private
name|String
name|baseUri
decl_stmt|;
annotation|@
name|UriParam
DECL|field|cryptoContextProperties
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
name|cryptoContextProperties
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"Boolean.TRUE"
argument_list|)
DECL|field|disallowDoctypeDecl
specifier|private
name|Boolean
name|disallowDoctypeDecl
init|=
name|Boolean
operator|.
name|TRUE
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"Boolean.FALSE"
argument_list|)
DECL|field|omitXmlDeclaration
specifier|private
name|Boolean
name|omitXmlDeclaration
init|=
name|Boolean
operator|.
name|FALSE
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|clearHeaders
specifier|private
name|Boolean
name|clearHeaders
init|=
name|Boolean
operator|.
name|TRUE
decl_stmt|;
annotation|@
name|UriParam
DECL|field|schemaResourceUri
specifier|private
name|String
name|schemaResourceUri
decl_stmt|;
annotation|@
name|UriParam
DECL|field|outputXmlEncoding
specifier|private
name|String
name|outputXmlEncoding
decl_stmt|;
DECL|method|XmlSignatureConfiguration ()
specifier|public
name|XmlSignatureConfiguration
parameter_list|()
block|{     }
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|camelContext
expr_stmt|;
block|}
DECL|method|getUriDereferencer ()
specifier|public
name|URIDereferencer
name|getUriDereferencer
parameter_list|()
block|{
return|return
name|uriDereferencer
return|;
block|}
comment|/**      * If you want to restrict the remote access via reference URIs, you can set      * an own dereferencer. Optional parameter. If not set the provider default      * dereferencer is used which can resolve URI fragments, HTTP, file and      * XPpointer URIs.      *<p>      * Attention: The implementation is provider dependent!      *       * @param uriDereferencer      * @see XMLCryptoContext#setURIDereferencer(URIDereferencer)      */
DECL|method|setUriDereferencer (URIDereferencer uriDereferencer)
specifier|public
name|void
name|setUriDereferencer
parameter_list|(
name|URIDereferencer
name|uriDereferencer
parameter_list|)
block|{
name|this
operator|.
name|uriDereferencer
operator|=
name|uriDereferencer
expr_stmt|;
block|}
DECL|method|getBaseUri ()
specifier|public
name|String
name|getBaseUri
parameter_list|()
block|{
return|return
name|baseUri
return|;
block|}
comment|/**      * You can set a base URI which is used in the URI dereferencing. Relative      * URIs are then concatenated with the base URI.      *       * @param baseUri      *            base URI      *       * @see XMLCryptoContext#setBaseURI(String)      */
DECL|method|setBaseUri (String baseUri)
specifier|public
name|void
name|setBaseUri
parameter_list|(
name|String
name|baseUri
parameter_list|)
block|{
name|this
operator|.
name|baseUri
operator|=
name|baseUri
expr_stmt|;
block|}
DECL|method|getCryptoContextProperties ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|?
extends|extends
name|Object
argument_list|>
name|getCryptoContextProperties
parameter_list|()
block|{
return|return
name|cryptoContextProperties
return|;
block|}
comment|/**      * Sets the crypto context properties. See      * {@link XMLCryptoContext#setProperty(String, Object)}. Possible properties      * are defined in {@link XMLSignContext} an {@link XMLValidateContext} (see      * Supported Properties).      *<p>      * The following properties are set by default to the value      * {@link Boolean#TRUE} for the XML validation. If you want to switch these      * features off you must set the property value to {@link Boolean#FALSE}.      *<ul>      *<li><code>"org.jcp.xml.dsig.validateManifests"</code></li>      *<li><code>"javax.xml.crypto.dsig.cacheReference"</code></li>      *</ul>      *       * @param cryptoContextProperties      */
DECL|method|setCryptoContextProperties (Map<String, ? extends Object> cryptoContextProperties)
specifier|public
name|void
name|setCryptoContextProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|?
extends|extends
name|Object
argument_list|>
name|cryptoContextProperties
parameter_list|)
block|{
name|this
operator|.
name|cryptoContextProperties
operator|=
name|cryptoContextProperties
expr_stmt|;
block|}
DECL|method|getDisallowDoctypeDecl ()
specifier|public
name|Boolean
name|getDisallowDoctypeDecl
parameter_list|()
block|{
return|return
name|disallowDoctypeDecl
return|;
block|}
comment|/**      * Disallows that the incoming XML document contains DTD DOCTYPE      * declaration. The default value is {@link Boolean#TRUE}.      *       * @param disallowDoctypeDecl      *            if set to {@link Boolean#FALSE} then DOCTYPE declaration is      *            allowed, otherwise not      */
DECL|method|setDisallowDoctypeDecl (Boolean disallowDoctypeDecl)
specifier|public
name|void
name|setDisallowDoctypeDecl
parameter_list|(
name|Boolean
name|disallowDoctypeDecl
parameter_list|)
block|{
name|this
operator|.
name|disallowDoctypeDecl
operator|=
name|disallowDoctypeDecl
expr_stmt|;
block|}
DECL|method|getOmitXmlDeclaration ()
specifier|public
name|Boolean
name|getOmitXmlDeclaration
parameter_list|()
block|{
return|return
name|omitXmlDeclaration
return|;
block|}
comment|/**      * Indicator whether the XML declaration in the outgoing message body should      * be omitted. Default value is<code>false</code>. Can be overwritten by      * the header {@link XmlSignatureConstants#HEADER_OMIT_XML_DECLARATION}.      */
DECL|method|setOmitXmlDeclaration (Boolean omitXmlDeclaration)
specifier|public
name|void
name|setOmitXmlDeclaration
parameter_list|(
name|Boolean
name|omitXmlDeclaration
parameter_list|)
block|{
name|this
operator|.
name|omitXmlDeclaration
operator|=
name|omitXmlDeclaration
expr_stmt|;
block|}
comment|/**      * Determines if the XML signature specific headers be cleared after signing      * and verification. Defaults to true.      *       * @return true if the Signature headers should be unset, false otherwise      */
DECL|method|getClearHeaders ()
specifier|public
name|Boolean
name|getClearHeaders
parameter_list|()
block|{
return|return
name|clearHeaders
return|;
block|}
comment|/**      * Determines if the XML signature specific headers be cleared after signing      * and verification. Defaults to true.      */
DECL|method|setClearHeaders (Boolean clearHeaders)
specifier|public
name|void
name|setClearHeaders
parameter_list|(
name|Boolean
name|clearHeaders
parameter_list|)
block|{
name|this
operator|.
name|clearHeaders
operator|=
name|clearHeaders
expr_stmt|;
block|}
DECL|method|getSchemaResourceUri ()
specifier|public
name|String
name|getSchemaResourceUri
parameter_list|()
block|{
return|return
name|schemaResourceUri
return|;
block|}
comment|/**      * Classpath to the XML Schema. Must be specified in the detached XML      * Signature case for determining the ID attributes, might be set in the      * enveloped and enveloping case. If set, then the XML document is validated      * with the specified XML schema. The schema resource URI can be overwritten      * by the header {@link XmlSignatureConstants#HEADER_SCHEMA_RESOURCE_URI}.      */
DECL|method|setSchemaResourceUri (String schemaResourceUri)
specifier|public
name|void
name|setSchemaResourceUri
parameter_list|(
name|String
name|schemaResourceUri
parameter_list|)
block|{
name|this
operator|.
name|schemaResourceUri
operator|=
name|schemaResourceUri
expr_stmt|;
block|}
DECL|method|getOutputXmlEncoding ()
specifier|public
name|String
name|getOutputXmlEncoding
parameter_list|()
block|{
return|return
name|outputXmlEncoding
return|;
block|}
comment|/**      * The character encoding of the resulting signed XML document. If      *<code>null</code> then the encoding of the original XML document is used.      *       * @param outputXmlEncoding      *            character encoding      */
DECL|method|setOutputXmlEncoding (String outputXmlEncoding)
specifier|public
name|void
name|setOutputXmlEncoding
parameter_list|(
name|String
name|outputXmlEncoding
parameter_list|)
block|{
name|this
operator|.
name|outputXmlEncoding
operator|=
name|outputXmlEncoding
expr_stmt|;
block|}
block|}
end_class

end_unit

