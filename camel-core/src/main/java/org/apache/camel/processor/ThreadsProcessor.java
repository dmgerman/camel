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
name|Future
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
name|WaitForTaskToComplete
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
name|ExchangeHelper
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
name|concurrent
operator|.
name|ExecutorServiceHelper
import|;
end_import

begin_comment
comment|/**  * Threads processor that leverage a thread pool for processing exchanges.  *<p/>  * The original caller thread will receive a<tt>Future&lt;Exchange&gt;</tt> in the OUT message body.  * It can then later use this handle to obtain the async response.  *<p/>  * Camel also provides type converters so you can just ask to get the desired object type and Camel  * will automatic wait for the async task to complete to return the response.  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|ThreadsProcessor
specifier|public
class|class
name|ThreadsProcessor
extends|extends
name|DelegateProcessor
implements|implements
name|Processor
block|{
DECL|field|DEFAULT_THREADPOOL_SIZE
specifier|protected
specifier|static
specifier|final
name|int
name|DEFAULT_THREADPOOL_SIZE
init|=
literal|5
decl_stmt|;
DECL|field|executorService
specifier|protected
name|ExecutorService
name|executorService
decl_stmt|;
DECL|field|waitForTaskToComplete
specifier|protected
name|WaitForTaskToComplete
name|waitForTaskToComplete
decl_stmt|;
DECL|method|ThreadsProcessor (Processor output, ExecutorService executorService, WaitForTaskToComplete waitForTaskToComplete)
specifier|public
name|ThreadsProcessor
parameter_list|(
name|Processor
name|output
parameter_list|,
name|ExecutorService
name|executorService
parameter_list|,
name|WaitForTaskToComplete
name|waitForTaskToComplete
parameter_list|)
block|{
name|super
argument_list|(
name|output
argument_list|)
expr_stmt|;
name|this
operator|.
name|executorService
operator|=
name|executorService
expr_stmt|;
name|this
operator|.
name|waitForTaskToComplete
operator|=
name|waitForTaskToComplete
expr_stmt|;
block|}
DECL|method|process (final Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|Processor
name|output
init|=
name|getProcessor
argument_list|()
decl_stmt|;
if|if
condition|(
name|output
operator|==
literal|null
condition|)
block|{
comment|// no output then return
return|return;
block|}
comment|// use a new copy of the exchange to route async and handover the on completion to the new copy
comment|// so its the new copy that performs the on completion callback when its done
specifier|final
name|Exchange
name|copy
init|=
name|exchange
operator|.
name|newCopy
argument_list|(
literal|true
argument_list|)
decl_stmt|;
comment|// let it execute async and return the Future
name|Callable
argument_list|<
name|Exchange
argument_list|>
name|task
init|=
name|createTask
argument_list|(
name|output
argument_list|,
name|copy
argument_list|)
decl_stmt|;
comment|// sumbit the task
name|Future
argument_list|<
name|Exchange
argument_list|>
name|future
init|=
name|getExecutorService
argument_list|()
operator|.
name|submit
argument_list|(
name|task
argument_list|)
decl_stmt|;
comment|// compute if we should wait for task to complete or not
name|WaitForTaskToComplete
name|wait
init|=
name|waitForTaskToComplete
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|ASYNC_WAIT
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|wait
operator|=
name|exchange
operator|.
name|getProperty
argument_list|(
name|Exchange
operator|.
name|ASYNC_WAIT
argument_list|,
name|WaitForTaskToComplete
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|wait
operator|==
name|WaitForTaskToComplete
operator|.
name|Always
condition|)
block|{
comment|// wait for task to complete
name|Exchange
name|response
init|=
name|future
operator|.
name|get
argument_list|()
decl_stmt|;
name|ExchangeHelper
operator|.
name|copyResults
argument_list|(
name|exchange
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|wait
operator|==
name|WaitForTaskToComplete
operator|.
name|IfReplyExpected
operator|&&
name|ExchangeHelper
operator|.
name|isOutCapable
argument_list|(
name|exchange
argument_list|)
condition|)
block|{
comment|// wait for task to complete as we expect a reply
name|Exchange
name|response
init|=
name|future
operator|.
name|get
argument_list|()
decl_stmt|;
name|ExchangeHelper
operator|.
name|copyResults
argument_list|(
name|exchange
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// no we do not expect a reply so lets continue, set a handle to the future task
comment|// in case end user need it later
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|future
argument_list|)
expr_stmt|;
block|}
block|}
DECL|method|createTask (final Processor output, final Exchange copy)
specifier|protected
name|Callable
argument_list|<
name|Exchange
argument_list|>
name|createTask
parameter_list|(
specifier|final
name|Processor
name|output
parameter_list|,
specifier|final
name|Exchange
name|copy
parameter_list|)
block|{
return|return
operator|new
name|Callable
argument_list|<
name|Exchange
argument_list|>
argument_list|()
block|{
specifier|public
name|Exchange
name|call
parameter_list|()
throws|throws
name|Exception
block|{
comment|// must use a copy of the original exchange for processing async
name|output
operator|.
name|process
argument_list|(
name|copy
argument_list|)
expr_stmt|;
return|return
name|copy
return|;
block|}
block|}
return|;
block|}
DECL|method|getExecutorService ()
specifier|public
name|ExecutorService
name|getExecutorService
parameter_list|()
block|{
if|if
condition|(
name|executorService
operator|==
literal|null
condition|)
block|{
name|executorService
operator|=
name|createExecutorService
argument_list|()
expr_stmt|;
block|}
return|return
name|executorService
return|;
block|}
DECL|method|createExecutorService ()
specifier|protected
name|ExecutorService
name|createExecutorService
parameter_list|()
block|{
return|return
name|ExecutorServiceHelper
operator|.
name|newScheduledThreadPool
argument_list|(
name|DEFAULT_THREADPOOL_SIZE
argument_list|,
literal|"AsyncProcessor"
argument_list|,
literal|true
argument_list|)
return|;
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|doStop
argument_list|()
expr_stmt|;
if|if
condition|(
name|executorService
operator|!=
literal|null
condition|)
block|{
name|executorService
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

