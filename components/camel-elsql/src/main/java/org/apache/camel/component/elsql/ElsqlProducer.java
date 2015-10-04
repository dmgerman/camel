begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.elsql
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|elsql
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
name|ResultSet
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
name|List
import|;
end_import

begin_import
import|import
name|com
operator|.
name|opengamma
operator|.
name|elsql
operator|.
name|ElSql
import|;
end_import

begin_import
import|import
name|com
operator|.
name|opengamma
operator|.
name|elsql
operator|.
name|SpringSqlParams
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
name|component
operator|.
name|sql
operator|.
name|SqlConstants
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
name|sql
operator|.
name|SqlEndpoint
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
name|sql
operator|.
name|SqlOutputType
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
name|impl
operator|.
name|DefaultProducer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|dao
operator|.
name|DataAccessException
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
name|PreparedStatementCallback
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
name|namedparam
operator|.
name|NamedParameterJdbcTemplate
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
name|namedparam
operator|.
name|SqlParameterSource
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|springframework
operator|.
name|jdbc
operator|.
name|support
operator|.
name|JdbcUtils
operator|.
name|closeResultSet
import|;
end_import

begin_class
DECL|class|ElsqlProducer
specifier|public
class|class
name|ElsqlProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|elSql
specifier|private
specifier|final
name|ElSql
name|elSql
decl_stmt|;
DECL|field|elSqlName
specifier|private
specifier|final
name|String
name|elSqlName
decl_stmt|;
DECL|field|jdbcTemplate
specifier|private
specifier|final
name|NamedParameterJdbcTemplate
name|jdbcTemplate
decl_stmt|;
DECL|method|ElsqlProducer (SqlEndpoint endpoint, ElSql elSql, String elSqlName, NamedParameterJdbcTemplate jdbcTemplate)
specifier|public
name|ElsqlProducer
parameter_list|(
name|SqlEndpoint
name|endpoint
parameter_list|,
name|ElSql
name|elSql
parameter_list|,
name|String
name|elSqlName
parameter_list|,
name|NamedParameterJdbcTemplate
name|jdbcTemplate
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|elSql
operator|=
name|elSql
expr_stmt|;
name|this
operator|.
name|elSqlName
operator|=
name|elSqlName
expr_stmt|;
name|this
operator|.
name|jdbcTemplate
operator|=
name|jdbcTemplate
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|ElsqlEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|ElsqlEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|process (final Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|data
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
specifier|final
name|SqlParameterSource
name|param
init|=
operator|new
name|ElsqlSqlMapSource
argument_list|(
name|exchange
argument_list|,
name|data
argument_list|)
decl_stmt|;
specifier|final
name|String
name|sql
init|=
name|elSql
operator|.
name|getSql
argument_list|(
name|elSqlName
argument_list|,
operator|new
name|SpringSqlParams
argument_list|(
name|param
argument_list|)
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"ElSql @{} using sql: {}"
argument_list|,
name|elSqlName
argument_list|,
name|sql
argument_list|)
expr_stmt|;
name|jdbcTemplate
operator|.
name|execute
argument_list|(
name|sql
argument_list|,
name|param
argument_list|,
operator|new
name|PreparedStatementCallback
argument_list|<
name|Object
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Object
name|doInPreparedStatement
parameter_list|(
name|PreparedStatement
name|ps
parameter_list|)
throws|throws
name|SQLException
throws|,
name|DataAccessException
block|{
name|ResultSet
name|rs
init|=
literal|null
decl_stmt|;
try|try
block|{
name|boolean
name|isResultSet
init|=
name|ps
operator|.
name|execute
argument_list|()
decl_stmt|;
if|if
condition|(
name|isResultSet
condition|)
block|{
name|rs
operator|=
name|ps
operator|.
name|getResultSet
argument_list|()
expr_stmt|;
comment|// preserve headers first, so we can override the SQL_ROW_COUNT header
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
name|SqlOutputType
name|outputType
init|=
name|getEndpoint
argument_list|()
operator|.
name|getOutputType
argument_list|()
decl_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Got result list from query: {}, outputType={}"
argument_list|,
name|rs
argument_list|,
name|outputType
argument_list|)
expr_stmt|;
if|if
condition|(
name|outputType
operator|==
name|SqlOutputType
operator|.
name|SelectList
condition|)
block|{
name|List
argument_list|<
name|?
argument_list|>
name|data
init|=
name|getEndpoint
argument_list|()
operator|.
name|queryForList
argument_list|(
name|rs
argument_list|,
literal|true
argument_list|)
decl_stmt|;
comment|// for noop=true we still want to enrich with the row count header
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isNoop
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getOutputHeader
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getOutputHeader
argument_list|()
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SqlConstants
operator|.
name|SQL_ROW_COUNT
argument_list|,
name|data
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|outputType
operator|==
name|SqlOutputType
operator|.
name|SelectOne
condition|)
block|{
name|Object
name|data
init|=
name|getEndpoint
argument_list|()
operator|.
name|queryForObject
argument_list|(
name|rs
argument_list|)
decl_stmt|;
if|if
condition|(
name|data
operator|!=
literal|null
condition|)
block|{
comment|// for noop=true we still want to enrich with the row count header
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isNoop
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getOutputHeader
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getOutputHeader
argument_list|()
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SqlConstants
operator|.
name|SQL_ROW_COUNT
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Invalid outputType="
operator|+
name|outputType
argument_list|)
throw|;
block|}
block|}
block|}
finally|finally
block|{
name|closeResultSet
argument_list|(
name|rs
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

