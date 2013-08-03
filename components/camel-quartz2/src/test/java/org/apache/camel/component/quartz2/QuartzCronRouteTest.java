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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
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
name|Assert
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
name|Trigger
import|;
end_import

begin_comment
comment|/**  * This test the  CronTrigger as a timer endpoint in a route.  * @version   */
end_comment

begin_class
DECL|class|QuartzCronRouteTest
specifier|public
class|class
name|QuartzCronRouteTest
extends|extends
name|CamelTestSupport
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
literal|3
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|Trigger
name|trigger
init|=
name|mock
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
literal|"trigger"
argument_list|,
name|Trigger
operator|.
name|class
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertThat
argument_list|(
name|trigger
operator|instanceof
name|CronTrigger
argument_list|,
name|CoreMatchers
operator|.
name|is
argument_list|(
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|JobDetail
name|detail
init|=
name|mock
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
name|getJobClass
argument_list|()
operator|.
name|equals
argument_list|(
name|CamelJob
operator|.
name|class
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
comment|// triggers every 2th second at precise 00,02,04,06..58
comment|// notice we must use + as space when configured using URI parameter
name|from
argument_list|(
literal|"quartz2://myGroup/myTimerName?cron=0/2+*+*+*+*+?"
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

