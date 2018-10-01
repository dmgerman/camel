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
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|JobDataMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|quartz
operator|.
name|JobKey
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
name|impl
operator|.
name|matchers
operator|.
name|GroupMatcher
import|;
end_import

begin_comment
comment|/**  * This test the  CronTrigger as a timer endpoint in a route.  */
end_comment

begin_class
DECL|class|QuartzManuallyTriggerJobTest
specifier|public
class|class
name|QuartzManuallyTriggerJobTest
extends|extends
name|BaseQuartzTest
block|{
annotation|@
name|Test
DECL|method|testQuartzCronRoute ()
specifier|public
name|void
name|testQuartzCronRoute
parameter_list|()
throws|throws
name|Exception
block|{
name|MockEndpoint
name|mock
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|mock
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|QuartzComponent
name|component
init|=
name|context
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
name|Scheduler
name|scheduler
init|=
name|component
operator|.
name|getScheduler
argument_list|()
decl_stmt|;
comment|// collect all jobKeys of this route (ideally only one).
name|ArrayList
argument_list|<
name|JobKey
argument_list|>
name|jobKeys
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|group
range|:
name|scheduler
operator|.
name|getJobGroupNames
argument_list|()
control|)
block|{
for|for
control|(
name|JobKey
name|jobKey
range|:
name|scheduler
operator|.
name|getJobKeys
argument_list|(
name|GroupMatcher
operator|.
name|jobGroupEquals
argument_list|(
name|group
argument_list|)
argument_list|)
control|)
block|{
name|jobKeys
operator|.
name|add
argument_list|(
name|jobKey
argument_list|)
expr_stmt|;
block|}
block|}
name|JobDataMap
name|jobDataMap
init|=
name|scheduler
operator|.
name|getJobDetail
argument_list|(
name|jobKeys
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|getJobDataMap
argument_list|()
decl_stmt|;
comment|// trigger job manually
name|scheduler
operator|.
name|triggerJob
argument_list|(
name|jobKeys
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|jobDataMap
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createRouteBuilder ()
specifier|protected
name|RouteBuilder
name|createRouteBuilder
parameter_list|()
block|{
return|return
operator|new
name|RouteBuilder
argument_list|()
block|{
specifier|public
name|void
name|configure
parameter_list|()
block|{
name|from
argument_list|(
literal|"quartz2://MyTimer?cron=05+00+00+*+*+?"
argument_list|)
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
block|}
end_class

end_unit

