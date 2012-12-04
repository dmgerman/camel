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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

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
name|List
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
name|HashOperations
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
import|import static
name|org
operator|.
name|mockito
operator|.
name|Matchers
operator|.
name|anyCollection
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
DECL|class|RedisHashTest
specifier|public
class|class
name|RedisHashTest
extends|extends
name|RedisTestSupport
block|{
DECL|field|redisTemplate
specifier|private
name|RedisTemplate
name|redisTemplate
decl_stmt|;
DECL|field|hashOperations
specifier|private
name|HashOperations
name|hashOperations
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
name|opsForHash
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|hashOperations
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
name|hashOperations
operator|=
name|mock
argument_list|(
name|HashOperations
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
DECL|method|shouldExecuteHDEL ()
specifier|public
name|void
name|shouldExecuteHDEL
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|result
init|=
name|sendHeaders
argument_list|(
name|RedisConstants
operator|.
name|COMMAND
argument_list|,
literal|"HDEL"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key"
argument_list|,
name|RedisConstants
operator|.
name|FIELD
argument_list|,
literal|"field"
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|hashOperations
argument_list|)
operator|.
name|delete
argument_list|(
literal|"key"
argument_list|,
literal|"field"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteHEXISTS ()
specifier|public
name|void
name|shouldExecuteHEXISTS
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|hashOperations
operator|.
name|hasKey
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
literal|true
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
literal|"HEXISTS"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key"
argument_list|,
name|RedisConstants
operator|.
name|FIELD
argument_list|,
literal|"field"
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|hashOperations
argument_list|)
operator|.
name|hasKey
argument_list|(
literal|"key"
argument_list|,
literal|"field"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteHINCRBY ()
specifier|public
name|void
name|shouldExecuteHINCRBY
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|hashOperations
operator|.
name|increment
argument_list|(
name|anyString
argument_list|()
argument_list|,
name|anyString
argument_list|()
argument_list|,
name|anyLong
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
literal|"HINCRBY"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key"
argument_list|,
name|RedisConstants
operator|.
name|FIELD
argument_list|,
literal|"field"
argument_list|,
name|RedisConstants
operator|.
name|VALUE
argument_list|,
literal|"1"
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|hashOperations
argument_list|)
operator|.
name|increment
argument_list|(
literal|"key"
argument_list|,
literal|"field"
argument_list|,
literal|1L
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
DECL|method|shouldExecuteHKEYS ()
specifier|public
name|void
name|shouldExecuteHKEYS
parameter_list|()
throws|throws
name|Exception
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|fields
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"field1, field2"
block|}
argument_list|)
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|hashOperations
operator|.
name|keys
argument_list|(
name|anyString
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|fields
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
literal|"HKEYS"
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
name|hashOperations
argument_list|)
operator|.
name|keys
argument_list|(
literal|"key"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|fields
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteHMSET ()
specifier|public
name|void
name|shouldExecuteHMSET
parameter_list|()
throws|throws
name|Exception
block|{
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|values
operator|.
name|put
argument_list|(
literal|"field1"
argument_list|,
literal|"value1"
argument_list|)
expr_stmt|;
name|values
operator|.
name|put
argument_list|(
literal|"field2"
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
name|COMMAND
argument_list|,
literal|"HMSET"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key"
argument_list|,
name|RedisConstants
operator|.
name|VALUES
argument_list|,
name|values
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|hashOperations
argument_list|)
operator|.
name|putAll
argument_list|(
literal|"key"
argument_list|,
name|values
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteHVALS ()
specifier|public
name|void
name|shouldExecuteHVALS
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|values
operator|.
name|add
argument_list|(
literal|"val1"
argument_list|)
expr_stmt|;
name|values
operator|.
name|add
argument_list|(
literal|"val2"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|hashOperations
operator|.
name|values
argument_list|(
name|anyString
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|values
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
literal|"HVALS"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key"
argument_list|,
name|RedisConstants
operator|.
name|VALUES
argument_list|,
name|values
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|hashOperations
argument_list|)
operator|.
name|values
argument_list|(
literal|"key"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|values
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteHLEN ()
specifier|public
name|void
name|shouldExecuteHLEN
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|hashOperations
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
literal|"HLEN"
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
name|hashOperations
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
DECL|method|shouldSetHashValue ()
specifier|public
name|void
name|shouldSetHashValue
parameter_list|()
throws|throws
name|Exception
block|{
name|Object
name|result
init|=
name|sendHeaders
argument_list|(
name|RedisConstants
operator|.
name|COMMAND
argument_list|,
literal|"HSET"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key"
argument_list|,
name|RedisConstants
operator|.
name|FIELD
argument_list|,
literal|"field"
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
name|hashOperations
argument_list|)
operator|.
name|put
argument_list|(
literal|"key"
argument_list|,
literal|"field"
argument_list|,
literal|"value"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteHSETNX ()
specifier|public
name|void
name|shouldExecuteHSETNX
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|hashOperations
operator|.
name|putIfAbsent
argument_list|(
name|anyString
argument_list|()
argument_list|,
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
literal|true
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
literal|"HSETNX"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key"
argument_list|,
name|RedisConstants
operator|.
name|FIELD
argument_list|,
literal|"field"
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
name|hashOperations
argument_list|)
operator|.
name|putIfAbsent
argument_list|(
literal|"key"
argument_list|,
literal|"field"
argument_list|,
literal|"value"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteHGET ()
specifier|public
name|void
name|shouldExecuteHGET
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|hashOperations
operator|.
name|get
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
name|COMMAND
argument_list|,
literal|"HGET"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key"
argument_list|,
name|RedisConstants
operator|.
name|FIELD
argument_list|,
literal|"field"
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|hashOperations
argument_list|)
operator|.
name|get
argument_list|(
literal|"key"
argument_list|,
literal|"field"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"value"
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteHGETALL ()
specifier|public
name|void
name|shouldExecuteHGETALL
parameter_list|()
throws|throws
name|Exception
block|{
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|values
operator|.
name|put
argument_list|(
literal|"field1"
argument_list|,
literal|"valu1"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|hashOperations
operator|.
name|entries
argument_list|(
name|anyString
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|values
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
literal|"HGETALL"
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
name|hashOperations
argument_list|)
operator|.
name|entries
argument_list|(
literal|"key"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|values
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteHMGET ()
specifier|public
name|void
name|shouldExecuteHMGET
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|fields
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|fields
operator|.
name|add
argument_list|(
literal|"field1"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|hashOperations
operator|.
name|multiGet
argument_list|(
name|anyString
argument_list|()
argument_list|,
name|anyCollection
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|fields
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
literal|"HMGET"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key"
argument_list|,
name|RedisConstants
operator|.
name|FIELDS
argument_list|,
name|fields
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|hashOperations
argument_list|)
operator|.
name|multiGet
argument_list|(
literal|"key"
argument_list|,
name|fields
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|fields
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

