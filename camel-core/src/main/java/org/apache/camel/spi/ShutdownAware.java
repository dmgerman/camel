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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|ShutdownRunningTask
import|;
end_import

begin_comment
comment|/**  * Allows {@link org.apache.camel.Consumer} to fine grained control on shutdown which mostly  * have to cater for in-memory based components. These components need to be able to have an extra  * chance to have their pending exchanges being completed to support graceful shutdown. This helps  * ensure that no messages get lost.  *  * @version   * @see org.apache.camel.spi.ShutdownStrategy  */
end_comment

begin_interface
DECL|interface|ShutdownAware
specifier|public
interface|interface
name|ShutdownAware
extends|extends
name|ShutdownPrepared
block|{
comment|/**      * To defer shutdown during first phase of shutdown. This allows any pending exchanges to be completed      * and therefore ensure a graceful shutdown without loosing messages. At the very end when there are no      * more inflight and pending messages the consumer could then safely be shutdown.      *<p/>      * This is needed by {@link org.apache.camel.component.seda.SedaConsumer}.      *      * @param shutdownRunningTask the configured option for how to act when shutting down running tasks.      * @return<tt>true</tt> to defer shutdown to very last.      */
DECL|method|deferShutdown (ShutdownRunningTask shutdownRunningTask)
name|boolean
name|deferShutdown
parameter_list|(
name|ShutdownRunningTask
name|shutdownRunningTask
parameter_list|)
function_decl|;
comment|/**      * Gets the number of pending exchanges.      *<p/>      * Some consumers has internal queues with {@link org.apache.camel.Exchange} which are pending.      * For example the {@link org.apache.camel.component.seda.SedaConsumer}.      *<p/>      * Return<tt>zero</tt> to indicate no pending exchanges and therefore ready to shutdown.      *      * @return number of pending exchanges      */
DECL|method|getPendingExchangesSize ()
name|int
name|getPendingExchangesSize
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

