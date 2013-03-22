begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sql
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
name|CamelContext
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
name|Endpoint
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
name|DefaultComponent
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
name|CamelContextHelper
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
comment|/**  * @version   */
end_comment

begin_class
DECL|class|SqlComponent
specifier|public
class|class
name|SqlComponent
extends|extends
name|DefaultComponent
block|{
DECL|field|dataSource
specifier|private
name|DataSource
name|dataSource
decl_stmt|;
DECL|field|usePlaceholder
specifier|private
name|boolean
name|usePlaceholder
init|=
literal|true
decl_stmt|;
DECL|method|SqlComponent ()
specifier|public
name|SqlComponent
parameter_list|()
block|{     }
DECL|method|SqlComponent (CamelContext context)
specifier|public
name|SqlComponent
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|context
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|DataSource
name|ds
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"dataSource"
argument_list|,
name|DataSource
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|ds
operator|!=
literal|null
condition|)
block|{
name|dataSource
operator|=
name|ds
expr_stmt|;
block|}
comment|//TODO cmueller: remove the 'dataSourceRef' lookup in Camel 3.0
if|if
condition|(
name|dataSource
operator|==
literal|null
condition|)
block|{
name|String
name|dataSourceRef
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"dataSourceRef"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|dataSourceRef
operator|!=
literal|null
condition|)
block|{
name|dataSource
operator|=
name|CamelContextHelper
operator|.
name|mandatoryLookup
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|dataSourceRef
argument_list|,
name|DataSource
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
name|String
name|parameterPlaceholderSubstitute
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"placeholder"
argument_list|,
name|String
operator|.
name|class
argument_list|,
literal|"#"
argument_list|)
decl_stmt|;
name|JdbcTemplate
name|jdbcTemplate
init|=
operator|new
name|JdbcTemplate
argument_list|(
name|dataSource
argument_list|)
decl_stmt|;
name|IntrospectionSupport
operator|.
name|setProperties
argument_list|(
name|jdbcTemplate
argument_list|,
name|parameters
argument_list|,
literal|"template."
argument_list|)
expr_stmt|;
name|String
name|query
init|=
name|remaining
operator|.
name|replaceAll
argument_list|(
name|parameterPlaceholderSubstitute
argument_list|,
literal|"?"
argument_list|)
decl_stmt|;
name|String
name|onConsume
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"consumer.onConsume"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|onConsume
operator|==
literal|null
condition|)
block|{
name|onConsume
operator|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"onConsume"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|onConsume
operator|!=
literal|null
operator|&&
name|usePlaceholder
condition|)
block|{
name|onConsume
operator|=
name|onConsume
operator|.
name|replaceAll
argument_list|(
name|parameterPlaceholderSubstitute
argument_list|,
literal|"?"
argument_list|)
expr_stmt|;
block|}
name|String
name|onConsumeFailed
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"consumer.onConsumeFailed"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|onConsumeFailed
operator|==
literal|null
condition|)
block|{
name|onConsumeFailed
operator|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"onConsumeFailed"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|onConsumeFailed
operator|!=
literal|null
operator|&&
name|usePlaceholder
condition|)
block|{
name|onConsumeFailed
operator|=
name|onConsumeFailed
operator|.
name|replaceAll
argument_list|(
name|parameterPlaceholderSubstitute
argument_list|,
literal|"?"
argument_list|)
expr_stmt|;
block|}
name|String
name|onConsumeBatchComplete
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"consumer.onConsumeBatchComplete"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|onConsumeBatchComplete
operator|==
literal|null
condition|)
block|{
name|onConsumeBatchComplete
operator|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"onConsumeBatchComplete"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|onConsumeBatchComplete
operator|!=
literal|null
operator|&&
name|usePlaceholder
condition|)
block|{
name|onConsumeBatchComplete
operator|=
name|onConsumeBatchComplete
operator|.
name|replaceAll
argument_list|(
name|parameterPlaceholderSubstitute
argument_list|,
literal|"?"
argument_list|)
expr_stmt|;
block|}
name|SqlEndpoint
name|endpoint
init|=
operator|new
name|SqlEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|jdbcTemplate
argument_list|,
name|query
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|setOnConsume
argument_list|(
name|onConsume
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setOnConsumeFailed
argument_list|(
name|onConsumeFailed
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|setOnConsumeBatchComplete
argument_list|(
name|onConsumeBatchComplete
argument_list|)
expr_stmt|;
return|return
name|endpoint
return|;
block|}
DECL|method|setDataSource (DataSource dataSource)
specifier|public
name|void
name|setDataSource
parameter_list|(
name|DataSource
name|dataSource
parameter_list|)
block|{
name|this
operator|.
name|dataSource
operator|=
name|dataSource
expr_stmt|;
block|}
DECL|method|getDataSource ()
specifier|public
name|DataSource
name|getDataSource
parameter_list|()
block|{
return|return
name|dataSource
return|;
block|}
DECL|method|isUsePlaceholder ()
specifier|public
name|boolean
name|isUsePlaceholder
parameter_list|()
block|{
return|return
name|usePlaceholder
return|;
block|}
comment|/**      * Sets whether to use placeholder and replace all placeholder characters with ? sign in the SQL queries.      *<p/>      * This option is default<tt>true</tt>      */
DECL|method|setUsePlaceholder (boolean usePlaceholder)
specifier|public
name|void
name|setUsePlaceholder
parameter_list|(
name|boolean
name|usePlaceholder
parameter_list|)
block|{
name|this
operator|.
name|usePlaceholder
operator|=
name|usePlaceholder
expr_stmt|;
block|}
block|}
end_class

end_unit

