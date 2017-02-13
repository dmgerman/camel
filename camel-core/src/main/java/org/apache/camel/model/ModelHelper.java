begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
package|;
end_package

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
name|StringWriter
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
name|LinkedHashMap
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
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|Binder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|Marshaller
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
name|TransformerException
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
name|Expression
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
name|NamedNode
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
name|TypeConversionException
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
name|converter
operator|.
name|jaxp
operator|.
name|XmlConverter
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
name|ExpressionDefinition
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
name|spi
operator|.
name|TypeConverterRegistry
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|ProcessorDefinitionHelper
operator|.
name|filterTypeInOutputs
import|;
end_import

begin_comment
comment|/**  * Helper for the Camel {@link org.apache.camel.model model} classes.  */
end_comment

begin_class
DECL|class|ModelHelper
specifier|public
specifier|final
class|class
name|ModelHelper
block|{
DECL|method|ModelHelper ()
specifier|private
name|ModelHelper
parameter_list|()
block|{
comment|// utility class
block|}
comment|/**      * Dumps the definition as XML      *      * @param context    the CamelContext, if<tt>null</tt> then {@link org.apache.camel.spi.ModelJAXBContextFactory} is not in use      * @param definition the definition, such as a {@link org.apache.camel.NamedNode}      * @return the output in XML (is formatted)      * @throws JAXBException is throw if error marshalling to XML      */
DECL|method|dumpModelAsXml (CamelContext context, NamedNode definition)
specifier|public
specifier|static
name|String
name|dumpModelAsXml
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|NamedNode
name|definition
parameter_list|)
throws|throws
name|JAXBException
block|{
name|JAXBContext
name|jaxbContext
init|=
name|getJAXBContext
argument_list|(
name|context
argument_list|)
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// gather all namespaces from the routes or route which is stored on the expression nodes
if|if
condition|(
name|definition
operator|instanceof
name|RoutesDefinition
condition|)
block|{
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|routes
init|=
operator|(
operator|(
name|RoutesDefinition
operator|)
name|definition
operator|)
operator|.
name|getRoutes
argument_list|()
decl_stmt|;
for|for
control|(
name|RouteDefinition
name|route
range|:
name|routes
control|)
block|{
name|extractNamespaces
argument_list|(
name|route
argument_list|,
name|namespaces
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|definition
operator|instanceof
name|RouteDefinition
condition|)
block|{
name|RouteDefinition
name|route
init|=
operator|(
name|RouteDefinition
operator|)
name|definition
decl_stmt|;
name|extractNamespaces
argument_list|(
name|route
argument_list|,
name|namespaces
argument_list|)
expr_stmt|;
block|}
name|Marshaller
name|marshaller
init|=
name|jaxbContext
operator|.
name|createMarshaller
argument_list|()
decl_stmt|;
name|marshaller
operator|.
name|setProperty
argument_list|(
name|Marshaller
operator|.
name|JAXB_FORMATTED_OUTPUT
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|StringWriter
name|buffer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|marshaller
operator|.
name|marshal
argument_list|(
name|definition
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
name|XmlConverter
name|xmlConverter
init|=
name|newXmlConverter
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|String
name|xml
init|=
name|buffer
operator|.
name|toString
argument_list|()
decl_stmt|;
name|Document
name|dom
decl_stmt|;
try|try
block|{
name|dom
operator|=
name|xmlConverter
operator|.
name|toDOMDocument
argument_list|(
name|xml
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|TypeConversionException
argument_list|(
name|xml
argument_list|,
name|Document
operator|.
name|class
argument_list|,
name|e
argument_list|)
throw|;
block|}
comment|// Add additional namespaces to the document root element
name|Element
name|documentElement
init|=
name|dom
operator|.
name|getDocumentElement
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|nsPrefix
range|:
name|namespaces
operator|.
name|keySet
argument_list|()
control|)
block|{
name|String
name|prefix
init|=
name|nsPrefix
operator|.
name|equals
argument_list|(
literal|"xmlns"
argument_list|)
condition|?
name|nsPrefix
else|:
literal|"xmlns:"
operator|+
name|nsPrefix
decl_stmt|;
name|documentElement
operator|.
name|setAttribute
argument_list|(
name|prefix
argument_list|,
name|namespaces
operator|.
name|get
argument_list|(
name|nsPrefix
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// We invoke the type converter directly because we need to pass some custom XML output options
name|Properties
name|outputProperties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|outputProperties
operator|.
name|put
argument_list|(
name|OutputKeys
operator|.
name|INDENT
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
name|outputProperties
operator|.
name|put
argument_list|(
name|OutputKeys
operator|.
name|STANDALONE
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
try|try
block|{
return|return
name|xmlConverter
operator|.
name|toStringFromDocument
argument_list|(
name|dom
argument_list|,
name|outputProperties
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|TransformerException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Failed converting document object to string"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Marshal the xml to the model definition      *      * @param context the CamelContext, if<tt>null</tt> then {@link org.apache.camel.spi.ModelJAXBContextFactory} is not in use      * @param xml     the xml      * @param type    the definition type to return, will throw a {@link ClassCastException} if not the expected type      * @return the model definition      * @throws javax.xml.bind.JAXBException is thrown if error unmarshalling from xml to model      */
DECL|method|createModelFromXml (CamelContext context, String xml, Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|NamedNode
parameter_list|>
name|T
name|createModelFromXml
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|String
name|xml
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
throws|throws
name|JAXBException
block|{
return|return
name|modelToXml
argument_list|(
name|context
argument_list|,
literal|null
argument_list|,
name|xml
argument_list|,
name|type
argument_list|)
return|;
block|}
comment|/**      * Marshal the xml to the model definition      *      * @param context the CamelContext, if<tt>null</tt> then {@link org.apache.camel.spi.ModelJAXBContextFactory} is not in use      * @param stream  the xml stream      * @param type    the definition type to return, will throw a {@link ClassCastException} if not the expected type      * @return the model definition      * @throws javax.xml.bind.JAXBException is thrown if error unmarshalling from xml to model      */
DECL|method|createModelFromXml (CamelContext context, InputStream stream, Class<T> type)
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|NamedNode
parameter_list|>
name|T
name|createModelFromXml
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|InputStream
name|stream
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
throws|throws
name|JAXBException
block|{
return|return
name|modelToXml
argument_list|(
name|context
argument_list|,
name|stream
argument_list|,
literal|null
argument_list|,
name|type
argument_list|)
return|;
block|}
comment|/**      * Marshal the xml to the model definition      *      * @param context the CamelContext, if<tt>null</tt> then {@link org.apache.camel.spi.ModelJAXBContextFactory} is not in use      * @param inputStream the xml stream      * @throws Exception is thrown if an error is encountered unmarshalling from xml to model      */
DECL|method|loadRoutesDefinition (CamelContext context, InputStream inputStream)
specifier|public
specifier|static
name|RoutesDefinition
name|loadRoutesDefinition
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|InputStream
name|inputStream
parameter_list|)
throws|throws
name|Exception
block|{
name|XmlConverter
name|xmlConverter
init|=
name|newXmlConverter
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Document
name|dom
init|=
name|xmlConverter
operator|.
name|toDOMDocument
argument_list|(
name|inputStream
argument_list|,
literal|null
argument_list|)
decl_stmt|;
return|return
name|loadRoutesDefinition
argument_list|(
name|context
argument_list|,
name|dom
argument_list|)
return|;
block|}
comment|/**      * Marshal the xml to the model definition      *      * @param context the CamelContext, if<tt>null</tt> then {@link org.apache.camel.spi.ModelJAXBContextFactory} is not in use      * @param node the xml node      * @throws Exception is thrown if an error is encountered unmarshalling from xml to model      */
DECL|method|loadRoutesDefinition (CamelContext context, Node node)
specifier|public
specifier|static
name|RoutesDefinition
name|loadRoutesDefinition
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|Node
name|node
parameter_list|)
throws|throws
name|Exception
block|{
name|JAXBContext
name|jaxbContext
init|=
name|getJAXBContext
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Document
name|dom
init|=
name|node
operator|instanceof
name|Document
condition|?
operator|(
name|Document
operator|)
name|node
else|:
name|node
operator|.
name|getOwnerDocument
argument_list|()
decl_stmt|;
name|extractNamespaces
argument_list|(
name|dom
argument_list|,
name|namespaces
argument_list|)
expr_stmt|;
name|Binder
argument_list|<
name|Node
argument_list|>
name|binder
init|=
name|jaxbContext
operator|.
name|createBinder
argument_list|()
decl_stmt|;
name|Object
name|result
init|=
name|binder
operator|.
name|unmarshal
argument_list|(
name|node
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|JAXBException
argument_list|(
literal|"Cannot unmarshal to RoutesDefinition using JAXB"
argument_list|)
throw|;
block|}
comment|// can either be routes or a single route
name|RoutesDefinition
name|answer
decl_stmt|;
if|if
condition|(
name|result
operator|instanceof
name|RouteDefinition
condition|)
block|{
name|RouteDefinition
name|route
init|=
operator|(
name|RouteDefinition
operator|)
name|result
decl_stmt|;
name|answer
operator|=
operator|new
name|RoutesDefinition
argument_list|()
expr_stmt|;
name|applyNamespaces
argument_list|(
name|route
argument_list|,
name|namespaces
argument_list|)
expr_stmt|;
name|answer
operator|.
name|getRoutes
argument_list|()
operator|.
name|add
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|result
operator|instanceof
name|RoutesDefinition
condition|)
block|{
name|answer
operator|=
operator|(
name|RoutesDefinition
operator|)
name|result
expr_stmt|;
for|for
control|(
name|RouteDefinition
name|route
range|:
name|answer
operator|.
name|getRoutes
argument_list|()
control|)
block|{
name|applyNamespaces
argument_list|(
name|route
argument_list|,
name|namespaces
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unmarshalled object is an unsupported type: "
operator|+
name|ObjectHelper
operator|.
name|className
argument_list|(
name|result
argument_list|)
operator|+
literal|" -> "
operator|+
name|result
argument_list|)
throw|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|modelToXml (CamelContext context, InputStream is, String xml, Class<T> type)
specifier|private
specifier|static
parameter_list|<
name|T
extends|extends
name|NamedNode
parameter_list|>
name|T
name|modelToXml
parameter_list|(
name|CamelContext
name|context
parameter_list|,
name|InputStream
name|is
parameter_list|,
name|String
name|xml
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
throws|throws
name|JAXBException
block|{
name|JAXBContext
name|jaxbContext
init|=
name|getJAXBContext
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|XmlConverter
name|xmlConverter
init|=
name|newXmlConverter
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|Document
name|dom
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|dom
operator|=
name|xmlConverter
operator|.
name|toDOMDocument
argument_list|(
name|is
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|xml
operator|!=
literal|null
condition|)
block|{
name|dom
operator|=
name|xmlConverter
operator|.
name|toDOMDocument
argument_list|(
name|xml
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|TypeConversionException
argument_list|(
name|xml
argument_list|,
name|Document
operator|.
name|class
argument_list|,
name|e
argument_list|)
throw|;
block|}
if|if
condition|(
name|dom
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"InputStream and XML is both null"
argument_list|)
throw|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|extractNamespaces
argument_list|(
name|dom
argument_list|,
name|namespaces
argument_list|)
expr_stmt|;
name|Binder
argument_list|<
name|Node
argument_list|>
name|binder
init|=
name|jaxbContext
operator|.
name|createBinder
argument_list|()
decl_stmt|;
name|Object
name|result
init|=
name|binder
operator|.
name|unmarshal
argument_list|(
name|dom
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|JAXBException
argument_list|(
literal|"Cannot unmarshal to "
operator|+
name|type
operator|+
literal|" using JAXB"
argument_list|)
throw|;
block|}
comment|// Restore namespaces to anything that's NamespaceAware
if|if
condition|(
name|result
operator|instanceof
name|RoutesDefinition
condition|)
block|{
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|routes
init|=
operator|(
operator|(
name|RoutesDefinition
operator|)
name|result
operator|)
operator|.
name|getRoutes
argument_list|()
decl_stmt|;
for|for
control|(
name|RouteDefinition
name|route
range|:
name|routes
control|)
block|{
name|applyNamespaces
argument_list|(
name|route
argument_list|,
name|namespaces
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|result
operator|instanceof
name|RouteDefinition
condition|)
block|{
name|RouteDefinition
name|route
init|=
operator|(
name|RouteDefinition
operator|)
name|result
decl_stmt|;
name|applyNamespaces
argument_list|(
name|route
argument_list|,
name|namespaces
argument_list|)
expr_stmt|;
block|}
return|return
name|type
operator|.
name|cast
argument_list|(
name|result
argument_list|)
return|;
block|}
DECL|method|getJAXBContext (CamelContext context)
specifier|private
specifier|static
name|JAXBContext
name|getJAXBContext
parameter_list|(
name|CamelContext
name|context
parameter_list|)
throws|throws
name|JAXBException
block|{
name|JAXBContext
name|jaxbContext
decl_stmt|;
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
name|jaxbContext
operator|=
name|createJAXBContext
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|jaxbContext
operator|=
name|context
operator|.
name|getModelJAXBContextFactory
argument_list|()
operator|.
name|newJAXBContext
argument_list|()
expr_stmt|;
block|}
return|return
name|jaxbContext
return|;
block|}
DECL|method|applyNamespaces (RouteDefinition route, Map<String, String> namespaces)
specifier|private
specifier|static
name|void
name|applyNamespaces
parameter_list|(
name|RouteDefinition
name|route
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
parameter_list|)
block|{
name|Iterator
argument_list|<
name|ExpressionNode
argument_list|>
name|it
init|=
name|filterTypeInOutputs
argument_list|(
name|route
operator|.
name|getOutputs
argument_list|()
argument_list|,
name|ExpressionNode
operator|.
name|class
argument_list|)
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|NamespaceAware
name|na
init|=
name|getNamespaceAwareFromExpression
argument_list|(
name|it
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|na
operator|!=
literal|null
condition|)
block|{
name|na
operator|.
name|setNamespaces
argument_list|(
name|namespaces
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|getNamespaceAwareFromExpression (ExpressionNode expressionNode)
specifier|private
specifier|static
name|NamespaceAware
name|getNamespaceAwareFromExpression
parameter_list|(
name|ExpressionNode
name|expressionNode
parameter_list|)
block|{
name|ExpressionDefinition
name|ed
init|=
name|expressionNode
operator|.
name|getExpression
argument_list|()
decl_stmt|;
name|NamespaceAware
name|na
init|=
literal|null
decl_stmt|;
name|Expression
name|exp
init|=
name|ed
operator|.
name|getExpressionValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|exp
operator|instanceof
name|NamespaceAware
condition|)
block|{
name|na
operator|=
operator|(
name|NamespaceAware
operator|)
name|exp
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ed
operator|instanceof
name|NamespaceAware
condition|)
block|{
name|na
operator|=
operator|(
name|NamespaceAware
operator|)
name|ed
expr_stmt|;
block|}
return|return
name|na
return|;
block|}
DECL|method|createJAXBContext ()
specifier|private
specifier|static
name|JAXBContext
name|createJAXBContext
parameter_list|()
throws|throws
name|JAXBException
block|{
comment|// must use classloader from CamelContext to have JAXB working
return|return
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|Constants
operator|.
name|JAXB_CONTEXT_PACKAGES
argument_list|,
name|CamelContext
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Extract all XML namespaces from the expressions in the route      *      * @param route       the route      * @param namespaces  the map of namespaces to add discovered XML namespaces into      */
DECL|method|extractNamespaces (RouteDefinition route, Map<String, String> namespaces)
specifier|private
specifier|static
name|void
name|extractNamespaces
parameter_list|(
name|RouteDefinition
name|route
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
parameter_list|)
block|{
name|Iterator
argument_list|<
name|ExpressionNode
argument_list|>
name|it
init|=
name|filterTypeInOutputs
argument_list|(
name|route
operator|.
name|getOutputs
argument_list|()
argument_list|,
name|ExpressionNode
operator|.
name|class
argument_list|)
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|NamespaceAware
name|na
init|=
name|getNamespaceAwareFromExpression
argument_list|(
name|it
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|na
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
name|na
operator|.
name|getNamespaces
argument_list|()
decl_stmt|;
if|if
condition|(
name|map
operator|!=
literal|null
operator|&&
operator|!
name|map
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|namespaces
operator|.
name|putAll
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Extract all XML namespaces from the root element in a DOM Document      *      * @param document    the DOM document      * @param namespaces  the map of namespaces to add new found XML namespaces      */
DECL|method|extractNamespaces (Document document, Map<String, String> namespaces)
specifier|private
specifier|static
name|void
name|extractNamespaces
parameter_list|(
name|Document
name|document
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
parameter_list|)
throws|throws
name|JAXBException
block|{
name|NamedNodeMap
name|attributes
init|=
name|document
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|getAttributes
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
name|attributes
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Node
name|item
init|=
name|attributes
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|String
name|nsPrefix
init|=
name|item
operator|.
name|getNodeName
argument_list|()
decl_stmt|;
if|if
condition|(
name|nsPrefix
operator|!=
literal|null
operator|&&
name|nsPrefix
operator|.
name|startsWith
argument_list|(
literal|"xmlns"
argument_list|)
condition|)
block|{
name|String
name|nsValue
init|=
name|item
operator|.
name|getNodeValue
argument_list|()
decl_stmt|;
name|String
index|[]
name|nsParts
init|=
name|nsPrefix
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
if|if
condition|(
name|nsParts
operator|.
name|length
operator|==
literal|1
condition|)
block|{
name|namespaces
operator|.
name|put
argument_list|(
name|nsParts
index|[
literal|0
index|]
argument_list|,
name|nsValue
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|nsParts
operator|.
name|length
operator|==
literal|2
condition|)
block|{
name|namespaces
operator|.
name|put
argument_list|(
name|nsParts
index|[
literal|1
index|]
argument_list|,
name|nsValue
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// Fallback on adding the namespace prefix as we find it
name|namespaces
operator|.
name|put
argument_list|(
name|nsPrefix
argument_list|,
name|nsValue
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Creates a new {@link XmlConverter}      *      * @param context CamelContext if provided      * @return a new XmlConverter instance      */
DECL|method|newXmlConverter (CamelContext context)
specifier|private
specifier|static
name|XmlConverter
name|newXmlConverter
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|XmlConverter
name|xmlConverter
decl_stmt|;
if|if
condition|(
name|context
operator|!=
literal|null
condition|)
block|{
name|TypeConverterRegistry
name|registry
init|=
name|context
operator|.
name|getTypeConverterRegistry
argument_list|()
decl_stmt|;
name|xmlConverter
operator|=
name|registry
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|XmlConverter
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|xmlConverter
operator|=
operator|new
name|XmlConverter
argument_list|()
expr_stmt|;
block|}
return|return
name|xmlConverter
return|;
block|}
block|}
end_class

end_unit

