begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.lumberjack.io
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|lumberjack
operator|.
name|io
package|;
end_package

begin_comment
comment|/**  * This functional interface defines a processor that will be called when a lumberjack message is received  */
end_comment

begin_interface
annotation|@
name|FunctionalInterface
DECL|interface|LumberjackMessageProcessor
specifier|public
interface|interface
name|LumberjackMessageProcessor
block|{
comment|/**      * Called when a message is received. The {@code callback} must be called with the status of the processing      *      * @param payload Lumberjack message payload      * @param callback Callback to call when the processing is complete      */
DECL|method|onMessageReceived (Object payload, Callback callback)
name|void
name|onMessageReceived
parameter_list|(
name|Object
name|payload
parameter_list|,
name|Callback
name|callback
parameter_list|)
function_decl|;
comment|/**      * This functional interface defines the callback to call when the processing of a Lumberjack message is complete      */
annotation|@
name|FunctionalInterface
DECL|interface|Callback
interface|interface
name|Callback
block|{
comment|/**          * Called when the processing is complete.          *          * @param success {@code true} is the processing is a success, {@code false} otherwise          */
DECL|method|onComplete (boolean success)
name|void
name|onComplete
parameter_list|(
name|boolean
name|success
parameter_list|)
function_decl|;
block|}
block|}
end_interface

end_unit

