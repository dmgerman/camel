begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.xml
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|xml
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|language
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
name|model
operator|.
name|language
operator|.
name|XQueryExpression
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
name|NamespaceAware
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
name|ObjectHelper
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
name|Attr
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
name|Element
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
name|NamedNodeMap
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

begin_comment
comment|/**  * A helper class for working with namespaces or creating namespace based expressions  *  * @version $Revision: $  */
end_comment

begin_class
DECL|class|Namespaces
specifier|public
class|class
name|Namespaces
block|{
DECL|field|DEFAULT_NAMESPACE
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_NAMESPACE
init|=
literal|"http://activemq.apache.org/camel/schema/spring"
decl_stmt|;
DECL|field|IN_NAMESPACE
specifier|public
specifier|static
specifier|final
name|String
name|IN_NAMESPACE
init|=
literal|"http://camel.apache.org/xml/in/"
decl_stmt|;
DECL|field|OUT_NAMESPACE
specifier|public
specifier|static
specifier|final
name|String
name|OUT_NAMESPACE
init|=
literal|"http://camel.apache.org/xml/out/"
decl_stmt|;
DECL|field|SYSTEM_PROPERTIES_NAMESPACE
specifier|public
specifier|static
specifier|final
name|String
name|SYSTEM_PROPERTIES_NAMESPACE
init|=
literal|"http://camel.apache.org/xml/variables/system-properties"
decl_stmt|;
DECL|field|ENVIRONMENT_VARIABLES
specifier|public
specifier|static
specifier|final
name|String
name|ENVIRONMENT_VARIABLES
init|=
literal|"http://camel.apache.org/xml/variables/environment-variables"
decl_stmt|;
DECL|field|EXCHANGE_PROPERTY
specifier|public
specifier|static
specifier|final
name|String
name|EXCHANGE_PROPERTY
init|=
literal|"http://camel.apache.org/xml/variables/exchange-property"
decl_stmt|;
DECL|field|namespaces
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * Returns true if the given namespaceURI is empty or if it matches the      * given expected namespace      */
DECL|method|isMatchingNamespaceOrEmptyNamespace (String namespaceURI, String expectedNamespace)
specifier|public
specifier|static
name|boolean
name|isMatchingNamespaceOrEmptyNamespace
parameter_list|(
name|String
name|namespaceURI
parameter_list|,
name|String
name|expectedNamespace
parameter_list|)
block|{
return|return
name|ObjectHelper
operator|.
name|isNullOrBlank
argument_list|(
name|namespaceURI
argument_list|)
operator|||
name|namespaceURI
operator|.
name|equals
argument_list|(
name|expectedNamespace
argument_list|)
return|;
block|}
comment|/**      * Creates a namespaces object from the given XML element      *      * @param element the XML element representing the XPath namespace context      */
DECL|method|Namespaces (Element element)
specifier|public
name|Namespaces
parameter_list|(
name|Element
name|element
parameter_list|)
block|{
name|add
argument_list|(
name|element
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates a namespace context with a single prefix and URI      */
DECL|method|Namespaces (String prefix, String uri)
specifier|public
name|Namespaces
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
name|add
argument_list|(
name|prefix
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
DECL|method|add (String prefix, String uri)
specifier|public
name|Namespaces
name|add
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
name|namespaces
operator|.
name|put
argument_list|(
name|prefix
argument_list|,
name|uri
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|add (Element element)
specifier|public
name|Namespaces
name|add
parameter_list|(
name|Element
name|element
parameter_list|)
block|{
comment|// lets set the parent first in case we overload a prefix here
name|Node
name|parentNode
init|=
name|element
operator|.
name|getParentNode
argument_list|()
decl_stmt|;
if|if
condition|(
name|parentNode
operator|instanceof
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
condition|)
block|{
name|add
argument_list|(
operator|(
name|Element
operator|)
name|parentNode
argument_list|)
expr_stmt|;
block|}
name|NamedNodeMap
name|attributes
init|=
name|element
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
name|int
name|size
init|=
name|attributes
operator|.
name|getLength
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
name|size
condition|;
name|i
operator|++
control|)
block|{
name|Attr
name|node
init|=
operator|(
name|Attr
operator|)
name|attributes
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|node
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|name
operator|.
name|startsWith
argument_list|(
literal|"xmlns:"
argument_list|)
condition|)
block|{
name|String
name|prefix
init|=
name|name
operator|.
name|substring
argument_list|(
literal|"xmlns:"
operator|.
name|length
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|uri
init|=
name|node
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|add
argument_list|(
name|prefix
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|this
return|;
block|}
comment|/**      * Creates the XPath expression using the current namespace context      */
DECL|method|xpath (String expression)
specifier|public
name|XPathExpression
name|xpath
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
name|XPathExpression
name|answer
init|=
operator|new
name|XPathExpression
argument_list|(
name|expression
argument_list|)
decl_stmt|;
name|configure
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * Creates the XPath expression using the current namespace context      */
DECL|method|xpath (String expression, Class<?> resultType)
specifier|public
name|XPathExpression
name|xpath
parameter_list|(
name|String
name|expression
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|resultType
parameter_list|)
block|{
name|XPathExpression
name|answer
init|=
name|xpath
argument_list|(
name|expression
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setResultType
argument_list|(
name|resultType
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * Creates the XQuery expression using the current namespace context      */
DECL|method|xquery (String expression)
specifier|public
name|XQueryExpression
name|xquery
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
name|XQueryExpression
name|answer
init|=
operator|new
name|XQueryExpression
argument_list|(
name|expression
argument_list|)
decl_stmt|;
name|configure
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
comment|/**      * Creates the XQuery expression using the current namespace context      * and the given expected return type      */
DECL|method|xquery (String expression, Class<?> resultType)
specifier|public
name|XQueryExpression
name|xquery
parameter_list|(
name|String
name|expression
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|resultType
parameter_list|)
block|{
name|XQueryExpression
name|answer
init|=
operator|new
name|XQueryExpression
argument_list|(
name|expression
argument_list|)
decl_stmt|;
name|answer
operator|.
name|setResultType
argument_list|(
name|resultType
argument_list|)
expr_stmt|;
name|configure
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|getNamespaces ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getNamespaces
parameter_list|()
block|{
return|return
name|namespaces
return|;
block|}
comment|/**      * Configures the namespace aware object      */
DECL|method|configure (NamespaceAware namespaceAware)
specifier|public
name|void
name|configure
parameter_list|(
name|NamespaceAware
name|namespaceAware
parameter_list|)
block|{
name|namespaceAware
operator|.
name|setNamespaces
argument_list|(
name|getNamespaces
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

