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
name|pulsar
operator|.
name|client
operator|.
name|api
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
name|pulsar
operator|.
name|client
operator|.
name|api
operator|.
name|Message
import|;
end_import

begin_comment
comment|/**  * Factory to create a new {@link PulsarMessageReceipt} to store on the {@link Exchange}.  *<p>  * Implement this interface if an alternate implementation of {@link PulsarMessageReceipt} is required  * as newer Pulsar clients may have acknowledgement functionality not yet supported by {@link DefaultPulsarMessageReceipt}.  */
end_comment

begin_interface
DECL|interface|PulsarMessageReceiptFactory
specifier|public
interface|interface
name|PulsarMessageReceiptFactory
block|{
comment|/**      * Creates a new instance of {@link PulsarMessageReceipt}.      */
DECL|method|newInstance (Exchange exchange, Message message, Consumer consumer)
name|PulsarMessageReceipt
name|newInstance
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|Message
name|message
parameter_list|,
name|Consumer
name|consumer
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

