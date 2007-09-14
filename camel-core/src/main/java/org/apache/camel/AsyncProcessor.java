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
comment|/**  * A more complex version of {@see Processor} which supports asynchronous  * processing of the {@see Exchange}.  Any processor can be coerced to  * have an {@see AsyncProcessor} interface by using the {@see AsyncProcessorTypeConverter.convert()}  * method.  *   * @version $Revision$  */
end_comment

begin_interface
DECL|interface|AsyncProcessor
specifier|public
interface|interface
name|AsyncProcessor
extends|extends
name|Processor
block|{
comment|/**      * Processes the message exchange.  Similar to {@see Processor.process}, but      * the caller supports having the exchange asynchronously processed.      *      * @param exchange the exchange to process      * @param  callback The @{see AsyncCallback} will be invoked when the processing      *         of the exchange is completed. If the exchange is completed synchronously, then the       *         callback is also invoked synchronously.  The callback should therefore be careful of      *         starting recursive loop.      *               * @return true if the processing was completed synchronously.      */
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
block|}
end_interface

end_unit

