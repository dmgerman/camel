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
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ScheduledThreadPoolExecutor
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
name|builder
operator|.
name|ThreadPoolBuilder
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
name|DefaultCamelContext
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
name|ThreadPoolProfile
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
DECL|class|DefaultTimeoutMapTest
specifier|public
class|class
name|DefaultTimeoutMapTest
extends|extends
name|TestCase
block|{
DECL|field|executor
specifier|private
name|ScheduledExecutorService
name|executor
init|=
operator|new
name|ScheduledThreadPoolExecutor
argument_list|(
literal|1
argument_list|)
decl_stmt|;
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
argument_list|(
name|executor
argument_list|)
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
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
operator|new
name|DefaultTimeoutMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|(
name|executor
argument_list|,
literal|100
argument_list|)
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
literal|50
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
literal|250
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
DECL|method|testDefaultTimeoutMapForcePurge ()
specifier|public
name|void
name|testDefaultTimeoutMapForcePurge
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultTimeoutMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
operator|new
name|DefaultTimeoutMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|(
name|executor
argument_list|,
literal|100
argument_list|)
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
literal|50
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
literal|250
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
DECL|method|testDefaultTimeoutMapGetRemove ()
specifier|public
name|void
name|testDefaultTimeoutMapGetRemove
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultTimeoutMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
operator|new
name|DefaultTimeoutMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|(
name|executor
argument_list|,
literal|100
argument_list|)
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
literal|50
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
operator|(
name|int
operator|)
name|map
operator|.
name|get
argument_list|(
literal|"A"
argument_list|)
argument_list|)
expr_stmt|;
name|Object
name|old
init|=
name|map
operator|.
name|remove
argument_list|(
literal|"A"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|123
argument_list|,
name|old
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
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
operator|new
name|DefaultTimeoutMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|(
name|executor
argument_list|,
literal|100
argument_list|)
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
literal|50
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
literal|50
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
name|CamelContext
name|camelContext
init|=
operator|new
name|DefaultCamelContext
argument_list|()
decl_stmt|;
name|ThreadPoolProfile
name|profile
init|=
operator|new
name|ThreadPoolBuilder
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|poolSize
argument_list|(
literal|2
argument_list|)
operator|.
name|daemon
argument_list|()
operator|.
name|build
argument_list|()
decl_stmt|;
name|ScheduledExecutorService
name|e
init|=
name|camelContext
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|getScheduledExecutorService
argument_list|(
name|profile
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|DefaultTimeoutMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
operator|new
name|DefaultTimeoutMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|(
name|e
argument_list|,
literal|50
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|50
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
literal|100
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
literal|250
argument_list|)
expr_stmt|;
comment|// should have been timed out now
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
DECL|method|testExpiredInCorrectOrder ()
specifier|public
name|void
name|testExpiredInCorrectOrder
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|keys
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|final
name|List
argument_list|<
name|Integer
argument_list|>
name|values
init|=
operator|new
name|ArrayList
argument_list|<
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
name|DefaultTimeoutMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
operator|new
name|DefaultTimeoutMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|(
name|executor
argument_list|,
literal|100
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|onEviction
parameter_list|(
name|String
name|key
parameter_list|,
name|Integer
name|value
parameter_list|)
block|{
name|keys
operator|.
name|add
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|values
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
decl_stmt|;
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
literal|1
argument_list|,
literal|50
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"B"
argument_list|,
literal|2
argument_list|,
literal|30
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"C"
argument_list|,
literal|3
argument_list|,
literal|40
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"D"
argument_list|,
literal|4
argument_list|,
literal|20
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"E"
argument_list|,
literal|5
argument_list|,
literal|40
argument_list|)
expr_stmt|;
comment|// is not expired
name|map
operator|.
name|put
argument_list|(
literal|"F"
argument_list|,
literal|6
argument_list|,
literal|800
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|250
argument_list|)
expr_stmt|;
comment|// force purge
name|map
operator|.
name|purge
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"D"
argument_list|,
name|keys
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|values
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"B"
argument_list|,
name|keys
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|values
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"C"
argument_list|,
name|keys
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|values
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"E"
argument_list|,
name|keys
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|values
operator|.
name|get
argument_list|(
literal|3
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"A"
argument_list|,
name|keys
operator|.
name|get
argument_list|(
literal|4
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|values
operator|.
name|get
argument_list|(
literal|4
argument_list|)
operator|.
name|intValue
argument_list|()
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
block|}
DECL|method|testExpiredNotEvicted ()
specifier|public
name|void
name|testExpiredNotEvicted
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|keys
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|final
name|List
argument_list|<
name|Integer
argument_list|>
name|values
init|=
operator|new
name|ArrayList
argument_list|<
name|Integer
argument_list|>
argument_list|()
decl_stmt|;
name|DefaultTimeoutMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
operator|new
name|DefaultTimeoutMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|(
name|executor
argument_list|,
literal|100
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|onEviction
parameter_list|(
name|String
name|key
parameter_list|,
name|Integer
name|value
parameter_list|)
block|{
comment|// do not evict special key
if|if
condition|(
literal|"gold"
operator|.
name|equals
argument_list|(
name|key
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|keys
operator|.
name|add
argument_list|(
name|key
argument_list|)
expr_stmt|;
name|values
operator|.
name|add
argument_list|(
name|value
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
decl_stmt|;
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
literal|1
argument_list|,
literal|90
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"B"
argument_list|,
literal|2
argument_list|,
literal|100
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"gold"
argument_list|,
literal|9
argument_list|,
literal|110
argument_list|)
expr_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"C"
argument_list|,
literal|3
argument_list|,
literal|120
argument_list|)
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|250
argument_list|)
expr_stmt|;
comment|// force purge
name|map
operator|.
name|purge
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"A"
argument_list|,
name|keys
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|values
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"B"
argument_list|,
name|keys
operator|.
name|get
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|values
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"C"
argument_list|,
name|keys
operator|.
name|get
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|values
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
comment|// and keep the gold in the map
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
name|Integer
operator|.
name|valueOf
argument_list|(
literal|9
argument_list|)
argument_list|,
name|map
operator|.
name|get
argument_list|(
literal|"gold"
argument_list|)
argument_list|)
expr_stmt|;
block|}
DECL|method|testDefaultTimeoutMapStopStart ()
specifier|public
name|void
name|testDefaultTimeoutMapStopStart
parameter_list|()
throws|throws
name|Exception
block|{
name|DefaultTimeoutMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
name|map
init|=
operator|new
name|DefaultTimeoutMap
argument_list|<
name|String
argument_list|,
name|Integer
argument_list|>
argument_list|(
name|executor
argument_list|,
literal|100
argument_list|)
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
literal|"A"
argument_list|,
literal|1
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
name|map
operator|.
name|stop
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
name|map
operator|.
name|put
argument_list|(
literal|"A"
argument_list|,
literal|1
argument_list|,
literal|50
argument_list|)
expr_stmt|;
comment|// should not timeout as the scheduler doesn't run
name|Thread
operator|.
name|sleep
argument_list|(
literal|250
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
comment|// start
name|map
operator|.
name|start
argument_list|()
expr_stmt|;
comment|// start and wait for scheduler to purge
name|Thread
operator|.
name|sleep
argument_list|(
literal|250
argument_list|)
expr_stmt|;
comment|// now it should be gone
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
block|}
end_class

end_unit

