begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.issues
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|issues
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
name|CamelExecutionException
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
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/*  */
end_comment

begin_class
DECL|class|ExceptionThrownFromOnExceptionTest
specifier|public
class|class
name|ExceptionThrownFromOnExceptionTest
extends|extends
name|ContextTestSupport
block|{
DECL|field|RETRY
specifier|private
specifier|static
specifier|final
name|AtomicInteger
name|RETRY
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
DECL|field|ON_EXCEPTION_RETRY
specifier|private
specifier|static
specifier|final
name|AtomicInteger
name|ON_EXCEPTION_RETRY
init|=
operator|new
name|AtomicInteger
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|isUseRouteBuilder ()
specifier|public
name|boolean
name|isUseRouteBuilder
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Test
DECL|method|testExceptionThrownFromOnException ()
specifier|public
name|void
name|testExceptionThrownFromOnException
parameter_list|()
throws|throws
name|Exception
block|{
name|RETRY
operator|.
name|set
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|ON_EXCEPTION_RETRY
operator|.
name|set
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
comment|// on exception to catch all IO exceptions and handle them specially
name|onException
argument_list|(
name|IOException
operator|.
name|class
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|3
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|process
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
name|ON_EXCEPTION_RETRY
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Some other IOException"
argument_list|)
throw|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:c"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:intermediate"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:intermediate"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|process
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
name|RETRY
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
literal|"IO error"
argument_list|)
throw|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:end"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:c"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:end"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|IOException
name|cause
init|=
name|assertIsInstanceOf
argument_list|(
name|IOException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Some other IOException"
argument_list|,
name|cause
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should try 4 times (1 first, 3 retry)"
argument_list|,
literal|4
argument_list|,
name|RETRY
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should only invoke onException once"
argument_list|,
literal|1
argument_list|,
name|ON_EXCEPTION_RETRY
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExceptionThrownFromOnExceptionAndHandled ()
specifier|public
name|void
name|testExceptionThrownFromOnExceptionAndHandled
parameter_list|()
throws|throws
name|Exception
block|{
name|RETRY
operator|.
name|set
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|ON_EXCEPTION_RETRY
operator|.
name|set
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
comment|// on exception to catch all IO exceptions and handle them specially
name|onException
argument_list|(
name|IOException
operator|.
name|class
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|3
argument_list|)
comment|// this time we handle the exception
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|process
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
name|ON_EXCEPTION_RETRY
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Some other IOException"
argument_list|)
throw|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:c"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:intermediate"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:intermediate"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|process
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
name|RETRY
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
literal|"IO error"
argument_list|)
throw|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:end"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:c"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:end"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|IOException
name|cause
init|=
name|assertIsInstanceOf
argument_list|(
name|IOException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Some other IOException"
argument_list|,
name|cause
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should try 4 times (1 first, 3 retry)"
argument_list|,
literal|4
argument_list|,
name|RETRY
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should only invoke onException once"
argument_list|,
literal|1
argument_list|,
name|ON_EXCEPTION_RETRY
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExceptionThrownFromOnExceptionWithDeadLetterChannel ()
specifier|public
name|void
name|testExceptionThrownFromOnExceptionWithDeadLetterChannel
parameter_list|()
throws|throws
name|Exception
block|{
name|RETRY
operator|.
name|set
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|ON_EXCEPTION_RETRY
operator|.
name|set
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
comment|// DLC
name|deadLetterChannel
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|3
argument_list|)
expr_stmt|;
comment|// on exception to catch all IO exceptions and handle them specially
name|onException
argument_list|(
name|IOException
operator|.
name|class
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|3
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|process
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
name|ON_EXCEPTION_RETRY
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Some other IOException"
argument_list|)
throw|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:c"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:intermediate"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:intermediate"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|process
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
name|RETRY
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
literal|"IO error"
argument_list|)
throw|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:end"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:c"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:end"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|// the error will not be handled by DLC since we had an onException, and that failed,
comment|// so the exchange will throw an exception
name|getMockEndpoint
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|IOException
name|cause
init|=
name|assertIsInstanceOf
argument_list|(
name|IOException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Some other IOException"
argument_list|,
name|cause
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should try 4 times (1 first, 3 retry)"
argument_list|,
literal|4
argument_list|,
name|RETRY
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should only invoke onException once"
argument_list|,
literal|1
argument_list|,
name|ON_EXCEPTION_RETRY
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExceptionThrownFromOnExceptionAndHandledWithDeadLetterChannel ()
specifier|public
name|void
name|testExceptionThrownFromOnExceptionAndHandledWithDeadLetterChannel
parameter_list|()
throws|throws
name|Exception
block|{
name|RETRY
operator|.
name|set
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|ON_EXCEPTION_RETRY
operator|.
name|set
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
comment|// DLC
name|deadLetterChannel
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|3
argument_list|)
expr_stmt|;
comment|// on exception to catch all IO exceptions and handle them specially
name|onException
argument_list|(
name|IOException
operator|.
name|class
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|3
argument_list|)
comment|// this time we handle the exception
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|process
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
name|ON_EXCEPTION_RETRY
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Some other IOException"
argument_list|)
throw|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:c"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:intermediate"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:intermediate"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|process
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
name|RETRY
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
literal|"IO error"
argument_list|)
throw|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:end"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:c"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:end"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|// the error will not be handled by DLC since we had an onException, and that failed,
comment|// so the exchange will throw an exception
name|getMockEndpoint
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
name|IOException
name|cause
init|=
name|assertIsInstanceOf
argument_list|(
name|IOException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Some other IOException"
argument_list|,
name|cause
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should try 4 times (1 first, 3 retry)"
argument_list|,
literal|4
argument_list|,
name|RETRY
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should only invoke onException once"
argument_list|,
literal|1
argument_list|,
name|ON_EXCEPTION_RETRY
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNoExceptionThrownFromOnExceptionWithDeadLetterChannel ()
specifier|public
name|void
name|testNoExceptionThrownFromOnExceptionWithDeadLetterChannel
parameter_list|()
throws|throws
name|Exception
block|{
name|RETRY
operator|.
name|set
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|ON_EXCEPTION_RETRY
operator|.
name|set
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
comment|// DLC
name|deadLetterChannel
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|3
argument_list|)
expr_stmt|;
comment|// on exception to catch all IO exceptions and handle them specially
name|onException
argument_list|(
name|IOException
operator|.
name|class
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|3
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|process
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
name|ON_EXCEPTION_RETRY
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
comment|// no exception is thrown this time
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:c"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:intermediate"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:intermediate"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|process
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
name|RETRY
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
literal|"IO error"
argument_list|)
throw|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:end"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:c"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:end"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|// the exception is handled by the onException and thus not the DLC
name|getMockEndpoint
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|// and this time there was no exception thrown from onException,
comment|// but the caller still fails since handled is false on onException
try|try
block|{
name|template
operator|.
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
literal|"Hello World"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|CamelExecutionException
name|e
parameter_list|)
block|{
comment|// this time its the first exception thrown from the route
name|IOException
name|cause
init|=
name|assertIsInstanceOf
argument_list|(
name|IOException
operator|.
name|class
argument_list|,
name|e
operator|.
name|getCause
argument_list|()
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"IO error"
argument_list|,
name|cause
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should try 4 times (1 first, 3 retry)"
argument_list|,
literal|4
argument_list|,
name|RETRY
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should only invoke onException once"
argument_list|,
literal|1
argument_list|,
name|ON_EXCEPTION_RETRY
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNoExceptionThrownFromOnExceptionAndHandledWithDeadLetterChannel ()
specifier|public
name|void
name|testNoExceptionThrownFromOnExceptionAndHandledWithDeadLetterChannel
parameter_list|()
throws|throws
name|Exception
block|{
name|RETRY
operator|.
name|set
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|ON_EXCEPTION_RETRY
operator|.
name|set
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|context
operator|.
name|addRoutes
argument_list|(
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
comment|// DLC
name|deadLetterChannel
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|3
argument_list|)
expr_stmt|;
comment|// on exception to catch all IO exceptions and handle them specially
name|onException
argument_list|(
name|IOException
operator|.
name|class
argument_list|)
operator|.
name|redeliveryDelay
argument_list|(
literal|0
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
literal|3
argument_list|)
comment|// we now handle the exception
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|process
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
name|ON_EXCEPTION_RETRY
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
comment|// no exception is thrown this time
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:c"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"direct:intermediate"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:intermediate"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|process
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
name|RETRY
operator|.
name|incrementAndGet
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
literal|"IO error"
argument_list|)
throw|;
block|}
block|}
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:end"
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|context
operator|.
name|start
argument_list|()
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:a"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:b"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:c"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|getMockEndpoint
argument_list|(
literal|"mock:end"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|// the exception is handled by onException so it goes not in DLC
name|getMockEndpoint
argument_list|(
literal|"mock:error"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
comment|// and this time there was no exception thrown from onException,
comment|// and the exception is handled so the caller should not fail
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
name|assertEquals
argument_list|(
literal|"Should try 4 times (1 first, 3 retry)"
argument_list|,
literal|4
argument_list|,
name|RETRY
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should only invoke onException once"
argument_list|,
literal|1
argument_list|,
name|ON_EXCEPTION_RETRY
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
