begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.management.mbean
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|management
operator|.
name|mbean
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|CompositeData
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|CompositeDataSupport
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|CompositeType
import|;
end_import

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
name|javax
operator|.
name|management
operator|.
name|openmbean
operator|.
name|TabularDataSupport
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
name|CamelContext
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
name|api
operator|.
name|management
operator|.
name|mbean
operator|.
name|CamelOpenMBeanTypes
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
name|mbean
operator|.
name|ManagedCamelHealthMBean
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
name|health
operator|.
name|HealthCheck
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
name|health
operator|.
name|HealthCheckHelper
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
name|health
operator|.
name|HealthCheckRegistry
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
name|ManagementStrategy
import|;
end_import

begin_class
DECL|class|ManagedCamelHealth
specifier|public
class|class
name|ManagedCamelHealth
implements|implements
name|ManagedCamelHealthMBean
block|{
DECL|field|context
specifier|private
specifier|final
name|CamelContext
name|context
decl_stmt|;
DECL|method|ManagedCamelHealth (CamelContext context)
specifier|public
name|ManagedCamelHealth
parameter_list|(
name|CamelContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
DECL|method|init (ManagementStrategy strategy)
specifier|public
name|void
name|init
parameter_list|(
name|ManagementStrategy
name|strategy
parameter_list|)
block|{
comment|// do nothing
block|}
DECL|method|getContext ()
specifier|public
name|CamelContext
name|getContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
annotation|@
name|Override
DECL|method|getIsHealthy ()
specifier|public
name|boolean
name|getIsHealthy
parameter_list|()
block|{
for|for
control|(
name|HealthCheck
operator|.
name|Result
name|result
range|:
name|HealthCheckHelper
operator|.
name|invoke
argument_list|(
name|context
argument_list|)
control|)
block|{
if|if
condition|(
name|result
operator|.
name|getState
argument_list|()
operator|==
name|HealthCheck
operator|.
name|State
operator|.
name|DOWN
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|getHealthChecksIDs ()
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getHealthChecksIDs
parameter_list|()
block|{
name|HealthCheckRegistry
name|registry
init|=
name|context
operator|.
name|getHealthCheckRegistry
argument_list|()
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
operator|.
name|getCheckIDs
argument_list|()
return|;
block|}
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|details ()
specifier|public
name|TabularData
name|details
parameter_list|()
block|{
try|try
block|{
specifier|final
name|TabularData
name|answer
init|=
operator|new
name|TabularDataSupport
argument_list|(
name|CamelOpenMBeanTypes
operator|.
name|camelHealthDetailsTabularType
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|CompositeType
name|type
init|=
name|CamelOpenMBeanTypes
operator|.
name|camelHealthDetailsCompositeType
argument_list|()
decl_stmt|;
for|for
control|(
name|HealthCheck
operator|.
name|Result
name|result
range|:
name|HealthCheckHelper
operator|.
name|invoke
argument_list|(
name|context
argument_list|)
control|)
block|{
name|CompositeData
name|data
init|=
operator|new
name|CompositeDataSupport
argument_list|(
name|type
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"id"
block|,
literal|"group"
block|,
literal|"state"
block|,
literal|"enabled"
block|,
literal|"interval"
block|,
literal|"failureThreshold"
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
name|result
operator|.
name|getCheck
argument_list|()
operator|.
name|getId
argument_list|()
block|,
name|result
operator|.
name|getCheck
argument_list|()
operator|.
name|getGroup
argument_list|()
block|,
name|result
operator|.
name|getState
argument_list|()
operator|.
name|name
argument_list|()
block|,
name|result
operator|.
name|getCheck
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|isEnabled
argument_list|()
block|,
name|result
operator|.
name|getCheck
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getInterval
argument_list|()
operator|!=
literal|null
condition|?
name|result
operator|.
name|getCheck
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getInterval
argument_list|()
operator|.
name|toMillis
argument_list|()
else|:
literal|null
block|,
name|result
operator|.
name|getCheck
argument_list|()
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getFailureThreshold
argument_list|()
block|}
argument_list|)
decl_stmt|;
name|answer
operator|.
name|put
argument_list|(
name|data
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
DECL|method|invoke (String id)
specifier|public
name|String
name|invoke
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|Optional
argument_list|<
name|HealthCheck
operator|.
name|Result
argument_list|>
name|result
init|=
name|HealthCheckHelper
operator|.
name|invoke
argument_list|(
name|context
argument_list|,
name|id
argument_list|,
name|Collections
operator|.
name|emptyMap
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|result
operator|.
name|map
argument_list|(
name|r
lambda|->
name|r
operator|.
name|getState
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
operator|.
name|orElse
argument_list|(
name|HealthCheck
operator|.
name|State
operator|.
name|UNKNOWN
operator|.
name|name
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

