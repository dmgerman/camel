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
name|concurrent
operator|.
name|atomic
operator|.
name|AtomicInteger
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
name|LoggingLevel
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
name|util
operator|.
name|StopWatch
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
DECL|class|RedeliveryDeadLetterErrorHandlerNoRedeliveryOnShutdownTest
specifier|public
class|class
name|RedeliveryDeadLetterErrorHandlerNoRedeliveryOnShutdownTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|counter
specifier|private
specifier|final
name|AtomicInteger
name|counter
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
annotation|@
name|Test
DECL|method|testRedeliveryErrorHandlerNoRedeliveryOnShutdown ()
specifier|public
name|void
name|testRedeliveryErrorHandlerNoRedeliveryOnShutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:deadLetter"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:deadLetter"
argument_list|)
operator|.
name|setResultWaitTime
argument_list|(
literal|25000
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"seda:foo"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// should not take long to stop the route
name|StopWatch
name|watch
init|=
operator|new
name|StopWatch
argument_list|()
decl_stmt|;
comment|// sleep 0.5 seconds to do some redeliveries before we stop
name|Thread
operator|.
name|sleep
argument_list|(
literal|500
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"==== stopping route foo ===="
argument_list|)
expr_stmt|;
name|context
operator|.
name|stopRoute
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|long
name|taken
init|=
name|watch
operator|.
name|taken
argument_list|()
decl_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:deadLetter"
argument_list|)
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"OnRedelivery processor counter {}"
argument_list|,
name|counter
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Should stop route faster, was "
operator|+
name|taken
argument_list|,
name|taken
operator|<
literal|5000
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Redelivery counter should be>= 20 and< 100, was: "
operator|+
name|counter
operator|.
name|get
argument_list|()
argument_list|,
name|counter
operator|.
name|get
argument_list|()
operator|>=
literal|20
operator|&&
name|counter
operator|.
name|get
argument_list|()
operator|<
literal|100
argument_list|)
expr_stmt|;
block|}
DECL|class|MyRedeliverProcessor
specifier|private
specifier|final
class|class
name|MyRedeliverProcessor
implements|implements
name|Processor
block|{
annotation|@
name|Override
DECL|method|process (Exchange exchange)
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
name|counter
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
block|}
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
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:deadLetter"
argument_list|)
operator|.
name|allowRedeliveryWhileStopping
argument_list|(
literal|false
argument_list|)
operator|.
name|onRedelivery
argument_list|(
operator|new
name|MyRedeliverProcessor
argument_list|()
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|200
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|10
argument_list|)
operator|.
name|retryAttemptedLogLevel
argument_list|(
name|LoggingLevel
operator|.
name|INFO
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"seda:foo"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"foo"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:foo"
argument_list|)
operator|.
name|throwException
argument_list|(
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Forced"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

