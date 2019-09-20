begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.pulsar
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|pulsar
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Exchange
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
name|component
operator|.
name|pulsar
operator|.
name|configuration
operator|.
name|PulsarConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pulsar
operator|.
name|client
operator|.
name|api
operator|.
name|MessageId
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|pulsar
operator|.
name|client
operator|.
name|api
operator|.
name|PulsarClientException
import|;
end_import

begin_comment
comment|/**  * Acknowledge the receipt of a message using the Pulsar consumer.  *<p>  * Available on the {@link Exchange} if  * {@link PulsarConfiguration#isAllowManualAcknowledgement()} is true. An  * alternative to the default may be provided by implementing  * {@link PulsarMessageReceiptFactory}.  */
end_comment

begin_interface
DECL|interface|PulsarMessageReceipt
specifier|public
interface|interface
name|PulsarMessageReceipt
block|{
comment|/**      * Acknowledge receipt of this message synchronously.      *      * @see org.apache.pulsar.client.api.Consumer#acknowledge(MessageId)      */
DECL|method|acknowledge ()
name|void
name|acknowledge
parameter_list|()
throws|throws
name|PulsarClientException
function_decl|;
comment|/**      * Acknowledge receipt of all of the messages in the stream up to and      * including this message synchronously.      *      * @see org.apache.pulsar.client.api.Consumer#acknowledgeCumulative(MessageId)      */
DECL|method|acknowledgeCumulative ()
name|void
name|acknowledgeCumulative
parameter_list|()
throws|throws
name|PulsarClientException
function_decl|;
comment|/**      * Acknowledge receipt of this message asynchronously.      *      * @see org.apache.pulsar.client.api.Consumer#acknowledgeAsync(MessageId)      */
DECL|method|acknowledgeAsync ()
name|CompletableFuture
argument_list|<
name|Void
argument_list|>
name|acknowledgeAsync
parameter_list|()
function_decl|;
comment|/**      * Acknowledge receipt of all of the messages in the stream up to and      * including this message asynchronously.      *      * @see org.apache.pulsar.client.api.Consumer#acknowledgeCumulativeAsync(MessageId)      */
DECL|method|acknowledgeCumulativeAsync ()
name|CompletableFuture
argument_list|<
name|Void
argument_list|>
name|acknowledgeCumulativeAsync
parameter_list|()
function_decl|;
comment|/**      * Acknowledge the failure to process this message.      *      * @see org.apache.pulsar.client.api.Consumer#negativeAcknowledge(MessageId)      *      Note: Available in Puslar 2.4.0. Implementations with earlier      *      versions should return an      *      {@link java.lang.UnsupportedOperationException}.      */
DECL|method|negativeAcknowledge ()
name|void
name|negativeAcknowledge
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

