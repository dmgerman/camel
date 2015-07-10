begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.quartz2
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|quartz2
package|;
end_package

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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
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
name|hamcrest
operator|.
name|CoreMatchers
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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
name|quartz
operator|.
name|CronTrigger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|JobDetail
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|Scheduler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|Trigger
import|;
end_import

begin_class
DECL|class|QuartzComponentCamelContextSharedSchedulerTest
specifier|public
class|class
name|QuartzComponentCamelContextSharedSchedulerTest
block|{
DECL|field|camel1
specifier|private
name|DefaultCamelContext
name|camel1
decl_stmt|;
DECL|field|camel2
specifier|private
name|DefaultCamelContext
name|camel2
decl_stmt|;
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
name|camel1
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|camel1
operator|.
name|setName
argument_list|(
literal|"camel-1"
argument_list|)
expr_stmt|;
name|camel1
operator|.
name|addRoutes
argument_list|(
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
literal|"quartz2://myGroup/myTimerName?cron=0/2+*+*+*+*+?"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:one"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|camel1
operator|.
name|start
argument_list|()
expr_stmt|;
name|camel2
operator|=
operator|new
name|DefaultCamelContext
argument_list|()
expr_stmt|;
name|camel2
operator|.
name|setName
argument_list|(
literal|"camel-2"
argument_list|)
expr_stmt|;
name|Scheduler
name|camel1Scheduler
init|=
name|camel1
operator|.
name|getComponent
argument_list|(
literal|"quartz2"
argument_list|,
name|QuartzComponent
operator|.
name|class
argument_list|)
operator|.
name|getScheduler
argument_list|()
decl_stmt|;
name|QuartzComponent
name|camel2QuartzComponent
init|=
name|camel2
operator|.
name|getComponent
argument_list|(
literal|"quartz2"
argument_list|,
name|QuartzComponent
operator|.
name|class
argument_list|)
decl_stmt|;
name|camel2QuartzComponent
operator|.
name|setScheduler
argument_list|(
name|camel1Scheduler
argument_list|)
expr_stmt|;
name|camel2
operator|.
name|addRoutes
argument_list|(
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
literal|"quartz2://myOtherGroup/myOtherTimerName?cron=0/1+*+*+*+*+?"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:two"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|camel2
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|camel1
operator|.
name|stop
argument_list|()
expr_stmt|;
name|camel2
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testTwoCamelContext ()
specifier|public
name|void
name|testTwoCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock1
init|=
name|camel1
operator|.
name|getEndpoint
argument_list|(
literal|"mock:one"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|mock1
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|MockEndpoint
name|mock2
init|=
name|camel2
operator|.
name|getEndpoint
argument_list|(
literal|"mock:two"
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|mock2
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|6
argument_list|)
expr_stmt|;
name|mock1
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|JobDetail
name|detail
init|=
name|mock1
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"jobDetail"
argument_list|,
name|JobDetail
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertThat
argument_list|(
name|detail
operator|.
name|getJobDataMap
argument_list|()
operator|.
name|get
argument_list|(
name|QuartzConstants
operator|.
name|QUARTZ_TRIGGER_CRON_EXPRESSION
argument_list|)
operator|.
name|equals
argument_list|(
literal|"0/2 * * * * ?"
argument_list|)
argument_list|,
name|CoreMatchers
operator|.
name|is
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|camel1
operator|.
name|stop
argument_list|()
expr_stmt|;
name|mock2
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|detail
operator|=
name|mock2
operator|.
name|getReceivedExchanges
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"jobDetail"
argument_list|,
name|JobDetail
operator|.
name|class
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertThat
argument_list|(
name|detail
operator|.
name|getJobDataMap
argument_list|()
operator|.
name|get
argument_list|(
name|QuartzConstants
operator|.
name|QUARTZ_TRIGGER_CRON_EXPRESSION
argument_list|)
operator|.
name|equals
argument_list|(
literal|"0/1 * * * * ?"
argument_list|)
argument_list|,
name|CoreMatchers
operator|.
name|is
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|camel2
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

