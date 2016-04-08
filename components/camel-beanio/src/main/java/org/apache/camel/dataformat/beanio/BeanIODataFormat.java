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
name|Iterator
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
name|BeanReaderErrorHandlerSupport
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
name|InvalidRecordException
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
name|UnexpectedRecordException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|beanio
operator|.
name|UnidentifiedRecordException
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
comment|/**  * A<a href="http://camel.apache.org/data-format.html">data format</a> (  * {@link DataFormat}) for beanio data.  */
end_comment

begin_class
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
DECL|field|LOG_PREFIX
specifier|private
specifier|static
specifier|final
name|String
name|LOG_PREFIX
init|=
literal|"BeanIO: "
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
name|BeanIODataFormat
operator|.
name|class
argument_list|)
decl_stmt|;
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
DECL|field|streamName
specifier|private
name|String
name|streamName
decl_stmt|;
DECL|field|mapping
specifier|private
name|String
name|mapping
decl_stmt|;
DECL|field|ignoreUnidentifiedRecords
specifier|private
name|boolean
name|ignoreUnidentifiedRecords
decl_stmt|;
DECL|field|ignoreUnexpectedRecords
specifier|private
name|boolean
name|ignoreUnexpectedRecords
decl_stmt|;
DECL|field|ignoreInvalidRecords
specifier|private
name|boolean
name|ignoreInvalidRecords
decl_stmt|;
DECL|field|encoding
specifier|private
name|Charset
name|encoding
init|=
name|Charset
operator|.
name|defaultCharset
argument_list|()
decl_stmt|;
DECL|field|properties
specifier|private
name|Properties
name|properties
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
name|this
operator|.
name|mapping
operator|=
name|mapping
expr_stmt|;
name|this
operator|.
name|streamName
operator|=
name|streamName
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
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|streamName
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
name|mapping
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|properties
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
name|properties
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
return|return
name|readModels
argument_list|(
name|exchange
argument_list|,
name|stream
argument_list|)
return|;
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
argument_list|<
name|Object
argument_list|>
argument_list|()
expr_stmt|;
name|Iterator
argument_list|<
name|Object
argument_list|>
name|it
init|=
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
name|body
argument_list|)
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|models
operator|.
name|add
argument_list|(
name|it
operator|.
name|next
argument_list|()
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
name|encoding
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
name|streamName
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
block|{
name|List
argument_list|<
name|Object
argument_list|>
name|results
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
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
name|encoding
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
name|streamName
argument_list|,
name|streamReader
argument_list|)
decl_stmt|;
try|try
block|{
name|registerErrorHandler
argument_list|(
name|in
argument_list|)
expr_stmt|;
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
DECL|method|registerErrorHandler (BeanReader in)
specifier|private
name|void
name|registerErrorHandler
parameter_list|(
name|BeanReader
name|in
parameter_list|)
block|{
name|in
operator|.
name|setErrorHandler
argument_list|(
operator|new
name|BeanReaderErrorHandlerSupport
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|invalidRecord
parameter_list|(
name|InvalidRecordException
name|ex
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|msg
init|=
name|LOG_PREFIX
operator|+
literal|"InvalidRecord: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
operator|+
literal|": "
operator|+
name|ex
operator|.
name|getRecordContext
argument_list|()
operator|.
name|getRecordText
argument_list|()
decl_stmt|;
if|if
condition|(
name|ignoreInvalidRecords
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
name|msg
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|unexpectedRecord
parameter_list|(
name|UnexpectedRecordException
name|ex
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|msg
init|=
name|LOG_PREFIX
operator|+
literal|"UnexpectedRecord: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
operator|+
literal|": "
operator|+
name|ex
operator|.
name|getRecordContext
argument_list|()
operator|.
name|getRecordText
argument_list|()
decl_stmt|;
if|if
condition|(
name|ignoreUnexpectedRecords
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
name|msg
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|unidentifiedRecord
parameter_list|(
name|UnidentifiedRecordException
name|ex
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|msg
init|=
name|LOG_PREFIX
operator|+
literal|"UnidentifiedRecord: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
operator|+
literal|": "
operator|+
name|ex
operator|.
name|getRecordContext
argument_list|()
operator|.
name|getRecordText
argument_list|()
decl_stmt|;
if|if
condition|(
name|ignoreUnidentifiedRecords
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|warn
argument_list|(
name|msg
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
block|}
block|}
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
name|encoding
return|;
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
name|this
operator|.
name|encoding
operator|=
name|encoding
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
name|this
operator|.
name|encoding
operator|=
name|Charset
operator|.
name|forName
argument_list|(
name|encoding
argument_list|)
expr_stmt|;
block|}
DECL|method|isIgnoreInvalidRecords ()
specifier|public
name|boolean
name|isIgnoreInvalidRecords
parameter_list|()
block|{
return|return
name|ignoreInvalidRecords
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
name|this
operator|.
name|ignoreInvalidRecords
operator|=
name|ignoreInvalidRecords
expr_stmt|;
block|}
DECL|method|isIgnoreUnexpectedRecords ()
specifier|public
name|boolean
name|isIgnoreUnexpectedRecords
parameter_list|()
block|{
return|return
name|ignoreUnexpectedRecords
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
name|this
operator|.
name|ignoreUnexpectedRecords
operator|=
name|ignoreUnexpectedRecords
expr_stmt|;
block|}
DECL|method|isIgnoreUnidentifiedRecords ()
specifier|public
name|boolean
name|isIgnoreUnidentifiedRecords
parameter_list|()
block|{
return|return
name|ignoreUnidentifiedRecords
return|;
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
name|this
operator|.
name|ignoreUnidentifiedRecords
operator|=
name|ignoreUnidentifiedRecords
expr_stmt|;
block|}
DECL|method|getMapping ()
specifier|public
name|String
name|getMapping
parameter_list|()
block|{
return|return
name|mapping
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
name|this
operator|.
name|mapping
operator|=
name|mapping
expr_stmt|;
block|}
DECL|method|getStreamName ()
specifier|public
name|String
name|getStreamName
parameter_list|()
block|{
return|return
name|streamName
return|;
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
name|this
operator|.
name|streamName
operator|=
name|streamName
expr_stmt|;
block|}
DECL|method|getProperties ()
specifier|public
name|Properties
name|getProperties
parameter_list|()
block|{
return|return
name|properties
return|;
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
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
block|}
end_class

end_unit

