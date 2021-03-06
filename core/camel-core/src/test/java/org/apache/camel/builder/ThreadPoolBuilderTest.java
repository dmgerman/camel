begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.builder
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
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
name|ExecutorService
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
name|Executors
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
name|ContextTestSupport
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
name|util
operator|.
name|concurrent
operator|.
name|ThreadPoolRejectedPolicy
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

begin_class
DECL|class|ThreadPoolBuilderTest
specifier|public
class|class
name|ThreadPoolBuilderTest
extends|extends
name|ContextTestSupport
block|{
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
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|ExecutorService
name|someone
init|=
name|Executors
operator|.
name|newCachedThreadPool
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"someonesPool"
argument_list|,
name|someone
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
annotation|@
name|Test
DECL|method|testThreadPoolBuilderDefault ()
specifier|public
name|void
name|testThreadPoolBuilderDefault
parameter_list|()
throws|throws
name|Exception
block|{
name|ThreadPoolBuilder
name|builder
init|=
operator|new
name|ThreadPoolBuilder
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|ExecutorService
name|executor
init|=
name|builder
operator|.
name|build
argument_list|(
name|this
argument_list|,
literal|"myPool"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|executor
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|executor
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|executor
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testThreadPoolBuilderMaxQueueSize ()
specifier|public
name|void
name|testThreadPoolBuilderMaxQueueSize
parameter_list|()
throws|throws
name|Exception
block|{
name|ThreadPoolBuilder
name|builder
init|=
operator|new
name|ThreadPoolBuilder
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|ExecutorService
name|executor
init|=
name|builder
operator|.
name|maxQueueSize
argument_list|(
literal|2000
argument_list|)
operator|.
name|build
argument_list|(
name|this
argument_list|,
literal|"myPool"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|executor
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|executor
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|executor
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testThreadPoolBuilderMax ()
specifier|public
name|void
name|testThreadPoolBuilderMax
parameter_list|()
throws|throws
name|Exception
block|{
name|ThreadPoolBuilder
name|builder
init|=
operator|new
name|ThreadPoolBuilder
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|ExecutorService
name|executor
init|=
name|builder
operator|.
name|maxPoolSize
argument_list|(
literal|100
argument_list|)
operator|.
name|build
argument_list|(
name|this
argument_list|,
literal|"myPool"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|executor
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|executor
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|executor
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testThreadPoolBuilderCoreAndMax ()
specifier|public
name|void
name|testThreadPoolBuilderCoreAndMax
parameter_list|()
throws|throws
name|Exception
block|{
name|ThreadPoolBuilder
name|builder
init|=
operator|new
name|ThreadPoolBuilder
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|ExecutorService
name|executor
init|=
name|builder
operator|.
name|poolSize
argument_list|(
literal|50
argument_list|)
operator|.
name|maxPoolSize
argument_list|(
literal|100
argument_list|)
operator|.
name|build
argument_list|(
name|this
argument_list|,
literal|"myPool"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|executor
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|executor
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|executor
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testThreadPoolBuilderKeepAlive ()
specifier|public
name|void
name|testThreadPoolBuilderKeepAlive
parameter_list|()
throws|throws
name|Exception
block|{
name|ThreadPoolBuilder
name|builder
init|=
operator|new
name|ThreadPoolBuilder
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|ExecutorService
name|executor
init|=
name|builder
operator|.
name|keepAliveTime
argument_list|(
literal|30
argument_list|)
operator|.
name|build
argument_list|(
name|this
argument_list|,
literal|"myPool"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|executor
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|executor
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|executor
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testThreadPoolBuilderKeepAliveTimeUnit ()
specifier|public
name|void
name|testThreadPoolBuilderKeepAliveTimeUnit
parameter_list|()
throws|throws
name|Exception
block|{
name|ThreadPoolBuilder
name|builder
init|=
operator|new
name|ThreadPoolBuilder
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|ExecutorService
name|executor
init|=
name|builder
operator|.
name|keepAliveTime
argument_list|(
literal|20000
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
operator|.
name|build
argument_list|(
name|this
argument_list|,
literal|"myPool"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|executor
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|executor
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|executor
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testThreadPoolBuilderAll ()
specifier|public
name|void
name|testThreadPoolBuilderAll
parameter_list|()
throws|throws
name|Exception
block|{
name|ThreadPoolBuilder
name|builder
init|=
operator|new
name|ThreadPoolBuilder
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|ExecutorService
name|executor
init|=
name|builder
operator|.
name|poolSize
argument_list|(
literal|50
argument_list|)
operator|.
name|maxPoolSize
argument_list|(
literal|100
argument_list|)
operator|.
name|maxQueueSize
argument_list|(
literal|2000
argument_list|)
operator|.
name|keepAliveTime
argument_list|(
literal|20000
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
operator|.
name|rejectedPolicy
argument_list|(
name|ThreadPoolRejectedPolicy
operator|.
name|DiscardOldest
argument_list|)
operator|.
name|build
argument_list|(
name|this
argument_list|,
literal|"myPool"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|executor
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|executor
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|executor
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testThreadPoolBuilderTwoPoolsDefault ()
specifier|public
name|void
name|testThreadPoolBuilderTwoPoolsDefault
parameter_list|()
throws|throws
name|Exception
block|{
name|ThreadPoolBuilder
name|builder
init|=
operator|new
name|ThreadPoolBuilder
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|ExecutorService
name|executor
init|=
name|builder
operator|.
name|build
argument_list|(
name|this
argument_list|,
literal|"myPool"
argument_list|)
decl_stmt|;
name|ExecutorService
name|executor2
init|=
name|builder
operator|.
name|build
argument_list|(
name|this
argument_list|,
literal|"myOtherPool"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|executor
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|executor2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|executor
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|executor2
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|executor
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|executor2
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testThreadPoolBuilderScheduled ()
specifier|public
name|void
name|testThreadPoolBuilderScheduled
parameter_list|()
throws|throws
name|Exception
block|{
name|ThreadPoolBuilder
name|builder
init|=
operator|new
name|ThreadPoolBuilder
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|ScheduledExecutorService
name|executor
init|=
name|builder
operator|.
name|poolSize
argument_list|(
literal|5
argument_list|)
operator|.
name|maxQueueSize
argument_list|(
literal|2000
argument_list|)
operator|.
name|buildScheduled
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|executor
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|executor
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|executor
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testThreadPoolBuilderScheduledName ()
specifier|public
name|void
name|testThreadPoolBuilderScheduledName
parameter_list|()
throws|throws
name|Exception
block|{
name|ThreadPoolBuilder
name|builder
init|=
operator|new
name|ThreadPoolBuilder
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|ScheduledExecutorService
name|executor
init|=
name|builder
operator|.
name|poolSize
argument_list|(
literal|5
argument_list|)
operator|.
name|maxQueueSize
argument_list|(
literal|2000
argument_list|)
operator|.
name|buildScheduled
argument_list|(
literal|"myScheduledPool"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|executor
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|executor
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|executor
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testThreadPoolBuilderScheduledSourceName ()
specifier|public
name|void
name|testThreadPoolBuilderScheduledSourceName
parameter_list|()
throws|throws
name|Exception
block|{
name|ThreadPoolBuilder
name|builder
init|=
operator|new
name|ThreadPoolBuilder
argument_list|(
name|context
argument_list|)
decl_stmt|;
name|ScheduledExecutorService
name|executor
init|=
name|builder
operator|.
name|poolSize
argument_list|(
literal|5
argument_list|)
operator|.
name|maxQueueSize
argument_list|(
literal|2000
argument_list|)
operator|.
name|buildScheduled
argument_list|(
name|this
argument_list|,
literal|"myScheduledPool"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|executor
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|false
argument_list|,
name|executor
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|stop
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|true
argument_list|,
name|executor
operator|.
name|isShutdown
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

