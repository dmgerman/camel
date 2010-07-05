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
comment|/**  * Callback for sending a exchange message to a endpoint using an {@link AsyncProcessor} capable producer.  *<p/>  * Using this callback as a template pattern ensures that Camel handles the resource handling and will  * start and stop the given producer, to avoid resource leaks.  *  * @version $Revision$  */
end_comment

begin_interface
DECL|interface|AsyncProducerCallback
specifier|public
interface|interface
name|AsyncProducerCallback
block|{
comment|/**      * Performs operation on the given producer to send the given exchange.      *      * @param producer        the producer, is newer<tt>null</tt>      * @param asyncProducer   the async producer, is newer<tt>null</tt>      * @param exchange        the exchange, can be<tt>null</tt> if so then create a new exchange from the producer      * @param exchangePattern the exchange pattern, can be<tt>null</tt>      * @param callback        the async callback      * @return (doneSync)<tt>true</tt> to continue execute synchronously,<tt>false</tt> to continue being executed asynchronously      */
DECL|method|doInAsyncProducer (Producer producer, AsyncProcessor asyncProducer, Exchange exchange, ExchangePattern exchangePattern, AsyncCallback callback)
name|boolean
name|doInAsyncProducer
parameter_list|(
name|Producer
name|producer
parameter_list|,
name|AsyncProcessor
name|asyncProducer
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|ExchangePattern
name|exchangePattern
parameter_list|,
name|AsyncCallback
name|callback
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

