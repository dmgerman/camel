begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Collections
import|;
end_import

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
DECL|class|MongoDBSpanDecorator
specifier|public
class|class
name|MongoDBSpanDecorator
extends|extends
name|AbstractSpanDecorator
block|{
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|String
name|getComponent
parameter_list|()
block|{
return|return
literal|"mongodb"
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
name|String
name|opName
init|=
name|queryParameters
operator|.
name|get
argument_list|(
literal|"operation"
argument_list|)
decl_stmt|;
if|if
condition|(
name|opName
operator|!=
literal|null
condition|)
block|{
return|return
name|opName
return|;
block|}
return|return
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
name|getComponent
argument_list|()
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
name|String
name|database
init|=
name|queryParameters
operator|.
name|get
argument_list|(
literal|"database"
argument_list|)
decl_stmt|;
if|if
condition|(
name|database
operator|!=
literal|null
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
name|database
argument_list|)
expr_stmt|;
block|}
name|span
operator|.
name|setTag
argument_list|(
name|Tags
operator|.
name|DB_STATEMENT
operator|.
name|getKey
argument_list|()
argument_list|,
name|queryParameters
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|toQueryParameters (String uri)
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|toQueryParameters
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|int
name|index
init|=
name|uri
operator|.
name|indexOf
argument_list|(
literal|'?'
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|!=
operator|-
literal|1
condition|)
block|{
name|String
name|queryString
init|=
name|uri
operator|.
name|substring
argument_list|(
name|index
operator|+
literal|1
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|param
range|:
name|queryString
operator|.
name|split
argument_list|(
literal|"&"
argument_list|)
control|)
block|{
name|String
index|[]
name|parts
init|=
name|param
operator|.
name|split
argument_list|(
literal|"="
argument_list|)
decl_stmt|;
if|if
condition|(
name|parts
operator|.
name|length
operator|==
literal|2
condition|)
block|{
name|map
operator|.
name|put
argument_list|(
name|parts
index|[
literal|0
index|]
argument_list|,
name|parts
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|map
return|;
block|}
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
block|}
end_class

end_unit

