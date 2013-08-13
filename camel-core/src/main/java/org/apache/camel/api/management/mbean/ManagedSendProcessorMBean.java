begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.api.management.mbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|api
operator|.
name|management
operator|.
name|mbean
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
name|api
operator|.
name|management
operator|.
name|ManagedAttribute
import|;
end_import

begin_interface
DECL|interface|ManagedSendProcessorMBean
specifier|public
interface|interface
name|ManagedSendProcessorMBean
extends|extends
name|ManagedProcessorMBean
block|{
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Destination as Endpoint URI"
argument_list|,
name|mask
operator|=
literal|true
argument_list|)
DECL|method|getDestination ()
name|String
name|getDestination
parameter_list|()
function_decl|;
comment|/**      * @deprecated no longer in use. Will be removed in a future Camel release.      */
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Destination as Endpoint URI"
argument_list|)
annotation|@
name|Deprecated
DECL|method|setDestination (String uri)
name|void
name|setDestination
parameter_list|(
name|String
name|uri
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Message Exchange Pattern"
argument_list|)
DECL|method|getMessageExchangePattern ()
name|String
name|getMessageExchangePattern
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

