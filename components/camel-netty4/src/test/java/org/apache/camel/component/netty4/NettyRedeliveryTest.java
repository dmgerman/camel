begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty4
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
name|lang
operator|.
name|reflect
operator|.
name|InvocationHandler
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Proxy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|ServerSocket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|Socket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|SocketTimeoutException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Deque
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
name|Callable
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
name|ExecutorService
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
name|Executors
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
name|LinkedBlockingDeque
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
name|ScheduledExecutorService
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
name|CamelContext
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
name|EndpointInject
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
name|DefaultCamelContext
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
name|test
operator|.
name|junit4
operator|.
name|CamelTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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
comment|/**  * Test the effect of redelivery in association with netty component.  */
end_comment

begin_class
DECL|class|NettyRedeliveryTest
specifier|public
class|class
name|NettyRedeliveryTest
extends|extends
name|CamelTestSupport
block|{
comment|/**      * Body of sufficient size such that it doesn't fit into the TCP buffer and has to be read.      */
DECL|field|LARGE_BUFFER_BODY
specifier|private
specifier|static
specifier|final
name|byte
index|[]
name|LARGE_BUFFER_BODY
init|=
operator|new
name|byte
index|[
literal|1000000
index|]
decl_stmt|;
comment|/**      * Failure will occur with 2 redeliveries however is increasingly more likely the more it retries.      */
DECL|field|REDELIVERY_COUNT
specifier|private
specifier|static
specifier|final
name|int
name|REDELIVERY_COUNT
init|=
literal|100
decl_stmt|;
DECL|field|listener
specifier|private
name|ExecutorService
name|listener
init|=
name|Executors
operator|.
name|newSingleThreadExecutor
argument_list|()
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:exception"
argument_list|)
DECL|field|exception
specifier|private
name|MockEndpoint
name|exception
decl_stmt|;
annotation|@
name|EndpointInject
argument_list|(
name|uri
operator|=
literal|"mock:downstream"
argument_list|)
DECL|field|downstream
specifier|private
name|MockEndpoint
name|downstream
decl_stmt|;
DECL|field|tasks
specifier|private
name|Deque
argument_list|<
name|Callable
argument_list|<
name|?
argument_list|>
argument_list|>
name|tasks
init|=
operator|new
name|LinkedBlockingDeque
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|port
specifier|private
name|int
name|port
decl_stmt|;
DECL|field|alive
specifier|private
name|boolean
name|alive
init|=
literal|true
decl_stmt|;
annotation|@
name|Override
DECL|method|doPreSetup ()
specifier|protected
name|void
name|doPreSetup
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Create a server to attempt to connect to
name|port
operator|=
name|createServerSocket
argument_list|(
literal|0
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
annotation|@
name|Override
specifier|public
name|void
name|configure
parameter_list|()
throws|throws
name|Exception
block|{
name|onException
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
operator|.
name|maximumRedeliveries
argument_list|(
name|REDELIVERY_COUNT
argument_list|)
operator|.
name|retryAttemptedLogLevel
argument_list|(
name|LoggingLevel
operator|.
name|INFO
argument_list|)
operator|.
name|retriesExhaustedLogLevel
argument_list|(
name|LoggingLevel
operator|.
name|ERROR
argument_list|)
comment|// lets have a little delay so we do async redelivery
operator|.
name|redeliveryDelay
argument_list|(
literal|10
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:exception"
argument_list|)
operator|.
name|handled
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"direct:start"
argument_list|)
operator|.
name|routeId
argument_list|(
literal|"start"
argument_list|)
operator|.
name|to
argument_list|(
literal|"netty4:tcp://localhost:"
operator|+
name|port
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:downstream"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:downstream"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
annotation|@
name|Override
annotation|@
name|After
DECL|method|tearDown ()
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
name|alive
operator|=
literal|false
expr_stmt|;
name|listener
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testExceptionHandler ()
specifier|public
name|void
name|testExceptionHandler
parameter_list|()
throws|throws
name|Exception
block|{
comment|/*          * We should have 0 for this as it should never be successful however it is usual that this actually returns 1.          *          * This is because two or more threads run concurrently and will setException(null) which is checked during          * redelivery to ascertain whether the delivery was successful, this leads to multiple downstream invocations being          * possible.          */
name|downstream
operator|.
name|setExpectedMessageCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|downstream
operator|.
name|setAssertPeriod
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
name|exception
operator|.
name|setExpectedMessageCount
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|sendBody
argument_list|(
literal|"direct:start"
argument_list|,
name|LARGE_BUFFER_BODY
argument_list|)
expr_stmt|;
name|exception
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
comment|// given 100 retries usually yields somewhere around -95
comment|// assertEquals(0, context.getInflightRepository().size("start"));
comment|// Verify the number of tasks submitted - sometimes both callbacks add a task
name|assertEquals
argument_list|(
name|REDELIVERY_COUNT
argument_list|,
name|tasks
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Verify the downstream completed messages - othertimes one callback gets treated as done
name|downstream
operator|.
name|assertIsSatisfied
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|createCamelContext ()
specifier|protected
name|CamelContext
name|createCamelContext
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Override the error handler executor service such that we can track the tasks created
name|CamelContext
name|context
init|=
operator|new
name|DefaultCamelContext
argument_list|(
name|createRegistry
argument_list|()
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|ScheduledExecutorService
name|getErrorHandlerExecutorService
parameter_list|()
block|{
return|return
name|getScheduledExecutorService
argument_list|()
return|;
block|}
block|}
decl_stmt|;
return|return
name|context
return|;
block|}
DECL|method|getScheduledExecutorService ()
specifier|private
name|ScheduledExecutorService
name|getScheduledExecutorService
parameter_list|()
block|{
specifier|final
name|ScheduledExecutorService
name|delegate
init|=
name|Executors
operator|.
name|newScheduledThreadPool
argument_list|(
literal|10
argument_list|)
decl_stmt|;
return|return
name|newProxy
argument_list|(
name|ScheduledExecutorService
operator|.
name|class
argument_list|,
operator|new
name|InvocationHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Object
name|invoke
parameter_list|(
name|Object
name|proxy
parameter_list|,
name|Method
name|method
parameter_list|,
name|Object
index|[]
name|args
parameter_list|)
throws|throws
name|Throwable
block|{
if|if
condition|(
literal|"submit"
operator|.
name|equals
argument_list|(
name|method
operator|.
name|getName
argument_list|()
argument_list|)
operator|||
literal|"schedule"
operator|.
name|equals
argument_list|(
name|method
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|tasks
operator|.
name|add
argument_list|(
operator|(
name|Callable
argument_list|<
name|?
argument_list|>
operator|)
name|args
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
return|return
name|method
operator|.
name|invoke
argument_list|(
name|delegate
argument_list|,
name|args
argument_list|)
return|;
block|}
block|}
argument_list|)
return|;
block|}
DECL|method|createServerSocket (int port)
specifier|private
name|int
name|createServerSocket
parameter_list|(
name|int
name|port
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|ServerSocket
name|listen
init|=
operator|new
name|ServerSocket
argument_list|(
name|port
argument_list|)
decl_stmt|;
name|listen
operator|.
name|setSoTimeout
argument_list|(
literal|100
argument_list|)
expr_stmt|;
name|listener
operator|.
name|execute
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|private
name|ExecutorService
name|pool
init|=
name|Executors
operator|.
name|newCachedThreadPool
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
while|while
condition|(
name|alive
condition|)
block|{
try|try
block|{
name|pool
operator|.
name|execute
argument_list|(
operator|new
name|ClosingClientRunnable
argument_list|(
name|listen
operator|.
name|accept
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SocketTimeoutException
name|ignored
parameter_list|)
block|{
comment|// Allow the server socket to terminate in a timely fashion
block|}
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
try|try
block|{
name|listen
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ignored
parameter_list|)
block|{                     }
block|}
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|listen
operator|.
name|getLocalPort
argument_list|()
return|;
block|}
DECL|method|newProxy (Class<T> interfaceType, InvocationHandler handler)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|newProxy
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|interfaceType
parameter_list|,
name|InvocationHandler
name|handler
parameter_list|)
block|{
name|Object
name|object
init|=
name|Proxy
operator|.
name|newProxyInstance
argument_list|(
name|interfaceType
operator|.
name|getClassLoader
argument_list|()
argument_list|,
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[]
block|{
name|interfaceType
block|}
operator|,
name|handler
block|)
function|;
return|return
name|interfaceType
operator|.
name|cast
argument_list|(
name|object
argument_list|)
return|;
block|}
end_class

begin_comment
comment|/**      * Handler for client connection.      */
end_comment

begin_class
DECL|class|ClosingClientRunnable
specifier|private
class|class
name|ClosingClientRunnable
implements|implements
name|Runnable
block|{
DECL|field|socket
specifier|private
specifier|final
name|Socket
name|socket
decl_stmt|;
DECL|method|ClosingClientRunnable (Socket socket)
name|ClosingClientRunnable
parameter_list|(
name|Socket
name|socket
parameter_list|)
block|{
name|this
operator|.
name|socket
operator|=
name|socket
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|run ()
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|socket
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
try|try
block|{
name|socket
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ignored
parameter_list|)
block|{                 }
block|}
block|}
block|}
end_class

unit|}
end_unit

