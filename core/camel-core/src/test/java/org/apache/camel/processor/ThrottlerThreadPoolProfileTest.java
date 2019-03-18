begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
DECL|class|ThrottlerThreadPoolProfileTest
specifier|public
class|class
name|ThrottlerThreadPoolProfileTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|canTest ()
specifier|protected
name|boolean
name|canTest
parameter_list|()
block|{
comment|// skip test on windows as it does not run well there
return|return
operator|!
name|isPlatform
argument_list|(
literal|"windows"
argument_list|)
return|;
block|}
annotation|@
name|Test
DECL|method|testThreadPool ()
specifier|public
name|void
name|testThreadPool
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|canTest
argument_list|()
condition|)
block|{
return|return;
block|}
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
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
comment|// create thread pool profile and register to camel
name|ThreadPoolProfile
name|profile
init|=
operator|new
name|ThreadPoolProfileBuilder
argument_list|(
literal|"myPool"
argument_list|)
operator|.
name|poolSize
argument_list|(
literal|2
argument_list|)
operator|.
name|maxPoolSize
argument_list|(
literal|5
argument_list|)
operator|.
name|maxQueueSize
argument_list|(
literal|10
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|context
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|registerThreadPoolProfile
argument_list|(
name|profile
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|throttle
argument_list|(
name|constant
argument_list|(
literal|2
argument_list|)
argument_list|)
operator|.
name|executorServiceRef
argument_list|(
literal|"myPool"
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

