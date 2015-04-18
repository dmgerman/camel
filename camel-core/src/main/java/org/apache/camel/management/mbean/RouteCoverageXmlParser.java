begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management.mbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
operator|.
name|mbean
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
name|Stack
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
name|SAXParser
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
name|SAXParserFactory
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
name|api
operator|.
name|management
operator|.
name|mbean
operator|.
name|ManagedProcessorMBean
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
name|api
operator|.
name|management
operator|.
name|mbean
operator|.
name|ManagedRouteMBean
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
name|xml
operator|.
name|sax
operator|.
name|Attributes
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
name|Locator
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
name|xml
operator|.
name|sax
operator|.
name|helpers
operator|.
name|DefaultHandler
import|;
end_import

begin_comment
comment|/**  * An XML parser that uses SAX to enrich route stats in the route dump.  *<p/>  * The coverage details:  *<ul>  *<li>exchangesTotal - Total number of exchanges</li>  *<li>totalProcessingTime - Total processing time in millis</li>  *</ul>  * Is included as attributes on the route nodes.  */
end_comment

begin_class
DECL|class|RouteCoverageXmlParser
specifier|public
specifier|final
class|class
name|RouteCoverageXmlParser
block|{
comment|/**      * Parses the XML.      *      * @param camelContext the CamelContext      * @param is           the XML content as an input stream      * @return the DOM model of the routes with coverage information stored as attributes      * @throws Exception is thrown if error parsing      */
DECL|method|parseXml (final CamelContext camelContext, final InputStream is)
specifier|public
specifier|static
name|Document
name|parseXml
parameter_list|(
specifier|final
name|CamelContext
name|camelContext
parameter_list|,
specifier|final
name|InputStream
name|is
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|Document
name|doc
decl_stmt|;
name|SAXParser
name|parser
decl_stmt|;
specifier|final
name|SAXParserFactory
name|factory
init|=
name|SAXParserFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|parser
operator|=
name|factory
operator|.
name|newSAXParser
argument_list|()
expr_stmt|;
specifier|final
name|DocumentBuilderFactory
name|docBuilderFactory
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
specifier|final
name|DocumentBuilder
name|docBuilder
init|=
name|docBuilderFactory
operator|.
name|newDocumentBuilder
argument_list|()
decl_stmt|;
name|doc
operator|=
name|docBuilder
operator|.
name|newDocument
argument_list|()
expr_stmt|;
specifier|final
name|Stack
argument_list|<
name|Element
argument_list|>
name|elementStack
init|=
operator|new
name|Stack
argument_list|<
name|Element
argument_list|>
argument_list|()
decl_stmt|;
specifier|final
name|StringBuilder
name|textBuffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
specifier|final
name|DefaultHandler
name|handler
init|=
operator|new
name|DefaultHandler
argument_list|()
block|{
specifier|private
name|Locator
name|locator
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|setDocumentLocator
parameter_list|(
specifier|final
name|Locator
name|locator
parameter_list|)
block|{
name|this
operator|.
name|locator
operator|=
name|locator
expr_stmt|;
comment|// Save the locator, so that it can be used later for line tracking when traversing nodes.
block|}
annotation|@
name|Override
specifier|public
name|void
name|startElement
parameter_list|(
specifier|final
name|String
name|uri
parameter_list|,
specifier|final
name|String
name|localName
parameter_list|,
specifier|final
name|String
name|qName
parameter_list|,
specifier|final
name|Attributes
name|attributes
parameter_list|)
throws|throws
name|SAXException
block|{
name|addTextIfNeeded
argument_list|()
expr_stmt|;
specifier|final
name|Element
name|el
init|=
name|doc
operator|.
name|createElement
argument_list|(
name|qName
argument_list|)
decl_stmt|;
comment|// add other elements
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
name|el
operator|.
name|setAttribute
argument_list|(
name|attributes
operator|.
name|getQName
argument_list|(
name|i
argument_list|)
argument_list|,
name|attributes
operator|.
name|getValue
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|id
init|=
name|el
operator|.
name|getAttribute
argument_list|(
literal|"id"
argument_list|)
decl_stmt|;
if|if
condition|(
name|id
operator|!=
literal|null
condition|)
block|{
try|try
block|{
if|if
condition|(
literal|"route"
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
condition|)
block|{
name|ManagedRouteMBean
name|route
init|=
name|camelContext
operator|.
name|getManagedRoute
argument_list|(
name|id
argument_list|,
name|ManagedRouteMBean
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|route
operator|!=
literal|null
condition|)
block|{
name|long
name|total
init|=
name|route
operator|.
name|getExchangesTotal
argument_list|()
decl_stmt|;
name|el
operator|.
name|setAttribute
argument_list|(
literal|"exchangesTotal"
argument_list|,
literal|""
operator|+
name|total
argument_list|)
expr_stmt|;
name|long
name|totalTime
init|=
name|route
operator|.
name|getTotalProcessingTime
argument_list|()
decl_stmt|;
name|el
operator|.
name|setAttribute
argument_list|(
literal|"totalProcessingTime"
argument_list|,
literal|""
operator|+
name|totalTime
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
literal|"from"
operator|.
name|equals
argument_list|(
name|qName
argument_list|)
condition|)
block|{
comment|// TODO: include the stats from the route mbean as that would be the same
block|}
else|else
block|{
name|ManagedProcessorMBean
name|processor
init|=
name|camelContext
operator|.
name|getManagedProcessor
argument_list|(
name|id
argument_list|,
name|ManagedProcessorMBean
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|processor
operator|!=
literal|null
condition|)
block|{
name|long
name|total
init|=
name|processor
operator|.
name|getExchangesTotal
argument_list|()
decl_stmt|;
name|el
operator|.
name|setAttribute
argument_list|(
literal|"exchangesTotal"
argument_list|,
literal|""
operator|+
name|total
argument_list|)
expr_stmt|;
name|long
name|totalTime
init|=
name|processor
operator|.
name|getTotalProcessingTime
argument_list|()
decl_stmt|;
name|el
operator|.
name|setAttribute
argument_list|(
literal|"totalProcessingTime"
argument_list|,
literal|""
operator|+
name|totalTime
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
comment|// we do not want customId in output
name|el
operator|.
name|removeAttribute
argument_list|(
literal|"customId"
argument_list|)
expr_stmt|;
name|elementStack
operator|.
name|push
argument_list|(
name|el
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|endElement
parameter_list|(
specifier|final
name|String
name|uri
parameter_list|,
specifier|final
name|String
name|localName
parameter_list|,
specifier|final
name|String
name|qName
parameter_list|)
block|{
name|addTextIfNeeded
argument_list|()
expr_stmt|;
specifier|final
name|Element
name|closedEl
init|=
name|elementStack
operator|.
name|pop
argument_list|()
decl_stmt|;
if|if
condition|(
name|elementStack
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// Is this the root element?
name|doc
operator|.
name|appendChild
argument_list|(
name|closedEl
argument_list|)
expr_stmt|;
block|}
else|else
block|{
specifier|final
name|Element
name|parentEl
init|=
name|elementStack
operator|.
name|peek
argument_list|()
decl_stmt|;
name|parentEl
operator|.
name|appendChild
argument_list|(
name|closedEl
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|characters
parameter_list|(
specifier|final
name|char
name|ch
index|[]
parameter_list|,
specifier|final
name|int
name|start
parameter_list|,
specifier|final
name|int
name|length
parameter_list|)
throws|throws
name|SAXException
block|{
name|textBuffer
operator|.
name|append
argument_list|(
name|ch
argument_list|,
name|start
argument_list|,
name|length
argument_list|)
expr_stmt|;
block|}
comment|// Outputs text accumulated under the current node
specifier|private
name|void
name|addTextIfNeeded
parameter_list|()
block|{
if|if
condition|(
name|textBuffer
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
specifier|final
name|Element
name|el
init|=
name|elementStack
operator|.
name|peek
argument_list|()
decl_stmt|;
specifier|final
name|Node
name|textNode
init|=
name|doc
operator|.
name|createTextNode
argument_list|(
name|textBuffer
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|el
operator|.
name|appendChild
argument_list|(
name|textNode
argument_list|)
expr_stmt|;
name|textBuffer
operator|.
name|delete
argument_list|(
literal|0
argument_list|,
name|textBuffer
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
decl_stmt|;
name|parser
operator|.
name|parse
argument_list|(
name|is
argument_list|,
name|handler
argument_list|)
expr_stmt|;
return|return
name|doc
return|;
block|}
block|}
end_class

end_unit

