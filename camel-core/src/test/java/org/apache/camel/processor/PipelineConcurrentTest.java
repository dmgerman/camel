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
name|java
operator|.
name|util
operator|.
name|Random
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
name|Processor
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
name|springframework
operator|.
name|scheduling
operator|.
name|concurrent
operator|.
name|ThreadPoolTaskExecutor
import|;
end_import

begin_comment
comment|/**  * Unit test the pipeline in concurrent conditions.  */
end_comment

begin_class
DECL|class|PipelineConcurrentTest
specifier|public
class|class
name|PipelineConcurrentTest
extends|extends
name|ContextTestSupport
block|{
DECL|method|testConcurrentPipeline ()
specifier|public
name|void
name|testConcurrentPipeline
parameter_list|()
throws|throws
name|Exception
block|{
name|int
name|total
init|=
literal|10000
decl_stmt|;
specifier|final
name|int
name|group
init|=
name|total
operator|/
literal|20
decl_stmt|;
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
name|expectedMessageCount
argument_list|(
name|total
argument_list|)
expr_stmt|;
name|ThreadPoolTaskExecutor
name|executor
init|=
operator|new
name|ThreadPoolTaskExecutor
argument_list|()
decl_stmt|;
name|executor
operator|.
name|setCorePoolSize
argument_list|(
literal|20
argument_list|)
expr_stmt|;
name|executor
operator|.
name|afterPropertiesSet
argument_list|()
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|20
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|int
name|threadCount
init|=
name|i
decl_stmt|;
name|executor
operator|.
name|execute
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
name|int
name|start
init|=
name|threadCount
operator|*
name|group
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|group
condition|;
name|i
operator|++
control|)
block|{
try|try
block|{
comment|// do some random sleep to simulate spread in user activity
name|Thread
operator|.
name|sleep
argument_list|(
operator|new
name|Random
argument_list|()
operator|.
name|nextInt
argument_list|(
literal|10
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:in"
argument_list|,
literal|""
operator|+
operator|(
name|start
operator|+
name|i
operator|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
name|mock
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|mock
operator|.
name|expectsNoDuplicates
argument_list|(
name|body
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
comment|// to force any exceptions coming forward imeddiately
name|errorHandler
argument_list|(
name|noErrorHandler
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:in"
argument_list|)
operator|.
name|thread
argument_list|(
literal|10
argument_list|)
operator|.
name|pipeline
argument_list|(
literal|"direct:do"
argument_list|,
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:do"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Bye "
operator|+
name|body
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

