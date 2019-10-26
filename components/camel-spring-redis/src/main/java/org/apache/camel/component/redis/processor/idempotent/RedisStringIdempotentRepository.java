begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.redis.processor.idempotent
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
operator|.
name|processor
operator|.
name|idempotent
package|;
end_package

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Duration
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
name|api
operator|.
name|management
operator|.
name|ManagedOperation
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
name|api
operator|.
name|management
operator|.
name|ManagedResource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|dao
operator|.
name|DataAccessException
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
name|RedisConnection
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
name|core
operator|.
name|Cursor
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
name|core
operator|.
name|RedisCallback
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
name|core
operator|.
name|RedisTemplate
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
name|core
operator|.
name|ScanOptions
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
name|core
operator|.
name|ValueOperations
import|;
end_import

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Spring Redis based message id repository"
argument_list|)
DECL|class|RedisStringIdempotentRepository
specifier|public
class|class
name|RedisStringIdempotentRepository
extends|extends
name|RedisIdempotentRepository
block|{
DECL|field|valueOperations
specifier|private
specifier|final
name|ValueOperations
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|valueOperations
decl_stmt|;
DECL|field|expiry
specifier|private
name|long
name|expiry
decl_stmt|;
DECL|method|RedisStringIdempotentRepository (RedisTemplate<String, String> redisTemplate, String processorName)
specifier|public
name|RedisStringIdempotentRepository
parameter_list|(
name|RedisTemplate
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|redisTemplate
parameter_list|,
name|String
name|processorName
parameter_list|)
block|{
name|super
argument_list|(
name|redisTemplate
argument_list|,
name|processorName
argument_list|)
expr_stmt|;
name|this
operator|.
name|valueOperations
operator|=
name|redisTemplate
operator|.
name|opsForValue
argument_list|()
expr_stmt|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Does the store contain the given key"
argument_list|)
annotation|@
name|Override
DECL|method|contains (String key)
specifier|public
name|boolean
name|contains
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|String
name|value
init|=
name|valueOperations
operator|.
name|get
argument_list|(
name|createRedisKey
argument_list|(
name|key
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|value
operator|!=
literal|null
return|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Adds the key to the store"
argument_list|)
annotation|@
name|Override
DECL|method|add (String key)
specifier|public
name|boolean
name|add
parameter_list|(
name|String
name|key
parameter_list|)
block|{
if|if
condition|(
name|expiry
operator|>
literal|0
condition|)
block|{
return|return
name|valueOperations
operator|.
name|setIfAbsent
argument_list|(
name|createRedisKey
argument_list|(
name|key
argument_list|)
argument_list|,
name|key
argument_list|,
name|Duration
operator|.
name|ofSeconds
argument_list|(
name|expiry
argument_list|)
argument_list|)
return|;
block|}
return|return
name|valueOperations
operator|.
name|setIfAbsent
argument_list|(
name|createRedisKey
argument_list|(
name|key
argument_list|)
argument_list|,
name|key
argument_list|)
return|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Remove the key from the store"
argument_list|)
annotation|@
name|Override
DECL|method|remove (String key)
specifier|public
name|boolean
name|remove
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|valueOperations
operator|.
name|getOperations
argument_list|()
operator|.
name|delete
argument_list|(
name|createRedisKey
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Clear the store"
argument_list|)
annotation|@
name|Override
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|valueOperations
operator|.
name|getOperations
argument_list|()
operator|.
name|execute
argument_list|(
operator|new
name|RedisCallback
argument_list|<
name|List
argument_list|<
name|byte
index|[]
argument_list|>
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|byte
index|[]
argument_list|>
name|doInRedis
parameter_list|(
name|RedisConnection
name|connection
parameter_list|)
throws|throws
name|DataAccessException
block|{
name|List
argument_list|<
name|byte
index|[]
argument_list|>
name|binaryKeys
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Cursor
argument_list|<
name|byte
index|[]
argument_list|>
name|cursor
init|=
name|connection
operator|.
name|scan
argument_list|(
name|ScanOptions
operator|.
name|scanOptions
argument_list|()
operator|.
name|match
argument_list|(
literal|"*"
operator|+
name|createRedisKey
argument_list|(
literal|"*"
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
decl_stmt|;
while|while
condition|(
name|cursor
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|byte
index|[]
name|key
init|=
name|cursor
operator|.
name|next
argument_list|()
decl_stmt|;
name|binaryKeys
operator|.
name|add
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|binaryKeys
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|connection
operator|.
name|del
argument_list|(
name|binaryKeys
operator|.
name|toArray
argument_list|(
operator|new
name|byte
index|[]
index|[]
block|{}
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|binaryKeys
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
DECL|method|createRedisKey (String key)
specifier|protected
name|String
name|createRedisKey
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|getProcessorName
argument_list|()
operator|+
literal|":"
operator|+
name|key
return|;
block|}
DECL|method|getExpiry ()
specifier|public
name|long
name|getExpiry
parameter_list|()
block|{
return|return
name|expiry
return|;
block|}
comment|/**      * Expire all newly added items after the given number of seconds (0 means never expire)      */
DECL|method|setExpiry (long expiry)
specifier|public
name|void
name|setExpiry
parameter_list|(
name|long
name|expiry
parameter_list|)
block|{
name|this
operator|.
name|expiry
operator|=
name|expiry
expr_stmt|;
block|}
block|}
end_class

end_unit

