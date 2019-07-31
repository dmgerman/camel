begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|Processor
import|;
end_import

begin_comment
comment|/**  * This is an endpoint when sending to it, is intercepted and is routed in a detour, with the following flow:  * before, send to original endpoint (can be skipped), after (optional).  */
end_comment

begin_interface
DECL|interface|InterceptSendToEndpoint
specifier|public
interface|interface
name|InterceptSendToEndpoint
extends|extends
name|Endpoint
block|{
comment|/**      * The original endpoint which was intercepted.      */
DECL|method|getOriginalEndpoint ()
name|Endpoint
name|getOriginalEndpoint
parameter_list|()
function_decl|;
comment|/**      * The processor for routing in a detour before sending to the original endpoint.      */
DECL|method|getBefore ()
name|Processor
name|getBefore
parameter_list|()
function_decl|;
comment|/**      * The processor (optional) for routing after sending to the original endpoint.      */
DECL|method|getAfter ()
name|Processor
name|getAfter
parameter_list|()
function_decl|;
comment|/**      * Whether to skip sending to the original endpoint.      */
DECL|method|isSkip ()
name|boolean
name|isSkip
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

