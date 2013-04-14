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

begin_comment
comment|/**  *  */
end_comment

begin_interface
DECL|interface|ManagedTypeConverterRegistryMBean
specifier|public
interface|interface
name|ManagedTypeConverterRegistryMBean
extends|extends
name|ManagedServiceMBean
block|{
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Number of type conversion attempts"
argument_list|)
annotation|@
name|Deprecated
DECL|method|getAttemptCounter ()
name|long
name|getAttemptCounter
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Number of type conversion hits (successful conversions)"
argument_list|)
annotation|@
name|Deprecated
DECL|method|getHitCounter ()
name|long
name|getHitCounter
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Number of type conversion misses (no suitable type converter)"
argument_list|)
annotation|@
name|Deprecated
DECL|method|getMissCounter ()
name|long
name|getMissCounter
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Number of type conversion failures (failed conversions)"
argument_list|)
annotation|@
name|Deprecated
DECL|method|getFailedCounter ()
name|long
name|getFailedCounter
parameter_list|()
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Resets the type conversion counters"
argument_list|)
annotation|@
name|Deprecated
DECL|method|resetTypeConversionCounters ()
name|void
name|resetTypeConversionCounters
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

