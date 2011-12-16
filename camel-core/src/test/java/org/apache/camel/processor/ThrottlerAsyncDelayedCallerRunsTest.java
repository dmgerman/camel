begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.processor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|processor
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
name|builder
operator|.
name|ThreadPoolProfileBuilder
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
DECL|class|ThrottlerAsyncDelayedCallerRunsTest
specifier|public
class|class
name|ThrottlerAsyncDelayedCallerRunsTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testThrottler ()
specifier|public
name|void
name|testThrottler
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
literal|6
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:start"
argument_list|,
literal|"A"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:start"
argument_list|,
literal|"B"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:start"
argument_list|,
literal|"C"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:start"
argument_list|,
literal|"D"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:start"
argument_list|,
literal|"E"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:start"
argument_list|,
literal|"F"
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
comment|// create a profile for the throttler
name|ThreadPoolProfileBuilder
name|builder
init|=
operator|new
name|ThreadPoolProfileBuilder
argument_list|(
literal|"myThrottler"
argument_list|)
decl_stmt|;
name|builder
operator|.
name|maxQueueSize
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|context
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|registerThreadPoolProfile
argument_list|(
name|builder
operator|.
name|build
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:start"
argument_list|)
operator|.
name|throttle
argument_list|(
literal|1
argument_list|)
operator|.
name|timePeriodMillis
argument_list|(
literal|100
argument_list|)
operator|.
name|asyncDelayed
argument_list|()
operator|.
name|executorServiceRef
argument_list|(
literal|"myThrottler"
argument_list|)
operator|.
name|callerRunsWhenRejected
argument_list|(
literal|true
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

