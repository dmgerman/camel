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
implements|implements
name|DataFormat
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
specifier|transient
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
DECL|field|factory
specifier|private
name|StreamFactory
name|factory
decl_stmt|;
DECL|field|streamName
specifier|private
name|String
name|streamName
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
DECL|field|characterSet
specifier|private
name|Charset
name|characterSet
init|=
name|Charset
operator|.
name|defaultCharset
argument_list|()
decl_stmt|;
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
comment|// Create the stream factory that will be used to read/write objects.
name|factory
operator|=
name|StreamFactory
operator|.
name|newInstance
argument_list|()
expr_stmt|;
comment|// Load the mapping file
name|factory
operator|.
name|loadResource
argument_list|(
name|mapping
argument_list|)
expr_stmt|;
comment|// Save the stream name that we want to read
name|this
operator|.
name|streamName
operator|=
name|streamName
expr_stmt|;
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
name|validateRequiredProperties
argument_list|()
expr_stmt|;
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
DECL|method|validateRequiredProperties ()
specifier|private
name|void
name|validateRequiredProperties
parameter_list|()
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|factory
argument_list|,
literal|"StreamFactory not configured."
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|streamName
argument_list|,
literal|"Stream name not configured."
argument_list|)
expr_stmt|;
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
operator|new
name|BufferedWriter
argument_list|(
operator|new
name|OutputStreamWriter
argument_list|(
name|stream
argument_list|,
name|characterSet
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
name|validateRequiredProperties
argument_list|()
expr_stmt|;
return|return
name|readModels
argument_list|(
name|exchange
argument_list|,
name|stream
argument_list|)
return|;
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
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|stream
argument_list|,
name|characterSet
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
name|LOG
operator|.
name|warn
argument_list|(
name|LOG_PREFIX
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
name|getContext
argument_list|()
operator|.
name|getRecordText
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|ignoreInvalidRecords
condition|)
block|{
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
name|LOG
operator|.
name|warn
argument_list|(
name|LOG_PREFIX
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
name|getContext
argument_list|()
operator|.
name|getRecordText
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|ignoreUnexpectedRecords
condition|)
block|{
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
name|LOG
operator|.
name|warn
argument_list|(
name|LOG_PREFIX
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
name|getContext
argument_list|()
operator|.
name|getRecordText
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|ignoreUnidentifiedRecords
condition|)
block|{
throw|throw
name|ex
throw|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
comment|/**      * @param streamName The beanio stream that will be marshaled/unmarshaled.      */
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
comment|/**      * @param ignoreUnidentifiedRecords When true any unidentified records will be ignored when      *                                  unmarshaling.      */
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
comment|/**      * @param ignoreUnexpectedRecords When true any unexpected records will be ignored when      *                                unmarshaling.      */
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
comment|/**      * @param ignoreInvalidRecords When true any invalid records will be ignored when      *                             unmarshaling.      */
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
comment|/**      * @param characterSet the characterSet to set      */
DECL|method|setCharacterSet (String characterSet)
specifier|public
name|void
name|setCharacterSet
parameter_list|(
name|String
name|characterSet
parameter_list|)
block|{
name|this
operator|.
name|characterSet
operator|=
name|Charset
operator|.
name|forName
argument_list|(
name|characterSet
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

