begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Exchange
import|;
end_import

begin_comment
comment|/**  * Provides a hook for custom {@link org.apache.camel.Processor} or {@link org.apache.camel.Component}  * instances to respond to completed or failed processing of an {@link Exchange} rather like Spring's  *<a href="http://static.springframework.org/spring/docs/2.5.x/api/org/springframework/transaction/  * support/TransactionSynchronization.html">TransactionSynchronization</a>  */
end_comment

begin_interface
DECL|interface|Synchronization
specifier|public
interface|interface
name|Synchronization
block|{
comment|/**      * Called when the processing of the message exchange is complete      *      * @param exchange the exchange being processed      */
DECL|method|onComplete (Exchange exchange)
name|void
name|onComplete
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
comment|/**      * Called when the processing of the message exchange has failed for some reason.      * The exception which caused the problem is in {@link Exchange#getException()} and      * there could be a fault message via {@link org.apache.camel.Message#isFault()}      *      * @param exchange the exchange being processed      */
DECL|method|onFailure (Exchange exchange)
name|void
name|onFailure
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

