begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|concurrent
operator|.
name|CountDownLatch
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
name|apache
operator|.
name|camel
operator|.
name|spi
operator|.
name|PollingConsumerPollStrategy
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

begin_comment
comment|/**  * Unit test for poll strategy  */
end_comment

begin_class
DECL|class|FileConsumerPollStrategyPolledMessagesTest
specifier|public
class|class
name|FileConsumerPollStrategyPolledMessagesTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|maxPolls
specifier|private
specifier|static
name|int
name|maxPolls
decl_stmt|;
DECL|field|latch
specifier|private
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
DECL|field|fileUrl
specifier|private
name|String
name|fileUrl
init|=
literal|"file://target/data/pollstrategy/?consumer.pollStrategy=#myPoll&initialDelay=0&delay=10"
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
literal|"myPoll"
argument_list|,
operator|new
name|MyPollStrategy
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|jndi
return|;
block|}
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
name|deleteDirectory
argument_list|(
literal|"target/data/pollstrategy"
argument_list|)
expr_stmt|;
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testPolledMessages ()
specifier|public
name|void
name|testPolledMessages
parameter_list|()
throws|throws
name|Exception
block|{
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/data/pollstrategy/"
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
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/data/pollstrategy/"
argument_list|,
literal|"Bye World"
argument_list|,
name|Exchange
operator|.
name|FILE_NAME
argument_list|,
literal|"bye.txt"
argument_list|)
expr_stmt|;
comment|// start route now files have been created
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
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
comment|// wait for commit to be issued
name|assertTrue
argument_list|(
name|latch
operator|.
name|await
argument_list|(
literal|5
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|maxPolls
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
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|from
argument_list|(
name|fileUrl
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
name|convertBodyTo
argument_list|(
name|String
operator|.
name|class
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
specifier|private
class|class
name|MyPollStrategy
implements|implements
name|PollingConsumerPollStrategy
block|{
annotation|@
name|Override
DECL|method|begin (Consumer consumer, Endpoint endpoint)
specifier|public
name|boolean
name|begin
parameter_list|(
name|Consumer
name|consumer
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
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
if|if
condition|(
name|polledMessages
operator|>
name|maxPolls
condition|)
block|{
name|maxPolls
operator|=
name|polledMessages
expr_stmt|;
block|}
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|rollback (Consumer consumer, Endpoint endpoint, int retryCounter, Exception cause)
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
name|cause
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
end_class

end_unit

