begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|TemplateParser
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
name|camel
operator|.
name|util
operator|.
name|LRUCache
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
name|JdbcTemplate
import|;
end_import

begin_comment
comment|/**  * Stateful class that cached template functions.  */
end_comment

begin_class
DECL|class|CallableStatementWrapperFactory
specifier|public
class|class
name|CallableStatementWrapperFactory
extends|extends
name|ServiceSupport
block|{
DECL|field|TEMPLATE_CACHE_DEFAULT_SIZE
specifier|public
specifier|static
specifier|final
name|int
name|TEMPLATE_CACHE_DEFAULT_SIZE
init|=
literal|200
decl_stmt|;
DECL|field|BATCH_TEMPLATE_CACHE_DEFAULT_SIZE
specifier|public
specifier|static
specifier|final
name|int
name|BATCH_TEMPLATE_CACHE_DEFAULT_SIZE
init|=
literal|200
decl_stmt|;
DECL|field|jdbcTemplate
specifier|final
name|JdbcTemplate
name|jdbcTemplate
decl_stmt|;
DECL|field|templateParser
specifier|final
name|TemplateParser
name|templateParser
decl_stmt|;
DECL|field|function
name|boolean
name|function
decl_stmt|;
DECL|field|templateCache
specifier|private
specifier|final
name|LRUCache
argument_list|<
name|String
argument_list|,
name|TemplateStoredProcedure
argument_list|>
name|templateCache
init|=
operator|new
name|LRUCache
argument_list|<>
argument_list|(
name|TEMPLATE_CACHE_DEFAULT_SIZE
argument_list|)
decl_stmt|;
DECL|field|batchTemplateCache
specifier|private
specifier|final
name|LRUCache
argument_list|<
name|String
argument_list|,
name|BatchCallableStatementCreatorFactory
argument_list|>
name|batchTemplateCache
init|=
operator|new
name|LRUCache
argument_list|<>
argument_list|(
name|BATCH_TEMPLATE_CACHE_DEFAULT_SIZE
argument_list|)
decl_stmt|;
DECL|method|CallableStatementWrapperFactory (JdbcTemplate jdbcTemplate, TemplateParser templateParser, boolean function)
specifier|public
name|CallableStatementWrapperFactory
parameter_list|(
name|JdbcTemplate
name|jdbcTemplate
parameter_list|,
name|TemplateParser
name|templateParser
parameter_list|,
name|boolean
name|function
parameter_list|)
block|{
name|this
operator|.
name|jdbcTemplate
operator|=
name|jdbcTemplate
expr_stmt|;
name|this
operator|.
name|templateParser
operator|=
name|templateParser
expr_stmt|;
name|this
operator|.
name|function
operator|=
name|function
expr_stmt|;
block|}
DECL|method|create (String sql)
specifier|public
name|StatementWrapper
name|create
parameter_list|(
name|String
name|sql
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
operator|new
name|CallableStatementWrapper
argument_list|(
name|sql
argument_list|,
name|this
argument_list|)
return|;
block|}
DECL|method|getTemplateForBatch (String sql)
specifier|public
name|BatchCallableStatementCreatorFactory
name|getTemplateForBatch
parameter_list|(
name|String
name|sql
parameter_list|)
block|{
name|BatchCallableStatementCreatorFactory
name|template
init|=
name|this
operator|.
name|batchTemplateCache
operator|.
name|get
argument_list|(
name|sql
argument_list|)
decl_stmt|;
if|if
condition|(
name|template
operator|!=
literal|null
condition|)
block|{
return|return
name|template
return|;
block|}
name|template
operator|=
operator|new
name|BatchCallableStatementCreatorFactory
argument_list|(
name|templateParser
operator|.
name|parseTemplate
argument_list|(
name|sql
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|batchTemplateCache
operator|.
name|put
argument_list|(
name|sql
argument_list|,
name|template
argument_list|)
expr_stmt|;
return|return
name|template
return|;
block|}
DECL|method|getTemplateStoredProcedure (String sql)
specifier|public
name|TemplateStoredProcedure
name|getTemplateStoredProcedure
parameter_list|(
name|String
name|sql
parameter_list|)
block|{
name|TemplateStoredProcedure
name|templateStoredProcedure
init|=
name|this
operator|.
name|templateCache
operator|.
name|get
argument_list|(
name|sql
argument_list|)
decl_stmt|;
if|if
condition|(
name|templateStoredProcedure
operator|!=
literal|null
condition|)
block|{
return|return
name|templateStoredProcedure
return|;
block|}
name|templateStoredProcedure
operator|=
operator|new
name|TemplateStoredProcedure
argument_list|(
name|jdbcTemplate
argument_list|,
name|templateParser
operator|.
name|parseTemplate
argument_list|(
name|sql
argument_list|)
argument_list|,
name|function
argument_list|)
expr_stmt|;
name|this
operator|.
name|templateCache
operator|.
name|put
argument_list|(
name|sql
argument_list|,
name|templateStoredProcedure
argument_list|)
expr_stmt|;
return|return
name|templateStoredProcedure
return|;
block|}
DECL|method|getJdbcTemplate ()
specifier|public
name|JdbcTemplate
name|getJdbcTemplate
parameter_list|()
block|{
return|return
name|jdbcTemplate
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
block|{     }
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
try|try
block|{
comment|// clear cache when we are stopping
name|templateCache
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//noop
block|}
try|try
block|{
comment|// clear cache when we are stopping
name|batchTemplateCache
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//noop
block|}
block|}
block|}
end_class

end_unit

