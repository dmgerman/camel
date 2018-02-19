begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.jsonpath
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|jsonpath
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
name|FileInputStream
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
name|Collections
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
name|com
operator|.
name|jayway
operator|.
name|jsonpath
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jayway
operator|.
name|jsonpath
operator|.
name|JsonPath
import|;
end_import

begin_import
import|import
name|com
operator|.
name|jayway
operator|.
name|jsonpath
operator|.
name|Option
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
name|CamelExchangeException
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
name|component
operator|.
name|file
operator|.
name|GenericFile
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

begin_import
import|import static
name|com
operator|.
name|jayway
operator|.
name|jsonpath
operator|.
name|Option
operator|.
name|ALWAYS_RETURN_LIST
import|;
end_import

begin_import
import|import static
name|com
operator|.
name|jayway
operator|.
name|jsonpath
operator|.
name|Option
operator|.
name|SUPPRESS_EXCEPTIONS
import|;
end_import

begin_class
DECL|class|JsonPathEngine
specifier|public
class|class
name|JsonPathEngine
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
name|JsonPathEngine
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|JACKSON_JSON_ADAPTER
specifier|private
specifier|static
specifier|final
name|String
name|JACKSON_JSON_ADAPTER
init|=
literal|"org.apache.camel.jsonpath.jackson.JacksonJsonAdapter"
decl_stmt|;
DECL|field|SIMPLE_PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|SIMPLE_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"\\$\\{[^\\}]+\\}"
argument_list|,
name|Pattern
operator|.
name|MULTILINE
argument_list|)
decl_stmt|;
DECL|field|expression
specifier|private
specifier|final
name|String
name|expression
decl_stmt|;
DECL|field|writeAsString
specifier|private
specifier|final
name|boolean
name|writeAsString
decl_stmt|;
DECL|field|headerName
specifier|private
specifier|final
name|String
name|headerName
decl_stmt|;
DECL|field|path
specifier|private
specifier|final
name|JsonPath
name|path
decl_stmt|;
DECL|field|configuration
specifier|private
specifier|final
name|Configuration
name|configuration
decl_stmt|;
DECL|field|adapter
specifier|private
name|JsonPathAdapter
name|adapter
decl_stmt|;
DECL|field|initJsonAdapter
specifier|private
specifier|volatile
name|boolean
name|initJsonAdapter
decl_stmt|;
annotation|@
name|Deprecated
DECL|method|JsonPathEngine (String expression)
specifier|public
name|JsonPathEngine
parameter_list|(
name|String
name|expression
parameter_list|)
block|{
name|this
argument_list|(
name|expression
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
DECL|method|JsonPathEngine (String expression, boolean writeAsString, boolean suppressExceptions, boolean allowSimple, String headerName, Option[] options)
specifier|public
name|JsonPathEngine
parameter_list|(
name|String
name|expression
parameter_list|,
name|boolean
name|writeAsString
parameter_list|,
name|boolean
name|suppressExceptions
parameter_list|,
name|boolean
name|allowSimple
parameter_list|,
name|String
name|headerName
parameter_list|,
name|Option
index|[]
name|options
parameter_list|)
block|{
name|this
operator|.
name|expression
operator|=
name|expression
expr_stmt|;
name|this
operator|.
name|writeAsString
operator|=
name|writeAsString
expr_stmt|;
name|this
operator|.
name|headerName
operator|=
name|headerName
expr_stmt|;
name|Configuration
operator|.
name|ConfigurationBuilder
name|builder
init|=
name|Configuration
operator|.
name|builder
argument_list|()
decl_stmt|;
if|if
condition|(
name|options
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|options
argument_list|(
name|options
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|suppressExceptions
condition|)
block|{
name|builder
operator|.
name|options
argument_list|(
name|SUPPRESS_EXCEPTIONS
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|configuration
operator|=
name|builder
operator|.
name|build
argument_list|()
expr_stmt|;
name|boolean
name|hasSimple
init|=
literal|false
decl_stmt|;
if|if
condition|(
name|allowSimple
condition|)
block|{
comment|// is simple language embedded
name|Matcher
name|matcher
init|=
name|SIMPLE_PATTERN
operator|.
name|matcher
argument_list|(
name|expression
argument_list|)
decl_stmt|;
if|if
condition|(
name|matcher
operator|.
name|find
argument_list|()
condition|)
block|{
name|hasSimple
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
name|hasSimple
condition|)
block|{
name|this
operator|.
name|path
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|path
operator|=
name|JsonPath
operator|.
name|compile
argument_list|(
name|expression
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Compiled static JsonPath: {}"
argument_list|,
name|expression
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|read (Exchange exchange)
specifier|public
name|Object
name|read
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|answer
decl_stmt|;
if|if
condition|(
name|path
operator|==
literal|null
condition|)
block|{
name|Expression
name|exp
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|resolveLanguage
argument_list|(
literal|"simple"
argument_list|)
operator|.
name|createExpression
argument_list|(
name|expression
argument_list|)
decl_stmt|;
name|String
name|text
init|=
name|exp
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|JsonPath
name|path
init|=
name|JsonPath
operator|.
name|compile
argument_list|(
name|text
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"Compiled dynamic JsonPath: {}"
argument_list|,
name|expression
argument_list|)
expr_stmt|;
name|answer
operator|=
name|doRead
argument_list|(
name|path
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
name|doRead
argument_list|(
name|path
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|writeAsString
condition|)
block|{
if|if
condition|(
operator|!
name|initJsonAdapter
condition|)
block|{
name|doInitAdapter
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|adapter
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Cannot writeAsString as adapter cannot be initialized"
argument_list|)
expr_stmt|;
comment|// return as-is as there is no adapter
return|return
name|answer
return|;
block|}
comment|// write each row as a string but keep it as a list/iterable
if|if
condition|(
name|answer
operator|instanceof
name|Iterable
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Iterable
name|it
init|=
operator|(
name|Iterable
operator|)
name|answer
decl_stmt|;
for|for
control|(
name|Object
name|o
range|:
name|it
control|)
block|{
if|if
condition|(
name|adapter
operator|!=
literal|null
condition|)
block|{
name|String
name|json
init|=
name|adapter
operator|.
name|writeAsString
argument_list|(
name|o
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|json
operator|!=
literal|null
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|json
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|list
return|;
block|}
elseif|else
if|if
condition|(
name|answer
operator|instanceof
name|Map
condition|)
block|{
name|Map
name|map
init|=
operator|(
name|Map
operator|)
name|answer
decl_stmt|;
for|for
control|(
name|Object
name|key
range|:
name|map
operator|.
name|keySet
argument_list|()
control|)
block|{
name|Object
name|value
init|=
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|adapter
operator|!=
literal|null
condition|)
block|{
name|String
name|json
init|=
name|adapter
operator|.
name|writeAsString
argument_list|(
name|value
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|json
operator|!=
literal|null
condition|)
block|{
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|json
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|map
return|;
block|}
else|else
block|{
name|String
name|json
init|=
name|adapter
operator|.
name|writeAsString
argument_list|(
name|answer
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|json
operator|!=
literal|null
condition|)
block|{
return|return
name|json
return|;
block|}
block|}
block|}
return|return
name|answer
return|;
block|}
DECL|method|doRead (JsonPath path, Exchange exchange)
specifier|private
name|Object
name|doRead
parameter_list|(
name|JsonPath
name|path
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
throws|,
name|CamelExchangeException
block|{
name|Object
name|json
init|=
name|headerName
operator|!=
literal|null
condition|?
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|headerName
argument_list|)
else|:
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|json
operator|instanceof
name|InputStream
condition|)
block|{
return|return
name|readWithInputStream
argument_list|(
name|path
argument_list|,
name|exchange
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|json
operator|instanceof
name|GenericFile
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"JSonPath: {} is read as generic file: {}"
argument_list|,
name|path
argument_list|,
name|json
argument_list|)
expr_stmt|;
name|GenericFile
argument_list|<
name|?
argument_list|>
name|genericFile
init|=
operator|(
name|GenericFile
argument_list|<
name|?
argument_list|>
operator|)
name|json
decl_stmt|;
if|if
condition|(
name|genericFile
operator|.
name|getCharset
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// special treatment for generic file with charset
name|InputStream
name|inputStream
init|=
operator|new
name|FileInputStream
argument_list|(
operator|(
name|File
operator|)
name|genericFile
operator|.
name|getFile
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|path
operator|.
name|read
argument_list|(
name|inputStream
argument_list|,
name|genericFile
operator|.
name|getCharset
argument_list|()
argument_list|,
name|configuration
argument_list|)
return|;
block|}
block|}
if|if
condition|(
name|json
operator|instanceof
name|String
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"JSonPath: {} is read as String: {}"
argument_list|,
name|path
argument_list|,
name|json
argument_list|)
expr_stmt|;
name|String
name|str
init|=
operator|(
name|String
operator|)
name|json
decl_stmt|;
return|return
name|path
operator|.
name|read
argument_list|(
name|str
argument_list|,
name|configuration
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|json
operator|instanceof
name|Map
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"JSonPath: {} is read as Map: {}"
argument_list|,
name|path
argument_list|,
name|json
argument_list|)
expr_stmt|;
name|Map
name|map
init|=
operator|(
name|Map
operator|)
name|json
decl_stmt|;
return|return
name|path
operator|.
name|read
argument_list|(
name|map
argument_list|,
name|configuration
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|json
operator|instanceof
name|List
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"JSonPath: {} is read as List: {}"
argument_list|,
name|path
argument_list|,
name|json
argument_list|)
expr_stmt|;
name|List
name|list
init|=
operator|(
name|List
operator|)
name|json
decl_stmt|;
return|return
name|path
operator|.
name|read
argument_list|(
name|list
argument_list|,
name|configuration
argument_list|)
return|;
block|}
else|else
block|{
comment|// can we find an adapter which can read the message body/header
name|Object
name|answer
init|=
name|readWithAdapter
argument_list|(
name|path
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
comment|// fallback and attempt input stream for any other types
name|answer
operator|=
name|readWithInputStream
argument_list|(
name|path
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|answer
operator|!=
literal|null
condition|)
block|{
return|return
name|answer
return|;
block|}
block|}
comment|// is json path configured to suppress exceptions
if|if
condition|(
name|configuration
operator|.
name|getOptions
argument_list|()
operator|.
name|contains
argument_list|(
name|SUPPRESS_EXCEPTIONS
argument_list|)
condition|)
block|{
if|if
condition|(
name|configuration
operator|.
name|getOptions
argument_list|()
operator|.
name|contains
argument_list|(
name|ALWAYS_RETURN_LIST
argument_list|)
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
comment|// okay it was not then lets throw a failure
if|if
condition|(
name|headerName
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Cannot read message header "
operator|+
name|headerName
operator|+
literal|" as supported JSon value"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|CamelExchangeException
argument_list|(
literal|"Cannot read message body as supported JSon value"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
block|}
DECL|method|readWithInputStream (JsonPath path, Exchange exchange)
specifier|private
name|Object
name|readWithInputStream
parameter_list|(
name|JsonPath
name|path
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|IOException
block|{
name|Object
name|json
init|=
name|headerName
operator|!=
literal|null
condition|?
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|headerName
argument_list|)
else|:
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"JSonPath: {} is read as InputStream: {}"
argument_list|,
name|path
argument_list|,
name|json
argument_list|)
expr_stmt|;
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
name|tryConvertTo
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|json
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
name|String
name|jsonEncoding
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JsonPathConstants
operator|.
name|HEADER_JSON_ENCODING
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|jsonEncoding
operator|!=
literal|null
condition|)
block|{
comment|// json encoding specified in header
return|return
name|path
operator|.
name|read
argument_list|(
name|is
argument_list|,
name|jsonEncoding
argument_list|,
name|configuration
argument_list|)
return|;
block|}
else|else
block|{
comment|// No json encoding specified --> assume json encoding is unicode and determine the specific unicode encoding according to RFC-4627.
comment|// This is a temporary solution, it can be removed as soon as jsonpath offers the encoding detection
name|JsonStream
name|jsonStream
init|=
operator|new
name|JsonStream
argument_list|(
name|is
argument_list|)
decl_stmt|;
return|return
name|path
operator|.
name|read
argument_list|(
name|jsonStream
argument_list|,
name|jsonStream
operator|.
name|getEncoding
argument_list|()
operator|.
name|name
argument_list|()
argument_list|,
name|configuration
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|readWithAdapter (JsonPath path, Exchange exchange)
specifier|private
name|Object
name|readWithAdapter
parameter_list|(
name|JsonPath
name|path
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
name|Object
name|json
init|=
name|headerName
operator|!=
literal|null
condition|?
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|headerName
argument_list|)
else|:
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"JSonPath: {} is read with adapter: {}"
argument_list|,
name|path
argument_list|,
name|json
argument_list|)
expr_stmt|;
name|doInitAdapter
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|adapter
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Attempting to use JacksonJsonAdapter: {}"
argument_list|,
name|adapter
argument_list|)
expr_stmt|;
name|Map
name|map
init|=
name|adapter
operator|.
name|readValue
argument_list|(
name|json
argument_list|,
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|map
operator|!=
literal|null
condition|)
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
literal|"JacksonJsonAdapter converted object from: {} to: java.util.Map"
argument_list|,
name|ObjectHelper
operator|.
name|classCanonicalName
argument_list|(
name|json
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|path
operator|.
name|read
argument_list|(
name|map
argument_list|,
name|configuration
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|doInitAdapter (Exchange exchange)
specifier|private
name|void
name|doInitAdapter
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
operator|!
name|initJsonAdapter
condition|)
block|{
try|try
block|{
comment|// need to load this adapter dynamically as its optional
name|LOG
operator|.
name|debug
argument_list|(
literal|"Attempting to enable JacksonJsonAdapter by resolving: {} from classpath"
argument_list|,
name|JACKSON_JSON_ADAPTER
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveClass
argument_list|(
name|JACKSON_JSON_ADAPTER
argument_list|)
decl_stmt|;
if|if
condition|(
name|clazz
operator|!=
literal|null
condition|)
block|{
name|Object
name|obj
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|JsonPathAdapter
condition|)
block|{
name|adapter
operator|=
operator|(
name|JsonPathAdapter
operator|)
name|obj
expr_stmt|;
name|adapter
operator|.
name|init
argument_list|(
name|exchange
operator|.
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|debug
argument_list|(
literal|"JacksonJsonAdapter found on classpath and enabled for camel-jsonpath: {}"
argument_list|,
name|adapter
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Cannot load "
operator|+
name|JACKSON_JSON_ADAPTER
operator|+
literal|" from classpath to enable JacksonJsonAdapter due "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
operator|+
literal|". JacksonJsonAdapter is not enabled."
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
name|initJsonAdapter
operator|=
literal|true
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

