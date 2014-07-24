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
name|io
operator|.
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
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
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|XMLConstants
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
name|spec
operator|.
name|ExcC14NParameterSpec
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
name|XPathFilter2ParameterSpec
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
name|crypto
operator|.
name|dsig
operator|.
name|spec
operator|.
name|XPathType
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
name|XSLTTransformParameterSpec
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|NamespaceContext
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
name|DocumentBuilder
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
name|DocumentBuilderFactory
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|OutputKeys
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
name|Transformer
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
name|TransformerFactory
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
name|transform
operator|.
name|dom
operator|.
name|DOMSource
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
name|stream
operator|.
name|StreamResult
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|validation
operator|.
name|Schema
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
name|XPath
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
name|javax
operator|.
name|xml
operator|.
name|xpath
operator|.
name|XPathExpressionException
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
name|XPathFactory
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
name|util
operator|.
name|IOHelper
import|;
end_import

begin_comment
comment|/**  * Helps to construct the transformations and the canonicalization methods for  * the XML Signature generator.  */
end_comment

begin_class
DECL|class|XmlSignatureHelper
specifier|public
specifier|final
class|class
name|XmlSignatureHelper
block|{
DECL|method|XmlSignatureHelper ()
specifier|private
name|XmlSignatureHelper
parameter_list|()
block|{
comment|//Helper class
block|}
comment|/**      * Returns a configuration for a canonicalization algorithm.      *       * @param algorithm      *            algorithm URI      * @return canonicalization      * @throws IllegalArgumentException      *             if<tt>algorithm</tt> is<code>null</code>      */
DECL|method|getCanonicalizationMethod (String algorithm)
specifier|public
specifier|static
name|AlgorithmMethod
name|getCanonicalizationMethod
parameter_list|(
name|String
name|algorithm
parameter_list|)
block|{
return|return
name|getCanonicalizationMethod
argument_list|(
name|algorithm
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Returns a configuration for a canonicalization algorithm.      *       * @param algorithm      *            algorithm URI      * @param inclusiveNamespacePrefixes      *            namespace prefixes which should be treated like in the      *            inclusive canonicalization, only relevant if the algorithm is      *            exclusive      * @return canonicalization      * @throws IllegalArgumentException      *             if<tt>algorithm</tt> is<code>null</code>      */
DECL|method|getCanonicalizationMethod (String algorithm, List<String> inclusiveNamespacePrefixes)
specifier|public
specifier|static
name|AlgorithmMethod
name|getCanonicalizationMethod
parameter_list|(
name|String
name|algorithm
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|inclusiveNamespacePrefixes
parameter_list|)
block|{
if|if
condition|(
name|algorithm
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"algorithm is null"
argument_list|)
throw|;
block|}
name|XmlSignatureTransform
name|canonicalizationMethod
init|=
operator|new
name|XmlSignatureTransform
argument_list|(
name|algorithm
argument_list|)
decl_stmt|;
if|if
condition|(
name|inclusiveNamespacePrefixes
operator|!=
literal|null
condition|)
block|{
name|ExcC14NParameterSpec
name|parameters
init|=
operator|new
name|ExcC14NParameterSpec
argument_list|(
name|inclusiveNamespacePrefixes
argument_list|)
decl_stmt|;
name|canonicalizationMethod
operator|.
name|setParameterSpec
argument_list|(
name|parameters
argument_list|)
expr_stmt|;
block|}
return|return
name|canonicalizationMethod
return|;
block|}
DECL|method|getEnvelopedTransform ()
specifier|public
specifier|static
name|AlgorithmMethod
name|getEnvelopedTransform
parameter_list|()
block|{
return|return
operator|new
name|XmlSignatureTransform
argument_list|(
name|Transform
operator|.
name|ENVELOPED
argument_list|)
return|;
block|}
comment|/**      * Returns a configuration for a base64 transformation.      *       * @return Base64 transformation      */
DECL|method|getBase64Transform ()
specifier|public
specifier|static
name|AlgorithmMethod
name|getBase64Transform
parameter_list|()
block|{
return|return
operator|new
name|XmlSignatureTransform
argument_list|(
name|Transform
operator|.
name|BASE64
argument_list|)
return|;
block|}
comment|/**      * Returns a configuration for an XPATH transformation.      *       * @param xpath      *            XPATH expression      * @return XPATH transformation      * @throws IllegalArgumentException      *             if<tt>xpath</tt> is<code>null</code>      */
DECL|method|getXPathTransform (String xpath)
specifier|public
specifier|static
name|AlgorithmMethod
name|getXPathTransform
parameter_list|(
name|String
name|xpath
parameter_list|)
block|{
return|return
name|getXPathTransform
argument_list|(
name|xpath
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Returns a configuration for an XPATH transformation which needs a      * namespace map.      *       * @param xpath      *            XPATH expression      * @param namespaceMap      *            namespace map, key is the prefix, value is the namespace, can      *            be<code>null</code>      * @throws IllegalArgumentException      *             if<tt>xpath</tt> is<code>null</code>      * @return XPATH transformation      */
DECL|method|getXPathTransform (String xpath, Map<String, String> namespaceMap)
specifier|public
specifier|static
name|AlgorithmMethod
name|getXPathTransform
parameter_list|(
name|String
name|xpath
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaceMap
parameter_list|)
block|{
if|if
condition|(
name|xpath
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"xpath is null"
argument_list|)
throw|;
block|}
name|XmlSignatureTransform
name|transformXPath
init|=
operator|new
name|XmlSignatureTransform
argument_list|()
decl_stmt|;
name|transformXPath
operator|.
name|setAlgorithm
argument_list|(
name|Transform
operator|.
name|XPATH
argument_list|)
expr_stmt|;
name|XPathFilterParameterSpec
name|params
init|=
name|getXpathFilter
argument_list|(
name|xpath
argument_list|,
name|namespaceMap
argument_list|)
decl_stmt|;
name|transformXPath
operator|.
name|setParameterSpec
argument_list|(
name|params
argument_list|)
expr_stmt|;
return|return
name|transformXPath
return|;
block|}
DECL|method|getXpathFilter (String xpath, Map<String, String> namespaceMap)
specifier|public
specifier|static
name|XPathFilterParameterSpec
name|getXpathFilter
parameter_list|(
name|String
name|xpath
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaceMap
parameter_list|)
block|{
name|XPathFilterParameterSpec
name|params
init|=
name|namespaceMap
operator|==
literal|null
condition|?
operator|new
name|XPathFilterParameterSpec
argument_list|(
name|xpath
argument_list|)
else|:
operator|new
name|XPathFilterParameterSpec
argument_list|(
name|xpath
argument_list|,
name|namespaceMap
argument_list|)
decl_stmt|;
return|return
name|params
return|;
block|}
DECL|method|getXpathFilter (String xpath)
specifier|public
specifier|static
name|XPathFilterParameterSpec
name|getXpathFilter
parameter_list|(
name|String
name|xpath
parameter_list|)
block|{
return|return
name|getXpathFilter
argument_list|(
name|xpath
argument_list|,
literal|null
argument_list|)
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getXPathExpression (XPathFilterParameterSpec xpathFilter)
specifier|public
specifier|static
name|XPathExpression
name|getXPathExpression
parameter_list|(
name|XPathFilterParameterSpec
name|xpathFilter
parameter_list|)
throws|throws
name|XPathExpressionException
block|{
name|XPathFactory
name|factory
init|=
name|XPathFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|XPath
name|xpath
init|=
name|factory
operator|.
name|newXPath
argument_list|()
decl_stmt|;
if|if
condition|(
name|xpathFilter
operator|.
name|getNamespaceMap
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|xpath
operator|.
name|setNamespaceContext
argument_list|(
operator|new
name|XPathNamespaceContext
argument_list|(
name|xpathFilter
operator|.
name|getNamespaceMap
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|xpath
operator|.
name|compile
argument_list|(
name|xpathFilter
operator|.
name|getXPath
argument_list|()
argument_list|)
return|;
block|}
DECL|class|XPathNamespaceContext
specifier|private
specifier|static
class|class
name|XPathNamespaceContext
implements|implements
name|NamespaceContext
block|{
DECL|field|prefix2Namespace
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|prefix2Namespace
decl_stmt|;
DECL|method|XPathNamespaceContext (Map<String, String> prefix2Namespace)
name|XPathNamespaceContext
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|prefix2Namespace
parameter_list|)
block|{
name|this
operator|.
name|prefix2Namespace
operator|=
name|prefix2Namespace
expr_stmt|;
block|}
DECL|method|getNamespaceURI (String prefix)
specifier|public
name|String
name|getNamespaceURI
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
if|if
condition|(
name|prefix
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null prefix"
argument_list|)
throw|;
block|}
if|if
condition|(
literal|"xml"
operator|.
name|equals
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
return|return
name|XMLConstants
operator|.
name|XML_NS_URI
return|;
block|}
name|String
name|ns
init|=
name|prefix2Namespace
operator|.
name|get
argument_list|(
name|prefix
argument_list|)
decl_stmt|;
if|if
condition|(
name|ns
operator|!=
literal|null
condition|)
block|{
return|return
name|ns
return|;
block|}
return|return
name|XMLConstants
operator|.
name|NULL_NS_URI
return|;
block|}
comment|// This method isn't necessary for XPath processing.
DECL|method|getPrefix (String uri)
specifier|public
name|String
name|getPrefix
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
comment|// This method isn't necessary for XPath processing either.
annotation|@
name|SuppressWarnings
argument_list|(
literal|"rawtypes"
argument_list|)
DECL|method|getPrefixes (String uri)
specifier|public
name|Iterator
name|getPrefixes
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
comment|/**      * Returns a configuration for an XPATH2 transformation.      *       * @param xpath      *            XPATH expression      * @param filter      *            possible values are "intersect", "subtract", "union"      * @throws IllegalArgumentException      *             if<tt>xpath</tt> or<tt>filter</tt> is<code>null</code>, or      *             is neither "intersect", nor "subtract", nor "union"      * @return XPATH transformation      */
DECL|method|getXPath2Transform (String xpath, String filter)
specifier|public
specifier|static
name|AlgorithmMethod
name|getXPath2Transform
parameter_list|(
name|String
name|xpath
parameter_list|,
name|String
name|filter
parameter_list|)
block|{
return|return
name|getXPath2Transform
argument_list|(
name|xpath
argument_list|,
name|filter
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Returns a configuration for an XPATH2 transformation which consists of      * several XPATH expressions.      *       * @param xpathAndFilterList      *            list of XPATH expressions with their filters      * @param namespaceMap      *            namespace map, key is the prefix, value is the namespace, can      *            be<code>null</code>      * @throws IllegalArgumentException      *             if<tt>xpathAndFilterList</tt> is<code>null</code> or empty,      *             or the specified filter values are neither "intersect", nor      *             "subtract", nor "union"      * @return XPATH transformation      */
DECL|method|getXPath2Transform (String xpath, String filter, Map<String, String> namespaceMap)
specifier|public
specifier|static
name|AlgorithmMethod
name|getXPath2Transform
parameter_list|(
name|String
name|xpath
parameter_list|,
name|String
name|filter
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaceMap
parameter_list|)
block|{
name|XPathAndFilter
name|xpathAndFilter
init|=
operator|new
name|XPathAndFilter
argument_list|()
decl_stmt|;
name|xpathAndFilter
operator|.
name|setXpath
argument_list|(
name|xpath
argument_list|)
expr_stmt|;
name|xpathAndFilter
operator|.
name|setFilter
argument_list|(
name|filter
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|XPathAndFilter
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|XmlSignatureHelper
operator|.
name|XPathAndFilter
argument_list|>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|xpathAndFilter
argument_list|)
expr_stmt|;
return|return
name|getXPath2Transform
argument_list|(
name|list
argument_list|,
name|namespaceMap
argument_list|)
return|;
block|}
comment|/**      * Returns a configuration for an XPATH2 transformation which consists of      * several XPATH expressions.      *       * @param xpathAndFilterList      *            list of XPATH expressions with their filters      * @param namespaceMap      *            namespace map, key is the prefix, value is the namespace, can      *            be<code>null</code>      * @throws IllegalArgumentException      *             if<tt>xpathAndFilterList</tt> is<code>null</code> or empty,      *             or the specified filter values are neither "intersect", nor      *             "subtract", nor "union"      * @return XPATH transformation      */
DECL|method|getXPath2Transform (List<XPathAndFilter> xpathAndFilterList, Map<String, String> namespaceMap)
specifier|public
specifier|static
name|AlgorithmMethod
name|getXPath2Transform
parameter_list|(
name|List
argument_list|<
name|XPathAndFilter
argument_list|>
name|xpathAndFilterList
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaceMap
parameter_list|)
block|{
if|if
condition|(
name|xpathAndFilterList
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"xpathAndFilterList is null"
argument_list|)
throw|;
block|}
if|if
condition|(
name|xpathAndFilterList
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"XPath and filter list is empty"
argument_list|)
throw|;
block|}
name|List
argument_list|<
name|XPathType
argument_list|>
name|list
init|=
name|getXPathTypeList
argument_list|(
name|xpathAndFilterList
argument_list|,
name|namespaceMap
argument_list|)
decl_stmt|;
name|XmlSignatureTransform
name|transformXPath
init|=
operator|new
name|XmlSignatureTransform
argument_list|(
name|Transform
operator|.
name|XPATH2
argument_list|)
decl_stmt|;
name|transformXPath
operator|.
name|setParameterSpec
argument_list|(
operator|new
name|XPathFilter2ParameterSpec
argument_list|(
name|list
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|transformXPath
return|;
block|}
DECL|method|getXPathTypeList (List<XPathAndFilter> xpathAndFilterList, Map<String, String> namespaceMap)
specifier|private
specifier|static
name|List
argument_list|<
name|XPathType
argument_list|>
name|getXPathTypeList
parameter_list|(
name|List
argument_list|<
name|XPathAndFilter
argument_list|>
name|xpathAndFilterList
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaceMap
parameter_list|)
block|{
name|List
argument_list|<
name|XPathType
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|XPathType
argument_list|>
argument_list|(
name|xpathAndFilterList
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|XPathAndFilter
name|xpathAndFilter
range|:
name|xpathAndFilterList
control|)
block|{
name|XPathType
operator|.
name|Filter
name|xpathFilter
decl_stmt|;
if|if
condition|(
name|XPathType
operator|.
name|Filter
operator|.
name|INTERSECT
operator|.
name|toString
argument_list|()
operator|.
name|equals
argument_list|(
name|xpathAndFilter
operator|.
name|getFilter
argument_list|()
argument_list|)
condition|)
block|{
name|xpathFilter
operator|=
name|XPathType
operator|.
name|Filter
operator|.
name|INTERSECT
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|XPathType
operator|.
name|Filter
operator|.
name|SUBTRACT
operator|.
name|toString
argument_list|()
operator|.
name|equals
argument_list|(
name|xpathAndFilter
operator|.
name|getFilter
argument_list|()
argument_list|)
condition|)
block|{
name|xpathFilter
operator|=
name|XPathType
operator|.
name|Filter
operator|.
name|SUBTRACT
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|XPathType
operator|.
name|Filter
operator|.
name|UNION
operator|.
name|toString
argument_list|()
operator|.
name|equals
argument_list|(
name|xpathAndFilter
operator|.
name|getFilter
argument_list|()
argument_list|)
condition|)
block|{
name|xpathFilter
operator|=
name|XPathType
operator|.
name|Filter
operator|.
name|UNION
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"XPATH %s has a filter %s not supported"
argument_list|,
name|xpathAndFilter
operator|.
name|getXpath
argument_list|()
argument_list|,
name|xpathAndFilter
operator|.
name|getFilter
argument_list|()
argument_list|)
argument_list|)
throw|;
block|}
name|XPathType
name|xpathtype
init|=
name|namespaceMap
operator|==
literal|null
condition|?
operator|new
name|XPathType
argument_list|(
name|xpathAndFilter
operator|.
name|getXpath
argument_list|()
argument_list|,
name|xpathFilter
argument_list|)
else|:
operator|new
name|XPathType
argument_list|(
name|xpathAndFilter
operator|.
name|getXpath
argument_list|()
argument_list|,
name|xpathFilter
argument_list|,
name|namespaceMap
argument_list|)
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|xpathtype
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
comment|/**      * Returns a configuration for an XPATH2 transformation which consists of      * several XPATH expressions.      *       * @param xpathAndFilterList      *            list of XPATH expressions with their filters      * @throws IllegalArgumentException      *             if<tt>xpathAndFilterList</tt> is<code>null</code> or empty,      *             or the specified filte values are neither "intersect", nor      *             "subtract", nor "union"      * @return XPATH transformation      */
DECL|method|getXPath2Transform (List<XPathAndFilter> xpathAndFilterList)
specifier|public
specifier|static
name|AlgorithmMethod
name|getXPath2Transform
parameter_list|(
name|List
argument_list|<
name|XPathAndFilter
argument_list|>
name|xpathAndFilterList
parameter_list|)
block|{
return|return
name|getXPath2Transform
argument_list|(
name|xpathAndFilterList
argument_list|,
literal|null
argument_list|)
return|;
block|}
comment|/**      * Returns a configuration for an XSL transformation.      *       * @param path      *            path to the XSL file in the classpath      * @return XSL transform      * @throws IllegalArgumentException      *             if<tt>path</tt> is<code>null</code>      * @throws IllegalStateException      *             if the XSL file cannot be found      * @throws Exception      *             if an error during the reading of the XSL file occurs      */
DECL|method|getXslTransform (String path)
specifier|public
specifier|static
name|AlgorithmMethod
name|getXslTransform
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|Exception
block|{
name|InputStream
name|is
init|=
name|readXslTransform
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"XSL file %s not found"
argument_list|,
name|path
argument_list|)
argument_list|)
throw|;
block|}
try|try
block|{
return|return
name|getXslTranform
argument_list|(
name|is
argument_list|)
return|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns a configuration for an XSL transformation.      *       * @param is      *            input stream of the XSL      * @return XSL transform      * @throws IllegalArgumentException      *             if<tt>is</tt> is<code>null</code>      * @throws Exception      *             if an error during the reading of the XSL file occurs      */
DECL|method|getXslTranform (InputStream is)
specifier|public
specifier|static
name|AlgorithmMethod
name|getXslTranform
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|SAXException
throws|,
name|IOException
throws|,
name|ParserConfigurationException
block|{
if|if
condition|(
name|is
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"is must not be null"
argument_list|)
throw|;
block|}
name|Document
name|doc
init|=
name|parseInput
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|DOMStructure
name|stylesheet
init|=
operator|new
name|DOMStructure
argument_list|(
name|doc
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
decl_stmt|;
name|XSLTTransformParameterSpec
name|spec
init|=
operator|new
name|XSLTTransformParameterSpec
argument_list|(
name|stylesheet
argument_list|)
decl_stmt|;
name|XmlSignatureTransform
name|transformXslt
init|=
operator|new
name|XmlSignatureTransform
argument_list|()
decl_stmt|;
name|transformXslt
operator|.
name|setAlgorithm
argument_list|(
name|Transform
operator|.
name|XSLT
argument_list|)
expr_stmt|;
name|transformXslt
operator|.
name|setParameterSpec
argument_list|(
name|spec
argument_list|)
expr_stmt|;
return|return
name|transformXslt
return|;
block|}
DECL|method|readXslTransform (String path)
specifier|protected
specifier|static
name|InputStream
name|readXslTransform
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"path is null"
argument_list|)
throw|;
block|}
return|return
name|XmlSignatureHelper
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
name|path
argument_list|)
return|;
block|}
DECL|method|getTransforms (List<AlgorithmMethod> list)
specifier|public
specifier|static
name|List
argument_list|<
name|AlgorithmMethod
argument_list|>
name|getTransforms
parameter_list|(
name|List
argument_list|<
name|AlgorithmMethod
argument_list|>
name|list
parameter_list|)
block|{
return|return
name|list
return|;
block|}
DECL|method|parseInput (InputStream is)
specifier|private
specifier|static
name|Document
name|parseInput
parameter_list|(
name|InputStream
name|is
parameter_list|)
throws|throws
name|SAXException
throws|,
name|IOException
throws|,
name|ParserConfigurationException
block|{
return|return
name|newDocumentBuilder
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
operator|.
name|parse
argument_list|(
name|is
argument_list|)
return|;
block|}
DECL|method|getTextAndElementChildren (Node node)
specifier|public
specifier|static
name|List
argument_list|<
name|Node
argument_list|>
name|getTextAndElementChildren
parameter_list|(
name|Node
name|node
parameter_list|)
block|{
name|List
argument_list|<
name|Node
argument_list|>
name|result
init|=
operator|new
name|LinkedList
argument_list|<
name|Node
argument_list|>
argument_list|()
decl_stmt|;
name|NodeList
name|children
init|=
name|node
operator|.
name|getChildNodes
argument_list|()
decl_stmt|;
if|if
condition|(
name|children
operator|==
literal|null
condition|)
block|{
return|return
name|result
return|;
block|}
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|children
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|child
init|=
name|children
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|Node
operator|.
name|ELEMENT_NODE
operator|==
name|child
operator|.
name|getNodeType
argument_list|()
operator|||
name|Node
operator|.
name|TEXT_NODE
operator|==
name|child
operator|.
name|getNodeType
argument_list|()
condition|)
block|{
name|result
operator|.
name|add
argument_list|(
name|child
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
DECL|method|newDocumentBuilder (Boolean disallowDoctypeDecl)
specifier|public
specifier|static
name|DocumentBuilder
name|newDocumentBuilder
parameter_list|(
name|Boolean
name|disallowDoctypeDecl
parameter_list|)
throws|throws
name|ParserConfigurationException
block|{
return|return
name|newDocumentBuilder
argument_list|(
name|disallowDoctypeDecl
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|newDocumentBuilder (Boolean disallowDoctypeDecl, Schema schema)
specifier|public
specifier|static
name|DocumentBuilder
name|newDocumentBuilder
parameter_list|(
name|Boolean
name|disallowDoctypeDecl
parameter_list|,
name|Schema
name|schema
parameter_list|)
throws|throws
name|ParserConfigurationException
block|{
name|DocumentBuilderFactory
name|dbf
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|dbf
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|dbf
operator|.
name|setValidating
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// avoid external entity attacks
name|dbf
operator|.
name|setFeature
argument_list|(
literal|"http://xml.org/sax/features/external-general-entities"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|dbf
operator|.
name|setFeature
argument_list|(
literal|"http://xml.org/sax/features/external-parameter-entities"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|boolean
name|isDissalowDoctypeDecl
init|=
name|disallowDoctypeDecl
operator|==
literal|null
condition|?
literal|true
else|:
name|disallowDoctypeDecl
decl_stmt|;
name|dbf
operator|.
name|setFeature
argument_list|(
literal|"http://apache.org/xml/features/disallow-doctype-decl"
argument_list|,
name|isDissalowDoctypeDecl
argument_list|)
expr_stmt|;
comment|// avoid overflow attacks
name|dbf
operator|.
name|setFeature
argument_list|(
name|XMLConstants
operator|.
name|FEATURE_SECURE_PROCESSING
argument_list|,
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|schema
operator|!=
literal|null
condition|)
block|{
name|dbf
operator|.
name|setSchema
argument_list|(
name|schema
argument_list|)
expr_stmt|;
block|}
return|return
name|dbf
operator|.
name|newDocumentBuilder
argument_list|()
return|;
block|}
DECL|method|transformToOutputStream (Node node, OutputStream os, boolean omitXmlDeclaration)
specifier|public
specifier|static
name|void
name|transformToOutputStream
parameter_list|(
name|Node
name|node
parameter_list|,
name|OutputStream
name|os
parameter_list|,
name|boolean
name|omitXmlDeclaration
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
if|if
condition|(
name|node
operator|.
name|getNodeType
argument_list|()
operator|==
name|Node
operator|.
name|TEXT_NODE
condition|)
block|{
name|byte
index|[]
name|bytes
init|=
name|tranformTextNodeToByteArray
argument_list|(
name|node
argument_list|)
decl_stmt|;
name|os
operator|.
name|write
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|transformNonTextNodeToOutputStream
argument_list|(
name|node
argument_list|,
name|os
argument_list|,
name|omitXmlDeclaration
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|transformNonTextNodeToOutputStream (Node node, OutputStream os, boolean omitXmlDeclaration)
specifier|public
specifier|static
name|void
name|transformNonTextNodeToOutputStream
parameter_list|(
name|Node
name|node
parameter_list|,
name|OutputStream
name|os
parameter_list|,
name|boolean
name|omitXmlDeclaration
parameter_list|)
throws|throws
name|TransformerFactoryConfigurationError
throws|,
name|TransformerConfigurationException
throws|,
name|TransformerException
block|{
name|TransformerFactory
name|tf
init|=
name|TransformerFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|Transformer
name|trans
init|=
name|tf
operator|.
name|newTransformer
argument_list|()
decl_stmt|;
if|if
condition|(
name|omitXmlDeclaration
condition|)
block|{
name|trans
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|OMIT_XML_DECLARATION
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
block|}
name|trans
operator|.
name|transform
argument_list|(
operator|new
name|DOMSource
argument_list|(
name|node
argument_list|)
argument_list|,
operator|new
name|StreamResult
argument_list|(
name|os
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|tranformTextNodeToByteArray (Node node)
specifier|public
specifier|static
name|byte
index|[]
name|tranformTextNodeToByteArray
parameter_list|(
name|Node
name|node
parameter_list|)
block|{
name|String
name|text
init|=
name|node
operator|.
name|getTextContent
argument_list|()
decl_stmt|;
if|if
condition|(
name|text
operator|!=
literal|null
condition|)
block|{
try|try
block|{
return|return
name|text
operator|.
name|getBytes
argument_list|(
literal|"UTF-8"
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
comment|// should not happen
throw|throw
operator|new
name|IllegalStateException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|method|getDocument (Node node)
specifier|public
specifier|static
name|Document
name|getDocument
parameter_list|(
name|Node
name|node
parameter_list|)
block|{
if|if
condition|(
name|node
operator|.
name|getNodeType
argument_list|()
operator|==
name|Node
operator|.
name|DOCUMENT_NODE
condition|)
block|{
return|return
operator|(
name|Document
operator|)
name|node
return|;
block|}
return|return
name|node
operator|.
name|getOwnerDocument
argument_list|()
return|;
block|}
DECL|class|XPathAndFilter
specifier|public
specifier|static
class|class
name|XPathAndFilter
block|{
DECL|field|xpath
specifier|private
name|String
name|xpath
decl_stmt|;
DECL|field|filter
specifier|private
name|String
name|filter
decl_stmt|;
DECL|method|XPathAndFilter (String xpath, String filter)
specifier|public
name|XPathAndFilter
parameter_list|(
name|String
name|xpath
parameter_list|,
name|String
name|filter
parameter_list|)
block|{
name|this
operator|.
name|xpath
operator|=
name|xpath
expr_stmt|;
name|this
operator|.
name|filter
operator|=
name|filter
expr_stmt|;
block|}
DECL|method|XPathAndFilter ()
specifier|public
name|XPathAndFilter
parameter_list|()
block|{          }
DECL|method|getXpath ()
specifier|public
name|String
name|getXpath
parameter_list|()
block|{
return|return
name|xpath
return|;
block|}
DECL|method|setXpath (String xpath)
specifier|public
name|void
name|setXpath
parameter_list|(
name|String
name|xpath
parameter_list|)
block|{
name|this
operator|.
name|xpath
operator|=
name|xpath
expr_stmt|;
block|}
DECL|method|getFilter ()
specifier|public
name|String
name|getFilter
parameter_list|()
block|{
return|return
name|filter
return|;
block|}
DECL|method|setFilter (String filter)
specifier|public
name|void
name|setFilter
parameter_list|(
name|String
name|filter
parameter_list|)
block|{
name|this
operator|.
name|filter
operator|=
name|filter
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

