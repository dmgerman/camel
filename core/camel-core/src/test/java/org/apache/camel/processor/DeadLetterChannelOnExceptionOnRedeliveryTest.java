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
name|java
operator|.
name|io
operator|.
name|IOException
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
name|Test
import|;
end_import

begin_comment
comment|/**  * Unit test for testing possibility to modify exchange before redelivering  * specific per on exception  */
end_comment

begin_class
DECL|class|DeadLetterChannelOnExceptionOnRedeliveryTest
specifier|public
class|class
name|DeadLetterChannelOnExceptionOnRedeliveryTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|counter
specifier|static
name|int
name|counter
decl_stmt|;
annotation|@
name|Test
DECL|method|testGlobalOnRedelivery ()
specifier|public
name|void
name|testGlobalOnRedelivery
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
name|expectedBodiesReceived
argument_list|(
literal|"Hello World3"
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testRouteSpecificOnRedelivery ()
specifier|public
name|void
name|testRouteSpecificOnRedelivery
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
name|expectedBodiesReceived
argument_list|(
literal|"Hello World"
argument_list|)
expr_stmt|;
name|mock
operator|.
name|message
argument_list|(
literal|0
argument_list|)
operator|.
name|header
argument_list|(
literal|"Timeout"
argument_list|)
operator|.
name|isEqualTo
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:io"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
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
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|counter
operator|=
literal|0
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
comment|// START SNIPPET: e1
comment|// when we redeliver caused by an IOException we want to do some
comment|// special
comment|// code before the redeliver attempt
name|onException
argument_list|(
name|IOException
operator|.
name|class
argument_list|)
comment|// try to redeliver at most 3 times
operator|.
name|maximumRedeliveries
argument_list|(
literal|3
argument_list|)
comment|// setting delay to zero is just to make unit testing faster
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
operator|.
name|onRedelivery
argument_list|(
operator|new
name|MyIORedeliverProcessor
argument_list|()
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e1
comment|// START SNIPPET: e2
comment|// we configure our Dead Letter Channel to invoke
comment|// MyRedeliveryProcessor before a redelivery is
comment|// attempted. This allows us to alter the message before
name|errorHandler
argument_list|(
name|deadLetterChannel
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|5
argument_list|)
operator|.
name|onRedelivery
argument_list|(
operator|new
name|MyRedeliverProcessor
argument_list|()
argument_list|)
comment|// setting delay to zero is just to make unit testing faster
operator|.
name|redeliveryDelay
argument_list|(
literal|0L
argument_list|)
argument_list|)
expr_stmt|;
comment|// END SNIPPET: e2
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|ThrowExceptionProcessor
argument_list|()
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:io"
argument_list|)
operator|.
name|process
argument_list|(
operator|new
name|ThrowIOExceptionProcessor
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
comment|// START SNIPPET: e3
comment|// This is our processor that is executed before every redelivery attempt
comment|// here we can do what we want in the java code, such as altering the
comment|// message
DECL|class|MyRedeliverProcessor
specifier|public
specifier|static
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
comment|// the message is being redelivered so we can alter it
comment|// we just append the redelivery counter to the body
comment|// you can of course do all kind of stuff instead
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
name|int
name|count
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
literal|"CamelRedeliveryCounter"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setBody
argument_list|(
name|body
operator|+
name|count
argument_list|)
expr_stmt|;
block|}
block|}
comment|// END SNIPPET: e3
comment|// START SNIPPET: e4
comment|// This is our processor that is executed before IOException redeliver
comment|// attempt
comment|// here we can do what we want in the java code, such as altering the
comment|// message
DECL|class|MyIORedeliverProcessor
specifier|public
specifier|static
class|class
name|MyIORedeliverProcessor
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
comment|// just for show and tell, here we set a special header to instruct
comment|// the receive a given timeout value
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
literal|"Timeout"
argument_list|,
literal|5000
argument_list|)
expr_stmt|;
block|}
block|}
comment|// END SNIPPET: e4
DECL|class|ThrowExceptionProcessor
specifier|public
specifier|static
class|class
name|ThrowExceptionProcessor
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
comment|// force some error so Camel will do redelivery
if|if
condition|(
operator|++
name|counter
operator|<=
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
block|}
DECL|class|ThrowIOExceptionProcessor
specifier|public
specifier|static
class|class
name|ThrowIOExceptionProcessor
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
comment|// force some error so Camel will do redelivery
if|if
condition|(
operator|++
name|counter
operator|<=
literal|3
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Cannot connect"
argument_list|)
throw|;
block|}
block|}
block|}
block|}
end_class

end_unit

