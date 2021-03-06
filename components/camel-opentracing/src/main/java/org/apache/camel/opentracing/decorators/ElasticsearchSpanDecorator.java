begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.opentracing.decorators
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|opentracing
operator|.
name|decorators
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
name|io
operator|.
name|opentracing
operator|.
name|Span
import|;
end_import

begin_import
import|import
name|io
operator|.
name|opentracing
operator|.
name|tag
operator|.
name|Tags
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
name|Exchange
import|;
end_import

begin_class
DECL|class|ElasticsearchSpanDecorator
specifier|public
class|class
name|ElasticsearchSpanDecorator
extends|extends
name|AbstractSpanDecorator
block|{
DECL|field|ELASTICSEARCH_DB_TYPE
specifier|public
specifier|static
specifier|final
name|String
name|ELASTICSEARCH_DB_TYPE
init|=
literal|"elasticsearch"
decl_stmt|;
DECL|field|ELASTICSEARCH_CLUSTER_TAG
specifier|public
specifier|static
specifier|final
name|String
name|ELASTICSEARCH_CLUSTER_TAG
init|=
literal|"elasticsearch.cluster"
decl_stmt|;
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|String
name|getComponent
parameter_list|()
block|{
return|return
literal|"elasticsearch-rest"
return|;
block|}
annotation|@
name|Override
DECL|method|getComponentClassName ()
specifier|public
name|String
name|getComponentClassName
parameter_list|()
block|{
return|return
literal|"org.apache.camel.component.elasticsearch.ElasticsearchComponent"
return|;
block|}
annotation|@
name|Override
DECL|method|getOperationName (Exchange exchange, Endpoint endpoint)
specifier|public
name|String
name|getOperationName
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|queryParameters
init|=
name|toQueryParameters
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|queryParameters
operator|.
name|containsKey
argument_list|(
literal|"operation"
argument_list|)
condition|?
name|queryParameters
operator|.
name|get
argument_list|(
literal|"operation"
argument_list|)
else|:
name|super
operator|.
name|getOperationName
argument_list|(
name|exchange
argument_list|,
name|endpoint
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|pre (Span span, Exchange exchange, Endpoint endpoint)
specifier|public
name|void
name|pre
parameter_list|(
name|Span
name|span
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|super
operator|.
name|pre
argument_list|(
name|span
argument_list|,
name|exchange
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|span
operator|.
name|setTag
argument_list|(
name|Tags
operator|.
name|DB_TYPE
operator|.
name|getKey
argument_list|()
argument_list|,
name|ELASTICSEARCH_DB_TYPE
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|queryParameters
init|=
name|toQueryParameters
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|queryParameters
operator|.
name|containsKey
argument_list|(
literal|"indexName"
argument_list|)
condition|)
block|{
name|span
operator|.
name|setTag
argument_list|(
name|Tags
operator|.
name|DB_INSTANCE
operator|.
name|getKey
argument_list|()
argument_list|,
name|queryParameters
operator|.
name|get
argument_list|(
literal|"indexName"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|String
name|cluster
init|=
name|stripSchemeAndOptions
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
if|if
condition|(
name|cluster
operator|!=
literal|null
condition|)
block|{
name|span
operator|.
name|setTag
argument_list|(
name|ELASTICSEARCH_CLUSTER_TAG
argument_list|,
name|cluster
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

