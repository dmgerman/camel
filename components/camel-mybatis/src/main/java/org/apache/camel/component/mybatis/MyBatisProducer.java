begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.mybatis
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|mybatis
package|;
end_package

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
name|Message
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
name|ExchangeHelper
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
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ibatis
operator|.
name|mapping
operator|.
name|MappedStatement
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ibatis
operator|.
name|session
operator|.
name|ExecutorType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ibatis
operator|.
name|session
operator|.
name|SqlSession
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
DECL|class|MyBatisProducer
specifier|public
class|class
name|MyBatisProducer
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
name|MyBatisProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|statement
specifier|private
name|String
name|statement
decl_stmt|;
DECL|field|endpoint
specifier|private
name|MyBatisEndpoint
name|endpoint
decl_stmt|;
DECL|method|MyBatisProducer (MyBatisEndpoint endpoint)
specifier|public
name|MyBatisProducer
parameter_list|(
name|MyBatisEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|statement
operator|=
name|endpoint
operator|.
name|getStatement
argument_list|()
expr_stmt|;
block|}
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
name|SqlSession
name|session
decl_stmt|;
name|ExecutorType
name|executorType
init|=
name|endpoint
operator|.
name|getExecutorType
argument_list|()
decl_stmt|;
if|if
condition|(
name|executorType
operator|==
literal|null
condition|)
block|{
name|session
operator|=
name|endpoint
operator|.
name|getSqlSessionFactory
argument_list|()
operator|.
name|openSession
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|session
operator|=
name|endpoint
operator|.
name|getSqlSessionFactory
argument_list|()
operator|.
name|openSession
argument_list|(
name|executorType
argument_list|)
expr_stmt|;
block|}
try|try
block|{
switch|switch
condition|(
name|endpoint
operator|.
name|getStatementType
argument_list|()
condition|)
block|{
case|case
name|SelectOne
case|:
name|doSelectOne
argument_list|(
name|exchange
argument_list|,
name|session
argument_list|)
expr_stmt|;
break|break;
case|case
name|SelectList
case|:
name|doSelectList
argument_list|(
name|exchange
argument_list|,
name|session
argument_list|)
expr_stmt|;
break|break;
case|case
name|Insert
case|:
name|doInsert
argument_list|(
name|exchange
argument_list|,
name|session
argument_list|)
expr_stmt|;
break|break;
case|case
name|InsertList
case|:
name|doInsertList
argument_list|(
name|exchange
argument_list|,
name|session
argument_list|)
expr_stmt|;
break|break;
case|case
name|Update
case|:
name|doUpdate
argument_list|(
name|exchange
argument_list|,
name|session
argument_list|)
expr_stmt|;
break|break;
case|case
name|UpdateList
case|:
name|doUpdateList
argument_list|(
name|exchange
argument_list|,
name|session
argument_list|)
expr_stmt|;
break|break;
case|case
name|Delete
case|:
name|doDelete
argument_list|(
name|exchange
argument_list|,
name|session
argument_list|)
expr_stmt|;
break|break;
case|case
name|DeleteList
case|:
name|doDeleteList
argument_list|(
name|exchange
argument_list|,
name|session
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported statementType: "
operator|+
name|endpoint
operator|.
name|getStatementType
argument_list|()
argument_list|)
throw|;
block|}
comment|// flush the batch statements and commit the database connection
name|session
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
comment|// discard the pending batch statements and roll the database connection back
name|session
operator|.
name|rollback
argument_list|()
expr_stmt|;
throw|throw
name|e
throw|;
block|}
finally|finally
block|{
comment|// and finally close the session as we're done
name|session
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|doSelectOne (Exchange exchange, SqlSession session)
specifier|private
name|void
name|doSelectOne
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|SqlSession
name|session
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|result
decl_stmt|;
name|Object
name|in
init|=
name|getInput
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"SelectOne: {} using statement: {}"
argument_list|,
name|in
argument_list|,
name|statement
argument_list|)
expr_stmt|;
name|result
operator|=
name|session
operator|.
name|selectOne
argument_list|(
name|statement
argument_list|,
name|in
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"SelectOne using statement: {}"
argument_list|,
name|statement
argument_list|)
expr_stmt|;
name|result
operator|=
name|session
operator|.
name|selectOne
argument_list|(
name|statement
argument_list|)
expr_stmt|;
block|}
name|doProcessResult
argument_list|(
name|exchange
argument_list|,
name|result
argument_list|,
name|session
argument_list|)
expr_stmt|;
block|}
DECL|method|doSelectList (Exchange exchange, SqlSession session)
specifier|private
name|void
name|doSelectList
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|SqlSession
name|session
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|result
decl_stmt|;
name|Object
name|in
init|=
name|getInput
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"SelectList: {} using statement: {}"
argument_list|,
name|in
argument_list|,
name|statement
argument_list|)
expr_stmt|;
name|result
operator|=
name|session
operator|.
name|selectList
argument_list|(
name|statement
argument_list|,
name|in
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"SelectList using statement: {}"
argument_list|,
name|statement
argument_list|)
expr_stmt|;
name|result
operator|=
name|session
operator|.
name|selectList
argument_list|(
name|statement
argument_list|)
expr_stmt|;
block|}
name|doProcessResult
argument_list|(
name|exchange
argument_list|,
name|result
argument_list|,
name|session
argument_list|)
expr_stmt|;
block|}
DECL|method|doInsert (Exchange exchange, SqlSession session)
specifier|private
name|void
name|doInsert
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|SqlSession
name|session
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|result
decl_stmt|;
name|Object
name|in
init|=
name|getInput
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
comment|// lets handle arrays or collections of objects
name|Iterator
argument_list|<
name|?
argument_list|>
name|iter
init|=
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
name|in
argument_list|)
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|value
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Inserting: {} using statement: {}"
argument_list|,
name|value
argument_list|,
name|statement
argument_list|)
expr_stmt|;
name|result
operator|=
name|session
operator|.
name|insert
argument_list|(
name|statement
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|doProcessResult
argument_list|(
name|exchange
argument_list|,
name|result
argument_list|,
name|session
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Inserting using statement: {}"
argument_list|,
name|statement
argument_list|)
expr_stmt|;
name|result
operator|=
name|session
operator|.
name|insert
argument_list|(
name|statement
argument_list|)
expr_stmt|;
name|doProcessResult
argument_list|(
name|exchange
argument_list|,
name|result
argument_list|,
name|session
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doInsertList (Exchange exchange, SqlSession session)
specifier|private
name|void
name|doInsertList
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|SqlSession
name|session
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|result
decl_stmt|;
name|Object
name|in
init|=
name|getInput
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
comment|// just pass in the body as Object and allow MyBatis to iterate using its own foreach statement
name|LOG
operator|.
name|trace
argument_list|(
literal|"Inserting: {} using statement: {}"
argument_list|,
name|in
argument_list|,
name|statement
argument_list|)
expr_stmt|;
name|result
operator|=
name|session
operator|.
name|insert
argument_list|(
name|statement
argument_list|,
name|in
argument_list|)
expr_stmt|;
name|doProcessResult
argument_list|(
name|exchange
argument_list|,
name|result
argument_list|,
name|session
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Inserting using statement: {}"
argument_list|,
name|statement
argument_list|)
expr_stmt|;
name|result
operator|=
name|session
operator|.
name|insert
argument_list|(
name|statement
argument_list|)
expr_stmt|;
name|doProcessResult
argument_list|(
name|exchange
argument_list|,
name|result
argument_list|,
name|session
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doUpdate (Exchange exchange, SqlSession session)
specifier|private
name|void
name|doUpdate
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|SqlSession
name|session
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|result
decl_stmt|;
name|Object
name|in
init|=
name|getInput
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
comment|// lets handle arrays or collections of objects
name|Iterator
argument_list|<
name|?
argument_list|>
name|iter
init|=
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
name|in
argument_list|)
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|value
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Updating: {} using statement: {}"
argument_list|,
name|value
argument_list|,
name|statement
argument_list|)
expr_stmt|;
name|result
operator|=
name|session
operator|.
name|update
argument_list|(
name|statement
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|doProcessResult
argument_list|(
name|exchange
argument_list|,
name|result
argument_list|,
name|session
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Updating using statement: {}"
argument_list|,
name|statement
argument_list|)
expr_stmt|;
name|result
operator|=
name|session
operator|.
name|update
argument_list|(
name|statement
argument_list|)
expr_stmt|;
name|doProcessResult
argument_list|(
name|exchange
argument_list|,
name|result
argument_list|,
name|session
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doUpdateList (Exchange exchange, SqlSession session)
specifier|private
name|void
name|doUpdateList
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|SqlSession
name|session
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|result
decl_stmt|;
name|Object
name|in
init|=
name|getInput
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
comment|// just pass in the body as Object and allow MyBatis to iterate using its own foreach statement
name|LOG
operator|.
name|trace
argument_list|(
literal|"Updating: {} using statement: {}"
argument_list|,
name|in
argument_list|,
name|statement
argument_list|)
expr_stmt|;
name|result
operator|=
name|session
operator|.
name|update
argument_list|(
name|statement
argument_list|,
name|in
argument_list|)
expr_stmt|;
name|doProcessResult
argument_list|(
name|exchange
argument_list|,
name|result
argument_list|,
name|session
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Updating using statement: {}"
argument_list|,
name|statement
argument_list|)
expr_stmt|;
name|result
operator|=
name|session
operator|.
name|update
argument_list|(
name|statement
argument_list|)
expr_stmt|;
name|doProcessResult
argument_list|(
name|exchange
argument_list|,
name|result
argument_list|,
name|session
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doDelete (Exchange exchange, SqlSession session)
specifier|private
name|void
name|doDelete
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|SqlSession
name|session
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|result
decl_stmt|;
name|Object
name|in
init|=
name|getInput
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
comment|// lets handle arrays or collections of objects
name|Iterator
argument_list|<
name|?
argument_list|>
name|iter
init|=
name|ObjectHelper
operator|.
name|createIterator
argument_list|(
name|in
argument_list|)
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|value
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|trace
argument_list|(
literal|"Deleting: {} using statement: {}"
argument_list|,
name|value
argument_list|,
name|statement
argument_list|)
expr_stmt|;
name|result
operator|=
name|session
operator|.
name|delete
argument_list|(
name|statement
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|doProcessResult
argument_list|(
name|exchange
argument_list|,
name|result
argument_list|,
name|session
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Deleting using statement: {}"
argument_list|,
name|statement
argument_list|)
expr_stmt|;
name|result
operator|=
name|session
operator|.
name|delete
argument_list|(
name|statement
argument_list|)
expr_stmt|;
name|doProcessResult
argument_list|(
name|exchange
argument_list|,
name|result
argument_list|,
name|session
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doDeleteList (Exchange exchange, SqlSession session)
specifier|private
name|void
name|doDeleteList
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|SqlSession
name|session
parameter_list|)
throws|throws
name|Exception
block|{
name|Object
name|result
decl_stmt|;
name|Object
name|in
init|=
name|getInput
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
comment|// just pass in the body as Object and allow MyBatis to iterate using its own foreach statement
name|LOG
operator|.
name|trace
argument_list|(
literal|"Deleting: {} using statement: {}"
argument_list|,
name|in
argument_list|,
name|statement
argument_list|)
expr_stmt|;
name|result
operator|=
name|session
operator|.
name|delete
argument_list|(
name|statement
argument_list|,
name|in
argument_list|)
expr_stmt|;
name|doProcessResult
argument_list|(
name|exchange
argument_list|,
name|result
argument_list|,
name|session
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Deleting using statement: {}"
argument_list|,
name|statement
argument_list|)
expr_stmt|;
name|result
operator|=
name|session
operator|.
name|delete
argument_list|(
name|statement
argument_list|)
expr_stmt|;
name|doProcessResult
argument_list|(
name|exchange
argument_list|,
name|result
argument_list|,
name|session
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|doProcessResult (Exchange exchange, Object result, SqlSession session)
specifier|private
name|void
name|doProcessResult
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|result
parameter_list|,
name|SqlSession
name|session
parameter_list|)
block|{
specifier|final
name|String
name|outputHeader
init|=
name|getEndpoint
argument_list|()
operator|.
name|getOutputHeader
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpoint
operator|.
name|getStatementType
argument_list|()
operator|==
name|StatementType
operator|.
name|SelectList
operator|||
name|endpoint
operator|.
name|getStatementType
argument_list|()
operator|==
name|StatementType
operator|.
name|SelectOne
condition|)
block|{
name|Message
name|answer
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
if|if
condition|(
name|ExchangeHelper
operator|.
name|isOutCapable
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
name|answer
operator|=
name|exchange
operator|.
name|getOut
argument_list|()
expr_stmt|;
comment|// preserve headers
name|answer
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
comment|// we should not set the body if its a stored procedure as the result is already in its OUT parameter
name|MappedStatement
name|ms
init|=
name|session
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getMappedStatement
argument_list|(
name|statement
argument_list|)
decl_stmt|;
if|if
condition|(
name|ms
operator|!=
literal|null
operator|&&
name|ms
operator|.
name|getStatementType
argument_list|()
operator|==
name|org
operator|.
name|apache
operator|.
name|ibatis
operator|.
name|mapping
operator|.
name|StatementType
operator|.
name|CALLABLE
condition|)
block|{
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Setting result as existing body as MyBatis statement type is Callable, and there was no result."
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setBody
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|outputHeader
operator|!=
literal|null
condition|)
block|{
comment|// set the result as header for insert
name|LOG
operator|.
name|trace
argument_list|(
literal|"Setting result as header [{}]: {}"
argument_list|,
name|outputHeader
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setHeader
argument_list|(
name|outputHeader
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// set the result as body for insert
name|LOG
operator|.
name|trace
argument_list|(
literal|"Setting result as body: {}"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setHeader
argument_list|(
name|MyBatisConstants
operator|.
name|MYBATIS_RESULT
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
if|if
condition|(
name|outputHeader
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Setting result as header [{}]: {}"
argument_list|,
name|outputHeader
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setHeader
argument_list|(
name|outputHeader
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// set the result as body for insert
name|LOG
operator|.
name|trace
argument_list|(
literal|"Setting result as body: {}"
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setHeader
argument_list|(
name|MyBatisConstants
operator|.
name|MYBATIS_RESULT
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
block|}
name|answer
operator|.
name|setHeader
argument_list|(
name|MyBatisConstants
operator|.
name|MYBATIS_STATEMENT_NAME
argument_list|,
name|statement
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Message
name|msg
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
if|if
condition|(
name|outputHeader
operator|!=
literal|null
condition|)
block|{
name|msg
operator|.
name|setHeader
argument_list|(
name|outputHeader
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|msg
operator|.
name|setHeader
argument_list|(
name|MyBatisConstants
operator|.
name|MYBATIS_RESULT
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
name|msg
operator|.
name|setHeader
argument_list|(
name|MyBatisConstants
operator|.
name|MYBATIS_STATEMENT_NAME
argument_list|,
name|statement
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|MyBatisEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|MyBatisEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
DECL|method|getInput (final Exchange exchange)
specifier|private
name|Object
name|getInput
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
specifier|final
name|String
name|inputHeader
init|=
name|getEndpoint
argument_list|()
operator|.
name|getInputHeader
argument_list|()
decl_stmt|;
if|if
condition|(
name|inputHeader
operator|!=
literal|null
condition|)
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|inputHeader
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

