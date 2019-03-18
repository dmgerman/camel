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
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
DECL|class|StringsRedisProcessorsCreator
specifier|public
specifier|final
class|class
name|StringsRedisProcessorsCreator
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
name|APPEND
argument_list|,
name|wrap
argument_list|(
name|exchange
lambda|->
name|redisClient
operator|.
name|append
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
name|getStringValue
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
name|DECR
argument_list|,
name|wrap
argument_list|(
name|exchange
lambda|->
name|redisClient
operator|.
name|decr
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
name|DECRBY
argument_list|,
name|wrap
argument_list|(
name|exchange
lambda|->
name|redisClient
operator|.
name|decrby
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
name|getLongValue
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
name|GET
argument_list|,
name|wrap
argument_list|(
name|exchange
lambda|->
name|redisClient
operator|.
name|get
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
name|GETBIT
argument_list|,
name|wrap
argument_list|(
name|exchange
lambda|->
name|redisClient
operator|.
name|getbit
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
name|getOffset
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
name|GETRANGE
argument_list|,
name|wrap
argument_list|(
name|exchange
lambda|->
name|redisClient
operator|.
name|getrange
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
name|getStart
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|exchangeConverter
operator|.
name|getEnd
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
name|GETSET
argument_list|,
name|wrap
argument_list|(
name|exchange
lambda|->
name|redisClient
operator|.
name|getset
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
name|INCR
argument_list|,
name|wrap
argument_list|(
name|exchange
lambda|->
name|redisClient
operator|.
name|incr
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
name|INCRBY
argument_list|,
name|wrap
argument_list|(
name|exchange
lambda|->
name|redisClient
operator|.
name|incrby
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
name|getLongValue
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
name|MGET
argument_list|,
name|wrap
argument_list|(
name|exchange
lambda|->
name|redisClient
operator|.
name|mget
argument_list|(
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
name|MSET
argument_list|,
name|exchange
lambda|->
name|redisClient
operator|.
name|mset
argument_list|(
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
name|MSETNX
argument_list|,
name|exchange
lambda|->
name|redisClient
operator|.
name|msetnx
argument_list|(
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
name|SET
argument_list|,
name|exchange
lambda|->
name|redisClient
operator|.
name|set
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
name|SETBIT
argument_list|,
name|exchange
lambda|->
name|redisClient
operator|.
name|setbit
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
name|getOffset
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|exchangeConverter
operator|.
name|getBooleanValue
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
name|SETEX
argument_list|,
name|exchange
lambda|->
name|redisClient
operator|.
name|setex
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
name|getValue
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|exchangeConverter
operator|.
name|getTimeout
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|bind
argument_list|(
name|Command
operator|.
name|SETNX
argument_list|,
name|wrap
argument_list|(
name|exchange
lambda|->
name|redisClient
operator|.
name|setnx
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
name|SETRANGE
argument_list|,
name|exchange
lambda|->
name|redisClient
operator|.
name|setex
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
name|getValue
argument_list|(
name|exchange
argument_list|)
argument_list|,
name|exchangeConverter
operator|.
name|getOffset
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
name|STRLEN
argument_list|,
name|wrap
argument_list|(
name|exchange
lambda|->
name|redisClient
operator|.
name|strlen
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
comment|//missing bitcount, bitfield, bitop, bitpos, incrbyfloat, psetex
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

