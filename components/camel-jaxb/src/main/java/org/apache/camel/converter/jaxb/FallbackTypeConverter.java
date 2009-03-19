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
name|bind
operator|.
name|util
operator|.
name|JAXBSource
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
name|RuntimeCamelException
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
name|commons
operator|.
name|logging
operator|.
name|Log
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
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|FallbackTypeConverter
specifier|public
class|class
name|FallbackTypeConverter
implements|implements
name|TypeConverter
implements|,
name|TypeConverterAware
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Log
name|LOG
init|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|FallbackTypeConverter
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|contexts
specifier|private
name|Map
argument_list|<
name|Class
argument_list|,
name|JAXBContext
argument_list|>
name|contexts
init|=
operator|new
name|HashMap
argument_list|<
name|Class
argument_list|,
name|JAXBContext
argument_list|>
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
name|value
argument_list|)
return|;
block|}
block|}
return|return
literal|null
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
name|RuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|isNotStreamCacheType (Class<T> type)
specifier|private
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
return|return
name|convertTo
argument_list|(
name|type
argument_list|,
name|value
argument_list|)
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
block|{
name|T
name|answer
init|=
name|convertTo
argument_list|(
name|type
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
block|{
return|return
name|mandatoryConvertTo
argument_list|(
name|type
argument_list|,
name|value
argument_list|)
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
name|XmlRootElement
name|element
init|=
name|type
operator|.
name|getAnnotation
argument_list|(
name|XmlRootElement
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|element
operator|!=
literal|null
return|;
block|}
comment|/**      * Lets try parse via JAXB      */
DECL|method|unmarshall (Class<T> type, Object value)
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
name|Object
name|value
parameter_list|)
throws|throws
name|JAXBException
block|{
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
name|JAXBContext
name|context
init|=
name|createContext
argument_list|(
name|type
argument_list|)
decl_stmt|;
comment|// must create a new instance of unmarshaller as its not thred safe
name|Unmarshaller
name|unmarshaller
init|=
name|context
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
if|if
condition|(
name|parentTypeConverter
operator|!=
literal|null
condition|)
block|{
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
name|inputStream
argument_list|)
decl_stmt|;
return|return
name|type
operator|.
name|cast
argument_list|(
name|unmarshalled
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
name|reader
argument_list|)
decl_stmt|;
return|return
name|type
operator|.
name|cast
argument_list|(
name|unmarshalled
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
name|source
argument_list|)
decl_stmt|;
return|return
name|type
operator|.
name|cast
argument_list|(
name|unmarshalled
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
name|value
argument_list|)
decl_stmt|;
return|return
name|type
operator|.
name|cast
argument_list|(
name|unmarshalled
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
DECL|method|marshall (Class<T> type, Object value)
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
name|Object
name|value
parameter_list|)
throws|throws
name|JAXBException
block|{
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
name|JAXBSource
name|source
init|=
operator|new
name|JAXBSource
argument_list|(
name|context
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|answer
operator|=
name|parentTypeConverter
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|source
argument_list|)
expr_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
comment|// lets try a stream
name|StringWriter
name|buffer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
comment|// must create a new instance of marshaller as its not thred safe
name|Marshaller
name|marshaller
init|=
name|context
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
name|isPrettyPrint
argument_list|()
condition|?
name|Boolean
operator|.
name|TRUE
else|:
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
name|marshaller
operator|.
name|marshal
argument_list|(
name|value
argument_list|,
name|buffer
argument_list|)
expr_stmt|;
name|answer
operator|=
name|parentTypeConverter
operator|.
name|convertTo
argument_list|(
name|type
argument_list|,
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Unmarshals the given value with the unmarshaller      *      * @param unmarshaller  the unmarshaller      * @param value  the stream to unmarshal (will close it after use, also if exception is thrown)      * @return  the value      * @throws JAXBException is thrown if an exception occur while unmarshalling      */
DECL|method|unmarshal (Unmarshaller unmarshaller, Object value)
specifier|protected
name|Object
name|unmarshal
parameter_list|(
name|Unmarshaller
name|unmarshaller
parameter_list|,
name|Object
name|value
parameter_list|)
throws|throws
name|JAXBException
block|{
try|try
block|{
if|if
condition|(
name|value
operator|instanceof
name|InputStream
condition|)
block|{
return|return
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
operator|(
name|InputStream
operator|)
name|value
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Reader
condition|)
block|{
return|return
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
operator|(
name|Reader
operator|)
name|value
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Source
condition|)
block|{
return|return
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
operator|(
name|Source
operator|)
name|value
argument_list|)
return|;
block|}
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
name|ObjectHelper
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
return|return
literal|null
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
name|JAXBContext
name|context
init|=
name|contexts
operator|.
name|get
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|context
operator|==
literal|null
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
return|return
name|context
return|;
block|}
block|}
end_class

end_unit

