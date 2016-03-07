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
name|Closeable
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
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
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
name|UnsupportedEncodingException
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
name|lang
operator|.
name|reflect
operator|.
name|AnnotatedElement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|annotation
operator|.
name|XmlRootElement
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
name|FactoryConfigurationError
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
name|Processor
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
name|StreamCache
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
name|component
operator|.
name|bean
operator|.
name|BeanInvocation
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
name|StaxConverter
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
name|TypeConverterAware
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
name|IOHelper
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
comment|/**  * @version  */
end_comment

begin_class
DECL|class|FallbackTypeConverter
specifier|public
class|class
name|FallbackTypeConverter
extends|extends
name|ServiceSupport
implements|implements
name|TypeConverter
implements|,
name|TypeConverterAware
implements|,
name|CamelContextAware
block|{
DECL|field|PRETTY_PRINT
specifier|public
specifier|static
specifier|final
name|String
name|PRETTY_PRINT
init|=
literal|"CamelJaxbPrettyPrint"
decl_stmt|;
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
name|FallbackTypeConverter
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|contexts
specifier|private
specifier|final
name|Map
argument_list|<
name|AnnotatedElement
argument_list|,
name|JAXBContext
argument_list|>
name|contexts
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|staxConverter
specifier|private
specifier|final
name|StaxConverter
name|staxConverter
init|=
operator|new
name|StaxConverter
argument_list|()
decl_stmt|;
DECL|field|parentTypeConverter
specifier|private
name|TypeConverter
name|parentTypeConverter
decl_stmt|;
DECL|field|prettyPrint
specifier|private
name|boolean
name|prettyPrint
init|=
literal|true
decl_stmt|;
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
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
DECL|method|allowNull ()
specifier|public
name|boolean
name|allowNull
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
DECL|method|setTypeConverter (TypeConverter parentTypeConverter)
specifier|public
name|void
name|setTypeConverter
parameter_list|(
name|TypeConverter
name|parentTypeConverter
parameter_list|)
block|{
name|this
operator|.
name|parentTypeConverter
operator|=
name|parentTypeConverter
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
DECL|method|convertTo (Class<T> type, Object value)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|convertTo
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
return|return
name|convertTo
argument_list|(
name|type
argument_list|,
literal|null
argument_list|,
name|value
argument_list|)
return|;
block|}
DECL|method|convertTo (Class<T> type, Exchange exchange, Object value)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|convertTo
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|BeanInvocation
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
operator|||
name|Processor
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
comment|// JAXB cannot convert to a BeanInvocation / Processor, so we need to indicate this
comment|// to avoid Camel trying to do this when using beans with JAXB payloads
return|return
literal|null
return|;
block|}
try|try
block|{
if|if
condition|(
name|isJaxbType
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
name|unmarshall
argument_list|(
name|type
argument_list|,
name|exchange
argument_list|,
name|value
argument_list|)
return|;
block|}
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|isJaxbType
argument_list|(
name|value
operator|.
name|getClass
argument_list|()
argument_list|)
operator|&&
name|isNotStreamCacheType
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
name|marshall
argument_list|(
name|type
argument_list|,
name|exchange
argument_list|,
name|value
argument_list|)
return|;
block|}
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
name|value
argument_list|,
name|type
argument_list|,
name|e
argument_list|)
throw|;
block|}
comment|// should return null if didn't even try to convert at all or for whatever reason the conversion is failed
return|return
literal|null
return|;
block|}
DECL|method|mandatoryConvertTo (Class<T> type, Object value)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|mandatoryConvertTo
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Object
name|value
parameter_list|)
throws|throws
name|NoTypeConversionAvailableException
block|{
return|return
name|mandatoryConvertTo
argument_list|(
name|type
argument_list|,
literal|null
argument_list|,
name|value
argument_list|)
return|;
block|}
DECL|method|mandatoryConvertTo (Class<T> type, Exchange exchange, Object value)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|mandatoryConvertTo
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Object
name|value
parameter_list|)
throws|throws
name|NoTypeConversionAvailableException
block|{
name|T
name|answer
init|=
name|convertTo
argument_list|(
name|type
argument_list|,
name|exchange
argument_list|,
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoTypeConversionAvailableException
argument_list|(
name|value
argument_list|,
name|type
argument_list|)
throw|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|tryConvertTo (Class<T> type, Object value)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|tryConvertTo
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
try|try
block|{
return|return
name|convertTo
argument_list|(
name|type
argument_list|,
literal|null
argument_list|,
name|value
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
DECL|method|tryConvertTo (Class<T> type, Exchange exchange, Object value)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|tryConvertTo
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
try|try
block|{
return|return
name|convertTo
argument_list|(
name|type
argument_list|,
name|exchange
argument_list|,
name|value
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
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
name|contexts
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|hasXmlRootElement (Class<T> type)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|boolean
name|hasXmlRootElement
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|type
operator|.
name|getAnnotation
argument_list|(
name|XmlRootElement
operator|.
name|class
argument_list|)
operator|!=
literal|null
return|;
block|}
DECL|method|isJaxbType (Class<T> type)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|boolean
name|isJaxbType
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|hasXmlRootElement
argument_list|(
name|type
argument_list|)
operator|||
name|JaxbHelper
operator|.
name|getJaxbElementFactoryMethod
argument_list|(
name|camelContext
argument_list|,
name|type
argument_list|)
operator|!=
literal|null
return|;
block|}
DECL|method|castJaxbType (Object o, Class<T> type)
specifier|private
parameter_list|<
name|T
parameter_list|>
name|T
name|castJaxbType
parameter_list|(
name|Object
name|o
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
if|if
condition|(
name|type
operator|.
name|isAssignableFrom
argument_list|(
name|o
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|type
operator|.
name|cast
argument_list|(
name|o
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|type
operator|.
name|cast
argument_list|(
operator|(
operator|(
name|JAXBElement
operator|)
name|o
operator|)
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
block|}
comment|/**      * Lets try parse via JAXB      */
DECL|method|unmarshall (Class<T> type, Exchange exchange, Object value)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|unmarshall
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Object
name|value
parameter_list|)
throws|throws
name|Exception
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Unmarshal to {} with value {}"
argument_list|,
name|type
argument_list|,
name|value
argument_list|)
expr_stmt|;
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot convert from null value to JAXBSource"
argument_list|)
throw|;
block|}
name|Unmarshaller
name|unmarshaller
init|=
name|getUnmarshaller
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|parentTypeConverter
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|needFiltering
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
comment|// we cannot filter the XMLStreamReader if necessary
name|XMLStreamReader
name|xmlReader
init|=
name|parentTypeConverter
operator|.
name|convertTo
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|xmlReader
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|Object
name|unmarshalled
init|=
name|unmarshal
argument_list|(
name|unmarshaller
argument_list|,
name|exchange
argument_list|,
name|xmlReader
argument_list|)
decl_stmt|;
return|return
name|castJaxbType
argument_list|(
name|unmarshalled
argument_list|,
name|type
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// There is some issue on the StaxStreamReader to CXFPayload message body with different namespaces
name|LOG
operator|.
name|debug
argument_list|(
literal|"Cannot use StaxStreamReader to unmarshal the message, due to {}"
argument_list|,
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|InputStream
name|inputStream
init|=
name|parentTypeConverter
operator|.
name|convertTo
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|inputStream
operator|!=
literal|null
condition|)
block|{
name|Object
name|unmarshalled
init|=
name|unmarshal
argument_list|(
name|unmarshaller
argument_list|,
name|exchange
argument_list|,
name|inputStream
argument_list|)
decl_stmt|;
return|return
name|castJaxbType
argument_list|(
name|unmarshalled
argument_list|,
name|type
argument_list|)
return|;
block|}
name|Reader
name|reader
init|=
name|parentTypeConverter
operator|.
name|convertTo
argument_list|(
name|Reader
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|reader
operator|!=
literal|null
condition|)
block|{
name|Object
name|unmarshalled
init|=
name|unmarshal
argument_list|(
name|unmarshaller
argument_list|,
name|exchange
argument_list|,
name|reader
argument_list|)
decl_stmt|;
return|return
name|castJaxbType
argument_list|(
name|unmarshalled
argument_list|,
name|type
argument_list|)
return|;
block|}
name|Source
name|source
init|=
name|parentTypeConverter
operator|.
name|convertTo
argument_list|(
name|Source
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|value
argument_list|)
decl_stmt|;
if|if
condition|(
name|source
operator|!=
literal|null
condition|)
block|{
name|Object
name|unmarshalled
init|=
name|unmarshal
argument_list|(
name|unmarshaller
argument_list|,
name|exchange
argument_list|,
name|source
argument_list|)
decl_stmt|;
return|return
name|castJaxbType
argument_list|(
name|unmarshalled
argument_list|,
name|type
argument_list|)
return|;
block|}
block|}
if|if
condition|(
name|value
operator|instanceof
name|String
condition|)
block|{
name|value
operator|=
operator|new
name|StringReader
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|value
operator|instanceof
name|InputStream
operator|||
name|value
operator|instanceof
name|Reader
condition|)
block|{
name|Object
name|unmarshalled
init|=
name|unmarshal
argument_list|(
name|unmarshaller
argument_list|,
name|exchange
argument_list|,
name|value
argument_list|)
decl_stmt|;
return|return
name|castJaxbType
argument_list|(
name|unmarshalled
argument_list|,
name|type
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|marshall (Class<T> type, Exchange exchange, Object value)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|marshall
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Object
name|value
parameter_list|)
throws|throws
name|JAXBException
throws|,
name|XMLStreamException
throws|,
name|FactoryConfigurationError
throws|,
name|TypeConversionException
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Marshal from value {} to type {}"
argument_list|,
name|value
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|T
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|parentTypeConverter
operator|!=
literal|null
condition|)
block|{
comment|// lets convert the object to a JAXB source and try convert that to
comment|// the required source
name|JAXBContext
name|context
init|=
name|createContext
argument_list|(
name|value
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
comment|// must create a new instance of marshaller as its not thread safe
name|Marshaller
name|marshaller
init|=
name|context
operator|.
name|createMarshaller
argument_list|()
decl_stmt|;
name|Writer
name|buffer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|boolean
name|prettyPrint
init|=
name|isPrettyPrint
argument_list|()
decl_stmt|;
comment|// check the camel context property to decide the value of PrettyPrint
if|if
condition|(
name|exchange
operator|!=
literal|null
condition|)
block|{
name|String
name|property
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getProperty
argument_list|(
name|PRETTY_PRINT
argument_list|)
decl_stmt|;
if|if
condition|(
name|property
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|property
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"false"
argument_list|)
condition|)
block|{
name|prettyPrint
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|prettyPrint
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|prettyPrint
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
if|if
condition|(
name|exchange
operator|!=
literal|null
operator|&&
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
argument_list|)
expr_stmt|;
block|}
name|Object
name|toMarshall
init|=
name|value
decl_stmt|;
if|if
condition|(
operator|!
name|hasXmlRootElement
argument_list|(
name|value
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|Method
name|m
init|=
name|JaxbHelper
operator|.
name|getJaxbElementFactoryMethod
argument_list|(
name|camelContext
argument_list|,
name|value
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|toMarshall
operator|=
name|m
operator|.
name|invoke
argument_list|(
name|JaxbHelper
operator|.
name|getObjectFactory
argument_list|(
name|camelContext
argument_list|,
name|value
operator|.
name|getClass
argument_list|()
argument_list|)
operator|.
name|newInstance
argument_list|()
argument_list|,
name|value
argument_list|)
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
name|error
argument_list|(
literal|"Unable to create JAXBElement object for type {} due to {}"
argument_list|,
name|value
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|needFiltering
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
name|XMLStreamWriter
name|writer
init|=
name|parentTypeConverter
operator|.
name|convertTo
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|,
name|buffer
argument_list|)
decl_stmt|;
name|FilteringXmlStreamWriter
name|filteringWriter
init|=
operator|new
name|FilteringXmlStreamWriter
argument_list|(
name|writer
argument_list|)
decl_stmt|;
name|marshaller
operator|.
name|marshal
argument_list|(
name|toMarshall
argument_list|,
name|filteringWriter
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|marshaller
operator|.
name|marshal
argument_list|(
name|toMarshall
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
block|}
comment|// we need to pass the exchange
name|answer
operator|=
name|parentTypeConverter
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|exchange
argument_list|,
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|unmarshal (Unmarshaller unmarshaller, Exchange exchange, Object value)
specifier|protected
name|Object
name|unmarshal
parameter_list|(
name|Unmarshaller
name|unmarshaller
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Object
name|value
parameter_list|)
throws|throws
name|JAXBException
throws|,
name|UnsupportedEncodingException
throws|,
name|XMLStreamException
block|{
try|try
block|{
name|XMLStreamReader
name|xmlReader
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|XMLStreamReader
condition|)
block|{
name|xmlReader
operator|=
operator|(
name|XMLStreamReader
operator|)
name|value
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|InputStream
condition|)
block|{
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
name|staxConverter
operator|.
name|createXMLStreamReader
argument_list|(
operator|new
name|NonXmlFilterReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
operator|(
name|InputStream
operator|)
name|value
argument_list|,
name|IOHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|xmlReader
operator|=
name|staxConverter
operator|.
name|createXMLStreamReader
argument_list|(
operator|(
name|InputStream
operator|)
name|value
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Reader
condition|)
block|{
name|Reader
name|reader
init|=
operator|(
name|Reader
operator|)
name|value
decl_stmt|;
if|if
condition|(
name|needFiltering
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
operator|(
name|value
operator|instanceof
name|NonXmlFilterReader
operator|)
condition|)
block|{
name|reader
operator|=
operator|new
name|NonXmlFilterReader
argument_list|(
operator|(
name|Reader
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
block|}
name|xmlReader
operator|=
name|staxConverter
operator|.
name|createXMLStreamReader
argument_list|(
name|reader
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Source
condition|)
block|{
name|xmlReader
operator|=
name|staxConverter
operator|.
name|createXMLStreamReader
argument_list|(
operator|(
name|Source
operator|)
name|value
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot convert from "
operator|+
name|value
operator|.
name|getClass
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
name|xmlReader
argument_list|)
return|;
block|}
finally|finally
block|{
if|if
condition|(
name|value
operator|instanceof
name|Closeable
condition|)
block|{
name|IOHelper
operator|.
name|close
argument_list|(
operator|(
name|Closeable
operator|)
name|value
argument_list|,
literal|"Unmarshalling"
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
block|}
block|}
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
operator|!=
literal|null
operator|&&
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|FILTER_NON_XML_CHARS
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|createContext (Class<T> type)
specifier|protected
specifier|synchronized
parameter_list|<
name|T
parameter_list|>
name|JAXBContext
name|createContext
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
throws|throws
name|JAXBException
block|{
name|AnnotatedElement
name|ae
init|=
name|hasXmlRootElement
argument_list|(
name|type
argument_list|)
condition|?
name|type
else|:
name|type
operator|.
name|getPackage
argument_list|()
decl_stmt|;
name|JAXBContext
name|context
init|=
name|contexts
operator|.
name|get
argument_list|(
name|ae
argument_list|)
decl_stmt|;
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|hasXmlRootElement
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|context
operator|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|contexts
operator|.
name|put
argument_list|(
name|type
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|context
operator|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|type
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|contexts
operator|.
name|put
argument_list|(
name|type
operator|.
name|getPackage
argument_list|()
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|context
return|;
block|}
DECL|method|getUnmarshaller (Class<T> type)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|Unmarshaller
name|getUnmarshaller
parameter_list|(
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
name|context
init|=
name|createContext
argument_list|(
name|type
argument_list|)
decl_stmt|;
return|return
name|context
operator|.
name|createUnmarshaller
argument_list|()
return|;
block|}
DECL|method|isNotStreamCacheType (Class<T> type)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|boolean
name|isNotStreamCacheType
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
operator|!
name|StreamCache
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
return|;
block|}
block|}
end_class

end_unit

