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
name|ArrayList
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
name|List
import|;
end_import

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
name|spi
operator|.
name|Registry
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
name|SimpleRegistry
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
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|junit
operator|.
name|MockitoJUnitRunner
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
name|ValueOperations
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
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
name|ArgumentMatchers
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
annotation|@
name|RunWith
argument_list|(
name|MockitoJUnitRunner
operator|.
name|class
argument_list|)
DECL|class|RedisStringTest
specifier|public
class|class
name|RedisStringTest
extends|extends
name|RedisTestSupport
block|{
annotation|@
name|Mock
DECL|field|valueOperations
specifier|private
name|ValueOperations
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|valueOperations
decl_stmt|;
annotation|@
name|Override
DECL|method|createCamelRegistry ()
specifier|protected
name|Registry
name|createCamelRegistry
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|redisTemplate
operator|.
name|opsForValue
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|valueOperations
argument_list|)
expr_stmt|;
name|Registry
name|registry
init|=
operator|new
name|SimpleRegistry
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
name|Test
DECL|method|shouldExecuteSET ()
specifier|public
name|void
name|shouldExecuteSET
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
literal|"key"
argument_list|,
name|RedisConstants
operator|.
name|VALUE
argument_list|,
literal|"value"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|valueOperations
argument_list|)
operator|.
name|set
argument_list|(
literal|"key"
argument_list|,
literal|"value"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteSETNX ()
specifier|public
name|void
name|shouldExecuteSETNX
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
literal|"SETNX"
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
expr_stmt|;
name|verify
argument_list|(
name|valueOperations
argument_list|)
operator|.
name|setIfAbsent
argument_list|(
literal|"key"
argument_list|,
literal|"value"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteSETEX ()
specifier|public
name|void
name|shouldExecuteSETEX
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
literal|"SETEX"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key"
argument_list|,
name|RedisConstants
operator|.
name|TIMEOUT
argument_list|,
literal|"10"
argument_list|,
name|RedisConstants
operator|.
name|VALUE
argument_list|,
literal|"value"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|valueOperations
argument_list|)
operator|.
name|set
argument_list|(
literal|"key"
argument_list|,
literal|"value"
argument_list|,
literal|10
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteSETRANGE ()
specifier|public
name|void
name|shouldExecuteSETRANGE
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
literal|"SETRANGE"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key"
argument_list|,
name|RedisConstants
operator|.
name|OFFSET
argument_list|,
literal|"10"
argument_list|,
name|RedisConstants
operator|.
name|VALUE
argument_list|,
literal|"value"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|valueOperations
argument_list|)
operator|.
name|set
argument_list|(
literal|"key"
argument_list|,
literal|"value"
argument_list|,
literal|10
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteGETRANGE ()
specifier|public
name|void
name|shouldExecuteGETRANGE
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|valueOperations
operator|.
name|get
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
literal|"test"
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
literal|"GETRANGE"
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
literal|"2"
argument_list|,
name|RedisConstants
operator|.
name|END
argument_list|,
literal|"4"
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|valueOperations
argument_list|)
operator|.
name|get
argument_list|(
literal|"key"
argument_list|,
literal|2
argument_list|,
literal|4
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteSETBIT ()
specifier|public
name|void
name|shouldExecuteSETBIT
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
literal|"SETBIT"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key"
argument_list|,
name|RedisConstants
operator|.
name|OFFSET
argument_list|,
literal|"10"
argument_list|,
name|RedisConstants
operator|.
name|VALUE
argument_list|,
literal|"0"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|redisTemplate
argument_list|)
operator|.
name|execute
argument_list|(
name|ArgumentMatchers
operator|.
expr|<
name|RedisCallback
argument_list|<
name|String
argument_list|>
operator|>
name|any
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteGETBIT ()
specifier|public
name|void
name|shouldExecuteGETBIT
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|redisTemplate
operator|.
name|execute
argument_list|(
name|ArgumentMatchers
operator|.
expr|<
name|RedisCallback
argument_list|<
name|Boolean
argument_list|>
operator|>
name|any
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
literal|"GETBIT"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key"
argument_list|,
name|RedisConstants
operator|.
name|OFFSET
argument_list|,
literal|"2"
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|redisTemplate
argument_list|)
operator|.
name|execute
argument_list|(
name|ArgumentMatchers
operator|.
expr|<
name|RedisCallback
argument_list|<
name|String
argument_list|>
operator|>
name|any
argument_list|()
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
DECL|method|shouldExecuteGET ()
specifier|public
name|void
name|shouldExecuteGET
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|valueOperations
operator|.
name|get
argument_list|(
literal|"key"
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
literal|"GET"
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
name|valueOperations
argument_list|)
operator|.
name|get
argument_list|(
literal|"key"
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
DECL|method|shouldExecuteAPPEND ()
specifier|public
name|void
name|shouldExecuteAPPEND
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|valueOperations
operator|.
name|append
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
literal|5
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
literal|"APPEND"
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
name|valueOperations
argument_list|)
operator|.
name|append
argument_list|(
literal|"key"
argument_list|,
literal|"value"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteDECR ()
specifier|public
name|void
name|shouldExecuteDECR
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|valueOperations
operator|.
name|increment
argument_list|(
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
literal|"DECR"
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
name|valueOperations
argument_list|)
operator|.
name|increment
argument_list|(
literal|"key"
argument_list|,
operator|-
literal|1
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
DECL|method|shouldExecuteDECRBY ()
specifier|public
name|void
name|shouldExecuteDECRBY
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|valueOperations
operator|.
name|increment
argument_list|(
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
literal|"DECRBY"
argument_list|,
name|RedisConstants
operator|.
name|VALUE
argument_list|,
literal|"2"
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
name|valueOperations
argument_list|)
operator|.
name|increment
argument_list|(
literal|"key"
argument_list|,
operator|-
literal|2
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
DECL|method|shouldExecuteINCR ()
specifier|public
name|void
name|shouldExecuteINCR
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|valueOperations
operator|.
name|increment
argument_list|(
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
literal|"INCR"
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
name|valueOperations
argument_list|)
operator|.
name|increment
argument_list|(
literal|"key"
argument_list|,
literal|1
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
DECL|method|shouldExecuteINCRBY ()
specifier|public
name|void
name|shouldExecuteINCRBY
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|valueOperations
operator|.
name|increment
argument_list|(
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
literal|"INCRBY"
argument_list|,
name|RedisConstants
operator|.
name|VALUE
argument_list|,
literal|"2"
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
name|valueOperations
argument_list|)
operator|.
name|increment
argument_list|(
literal|"key"
argument_list|,
literal|2
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
DECL|method|shouldExecuteSTRLEN ()
specifier|public
name|void
name|shouldExecuteSTRLEN
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|valueOperations
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
literal|5L
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
literal|"STRLEN"
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
name|valueOperations
argument_list|)
operator|.
name|size
argument_list|(
literal|"key"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5L
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteMGET ()
specifier|public
name|void
name|shouldExecuteMGET
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
argument_list|<>
argument_list|()
decl_stmt|;
name|fields
operator|.
name|add
argument_list|(
literal|"field1"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|values
operator|.
name|add
argument_list|(
literal|"value1"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|valueOperations
operator|.
name|multiGet
argument_list|(
name|fields
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
literal|"MGET"
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
name|valueOperations
argument_list|)
operator|.
name|multiGet
argument_list|(
name|fields
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
DECL|method|shouldExecuteMSET ()
specifier|public
name|void
name|shouldExecuteMSET
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
init|=
operator|new
name|HashMap
argument_list|<>
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
name|sendHeaders
argument_list|(
name|RedisConstants
operator|.
name|COMMAND
argument_list|,
literal|"MSET"
argument_list|,
name|RedisConstants
operator|.
name|VALUES
argument_list|,
name|values
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|valueOperations
argument_list|)
operator|.
name|multiSet
argument_list|(
name|values
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteMSETNX ()
specifier|public
name|void
name|shouldExecuteMSETNX
parameter_list|()
throws|throws
name|Exception
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
init|=
operator|new
name|HashMap
argument_list|<>
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
name|sendHeaders
argument_list|(
name|RedisConstants
operator|.
name|COMMAND
argument_list|,
literal|"MSETNX"
argument_list|,
name|RedisConstants
operator|.
name|VALUES
argument_list|,
name|values
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|valueOperations
argument_list|)
operator|.
name|multiSetIfAbsent
argument_list|(
name|values
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteGETSET ()
specifier|public
name|void
name|shouldExecuteGETSET
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|valueOperations
operator|.
name|getAndSet
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
literal|"old value"
argument_list|)
expr_stmt|;
name|String
name|value
init|=
literal|"new value"
decl_stmt|;
name|Object
name|result
init|=
name|sendHeaders
argument_list|(
name|RedisConstants
operator|.
name|COMMAND
argument_list|,
literal|"GETSET"
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
name|value
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|valueOperations
argument_list|)
operator|.
name|getAndSet
argument_list|(
literal|"key"
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"old value"
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

