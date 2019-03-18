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
name|ManagedAttribute
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
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|redis
operator|.
name|RedisConfiguration
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
name|IdempotentRepository
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
name|service
operator|.
name|ServiceSupport
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
name|SetOperations
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
DECL|class|RedisIdempotentRepository
specifier|public
class|class
name|RedisIdempotentRepository
extends|extends
name|ServiceSupport
implements|implements
name|IdempotentRepository
block|{
DECL|field|setOperations
specifier|private
specifier|final
name|SetOperations
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|setOperations
decl_stmt|;
DECL|field|processorName
specifier|private
specifier|final
name|String
name|processorName
decl_stmt|;
DECL|field|redisConfiguration
specifier|private
name|RedisConfiguration
name|redisConfiguration
decl_stmt|;
DECL|field|redisTemplate
specifier|private
name|RedisTemplate
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|redisTemplate
decl_stmt|;
DECL|method|RedisIdempotentRepository (RedisTemplate<String, String> redisTemplate, String processorName)
specifier|public
name|RedisIdempotentRepository
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
name|this
operator|.
name|setOperations
operator|=
name|redisTemplate
operator|.
name|opsForSet
argument_list|()
expr_stmt|;
name|this
operator|.
name|processorName
operator|=
name|processorName
expr_stmt|;
name|this
operator|.
name|redisTemplate
operator|=
name|redisTemplate
expr_stmt|;
block|}
DECL|method|RedisIdempotentRepository (String processorName)
specifier|public
name|RedisIdempotentRepository
parameter_list|(
name|String
name|processorName
parameter_list|)
block|{
name|redisConfiguration
operator|=
operator|new
name|RedisConfiguration
argument_list|()
expr_stmt|;
name|RedisTemplate
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|redisTemplate
init|=
name|redisConfiguration
operator|.
name|getRedisTemplate
argument_list|()
decl_stmt|;
name|this
operator|.
name|redisTemplate
operator|=
name|redisTemplate
expr_stmt|;
name|this
operator|.
name|setOperations
operator|=
name|redisTemplate
operator|.
name|opsForSet
argument_list|()
expr_stmt|;
name|redisTemplate
operator|.
name|getConnectionFactory
argument_list|()
operator|.
name|getConnection
argument_list|()
operator|.
name|flushDb
argument_list|()
expr_stmt|;
name|this
operator|.
name|processorName
operator|=
name|processorName
expr_stmt|;
block|}
DECL|method|redisIdempotentRepository (String processorName)
specifier|public
specifier|static
name|RedisIdempotentRepository
name|redisIdempotentRepository
parameter_list|(
name|String
name|processorName
parameter_list|)
block|{
return|return
operator|new
name|RedisIdempotentRepository
argument_list|(
name|processorName
argument_list|)
return|;
block|}
DECL|method|redisIdempotentRepository ( RedisTemplate<String, String> redisTemplate, String processorName)
specifier|public
specifier|static
name|RedisIdempotentRepository
name|redisIdempotentRepository
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
return|return
operator|new
name|RedisIdempotentRepository
argument_list|(
name|redisTemplate
argument_list|,
name|processorName
argument_list|)
return|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Adds the key to the store"
argument_list|)
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
operator|!
name|contains
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
name|setOperations
operator|.
name|add
argument_list|(
name|processorName
argument_list|,
name|key
argument_list|)
operator|!=
literal|null
return|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Does the store contain the given key"
argument_list|)
DECL|method|contains (String key)
specifier|public
name|boolean
name|contains
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|setOperations
operator|.
name|isMember
argument_list|(
name|processorName
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
DECL|method|remove (String key)
specifier|public
name|boolean
name|remove
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|setOperations
operator|.
name|remove
argument_list|(
name|processorName
argument_list|,
name|key
argument_list|)
operator|!=
literal|null
return|;
block|}
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Clear the store"
argument_list|)
DECL|method|clear ()
specifier|public
name|void
name|clear
parameter_list|()
block|{
name|redisTemplate
operator|.
name|getConnectionFactory
argument_list|()
operator|.
name|getConnection
argument_list|()
operator|.
name|flushDb
argument_list|()
expr_stmt|;
block|}
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The processor name"
argument_list|)
DECL|method|getProcessorName ()
specifier|public
name|String
name|getProcessorName
parameter_list|()
block|{
return|return
name|processorName
return|;
block|}
DECL|method|confirm (String key)
specifier|public
name|boolean
name|confirm
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
comment|// noop
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
if|if
condition|(
name|redisConfiguration
operator|!=
literal|null
condition|)
block|{
name|redisConfiguration
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

