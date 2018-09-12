begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy.csv
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
operator|.
name|bindy
operator|.
name|csv
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
name|Arrays
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
name|atomic
operator|.
name|AtomicInteger
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Consumer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Stream
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
name|dataformat
operator|.
name|bindy
operator|.
name|BindyAbstractDataFormat
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
name|dataformat
operator|.
name|bindy
operator|.
name|BindyAbstractFactory
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
name|dataformat
operator|.
name|bindy
operator|.
name|BindyCsvFactory
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
name|dataformat
operator|.
name|bindy
operator|.
name|FormatFactory
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
name|dataformat
operator|.
name|bindy
operator|.
name|WrappedException
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
name|dataformat
operator|.
name|bindy
operator|.
name|util
operator|.
name|ConverterUtils
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
comment|/**  * A<a href="http://camel.apache.org/data-format.html">data format</a> (  * {@link DataFormat}) using Bindy to marshal to and from CSV files  */
end_comment

begin_class
DECL|class|BindyCsvDataFormat
specifier|public
class|class
name|BindyCsvDataFormat
extends|extends
name|BindyAbstractDataFormat
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
name|BindyCsvDataFormat
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|BindyCsvDataFormat ()
specifier|public
name|BindyCsvDataFormat
parameter_list|()
block|{     }
DECL|method|BindyCsvDataFormat (Class<?> type)
specifier|public
name|BindyCsvDataFormat
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|super
argument_list|(
name|type
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
literal|"bindy-csv"
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|marshal (Exchange exchange, Object body, OutputStream outputStream)
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
name|outputStream
parameter_list|)
throws|throws
name|Exception
block|{
name|BindyCsvFactory
name|factory
init|=
operator|(
name|BindyCsvFactory
operator|)
name|getFactory
argument_list|()
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|factory
argument_list|,
literal|"not instantiated"
argument_list|)
expr_stmt|;
comment|// Get CRLF
name|byte
index|[]
name|bytesCRLF
init|=
name|ConverterUtils
operator|.
name|getByteReturn
argument_list|(
name|factory
operator|.
name|getCarriageReturn
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|factory
operator|.
name|getGenerateHeaderColumnNames
argument_list|()
condition|)
block|{
name|String
name|result
init|=
name|factory
operator|.
name|generateHeader
argument_list|()
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
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
name|byte
index|[]
operator|.
expr|class
argument_list|,
name|exchange
argument_list|,
name|result
argument_list|)
decl_stmt|;
name|outputStream
operator|.
name|write
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
comment|// Add a carriage return
name|outputStream
operator|.
name|write
argument_list|(
name|bytesCRLF
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|models
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
comment|// the body is not a prepared list of map that bindy expects so help a bit here and create one for us
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
name|Object
name|model
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|model
operator|instanceof
name|Map
condition|)
block|{
name|models
operator|.
name|add
argument_list|(
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
operator|)
name|model
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|name
init|=
name|model
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|row
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|row
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|model
argument_list|)
expr_stmt|;
name|row
operator|.
name|putAll
argument_list|(
name|createLinkedFieldsModel
argument_list|(
name|model
argument_list|)
argument_list|)
expr_stmt|;
name|models
operator|.
name|add
argument_list|(
name|row
argument_list|)
expr_stmt|;
block|}
block|}
name|Iterator
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|modelsMap
init|=
name|models
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|modelsMap
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|result
init|=
name|factory
operator|.
name|unbind
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|modelsMap
operator|.
name|next
argument_list|()
argument_list|)
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
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
name|byte
index|[]
operator|.
expr|class
argument_list|,
name|exchange
argument_list|,
name|result
argument_list|)
decl_stmt|;
name|outputStream
operator|.
name|write
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
if|if
condition|(
name|factory
operator|.
name|isEndWithLineBreak
argument_list|()
operator|||
name|modelsMap
operator|.
name|hasNext
argument_list|()
condition|)
block|{
comment|// Add a carriage return
name|outputStream
operator|.
name|write
argument_list|(
name|bytesCRLF
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * check emptyStream and if CVSRecord is allow to process emptyStreams      * avoid IllegalArgumentException and return empty list when unmarshalling      */
DECL|method|checkEmptyStream (BindyCsvFactory factory, InputStream inputStream)
specifier|private
name|boolean
name|checkEmptyStream
parameter_list|(
name|BindyCsvFactory
name|factory
parameter_list|,
name|InputStream
name|inputStream
parameter_list|)
throws|throws
name|IOException
block|{
name|boolean
name|allowEmptyStream
init|=
name|factory
operator|.
name|isAllowEmptyStream
argument_list|()
decl_stmt|;
name|boolean
name|isStreamEmpty
init|=
literal|false
decl_stmt|;
name|boolean
name|canReturnEmptyListOfModels
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|inputStream
operator|==
literal|null
operator|||
name|inputStream
operator|.
name|available
argument_list|()
operator|==
literal|0
condition|)
block|{
name|isStreamEmpty
operator|=
literal|true
expr_stmt|;
block|}
if|if
condition|(
name|isStreamEmpty
operator|&&
name|allowEmptyStream
condition|)
block|{
name|canReturnEmptyListOfModels
operator|=
literal|true
expr_stmt|;
block|}
return|return
name|canReturnEmptyListOfModels
return|;
block|}
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
name|BindyCsvFactory
name|factory
init|=
operator|(
name|BindyCsvFactory
operator|)
name|getFactory
argument_list|()
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|factory
argument_list|,
literal|"not instantiated"
argument_list|)
expr_stmt|;
comment|// List of Pojos
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|models
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|InputStreamReader
name|in
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|checkEmptyStream
argument_list|(
name|factory
argument_list|,
name|inputStream
argument_list|)
condition|)
block|{
return|return
name|models
return|;
block|}
name|in
operator|=
operator|new
name|InputStreamReader
argument_list|(
name|inputStream
argument_list|,
name|IOHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
comment|// Retrieve the separator defined to split the record
name|String
name|separator
init|=
name|factory
operator|.
name|getSeparator
argument_list|()
decl_stmt|;
name|String
name|quote
init|=
name|factory
operator|.
name|getQuote
argument_list|()
decl_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|separator
argument_list|,
literal|"The separator has not been defined in the annotation @CsvRecord or not instantiated during initModel."
argument_list|)
expr_stmt|;
name|AtomicInteger
name|count
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// Use a Stream to stream a file across.
try|try
init|(
name|Stream
argument_list|<
name|String
argument_list|>
name|lines
init|=
operator|new
name|BufferedReader
argument_list|(
name|in
argument_list|)
operator|.
name|lines
argument_list|()
init|)
block|{
name|int
name|linesToSkip
init|=
literal|0
decl_stmt|;
comment|// If the first line of the CSV file contains columns name, then we
comment|// skip this line
if|if
condition|(
name|factory
operator|.
name|getSkipFirstLine
argument_list|()
condition|)
block|{
name|linesToSkip
operator|=
literal|1
expr_stmt|;
block|}
comment|// Consume the lines in the file via a consumer method, passing in state as necessary.
comment|// If the internals of the consumer fail, we unrap the checked exception upstream.
try|try
block|{
name|lines
operator|.
name|skip
argument_list|(
name|linesToSkip
argument_list|)
operator|.
name|forEachOrdered
argument_list|(
name|consumeFile
argument_list|(
name|factory
argument_list|,
name|models
argument_list|,
name|separator
argument_list|,
name|quote
argument_list|,
name|count
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WrappedException
name|e
parameter_list|)
block|{
throw|throw
name|e
operator|.
name|getWrappedException
argument_list|()
throw|;
block|}
comment|// BigIntegerFormatFactory if models list is empty or not
comment|// If this is the case (correspond to an empty stream, ...)
if|if
condition|(
name|models
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
throw|throw
operator|new
name|java
operator|.
name|lang
operator|.
name|IllegalArgumentException
argument_list|(
literal|"No records have been defined in the CSV"
argument_list|)
throw|;
block|}
else|else
block|{
return|return
name|extractUnmarshalResult
argument_list|(
name|models
argument_list|)
return|;
block|}
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|in
argument_list|,
literal|"in"
argument_list|,
name|LOG
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|consumeFile (BindyCsvFactory factory, List<Map<String, Object>> models, String separator, String quote, AtomicInteger count)
specifier|private
name|Consumer
argument_list|<
name|String
argument_list|>
name|consumeFile
parameter_list|(
name|BindyCsvFactory
name|factory
parameter_list|,
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|models
parameter_list|,
name|String
name|separator
parameter_list|,
name|String
name|quote
parameter_list|,
name|AtomicInteger
name|count
parameter_list|)
block|{
return|return
name|line
lambda|->
block|{
try|try
block|{
comment|// Trim the line coming in to remove any trailing whitespace
name|String
name|trimmedLine
init|=
name|line
operator|.
name|trim
argument_list|()
decl_stmt|;
comment|// Increment counter
name|count
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|model
decl_stmt|;
comment|// Create POJO where CSV data will be stored
name|model
operator|=
name|factory
operator|.
name|factory
argument_list|()
expr_stmt|;
comment|// Split the CSV record according to the separator defined in
comment|// annotated class @CSVRecord
name|Pattern
name|pattern
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|separator
argument_list|)
decl_stmt|;
name|Matcher
name|matcher
init|=
name|pattern
operator|.
name|matcher
argument_list|(
name|trimmedLine
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|separators
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
comment|// Retrieve separators for each match
while|while
condition|(
name|matcher
operator|.
name|find
argument_list|()
condition|)
block|{
name|separators
operator|.
name|add
argument_list|(
name|matcher
operator|.
name|group
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Add terminal separator
if|if
condition|(
name|separators
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|separators
operator|.
name|add
argument_list|(
name|separators
operator|.
name|get
argument_list|(
name|separators
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
index|[]
name|tokens
init|=
name|pattern
operator|.
name|split
argument_list|(
name|trimmedLine
argument_list|,
name|factory
operator|.
name|getAutospanLine
argument_list|()
condition|?
name|factory
operator|.
name|getMaxpos
argument_list|()
else|:
operator|-
literal|1
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|result
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|tokens
argument_list|)
decl_stmt|;
comment|// must unquote tokens before use
name|result
operator|=
name|unquoteTokens
argument_list|(
name|result
argument_list|,
name|separators
argument_list|,
name|quote
argument_list|)
expr_stmt|;
if|if
condition|(
name|result
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No records have been defined in the CSV"
argument_list|)
throw|;
block|}
else|else
block|{
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
literal|"Size of the record splitted : {}"
argument_list|,
name|result
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Bind data from CSV record with model classes
name|factory
operator|.
name|bind
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|result
argument_list|,
name|model
argument_list|,
name|count
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
comment|// Link objects together
name|factory
operator|.
name|link
argument_list|(
name|model
argument_list|)
expr_stmt|;
comment|// Add objects graph to the list
name|models
operator|.
name|add
argument_list|(
name|model
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Graph of objects created: {}"
argument_list|,
name|model
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
name|WrappedException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
return|;
block|}
comment|/**      * Unquote the tokens, by removing leading and trailing quote chars,      * as will handling fixing broken tokens which may have been split      * by a separator inside a quote.      */
DECL|method|unquoteTokens (List<String> result, List<String> separators, String quote)
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|unquoteTokens
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|result
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|separators
parameter_list|,
name|String
name|quote
parameter_list|)
block|{
comment|// a current quoted token which we assemble from the broken pieces
comment|// we need to do this as we use the split method on the String class
comment|// to split the line using regular expression, and it does not handle
comment|// if the separator char is also inside a quoted token, therefore we need
comment|// to fix this afterwards
name|StringBuilder
name|current
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|boolean
name|inProgress
init|=
literal|false
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|int
name|idxSeparator
init|=
literal|0
decl_stmt|;
comment|//parsing assumes matching close and end quotes
for|for
control|(
name|String
name|s
range|:
name|result
control|)
block|{
name|boolean
name|canStart
init|=
literal|false
decl_stmt|;
name|boolean
name|canClose
init|=
literal|false
decl_stmt|;
name|boolean
name|cutStart
init|=
literal|false
decl_stmt|;
name|boolean
name|cutEnd
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|s
operator|.
name|startsWith
argument_list|(
name|quote
argument_list|)
condition|)
block|{
comment|//token is just a quote
if|if
condition|(
name|s
operator|.
name|length
argument_list|()
operator|==
literal|1
condition|)
block|{
name|s
operator|=
literal|""
expr_stmt|;
comment|//if token is a quote then it can only close processing if it has begun
if|if
condition|(
name|inProgress
condition|)
block|{
name|canClose
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|canStart
operator|=
literal|true
expr_stmt|;
block|}
block|}
else|else
block|{
comment|//quote+"not empty"
name|cutStart
operator|=
literal|true
expr_stmt|;
name|canStart
operator|=
literal|true
expr_stmt|;
block|}
block|}
comment|//"not empty"+quote
if|if
condition|(
name|s
operator|.
name|endsWith
argument_list|(
name|quote
argument_list|)
condition|)
block|{
name|cutEnd
operator|=
literal|true
expr_stmt|;
name|canClose
operator|=
literal|true
expr_stmt|;
block|}
comment|//optimize to only substring once
if|if
condition|(
name|cutEnd
operator|||
name|cutStart
condition|)
block|{
name|s
operator|=
name|s
operator|.
name|substring
argument_list|(
name|cutStart
condition|?
literal|1
else|:
literal|0
argument_list|,
name|cutEnd
condition|?
name|s
operator|.
name|length
argument_list|()
operator|-
literal|1
else|:
name|s
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// are we in progress of rebuilding a broken token
if|if
condition|(
name|inProgress
condition|)
block|{
name|current
operator|.
name|append
argument_list|(
name|separators
operator|.
name|get
argument_list|(
name|idxSeparator
argument_list|)
argument_list|)
expr_stmt|;
name|current
operator|.
name|append
argument_list|(
name|s
argument_list|)
expr_stmt|;
if|if
condition|(
name|canClose
condition|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|current
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|current
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|inProgress
operator|=
literal|false
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|canStart
operator|&&
operator|!
name|canClose
condition|)
block|{
name|current
operator|.
name|append
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|inProgress
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
comment|//case where no quotes
name|answer
operator|.
name|add
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
block|}
name|idxSeparator
operator|++
expr_stmt|;
block|}
comment|// any left over from current?
if|if
condition|(
name|current
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|answer
operator|.
name|add
argument_list|(
name|current
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|current
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|createModelFactory (FormatFactory formatFactory)
specifier|protected
name|BindyAbstractFactory
name|createModelFactory
parameter_list|(
name|FormatFactory
name|formatFactory
parameter_list|)
throws|throws
name|Exception
block|{
name|BindyCsvFactory
name|bindyCsvFactory
init|=
operator|new
name|BindyCsvFactory
argument_list|(
name|getClassType
argument_list|()
argument_list|)
decl_stmt|;
name|bindyCsvFactory
operator|.
name|setFormatFactory
argument_list|(
name|formatFactory
argument_list|)
expr_stmt|;
return|return
name|bindyCsvFactory
return|;
block|}
block|}
end_class

end_unit

