begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.zipkin
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|zipkin
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|kristofa
operator|.
name|brave
operator|.
name|ClientRequestAdapter
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|kristofa
operator|.
name|brave
operator|.
name|IdConversion
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|kristofa
operator|.
name|brave
operator|.
name|KeyValueAnnotation
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|kristofa
operator|.
name|brave
operator|.
name|SpanId
import|;
end_import

begin_import
import|import
name|com
operator|.
name|github
operator|.
name|kristofa
operator|.
name|brave
operator|.
name|internal
operator|.
name|Nullable
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
name|util
operator|.
name|MessageHelper
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

begin_class
DECL|class|ZipkinClientRequestAdapter
specifier|public
specifier|final
class|class
name|ZipkinClientRequestAdapter
implements|implements
name|ClientRequestAdapter
block|{
DECL|field|eventNotifier
specifier|private
specifier|final
name|ZipkinEventNotifier
name|eventNotifier
decl_stmt|;
DECL|field|serviceName
specifier|private
specifier|final
name|String
name|serviceName
decl_stmt|;
DECL|field|exchange
specifier|private
specifier|final
name|Exchange
name|exchange
decl_stmt|;
DECL|field|endpoint
specifier|private
specifier|final
name|Endpoint
name|endpoint
decl_stmt|;
DECL|field|spanName
specifier|private
specifier|final
name|String
name|spanName
decl_stmt|;
DECL|field|url
specifier|private
specifier|final
name|String
name|url
decl_stmt|;
DECL|method|ZipkinClientRequestAdapter (ZipkinEventNotifier eventNotifier, String serviceName, Exchange exchange, Endpoint endpoint)
specifier|public
name|ZipkinClientRequestAdapter
parameter_list|(
name|ZipkinEventNotifier
name|eventNotifier
parameter_list|,
name|String
name|serviceName
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|this
operator|.
name|eventNotifier
operator|=
name|eventNotifier
expr_stmt|;
name|this
operator|.
name|serviceName
operator|=
name|serviceName
expr_stmt|;
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|endpoint
expr_stmt|;
name|this
operator|.
name|spanName
operator|=
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|endpoint
operator|.
name|getEndpointKey
argument_list|()
argument_list|)
operator|.
name|toLowerCase
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
expr_stmt|;
name|this
operator|.
name|url
operator|=
name|URISupport
operator|.
name|sanitizeUri
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getSpanName ()
specifier|public
name|String
name|getSpanName
parameter_list|()
block|{
return|return
name|spanName
return|;
block|}
annotation|@
name|Override
DECL|method|addSpanIdToRequest (@ullable SpanId spanId)
specifier|public
name|void
name|addSpanIdToRequest
parameter_list|(
annotation|@
name|Nullable
name|SpanId
name|spanId
parameter_list|)
block|{
if|if
condition|(
name|spanId
operator|==
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|ZipkinConstants
operator|.
name|SAMPLED
argument_list|,
literal|"0"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|ZipkinConstants
operator|.
name|SAMPLED
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|ZipkinConstants
operator|.
name|TRACE_ID
argument_list|,
name|IdConversion
operator|.
name|convertToString
argument_list|(
name|spanId
operator|.
name|getTraceId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|ZipkinConstants
operator|.
name|SPAN_ID
argument_list|,
name|IdConversion
operator|.
name|convertToString
argument_list|(
name|spanId
operator|.
name|getSpanId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|spanId
operator|.
name|getParentSpanId
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|ZipkinConstants
operator|.
name|PARENT_SPAN_ID
argument_list|,
name|IdConversion
operator|.
name|convertToString
argument_list|(
name|spanId
operator|.
name|getParentSpanId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
DECL|method|getClientServiceName ()
specifier|public
name|String
name|getClientServiceName
parameter_list|()
block|{
return|return
name|serviceName
return|;
block|}
annotation|@
name|Override
DECL|method|requestAnnotations ()
specifier|public
name|Collection
argument_list|<
name|KeyValueAnnotation
argument_list|>
name|requestAnnotations
parameter_list|()
block|{
name|KeyValueAnnotation
name|key1
init|=
name|KeyValueAnnotation
operator|.
name|create
argument_list|(
literal|"camel.client.endpoint.url"
argument_list|,
name|url
argument_list|)
decl_stmt|;
name|KeyValueAnnotation
name|key2
init|=
name|KeyValueAnnotation
operator|.
name|create
argument_list|(
literal|"camel.client.exchange.id"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
decl_stmt|;
name|KeyValueAnnotation
name|key3
init|=
name|KeyValueAnnotation
operator|.
name|create
argument_list|(
literal|"camel.client.exchange.pattern"
argument_list|,
name|exchange
operator|.
name|getPattern
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
decl_stmt|;
name|KeyValueAnnotation
name|key4
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|eventNotifier
operator|.
name|isIncludeMessageBody
argument_list|()
condition|)
block|{
name|String
name|body
init|=
name|MessageHelper
operator|.
name|extractBodyForLogging
argument_list|(
name|exchange
operator|.
name|hasOut
argument_list|()
condition|?
name|exchange
operator|.
name|getOut
argument_list|()
else|:
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|key4
operator|=
name|KeyValueAnnotation
operator|.
name|create
argument_list|(
literal|"camel.client.exchange.message.request.body"
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
name|List
argument_list|<
name|KeyValueAnnotation
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
name|key1
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|key2
argument_list|)
expr_stmt|;
name|list
operator|.
name|add
argument_list|(
name|key3
argument_list|)
expr_stmt|;
if|if
condition|(
name|key4
operator|!=
literal|null
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|key4
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
block|}
end_class

end_unit

