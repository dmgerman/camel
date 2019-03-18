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
name|javax
operator|.
name|annotation
operator|.
name|Resource
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
name|CamelContext
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
name|EndpointInject
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
name|Produce
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
name|ProducerTemplate
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
name|builder
operator|.
name|RouteBuilder
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
name|mock
operator|.
name|MockEndpoint
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
name|impl
operator|.
name|JndiRegistry
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Ignore
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
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
name|jedis
operator|.
name|JedisConnectionFactory
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

begin_class
annotation|@
name|Ignore
argument_list|(
literal|"requires online connection"
argument_list|)
DECL|class|RedisStringIdempotentRepositoryIntegrationTest
specifier|public
class|class
name|RedisStringIdempotentRepositoryIntegrationTest
extends|extends
name|CamelTestSupport
block|{
DECL|field|CONNECTION_FACTORY
specifier|private
specifier|static
specifier|final
name|JedisConnectionFactory
name|CONNECTION_FACTORY
init|=
operator|new
name|JedisConnectionFactory
argument_list|()
decl_stmt|;
DECL|field|idempotentRepository
specifier|protected
name|RedisStringIdempotentRepository
name|idempotentRepository
decl_stmt|;
annotation|@
name|Produce
argument_list|(
name|uri
operator|=
literal|"direct:start"
argument_list|)
DECL|field|producer
specifier|private
name|ProducerTemplate
name|producer
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:result"
argument_list|)
DECL|field|mockResult
specifier|private
name|MockEndpoint
name|mockResult
decl_stmt|;
annotation|@
name|Resource
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
static|static
block|{
name|CONNECTION_FACTORY
operator|.
name|afterPropertiesSet
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRegistry ()
specifier|protected
name|JndiRegistry
name|createRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|JndiRegistry
name|registry
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|redisTemplate
operator|=
operator|new
name|RedisTemplate
argument_list|<>
argument_list|()
expr_stmt|;
name|redisTemplate
operator|.
name|setConnectionFactory
argument_list|(
name|CONNECTION_FACTORY
argument_list|)
expr_stmt|;
name|redisTemplate
operator|.
name|afterPropertiesSet
argument_list|()
expr_stmt|;
name|registry
operator|.
name|bind
argument_list|(
literal|"redisTemplate"
argument_list|,
name|redisTemplate
argument_list|)
expr_stmt|;
return|return
name|registry
return|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
name|idempotentRepository
operator|=
operator|new
name|RedisStringIdempotentRepository
argument_list|(
name|redisTemplate
argument_list|,
literal|"redis-idempotent-repository"
argument_list|)
expr_stmt|;
name|RouteBuilder
name|rb
init|=
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|idempotentConsumer
argument_list|(
name|body
argument_list|()
argument_list|,
name|idempotentRepository
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
decl_stmt|;
return|return
name|rb
return|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|super
operator|.
name|createCamelContext
argument_list|()
decl_stmt|;
name|context
operator|.
name|setTracing
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
annotation|@
name|Test
DECL|method|blockDoubleSubmission ()
specifier|public
name|void
name|blockDoubleSubmission
parameter_list|()
throws|throws
name|Exception
block|{
name|mockResult
operator|.
name|expectedMessageCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|mockResult
operator|.
name|setResultWaitTime
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
name|producer
operator|.
name|sendBody
argument_list|(
literal|"abc"
argument_list|)
expr_stmt|;
name|producer
operator|.
name|sendBody
argument_list|(
literal|"bcd"
argument_list|)
expr_stmt|;
name|producer
operator|.
name|sendBody
argument_list|(
literal|"abc"
argument_list|)
expr_stmt|;
name|producer
operator|.
name|sendBody
argument_list|(
literal|"xyz"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|idempotentRepository
operator|.
name|contains
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|idempotentRepository
operator|.
name|contains
argument_list|(
literal|"bcd"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|idempotentRepository
operator|.
name|contains
argument_list|(
literal|"xyz"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|idempotentRepository
operator|.
name|contains
argument_list|(
literal|"mustNotContain"
argument_list|)
argument_list|)
expr_stmt|;
name|mockResult
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|clearIdempotentRepository ()
specifier|public
name|void
name|clearIdempotentRepository
parameter_list|()
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10000
condition|;
name|i
operator|++
control|)
block|{
name|redisTemplate
operator|.
name|opsForValue
argument_list|()
operator|.
name|set
argument_list|(
literal|"key4711"
argument_list|,
literal|"value4711"
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|"value4711"
argument_list|,
name|redisTemplate
operator|.
name|opsForValue
argument_list|()
operator|.
name|get
argument_list|(
literal|"key4711"
argument_list|)
argument_list|)
expr_stmt|;
name|producer
operator|.
name|sendBody
argument_list|(
literal|"abc"
argument_list|)
expr_stmt|;
name|producer
operator|.
name|sendBody
argument_list|(
literal|"bcd"
argument_list|)
expr_stmt|;
name|redisTemplate
operator|.
name|opsForValue
argument_list|()
operator|.
name|set
argument_list|(
literal|"redis1"
argument_list|,
literal|"1"
argument_list|)
expr_stmt|;
name|redisTemplate
operator|.
name|opsForValue
argument_list|()
operator|.
name|set
argument_list|(
literal|"different:xyz"
argument_list|,
literal|"2"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|idempotentRepository
operator|.
name|contains
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|idempotentRepository
operator|.
name|contains
argument_list|(
literal|"bcd"
argument_list|)
argument_list|)
expr_stmt|;
name|idempotentRepository
operator|.
name|clear
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|idempotentRepository
operator|.
name|contains
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|idempotentRepository
operator|.
name|contains
argument_list|(
literal|"bcd"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|idempotentRepository
operator|.
name|contains
argument_list|(
literal|"redis1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|idempotentRepository
operator|.
name|contains
argument_list|(
literal|"different:xyz"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1"
argument_list|,
name|redisTemplate
operator|.
name|opsForValue
argument_list|()
operator|.
name|get
argument_list|(
literal|"redis1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"2"
argument_list|,
name|redisTemplate
operator|.
name|opsForValue
argument_list|()
operator|.
name|get
argument_list|(
literal|"different:xyz"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|expireIdempotent ()
specifier|public
name|void
name|expireIdempotent
parameter_list|()
throws|throws
name|Exception
block|{
name|idempotentRepository
operator|.
name|setExpiry
argument_list|(
literal|5L
argument_list|)
expr_stmt|;
name|producer
operator|.
name|sendBody
argument_list|(
literal|"abc"
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|idempotentRepository
operator|.
name|contains
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|idempotentRepository
operator|.
name|contains
argument_list|(
literal|"abc"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

