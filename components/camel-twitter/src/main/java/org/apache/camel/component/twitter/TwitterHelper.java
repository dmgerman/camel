begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.twitter
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
name|Message
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
name|consumer
operator|.
name|DefaultTwitterConsumer
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
name|ConsumerType
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
name|component
operator|.
name|twitter
operator|.
name|data
operator|.
name|TimelineType
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
name|directmessage
operator|.
name|DirectMessageConsumerHandler
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
name|directmessage
operator|.
name|DirectMessageProducer
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
name|search
operator|.
name|SearchConsumerHandler
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
name|search
operator|.
name|SearchProducer
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
name|streaming
operator|.
name|FilterStreamingConsumerHandler
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
name|streaming
operator|.
name|SampleStreamingConsumerHandler
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
name|streaming
operator|.
name|UserStreamingConsumerHandler
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
name|timeline
operator|.
name|HomeConsumerHandler
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
name|timeline
operator|.
name|MentionsConsumerHandler
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
name|timeline
operator|.
name|RetweetsConsumerHandler
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
name|timeline
operator|.
name|UserConsumerHandler
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
name|timeline
operator|.
name|UserProducer
import|;
end_import

begin_import
import|import
name|twitter4j
operator|.
name|User
import|;
end_import

begin_class
DECL|class|TwitterHelper
specifier|public
specifier|final
class|class
name|TwitterHelper
block|{
DECL|method|TwitterHelper ()
specifier|private
name|TwitterHelper
parameter_list|()
block|{     }
DECL|method|setUserHeader (Exchange exchange, User user)
specifier|public
specifier|static
name|void
name|setUserHeader
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|User
name|user
parameter_list|)
block|{
name|setUserHeader
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
name|user
argument_list|)
expr_stmt|;
block|}
DECL|method|setUserHeader (Message message, User user)
specifier|public
specifier|static
name|void
name|setUserHeader
parameter_list|(
name|Message
name|message
parameter_list|,
name|User
name|user
parameter_list|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|TwitterConstants
operator|.
name|TWITTER_USER
argument_list|,
name|user
argument_list|)
expr_stmt|;
block|}
DECL|method|setUserHeader (Exchange exchange, int index, User user, String role)
specifier|public
specifier|static
name|void
name|setUserHeader
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|int
name|index
parameter_list|,
name|User
name|user
parameter_list|,
name|String
name|role
parameter_list|)
block|{
name|setUserHeader
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
argument_list|,
name|index
argument_list|,
name|user
argument_list|,
name|role
argument_list|)
expr_stmt|;
block|}
DECL|method|setUserHeader (Message message, int index, User user, String role)
specifier|public
specifier|static
name|void
name|setUserHeader
parameter_list|(
name|Message
name|message
parameter_list|,
name|int
name|index
parameter_list|,
name|User
name|user
parameter_list|,
name|String
name|role
parameter_list|)
block|{
name|message
operator|.
name|setHeader
argument_list|(
name|TwitterConstants
operator|.
name|TWITTER_USER
operator|+
name|index
argument_list|,
name|user
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
name|TwitterConstants
operator|.
name|TWITTER_USER_ROLE
operator|+
name|index
argument_list|,
name|role
argument_list|)
expr_stmt|;
block|}
DECL|method|createConsumer (Processor processor, AbstractTwitterEndpoint endpoint, AbstractTwitterConsumerHandler handler)
specifier|public
specifier|static
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|,
name|AbstractTwitterEndpoint
name|endpoint
parameter_list|,
name|AbstractTwitterConsumerHandler
name|handler
parameter_list|)
throws|throws
name|Exception
block|{
name|Consumer
name|answer
init|=
operator|new
name|DefaultTwitterConsumer
argument_list|(
name|endpoint
argument_list|,
name|processor
argument_list|,
name|handler
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|endpoint
operator|.
name|getEndpointType
argument_list|()
condition|)
block|{
case|case
name|POLLING
case|:
name|handler
operator|.
name|setLastId
argument_list|(
name|endpoint
operator|.
name|getProperties
argument_list|()
operator|.
name|getSinceId
argument_list|()
argument_list|)
expr_stmt|;
name|endpoint
operator|.
name|configureConsumer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
break|break;
case|case
name|DIRECT
case|:
name|endpoint
operator|.
name|configureConsumer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
break|break;
default|default:
break|break;
block|}
return|return
name|answer
return|;
block|}
DECL|method|enumFromString (T[] values, String uri, T defaultValue)
specifier|public
specifier|static
parameter_list|<
name|T
extends|extends
name|Enum
argument_list|<
name|T
argument_list|>
parameter_list|>
name|T
name|enumFromString
parameter_list|(
name|T
index|[]
name|values
parameter_list|,
name|String
name|uri
parameter_list|,
name|T
name|defaultValue
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
name|values
operator|.
name|length
operator|-
literal|1
init|;
name|i
operator|>=
literal|0
condition|;
name|i
operator|--
control|)
block|{
if|if
condition|(
name|values
index|[
name|i
index|]
operator|.
name|name
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|uri
argument_list|)
condition|)
block|{
return|return
name|values
index|[
name|i
index|]
return|;
block|}
block|}
return|return
name|defaultValue
return|;
block|}
block|}
end_class

end_unit

