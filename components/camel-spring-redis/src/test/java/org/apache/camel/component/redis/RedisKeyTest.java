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
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
name|BindToRegistry
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
name|connection
operator|.
name|DataType
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
name|query
operator|.
name|SortQuery
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
name|any
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
name|anyInt
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
DECL|class|RedisKeyTest
specifier|public
class|class
name|RedisKeyTest
extends|extends
name|RedisTestSupport
block|{
annotation|@
name|Mock
annotation|@
name|BindToRegistry
argument_list|(
literal|"redisTemplate"
argument_list|)
DECL|field|redisTemplate
specifier|private
name|RedisTemplate
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|redisTemplate
decl_stmt|;
annotation|@
name|Test
DECL|method|shouldExecuteDEL ()
specifier|public
name|void
name|shouldExecuteDEL
parameter_list|()
throws|throws
name|Exception
block|{
name|Collection
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
literal|"key1"
argument_list|)
expr_stmt|;
name|keys
operator|.
name|add
argument_list|(
literal|"key2"
argument_list|)
expr_stmt|;
name|sendHeaders
argument_list|(
name|RedisConstants
operator|.
name|COMMAND
argument_list|,
literal|"DEL"
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
name|redisTemplate
argument_list|)
operator|.
name|delete
argument_list|(
name|keys
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteEXISTS ()
specifier|public
name|void
name|shouldExecuteEXISTS
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|redisTemplate
operator|.
name|hasKey
argument_list|(
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
literal|"EXISTS"
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
name|redisTemplate
argument_list|)
operator|.
name|hasKey
argument_list|(
literal|"key"
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
DECL|method|shouldExecuteEXPIRE ()
specifier|public
name|void
name|shouldExecuteEXPIRE
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|redisTemplate
operator|.
name|expire
argument_list|(
name|anyString
argument_list|()
argument_list|,
name|anyLong
argument_list|()
argument_list|,
name|any
argument_list|(
name|TimeUnit
operator|.
name|class
argument_list|)
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
literal|"EXPIRE"
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
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|redisTemplate
argument_list|)
operator|.
name|expire
argument_list|(
literal|"key"
argument_list|,
literal|10L
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
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
DECL|method|shouldExecuteEXPIREAT ()
specifier|public
name|void
name|shouldExecuteEXPIREAT
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|redisTemplate
operator|.
name|expireAt
argument_list|(
name|anyString
argument_list|()
argument_list|,
name|any
argument_list|(
name|Date
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|long
name|unixTime
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|/
literal|1000L
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
literal|"EXPIREAT"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key"
argument_list|,
name|RedisConstants
operator|.
name|TIMESTAMP
argument_list|,
name|unixTime
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|redisTemplate
argument_list|)
operator|.
name|expireAt
argument_list|(
literal|"key"
argument_list|,
operator|new
name|Date
argument_list|(
name|unixTime
operator|*
literal|1000L
argument_list|)
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
DECL|method|shouldExecuteKEYS ()
specifier|public
name|void
name|shouldExecuteKEYS
parameter_list|()
throws|throws
name|Exception
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
literal|"key1"
argument_list|)
expr_stmt|;
name|keys
operator|.
name|add
argument_list|(
literal|"key2"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|redisTemplate
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
literal|"KEYS"
argument_list|,
name|RedisConstants
operator|.
name|PATTERN
argument_list|,
literal|"key*"
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|redisTemplate
argument_list|)
operator|.
name|keys
argument_list|(
literal|"key*"
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
DECL|method|shouldExecuteMOVE ()
specifier|public
name|void
name|shouldExecuteMOVE
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|redisTemplate
operator|.
name|move
argument_list|(
name|anyString
argument_list|()
argument_list|,
name|anyInt
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
literal|"MOVE"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key"
argument_list|,
name|RedisConstants
operator|.
name|DB
argument_list|,
literal|"2"
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|redisTemplate
argument_list|)
operator|.
name|move
argument_list|(
literal|"key"
argument_list|,
literal|2
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
DECL|method|shouldExecutePERSIST ()
specifier|public
name|void
name|shouldExecutePERSIST
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|redisTemplate
operator|.
name|persist
argument_list|(
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
literal|"PERSIST"
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
name|redisTemplate
argument_list|)
operator|.
name|persist
argument_list|(
literal|"key"
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
DECL|method|shouldExecutePEXPIRE ()
specifier|public
name|void
name|shouldExecutePEXPIRE
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|redisTemplate
operator|.
name|expire
argument_list|(
name|anyString
argument_list|()
argument_list|,
name|anyLong
argument_list|()
argument_list|,
name|any
argument_list|(
name|TimeUnit
operator|.
name|class
argument_list|)
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
literal|"PEXPIRE"
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
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|redisTemplate
argument_list|)
operator|.
name|expire
argument_list|(
literal|"key"
argument_list|,
literal|10L
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
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
DECL|method|shouldExecutePEXPIREAT ()
specifier|public
name|void
name|shouldExecutePEXPIREAT
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|redisTemplate
operator|.
name|expireAt
argument_list|(
name|anyString
argument_list|()
argument_list|,
name|any
argument_list|(
name|Date
operator|.
name|class
argument_list|)
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|long
name|millis
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
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
literal|"PEXPIREAT"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key"
argument_list|,
name|RedisConstants
operator|.
name|TIMESTAMP
argument_list|,
name|millis
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|redisTemplate
argument_list|)
operator|.
name|expireAt
argument_list|(
literal|"key"
argument_list|,
operator|new
name|Date
argument_list|(
name|millis
argument_list|)
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
DECL|method|shouldExecuteRANDOMKEY ()
specifier|public
name|void
name|shouldExecuteRANDOMKEY
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|redisTemplate
operator|.
name|randomKey
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
literal|"key"
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
literal|"RANDOMKEY"
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|redisTemplate
argument_list|)
operator|.
name|randomKey
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"key"
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteRENAME ()
specifier|public
name|void
name|shouldExecuteRENAME
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
literal|"RENAME"
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
literal|"newkey"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|redisTemplate
argument_list|)
operator|.
name|rename
argument_list|(
literal|"key"
argument_list|,
literal|"newkey"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteRENAMENX ()
specifier|public
name|void
name|shouldExecuteRENAMENX
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|redisTemplate
operator|.
name|renameIfAbsent
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
literal|"RENAMENX"
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
literal|"newkey"
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|redisTemplate
argument_list|)
operator|.
name|renameIfAbsent
argument_list|(
literal|"key"
argument_list|,
literal|"newkey"
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
DECL|method|shouldExecuteSORT ()
specifier|public
name|void
name|shouldExecuteSORT
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Integer
argument_list|>
name|list
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|redisTemplate
operator|.
name|sort
argument_list|(
name|ArgumentMatchers
operator|.
expr|<
name|SortQuery
argument_list|<
name|String
argument_list|>
operator|>
name|any
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|list
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
literal|"SORT"
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
name|redisTemplate
argument_list|)
operator|.
name|sort
argument_list|(
name|ArgumentMatchers
operator|.
expr|<
name|SortQuery
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
name|list
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteTTL ()
specifier|public
name|void
name|shouldExecuteTTL
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|redisTemplate
operator|.
name|getExpire
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
literal|"TTL"
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
name|redisTemplate
argument_list|)
operator|.
name|getExpire
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
DECL|method|shouldExecuteTYPE ()
specifier|public
name|void
name|shouldExecuteTYPE
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|redisTemplate
operator|.
name|type
argument_list|(
name|anyString
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|DataType
operator|.
name|STRING
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
literal|"TYPE"
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
name|redisTemplate
argument_list|)
operator|.
name|type
argument_list|(
literal|"key"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|DataType
operator|.
name|STRING
operator|.
name|toString
argument_list|()
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

