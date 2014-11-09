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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
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
name|ServiceStatus
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
name|StatefulService
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
name|ManagedInstance
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
name|ManagedResource
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
name|ManagedEndpointMBean
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|JsonSchemaHelper
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
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed Endpoint"
argument_list|)
DECL|class|ManagedEndpoint
specifier|public
class|class
name|ManagedEndpoint
implements|implements
name|ManagedInstance
implements|,
name|ManagedEndpointMBean
block|{
DECL|field|endpoint
specifier|private
specifier|final
name|Endpoint
name|endpoint
decl_stmt|;
DECL|method|ManagedEndpoint (Endpoint endpoint)
specifier|public
name|ManagedEndpoint
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|this
operator|.
name|endpoint
operator|=
name|endpoint
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
comment|// noop
block|}
DECL|method|getEndpoint ()
specifier|public
name|Endpoint
name|getEndpoint
parameter_list|()
block|{
return|return
name|endpoint
return|;
block|}
annotation|@
name|Override
DECL|method|getCamelId ()
specifier|public
name|String
name|getCamelId
parameter_list|()
block|{
return|return
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getCamelManagementName ()
specifier|public
name|String
name|getCamelManagementName
parameter_list|()
block|{
return|return
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|getManagementName
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getEndpointUri ()
specifier|public
name|String
name|getEndpointUri
parameter_list|()
block|{
return|return
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
name|endpoint
operator|.
name|isSingleton
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|getState ()
specifier|public
name|String
name|getState
parameter_list|()
block|{
comment|// must use String type to be sure remote JMX can read the attribute without requiring Camel classes.
if|if
condition|(
name|endpoint
operator|instanceof
name|StatefulService
condition|)
block|{
name|ServiceStatus
name|status
init|=
operator|(
operator|(
name|StatefulService
operator|)
name|endpoint
operator|)
operator|.
name|getStatus
argument_list|()
decl_stmt|;
return|return
name|status
operator|.
name|name
argument_list|()
return|;
block|}
comment|// assume started if not a ServiceSupport instance
return|return
name|ServiceStatus
operator|.
name|Started
operator|.
name|name
argument_list|()
return|;
block|}
annotation|@
name|Override
DECL|method|explain (boolean allOptions)
specifier|public
name|TabularData
name|explain
parameter_list|(
name|boolean
name|allOptions
parameter_list|)
block|{
try|try
block|{
name|String
name|json
init|=
name|endpoint
operator|.
name|getCamelContext
argument_list|()
operator|.
name|explainEndpointJson
argument_list|(
name|getEndpointUri
argument_list|()
argument_list|,
name|allOptions
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|rows
init|=
name|JsonSchemaHelper
operator|.
name|parseJsonSchema
argument_list|(
literal|"properties"
argument_list|,
name|json
argument_list|)
decl_stmt|;
name|TabularData
name|answer
init|=
operator|new
name|TabularDataSupport
argument_list|(
name|CamelOpenMBeanTypes
operator|.
name|explainEndpointTabularType
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|row
range|:
name|rows
control|)
block|{
name|String
name|option
init|=
name|row
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
name|String
name|type
init|=
name|row
operator|.
name|get
argument_list|(
literal|"type"
argument_list|)
decl_stmt|;
name|String
name|javaType
init|=
name|row
operator|.
name|get
argument_list|(
literal|"javaType"
argument_list|)
decl_stmt|;
name|String
name|value
init|=
name|row
operator|.
name|get
argument_list|(
literal|"value"
argument_list|)
operator|!=
literal|null
condition|?
name|row
operator|.
name|get
argument_list|(
literal|"value"
argument_list|)
else|:
literal|""
decl_stmt|;
name|String
name|defaultValue
init|=
name|row
operator|.
name|get
argument_list|(
literal|"defaultValue"
argument_list|)
operator|!=
literal|null
condition|?
name|row
operator|.
name|get
argument_list|(
literal|"defaultValue"
argument_list|)
else|:
literal|""
decl_stmt|;
name|String
name|description
init|=
name|row
operator|.
name|get
argument_list|(
literal|"description"
argument_list|)
operator|!=
literal|null
condition|?
name|row
operator|.
name|get
argument_list|(
literal|"description"
argument_list|)
else|:
literal|""
decl_stmt|;
name|CompositeType
name|ct
init|=
name|CamelOpenMBeanTypes
operator|.
name|explainEndpointsCompositeType
argument_list|()
decl_stmt|;
name|CompositeData
name|data
init|=
operator|new
name|CompositeDataSupport
argument_list|(
name|ct
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"option"
block|,
literal|"type"
block|,
literal|"java type"
block|,
literal|"value"
block|,
literal|"default value"
block|,
literal|"description"
block|}
argument_list|,
operator|new
name|Object
index|[]
block|{
name|option
block|,
name|type
block|,
name|javaType
block|,
name|value
block|,
name|defaultValue
block|,
name|description
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
name|ObjectHelper
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
DECL|method|getInstance ()
specifier|public
name|Endpoint
name|getInstance
parameter_list|()
block|{
return|return
name|endpoint
return|;
block|}
block|}
end_class

end_unit

