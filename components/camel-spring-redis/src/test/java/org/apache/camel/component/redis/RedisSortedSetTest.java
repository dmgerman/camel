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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|junit
operator|.
name|Before
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
name|ZSetOperations
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Matchers
operator|.
name|anyDouble
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Matchers
operator|.
name|anyLong
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Matchers
operator|.
name|anyObject
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Matchers
operator|.
name|anyString
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|verify
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
import|;
end_import

begin_class
DECL|class|RedisSortedSetTest
specifier|public
class|class
name|RedisSortedSetTest
extends|extends
name|RedisTestSupport
block|{
DECL|field|redisTemplate
specifier|private
name|RedisTemplate
name|redisTemplate
decl_stmt|;
DECL|field|zSetOperations
specifier|private
name|ZSetOperations
name|zSetOperations
decl_stmt|;
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
name|when
argument_list|(
name|redisTemplate
operator|.
name|opsForZSet
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|zSetOperations
argument_list|)
expr_stmt|;
name|JndiRegistry
name|registry
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
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
name|Before
DECL|method|setUp ()
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|redisTemplate
operator|=
name|mock
argument_list|(
name|RedisTemplate
operator|.
name|class
argument_list|)
expr_stmt|;
name|zSetOperations
operator|=
name|mock
argument_list|(
name|ZSetOperations
operator|.
name|class
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteZADD ()
specifier|public
name|void
name|shouldExecuteZADD
parameter_list|()
block|{
name|when
argument_list|(
name|zSetOperations
operator|.
name|add
argument_list|(
name|anyString
argument_list|()
argument_list|,
name|anyObject
argument_list|()
argument_list|,
name|anyDouble
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|sendHeaders
argument_list|(
name|RedisConstants
operator|.
name|COMMAND
argument_list|,
literal|"ZADD"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key"
argument_list|,
name|RedisConstants
operator|.
name|VALUE
argument_list|,
literal|"value"
argument_list|,
name|RedisConstants
operator|.
name|SCORE
argument_list|,
literal|1.0
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|zSetOperations
argument_list|)
operator|.
name|add
argument_list|(
literal|"key"
argument_list|,
literal|"value"
argument_list|,
literal|1.0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteZCARD ()
specifier|public
name|void
name|shouldExecuteZCARD
parameter_list|()
block|{
name|when
argument_list|(
name|zSetOperations
operator|.
name|size
argument_list|(
name|anyString
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|2L
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|sendHeaders
argument_list|(
name|RedisConstants
operator|.
name|COMMAND
argument_list|,
literal|"ZCARD"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key"
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|zSetOperations
argument_list|)
operator|.
name|size
argument_list|(
literal|"key"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2L
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteZCOUNT ()
specifier|public
name|void
name|shouldExecuteZCOUNT
parameter_list|()
block|{
name|when
argument_list|(
name|zSetOperations
operator|.
name|count
argument_list|(
name|anyString
argument_list|()
argument_list|,
name|anyDouble
argument_list|()
argument_list|,
name|anyDouble
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|3L
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|sendHeaders
argument_list|(
name|RedisConstants
operator|.
name|COMMAND
argument_list|,
literal|"ZCOUNT"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key"
argument_list|,
name|RedisConstants
operator|.
name|MIN
argument_list|,
literal|1.0
argument_list|,
name|RedisConstants
operator|.
name|MAX
argument_list|,
literal|2.0
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|zSetOperations
argument_list|)
operator|.
name|count
argument_list|(
literal|"key"
argument_list|,
literal|1.0
argument_list|,
literal|2.0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3L
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteZINCRBY ()
specifier|public
name|void
name|shouldExecuteZINCRBY
parameter_list|()
block|{
name|when
argument_list|(
name|zSetOperations
operator|.
name|incrementScore
argument_list|(
name|anyString
argument_list|()
argument_list|,
name|anyString
argument_list|()
argument_list|,
name|anyDouble
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|3.0
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|sendHeaders
argument_list|(
name|RedisConstants
operator|.
name|COMMAND
argument_list|,
literal|"ZINCRBY"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key"
argument_list|,
name|RedisConstants
operator|.
name|VALUE
argument_list|,
literal|"value"
argument_list|,
name|RedisConstants
operator|.
name|INCREMENT
argument_list|,
literal|2.0
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|zSetOperations
argument_list|)
operator|.
name|incrementScore
argument_list|(
literal|"key"
argument_list|,
literal|"value"
argument_list|,
literal|2.0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3.0
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteZINTERSTORE ()
specifier|public
name|void
name|shouldExecuteZINTERSTORE
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|keys
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|keys
operator|.
name|add
argument_list|(
literal|"key2"
argument_list|)
expr_stmt|;
name|keys
operator|.
name|add
argument_list|(
literal|"key3"
argument_list|)
expr_stmt|;
name|sendHeaders
argument_list|(
name|RedisConstants
operator|.
name|COMMAND
argument_list|,
literal|"ZINTERSTORE"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key"
argument_list|,
name|RedisConstants
operator|.
name|DESTINATION
argument_list|,
literal|"destination"
argument_list|,
name|RedisConstants
operator|.
name|KEYS
argument_list|,
name|keys
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|zSetOperations
argument_list|)
operator|.
name|intersectAndStore
argument_list|(
literal|"key"
argument_list|,
name|keys
argument_list|,
literal|"destination"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteZRANGE ()
specifier|public
name|void
name|shouldExecuteZRANGE
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|keys
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|keys
operator|.
name|add
argument_list|(
literal|"key2"
argument_list|)
expr_stmt|;
name|keys
operator|.
name|add
argument_list|(
literal|"key3"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|zSetOperations
operator|.
name|range
argument_list|(
name|anyString
argument_list|()
argument_list|,
name|anyLong
argument_list|()
argument_list|,
name|anyLong
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|keys
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|sendHeaders
argument_list|(
name|RedisConstants
operator|.
name|COMMAND
argument_list|,
literal|"ZRANGE"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key"
argument_list|,
name|RedisConstants
operator|.
name|START
argument_list|,
literal|1
argument_list|,
name|RedisConstants
operator|.
name|END
argument_list|,
literal|3
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|zSetOperations
argument_list|)
operator|.
name|range
argument_list|(
literal|"key"
argument_list|,
literal|1
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|keys
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteZRANGEWithScores ()
specifier|public
name|void
name|shouldExecuteZRANGEWithScores
parameter_list|()
block|{
name|when
argument_list|(
name|zSetOperations
operator|.
name|rangeWithScores
argument_list|(
name|anyString
argument_list|()
argument_list|,
name|anyLong
argument_list|()
argument_list|,
name|anyLong
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|sendHeaders
argument_list|(
name|RedisConstants
operator|.
name|COMMAND
argument_list|,
literal|"ZRANGE"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key"
argument_list|,
name|RedisConstants
operator|.
name|WITHSCORE
argument_list|,
literal|true
argument_list|,
name|RedisConstants
operator|.
name|START
argument_list|,
literal|1
argument_list|,
name|RedisConstants
operator|.
name|END
argument_list|,
literal|3
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|zSetOperations
argument_list|)
operator|.
name|rangeWithScores
argument_list|(
literal|"key"
argument_list|,
literal|1
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteZRANGEBYSCORE ()
specifier|public
name|void
name|shouldExecuteZRANGEBYSCORE
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|keys
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|keys
operator|.
name|add
argument_list|(
literal|"key2"
argument_list|)
expr_stmt|;
name|keys
operator|.
name|add
argument_list|(
literal|"key3"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|zSetOperations
operator|.
name|rangeByScore
argument_list|(
name|anyString
argument_list|()
argument_list|,
name|anyDouble
argument_list|()
argument_list|,
name|anyDouble
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|keys
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|sendHeaders
argument_list|(
name|RedisConstants
operator|.
name|COMMAND
argument_list|,
literal|"ZRANGEBYSCORE"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key"
argument_list|,
name|RedisConstants
operator|.
name|MIN
argument_list|,
literal|1.0
argument_list|,
name|RedisConstants
operator|.
name|MAX
argument_list|,
literal|2.0
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|zSetOperations
argument_list|)
operator|.
name|rangeByScore
argument_list|(
literal|"key"
argument_list|,
literal|1.0
argument_list|,
literal|2.0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|keys
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteZRANK ()
specifier|public
name|void
name|shouldExecuteZRANK
parameter_list|()
block|{
name|when
argument_list|(
name|zSetOperations
operator|.
name|rank
argument_list|(
name|anyString
argument_list|()
argument_list|,
name|anyString
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|1L
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|sendHeaders
argument_list|(
name|RedisConstants
operator|.
name|COMMAND
argument_list|,
literal|"ZRANK"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key"
argument_list|,
name|RedisConstants
operator|.
name|VALUE
argument_list|,
literal|"value"
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|zSetOperations
argument_list|)
operator|.
name|rank
argument_list|(
literal|"key"
argument_list|,
literal|"value"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1L
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteZREM ()
specifier|public
name|void
name|shouldExecuteZREM
parameter_list|()
block|{
name|when
argument_list|(
name|zSetOperations
operator|.
name|remove
argument_list|(
name|anyString
argument_list|()
argument_list|,
name|anyString
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|sendHeaders
argument_list|(
name|RedisConstants
operator|.
name|COMMAND
argument_list|,
literal|"ZREM"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key"
argument_list|,
name|RedisConstants
operator|.
name|VALUE
argument_list|,
literal|"value"
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|zSetOperations
argument_list|)
operator|.
name|remove
argument_list|(
literal|"key"
argument_list|,
literal|"value"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1L
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteZREMRANGEBYRANK ()
specifier|public
name|void
name|shouldExecuteZREMRANGEBYRANK
parameter_list|()
block|{
name|sendHeaders
argument_list|(
name|RedisConstants
operator|.
name|COMMAND
argument_list|,
literal|"ZREMRANGEBYRANK"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key"
argument_list|,
name|RedisConstants
operator|.
name|START
argument_list|,
literal|1
argument_list|,
name|RedisConstants
operator|.
name|END
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|zSetOperations
argument_list|)
operator|.
name|removeRange
argument_list|(
literal|"key"
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteZREMRANGEBYSCORE ()
specifier|public
name|void
name|shouldExecuteZREMRANGEBYSCORE
parameter_list|()
block|{
name|sendHeaders
argument_list|(
name|RedisConstants
operator|.
name|COMMAND
argument_list|,
literal|"ZREMRANGEBYSCORE"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key"
argument_list|,
name|RedisConstants
operator|.
name|START
argument_list|,
literal|1
argument_list|,
name|RedisConstants
operator|.
name|END
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|zSetOperations
argument_list|)
operator|.
name|removeRangeByScore
argument_list|(
literal|"key"
argument_list|,
literal|1.0
argument_list|,
literal|2.0
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteZREVRANGE ()
specifier|public
name|void
name|shouldExecuteZREVRANGE
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|keys
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|keys
operator|.
name|add
argument_list|(
literal|"key2"
argument_list|)
expr_stmt|;
name|keys
operator|.
name|add
argument_list|(
literal|"key3"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|zSetOperations
operator|.
name|reverseRange
argument_list|(
name|anyString
argument_list|()
argument_list|,
name|anyLong
argument_list|()
argument_list|,
name|anyLong
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|keys
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|sendHeaders
argument_list|(
name|RedisConstants
operator|.
name|COMMAND
argument_list|,
literal|"ZREVRANGE"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key"
argument_list|,
name|RedisConstants
operator|.
name|START
argument_list|,
literal|1
argument_list|,
name|RedisConstants
operator|.
name|END
argument_list|,
literal|3
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|zSetOperations
argument_list|)
operator|.
name|reverseRange
argument_list|(
literal|"key"
argument_list|,
literal|1
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|keys
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteZREVRANGEWithScores ()
specifier|public
name|void
name|shouldExecuteZREVRANGEWithScores
parameter_list|()
block|{
name|when
argument_list|(
name|zSetOperations
operator|.
name|reverseRangeWithScores
argument_list|(
name|anyString
argument_list|()
argument_list|,
name|anyLong
argument_list|()
argument_list|,
name|anyLong
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|sendHeaders
argument_list|(
name|RedisConstants
operator|.
name|COMMAND
argument_list|,
literal|"ZREVRANGE"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key"
argument_list|,
name|RedisConstants
operator|.
name|WITHSCORE
argument_list|,
literal|true
argument_list|,
name|RedisConstants
operator|.
name|START
argument_list|,
literal|1
argument_list|,
name|RedisConstants
operator|.
name|END
argument_list|,
literal|3
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|zSetOperations
argument_list|)
operator|.
name|reverseRangeWithScores
argument_list|(
literal|"key"
argument_list|,
literal|1
argument_list|,
literal|3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteZREVRANGEBYSCORE ()
specifier|public
name|void
name|shouldExecuteZREVRANGEBYSCORE
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|keys
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|keys
operator|.
name|add
argument_list|(
literal|"key2"
argument_list|)
expr_stmt|;
name|keys
operator|.
name|add
argument_list|(
literal|"key3"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|zSetOperations
operator|.
name|reverseRangeByScore
argument_list|(
name|anyString
argument_list|()
argument_list|,
name|anyDouble
argument_list|()
argument_list|,
name|anyDouble
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|keys
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|sendHeaders
argument_list|(
name|RedisConstants
operator|.
name|COMMAND
argument_list|,
literal|"ZREVRANGEBYSCORE"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key"
argument_list|,
name|RedisConstants
operator|.
name|MIN
argument_list|,
literal|1.0
argument_list|,
name|RedisConstants
operator|.
name|MAX
argument_list|,
literal|2.0
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|zSetOperations
argument_list|)
operator|.
name|reverseRangeByScore
argument_list|(
literal|"key"
argument_list|,
literal|1.0
argument_list|,
literal|2.0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|keys
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteZREVRANK ()
specifier|public
name|void
name|shouldExecuteZREVRANK
parameter_list|()
block|{
name|when
argument_list|(
name|zSetOperations
operator|.
name|reverseRank
argument_list|(
name|anyString
argument_list|()
argument_list|,
name|anyString
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|1L
argument_list|)
expr_stmt|;
name|Object
name|result
init|=
name|sendHeaders
argument_list|(
name|RedisConstants
operator|.
name|COMMAND
argument_list|,
literal|"ZREVRANK"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key"
argument_list|,
name|RedisConstants
operator|.
name|VALUE
argument_list|,
literal|"value"
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|zSetOperations
argument_list|)
operator|.
name|reverseRank
argument_list|(
literal|"key"
argument_list|,
literal|"value"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1L
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteZUNIONSTORE ()
specifier|public
name|void
name|shouldExecuteZUNIONSTORE
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|keys
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|keys
operator|.
name|add
argument_list|(
literal|"key2"
argument_list|)
expr_stmt|;
name|keys
operator|.
name|add
argument_list|(
literal|"key3"
argument_list|)
expr_stmt|;
name|sendHeaders
argument_list|(
name|RedisConstants
operator|.
name|COMMAND
argument_list|,
literal|"ZUNIONSTORE"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key"
argument_list|,
name|RedisConstants
operator|.
name|DESTINATION
argument_list|,
literal|"destination"
argument_list|,
name|RedisConstants
operator|.
name|KEYS
argument_list|,
name|keys
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|zSetOperations
argument_list|)
operator|.
name|unionAndStore
argument_list|(
literal|"key"
argument_list|,
name|keys
argument_list|,
literal|"destination"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

