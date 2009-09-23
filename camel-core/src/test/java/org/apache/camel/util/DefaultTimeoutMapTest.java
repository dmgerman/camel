begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ScheduledExecutorService
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|util
operator|.
name|concurrent
operator|.
name|ExecutorServiceHelper
import|;
end_import

begin_comment
comment|/**  * @version $Revision$  */
end_comment

begin_class
DECL|class|DefaultTimeoutMapTest
specifier|public
class|class
name|DefaultTimeoutMapTest
extends|extends
name|TestCase
block|{
DECL|method|testDefaultTimeoutMap ()
specifier|public
name|void
name|testDefaultTimeoutMap
parameter_list|()
block|{
name|DefaultTimeoutMap
name|map
init|=
operator|new
name|DefaultTimeoutMap
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|currentTime
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testDefaultTimeoutMapPurge ()
specifier|public
name|void
name|testDefaultTimeoutMapPurge
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultTimeoutMap
name|map
init|=
operator|new
name|DefaultTimeoutMap
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|currentTime
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"A"
argument_list|,
literal|123
argument_list|,
literal|500
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
comment|// will purge and remove old entries
name|map
operator|.
name|purge
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testDefaultTimeoutMapGetPurge ()
specifier|public
name|void
name|testDefaultTimeoutMapGetPurge
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultTimeoutMap
name|map
init|=
operator|new
name|DefaultTimeoutMap
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|currentTime
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"A"
argument_list|,
literal|123
argument_list|,
literal|500
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"A"
argument_list|)
argument_list|)
expr_stmt|;
comment|// will purge and remove old entries
name|map
operator|.
name|purge
argument_list|()
expr_stmt|;
comment|// but we just used get to get it so its refreshed
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testDefaultTimeoutMapGetRemove ()
specifier|public
name|void
name|testDefaultTimeoutMapGetRemove
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultTimeoutMap
name|map
init|=
operator|new
name|DefaultTimeoutMap
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|currentTime
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"A"
argument_list|,
literal|123
argument_list|,
literal|500
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"A"
argument_list|)
argument_list|)
expr_stmt|;
name|map
operator|.
name|remove
argument_list|(
literal|"A"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|null
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"A"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
DECL|method|testDefaultTimeoutMapGetKeys ()
specifier|public
name|void
name|testDefaultTimeoutMapGetKeys
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultTimeoutMap
name|map
init|=
operator|new
name|DefaultTimeoutMap
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|map
operator|.
name|currentTime
argument_list|()
operator|>
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"A"
argument_list|,
literal|123
argument_list|,
literal|500
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"B"
argument_list|,
literal|456
argument_list|,
literal|500
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Object
index|[]
name|keys
init|=
name|map
operator|.
name|getKeys
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|keys
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|keys
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
DECL|method|testExecutor ()
specifier|public
name|void
name|testExecutor
parameter_list|()
throws|throws
name|Exception
block|{
name|ScheduledExecutorService
name|e
init|=
name|ExecutorServiceHelper
operator|.
name|newScheduledThreadPool
argument_list|(
literal|1
argument_list|,
literal|"foo"
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|DefaultTimeoutMap
name|map
init|=
operator|new
name|DefaultTimeoutMap
argument_list|(
name|e
argument_list|,
literal|500
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|500
argument_list|,
name|map
operator|.
name|getPurgePollTime
argument_list|()
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"A"
argument_list|,
literal|123
argument_list|,
literal|1000
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
comment|// should be gone now
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|map
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertSame
argument_list|(
name|e
argument_list|,
name|map
operator|.
name|getExecutor
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

