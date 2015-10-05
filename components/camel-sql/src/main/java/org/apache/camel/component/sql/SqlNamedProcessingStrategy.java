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

begin_comment
comment|/**  * Extended processing strategy for dealing with SQL when consuming, which uses a {@link NamedParameterJdbcTemplate}  * instead of plain {@link org.springframework.jdbc.core.JdbcTemplate}.  */
end_comment

begin_interface
DECL|interface|SqlNamedProcessingStrategy
specifier|public
interface|interface
name|SqlNamedProcessingStrategy
extends|extends
name|SqlProcessingStrategy
block|{
comment|/**      * Commit callback if there are a query to be run after processing.      *      * @param endpoint          the endpoint      * @param exchange          The exchange after it has been processed      * @param data              The original data delivered to the route      * @param namedJdbcTemplate The JDBC template      * @param parameterSource   Parameter sources for the named JDBC template      * @param query             The SQL query to execute      * @return the update count if the query returned an update count      * @throws Exception can be thrown in case of error      */
DECL|method|commit (DefaultSqlEndpoint endpoint, Exchange exchange, Object data, NamedParameterJdbcTemplate namedJdbcTemplate, SqlParameterSource parameterSource, String query)
name|int
name|commit
parameter_list|(
name|DefaultSqlEndpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Object
name|data
parameter_list|,
name|NamedParameterJdbcTemplate
name|namedJdbcTemplate
parameter_list|,
name|SqlParameterSource
name|parameterSource
parameter_list|,
name|String
name|query
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Commit callback when the batch is complete. This allows you to do one extra query after all rows has been processed in the batch.      *      * @param endpoint          the endpoint      * @param namedJdbcTemplate The JDBC template      * @param parameterSource   Parameter sources for the named JDBC template      * @param query             The SQL query to execute      * @return the update count if the query returned an update count      * @throws Exception can be thrown in case of error      */
DECL|method|commitBatchComplete (DefaultSqlEndpoint endpoint, NamedParameterJdbcTemplate namedJdbcTemplate, SqlParameterSource parameterSource, String query)
name|int
name|commitBatchComplete
parameter_list|(
name|DefaultSqlEndpoint
name|endpoint
parameter_list|,
name|NamedParameterJdbcTemplate
name|namedJdbcTemplate
parameter_list|,
name|SqlParameterSource
name|parameterSource
parameter_list|,
name|String
name|query
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

