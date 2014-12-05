begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.sql
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|sql
package|;
end_package

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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|josql
operator|.
name|Query
import|;
end_import

begin_import
import|import
name|org
operator|.
name|josql
operator|.
name|QueryExecutionException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|josql
operator|.
name|QueryParseException
import|;
end_import

begin_comment
comment|/**  * A builder of SQL {@link org.apache.camel.Expression} and  * {@link org.apache.camel.Predicate} implementations  *   * @version   */
end_comment

begin_class
DECL|class|SqlBuilder
specifier|public
class|class
name|SqlBuilder
implements|implements
name|Expression
implements|,
name|Predicate
block|{
DECL|field|query
specifier|private
name|Query
name|query
decl_stmt|;
DECL|field|variables
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|variables
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
DECL|method|SqlBuilder (Query query)
specifier|public
name|SqlBuilder
parameter_list|(
name|Query
name|query
parameter_list|)
block|{
name|this
operator|.
name|query
operator|=
name|query
expr_stmt|;
block|}
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
name|Object
name|result
init|=
name|evaluateQuery
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
name|result
argument_list|)
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
name|List
argument_list|<
name|?
argument_list|>
name|list
init|=
name|evaluateQuery
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
name|List
argument_list|<
name|?
argument_list|>
name|list
init|=
name|evaluateQuery
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
literal|" as found "
operator|+
name|list
argument_list|)
throw|;
block|}
block|}
comment|// Builder API
comment|// -----------------------------------------------------------------------
comment|/**      * Creates a new builder for the given SQL query string      *       * @param sql the SQL query to perform      * @return a new builder      * @throws QueryParseException if there is an issue with the SQL      */
DECL|method|sql (String sql)
specifier|public
specifier|static
name|SqlBuilder
name|sql
parameter_list|(
name|String
name|sql
parameter_list|)
throws|throws
name|QueryParseException
block|{
name|Query
name|q
init|=
operator|new
name|Query
argument_list|()
decl_stmt|;
name|q
operator|.
name|parse
argument_list|(
name|sql
argument_list|)
expr_stmt|;
return|return
operator|new
name|SqlBuilder
argument_list|(
name|q
argument_list|)
return|;
block|}
comment|/**      * Adds the variable value to be used by the SQL query      */
DECL|method|variable (String name, Object value)
specifier|public
name|SqlBuilder
name|variable
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|getVariables
argument_list|()
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
comment|// Properties
comment|// -----------------------------------------------------------------------
DECL|method|getVariables ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getVariables
parameter_list|()
block|{
return|return
name|variables
return|;
block|}
DECL|method|setVariables (Map<String, Object> properties)
specifier|public
name|void
name|setVariables
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
name|this
operator|.
name|variables
operator|=
name|properties
expr_stmt|;
block|}
comment|// Implementation methods
comment|// -----------------------------------------------------------------------
DECL|method|matches (Exchange exchange, List<?> list)
specifier|protected
name|boolean
name|matches
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|List
argument_list|<
name|?
argument_list|>
name|list
parameter_list|)
block|{
return|return
name|ObjectHelper
operator|.
name|matches
argument_list|(
name|list
argument_list|)
return|;
block|}
DECL|method|evaluateQuery (Exchange exchange)
specifier|protected
name|List
argument_list|<
name|?
argument_list|>
name|evaluateQuery
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|configureQuery
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
name|Message
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|?
argument_list|>
name|list
init|=
name|in
operator|.
name|getBody
argument_list|(
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|list
operator|==
literal|null
condition|)
block|{
name|list
operator|=
name|Collections
operator|.
name|singletonList
argument_list|(
name|in
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
return|return
name|query
operator|.
name|execute
argument_list|(
name|list
argument_list|)
operator|.
name|getResults
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|QueryExecutionException
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
DECL|method|configureQuery (Exchange exchange)
specifier|protected
name|void
name|configureQuery
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
comment|// lets pass in the headers as variables that the SQL can use
name|addVariables
argument_list|(
name|exchange
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
name|addVariables
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
name|addVariables
argument_list|(
name|getVariables
argument_list|()
argument_list|)
expr_stmt|;
name|query
operator|.
name|setVariable
argument_list|(
literal|"exchange"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|query
operator|.
name|setVariable
argument_list|(
literal|"in"
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|)
expr_stmt|;
comment|// To avoid the side effect of creating out message without notice
if|if
condition|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|)
block|{
name|query
operator|.
name|setVariable
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
DECL|method|addVariables (Map<String, Object> map)
specifier|protected
name|void
name|addVariables
parameter_list|(
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
name|query
operator|.
name|setVariable
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
block|}
end_class

end_unit

