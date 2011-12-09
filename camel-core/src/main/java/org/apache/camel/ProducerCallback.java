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
comment|/**  * Callback for sending a exchange message to a endpoint using a producer.  *<p/>  * Using this callback as a template pattern ensures that Camel handles the resource handling and will  * start and stop the given producer, to avoid resource leaks.  *  * @version   */
end_comment

begin_interface
DECL|interface|ProducerCallback
specifier|public
interface|interface
name|ProducerCallback
parameter_list|<
name|T
parameter_list|>
block|{
comment|/**      * Performs operation on the given producer to send the given exchange.      *      * @param producer        the producer, is never<tt>null</tt>      * @param exchange        the exchange, can be<tt>null</tt> if so then create a new exchange from the producer      * @param exchangePattern the exchange pattern, can be<tt>null</tt>      * @return the response      * @throws Exception if an internal processing error has occurred.      */
DECL|method|doInProducer (Producer producer, Exchange exchange, ExchangePattern exchangePattern)
name|T
name|doInProducer
parameter_list|(
name|Producer
name|producer
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|ExchangePattern
name|exchangePattern
parameter_list|)
throws|throws
name|Exception
function_decl|;
block|}
end_interface

end_unit

