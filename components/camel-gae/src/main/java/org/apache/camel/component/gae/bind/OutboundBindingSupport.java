begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.gae.bind
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|gae
operator|.
name|bind
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
name|Endpoint
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
name|Producer
import|;
end_import

begin_comment
comment|/**  * Implemented by {@link Endpoint}s that provide an {@link OutboundBinding} to  * {@link Producer}s.  *   * @see InboundBinding  */
end_comment

begin_interface
DECL|interface|OutboundBindingSupport
specifier|public
interface|interface
name|OutboundBindingSupport
parameter_list|<
name|E
extends|extends
name|Endpoint
parameter_list|,
name|S
parameter_list|,
name|T
parameter_list|>
block|{
DECL|method|getOutboundBinding ()
name|OutboundBinding
argument_list|<
name|E
argument_list|,
name|S
argument_list|,
name|T
argument_list|>
name|getOutboundBinding
parameter_list|()
function_decl|;
DECL|method|setOutboundBinding (OutboundBinding<E, S, T> outboundBinding)
name|void
name|setOutboundBinding
parameter_list|(
name|OutboundBinding
argument_list|<
name|E
argument_list|,
name|S
argument_list|,
name|T
argument_list|>
name|outboundBinding
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

