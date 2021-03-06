begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|File
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
name|Expression
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
name|Message
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
name|WrappedFile
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
name|StreamFactory
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
comment|/**  * You can use {@link BeanIOSplitter} with the Camel Splitter EIP to split big payloads  * using a stream mode to avoid reading the entire content into memory.  */
end_comment

begin_class
DECL|class|BeanIOSplitter
specifier|public
class|class
name|BeanIOSplitter
implements|implements
name|Expression
block|{
DECL|field|configuration
specifier|private
name|BeanIOConfiguration
name|configuration
init|=
operator|new
name|BeanIOConfiguration
argument_list|()
decl_stmt|;
DECL|field|factory
specifier|private
name|StreamFactory
name|factory
decl_stmt|;
DECL|method|BeanIOSplitter ()
specifier|public
name|BeanIOSplitter
parameter_list|()
throws|throws
name|Exception
block|{     }
DECL|method|BeanIOSplitter (BeanIOConfiguration configuration)
specifier|public
name|BeanIOSplitter
parameter_list|(
name|BeanIOConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|BeanIOSplitter (String mapping, String streamName)
specifier|public
name|BeanIOSplitter
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
DECL|method|createStreamFactory (CamelContext camelContext)
specifier|protected
name|StreamFactory
name|createStreamFactory
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
throws|throws
name|Exception
block|{
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
comment|// Create the stream factory that will be used to read/write objects.
name|StreamFactory
name|answer
init|=
name|StreamFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
comment|// Load the mapping file using the resource helper to ensure it can be loaded in OSGi and other environments
name|InputStream
name|is
init|=
name|ResourceHelper
operator|.
name|resolveMandatoryResourceAsInputStream
argument_list|(
name|camelContext
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
name|answer
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
name|answer
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
return|return
name|answer
return|;
block|}
DECL|method|evaluate (Exchange exchange)
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Message
name|msg
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|Object
name|body
init|=
name|msg
operator|.
name|getBody
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
name|createStreamFactory
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|BeanReader
name|beanReader
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|body
operator|instanceof
name|WrappedFile
condition|)
block|{
name|body
operator|=
operator|(
operator|(
name|WrappedFile
operator|)
name|body
operator|)
operator|.
name|getFile
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|body
operator|instanceof
name|File
condition|)
block|{
name|File
name|file
init|=
operator|(
name|File
operator|)
name|body
decl_stmt|;
name|beanReader
operator|=
name|factory
operator|.
name|createReader
argument_list|(
name|getStreamName
argument_list|()
argument_list|,
name|file
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|beanReader
operator|==
literal|null
condition|)
block|{
name|Reader
name|reader
init|=
name|msg
operator|.
name|getMandatoryBody
argument_list|(
name|Reader
operator|.
name|class
argument_list|)
decl_stmt|;
name|beanReader
operator|=
name|factory
operator|.
name|createReader
argument_list|(
name|getStreamName
argument_list|()
argument_list|,
name|reader
argument_list|)
expr_stmt|;
block|}
name|BeanIOIterator
name|iterator
init|=
operator|new
name|BeanIOIterator
argument_list|(
name|beanReader
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
literal|null
argument_list|,
name|iterator
argument_list|)
decl_stmt|;
name|beanReader
operator|.
name|setErrorHandler
argument_list|(
name|errorHandler
argument_list|)
expr_stmt|;
return|return
name|iterator
return|;
block|}
annotation|@
name|Override
DECL|method|evaluate (Exchange exchange, Class<T> type)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
try|try
block|{
name|Object
name|result
init|=
name|evaluate
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
return|return
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
name|type
argument_list|,
name|exchange
argument_list|,
name|result
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|getConfiguration ()
specifier|public
name|BeanIOConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (BeanIOConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|BeanIOConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
DECL|method|getFactory ()
specifier|public
name|StreamFactory
name|getFactory
parameter_list|()
block|{
return|return
name|factory
return|;
block|}
DECL|method|setFactory (StreamFactory factory)
specifier|public
name|void
name|setFactory
parameter_list|(
name|StreamFactory
name|factory
parameter_list|)
block|{
name|this
operator|.
name|factory
operator|=
name|factory
expr_stmt|;
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
DECL|method|setEncoding (Charset encoding)
specifier|public
name|void
name|setEncoding
parameter_list|(
name|Charset
name|encoding
parameter_list|)
block|{
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
block|}
end_class

end_unit

