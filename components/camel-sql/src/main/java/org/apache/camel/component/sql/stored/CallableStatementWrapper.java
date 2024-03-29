begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sql.stored
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
operator|.
name|stored
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|CallableStatement
import|;
end_import

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
name|SQLException
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
name|Iterator
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
name|sql
operator|.
name|stored
operator|.
name|template
operator|.
name|ast
operator|.
name|InParameter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|dao
operator|.
name|DataAccessException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jdbc
operator|.
name|core
operator|.
name|CallableStatementCallback
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|jdbc
operator|.
name|core
operator|.
name|CallableStatementCreator
import|;
end_import

begin_class
DECL|class|CallableStatementWrapper
specifier|public
class|class
name|CallableStatementWrapper
implements|implements
name|StatementWrapper
block|{
DECL|field|factory
specifier|final
name|CallableStatementWrapperFactory
name|factory
decl_stmt|;
DECL|field|template
specifier|final
name|String
name|template
decl_stmt|;
DECL|field|result
name|Map
name|result
decl_stmt|;
DECL|field|batchItems
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
argument_list|>
name|batchItems
decl_stmt|;
DECL|field|updateCount
name|Integer
name|updateCount
decl_stmt|;
DECL|field|batchFactory
name|BatchCallableStatementCreatorFactory
name|batchFactory
decl_stmt|;
DECL|method|CallableStatementWrapper (String template, CallableStatementWrapperFactory wrapperFactory)
specifier|public
name|CallableStatementWrapper
parameter_list|(
name|String
name|template
parameter_list|,
name|CallableStatementWrapperFactory
name|wrapperFactory
parameter_list|)
block|{
name|this
operator|.
name|factory
operator|=
name|wrapperFactory
expr_stmt|;
name|this
operator|.
name|template
operator|=
name|template
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|call (final WrapperExecuteCallback cb)
specifier|public
name|void
name|call
parameter_list|(
specifier|final
name|WrapperExecuteCallback
name|cb
parameter_list|)
throws|throws
name|Exception
block|{
name|cb
operator|.
name|execute
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|executeBatch ()
specifier|public
name|int
index|[]
name|executeBatch
parameter_list|()
throws|throws
name|SQLException
block|{
if|if
condition|(
name|this
operator|.
name|batchItems
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Batch must have at least one item"
argument_list|)
throw|;
block|}
specifier|final
name|Iterator
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|?
argument_list|>
argument_list|>
name|params
init|=
name|batchItems
operator|.
name|iterator
argument_list|()
decl_stmt|;
return|return
name|factory
operator|.
name|getJdbcTemplate
argument_list|()
operator|.
name|execute
argument_list|(
operator|new
name|CallableStatementCreator
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|CallableStatement
name|createCallableStatement
parameter_list|(
name|Connection
name|connection
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|batchFactory
operator|.
name|newCallableStatementCreator
argument_list|(
name|params
operator|.
name|next
argument_list|()
argument_list|)
operator|.
name|createCallableStatement
argument_list|(
name|connection
argument_list|)
return|;
block|}
block|}
argument_list|,
operator|new
name|CallableStatementCallback
argument_list|<
name|int
index|[]
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|int
index|[]
name|doInCallableStatement
parameter_list|(
name|CallableStatement
name|callableStatement
parameter_list|)
throws|throws
name|SQLException
throws|,
name|DataAccessException
block|{
comment|//add first item to batch
name|callableStatement
operator|.
name|addBatch
argument_list|()
expr_stmt|;
while|while
condition|(
name|params
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|batchFactory
operator|.
name|addParameter
argument_list|(
name|callableStatement
argument_list|,
name|params
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|callableStatement
operator|.
name|addBatch
argument_list|()
expr_stmt|;
block|}
return|return
name|callableStatement
operator|.
name|executeBatch
argument_list|()
return|;
block|}
block|}
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getUpdateCount ()
specifier|public
name|Integer
name|getUpdateCount
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
name|this
operator|.
name|updateCount
return|;
block|}
annotation|@
name|Override
DECL|method|executeStatement ()
specifier|public
name|Object
name|executeStatement
parameter_list|()
throws|throws
name|SQLException
block|{
return|return
name|this
operator|.
name|result
return|;
block|}
annotation|@
name|Override
DECL|method|populateStatement (Object value, Exchange exchange)
specifier|public
name|void
name|populateStatement
parameter_list|(
name|Object
name|value
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|SQLException
block|{
name|this
operator|.
name|result
operator|=
name|this
operator|.
name|factory
operator|.
name|getTemplateStoredProcedure
argument_list|(
name|this
operator|.
name|template
argument_list|)
operator|.
name|execute
argument_list|(
name|exchange
argument_list|,
name|value
argument_list|)
expr_stmt|;
comment|//Spring sets #update-result-1
name|this
operator|.
name|updateCount
operator|=
operator|(
name|Integer
operator|)
name|this
operator|.
name|result
operator|.
name|get
argument_list|(
literal|"#update-count-1"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|addBatch (Object value, Exchange exchange)
specifier|public
name|void
name|addBatch
parameter_list|(
name|Object
name|value
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|this
operator|.
name|batchFactory
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|batchFactory
operator|=
name|factory
operator|.
name|getTemplateForBatch
argument_list|(
name|template
argument_list|)
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|batchValues
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|//only IN-parameters supported by template
for|for
control|(
name|Object
name|param
range|:
name|this
operator|.
name|batchFactory
operator|.
name|getTemplate
argument_list|()
operator|.
name|getParameterList
argument_list|()
control|)
block|{
name|InParameter
name|inputParameter
init|=
operator|(
name|InParameter
operator|)
name|param
decl_stmt|;
name|Object
name|paramValue
init|=
name|inputParameter
operator|.
name|getValueExtractor
argument_list|()
operator|.
name|eval
argument_list|(
name|exchange
argument_list|,
name|value
argument_list|)
decl_stmt|;
name|batchValues
operator|.
name|put
argument_list|(
name|inputParameter
operator|.
name|getName
argument_list|()
argument_list|,
name|paramValue
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|this
operator|.
name|batchItems
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|batchItems
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|batchItems
operator|.
name|add
argument_list|(
name|batchValues
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

