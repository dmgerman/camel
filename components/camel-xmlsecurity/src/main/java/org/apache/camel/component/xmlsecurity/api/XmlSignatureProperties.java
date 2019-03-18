begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xmlsecurity.api
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
name|api
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
name|keyinfo
operator|.
name|KeyInfo
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
name|apache
operator|.
name|camel
operator|.
name|Message
import|;
end_import

begin_comment
comment|/**  * You can provide further XML objects and references which will be added by the  * XML signature generator to the XML signature.  */
end_comment

begin_interface
DECL|interface|XmlSignatureProperties
specifier|public
interface|interface
name|XmlSignatureProperties
block|{
comment|/**      * Returns further configuration objects for the XML signature      *       * @param input      *            input      * @return output must not be<code>null</code>      * @throws Exception      *             if an error occurs during creating the output      */
DECL|method|get (Input input)
name|Output
name|get
parameter_list|(
name|Input
name|input
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|//NOPMD
DECL|interface|Input
specifier|public
interface|interface
name|Input
block|{
comment|/** Input message for reading header data */
DECL|method|getMessage ()
name|Message
name|getMessage
parameter_list|()
function_decl|;
comment|/**          * The message body as DOM node. If the message body is plain text then          * the node will be a text node. If the message body is a XML document,          * then the node is the root element.          */
DECL|method|getMessageBodyNode ()
name|Node
name|getMessageBodyNode
parameter_list|()
function_decl|;
comment|/**          * Returns the parent node of the signature element in the case of          * enveloped or detached XML signature, or the empty result document in          * the case of enveloping XML signature.          *           * @return parent node, cannot be<code>null</code>          */
DECL|method|getParent ()
name|Node
name|getParent
parameter_list|()
function_decl|;
comment|/** Key info. */
DECL|method|getKeyInfo ()
name|KeyInfo
name|getKeyInfo
parameter_list|()
function_decl|;
comment|/**          * XML signature factory which can be used to create Reference and          * XMLObject instances.          *           * @return factory          */
DECL|method|getSignatureFactory ()
name|XMLSignatureFactory
name|getSignatureFactory
parameter_list|()
function_decl|;
comment|/**          * Signature algorithm. Example:          * "http://www.w3.org/2000/09/xmldsig#dsa-sha1".          */
DECL|method|getSignatureAlgorithm ()
name|String
name|getSignatureAlgorithm
parameter_list|()
function_decl|;
comment|/**          * Digest algorithm which is used for the digest calculation of the          * message body.          */
DECL|method|getContentDigestAlgorithm ()
name|String
name|getContentDigestAlgorithm
parameter_list|()
function_decl|;
comment|/**          * Signature Id. Can be<code>null</code>, then no signature Id          * attribute is generated.          */
DECL|method|getSignatureId ()
name|String
name|getSignatureId
parameter_list|()
function_decl|;
comment|/**          * Reference URI which points to the content of the original XML          * document to be signed. For enveloped signature it is typically the          * empty string. For detached signature, it is an ID attribute value          * preceded by '#'. For enveloping signature, it is<code>null</code>.          */
DECL|method|getContentReferenceUri ()
name|String
name|getContentReferenceUri
parameter_list|()
function_decl|;
comment|/**          * Retruns the signature type.          *           * @return signature type          */
DECL|method|getSignatureType ()
name|SignatureType
name|getSignatureType
parameter_list|()
function_decl|;
comment|/**          * Returns the prefix for the XML Signature namespace          * ("http://www.w3.org/2000/09/xmldsig#"). Can be null or empty.          */
DECL|method|getPrefixForXmlSignatureNamespace ()
name|String
name|getPrefixForXmlSignatureNamespace
parameter_list|()
function_decl|;
block|}
DECL|class|Output
class|class
name|Output
block|{
DECL|field|contentReferenceId
specifier|private
name|String
name|contentReferenceId
decl_stmt|;
DECL|field|signatureId
specifier|private
name|String
name|signatureId
decl_stmt|;
DECL|field|objects
specifier|private
name|List
argument_list|<
name|?
extends|extends
name|XMLObject
argument_list|>
name|objects
decl_stmt|;
DECL|field|references
specifier|private
name|List
argument_list|<
name|?
extends|extends
name|Reference
argument_list|>
name|references
decl_stmt|;
DECL|method|getObjects ()
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
name|objects
return|;
block|}
DECL|method|setObjects (List<? extends XMLObject> objects)
specifier|public
name|void
name|setObjects
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|XMLObject
argument_list|>
name|objects
parameter_list|)
block|{
name|this
operator|.
name|objects
operator|=
name|objects
expr_stmt|;
block|}
DECL|method|getReferences ()
specifier|public
name|List
argument_list|<
name|?
extends|extends
name|Reference
argument_list|>
name|getReferences
parameter_list|()
block|{
return|return
name|references
return|;
block|}
DECL|method|setReferences (List<? extends Reference> references)
specifier|public
name|void
name|setReferences
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|Reference
argument_list|>
name|references
parameter_list|)
block|{
name|this
operator|.
name|references
operator|=
name|references
expr_stmt|;
block|}
DECL|method|getContentReferenceId ()
specifier|public
name|String
name|getContentReferenceId
parameter_list|()
block|{
return|return
name|contentReferenceId
return|;
block|}
comment|/**          * Id value for the reference of the signed content. Currently used by          * the XAdES parameter DataObjectFormat. See XAdESSignatureProperties.          * */
DECL|method|setContentReferenceId (String contentReferenceId)
specifier|public
name|void
name|setContentReferenceId
parameter_list|(
name|String
name|contentReferenceId
parameter_list|)
block|{
name|this
operator|.
name|contentReferenceId
operator|=
name|contentReferenceId
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
comment|/**          * You can overwrite the value of the Id attribute of the Signature          * element that you get from {@link Input#getSignatureId()}. Only if the          * provided value is not<code>null</code> and not empty, then the          * signature Id will be overwritten.          *           * @param signatureId          *            Id attribute value of the Signature element          */
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
block|}
block|}
end_interface

end_unit

