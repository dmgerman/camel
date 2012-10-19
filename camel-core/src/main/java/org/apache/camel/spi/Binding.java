begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  *  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Processor
import|;
end_import

begin_comment
comment|/**  * Represents a<a href="http://camel.apache.org/binding.html">Binding</a> or contract  * which can be applied to an Endpoint; such as ensuring that a particular  *<a href="http://camel.apache.org/data-format.html">Data Format</a> is used on messages in and out of an endpoint.  */
end_comment

begin_interface
DECL|interface|Binding
specifier|public
interface|interface
name|Binding
block|{
comment|/**      * Returns a new {@link Processor} which is used by a producer on an endpoint to implement      * the producer side binding before the message is sent to the underlying endpoint.      */
DECL|method|createProduceProcessor ()
name|Processor
name|createProduceProcessor
parameter_list|()
function_decl|;
comment|/**      * Returns a new {@link Processor} which is used by a consumer on an endpoint to process the      * message with the binding before its passed to the endpoint consumer producer.      */
DECL|method|createConsumeProcessor ()
name|Processor
name|createConsumeProcessor
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

