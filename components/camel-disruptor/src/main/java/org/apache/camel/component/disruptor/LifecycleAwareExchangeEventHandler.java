begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.disruptor
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|disruptor
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
name|TimeUnit
import|;
end_import

begin_import
import|import
name|com
operator|.
name|lmax
operator|.
name|disruptor
operator|.
name|EventHandler
import|;
end_import

begin_import
import|import
name|com
operator|.
name|lmax
operator|.
name|disruptor
operator|.
name|LifecycleAware
import|;
end_import

begin_comment
comment|/**  * This interface fuses the EventHandler and LifecycleAware interfaces.  * It also provides a handle to await the termination of this EventHandler.  */
end_comment

begin_interface
DECL|interface|LifecycleAwareExchangeEventHandler
interface|interface
name|LifecycleAwareExchangeEventHandler
extends|extends
name|EventHandler
argument_list|<
name|ExchangeEvent
argument_list|>
extends|,
name|LifecycleAware
block|{
comment|/**      * Causes the current thread to wait until the event handler has been      * started, unless the thread is {@linkplain Thread#interrupt interrupted}.      *<p/>      *<p>If the event handler is already started then this method returns      * immediately.      *<p/>      *<p>If the current thread:      *<ul>      *<li>has its interrupted status set on entry to this method; or      *<li>is {@linkplain Thread#interrupt interrupted} while waiting,      *</ul>      * then {@link InterruptedException} is thrown and the current thread's      * interrupted status is cleared.      *      * @throws InterruptedException if the current thread is interrupted      *                              while waiting      */
DECL|method|awaitStarted ()
name|void
name|awaitStarted
parameter_list|()
throws|throws
name|InterruptedException
function_decl|;
comment|/**      * Causes the current thread to wait until the event handler has been      * started, unless the thread is {@linkplain Thread#interrupt interrupted},      * or the specified waiting time elapses.      *<p/>      *<p>If the event handler is already started then this method returns      * immediately with the value {@code true}.      *<p/>      *<p>If the current thread:      *<ul>      *<li>has its interrupted status set on entry to this method; âor      *<li>is {@linkplain Thread#interrupt interrupted} while waiting,      *</ul>      * then {@link InterruptedException} is thrown and the current thread's      * interrupted status is cleared.      *<p/>      *<p>If the specified waiting time elapses then the value {@code false}      * is returned.  If the time is less than or equal to zero, the method      * will not wait at all.      *      * @param timeout the maximum time to wait      * @param unit    the time unit of the {@code timeout} argument      * @return {@code true} if the event hanlder is stopped and {@code false}      *         if the waiting time elapsed before the count reached zero      * @throws InterruptedException if the current thread is interrupted      *                              while waiting      */
DECL|method|awaitStarted (long timeout, TimeUnit unit)
name|boolean
name|awaitStarted
parameter_list|(
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
throws|throws
name|InterruptedException
function_decl|;
comment|/**      * Causes the current thread to wait until the event handler has been shut      * down, unless the thread is {@linkplain Thread#interrupt interrupted}.      *<p/>      *<p>If the event handler is not (yet) started then this method returns      * immediately.      *<p/>      *<p>If the current thread:      *<ul>      *<li>has its interrupted status set on entry to this method; or      *<li>is {@linkplain Thread#interrupt interrupted} while waiting,      *</ul>      * then {@link InterruptedException} is thrown and the current thread's      * interrupted status is cleared.      *      * @throws InterruptedException if the current thread is interrupted      *                              while waiting      */
DECL|method|awaitStopped ()
name|void
name|awaitStopped
parameter_list|()
throws|throws
name|InterruptedException
function_decl|;
comment|/**      * Causes the current thread to wait until the event handler has been shut      * down, unless the thread is {@linkplain Thread#interrupt interrupted},      * or the specified waiting time elapses.      *<p/>      *<p>If the event handler is not (yet) started then this method returns      * immediately with the value {@code true}.      *<p/>      *<p>If the current thread:      *<ul>      *<li>has its interrupted status set on entry to this method; âor      *<li>is {@linkplain Thread#interrupt interrupted} while waiting,      *</ul>      * then {@link InterruptedException} is thrown and the current thread's      * interrupted status is cleared.      *<p/>      *<p>If the specified waiting time elapses then the value {@code false}      * is returned.  If the time is less than or equal to zero, the method      * will not wait at all.      *      * @param timeout the maximum time to wait      * @param unit    the time unit of the {@code timeout} argument      * @return {@code true} if the event hanlder is stopped and {@code false}      *         if the waiting time elapsed before the count reached zero      * @throws InterruptedException if the current thread is interrupted      *                              while waiting      */
DECL|method|awaitStopped (long timeout, TimeUnit unit)
name|boolean
name|awaitStopped
parameter_list|(
name|long
name|timeout
parameter_list|,
name|TimeUnit
name|unit
parameter_list|)
throws|throws
name|InterruptedException
function_decl|;
block|}
end_interface

end_unit

