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

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Connection
import|;
end_import

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
name|SQLDataException
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
name|sql
operator|.
name|Statement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|LinkedHashMap
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
name|javax
operator|.
name|sql
operator|.
name|DataSource
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
name|DefaultProducer
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
name|spi
operator|.
name|Synchronization
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
name|IntrospectionSupport
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
comment|/**  * @version  */
end_comment

begin_class
DECL|class|JdbcProducer
specifier|public
class|class
name|JdbcProducer
extends|extends
name|DefaultProducer
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
name|JdbcProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|dataSource
specifier|private
name|DataSource
name|dataSource
decl_stmt|;
DECL|field|readSize
specifier|private
name|int
name|readSize
decl_stmt|;
DECL|field|parameters
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
decl_stmt|;
DECL|method|JdbcProducer (JdbcEndpoint endpoint, DataSource dataSource, int readSize, Map<String, Object> parameters)
specifier|public
name|JdbcProducer
parameter_list|(
name|JdbcEndpoint
name|endpoint
parameter_list|,
name|DataSource
name|dataSource
parameter_list|,
name|int
name|readSize
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|dataSource
operator|=
name|dataSource
expr_stmt|;
name|this
operator|.
name|readSize
operator|=
name|readSize
expr_stmt|;
name|this
operator|.
name|parameters
operator|=
name|parameters
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|JdbcEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|JdbcEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
comment|/**      * Execute sql of exchange and set results on output      */
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isResetAutoCommit
argument_list|()
condition|)
block|{
name|processingSqlBySettingAutoCommit
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|processingSqlWithoutSettingAutoCommit
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|processingSqlBySettingAutoCommit (Exchange exchange)
specifier|private
name|void
name|processingSqlBySettingAutoCommit
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|sql
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Connection
name|conn
init|=
literal|null
decl_stmt|;
name|Boolean
name|autoCommit
init|=
literal|null
decl_stmt|;
name|boolean
name|shouldCloseResources
init|=
literal|true
decl_stmt|;
try|try
block|{
name|conn
operator|=
name|dataSource
operator|.
name|getConnection
argument_list|()
expr_stmt|;
name|autoCommit
operator|=
name|conn
operator|.
name|getAutoCommit
argument_list|()
expr_stmt|;
if|if
condition|(
name|autoCommit
condition|)
block|{
name|conn
operator|.
name|setAutoCommit
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
name|shouldCloseResources
operator|=
name|createAndExecuteSqlStatement
argument_list|(
name|exchange
argument_list|,
name|sql
argument_list|,
name|conn
argument_list|)
expr_stmt|;
name|conn
operator|.
name|commit
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
name|conn
operator|!=
literal|null
condition|)
block|{
name|conn
operator|.
name|rollback
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|SQLException
name|sqle
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error occurred during jdbc rollback. This exception will be ignored."
argument_list|,
name|sqle
argument_list|)
expr_stmt|;
block|}
throw|throw
name|e
throw|;
block|}
finally|finally
block|{
if|if
condition|(
name|shouldCloseResources
condition|)
block|{
name|resetAutoCommit
argument_list|(
name|conn
argument_list|,
name|autoCommit
argument_list|)
expr_stmt|;
name|closeQuietly
argument_list|(
name|conn
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|processingSqlWithoutSettingAutoCommit (Exchange exchange)
specifier|private
name|void
name|processingSqlWithoutSettingAutoCommit
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|sql
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|Connection
name|conn
init|=
literal|null
decl_stmt|;
name|boolean
name|shouldCloseResources
init|=
literal|true
decl_stmt|;
try|try
block|{
name|conn
operator|=
name|dataSource
operator|.
name|getConnection
argument_list|()
expr_stmt|;
name|shouldCloseResources
operator|=
name|createAndExecuteSqlStatement
argument_list|(
name|exchange
argument_list|,
name|sql
argument_list|,
name|conn
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|shouldCloseResources
condition|)
block|{
name|closeQuietly
argument_list|(
name|conn
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|createAndExecuteSqlStatement (Exchange exchange, String sql, Connection conn)
specifier|private
name|boolean
name|createAndExecuteSqlStatement
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|sql
parameter_list|,
name|Connection
name|conn
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isUseHeadersAsParameters
argument_list|()
condition|)
block|{
return|return
name|doCreateAndExecuteSqlStatementWithHeaders
argument_list|(
name|exchange
argument_list|,
name|sql
argument_list|,
name|conn
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|doCreateAndExecuteSqlStatement
argument_list|(
name|exchange
argument_list|,
name|sql
argument_list|,
name|conn
argument_list|)
return|;
block|}
block|}
DECL|method|doCreateAndExecuteSqlStatementWithHeaders (Exchange exchange, String sql, Connection conn)
specifier|private
name|boolean
name|doCreateAndExecuteSqlStatementWithHeaders
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|sql
parameter_list|,
name|Connection
name|conn
parameter_list|)
throws|throws
name|Exception
block|{
name|PreparedStatement
name|ps
init|=
literal|null
decl_stmt|;
name|ResultSet
name|rs
init|=
literal|null
decl_stmt|;
name|boolean
name|shouldCloseResources
init|=
literal|true
decl_stmt|;
try|try
block|{
specifier|final
name|String
name|preparedQuery
init|=
name|getEndpoint
argument_list|()
operator|.
name|getPrepareStatementStrategy
argument_list|()
operator|.
name|prepareQuery
argument_list|(
name|sql
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|isAllowNamedParameters
argument_list|()
argument_list|)
decl_stmt|;
name|Boolean
name|shouldRetrieveGeneratedKeys
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JdbcConstants
operator|.
name|JDBC_RETRIEVE_GENERATED_KEYS
argument_list|,
literal|false
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|shouldRetrieveGeneratedKeys
condition|)
block|{
name|Object
name|expectedGeneratedColumns
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JdbcConstants
operator|.
name|JDBC_GENERATED_COLUMNS
argument_list|)
decl_stmt|;
if|if
condition|(
name|expectedGeneratedColumns
operator|==
literal|null
condition|)
block|{
name|ps
operator|=
name|conn
operator|.
name|prepareStatement
argument_list|(
name|preparedQuery
argument_list|,
name|Statement
operator|.
name|RETURN_GENERATED_KEYS
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|expectedGeneratedColumns
operator|instanceof
name|String
index|[]
condition|)
block|{
name|ps
operator|=
name|conn
operator|.
name|prepareStatement
argument_list|(
name|preparedQuery
argument_list|,
operator|(
name|String
index|[]
operator|)
name|expectedGeneratedColumns
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|expectedGeneratedColumns
operator|instanceof
name|int
index|[]
condition|)
block|{
name|ps
operator|=
name|conn
operator|.
name|prepareStatement
argument_list|(
name|preparedQuery
argument_list|,
operator|(
name|int
index|[]
operator|)
name|expectedGeneratedColumns
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Header specifying expected returning columns isn't an instance of String[] or int[] but "
operator|+
name|expectedGeneratedColumns
operator|.
name|getClass
argument_list|()
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|ps
operator|=
name|conn
operator|.
name|prepareStatement
argument_list|(
name|preparedQuery
argument_list|)
expr_stmt|;
block|}
name|int
name|expectedCount
init|=
name|ps
operator|.
name|getParameterMetaData
argument_list|()
operator|.
name|getParameterCount
argument_list|()
decl_stmt|;
if|if
condition|(
name|expectedCount
operator|>
literal|0
condition|)
block|{
name|Iterator
argument_list|<
name|?
argument_list|>
name|it
init|=
name|getEndpoint
argument_list|()
operator|.
name|getPrepareStatementStrategy
argument_list|()
operator|.
name|createPopulateIterator
argument_list|(
name|sql
argument_list|,
name|preparedQuery
argument_list|,
name|expectedCount
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
decl_stmt|;
name|getEndpoint
argument_list|()
operator|.
name|getPrepareStatementStrategy
argument_list|()
operator|.
name|populateStatement
argument_list|(
name|ps
argument_list|,
name|it
argument_list|,
name|expectedCount
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Executing JDBC PreparedStatement: {}"
argument_list|,
name|sql
argument_list|)
expr_stmt|;
name|boolean
name|stmtExecutionResult
init|=
name|ps
operator|.
name|execute
argument_list|()
decl_stmt|;
if|if
condition|(
name|stmtExecutionResult
condition|)
block|{
name|rs
operator|=
name|ps
operator|.
name|getResultSet
argument_list|()
expr_stmt|;
name|setResultSet
argument_list|(
name|exchange
argument_list|,
name|rs
argument_list|)
expr_stmt|;
name|shouldCloseResources
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|int
name|updateCount
init|=
name|ps
operator|.
name|getUpdateCount
argument_list|()
decl_stmt|;
comment|// preserve headers
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
comment|// and then set the new header
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|JdbcConstants
operator|.
name|JDBC_UPDATE_COUNT
argument_list|,
name|updateCount
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|shouldRetrieveGeneratedKeys
condition|)
block|{
name|setGeneratedKeys
argument_list|(
name|exchange
argument_list|,
name|ps
operator|.
name|getGeneratedKeys
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|shouldCloseResources
condition|)
block|{
name|closeQuietly
argument_list|(
name|rs
argument_list|)
expr_stmt|;
name|closeQuietly
argument_list|(
name|ps
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|shouldCloseResources
return|;
block|}
DECL|method|doCreateAndExecuteSqlStatement (Exchange exchange, String sql, Connection conn)
specifier|private
name|boolean
name|doCreateAndExecuteSqlStatement
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|sql
parameter_list|,
name|Connection
name|conn
parameter_list|)
throws|throws
name|Exception
block|{
name|Statement
name|stmt
init|=
literal|null
decl_stmt|;
name|ResultSet
name|rs
init|=
literal|null
decl_stmt|;
name|boolean
name|shouldCloseResources
init|=
literal|true
decl_stmt|;
try|try
block|{
name|stmt
operator|=
name|conn
operator|.
name|createStatement
argument_list|()
expr_stmt|;
if|if
condition|(
name|parameters
operator|!=
literal|null
operator|&&
operator|!
name|parameters
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|stmt
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Executing JDBC Statement: {}"
argument_list|,
name|sql
argument_list|)
expr_stmt|;
name|Boolean
name|shouldRetrieveGeneratedKeys
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JdbcConstants
operator|.
name|JDBC_RETRIEVE_GENERATED_KEYS
argument_list|,
literal|false
argument_list|,
name|Boolean
operator|.
name|class
argument_list|)
decl_stmt|;
name|boolean
name|stmtExecutionResult
decl_stmt|;
if|if
condition|(
name|shouldRetrieveGeneratedKeys
condition|)
block|{
name|Object
name|expectedGeneratedColumns
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|JdbcConstants
operator|.
name|JDBC_GENERATED_COLUMNS
argument_list|)
decl_stmt|;
if|if
condition|(
name|expectedGeneratedColumns
operator|==
literal|null
condition|)
block|{
name|stmtExecutionResult
operator|=
name|stmt
operator|.
name|execute
argument_list|(
name|sql
argument_list|,
name|Statement
operator|.
name|RETURN_GENERATED_KEYS
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|expectedGeneratedColumns
operator|instanceof
name|String
index|[]
condition|)
block|{
name|stmtExecutionResult
operator|=
name|stmt
operator|.
name|execute
argument_list|(
name|sql
argument_list|,
operator|(
name|String
index|[]
operator|)
name|expectedGeneratedColumns
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|expectedGeneratedColumns
operator|instanceof
name|int
index|[]
condition|)
block|{
name|stmtExecutionResult
operator|=
name|stmt
operator|.
name|execute
argument_list|(
name|sql
argument_list|,
operator|(
name|int
index|[]
operator|)
name|expectedGeneratedColumns
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Header specifying expected returning columns isn't an instance of String[] or int[] but "
operator|+
name|expectedGeneratedColumns
operator|.
name|getClass
argument_list|()
argument_list|)
throw|;
block|}
block|}
else|else
block|{
name|stmtExecutionResult
operator|=
name|stmt
operator|.
name|execute
argument_list|(
name|sql
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|stmtExecutionResult
condition|)
block|{
name|rs
operator|=
name|stmt
operator|.
name|getResultSet
argument_list|()
expr_stmt|;
name|setResultSet
argument_list|(
name|exchange
argument_list|,
name|rs
argument_list|)
expr_stmt|;
name|shouldCloseResources
operator|=
literal|false
expr_stmt|;
block|}
else|else
block|{
name|int
name|updateCount
init|=
name|stmt
operator|.
name|getUpdateCount
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|JdbcConstants
operator|.
name|JDBC_UPDATE_COUNT
argument_list|,
name|updateCount
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|shouldRetrieveGeneratedKeys
condition|)
block|{
name|setGeneratedKeys
argument_list|(
name|exchange
argument_list|,
name|stmt
operator|.
name|getGeneratedKeys
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|shouldCloseResources
condition|)
block|{
name|closeQuietly
argument_list|(
name|rs
argument_list|)
expr_stmt|;
name|closeQuietly
argument_list|(
name|stmt
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|shouldCloseResources
return|;
block|}
DECL|method|closeQuietly (ResultSet rs)
specifier|private
name|void
name|closeQuietly
parameter_list|(
name|ResultSet
name|rs
parameter_list|)
block|{
if|if
condition|(
name|rs
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|rs
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|sqle
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error by closing result set: "
operator|+
name|sqle
argument_list|,
name|sqle
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|closeQuietly (Statement stmt)
specifier|private
name|void
name|closeQuietly
parameter_list|(
name|Statement
name|stmt
parameter_list|)
block|{
if|if
condition|(
name|stmt
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|stmt
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|sqle
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error by closing statement: "
operator|+
name|sqle
argument_list|,
name|sqle
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|resetAutoCommit (Connection con, Boolean autoCommit)
specifier|private
name|void
name|resetAutoCommit
parameter_list|(
name|Connection
name|con
parameter_list|,
name|Boolean
name|autoCommit
parameter_list|)
block|{
if|if
condition|(
name|con
operator|!=
literal|null
operator|&&
name|autoCommit
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|con
operator|.
name|setAutoCommit
argument_list|(
name|autoCommit
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|sqle
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error by resetting auto commit to its original value: "
operator|+
name|sqle
argument_list|,
name|sqle
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|closeQuietly (Connection con)
specifier|private
name|void
name|closeQuietly
parameter_list|(
name|Connection
name|con
parameter_list|)
block|{
if|if
condition|(
name|con
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|con
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|sqle
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error by closing connection: "
operator|+
name|sqle
argument_list|,
name|sqle
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Sets the generated if any to the Exchange in headers :      * - {@link JdbcConstants#JDBC_GENERATED_KEYS_ROW_COUNT} : the row count of generated keys      * - {@link JdbcConstants#JDBC_GENERATED_KEYS_DATA} : the generated keys data      *      * @param exchange The exchange where to store the generated keys      * @param generatedKeys The result set containing the generated keys      */
DECL|method|setGeneratedKeys (Exchange exchange, ResultSet generatedKeys)
specifier|protected
name|void
name|setGeneratedKeys
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|ResultSet
name|generatedKeys
parameter_list|)
throws|throws
name|SQLException
block|{
if|if
condition|(
name|generatedKeys
operator|!=
literal|null
condition|)
block|{
name|ResultSetIterator
name|iterator
init|=
operator|new
name|ResultSetIterator
argument_list|(
name|generatedKeys
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|isUseJDBC4ColumnNameAndLabelSemantics
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|data
init|=
name|extractRows
argument_list|(
name|iterator
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|JdbcConstants
operator|.
name|JDBC_GENERATED_KEYS_ROW_COUNT
argument_list|,
name|data
operator|.
name|size
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
name|JdbcConstants
operator|.
name|JDBC_GENERATED_KEYS_DATA
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Sets the result from the ResultSet to the Exchange as its OUT body.      */
DECL|method|setResultSet (Exchange exchange, ResultSet rs)
specifier|protected
name|void
name|setResultSet
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|ResultSet
name|rs
parameter_list|)
throws|throws
name|SQLException
block|{
name|ResultSetIterator
name|iterator
init|=
operator|new
name|ResultSetIterator
argument_list|(
name|rs
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|isUseJDBC4ColumnNameAndLabelSemantics
argument_list|()
argument_list|)
decl_stmt|;
comment|// preserve headers
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
name|JdbcOutputType
name|outputType
init|=
name|getEndpoint
argument_list|()
operator|.
name|getOutputType
argument_list|()
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|JdbcConstants
operator|.
name|JDBC_COLUMN_NAMES
argument_list|,
name|iterator
operator|.
name|getColumnNames
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|outputType
operator|==
name|JdbcOutputType
operator|.
name|StreamList
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|iterator
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|addOnCompletion
argument_list|(
operator|new
name|ResultSetIteratorCompletion
argument_list|(
name|iterator
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|outputType
operator|==
name|JdbcOutputType
operator|.
name|SelectList
condition|)
block|{
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|list
init|=
name|extractRows
argument_list|(
name|iterator
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|JdbcConstants
operator|.
name|JDBC_ROW_COUNT
argument_list|,
name|list
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|list
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|outputType
operator|==
name|JdbcOutputType
operator|.
name|SelectOne
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|extractSingleRow
argument_list|(
name|iterator
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|extractRows (ResultSetIterator iterator)
specifier|private
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|extractRows
parameter_list|(
name|ResultSetIterator
name|iterator
parameter_list|)
block|{
try|try
block|{
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|int
name|maxRowCount
init|=
name|readSize
operator|==
literal|0
condition|?
name|Integer
operator|.
name|MAX_VALUE
else|:
name|readSize
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|iterator
operator|.
name|hasNext
argument_list|()
operator|&&
name|i
operator|<
name|maxRowCount
condition|;
name|i
operator|++
control|)
block|{
name|result
operator|.
name|add
argument_list|(
name|iterator
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
finally|finally
block|{
name|iterator
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|extractSingleRow (ResultSetIterator iterator)
specifier|private
name|Object
name|extractSingleRow
parameter_list|(
name|ResultSetIterator
name|iterator
parameter_list|)
throws|throws
name|SQLException
block|{
try|try
block|{
if|if
condition|(
operator|!
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|row
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|SQLDataException
argument_list|(
literal|"Query result not unique for outputType=SelectOne."
argument_list|)
throw|;
block|}
elseif|else
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getOutputClass
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|newBeanInstance
argument_list|(
name|row
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|row
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|row
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
else|else
block|{
return|return
name|row
return|;
block|}
block|}
finally|finally
block|{
name|iterator
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|newBeanInstance (Map<String, Object> row)
specifier|private
name|Object
name|newBeanInstance
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|row
parameter_list|)
throws|throws
name|SQLException
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|outputClass
init|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveClass
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getOutputClass
argument_list|()
argument_list|)
decl_stmt|;
name|Object
name|answer
init|=
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|outputClass
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
operator|new
name|LinkedHashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
comment|// map row names using the bean row mapper
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
name|row
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|Object
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
name|String
name|name
init|=
name|getEndpoint
argument_list|()
operator|.
name|getBeanRowMapper
argument_list|()
operator|.
name|map
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|properties
operator|.
name|put
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|answer
argument_list|,
name|properties
argument_list|)
expr_stmt|;
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
literal|"Error setting properties on output class "
operator|+
name|outputClass
argument_list|,
name|e
argument_list|)
throw|;
block|}
comment|// check we could map all properties to the bean
if|if
condition|(
operator|!
name|properties
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot map all properties to bean of type "
operator|+
name|outputClass
operator|+
literal|". There are "
operator|+
name|properties
operator|.
name|size
argument_list|()
operator|+
literal|" unmapped properties. "
operator|+
name|properties
argument_list|)
throw|;
block|}
return|return
name|answer
return|;
block|}
DECL|class|ResultSetIteratorCompletion
specifier|private
specifier|static
specifier|final
class|class
name|ResultSetIteratorCompletion
implements|implements
name|Synchronization
block|{
DECL|field|iterator
specifier|private
specifier|final
name|ResultSetIterator
name|iterator
decl_stmt|;
DECL|method|ResultSetIteratorCompletion (ResultSetIterator iterator)
specifier|private
name|ResultSetIteratorCompletion
parameter_list|(
name|ResultSetIterator
name|iterator
parameter_list|)
block|{
name|this
operator|.
name|iterator
operator|=
name|iterator
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onComplete (Exchange exchange)
specifier|public
name|void
name|onComplete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|iterator
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|onFailure (Exchange exchange)
specifier|public
name|void
name|onFailure
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|iterator
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

