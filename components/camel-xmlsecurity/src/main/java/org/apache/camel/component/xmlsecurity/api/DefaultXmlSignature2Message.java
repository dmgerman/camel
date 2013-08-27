begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

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
name|dom
operator|.
name|DOMStructure
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
name|Transform
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
name|spec
operator|.
name|XPathFilterParameterSpec
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerConfigurationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerFactoryConfigurationError
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathConstants
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathExpression
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

begin_comment
comment|/**  * Maps the XML signature to a camel message. A output node is determined from  * the XML signature document via a node search and then serialized and set to  * the output message body.  *<p>  * There are three output node search types supported: "Default", "ElementName",  * and "XPath". All these search types support enveloped XML signature or  * enveloping XML signature.  *<p>  *<ul>  *<li>The "ElementName" search uses the local name and namespace specified in  * the search value to determine the output element from the XML signature  * document. With the input parameter 'RemoveSignatureElements", you can specify  * whether the signature elements should be removed from the resulting output  * document. This flag shall be used for enveloped XML signatures.  *<li>The "XPath" search uses an XPath expression to evaluate the output node.  * In this case the output node can be of type Element, TextNode, or Document.  * With the input parameter 'RemoveSignatureElements", you can specify whether  * the signature elements should be removed from the resulting output document.  * This flag shall be used for enveloped XML signatures.  *<li>The "Default" search is explained in more detail below.  *</ul>  *<p>  * Default Output Node Search:  *<ul>  * In the enveloped XML signature case, the XML document without the signature  * part is returned in the message body.  *<p>  * In the enveloping XML signature case, the message body is determined from a  * referenced Object element in the following way:  *<ul>  *<li>Only same document references are taken into account (URI must start with  * '#').  *<li>Also indirect same document references to an object via manifest are  * taken into account.  *<li>The resulting number of object references must be 1.  *<li>The referenced object must contain exactly 1 {@link DOMStructure}.  *<li>The node of the DOMStructure is serialized to a byte array and added as  * body to the message.  *</ul>  * This does mean that the enveloping XML signature must have either the  * structure  *   *<pre>  *     {@code  *<Signature>  *<SignedInfo>  *<Reference URI="#object"/>         *<!-- further references possible but they must not point to an Object or Manifest containing an object reference -->  *            ...  *</SignedInfo>  *       *<Object Id="object">  *<!-- contains the DOM node which should be extracted to the message body -->  *<Object>  *<!-- further object elements possible which are not referenced-->  *         ...  *         (<KeyInfo>)?  *</Signature>  *     }  *</pre>  *   * or the structure  *   *<pre>  *     {@code  *<Signature>  *<SignedInfo>  *<Reference URI="#manifest"/>         *<!-- further references  are possible but they must not point to an Object or other manifest containing an object reference -->  *            ...  *</SignedInfo>  *       *<Object>  *<Manifest Id="manifest">  *<Reference URI=#object/>  *</Manifest>  *</Objet>  *<Object Id="object">  *<!-- contains the DOM node which should be extracted to the message body -->  *</Object>  *<!-- further object elements possible which are not referenced -->  *         ...  *         (<KeyInfo>)?  *</Signature>  *     }  *</pre>  *</ul>  */
end_comment

begin_class
DECL|class|DefaultXmlSignature2Message
specifier|public
class|class
name|DefaultXmlSignature2Message
implements|implements
name|XmlSignature2Message
block|{
comment|/**      * Search type 'Default' for determining the output node.      *       */
DECL|field|OUTPUT_NODE_SEARCH_TYPE_DEFAULT
specifier|public
specifier|static
specifier|final
name|String
name|OUTPUT_NODE_SEARCH_TYPE_DEFAULT
init|=
literal|"Default"
decl_stmt|;
comment|/**      * Search type 'ElementName' for determining the output element.      *       */
DECL|field|OUTPUT_NODE_SEARCH_TYPE_ELEMENT_NAME
specifier|public
specifier|static
specifier|final
name|String
name|OUTPUT_NODE_SEARCH_TYPE_ELEMENT_NAME
init|=
literal|"ElementName"
decl_stmt|;
comment|/**      * Search type 'XPath' for determining the output node. Search value must be      * of type {@link XPathFilterParameterSpec}.      *       */
DECL|field|OUTPUT_NODE_SEARCH_TYPE_XPATH
specifier|public
specifier|static
specifier|final
name|String
name|OUTPUT_NODE_SEARCH_TYPE_XPATH
init|=
literal|"XPath"
decl_stmt|;
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
name|DefaultXmlSignature2Message
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|mapToMessage (Input input, Message output)
specifier|public
name|void
name|mapToMessage
parameter_list|(
name|Input
name|input
parameter_list|,
name|Message
name|output
parameter_list|)
throws|throws
name|Exception
block|{
comment|//NOPMD
name|Node
name|node
decl_stmt|;
name|boolean
name|removeSignatureElements
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|OUTPUT_NODE_SEARCH_TYPE_DEFAULT
operator|.
name|equals
argument_list|(
name|input
operator|.
name|getOutputNodeSearchType
argument_list|()
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Searching for output node via default search"
argument_list|)
expr_stmt|;
if|if
condition|(
name|isEnveloped
argument_list|(
name|input
argument_list|)
condition|)
block|{
comment|// enveloped XML signature --> remove signature element
name|node
operator|=
name|input
operator|.
name|getMessageBodyDocument
argument_list|()
operator|.
name|getDocumentElement
argument_list|()
expr_stmt|;
name|removeSignatureElements
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|node
operator|=
name|getNodeForMessageBodyInNonEnvelopedCase
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|OUTPUT_NODE_SEARCH_TYPE_ELEMENT_NAME
operator|.
name|equals
argument_list|(
name|input
operator|.
name|getOutputNodeSearchType
argument_list|()
argument_list|)
condition|)
block|{
name|node
operator|=
name|getOutputElementViaLocalNameAndNamespace
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|OUTPUT_NODE_SEARCH_TYPE_XPATH
operator|.
name|equals
argument_list|(
name|input
operator|.
name|getOutputNodeSearchType
argument_list|()
argument_list|)
condition|)
block|{
name|node
operator|=
name|getOutputNodeViaXPath
argument_list|(
name|input
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|XmlSignatureException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Wrong configuration: The output node search type %s is not supported."
argument_list|,
name|input
operator|.
name|getOutputNodeSearchType
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Output node with local name {} and namespace {} found"
argument_list|,
name|node
operator|.
name|getLocalName
argument_list|()
argument_list|,
name|node
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|removeSignatureElements
condition|)
block|{
name|removeSignatureElements
operator|=
name|input
operator|.
name|getRemoveSignatureElements
argument_list|()
operator|!=
literal|null
operator|&&
name|input
operator|.
name|getRemoveSignatureElements
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|removeSignatureElements
condition|)
block|{
name|removeSignatureElements
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
name|transformNodeToByteArrayAndSetToOutputMessage
argument_list|(
name|input
argument_list|,
name|output
argument_list|,
name|node
argument_list|)
expr_stmt|;
block|}
DECL|method|transformNodeToByteArrayAndSetToOutputMessage (Input input, Message output, Node node)
specifier|protected
name|void
name|transformNodeToByteArrayAndSetToOutputMessage
parameter_list|(
name|Input
name|input
parameter_list|,
name|Message
name|output
parameter_list|,
name|Node
name|node
parameter_list|)
throws|throws
name|TransformerFactoryConfigurationError
throws|,
name|TransformerConfigurationException
throws|,
name|TransformerException
throws|,
name|IOException
block|{
name|ByteArrayOutputStream
name|os
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|XmlSignatureHelper
operator|.
name|transformToOutputStream
argument_list|(
name|node
argument_list|,
name|os
argument_list|,
name|omitXmlDeclaration
argument_list|(
name|output
argument_list|,
name|input
argument_list|)
argument_list|)
expr_stmt|;
name|output
operator|.
name|setBody
argument_list|(
name|os
operator|.
name|toByteArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|getOutputNodeViaXPath (Input input)
specifier|protected
name|Node
name|getOutputNodeViaXPath
parameter_list|(
name|Input
name|input
parameter_list|)
throws|throws
name|Exception
block|{
comment|//NOPMD
name|checkSearchValueNotNull
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|checkSearchValueOfType
argument_list|(
name|XPathFilterParameterSpec
operator|.
name|class
argument_list|,
name|input
argument_list|)
expr_stmt|;
name|XPathFilterParameterSpec
name|xpathFilter
init|=
operator|(
name|XPathFilterParameterSpec
operator|)
name|input
operator|.
name|getOutputNodeSearch
argument_list|()
decl_stmt|;
name|XPathExpression
name|expr
init|=
name|XmlSignatureHelper
operator|.
name|getXPathExpression
argument_list|(
name|xpathFilter
argument_list|)
decl_stmt|;
name|NodeList
name|nodes
init|=
operator|(
name|NodeList
operator|)
name|expr
operator|.
name|evaluate
argument_list|(
name|input
operator|.
name|getMessageBodyDocument
argument_list|()
argument_list|,
name|XPathConstants
operator|.
name|NODESET
argument_list|)
decl_stmt|;
if|if
condition|(
name|nodes
operator|==
literal|null
operator|||
name|nodes
operator|.
name|getLength
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|XmlSignatureException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Cannot extract root node for the output document from the XML signature document. No node found for XPATH %s as specified in the output node search."
argument_list|,
name|xpathFilter
operator|.
name|getXPath
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
if|if
condition|(
name|nodes
operator|.
name|getLength
argument_list|()
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|XmlSignatureException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Cannot extract root node for the output document from the XML signature document. XPATH %s as specified in the output node search results into more than one child."
argument_list|,
name|xpathFilter
operator|.
name|getXPath
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
name|Node
name|result
init|=
name|nodes
operator|.
name|item
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|Node
operator|.
name|ELEMENT_NODE
operator|==
name|result
operator|.
name|getNodeType
argument_list|()
operator|||
name|Node
operator|.
name|TEXT_NODE
operator|==
name|result
operator|.
name|getNodeType
argument_list|()
operator|||
name|Node
operator|.
name|DOCUMENT_NODE
operator|==
name|result
operator|.
name|getNodeType
argument_list|()
condition|)
block|{
return|return
name|result
return|;
block|}
throw|throw
operator|new
name|XmlSignatureException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Cannot extract root node for the output document from the XML signature document. XPATH %s as specified in the output node search results into a node which has the wrong type."
argument_list|,
name|xpathFilter
operator|.
name|getXPath
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
DECL|method|getOutputElementViaLocalNameAndNamespace (Input input)
specifier|protected
name|Node
name|getOutputElementViaLocalNameAndNamespace
parameter_list|(
name|Input
name|input
parameter_list|)
throws|throws
name|Exception
block|{
comment|//NOPMD
name|String
name|search
init|=
name|getNonEmptyStringSearchValue
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|String
name|namespace
decl_stmt|;
name|String
name|localName
decl_stmt|;
if|if
condition|(
literal|'{'
operator|==
name|search
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
condition|)
block|{
comment|// namespace
name|int
name|index
init|=
name|search
operator|.
name|indexOf
argument_list|(
literal|'}'
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|<
literal|1
condition|)
block|{
throw|throw
operator|new
name|XmlSignatureException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Wrong configuration: Value %s for the output node search %s has wrong format. Value must have the form '{<namespace>}<element local name>' or '<element local name>' if no the element has no namespace."
argument_list|,
name|search
argument_list|,
name|input
operator|.
name|getOutputNodeSearchType
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
name|namespace
operator|=
name|search
operator|.
name|substring
argument_list|(
literal|1
argument_list|,
name|index
argument_list|)
expr_stmt|;
if|if
condition|(
name|search
operator|.
name|length
argument_list|()
operator|<
name|index
operator|+
literal|1
condition|)
block|{
throw|throw
operator|new
name|XmlSignatureException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Wrong configuration: Value %s for the output node search %s has wrong format. Value must have the form '{<namespace>}<element local name>' or '<element local name>' if no the element has no namespace."
argument_list|,
name|search
argument_list|,
name|input
operator|.
name|getOutputNodeSearchType
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
name|localName
operator|=
name|search
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|namespace
operator|=
literal|null
expr_stmt|;
name|localName
operator|=
name|search
expr_stmt|;
block|}
name|NodeList
name|nodeList
init|=
name|input
operator|.
name|getMessageBodyDocument
argument_list|()
operator|.
name|getElementsByTagNameNS
argument_list|(
name|namespace
argument_list|,
name|localName
argument_list|)
decl_stmt|;
if|if
condition|(
name|nodeList
operator|.
name|getLength
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|XmlSignatureException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Cannot extract root element for the output document from the XML signature document. Element with local name %s and namespace %s does not exist."
argument_list|,
name|namespace
argument_list|,
name|localName
argument_list|)
argument_list|)
throw|;
block|}
if|if
condition|(
name|nodeList
operator|.
name|getLength
argument_list|()
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|XmlSignatureException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Cannot extract root element for the output document from the XML signature document. More than one element found with local name %s and namespace %s."
argument_list|,
name|namespace
argument_list|,
name|localName
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|nodeList
operator|.
name|item
argument_list|(
literal|0
argument_list|)
return|;
block|}
DECL|method|getNonEmptyStringSearchValue (Input input)
specifier|protected
name|String
name|getNonEmptyStringSearchValue
parameter_list|(
name|Input
name|input
parameter_list|)
throws|throws
name|Exception
block|{
comment|//NOPMD
name|checkSearchValueNotNull
argument_list|(
name|input
argument_list|)
expr_stmt|;
name|checkSearchValueOfType
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|input
argument_list|)
expr_stmt|;
name|String
name|search
init|=
operator|(
name|String
operator|)
name|input
operator|.
name|getOutputNodeSearch
argument_list|()
decl_stmt|;
name|checkStringSarchValueNotEmpty
argument_list|(
name|search
argument_list|,
name|input
operator|.
name|getOutputNodeSearchType
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|search
return|;
block|}
DECL|method|checkSearchValueOfType (Class<?> cl, Input input)
specifier|protected
name|void
name|checkSearchValueOfType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cl
parameter_list|,
name|Input
name|input
parameter_list|)
throws|throws
name|Exception
block|{
comment|//NOPMD
if|if
condition|(
operator|!
name|cl
operator|.
name|isAssignableFrom
argument_list|(
name|input
operator|.
name|getOutputNodeSearch
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|XMLSignatureException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Wrong configruation: Search value is of class %s, the output node search %s requires class %s."
argument_list|,
name|input
operator|.
name|getOutputNodeSearch
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|input
operator|.
name|getOutputNodeSearchType
argument_list|()
argument_list|,
name|cl
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
block|}
DECL|method|checkStringSarchValueNotEmpty (String searchValue, String outputNodeSearchType)
specifier|protected
name|void
name|checkStringSarchValueNotEmpty
parameter_list|(
name|String
name|searchValue
parameter_list|,
name|String
name|outputNodeSearchType
parameter_list|)
throws|throws
name|Exception
block|{
comment|//NOPMD
if|if
condition|(
name|searchValue
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|XMLSignatureException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Wrong configruation: Value for output node search %s is empty."
argument_list|,
name|outputNodeSearchType
argument_list|)
argument_list|)
throw|;
block|}
block|}
DECL|method|checkSearchValueNotNull (Input input)
specifier|protected
name|void
name|checkSearchValueNotNull
parameter_list|(
name|Input
name|input
parameter_list|)
throws|throws
name|Exception
block|{
comment|//NOPMD
name|LOG
operator|.
name|debug
argument_list|(
literal|"Searching for output element with search value '{}' and sarch type {}"
argument_list|,
name|input
operator|.
name|getOutputNodeSearch
argument_list|()
argument_list|,
name|input
operator|.
name|getOutputNodeSearchType
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|input
operator|.
name|getOutputNodeSearch
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|XMLSignatureException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Wrong configruation: Value is missing for output node search %s."
argument_list|,
name|input
operator|.
name|getOutputNodeSearchType
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
block|}
DECL|method|getNodeForMessageBodyInNonEnvelopedCase (Input input)
specifier|protected
name|Node
name|getNodeForMessageBodyInNonEnvelopedCase
parameter_list|(
name|Input
name|input
parameter_list|)
throws|throws
name|Exception
block|{
comment|//NOPMD
name|Node
name|node
decl_stmt|;
name|List
argument_list|<
name|Reference
argument_list|>
name|relevantReferences
init|=
name|getReferencesForMessageMapping
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|XMLObject
argument_list|>
name|relevantObjects
init|=
name|getObjectsForMessageMapping
argument_list|(
name|input
argument_list|)
decl_stmt|;
name|DOMStructure
name|domStruc
init|=
name|getDomStructureForMessageBody
argument_list|(
name|relevantReferences
argument_list|,
name|relevantObjects
argument_list|)
decl_stmt|;
name|node
operator|=
name|domStruc
operator|.
name|getNode
argument_list|()
expr_stmt|;
return|return
name|node
return|;
block|}
comment|/**      * Removes the Signature elements from the document.      *       * @param doc      *            document      */
DECL|method|removeSignatureElements (Node node)
specifier|protected
name|void
name|removeSignatureElements
parameter_list|(
name|Node
name|node
parameter_list|)
block|{
name|Document
name|doc
init|=
name|XmlSignatureHelper
operator|.
name|getDocument
argument_list|(
name|node
argument_list|)
decl_stmt|;
name|NodeList
name|nl
init|=
name|doc
operator|.
name|getElementsByTagNameNS
argument_list|(
literal|"http://www.w3.org/2000/09/xmldsig#"
argument_list|,
literal|"Signature"
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|nl
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|n
init|=
name|nl
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|Node
name|parent
init|=
name|n
operator|.
name|getParentNode
argument_list|()
decl_stmt|;
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
name|parent
operator|.
name|removeChild
argument_list|(
name|n
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Returns the enveloped data in case of an enveloped XML signature.      *       * @param input      *            references of signed info and objects      * @return<code>true</code> if there exists a reference with URI = "" and      *         with {@link Transform#ENVELOPED} transform; otherwise      *<code>false</code>      * @throws Exception      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|isEnveloped (Input input)
specifier|protected
name|boolean
name|isEnveloped
parameter_list|(
name|Input
name|input
parameter_list|)
throws|throws
name|Exception
block|{
comment|//NOPMD
for|for
control|(
name|Reference
name|ref
range|:
name|input
operator|.
name|getReferences
argument_list|()
control|)
block|{
if|if
condition|(
literal|""
operator|.
name|equals
argument_list|(
name|ref
operator|.
name|getURI
argument_list|()
argument_list|)
condition|)
block|{
for|for
control|(
name|Transform
name|t
range|:
operator|(
operator|(
name|List
argument_list|<
name|Transform
argument_list|>
operator|)
name|ref
operator|.
name|getTransforms
argument_list|()
operator|)
control|)
block|{
if|if
condition|(
name|Transform
operator|.
name|ENVELOPED
operator|.
name|equals
argument_list|(
name|t
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
DECL|method|omitXmlDeclaration (Message message, Input input)
specifier|protected
name|Boolean
name|omitXmlDeclaration
parameter_list|(
name|Message
name|message
parameter_list|,
name|Input
name|input
parameter_list|)
block|{
name|Boolean
name|omitXmlDeclaration
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|XmlSignatureConstants
operator|.
name|HEADER_OMIT_XML_DECLARATION
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|omitXmlDeclaration
operator|==
literal|null
condition|)
block|{
name|omitXmlDeclaration
operator|=
name|input
operator|.
name|omitXmlDeclaration
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|omitXmlDeclaration
operator|==
literal|null
condition|)
block|{
name|omitXmlDeclaration
operator|=
name|Boolean
operator|.
name|FALSE
expr_stmt|;
block|}
return|return
name|omitXmlDeclaration
return|;
block|}
comment|/**      * Returns the references whose referenced objects are taken into account      * for the message body. This message you can use to filter the relevant      * references from the references provided by the input parameter.      *       *       * @param input      *            references and objects      * @return relevant references for the mapping to the camel message      * @throws Exception      *             if an error occurs      */
DECL|method|getReferencesForMessageMapping (Input input)
specifier|protected
name|List
argument_list|<
name|Reference
argument_list|>
name|getReferencesForMessageMapping
parameter_list|(
name|Input
name|input
parameter_list|)
throws|throws
name|Exception
block|{
comment|//NOPMD
return|return
name|input
operator|.
name|getReferences
argument_list|()
return|;
block|}
comment|/**      * Returns the objects which must be taken into account for the mapping to      * the camel message.      *       * @param input      *            references and objects      * @return relevant objects for the mapping to camel message      * @throws Exception      *             if an error occurs      */
DECL|method|getObjectsForMessageMapping (Input input)
specifier|protected
name|List
argument_list|<
name|XMLObject
argument_list|>
name|getObjectsForMessageMapping
parameter_list|(
name|Input
name|input
parameter_list|)
throws|throws
name|Exception
block|{
comment|//NOPMD
return|return
name|input
operator|.
name|getObjects
argument_list|()
return|;
block|}
comment|/**      * Returns the DOM structure which is transformed to a byte array and set to      * the camel message body.      *       * @param relevantReferences      *            input from method      *            {@link #getReferencesForMessageMapping(ReferencesAndObjects)}      * @param relevantObjects      *            input from method      *            {@link #getObjectsForMessageMapping(ReferencesAndObjects)}      * @return dom structure      * @throws Exception      *             if an error occurs      */
DECL|method|getDomStructureForMessageBody (List<Reference> relevantReferences, List<XMLObject> relevantObjects)
specifier|protected
name|DOMStructure
name|getDomStructureForMessageBody
parameter_list|(
name|List
argument_list|<
name|Reference
argument_list|>
name|relevantReferences
parameter_list|,
name|List
argument_list|<
name|XMLObject
argument_list|>
name|relevantObjects
parameter_list|)
throws|throws
name|Exception
block|{
comment|//NOPMD
name|List
argument_list|<
name|XMLObject
argument_list|>
name|referencedObjects
init|=
name|getReferencedSameDocumentObjects
argument_list|(
name|relevantReferences
argument_list|,
name|relevantObjects
argument_list|)
decl_stmt|;
if|if
condition|(
name|referencedObjects
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|XmlSignatureException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Unsupported XML signature document: Content object not found in the XML signature. Detached or enveloped signatures are not supported."
argument_list|)
argument_list|)
throw|;
block|}
if|if
condition|(
name|referencedObjects
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|referencedObjects
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|XMLObject
name|xmlOb
init|=
name|referencedObjects
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|xmlOb
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|<
name|referencedObjects
operator|.
name|size
argument_list|()
operator|-
literal|1
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
expr_stmt|;
block|}
block|}
throw|throw
operator|new
name|XmlSignatureException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Unsupported XML signature document: More than one content objects found. Object IDs: %s"
argument_list|,
name|sb
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|List
argument_list|<
name|XMLStructure
argument_list|>
name|structures
init|=
name|referencedObjects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getContent
argument_list|()
decl_stmt|;
if|if
condition|(
name|structures
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|XmlSignatureException
argument_list|(
literal|"Unsupported XML signature: XML signature is not enveloping; content not found in XML signature: structure list is empty."
argument_list|)
throw|;
block|}
if|if
condition|(
name|structures
operator|.
name|size
argument_list|()
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|XmlSignatureException
argument_list|(
literal|"Unsupported XML signature: more than one structure elements in referenced content object."
argument_list|)
throw|;
block|}
name|XMLStructure
name|structure
init|=
name|structures
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// only dom currently supported
name|DOMStructure
name|domStruc
init|=
operator|(
name|DOMStructure
operator|)
name|structure
decl_stmt|;
return|return
name|domStruc
return|;
block|}
DECL|method|getReferencedSameDocumentObjects (List<Reference> relevantReferences, List<XMLObject> relevantObjects)
specifier|protected
name|List
argument_list|<
name|XMLObject
argument_list|>
name|getReferencedSameDocumentObjects
parameter_list|(
name|List
argument_list|<
name|Reference
argument_list|>
name|relevantReferences
parameter_list|,
name|List
argument_list|<
name|XMLObject
argument_list|>
name|relevantObjects
parameter_list|)
block|{
name|List
argument_list|<
name|XMLObject
argument_list|>
name|referencedObjects
init|=
operator|new
name|ArrayList
argument_list|<
name|XMLObject
argument_list|>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
for|for
control|(
name|Reference
name|ref
range|:
name|relevantReferences
control|)
block|{
name|String
name|refUri
init|=
name|getSameDocumentReferenceUri
argument_list|(
name|ref
argument_list|)
decl_stmt|;
if|if
condition|(
name|refUri
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|XMLObject
name|referencedOb
init|=
name|getReferencedObject
argument_list|(
name|relevantObjects
argument_list|,
name|refUri
argument_list|)
decl_stmt|;
if|if
condition|(
name|referencedOb
operator|!=
literal|null
condition|)
block|{
name|referencedObjects
operator|.
name|add
argument_list|(
name|referencedOb
argument_list|)
expr_stmt|;
continue|continue;
block|}
comment|// content could also be indirectly referenced via manifest
name|addManifestReferencedObjects
argument_list|(
name|relevantObjects
argument_list|,
name|referencedObjects
argument_list|,
name|refUri
argument_list|)
expr_stmt|;
block|}
return|return
name|referencedObjects
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|addManifestReferencedObjects (List<XMLObject> allObjects, List<XMLObject> referencedObjects, String manifestId)
specifier|protected
name|void
name|addManifestReferencedObjects
parameter_list|(
name|List
argument_list|<
name|XMLObject
argument_list|>
name|allObjects
parameter_list|,
name|List
argument_list|<
name|XMLObject
argument_list|>
name|referencedObjects
parameter_list|,
name|String
name|manifestId
parameter_list|)
block|{
name|Manifest
name|manifest
init|=
name|getReferencedManifest
argument_list|(
name|allObjects
argument_list|,
name|manifestId
argument_list|)
decl_stmt|;
if|if
condition|(
name|manifest
operator|==
literal|null
condition|)
block|{
return|return;
block|}
for|for
control|(
name|Reference
name|manifestRef
range|:
operator|(
name|List
argument_list|<
name|Reference
argument_list|>
operator|)
name|manifest
operator|.
name|getReferences
argument_list|()
control|)
block|{
name|String
name|manifestRefUri
init|=
name|getSameDocumentReferenceUri
argument_list|(
name|manifestRef
argument_list|)
decl_stmt|;
if|if
condition|(
name|manifestRefUri
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
name|XMLObject
name|manifestReferencedOb
init|=
name|getReferencedObject
argument_list|(
name|allObjects
argument_list|,
name|manifestRefUri
argument_list|)
decl_stmt|;
if|if
condition|(
name|manifestReferencedOb
operator|!=
literal|null
condition|)
block|{
name|referencedObjects
operator|.
name|add
argument_list|(
name|manifestReferencedOb
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|getSameDocumentReferenceUri (Reference ref)
specifier|protected
name|String
name|getSameDocumentReferenceUri
parameter_list|(
name|Reference
name|ref
parameter_list|)
block|{
name|String
name|refUri
init|=
name|ref
operator|.
name|getURI
argument_list|()
decl_stmt|;
if|if
condition|(
name|refUri
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Ignoring reference {} which has no URI"
argument_list|,
name|ref
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
if|if
condition|(
operator|!
name|refUri
operator|.
name|startsWith
argument_list|(
literal|"#"
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Ignoring non-same document reference {}"
argument_list|,
name|refUri
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
return|return
name|refUri
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
return|;
block|}
DECL|method|getReferencedManifest (List<XMLObject> objects, String id)
specifier|protected
name|Manifest
name|getReferencedManifest
parameter_list|(
name|List
argument_list|<
name|XMLObject
argument_list|>
name|objects
parameter_list|,
name|String
name|id
parameter_list|)
block|{
for|for
control|(
name|XMLObject
name|xo
range|:
name|objects
control|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
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
if|if
condition|(
name|id
operator|.
name|equals
argument_list|(
name|man
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|man
return|;
block|}
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|getReferencedObject (List<XMLObject> objects, String id)
specifier|protected
name|XMLObject
name|getReferencedObject
parameter_list|(
name|List
argument_list|<
name|XMLObject
argument_list|>
name|objects
parameter_list|,
name|String
name|id
parameter_list|)
block|{
for|for
control|(
name|XMLObject
name|ob
range|:
name|objects
control|)
block|{
if|if
condition|(
name|id
operator|.
name|equals
argument_list|(
name|ob
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|ob
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

