begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.spi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|spi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|StaticService
import|;
end_import

begin_comment
comment|/**  * A manager to handle async routing engine, when {@link Exchange}s are being handed over from one thread to another, while  * the callee thread is blocked waiting for the other threads to complete, before it can continue.  *<p/>  * This manager offers insight into the state, and allow to force stuck exchanges to be continued and threads to continue,  * in case of malfunctions.  */
end_comment

begin_interface
DECL|interface|AsyncProcessorAwaitManager
specifier|public
interface|interface
name|AsyncProcessorAwaitManager
extends|extends
name|StaticService
block|{
comment|/**      * Information about the thread and exchange that are inflight.      */
DECL|interface|AwaitThread
interface|interface
name|AwaitThread
block|{
comment|/**          * The thread which is blocked waiting for other threads to signal the callback.          */
DECL|method|getBlockedThread ()
name|Thread
name|getBlockedThread
parameter_list|()
function_decl|;
comment|/**          * The exchange being processed by the other thread.          */
DECL|method|getExchange ()
name|Exchange
name|getExchange
parameter_list|()
function_decl|;
comment|/**          * Time in millis the thread has been blocked waiting for the signal.          */
DECL|method|getWaitDuration ()
name|long
name|getWaitDuration
parameter_list|()
function_decl|;
block|}
comment|/**      * Registers the exchange to await for the callback to be triggered by another thread which has taken over processing      * this exchange. The current thread will await until that callback happens in the future (blocking until this happens).      *      * @param exchange   the exchange      * @param latch      the latch used to wait for other thread to signal when its done      */
DECL|method|await (Exchange exchange, CountDownLatch latch)
name|void
name|await
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|CountDownLatch
name|latch
parameter_list|)
function_decl|;
comment|/**      * Triggered when the other thread is done processing the exchange, to signal to the waiting thread is done, and can take      * over control to further process the exchange.      *      * @param exchange   the exchange      * @param latch      the latch used to wait for other thread to signal when its done      */
DECL|method|countDown (Exchange exchange, CountDownLatch latch)
name|void
name|countDown
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|CountDownLatch
name|latch
parameter_list|)
function_decl|;
comment|/**      * Number of threads that are blocked waiting for other threads to trigger the callback when they are done processing      * the exchange.      */
DECL|method|size ()
name|int
name|size
parameter_list|()
function_decl|;
comment|/**      * A<i>read-only</i> browser of the {@link AwaitThread}s that are currently inflight.      */
DECL|method|browse ()
name|Collection
argument_list|<
name|AwaitThread
argument_list|>
name|browse
parameter_list|()
function_decl|;
comment|/**      * To interrupt an exchange which may seem as stuck, to force the exchange to continue,      * allowing any blocking thread to be released.      *<p/>      *<b>Important:</b> Use this with caution as the other thread is still assumed to be process the exchange. Though      * if it appears as the exchange is<i>stuck</i>, then this method can remedy this, by forcing the latch to count-down      * so the blocked thread can continue. An exception is set on the exchange which allows Camel's error handler to deal      * with this malfunctioned exchange.      *      * @param exchange    the exchange to interrupt.      */
DECL|method|interrupt (Exchange exchange)
name|void
name|interrupt
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Whether to interrupt any blocking threads during stopping.      *<p/>      * This is enabled by default which allows Camel to release any blocked thread during shutting down Camel itself.      */
DECL|method|isInterruptThreadsWhileStopping ()
name|boolean
name|isInterruptThreadsWhileStopping
parameter_list|()
function_decl|;
comment|/**      * Sets whether to interrupt any blocking threads during stopping.      *<p/>      * This is enabled by default which allows Camel to release any blocked thread during shutting down Camel itself.      */
DECL|method|setInterruptThreadsWhileStopping (boolean interruptThreadsWhileStopping)
name|void
name|setInterruptThreadsWhileStopping
parameter_list|(
name|boolean
name|interruptThreadsWhileStopping
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

