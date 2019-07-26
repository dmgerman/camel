begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|brave
operator|.
name|SpanCustomizer
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
name|StreamCache
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
name|support
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
name|prepareBodyForLogging
import|;
end_import

begin_class
DECL|class|ZipkinClientResponseAdaptor
class|class
name|ZipkinClientResponseAdaptor
block|{
DECL|field|eventNotifier
specifier|private
specifier|final
name|ZipkinTracer
name|eventNotifier
decl_stmt|;
DECL|field|url
specifier|private
specifier|final
name|String
name|url
decl_stmt|;
DECL|method|ZipkinClientResponseAdaptor (ZipkinTracer eventNotifier, Endpoint endpoint)
name|ZipkinClientResponseAdaptor
parameter_list|(
name|ZipkinTracer
name|eventNotifier
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
DECL|method|onResponse (Exchange exchange, SpanCustomizer span)
name|void
name|onResponse
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|SpanCustomizer
name|span
parameter_list|)
block|{
name|span
operator|.
name|tag
argument_list|(
literal|"camel.client.endpoint.url"
argument_list|,
name|url
argument_list|)
expr_stmt|;
name|span
operator|.
name|tag
argument_list|(
literal|"camel.client.exchange.id"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
name|span
operator|.
name|tag
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
expr_stmt|;
if|if
condition|(
name|eventNotifier
operator|.
name|isIncludeMessageBody
argument_list|()
operator|||
name|eventNotifier
operator|.
name|isIncludeMessageBodyStreams
argument_list|()
condition|)
block|{
name|boolean
name|streams
init|=
name|eventNotifier
operator|.
name|isIncludeMessageBodyStreams
argument_list|()
decl_stmt|;
name|StreamCache
name|cache
init|=
name|prepareBodyForLogging
argument_list|(
name|exchange
argument_list|,
name|streams
argument_list|)
decl_stmt|;
name|String
name|body
init|=
name|MessageHelper
operator|.
name|extractBodyForLogging
argument_list|(
name|exchange
operator|.
name|getMessage
argument_list|()
argument_list|,
literal|""
argument_list|,
name|streams
argument_list|,
name|streams
argument_list|)
decl_stmt|;
name|span
operator|.
name|tag
argument_list|(
literal|"camel.client.exchange.message.response.body"
argument_list|,
name|body
argument_list|)
expr_stmt|;
if|if
condition|(
name|cache
operator|!=
literal|null
condition|)
block|{
name|cache
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
block|}
comment|// lets capture http response code for http based components
name|String
name|responseCode
init|=
name|exchange
operator|.
name|getMessage
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Exchange
operator|.
name|HTTP_RESPONSE_CODE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|responseCode
operator|!=
literal|null
condition|)
block|{
name|span
operator|.
name|tag
argument_list|(
literal|"camel.client.exchange.message.response.code"
argument_list|,
name|responseCode
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

