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
name|ServerResponseAdapter
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
DECL|class|ZipkinServerResponseAdapter
specifier|public
class|class
name|ZipkinServerResponseAdapter
implements|implements
name|ServerResponseAdapter
block|{
DECL|field|eventNotifier
specifier|private
specifier|final
name|ZipkinEventNotifier
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
DECL|field|url
specifier|private
specifier|final
name|String
name|url
decl_stmt|;
DECL|method|ZipkinServerResponseAdapter (ZipkinEventNotifier eventNotifier, Exchange exchange)
specifier|public
name|ZipkinServerResponseAdapter
parameter_list|(
name|ZipkinEventNotifier
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
DECL|method|responseAnnotations ()
specifier|public
name|Collection
argument_list|<
name|KeyValueAnnotation
argument_list|>
name|responseAnnotations
parameter_list|()
block|{
name|String
name|id
init|=
name|exchange
operator|.
name|getExchangeId
argument_list|()
decl_stmt|;
name|String
name|mep
init|=
name|exchange
operator|.
name|getPattern
argument_list|()
operator|.
name|name
argument_list|()
decl_stmt|;
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
name|id
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
name|mep
argument_list|)
decl_stmt|;
name|KeyValueAnnotation
name|key4
init|=
literal|null
decl_stmt|;
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
name|String
name|message
init|=
name|exchange
operator|.
name|getException
argument_list|()
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|key4
operator|=
name|KeyValueAnnotation
operator|.
name|create
argument_list|(
literal|"camel.server.exchange.failure"
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
elseif|else
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
literal|"camel.server.exchange.message.response.body"
argument_list|,
name|body
argument_list|)
expr_stmt|;
block|}
name|KeyValueAnnotation
name|key5
init|=
literal|null
decl_stmt|;
comment|// lets capture http response code for http based components
name|String
name|responseCode
init|=
name|exchange
operator|.
name|hasOut
argument_list|()
condition|?
name|exchange
operator|.
name|getOut
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
else|:
name|exchange
operator|.
name|getIn
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
name|key5
operator|=
name|KeyValueAnnotation
operator|.
name|create
argument_list|(
literal|"camel.server.exchange.message.response.code"
argument_list|,
name|responseCode
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
if|if
condition|(
name|key5
operator|!=
literal|null
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|key5
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

