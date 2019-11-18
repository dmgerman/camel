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
comment|/**  * The elasticsearch component is used for interfacing with ElasticSearch server  * using REST API.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|ElasticsearchEndpointBuilderFactory
specifier|public
interface|interface
name|ElasticsearchEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the Elastichsearch Rest component.      */
DECL|interface|ElasticsearchEndpointBuilder
specifier|public
interface|interface
name|ElasticsearchEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedElasticsearchEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedElasticsearchEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * The time in ms to wait before connection will timeout.          *           * The option is a:<code>int</code> type.          *           * Group: producer          */
DECL|method|connectionTimeout ( int connectionTimeout)
specifier|default
name|ElasticsearchEndpointBuilder
name|connectionTimeout
parameter_list|(
name|int
name|connectionTimeout
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"connectionTimeout"
argument_list|,
name|connectionTimeout
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The time in ms to wait before connection will timeout.          *           * The option will be converted to a<code>int</code> type.          *           * Group: producer          */
DECL|method|connectionTimeout ( String connectionTimeout)
specifier|default
name|ElasticsearchEndpointBuilder
name|connectionTimeout
parameter_list|(
name|String
name|connectionTimeout
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"connectionTimeout"
argument_list|,
name|connectionTimeout
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Disconnect after it finish calling the producer.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|disconnect (boolean disconnect)
specifier|default
name|ElasticsearchEndpointBuilder
name|disconnect
parameter_list|(
name|boolean
name|disconnect
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"disconnect"
argument_list|,
name|disconnect
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Disconnect after it finish calling the producer.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|disconnect (String disconnect)
specifier|default
name|ElasticsearchEndpointBuilder
name|disconnect
parameter_list|(
name|String
name|disconnect
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"disconnect"
argument_list|,
name|disconnect
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Enable SSL.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|enableSSL (boolean enableSSL)
specifier|default
name|ElasticsearchEndpointBuilder
name|enableSSL
parameter_list|(
name|boolean
name|enableSSL
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"enableSSL"
argument_list|,
name|enableSSL
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Enable SSL.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|enableSSL (String enableSSL)
specifier|default
name|ElasticsearchEndpointBuilder
name|enableSSL
parameter_list|(
name|String
name|enableSSL
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"enableSSL"
argument_list|,
name|enableSSL
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Comma separated list with ip:port formatted remote transport          * addresses to use.          *           * The option is a:<code>java.lang.String</code> type.          *           * Required: true          * Group: producer          */
DECL|method|hostAddresses (String hostAddresses)
specifier|default
name|ElasticsearchEndpointBuilder
name|hostAddresses
parameter_list|(
name|String
name|hostAddresses
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"hostAddresses"
argument_list|,
name|hostAddresses
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The name of the index to act against.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|indexName (String indexName)
specifier|default
name|ElasticsearchEndpointBuilder
name|indexName
parameter_list|(
name|String
name|indexName
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"indexName"
argument_list|,
name|indexName
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|lazyStartProducer ( boolean lazyStartProducer)
specifier|default
name|ElasticsearchEndpointBuilder
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
name|ElasticsearchEndpointBuilder
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
comment|/**          * The time in ms before retry.          *           * The option is a:<code>int</code> type.          *           * Group: producer          */
DECL|method|maxRetryTimeout (int maxRetryTimeout)
specifier|default
name|ElasticsearchEndpointBuilder
name|maxRetryTimeout
parameter_list|(
name|int
name|maxRetryTimeout
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"maxRetryTimeout"
argument_list|,
name|maxRetryTimeout
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The time in ms before retry.          *           * The option will be converted to a<code>int</code> type.          *           * Group: producer          */
DECL|method|maxRetryTimeout ( String maxRetryTimeout)
specifier|default
name|ElasticsearchEndpointBuilder
name|maxRetryTimeout
parameter_list|(
name|String
name|maxRetryTimeout
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"maxRetryTimeout"
argument_list|,
name|maxRetryTimeout
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * What operation to perform.          *           * The option is a:          *<code>org.apache.camel.component.elasticsearch.ElasticsearchOperation</code> type.          *           * Group: producer          */
DECL|method|operation ( ElasticsearchOperation operation)
specifier|default
name|ElasticsearchEndpointBuilder
name|operation
parameter_list|(
name|ElasticsearchOperation
name|operation
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * What operation to perform.          *           * The option will be converted to a          *<code>org.apache.camel.component.elasticsearch.ElasticsearchOperation</code> type.          *           * Group: producer          */
DECL|method|operation (String operation)
specifier|default
name|ElasticsearchEndpointBuilder
name|operation
parameter_list|(
name|String
name|operation
parameter_list|)
block|{
name|doSetProperty
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
comment|/**          * Time in ms during which elasticsearch will keep search context alive.          *           * The option is a:<code>int</code> type.          *           * Group: producer          */
DECL|method|scrollKeepAliveMs ( int scrollKeepAliveMs)
specifier|default
name|ElasticsearchEndpointBuilder
name|scrollKeepAliveMs
parameter_list|(
name|int
name|scrollKeepAliveMs
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"scrollKeepAliveMs"
argument_list|,
name|scrollKeepAliveMs
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Time in ms during which elasticsearch will keep search context alive.          *           * The option will be converted to a<code>int</code> type.          *           * Group: producer          */
DECL|method|scrollKeepAliveMs ( String scrollKeepAliveMs)
specifier|default
name|ElasticsearchEndpointBuilder
name|scrollKeepAliveMs
parameter_list|(
name|String
name|scrollKeepAliveMs
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"scrollKeepAliveMs"
argument_list|,
name|scrollKeepAliveMs
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The timeout in ms to wait before the socket will timeout.          *           * The option is a:<code>int</code> type.          *           * Group: producer          */
DECL|method|socketTimeout (int socketTimeout)
specifier|default
name|ElasticsearchEndpointBuilder
name|socketTimeout
parameter_list|(
name|int
name|socketTimeout
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"socketTimeout"
argument_list|,
name|socketTimeout
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * The timeout in ms to wait before the socket will timeout.          *           * The option will be converted to a<code>int</code> type.          *           * Group: producer          */
DECL|method|socketTimeout (String socketTimeout)
specifier|default
name|ElasticsearchEndpointBuilder
name|socketTimeout
parameter_list|(
name|String
name|socketTimeout
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"socketTimeout"
argument_list|,
name|socketTimeout
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Enable scroll usage.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|useScroll (boolean useScroll)
specifier|default
name|ElasticsearchEndpointBuilder
name|useScroll
parameter_list|(
name|boolean
name|useScroll
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"useScroll"
argument_list|,
name|useScroll
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Enable scroll usage.          *           * The option will be converted to a<code>boolean</code> type.          *           * Group: producer          */
DECL|method|useScroll (String useScroll)
specifier|default
name|ElasticsearchEndpointBuilder
name|useScroll
parameter_list|(
name|String
name|useScroll
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"useScroll"
argument_list|,
name|useScroll
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Index creation waits for the write consistency number of shards to be          * available.          *           * The option is a:<code>int</code> type.          *           * Group: producer          */
DECL|method|waitForActiveShards ( int waitForActiveShards)
specifier|default
name|ElasticsearchEndpointBuilder
name|waitForActiveShards
parameter_list|(
name|int
name|waitForActiveShards
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"waitForActiveShards"
argument_list|,
name|waitForActiveShards
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Index creation waits for the write consistency number of shards to be          * available.          *           * The option will be converted to a<code>int</code> type.          *           * Group: producer          */
DECL|method|waitForActiveShards ( String waitForActiveShards)
specifier|default
name|ElasticsearchEndpointBuilder
name|waitForActiveShards
parameter_list|(
name|String
name|waitForActiveShards
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"waitForActiveShards"
argument_list|,
name|waitForActiveShards
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Elastichsearch Rest component.      */
DECL|interface|AdvancedElasticsearchEndpointBuilder
specifier|public
interface|interface
name|AdvancedElasticsearchEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|ElasticsearchEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|ElasticsearchEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedElasticsearchEndpointBuilder
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
name|AdvancedElasticsearchEndpointBuilder
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
name|AdvancedElasticsearchEndpointBuilder
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
name|AdvancedElasticsearchEndpointBuilder
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
comment|/**      * Proxy enum for      *<code>org.apache.camel.component.elasticsearch.ElasticsearchOperation</code> enum.      */
DECL|enum|ElasticsearchOperation
enum|enum
name|ElasticsearchOperation
block|{
DECL|enumConstant|Index
name|Index
block|,
DECL|enumConstant|Update
name|Update
block|,
DECL|enumConstant|Bulk
name|Bulk
block|,
DECL|enumConstant|BulkIndex
name|BulkIndex
block|,
DECL|enumConstant|GetById
name|GetById
block|,
DECL|enumConstant|MultiGet
name|MultiGet
block|,
DECL|enumConstant|MultiSearch
name|MultiSearch
block|,
DECL|enumConstant|Delete
name|Delete
block|,
DECL|enumConstant|DeleteIndex
name|DeleteIndex
block|,
DECL|enumConstant|Search
name|Search
block|,
DECL|enumConstant|Exists
name|Exists
block|,
DECL|enumConstant|Ping
name|Ping
block|;     }
comment|/**      * Elastichsearch Rest (camel-elasticsearch-rest)      * The elasticsearch component is used for interfacing with ElasticSearch      * server using REST API.      *       * Category: monitoring,search      * Since: 2.21      * Maven coordinates: org.apache.camel:camel-elasticsearch-rest      *       * Syntax:<code>elasticsearch-rest:clusterName</code>      *       * Path parameter: clusterName (required)      * Name of the cluster      */
DECL|method|elasticsearchRest (String path)
specifier|default
name|ElasticsearchEndpointBuilder
name|elasticsearchRest
parameter_list|(
name|String
name|path
parameter_list|)
block|{
class|class
name|ElasticsearchEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|ElasticsearchEndpointBuilder
implements|,
name|AdvancedElasticsearchEndpointBuilder
block|{
specifier|public
name|ElasticsearchEndpointBuilderImpl
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
literal|"elasticsearch-rest"
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|ElasticsearchEndpointBuilderImpl
argument_list|(
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

