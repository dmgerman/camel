begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util.concurrent
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|concurrent
package|;
end_package

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
name|AbstractExecutorService
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

begin_comment
comment|/**  * A synchronous {@link java.util.concurrent.ExecutorService} which always invokes  * the task in the caller thread (just a thread pool facade).  *<p/>  * There is no task queue, and no thread pool. The task will thus always be executed  * by the caller thread in a fully synchronous method invocation.  *<p/>  * This implementation is very simple and does not support waiting for tasks to complete during shutdown.  */
end_comment

begin_class
DECL|class|SynchronousExecutorService
specifier|public
class|class
name|SynchronousExecutorService
extends|extends
name|AbstractExecutorService
block|{
DECL|field|shutdown
specifier|private
specifier|volatile
name|boolean
name|shutdown
decl_stmt|;
annotation|@
name|Override
DECL|method|shutdown ()
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
name|shutdown
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|shutdownNow ()
specifier|public
name|List
argument_list|<
name|Runnable
argument_list|>
name|shutdownNow
parameter_list|()
block|{
comment|// not implemented
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|isShutdown ()
specifier|public
name|boolean
name|isShutdown
parameter_list|()
block|{
return|return
name|shutdown
return|;
block|}
annotation|@
name|Override
DECL|method|isTerminated ()
specifier|public
name|boolean
name|isTerminated
parameter_list|()
block|{
return|return
name|shutdown
return|;
block|}
annotation|@
name|Override
DECL|method|awaitTermination (long time, TimeUnit unit)
specifier|public
name|boolean
name|awaitTermination
parameter_list|(
name|long
name|time
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
throws|throws
name|InterruptedException
block|{
comment|// not implemented
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|execute (Runnable runnable)
specifier|public
name|void
name|execute
parameter_list|(
name|Runnable
name|runnable
parameter_list|)
block|{
comment|// run the task synchronously
name|runnable
operator|.
name|run
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

