begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|concurrent
operator|.
name|CompletableFuture
import|;
end_import

begin_comment
comment|/**  * An<b>asynchronous</b> processor which can process an {@link Exchange} in an asynchronous fashion  * and signal completion by invoking the {@link AsyncCallback}.  *<p/>  * Any processor can be coerced to have an {@link AsyncProcessor} interface by using the  * {@link org.apache.camel.support.AsyncProcessorConverterHelper#convert AsyncProcessorConverterHelper.convert}  * method.  */
end_comment

begin_interface
DECL|interface|AsyncProcessor
specifier|public
interface|interface
name|AsyncProcessor
extends|extends
name|Processor
block|{
comment|/**      * Processes the message exchange.      * Similar to {@link Processor#process}, but the caller supports having the exchange asynchronously processed.      *<p/>      * If there was a failure processing then the caused {@link Exception} would be set on the {@link Exchange}.      *      * @param exchange the message exchange      * @param callback the {@link AsyncCallback} will be invoked when the processing of the exchange is completed.      *                 If the exchange is completed synchronously, then the callback is also invoked synchronously.      *                 The callback should therefore be careful of starting recursive loop.      * @return (doneSync)<tt>true</tt> to continue execute synchronously,<tt>false</tt> to continue being executed asynchronously      */
DECL|method|process (Exchange exchange, AsyncCallback callback)
name|boolean
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
function_decl|;
DECL|method|processAsync (Exchange exchange)
name|CompletableFuture
argument_list|<
name|Exchange
argument_list|>
name|processAsync
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

