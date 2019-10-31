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
comment|/**  * The solr component allows you to interface with an Apache Lucene Solr server.  *   * Generated by camel-package-maven-plugin - do not edit this file!  */
end_comment

begin_interface
annotation|@
name|Generated
argument_list|(
literal|"org.apache.camel.maven.packaging.EndpointDslMojo"
argument_list|)
DECL|interface|SolrEndpointBuilderFactory
specifier|public
interface|interface
name|SolrEndpointBuilderFactory
block|{
comment|/**      * Builder for endpoint for the Solr component.      */
DECL|interface|SolrEndpointBuilder
specifier|public
interface|interface
name|SolrEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|advanced ()
specifier|default
name|AdvancedSolrEndpointBuilder
name|advanced
parameter_list|()
block|{
return|return
operator|(
name|AdvancedSolrEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Server side must support gzip or deflate for this to have any effect.          *           * The option is a:<code>java.lang.Boolean</code> type.          *           * Group: producer          */
DECL|method|allowCompression (Boolean allowCompression)
specifier|default
name|SolrEndpointBuilder
name|allowCompression
parameter_list|(
name|Boolean
name|allowCompression
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"allowCompression"
argument_list|,
name|allowCompression
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Server side must support gzip or deflate for this to have any effect.          *           * The option will be converted to a<code>java.lang.Boolean</code>          * type.          *           * Group: producer          */
DECL|method|allowCompression (String allowCompression)
specifier|default
name|SolrEndpointBuilder
name|allowCompression
parameter_list|(
name|String
name|allowCompression
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"allowCompression"
argument_list|,
name|allowCompression
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * connectionTimeout on the underlying HttpConnectionManager.          *           * The option is a:<code>java.lang.Integer</code> type.          *           * Group: producer          */
DECL|method|connectionTimeout (Integer connectionTimeout)
specifier|default
name|SolrEndpointBuilder
name|connectionTimeout
parameter_list|(
name|Integer
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
comment|/**          * connectionTimeout on the underlying HttpConnectionManager.          *           * The option will be converted to a<code>java.lang.Integer</code>          * type.          *           * Group: producer          */
DECL|method|connectionTimeout (String connectionTimeout)
specifier|default
name|SolrEndpointBuilder
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
comment|/**          * maxConnectionsPerHost on the underlying HttpConnectionManager.          *           * The option is a:<code>java.lang.Integer</code> type.          *           * Group: producer          */
DECL|method|defaultMaxConnectionsPerHost ( Integer defaultMaxConnectionsPerHost)
specifier|default
name|SolrEndpointBuilder
name|defaultMaxConnectionsPerHost
parameter_list|(
name|Integer
name|defaultMaxConnectionsPerHost
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"defaultMaxConnectionsPerHost"
argument_list|,
name|defaultMaxConnectionsPerHost
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * maxConnectionsPerHost on the underlying HttpConnectionManager.          *           * The option will be converted to a<code>java.lang.Integer</code>          * type.          *           * Group: producer          */
DECL|method|defaultMaxConnectionsPerHost ( String defaultMaxConnectionsPerHost)
specifier|default
name|SolrEndpointBuilder
name|defaultMaxConnectionsPerHost
parameter_list|(
name|String
name|defaultMaxConnectionsPerHost
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"defaultMaxConnectionsPerHost"
argument_list|,
name|defaultMaxConnectionsPerHost
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * indicates whether redirects are used to get to the Solr server.          *           * The option is a:<code>java.lang.Boolean</code> type.          *           * Group: producer          */
DECL|method|followRedirects (Boolean followRedirects)
specifier|default
name|SolrEndpointBuilder
name|followRedirects
parameter_list|(
name|Boolean
name|followRedirects
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"followRedirects"
argument_list|,
name|followRedirects
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * indicates whether redirects are used to get to the Solr server.          *           * The option will be converted to a<code>java.lang.Boolean</code>          * type.          *           * Group: producer          */
DECL|method|followRedirects (String followRedirects)
specifier|default
name|SolrEndpointBuilder
name|followRedirects
parameter_list|(
name|String
name|followRedirects
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"followRedirects"
argument_list|,
name|followRedirects
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Whether the producer should be started lazy (on the first message).          * By starting lazy you can use this to allow CamelContext and routes to          * startup in situations where a producer may otherwise fail during          * starting and cause the route to fail being started. By deferring this          * startup to be lazy then the startup failure can be handled during          * routing messages via Camel's routing error handlers. Beware that when          * the first message is processed then creating and starting the          * producer may take a little time and prolong the total processing time          * of the processing.          *           * The option is a:<code>boolean</code> type.          *           * Group: producer          */
DECL|method|lazyStartProducer (boolean lazyStartProducer)
specifier|default
name|SolrEndpointBuilder
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
DECL|method|lazyStartProducer (String lazyStartProducer)
specifier|default
name|SolrEndpointBuilder
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
comment|/**          * Maximum number of retries to attempt in the event of transient          * errors.          *           * The option is a:<code>java.lang.Integer</code> type.          *           * Group: producer          */
DECL|method|maxRetries (Integer maxRetries)
specifier|default
name|SolrEndpointBuilder
name|maxRetries
parameter_list|(
name|Integer
name|maxRetries
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"maxRetries"
argument_list|,
name|maxRetries
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Maximum number of retries to attempt in the event of transient          * errors.          *           * The option will be converted to a<code>java.lang.Integer</code>          * type.          *           * Group: producer          */
DECL|method|maxRetries (String maxRetries)
specifier|default
name|SolrEndpointBuilder
name|maxRetries
parameter_list|(
name|String
name|maxRetries
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"maxRetries"
argument_list|,
name|maxRetries
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * maxTotalConnection on the underlying HttpConnectionManager.          *           * The option is a:<code>java.lang.Integer</code> type.          *           * Group: producer          */
DECL|method|maxTotalConnections ( Integer maxTotalConnections)
specifier|default
name|SolrEndpointBuilder
name|maxTotalConnections
parameter_list|(
name|Integer
name|maxTotalConnections
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"maxTotalConnections"
argument_list|,
name|maxTotalConnections
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * maxTotalConnection on the underlying HttpConnectionManager.          *           * The option will be converted to a<code>java.lang.Integer</code>          * type.          *           * Group: producer          */
DECL|method|maxTotalConnections ( String maxTotalConnections)
specifier|default
name|SolrEndpointBuilder
name|maxTotalConnections
parameter_list|(
name|String
name|maxTotalConnections
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"maxTotalConnections"
argument_list|,
name|maxTotalConnections
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Set the request handler to be used.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: producer          */
DECL|method|requestHandler (String requestHandler)
specifier|default
name|SolrEndpointBuilder
name|requestHandler
parameter_list|(
name|String
name|requestHandler
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"requestHandler"
argument_list|,
name|requestHandler
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Read timeout on the underlying HttpConnectionManager. This is          * desirable for queries, but probably not for indexing.          *           * The option is a:<code>java.lang.Integer</code> type.          *           * Group: producer          */
DECL|method|soTimeout (Integer soTimeout)
specifier|default
name|SolrEndpointBuilder
name|soTimeout
parameter_list|(
name|Integer
name|soTimeout
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"soTimeout"
argument_list|,
name|soTimeout
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Read timeout on the underlying HttpConnectionManager. This is          * desirable for queries, but probably not for indexing.          *           * The option will be converted to a<code>java.lang.Integer</code>          * type.          *           * Group: producer          */
DECL|method|soTimeout (String soTimeout)
specifier|default
name|SolrEndpointBuilder
name|soTimeout
parameter_list|(
name|String
name|soTimeout
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"soTimeout"
argument_list|,
name|soTimeout
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Set the queue size for the StreamingUpdateSolrServer.          *           * The option is a:<code>int</code> type.          *           * Group: producer          */
DECL|method|streamingQueueSize (int streamingQueueSize)
specifier|default
name|SolrEndpointBuilder
name|streamingQueueSize
parameter_list|(
name|int
name|streamingQueueSize
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"streamingQueueSize"
argument_list|,
name|streamingQueueSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Set the queue size for the StreamingUpdateSolrServer.          *           * The option will be converted to a<code>int</code> type.          *           * Group: producer          */
DECL|method|streamingQueueSize (String streamingQueueSize)
specifier|default
name|SolrEndpointBuilder
name|streamingQueueSize
parameter_list|(
name|String
name|streamingQueueSize
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"streamingQueueSize"
argument_list|,
name|streamingQueueSize
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Set the number of threads for the StreamingUpdateSolrServer.          *           * The option is a:<code>int</code> type.          *           * Group: producer          */
DECL|method|streamingThreadCount ( int streamingThreadCount)
specifier|default
name|SolrEndpointBuilder
name|streamingThreadCount
parameter_list|(
name|int
name|streamingThreadCount
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"streamingThreadCount"
argument_list|,
name|streamingThreadCount
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Set the number of threads for the StreamingUpdateSolrServer.          *           * The option will be converted to a<code>int</code> type.          *           * Group: producer          */
DECL|method|streamingThreadCount ( String streamingThreadCount)
specifier|default
name|SolrEndpointBuilder
name|streamingThreadCount
parameter_list|(
name|String
name|streamingThreadCount
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"streamingThreadCount"
argument_list|,
name|streamingThreadCount
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets password for basic auth plugin enabled servers.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: security          */
DECL|method|password (String password)
specifier|default
name|SolrEndpointBuilder
name|password
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"password"
argument_list|,
name|password
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Sets username for basic auth plugin enabled servers.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: security          */
DECL|method|username (String username)
specifier|default
name|SolrEndpointBuilder
name|username
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"username"
argument_list|,
name|username
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Set the collection name which the solrCloud server could use.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: solrCloud          */
DECL|method|collection (String collection)
specifier|default
name|SolrEndpointBuilder
name|collection
parameter_list|(
name|String
name|collection
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"collection"
argument_list|,
name|collection
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**          * Set the ZooKeeper host information which the solrCloud could use,          * such as zkhost=localhost:8123.          *           * The option is a:<code>java.lang.String</code> type.          *           * Group: solrCloud          */
DECL|method|zkHost (String zkHost)
specifier|default
name|SolrEndpointBuilder
name|zkHost
parameter_list|(
name|String
name|zkHost
parameter_list|)
block|{
name|doSetProperty
argument_list|(
literal|"zkHost"
argument_list|,
name|zkHost
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
comment|/**      * Advanced builder for endpoint for the Solr component.      */
DECL|interface|AdvancedSolrEndpointBuilder
specifier|public
interface|interface
name|AdvancedSolrEndpointBuilder
extends|extends
name|EndpointProducerBuilder
block|{
DECL|method|basic ()
specifier|default
name|SolrEndpointBuilder
name|basic
parameter_list|()
block|{
return|return
operator|(
name|SolrEndpointBuilder
operator|)
name|this
return|;
block|}
comment|/**          * Whether the endpoint should use basic property binding (Camel 2.x) or          * the newer property binding with additional capabilities.          *           * The option is a:<code>boolean</code> type.          *           * Group: advanced          */
DECL|method|basicPropertyBinding ( boolean basicPropertyBinding)
specifier|default
name|AdvancedSolrEndpointBuilder
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
name|AdvancedSolrEndpointBuilder
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
name|AdvancedSolrEndpointBuilder
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
name|AdvancedSolrEndpointBuilder
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
comment|/**      * Solr (camel-solr)      * The solr component allows you to interface with an Apache Lucene Solr      * server.      *       * Category: monitoring,search      * Available as of version: 2.9      * Maven coordinates: org.apache.camel:camel-solr      *       * Syntax:<code>solr:url</code>      *       * Path parameter: url (required)      * Hostname and port for the solr server      */
DECL|method|solr (String path)
specifier|default
name|SolrEndpointBuilder
name|solr
parameter_list|(
name|String
name|path
parameter_list|)
block|{
return|return
name|solr
argument_list|(
literal|"solr"
argument_list|,
name|path
argument_list|)
return|;
block|}
comment|/**      * Solr (camel-solr)      * The solr component allows you to interface with an Apache Lucene Solr      * server.      *       * Category: monitoring,search      * Available as of version: 2.9      * Maven coordinates: org.apache.camel:camel-solr      *       * Syntax:<code>solrCloud:url</code>      *       * Path parameter: url (required)      * Hostname and port for the solr server      */
DECL|method|solrCloud (String path)
specifier|default
name|SolrEndpointBuilder
name|solrCloud
parameter_list|(
name|String
name|path
parameter_list|)
block|{
return|return
name|solr
argument_list|(
literal|"solrCloud"
argument_list|,
name|path
argument_list|)
return|;
block|}
comment|/**      * Solr (Secure) (camel-solr)      * The solr component allows you to interface with an Apache Lucene Solr      * server.      *       * Category: monitoring,search      * Available as of version: 2.9      * Maven coordinates: org.apache.camel:camel-solr      *       * Syntax:<code>solrs:url</code>      *       * Path parameter: url (required)      * Hostname and port for the solr server      */
DECL|method|solrs (String path)
specifier|default
name|SolrEndpointBuilder
name|solrs
parameter_list|(
name|String
name|path
parameter_list|)
block|{
return|return
name|solr
argument_list|(
literal|"solrs"
argument_list|,
name|path
argument_list|)
return|;
block|}
comment|/**      * Solr (camel-solr)      * The solr component allows you to interface with an Apache Lucene Solr      * server.      *       * Category: monitoring,search      * Available as of version: 2.9      * Maven coordinates: org.apache.camel:camel-solr      */
DECL|method|solr (String scheme, String path)
specifier|default
name|SolrEndpointBuilder
name|solr
parameter_list|(
name|String
name|scheme
parameter_list|,
name|String
name|path
parameter_list|)
block|{
class|class
name|SolrEndpointBuilderImpl
extends|extends
name|AbstractEndpointBuilder
implements|implements
name|SolrEndpointBuilder
implements|,
name|AdvancedSolrEndpointBuilder
block|{
specifier|public
name|SolrEndpointBuilderImpl
parameter_list|(
name|String
name|scheme
parameter_list|,
name|String
name|path
parameter_list|)
block|{
name|super
argument_list|(
name|scheme
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
block|}
return|return
operator|new
name|SolrEndpointBuilderImpl
argument_list|(
name|scheme
argument_list|,
name|path
argument_list|)
return|;
block|}
block|}
end_interface

end_unit

