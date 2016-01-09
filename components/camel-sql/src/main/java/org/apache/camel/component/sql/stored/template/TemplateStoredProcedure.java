begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sql.stored.template
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
operator|.
name|template
package|;
end_package

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
name|InputParameter
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
name|OutParameter
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
name|Template
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
name|SqlOutParameter
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
name|SqlParameter
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
name|object
operator|.
name|StoredProcedure
import|;
end_import

begin_class
DECL|class|TemplateStoredProcedure
specifier|public
class|class
name|TemplateStoredProcedure
extends|extends
name|StoredProcedure
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
name|TemplateStoredProcedure
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|template
specifier|private
specifier|final
name|Template
name|template
decl_stmt|;
DECL|method|TemplateStoredProcedure (JdbcTemplate jdbcTemplate, Template template)
specifier|public
name|TemplateStoredProcedure
parameter_list|(
name|JdbcTemplate
name|jdbcTemplate
parameter_list|,
name|Template
name|template
parameter_list|)
block|{
name|this
operator|.
name|template
operator|=
name|template
expr_stmt|;
name|setDataSource
argument_list|(
name|jdbcTemplate
operator|.
name|getDataSource
argument_list|()
argument_list|)
expr_stmt|;
name|setSql
argument_list|(
name|template
operator|.
name|getProcedureName
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|InputParameter
name|inputParameter
range|:
name|template
operator|.
name|getInputParameterList
argument_list|()
control|)
block|{
name|declareParameter
argument_list|(
operator|new
name|SqlParameter
argument_list|(
name|inputParameter
operator|.
name|getName
argument_list|()
argument_list|,
name|inputParameter
operator|.
name|getSqlType
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|OutParameter
name|outParameter
range|:
name|template
operator|.
name|getOutParameterList
argument_list|()
control|)
block|{
name|declareParameter
argument_list|(
operator|new
name|SqlOutParameter
argument_list|(
name|outParameter
operator|.
name|getName
argument_list|()
argument_list|,
name|outParameter
operator|.
name|getSqlType
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|setFunction
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Compiling stored procedure: {}"
argument_list|,
name|template
operator|.
name|getProcedureName
argument_list|()
argument_list|)
expr_stmt|;
name|compile
argument_list|()
expr_stmt|;
block|}
DECL|method|execute (Exchange exchange)
specifier|public
name|void
name|execute
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|params
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|InputParameter
name|inputParameter
range|:
name|template
operator|.
name|getInputParameterList
argument_list|()
control|)
block|{
name|params
operator|.
name|put
argument_list|(
name|inputParameter
operator|.
name|getName
argument_list|()
argument_list|,
name|inputParameter
operator|.
name|getValueExpression
argument_list|()
operator|.
name|evaluate
argument_list|(
name|exchange
argument_list|,
name|inputParameter
operator|.
name|getJavaType
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|LOG
operator|.
name|debug
argument_list|(
literal|"Invoking stored procedure: {}"
argument_list|,
name|template
operator|.
name|getProcedureName
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|ret
init|=
name|super
operator|.
name|execute
argument_list|(
name|params
argument_list|)
decl_stmt|;
for|for
control|(
name|OutParameter
name|out
range|:
name|template
operator|.
name|getOutParameterList
argument_list|()
control|)
block|{
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setHeader
argument_list|(
name|out
operator|.
name|getOutHeader
argument_list|()
argument_list|,
name|ret
operator|.
name|get
argument_list|(
name|out
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

