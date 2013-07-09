begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.netty
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|netty
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
name|impl
operator|.
name|JndiRegistry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jboss
operator|.
name|netty
operator|.
name|channel
operator|.
name|socket
operator|.
name|nio
operator|.
name|WorkerPool
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
DECL|class|NettyUseSharedWorkerThreadPoolTest
specifier|public
class|class
name|NettyUseSharedWorkerThreadPoolTest
extends|extends
name|BaseNettyTest
block|{
DECL|field|jndi
specifier|private
name|JndiRegistry
name|jndi
decl_stmt|;
DECL|field|shared
specifier|private
name|WorkerPool
name|shared
decl_stmt|;
DECL|field|port
specifier|private
name|int
name|port
decl_stmt|;
DECL|field|port2
specifier|private
name|int
name|port2
decl_stmt|;
DECL|field|port3
specifier|private
name|int
name|port3
decl_stmt|;
annotation|@
name|Override
DECL|method|useJmx ()
specifier|protected
name|boolean
name|useJmx
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
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
name|jndi
operator|=
name|super
operator|.
name|createRegistry
argument_list|()
expr_stmt|;
return|return
name|jndi
return|;
block|}
annotation|@
name|Test
DECL|method|testSharedThreadPool ()
specifier|public
name|void
name|testSharedThreadPool
parameter_list|()
throws|throws
name|Exception
block|{
name|getMockEndpoint
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|expectedMessageCount
argument_list|(
literal|30
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
literal|10
condition|;
name|i
operator|++
control|)
block|{
name|String
name|reply
init|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"netty:tcp://localhost:"
operator|+
name|port
operator|+
literal|"?textline=true&sync=true"
argument_list|,
literal|"Hello World"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bye World"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|reply
operator|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"netty:tcp://localhost:"
operator|+
name|port2
operator|+
literal|"?textline=true&sync=true"
argument_list|,
literal|"Hello Camel"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hi Camel"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
name|reply
operator|=
name|template
operator|.
name|requestBody
argument_list|(
literal|"netty:tcp://localhost:"
operator|+
name|port3
operator|+
literal|"?textline=true&sync=true"
argument_list|,
literal|"Hello Claus"
argument_list|,
name|String
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Hej Claus"
argument_list|,
name|reply
argument_list|)
expr_stmt|;
block|}
name|assertMockEndpointsSatisfied
argument_list|()
expr_stmt|;
name|shared
operator|.
name|shutdown
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
comment|// we have 3 routes, but lets try to have only 2 threads in the pool
name|shared
operator|=
operator|new
name|NettyWorkerPoolBuilder
argument_list|()
operator|.
name|withWorkerCount
argument_list|(
literal|2
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|jndi
operator|.
name|bind
argument_list|(
literal|"sharedPool"
argument_list|,
name|shared
argument_list|)
expr_stmt|;
name|port
operator|=
name|getPort
argument_list|()
expr_stmt|;
name|port2
operator|=
name|getNextPort
argument_list|()
expr_stmt|;
name|port3
operator|=
name|getNextPort
argument_list|()
expr_stmt|;
name|from
argument_list|(
literal|"netty:tcp://localhost:"
operator|+
name|port
operator|+
literal|"?textline=true&sync=true&workerPool=#sharedPool&orderedThreadPoolExecutor=false"
argument_list|)
operator|.
name|validate
argument_list|(
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:result"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|transform
argument_list|(
name|body
argument_list|()
operator|.
name|regexReplaceAll
argument_list|(
literal|"Hello"
argument_list|,
literal|"Bye"
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"netty:tcp://localhost:"
operator|+
name|port2
operator|+
literal|"?textline=true&sync=true&workerPool=#sharedPool&orderedThreadPoolExecutor=false"
argument_list|)
operator|.
name|validate
argument_list|(
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:result"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|transform
argument_list|(
name|body
argument_list|()
operator|.
name|regexReplaceAll
argument_list|(
literal|"Hello"
argument_list|,
literal|"Hi"
argument_list|)
argument_list|)
expr_stmt|;
name|from
argument_list|(
literal|"netty:tcp://localhost:"
operator|+
name|port3
operator|+
literal|"?textline=true&sync=true&workerPool=#sharedPool&orderedThreadPoolExecutor=false"
argument_list|)
operator|.
name|validate
argument_list|(
name|body
argument_list|()
operator|.
name|isInstanceOf
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
operator|.
name|to
argument_list|(
literal|"log:result"
argument_list|)
operator|.
name|to
argument_list|(
literal|"mock:result"
argument_list|)
operator|.
name|transform
argument_list|(
name|body
argument_list|()
operator|.
name|regexReplaceAll
argument_list|(
literal|"Hello"
argument_list|,
literal|"Hej"
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

