begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|TabularData
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
name|api
operator|.
name|management
operator|.
name|ManagedAttribute
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
name|api
operator|.
name|management
operator|.
name|ManagedOperation
import|;
end_import

begin_interface
DECL|interface|ManagedWireTapMBean
specifier|public
interface|interface
name|ManagedWireTapMBean
extends|extends
name|ManagedProcessorMBean
extends|,
name|ManagedExtendedInformation
block|{
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"The uri of the endpoint to wiretap to. The uri can be dynamic computed using the expressions."
argument_list|,
name|mask
operator|=
literal|true
argument_list|)
DECL|method|getUri ()
name|String
name|getUri
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Sets the maximum size used by the ProducerCache which is used to cache and reuse producers"
argument_list|)
DECL|method|getCacheSize ()
name|Integer
name|getCacheSize
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Ignore the invalidate endpoint exception when try to create a producer with that endpoint"
argument_list|)
DECL|method|isIgnoreInvalidEndpoint ()
name|Boolean
name|isIgnoreInvalidEndpoint
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Uses a copy of the original exchange"
argument_list|)
DECL|method|isCopy ()
name|Boolean
name|isCopy
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Whether the uri is dynamic or static"
argument_list|)
DECL|method|isDynamicUri ()
name|Boolean
name|isDynamicUri
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Current size of inflight wire tapped exchanges."
argument_list|)
DECL|method|getTaskSize ()
name|Integer
name|getTaskSize
parameter_list|()
function_decl|;
annotation|@
name|Override
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Statistics of the endpoints which has been sent to"
argument_list|)
DECL|method|extendedInformation ()
name|TabularData
name|extendedInformation
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

