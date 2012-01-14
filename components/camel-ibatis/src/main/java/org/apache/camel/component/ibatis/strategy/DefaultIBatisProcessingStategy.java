begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ibatis.strategy
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ibatis
operator|.
name|strategy
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
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ibatis
operator|.
name|sqlmap
operator|.
name|client
operator|.
name|SqlMapClient
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
name|component
operator|.
name|ibatis
operator|.
name|IBatisConsumer
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
name|ibatis
operator|.
name|IBatisEndpoint
import|;
end_import

begin_comment
comment|/**  * Default strategy for consuming messages for a route  */
end_comment

begin_class
DECL|class|DefaultIBatisProcessingStategy
specifier|public
class|class
name|DefaultIBatisProcessingStategy
implements|implements
name|IBatisProcessingStrategy
block|{
DECL|field|isolation
specifier|private
name|int
name|isolation
init|=
name|Connection
operator|.
name|TRANSACTION_REPEATABLE_READ
decl_stmt|;
DECL|method|commit (IBatisEndpoint endpoint, Exchange exchange, Object data, String consumeStatements)
specifier|public
name|void
name|commit
parameter_list|(
name|IBatisEndpoint
name|endpoint
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Object
name|data
parameter_list|,
name|String
name|consumeStatements
parameter_list|)
throws|throws
name|Exception
block|{
name|SqlMapClient
name|client
init|=
name|endpoint
operator|.
name|getSqlMapClient
argument_list|()
decl_stmt|;
name|boolean
name|useTrans
init|=
name|endpoint
operator|.
name|isUseTransactions
argument_list|()
decl_stmt|;
name|String
index|[]
name|statements
init|=
name|consumeStatements
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|useTrans
condition|)
block|{
name|client
operator|.
name|startTransaction
argument_list|(
name|isolation
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|String
name|statement
range|:
name|statements
control|)
block|{
name|client
operator|.
name|update
argument_list|(
name|statement
operator|.
name|trim
argument_list|()
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|useTrans
condition|)
block|{
name|client
operator|.
name|commitTransaction
argument_list|()
expr_stmt|;
block|}
block|}
finally|finally
block|{
if|if
condition|(
name|useTrans
condition|)
block|{
name|client
operator|.
name|endTransaction
argument_list|()
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|poll (IBatisConsumer consumer, IBatisEndpoint endpoint)
specifier|public
name|List
argument_list|<
name|Object
argument_list|>
name|poll
parameter_list|(
name|IBatisConsumer
name|consumer
parameter_list|,
name|IBatisEndpoint
name|endpoint
parameter_list|)
throws|throws
name|Exception
block|{
name|SqlMapClient
name|client
init|=
name|endpoint
operator|.
name|getSqlMapClient
argument_list|()
decl_stmt|;
return|return
name|client
operator|.
name|queryForList
argument_list|(
name|endpoint
operator|.
name|getStatement
argument_list|()
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|getIsolation ()
specifier|public
name|int
name|getIsolation
parameter_list|()
block|{
return|return
name|isolation
return|;
block|}
DECL|method|setIsolation (int isolation)
specifier|public
name|void
name|setIsolation
parameter_list|(
name|int
name|isolation
parameter_list|)
block|{
name|this
operator|.
name|isolation
operator|=
name|isolation
expr_stmt|;
block|}
block|}
end_class

end_unit

