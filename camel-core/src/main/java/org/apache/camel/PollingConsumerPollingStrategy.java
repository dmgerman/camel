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

begin_comment
comment|/**  * Strategy that allows {@link Consumer}s to influence the {@link PollingConsumer}.  *<p/>  * For example this is used by schedule based consumers to be able to suspend/resume  * upon polling using a {@link PollingConsumer}.  *  * @see org.apache.camel.support.EventDrivenPollingConsumer  */
end_comment

begin_interface
DECL|interface|PollingConsumerPollingStrategy
specifier|public
interface|interface
name|PollingConsumerPollingStrategy
block|{
comment|/**      * Callback invoked when the consumer is initialized such as when the {@link PollingConsumer} starts.      *      * @throws Exception can be thrown if error initializing.      */
DECL|method|onInit ()
name|void
name|onInit
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Callback invoked before the poll.      *      * @param timeout the timeout      * @throws Exception can be thrown if error occurred      * @return timeout to be used, this allows returning a higher timeout value      * to ensure at least one poll is being performed      */
DECL|method|beforePoll (long timeout)
name|long
name|beforePoll
parameter_list|(
name|long
name|timeout
parameter_list|)
throws|throws
name|Exception
function_decl|;
comment|/**      * Callback invoked after the poll.      *      * @throws Exception can be thrown if error occurred      */
DECL|method|afterPoll ()
name|void
name|afterPoll
parameter_list|()
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

