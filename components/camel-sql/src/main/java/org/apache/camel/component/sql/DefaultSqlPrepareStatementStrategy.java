begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sql
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sql
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|PreparedStatement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
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
name|Collections
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
name|RuntimeExchangeException
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
name|language
operator|.
name|simple
operator|.
name|SimpleLanguage
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
name|StringQuoteHelper
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
import|import
name|org
operator|.
name|springframework
operator|.
name|jdbc
operator|.
name|core
operator|.
name|ArgumentPreparedStatementSetter
import|;
end_import

begin_comment
comment|/**  * Default {@link SqlPrepareStatementStrategy} that supports named query parameters as well index based.  */
end_comment

begin_class
DECL|class|DefaultSqlPrepareStatementStrategy
specifier|public
class|class
name|DefaultSqlPrepareStatementStrategy
implements|implements
name|SqlPrepareStatementStrategy
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
name|DefaultSqlPrepareStatementStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|separator
specifier|private
specifier|final
name|char
name|separator
decl_stmt|;
DECL|method|DefaultSqlPrepareStatementStrategy ()
specifier|public
name|DefaultSqlPrepareStatementStrategy
parameter_list|()
block|{
name|this
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
DECL|method|DefaultSqlPrepareStatementStrategy (char separator)
specifier|public
name|DefaultSqlPrepareStatementStrategy
parameter_list|(
name|char
name|separator
parameter_list|)
block|{
name|this
operator|.
name|separator
operator|=
name|separator
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|prepareQuery (String query, boolean allowNamedParameters)
specifier|public
name|String
name|prepareQuery
parameter_list|(
name|String
name|query
parameter_list|,
name|boolean
name|allowNamedParameters
parameter_list|)
throws|throws
name|SQLException
block|{
name|String
name|answer
decl_stmt|;
if|if
condition|(
name|allowNamedParameters
operator|&&
name|hasNamedParameters
argument_list|(
name|query
argument_list|)
condition|)
block|{
comment|// replace all :?word and :?${foo} with just ?
name|answer
operator|=
name|query
operator|.
name|replaceAll
argument_list|(
literal|"\\:\\?\\w+|\\:\\?\\$\\{[^\\}]+\\}"
argument_list|,
literal|"\\?"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
name|query
expr_stmt|;
block|}
name|LOG
operator|.
name|trace
argument_list|(
literal|"Prepared query: {}"
argument_list|,
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|createPopulateIterator (final String query, final String preparedQuery, final int expectedParams, final Exchange exchange, final Object value)
specifier|public
name|Iterator
argument_list|<
name|?
argument_list|>
name|createPopulateIterator
parameter_list|(
specifier|final
name|String
name|query
parameter_list|,
specifier|final
name|String
name|preparedQuery
parameter_list|,
specifier|final
name|int
name|expectedParams
parameter_list|,
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|Object
name|value
parameter_list|)
throws|throws
name|SQLException
block|{
if|if
condition|(
name|hasNamedParameters
argument_list|(
name|query
argument_list|)
condition|)
block|{
comment|// create an iterator that returns the value in the named order
return|return
operator|new
name|PopulateIterator
argument_list|(
name|query
argument_list|,
name|exchange
argument_list|,
name|value
argument_list|)
return|;
block|}
else|else
block|{
comment|// if only 1 parameter and the body is a String then use body as is
if|if
condition|(
name|expectedParams
operator|==
literal|1
operator|&&
name|value
operator|instanceof
name|String
condition|)
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|value
argument_list|)
operator|.
name|iterator
argument_list|()
return|;
block|}
else|else
block|{
comment|// is the body a String
if|if
condition|(
name|value
operator|instanceof
name|String
condition|)
block|{
comment|// if the body is a String then honor quotes etc.
name|String
index|[]
name|tokens
init|=
name|StringQuoteHelper
operator|.
name|splitSafeQuote
argument_list|(
operator|(
name|String
operator|)
name|value
argument_list|,
name|separator
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|tokens
argument_list|)
decl_stmt|;
return|return
name|list
operator|.
name|iterator
argument_list|()
return|;
block|}
else|else
block|{
comment|// just use a regular iterator
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
name|Iterator
operator|.
name|class
argument_list|,
name|value
argument_list|)
return|;
block|}
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|populateStatement (PreparedStatement ps, Iterator<?> iterator, int expectedParams)
specifier|public
name|void
name|populateStatement
parameter_list|(
name|PreparedStatement
name|ps
parameter_list|,
name|Iterator
argument_list|<
name|?
argument_list|>
name|iterator
parameter_list|,
name|int
name|expectedParams
parameter_list|)
throws|throws
name|SQLException
block|{
if|if
condition|(
name|expectedParams
operator|<=
literal|0
condition|)
block|{
return|return;
block|}
specifier|final
name|Object
index|[]
name|args
init|=
operator|new
name|Object
index|[
name|expectedParams
index|]
decl_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
name|int
name|argNumber
init|=
literal|1
decl_stmt|;
while|while
condition|(
name|iterator
operator|!=
literal|null
operator|&&
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|value
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Setting parameter #{} with value: {}"
argument_list|,
name|argNumber
argument_list|,
name|value
argument_list|)
expr_stmt|;
if|if
condition|(
name|argNumber
operator|<=
name|expectedParams
condition|)
block|{
name|args
index|[
name|i
index|]
operator|=
name|value
expr_stmt|;
block|}
name|argNumber
operator|++
expr_stmt|;
name|i
operator|++
expr_stmt|;
block|}
if|if
condition|(
name|argNumber
operator|-
literal|1
operator|!=
name|expectedParams
condition|)
block|{
throw|throw
operator|new
name|SQLException
argument_list|(
literal|"Number of parameters mismatch. Expected: "
operator|+
name|expectedParams
operator|+
literal|", was: "
operator|+
operator|(
name|argNumber
operator|-
literal|1
operator|)
argument_list|)
throw|;
block|}
comment|// use argument setter as it deals with various JDBC drivers setObject vs setLong/setInteger/setString etc.
name|ArgumentPreparedStatementSetter
name|setter
init|=
operator|new
name|ArgumentPreparedStatementSetter
argument_list|(
name|args
argument_list|)
decl_stmt|;
name|setter
operator|.
name|setValues
argument_list|(
name|ps
argument_list|)
expr_stmt|;
block|}
DECL|method|hasNamedParameters (String query)
specifier|protected
name|boolean
name|hasNamedParameters
parameter_list|(
name|String
name|query
parameter_list|)
block|{
name|NamedQueryParser
name|parser
init|=
operator|new
name|NamedQueryParser
argument_list|(
name|query
argument_list|)
decl_stmt|;
return|return
name|parser
operator|.
name|next
argument_list|()
operator|!=
literal|null
return|;
block|}
DECL|class|NamedQueryParser
specifier|private
specifier|static
specifier|final
class|class
name|NamedQueryParser
block|{
DECL|field|PATTERN
specifier|private
specifier|static
specifier|final
name|Pattern
name|PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"\\:\\?(\\w+|\\$\\{[^\\}]+\\})"
argument_list|)
decl_stmt|;
DECL|field|matcher
specifier|private
specifier|final
name|Matcher
name|matcher
decl_stmt|;
DECL|method|NamedQueryParser (String query)
specifier|private
name|NamedQueryParser
parameter_list|(
name|String
name|query
parameter_list|)
block|{
name|this
operator|.
name|matcher
operator|=
name|PATTERN
operator|.
name|matcher
argument_list|(
name|query
argument_list|)
expr_stmt|;
block|}
DECL|method|next ()
specifier|public
name|String
name|next
parameter_list|()
block|{
if|if
condition|(
operator|!
name|matcher
operator|.
name|find
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|matcher
operator|.
name|group
argument_list|(
literal|1
argument_list|)
return|;
block|}
block|}
DECL|class|PopulateIterator
specifier|private
specifier|static
specifier|final
class|class
name|PopulateIterator
implements|implements
name|Iterator
argument_list|<
name|Object
argument_list|>
block|{
DECL|field|MISSING_PARAMETER_EXCEPTION
specifier|private
specifier|static
specifier|final
name|String
name|MISSING_PARAMETER_EXCEPTION
init|=
literal|"Cannot find key [%s] in message body or headers to use when setting named parameter in query [%s]"
decl_stmt|;
DECL|field|query
specifier|private
specifier|final
name|String
name|query
decl_stmt|;
DECL|field|parser
specifier|private
specifier|final
name|NamedQueryParser
name|parser
decl_stmt|;
DECL|field|exchange
specifier|private
specifier|final
name|Exchange
name|exchange
decl_stmt|;
DECL|field|bodyMap
specifier|private
specifier|final
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|bodyMap
decl_stmt|;
DECL|field|headersMap
specifier|private
specifier|final
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|headersMap
decl_stmt|;
DECL|field|nextParam
specifier|private
name|String
name|nextParam
decl_stmt|;
DECL|method|PopulateIterator (String query, Exchange exchange, Object body)
specifier|private
name|PopulateIterator
parameter_list|(
name|String
name|query
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Object
name|body
parameter_list|)
block|{
name|this
operator|.
name|query
operator|=
name|query
expr_stmt|;
name|this
operator|.
name|parser
operator|=
operator|new
name|NamedQueryParser
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
name|this
operator|.
name|bodyMap
operator|=
name|safeMap
argument_list|(
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
name|Map
operator|.
name|class
argument_list|,
name|body
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|headersMap
operator|=
name|safeMap
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
name|this
operator|.
name|nextParam
operator|=
name|parser
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|hasNext ()
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
return|return
name|nextParam
operator|!=
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|next ()
specifier|public
name|Object
name|next
parameter_list|()
block|{
if|if
condition|(
name|nextParam
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NoSuchElementException
argument_list|()
throw|;
block|}
try|try
block|{
if|if
condition|(
name|nextParam
operator|.
name|startsWith
argument_list|(
literal|"${"
argument_list|)
operator|&&
name|nextParam
operator|.
name|endsWith
argument_list|(
literal|"}"
argument_list|)
condition|)
block|{
return|return
name|SimpleLanguage
operator|.
name|expression
argument_list|(
name|nextParam
argument_list|)
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|Object
operator|.
name|class
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|bodyMap
operator|.
name|containsKey
argument_list|(
name|nextParam
argument_list|)
condition|)
block|{
return|return
name|bodyMap
operator|.
name|get
argument_list|(
name|nextParam
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|headersMap
operator|.
name|containsKey
argument_list|(
name|nextParam
argument_list|)
condition|)
block|{
return|return
name|headersMap
operator|.
name|get
argument_list|(
name|nextParam
argument_list|)
return|;
block|}
throw|throw
operator|new
name|RuntimeExchangeException
argument_list|(
name|String
operator|.
name|format
argument_list|(
name|MISSING_PARAMETER_EXCEPTION
argument_list|,
name|nextParam
argument_list|,
name|query
argument_list|)
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
finally|finally
block|{
name|nextParam
operator|=
name|parser
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|remove ()
specifier|public
name|void
name|remove
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
DECL|method|safeMap (Map<?, ?> map)
specifier|private
specifier|static
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|safeMap
parameter_list|(
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|map
parameter_list|)
block|{
return|return
operator|(
name|map
operator|==
literal|null
operator|||
name|map
operator|.
name|isEmpty
argument_list|()
operator|)
condition|?
name|Collections
operator|.
name|emptyMap
argument_list|()
else|:
name|map
return|;
block|}
block|}
block|}
end_class

end_unit

