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
name|SQLException
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
name|SqlHelper
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
name|springframework
operator|.
name|dao
operator|.
name|DataAccessException
import|;
end_import

begin_class
DECL|class|SqlStoredProducer
specifier|public
class|class
name|SqlStoredProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|resolvedTemplate
specifier|private
name|String
name|resolvedTemplate
decl_stmt|;
DECL|field|callableStatementWrapperFactory
specifier|private
name|CallableStatementWrapperFactory
name|callableStatementWrapperFactory
decl_stmt|;
DECL|method|SqlStoredProducer (SqlStoredEndpoint endpoint)
specifier|public
name|SqlStoredProducer
parameter_list|(
name|SqlStoredEndpoint
name|endpoint
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getEndpoint ()
specifier|public
name|SqlStoredEndpoint
name|getEndpoint
parameter_list|()
block|{
return|return
operator|(
name|SqlStoredEndpoint
operator|)
name|super
operator|.
name|getEndpoint
argument_list|()
return|;
block|}
DECL|method|process (final Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|StatementWrapper
name|statementWrapper
init|=
name|createStatement
argument_list|(
name|exchange
argument_list|)
decl_stmt|;
name|statementWrapper
operator|.
name|call
argument_list|(
operator|new
name|WrapperExecuteCallback
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|execute
parameter_list|(
name|StatementWrapper
name|ps
parameter_list|)
throws|throws
name|SQLException
throws|,
name|DataAccessException
block|{
comment|// transfer incoming message body data to prepared statement parameters, if necessary
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isBatch
argument_list|()
condition|)
block|{
name|Iterator
argument_list|<
name|?
argument_list|>
name|iterator
decl_stmt|;
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isUseMessageBodyForTemplate
argument_list|()
condition|)
block|{
name|iterator
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SqlStoredConstants
operator|.
name|SQL_STORED_PARAMETERS
argument_list|,
name|Iterator
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|iterator
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|Iterator
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|iterator
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"batch=true but Iterator cannot be found from body or header"
argument_list|)
throw|;
block|}
while|while
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Object
name|value
init|=
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
name|ps
operator|.
name|addBatch
argument_list|(
name|value
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|Object
name|value
decl_stmt|;
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isUseMessageBodyForTemplate
argument_list|()
condition|)
block|{
name|value
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SqlStoredConstants
operator|.
name|SQL_STORED_PARAMETERS
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|value
operator|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
expr_stmt|;
block|}
name|ps
operator|.
name|populateStatement
argument_list|(
name|value
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
comment|// call the prepared statement and populate the outgoing message
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isBatch
argument_list|()
condition|)
block|{
name|int
index|[]
name|updateCounts
init|=
name|ps
operator|.
name|executeBatch
argument_list|()
decl_stmt|;
name|int
name|total
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|count
range|:
name|updateCounts
control|)
block|{
name|total
operator|+=
name|count
expr_stmt|;
block|}
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SqlStoredConstants
operator|.
name|SQL_STORED_UPDATE_COUNT
argument_list|,
name|total
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Object
name|result
init|=
name|ps
operator|.
name|executeStatement
argument_list|()
decl_stmt|;
comment|// preserve headers first, so we can override the SQL_ROW_COUNT and SQL_UPDATE_COUNT headers
comment|// if statement returns them
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
if|if
condition|(
name|result
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isNoop
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
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
elseif|else
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|getOutputHeader
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
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
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getOutputHeader
argument_list|()
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
block|}
comment|// for noop=true we still want to enrich with the headers
if|if
condition|(
name|ps
operator|.
name|getUpdateCount
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|SqlStoredConstants
operator|.
name|SQL_STORED_UPDATE_COUNT
argument_list|,
name|ps
operator|.
name|getUpdateCount
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|createStatement (Exchange exchange)
specifier|private
name|StatementWrapper
name|createStatement
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|SQLException
block|{
name|String
name|sql
decl_stmt|;
if|if
condition|(
name|getEndpoint
argument_list|()
operator|.
name|isUseMessageBodyForTemplate
argument_list|()
condition|)
block|{
name|sql
operator|=
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
expr_stmt|;
block|}
else|else
block|{
name|String
name|templateHeader
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|SqlStoredConstants
operator|.
name|SQL_STORED_TEMPLATE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|sql
operator|=
name|templateHeader
operator|!=
literal|null
condition|?
name|templateHeader
else|:
name|resolvedTemplate
expr_stmt|;
block|}
try|try
block|{
name|sql
operator|=
name|SqlHelper
operator|.
name|resolveQuery
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|sql
argument_list|,
literal|null
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
literal|"Error loading template resource: "
operator|+
name|sql
argument_list|,
name|e
argument_list|)
throw|;
block|}
return|return
name|getEndpoint
argument_list|()
operator|.
name|getWrapperFactory
argument_list|()
operator|.
name|create
argument_list|(
name|sql
argument_list|)
return|;
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
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|resolvedTemplate
operator|=
name|SqlHelper
operator|.
name|resolveQuery
argument_list|(
name|getEndpoint
argument_list|()
operator|.
name|getCamelContext
argument_list|()
argument_list|,
name|getEndpoint
argument_list|()
operator|.
name|getTemplate
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

