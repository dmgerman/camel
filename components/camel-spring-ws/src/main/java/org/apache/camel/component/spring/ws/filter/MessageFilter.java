begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.spring.ws.filter
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|spring
operator|.
name|ws
operator|.
name|filter
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
name|springframework
operator|.
name|ws
operator|.
name|WebServiceMessage
import|;
end_import

begin_comment
comment|/**  * A strategy instance that filters a WebServiceMessage response.  *   * This class provides an additional configuration that can be managed in your Spring's context.  *   *   */
end_comment

begin_interface
DECL|interface|MessageFilter
specifier|public
interface|interface
name|MessageFilter
block|{
comment|/**      * Calls filter for a producer      *       * @param exchange      * @param response provided by the producer      */
DECL|method|filterProducer (Exchange exchange, WebServiceMessage produceRresponse)
name|void
name|filterProducer
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|WebServiceMessage
name|produceRresponse
parameter_list|)
function_decl|;
comment|/**      * Calls filter for a consumer      *       * @param exchange      * @param response provided by the consumer      */
DECL|method|filterConsumer (Exchange exchange, WebServiceMessage consumerResponse)
name|void
name|filterConsumer
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|WebServiceMessage
name|consumerResponse
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

