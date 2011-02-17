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
name|session
operator|.
name|SqlSession
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
name|SqlSessionFactory
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
argument_list|)
expr_stmt|;
break|break;
case|case
name|SelectList
case|:
name|doSelectList
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|Insert
case|:
name|doInsert
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|Update
case|:
name|doUpdate
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
break|break;
case|case
name|Delete
case|:
name|doDelete
argument_list|(
name|exchange
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
block|}
DECL|method|doSelectOne (Exchange exchange)
specifier|private
name|void
name|doSelectOne
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|SqlSessionFactory
name|client
init|=
name|endpoint
operator|.
name|getSqlSessionFactory
argument_list|()
decl_stmt|;
name|SqlSession
name|session
init|=
name|client
operator|.
name|openSession
argument_list|()
decl_stmt|;
try|try
block|{
name|Object
name|result
decl_stmt|;
name|Object
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"SelectOne: "
operator|+
name|in
operator|+
literal|"  using statement: "
operator|+
name|statement
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"SelectOne using statement: "
operator|+
name|statement
argument_list|)
expr_stmt|;
block|}
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
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|session
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|doSelectList (Exchange exchange)
specifier|private
name|void
name|doSelectList
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|SqlSessionFactory
name|client
init|=
name|endpoint
operator|.
name|getSqlSessionFactory
argument_list|()
decl_stmt|;
name|SqlSession
name|session
init|=
name|client
operator|.
name|openSession
argument_list|()
decl_stmt|;
try|try
block|{
name|Object
name|result
decl_stmt|;
name|Object
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
if|if
condition|(
name|in
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"SelectList: "
operator|+
name|in
operator|+
literal|"  using statement: "
operator|+
name|statement
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"SelectList using statement: "
operator|+
name|statement
argument_list|)
expr_stmt|;
block|}
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
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|session
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|doInsert (Exchange exchange)
specifier|private
name|void
name|doInsert
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|SqlSessionFactory
name|client
init|=
name|endpoint
operator|.
name|getSqlSessionFactory
argument_list|()
decl_stmt|;
name|SqlSession
name|session
init|=
name|client
operator|.
name|openSession
argument_list|()
decl_stmt|;
try|try
block|{
name|Object
name|result
decl_stmt|;
name|Object
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
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
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Inserting: "
operator|+
name|value
operator|+
literal|" using statement: "
operator|+
name|statement
argument_list|)
expr_stmt|;
block|}
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
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Inserting using statement: "
operator|+
name|statement
argument_list|)
expr_stmt|;
block|}
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
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|session
operator|.
name|commit
argument_list|()
expr_stmt|;
name|session
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|doUpdate (Exchange exchange)
specifier|private
name|void
name|doUpdate
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|SqlSessionFactory
name|client
init|=
name|endpoint
operator|.
name|getSqlSessionFactory
argument_list|()
decl_stmt|;
name|SqlSession
name|session
init|=
name|client
operator|.
name|openSession
argument_list|()
decl_stmt|;
try|try
block|{
name|Object
name|result
decl_stmt|;
name|Object
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
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
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Updating: "
operator|+
name|value
operator|+
literal|" using statement: "
operator|+
name|statement
argument_list|)
expr_stmt|;
block|}
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
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Updating using statement: "
operator|+
name|statement
argument_list|)
expr_stmt|;
block|}
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
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|session
operator|.
name|commit
argument_list|()
expr_stmt|;
name|session
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|doDelete (Exchange exchange)
specifier|private
name|void
name|doDelete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|SqlSessionFactory
name|client
init|=
name|endpoint
operator|.
name|getSqlSessionFactory
argument_list|()
decl_stmt|;
name|SqlSession
name|session
init|=
name|client
operator|.
name|openSession
argument_list|()
decl_stmt|;
try|try
block|{
name|Object
name|result
decl_stmt|;
name|Object
name|in
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
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
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Deleting: "
operator|+
name|value
operator|+
literal|" using statement: "
operator|+
name|statement
argument_list|)
expr_stmt|;
block|}
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
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Deleting using statement: "
operator|+
name|statement
argument_list|)
expr_stmt|;
block|}
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
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|session
operator|.
name|commit
argument_list|()
expr_stmt|;
name|session
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
DECL|method|doProcessResult (Exchange exchange, Object result)
specifier|private
name|void
name|doProcessResult
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Object
name|result
parameter_list|)
block|{
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
comment|// set the result as body for insert
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
block|}
end_class

end_unit

