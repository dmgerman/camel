begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.http4
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|http4
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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|Future
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
name|http
operator|.
name|HttpException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|HttpStatus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|entity
operator|.
name|StringEntity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|localserver
operator|.
name|LocalTestServer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|protocol
operator|.
name|HttpContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|http
operator|.
name|protocol
operator|.
name|HttpRequestHandler
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
comment|/**  * @version   */
end_comment

begin_class
DECL|class|HttpConcurrentTest
specifier|public
class|class
name|HttpConcurrentTest
extends|extends
name|BaseHttpTest
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
name|Override
DECL|method|registerHandler (LocalTestServer server)
specifier|protected
name|void
name|registerHandler
parameter_list|(
name|LocalTestServer
name|server
parameter_list|)
block|{
name|server
operator|.
name|register
argument_list|(
literal|"/"
argument_list|,
operator|new
name|HttpRequestHandler
argument_list|()
block|{
specifier|public
name|void
name|handle
parameter_list|(
name|HttpRequest
name|request
parameter_list|,
name|HttpResponse
name|response
parameter_list|,
name|HttpContext
name|context
parameter_list|)
throws|throws
name|HttpException
throws|,
name|IOException
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
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
name|response
operator|.
name|setStatusCode
argument_list|(
name|HttpStatus
operator|.
name|SC_OK
argument_list|)
expr_stmt|;
name|response
operator|.
name|setEntity
argument_list|(
operator|new
name|StringEntity
argument_list|(
literal|""
operator|+
name|counter
operator|.
name|incrementAndGet
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testNoConcurrentProducers ()
specifier|public
name|void
name|testNoConcurrentProducers
parameter_list|()
throws|throws
name|Exception
block|{
name|doSendMessages
argument_list|(
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
DECL|method|testConcurrentProducers ()
specifier|public
name|void
name|testConcurrentProducers
parameter_list|()
throws|throws
name|Exception
block|{
name|doSendMessages
argument_list|(
literal|10
argument_list|,
literal|5
argument_list|)
expr_stmt|;
block|}
DECL|method|doSendMessages (int files, int poolSize)
specifier|private
name|void
name|doSendMessages
parameter_list|(
name|int
name|files
parameter_list|,
name|int
name|poolSize
parameter_list|)
throws|throws
name|Exception
block|{
name|ExecutorService
name|executor
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
name|poolSize
argument_list|)
decl_stmt|;
comment|// we access the responses Map below only inside the main thread,
comment|// so no need for a thread-safe Map implementation
name|Map
argument_list|<
name|Integer
argument_list|,
name|Future
argument_list|<
name|String
argument_list|>
argument_list|>
name|responses
init|=
operator|new
name|HashMap
argument_list|<
name|Integer
argument_list|,
name|Future
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
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
name|files
condition|;
name|i
operator|++
control|)
block|{
specifier|final
name|int
name|index
init|=
name|i
decl_stmt|;
name|Future
argument_list|<
name|String
argument_list|>
name|out
init|=
name|executor
operator|.
name|submit
argument_list|(
operator|new
name|Callable
argument_list|<
name|String
argument_list|>
argument_list|()
block|{
specifier|public
name|String
name|call
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|template
operator|.
name|requestBody
argument_list|(
literal|"http4://"
operator|+
name|getHostName
argument_list|()
operator|+
literal|":"
operator|+
name|getPort
argument_list|()
argument_list|,
literal|null
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
block|}
argument_list|)
decl_stmt|;
name|responses
operator|.
name|put
argument_list|(
name|index
argument_list|,
name|out
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|files
argument_list|,
name|responses
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// get all responses
name|Set
argument_list|<
name|String
argument_list|>
name|unique
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Future
argument_list|<
name|String
argument_list|>
name|future
range|:
name|responses
operator|.
name|values
argument_list|()
control|)
block|{
name|unique
operator|.
name|add
argument_list|(
name|future
operator|.
name|get
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// should be 'files' unique responses
name|assertEquals
argument_list|(
literal|"Should be "
operator|+
name|files
operator|+
literal|" unique responses"
argument_list|,
name|files
argument_list|,
name|unique
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|executor
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

