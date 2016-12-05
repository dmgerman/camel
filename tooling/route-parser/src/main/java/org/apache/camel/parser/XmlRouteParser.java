begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.parser
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|parser
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
name|util
operator|.
name|List
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
name|parser
operator|.
name|helper
operator|.
name|CamelJavaParserHelper
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
name|parser
operator|.
name|helper
operator|.
name|CamelXmlHelper
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
name|parser
operator|.
name|helper
operator|.
name|XmlLineNumberParser
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
name|apache
operator|.
name|camel
operator|.
name|parser
operator|.
name|model
operator|.
name|CamelEndpointDetails
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
name|parser
operator|.
name|model
operator|.
name|CamelSimpleExpressionDetails
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|forge
operator|.
name|roaster
operator|.
name|model
operator|.
name|util
operator|.
name|Strings
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
name|parser
operator|.
name|helper
operator|.
name|CamelXmlHelper
operator|.
name|getSafeAttribute
import|;
end_import

begin_comment
comment|/**  * A Camel XML parser that parses Camel XML routes source code.  *<p/>  * This implementation is higher level details, and uses the lower level parser {@link CamelJavaParserHelper}.  */
end_comment

begin_class
DECL|class|XmlRouteParser
specifier|public
specifier|final
class|class
name|XmlRouteParser
block|{
DECL|method|XmlRouteParser ()
specifier|private
name|XmlRouteParser
parameter_list|()
block|{     }
comment|/**      * Parses the XML source to discover Camel endpoints.      *      * @param xml                     the xml file as input stream      * @param baseDir                 the base of the source code      * @param fullyQualifiedFileName  the fully qualified source code file name      * @param endpoints               list to add discovered and parsed endpoints      */
DECL|method|parseXmlRouteEndpoints (InputStream xml, String baseDir, String fullyQualifiedFileName, List<CamelEndpointDetails> endpoints)
specifier|public
specifier|static
name|void
name|parseXmlRouteEndpoints
parameter_list|(
name|InputStream
name|xml
parameter_list|,
name|String
name|baseDir
parameter_list|,
name|String
name|fullyQualifiedFileName
parameter_list|,
name|List
argument_list|<
name|CamelEndpointDetails
argument_list|>
name|endpoints
parameter_list|)
throws|throws
name|Exception
block|{
comment|// find all the endpoints (currently only<endpoint> and within<route>)
comment|// try parse it as dom
name|Document
name|dom
init|=
literal|null
decl_stmt|;
try|try
block|{
name|dom
operator|=
name|XmlLineNumberParser
operator|.
name|parseXml
argument_list|(
name|xml
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore as the xml file may not be valid at this point
block|}
if|if
condition|(
name|dom
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|Node
argument_list|>
name|nodes
init|=
name|CamelXmlHelper
operator|.
name|findAllEndpoints
argument_list|(
name|dom
argument_list|)
decl_stmt|;
for|for
control|(
name|Node
name|node
range|:
name|nodes
control|)
block|{
name|String
name|uri
init|=
name|getSafeAttribute
argument_list|(
name|node
argument_list|,
literal|"uri"
argument_list|)
decl_stmt|;
if|if
condition|(
name|uri
operator|!=
literal|null
condition|)
block|{
comment|// trim and remove whitespace noise
name|uri
operator|=
name|trimEndpointUri
argument_list|(
name|uri
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|Strings
operator|.
name|isBlank
argument_list|(
name|uri
argument_list|)
condition|)
block|{
name|String
name|id
init|=
name|getSafeAttribute
argument_list|(
name|node
argument_list|,
literal|"id"
argument_list|)
decl_stmt|;
name|String
name|lineNumber
init|=
operator|(
name|String
operator|)
name|node
operator|.
name|getUserData
argument_list|(
name|XmlLineNumberParser
operator|.
name|LINE_NUMBER
argument_list|)
decl_stmt|;
name|String
name|lineNumberEnd
init|=
operator|(
name|String
operator|)
name|node
operator|.
name|getUserData
argument_list|(
name|XmlLineNumberParser
operator|.
name|LINE_NUMBER_END
argument_list|)
decl_stmt|;
comment|// we only want the relative dir name from the resource directory, eg META-INF/spring/foo.xml
name|String
name|fileName
init|=
name|fullyQualifiedFileName
decl_stmt|;
if|if
condition|(
name|fileName
operator|.
name|startsWith
argument_list|(
name|baseDir
argument_list|)
condition|)
block|{
name|fileName
operator|=
name|fileName
operator|.
name|substring
argument_list|(
name|baseDir
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|boolean
name|consumerOnly
init|=
literal|false
decl_stmt|;
name|boolean
name|producerOnly
init|=
literal|false
decl_stmt|;
name|String
name|nodeName
init|=
name|node
operator|.
name|getNodeName
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"from"
operator|.
name|equals
argument_list|(
name|nodeName
argument_list|)
operator|||
literal|"pollEnrich"
operator|.
name|equals
argument_list|(
name|nodeName
argument_list|)
condition|)
block|{
name|consumerOnly
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"to"
operator|.
name|equals
argument_list|(
name|nodeName
argument_list|)
operator|||
literal|"enrich"
operator|.
name|equals
argument_list|(
name|nodeName
argument_list|)
operator|||
literal|"wireTap"
operator|.
name|equals
argument_list|(
name|nodeName
argument_list|)
condition|)
block|{
name|producerOnly
operator|=
literal|true
expr_stmt|;
block|}
name|CamelEndpointDetails
name|detail
init|=
operator|new
name|CamelEndpointDetails
argument_list|()
decl_stmt|;
name|detail
operator|.
name|setFileName
argument_list|(
name|fileName
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setLineNumber
argument_list|(
name|lineNumber
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setLineNumberEnd
argument_list|(
name|lineNumberEnd
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setEndpointInstance
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setEndpointUri
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setEndpointComponentName
argument_list|(
name|endpointComponentName
argument_list|(
name|uri
argument_list|)
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setConsumerOnly
argument_list|(
name|consumerOnly
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setProducerOnly
argument_list|(
name|producerOnly
argument_list|)
expr_stmt|;
name|endpoints
operator|.
name|add
argument_list|(
name|detail
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Parses the XML source to discover Camel endpoints.      *      * @param xml                     the xml file as input stream      * @param baseDir                 the base of the source code      * @param fullyQualifiedFileName  the fully qualified source code file name      * @param simpleExpressions       list to add discovered and parsed simple expressions      */
DECL|method|parseXmlRouteSimpleExpressions (InputStream xml, String baseDir, String fullyQualifiedFileName, List<CamelSimpleExpressionDetails> simpleExpressions)
specifier|public
specifier|static
name|void
name|parseXmlRouteSimpleExpressions
parameter_list|(
name|InputStream
name|xml
parameter_list|,
name|String
name|baseDir
parameter_list|,
name|String
name|fullyQualifiedFileName
parameter_list|,
name|List
argument_list|<
name|CamelSimpleExpressionDetails
argument_list|>
name|simpleExpressions
parameter_list|)
throws|throws
name|Exception
block|{
comment|// find all the simple expressions
comment|// try parse it as dom
name|Document
name|dom
init|=
literal|null
decl_stmt|;
try|try
block|{
name|dom
operator|=
name|XmlLineNumberParser
operator|.
name|parseXml
argument_list|(
name|xml
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore as the xml file may not be valid at this point
block|}
if|if
condition|(
name|dom
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|Node
argument_list|>
name|nodes
init|=
name|CamelXmlHelper
operator|.
name|findAllSimpleExpressions
argument_list|(
name|dom
argument_list|)
decl_stmt|;
for|for
control|(
name|Node
name|node
range|:
name|nodes
control|)
block|{
name|String
name|simple
init|=
name|node
operator|.
name|getTextContent
argument_list|()
decl_stmt|;
name|String
name|lineNumber
init|=
operator|(
name|String
operator|)
name|node
operator|.
name|getUserData
argument_list|(
name|XmlLineNumberParser
operator|.
name|LINE_NUMBER
argument_list|)
decl_stmt|;
name|String
name|lineNumberEnd
init|=
operator|(
name|String
operator|)
name|node
operator|.
name|getUserData
argument_list|(
name|XmlLineNumberParser
operator|.
name|LINE_NUMBER_END
argument_list|)
decl_stmt|;
comment|// we only want the relative dir name from the resource directory, eg META-INF/spring/foo.xml
name|String
name|fileName
init|=
name|fullyQualifiedFileName
decl_stmt|;
if|if
condition|(
name|fileName
operator|.
name|startsWith
argument_list|(
name|baseDir
argument_list|)
condition|)
block|{
name|fileName
operator|=
name|fileName
operator|.
name|substring
argument_list|(
name|baseDir
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
name|CamelSimpleExpressionDetails
name|detail
init|=
operator|new
name|CamelSimpleExpressionDetails
argument_list|()
decl_stmt|;
name|detail
operator|.
name|setFileName
argument_list|(
name|fileName
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setLineNumber
argument_list|(
name|lineNumber
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setLineNumberEnd
argument_list|(
name|lineNumberEnd
argument_list|)
expr_stmt|;
name|detail
operator|.
name|setSimple
argument_list|(
name|simple
argument_list|)
expr_stmt|;
name|simpleExpressions
operator|.
name|add
argument_list|(
name|detail
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|endpointComponentName (String uri)
specifier|private
specifier|static
name|String
name|endpointComponentName
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
if|if
condition|(
name|uri
operator|!=
literal|null
condition|)
block|{
name|int
name|idx
init|=
name|uri
operator|.
name|indexOf
argument_list|(
literal|":"
argument_list|)
decl_stmt|;
if|if
condition|(
name|idx
operator|>
literal|0
condition|)
block|{
return|return
name|uri
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|trimEndpointUri (String uri)
specifier|private
specifier|static
name|String
name|trimEndpointUri
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|uri
operator|=
name|uri
operator|.
name|trim
argument_list|()
expr_stmt|;
comment|// if the uri is using new-lines then remove whitespace noise before& and ? separator
name|uri
operator|=
name|uri
operator|.
name|replaceAll
argument_list|(
literal|"(\\s+)(\\&)"
argument_list|,
literal|"$2"
argument_list|)
expr_stmt|;
name|uri
operator|=
name|uri
operator|.
name|replaceAll
argument_list|(
literal|"(\\&)(\\s+)"
argument_list|,
literal|"$1"
argument_list|)
expr_stmt|;
name|uri
operator|=
name|uri
operator|.
name|replaceAll
argument_list|(
literal|"(\\?)(\\s+)"
argument_list|,
literal|"$1"
argument_list|)
expr_stmt|;
return|return
name|uri
return|;
block|}
block|}
end_class

end_unit

