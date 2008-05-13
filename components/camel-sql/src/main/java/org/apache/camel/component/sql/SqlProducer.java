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
name|impl
operator|.
name|DefaultExchange
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
name|ColumnMapRowMapper
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
name|RowMapperResultSetExtractor
import|;
end_import

begin_class
DECL|class|SqlProducer
specifier|public
class|class
name|SqlProducer
extends|extends
name|DefaultProducer
argument_list|<
name|DefaultExchange
argument_list|>
block|{
DECL|field|UPDATE_COUNT
specifier|public
specifier|static
specifier|final
name|String
name|UPDATE_COUNT
init|=
literal|"org.apache.camel.sql.update-count"
decl_stmt|;
DECL|field|query
specifier|private
name|String
name|query
decl_stmt|;
DECL|field|jdbcTemplate
specifier|private
name|JdbcTemplate
name|jdbcTemplate
decl_stmt|;
DECL|method|SqlProducer (SqlEndpoint endpoint, String query, JdbcTemplate jdbcTemplate)
specifier|public
name|SqlProducer
parameter_list|(
name|SqlEndpoint
name|endpoint
parameter_list|,
name|String
name|query
parameter_list|,
name|JdbcTemplate
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
name|jdbcTemplate
operator|=
name|jdbcTemplate
expr_stmt|;
name|this
operator|.
name|query
operator|=
name|query
expr_stmt|;
block|}
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
name|jdbcTemplate
operator|.
name|execute
argument_list|(
name|query
argument_list|,
operator|new
name|PreparedStatementCallback
argument_list|()
block|{
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
name|int
name|argNumber
init|=
literal|1
decl_stmt|;
for|for
control|(
name|Iterator
argument_list|<
name|?
argument_list|>
name|i
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Iterator
operator|.
name|class
argument_list|)
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|ps
operator|.
name|setObject
argument_list|(
name|argNumber
operator|++
argument_list|,
name|i
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|RowMapperResultSetExtractor
name|mapper
init|=
operator|new
name|RowMapperResultSetExtractor
argument_list|(
operator|new
name|ColumnMapRowMapper
argument_list|()
argument_list|)
decl_stmt|;
name|List
name|result
init|=
operator|(
name|List
operator|)
name|mapper
operator|.
name|extractData
argument_list|(
name|ps
operator|.
name|getResultSet
argument_list|()
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|UPDATE_COUNT
argument_list|,
name|ps
operator|.
name|getUpdateCount
argument_list|()
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

