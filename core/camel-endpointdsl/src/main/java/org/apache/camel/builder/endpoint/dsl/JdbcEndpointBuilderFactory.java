begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder.endpoint.dsl
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|endpoint
operator|.
name|dsl
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
name|annotation
operator|.
name|Generated
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
name|builder
operator|.
name|EndpointConsumerBuilder
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
name|builder
operator|.
name|EndpointProducerBuilder
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
name|builder
operator|.
name|endpoint
operator|.
name|AbstractEndpointBuilder
import|;
end_import

begin_comment
comment|/**  * The jdbc component enables you to access databases through JDBC, where SQL  * queries are sent in the message body.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|JdbcEndpointBuilderFactory
specifier|public
interface|interface
name|JdbcEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the JDBC component.      */
DECL|interface|JdbcEndpointBuilder
specifier|public
interface|interface
name|JdbcEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedJdbcEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedJdbcEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether to allow using named parameters in the queries.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|allowNamedParameters ( boolean allowNamedParameters)
specifier|default
name|JdbcEndpointBuilder
name|allowNamedParameters
parameter_list|(
name|boolean
name|allowNamedParameters
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"allowNamedParameters"
argument_list|,
name|allowNamedParameters
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether to allow using named parameters in the queries.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|allowNamedParameters ( String allowNamedParameters)
specifier|default
name|JdbcEndpointBuilder
name|allowNamedParameters
parameter_list|(
name|String
name|allowNamedParameters
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"allowNamedParameters"
argument_list|,
name|allowNamedParameters
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Specify the full package and class name to use as conversion when          * outputType=SelectOne or SelectList.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|outputClass (String outputClass)
specifier|default
name|JdbcEndpointBuilder
name|outputClass
parameter_list|(
name|String
name|outputClass
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"outputClass"
argument_list|,
name|outputClass
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Determines the output the producer should use.          *           * The option is a:          *<code>org.apache.camel.component.jdbc.JdbcOutputType</code> type.          *           * Group: producer          */
DECL|method|outputType (JdbcOutputType outputType)
specifier|default
name|JdbcEndpointBuilder
name|outputType
parameter_list|(
name|JdbcOutputType
name|outputType
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"outputType"
argument_list|,
name|outputType
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Determines the output the producer should use.          *           * The option will be converted to a          *<code>org.apache.camel.component.jdbc.JdbcOutputType</code> type.          *           * Group: producer          */
DECL|method|outputType (String outputType)
specifier|default
name|JdbcEndpointBuilder
name|outputType
parameter_list|(
name|String
name|outputType
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"outputType"
argument_list|,
name|outputType
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Optional parameters to the java.sql.Statement. For example to set          * maxRows, fetchSize etc.          *           * The option is a:<code>java.util.Map&lt;java.lang.String,          * java.lang.Object&gt;</code> type.          *           * Group: producer          */
DECL|method|parameters (Map<String, Object> parameters)
specifier|default
name|JdbcEndpointBuilder
name|parameters
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
name|doSetProperty
argument_list|(
literal|"parameters"
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Optional parameters to the java.sql.Statement. For example to set          * maxRows, fetchSize etc.          *           * The option will be converted to a          *<code>java.util.Map&lt;java.lang.String, java.lang.Object&gt;</code>          * type.          *           * Group: producer          */
DECL|method|parameters (String parameters)
specifier|default
name|JdbcEndpointBuilder
name|parameters
parameter_list|(
name|String
name|parameters
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"parameters"
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The default maximum number of rows that can be read by a polling          * query. The default value is 0.          *           * The option is a:<code>int</code> type.          *           * Group: producer          */
DECL|method|readSize (int readSize)
specifier|default
name|JdbcEndpointBuilder
name|readSize
parameter_list|(
name|int
name|readSize
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"readSize"
argument_list|,
name|readSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The default maximum number of rows that can be read by a polling          * query. The default value is 0.          *           * The option will be converted to a<code>int</code> type.          *           * Group: producer          */
DECL|method|readSize (String readSize)
specifier|default
name|JdbcEndpointBuilder
name|readSize
parameter_list|(
name|String
name|readSize
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"readSize"
argument_list|,
name|readSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Camel will set the autoCommit on the JDBC connection to be false,          * commit the change after executed the statement and reset the          * autoCommit flag of the connection at the end, if the resetAutoCommit          * is true. If the JDBC connection doesn't support to reset the          * autoCommit flag, you can set the resetAutoCommit flag to be false,          * and Camel will not try to reset the autoCommit flag. When used with          * XA transactions you most likely need to set it to false so that the          * transaction manager is in charge of committing this tx.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|resetAutoCommit (boolean resetAutoCommit)
specifier|default
name|JdbcEndpointBuilder
name|resetAutoCommit
parameter_list|(
name|boolean
name|resetAutoCommit
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"resetAutoCommit"
argument_list|,
name|resetAutoCommit
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Camel will set the autoCommit on the JDBC connection to be false,          * commit the change after executed the statement and reset the          * autoCommit flag of the connection at the end, if the resetAutoCommit          * is true. If the JDBC connection doesn't support to reset the          * autoCommit flag, you can set the resetAutoCommit flag to be false,          * and Camel will not try to reset the autoCommit flag. When used with          * XA transactions you most likely need to set it to false so that the          * transaction manager is in charge of committing this tx.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|resetAutoCommit (String resetAutoCommit)
specifier|default
name|JdbcEndpointBuilder
name|resetAutoCommit
parameter_list|(
name|String
name|resetAutoCommit
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"resetAutoCommit"
argument_list|,
name|resetAutoCommit
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether transactions are in use.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|transacted (boolean transacted)
specifier|default
name|JdbcEndpointBuilder
name|transacted
parameter_list|(
name|boolean
name|transacted
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"transacted"
argument_list|,
name|transacted
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether transactions are in use.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|transacted (String transacted)
specifier|default
name|JdbcEndpointBuilder
name|transacted
parameter_list|(
name|String
name|transacted
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"transacted"
argument_list|,
name|transacted
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To read BLOB columns as bytes instead of string data. This may be          * needed for certain databases such as Oracle where you must read BLOB          * columns as bytes.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|useGetBytesForBlob ( boolean useGetBytesForBlob)
specifier|default
name|JdbcEndpointBuilder
name|useGetBytesForBlob
parameter_list|(
name|boolean
name|useGetBytesForBlob
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"useGetBytesForBlob"
argument_list|,
name|useGetBytesForBlob
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To read BLOB columns as bytes instead of string data. This may be          * needed for certain databases such as Oracle where you must read BLOB          * columns as bytes.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|useGetBytesForBlob (String useGetBytesForBlob)
specifier|default
name|JdbcEndpointBuilder
name|useGetBytesForBlob
parameter_list|(
name|String
name|useGetBytesForBlob
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"useGetBytesForBlob"
argument_list|,
name|useGetBytesForBlob
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Set this option to true to use the prepareStatementStrategy with          * named parameters. This allows to define queries with named          * placeholders, and use headers with the dynamic values for the query          * placeholders.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|useHeadersAsParameters ( boolean useHeadersAsParameters)
specifier|default
name|JdbcEndpointBuilder
name|useHeadersAsParameters
parameter_list|(
name|boolean
name|useHeadersAsParameters
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"useHeadersAsParameters"
argument_list|,
name|useHeadersAsParameters
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Set this option to true to use the prepareStatementStrategy with          * named parameters. This allows to define queries with named          * placeholders, and use headers with the dynamic values for the query          * placeholders.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|useHeadersAsParameters ( String useHeadersAsParameters)
specifier|default
name|JdbcEndpointBuilder
name|useHeadersAsParameters
parameter_list|(
name|String
name|useHeadersAsParameters
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"useHeadersAsParameters"
argument_list|,
name|useHeadersAsParameters
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether to use JDBC 4 or JDBC 3.0 or older semantic when          * retrieving column name. JDBC 4.0 uses columnLabel to get the column          * name where as JDBC 3.0 uses both columnName or columnLabel.          * Unfortunately JDBC drivers behave differently so you can use this          * option to work out issues around your JDBC driver if you get problem          * using this component This option is default true.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|useJDBC4ColumnNameAndLabelSemantics ( boolean useJDBC4ColumnNameAndLabelSemantics)
specifier|default
name|JdbcEndpointBuilder
name|useJDBC4ColumnNameAndLabelSemantics
parameter_list|(
name|boolean
name|useJDBC4ColumnNameAndLabelSemantics
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"useJDBC4ColumnNameAndLabelSemantics"
argument_list|,
name|useJDBC4ColumnNameAndLabelSemantics
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether to use JDBC 4 or JDBC 3.0 or older semantic when          * retrieving column name. JDBC 4.0 uses columnLabel to get the column          * name where as JDBC 3.0 uses both columnName or columnLabel.          * Unfortunately JDBC drivers behave differently so you can use this          * option to work out issues around your JDBC driver if you get problem          * using this component This option is default true.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|useJDBC4ColumnNameAndLabelSemantics ( String useJDBC4ColumnNameAndLabelSemantics)
specifier|default
name|JdbcEndpointBuilder
name|useJDBC4ColumnNameAndLabelSemantics
parameter_list|(
name|String
name|useJDBC4ColumnNameAndLabelSemantics
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"useJDBC4ColumnNameAndLabelSemantics"
argument_list|,
name|useJDBC4ColumnNameAndLabelSemantics
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the JDBC component.      */
DECL|interface|AdvancedJdbcEndpointBuilder
specifier|public
interface|interface
name|AdvancedJdbcEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|JdbcEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|JdbcEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedJdbcEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedJdbcEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"basicPropertyBinding"
argument_list|,
name|basicPropertyBinding
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom org.apache.camel.component.jdbc.BeanRowMapper when          * using outputClass. The default implementation will lower case the row          * names and skip underscores, and dashes. For example CUST_ID is mapped          * as custId.          *           * The option is a:          *<code>org.apache.camel.component.jdbc.BeanRowMapper</code> type.          *           * Group: advanced          */
DECL|method|beanRowMapper (Object beanRowMapper)
specifier|default
name|AdvancedJdbcEndpointBuilder
name|beanRowMapper
parameter_list|(
name|Object
name|beanRowMapper
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"beanRowMapper"
argument_list|,
name|beanRowMapper
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * To use a custom org.apache.camel.component.jdbc.BeanRowMapper when          * using outputClass. The default implementation will lower case the row          * names and skip underscores, and dashes. For example CUST_ID is mapped          * as custId.          *           * The option will be converted to a          *<code>org.apache.camel.component.jdbc.BeanRowMapper</code> type.          *           * Group: advanced          */
DECL|method|beanRowMapper (String beanRowMapper)
specifier|default
name|AdvancedJdbcEndpointBuilder
name|beanRowMapper
parameter_list|(
name|String
name|beanRowMapper
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"beanRowMapper"
argument_list|,
name|beanRowMapper
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows to plugin to use a custom          * org.apache.camel.component.jdbc.JdbcPrepareStatementStrategy to          * control preparation of the query and prepared statement.          *           * The option is a:          *<code>org.apache.camel.component.jdbc.JdbcPrepareStatementStrategy</code> type.          *           * Group: advanced          */
DECL|method|prepareStatementStrategy ( Object prepareStatementStrategy)
specifier|default
name|AdvancedJdbcEndpointBuilder
name|prepareStatementStrategy
parameter_list|(
name|Object
name|prepareStatementStrategy
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"prepareStatementStrategy"
argument_list|,
name|prepareStatementStrategy
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Allows to plugin to use a custom          * org.apache.camel.component.jdbc.JdbcPrepareStatementStrategy to          * control preparation of the query and prepared statement.          *           * The option will be converted to a          *<code>org.apache.camel.component.jdbc.JdbcPrepareStatementStrategy</code> type.          *           * Group: advanced          */
DECL|method|prepareStatementStrategy ( String prepareStatementStrategy)
specifier|default
name|AdvancedJdbcEndpointBuilder
name|prepareStatementStrategy
parameter_list|(
name|String
name|prepareStatementStrategy
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"prepareStatementStrategy"
argument_list|,
name|prepareStatementStrategy
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous (boolean synchronous)
specifier|default
name|AdvancedJdbcEndpointBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous (String synchronous)
specifier|default
name|AdvancedJdbcEndpointBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"synchronous"
argument_list|,
name|synchronous
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Proxy enum for      *<code>org.apache.camel.component.jdbc.JdbcOutputType</code> enum.      */
DECL|enum|JdbcOutputType
enum|enum
name|JdbcOutputType
block|{
DECL|enumConstant|SelectOne
name|SelectOne
block|,
DECL|enumConstant|SelectList
name|SelectList
block|,
DECL|enumConstant|StreamList
name|StreamList
block|;     }
comment|/**      * JDBC (camel-jdbc)      * The jdbc component enables you to access databases through JDBC, where      * SQL queries are sent in the message body.      *       * Category: database,sql      * Available as of version: 1.2      * Maven coordinates: org.apache.camel:camel-jdbc      *       * Syntax:<code>jdbc:dataSourceName</code>      *       * Path parameter: dataSourceName (required)      * Name of DataSource to lookup in the Registry. If the name is dataSource      * or default, then Camel will attempt to lookup a default DataSource from      * the registry, meaning if there is a only one instance of DataSource      * found, then this DataSource will be used.      */
DECL|method|jdbc (String path)
specifier|default
name|JdbcEndpointBuilder
name|jdbc
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|JdbcEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|JdbcEndpointBuilder
implements|,
name|AdvancedJdbcEndpointBuilder
block|{
specifier|public
name|JdbcEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"jdbc"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|JdbcEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

