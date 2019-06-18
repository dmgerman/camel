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
comment|/**  * The influxdb component allows you to interact with InfluxDB, a time series  * database.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|InfluxDbEndpointBuilderFactory
specifier|public
interface|interface
name|InfluxDbEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the InfluxDB component.      */
DECL|interface|InfluxDbEndpointBuilder
specifier|public
interface|interface
name|InfluxDbEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedInfluxDbEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedInfluxDbEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Connection to the influx database, of class InfluxDB.class.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|connectionBean (String connectionBean)
specifier|default
name|InfluxDbEndpointBuilder
name|connectionBean
parameter_list|(
name|String
name|connectionBean
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"connectionBean"
argument_list|,
name|connectionBean
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Define if this operation is a batch operation or not.          * The option is a<code>boolean</code> type.          * @group producer          */
DECL|method|batch (boolean batch)
specifier|default
name|InfluxDbEndpointBuilder
name|batch
parameter_list|(
name|boolean
name|batch
parameter_list|)
block|{
name|setProperty
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
comment|/**          * Define if this operation is a batch operation or not.          * The option will be converted to a<code>boolean</code> type.          * @group producer          */
DECL|method|batch (String batch)
specifier|default
name|InfluxDbEndpointBuilder
name|batch
parameter_list|(
name|String
name|batch
parameter_list|)
block|{
name|setProperty
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
comment|/**          * The name of the database where the time series will be stored.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|databaseName (String databaseName)
specifier|default
name|InfluxDbEndpointBuilder
name|databaseName
parameter_list|(
name|String
name|databaseName
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"databaseName"
argument_list|,
name|databaseName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Define if this operation is an insert or a query.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|operation (String operation)
specifier|default
name|InfluxDbEndpointBuilder
name|operation
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"operation"
argument_list|,
name|operation
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Define the query in case of operation query.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|query (String query)
specifier|default
name|InfluxDbEndpointBuilder
name|query
parameter_list|(
name|String
name|query
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"query"
argument_list|,
name|query
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The string that defines the retention policy to the data created by          * the endpoint.          * The option is a<code>java.lang.String</code> type.          * @group producer          */
DECL|method|retentionPolicy (String retentionPolicy)
specifier|default
name|InfluxDbEndpointBuilder
name|retentionPolicy
parameter_list|(
name|String
name|retentionPolicy
parameter_list|)
block|{
name|setProperty
argument_list|(
literal|"retentionPolicy"
argument_list|,
name|retentionPolicy
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the InfluxDB component.      */
DECL|interface|AdvancedInfluxDbEndpointBuilder
specifier|public
interface|interface
name|AdvancedInfluxDbEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|InfluxDbEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|InfluxDbEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedInfluxDbEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|boolean
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
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
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|basicPropertyBinding ( String basicPropertyBinding)
specifier|default
name|AdvancedInfluxDbEndpointBuilder
name|basicPropertyBinding
parameter_list|(
name|String
name|basicPropertyBinding
parameter_list|)
block|{
name|setProperty
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
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          * The option is a<code>boolean</code> type.          * @group advanced          */
DECL|method|synchronous (boolean synchronous)
specifier|default
name|AdvancedInfluxDbEndpointBuilder
name|synchronous
parameter_list|(
name|boolean
name|synchronous
parameter_list|)
block|{
name|setProperty
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
comment|/**          * Sets whether synchronous processing should be strictly used, or Camel          * is allowed to use asynchronous processing (if supported).          * The option will be converted to a<code>boolean</code> type.          * @group advanced          */
DECL|method|synchronous (String synchronous)
specifier|default
name|AdvancedInfluxDbEndpointBuilder
name|synchronous
parameter_list|(
name|String
name|synchronous
parameter_list|)
block|{
name|setProperty
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
comment|/**      * The influxdb component allows you to interact with InfluxDB, a time      * series database.      * Maven coordinates: org.apache.camel:camel-influxdb      */
DECL|method|influxDb (String path)
specifier|default
name|InfluxDbEndpointBuilder
name|influxDb
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|InfluxDbEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|InfluxDbEndpointBuilder
implements|,
name|AdvancedInfluxDbEndpointBuilder
block|{
specifier|public
name|InfluxDbEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"influxdb"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|InfluxDbEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

