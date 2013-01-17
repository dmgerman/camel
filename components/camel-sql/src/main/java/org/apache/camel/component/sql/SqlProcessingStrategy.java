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
name|JdbcTemplate
import|;
end_import

begin_comment
comment|/**  * Processing strategy for dealing with SQL when consuming.  */
end_comment

begin_interface
DECL|interface|SqlProcessingStrategy
specifier|public
interface|interface
name|SqlProcessingStrategy
block|{
comment|/**      * Commit callback if there are a query to be run after processing.      *      * @param endpoint     the endpoint      * @param exchange     The exchange after it has been processed      * @param data         The original data delivered to the route      * @param jdbcTemplate The JDBC template      * @param query        The SQL query to execute      * @throws Exception can be thrown in case of error      */
DECL|method|commit (SqlEndpoint endpoint, Exchange exchange, Object data, JdbcTemplate jdbcTemplate, String query)
name|void
name|commit
parameter_list|(
name|SqlEndpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Object
name|data
parameter_list|,
name|JdbcTemplate
name|jdbcTemplate
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

