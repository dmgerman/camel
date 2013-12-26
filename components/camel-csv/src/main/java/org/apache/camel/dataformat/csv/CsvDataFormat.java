begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.dataformat.csv
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|dataformat
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
name|OutputStreamWriter
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|NoSuchElementException
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
name|commons
operator|.
name|csv
operator|.
name|CSVParser
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
name|csv
operator|.
name|CSVStrategy
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
name|csv
operator|.
name|writer
operator|.
name|CSVConfig
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
name|csv
operator|.
name|writer
operator|.
name|CSVField
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
name|csv
operator|.
name|writer
operator|.
name|CSVWriter
import|;
end_import

begin_comment
comment|/**  * CSV Data format.  *<p/>  * By default, columns are autogenerated in the resulting CSV. Subsequent  * messages use the previously created columns with new fields being added at  * the end of the line. Thus, field order is the same from message to message.  * Autogeneration can be disabled. In this case, only the fields defined in  * csvConfig are written on the output.  *  * @version   */
end_comment

begin_class
DECL|class|CsvDataFormat
specifier|public
class|class
name|CsvDataFormat
implements|implements
name|DataFormat
block|{
DECL|field|strategy
specifier|private
name|CSVStrategy
name|strategy
init|=
name|CSVStrategy
operator|.
name|DEFAULT_STRATEGY
decl_stmt|;
DECL|field|config
specifier|private
name|CSVConfig
name|config
init|=
operator|new
name|CSVConfig
argument_list|()
decl_stmt|;
DECL|field|autogenColumns
specifier|private
name|boolean
name|autogenColumns
init|=
literal|true
decl_stmt|;
DECL|field|delimiter
specifier|private
name|String
name|delimiter
decl_stmt|;
DECL|field|skipFirstLine
specifier|private
name|boolean
name|skipFirstLine
decl_stmt|;
comment|/**      * Lazy row loading with iterator for big files.      */
DECL|field|lazyLoad
specifier|private
name|boolean
name|lazyLoad
decl_stmt|;
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
if|if
condition|(
name|delimiter
operator|!=
literal|null
condition|)
block|{
name|config
operator|.
name|setDelimiter
argument_list|(
name|delimiter
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|OutputStreamWriter
name|out
init|=
operator|new
name|OutputStreamWriter
argument_list|(
name|outputStream
argument_list|,
name|IOHelper
operator|.
name|getCharsetName
argument_list|(
name|exchange
argument_list|)
argument_list|)
decl_stmt|;
name|CSVWriter
name|csv
init|=
operator|new
name|CSVWriter
argument_list|(
name|config
argument_list|)
decl_stmt|;
name|csv
operator|.
name|setWriter
argument_list|(
name|out
argument_list|)
expr_stmt|;
try|try
block|{
name|List
argument_list|<
name|?
argument_list|>
name|list
init|=
name|ExchangeHelper
operator|.
name|convertToType
argument_list|(
name|exchange
argument_list|,
name|List
operator|.
name|class
argument_list|,
name|object
argument_list|)
decl_stmt|;
if|if
condition|(
name|list
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Object
name|child
range|:
name|list
control|)
block|{
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|row
init|=
name|ExchangeHelper
operator|.
name|convertToMandatoryType
argument_list|(
name|exchange
argument_list|,
name|Map
operator|.
name|class
argument_list|,
name|child
argument_list|)
decl_stmt|;
name|doMarshalRecord
argument_list|(
name|exchange
argument_list|,
name|row
argument_list|,
name|out
argument_list|,
name|csv
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|row
init|=
name|ExchangeHelper
operator|.
name|convertToMandatoryType
argument_list|(
name|exchange
argument_list|,
name|Map
operator|.
name|class
argument_list|,
name|object
argument_list|)
decl_stmt|;
name|doMarshalRecord
argument_list|(
name|exchange
argument_list|,
name|row
argument_list|,
name|out
argument_list|,
name|csv
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
name|out
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doMarshalRecord (Exchange exchange, Map<?, ?> row, Writer out, CSVWriter csv)
specifier|private
name|void
name|doMarshalRecord
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|row
parameter_list|,
name|Writer
name|out
parameter_list|,
name|CSVWriter
name|csv
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|autogenColumns
condition|)
block|{
comment|// no specific config has been set so lets add fields
name|Set
argument_list|<
name|?
argument_list|>
name|set
init|=
name|row
operator|.
name|keySet
argument_list|()
decl_stmt|;
name|updateFieldsInConfig
argument_list|(
name|set
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
name|csv
operator|.
name|writeRecord
argument_list|(
name|row
argument_list|)
expr_stmt|;
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
if|if
condition|(
name|delimiter
operator|!=
literal|null
condition|)
block|{
name|config
operator|.
name|setDelimiter
argument_list|(
name|delimiter
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|strategy
operator|.
name|setDelimiter
argument_list|(
name|config
operator|.
name|getDelimiter
argument_list|()
argument_list|)
expr_stmt|;
name|InputStreamReader
name|in
init|=
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
decl_stmt|;
name|boolean
name|closeStream
init|=
literal|false
decl_stmt|;
name|CsvIterator
name|csvIterator
init|=
literal|null
decl_stmt|;
try|try
block|{
name|CSVParser
name|parser
init|=
name|createParser
argument_list|(
name|in
argument_list|)
decl_stmt|;
if|if
condition|(
name|parser
operator|==
literal|null
condition|)
block|{
name|closeStream
operator|=
literal|true
expr_stmt|;
comment|// return an empty Iterator
return|return
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
literal|null
argument_list|)
return|;
block|}
name|csvIterator
operator|=
operator|new
name|CsvIterator
argument_list|(
name|parser
argument_list|,
name|in
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
name|closeStream
operator|=
literal|true
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|closeStream
condition|)
block|{
name|IOHelper
operator|.
name|close
argument_list|(
name|in
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|lazyLoad
condition|)
block|{
return|return
name|csvIterator
return|;
block|}
return|return
name|loadAllAsList
argument_list|(
name|csvIterator
argument_list|)
return|;
block|}
DECL|method|createParser (InputStreamReader in)
specifier|private
name|CSVParser
name|createParser
parameter_list|(
name|InputStreamReader
name|in
parameter_list|)
throws|throws
name|IOException
block|{
name|CSVParser
name|parser
init|=
operator|new
name|CSVParser
argument_list|(
name|in
argument_list|,
name|strategy
argument_list|)
decl_stmt|;
if|if
condition|(
name|skipFirstLine
condition|)
block|{
if|if
condition|(
name|parser
operator|.
name|getLine
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
block|}
return|return
name|parser
return|;
block|}
DECL|method|loadAllAsList (CsvIterator iter)
specifier|private
name|List
argument_list|<
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|loadAllAsList
parameter_list|(
name|CsvIterator
name|iter
parameter_list|)
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|iter
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
DECL|method|getDelimiter ()
specifier|public
name|String
name|getDelimiter
parameter_list|()
block|{
return|return
name|delimiter
return|;
block|}
DECL|method|setDelimiter (String delimiter)
specifier|public
name|void
name|setDelimiter
parameter_list|(
name|String
name|delimiter
parameter_list|)
block|{
if|if
condition|(
name|delimiter
operator|!=
literal|null
operator|&&
name|delimiter
operator|.
name|length
argument_list|()
operator|>
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Delimiter must have a length of one!"
argument_list|)
throw|;
block|}
name|this
operator|.
name|delimiter
operator|=
name|delimiter
expr_stmt|;
block|}
DECL|method|getConfig ()
specifier|public
name|CSVConfig
name|getConfig
parameter_list|()
block|{
return|return
name|config
return|;
block|}
DECL|method|setConfig (CSVConfig config)
specifier|public
name|void
name|setConfig
parameter_list|(
name|CSVConfig
name|config
parameter_list|)
block|{
name|this
operator|.
name|config
operator|=
name|config
expr_stmt|;
block|}
DECL|method|getStrategy ()
specifier|public
name|CSVStrategy
name|getStrategy
parameter_list|()
block|{
return|return
name|strategy
return|;
block|}
DECL|method|setStrategy (CSVStrategy strategy)
specifier|public
name|void
name|setStrategy
parameter_list|(
name|CSVStrategy
name|strategy
parameter_list|)
block|{
name|this
operator|.
name|strategy
operator|=
name|strategy
expr_stmt|;
block|}
DECL|method|isAutogenColumns ()
specifier|public
name|boolean
name|isAutogenColumns
parameter_list|()
block|{
return|return
name|autogenColumns
return|;
block|}
comment|/**      * Auto generate columns.      *      * @param autogenColumns set to false to disallow column autogeneration (default true)      */
DECL|method|setAutogenColumns (boolean autogenColumns)
specifier|public
name|void
name|setAutogenColumns
parameter_list|(
name|boolean
name|autogenColumns
parameter_list|)
block|{
name|this
operator|.
name|autogenColumns
operator|=
name|autogenColumns
expr_stmt|;
block|}
DECL|method|isSkipFirstLine ()
specifier|public
name|boolean
name|isSkipFirstLine
parameter_list|()
block|{
return|return
name|skipFirstLine
return|;
block|}
DECL|method|setSkipFirstLine (boolean skipFirstLine)
specifier|public
name|void
name|setSkipFirstLine
parameter_list|(
name|boolean
name|skipFirstLine
parameter_list|)
block|{
name|this
operator|.
name|skipFirstLine
operator|=
name|skipFirstLine
expr_stmt|;
block|}
DECL|method|isLazyLoad ()
specifier|public
name|boolean
name|isLazyLoad
parameter_list|()
block|{
return|return
name|lazyLoad
return|;
block|}
DECL|method|setLazyLoad (boolean lazyLoad)
specifier|public
name|void
name|setLazyLoad
parameter_list|(
name|boolean
name|lazyLoad
parameter_list|)
block|{
name|this
operator|.
name|lazyLoad
operator|=
name|lazyLoad
expr_stmt|;
block|}
DECL|method|updateFieldsInConfig (Set<?> set, Exchange exchange)
specifier|private
specifier|synchronized
name|void
name|updateFieldsInConfig
parameter_list|(
name|Set
argument_list|<
name|?
argument_list|>
name|set
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
for|for
control|(
name|Object
name|value
range|:
name|set
control|)
block|{
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|String
name|text
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
name|String
operator|.
name|class
argument_list|,
name|value
argument_list|)
decl_stmt|;
comment|// do not add field twice
if|if
condition|(
name|config
operator|.
name|getField
argument_list|(
name|text
argument_list|)
operator|==
literal|null
condition|)
block|{
name|CSVField
name|field
init|=
operator|new
name|CSVField
argument_list|(
name|text
argument_list|)
decl_stmt|;
name|config
operator|.
name|addField
argument_list|(
name|field
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

