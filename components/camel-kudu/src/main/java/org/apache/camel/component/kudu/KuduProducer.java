begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.kudu
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|kudu
package|;
end_package

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
name|support
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
name|kudu
operator|.
name|Schema
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|kudu
operator|.
name|client
operator|.
name|CreateTableOptions
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|kudu
operator|.
name|client
operator|.
name|Insert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|kudu
operator|.
name|client
operator|.
name|KuduClient
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|kudu
operator|.
name|client
operator|.
name|KuduException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|kudu
operator|.
name|client
operator|.
name|KuduTable
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|kudu
operator|.
name|client
operator|.
name|PartialRow
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
comment|/**  * The Kudu producer.  *  * @see org.apache.camel.component.kudu.KuduEndpoint  */
end_comment

begin_class
DECL|class|KuduProducer
specifier|public
class|class
name|KuduProducer
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
name|KuduProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|KuduEndpoint
name|endpoint
decl_stmt|;
DECL|method|KuduProducer (KuduEndpoint endpoint)
specifier|public
name|KuduProducer
parameter_list|(
name|KuduEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
if|if
condition|(
name|endpoint
operator|==
literal|null
operator|||
name|endpoint
operator|.
name|getKuduClient
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Can't create a producer when the database connection is null"
argument_list|)
throw|;
block|}
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
block|}
annotation|@
name|Override
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
name|table
init|=
name|endpoint
operator|.
name|getTableName
argument_list|()
decl_stmt|;
switch|switch
condition|(
name|endpoint
operator|.
name|getOperation
argument_list|()
condition|)
block|{
case|case
name|INSERT
case|:
name|doInsert
argument_list|(
name|exchange
argument_list|,
name|table
argument_list|)
expr_stmt|;
break|break;
case|case
name|CREATE_TABLE
case|:
name|doCreateTable
argument_list|(
name|exchange
argument_list|,
name|table
argument_list|)
expr_stmt|;
break|break;
case|case
name|SCAN
case|:
name|doScan
argument_list|(
name|exchange
argument_list|,
name|table
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The operation "
operator|+
name|endpoint
operator|.
name|getOperation
argument_list|()
operator|+
literal|" is not supported"
argument_list|)
throw|;
block|}
block|}
DECL|method|doInsert (Exchange exchange, String tableName)
specifier|private
name|void
name|doInsert
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|tableName
parameter_list|)
throws|throws
name|KuduException
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Insert on table {}"
argument_list|,
name|tableName
argument_list|)
expr_stmt|;
name|KuduClient
name|connection
init|=
name|endpoint
operator|.
name|getKuduClient
argument_list|()
decl_stmt|;
name|KuduTable
name|table
init|=
name|connection
operator|.
name|openTable
argument_list|(
name|tableName
argument_list|)
decl_stmt|;
name|Insert
name|insert
init|=
name|table
operator|.
name|newInsert
argument_list|()
decl_stmt|;
name|PartialRow
name|row
init|=
name|insert
operator|.
name|getRow
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|rows
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Map
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
name|entry
range|:
name|rows
operator|.
name|entrySet
argument_list|()
control|)
block|{
specifier|final
name|String
name|colName
init|=
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|toString
argument_list|()
decl_stmt|;
specifier|final
name|Object
name|value
init|=
name|entry
operator|.
name|getValue
argument_list|()
decl_stmt|;
comment|//Add only if column exist
comment|//If not, this will throw an IllegalArgumentException
if|if
condition|(
name|table
operator|.
name|getSchema
argument_list|()
operator|.
name|getColumn
argument_list|(
name|colName
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|row
operator|.
name|addObject
argument_list|(
name|colName
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
name|connection
operator|.
name|newSession
argument_list|()
operator|.
name|apply
argument_list|(
name|insert
argument_list|)
expr_stmt|;
block|}
DECL|method|doCreateTable (Exchange exchange, String tableName)
specifier|private
name|void
name|doCreateTable
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|tableName
parameter_list|)
throws|throws
name|KuduException
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Creating table {}"
argument_list|,
name|tableName
argument_list|)
expr_stmt|;
name|KuduClient
name|connection
init|=
name|endpoint
operator|.
name|getKuduClient
argument_list|()
decl_stmt|;
name|Schema
name|schema
init|=
operator|(
name|Schema
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KuduConstants
operator|.
name|CAMEL_KUDU_SCHEMA
argument_list|)
decl_stmt|;
name|CreateTableOptions
name|builder
init|=
operator|(
name|CreateTableOptions
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|KuduConstants
operator|.
name|CAMEL_KUDU_TABLE_OPTIONS
argument_list|)
decl_stmt|;
name|connection
operator|.
name|createTable
argument_list|(
name|tableName
argument_list|,
name|schema
argument_list|,
name|builder
argument_list|)
expr_stmt|;
block|}
DECL|method|doScan (Exchange exchange, String tableName)
specifier|private
name|void
name|doScan
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|tableName
parameter_list|)
throws|throws
name|KuduException
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|KuduUtils
operator|.
name|doScan
argument_list|(
name|tableName
argument_list|,
name|endpoint
operator|.
name|getKuduClient
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

