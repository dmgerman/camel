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
name|SetOperations
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
DECL|class|RedisSetTest
specifier|public
class|class
name|RedisSetTest
extends|extends
name|RedisTestSupport
block|{
DECL|field|redisTemplate
specifier|private
name|RedisTemplate
name|redisTemplate
decl_stmt|;
DECL|field|setOperations
specifier|private
name|SetOperations
name|setOperations
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
name|opsForSet
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|setOperations
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
name|setOperations
operator|=
name|mock
argument_list|(
name|SetOperations
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
DECL|method|shouldExecuteSADD ()
specifier|public
name|void
name|shouldExecuteSADD
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|setOperations
operator|.
name|add
argument_list|(
name|anyString
argument_list|()
argument_list|,
name|anyObject
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
literal|"SADD"
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
name|setOperations
argument_list|)
operator|.
name|add
argument_list|(
literal|"key"
argument_list|,
literal|"value"
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
DECL|method|shouldExecuteSCARD ()
specifier|public
name|void
name|shouldExecuteSCARD
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|setOperations
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
literal|"SCARD"
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
name|setOperations
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
DECL|method|shouldExecuteSDIFF ()
specifier|public
name|void
name|shouldExecuteSDIFF
parameter_list|()
throws|throws
name|Exception
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|difference
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|difference
operator|.
name|add
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|difference
operator|.
name|add
argument_list|(
literal|"b"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|setOperations
operator|.
name|difference
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
name|difference
argument_list|)
expr_stmt|;
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
name|Object
name|result
init|=
name|sendHeaders
argument_list|(
name|RedisConstants
operator|.
name|COMMAND
argument_list|,
literal|"SDIFF"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key"
argument_list|,
name|RedisConstants
operator|.
name|KEYS
argument_list|,
name|keys
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|setOperations
argument_list|)
operator|.
name|difference
argument_list|(
literal|"key"
argument_list|,
name|keys
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|difference
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteSDIFFSTORE ()
specifier|public
name|void
name|shouldExecuteSDIFFSTORE
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
literal|"SDIFFSTORE"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key"
argument_list|,
name|RedisConstants
operator|.
name|KEYS
argument_list|,
name|keys
argument_list|,
name|RedisConstants
operator|.
name|DESTINATION
argument_list|,
literal|"destination"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|setOperations
argument_list|)
operator|.
name|differenceAndStore
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
DECL|method|shouldExecuteSINTER ()
specifier|public
name|void
name|shouldExecuteSINTER
parameter_list|()
throws|throws
name|Exception
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|difference
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|difference
operator|.
name|add
argument_list|(
literal|"a"
argument_list|)
expr_stmt|;
name|difference
operator|.
name|add
argument_list|(
literal|"b"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|setOperations
operator|.
name|intersect
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
name|difference
argument_list|)
expr_stmt|;
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
name|Object
name|result
init|=
name|sendHeaders
argument_list|(
name|RedisConstants
operator|.
name|COMMAND
argument_list|,
literal|"SINTER"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key"
argument_list|,
name|RedisConstants
operator|.
name|KEYS
argument_list|,
name|keys
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|setOperations
argument_list|)
operator|.
name|intersect
argument_list|(
literal|"key"
argument_list|,
name|keys
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|difference
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteSINTERSTORE ()
specifier|public
name|void
name|shouldExecuteSINTERSTORE
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
literal|"SINTERSTORE"
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
name|setOperations
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
DECL|method|shouldExecuteSISMEMBER ()
specifier|public
name|void
name|shouldExecuteSISMEMBER
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|setOperations
operator|.
name|isMember
argument_list|(
name|anyString
argument_list|()
argument_list|,
name|anyObject
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
literal|"SISMEMBER"
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
literal|"set"
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|setOperations
argument_list|)
operator|.
name|isMember
argument_list|(
literal|"key"
argument_list|,
literal|"set"
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
DECL|method|shouldExecuteSMEMBERS ()
specifier|public
name|void
name|shouldExecuteSMEMBERS
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
name|setOperations
operator|.
name|members
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
literal|"SMEMBERS"
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
name|setOperations
argument_list|)
operator|.
name|members
argument_list|(
literal|"key"
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
DECL|method|shouldExecuteSMOVE ()
specifier|public
name|void
name|shouldExecuteSMOVE
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
literal|"SMOVE"
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
name|DESTINATION
argument_list|,
literal|"destination"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|setOperations
argument_list|)
operator|.
name|move
argument_list|(
literal|"key"
argument_list|,
literal|"value"
argument_list|,
literal|"destination"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteSPOP ()
specifier|public
name|void
name|shouldExecuteSPOP
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|field
init|=
literal|"value"
decl_stmt|;
name|when
argument_list|(
name|setOperations
operator|.
name|pop
argument_list|(
name|anyString
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|field
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
literal|"SPOP"
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
name|setOperations
argument_list|)
operator|.
name|pop
argument_list|(
literal|"key"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|field
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteSRANDMEMBER ()
specifier|public
name|void
name|shouldExecuteSRANDMEMBER
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|field
init|=
literal|"value"
decl_stmt|;
name|when
argument_list|(
name|setOperations
operator|.
name|randomMember
argument_list|(
name|anyString
argument_list|()
argument_list|)
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|field
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
literal|"SRANDMEMBER"
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
name|setOperations
argument_list|)
operator|.
name|randomMember
argument_list|(
literal|"key"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|field
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteSREM ()
specifier|public
name|void
name|shouldExecuteSREM
parameter_list|()
throws|throws
name|Exception
block|{
name|when
argument_list|(
name|setOperations
operator|.
name|remove
argument_list|(
name|anyString
argument_list|()
argument_list|,
name|anyObject
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
literal|"SREM"
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
name|setOperations
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
DECL|method|shouldExecuteSUNION ()
specifier|public
name|void
name|shouldExecuteSUNION
parameter_list|()
throws|throws
name|Exception
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|resultKeys
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|resultKeys
operator|.
name|add
argument_list|(
literal|"key2"
argument_list|)
expr_stmt|;
name|resultKeys
operator|.
name|add
argument_list|(
literal|"key3"
argument_list|)
expr_stmt|;
name|when
argument_list|(
name|setOperations
operator|.
name|union
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
name|resultKeys
argument_list|)
expr_stmt|;
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
literal|"key4"
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
literal|"SUNION"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key"
argument_list|,
name|RedisConstants
operator|.
name|KEYS
argument_list|,
name|keys
argument_list|)
decl_stmt|;
name|verify
argument_list|(
name|setOperations
argument_list|)
operator|.
name|union
argument_list|(
literal|"key"
argument_list|,
name|keys
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|resultKeys
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteSUNIONSTORE ()
specifier|public
name|void
name|shouldExecuteSUNIONSTORE
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
literal|"key2"
argument_list|)
expr_stmt|;
name|keys
operator|.
name|add
argument_list|(
literal|"key4"
argument_list|)
expr_stmt|;
name|sendHeaders
argument_list|(
name|RedisConstants
operator|.
name|COMMAND
argument_list|,
literal|"SUNIONSTORE"
argument_list|,
name|RedisConstants
operator|.
name|KEY
argument_list|,
literal|"key"
argument_list|,
name|RedisConstants
operator|.
name|KEYS
argument_list|,
name|keys
argument_list|,
name|RedisConstants
operator|.
name|DESTINATION
argument_list|,
literal|"destination"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|setOperations
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

