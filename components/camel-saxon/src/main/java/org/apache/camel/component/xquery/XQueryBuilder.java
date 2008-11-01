begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xquery
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xquery
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

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
name|FileNotFoundException
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
name|Reader
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
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|Properties
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
name|AtomicBoolean
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
name|Result
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|dom
operator|.
name|DOMResult
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
name|stream
operator|.
name|StreamResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|saxon
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|saxon
operator|.
name|om
operator|.
name|DocumentInfo
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|saxon
operator|.
name|om
operator|.
name|Item
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|saxon
operator|.
name|om
operator|.
name|SequenceIterator
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|saxon
operator|.
name|query
operator|.
name|DynamicQueryContext
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|saxon
operator|.
name|query
operator|.
name|StaticQueryContext
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|saxon
operator|.
name|query
operator|.
name|XQueryExpression
import|;
end_import

begin_import
import|import
name|net
operator|.
name|sf
operator|.
name|saxon
operator|.
name|trans
operator|.
name|XPathException
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
name|Predicate
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
name|RuntimeExpressionException
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
name|IOConverter
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
name|BytesSource
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
name|StringSource
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
name|XmlConverter
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
name|NamespaceAware
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
comment|/**  * Creates an XQuery builder  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|XQueryBuilder
specifier|public
specifier|abstract
class|class
name|XQueryBuilder
implements|implements
name|Expression
implements|,
name|Predicate
implements|,
name|NamespaceAware
implements|,
name|Processor
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
name|XQueryBuilder
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|configuration
specifier|private
name|Configuration
name|configuration
decl_stmt|;
DECL|field|expression
specifier|private
name|XQueryExpression
name|expression
decl_stmt|;
DECL|field|staticQueryContext
specifier|private
name|StaticQueryContext
name|staticQueryContext
decl_stmt|;
DECL|field|parameters
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|namespacePrefixes
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespacePrefixes
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
DECL|field|converter
specifier|private
name|XmlConverter
name|converter
init|=
operator|new
name|XmlConverter
argument_list|()
decl_stmt|;
DECL|field|resultsFormat
specifier|private
name|ResultFormat
name|resultsFormat
init|=
name|ResultFormat
operator|.
name|DOM
decl_stmt|;
DECL|field|properties
specifier|private
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
DECL|field|resultType
specifier|private
name|Class
name|resultType
decl_stmt|;
DECL|field|initialized
specifier|private
specifier|final
name|AtomicBoolean
name|initialized
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|false
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"XQuery["
operator|+
name|expression
operator|+
literal|"]"
return|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|body
init|=
name|evaluate
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|(
literal|true
argument_list|)
operator|.
name|setBody
argument_list|(
name|body
argument_list|)
expr_stmt|;
comment|// propogate headers
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
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|evaluate (Exchange exchange)
specifier|public
name|Object
name|evaluate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
try|try
block|{
name|initialize
argument_list|()
expr_stmt|;
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
literal|"Evaluation "
operator|+
name|expression
operator|+
literal|" for exchange: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|resultType
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|resultType
operator|.
name|equals
argument_list|(
name|String
operator|.
name|class
argument_list|)
condition|)
block|{
return|return
name|evaluateAsString
argument_list|(
name|exchange
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|resultType
operator|.
name|isAssignableFrom
argument_list|(
name|Collection
operator|.
name|class
argument_list|)
condition|)
block|{
return|return
name|evaluateAsList
argument_list|(
name|exchange
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|resultType
operator|.
name|isAssignableFrom
argument_list|(
name|Node
operator|.
name|class
argument_list|)
condition|)
block|{
return|return
name|evaluateAsDOM
argument_list|(
name|exchange
argument_list|)
return|;
block|}
else|else
block|{
comment|// TODO figure out how to convert to the given type
block|}
block|}
switch|switch
condition|(
name|resultsFormat
condition|)
block|{
case|case
name|Bytes
case|:
return|return
name|evaluateAsBytes
argument_list|(
name|exchange
argument_list|)
return|;
case|case
name|BytesSource
case|:
return|return
name|evaluateAsBytesSource
argument_list|(
name|exchange
argument_list|)
return|;
case|case
name|DOM
case|:
return|return
name|evaluateAsDOM
argument_list|(
name|exchange
argument_list|)
return|;
case|case
name|List
case|:
return|return
name|evaluateAsList
argument_list|(
name|exchange
argument_list|)
return|;
case|case
name|StringSource
case|:
return|return
name|evaluateAsStringSource
argument_list|(
name|exchange
argument_list|)
return|;
case|case
name|String
case|:
default|default:
return|return
name|evaluateAsString
argument_list|(
name|exchange
argument_list|)
return|;
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
name|RuntimeExpressionException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|evaluateAsList (Exchange exchange)
specifier|public
name|List
name|evaluateAsList
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|initialize
argument_list|()
expr_stmt|;
return|return
name|getExpression
argument_list|()
operator|.
name|evaluate
argument_list|(
name|createDynamicContext
argument_list|(
name|exchange
argument_list|)
argument_list|)
return|;
block|}
DECL|method|evaluateAsStringSource (Exchange exchange)
specifier|public
name|Object
name|evaluateAsStringSource
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|initialize
argument_list|()
expr_stmt|;
name|String
name|text
init|=
name|evaluateAsString
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
return|return
operator|new
name|StringSource
argument_list|(
name|text
argument_list|)
return|;
block|}
DECL|method|evaluateAsBytesSource (Exchange exchange)
specifier|public
name|Object
name|evaluateAsBytesSource
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|initialize
argument_list|()
expr_stmt|;
name|byte
index|[]
name|bytes
init|=
name|evaluateAsBytes
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
return|return
operator|new
name|BytesSource
argument_list|(
name|bytes
argument_list|)
return|;
block|}
DECL|method|evaluateAsDOM (Exchange exchange)
specifier|public
name|Node
name|evaluateAsDOM
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|initialize
argument_list|()
expr_stmt|;
name|DOMResult
name|result
init|=
operator|new
name|DOMResult
argument_list|()
decl_stmt|;
name|DynamicQueryContext
name|context
init|=
name|createDynamicContext
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|XQueryExpression
name|expression
init|=
name|getExpression
argument_list|()
decl_stmt|;
name|expression
operator|.
name|pull
argument_list|(
name|context
argument_list|,
name|result
argument_list|,
name|properties
argument_list|)
expr_stmt|;
return|return
name|result
operator|.
name|getNode
argument_list|()
return|;
block|}
DECL|method|evaluateAsBytes (Exchange exchange)
specifier|public
name|byte
index|[]
name|evaluateAsBytes
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|initialize
argument_list|()
expr_stmt|;
name|ByteArrayOutputStream
name|buffer
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|Result
name|result
init|=
operator|new
name|StreamResult
argument_list|(
name|buffer
argument_list|)
decl_stmt|;
name|getExpression
argument_list|()
operator|.
name|pull
argument_list|(
name|createDynamicContext
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|result
argument_list|,
name|properties
argument_list|)
expr_stmt|;
name|byte
index|[]
name|bytes
init|=
name|buffer
operator|.
name|toByteArray
argument_list|()
decl_stmt|;
return|return
name|bytes
return|;
block|}
DECL|method|evaluateAsString (Exchange exchange)
specifier|public
name|String
name|evaluateAsString
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|initialize
argument_list|()
expr_stmt|;
name|StringWriter
name|buffer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|SequenceIterator
name|iter
init|=
name|getExpression
argument_list|()
operator|.
name|iterator
argument_list|(
name|createDynamicContext
argument_list|(
name|exchange
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|Item
name|item
init|=
name|iter
operator|.
name|next
argument_list|()
init|;
name|item
operator|!=
literal|null
condition|;
name|item
operator|=
name|iter
operator|.
name|next
argument_list|()
control|)
block|{
name|buffer
operator|.
name|append
argument_list|(
name|item
operator|.
name|getStringValueCS
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
DECL|method|matches (Exchange exchange)
specifier|public
name|boolean
name|matches
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
try|try
block|{
name|List
name|list
init|=
name|evaluateAsList
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
return|return
name|matches
argument_list|(
name|exchange
argument_list|,
name|list
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
operator|new
name|RuntimeExpressionException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|assertMatches (String text, Exchange exchange)
specifier|public
name|void
name|assertMatches
parameter_list|(
name|String
name|text
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|AssertionError
block|{
try|try
block|{
name|List
name|list
init|=
name|evaluateAsList
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|matches
argument_list|(
name|exchange
argument_list|,
name|list
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|AssertionError
argument_list|(
name|this
operator|+
literal|" failed on "
operator|+
name|exchange
operator|+
literal|" as evaluated: "
operator|+
name|list
argument_list|)
throw|;
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
name|AssertionError
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
comment|// Static helper methods
comment|//-------------------------------------------------------------------------
DECL|method|xquery (final String queryText)
specifier|public
specifier|static
name|XQueryBuilder
name|xquery
parameter_list|(
specifier|final
name|String
name|queryText
parameter_list|)
block|{
return|return
operator|new
name|XQueryBuilder
argument_list|()
block|{
specifier|protected
name|XQueryExpression
name|createQueryExpression
parameter_list|(
name|StaticQueryContext
name|staticQueryContext
parameter_list|)
throws|throws
name|XPathException
block|{
return|return
name|staticQueryContext
operator|.
name|compileQuery
argument_list|(
name|queryText
argument_list|)
return|;
block|}
block|}
return|;
block|}
DECL|method|xquery (final Reader reader)
specifier|public
specifier|static
name|XQueryBuilder
name|xquery
parameter_list|(
specifier|final
name|Reader
name|reader
parameter_list|)
block|{
return|return
operator|new
name|XQueryBuilder
argument_list|()
block|{
specifier|protected
name|XQueryExpression
name|createQueryExpression
parameter_list|(
name|StaticQueryContext
name|staticQueryContext
parameter_list|)
throws|throws
name|XPathException
throws|,
name|IOException
block|{
return|return
name|staticQueryContext
operator|.
name|compileQuery
argument_list|(
name|reader
argument_list|)
return|;
block|}
block|}
return|;
block|}
DECL|method|xquery (final InputStream in, final String characterSet)
specifier|public
specifier|static
name|XQueryBuilder
name|xquery
parameter_list|(
specifier|final
name|InputStream
name|in
parameter_list|,
specifier|final
name|String
name|characterSet
parameter_list|)
block|{
return|return
operator|new
name|XQueryBuilder
argument_list|()
block|{
specifier|protected
name|XQueryExpression
name|createQueryExpression
parameter_list|(
name|StaticQueryContext
name|staticQueryContext
parameter_list|)
throws|throws
name|XPathException
throws|,
name|IOException
block|{
return|return
name|staticQueryContext
operator|.
name|compileQuery
argument_list|(
name|in
argument_list|,
name|characterSet
argument_list|)
return|;
block|}
block|}
return|;
block|}
DECL|method|xquery (File file, String characterSet)
specifier|public
specifier|static
name|XQueryBuilder
name|xquery
parameter_list|(
name|File
name|file
parameter_list|,
name|String
name|characterSet
parameter_list|)
throws|throws
name|FileNotFoundException
block|{
return|return
name|xquery
argument_list|(
name|IOConverter
operator|.
name|toInputStream
argument_list|(
name|file
argument_list|)
argument_list|,
name|characterSet
argument_list|)
return|;
block|}
DECL|method|xquery (URL url, String characterSet)
specifier|public
specifier|static
name|XQueryBuilder
name|xquery
parameter_list|(
name|URL
name|url
parameter_list|,
name|String
name|characterSet
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|xquery
argument_list|(
name|IOConverter
operator|.
name|toInputStream
argument_list|(
name|url
argument_list|)
argument_list|,
name|characterSet
argument_list|)
return|;
block|}
DECL|method|xquery (File file)
specifier|public
specifier|static
name|XQueryBuilder
name|xquery
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|FileNotFoundException
block|{
return|return
name|xquery
argument_list|(
name|IOConverter
operator|.
name|toInputStream
argument_list|(
name|file
argument_list|)
argument_list|,
name|ObjectHelper
operator|.
name|getDefaultCharacterSet
argument_list|()
argument_list|)
return|;
block|}
DECL|method|xquery (URL url)
specifier|public
specifier|static
name|XQueryBuilder
name|xquery
parameter_list|(
name|URL
name|url
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|xquery
argument_list|(
name|IOConverter
operator|.
name|toInputStream
argument_list|(
name|url
argument_list|)
argument_list|,
name|ObjectHelper
operator|.
name|getDefaultCharacterSet
argument_list|()
argument_list|)
return|;
block|}
comment|// Fluent API
comment|// -------------------------------------------------------------------------
DECL|method|parameter (String name, Object value)
specifier|public
name|XQueryBuilder
name|parameter
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|parameters
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|namespace (String prefix, String uri)
specifier|public
name|XQueryBuilder
name|namespace
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
name|namespacePrefixes
operator|.
name|put
argument_list|(
name|prefix
argument_list|,
name|uri
argument_list|)
expr_stmt|;
comment|// more namespace, we must re initialize
name|initialized
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|resultType (Class resultType)
specifier|public
name|XQueryBuilder
name|resultType
parameter_list|(
name|Class
name|resultType
parameter_list|)
block|{
name|setResultType
argument_list|(
name|resultType
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|asBytes ()
specifier|public
name|XQueryBuilder
name|asBytes
parameter_list|()
block|{
name|setResultsFormat
argument_list|(
name|ResultFormat
operator|.
name|Bytes
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|asBytesSource ()
specifier|public
name|XQueryBuilder
name|asBytesSource
parameter_list|()
block|{
name|setResultsFormat
argument_list|(
name|ResultFormat
operator|.
name|BytesSource
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|asDOM ()
specifier|public
name|XQueryBuilder
name|asDOM
parameter_list|()
block|{
name|setResultsFormat
argument_list|(
name|ResultFormat
operator|.
name|DOM
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|asDOMSource ()
specifier|public
name|XQueryBuilder
name|asDOMSource
parameter_list|()
block|{
name|setResultsFormat
argument_list|(
name|ResultFormat
operator|.
name|DOMSource
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|asList ()
specifier|public
name|XQueryBuilder
name|asList
parameter_list|()
block|{
name|setResultsFormat
argument_list|(
name|ResultFormat
operator|.
name|List
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|asString ()
specifier|public
name|XQueryBuilder
name|asString
parameter_list|()
block|{
name|setResultsFormat
argument_list|(
name|ResultFormat
operator|.
name|String
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|asStringSource ()
specifier|public
name|XQueryBuilder
name|asStringSource
parameter_list|()
block|{
name|setResultsFormat
argument_list|(
name|ResultFormat
operator|.
name|StringSource
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|// Properties
comment|// -------------------------------------------------------------------------
comment|/**      * Configures the namespace context from the given DOM element      */
DECL|method|setNamespaces (Map<String, String> namespaces)
specifier|public
name|void
name|setNamespaces
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaces
parameter_list|)
block|{
name|namespacePrefixes
operator|.
name|putAll
argument_list|(
name|namespaces
argument_list|)
expr_stmt|;
comment|// more namespace, we must re initialize
name|initialized
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|getExpression ()
specifier|public
name|XQueryExpression
name|getExpression
parameter_list|()
throws|throws
name|IOException
throws|,
name|XPathException
block|{
return|return
name|expression
return|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|Configuration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|setConfiguration (Configuration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|Configuration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
comment|// change configuration, we must re intialize
name|initialized
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|getStaticQueryContext ()
specifier|public
name|StaticQueryContext
name|getStaticQueryContext
parameter_list|()
block|{
return|return
name|staticQueryContext
return|;
block|}
DECL|method|setStaticQueryContext (StaticQueryContext staticQueryContext)
specifier|public
name|void
name|setStaticQueryContext
parameter_list|(
name|StaticQueryContext
name|staticQueryContext
parameter_list|)
block|{
name|this
operator|.
name|staticQueryContext
operator|=
name|staticQueryContext
expr_stmt|;
comment|// change context, we must re intialize
name|initialized
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|getParameters ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getParameters
parameter_list|()
block|{
return|return
name|parameters
return|;
block|}
DECL|method|setParameters (Map<String, Object> parameters)
specifier|public
name|void
name|setParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
name|this
operator|.
name|parameters
operator|=
name|parameters
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
DECL|method|getResultsFormat ()
specifier|public
name|ResultFormat
name|getResultsFormat
parameter_list|()
block|{
return|return
name|resultsFormat
return|;
block|}
DECL|method|setResultsFormat (ResultFormat resultsFormat)
specifier|public
name|void
name|setResultsFormat
parameter_list|(
name|ResultFormat
name|resultsFormat
parameter_list|)
block|{
name|this
operator|.
name|resultsFormat
operator|=
name|resultsFormat
expr_stmt|;
block|}
DECL|method|getResultType ()
specifier|public
name|Class
name|getResultType
parameter_list|()
block|{
return|return
name|resultType
return|;
block|}
DECL|method|setResultType (Class resultType)
specifier|public
name|void
name|setResultType
parameter_list|(
name|Class
name|resultType
parameter_list|)
block|{
name|this
operator|.
name|resultType
operator|=
name|resultType
expr_stmt|;
block|}
comment|// Implementation methods
comment|// -------------------------------------------------------------------------
comment|/**      * A factory method to create the XQuery expression      */
DECL|method|createQueryExpression (StaticQueryContext staticQueryContext)
specifier|protected
specifier|abstract
name|XQueryExpression
name|createQueryExpression
parameter_list|(
name|StaticQueryContext
name|staticQueryContext
parameter_list|)
throws|throws
name|XPathException
throws|,
name|IOException
function_decl|;
comment|/**      * Creates a dynamic context for the given exchange      */
DECL|method|createDynamicContext (Exchange exchange)
specifier|protected
name|DynamicQueryContext
name|createDynamicContext
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Configuration
name|config
init|=
name|getConfiguration
argument_list|()
decl_stmt|;
name|DynamicQueryContext
name|dynamicQueryContext
init|=
operator|new
name|DynamicQueryContext
argument_list|(
name|config
argument_list|)
decl_stmt|;
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|Source
name|source
init|=
literal|null
decl_stmt|;
try|try
block|{
name|Item
name|item
init|=
name|in
operator|.
name|getBody
argument_list|(
name|Item
operator|.
name|class
argument_list|)
decl_stmt|;
name|dynamicQueryContext
operator|.
name|setContextItem
argument_list|(
name|item
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoTypeConversionAvailableException
name|e
parameter_list|)
block|{
try|try
block|{
name|source
operator|=
name|in
operator|.
name|getBody
argument_list|(
name|Source
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoTypeConversionAvailableException
name|e2
parameter_list|)
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
literal|"No body available on exchange so using an empty document: "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
name|source
operator|=
name|converter
operator|.
name|toSource
argument_list|(
name|converter
operator|.
name|createDocument
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|DocumentInfo
name|doc
init|=
name|getStaticQueryContext
argument_list|()
operator|.
name|buildDocument
argument_list|(
name|source
argument_list|)
decl_stmt|;
name|dynamicQueryContext
operator|.
name|setContextItem
argument_list|(
name|doc
argument_list|)
expr_stmt|;
block|}
name|configureQuery
argument_list|(
name|dynamicQueryContext
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
return|return
name|dynamicQueryContext
return|;
block|}
comment|/**      * Configures the dynamic context with exchange specific parameters      */
DECL|method|configureQuery (DynamicQueryContext dynamicQueryContext, Exchange exchange)
specifier|protected
name|void
name|configureQuery
parameter_list|(
name|DynamicQueryContext
name|dynamicQueryContext
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|addParameters
argument_list|(
name|dynamicQueryContext
argument_list|,
name|exchange
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
name|addParameters
argument_list|(
name|dynamicQueryContext
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|addParameters
argument_list|(
name|dynamicQueryContext
argument_list|,
name|getParameters
argument_list|()
argument_list|)
expr_stmt|;
name|dynamicQueryContext
operator|.
name|setParameter
argument_list|(
literal|"exchange"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getPattern
argument_list|()
operator|.
name|isOutCapable
argument_list|()
condition|)
block|{
name|dynamicQueryContext
operator|.
name|setParameter
argument_list|(
literal|"out"
argument_list|,
name|exchange
operator|.
name|getOut
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|addParameters (DynamicQueryContext dynamicQueryContext, Map<String, Object> map)
specifier|protected
name|void
name|addParameters
parameter_list|(
name|DynamicQueryContext
name|dynamicQueryContext
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|)
block|{
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|propertyEntries
init|=
name|map
operator|.
name|entrySet
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|propertyEntries
control|)
block|{
name|dynamicQueryContext
operator|.
name|setParameter
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|matches (Exchange exchange, List results)
specifier|protected
name|boolean
name|matches
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|List
name|results
parameter_list|)
block|{
return|return
name|ObjectHelper
operator|.
name|matches
argument_list|(
name|results
argument_list|)
return|;
block|}
comment|/**      * Initializes this builder -<b>Must be invoked before evaluation</b>.      */
DECL|method|initialize ()
specifier|protected
specifier|synchronized
name|void
name|initialize
parameter_list|()
throws|throws
name|XPathException
throws|,
name|IOException
block|{
comment|// must use synchronized for concurrency issues and only let it initialize once
if|if
condition|(
operator|!
name|initialized
operator|.
name|get
argument_list|()
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
literal|"Initializing XQueryBuilder "
operator|+
name|this
argument_list|)
expr_stmt|;
block|}
name|configuration
operator|=
operator|new
name|Configuration
argument_list|()
expr_stmt|;
name|configuration
operator|.
name|setHostLanguage
argument_list|(
name|Configuration
operator|.
name|XQUERY
argument_list|)
expr_stmt|;
name|staticQueryContext
operator|=
operator|new
name|StaticQueryContext
argument_list|(
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|entries
init|=
name|namespacePrefixes
operator|.
name|entrySet
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|entries
control|)
block|{
name|String
name|prefix
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|String
name|uri
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|staticQueryContext
operator|.
name|declareNamespace
argument_list|(
name|prefix
argument_list|,
name|uri
argument_list|)
expr_stmt|;
name|staticQueryContext
operator|.
name|setInheritNamespaces
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|expression
operator|=
name|createQueryExpression
argument_list|(
name|staticQueryContext
argument_list|)
expr_stmt|;
name|initialized
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

