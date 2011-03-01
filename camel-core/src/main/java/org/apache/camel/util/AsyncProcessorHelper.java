begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.util
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
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
name|CountDownLatch
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
comment|/**  * Helper methods for {@link AsyncProcessor} objects.  */
end_comment

begin_class
DECL|class|AsyncProcessorHelper
specifier|public
specifier|final
class|class
name|AsyncProcessorHelper
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
specifier|transient
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|AsyncProcessorHelper
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|AsyncProcessorHelper ()
specifier|private
name|AsyncProcessorHelper
parameter_list|()
block|{
comment|// utility class
block|}
comment|/**      * Calls the async version of the processor's process method.      *<p/>      * This implementation supports transacted {@link Exchange}s which ensure those are run in a synchronous fashion.      * See more details at {@link org.apache.camel.AsyncProcessor}.      *      * @param processor the processor      * @param exchange  the exchange      * @param callback  the callback      * @return<tt>true</tt> to continue execute synchronously,<tt>false</tt> to continue being executed asynchronously      */
DECL|method|process (final AsyncProcessor processor, final Exchange exchange, final AsyncCallback callback)
specifier|public
specifier|static
name|boolean
name|process
parameter_list|(
specifier|final
name|AsyncProcessor
name|processor
parameter_list|,
specifier|final
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|AsyncCallback
name|callback
parameter_list|)
block|{
name|boolean
name|sync
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|isTransacted
argument_list|()
condition|)
block|{
comment|// must be synchronized for transacted exchanges
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Transacted Exchange must be routed synchronously for exchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|" -> "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|process
argument_list|(
name|processor
argument_list|,
name|exchange
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|exchange
operator|.
name|setException
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
name|callback
operator|.
name|done
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|sync
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
comment|// allow unit of work to wrap callback in case it need to do some special work
comment|// for example the MDCUnitOfWork
name|AsyncCallback
name|async
init|=
name|callback
decl_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|async
operator|=
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|.
name|beforeProcess
argument_list|(
name|processor
argument_list|,
name|exchange
argument_list|,
name|callback
argument_list|)
expr_stmt|;
block|}
comment|// we support asynchronous routing so invoke it
name|sync
operator|=
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|,
name|async
argument_list|)
expr_stmt|;
comment|// execute any after processor work
if|if
condition|(
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|exchange
operator|.
name|getUnitOfWork
argument_list|()
operator|.
name|afterProcess
argument_list|(
name|processor
argument_list|,
name|exchange
argument_list|,
name|callback
argument_list|,
name|sync
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Exchange processed and is continued routed "
operator|+
operator|(
name|sync
condition|?
literal|"synchronously"
else|:
literal|"asynchronously"
operator|)
operator|+
literal|" for exchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|" -> "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
return|return
name|sync
return|;
block|}
comment|/**      * Calls the async version of the processor's process method and waits      * for it to complete before returning. This can be used by {@link AsyncProcessor}      * objects to implement their sync version of the process method.      *      * @param processor the processor      * @param exchange  the exchange      * @throws Exception can be thrown if waiting is interrupted      */
DECL|method|process (final AsyncProcessor processor, final Exchange exchange)
specifier|public
specifier|static
name|void
name|process
parameter_list|(
specifier|final
name|AsyncProcessor
name|processor
parameter_list|,
specifier|final
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|CountDownLatch
name|latch
init|=
operator|new
name|CountDownLatch
argument_list|(
literal|1
argument_list|)
decl_stmt|;
specifier|final
name|AsyncCallback
name|callback
init|=
operator|new
name|AsyncCallback
argument_list|()
block|{
specifier|public
name|void
name|done
parameter_list|(
name|boolean
name|doneSync
parameter_list|)
block|{
if|if
condition|(
operator|!
name|doneSync
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Asynchronous callback received for exchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|latch
operator|.
name|countDown
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"Done "
operator|+
name|processor
return|;
block|}
block|}
decl_stmt|;
name|boolean
name|sync
init|=
name|process
argument_list|(
name|processor
argument_list|,
name|exchange
argument_list|,
name|callback
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|sync
condition|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Waiting for asynchronous callback before continuing for exchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|" -> "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
name|latch
operator|.
name|await
argument_list|()
expr_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isTraceEnabled
argument_list|()
condition|)
block|{
name|LOG
operator|.
name|trace
argument_list|(
literal|"Asynchronous callback received, will continue routing exchangeId: "
operator|+
name|exchange
operator|.
name|getExchangeId
argument_list|()
operator|+
literal|" -> "
operator|+
name|exchange
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Processes the exchange async.      *      * @param executor  executor service      * @param processor the processor      * @param exchange  the exchange      * @return a future handle for the task being executed asynchronously      * @deprecated will be removed in Camel 2.5      */
annotation|@
name|Deprecated
DECL|method|asyncProcess (final ExecutorService executor, final Processor processor, final Exchange exchange)
specifier|public
specifier|static
name|Future
argument_list|<
name|Exchange
argument_list|>
name|asyncProcess
parameter_list|(
specifier|final
name|ExecutorService
name|executor
parameter_list|,
specifier|final
name|Processor
name|processor
parameter_list|,
specifier|final
name|Exchange
name|exchange
parameter_list|)
block|{
name|Callable
argument_list|<
name|Exchange
argument_list|>
name|task
init|=
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
name|processor
operator|.
name|process
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
return|return
name|exchange
return|;
block|}
block|}
decl_stmt|;
return|return
name|executor
operator|.
name|submit
argument_list|(
name|task
argument_list|)
return|;
block|}
block|}
end_class

end_unit

