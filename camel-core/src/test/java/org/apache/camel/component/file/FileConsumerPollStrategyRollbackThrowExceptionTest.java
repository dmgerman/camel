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
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_comment
comment|/**  * Unit test for poll strategy  */
end_comment

begin_class
DECL|class|FileConsumerPollStrategyRollbackThrowExceptionTest
specifier|public
class|class
name|FileConsumerPollStrategyRollbackThrowExceptionTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|event
specifier|private
specifier|static
name|String
name|event
init|=
literal|""
decl_stmt|;
DECL|field|fileUrl
specifier|private
name|String
name|fileUrl
init|=
literal|"file://target/pollstrategy/?pollStrategy=#myPoll"
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
DECL|method|setUp ()
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|deleteDirectory
argument_list|(
literal|"target/pollstrategy"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBodyAndHeader
argument_list|(
literal|"file:target/pollstrategy/"
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
block|}
DECL|method|testRollbackThrowException ()
specifier|public
name|void
name|testRollbackThrowException
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
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|// let it run for a little while since we rethrow the exception the consumer
comment|// will stop scheduling and not poll anymore
name|Thread
operator|.
name|sleep
argument_list|(
literal|2000
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"rollback"
argument_list|,
name|event
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
name|fileUrl
argument_list|)
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
DECL|method|begin (Consumer consumer, Endpoint endpoint)
specifier|public
name|void
name|begin
parameter_list|(
name|Consumer
name|consumer
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
block|{
comment|// start consumer as we simulate the fail in begin
comment|// and thus before camel lazy start it itself
try|try
block|{
name|consumer
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
comment|// simulate an error on first poll
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Damn I cannot do this"
argument_list|)
throw|;
block|}
DECL|method|commit (Consumer consumer, Endpoint endpoint)
specifier|public
name|void
name|commit
parameter_list|(
name|Consumer
name|consumer
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|event
operator|+=
literal|"commit"
expr_stmt|;
block|}
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
name|event
operator|+=
literal|"rollback"
expr_stmt|;
throw|throw
name|cause
throw|;
block|}
block|}
block|}
end_class

end_unit

