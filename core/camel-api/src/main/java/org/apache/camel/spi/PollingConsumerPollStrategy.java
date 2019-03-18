begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Consumer
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
name|Endpoint
import|;
end_import

begin_comment
comment|/**  * Strategy for a {@link org.apache.camel.PollingConsumer} when polling an {@link org.apache.camel.Endpoint}.  *<p/>  * This pluggable strategy allows to plugin different implementations what to do, most noticeable what to  * do in case the polling goes wrong. This can be handled in the  * {@link #rollback(org.apache.camel.Consumer , org.apache.camel.Endpoint , int, Exception) rollback} method.  */
end_comment

begin_interface
DECL|interface|PollingConsumerPollStrategy
specifier|public
interface|interface
name|PollingConsumerPollStrategy
block|{
comment|/**      * Called when poll is about to begin      *      * @param consumer the consumer      * @param endpoint the endpoint being consumed      * @return<tt>true</tt> to begin polling, or<tt>false</tt> to skip polling this time.      */
DECL|method|begin (Consumer consumer, Endpoint endpoint)
name|boolean
name|begin
parameter_list|(
name|Consumer
name|consumer
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
function_decl|;
comment|/**      * Called when poll is completed successfully      *      * @param consumer the consumer      * @param endpoint the endpoint being consumed      * @param polledMessages number of messages polled, will be<tt>0</tt> if no message was polled at all.      */
DECL|method|commit (Consumer consumer, Endpoint endpoint, int polledMessages)
name|void
name|commit
parameter_list|(
name|Consumer
name|consumer
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|int
name|polledMessages
parameter_list|)
function_decl|;
comment|/**      * Called when poll failed      *      * @param consumer the consumer      * @param endpoint the endpoint being consumed      * @param retryCounter current retry attempt, starting from 0.      * @param cause the caused exception      * @throws Exception can be used to rethrow the caused exception. Notice that thrown an exception will      *         terminate the scheduler and thus Camel will not trigger again. So if you want to let the scheduler      *         to continue to run then do<b>not</b> throw any exception from this method.      * @return whether to retry immediately or not. Return<tt>false</tt> to ignore the problem,      *<tt>true</tt> to try immediately again      */
DECL|method|rollback (Consumer consumer, Endpoint endpoint, int retryCounter, Exception cause)
name|boolean
name|rollback
parameter_list|(
name|Consumer
name|consumer
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|,
name|int
name|retryCounter
parameter_list|,
name|Exception
name|cause
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

