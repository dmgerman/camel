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
name|ResultSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|ResultSetMetaData
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
comment|/**  * @version   */
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
specifier|transient
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
name|Boolean
name|autoCommit
init|=
literal|null
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
name|conn
operator|.
name|setAutoCommit
argument_list|(
literal|false
argument_list|)
expr_stmt|;
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
literal|"Executing JDBC statement: {}"
argument_list|,
name|sql
argument_list|)
expr_stmt|;
if|if
condition|(
name|stmt
operator|.
name|execute
argument_list|(
name|sql
argument_list|)
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
name|conn
operator|.
name|rollback
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
literal|"Error on jdbc component rollback: "
operator|+
name|sqle
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
comment|// populate headers
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
name|ResultSetMetaData
name|meta
init|=
name|rs
operator|.
name|getMetaData
argument_list|()
decl_stmt|;
comment|// should we use jdbc4 or jdbc3 semantics
name|boolean
name|jdbc4
init|=
name|getEndpoint
argument_list|()
operator|.
name|isUseJDBC4ColumnNameAndLabelSemantics
argument_list|()
decl_stmt|;
name|int
name|count
init|=
name|meta
operator|.
name|getColumnCount
argument_list|()
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
name|rowNumber
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|rs
operator|.
name|next
argument_list|()
operator|&&
operator|(
name|readSize
operator|==
literal|0
operator|||
name|rowNumber
operator|<
name|readSize
operator|)
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|row
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
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|count
condition|;
name|i
operator|++
control|)
block|{
name|int
name|columnNumber
init|=
name|i
operator|+
literal|1
decl_stmt|;
comment|// use column label to get the name as it also handled SQL SELECT aliases
name|String
name|columnName
decl_stmt|;
if|if
condition|(
name|jdbc4
condition|)
block|{
comment|// jdbc 4 should use label to get the name
name|columnName
operator|=
name|meta
operator|.
name|getColumnLabel
argument_list|(
name|columnNumber
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// jdbc 3 uses the label or name to get the name
try|try
block|{
name|columnName
operator|=
name|meta
operator|.
name|getColumnLabel
argument_list|(
name|columnNumber
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SQLException
name|e
parameter_list|)
block|{
name|columnName
operator|=
name|meta
operator|.
name|getColumnName
argument_list|(
name|columnNumber
argument_list|)
expr_stmt|;
block|}
block|}
comment|// use index based which should be faster
name|row
operator|.
name|put
argument_list|(
name|columnName
argument_list|,
name|rs
operator|.
name|getObject
argument_list|(
name|columnNumber
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|data
operator|.
name|add
argument_list|(
name|row
argument_list|)
expr_stmt|;
name|rowNumber
operator|++
expr_stmt|;
block|}
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
name|rowNumber
argument_list|)
expr_stmt|;
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
block|}
end_class

end_unit

