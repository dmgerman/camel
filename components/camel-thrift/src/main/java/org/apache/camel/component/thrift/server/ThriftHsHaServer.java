begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.thrift.server
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|thrift
operator|.
name|server
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
name|ExecutorService
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
name|thrift
operator|.
name|server
operator|.
name|THsHaServer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|thrift
operator|.
name|transport
operator|.
name|TNonblockingServerTransport
import|;
end_import

begin_comment
comment|/*  * Thrift HsHaServer implementation with executors controlled by the Camel Executor Service Manager  */
end_comment

begin_class
DECL|class|ThriftHsHaServer
specifier|public
class|class
name|ThriftHsHaServer
extends|extends
name|THsHaServer
block|{
DECL|class|Args
specifier|public
specifier|static
class|class
name|Args
extends|extends
name|THsHaServer
operator|.
name|Args
block|{
DECL|field|startThreadPool
specifier|private
name|ExecutorService
name|startThreadPool
decl_stmt|;
DECL|field|context
specifier|private
name|CamelContext
name|context
decl_stmt|;
DECL|method|Args (TNonblockingServerTransport transport)
specifier|public
name|Args
parameter_list|(
name|TNonblockingServerTransport
name|transport
parameter_list|)
block|{
name|super
argument_list|(
name|transport
argument_list|)
expr_stmt|;
block|}
DECL|method|startThreadPool (ExecutorService startThreadPool)
specifier|public
name|Args
name|startThreadPool
parameter_list|(
name|ExecutorService
name|startThreadPool
parameter_list|)
block|{
name|this
operator|.
name|startThreadPool
operator|=
name|startThreadPool
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|context (CamelContext context)
specifier|public
name|Args
name|context
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
return|return
name|this
return|;
block|}
block|}
DECL|field|context
specifier|private
specifier|final
name|CamelContext
name|context
decl_stmt|;
DECL|field|startExecutor
specifier|private
specifier|final
name|ExecutorService
name|startExecutor
decl_stmt|;
DECL|method|ThriftHsHaServer (Args args)
specifier|public
name|ThriftHsHaServer
parameter_list|(
name|Args
name|args
parameter_list|)
block|{
name|super
argument_list|(
name|args
argument_list|)
expr_stmt|;
name|this
operator|.
name|context
operator|=
name|args
operator|.
name|context
expr_stmt|;
name|this
operator|.
name|startExecutor
operator|=
name|args
operator|.
name|startThreadPool
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|serve ()
specifier|public
name|void
name|serve
parameter_list|()
throws|throws
name|IllegalArgumentException
block|{
if|if
condition|(
operator|!
name|startThreads
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Failed to start selector thread!"
argument_list|)
throw|;
block|}
if|if
condition|(
operator|!
name|startListening
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Failed to start listening on server socket!"
argument_list|)
throw|;
block|}
name|startExecutor
operator|.
name|execute
argument_list|(
parameter_list|()
lambda|->
block|{
name|setServing
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|waitForShutdown
argument_list|()
expr_stmt|;
name|setServing
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|stopListening
argument_list|()
expr_stmt|;
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|stop ()
specifier|public
name|void
name|stop
parameter_list|()
block|{
name|super
operator|.
name|stop
argument_list|()
expr_stmt|;
name|context
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdownGraceful
argument_list|(
name|startExecutor
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|waitForShutdown ()
specifier|protected
name|void
name|waitForShutdown
parameter_list|()
block|{
name|joinSelector
argument_list|()
expr_stmt|;
name|context
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdownGraceful
argument_list|(
name|getInvoker
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

