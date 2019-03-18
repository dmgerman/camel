begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * SQL Constants.  */
end_comment

begin_class
DECL|class|SqlConstants
specifier|public
specifier|final
class|class
name|SqlConstants
block|{
DECL|field|SQL_QUERY
specifier|public
specifier|static
specifier|final
name|String
name|SQL_QUERY
init|=
literal|"CamelSqlQuery"
decl_stmt|;
DECL|field|SQL_UPDATE_COUNT
specifier|public
specifier|static
specifier|final
name|String
name|SQL_UPDATE_COUNT
init|=
literal|"CamelSqlUpdateCount"
decl_stmt|;
DECL|field|SQL_ROW_COUNT
specifier|public
specifier|static
specifier|final
name|String
name|SQL_ROW_COUNT
init|=
literal|"CamelSqlRowCount"
decl_stmt|;
comment|/**      * Boolean input header.      * Set its value to true to retrieve generated keys, default is false      */
DECL|field|SQL_RETRIEVE_GENERATED_KEYS
specifier|public
specifier|static
specifier|final
name|String
name|SQL_RETRIEVE_GENERATED_KEYS
init|=
literal|"CamelSqlRetrieveGeneratedKeys"
decl_stmt|;
comment|/**      *<tt>String[]</tt> or<tt>int[]</tt> input header - optional      * Set it to specify the expected generated columns, see:      *      * @see<a href="http://docs.oracle.com/javase/6/docs/api/java/sql/Statement.html#execute(java.lang.String, int[])">      *      java.sql.Statement.execute(java.lang.String, int[])</a>      * @see<a href="http://docs.oracle.com/javase/6/docs/api/java/sql/Statement.html#execute(java.lang.String, java.lang.String[])">      *      java.sql.Statement.execute(java.lang.String, java.lang.String[])</a>      */
DECL|field|SQL_GENERATED_COLUMNS
specifier|public
specifier|static
specifier|final
name|String
name|SQL_GENERATED_COLUMNS
init|=
literal|"CamelSqlGeneratedColumns"
decl_stmt|;
comment|/**      * int output header giving the number of rows of generated keys      */
DECL|field|SQL_GENERATED_KEYS_ROW_COUNT
specifier|public
specifier|static
specifier|final
name|String
name|SQL_GENERATED_KEYS_ROW_COUNT
init|=
literal|"CamelSqlGeneratedKeysRowCount"
decl_stmt|;
comment|/**      *<tt>List<Map<String, Object>></tt> output header containing the generated keys retrieved      */
DECL|field|SQL_GENERATED_KEYS_DATA
specifier|public
specifier|static
specifier|final
name|String
name|SQL_GENERATED_KEYS_DATA
init|=
literal|"CamelSqlGeneratedKeyRows"
decl_stmt|;
comment|/**      * The SQL parameters when using the option useMessageBodyForSql      */
DECL|field|SQL_PARAMETERS
specifier|public
specifier|static
specifier|final
name|String
name|SQL_PARAMETERS
init|=
literal|"CamelSqlParameters"
decl_stmt|;
DECL|method|SqlConstants ()
specifier|private
name|SqlConstants
parameter_list|()
block|{
comment|// Utility class
block|}
block|}
end_class

end_unit

