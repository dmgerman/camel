begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.redis
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|redis
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
import|;
end_import

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
name|support
operator|.
name|DefaultConsumer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|data
operator|.
name|redis
operator|.
name|connection
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|data
operator|.
name|redis
operator|.
name|connection
operator|.
name|MessageListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|data
operator|.
name|redis
operator|.
name|listener
operator|.
name|ChannelTopic
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|data
operator|.
name|redis
operator|.
name|listener
operator|.
name|PatternTopic
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|data
operator|.
name|redis
operator|.
name|listener
operator|.
name|Topic
import|;
end_import

begin_class
DECL|class|RedisConsumer
specifier|public
class|class
name|RedisConsumer
extends|extends
name|DefaultConsumer
implements|implements
name|MessageListener
block|{
DECL|field|redisConfiguration
specifier|private
specifier|final
name|RedisConfiguration
name|redisConfiguration
decl_stmt|;
DECL|method|RedisConsumer (RedisEndpoint redisEndpoint, Processor processor, RedisConfiguration redisConfiguration)
specifier|public
name|RedisConsumer
parameter_list|(
name|RedisEndpoint
name|redisEndpoint
parameter_list|,
name|Processor
name|processor
parameter_list|,
name|RedisConfiguration
name|redisConfiguration
parameter_list|)
block|{
name|super
argument_list|(
name|redisEndpoint
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|this
operator|.
name|redisConfiguration
operator|=
name|redisConfiguration
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStart
argument_list|()
expr_stmt|;
name|Collection
argument_list|<
name|Topic
argument_list|>
name|topics
init|=
name|toTopics
argument_list|(
name|redisConfiguration
operator|.
name|getChannels
argument_list|()
argument_list|)
decl_stmt|;
name|redisConfiguration
operator|.
name|getListenerContainer
argument_list|()
operator|.
name|addMessageListener
argument_list|(
name|this
argument_list|,
name|topics
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|Collection
argument_list|<
name|Topic
argument_list|>
name|topics
init|=
name|toTopics
argument_list|(
name|redisConfiguration
operator|.
name|getChannels
argument_list|()
argument_list|)
decl_stmt|;
name|redisConfiguration
operator|.
name|getListenerContainer
argument_list|()
operator|.
name|removeMessageListener
argument_list|(
name|this
argument_list|,
name|topics
argument_list|)
expr_stmt|;
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
block|}
DECL|method|toTopics (String channels)
specifier|private
name|Collection
argument_list|<
name|Topic
argument_list|>
name|toTopics
parameter_list|(
name|String
name|channels
parameter_list|)
block|{
name|String
index|[]
name|channelsArrays
init|=
name|channels
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Topic
argument_list|>
name|topics
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|channel
range|:
name|channelsArrays
control|)
block|{
name|String
name|name
init|=
name|channel
operator|.
name|trim
argument_list|()
decl_stmt|;
if|if
condition|(
name|Command
operator|.
name|PSUBSCRIBE
operator|.
name|equals
argument_list|(
name|redisConfiguration
operator|.
name|getCommand
argument_list|()
argument_list|)
condition|)
block|{
name|topics
operator|.
name|add
argument_list|(
operator|new
name|PatternTopic
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|Command
operator|.
name|SUBSCRIBE
operator|.
name|equals
argument_list|(
name|redisConfiguration
operator|.
name|getCommand
argument_list|()
argument_list|)
condition|)
block|{
name|topics
operator|.
name|add
argument_list|(
operator|new
name|ChannelTopic
argument_list|(
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported Command "
operator|+
name|redisConfiguration
operator|.
name|getCommand
argument_list|()
argument_list|)
throw|;
block|}
block|}
return|return
name|topics
return|;
block|}
annotation|@
name|Override
DECL|method|onMessage (Message message, byte[] pattern)
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
name|message
parameter_list|,
name|byte
index|[]
name|pattern
parameter_list|)
block|{
try|try
block|{
name|Exchange
name|exchange
init|=
name|getEndpoint
argument_list|()
operator|.
name|createExchange
argument_list|()
decl_stmt|;
name|setChannel
argument_list|(
name|exchange
argument_list|,
name|message
operator|.
name|getChannel
argument_list|()
argument_list|)
expr_stmt|;
name|setPattern
argument_list|(
name|exchange
argument_list|,
name|pattern
argument_list|)
expr_stmt|;
name|setBody
argument_list|(
name|exchange
argument_list|,
name|message
operator|.
name|getBody
argument_list|()
argument_list|)
expr_stmt|;
name|getProcessor
argument_list|()
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|setBody (Exchange exchange, byte[] body)
specifier|private
name|void
name|setBody
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|byte
index|[]
name|body
parameter_list|)
block|{
if|if
condition|(
name|body
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|redisConfiguration
operator|.
name|getSerializer
argument_list|()
operator|.
name|deserialize
argument_list|(
name|body
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setPattern (Exchange exchange, byte[] pattern)
specifier|private
name|void
name|setPattern
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|byte
index|[]
name|pattern
parameter_list|)
block|{
if|if
condition|(
name|pattern
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
name|RedisConstants
operator|.
name|PATTERN
argument_list|,
name|pattern
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|setChannel (Exchange exchange, byte[] message)
specifier|private
name|void
name|setChannel
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|byte
index|[]
name|message
parameter_list|)
throws|throws
name|UnsupportedEncodingException
block|{
if|if
condition|(
name|message
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
name|RedisConstants
operator|.
name|CHANNEL
argument_list|,
operator|new
name|String
argument_list|(
name|message
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

