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
literal|"Number of noop attempts (no type conversion was needed)"
argument_list|)
DECL|method|getNoopCounter ()
name|long
name|getNoopCounter
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Number of type conversion attempts"
argument_list|)
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
literal|"Number of type conversion hits by base core converters (successful conversions)"
argument_list|)
DECL|method|getBaseHitCounter ()
name|long
name|getBaseHitCounter
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Number of type conversion misses (no suitable type converter)"
argument_list|)
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
DECL|method|resetTypeConversionCounters ()
name|void
name|resetTypeConversionCounters
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Utilization statistics enabled"
argument_list|)
DECL|method|isStatisticsEnabled ()
name|boolean
name|isStatisticsEnabled
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Utilization statistics enabled"
argument_list|)
DECL|method|setStatisticsEnabled (boolean statisticsEnabled)
name|void
name|setStatisticsEnabled
parameter_list|(
name|boolean
name|statisticsEnabled
parameter_list|)
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Number of type converters in the registry"
argument_list|)
DECL|method|getNumberOfTypeConverters ()
name|int
name|getNumberOfTypeConverters
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"Logging level to use if attempting to add a duplicate type converter"
argument_list|)
DECL|method|getTypeConverterExistsLoggingLevel ()
name|String
name|getTypeConverterExistsLoggingLevel
parameter_list|()
function_decl|;
annotation|@
name|ManagedAttribute
argument_list|(
name|description
operator|=
literal|"What to do if attempting to add a duplicate type converter (Override, Ignore or Fail)"
argument_list|)
DECL|method|getTypeConverterExists ()
name|String
name|getTypeConverterExists
parameter_list|()
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Checks whether a type converter exists for converting (from -> to)"
argument_list|)
DECL|method|hasTypeConverter (String fromType, String toType)
name|boolean
name|hasTypeConverter
parameter_list|(
name|String
name|fromType
parameter_list|,
name|String
name|toType
parameter_list|)
function_decl|;
annotation|@
name|ManagedOperation
argument_list|(
name|description
operator|=
literal|"Lists all the type converters in the registry (from -> to)"
argument_list|)
DECL|method|listTypeConverters ()
name|TabularData
name|listTypeConverters
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

