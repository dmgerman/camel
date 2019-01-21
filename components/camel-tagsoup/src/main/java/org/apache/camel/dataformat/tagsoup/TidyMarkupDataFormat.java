begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.tagsoup
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|tagsoup
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
name|OutputStream
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
name|io
operator|.
name|Writer
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
name|Map
operator|.
name|Entry
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
name|dom
operator|.
name|DOMResult
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
name|sax
operator|.
name|SAXSource
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
name|ContentHandler
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
name|InputSource
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
name|XMLReader
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
name|CamelException
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
name|Exchange
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
name|DataFormat
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
name|DataFormatName
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
name|annotations
operator|.
name|Dataformat
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
name|service
operator|.
name|ServiceSupport
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
name|ccil
operator|.
name|cowan
operator|.
name|tagsoup
operator|.
name|HTMLSchema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ccil
operator|.
name|cowan
operator|.
name|tagsoup
operator|.
name|Parser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ccil
operator|.
name|cowan
operator|.
name|tagsoup
operator|.
name|Schema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ccil
operator|.
name|cowan
operator|.
name|tagsoup
operator|.
name|XMLWriter
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

begin_comment
comment|/**  * Dataformat for TidyMarkup (aka Well formed HTML in XML form.. may or may not  * be XHTML) This dataformat is intended to convert bad HTML from a site (or  * file) into a well formed HTML document which can then be sent to XSLT or  * xpath'ed on.  */
end_comment

begin_class
annotation|@
name|Dataformat
argument_list|(
literal|"tidyMarkup"
argument_list|)
DECL|class|TidyMarkupDataFormat
specifier|public
class|class
name|TidyMarkupDataFormat
extends|extends
name|ServiceSupport
implements|implements
name|DataFormat
implements|,
name|DataFormatName
block|{
comment|/*      * Our Logger      */
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
name|TidyMarkupDataFormat
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|NO
specifier|private
specifier|static
specifier|final
name|String
name|NO
init|=
literal|"no"
decl_stmt|;
DECL|field|YES
specifier|private
specifier|static
specifier|final
name|String
name|YES
init|=
literal|"yes"
decl_stmt|;
DECL|field|XML
specifier|private
specifier|static
specifier|final
name|String
name|XML
init|=
literal|"xml"
decl_stmt|;
comment|/**      * When returning a String, do we omit the XML declaration in the top.      */
DECL|field|omitXmlDeclaration
specifier|private
name|boolean
name|omitXmlDeclaration
decl_stmt|;
comment|/**      * String or Node to return      */
DECL|field|dataObjectType
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|dataObjectType
decl_stmt|;
comment|/**      * What is the default output format ?      */
DECL|field|method
specifier|private
name|String
name|method
decl_stmt|;
comment|/**      * The Schema which we are parsing (default HTMLSchema)      */
DECL|field|parsingSchema
specifier|private
name|Schema
name|parsingSchema
decl_stmt|;
comment|/**      * User supplied Parser features      *<p>      * {@link http://home.ccil.org/~cowan/XML/tagsoup/#properties}      * {@link http://www.saxproject.org/apidoc/org/xml/sax/package-summary.html}      *</p>      */
DECL|field|parserFeatures
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|parserFeatures
decl_stmt|;
comment|/**      * User supplied Parser properties      *<p>      * {@link http://home.ccil.org/~cowan/XML/tagsoup/#properties}      * {@link http://www.saxproject.org/apidoc/org/xml/sax/package-summary.html}      *</p>      */
DECL|field|parserProperties
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parserProperties
decl_stmt|;
annotation|@
name|Override
DECL|method|getDataFormatName ()
specifier|public
name|String
name|getDataFormatName
parameter_list|()
block|{
return|return
literal|"tidyMarkup"
return|;
block|}
comment|/**      * Unsupported operation. We cannot create ugly HTML.      */
DECL|method|marshal (Exchange exchange, Object object, OutputStream outputStream)
specifier|public
name|void
name|marshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|object
parameter_list|,
name|OutputStream
name|outputStream
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
literal|"Marshalling from Well Formed HTML to ugly HTML is not supported."
operator|+
literal|" Only unmarshal is supported"
argument_list|)
throw|;
block|}
comment|/**      * Unmarshal the data      */
DECL|method|unmarshal (Exchange exchange, InputStream inputStream)
specifier|public
name|Object
name|unmarshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|inputStream
parameter_list|)
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|dataObjectType
argument_list|,
literal|"dataObjectType"
argument_list|,
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|dataObjectType
operator|.
name|isAssignableFrom
argument_list|(
name|Node
operator|.
name|class
argument_list|)
condition|)
block|{
return|return
name|asNodeTidyMarkup
argument_list|(
name|inputStream
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|dataObjectType
operator|.
name|isAssignableFrom
argument_list|(
name|String
operator|.
name|class
argument_list|)
condition|)
block|{
return|return
name|asStringTidyMarkup
argument_list|(
name|inputStream
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The return type ["
operator|+
name|dataObjectType
operator|.
name|getCanonicalName
argument_list|()
operator|+
literal|"] is unsupported"
argument_list|)
throw|;
block|}
block|}
comment|/**      * Return the tidy markup as a string      *       * @param inputStream      * @return String of XML      * @throws CamelException      */
DECL|method|asStringTidyMarkup (InputStream inputStream)
specifier|public
name|String
name|asStringTidyMarkup
parameter_list|(
name|InputStream
name|inputStream
parameter_list|)
throws|throws
name|CamelException
block|{
name|XMLReader
name|parser
init|=
name|createTagSoupParser
argument_list|()
decl_stmt|;
name|StringWriter
name|w
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|parser
operator|.
name|setContentHandler
argument_list|(
name|createContentHandler
argument_list|(
name|w
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|parser
operator|.
name|parse
argument_list|(
operator|new
name|InputSource
argument_list|(
name|inputStream
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|w
operator|.
name|toString
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
literal|"Failed to convert the HTML to tidy Markup"
argument_list|,
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
try|try
block|{
name|inputStream
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Failed to close the inputStream"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Return the HTML Markup as an {@link org.w3c.dom.Node}      *       * @param inputStream      *            The input Stream to convert      * @return org.w3c.dom.Node The HTML Markup as a DOM Node      * @throws CamelException      */
DECL|method|asNodeTidyMarkup (InputStream inputStream)
specifier|public
name|Node
name|asNodeTidyMarkup
parameter_list|(
name|InputStream
name|inputStream
parameter_list|)
throws|throws
name|CamelException
block|{
name|XMLReader
name|parser
init|=
name|createTagSoupParser
argument_list|()
decl_stmt|;
name|StringWriter
name|w
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|parser
operator|.
name|setContentHandler
argument_list|(
name|createContentHandler
argument_list|(
name|w
argument_list|)
argument_list|)
expr_stmt|;
try|try
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
name|DOMResult
name|result
init|=
operator|new
name|DOMResult
argument_list|()
decl_stmt|;
name|transformer
operator|.
name|transform
argument_list|(
operator|new
name|SAXSource
argument_list|(
name|parser
argument_list|,
operator|new
name|InputSource
argument_list|(
name|inputStream
argument_list|)
argument_list|)
argument_list|,
name|result
argument_list|)
expr_stmt|;
return|return
name|result
operator|.
name|getNode
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CamelException
argument_list|(
literal|"Failed to convert the HTML to tidy Markup"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Create the tagSoup Parser      */
DECL|method|createTagSoupParser ()
specifier|protected
name|XMLReader
name|createTagSoupParser
parameter_list|()
throws|throws
name|CamelException
block|{
name|XMLReader
name|reader
init|=
operator|new
name|Parser
argument_list|()
decl_stmt|;
try|try
block|{
name|reader
operator|.
name|setFeature
argument_list|(
name|Parser
operator|.
name|namespacesFeature
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|reader
operator|.
name|setFeature
argument_list|(
name|Parser
operator|.
name|namespacePrefixesFeature
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|/*              * set each parser feature that the user may have supplied.              * http://www.saxproject.org/apidoc/org/xml/sax/package-summary.html              * http://home.ccil.org/~cowan/XML/tagsoup/#properties              */
if|if
condition|(
name|getParserFeatures
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|e
range|:
name|getParserFeatures
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|reader
operator|.
name|setFeature
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/*              * set each parser feature that the user may have supplied. {@link              * http://home.ccil.org/~cowan/XML/tagsoup/#properties}              */
if|if
condition|(
name|getParserProperties
argument_list|()
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|e
range|:
name|getParserProperties
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|reader
operator|.
name|setProperty
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/*              * default the schema to HTML              */
if|if
condition|(
name|this
operator|.
name|getParsingSchema
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|reader
operator|.
name|setProperty
argument_list|(
name|Parser
operator|.
name|schemaProperty
argument_list|,
name|getParsingSchema
argument_list|()
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
name|IllegalArgumentException
argument_list|(
literal|"Problem configuring the parser"
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|reader
return|;
block|}
DECL|method|createContentHandler (Writer w)
specifier|protected
name|ContentHandler
name|createContentHandler
parameter_list|(
name|Writer
name|w
parameter_list|)
block|{
name|XMLWriter
name|xmlWriter
init|=
operator|new
name|XMLWriter
argument_list|(
name|w
argument_list|)
decl_stmt|;
comment|// we might need to expose more than these two but that is pretty good
comment|// for a default well formed Html generator
if|if
condition|(
name|getMethod
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|xmlWriter
operator|.
name|setOutputProperty
argument_list|(
name|XMLWriter
operator|.
name|METHOD
argument_list|,
name|getMethod
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|xmlWriter
operator|.
name|setOutputProperty
argument_list|(
name|XMLWriter
operator|.
name|METHOD
argument_list|,
name|XML
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|omitXmlDeclaration
condition|)
block|{
name|xmlWriter
operator|.
name|setOutputProperty
argument_list|(
name|XMLWriter
operator|.
name|OMIT_XML_DECLARATION
argument_list|,
name|YES
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|xmlWriter
operator|.
name|setOutputProperty
argument_list|(
name|XMLWriter
operator|.
name|OMIT_XML_DECLARATION
argument_list|,
name|NO
argument_list|)
expr_stmt|;
block|}
return|return
name|xmlWriter
return|;
block|}
DECL|method|setParsingSchema (Schema schema)
specifier|public
name|void
name|setParsingSchema
parameter_list|(
name|Schema
name|schema
parameter_list|)
block|{
name|this
operator|.
name|parsingSchema
operator|=
name|schema
expr_stmt|;
block|}
DECL|method|getParsingSchema ()
specifier|public
name|Schema
name|getParsingSchema
parameter_list|()
block|{
if|if
condition|(
name|parsingSchema
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|parsingSchema
operator|=
operator|new
name|HTMLSchema
argument_list|()
expr_stmt|;
block|}
return|return
name|parsingSchema
return|;
block|}
DECL|method|isOmitXmlDeclaration ()
specifier|public
name|boolean
name|isOmitXmlDeclaration
parameter_list|()
block|{
return|return
name|omitXmlDeclaration
return|;
block|}
DECL|method|setOmitXmlDeclaration (boolean omitXmlDeclaration)
specifier|public
name|void
name|setOmitXmlDeclaration
parameter_list|(
name|boolean
name|omitXmlDeclaration
parameter_list|)
block|{
name|this
operator|.
name|omitXmlDeclaration
operator|=
name|omitXmlDeclaration
expr_stmt|;
block|}
DECL|method|setParserFeatures (Map<String, Boolean> parserFeatures)
specifier|public
name|void
name|setParserFeatures
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|parserFeatures
parameter_list|)
block|{
name|this
operator|.
name|parserFeatures
operator|=
name|parserFeatures
expr_stmt|;
block|}
DECL|method|getParserFeatures ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Boolean
argument_list|>
name|getParserFeatures
parameter_list|()
block|{
return|return
name|parserFeatures
return|;
block|}
DECL|method|setParserProperties (Map<String, Object> parserProperties)
specifier|public
name|void
name|setParserProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parserProperties
parameter_list|)
block|{
name|this
operator|.
name|parserProperties
operator|=
name|parserProperties
expr_stmt|;
block|}
DECL|method|getParserProperties ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getParserProperties
parameter_list|()
block|{
return|return
name|parserProperties
return|;
block|}
DECL|method|setMethod (String method)
specifier|public
name|void
name|setMethod
parameter_list|(
name|String
name|method
parameter_list|)
block|{
name|this
operator|.
name|method
operator|=
name|method
expr_stmt|;
block|}
DECL|method|getMethod ()
specifier|public
name|String
name|getMethod
parameter_list|()
block|{
return|return
name|method
return|;
block|}
DECL|method|getDataObjectType ()
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getDataObjectType
parameter_list|()
block|{
return|return
name|dataObjectType
return|;
block|}
DECL|method|setDataObjectType (Class<?> dataObjectType)
specifier|public
name|void
name|setDataObjectType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|dataObjectType
parameter_list|)
block|{
name|this
operator|.
name|dataObjectType
operator|=
name|dataObjectType
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
block|}
end_class

end_unit

