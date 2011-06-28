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
comment|/**  * Strategy that allows consumers to influence the {@link PollingConsumer}.  */
end_comment

begin_interface
DECL|interface|PollingConsumerPollingStrategy
specifier|public
interface|interface
name|PollingConsumerPollingStrategy
block|{
comment|/**      * Callback invoked when the consumer is started.      *      * @throws Exception can be thrown if error starting.      */
DECL|method|onStartup ()
name|void
name|onStartup
parameter_list|()
throws|throws
name|Exception
function_decl|;
comment|/**      * Callback invoked before the poll.      *      * @throws Exception can be thrown if error occurred      */
DECL|method|beforePoll ()
name|void
name|beforePoll
parameter_list|()
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

