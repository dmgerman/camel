begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.twitter.streaming
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|twitter
operator|.
name|streaming
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Consumer
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
name|Processor
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
name|Producer
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
name|component
operator|.
name|twitter
operator|.
name|AbstractTwitterEndpoint
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
name|component
operator|.
name|twitter
operator|.
name|TwitterConfiguration
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
name|component
operator|.
name|twitter
operator|.
name|TwitterHelper
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
name|component
operator|.
name|twitter
operator|.
name|consumer
operator|.
name|AbstractTwitterConsumerHandler
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
name|component
operator|.
name|twitter
operator|.
name|data
operator|.
name|StreamingType
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
name|spi
operator|.
name|Metadata
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
name|spi
operator|.
name|UriEndpoint
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
name|spi
operator|.
name|UriParam
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
name|spi
operator|.
name|UriPath
import|;
end_import

begin_comment
comment|/**  * The Twitter Streaming component consumes twitter statuses using Streaming API.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.10.0"
argument_list|,
name|scheme
operator|=
literal|"twitter-streaming"
argument_list|,
name|title
operator|=
literal|"Twitter Streaming"
argument_list|,
name|syntax
operator|=
literal|"twitter-streaming:streamingType"
argument_list|,
name|consumerClass
operator|=
name|AbstractStreamingConsumerHandler
operator|.
name|class
argument_list|,
name|consumerOnly
operator|=
literal|true
argument_list|,
name|label
operator|=
literal|"api,social"
argument_list|)
DECL|class|TwitterStreamingEndpoint
specifier|public
class|class
name|TwitterStreamingEndpoint
extends|extends
name|AbstractTwitterEndpoint
block|{
annotation|@
name|UriPath
argument_list|(
name|description
operator|=
literal|"The streaming type to consume."
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|"true"
argument_list|)
DECL|field|streamingType
specifier|private
name|StreamingType
name|streamingType
decl_stmt|;
annotation|@
name|UriParam
argument_list|(
name|description
operator|=
literal|"Can be used for a streaming filter. Multiple values can be separated with comma."
argument_list|,
name|label
operator|=
literal|"consumer,filter"
argument_list|)
DECL|field|keywords
specifier|private
name|String
name|keywords
decl_stmt|;
DECL|method|TwitterStreamingEndpoint (String uri, String remaining, String keywords, TwitterStreamingComponent component, TwitterConfiguration properties)
specifier|public
name|TwitterStreamingEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|String
name|keywords
parameter_list|,
name|TwitterStreamingComponent
name|component
parameter_list|,
name|TwitterConfiguration
name|properties
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|,
name|properties
argument_list|)
expr_stmt|;
if|if
condition|(
name|remaining
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"The streaming type must be specified for '%s'"
argument_list|,
name|uri
argument_list|)
argument_list|)
throw|;
block|}
name|this
operator|.
name|streamingType
operator|=
name|StreamingType
operator|.
name|valueOf
argument_list|(
name|remaining
operator|.
name|toUpperCase
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|keywords
operator|=
name|keywords
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Producer not supported for twitter-streaming"
argument_list|)
throw|;
block|}
annotation|@
name|Override
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
name|AbstractTwitterConsumerHandler
name|handler
decl_stmt|;
switch|switch
condition|(
name|streamingType
condition|)
block|{
case|case
name|SAMPLE
case|:
name|handler
operator|=
operator|new
name|SampleStreamingConsumerHandler
argument_list|(
name|this
argument_list|)
expr_stmt|;
break|break;
case|case
name|FILTER
case|:
name|handler
operator|=
operator|new
name|FilterStreamingConsumerHandler
argument_list|(
name|this
argument_list|,
name|keywords
argument_list|)
expr_stmt|;
break|break;
case|case
name|USER
case|:
name|handler
operator|=
operator|new
name|UserStreamingConsumerHandler
argument_list|(
name|this
argument_list|)
expr_stmt|;
break|break;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot create any consumer with uri "
operator|+
name|getEndpointUri
argument_list|()
operator|+
literal|". A streaming type was not provided (or an incorrect pairing was used)."
argument_list|)
throw|;
block|}
return|return
name|TwitterHelper
operator|.
name|createConsumer
argument_list|(
name|processor
argument_list|,
name|this
argument_list|,
name|handler
argument_list|)
return|;
block|}
DECL|method|getStreamingType ()
specifier|public
name|StreamingType
name|getStreamingType
parameter_list|()
block|{
return|return
name|streamingType
return|;
block|}
block|}
end_class

end_unit

