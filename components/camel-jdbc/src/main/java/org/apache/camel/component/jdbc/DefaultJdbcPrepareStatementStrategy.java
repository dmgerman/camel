begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jdbc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jdbc
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
name|Iterator
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
comment|/**  * Default {@link JdbcPrepareStatementStrategy} which is a copy from the camel-sql component having  * this functionality first.  */
end_comment

begin_class
DECL|class|DefaultJdbcPrepareStatementStrategy
specifier|public
class|class
name|DefaultJdbcPrepareStatementStrategy
implements|implements
name|JdbcPrepareStatementStrategy
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
name|DefaultJdbcPrepareStatementStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
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
comment|// replace all :?word with just ?
name|answer
operator|=
name|query
operator|.
name|replaceAll
argument_list|(
literal|"\\:\\?\\w+"
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
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|map
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|hasHeaders
argument_list|()
condition|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JdbcConstants
operator|.
name|JDBC_PARAMETERS
argument_list|)
operator|!=
literal|null
condition|)
block|{
comment|// header JDBC_PARAMETERS takes precedence over regular headers
name|map
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JdbcConstants
operator|.
name|JDBC_PARAMETERS
argument_list|,
name|Map
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|map
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeaders
argument_list|()
expr_stmt|;
block|}
block|}
specifier|final
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|headerMap
init|=
name|map
decl_stmt|;
if|if
condition|(
name|hasNamedParameters
argument_list|(
name|query
argument_list|)
condition|)
block|{
comment|// create an iterator that returns the value in the named order
try|try
block|{
return|return
operator|new
name|Iterator
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
specifier|private
name|NamedQueryParser
name|parser
init|=
operator|new
name|NamedQueryParser
argument_list|(
name|query
argument_list|)
decl_stmt|;
specifier|private
name|Object
name|next
decl_stmt|;
specifier|private
name|boolean
name|done
decl_stmt|;
specifier|private
name|boolean
name|preFetched
decl_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|hasNext
parameter_list|()
block|{
if|if
condition|(
operator|!
name|done
operator|&&
operator|!
name|preFetched
condition|)
block|{
name|next
argument_list|()
expr_stmt|;
name|preFetched
operator|=
literal|true
expr_stmt|;
block|}
return|return
operator|!
name|done
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|next
parameter_list|()
block|{
if|if
condition|(
operator|!
name|preFetched
condition|)
block|{
name|String
name|key
init|=
name|parser
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|key
operator|==
literal|null
condition|)
block|{
name|done
operator|=
literal|true
expr_stmt|;
return|return
literal|null
return|;
block|}
comment|// the key is expected to exist, if not report so end user can see this
name|boolean
name|contains
init|=
name|headerMap
operator|!=
literal|null
operator|&&
name|headerMap
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|contains
condition|)
block|{
throw|throw
operator|new
name|RuntimeExchangeException
argument_list|(
literal|"Cannot find key ["
operator|+
name|key
operator|+
literal|"] in message body or headers to use when setting named parameter in query ["
operator|+
name|query
operator|+
literal|"]"
argument_list|,
name|exchange
argument_list|)
throw|;
block|}
name|next
operator|=
name|headerMap
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
name|preFetched
operator|=
literal|false
expr_stmt|;
return|return
name|next
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|remove
parameter_list|()
block|{
comment|// noop
block|}
block|}
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
name|SQLException
argument_list|(
literal|"Error iterating parameters for the query: "
operator|+
name|query
argument_list|,
name|e
argument_list|)
throw|;
block|}
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
name|headerMap
operator|!=
literal|null
condition|?
name|headerMap
operator|.
name|values
argument_list|()
else|:
literal|null
argument_list|)
return|;
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
name|int
name|argNumber
init|=
literal|1
decl_stmt|;
if|if
condition|(
name|expectedParams
operator|>
literal|0
condition|)
block|{
comment|// as the headers may have more values than the SQL needs we just break out when we reached the expected number
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
operator|&&
name|argNumber
operator|<=
name|expectedParams
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
name|ps
operator|.
name|setObject
argument_list|(
name|argNumber
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|argNumber
operator|++
expr_stmt|;
block|}
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
literal|", was:"
operator|+
operator|(
name|argNumber
operator|-
literal|1
operator|)
argument_list|)
throw|;
block|}
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
literal|"\\:\\?(\\w+)"
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
block|}
end_class

end_unit

