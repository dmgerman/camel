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
name|RedisTemplate
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

begin_class
annotation|@
name|RunWith
argument_list|(
name|MockitoJUnitRunner
operator|.
name|class
argument_list|)
DECL|class|RedisTransactionTest
specifier|public
class|class
name|RedisTransactionTest
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
name|?
argument_list|>
name|redisTemplate
decl_stmt|;
annotation|@
name|Test
DECL|method|shouldExecuteMULTI ()
specifier|public
name|void
name|shouldExecuteMULTI
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
literal|"MULTI"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|redisTemplate
argument_list|)
operator|.
name|multi
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteDISCARD ()
specifier|public
name|void
name|shouldExecuteDISCARD
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
literal|"DISCARD"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|redisTemplate
argument_list|)
operator|.
name|discard
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteEXEC ()
specifier|public
name|void
name|shouldExecuteEXEC
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
literal|"EXEC"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|redisTemplate
argument_list|)
operator|.
name|exec
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteUNWATCH ()
specifier|public
name|void
name|shouldExecuteUNWATCH
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
literal|"UNWATCH"
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|redisTemplate
argument_list|)
operator|.
name|unwatch
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|shouldExecuteWATCH ()
specifier|public
name|void
name|shouldExecuteWATCH
parameter_list|()
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|keys
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|keys
operator|.
name|add
argument_list|(
literal|"key"
argument_list|)
expr_stmt|;
name|sendHeaders
argument_list|(
name|RedisConstants
operator|.
name|COMMAND
argument_list|,
literal|"WATCH"
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
name|watch
argument_list|(
name|keys
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

