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
DECL|class|RedisProducerIntegrationTest
specifier|public
class|class
name|RedisProducerIntegrationTest
extends|extends
name|RedisTestSupport
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
name|Test
DECL|method|shouldSetAString ()
specifier|public
name|void
name|shouldSetAString
parameter_list|()
throws|throws
name|Exception
block|{
name|sendHeaders
argument_list|(
name|RedisConstants
operator|.
name|COMMAND
argument_list|,
literal|"SET"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key1"
argument_list|,
name|RedisConstants
operator|.
name|VALUE
argument_list|,
literal|"value"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"value"
argument_list|,
name|redisTemplate
operator|.
name|opsForValue
argument_list|()
operator|.
name|get
argument_list|(
literal|"key1"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldGetAString ()
specifier|public
name|void
name|shouldGetAString
parameter_list|()
throws|throws
name|Exception
block|{
name|redisTemplate
operator|.
name|opsForValue
argument_list|()
operator|.
name|set
argument_list|(
literal|"key2"
argument_list|,
literal|"value"
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|sendHeaders
argument_list|(
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key2"
argument_list|,
name|RedisConstants
operator|.
name|COMMAND
argument_list|,
literal|"GET"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"value"
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

