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
name|propagation
operator|.
name|TextMap
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|opentracing
operator|.
name|SpanDecorator
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
name|opentracing
operator|.
name|propagation
operator|.
name|CamelHeadersExtractAdapter
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
name|opentracing
operator|.
name|propagation
operator|.
name|CamelHeadersInjectAdapter
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
name|StringHelper
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
name|URISupport
import|;
end_import

begin_comment
comment|/**  * An abstract base implementation of the {@link SpanDecorator} interface.  */
end_comment

begin_class
DECL|class|AbstractSpanDecorator
specifier|public
specifier|abstract
class|class
name|AbstractSpanDecorator
implements|implements
name|SpanDecorator
block|{
annotation|@
name|Override
DECL|method|newSpan ()
specifier|public
name|boolean
name|newSpan
parameter_list|()
block|{
return|return
literal|true
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
comment|// OpenTracing aims to use low cardinality operation names. Ideally a
comment|// specific
comment|// span decorator should be defined for all relevant Camel components
comment|// that
comment|// identify a meaningful operation name
return|return
name|getComponentName
argument_list|(
name|endpoint
argument_list|)
return|;
block|}
comment|/**      * This method removes the scheme, any leading slash characters and options      * from the supplied URI. This is intended to extract a meaningful name from      * the URI that can be used in situations, such as the operation name.      *      * @param endpoint The endpoint      * @return The stripped value from the URI      */
DECL|method|stripSchemeAndOptions (Endpoint endpoint)
specifier|public
specifier|static
name|String
name|stripSchemeAndOptions
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|int
name|start
init|=
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|':'
argument_list|)
decl_stmt|;
name|start
operator|++
expr_stmt|;
comment|// Remove any leading '/'
while|while
condition|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
operator|.
name|charAt
argument_list|(
name|start
argument_list|)
operator|==
literal|'/'
condition|)
block|{
name|start
operator|++
expr_stmt|;
block|}
name|int
name|end
init|=
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|'?'
argument_list|)
decl_stmt|;
return|return
name|end
operator|==
operator|-
literal|1
condition|?
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
operator|.
name|substring
argument_list|(
name|start
argument_list|)
else|:
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
operator|.
name|substring
argument_list|(
name|start
argument_list|,
name|end
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
name|String
name|scheme
init|=
name|getComponentName
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|span
operator|.
name|setTag
argument_list|(
name|Tags
operator|.
name|COMPONENT
operator|.
name|getKey
argument_list|()
argument_list|,
name|CAMEL_COMPONENT
operator|+
name|scheme
argument_list|)
expr_stmt|;
comment|// Including the endpoint URI provides access to any options that may
comment|// have been provided, for
comment|// subsequent analysis
name|span
operator|.
name|setTag
argument_list|(
literal|"camel.uri"
argument_list|,
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|post (Span span, Exchange exchange, Endpoint endpoint)
specifier|public
name|void
name|post
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
if|if
condition|(
name|exchange
operator|.
name|isFailed
argument_list|()
condition|)
block|{
name|span
operator|.
name|setTag
argument_list|(
name|Tags
operator|.
name|ERROR
operator|.
name|getKey
argument_list|()
argument_list|,
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getException
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|logEvent
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|logEvent
operator|.
name|put
argument_list|(
literal|"event"
argument_list|,
literal|"error"
argument_list|)
expr_stmt|;
name|logEvent
operator|.
name|put
argument_list|(
literal|"error.kind"
argument_list|,
literal|"Exception"
argument_list|)
expr_stmt|;
name|logEvent
operator|.
name|put
argument_list|(
literal|"message"
argument_list|,
name|exchange
operator|.
name|getException
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|span
operator|.
name|log
argument_list|(
name|logEvent
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|getInitiatorSpanKind ()
specifier|public
name|String
name|getInitiatorSpanKind
parameter_list|()
block|{
return|return
name|Tags
operator|.
name|SPAN_KIND_CLIENT
return|;
block|}
annotation|@
name|Override
DECL|method|getReceiverSpanKind ()
specifier|public
name|String
name|getReceiverSpanKind
parameter_list|()
block|{
return|return
name|Tags
operator|.
name|SPAN_KIND_SERVER
return|;
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
DECL|method|getComponentName (Endpoint endpoint)
specifier|private
specifier|static
name|String
name|getComponentName
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|String
index|[]
name|splitURI
init|=
name|StringHelper
operator|.
name|splitOnCharacter
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|,
literal|":"
argument_list|,
literal|2
argument_list|)
decl_stmt|;
if|if
condition|(
name|splitURI
operator|.
name|length
operator|>
literal|0
condition|)
block|{
return|return
name|splitURI
index|[
literal|0
index|]
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|getExtractAdapter (final Map<String, Object> map, boolean encoding)
specifier|public
name|TextMap
name|getExtractAdapter
parameter_list|(
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|,
name|boolean
name|encoding
parameter_list|)
block|{
comment|// no encoding supported per default
return|return
operator|new
name|CamelHeadersExtractAdapter
argument_list|(
name|map
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|getInjectAdapter (final Map<String, Object> map, boolean encoding)
specifier|public
name|TextMap
name|getInjectAdapter
parameter_list|(
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|map
parameter_list|,
name|boolean
name|encoding
parameter_list|)
block|{
comment|// no encoding supported per default
return|return
operator|new
name|CamelHeadersInjectAdapter
argument_list|(
name|map
argument_list|)
return|;
block|}
block|}
end_class

end_unit

