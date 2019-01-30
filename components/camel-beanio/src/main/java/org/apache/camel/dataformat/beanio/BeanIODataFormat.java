begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.beanio
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|beanio
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedWriter
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
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|Properties
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
name|support
operator|.
name|ResourceHelper
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
name|IOHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|beanio
operator|.
name|BeanReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|beanio
operator|.
name|BeanReaderErrorHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|beanio
operator|.
name|BeanWriter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|beanio
operator|.
name|StreamFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|beanio
operator|.
name|Unmarshaller
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
name|dataformat
operator|.
name|beanio
operator|.
name|BeanIOHelper
operator|.
name|getOrCreateBeanReaderErrorHandler
import|;
end_import

begin_comment
comment|/**  * A<a href="http://camel.apache.org/data-format.html">data format</a> (  * {@link DataFormat}) for beanio data.  */
end_comment

begin_class
annotation|@
name|Dataformat
argument_list|(
literal|"beanio"
argument_list|)
DECL|class|BeanIODataFormat
specifier|public
class|class
name|BeanIODataFormat
extends|extends
name|ServiceSupport
implements|implements
name|DataFormat
implements|,
name|DataFormatName
implements|,
name|CamelContextAware
block|{
DECL|field|camelContext
specifier|private
specifier|transient
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|factory
specifier|private
specifier|transient
name|StreamFactory
name|factory
decl_stmt|;
DECL|field|configuration
specifier|private
name|BeanIOConfiguration
name|configuration
init|=
operator|new
name|BeanIOConfiguration
argument_list|()
decl_stmt|;
DECL|method|BeanIODataFormat ()
specifier|public
name|BeanIODataFormat
parameter_list|()
block|{     }
DECL|method|BeanIODataFormat (String mapping, String streamName)
specifier|public
name|BeanIODataFormat
parameter_list|(
name|String
name|mapping
parameter_list|,
name|String
name|streamName
parameter_list|)
block|{
name|setMapping
argument_list|(
name|mapping
argument_list|)
expr_stmt|;
name|setStreamName
argument_list|(
name|streamName
argument_list|)
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
literal|"beanio"
return|;
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|getStreamName
argument_list|()
argument_list|,
literal|"Stream name not configured."
argument_list|)
expr_stmt|;
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
comment|// Create the stream factory that will be used to read/write objects.
name|factory
operator|=
name|StreamFactory
operator|.
name|newInstance
argument_list|()
expr_stmt|;
comment|// Load the mapping file using the resource helper to ensure it can be loaded in OSGi and other environments
name|InputStream
name|is
init|=
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsInputStream
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|getMapping
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|getProperties
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|factory
operator|.
name|load
argument_list|(
name|is
argument_list|,
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|factory
operator|.
name|load
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
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
block|{
name|factory
operator|=
literal|null
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
DECL|method|getFactory ()
name|StreamFactory
name|getFactory
parameter_list|()
block|{
return|return
name|factory
return|;
block|}
DECL|method|marshal (Exchange exchange, Object body, OutputStream stream)
specifier|public
name|void
name|marshal
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|body
parameter_list|,
name|OutputStream
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|models
init|=
name|getModels
argument_list|(
name|exchange
argument_list|,
name|body
argument_list|)
decl_stmt|;
name|writeModels
argument_list|(
name|stream
argument_list|,
name|models
argument_list|)
expr_stmt|;
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
name|Exception
block|{
if|if
condition|(
name|isUnmarshalSingleObject
argument_list|()
condition|)
block|{
return|return
name|readSingleModel
argument_list|(
name|exchange
argument_list|,
name|stream
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|readModels
argument_list|(
name|exchange
argument_list|,
name|stream
argument_list|)
return|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|getModels (Exchange exchange, Object body)
specifier|private
name|List
argument_list|<
name|Object
argument_list|>
name|getModels
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|body
parameter_list|)
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|models
decl_stmt|;
if|if
condition|(
operator|(
name|models
operator|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
operator|.
name|convertTo
argument_list|(
name|List
operator|.
name|class
argument_list|,
name|body
argument_list|)
operator|)
operator|==
literal|null
condition|)
block|{
name|models
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
for|for
control|(
name|Object
name|model
range|:
name|ObjectHelper
operator|.
name|createIterable
argument_list|(
name|body
argument_list|)
control|)
block|{
name|models
operator|.
name|add
argument_list|(
name|model
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|models
return|;
block|}
DECL|method|writeModels (OutputStream stream, List<Object> models)
specifier|private
name|void
name|writeModels
parameter_list|(
name|OutputStream
name|stream
parameter_list|,
name|List
argument_list|<
name|Object
argument_list|>
name|models
parameter_list|)
block|{
name|BufferedWriter
name|streamWriter
init|=
name|IOHelper
operator|.
name|buffered
argument_list|(
operator|new
name|OutputStreamWriter
argument_list|(
name|stream
argument_list|,
name|getEncoding
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|BeanWriter
name|out
init|=
name|factory
operator|.
name|createWriter
argument_list|(
name|getStreamName
argument_list|()
argument_list|,
name|streamWriter
argument_list|)
decl_stmt|;
for|for
control|(
name|Object
name|obj
range|:
name|models
control|)
block|{
name|out
operator|.
name|write
argument_list|(
name|obj
argument_list|)
expr_stmt|;
block|}
name|out
operator|.
name|flush
argument_list|()
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
DECL|method|readModels (Exchange exchange, InputStream stream)
specifier|private
name|List
argument_list|<
name|Object
argument_list|>
name|readModels
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|stream
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|results
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|BufferedReader
name|streamReader
init|=
name|IOHelper
operator|.
name|buffered
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|stream
argument_list|,
name|getEncoding
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|BeanReader
name|in
init|=
name|factory
operator|.
name|createReader
argument_list|(
name|getStreamName
argument_list|()
argument_list|,
name|streamReader
argument_list|)
decl_stmt|;
name|BeanReaderErrorHandler
name|errorHandler
init|=
name|getOrCreateBeanReaderErrorHandler
argument_list|(
name|configuration
argument_list|,
name|exchange
argument_list|,
name|results
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|in
operator|.
name|setErrorHandler
argument_list|(
name|errorHandler
argument_list|)
expr_stmt|;
try|try
block|{
name|Object
name|readObject
decl_stmt|;
while|while
condition|(
operator|(
name|readObject
operator|=
name|in
operator|.
name|read
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|readObject
operator|instanceof
name|BeanIOHeader
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|getHeaders
argument_list|()
operator|.
name|putAll
argument_list|(
operator|(
operator|(
name|BeanIOHeader
operator|)
name|readObject
operator|)
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|results
operator|.
name|add
argument_list|(
name|readObject
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
name|results
return|;
block|}
DECL|method|readSingleModel (Exchange exchange, InputStream stream)
specifier|private
name|Object
name|readSingleModel
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|InputStream
name|stream
parameter_list|)
throws|throws
name|NoTypeConversionAvailableException
block|{
name|BufferedReader
name|streamReader
init|=
name|IOHelper
operator|.
name|buffered
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|stream
argument_list|,
name|getEncoding
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
try|try
block|{
name|String
name|data
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
name|String
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|streamReader
argument_list|)
decl_stmt|;
name|Unmarshaller
name|unmarshaller
init|=
name|factory
operator|.
name|createUnmarshaller
argument_list|(
name|getStreamName
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
name|data
argument_list|)
return|;
block|}
finally|finally
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|stream
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|getMapping ()
specifier|public
name|String
name|getMapping
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getMapping
argument_list|()
return|;
block|}
DECL|method|setIgnoreUnexpectedRecords (boolean ignoreUnexpectedRecords)
specifier|public
name|void
name|setIgnoreUnexpectedRecords
parameter_list|(
name|boolean
name|ignoreUnexpectedRecords
parameter_list|)
block|{
name|configuration
operator|.
name|setIgnoreUnexpectedRecords
argument_list|(
name|ignoreUnexpectedRecords
argument_list|)
expr_stmt|;
block|}
DECL|method|setProperties (Properties properties)
specifier|public
name|void
name|setProperties
parameter_list|(
name|Properties
name|properties
parameter_list|)
block|{
name|configuration
operator|.
name|setProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
DECL|method|setStreamName (String streamName)
specifier|public
name|void
name|setStreamName
parameter_list|(
name|String
name|streamName
parameter_list|)
block|{
name|configuration
operator|.
name|setStreamName
argument_list|(
name|streamName
argument_list|)
expr_stmt|;
block|}
DECL|method|isIgnoreUnidentifiedRecords ()
specifier|public
name|boolean
name|isIgnoreUnidentifiedRecords
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|isIgnoreUnidentifiedRecords
argument_list|()
return|;
block|}
DECL|method|isIgnoreInvalidRecords ()
specifier|public
name|boolean
name|isIgnoreInvalidRecords
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|isIgnoreInvalidRecords
argument_list|()
return|;
block|}
DECL|method|setIgnoreInvalidRecords (boolean ignoreInvalidRecords)
specifier|public
name|void
name|setIgnoreInvalidRecords
parameter_list|(
name|boolean
name|ignoreInvalidRecords
parameter_list|)
block|{
name|configuration
operator|.
name|setIgnoreInvalidRecords
argument_list|(
name|ignoreInvalidRecords
argument_list|)
expr_stmt|;
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
name|setEncoding
argument_list|(
name|Charset
operator|.
name|forName
argument_list|(
name|encoding
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|setEncoding (Charset encoding)
specifier|public
name|void
name|setEncoding
parameter_list|(
name|Charset
name|encoding
parameter_list|)
block|{
if|if
condition|(
name|encoding
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Charset encoding is null"
argument_list|)
throw|;
block|}
name|configuration
operator|.
name|setEncoding
argument_list|(
name|encoding
argument_list|)
expr_stmt|;
block|}
DECL|method|isIgnoreUnexpectedRecords ()
specifier|public
name|boolean
name|isIgnoreUnexpectedRecords
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|isIgnoreUnexpectedRecords
argument_list|()
return|;
block|}
DECL|method|getProperties ()
specifier|public
name|Properties
name|getProperties
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getProperties
argument_list|()
return|;
block|}
DECL|method|getStreamName ()
specifier|public
name|String
name|getStreamName
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getStreamName
argument_list|()
return|;
block|}
DECL|method|setMapping (String mapping)
specifier|public
name|void
name|setMapping
parameter_list|(
name|String
name|mapping
parameter_list|)
block|{
name|configuration
operator|.
name|setMapping
argument_list|(
name|mapping
argument_list|)
expr_stmt|;
block|}
DECL|method|setIgnoreUnidentifiedRecords (boolean ignoreUnidentifiedRecords)
specifier|public
name|void
name|setIgnoreUnidentifiedRecords
parameter_list|(
name|boolean
name|ignoreUnidentifiedRecords
parameter_list|)
block|{
name|configuration
operator|.
name|setIgnoreUnidentifiedRecords
argument_list|(
name|ignoreUnidentifiedRecords
argument_list|)
expr_stmt|;
block|}
DECL|method|getEncoding ()
specifier|public
name|Charset
name|getEncoding
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getEncoding
argument_list|()
return|;
block|}
DECL|method|getBeanReaderErrorHandler ()
specifier|public
name|BeanReaderErrorHandler
name|getBeanReaderErrorHandler
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getBeanReaderErrorHandler
argument_list|()
return|;
block|}
DECL|method|setBeanReaderErrorHandler (BeanReaderErrorHandler beanReaderErrorHandler)
specifier|public
name|void
name|setBeanReaderErrorHandler
parameter_list|(
name|BeanReaderErrorHandler
name|beanReaderErrorHandler
parameter_list|)
block|{
name|configuration
operator|.
name|setBeanReaderErrorHandler
argument_list|(
name|beanReaderErrorHandler
argument_list|)
expr_stmt|;
block|}
DECL|method|getBeanReaderErrorHandlerType ()
specifier|public
name|String
name|getBeanReaderErrorHandlerType
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|getBeanReaderErrorHandlerType
argument_list|()
return|;
block|}
DECL|method|setBeanReaderErrorHandlerType (String beanReaderErrorHandlerType)
specifier|public
name|void
name|setBeanReaderErrorHandlerType
parameter_list|(
name|String
name|beanReaderErrorHandlerType
parameter_list|)
block|{
name|configuration
operator|.
name|setBeanReaderErrorHandlerType
argument_list|(
name|beanReaderErrorHandlerType
argument_list|)
expr_stmt|;
block|}
DECL|method|setBeanReaderErrorHandlerType (Class<?> beanReaderErrorHandlerType)
specifier|public
name|void
name|setBeanReaderErrorHandlerType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|beanReaderErrorHandlerType
parameter_list|)
block|{
name|configuration
operator|.
name|setBeanReaderErrorHandlerType
argument_list|(
name|beanReaderErrorHandlerType
argument_list|)
expr_stmt|;
block|}
DECL|method|isUnmarshalSingleObject ()
specifier|public
name|boolean
name|isUnmarshalSingleObject
parameter_list|()
block|{
return|return
name|configuration
operator|.
name|isUnmarshalSingleObject
argument_list|()
return|;
block|}
DECL|method|setUnmarshalSingleObject (boolean unmarshalSingleObject)
specifier|public
name|void
name|setUnmarshalSingleObject
parameter_list|(
name|boolean
name|unmarshalSingleObject
parameter_list|)
block|{
name|configuration
operator|.
name|setUnmarshalSingleObject
argument_list|(
name|unmarshalSingleObject
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

