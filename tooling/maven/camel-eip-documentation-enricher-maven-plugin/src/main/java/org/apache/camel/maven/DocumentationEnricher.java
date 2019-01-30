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
name|IOException
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
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|CDATASection
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
name|apache
operator|.
name|camel
operator|.
name|support
operator|.
name|JSonSchemaHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|StringUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|WordUtils
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
name|logging
operator|.
name|Log
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
name|maven
operator|.
name|XmlHelper
operator|.
name|isNullOrEmpty
import|;
end_import

begin_comment
comment|/**  * Enriches xml document with documentation from json files.  */
end_comment

begin_class
DECL|class|DocumentationEnricher
specifier|public
class|class
name|DocumentationEnricher
block|{
DECL|field|document
specifier|private
specifier|final
name|Document
name|document
decl_stmt|;
DECL|method|DocumentationEnricher (Document document)
specifier|public
name|DocumentationEnricher
parameter_list|(
name|Document
name|document
parameter_list|)
block|{
name|this
operator|.
name|document
operator|=
name|document
expr_stmt|;
block|}
DECL|method|enrichTopLevelElementsDocumentation (NodeList elements, Map<String, File> jsonFiles)
specifier|public
name|void
name|enrichTopLevelElementsDocumentation
parameter_list|(
name|NodeList
name|elements
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|File
argument_list|>
name|jsonFiles
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|elements
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
name|elements
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
if|if
condition|(
name|jsonFiles
operator|.
name|containsKey
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|addElementDocumentation
argument_list|(
name|item
argument_list|,
name|jsonFiles
operator|.
name|get
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|enrichTypeAttributesDocumentation (Log log, NodeList attributeElements, File jsonFile)
specifier|public
name|void
name|enrichTypeAttributesDocumentation
parameter_list|(
name|Log
name|log
parameter_list|,
name|NodeList
name|attributeElements
parameter_list|,
name|File
name|jsonFile
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|int
name|j
init|=
literal|0
init|;
name|j
operator|<
name|attributeElements
operator|.
name|getLength
argument_list|()
condition|;
name|j
operator|++
control|)
block|{
name|Element
name|item
init|=
operator|(
name|Element
operator|)
name|attributeElements
operator|.
name|item
argument_list|(
name|j
argument_list|)
decl_stmt|;
name|addAttributeDocumentation
argument_list|(
name|log
argument_list|,
name|item
argument_list|,
name|jsonFile
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|addElementDocumentation (Element item, File jsonFile)
specifier|private
name|void
name|addElementDocumentation
parameter_list|(
name|Element
name|item
parameter_list|,
name|File
name|jsonFile
parameter_list|)
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|rows
init|=
name|JSonSchemaHelper
operator|.
name|parseJsonSchema
argument_list|(
name|Constants
operator|.
name|MODEL_ATTRIBUTE_NAME
argument_list|,
name|PackageHelper
operator|.
name|fileToString
argument_list|(
name|jsonFile
argument_list|)
argument_list|,
literal|false
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|row
range|:
name|rows
control|)
block|{
if|if
condition|(
name|row
operator|.
name|containsKey
argument_list|(
name|Constants
operator|.
name|DESCRIPTION_ATTRIBUTE_NAME
argument_list|)
condition|)
block|{
name|String
name|descriptionText
init|=
name|row
operator|.
name|get
argument_list|(
name|Constants
operator|.
name|DESCRIPTION_ATTRIBUTE_NAME
argument_list|)
decl_stmt|;
name|addDocumentation
argument_list|(
name|item
argument_list|,
name|descriptionText
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
block|}
DECL|method|addAttributeDocumentation (Log log, Element item, File jsonFile)
specifier|private
name|void
name|addAttributeDocumentation
parameter_list|(
name|Log
name|log
parameter_list|,
name|Element
name|item
parameter_list|,
name|File
name|jsonFile
parameter_list|)
throws|throws
name|IOException
block|{
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
if|if
condition|(
name|isNullOrEmpty
argument_list|(
name|name
argument_list|)
condition|)
block|{
return|return;
block|}
name|String
name|descriptionText
init|=
literal|null
decl_stmt|;
name|String
name|defaultValueText
init|=
literal|null
decl_stmt|;
name|String
name|deprecatedText
init|=
literal|null
decl_stmt|;
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|rows
init|=
name|JSonSchemaHelper
operator|.
name|parseJsonSchema
argument_list|(
name|Constants
operator|.
name|PROPERTIES_ATTRIBUTE_NAME
argument_list|,
name|PackageHelper
operator|.
name|fileToString
argument_list|(
name|jsonFile
argument_list|)
argument_list|,
literal|true
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|row
range|:
name|rows
control|)
block|{
if|if
condition|(
name|name
operator|.
name|equals
argument_list|(
name|row
operator|.
name|get
argument_list|(
name|Constants
operator|.
name|NAME_ATTRIBUTE_NAME
argument_list|)
argument_list|)
condition|)
block|{
name|descriptionText
operator|=
name|row
operator|.
name|get
argument_list|(
name|Constants
operator|.
name|DESCRIPTION_ATTRIBUTE_NAME
argument_list|)
expr_stmt|;
name|defaultValueText
operator|=
name|row
operator|.
name|get
argument_list|(
name|Constants
operator|.
name|DEFAULT_VALUE_ATTRIBUTE_NAME
argument_list|)
expr_stmt|;
name|deprecatedText
operator|=
name|row
operator|.
name|get
argument_list|(
name|Constants
operator|.
name|DEPRECATED_ATTRIBUTE_NAME
argument_list|)
expr_stmt|;
block|}
block|}
comment|// special as this option is only in camel-blueprint
if|if
condition|(
literal|"useBlueprintPropertyResolver"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|descriptionText
operator|=
literal|"Whether to automatic detect OSGi Blueprint property placeholder service in use, and bridge with Camel property placeholder."
operator|+
literal|" When enabled this allows you to only setup OSGi Blueprint property placeholder and Camel can use the properties in the<camelContext>."
expr_stmt|;
name|defaultValueText
operator|=
literal|"true"
expr_stmt|;
block|}
if|if
condition|(
literal|"true"
operator|.
name|equals
argument_list|(
name|deprecatedText
argument_list|)
condition|)
block|{
name|descriptionText
operator|=
literal|"Deprecated: "
operator|+
name|descriptionText
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|isNullOrEmpty
argument_list|(
name|descriptionText
argument_list|)
condition|)
block|{
name|String
name|text
init|=
name|descriptionText
decl_stmt|;
if|if
condition|(
operator|!
name|isNullOrEmpty
argument_list|(
name|defaultValueText
argument_list|)
condition|)
block|{
name|text
operator|+=
operator|(
operator|!
name|text
operator|.
name|endsWith
argument_list|(
literal|"."
argument_list|)
condition|?
literal|"."
else|:
literal|""
operator|)
operator|+
operator|(
literal|" Default value: "
operator|+
name|defaultValueText
operator|)
expr_stmt|;
block|}
name|addDocumentation
argument_list|(
name|item
argument_list|,
name|text
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// we should skip warning about these if no documentation as they are special
name|boolean
name|skip
init|=
literal|"customId"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"inheritErrorHandler"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|||
literal|"rest"
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|&&
name|jsonFile
operator|.
name|getName
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"route.json"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|skip
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot find documentation for name: "
operator|+
name|name
operator|+
literal|" in json schema: "
operator|+
name|jsonFile
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|addDocumentation (Element item, String textContent)
specifier|private
name|void
name|addDocumentation
parameter_list|(
name|Element
name|item
parameter_list|,
name|String
name|textContent
parameter_list|)
block|{
name|Element
name|annotation
init|=
name|document
operator|.
name|createElement
argument_list|(
name|Constants
operator|.
name|XS_ANNOTATION_ELEMENT_NAME
argument_list|)
decl_stmt|;
name|Element
name|documentation
init|=
name|document
operator|.
name|createElement
argument_list|(
name|Constants
operator|.
name|XS_DOCUMENTATION_ELEMENT_NAME
argument_list|)
decl_stmt|;
name|documentation
operator|.
name|setAttribute
argument_list|(
literal|"xml:lang"
argument_list|,
literal|"en"
argument_list|)
expr_stmt|;
name|CDATASection
name|cdataDocumentationText
init|=
name|document
operator|.
name|createCDATASection
argument_list|(
name|formatTextContent
argument_list|(
name|item
argument_list|,
name|textContent
argument_list|)
argument_list|)
decl_stmt|;
name|documentation
operator|.
name|appendChild
argument_list|(
name|cdataDocumentationText
argument_list|)
expr_stmt|;
name|annotation
operator|.
name|appendChild
argument_list|(
name|documentation
argument_list|)
expr_stmt|;
if|if
condition|(
name|item
operator|.
name|getFirstChild
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|item
operator|.
name|insertBefore
argument_list|(
name|annotation
argument_list|,
name|item
operator|.
name|getFirstChild
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|item
operator|.
name|appendChild
argument_list|(
name|annotation
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|formatTextContent (Element item, String textContent)
specifier|private
name|String
name|formatTextContent
parameter_list|(
name|Element
name|item
parameter_list|,
name|String
name|textContent
parameter_list|)
block|{
name|StringBuilder
name|stringBuilder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|stringBuilder
operator|.
name|append
argument_list|(
name|System
operator|.
name|lineSeparator
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
name|WordUtils
operator|.
name|wrap
argument_list|(
name|textContent
argument_list|,
name|Constants
operator|.
name|WRAP_LENGTH
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|System
operator|.
name|lineSeparator
argument_list|()
argument_list|)
comment|// Fix closing tag intention.
operator|.
name|append
argument_list|(
name|StringUtils
operator|.
name|repeat
argument_list|(
name|Constants
operator|.
name|DEFAULT_XML_INTENTION
argument_list|,
name|getNodeDepth
argument_list|(
name|item
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|stringBuilder
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|getNodeDepth (Node item)
specifier|private
name|int
name|getNodeDepth
parameter_list|(
name|Node
name|item
parameter_list|)
block|{
name|int
name|depth
init|=
literal|1
decl_stmt|;
while|while
condition|(
name|item
operator|.
name|getParentNode
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|depth
operator|++
expr_stmt|;
name|item
operator|=
name|item
operator|.
name|getParentNode
argument_list|()
expr_stmt|;
block|}
return|return
name|depth
return|;
block|}
block|}
end_class

end_unit

