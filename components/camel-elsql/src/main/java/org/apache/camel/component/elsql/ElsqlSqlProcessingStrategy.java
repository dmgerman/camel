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
name|SQLException
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
name|SqlProcessingStrategy
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
name|JdbcTemplate
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

begin_class
DECL|class|ElsqlSqlProcessingStrategy
specifier|public
class|class
name|ElsqlSqlProcessingStrategy
implements|implements
name|SqlProcessingStrategy
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
name|ElsqlSqlProcessingStrategy
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|elSqlName
specifier|private
specifier|final
name|String
name|elSqlName
decl_stmt|;
DECL|field|elSql
specifier|private
specifier|final
name|ElSql
name|elSql
decl_stmt|;
DECL|method|ElsqlSqlProcessingStrategy (String elSqlName, ElSql elSql)
specifier|public
name|ElsqlSqlProcessingStrategy
parameter_list|(
name|String
name|elSqlName
parameter_list|,
name|ElSql
name|elSql
parameter_list|)
block|{
name|this
operator|.
name|elSqlName
operator|=
name|elSqlName
expr_stmt|;
name|this
operator|.
name|elSql
operator|=
name|elSql
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|commit (final SqlEndpoint endpoint, final Exchange exchange, final Object data, final JdbcTemplate jdbcTemplate, final String query)
specifier|public
name|int
name|commit
parameter_list|(
specifier|final
name|SqlEndpoint
name|endpoint
parameter_list|,
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|Object
name|data
parameter_list|,
specifier|final
name|JdbcTemplate
name|jdbcTemplate
parameter_list|,
specifier|final
name|String
name|query
parameter_list|)
throws|throws
name|Exception
block|{
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
name|ElsqlSqlParams
argument_list|(
name|exchange
argument_list|,
name|data
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|jdbcTemplate
operator|.
name|execute
argument_list|(
name|sql
argument_list|,
operator|new
name|PreparedStatementCallback
argument_list|<
name|Integer
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Integer
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
name|ps
operator|.
name|execute
argument_list|()
expr_stmt|;
name|int
name|updateCount
init|=
name|ps
operator|.
name|getUpdateCount
argument_list|()
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Update count {}"
argument_list|,
name|updateCount
argument_list|)
expr_stmt|;
block|}
return|return
name|updateCount
return|;
block|}
block|}
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|commitBatchComplete (final SqlEndpoint endpoint, final JdbcTemplate jdbcTemplate, final String query)
specifier|public
name|int
name|commitBatchComplete
parameter_list|(
specifier|final
name|SqlEndpoint
name|endpoint
parameter_list|,
specifier|final
name|JdbcTemplate
name|jdbcTemplate
parameter_list|,
specifier|final
name|String
name|query
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|0
return|;
block|}
block|}
end_class

end_unit

