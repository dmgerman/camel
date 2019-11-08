begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Collections
import|;
end_import

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
name|UUID
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
name|AlgorithmMethod
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
name|CanonicalizationMethod
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
name|keyinfo
operator|.
name|KeyInfo
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
name|spec
operator|.
name|XPathFilterParameterSpec
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
name|RuntimeCamelException
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
name|KeyAccessor
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
name|component
operator|.
name|xmlsecurity
operator|.
name|api
operator|.
name|XmlSignatureHelper
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
name|XmlSignatureProperties
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
name|XmlSignatureTransform
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
DECL|class|XmlSignerConfiguration
specifier|public
class|class
name|XmlSignerConfiguration
extends|extends
name|XmlSignatureConfiguration
block|{
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"sign"
argument_list|)
DECL|field|parentXpath
specifier|private
name|XPathFilterParameterSpec
name|parentXpath
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"sign"
argument_list|)
DECL|field|xpathsToIdAttributes
specifier|private
name|List
argument_list|<
name|XPathFilterParameterSpec
argument_list|>
name|xpathsToIdAttributes
init|=
name|Collections
operator|.
name|emptyList
argument_list|()
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"sign"
argument_list|)
DECL|field|transformMethods
specifier|private
name|List
argument_list|<
name|AlgorithmMethod
argument_list|>
name|transformMethods
init|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|XmlSignatureHelper
operator|.
name|getCanonicalizationMethod
argument_list|(
name|CanonicalizationMethod
operator|.
name|INCLUSIVE
argument_list|)
argument_list|)
decl_stmt|;
DECL|field|transformMethodsName
specifier|private
name|String
name|transformMethodsName
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"sign"
argument_list|)
DECL|field|keyAccessor
specifier|private
name|KeyAccessor
name|keyAccessor
decl_stmt|;
DECL|field|keyAccessorName
specifier|private
name|String
name|keyAccessorName
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"sign"
argument_list|,
name|defaultValue
operator|=
literal|"http://www.w3.org/TR/2001/REC-xml-c14n-20010315"
argument_list|)
DECL|field|canonicalizationMethod
specifier|private
name|AlgorithmMethod
name|canonicalizationMethod
init|=
operator|new
name|XmlSignatureTransform
argument_list|(
name|CanonicalizationMethod
operator|.
name|INCLUSIVE
argument_list|)
decl_stmt|;
DECL|field|canonicalizationMethodName
specifier|private
name|String
name|canonicalizationMethodName
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"sign"
argument_list|,
name|defaultValue
operator|=
literal|"http://www.w3.org/2001/04/xmldsig-more#rsa-sha256"
argument_list|)
DECL|field|signatureAlgorithm
specifier|private
name|String
name|signatureAlgorithm
init|=
literal|"http://www.w3.org/2001/04/xmldsig-more#rsa-sha256"
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"sign"
argument_list|)
DECL|field|digestAlgorithm
specifier|private
name|String
name|digestAlgorithm
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"sign"
argument_list|,
name|defaultValue
operator|=
literal|"true"
argument_list|)
DECL|field|addKeyInfoReference
specifier|private
name|Boolean
name|addKeyInfoReference
init|=
name|Boolean
operator|.
name|TRUE
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"sign"
argument_list|,
name|defaultValue
operator|=
literal|"ds"
argument_list|)
DECL|field|prefixForXmlSignatureNamespace
specifier|private
name|String
name|prefixForXmlSignatureNamespace
init|=
literal|"ds"
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"sign"
argument_list|)
DECL|field|contentObjectId
specifier|private
name|String
name|contentObjectId
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"sign"
argument_list|)
DECL|field|signatureId
specifier|private
name|String
name|signatureId
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"sign"
argument_list|)
DECL|field|contentReferenceUri
specifier|private
name|String
name|contentReferenceUri
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"sign"
argument_list|)
DECL|field|contentReferenceType
specifier|private
name|String
name|contentReferenceType
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"sign"
argument_list|)
DECL|field|parentLocalName
specifier|private
name|String
name|parentLocalName
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"sign"
argument_list|)
DECL|field|parentNamespace
specifier|private
name|String
name|parentNamespace
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"sign"
argument_list|,
name|defaultValue
operator|=
literal|"false"
argument_list|)
DECL|field|plainText
specifier|private
name|Boolean
name|plainText
init|=
name|Boolean
operator|.
name|FALSE
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"sign"
argument_list|,
name|defaultValue
operator|=
literal|"UTF-8"
argument_list|)
DECL|field|plainTextEncoding
specifier|private
name|String
name|plainTextEncoding
init|=
literal|"UTF-8"
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|label
operator|=
literal|"sign"
argument_list|)
DECL|field|properties
specifier|private
name|XmlSignatureProperties
name|properties
decl_stmt|;
DECL|field|propertiesName
specifier|private
name|String
name|propertiesName
decl_stmt|;
DECL|method|XmlSignerConfiguration ()
specifier|public
name|XmlSignerConfiguration
parameter_list|()
block|{     }
DECL|method|copy ()
specifier|public
name|XmlSignerConfiguration
name|copy
parameter_list|()
block|{
try|try
block|{
return|return
operator|(
name|XmlSignerConfiguration
operator|)
name|clone
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|super
operator|.
name|setCamelContext
argument_list|(
name|camelContext
argument_list|)
expr_stmt|;
comment|// try to retrieve the references once the context is available.
name|setTransformMethods
argument_list|(
name|transformMethodsName
argument_list|)
expr_stmt|;
name|setCanonicalizationMethod
argument_list|(
name|canonicalizationMethodName
argument_list|)
expr_stmt|;
name|setKeyAccessor
argument_list|(
name|keyAccessorName
argument_list|)
expr_stmt|;
name|setProperties
argument_list|(
name|propertiesName
argument_list|)
expr_stmt|;
block|}
DECL|method|getKeyAccessor ()
specifier|public
name|KeyAccessor
name|getKeyAccessor
parameter_list|()
block|{
return|return
name|keyAccessor
return|;
block|}
comment|/**      * For the signing process, a private key is necessary. You specify a key accessor bean which provides this private key.      * The key accessor bean must implement the KeyAccessor interface. The package org.apache.camel.component.xmlsecurity.api      * contains the default implementation class DefaultKeyAccessor which reads the private key from a Java keystore.      */
DECL|method|setKeyAccessor (KeyAccessor keyAccessor)
specifier|public
name|void
name|setKeyAccessor
parameter_list|(
name|KeyAccessor
name|keyAccessor
parameter_list|)
block|{
name|this
operator|.
name|keyAccessor
operator|=
name|keyAccessor
expr_stmt|;
block|}
comment|/**      * Sets the reference name for a KeyAccessor that can be found in the registry.      */
DECL|method|setKeyAccessor (String keyAccessorName)
specifier|public
name|void
name|setKeyAccessor
parameter_list|(
name|String
name|keyAccessorName
parameter_list|)
block|{
if|if
condition|(
name|getCamelContext
argument_list|()
operator|!=
literal|null
operator|&&
name|keyAccessorName
operator|!=
literal|null
condition|)
block|{
name|KeyAccessor
name|accessor
init|=
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
name|keyAccessorName
argument_list|,
name|KeyAccessor
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|accessor
operator|!=
literal|null
condition|)
block|{
name|setKeyAccessor
argument_list|(
name|accessor
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|keyAccessorName
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|keyAccessorName
operator|=
name|keyAccessorName
expr_stmt|;
block|}
block|}
DECL|method|getCanonicalizationMethod ()
specifier|public
name|AlgorithmMethod
name|getCanonicalizationMethod
parameter_list|()
block|{
return|return
name|canonicalizationMethod
return|;
block|}
comment|/**      * Canonicalization method used to canonicalize the SignedInfo element before the digest is calculated.      * You can use the helper methods XmlSignatureHelper.getCanonicalizationMethod(String algorithm)      * or getCanonicalizationMethod(String algorithm, List<String> inclusiveNamespacePrefixes) to create a canonicalization method.      */
DECL|method|setCanonicalizationMethod (AlgorithmMethod canonicalizationMethod)
specifier|public
name|void
name|setCanonicalizationMethod
parameter_list|(
name|AlgorithmMethod
name|canonicalizationMethod
parameter_list|)
block|{
name|this
operator|.
name|canonicalizationMethod
operator|=
name|canonicalizationMethod
expr_stmt|;
block|}
comment|/**      * Sets the reference name for a AlgorithmMethod that can be found in the registry.      */
DECL|method|setCanonicalizationMethod (String canonicalizationMethodName)
specifier|public
name|void
name|setCanonicalizationMethod
parameter_list|(
name|String
name|canonicalizationMethodName
parameter_list|)
block|{
if|if
condition|(
name|getCamelContext
argument_list|()
operator|!=
literal|null
operator|&&
name|canonicalizationMethodName
operator|!=
literal|null
condition|)
block|{
name|AlgorithmMethod
name|method
init|=
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
name|canonicalizationMethodName
argument_list|,
name|AlgorithmMethod
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|method
operator|!=
literal|null
condition|)
block|{
name|setCanonicalizationMethod
argument_list|(
name|method
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|canonicalizationMethodName
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|canonicalizationMethodName
operator|=
name|canonicalizationMethodName
expr_stmt|;
block|}
block|}
DECL|method|getTransformMethods ()
specifier|public
name|List
argument_list|<
name|AlgorithmMethod
argument_list|>
name|getTransformMethods
parameter_list|()
block|{
return|return
name|transformMethods
return|;
block|}
comment|/**      * Transforms which are executed on the message body before the digest is calculated.      * By default, C14n is added and in the case of enveloped signature (see option parentLocalName) also http://www.w3.org/2000/09/xmldsig#enveloped-signature      * is added at position 0 of the list. Use methods in XmlSignatureHelper to create the transform methods.      */
DECL|method|setTransformMethods (List<AlgorithmMethod> transformMethods)
specifier|public
name|void
name|setTransformMethods
parameter_list|(
name|List
argument_list|<
name|AlgorithmMethod
argument_list|>
name|transformMethods
parameter_list|)
block|{
name|this
operator|.
name|transformMethods
operator|=
name|transformMethods
expr_stmt|;
block|}
comment|/**      * Sets the reference name for a List<AlgorithmMethod> that can be found in the registry.      */
DECL|method|setTransformMethods (String transformMethodsName)
specifier|public
name|void
name|setTransformMethods
parameter_list|(
name|String
name|transformMethodsName
parameter_list|)
block|{
if|if
condition|(
name|getCamelContext
argument_list|()
operator|!=
literal|null
operator|&&
name|transformMethodsName
operator|!=
literal|null
condition|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|List
argument_list|<
name|AlgorithmMethod
argument_list|>
name|list
init|=
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
name|transformMethodsName
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|list
operator|!=
literal|null
condition|)
block|{
name|setTransformMethods
argument_list|(
name|list
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|transformMethodsName
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|transformMethodsName
operator|=
name|transformMethodsName
expr_stmt|;
block|}
block|}
DECL|method|getSignatureAlgorithm ()
specifier|public
name|String
name|getSignatureAlgorithm
parameter_list|()
block|{
return|return
name|signatureAlgorithm
return|;
block|}
comment|/**      * Signature algorithm. Default value is      * "http://www.w3.org/2000/09/xmldsig#rsa-sha1".      */
DECL|method|setSignatureAlgorithm (String signatureAlgorithm)
specifier|public
name|void
name|setSignatureAlgorithm
parameter_list|(
name|String
name|signatureAlgorithm
parameter_list|)
block|{
name|this
operator|.
name|signatureAlgorithm
operator|=
name|signatureAlgorithm
expr_stmt|;
block|}
DECL|method|getDigestAlgorithm ()
specifier|public
name|String
name|getDigestAlgorithm
parameter_list|()
block|{
return|return
name|digestAlgorithm
return|;
block|}
comment|/**      * Digest algorithm URI. Optional parameter. This digest algorithm is used      * for calculating the digest of the input message. If this digest algorithm      * is not specified then the digest algorithm is calculated from the      * signature algorithm. Example: "http://www.w3.org/2001/04/xmlenc#sha256"      */
DECL|method|setDigestAlgorithm (String digestAlgorithm)
specifier|public
name|void
name|setDigestAlgorithm
parameter_list|(
name|String
name|digestAlgorithm
parameter_list|)
block|{
name|this
operator|.
name|digestAlgorithm
operator|=
name|digestAlgorithm
expr_stmt|;
block|}
DECL|method|getAddKeyInfoReference ()
specifier|public
name|Boolean
name|getAddKeyInfoReference
parameter_list|()
block|{
return|return
name|addKeyInfoReference
return|;
block|}
comment|/**      * In order to protect the KeyInfo element from tampering you can add a      * reference to the signed info element so that it is protected via the      * signature value. The default value is<tt>true</tt>.      *<p>      * Only relevant when a KeyInfo is returned by {@link KeyAccessor}. and      * {@link KeyInfo#getId()} is not<code>null</code>.      */
DECL|method|setAddKeyInfoReference (Boolean addKeyInfoReference)
specifier|public
name|void
name|setAddKeyInfoReference
parameter_list|(
name|Boolean
name|addKeyInfoReference
parameter_list|)
block|{
name|this
operator|.
name|addKeyInfoReference
operator|=
name|addKeyInfoReference
expr_stmt|;
block|}
DECL|method|getPrefixForXmlSignatureNamespace ()
specifier|public
name|String
name|getPrefixForXmlSignatureNamespace
parameter_list|()
block|{
return|return
name|prefixForXmlSignatureNamespace
return|;
block|}
comment|/**      * Namespace prefix for the XML signature namespace      * "http://www.w3.org/2000/09/xmldsig#". Default value is "ds".      *      * If<code>null</code> or an empty value is set then no prefix is used for      * the XML signature namespace.      *<p>      * See best practice      * http://www.w3.org/TR/xmldsig-bestpractices/#signing-xml-      * without-namespaces      *      * @param prefixForXmlSignatureNamespace      *            prefix      */
DECL|method|setPrefixForXmlSignatureNamespace (String prefixForXmlSignatureNamespace)
specifier|public
name|void
name|setPrefixForXmlSignatureNamespace
parameter_list|(
name|String
name|prefixForXmlSignatureNamespace
parameter_list|)
block|{
name|this
operator|.
name|prefixForXmlSignatureNamespace
operator|=
name|prefixForXmlSignatureNamespace
expr_stmt|;
block|}
DECL|method|getParentLocalName ()
specifier|public
name|String
name|getParentLocalName
parameter_list|()
block|{
return|return
name|parentLocalName
return|;
block|}
comment|/**      * Local name of the parent element to which the XML signature element will      * be added. Only relevant for enveloped XML signature. Alternatively you can      * also use {@link #setParentXpath(XPathFilterParameterSpec)}.      *      *<p> Default value is      *<code>null</code>. The value must be<code>null</code> for enveloping and      * detached XML signature.      *<p>      * This parameter or the parameter {@link #setParentXpath(XPathFilterParameterSpec)}      * for enveloped signature and the parameter {@link #setXpathsToIdAttributes(List)}      * for detached signature must not be set in the same configuration.      *<p>      * If the parameters<tt>parentXpath</tt> and<tt>parentLocalName</tt> are specified      * in the same configuration then an exception is thrown.      *      * @param parentLocalName      *            local name      */
DECL|method|setParentLocalName (String parentLocalName)
specifier|public
name|void
name|setParentLocalName
parameter_list|(
name|String
name|parentLocalName
parameter_list|)
block|{
name|this
operator|.
name|parentLocalName
operator|=
name|parentLocalName
expr_stmt|;
block|}
DECL|method|getParentNamespace ()
specifier|public
name|String
name|getParentNamespace
parameter_list|()
block|{
return|return
name|parentNamespace
return|;
block|}
comment|/**      * Namespace of the parent element to which the XML signature element will      * be added.      */
DECL|method|setParentNamespace (String parentNamespace)
specifier|public
name|void
name|setParentNamespace
parameter_list|(
name|String
name|parentNamespace
parameter_list|)
block|{
name|this
operator|.
name|parentNamespace
operator|=
name|parentNamespace
expr_stmt|;
block|}
DECL|method|getContentObjectId ()
specifier|public
name|String
name|getContentObjectId
parameter_list|()
block|{
if|if
condition|(
name|contentObjectId
operator|==
literal|null
condition|)
block|{
comment|// content object ID must always be set, because it is only used in enveloping case.
name|contentObjectId
operator|=
literal|"_"
operator|+
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
expr_stmt|;
block|}
return|return
name|contentObjectId
return|;
block|}
comment|/**      * Sets the content object Id attribute value. By default a UUID is      * generated. If you set the<code>null</code> value, then a new UUID will      * be generated. Only used in the enveloping case.      */
DECL|method|setContentObjectId (String contentObjectId)
specifier|public
name|void
name|setContentObjectId
parameter_list|(
name|String
name|contentObjectId
parameter_list|)
block|{
name|this
operator|.
name|contentObjectId
operator|=
name|contentObjectId
expr_stmt|;
block|}
DECL|method|getSignatureId ()
specifier|public
name|String
name|getSignatureId
parameter_list|()
block|{
return|return
name|signatureId
return|;
block|}
comment|/**      * Sets the signature Id. If this parameter is not set (null value) then a      * unique ID is generated for the signature ID (default). If this parameter      * is set to "" (empty string) then no Id attribute is created in the      * signature element.      */
DECL|method|setSignatureId (String signatureId)
specifier|public
name|void
name|setSignatureId
parameter_list|(
name|String
name|signatureId
parameter_list|)
block|{
name|this
operator|.
name|signatureId
operator|=
name|signatureId
expr_stmt|;
block|}
DECL|method|getContentReferenceUri ()
specifier|public
name|String
name|getContentReferenceUri
parameter_list|()
block|{
return|return
name|contentReferenceUri
return|;
block|}
comment|/**      * Reference URI for the content to be signed. Only used in the enveloped      * case. If the reference URI contains an ID attribute value, then the      * resource schema URI ( {@link #setSchemaResourceUri(String)}) must also be      * set because the schema validator will then find out which attributes are      * ID attributes. Will be ignored in the enveloping or detached case.      */
DECL|method|setContentReferenceUri (String referenceUri)
specifier|public
name|void
name|setContentReferenceUri
parameter_list|(
name|String
name|referenceUri
parameter_list|)
block|{
name|this
operator|.
name|contentReferenceUri
operator|=
name|referenceUri
expr_stmt|;
block|}
DECL|method|getContentReferenceType ()
specifier|public
name|String
name|getContentReferenceType
parameter_list|()
block|{
return|return
name|contentReferenceType
return|;
block|}
comment|/**      * Type of the content reference. The default value is<code>null</code>.      * This value can be overwritten by the header      * {@link XmlSignatureConstants#HEADER_CONTENT_REFERENCE_TYPE}.      */
DECL|method|setContentReferenceType (String referenceType)
specifier|public
name|void
name|setContentReferenceType
parameter_list|(
name|String
name|referenceType
parameter_list|)
block|{
name|this
operator|.
name|contentReferenceType
operator|=
name|referenceType
expr_stmt|;
block|}
DECL|method|getPlainText ()
specifier|public
name|Boolean
name|getPlainText
parameter_list|()
block|{
return|return
name|plainText
return|;
block|}
comment|/**      * Indicator whether the message body contains plain text. The default value      * is<code>false</code>, indicating that the message body contains XML. The      * value can be overwritten by the header      * {@link XmlSignatureConstants#HEADER_MESSAGE_IS_PLAIN_TEXT}.      */
DECL|method|setPlainText (Boolean plainText)
specifier|public
name|void
name|setPlainText
parameter_list|(
name|Boolean
name|plainText
parameter_list|)
block|{
name|this
operator|.
name|plainText
operator|=
name|plainText
expr_stmt|;
block|}
DECL|method|getPlainTextEncoding ()
specifier|public
name|String
name|getPlainTextEncoding
parameter_list|()
block|{
return|return
name|plainTextEncoding
return|;
block|}
comment|/**      * Encoding of the plain text. Only relevant if the message body is plain      * text (see parameter {@link #plainText}. Default value is "UTF-8".      */
DECL|method|setPlainTextEncoding (String plainTextEncoding)
specifier|public
name|void
name|setPlainTextEncoding
parameter_list|(
name|String
name|plainTextEncoding
parameter_list|)
block|{
name|this
operator|.
name|plainTextEncoding
operator|=
name|plainTextEncoding
expr_stmt|;
block|}
DECL|method|getProperties ()
specifier|public
name|XmlSignatureProperties
name|getProperties
parameter_list|()
block|{
return|return
name|properties
return|;
block|}
comment|/**      * For adding additional References and Objects to the XML signature which contain additional properties,      * you can provide a bean which implements the XmlSignatureProperties interface.      */
DECL|method|setProperties (XmlSignatureProperties properties)
specifier|public
name|void
name|setProperties
parameter_list|(
name|XmlSignatureProperties
name|properties
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
comment|/**      * Sets the reference name for a XmlSignatureProperties that can be found in the registry.      */
DECL|method|setProperties (String propertiesName)
specifier|public
name|void
name|setProperties
parameter_list|(
name|String
name|propertiesName
parameter_list|)
block|{
if|if
condition|(
name|getCamelContext
argument_list|()
operator|!=
literal|null
operator|&&
name|propertiesName
operator|!=
literal|null
condition|)
block|{
name|XmlSignatureProperties
name|props
init|=
name|getCamelContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookupByNameAndType
argument_list|(
name|propertiesName
argument_list|,
name|XmlSignatureProperties
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|props
operator|!=
literal|null
condition|)
block|{
name|setProperties
argument_list|(
name|props
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|propertiesName
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|propertiesName
operator|=
name|propertiesName
expr_stmt|;
block|}
block|}
DECL|method|getKeyAccessorName ()
specifier|public
name|String
name|getKeyAccessorName
parameter_list|()
block|{
return|return
name|keyAccessorName
return|;
block|}
DECL|method|setKeyAccessorName (String keyAccessorName)
specifier|public
name|void
name|setKeyAccessorName
parameter_list|(
name|String
name|keyAccessorName
parameter_list|)
block|{
name|this
operator|.
name|keyAccessorName
operator|=
name|keyAccessorName
expr_stmt|;
block|}
DECL|method|getCanonicalizationMethodName ()
specifier|public
name|String
name|getCanonicalizationMethodName
parameter_list|()
block|{
return|return
name|canonicalizationMethodName
return|;
block|}
DECL|method|setCanonicalizationMethodName (String canonicalizationMethodName)
specifier|public
name|void
name|setCanonicalizationMethodName
parameter_list|(
name|String
name|canonicalizationMethodName
parameter_list|)
block|{
name|this
operator|.
name|canonicalizationMethodName
operator|=
name|canonicalizationMethodName
expr_stmt|;
block|}
DECL|method|getTransformMethodsName ()
specifier|public
name|String
name|getTransformMethodsName
parameter_list|()
block|{
return|return
name|transformMethodsName
return|;
block|}
DECL|method|setTransformMethodsName (String transformMethodsName)
specifier|public
name|void
name|setTransformMethodsName
parameter_list|(
name|String
name|transformMethodsName
parameter_list|)
block|{
name|this
operator|.
name|transformMethodsName
operator|=
name|transformMethodsName
expr_stmt|;
block|}
DECL|method|getPropertiesName ()
specifier|public
name|String
name|getPropertiesName
parameter_list|()
block|{
return|return
name|propertiesName
return|;
block|}
DECL|method|setPropertiesName (String propertiesName)
specifier|public
name|void
name|setPropertiesName
parameter_list|(
name|String
name|propertiesName
parameter_list|)
block|{
name|this
operator|.
name|propertiesName
operator|=
name|propertiesName
expr_stmt|;
block|}
DECL|method|getXpathsToIdAttributes ()
specifier|public
name|List
argument_list|<
name|XPathFilterParameterSpec
argument_list|>
name|getXpathsToIdAttributes
parameter_list|()
block|{
return|return
name|xpathsToIdAttributes
return|;
block|}
comment|/**      * Define the elements which are signed in the detached case via XPATH      * expressions to ID attributes (attributes of type ID). For each element      * found via the XPATH expression a detached signature is created whose      * reference URI contains the corresponding attribute value (preceded by      * '#'). The signature becomes the last sibling of the signed element.      * Elements with deeper hierarchy level are signed first.      *<p>      * You can also set the XPATH list dynamically via the header      * {@link XmlSignatureConstants#HEADER_XPATHS_TO_ID_ATTRIBUTES}.      *<p>      * The parameter {@link #setParentLocalName(String)} or {@link #setParentXpath(XPathFilterParameterSpec)}      * for enveloped signature and this parameter for detached signature must not      * be set in the same configuration.      */
DECL|method|setXpathsToIdAttributes (List<XPathFilterParameterSpec> xpathsToIdAttributes)
specifier|public
name|void
name|setXpathsToIdAttributes
parameter_list|(
name|List
argument_list|<
name|XPathFilterParameterSpec
argument_list|>
name|xpathsToIdAttributes
parameter_list|)
block|{
if|if
condition|(
name|xpathsToIdAttributes
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|xpathsToIdAttributes
operator|=
name|Collections
operator|.
name|emptyList
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|xpathsToIdAttributes
operator|=
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|xpathsToIdAttributes
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getParentXpath ()
specifier|public
name|XPathFilterParameterSpec
name|getParentXpath
parameter_list|()
block|{
return|return
name|parentXpath
return|;
block|}
comment|/**      * Sets the XPath to find the parent node in the enveloped case.      * Either you specify the parent node via this method or the local name and namespace of the parent      * with the methods {@link #setParentLocalName(String)} and {@link #setParentNamespace(String)}.      *<p>      * Default value is<code>null</code>. The value must be<code>null</code> for enveloping and      * detached XML signature.      *<p>      * If the parameters<tt>parentXpath</tt> and<tt>parentLocalName</tt> are specified      * in the same configuration then an exception is thrown.      *      * @param parentXpath xpath to the parent node, if the xpath returns several values then the first Element node is used      */
DECL|method|setParentXpath (XPathFilterParameterSpec parentXpath)
specifier|public
name|void
name|setParentXpath
parameter_list|(
name|XPathFilterParameterSpec
name|parentXpath
parameter_list|)
block|{
name|this
operator|.
name|parentXpath
operator|=
name|parentXpath
expr_stmt|;
block|}
block|}
end_class

end_unit

