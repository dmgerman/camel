begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.converter.jaxb
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|converter
operator|.
name|jaxb
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
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
name|InputStreamReader
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
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|concurrent
operator|.
name|BlockingQueue
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|LinkedBlockingQueue
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
name|JAXBElement
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
name|JAXBIntrospector
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
name|bind
operator|.
name|Unmarshaller
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
name|ValidationEvent
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
name|ValidationEventHandler
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
name|QName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamWriter
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
name|Source
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
name|StreamSource
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
name|validation
operator|.
name|SchemaFactory
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
name|CamelContextAware
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
name|InvalidPayloadException
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
name|NoTypeConversionAvailableException
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
name|TypeConverter
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
name|support
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
name|CamelContextHelper
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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ResourceHelper
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
comment|/**  * A<a href="http://camel.apache.org/data-format.html">data format</a> ({@link DataFormat})  * using JAXB2 to marshal to and from XML  *  * @version  */
end_comment

begin_class
DECL|class|JaxbDataFormat
specifier|public
class|class
name|JaxbDataFormat
extends|extends
name|ServiceSupport
implements|implements
name|DataFormat
implements|,
name|DataFormatName
implements|,
name|CamelContextAware
block|{
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
name|JaxbDataFormat
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|SCHEMA_FACTORY_POOL
specifier|private
specifier|static
specifier|final
name|BlockingQueue
argument_list|<
name|SchemaFactory
argument_list|>
name|SCHEMA_FACTORY_POOL
init|=
operator|new
name|LinkedBlockingQueue
argument_list|<
name|SchemaFactory
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|schemaFactory
specifier|private
name|SchemaFactory
name|schemaFactory
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|context
specifier|private
name|JAXBContext
name|context
decl_stmt|;
DECL|field|introspector
specifier|private
name|JAXBIntrospector
name|introspector
decl_stmt|;
DECL|field|contextPath
specifier|private
name|String
name|contextPath
decl_stmt|;
DECL|field|schema
specifier|private
name|String
name|schema
decl_stmt|;
DECL|field|schemaLocation
specifier|private
name|String
name|schemaLocation
decl_stmt|;
DECL|field|prettyPrint
specifier|private
name|boolean
name|prettyPrint
init|=
literal|true
decl_stmt|;
DECL|field|ignoreJAXBElement
specifier|private
name|boolean
name|ignoreJAXBElement
init|=
literal|true
decl_stmt|;
DECL|field|mustBeJAXBElement
specifier|private
name|boolean
name|mustBeJAXBElement
init|=
literal|true
decl_stmt|;
DECL|field|filterNonXmlChars
specifier|private
name|boolean
name|filterNonXmlChars
decl_stmt|;
DECL|field|encoding
specifier|private
name|String
name|encoding
decl_stmt|;
DECL|field|fragment
specifier|private
name|boolean
name|fragment
decl_stmt|;
comment|// partial support
DECL|field|partNamespace
specifier|private
name|QName
name|partNamespace
decl_stmt|;
DECL|field|partClass
specifier|private
name|String
name|partClass
decl_stmt|;
DECL|field|partialClass
specifier|private
name|Class
argument_list|<
name|Object
argument_list|>
name|partialClass
decl_stmt|;
DECL|field|namespacePrefixRef
specifier|private
name|String
name|namespacePrefixRef
decl_stmt|;
DECL|field|namespacePrefix
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespacePrefix
decl_stmt|;
DECL|field|namespacePrefixMapper
specifier|private
name|JaxbNamespacePrefixMapper
name|namespacePrefixMapper
decl_stmt|;
DECL|field|xmlStreamWriterWrapper
specifier|private
name|JaxbXmlStreamWriterWrapper
name|xmlStreamWriterWrapper
decl_stmt|;
DECL|field|typeConverter
specifier|private
name|TypeConverter
name|typeConverter
decl_stmt|;
DECL|field|cachedSchema
specifier|private
name|Schema
name|cachedSchema
decl_stmt|;
DECL|method|JaxbDataFormat ()
specifier|public
name|JaxbDataFormat
parameter_list|()
block|{     }
DECL|method|JaxbDataFormat (JAXBContext context)
specifier|public
name|JaxbDataFormat
parameter_list|(
name|JAXBContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
DECL|method|JaxbDataFormat (String contextPath)
specifier|public
name|JaxbDataFormat
parameter_list|(
name|String
name|contextPath
parameter_list|)
block|{
name|this
operator|.
name|contextPath
operator|=
name|contextPath
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getDataFormatName ()
specifier|public
name|String
name|getDataFormatName
parameter_list|()
block|{
return|return
literal|"jaxb"
return|;
block|}
DECL|method|marshal (Exchange exchange, Object graph, OutputStream stream)
specifier|public
name|void
name|marshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|graph
parameter_list|,
name|OutputStream
name|stream
parameter_list|)
throws|throws
name|IOException
throws|,
name|SAXException
block|{
try|try
block|{
comment|// must create a new instance of marshaller as its not thread safe
name|Marshaller
name|marshaller
init|=
name|createMarshaller
argument_list|()
decl_stmt|;
if|if
condition|(
name|isPrettyPrint
argument_list|()
condition|)
block|{
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
block|}
comment|// exchange take precedence over encoding option
name|String
name|charset
init|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|CHARSET_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|charset
operator|==
literal|null
condition|)
block|{
name|charset
operator|=
name|encoding
expr_stmt|;
block|}
if|if
condition|(
name|charset
operator|!=
literal|null
condition|)
block|{
name|marshaller
operator|.
name|setProperty
argument_list|(
name|Marshaller
operator|.
name|JAXB_ENCODING
argument_list|,
name|charset
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isFragment
argument_list|()
condition|)
block|{
name|marshaller
operator|.
name|setProperty
argument_list|(
name|Marshaller
operator|.
name|JAXB_FRAGMENT
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|schemaLocation
argument_list|)
condition|)
block|{
name|marshaller
operator|.
name|setProperty
argument_list|(
name|Marshaller
operator|.
name|JAXB_SCHEMA_LOCATION
argument_list|,
name|schemaLocation
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|namespacePrefixMapper
operator|!=
literal|null
condition|)
block|{
name|marshaller
operator|.
name|setProperty
argument_list|(
name|namespacePrefixMapper
operator|.
name|getRegistrationKey
argument_list|()
argument_list|,
name|namespacePrefixMapper
argument_list|)
expr_stmt|;
block|}
name|marshal
argument_list|(
name|exchange
argument_list|,
name|graph
argument_list|,
name|stream
argument_list|,
name|marshaller
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
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|marshal (Exchange exchange, Object graph, OutputStream stream, Marshaller marshaller)
name|void
name|marshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|graph
parameter_list|,
name|OutputStream
name|stream
parameter_list|,
name|Marshaller
name|marshaller
parameter_list|)
throws|throws
name|XMLStreamException
throws|,
name|JAXBException
throws|,
name|NoTypeConversionAvailableException
throws|,
name|IOException
throws|,
name|InvalidPayloadException
block|{
name|Object
name|e
init|=
name|graph
decl_stmt|;
if|if
condition|(
name|partialClass
operator|!=
literal|null
operator|&&
name|getPartNamespace
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|e
operator|=
operator|new
name|JAXBElement
argument_list|<
name|Object
argument_list|>
argument_list|(
name|getPartNamespace
argument_list|()
argument_list|,
name|partialClass
argument_list|,
name|graph
argument_list|)
expr_stmt|;
block|}
comment|// only marshal if its possible
if|if
condition|(
name|introspector
operator|.
name|isElement
argument_list|(
name|e
argument_list|)
condition|)
block|{
if|if
condition|(
name|asXmlStreamWriter
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
name|XMLStreamWriter
name|writer
init|=
name|typeConverter
operator|.
name|convertTo
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|,
name|stream
argument_list|)
decl_stmt|;
if|if
condition|(
name|needFiltering
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
name|writer
operator|=
operator|new
name|FilteringXmlStreamWriter
argument_list|(
name|writer
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|xmlStreamWriterWrapper
operator|!=
literal|null
condition|)
block|{
name|writer
operator|=
name|xmlStreamWriterWrapper
operator|.
name|wrapWriter
argument_list|(
name|writer
argument_list|)
expr_stmt|;
block|}
name|marshaller
operator|.
name|marshal
argument_list|(
name|e
argument_list|,
name|writer
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|marshaller
operator|.
name|marshal
argument_list|(
name|e
argument_list|,
name|stream
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
operator|!
name|mustBeJAXBElement
condition|)
block|{
comment|// write the graph as is to the output stream
if|if
condition|(
name|LOG
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Attempt to marshalling non JAXBElement with type {} as InputStream"
argument_list|,
name|ObjectHelper
operator|.
name|classCanonicalName
argument_list|(
name|graph
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|InputStream
name|is
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|mandatoryConvertTo
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|graph
argument_list|)
decl_stmt|;
name|IOHelper
operator|.
name|copyAndCloseInput
argument_list|(
name|is
argument_list|,
name|stream
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|InvalidPayloadException
argument_list|(
name|exchange
argument_list|,
name|JAXBElement
operator|.
name|class
argument_list|)
throw|;
block|}
block|}
DECL|method|asXmlStreamWriter (Exchange exchange)
specifier|private
name|boolean
name|asXmlStreamWriter
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
return|return
name|needFiltering
argument_list|(
name|exchange
argument_list|)
operator|||
operator|(
name|xmlStreamWriterWrapper
operator|!=
literal|null
operator|)
return|;
block|}
DECL|method|unmarshal (Exchange exchange, InputStream stream)
specifier|public
name|Object
name|unmarshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|stream
parameter_list|)
throws|throws
name|IOException
throws|,
name|SAXException
block|{
try|try
block|{
name|Object
name|answer
decl_stmt|;
name|XMLStreamReader
name|xmlReader
decl_stmt|;
if|if
condition|(
name|needFiltering
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
name|xmlReader
operator|=
name|typeConverter
operator|.
name|convertTo
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|createNonXmlFilterReader
argument_list|(
name|exchange
argument_list|,
name|stream
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|xmlReader
operator|=
name|typeConverter
operator|.
name|convertTo
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|stream
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|partialClass
operator|!=
literal|null
condition|)
block|{
comment|// partial unmarshalling
name|answer
operator|=
name|createUnmarshaller
argument_list|()
operator|.
name|unmarshal
argument_list|(
name|xmlReader
argument_list|,
name|partialClass
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
name|createUnmarshaller
argument_list|()
operator|.
name|unmarshal
argument_list|(
name|xmlReader
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|answer
operator|instanceof
name|JAXBElement
operator|&&
name|isIgnoreJAXBElement
argument_list|()
condition|)
block|{
name|answer
operator|=
operator|(
operator|(
name|JAXBElement
argument_list|<
name|?
argument_list|>
operator|)
name|answer
operator|)
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|createNonXmlFilterReader (Exchange exchange, InputStream stream)
specifier|private
name|NonXmlFilterReader
name|createNonXmlFilterReader
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|stream
parameter_list|)
throws|throws
name|UnsupportedEncodingException
block|{
return|return
operator|new
name|NonXmlFilterReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|stream
argument_list|,
name|IOHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
DECL|method|needFiltering (Exchange exchange)
specifier|protected
name|boolean
name|needFiltering
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// exchange property takes precedence over data format property
return|return
name|exchange
operator|==
literal|null
condition|?
name|filterNonXmlChars
else|:
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|FILTER_NON_XML_CHARS
argument_list|,
name|filterNonXmlChars
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
DECL|method|isIgnoreJAXBElement ()
specifier|public
name|boolean
name|isIgnoreJAXBElement
parameter_list|()
block|{
return|return
name|ignoreJAXBElement
return|;
block|}
DECL|method|setIgnoreJAXBElement (boolean flag)
specifier|public
name|void
name|setIgnoreJAXBElement
parameter_list|(
name|boolean
name|flag
parameter_list|)
block|{
name|ignoreJAXBElement
operator|=
name|flag
expr_stmt|;
block|}
DECL|method|isMustBeJAXBElement ()
specifier|public
name|boolean
name|isMustBeJAXBElement
parameter_list|()
block|{
return|return
name|mustBeJAXBElement
return|;
block|}
DECL|method|setMustBeJAXBElement (boolean mustBeJAXBElement)
specifier|public
name|void
name|setMustBeJAXBElement
parameter_list|(
name|boolean
name|mustBeJAXBElement
parameter_list|)
block|{
name|this
operator|.
name|mustBeJAXBElement
operator|=
name|mustBeJAXBElement
expr_stmt|;
block|}
DECL|method|getContext ()
specifier|public
name|JAXBContext
name|getContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
DECL|method|setContext (JAXBContext context)
specifier|public
name|void
name|setContext
parameter_list|(
name|JAXBContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
DECL|method|getContextPath ()
specifier|public
name|String
name|getContextPath
parameter_list|()
block|{
return|return
name|contextPath
return|;
block|}
DECL|method|setContextPath (String contextPath)
specifier|public
name|void
name|setContextPath
parameter_list|(
name|String
name|contextPath
parameter_list|)
block|{
name|this
operator|.
name|contextPath
operator|=
name|contextPath
expr_stmt|;
block|}
DECL|method|getSchemaFactory ()
specifier|public
name|SchemaFactory
name|getSchemaFactory
parameter_list|()
block|{
if|if
condition|(
name|schemaFactory
operator|==
literal|null
condition|)
block|{
return|return
name|getOrCreateSchemaFactory
argument_list|()
return|;
block|}
return|return
name|schemaFactory
return|;
block|}
DECL|method|setSchemaFactory (SchemaFactory schemaFactory)
specifier|public
name|void
name|setSchemaFactory
parameter_list|(
name|SchemaFactory
name|schemaFactory
parameter_list|)
block|{
name|this
operator|.
name|schemaFactory
operator|=
name|schemaFactory
expr_stmt|;
block|}
DECL|method|getSchema ()
specifier|public
name|String
name|getSchema
parameter_list|()
block|{
return|return
name|schema
return|;
block|}
DECL|method|setSchema (String schema)
specifier|public
name|void
name|setSchema
parameter_list|(
name|String
name|schema
parameter_list|)
block|{
name|this
operator|.
name|schema
operator|=
name|schema
expr_stmt|;
block|}
DECL|method|isPrettyPrint ()
specifier|public
name|boolean
name|isPrettyPrint
parameter_list|()
block|{
return|return
name|prettyPrint
return|;
block|}
DECL|method|setPrettyPrint (boolean prettyPrint)
specifier|public
name|void
name|setPrettyPrint
parameter_list|(
name|boolean
name|prettyPrint
parameter_list|)
block|{
name|this
operator|.
name|prettyPrint
operator|=
name|prettyPrint
expr_stmt|;
block|}
DECL|method|isFragment ()
specifier|public
name|boolean
name|isFragment
parameter_list|()
block|{
return|return
name|fragment
return|;
block|}
DECL|method|setFragment (boolean fragment)
specifier|public
name|void
name|setFragment
parameter_list|(
name|boolean
name|fragment
parameter_list|)
block|{
name|this
operator|.
name|fragment
operator|=
name|fragment
expr_stmt|;
block|}
DECL|method|isFilterNonXmlChars ()
specifier|public
name|boolean
name|isFilterNonXmlChars
parameter_list|()
block|{
return|return
name|filterNonXmlChars
return|;
block|}
DECL|method|setFilterNonXmlChars (boolean filterNonXmlChars)
specifier|public
name|void
name|setFilterNonXmlChars
parameter_list|(
name|boolean
name|filterNonXmlChars
parameter_list|)
block|{
name|this
operator|.
name|filterNonXmlChars
operator|=
name|filterNonXmlChars
expr_stmt|;
block|}
DECL|method|getEncoding ()
specifier|public
name|String
name|getEncoding
parameter_list|()
block|{
return|return
name|encoding
return|;
block|}
DECL|method|setEncoding (String encoding)
specifier|public
name|void
name|setEncoding
parameter_list|(
name|String
name|encoding
parameter_list|)
block|{
name|this
operator|.
name|encoding
operator|=
name|encoding
expr_stmt|;
block|}
DECL|method|getPartNamespace ()
specifier|public
name|QName
name|getPartNamespace
parameter_list|()
block|{
return|return
name|partNamespace
return|;
block|}
DECL|method|setPartNamespace (QName partNamespace)
specifier|public
name|void
name|setPartNamespace
parameter_list|(
name|QName
name|partNamespace
parameter_list|)
block|{
name|this
operator|.
name|partNamespace
operator|=
name|partNamespace
expr_stmt|;
block|}
DECL|method|getPartClass ()
specifier|public
name|String
name|getPartClass
parameter_list|()
block|{
return|return
name|partClass
return|;
block|}
DECL|method|setPartClass (String partClass)
specifier|public
name|void
name|setPartClass
parameter_list|(
name|String
name|partClass
parameter_list|)
block|{
name|this
operator|.
name|partClass
operator|=
name|partClass
expr_stmt|;
block|}
DECL|method|getNamespacePrefix ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getNamespacePrefix
parameter_list|()
block|{
return|return
name|namespacePrefix
return|;
block|}
DECL|method|setNamespacePrefix (Map<String, String> namespacePrefix)
specifier|public
name|void
name|setNamespacePrefix
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespacePrefix
parameter_list|)
block|{
name|this
operator|.
name|namespacePrefix
operator|=
name|namespacePrefix
expr_stmt|;
block|}
DECL|method|getNamespacePrefixRef ()
specifier|public
name|String
name|getNamespacePrefixRef
parameter_list|()
block|{
return|return
name|namespacePrefixRef
return|;
block|}
DECL|method|setNamespacePrefixRef (String namespacePrefixRef)
specifier|public
name|void
name|setNamespacePrefixRef
parameter_list|(
name|String
name|namespacePrefixRef
parameter_list|)
block|{
name|this
operator|.
name|namespacePrefixRef
operator|=
name|namespacePrefixRef
expr_stmt|;
block|}
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
DECL|method|getXmlStreamWriterWrapper ()
specifier|public
name|JaxbXmlStreamWriterWrapper
name|getXmlStreamWriterWrapper
parameter_list|()
block|{
return|return
name|xmlStreamWriterWrapper
return|;
block|}
DECL|method|setXmlStreamWriterWrapper (JaxbXmlStreamWriterWrapper xmlStreamWriterWrapper)
specifier|public
name|void
name|setXmlStreamWriterWrapper
parameter_list|(
name|JaxbXmlStreamWriterWrapper
name|xmlStreamWriterWrapper
parameter_list|)
block|{
name|this
operator|.
name|xmlStreamWriterWrapper
operator|=
name|xmlStreamWriterWrapper
expr_stmt|;
block|}
DECL|method|getSchemaLocation ()
specifier|public
name|String
name|getSchemaLocation
parameter_list|()
block|{
return|return
name|schemaLocation
return|;
block|}
DECL|method|setSchemaLocation (String schemaLocation)
specifier|public
name|void
name|setSchemaLocation
parameter_list|(
name|String
name|schemaLocation
parameter_list|)
block|{
name|this
operator|.
name|schemaLocation
operator|=
name|schemaLocation
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"CamelContext"
argument_list|)
expr_stmt|;
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
comment|// if context not injected, create one and resolve partial class up front so they are ready to be used
name|context
operator|=
name|createContext
argument_list|()
expr_stmt|;
block|}
name|introspector
operator|=
name|context
operator|.
name|createJAXBIntrospector
argument_list|()
expr_stmt|;
if|if
condition|(
name|partClass
operator|!=
literal|null
condition|)
block|{
name|partialClass
operator|=
name|camelContext
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveMandatoryClass
argument_list|(
name|partClass
argument_list|,
name|Object
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|namespacePrefixRef
operator|!=
literal|null
condition|)
block|{
name|namespacePrefix
operator|=
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|camelContext
argument_list|,
name|namespacePrefixRef
argument_list|,
name|Map
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|namespacePrefix
operator|!=
literal|null
condition|)
block|{
name|namespacePrefixMapper
operator|=
name|NamespacePrefixMapperFactory
operator|.
name|newNamespacePrefixMapper
argument_list|(
name|camelContext
argument_list|,
name|namespacePrefix
argument_list|)
expr_stmt|;
block|}
name|typeConverter
operator|=
name|camelContext
operator|.
name|getTypeConverter
argument_list|()
expr_stmt|;
if|if
condition|(
name|schema
operator|!=
literal|null
condition|)
block|{
name|cachedSchema
operator|=
name|createSchema
argument_list|(
name|getSources
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
block|{     }
comment|/**      * Strategy to create JAXB context      */
DECL|method|createContext ()
specifier|protected
name|JAXBContext
name|createContext
parameter_list|()
throws|throws
name|JAXBException
block|{
if|if
condition|(
name|contextPath
operator|!=
literal|null
condition|)
block|{
comment|// prefer to use application class loader which is most likely to be able to
comment|// load the the class which has been JAXB annotated
name|ClassLoader
name|cl
init|=
name|camelContext
operator|.
name|getApplicationContextClassLoader
argument_list|()
decl_stmt|;
if|if
condition|(
name|cl
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Creating JAXBContext with contextPath: "
operator|+
name|contextPath
operator|+
literal|" and ApplicationContextClassLoader: "
operator|+
name|cl
argument_list|)
expr_stmt|;
return|return
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|contextPath
argument_list|,
name|cl
argument_list|)
return|;
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Creating JAXBContext with contextPath: "
operator|+
name|contextPath
argument_list|)
expr_stmt|;
return|return
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|contextPath
argument_list|)
return|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Creating JAXBContext"
argument_list|)
expr_stmt|;
return|return
name|JAXBContext
operator|.
name|newInstance
argument_list|()
return|;
block|}
block|}
DECL|method|createUnmarshaller ()
specifier|protected
name|Unmarshaller
name|createUnmarshaller
parameter_list|()
throws|throws
name|JAXBException
throws|,
name|SAXException
throws|,
name|FileNotFoundException
throws|,
name|MalformedURLException
block|{
name|Unmarshaller
name|unmarshaller
init|=
name|getContext
argument_list|()
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
if|if
condition|(
name|schema
operator|!=
literal|null
condition|)
block|{
name|unmarshaller
operator|.
name|setSchema
argument_list|(
name|cachedSchema
argument_list|)
expr_stmt|;
name|unmarshaller
operator|.
name|setEventHandler
argument_list|(
operator|new
name|ValidationEventHandler
argument_list|()
block|{
specifier|public
name|boolean
name|handleEvent
parameter_list|(
name|ValidationEvent
name|event
parameter_list|)
block|{
comment|// stop unmarshalling if the event is an ERROR or FATAL
comment|// ERROR
return|return
name|event
operator|.
name|getSeverity
argument_list|()
operator|==
name|ValidationEvent
operator|.
name|WARNING
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
return|return
name|unmarshaller
return|;
block|}
DECL|method|createMarshaller ()
specifier|protected
name|Marshaller
name|createMarshaller
parameter_list|()
throws|throws
name|JAXBException
throws|,
name|SAXException
throws|,
name|FileNotFoundException
throws|,
name|MalformedURLException
block|{
name|Marshaller
name|marshaller
init|=
name|getContext
argument_list|()
operator|.
name|createMarshaller
argument_list|()
decl_stmt|;
if|if
condition|(
name|schema
operator|!=
literal|null
condition|)
block|{
name|marshaller
operator|.
name|setSchema
argument_list|(
name|cachedSchema
argument_list|)
expr_stmt|;
name|marshaller
operator|.
name|setEventHandler
argument_list|(
operator|new
name|ValidationEventHandler
argument_list|()
block|{
specifier|public
name|boolean
name|handleEvent
parameter_list|(
name|ValidationEvent
name|event
parameter_list|)
block|{
comment|// stop marshalling if the event is an ERROR or FATAL ERROR
return|return
name|event
operator|.
name|getSeverity
argument_list|()
operator|==
name|ValidationEvent
operator|.
name|WARNING
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
return|return
name|marshaller
return|;
block|}
DECL|method|createSchema (Source[] sources)
specifier|private
name|Schema
name|createSchema
parameter_list|(
name|Source
index|[]
name|sources
parameter_list|)
throws|throws
name|SAXException
block|{
name|SchemaFactory
name|factory
init|=
name|getOrCreateSchemaFactory
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|factory
operator|.
name|newSchema
argument_list|(
name|sources
argument_list|)
return|;
block|}
finally|finally
block|{
name|returnSchemaFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getSources ()
specifier|private
name|Source
index|[]
name|getSources
parameter_list|()
throws|throws
name|FileNotFoundException
throws|,
name|MalformedURLException
block|{
comment|// we support multiple schema by delimiting they by ','
name|String
index|[]
name|schemas
init|=
name|schema
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|Source
index|[]
name|sources
init|=
operator|new
name|Source
index|[
name|schemas
operator|.
name|length
index|]
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
name|schemas
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|URL
name|schemaUrl
init|=
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsUrl
argument_list|(
name|camelContext
operator|.
name|getClassResolver
argument_list|()
argument_list|,
name|schemas
index|[
name|i
index|]
argument_list|)
decl_stmt|;
name|sources
index|[
name|i
index|]
operator|=
operator|new
name|StreamSource
argument_list|(
name|schemaUrl
operator|.
name|toExternalForm
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|sources
return|;
block|}
DECL|method|getOrCreateSchemaFactory ()
specifier|private
name|SchemaFactory
name|getOrCreateSchemaFactory
parameter_list|()
block|{
name|SchemaFactory
name|factory
init|=
name|SCHEMA_FACTORY_POOL
operator|.
name|poll
argument_list|()
decl_stmt|;
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
name|factory
operator|=
name|createSchemaFactory
argument_list|()
expr_stmt|;
block|}
return|return
name|factory
return|;
block|}
DECL|method|createSchemaFactory ()
specifier|public
specifier|static
name|SchemaFactory
name|createSchemaFactory
parameter_list|()
block|{
return|return
name|SchemaFactory
operator|.
name|newInstance
argument_list|(
name|XMLConstants
operator|.
name|W3C_XML_SCHEMA_NS_URI
argument_list|)
return|;
block|}
DECL|method|returnSchemaFactory (SchemaFactory factory)
specifier|private
name|void
name|returnSchemaFactory
parameter_list|(
name|SchemaFactory
name|factory
parameter_list|)
block|{
if|if
condition|(
name|factory
operator|!=
name|schemaFactory
condition|)
block|{
name|SCHEMA_FACTORY_POOL
operator|.
name|offer
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

