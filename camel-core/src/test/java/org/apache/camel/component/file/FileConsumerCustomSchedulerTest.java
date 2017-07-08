begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Timer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TimerTask
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
name|Consumer
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
name|Exchange
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
name|RouteBuilder
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
name|ScheduledPollConsumerScheduler
import|;
end_import

begin_class
DECL|class|FileConsumerCustomSchedulerTest
specifier|public
class|class
name|FileConsumerCustomSchedulerTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|scheduler
specifier|private
name|MyScheduler
name|scheduler
init|=
operator|new
name|MyScheduler
argument_list|()
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
name|JndiRegistry
name|jndi
init|=
name|super
operator|.
name|createRegistry
argument_list|()
decl_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"myScheduler"
argument_list|,
name|scheduler
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
annotation|@
name|Override
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|deleteDirectory
argument_list|(
literal|"target/file/custom"
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
DECL|method|testCustomScheduler ()
specifier|public
name|void
name|testCustomScheduler
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/file/custom"
argument_list|,
literal|"Hello World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"hello.txt"
argument_list|)
expr_stmt|;
name|context
operator|.
name|startRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// the scheduler is only run once, and we can configure its properties
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|scheduler
operator|.
name|getCounter
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|scheduler
operator|.
name|getFoo
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
literal|"file:target/file/custom?scheduler=#myScheduler&scheduler.foo=bar&initialDelay=0&delay=10"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|noAutoStartup
argument_list|()
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|MyScheduler
specifier|private
specifier|static
specifier|final
class|class
name|MyScheduler
implements|implements
name|ScheduledPollConsumerScheduler
block|{
DECL|field|camelContext
specifier|private
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|timer
specifier|private
name|Timer
name|timer
decl_stmt|;
DECL|field|timerTask
specifier|private
name|TimerTask
name|timerTask
decl_stmt|;
DECL|field|counter
specifier|private
specifier|volatile
name|int
name|counter
decl_stmt|;
DECL|field|foo
specifier|private
name|String
name|foo
decl_stmt|;
annotation|@
name|Override
DECL|method|onInit (Consumer consumer)
specifier|public
name|void
name|onInit
parameter_list|(
name|Consumer
name|consumer
parameter_list|)
block|{
comment|// noop
block|}
annotation|@
name|Override
DECL|method|scheduleTask (final Runnable task)
specifier|public
name|void
name|scheduleTask
parameter_list|(
specifier|final
name|Runnable
name|task
parameter_list|)
block|{
name|this
operator|.
name|timerTask
operator|=
operator|new
name|TimerTask
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
name|counter
operator|++
expr_stmt|;
name|task
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|unscheduleTask ()
specifier|public
name|void
name|unscheduleTask
parameter_list|()
block|{
comment|// noop
block|}
DECL|method|getCounter ()
specifier|public
name|int
name|getCounter
parameter_list|()
block|{
return|return
name|counter
return|;
block|}
DECL|method|getFoo ()
specifier|public
name|String
name|getFoo
parameter_list|()
block|{
return|return
name|foo
return|;
block|}
DECL|method|setFoo (String foo)
specifier|public
name|void
name|setFoo
parameter_list|(
name|String
name|foo
parameter_list|)
block|{
name|this
operator|.
name|foo
operator|=
name|foo
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|startScheduler ()
specifier|public
name|void
name|startScheduler
parameter_list|()
block|{
name|timer
operator|=
operator|new
name|Timer
argument_list|()
expr_stmt|;
name|timer
operator|.
name|schedule
argument_list|(
name|timerTask
argument_list|,
literal|10
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|isSchedulerStarted ()
specifier|public
name|boolean
name|isSchedulerStarted
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|setCamelContext (CamelContext camelContext)
specifier|public
name|void
name|setCamelContext
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getCamelContext ()
specifier|public
name|CamelContext
name|getCamelContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
annotation|@
name|Override
DECL|method|shutdown ()
specifier|public
name|void
name|shutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|timerTask
operator|.
name|cancel
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|start ()
specifier|public
name|void
name|start
parameter_list|()
throws|throws
name|Exception
block|{         }
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
throws|throws
name|Exception
block|{         }
block|}
block|}
end_class

end_unit

