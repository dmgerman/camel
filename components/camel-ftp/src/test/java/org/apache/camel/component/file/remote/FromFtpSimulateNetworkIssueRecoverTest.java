begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file.remote
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
name|remote
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
name|BindToRegistry
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
name|Endpoint
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
name|JndiRegistry
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

begin_comment
comment|/**  * Simulate network issues by using a custom poll strategy to force exceptions  * occurring during poll.  */
end_comment

begin_class
DECL|class|FromFtpSimulateNetworkIssueRecoverTest
specifier|public
class|class
name|FromFtpSimulateNetworkIssueRecoverTest
extends|extends
name|FtpServerTestSupport
block|{
DECL|field|counter
specifier|private
specifier|static
name|int
name|counter
decl_stmt|;
DECL|field|rollback
specifier|private
specifier|static
name|int
name|rollback
decl_stmt|;
annotation|@
name|BindToRegistry
argument_list|(
literal|"myPoll"
argument_list|)
DECL|field|strategy
specifier|private
name|MyPollStrategy
name|strategy
init|=
operator|new
name|MyPollStrategy
argument_list|()
decl_stmt|;
DECL|method|getFtpUrl ()
specifier|private
name|String
name|getFtpUrl
parameter_list|()
block|{
return|return
literal|"ftp://admin@localhost:"
operator|+
name|getPort
argument_list|()
operator|+
literal|"/recover?password=admin&pollStrategy=#myPoll"
return|;
block|}
annotation|@
name|Test
DECL|method|testFtpRecover ()
specifier|public
name|void
name|testFtpRecover
parameter_list|()
throws|throws
name|Exception
block|{
comment|// should be able to download the file after recovering
name|MockEndpoint
name|resultEndpoint
init|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
decl_stmt|;
name|resultEndpoint
operator|.
name|expectedMinimumMessageCount
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
name|getFtpUrl
argument_list|()
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|resultEndpoint
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should have tried at least 3 times was "
operator|+
name|counter
argument_list|,
name|counter
operator|>=
literal|3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|rollback
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
name|from
argument_list|(
name|getFtpUrl
argument_list|()
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
DECL|class|MyPollStrategy
specifier|public
class|class
name|MyPollStrategy
extends|extends
name|RemoteFilePollingConsumerPollStrategy
block|{
annotation|@
name|Override
DECL|method|commit (Consumer consumer, Endpoint endpoint, int polledMessages)
specifier|public
name|void
name|commit
parameter_list|(
name|Consumer
name|consumer
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|int
name|polledMessages
parameter_list|)
block|{
name|counter
operator|++
expr_stmt|;
if|if
condition|(
name|counter
operator|<
literal|3
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Forced by unit test"
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|rollback (Consumer consumer, Endpoint endpoint, int retryCounter, Exception e)
specifier|public
name|boolean
name|rollback
parameter_list|(
name|Consumer
name|consumer
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|int
name|retryCounter
parameter_list|,
name|Exception
name|e
parameter_list|)
throws|throws
name|Exception
block|{
name|rollback
operator|++
expr_stmt|;
return|return
name|super
operator|.
name|rollback
argument_list|(
name|consumer
argument_list|,
name|endpoint
argument_list|,
name|retryCounter
argument_list|,
name|e
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

