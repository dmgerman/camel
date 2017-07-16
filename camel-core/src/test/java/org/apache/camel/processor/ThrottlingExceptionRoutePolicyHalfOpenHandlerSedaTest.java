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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|apache
operator|.
name|camel
operator|.
name|impl
operator|.
name|ThrottlingExceptionHalfOpenHandler
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
name|ThrottlingExceptionRoutePolicy
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
name|support
operator|.
name|ServiceSupport
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|awaitility
operator|.
name|Awaitility
operator|.
name|await
import|;
end_import

begin_class
DECL|class|ThrottlingExceptionRoutePolicyHalfOpenHandlerSedaTest
specifier|public
class|class
name|ThrottlingExceptionRoutePolicyHalfOpenHandlerSedaTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|log
specifier|private
specifier|static
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ThrottlingExceptionRoutePolicyHalfOpenHandlerSedaTest
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|url
specifier|private
name|String
name|url
init|=
literal|"seda:foo?concurrentConsumers=2"
decl_stmt|;
DECL|field|result
specifier|private
name|MockEndpoint
name|result
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
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|this
operator|.
name|setUseRouteBuilder
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|result
operator|=
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|context
operator|.
name|getShutdownStrategy
argument_list|()
operator|.
name|setTimeout
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testHalfOpenCircuit ()
specifier|public
name|void
name|testHalfOpenCircuit
parameter_list|()
throws|throws
name|Exception
block|{
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|bodies
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"Message One"
argument_list|,
literal|"Message Two"
argument_list|)
decl_stmt|;
name|result
operator|.
name|expectedBodiesReceivedInAnyOrder
argument_list|(
name|bodies
argument_list|)
expr_stmt|;
name|result
operator|.
name|whenAnyExchangeReceived
argument_list|(
operator|new
name|Processor
argument_list|()
block|{
annotation|@
name|Override
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
name|msg
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
name|setException
argument_list|(
operator|new
name|ThrottlingException
argument_list|(
name|msg
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
comment|// send two messages which will fail
name|sendMessage
argument_list|(
literal|"Message One"
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"Message Two"
argument_list|)
expr_stmt|;
specifier|final
name|ServiceSupport
name|consumer
init|=
operator|(
name|ServiceSupport
operator|)
name|context
operator|.
name|getRoute
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|getConsumer
argument_list|()
decl_stmt|;
comment|// wait long enough to have the consumer suspended
name|await
argument_list|()
operator|.
name|atMost
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|.
name|until
argument_list|(
name|consumer
operator|::
name|isSuspended
argument_list|)
expr_stmt|;
comment|// send more messages
comment|// but should get there (yet)
comment|// due to open circuit
comment|// SEDA will queue it up
name|log
operator|.
name|debug
argument_list|(
literal|"sending message three"
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"Message Three"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|result
operator|.
name|reset
argument_list|()
expr_stmt|;
name|result
operator|.
name|expectedMessageCount
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|bodies
operator|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"Message Three"
argument_list|,
literal|"Message Four"
argument_list|)
expr_stmt|;
name|result
operator|.
name|expectedBodiesReceivedInAnyOrder
argument_list|(
name|bodies
argument_list|)
expr_stmt|;
comment|// wait long enough to have the consumer resumed
name|await
argument_list|()
operator|.
name|atMost
argument_list|(
literal|2
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
operator|.
name|until
argument_list|(
name|consumer
operator|::
name|isStarted
argument_list|)
expr_stmt|;
comment|// send message
comment|// should get through
name|log
operator|.
name|debug
argument_list|(
literal|"sending message four"
argument_list|)
expr_stmt|;
name|sendMessage
argument_list|(
literal|"Message Four"
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
name|int
name|threshold
init|=
literal|2
decl_stmt|;
name|long
name|failureWindow
init|=
literal|30
decl_stmt|;
name|long
name|halfOpenAfter
init|=
literal|250
decl_stmt|;
name|ThrottlingExceptionRoutePolicy
name|policy
init|=
operator|new
name|ThrottlingExceptionRoutePolicy
argument_list|(
name|threshold
argument_list|,
name|failureWindow
argument_list|,
name|halfOpenAfter
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|policy
operator|.
name|setHalfOpenHandler
argument_list|(
operator|new
name|AlwaysCloseHandler
argument_list|()
argument_list|)
expr_stmt|;
name|from
argument_list|(
name|url
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|routePolicy
argument_list|(
name|policy
argument_list|)
operator|.
name|log
argument_list|(
literal|"${body}"
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:foo?groupSize=10"
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
DECL|class|AlwaysCloseHandler
specifier|public
class|class
name|AlwaysCloseHandler
implements|implements
name|ThrottlingExceptionHalfOpenHandler
block|{
annotation|@
name|Override
DECL|method|isReadyToBeClosed ()
specifier|public
name|boolean
name|isReadyToBeClosed
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
DECL|method|sendMessage (String bodyText)
specifier|protected
name|void
name|sendMessage
parameter_list|(
name|String
name|bodyText
parameter_list|)
block|{
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
name|url
argument_list|,
name|bodyText
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Error sending:"
operator|+
name|e
operator|.
name|getCause
argument_list|()
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

