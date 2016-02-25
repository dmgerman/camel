begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.hbase.processor.idempotent
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|hbase
operator|.
name|processor
operator|.
name|idempotent
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|hbase
operator|.
name|HBaseHelper
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
name|IdempotentRepository
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
name|ServiceSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|conf
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|TableName
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|client
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|client
operator|.
name|ConnectionFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|client
operator|.
name|Delete
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|client
operator|.
name|Get
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|client
operator|.
name|Put
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|client
operator|.
name|Result
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|client
operator|.
name|ResultScanner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|client
operator|.
name|Scan
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|client
operator|.
name|Table
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

begin_class
DECL|class|HBaseIdempotentRepository
specifier|public
class|class
name|HBaseIdempotentRepository
extends|extends
name|ServiceSupport
implements|implements
name|IdempotentRepository
argument_list|<
name|Object
argument_list|>
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
name|HBaseIdempotentRepository
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|tableName
specifier|private
specifier|final
name|String
name|tableName
decl_stmt|;
DECL|field|family
specifier|private
specifier|final
name|String
name|family
decl_stmt|;
DECL|field|qualifier
specifier|private
specifier|final
name|String
name|qualifier
decl_stmt|;
DECL|field|configuration
specifier|private
specifier|final
name|Configuration
name|configuration
decl_stmt|;
DECL|field|connection
specifier|private
name|Connection
name|connection
decl_stmt|;
DECL|field|table
specifier|private
name|Table
name|table
decl_stmt|;
DECL|method|HBaseIdempotentRepository (Configuration configuration, String tableName, String family, String qualifier)
specifier|public
name|HBaseIdempotentRepository
parameter_list|(
name|Configuration
name|configuration
parameter_list|,
name|String
name|tableName
parameter_list|,
name|String
name|family
parameter_list|,
name|String
name|qualifier
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|tableName
operator|=
name|tableName
expr_stmt|;
name|this
operator|.
name|family
operator|=
name|family
expr_stmt|;
name|this
operator|.
name|qualifier
operator|=
name|qualifier
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|this
operator|.
name|connection
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|table
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|add (Object o)
specifier|public
name|boolean
name|add
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
try|try
block|{
synchronized|synchronized
init|(
name|tableName
operator|.
name|intern
argument_list|()
init|)
block|{
if|if
condition|(
name|contains
argument_list|(
name|o
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|byte
index|[]
name|b
init|=
name|HBaseHelper
operator|.
name|toBytes
argument_list|(
name|o
argument_list|)
decl_stmt|;
name|Put
name|put
init|=
operator|new
name|Put
argument_list|(
name|b
argument_list|)
decl_stmt|;
name|put
operator|.
name|addColumn
argument_list|(
name|HBaseHelper
operator|.
name|getHBaseFieldAsBytes
argument_list|(
name|family
argument_list|)
argument_list|,
name|HBaseHelper
operator|.
name|getHBaseFieldAsBytes
argument_list|(
name|qualifier
argument_list|)
argument_list|,
name|b
argument_list|)
expr_stmt|;
name|table
operator|.
name|put
argument_list|(
name|put
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error adding object {} to HBase repository."
argument_list|,
name|o
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|contains (Object o)
specifier|public
name|boolean
name|contains
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
try|try
block|{
name|byte
index|[]
name|b
init|=
name|HBaseHelper
operator|.
name|toBytes
argument_list|(
name|o
argument_list|)
decl_stmt|;
name|Get
name|get
init|=
operator|new
name|Get
argument_list|(
name|b
argument_list|)
decl_stmt|;
name|get
operator|.
name|addColumn
argument_list|(
name|HBaseHelper
operator|.
name|getHBaseFieldAsBytes
argument_list|(
name|family
argument_list|)
argument_list|,
name|HBaseHelper
operator|.
name|getHBaseFieldAsBytes
argument_list|(
name|qualifier
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|table
operator|.
name|exists
argument_list|(
name|get
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error reading object {} from HBase repository."
argument_list|,
name|o
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|remove (Object o)
specifier|public
name|boolean
name|remove
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
try|try
block|{
name|byte
index|[]
name|b
init|=
name|HBaseHelper
operator|.
name|toBytes
argument_list|(
name|o
argument_list|)
decl_stmt|;
if|if
condition|(
name|table
operator|.
name|exists
argument_list|(
operator|new
name|Get
argument_list|(
name|b
argument_list|)
argument_list|)
condition|)
block|{
name|Delete
name|delete
init|=
operator|new
name|Delete
argument_list|(
name|b
argument_list|)
decl_stmt|;
name|table
operator|.
name|delete
argument_list|(
name|delete
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error removing object {} from HBase repository."
argument_list|,
name|o
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|confirm (Object o)
specifier|public
name|boolean
name|confirm
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|Scan
name|s
init|=
operator|new
name|Scan
argument_list|()
decl_stmt|;
name|ResultScanner
name|scanner
decl_stmt|;
try|try
block|{
name|scanner
operator|=
name|table
operator|.
name|getScanner
argument_list|(
name|s
argument_list|)
expr_stmt|;
for|for
control|(
name|Result
name|rr
range|:
name|scanner
control|)
block|{
name|Delete
name|d
init|=
operator|new
name|Delete
argument_list|(
name|rr
operator|.
name|getRow
argument_list|()
argument_list|)
decl_stmt|;
name|table
operator|.
name|delete
argument_list|(
name|d
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warn
argument_list|(
literal|"Error clear HBase repository {}"
argument_list|,
name|table
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|connection
operator|=
name|ConnectionFactory
operator|.
name|createConnection
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|this
operator|.
name|table
operator|=
name|this
operator|.
name|connection
operator|.
name|getTable
argument_list|(
name|TableName
operator|.
name|valueOf
argument_list|(
name|tableName
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|table
operator|!=
literal|null
condition|)
block|{
name|table
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|connection
operator|!=
literal|null
condition|)
block|{
name|connection
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

