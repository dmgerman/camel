begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.stress
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
operator|.
name|stress
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
name|Ignore
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
annotation|@
name|Ignore
argument_list|(
literal|"Manual test"
argument_list|)
DECL|class|FileAsyncStressTest
specifier|public
class|class
name|FileAsyncStressTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|files
specifier|private
name|int
name|files
init|=
literal|150
decl_stmt|;
annotation|@
name|Override
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
comment|// do not test on windows
if|if
condition|(
name|isPlatform
argument_list|(
literal|"windows"
argument_list|)
condition|)
block|{
return|return;
block|}
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|deleteDirectory
argument_list|(
literal|"target/data/filestress"
argument_list|)
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
name|files
condition|;
name|i
operator|++
control|)
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/data/filestress"
argument_list|,
literal|"Hello World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
name|i
operator|+
literal|".txt"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testAsyncStress ()
specifier|public
name|void
name|testAsyncStress
parameter_list|()
throws|throws
name|Exception
block|{
comment|// do not test on windows
if|if
condition|(
name|isPlatform
argument_list|(
literal|"windows"
argument_list|)
condition|)
block|{
return|return;
block|}
comment|// start route when all the files have been written
name|context
operator|.
name|getRouteController
argument_list|()
operator|.
name|startRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
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
literal|100
argument_list|)
expr_stmt|;
name|mock
operator|.
name|setResultWaitTime
argument_list|(
literal|30000
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
comment|// leverage the fact that we can limit to max 50 files per poll
comment|// this will result in polling again and potentially picking up
comment|// files
comment|// that already are in progress
name|from
argument_list|(
literal|"file:target/data/filestress?maxMessagesPerPoll=50"
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
name|threads
argument_list|(
literal|10
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
comment|// simulate some work with random time to complete
name|Random
name|ran
init|=
operator|new
name|Random
argument_list|()
decl_stmt|;
name|int
name|delay
init|=
name|ran
operator|.
name|nextInt
argument_list|(
literal|50
argument_list|)
operator|+
literal|10
decl_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
name|delay
argument_list|)
expr_stmt|;
block|}
block|}
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

