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
name|RejectedExecutionException
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
name|ThreadPoolExecutor
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
name|AtomicBoolean
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
name|AsyncCallback
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
name|AsyncProcessor
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
name|Rejectable
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
name|ThreadPoolRejectedPolicy
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
name|spi
operator|.
name|IdAware
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
name|support
operator|.
name|ServiceSupport
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
name|AsyncProcessorHelper
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
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * Threads processor that leverage a thread pool for continue processing the {@link Exchange}s  * using the asynchronous routing engine.  *<p/>  *<b>Notice:</b> For transacted routes then this {@link ThreadsProcessor} is not in use, as we want to  * process messages using the same thread to support all work done in the same transaction. The reason  * is that the transaction manager that orchestrate the transaction, requires all the work to be done  * on the same thread.  *<p/>  * Pay attention to how this processor handles rejected tasks.  *<ul>  *<li>Abort - The current exchange will be set with a {@link RejectedExecutionException} exception,  * and marked to stop continue routing.  * The {@link org.apache.camel.spi.UnitOfWork} will be regarded as<b>failed</b>, due the exception.</li>  *<li>Discard - The current exchange will be marked to stop continue routing (notice no exception is set).  * The {@link org.apache.camel.spi.UnitOfWork} will be regarded as<b>successful</b>, due no exception being set.</li>  *<li>DiscardOldest - The oldest exchange will be marked to stop continue routing (notice no exception is set).  * The {@link org.apache.camel.spi.UnitOfWork} will be regarded as<b>successful</b>, due no exception being set.  * And the current exchange will be added to the task queue.</li>  *<li>CallerRuns - The current exchange will be processed by the current thread. Which mean the current thread  * will not be free to process a new exchange, as its processing the current exchange.</li>  *</ul>  */
end_comment

begin_class
DECL|class|ThreadsProcessor
specifier|public
class|class
name|ThreadsProcessor
extends|extends
name|ServiceSupport
implements|implements
name|AsyncProcessor
implements|,
name|IdAware
block|{
DECL|field|id
specifier|private
name|String
name|id
decl_stmt|;
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|executorService
specifier|private
specifier|final
name|ExecutorService
name|executorService
decl_stmt|;
DECL|field|rejectedPolicy
specifier|private
specifier|final
name|ThreadPoolRejectedPolicy
name|rejectedPolicy
decl_stmt|;
DECL|field|shutdownExecutorService
specifier|private
specifier|volatile
name|boolean
name|shutdownExecutorService
decl_stmt|;
DECL|field|shutdown
specifier|private
specifier|final
name|AtomicBoolean
name|shutdown
init|=
operator|new
name|AtomicBoolean
argument_list|(
literal|true
argument_list|)
decl_stmt|;
DECL|class|ProcessCall
specifier|private
specifier|final
class|class
name|ProcessCall
implements|implements
name|Runnable
implements|,
name|Rejectable
block|{
DECL|field|exchange
specifier|private
specifier|final
name|Exchange
name|exchange
decl_stmt|;
DECL|field|callback
specifier|private
specifier|final
name|AsyncCallback
name|callback
decl_stmt|;
DECL|field|done
specifier|private
specifier|final
name|boolean
name|done
decl_stmt|;
DECL|method|ProcessCall (Exchange exchange, AsyncCallback callback, boolean done)
name|ProcessCall
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|,
name|boolean
name|done
parameter_list|)
block|{
name|this
operator|.
name|exchange
operator|=
name|exchange
expr_stmt|;
name|this
operator|.
name|callback
operator|=
name|callback
expr_stmt|;
name|this
operator|.
name|done
operator|=
name|done
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
name|log
operator|.
name|trace
argument_list|(
literal|"Continue routing exchange {}"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|shutdown
operator|.
name|get
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|RejectedExecutionException
argument_list|(
literal|"ThreadsProcessor is not running."
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|callback
operator|.
name|done
argument_list|(
name|done
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|reject ()
specifier|public
name|void
name|reject
parameter_list|()
block|{
comment|// reject should mark the exchange with an rejected exception and mark not to route anymore
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|RejectedExecutionException
argument_list|()
argument_list|)
expr_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Rejected routing exchange {}"
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|shutdown
operator|.
name|get
argument_list|()
condition|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
operator|new
name|RejectedExecutionException
argument_list|(
literal|"ThreadsProcessor is not running."
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|callback
operator|.
name|done
argument_list|(
name|done
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"ProcessCall["
operator|+
name|exchange
operator|+
literal|"]"
return|;
block|}
block|}
DECL|method|ThreadsProcessor (CamelContext camelContext, ExecutorService executorService, boolean shutdownExecutorService, ThreadPoolRejectedPolicy rejectedPolicy)
specifier|public
name|ThreadsProcessor
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|ExecutorService
name|executorService
parameter_list|,
name|boolean
name|shutdownExecutorService
parameter_list|,
name|ThreadPoolRejectedPolicy
name|rejectedPolicy
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"camelContext"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|executorService
argument_list|,
literal|"executorService"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|rejectedPolicy
argument_list|,
literal|"rejectedPolicy"
argument_list|)
expr_stmt|;
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|executorService
operator|=
name|executorService
expr_stmt|;
name|this
operator|.
name|shutdownExecutorService
operator|=
name|shutdownExecutorService
expr_stmt|;
name|this
operator|.
name|rejectedPolicy
operator|=
name|rejectedPolicy
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
name|AsyncProcessorHelper
operator|.
name|process
argument_list|(
name|this
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
DECL|method|process (Exchange exchange, AsyncCallback callback)
specifier|public
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
block|{
if|if
condition|(
name|shutdown
operator|.
name|get
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"ThreadsProcessor is not running."
argument_list|)
throw|;
block|}
comment|// we cannot execute this asynchronously for transacted exchanges, as the transaction manager doesn't support
comment|// using different threads in the same transaction
if|if
condition|(
name|exchange
operator|.
name|isTransacted
argument_list|()
condition|)
block|{
name|log
operator|.
name|trace
argument_list|(
literal|"Transacted Exchange must be routed synchronously for exchangeId: {} -> {}"
argument_list|,
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
try|try
block|{
comment|// process the call in asynchronous mode
name|ProcessCall
name|call
init|=
operator|new
name|ProcessCall
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|log
operator|.
name|trace
argument_list|(
literal|"Submitting task {}"
argument_list|,
name|call
argument_list|)
expr_stmt|;
name|executorService
operator|.
name|submit
argument_list|(
name|call
argument_list|)
expr_stmt|;
comment|// tell Camel routing engine we continue routing asynchronous
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
if|if
condition|(
name|executorService
operator|instanceof
name|ThreadPoolExecutor
condition|)
block|{
name|ThreadPoolExecutor
name|tpe
init|=
operator|(
name|ThreadPoolExecutor
operator|)
name|executorService
decl_stmt|;
comment|// process the call in synchronous mode
name|ProcessCall
name|call
init|=
operator|new
name|ProcessCall
argument_list|(
name|exchange
argument_list|,
name|callback
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|rejectedPolicy
operator|.
name|asRejectedExecutionHandler
argument_list|()
operator|.
name|rejectedExecution
argument_list|(
name|call
argument_list|,
name|tpe
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
else|else
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
block|}
block|}
DECL|method|getExecutorService ()
specifier|public
name|ExecutorService
name|getExecutorService
parameter_list|()
block|{
return|return
name|executorService
return|;
block|}
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Threads"
return|;
block|}
DECL|method|getId ()
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
DECL|method|setId (String id)
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
DECL|method|getRejectedPolicy ()
specifier|public
name|ThreadPoolRejectedPolicy
name|getRejectedPolicy
parameter_list|()
block|{
return|return
name|rejectedPolicy
return|;
block|}
DECL|method|doStart ()
specifier|protected
name|void
name|doStart
parameter_list|()
throws|throws
name|Exception
block|{
name|shutdown
operator|.
name|set
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
DECL|method|doStop ()
specifier|protected
name|void
name|doStop
parameter_list|()
throws|throws
name|Exception
block|{
name|shutdown
operator|.
name|set
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
DECL|method|doShutdown ()
specifier|protected
name|void
name|doShutdown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|shutdownExecutorService
condition|)
block|{
name|camelContext
operator|.
name|getExecutorServiceManager
argument_list|()
operator|.
name|shutdownNow
argument_list|(
name|executorService
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|doShutdown
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

