begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.maven
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|maven
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|AbstractMojo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|MojoExecutionException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|MojoFailureException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugins
operator|.
name|annotations
operator|.
name|LifecyclePhase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugins
operator|.
name|annotations
operator|.
name|Mojo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugins
operator|.
name|annotations
operator|.
name|Parameter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugins
operator|.
name|annotations
operator|.
name|ResolutionScope
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
name|*
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
name|XPathFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
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

begin_class
annotation|@
name|Mojo
argument_list|(
name|name
operator|=
literal|"eip-documentation-enricher"
argument_list|,
name|requiresDependencyResolution
operator|=
name|ResolutionScope
operator|.
name|COMPILE_PLUS_RUNTIME
argument_list|,
name|requiresProject
operator|=
literal|true
argument_list|,
name|defaultPhase
operator|=
name|LifecyclePhase
operator|.
name|PACKAGE
argument_list|)
DECL|class|EipDocumentationGeneratorMojo
specifier|public
class|class
name|EipDocumentationGeneratorMojo
extends|extends
name|AbstractMojo
block|{
DECL|field|logger
specifier|private
specifier|final
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|EipDocumentationGeneratorMojo
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**    * Project's source directory as specified in the POM.    */
annotation|@
name|Parameter
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|inputCamelSchemaFile
name|File
name|inputCamelSchemaFile
decl_stmt|;
annotation|@
name|Parameter
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|outputCamelSchemaFile
name|File
name|outputCamelSchemaFile
decl_stmt|;
comment|//  @Parameter(defaultValue = "${project.build.directory}/../../../..//camel-core")
annotation|@
name|Parameter
argument_list|(
name|defaultValue
operator|=
literal|"${project.build.directory}/../../..//camel-core"
argument_list|)
DECL|field|camelCoreDir
name|File
name|camelCoreDir
decl_stmt|;
annotation|@
name|Override
DECL|method|execute ()
specifier|public
name|void
name|execute
parameter_list|()
throws|throws
name|MojoExecutionException
throws|,
name|MojoFailureException
block|{
name|File
name|rootDir
init|=
operator|new
name|File
argument_list|(
name|camelCoreDir
argument_list|,
name|Constants
operator|.
name|PATH_TO_MODEL_DIR
argument_list|)
decl_stmt|;
name|DomParser
name|domParser
init|=
operator|new
name|DomParser
argument_list|()
decl_stmt|;
name|DocumentationEnricher
name|documentationEnricher
init|=
operator|new
name|DocumentationEnricher
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|File
argument_list|>
name|jsonFiles
init|=
name|PackageHelper
operator|.
name|findJsonFiles
argument_list|(
name|rootDir
argument_list|)
decl_stmt|;
name|XPath
name|xPath
init|=
name|buildXPath
argument_list|(
operator|new
name|CamelSpringNamespace
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|Document
name|document
init|=
name|buildNamespaceAwareDocument
argument_list|(
name|inputCamelSchemaFile
argument_list|)
decl_stmt|;
name|NodeList
name|elementsAndTypes
init|=
name|domParser
operator|.
name|findElementsAndTypes
argument_list|(
name|document
argument_list|,
name|xPath
argument_list|)
decl_stmt|;
name|documentationEnricher
operator|.
name|enrichTopLevelElementsDocumentation
argument_list|(
name|document
argument_list|,
name|elementsAndTypes
argument_list|,
name|jsonFiles
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|typeToNameMap
init|=
name|buildTypeToNameMap
argument_list|(
name|elementsAndTypes
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|typeToNameMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|NodeList
name|attributeElements
init|=
name|domParser
operator|.
name|findAttributesElements
argument_list|(
name|document
argument_list|,
name|xPath
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|jsonFiles
operator|.
name|containsKey
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
name|documentationEnricher
operator|.
name|enrichTypeAttributesDocumentation
argument_list|(
name|document
argument_list|,
name|attributeElements
argument_list|,
name|jsonFiles
operator|.
name|get
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|saveToFile
argument_list|(
name|document
argument_list|,
name|outputCamelSchemaFile
argument_list|,
name|buildTransformer
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|getLog
argument_list|()
operator|.
name|error
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|buildTypeToNameMap (NodeList elementsAndTypes)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|buildTypeToNameMap
parameter_list|(
name|NodeList
name|elementsAndTypes
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|typeToNameMap
init|=
operator|new
name|HashMap
argument_list|<>
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
name|elementsAndTypes
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Element
name|item
init|=
operator|(
name|Element
operator|)
name|elementsAndTypes
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|String
name|name
init|=
name|item
operator|.
name|getAttribute
argument_list|(
name|Constants
operator|.
name|NAME_ATTRIBUTE_NAME
argument_list|)
decl_stmt|;
name|String
name|type
init|=
name|item
operator|.
name|getAttribute
argument_list|(
name|Constants
operator|.
name|TYPE_ATTRIBUTE_NAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
operator|&&
name|type
operator|!=
literal|null
condition|)
block|{
name|type
operator|=
name|type
operator|.
name|replaceAll
argument_list|(
literal|"tns:"
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|logger
operator|.
name|debug
argument_list|(
literal|"Putting attributes type:'{}', name:'{}'"
argument_list|,
name|name
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|typeToNameMap
operator|.
name|put
argument_list|(
name|type
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|typeToNameMap
return|;
block|}
DECL|method|buildXPath (NamespaceContext namespaceContext)
specifier|private
name|XPath
name|buildXPath
parameter_list|(
name|NamespaceContext
name|namespaceContext
parameter_list|)
block|{
name|XPath
name|xPath
init|=
name|XPathFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newXPath
argument_list|()
decl_stmt|;
name|xPath
operator|.
name|setNamespaceContext
argument_list|(
name|namespaceContext
argument_list|)
expr_stmt|;
return|return
name|xPath
return|;
block|}
DECL|method|buildTransformer ()
specifier|private
name|Transformer
name|buildTransformer
parameter_list|()
throws|throws
name|TransformerConfigurationException
block|{
name|Transformer
name|transformer
init|=
name|TransformerFactory
operator|.
name|newInstance
argument_list|()
operator|.
name|newTransformer
argument_list|()
decl_stmt|;
name|transformer
operator|.
name|setOutputProperty
argument_list|(
name|OutputKeys
operator|.
name|INDENT
argument_list|,
literal|"yes"
argument_list|)
expr_stmt|;
name|transformer
operator|.
name|setOutputProperty
argument_list|(
literal|"{http://xml.apache.org/xslt}indent-amount"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
return|return
name|transformer
return|;
block|}
DECL|method|buildNamespaceAwareDocument (File xml)
specifier|public
name|Document
name|buildNamespaceAwareDocument
parameter_list|(
name|File
name|xml
parameter_list|)
throws|throws
name|ParserConfigurationException
throws|,
name|IOException
throws|,
name|SAXException
block|{
name|DocumentBuilderFactory
name|factory
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setNamespaceAware
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|DocumentBuilder
name|builder
init|=
name|factory
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
return|return
name|builder
operator|.
name|parse
argument_list|(
name|xml
argument_list|)
return|;
block|}
DECL|method|saveToFile (Document document, File outputFile, Transformer transformer)
specifier|private
name|void
name|saveToFile
parameter_list|(
name|Document
name|document
parameter_list|,
name|File
name|outputFile
parameter_list|,
name|Transformer
name|transformer
parameter_list|)
throws|throws
name|IOException
throws|,
name|TransformerException
block|{
name|StreamResult
name|result
init|=
operator|new
name|StreamResult
argument_list|(
operator|new
name|FileOutputStream
argument_list|(
name|outputFile
argument_list|)
argument_list|)
decl_stmt|;
name|DOMSource
name|source
init|=
operator|new
name|DOMSource
argument_list|(
name|document
argument_list|)
decl_stmt|;
name|transformer
operator|.
name|transform
argument_list|(
name|source
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

