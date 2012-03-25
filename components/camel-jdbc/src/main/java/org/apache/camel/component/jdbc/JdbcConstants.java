begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * JDBC Constants  */
end_comment

begin_class
DECL|class|JdbcConstants
specifier|public
specifier|final
class|class
name|JdbcConstants
block|{
DECL|field|JDBC_UPDATE_COUNT
specifier|public
specifier|static
specifier|final
name|String
name|JDBC_UPDATE_COUNT
init|=
literal|"CamelJdbcUpdateCount"
decl_stmt|;
DECL|field|JDBC_ROW_COUNT
specifier|public
specifier|static
specifier|final
name|String
name|JDBC_ROW_COUNT
init|=
literal|"CamelJdbcRowCount"
decl_stmt|;
comment|/**      * Boolean input header.      * Set its value to true to retrieve generated keys, default is false      */
DECL|field|JDBC_RETRIEVE_GENERATED_KEYS
specifier|public
specifier|static
specifier|final
name|String
name|JDBC_RETRIEVE_GENERATED_KEYS
init|=
literal|"CamelRetrieveGeneratedKeys"
decl_stmt|;
comment|/**      *<tt>String[]</tt> or<tt>int[]</tt> input header - optional      * Set it to specify the expected generated columns, see:      * @see<a href="http://docs.oracle.com/javase/6/docs/api/java/sql/Statement.html#execute(java.lang.String, int[])">      *     java.sql.Statement.execute(java.lang.String, int[])</a>      * @see<a href="http://docs.oracle.com/javase/6/docs/api/java/sql/Statement.html#execute(java.lang.String, java.lang.String[])">      *     java.sql.Statement.execute(java.lang.String, java.lang.String[])</a>      */
DECL|field|JDBC_GENERATED_COLUMNS
specifier|public
specifier|static
specifier|final
name|String
name|JDBC_GENERATED_COLUMNS
init|=
literal|"CamelGeneratedColumns"
decl_stmt|;
comment|/**      * int output header giving the number of rows of generated keys      */
DECL|field|JDBC_GENERATED_KEYS_ROW_COUNT
specifier|public
specifier|static
specifier|final
name|String
name|JDBC_GENERATED_KEYS_ROW_COUNT
init|=
literal|"CamelGeneratedKeysRowCount"
decl_stmt|;
comment|/**      *<tt>List<Map<String, Object>></tt> output header containing the generated keys retrieved      */
DECL|field|JDBC_GENERATED_KEYS_DATA
specifier|public
specifier|static
specifier|final
name|String
name|JDBC_GENERATED_KEYS_DATA
init|=
literal|"CamelGeneratedKeysRows"
decl_stmt|;
DECL|method|JdbcConstants ()
specifier|private
name|JdbcConstants
parameter_list|()
block|{
comment|// Utility class
block|}
block|}
end_class

end_unit

