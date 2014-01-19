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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|NoSuchProviderException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|javax
operator|.
name|xml
operator|.
name|crypto
operator|.
name|KeySelector
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
name|XMLStructure
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
name|Manifest
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
name|Reference
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
name|SignedInfo
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
name|XMLObject
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
name|XMLSignature
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
name|XMLSignature
operator|.
name|SignatureValue
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
name|XMLSignatureException
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
name|XMLSignatureFactory
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
name|dom
operator|.
name|DOMValidateContext
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
name|parsers
operator|.
name|ParserConfigurationException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|NodeList
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXException
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
name|Exchange
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
name|Message
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
name|ValidationFailedHandler
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
name|XmlSignature2Message
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
name|XmlSignatureChecker
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
name|XmlSignatureFormatException
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
name|XmlSignatureInvalidException
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
name|util
operator|.
name|IOHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * XML signature verifier. Assumes that the input XML contains exactly one  * Signature element.  */
end_comment

begin_class
DECL|class|XmlVerifierProcessor
specifier|public
class|class
name|XmlVerifierProcessor
extends|extends
name|XmlSignatureProcessor
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|XmlVerifierProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|config
specifier|private
specifier|final
name|XmlVerifierConfiguration
name|config
decl_stmt|;
DECL|method|XmlVerifierProcessor (XmlVerifierConfiguration config)
specifier|public
name|XmlVerifierProcessor
parameter_list|(
name|XmlVerifierConfiguration
name|config
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getConfiguration ()
specifier|public
name|XmlVerifierConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|config
return|;
block|}
annotation|@
name|Override
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|InputStream
name|stream
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
try|try
block|{
comment|// lets setup the out message before we invoke the signing
comment|// so that it can mutate it if necessary
name|Message
name|out
init|=
name|exchange
operator|.
name|getOut
argument_list|()
decl_stmt|;
name|out
operator|.
name|copyFrom
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|stream
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|clearMessageHeaders
argument_list|(
name|out
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// remove OUT message, as an exception occurred
name|exchange
operator|.
name|setOut
argument_list|(
literal|null
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|stream
argument_list|,
literal|"input stream"
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|verify (InputStream input, final Message out)
specifier|protected
name|void
name|verify
parameter_list|(
name|InputStream
name|input
parameter_list|,
specifier|final
name|Message
name|out
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Verification of XML signature document started"
argument_list|)
expr_stmt|;
specifier|final
name|Document
name|doc
init|=
name|parseInput
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|Node
name|signatureNode
init|=
name|getSignatureNode
argument_list|(
name|doc
argument_list|)
decl_stmt|;
name|XMLSignatureFactory
name|fac
decl_stmt|;
comment|// Try to install the Santuario Provider - fall back to the JDK provider if this does
comment|// not work
try|try
block|{
name|fac
operator|=
name|XMLSignatureFactory
operator|.
name|getInstance
argument_list|(
literal|"DOM"
argument_list|,
literal|"ApacheXMLDSig"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchProviderException
name|ex
parameter_list|)
block|{
name|fac
operator|=
name|XMLSignatureFactory
operator|.
name|getInstance
argument_list|(
literal|"DOM"
argument_list|)
expr_stmt|;
block|}
name|KeySelector
name|selector
init|=
name|getConfiguration
argument_list|()
operator|.
name|getKeySelector
argument_list|()
decl_stmt|;
if|if
condition|(
name|selector
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Wrong configuration. Key selector is missing."
argument_list|)
throw|;
block|}
name|DOMValidateContext
name|valContext
init|=
operator|new
name|DOMValidateContext
argument_list|(
name|selector
argument_list|,
name|signatureNode
argument_list|)
decl_stmt|;
name|valContext
operator|.
name|setProperty
argument_list|(
literal|"javax.xml.crypto.dsig.cacheReference"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|valContext
operator|.
name|setProperty
argument_list|(
literal|"org.jcp.xml.dsig.validateManifests"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|getSecureValidation
argument_list|()
operator|==
name|Boolean
operator|.
name|TRUE
condition|)
block|{
name|valContext
operator|.
name|setProperty
argument_list|(
literal|"org.apache.jcp.xml.dsig.secureValidation"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|valContext
operator|.
name|setProperty
argument_list|(
literal|"org.jcp.xml.dsig.secureValidation"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
name|setUriDereferencerAndBaseUri
argument_list|(
name|valContext
argument_list|)
expr_stmt|;
name|setCryptoContextProperties
argument_list|(
name|valContext
argument_list|)
expr_stmt|;
specifier|final
name|XMLSignature
name|signature
init|=
name|fac
operator|.
name|unmarshalXMLSignature
argument_list|(
name|valContext
argument_list|)
decl_stmt|;
name|executeApplicationCheck
argument_list|(
name|out
argument_list|,
name|doc
argument_list|,
name|signature
argument_list|)
expr_stmt|;
name|boolean
name|coreValidity
decl_stmt|;
try|try
block|{
name|coreValidity
operator|=
name|signature
operator|.
name|validate
argument_list|(
name|valContext
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLSignatureException
name|se
parameter_list|)
block|{
throw|throw
name|getConfiguration
argument_list|()
operator|.
name|getValidationFailedHandler
argument_list|()
operator|.
name|onXMLSignatureException
argument_list|(
name|se
argument_list|)
throw|;
block|}
comment|// Check core validation status
name|boolean
name|goon
init|=
name|coreValidity
decl_stmt|;
if|if
condition|(
operator|!
name|coreValidity
condition|)
block|{
name|goon
operator|=
name|handleSignatureValidationFailed
argument_list|(
name|valContext
argument_list|,
name|signature
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|goon
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"XML signature verified"
argument_list|)
expr_stmt|;
name|map2Message
argument_list|(
name|signature
argument_list|,
name|out
argument_list|,
name|doc
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|XmlSignatureInvalidException
argument_list|(
literal|""
argument_list|)
throw|;
block|}
block|}
DECL|method|executeApplicationCheck (final Message out, final Document doc, final XMLSignature signature)
specifier|private
name|void
name|executeApplicationCheck
parameter_list|(
specifier|final
name|Message
name|out
parameter_list|,
specifier|final
name|Document
name|doc
parameter_list|,
specifier|final
name|XMLSignature
name|signature
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|getConfiguration
argument_list|()
operator|.
name|getXmlSignatureChecker
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|XmlSignatureChecker
operator|.
name|Input
name|checkerInput
init|=
operator|new
name|XmlSignatureChecker
operator|.
name|Input
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|SignedInfo
name|getSignedInfo
parameter_list|()
block|{
return|return
name|signature
operator|.
name|getSignedInfo
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|SignatureValue
name|getSignatureValue
parameter_list|()
block|{
return|return
name|signature
operator|.
name|getSignatureValue
argument_list|()
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|?
extends|extends
name|XMLObject
argument_list|>
name|getObjects
parameter_list|()
block|{
return|return
name|signature
operator|.
name|getObjects
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Document
name|getMessageBodyDocument
parameter_list|()
block|{
return|return
name|doc
return|;
block|}
annotation|@
name|Override
specifier|public
name|Message
name|getMessage
parameter_list|()
block|{
return|return
name|out
return|;
block|}
annotation|@
name|Override
specifier|public
name|KeyInfo
name|getKeyInfo
parameter_list|()
block|{
return|return
name|signature
operator|.
name|getKeyInfo
argument_list|()
return|;
block|}
block|}
decl_stmt|;
name|getConfiguration
argument_list|()
operator|.
name|getXmlSignatureChecker
argument_list|()
operator|.
name|checkBeforeCoreValidation
argument_list|(
name|checkerInput
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|map2Message (XMLSignature signature, Message out, final Document messageBodyDocument)
specifier|private
name|void
name|map2Message
parameter_list|(
name|XMLSignature
name|signature
parameter_list|,
name|Message
name|out
parameter_list|,
specifier|final
name|Document
name|messageBodyDocument
parameter_list|)
throws|throws
name|Exception
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|final
name|List
argument_list|<
name|Reference
argument_list|>
name|refs
init|=
operator|new
name|ArrayList
argument_list|<
name|Reference
argument_list|>
argument_list|(
name|signature
operator|.
name|getSignedInfo
argument_list|()
operator|.
name|getReferences
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|final
name|List
argument_list|<
name|XMLObject
argument_list|>
name|objs
init|=
operator|new
name|ArrayList
argument_list|<
name|XMLObject
argument_list|>
argument_list|(
name|signature
operator|.
name|getObjects
argument_list|()
argument_list|)
decl_stmt|;
name|XmlSignature2Message
operator|.
name|Input
name|refsAndObjects
init|=
operator|new
name|XmlSignature2Message
operator|.
name|Input
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|Reference
argument_list|>
name|getReferences
parameter_list|()
block|{
return|return
name|refs
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|XMLObject
argument_list|>
name|getObjects
parameter_list|()
block|{
return|return
name|objs
return|;
block|}
annotation|@
name|Override
specifier|public
name|Document
name|getMessageBodyDocument
parameter_list|()
block|{
return|return
name|messageBodyDocument
return|;
block|}
annotation|@
name|Override
specifier|public
name|Boolean
name|omitXmlDeclaration
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getOmitXmlDeclaration
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|getOutputNodeSearch
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getOutputNodeSearch
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getOutputNodeSearchType
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getOutputNodeSearchType
argument_list|()
return|;
block|}
specifier|public
name|Boolean
name|getRemoveSignatureElements
parameter_list|()
block|{
return|return
name|getConfiguration
argument_list|()
operator|.
name|getRemoveSignatureElements
argument_list|()
return|;
block|}
block|}
decl_stmt|;
name|getConfiguration
argument_list|()
operator|.
name|getXmlSignature2Message
argument_list|()
operator|.
name|mapToMessage
argument_list|(
name|refsAndObjects
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
DECL|method|getSignatureNode (Document doc)
specifier|private
name|Node
name|getSignatureNode
parameter_list|(
name|Document
name|doc
parameter_list|)
throws|throws
name|IOException
throws|,
name|ParserConfigurationException
throws|,
name|XmlSignatureFormatException
block|{
comment|// Find Signature element
name|NodeList
name|nl
init|=
name|doc
operator|.
name|getElementsByTagNameNS
argument_list|(
name|XMLSignature
operator|.
name|XMLNS
argument_list|,
literal|"Signature"
argument_list|)
decl_stmt|;
if|if
condition|(
name|nl
operator|.
name|getLength
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|XmlSignatureFormatException
argument_list|(
literal|"Message is not a correct XML signature document: 'Signature' element is missing. Check the sent message."
argument_list|)
throw|;
block|}
if|if
condition|(
name|nl
operator|.
name|getLength
argument_list|()
operator|!=
literal|1
condition|)
block|{
throw|throw
operator|new
name|XmlSignatureFormatException
argument_list|(
literal|"XML signature document is not supported; it contains more than one signature element. Check the sent message."
argument_list|)
throw|;
block|}
name|Node
name|signatureNode
init|=
name|nl
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Signature element found"
argument_list|)
expr_stmt|;
return|return
name|signatureNode
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|handleSignatureValidationFailed (DOMValidateContext valContext, XMLSignature signature)
specifier|protected
name|boolean
name|handleSignatureValidationFailed
parameter_list|(
name|DOMValidateContext
name|valContext
parameter_list|,
name|XMLSignature
name|signature
parameter_list|)
throws|throws
name|Exception
block|{
name|ValidationFailedHandler
name|handler
init|=
name|getConfiguration
argument_list|()
operator|.
name|getValidationFailedHandler
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"handleSignatureValidationFailed called"
argument_list|)
expr_stmt|;
try|try
block|{
name|handler
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// first check signature value, see
comment|// https://www.isecpartners.com/media/12012/XMLDSIG_Command_Injection.pdf
name|SignatureValue
name|sigValue
init|=
name|signature
operator|.
name|getSignatureValue
argument_list|()
decl_stmt|;
name|boolean
name|sv
init|=
name|sigValue
operator|.
name|validate
argument_list|(
name|valContext
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|sv
condition|)
block|{
name|handler
operator|.
name|signatureValueValidationFailed
argument_list|(
name|sigValue
argument_list|)
expr_stmt|;
block|}
comment|// then the references!
comment|// check the validation status of each Reference
for|for
control|(
name|Reference
name|ref
range|:
operator|(
name|List
argument_list|<
name|Reference
argument_list|>
operator|)
name|signature
operator|.
name|getSignedInfo
argument_list|()
operator|.
name|getReferences
argument_list|()
control|)
block|{
name|boolean
name|refValid
init|=
name|ref
operator|.
name|validate
argument_list|(
name|valContext
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|refValid
condition|)
block|{
name|handler
operator|.
name|referenceValidationFailed
argument_list|(
name|ref
argument_list|)
expr_stmt|;
block|}
block|}
comment|// validate Manifests, if property set
if|if
condition|(
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|valContext
operator|.
name|getProperty
argument_list|(
literal|"org.jcp.xml.dsig.validateManifests"
argument_list|)
argument_list|)
condition|)
block|{
for|for
control|(
name|XMLObject
name|xo
range|:
operator|(
name|List
argument_list|<
name|XMLObject
argument_list|>
operator|)
name|signature
operator|.
name|getObjects
argument_list|()
control|)
block|{
name|List
argument_list|<
name|XMLStructure
argument_list|>
name|content
init|=
name|xo
operator|.
name|getContent
argument_list|()
decl_stmt|;
for|for
control|(
name|XMLStructure
name|xs
range|:
name|content
control|)
block|{
if|if
condition|(
name|xs
operator|instanceof
name|Manifest
condition|)
block|{
name|Manifest
name|man
init|=
operator|(
name|Manifest
operator|)
name|xs
decl_stmt|;
for|for
control|(
name|Reference
name|ref
range|:
operator|(
name|List
argument_list|<
name|Reference
argument_list|>
operator|)
name|man
operator|.
name|getReferences
argument_list|()
control|)
block|{
name|boolean
name|refValid
init|=
name|ref
operator|.
name|validate
argument_list|(
name|valContext
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|refValid
condition|)
block|{
name|handler
operator|.
name|manifestReferenceValidationFailed
argument_list|(
name|ref
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
name|boolean
name|goon
init|=
name|handler
operator|.
name|ignoreCoreValidationFailure
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Ignore Core Validation failure: {}"
argument_list|,
name|goon
argument_list|)
expr_stmt|;
return|return
name|goon
return|;
block|}
finally|finally
block|{
name|handler
operator|.
name|end
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|parseInput (InputStream is)
specifier|protected
name|Document
name|parseInput
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|XmlSignatureFormatException
throws|,
name|ParserConfigurationException
throws|,
name|IOException
block|{
try|try
block|{
name|Document
name|doc
init|=
name|XmlSignatureHelper
operator|.
name|newDocumentBuilder
argument_list|(
name|getConfiguration
argument_list|()
operator|.
name|getDisallowDoctypeDecl
argument_list|()
argument_list|)
operator|.
name|parse
argument_list|(
name|is
argument_list|)
decl_stmt|;
return|return
name|doc
return|;
block|}
catch|catch
parameter_list|(
name|SAXException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|XmlSignatureFormatException
argument_list|(
literal|"Message has wrong format, it is not a XML signature document. Check the sent message."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

