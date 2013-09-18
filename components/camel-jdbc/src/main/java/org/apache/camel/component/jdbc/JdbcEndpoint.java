begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jdbc
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jdbc
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
name|Component
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
name|Consumer
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
name|Processor
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
name|Producer
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
name|DefaultEndpoint
import|;
end_import

begin_comment
comment|/**  * @version  */
end_comment

begin_class
DECL|class|JdbcEndpoint
specifier|public
class|class
name|JdbcEndpoint
extends|extends
name|DefaultEndpoint
block|{
DECL|field|readSize
specifier|private
name|int
name|readSize
decl_stmt|;
DECL|field|transacted
specifier|private
name|boolean
name|transacted
decl_stmt|;
DECL|field|resetAutoCommit
specifier|private
name|boolean
name|resetAutoCommit
init|=
literal|true
decl_stmt|;
DECL|field|dataSource
specifier|private
name|DataSource
name|dataSource
decl_stmt|;
DECL|field|parameters
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
decl_stmt|;
DECL|field|useJDBC4ColumnNameAndLabelSemantics
specifier|private
name|boolean
name|useJDBC4ColumnNameAndLabelSemantics
init|=
literal|true
decl_stmt|;
DECL|field|prepareStatementStrategy
specifier|private
name|JdbcPrepareStatementStrategy
name|prepareStatementStrategy
init|=
operator|new
name|DefaultJdbcPrepareStatementStrategy
argument_list|()
decl_stmt|;
DECL|field|allowNamedParameters
specifier|private
name|boolean
name|allowNamedParameters
init|=
literal|true
decl_stmt|;
DECL|field|useHeadersAsParameters
specifier|private
name|boolean
name|useHeadersAsParameters
decl_stmt|;
DECL|field|outputType
specifier|private
name|JdbcOutputType
name|outputType
init|=
name|JdbcOutputType
operator|.
name|SelectList
decl_stmt|;
DECL|field|outputClass
specifier|private
name|String
name|outputClass
decl_stmt|;
DECL|field|beanRowMapper
specifier|private
name|BeanRowMapper
name|beanRowMapper
init|=
operator|new
name|DefaultBeanRowMapper
argument_list|()
decl_stmt|;
DECL|method|JdbcEndpoint ()
specifier|public
name|JdbcEndpoint
parameter_list|()
block|{     }
DECL|method|JdbcEndpoint (String endpointUri, Component component, DataSource dataSource)
specifier|public
name|JdbcEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|Component
name|component
parameter_list|,
name|DataSource
name|dataSource
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|dataSource
operator|=
name|dataSource
expr_stmt|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Not supported"
argument_list|)
throw|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|JdbcProducer
argument_list|(
name|this
argument_list|,
name|dataSource
argument_list|,
name|readSize
argument_list|,
name|parameters
argument_list|)
return|;
block|}
DECL|method|getReadSize ()
specifier|public
name|int
name|getReadSize
parameter_list|()
block|{
return|return
name|readSize
return|;
block|}
DECL|method|setReadSize (int readSize)
specifier|public
name|void
name|setReadSize
parameter_list|(
name|int
name|readSize
parameter_list|)
block|{
name|this
operator|.
name|readSize
operator|=
name|readSize
expr_stmt|;
block|}
DECL|method|isTransacted ()
specifier|public
name|boolean
name|isTransacted
parameter_list|()
block|{
return|return
name|transacted
return|;
block|}
DECL|method|setTransacted (boolean transacted)
specifier|public
name|void
name|setTransacted
parameter_list|(
name|boolean
name|transacted
parameter_list|)
block|{
name|this
operator|.
name|transacted
operator|=
name|transacted
expr_stmt|;
block|}
DECL|method|isResetAutoCommit ()
specifier|public
name|boolean
name|isResetAutoCommit
parameter_list|()
block|{
return|return
name|resetAutoCommit
return|;
block|}
DECL|method|setResetAutoCommit (boolean resetAutoCommit)
specifier|public
name|void
name|setResetAutoCommit
parameter_list|(
name|boolean
name|resetAutoCommit
parameter_list|)
block|{
name|this
operator|.
name|resetAutoCommit
operator|=
name|resetAutoCommit
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
DECL|method|getParameters ()
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getParameters
parameter_list|()
block|{
return|return
name|parameters
return|;
block|}
comment|/**      * Optional parameters to the {@link java.sql.Statement}.      *<p/>      * For example to set maxRows, fetchSize etc.      *      * @param parameters parameters which will be set using reflection      */
DECL|method|setParameters (Map<String, Object> parameters)
specifier|public
name|void
name|setParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
block|{
name|this
operator|.
name|parameters
operator|=
name|parameters
expr_stmt|;
block|}
DECL|method|isUseJDBC4ColumnNameAndLabelSemantics ()
specifier|public
name|boolean
name|isUseJDBC4ColumnNameAndLabelSemantics
parameter_list|()
block|{
return|return
name|useJDBC4ColumnNameAndLabelSemantics
return|;
block|}
comment|/**      * Sets whether to use JDBC 4 or JDBC 3.0 or older semantic when retrieving column name.      *<p/>      * JDBC 4.0 uses columnLabel to get the column name where as JDBC 3.0 uses both columnName or columnLabel.      * Unfortunately JDBC drivers behave differently so you can use this option to work out issues around your      * JDBC driver if you get problem using this component      *<p/>      * This option is default<tt>true</tt>.      *      * @param useJDBC4ColumnNameAndLabelSemantics      *<tt>true</tt> to use JDBC 4.0 semantics,<tt>false</tt> to use JDBC 3.0.      */
DECL|method|setUseJDBC4ColumnNameAndLabelSemantics (boolean useJDBC4ColumnNameAndLabelSemantics)
specifier|public
name|void
name|setUseJDBC4ColumnNameAndLabelSemantics
parameter_list|(
name|boolean
name|useJDBC4ColumnNameAndLabelSemantics
parameter_list|)
block|{
name|this
operator|.
name|useJDBC4ColumnNameAndLabelSemantics
operator|=
name|useJDBC4ColumnNameAndLabelSemantics
expr_stmt|;
block|}
DECL|method|getPrepareStatementStrategy ()
specifier|public
name|JdbcPrepareStatementStrategy
name|getPrepareStatementStrategy
parameter_list|()
block|{
return|return
name|prepareStatementStrategy
return|;
block|}
DECL|method|setPrepareStatementStrategy (JdbcPrepareStatementStrategy prepareStatementStrategy)
specifier|public
name|void
name|setPrepareStatementStrategy
parameter_list|(
name|JdbcPrepareStatementStrategy
name|prepareStatementStrategy
parameter_list|)
block|{
name|this
operator|.
name|prepareStatementStrategy
operator|=
name|prepareStatementStrategy
expr_stmt|;
block|}
DECL|method|isAllowNamedParameters ()
specifier|public
name|boolean
name|isAllowNamedParameters
parameter_list|()
block|{
return|return
name|allowNamedParameters
return|;
block|}
DECL|method|setAllowNamedParameters (boolean allowNamedParameters)
specifier|public
name|void
name|setAllowNamedParameters
parameter_list|(
name|boolean
name|allowNamedParameters
parameter_list|)
block|{
name|this
operator|.
name|allowNamedParameters
operator|=
name|allowNamedParameters
expr_stmt|;
block|}
DECL|method|isUseHeadersAsParameters ()
specifier|public
name|boolean
name|isUseHeadersAsParameters
parameter_list|()
block|{
return|return
name|useHeadersAsParameters
return|;
block|}
DECL|method|setUseHeadersAsParameters (boolean useHeadersAsParameters)
specifier|public
name|void
name|setUseHeadersAsParameters
parameter_list|(
name|boolean
name|useHeadersAsParameters
parameter_list|)
block|{
name|this
operator|.
name|useHeadersAsParameters
operator|=
name|useHeadersAsParameters
expr_stmt|;
block|}
DECL|method|getOutputType ()
specifier|public
name|JdbcOutputType
name|getOutputType
parameter_list|()
block|{
return|return
name|outputType
return|;
block|}
DECL|method|setOutputType (JdbcOutputType outputType)
specifier|public
name|void
name|setOutputType
parameter_list|(
name|JdbcOutputType
name|outputType
parameter_list|)
block|{
name|this
operator|.
name|outputType
operator|=
name|outputType
expr_stmt|;
block|}
DECL|method|getOutputClass ()
specifier|public
name|String
name|getOutputClass
parameter_list|()
block|{
return|return
name|outputClass
return|;
block|}
DECL|method|setOutputClass (String outputClass)
specifier|public
name|void
name|setOutputClass
parameter_list|(
name|String
name|outputClass
parameter_list|)
block|{
name|this
operator|.
name|outputClass
operator|=
name|outputClass
expr_stmt|;
block|}
DECL|method|getBeanRowMapper ()
specifier|public
name|BeanRowMapper
name|getBeanRowMapper
parameter_list|()
block|{
return|return
name|beanRowMapper
return|;
block|}
DECL|method|setBeanRowMapper (BeanRowMapper beanRowMapper)
specifier|public
name|void
name|setBeanRowMapper
parameter_list|(
name|BeanRowMapper
name|beanRowMapper
parameter_list|)
block|{
name|this
operator|.
name|beanRowMapper
operator|=
name|beanRowMapper
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createEndpointUri ()
specifier|protected
name|String
name|createEndpointUri
parameter_list|()
block|{
return|return
literal|"jdbc"
return|;
block|}
block|}
end_class

end_unit

