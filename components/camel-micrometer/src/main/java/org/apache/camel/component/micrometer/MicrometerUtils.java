begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.micrometer
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|micrometer
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|io
operator|.
name|micrometer
operator|.
name|core
operator|.
name|instrument
operator|.
name|Meter
import|;
end_import

begin_import
import|import
name|io
operator|.
name|micrometer
operator|.
name|core
operator|.
name|instrument
operator|.
name|MeterRegistry
import|;
end_import

begin_import
import|import
name|io
operator|.
name|micrometer
operator|.
name|core
operator|.
name|instrument
operator|.
name|simple
operator|.
name|SimpleMeterRegistry
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
name|RuntimeCamelException
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
name|spi
operator|.
name|Registry
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_class
DECL|class|MicrometerUtils
specifier|public
specifier|abstract
class|class
name|MicrometerUtils
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MicrometerUtils
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|method|getByName (String meterName)
specifier|public
specifier|static
name|Meter
operator|.
name|Type
name|getByName
parameter_list|(
name|String
name|meterName
parameter_list|)
block|{
switch|switch
condition|(
name|meterName
condition|)
block|{
case|case
literal|"summary"
case|:
return|return
name|Meter
operator|.
name|Type
operator|.
name|DISTRIBUTION_SUMMARY
return|;
case|case
literal|"counter"
case|:
return|return
name|Meter
operator|.
name|Type
operator|.
name|COUNTER
return|;
case|case
literal|"timer"
case|:
return|return
name|Meter
operator|.
name|Type
operator|.
name|TIMER
return|;
default|default:
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Unsupported meter type "
operator|+
name|meterName
argument_list|)
throw|;
block|}
block|}
DECL|method|getName (Meter.Type type)
specifier|public
specifier|static
name|String
name|getName
parameter_list|(
name|Meter
operator|.
name|Type
name|type
parameter_list|)
block|{
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|DISTRIBUTION_SUMMARY
case|:
return|return
literal|"summary"
return|;
case|case
name|COUNTER
case|:
return|return
literal|"counter"
return|;
case|case
name|TIMER
case|:
return|return
literal|"timer"
return|;
default|default:
throw|throw
operator|new
name|RuntimeCamelException
argument_list|(
literal|"Unsupported meter type "
operator|+
name|type
argument_list|)
throw|;
block|}
block|}
DECL|method|getOrCreateMeterRegistry (Registry camelRegistry, String registryName)
specifier|public
specifier|static
name|MeterRegistry
name|getOrCreateMeterRegistry
parameter_list|(
name|Registry
name|camelRegistry
parameter_list|,
name|String
name|registryName
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"Looking up MeterRegistry from Camel Registry for name \"{}\""
argument_list|,
name|registryName
argument_list|)
expr_stmt|;
name|MeterRegistry
name|result
init|=
name|getMeterRegistryFromCamelRegistry
argument_list|(
name|camelRegistry
argument_list|,
name|registryName
argument_list|)
decl_stmt|;
if|if
condition|(
name|result
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"MeterRegistry not found from Camel Registry for name \"{}\""
argument_list|,
name|registryName
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"Creating new default MeterRegistry"
argument_list|)
expr_stmt|;
name|result
operator|=
name|createMeterRegistry
argument_list|()
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
DECL|method|getMeterRegistryFromCamelRegistry (Registry camelRegistry, String registryName)
specifier|public
specifier|static
name|MeterRegistry
name|getMeterRegistryFromCamelRegistry
parameter_list|(
name|Registry
name|camelRegistry
parameter_list|,
name|String
name|registryName
parameter_list|)
block|{
name|MeterRegistry
name|registry
init|=
name|camelRegistry
operator|.
name|lookupByNameAndType
argument_list|(
name|registryName
argument_list|,
name|MeterRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|registry
operator|!=
literal|null
condition|)
block|{
return|return
name|registry
return|;
block|}
else|else
block|{
name|Set
argument_list|<
name|MeterRegistry
argument_list|>
name|registries
init|=
name|camelRegistry
operator|.
name|findByType
argument_list|(
name|MeterRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|registries
operator|.
name|size
argument_list|()
operator|==
literal|1
condition|)
block|{
return|return
name|registries
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
DECL|method|createMeterRegistry ()
specifier|public
specifier|static
name|MeterRegistry
name|createMeterRegistry
parameter_list|()
block|{
return|return
operator|new
name|SimpleMeterRegistry
argument_list|()
return|;
block|}
block|}
end_class

end_unit

