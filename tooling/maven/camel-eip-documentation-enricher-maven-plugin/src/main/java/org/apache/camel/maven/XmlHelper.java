begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|TransformerFactory
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
name|xml
operator|.
name|sax
operator|.
name|SAXException
import|;
end_import

begin_class
DECL|class|XmlHelper
specifier|public
specifier|final
class|class
name|XmlHelper
block|{
DECL|method|XmlHelper ()
specifier|private
name|XmlHelper
parameter_list|()
block|{ }
DECL|method|buildNamespaceAwareDocument (File xml)
specifier|public
specifier|static
name|Document
name|buildNamespaceAwareDocument
parameter_list|(
name|File
name|xml
parameter_list|)
throws|throws
name|SAXException
throws|,
name|ParserConfigurationException
throws|,
name|IOException
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
name|factory
operator|.
name|setFeature
argument_list|(
name|XMLConstants
operator|.
name|FEATURE_SECURE_PROCESSING
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
return|return
name|factory
operator|.
name|newDocumentBuilder
argument_list|()
operator|.
name|parse
argument_list|(
name|xml
argument_list|)
return|;
block|}
DECL|method|buildTransformer ()
specifier|public
specifier|static
name|Transformer
name|buildTransformer
parameter_list|()
throws|throws
name|TransformerConfigurationException
block|{
name|TransformerFactory
name|transformerFactory
init|=
name|TransformerFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|transformerFactory
operator|.
name|setFeature
argument_list|(
name|XMLConstants
operator|.
name|FEATURE_SECURE_PROCESSING
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|Transformer
name|transformer
init|=
name|transformerFactory
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
DECL|method|buildXPath (NamespaceContext namespaceContext)
specifier|public
specifier|static
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
DECL|method|isNullOrEmpty (String text)
specifier|public
specifier|static
name|boolean
name|isNullOrEmpty
parameter_list|(
name|String
name|text
parameter_list|)
block|{
return|return
name|text
operator|==
literal|null
operator|||
name|text
operator|.
name|isEmpty
argument_list|()
return|;
block|}
block|}
end_class

end_unit

