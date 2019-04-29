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
name|support
operator|.
name|DefaultEndpoint
import|;
end_import

begin_comment
comment|/**  * The spring-redis component allows sending and receiving messages from Redis.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.11.0"
argument_list|,
name|scheme
operator|=
literal|"spring-redis"
argument_list|,
name|title
operator|=
literal|"Spring Redis"
argument_list|,
name|syntax
operator|=
literal|"spring-redist:host:port"
argument_list|,
name|label
operator|=
literal|"spring,nosql"
argument_list|)
DECL|class|RedisEndpoint
specifier|public
class|class
name|RedisEndpoint
extends|extends
name|DefaultEndpoint
block|{
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|RedisConfiguration
name|configuration
decl_stmt|;
DECL|field|redisProcessorsCreator
specifier|private
name|RedisProcessorsCreator
name|redisProcessorsCreator
decl_stmt|;
DECL|method|RedisEndpoint (String uri, RedisComponent component, RedisConfiguration configuration)
specifier|public
name|RedisEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|RedisComponent
name|component
parameter_list|,
name|RedisConfiguration
name|configuration
parameter_list|)
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|redisProcessorsCreator
operator|=
operator|new
name|AllRedisProcessorsCreator
argument_list|(
operator|new
name|RedisClient
argument_list|(
name|configuration
operator|.
name|getRedisTemplate
argument_list|()
argument_list|)
argument_list|,
operator|(
operator|(
name|RedisComponent
operator|)
name|getComponent
argument_list|()
operator|)
operator|.
name|getExchangeConverter
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
name|Command
name|defaultCommand
init|=
name|configuration
operator|.
name|getCommand
argument_list|()
decl_stmt|;
if|if
condition|(
name|defaultCommand
operator|==
literal|null
condition|)
block|{
name|defaultCommand
operator|=
name|Command
operator|.
name|SET
expr_stmt|;
block|}
return|return
operator|new
name|RedisProducer
argument_list|(
name|this
argument_list|,
name|RedisConstants
operator|.
name|COMMAND
argument_list|,
name|defaultCommand
operator|.
name|name
argument_list|()
argument_list|,
name|redisProcessorsCreator
argument_list|)
return|;
block|}
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
name|RedisConsumer
name|answer
init|=
operator|new
name|RedisConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|,
name|configuration
argument_list|)
decl_stmt|;
name|configureConsumer
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|answer
return|;
block|}
DECL|method|doShutdown ()
specifier|protected
name|void
name|doShutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doShutdown
argument_list|()
expr_stmt|;
name|configuration
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|getConfiguration ()
specifier|public
name|RedisConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
block|}
end_class

end_unit

