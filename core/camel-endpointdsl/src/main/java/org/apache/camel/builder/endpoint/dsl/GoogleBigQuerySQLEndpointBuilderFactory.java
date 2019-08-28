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
comment|/**  * Google BigQuery data warehouse for analytics (using SQL queries).  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|GoogleBigQuerySQLEndpointBuilderFactory
specifier|public
interface|interface
name|GoogleBigQuerySQLEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the Google BigQuery Standard SQL component.      */
DECL|interface|GoogleBigQuerySQLEndpointBuilder
specifier|public
interface|interface
name|GoogleBigQuerySQLEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedGoogleBigQuerySQLEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedGoogleBigQuerySQLEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * ConnectionFactory to obtain connection to Bigquery Service. If non          * provided the default will be used.          *           * The option is a:          *<code>org.apache.camel.component.google.bigquery.GoogleBigQueryConnectionFactory</code> type.          *           * Group: producer          */
DECL|method|connectionFactory ( Object connectionFactory)
specifier|default
name|GoogleBigQuerySQLEndpointBuilder
name|connectionFactory
parameter_list|(
name|Object
name|connectionFactory
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"connectionFactory"
argument_list|,
name|connectionFactory
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * ConnectionFactory to obtain connection to Bigquery Service. If non          * provided the default will be used.          *           * The option will be converted to a          *<code>org.apache.camel.component.google.bigquery.GoogleBigQueryConnectionFactory</code> type.          *           * Group: producer          */
DECL|method|connectionFactory ( String connectionFactory)
specifier|default
name|GoogleBigQuerySQLEndpointBuilder
name|connectionFactory
parameter_list|(
name|String
name|connectionFactory
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"connectionFactory"
argument_list|,
name|connectionFactory
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Google BigQuery Standard SQL      * component.      */
DECL|interface|AdvancedGoogleBigQuerySQLEndpointBuilder
specifier|public
interface|interface
name|AdvancedGoogleBigQuerySQLEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|GoogleBigQuerySQLEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|GoogleBigQuerySQLEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedGoogleBigQuerySQLEndpointBuilder
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
name|AdvancedGoogleBigQuerySQLEndpointBuilder
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
DECL|method|synchronous ( boolean synchronous)
specifier|default
name|AdvancedGoogleBigQuerySQLEndpointBuilder
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
DECL|method|synchronous ( String synchronous)
specifier|default
name|AdvancedGoogleBigQuerySQLEndpointBuilder
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
comment|/**      * Google BigQuery Standard SQL (camel-google-bigquery)      * Google BigQuery data warehouse for analytics (using SQL queries).      *       * Category: cloud,messaging      * Available as of version: 2.23      * Maven coordinates: org.apache.camel:camel-google-bigquery      *       * Syntax:<code>google-bigquery-sql:projectId:query</code>      *       * Path parameter: query (required)      * BigQuery standard SQL query      *       * Path parameter: projectId (required)      * Google Cloud Project Id      */
DECL|method|googleBigQuerySQL (String path)
specifier|default
name|GoogleBigQuerySQLEndpointBuilder
name|googleBigQuerySQL
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|GoogleBigQuerySQLEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|GoogleBigQuerySQLEndpointBuilder
implements|,
name|AdvancedGoogleBigQuerySQLEndpointBuilder
block|{
specifier|public
name|GoogleBigQuerySQLEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"google-bigquery-sql"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|GoogleBigQuerySQLEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

