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
name|util
operator|.
name|ArrayList
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
name|List
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
name|ProducerTemplate
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

begin_comment
comment|/**  * @version   */
end_comment

begin_class
annotation|@
name|Ignore
argument_list|(
literal|"This test can cause CI servers to hang"
argument_list|)
DECL|class|Netty2978IssueTest
specifier|public
class|class
name|Netty2978IssueTest
extends|extends
name|BaseNettyTest
block|{
annotation|@
name|Test
DECL|method|testNetty2978 ()
specifier|public
name|void
name|testNetty2978
parameter_list|()
throws|throws
name|Exception
block|{
name|CamelClient
name|client
init|=
operator|new
name|CamelClient
argument_list|(
name|context
argument_list|)
decl_stmt|;
try|try
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|1000
condition|;
name|i
operator|++
control|)
block|{
name|Object
name|reply
init|=
name|client
operator|.
name|lookup
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye "
operator|+
name|i
argument_list|,
name|reply
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|client
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
DECL|method|testNetty2978Concurrent ()
specifier|public
name|void
name|testNetty2978Concurrent
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|CamelClient
name|client
init|=
operator|new
name|CamelClient
argument_list|(
name|context
argument_list|)
decl_stmt|;
try|try
block|{
specifier|final
name|List
argument_list|<
name|Callable
argument_list|<
name|String
argument_list|>
argument_list|>
name|callables
init|=
operator|new
name|ArrayList
argument_list|<
name|Callable
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|count
init|=
literal|0
init|;
name|count
operator|<
literal|1000
condition|;
name|count
operator|++
control|)
block|{
specifier|final
name|int
name|i
init|=
name|count
decl_stmt|;
name|callables
operator|.
name|add
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
block|{
return|return
name|client
operator|.
name|lookup
argument_list|(
name|i
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|final
name|ExecutorService
name|executorService
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
literal|10
argument_list|)
decl_stmt|;
specifier|final
name|List
argument_list|<
name|Future
argument_list|<
name|String
argument_list|>
argument_list|>
name|results
init|=
name|executorService
operator|.
name|invokeAll
argument_list|(
name|callables
argument_list|)
decl_stmt|;
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|replies
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
name|results
control|)
block|{
comment|// wait at most 60 sec to not hang test
name|String
name|reply
init|=
name|future
operator|.
name|get
argument_list|(
literal|60
argument_list|,
name|TimeUnit
operator|.
name|SECONDS
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|reply
operator|.
name|startsWith
argument_list|(
literal|"Bye "
argument_list|)
argument_list|)
expr_stmt|;
name|replies
operator|.
name|add
argument_list|(
name|reply
argument_list|)
expr_stmt|;
block|}
comment|// should be 1000 unique replies
name|assertEquals
argument_list|(
literal|1000
argument_list|,
name|replies
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|executorService
operator|.
name|shutdownNow
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|client
operator|.
name|close
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
name|from
argument_list|(
literal|"netty4:tcp://localhost:{{port}}?sync=true"
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
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
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
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
literal|"Bye "
operator|+
name|body
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
DECL|class|CamelClient
specifier|private
specifier|static
specifier|final
class|class
name|CamelClient
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|Endpoint
name|endpoint
decl_stmt|;
DECL|field|producerTemplate
specifier|private
specifier|final
name|ProducerTemplate
name|producerTemplate
decl_stmt|;
DECL|method|CamelClient (CamelContext camelContext)
name|CamelClient
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|camelContext
operator|.
name|getEndpoint
argument_list|(
literal|"netty4:tcp://localhost:{{port}}?sync=true"
argument_list|)
expr_stmt|;
name|this
operator|.
name|producerTemplate
operator|=
name|camelContext
operator|.
name|createProducerTemplate
argument_list|()
expr_stmt|;
block|}
DECL|method|close ()
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|Exception
block|{
name|producerTemplate
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
DECL|method|lookup (int num)
specifier|public
name|String
name|lookup
parameter_list|(
name|int
name|num
parameter_list|)
block|{
return|return
name|producerTemplate
operator|.
name|requestBody
argument_list|(
name|endpoint
argument_list|,
name|num
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

