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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|IntrospectionSupport
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|JdbcProducer
specifier|public
class|class
name|JdbcProducer
extends|extends
name|DefaultProducer
argument_list|<
name|DefaultExchange
argument_list|>
block|{
DECL|field|source
specifier|private
name|DataSource
name|source
decl_stmt|;
comment|/** The maximum size for reading a result set<code>readSize</code> */
DECL|field|readSize
specifier|private
name|int
name|readSize
init|=
literal|2000
decl_stmt|;
DECL|method|JdbcProducer (JdbcEndpoint endpoint, String remaining, int readSize)
specifier|public
name|JdbcProducer
parameter_list|(
name|JdbcEndpoint
name|endpoint
parameter_list|,
name|String
name|remaining
parameter_list|,
name|int
name|readSize
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
name|readSize
operator|=
name|readSize
expr_stmt|;
name|source
operator|=
operator|(
name|DataSource
operator|)
name|getEndpoint
argument_list|()
operator|.
name|getContext
argument_list|()
operator|.
name|getRegistry
argument_list|()
operator|.
name|lookup
argument_list|(
name|remaining
argument_list|)
expr_stmt|;
block|}
comment|/**      * Execute sql of exchange and set results on output      *      * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)      */
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
try|try
block|{
name|conn
operator|=
name|source
operator|.
name|getConnection
argument_list|()
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
name|stmt
operator|.
name|execute
argument_list|(
name|sql
argument_list|)
condition|)
block|{
name|ResultSet
name|rs
init|=
name|stmt
operator|.
name|getResultSet
argument_list|()
decl_stmt|;
name|setResultSet
argument_list|(
name|exchange
argument_list|,
name|rs
argument_list|)
expr_stmt|;
name|rs
operator|.
name|close
argument_list|()
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
literal|"jdbc.updateCount"
argument_list|,
name|updateCount
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|stmt
operator|!=
literal|null
condition|)
block|{
name|stmt
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|conn
operator|!=
literal|null
condition|)
block|{
name|conn
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|getReadSize ()
specifier|public
name|int
name|getReadSize
parameter_list|()
block|{
return|return
name|this
operator|.
name|readSize
return|;
block|}
DECL|method|setReadSize (int readSize)
specifier|public
name|void
name|setReadSize
parameter_list|(
name|int
name|readSize
parameter_list|)
block|{
name|this
operator|.
name|readSize
operator|=
name|readSize
expr_stmt|;
block|}
DECL|method|setResultSet (Exchange exchange, ResultSet rs)
specifier|public
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
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
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
name|IntrospectionSupport
operator|.
name|getProperties
argument_list|(
name|meta
argument_list|,
name|props
argument_list|,
literal|"jdbc."
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeaders
argument_list|(
name|props
argument_list|)
expr_stmt|;
comment|//
name|int
name|count
init|=
name|meta
operator|.
name|getColumnCount
argument_list|()
decl_stmt|;
name|ArrayList
argument_list|<
name|HashMap
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
name|HashMap
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
name|rowNumber
operator|<
name|readSize
condition|)
block|{
name|HashMap
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
name|String
name|columnName
init|=
name|meta
operator|.
name|getColumnName
argument_list|(
name|columnNumber
argument_list|)
decl_stmt|;
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
name|columnName
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
name|setBody
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

