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
name|ManagedDataFormatMBean
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
name|DataFormat
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
name|DataFormatName
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
name|support
operator|.
name|JSonSchemaHelper
import|;
end_import

begin_class
annotation|@
name|ManagedResource
argument_list|(
name|description
operator|=
literal|"Managed DataFormat"
argument_list|)
DECL|class|ManagedDataFormat
specifier|public
class|class
name|ManagedDataFormat
implements|implements
name|ManagedInstance
implements|,
name|ManagedDataFormatMBean
block|{
DECL|field|camelContext
specifier|private
specifier|final
name|CamelContext
name|camelContext
decl_stmt|;
DECL|field|dataFormat
specifier|private
specifier|final
name|DataFormat
name|dataFormat
decl_stmt|;
DECL|method|ManagedDataFormat (CamelContext camelContext, DataFormat dataFormat)
specifier|public
name|ManagedDataFormat
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|,
name|DataFormat
name|dataFormat
parameter_list|)
block|{
name|this
operator|.
name|camelContext
operator|=
name|camelContext
expr_stmt|;
name|this
operator|.
name|dataFormat
operator|=
name|dataFormat
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
DECL|method|getDataFormat ()
specifier|public
name|DataFormat
name|getDataFormat
parameter_list|()
block|{
return|return
name|dataFormat
return|;
block|}
DECL|method|getContext ()
specifier|public
name|CamelContext
name|getContext
parameter_list|()
block|{
return|return
name|camelContext
return|;
block|}
annotation|@
name|Override
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
if|if
condition|(
name|dataFormat
operator|instanceof
name|DataFormatName
condition|)
block|{
return|return
operator|(
operator|(
name|DataFormatName
operator|)
name|dataFormat
operator|)
operator|.
name|getDataFormatName
argument_list|()
return|;
block|}
return|return
literal|null
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
name|camelContext
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
name|camelContext
operator|.
name|getManagementName
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
name|dataFormat
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
name|dataFormat
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
DECL|method|informationJson ()
specifier|public
name|String
name|informationJson
parameter_list|()
block|{
name|String
name|dataFormatName
init|=
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|dataFormatName
operator|!=
literal|null
condition|)
block|{
return|return
name|camelContext
operator|.
name|explainDataFormatJson
argument_list|(
name|dataFormatName
argument_list|,
name|dataFormat
argument_list|,
literal|true
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
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
name|String
name|dataFormatName
init|=
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
name|dataFormatName
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|TabularData
name|answer
init|=
operator|new
name|TabularDataSupport
argument_list|(
name|CamelOpenMBeanTypes
operator|.
name|explainDataFormatTabularType
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|json
init|=
name|camelContext
operator|.
name|explainDataFormatJson
argument_list|(
name|dataFormatName
argument_list|,
name|dataFormat
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
name|JSonSchemaHelper
operator|.
name|parseJsonSchema
argument_list|(
literal|"properties"
argument_list|,
name|json
argument_list|,
literal|true
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
name|name
init|=
name|row
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
name|String
name|kind
init|=
name|row
operator|.
name|get
argument_list|(
literal|"kind"
argument_list|)
decl_stmt|;
name|String
name|label
init|=
name|row
operator|.
name|get
argument_list|(
literal|"label"
argument_list|)
operator|!=
literal|null
condition|?
name|row
operator|.
name|get
argument_list|(
literal|"label"
argument_list|)
else|:
literal|""
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
name|deprecated
init|=
name|row
operator|.
name|get
argument_list|(
literal|"deprecated"
argument_list|)
operator|!=
literal|null
condition|?
name|row
operator|.
name|get
argument_list|(
literal|"deprecated"
argument_list|)
else|:
literal|""
decl_stmt|;
name|String
name|secret
init|=
name|row
operator|.
name|get
argument_list|(
literal|"secret"
argument_list|)
operator|!=
literal|null
condition|?
name|row
operator|.
name|get
argument_list|(
literal|"secret"
argument_list|)
else|:
literal|""
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
name|explainDataFormatsCompositeType
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
literal|"kind"
block|,
literal|"label"
block|,
literal|"type"
block|,
literal|"java type"
block|,
literal|"deprecated"
block|,
literal|"secret"
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
name|name
block|,
name|kind
block|,
name|label
block|,
name|type
block|,
name|javaType
block|,
name|deprecated
block|,
name|secret
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
name|RuntimeCamelException
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|getInstance ()
specifier|public
name|DataFormat
name|getInstance
parameter_list|()
block|{
return|return
name|dataFormat
return|;
block|}
block|}
end_class

end_unit

