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
name|ServerRequestAdapter
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
name|TraceData
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

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|zipkin
operator|.
name|ZipkinHelper
operator|.
name|createSpanId
import|;
end_import

begin_class
DECL|class|ZipkinServerRequestAdapter
specifier|public
class|class
name|ZipkinServerRequestAdapter
implements|implements
name|ServerRequestAdapter
block|{
DECL|field|eventNotifier
specifier|private
specifier|final
name|ZipkinTracer
name|eventNotifier
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
DECL|method|ZipkinServerRequestAdapter (ZipkinTracer eventNotifier, Exchange exchange)
specifier|public
name|ZipkinServerRequestAdapter
parameter_list|(
name|ZipkinTracer
name|eventNotifier
parameter_list|,
name|Exchange
name|exchange
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
name|exchange
operator|=
name|exchange
expr_stmt|;
name|this
operator|.
name|endpoint
operator|=
name|exchange
operator|.
name|getFromEndpoint
argument_list|()
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
DECL|method|getTraceData ()
specifier|public
name|TraceData
name|getTraceData
parameter_list|()
block|{
name|String
name|sampled
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ZipkinConstants
operator|.
name|SAMPLED
argument_list|,
literal|"0"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|sampled
operator|.
name|equals
argument_list|(
literal|"0"
argument_list|)
operator|||
name|sampled
operator|.
name|toLowerCase
argument_list|()
operator|.
name|equals
argument_list|(
literal|"false"
argument_list|)
condition|)
block|{
return|return
name|TraceData
operator|.
name|builder
argument_list|()
operator|.
name|sample
argument_list|(
literal|false
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
else|else
block|{
name|String
name|traceId
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ZipkinConstants
operator|.
name|TRACE_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|spanId
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ZipkinConstants
operator|.
name|SPAN_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|parentSpanId
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|ZipkinConstants
operator|.
name|PARENT_SPAN_ID
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|traceId
operator|!=
literal|null
operator|&&
name|spanId
operator|!=
literal|null
condition|)
block|{
name|SpanId
name|span
init|=
name|createSpanId
argument_list|(
name|traceId
argument_list|,
name|spanId
argument_list|,
name|parentSpanId
argument_list|)
decl_stmt|;
return|return
name|TraceData
operator|.
name|builder
argument_list|()
operator|.
name|sample
argument_list|(
literal|true
argument_list|)
operator|.
name|spanId
argument_list|(
name|span
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
return|return
name|TraceData
operator|.
name|builder
argument_list|()
operator|.
name|build
argument_list|()
return|;
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
literal|"camel.server.endpoint.url"
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
literal|"camel.server.exchange.id"
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
literal|"camel.server.exchange.pattern"
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
literal|"camel.server.exchange.message.request.body"
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

