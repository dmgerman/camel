begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.bindy.fixed
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
name|fixed
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
name|HashMap
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
name|Scanner
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|BindyFixedLengthFactory
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
name|ExchangeHelper
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
comment|/**  * A<a href="http://camel.apache.org/data-format.html">data format</a> (  * {@link DataFormat}) using Bindy to marshal to and from Fixed Length  */
end_comment

begin_class
annotation|@
name|Dataformat
argument_list|(
literal|"bindy-fixed"
argument_list|)
DECL|class|BindyFixedLengthDataFormat
specifier|public
class|class
name|BindyFixedLengthDataFormat
extends|extends
name|BindyAbstractDataFormat
block|{
DECL|field|CAMEL_BINDY_FIXED_LENGTH_HEADER
specifier|public
specifier|static
specifier|final
name|String
name|CAMEL_BINDY_FIXED_LENGTH_HEADER
init|=
literal|"CamelBindyFixedLengthHeader"
decl_stmt|;
DECL|field|CAMEL_BINDY_FIXED_LENGTH_FOOTER
specifier|public
specifier|static
specifier|final
name|String
name|CAMEL_BINDY_FIXED_LENGTH_FOOTER
init|=
literal|"CamelBindyFixedLengthFooter"
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
name|BindyFixedLengthDataFormat
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|headerFactory
specifier|private
name|BindyFixedLengthFactory
name|headerFactory
decl_stmt|;
DECL|field|footerFactory
specifier|private
name|BindyFixedLengthFactory
name|footerFactory
decl_stmt|;
DECL|method|BindyFixedLengthDataFormat ()
specifier|public
name|BindyFixedLengthDataFormat
parameter_list|()
block|{     }
DECL|method|BindyFixedLengthDataFormat (Class<?> type)
specifier|public
name|BindyFixedLengthDataFormat
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
literal|"bindy-fixed"
return|;
block|}
annotation|@
name|Override
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
name|BindyFixedLengthFactory
name|factory
init|=
operator|(
name|BindyFixedLengthFactory
operator|)
name|getFactory
argument_list|()
decl_stmt|;
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
decl_stmt|;
comment|// the body is not a prepared list so help a bit here and create one for us
if|if
condition|(
operator|!
name|isPreparedList
argument_list|(
name|body
argument_list|)
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
argument_list|()
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
else|else
block|{
comment|// cast to the expected type
name|models
operator|=
operator|(
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
operator|)
name|body
expr_stmt|;
block|}
comment|// add the header if it is in the exchange header
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headerRow
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|CAMEL_BINDY_FIXED_LENGTH_HEADER
argument_list|)
decl_stmt|;
if|if
condition|(
name|headerRow
operator|!=
literal|null
condition|)
block|{
name|models
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|headerRow
argument_list|)
expr_stmt|;
block|}
comment|// add the footer if it is in the exchange header
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|footerRow
init|=
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|CAMEL_BINDY_FIXED_LENGTH_FOOTER
argument_list|)
decl_stmt|;
if|if
condition|(
name|footerRow
operator|!=
literal|null
condition|)
block|{
name|models
operator|.
name|add
argument_list|(
name|models
operator|.
name|size
argument_list|()
argument_list|,
name|footerRow
argument_list|)
expr_stmt|;
block|}
name|int
name|row
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|model
range|:
name|models
control|)
block|{
name|row
operator|++
expr_stmt|;
name|String
name|result
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|row
operator|==
literal|1
operator|&&
name|headerFactory
operator|!=
literal|null
condition|)
block|{
comment|// marshal the first row as a header if the models match
name|Set
argument_list|<
name|String
argument_list|>
name|modelClassNames
init|=
name|model
operator|.
name|keySet
argument_list|()
decl_stmt|;
comment|// only use the header factory if the row is the header
if|if
condition|(
name|headerFactory
operator|.
name|supportsModel
argument_list|(
name|modelClassNames
argument_list|)
condition|)
block|{
if|if
condition|(
name|factory
operator|.
name|skipHeader
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Skipping marshal of header row; 'skipHeader=true'"
argument_list|)
expr_stmt|;
continue|continue;
block|}
else|else
block|{
name|result
operator|=
name|headerFactory
operator|.
name|unbind
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|model
argument_list|)
expr_stmt|;
block|}
block|}
block|}
elseif|else
if|if
condition|(
name|row
operator|==
name|models
operator|.
name|size
argument_list|()
operator|&&
name|footerFactory
operator|!=
literal|null
condition|)
block|{
comment|// marshal the last row as a footer if the models match
name|Set
argument_list|<
name|String
argument_list|>
name|modelClassNames
init|=
name|model
operator|.
name|keySet
argument_list|()
decl_stmt|;
comment|// only use the header factory if the row is the header
if|if
condition|(
name|footerFactory
operator|.
name|supportsModel
argument_list|(
name|modelClassNames
argument_list|)
condition|)
block|{
if|if
condition|(
name|factory
operator|.
name|skipFooter
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Skipping marshal of footer row; 'skipFooter=true'"
argument_list|)
expr_stmt|;
continue|continue;
block|}
else|else
block|{
name|result
operator|=
name|footerFactory
operator|.
name|unbind
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|model
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
comment|// marshal as a normal / default row
name|result
operator|=
name|factory
operator|.
name|unbind
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|model
argument_list|)
expr_stmt|;
block|}
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
block|}
comment|/*      * Check if the body is already parsed.      * Bindy expects a list containing Map<String, Object> entries      * where each Map contains only one entry where the key is the class      * name of the object to be marshalled, and the value is the      * object to be marshalled.      */
DECL|method|isPreparedList (Object object)
specifier|private
name|boolean
name|isPreparedList
parameter_list|(
name|Object
name|object
parameter_list|)
block|{
if|if
condition|(
name|List
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|object
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|?
argument_list|>
name|list
init|=
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|object
decl_stmt|;
if|if
condition|(
name|list
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
comment|// Check first entry, should be enough
name|Object
name|entry
init|=
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|Map
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|entry
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|map
init|=
operator|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
operator|)
name|entry
decl_stmt|;
if|if
condition|(
name|map
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
if|if
condition|(
name|map
operator|.
name|keySet
argument_list|()
operator|.
name|toArray
argument_list|()
index|[
literal|0
index|]
operator|instanceof
name|String
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
annotation|@
name|Override
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
name|BindyFixedLengthFactory
name|factory
init|=
operator|(
name|BindyFixedLengthFactory
operator|)
name|getFactory
argument_list|()
decl_stmt|;
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
comment|// Pojos of the model
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|model
decl_stmt|;
name|InputStreamReader
name|in
init|=
operator|new
name|InputStreamReader
argument_list|(
name|inputStream
argument_list|,
name|ExchangeHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
argument_list|)
decl_stmt|;
comment|// Scanner is used to read big file
name|Scanner
name|scanner
init|=
operator|new
name|Scanner
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|boolean
name|isEolSet
init|=
literal|false
decl_stmt|;
if|if
condition|(
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|factory
operator|.
name|getEndOfLine
argument_list|()
argument_list|)
condition|)
block|{
name|scanner
operator|.
name|useDelimiter
argument_list|(
name|factory
operator|.
name|getEndOfLine
argument_list|()
argument_list|)
expr_stmt|;
name|isEolSet
operator|=
literal|true
expr_stmt|;
block|}
name|AtomicInteger
name|count
init|=
operator|new
name|AtomicInteger
argument_list|(
literal|0
argument_list|)
decl_stmt|;
try|try
block|{
comment|// Parse the header if it exists
if|if
condition|(
operator|(
operator|(
name|isEolSet
operator|&&
name|scanner
operator|.
name|hasNext
argument_list|()
operator|)
operator|||
operator|(
operator|!
name|isEolSet
operator|&&
name|scanner
operator|.
name|hasNextLine
argument_list|()
operator|)
operator|)
operator|&&
name|factory
operator|.
name|hasHeader
argument_list|()
condition|)
block|{
comment|// Read the line (should not trim as its fixed length)
name|String
name|line
init|=
name|getNextNonEmptyLine
argument_list|(
name|scanner
argument_list|,
name|count
argument_list|,
name|isEolSet
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|factory
operator|.
name|skipHeader
argument_list|()
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headerObjMap
init|=
name|createModel
argument_list|(
name|headerFactory
argument_list|,
name|line
argument_list|,
name|count
operator|.
name|intValue
argument_list|()
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CAMEL_BINDY_FIXED_LENGTH_HEADER
argument_list|,
name|headerObjMap
argument_list|)
expr_stmt|;
block|}
block|}
name|String
name|thisLine
init|=
name|getNextNonEmptyLine
argument_list|(
name|scanner
argument_list|,
name|count
argument_list|,
name|isEolSet
argument_list|)
decl_stmt|;
name|String
name|nextLine
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|thisLine
operator|!=
literal|null
condition|)
block|{
name|nextLine
operator|=
name|getNextNonEmptyLine
argument_list|(
name|scanner
argument_list|,
name|count
argument_list|,
name|isEolSet
argument_list|)
expr_stmt|;
block|}
comment|// Parse the main file content
while|while
condition|(
name|thisLine
operator|!=
literal|null
operator|&&
name|nextLine
operator|!=
literal|null
condition|)
block|{
name|model
operator|=
name|createModel
argument_list|(
name|factory
argument_list|,
name|thisLine
argument_list|,
name|count
operator|.
name|intValue
argument_list|()
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
name|thisLine
operator|=
name|nextLine
expr_stmt|;
name|nextLine
operator|=
name|getNextNonEmptyLine
argument_list|(
name|scanner
argument_list|,
name|count
argument_list|,
name|isEolSet
argument_list|)
expr_stmt|;
block|}
comment|// this line should be the last non-empty line from the file
comment|// optionally parse the line as a footer
if|if
condition|(
name|thisLine
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|factory
operator|.
name|hasFooter
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|factory
operator|.
name|skipFooter
argument_list|()
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|footerObjMap
init|=
name|createModel
argument_list|(
name|footerFactory
argument_list|,
name|thisLine
argument_list|,
name|count
operator|.
name|intValue
argument_list|()
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|CAMEL_BINDY_FIXED_LENGTH_FOOTER
argument_list|,
name|footerObjMap
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|model
operator|=
name|createModel
argument_list|(
name|factory
argument_list|,
name|thisLine
argument_list|,
name|count
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|models
operator|.
name|add
argument_list|(
name|model
argument_list|)
expr_stmt|;
block|}
block|}
comment|// BigIntegerFormatFactory if models list is empty or not
comment|// If this is the case (correspond to an empty stream, ...)
if|if
condition|(
name|models
operator|.
name|isEmpty
argument_list|()
operator|&&
operator|!
name|isAllowEmptyStream
argument_list|()
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
literal|"No records have been defined in the file"
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
finally|finally
block|{
name|scanner
operator|.
name|close
argument_list|()
expr_stmt|;
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
DECL|method|getNextNonEmptyLine (Scanner scanner, AtomicInteger count, boolean isEolSet)
specifier|private
name|String
name|getNextNonEmptyLine
parameter_list|(
name|Scanner
name|scanner
parameter_list|,
name|AtomicInteger
name|count
parameter_list|,
name|boolean
name|isEolSet
parameter_list|)
block|{
name|String
name|line
init|=
literal|""
decl_stmt|;
while|while
condition|(
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
name|isEmpty
argument_list|(
name|line
argument_list|)
operator|&&
operator|(
operator|(
name|isEolSet
operator|&&
name|scanner
operator|.
name|hasNext
argument_list|()
operator|)
operator|||
operator|(
operator|!
name|isEolSet
operator|&&
name|scanner
operator|.
name|hasNextLine
argument_list|()
operator|)
operator|)
condition|)
block|{
name|count
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|isEolSet
condition|)
block|{
name|line
operator|=
name|scanner
operator|.
name|nextLine
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|line
operator|=
name|scanner
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
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
name|isEmpty
argument_list|(
name|line
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
else|else
block|{
return|return
name|line
return|;
block|}
block|}
DECL|method|createModel (BindyFixedLengthFactory factory, String line, int count)
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|createModel
parameter_list|(
name|BindyFixedLengthFactory
name|factory
parameter_list|,
name|String
name|line
parameter_list|,
name|int
name|count
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|myLine
init|=
name|line
decl_stmt|;
comment|// Check if the record length corresponds to the parameter
comment|// provided in the @FixedLengthRecord
if|if
condition|(
name|factory
operator|.
name|recordLength
argument_list|()
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|isPaddingNeededAndEnable
argument_list|(
name|factory
argument_list|,
name|myLine
argument_list|)
condition|)
block|{
comment|//myLine = rightPad(myLine, factory.recordLength());
block|}
if|if
condition|(
name|isTrimmingNeededAndEnabled
argument_list|(
name|factory
argument_list|,
name|myLine
argument_list|)
condition|)
block|{
name|myLine
operator|=
name|myLine
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|factory
operator|.
name|recordLength
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|myLine
operator|.
name|length
argument_list|()
operator|<
name|factory
operator|.
name|recordLength
argument_list|()
operator|&&
operator|!
name|factory
operator|.
name|isIgnoreMissingChars
argument_list|()
operator|)
operator|||
operator|(
name|myLine
operator|.
name|length
argument_list|()
operator|>
name|factory
operator|.
name|recordLength
argument_list|()
operator|)
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
literal|"Size of the record: "
operator|+
name|myLine
operator|.
name|length
argument_list|()
operator|+
literal|" is not equal to the value provided in the model: "
operator|+
name|factory
operator|.
name|recordLength
argument_list|()
argument_list|)
throw|;
block|}
block|}
comment|// Create POJO where Fixed data will be stored
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|model
init|=
name|factory
operator|.
name|factory
argument_list|()
decl_stmt|;
comment|// Bind data from Fixed record with model classes
name|factory
operator|.
name|bind
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|myLine
argument_list|,
name|model
argument_list|,
name|count
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
name|LOG
operator|.
name|debug
argument_list|(
literal|"Graph of objects created: {}"
argument_list|,
name|model
argument_list|)
expr_stmt|;
return|return
name|model
return|;
block|}
DECL|method|isTrimmingNeededAndEnabled (BindyFixedLengthFactory factory, String myLine)
specifier|private
name|boolean
name|isTrimmingNeededAndEnabled
parameter_list|(
name|BindyFixedLengthFactory
name|factory
parameter_list|,
name|String
name|myLine
parameter_list|)
block|{
return|return
name|factory
operator|.
name|isIgnoreTrailingChars
argument_list|()
operator|&&
name|myLine
operator|.
name|length
argument_list|()
operator|>
name|factory
operator|.
name|recordLength
argument_list|()
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
DECL|method|rightPad (String myLine, int length)
specifier|private
name|String
name|rightPad
parameter_list|(
name|String
name|myLine
parameter_list|,
name|int
name|length
parameter_list|)
block|{
return|return
name|String
operator|.
name|format
argument_list|(
literal|"%1$-"
operator|+
name|length
operator|+
literal|"s"
argument_list|,
name|myLine
argument_list|)
return|;
block|}
DECL|method|isPaddingNeededAndEnable (BindyFixedLengthFactory factory, String myLine)
specifier|private
name|boolean
name|isPaddingNeededAndEnable
parameter_list|(
name|BindyFixedLengthFactory
name|factory
parameter_list|,
name|String
name|myLine
parameter_list|)
block|{
return|return
name|myLine
operator|.
name|length
argument_list|()
operator|<
name|factory
operator|.
name|recordLength
argument_list|()
operator|&&
name|factory
operator|.
name|isIgnoreMissingChars
argument_list|()
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
name|BindyFixedLengthFactory
name|factory
init|=
operator|new
name|BindyFixedLengthFactory
argument_list|(
name|getClassType
argument_list|()
argument_list|)
decl_stmt|;
name|factory
operator|.
name|setFormatFactory
argument_list|(
name|formatFactory
argument_list|)
expr_stmt|;
comment|// Optionally initialize the header factory... using header model classes
if|if
condition|(
name|factory
operator|.
name|hasHeader
argument_list|()
condition|)
block|{
name|this
operator|.
name|headerFactory
operator|=
operator|new
name|BindyFixedLengthFactory
argument_list|(
name|factory
operator|.
name|header
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|headerFactory
operator|.
name|setFormatFactory
argument_list|(
name|formatFactory
argument_list|)
expr_stmt|;
block|}
comment|// Optionally initialize the footer factory... using footer model classes
if|if
condition|(
name|factory
operator|.
name|hasFooter
argument_list|()
condition|)
block|{
name|this
operator|.
name|footerFactory
operator|=
operator|new
name|BindyFixedLengthFactory
argument_list|(
name|factory
operator|.
name|footer
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|footerFactory
operator|.
name|setFormatFactory
argument_list|(
name|formatFactory
argument_list|)
expr_stmt|;
block|}
return|return
name|factory
return|;
block|}
block|}
end_class

end_unit

