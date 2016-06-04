begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|util
operator|.
name|Map
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

begin_class
DECL|class|HashesRedisProcessorsCreator
specifier|public
specifier|final
class|class
name|HashesRedisProcessorsCreator
extends|extends
name|AbstractRedisProcessorCreator
block|{
DECL|method|getProcessors (RedisClient redisClient, ExchangeConverter exchangeConverter)
name|Map
argument_list|<
name|Command
argument_list|,
name|Processor
argument_list|>
name|getProcessors
parameter_list|(
name|RedisClient
name|redisClient
parameter_list|,
name|ExchangeConverter
name|exchangeConverter
parameter_list|)
block|{
name|bind
argument_list|(
name|Command
operator|.
name|HDEL
argument_list|,
name|exchange
lambda|->
name|redisClient
operator|.
name|hdel
argument_list|(
name|exchangeConverter
operator|.
name|getKey
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|exchangeConverter
operator|.
name|getField
argument_list|(
name|exchange
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|Command
operator|.
name|HEXISTS
argument_list|,
name|wrap
argument_list|(
name|exchange
lambda|->
name|redisClient
operator|.
name|hexists
argument_list|(
name|exchangeConverter
operator|.
name|getKey
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|exchangeConverter
operator|.
name|getField
argument_list|(
name|exchange
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|Command
operator|.
name|HGET
argument_list|,
name|wrap
argument_list|(
name|exchange
lambda|->
name|redisClient
operator|.
name|hget
argument_list|(
name|exchangeConverter
operator|.
name|getKey
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|exchangeConverter
operator|.
name|getField
argument_list|(
name|exchange
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|Command
operator|.
name|HGETALL
argument_list|,
name|wrap
argument_list|(
name|exchange
lambda|->
name|redisClient
operator|.
name|hgetAll
argument_list|(
name|exchangeConverter
operator|.
name|getKey
argument_list|(
name|exchange
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|Command
operator|.
name|HINCRBY
argument_list|,
name|wrap
argument_list|(
name|exchange
lambda|->
name|redisClient
operator|.
name|hincrBy
argument_list|(
name|exchangeConverter
operator|.
name|getKey
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|exchangeConverter
operator|.
name|getField
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|exchangeConverter
operator|.
name|getValueAsLong
argument_list|(
name|exchange
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|Command
operator|.
name|HKEYS
argument_list|,
name|wrap
argument_list|(
name|exchange
lambda|->
name|redisClient
operator|.
name|hkeys
argument_list|(
name|exchangeConverter
operator|.
name|getKey
argument_list|(
name|exchange
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|Command
operator|.
name|HLEN
argument_list|,
name|wrap
argument_list|(
name|exchange
lambda|->
name|redisClient
operator|.
name|hlen
argument_list|(
name|exchangeConverter
operator|.
name|getKey
argument_list|(
name|exchange
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|Command
operator|.
name|HMGET
argument_list|,
name|wrap
argument_list|(
name|exchange
lambda|->
name|redisClient
operator|.
name|hmget
argument_list|(
name|exchangeConverter
operator|.
name|getKey
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|exchangeConverter
operator|.
name|getFields
argument_list|(
name|exchange
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|Command
operator|.
name|HMSET
argument_list|,
name|exchange
lambda|->
name|redisClient
operator|.
name|hmset
argument_list|(
name|exchangeConverter
operator|.
name|getKey
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|exchangeConverter
operator|.
name|getValuesAsMap
argument_list|(
name|exchange
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|Command
operator|.
name|HSET
argument_list|,
name|exchange
lambda|->
name|redisClient
operator|.
name|hset
argument_list|(
name|exchangeConverter
operator|.
name|getKey
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|exchangeConverter
operator|.
name|getField
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|exchangeConverter
operator|.
name|getValue
argument_list|(
name|exchange
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|Command
operator|.
name|HSETNX
argument_list|,
name|wrap
argument_list|(
name|exchange
lambda|->
name|redisClient
operator|.
name|hsetnx
argument_list|(
name|exchangeConverter
operator|.
name|getKey
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|exchangeConverter
operator|.
name|getField
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|exchangeConverter
operator|.
name|getValue
argument_list|(
name|exchange
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|Command
operator|.
name|HVALS
argument_list|,
name|wrap
argument_list|(
name|exchange
lambda|->
name|redisClient
operator|.
name|hvals
argument_list|(
name|exchangeConverter
operator|.
name|getKey
argument_list|(
name|exchange
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|//missing: hincrbyfloat, hstrlen, hscan
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

