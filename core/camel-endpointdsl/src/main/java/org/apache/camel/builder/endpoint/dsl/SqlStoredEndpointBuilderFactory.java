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
comment|/**  * The sql component allows you to work with databases using JDBC Stored  * Procedure queries.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|SqlStoredEndpointBuilderFactory
specifier|public
interface|interface
name|SqlStoredEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the SQL Stored Procedure component.      */
DECL|interface|SqlStoredEndpointBuilder
specifier|public
interface|interface
name|SqlStoredEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedSqlStoredEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedSqlStoredEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Enables or disables batch mode.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|batch (boolean batch)
specifier|default
name|SqlStoredEndpointBuilder
name|batch
parameter_list|(
name|boolean
name|batch
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"batch"
argument_list|,
name|batch
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Enables or disables batch mode.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|batch (String batch)
specifier|default
name|SqlStoredEndpointBuilder
name|batch
parameter_list|(
name|String
name|batch
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"batch"
argument_list|,
name|batch
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the DataSource to use to communicate with the database.          *           * The option is a:<code>javax.sql.DataSource</code> type.          *           * Group: producer          */
DECL|method|dataSource (Object dataSource)
specifier|default
name|SqlStoredEndpointBuilder
name|dataSource
parameter_list|(
name|Object
name|dataSource
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"dataSource"
argument_list|,
name|dataSource
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets the DataSource to use to communicate with the database.          *           * The option will be converted to a<code>javax.sql.DataSource</code>          * type.          *           * Group: producer          */
DECL|method|dataSource (String dataSource)
specifier|default
name|SqlStoredEndpointBuilder
name|dataSource
parameter_list|(
name|String
name|dataSource
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"dataSource"
argument_list|,
name|dataSource
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether this call is for a function.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|function (boolean function)
specifier|default
name|SqlStoredEndpointBuilder
name|function
parameter_list|(
name|boolean
name|function
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"function"
argument_list|,
name|function
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether this call is for a function.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|function (String function)
specifier|default
name|SqlStoredEndpointBuilder
name|function
parameter_list|(
name|String
name|function
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"function"
argument_list|,
name|function
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|lazyStartProducer ( boolean lazyStartProducer)
specifier|default
name|SqlStoredEndpointBuilder
name|lazyStartProducer
parameter_list|(
name|boolean
name|lazyStartProducer
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"lazyStartProducer"
argument_list|,
name|lazyStartProducer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|lazyStartProducer ( String lazyStartProducer)
specifier|default
name|SqlStoredEndpointBuilder
name|lazyStartProducer
parameter_list|(
name|String
name|lazyStartProducer
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"lazyStartProducer"
argument_list|,
name|lazyStartProducer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If set, will ignore the results of the template and use the existing          * IN message as the OUT message for the continuation of processing.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|noop (boolean noop)
specifier|default
name|SqlStoredEndpointBuilder
name|noop
parameter_list|(
name|boolean
name|noop
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"noop"
argument_list|,
name|noop
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * If set, will ignore the results of the template and use the existing          * IN message as the OUT message for the continuation of processing.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|noop (String noop)
specifier|default
name|SqlStoredEndpointBuilder
name|noop
parameter_list|(
name|String
name|noop
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"noop"
argument_list|,
name|noop
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Store the template result in a header instead of the message body. By          * default, outputHeader == null and the template result is stored in          * the message body, any existing content in the message body is          * discarded. If outputHeader is set, the value is used as the name of          * the header to store the template result and the original message body          * is preserved.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|outputHeader (String outputHeader)
specifier|default
name|SqlStoredEndpointBuilder
name|outputHeader
parameter_list|(
name|String
name|outputHeader
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"outputHeader"
argument_list|,
name|outputHeader
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether to use the message body as the template and then headers for          * parameters. If this option is enabled then the template in the uri is          * not used.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|useMessageBodyForTemplate ( boolean useMessageBodyForTemplate)
specifier|default
name|SqlStoredEndpointBuilder
name|useMessageBodyForTemplate
parameter_list|(
name|boolean
name|useMessageBodyForTemplate
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"useMessageBodyForTemplate"
argument_list|,
name|useMessageBodyForTemplate
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether to use the message body as the template and then headers for          * parameters. If this option is enabled then the template in the uri is          * not used.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|useMessageBodyForTemplate ( String useMessageBodyForTemplate)
specifier|default
name|SqlStoredEndpointBuilder
name|useMessageBodyForTemplate
parameter_list|(
name|String
name|useMessageBodyForTemplate
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"useMessageBodyForTemplate"
argument_list|,
name|useMessageBodyForTemplate
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the SQL Stored Procedure component.      */
DECL|interface|AdvancedSqlStoredEndpointBuilder
specifier|public
interface|interface
name|AdvancedSqlStoredEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|SqlStoredEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|SqlStoredEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedSqlStoredEndpointBuilder
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
name|AdvancedSqlStoredEndpointBuilder
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
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|synchronous (boolean synchronous)
specifier|default
name|AdvancedSqlStoredEndpointBuilder
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
name|AdvancedSqlStoredEndpointBuilder
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
comment|/**      * SQL Stored Procedure (camel-sql)      * The sql component allows you to work with databases using JDBC Stored      * Procedure queries.      *       * Category: database,sql      * Available as of version: 2.17      * Maven coordinates: org.apache.camel:camel-sql      *       * Syntax:<code>sql-stored:template</code>      *       * Path parameter: template (required)      * Sets the StoredProcedure template to perform      */
DECL|method|sqlStored (String path)
specifier|default
name|SqlStoredEndpointBuilder
name|sqlStored
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|SqlStoredEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|SqlStoredEndpointBuilder
implements|,
name|AdvancedSqlStoredEndpointBuilder
block|{
specifier|public
name|SqlStoredEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"sql-stored"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|SqlStoredEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

