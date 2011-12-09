begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel
package|package
name|org
operator|.
name|apache
operator|.
name|camel
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Queue
import|;
end_import

begin_comment
comment|/**  * A consumer of a batch of message exchanges from an {@link Endpoint}  *  * @version   */
end_comment

begin_interface
DECL|interface|BatchConsumer
specifier|public
interface|interface
name|BatchConsumer
extends|extends
name|Consumer
block|{
comment|/**      * Sets a maximum number of messages as a limit to poll at each polling.      *<p/>      * Can be used to limit e.g. to 100 to avoid reading thousands or more       * messages within the first polling at startup.      *<p/>      * Is default unlimited, but use 0 or negative number to disable it as unlimited.      *      * @param maxMessagesPerPoll  maximum messages to poll.      */
DECL|method|setMaxMessagesPerPoll (int maxMessagesPerPoll)
name|void
name|setMaxMessagesPerPoll
parameter_list|(
name|int
name|maxMessagesPerPoll
parameter_list|)
function_decl|;
comment|/**      * Processes the list of {@link org.apache.camel.Exchange} objects in a batch.       *<p/>      * Each message exchange will be processed individually but the batch      * consumer will add properties with the current index and total in the batch.      * The items in the Queue may actually be Holder objects that store other       * data alongside the Exchange.      *      * @param exchanges list of items in this batch      * @return number of messages actually processed      * @throws Exception if an internal processing error has occurred.      */
DECL|method|processBatch (Queue<Object> exchanges)
name|int
name|processBatch
parameter_list|(
name|Queue
argument_list|<
name|Object
argument_list|>
name|exchanges
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Whether processing the batch is still allowed.      *<p/>      * This is used during shutdown to indicate whether to complete the pending      * exchanges or stop after the current exchange has been processed.      *      * @return<tt>true</tt> to continue processing from the batch, or<tt>false</tt> to stop.      * @see org.apache.camel.ShutdownRunningTask      */
DECL|method|isBatchAllowed ()
name|boolean
name|isBatchAllowed
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

